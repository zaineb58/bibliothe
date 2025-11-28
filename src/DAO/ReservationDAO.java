package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Reservation;
import util.Connexion;

/**
 * Data Access Object for managing book reservations
 * Implements FIFO queue system for fair book allocation
 */
public class ReservationDAO {

    /**
     * Create a new reservation
     */
    public void creerReservation(Reservation reservation) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "INSERT INTO reservation (isbnLivre, numeroAdherent, dateReservation, statut) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, reservation.getIsbnLivre());
            ps.setString(2, reservation.getNumeroAdherent());
            ps.setDate(3, new java.sql.Date(reservation.getDateReservation().getTime()));
            ps.setString(4, reservation.getStatut());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating reservation: " + e.getMessage(), e);
        }
    }

    /**
     * Get the next pending reservation for a book (FIFO - First In First Out)
     * Returns the oldest reservation that is still waiting
     */
    public Reservation getNextPendingReservation(String isbnLivre) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM reservation WHERE isbnLivre = ? AND statut = 'en_attente' " +
                        "ORDER BY dateReservation ASC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbnLivre);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapReservation(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting next reservation: " + e.getMessage(), e);
        }
    }

    /**
     * Get all pending reservations for a specific book
     */
    public List<Reservation> getReservationsEnAttenteParLivre(String isbnLivre) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM reservation WHERE isbnLivre = ? AND statut = 'en_attente' " +
                        "ORDER BY dateReservation ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbnLivre);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting reservations: " + e.getMessage(), e);
        }
        return reservations;
    }

    /**
     * Get all reservations for a specific member
     */
    public List<Reservation> getReservationsParAdherent(String numeroAdherent) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM reservation WHERE numeroAdherent = ? " +
                        "ORDER BY dateReservation DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numeroAdherent);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting member reservations: " + e.getMessage(), e);
        }
        return reservations;
    }

    /**
     * Get active (pending) reservations for a member
     */
    public List<Reservation> getReservationsActivesParAdherent(String numeroAdherent) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM reservation WHERE numeroAdherent = ? AND statut = 'en_attente' " +
                        "ORDER BY dateReservation DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numeroAdherent);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting active reservations: " + e.getMessage(), e);
        }
        return reservations;
    }

    /**
     * Check if a member already has an active reservation for a book
     */
    public boolean hasActiveReservation(String isbnLivre, String numeroAdherent) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT COUNT(*) FROM reservation WHERE isbnLivre = ? AND numeroAdherent = ? " +
                        "AND statut = 'en_attente'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbnLivre);
            ps.setString(2, numeroAdherent);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking reservation: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Mark a reservation as assigned (attribuee)
     */
    public void attribuerReservation(int reservationId, Date dateAttribution, Date dateExpiration) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "UPDATE reservation SET statut = 'attribuee', dateAttribution = ?, " +
                        "dateExpiration = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dateAttribution.getTime()));
            ps.setDate(2, new java.sql.Date(dateExpiration.getTime()));
            ps.setInt(3, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning reservation: " + e.getMessage(), e);
        }
    }

    /**
     * Cancel a reservation
     */
    public void annulerReservation(int reservationId) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "UPDATE reservation SET statut = 'annulee' WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error canceling reservation: " + e.getMessage(), e);
        }
    }

    /**
     * Mark expired reservations as expiree
     * Called when assigned book is not borrowed within expiration date
     */
    public void marquerReservationsExpirees() {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "UPDATE reservation SET statut = 'expiree' " +
                        "WHERE statut = 'attribuee' AND dateExpiration < CURDATE()";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error marking expired reservations: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a reservation
     */
    public void supprimerReservation(int reservationId) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "DELETE FROM reservation WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting reservation: " + e.getMessage(), e);
        }
    }

    /**
     * Count pending reservations for a book
     */
    public int compterReservationsEnAttente(String isbnLivre) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT COUNT(*) FROM reservation WHERE isbnLivre = ? AND statut = 'en_attente'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, isbnLivre);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting reservations: " + e.getMessage(), e);
        }
        return 0;
    }

    /**
     * Get reservation by ID
     */
    public Reservation getReservationById(int id) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM reservation WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapReservation(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting reservation: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Get all reservations
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM reservation ORDER BY dateReservation DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all reservations: " + e.getMessage(), e);
        }
        return reservations;
    }

    /**
     * Map ResultSet to Reservation object
     */
    private Reservation mapReservation(ResultSet rs) throws SQLException {
        return new Reservation(
            rs.getInt("id"),
            rs.getString("isbnLivre"),
            rs.getString("numeroAdherent"),
            rs.getDate("dateReservation"),
            rs.getString("statut"),
            rs.getDate("dateAttribution"),
            rs.getDate("dateExpiration")
        );
    }
}
