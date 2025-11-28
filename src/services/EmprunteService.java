package services;

import DAO.EmpruntDAO;
import DAO.AdherentDAO;
import model.Emprunt;
import model.Adherent;
import java.util.List;
import java.sql.Date;

public class EmprunteService {
    private final EmpruntDAO dao = new EmpruntDAO();
    private final AdherentDAO adherentDAO = new AdherentDAO();
    private final ReservationService reservationService = new ReservationService();

    public void addEmprunt(Emprunt emprunt) {
        dao.enregistrerEmprunt(emprunt);
    }

    public String enregistrerRetour(String isbnLivre, String numeroAdherent, Date dateRetour) {
        boolean isLate = dao.enregistrerRetour(isbnLivre, numeroAdherent, dateRetour);
        
        String returnMessage = "Book returned successfully.";
        
        if (isLate) {
            // Increment signalements
            adherentDAO.incrementerSignalements(numeroAdherent);
            
            // Check if member should be deleted
            Adherent adherent = adherentDAO.getAdherentByNumero(numeroAdherent);
            if (adherent != null && adherent.getSignalements() >= 3) {
                adherentDAO.supprimerAdherent(numeroAdherent);
                returnMessage = "DELETED: Member " + adherent.getNom() + " " + adherent.getPrenom() + " has been automatically removed after 3 warnings for late returns.";
            } else if (adherent != null) {
                returnMessage = "WARNING: Late return detected! Member now has " + adherent.getSignalements() + " warning(s). After 3 warnings, the member will be automatically removed.";
            }
        }
        
        // Check for pending reservations and assign to next in queue
        String reservationMessage = reservationService.traiterProchaineReservation(isbnLivre);
        
        if (reservationMessage != null) {
            returnMessage += "\n\n" + reservationMessage;
        }
        
        return returnMessage;
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return dao.getEmpruntsEnCours();
    }

    public void supprimerTous() {
        dao.supprimerTousLesEmprunts();
    }
}
