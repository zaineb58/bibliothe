package services;

import DAO.ReservationDAO;
import DAO.AdherentDAO;
import model.Reservation;
import model.Adherent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for managing book reservations
 * Handles business logic for reservation queue system
 */
public class ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final AdherentDAO adherentDAO = new AdherentDAO();
    
    // Number of days a member has to borrow after reservation is assigned
    private static final int JOURS_EXPIRATION = 3;

    /**
     * Create a new reservation for a book
     * Returns success message or error message
     */
    @SuppressWarnings("unused")
    public String creerReservation(String isbnLivre, String numeroAdherent) {
        // Check if member exists
        Adherent adherent = adherentDAO.getAdherentByNumero(numeroAdherent);
        if (adherent == null) {
            return "ERROR: Member not found.";
        }

        // Check if member already has an active reservation for this book
        if (reservationDAO.hasActiveReservation(isbnLivre, numeroAdherent)) {
            return "ERROR: You already have an active reservation for this book.";
        }

        // Check member's borrowing limit (shouldn't reserve if at limit)
        int maxEmprunts = adherent.isPremium() ? 5 : 3;
        // This check should be done in the view, but double-check here
        
        try {
            Reservation reservation = new Reservation(
                isbnLivre,
                numeroAdherent,
                Date.valueOf(LocalDate.now()),
                "en_attente"
            );
            reservationDAO.creerReservation(reservation);
            
            // Get queue position
            int position = getPositionInQueue(isbnLivre, numeroAdherent);
            
            return "SUCCESS: Reservation created! You are #" + position + " in the queue.";
        } catch (Exception e) {
            return "ERROR: Failed to create reservation - " + e.getMessage();
        }
    }

    /**
     * Cancel a reservation
     */
    public String annulerReservation(int reservationId, String numeroAdherent) {
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        
        if (reservation == null) {
            return "ERROR: Reservation not found.";
        }

        // Verify the reservation belongs to this member
        if (!reservation.getNumeroAdherent().equals(numeroAdherent)) {
            return "ERROR: You can only cancel your own reservations.";
        }

        // Can only cancel if status is en_attente
        if (!reservation.isEnAttente()) {
            return "ERROR: Can only cancel pending reservations.";
        }

        try {
            reservationDAO.annulerReservation(reservationId);
            return "SUCCESS: Reservation canceled successfully.";
        } catch (Exception e) {
            return "ERROR: Failed to cancel reservation - " + e.getMessage();
        }
    }

    /**
     * Process next reservation when a book becomes available
     * Called when a book is returned
     * Returns message about what happened
     */
    public String traiterProchaineReservation(String isbnLivre) {
        // Mark any expired reservations first
        reservationDAO.marquerReservationsExpirees();
        
        // Get next pending reservation (FIFO)
        Reservation nextReservation = reservationDAO.getNextPendingReservation(isbnLivre);
        
        if (nextReservation == null) {
            return null; // No reservations waiting
        }

        // Check if member still exists and is eligible
        Adherent adherent = adherentDAO.getAdherentByNumero(nextReservation.getNumeroAdherent());
        if (adherent == null) {
            // Member deleted, cancel reservation and try next
            reservationDAO.annulerReservation(nextReservation.getId());
            return traiterProchaineReservation(isbnLivre); // Recursive call for next
        }

        // Check if member has warnings (3 warnings = deleted)
        if (adherent.getSignalements() >= 3) {
            reservationDAO.annulerReservation(nextReservation.getId());
            return traiterProchaineReservation(isbnLivre);
        }

        // Assign the reservation
        Date dateAttribution = Date.valueOf(LocalDate.now());
        Date dateExpiration = Date.valueOf(LocalDate.now().plusDays(JOURS_EXPIRATION));
        
        try {
            reservationDAO.attribuerReservation(nextReservation.getId(), dateAttribution, dateExpiration);
            
            return "RESERVATION ASSIGNED: Book reserved for member " + adherent.getNom() + " " + 
                   adherent.getPrenom() + " (№" + adherent.getNumero() + "). " +
                   "They have until " + dateExpiration + " to borrow it.";
        } catch (Exception e) {
            return "ERROR: Failed to assign reservation - " + e.getMessage();
        }
    }

    /**
     * Get member's position in queue for a book
     */
    public int getPositionInQueue(String isbnLivre, String numeroAdherent) {
        List<Reservation> reservations = reservationDAO.getReservationsEnAttenteParLivre(isbnLivre);
        
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getNumeroAdherent().equals(numeroAdherent)) {
                return i + 1; // Position starts at 1
            }
        }
        return -1; // Not found
    }

    /**
     * Get all active reservations for a member
     */
    public List<Reservation> getReservationsActives(String numeroAdherent) {
        return reservationDAO.getReservationsActivesParAdherent(numeroAdherent);
    }

    /**
     * Get all reservations for a member
     */
    public List<Reservation> getAllReservations(String numeroAdherent) {
        return reservationDAO.getReservationsParAdherent(numeroAdherent);
    }

    /**
     * Count pending reservations for a book
     */
    public int compterReservationsEnAttente(String isbnLivre) {
        return reservationDAO.compterReservationsEnAttente(isbnLivre);
    }

    /**
     * Check if a book has a reservation assigned to a specific member
     */
    public Reservation getReservationAttribuee(String isbnLivre, String numeroAdherent) {
        List<Reservation> reservations = reservationDAO.getReservationsParAdherent(numeroAdherent);
        
        for (Reservation r : reservations) {
            if (r.getIsbnLivre().equals(isbnLivre) && r.isAttribuee()) {
                // Check if not expired
                if (r.getDateExpiration() != null && 
                    r.getDateExpiration().toLocalDate().isBefore(LocalDate.now())) {
                    // Expired, mark it
                    reservationDAO.marquerReservationsExpirees();
                    return null;
                }
                return r;
            }
        }
        return null;
    }

    /**
     * Mark reservation as used (when member borrows the reserved book)
     */
    public void marquerReservationUtilisee(int reservationId) {
        // When a member borrows a reserved book, we can either:
        // 1. Delete the reservation
        // 2. Keep it with status "utilisee" for history
        // For now, we'll delete it since the emprunt record exists
        reservationDAO.supprimerReservation(reservationId);
    }

    /**
     * Get all reservations for a book (for admin view)
     */
    public List<Reservation> getReservationsParLivre(String isbnLivre) {
        return reservationDAO.getReservationsEnAttenteParLivre(isbnLivre);
    }

    /**
     * Clean up expired reservations
     */
    public void nettoyerReservationsExpirees() {
        reservationDAO.marquerReservationsExpirees();
    }
}
