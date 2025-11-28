package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Adherent;
import util.Connexion;

public class AdherentDAO {

    // Ajouter un nouvel adhérent
    public void ajouterAdherent(Adherent adherent) {
        try (Connection conn = Connexion.getConnection()) {
            // Try with signalements column first
            try {
                String sql = "INSERT INTO adherent (numero, nom, prenom, datenaissance, Premium, signalements) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, adherent.getNumero());
                ps.setString(2, adherent.getNom());
                ps.setString(3, adherent.getPrenom());
                ps.setDate(4, new java.sql.Date(adherent.getDatenaissance().getTime()));
                ps.setBoolean(5, adherent.isPremium());
                ps.setInt(6, adherent.getSignalements());
                ps.executeUpdate();
            } catch (SQLException e) {
                // If signalements column doesn't exist, try without it
                if (e.getMessage().contains("signalements")) {
                    String sql = "INSERT INTO adherent (numero, nom, prenom, datenaissance, Premium) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, adherent.getNumero());
                    ps.setString(2, adherent.getNom());
                    ps.setString(3, adherent.getPrenom());
                    ps.setDate(4, new java.sql.Date(adherent.getDatenaissance().getTime()));
                    ps.setBoolean(5, adherent.isPremium());
                    ps.executeUpdate();
                } else {
                    throw e;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding member: " + e.getMessage(), e);
        }
    }

    // Modifier les infos d'un adhérent
    public void modifierAdherent(Adherent adherent) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "UPDATE adherent SET nom=?, prenom=?, datenaissance=?, Premium=?, signalements=? WHERE numero=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, adherent.getNom());
            ps.setString(2, adherent.getPrenom());
            ps.setDate(3, new java.sql.Date(adherent.getDatenaissance().getTime()));
            ps.setBoolean(4, adherent.isPremium());
            ps.setInt(5, adherent.getSignalements());
            ps.setString(6, adherent.getNumero());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating member", e);
        }
    }

    // Supprimer un adhérent
    public void supprimerAdherent(String numero) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "DELETE FROM adherent WHERE numero=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numero);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting member", e);
        }
    }

    // Supprimer tous les adhérents
    public void supprimerTousLesAdherents() {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "DELETE FROM adherent";
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all members", e);
        }
    }

    // Rechercher adhérent par nom
    public List<Adherent> chercherParNom(String nom) {
        List<Adherent> list = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM adherent WHERE nom LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAdherent(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Rechercher adhérent par prénom
    public List<Adherent> chercherParPrenom(String prenom) {
        List<Adherent> list = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM adherent WHERE prenom LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + prenom + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAdherent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching members by first name", e);
        }
        return list;
    }

    // Rechercher par numéro
    public List<Adherent> chercherParNumero(String numero) {
        List<Adherent> list = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM adherent WHERE numero LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + numero + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAdherent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching members by number", e);
        }
        return list;
    }

    // Afficher la liste des adhérents
    public List<Adherent> getAllAdherents() {
        List<Adherent> list = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM adherent";
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapAdherent(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all members", e);
        }
        return list;
    }

    // Rechercher par téléphone
    public Adherent chercherParTelephone(String telephone) {
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM adherent WHERE telephone = ?")) {
            ps.setString(1, telephone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAdherent(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching member by phone", e);
        }
        return null;
    }

    // Utilitaire
    private Adherent mapAdherent(ResultSet rs) throws SQLException {
        try {
            int signalements = 0;
            try {
                signalements = rs.getInt("signalements");
            } catch (SQLException e) {
                // Column might not exist yet
            }
            
            return new Adherent(
                rs.getString("numero"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getDate("datenaissance"),
                rs.getBoolean("Premium"),
                rs.getDate("dateAjout"),
                signalements
            );
        } catch (SQLException e) {
            // Si la colonne dateAjout n'existe pas, retourner avec valeurs par défaut
            return new Adherent(
                rs.getString("numero"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getDate("datenaissance"),
                rs.getBoolean("Premium")
            );
        }
    }
    
    // Increment signalements for a member
    public void incrementerSignalements(String numero) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "UPDATE adherent SET signalements = signalements + 1 WHERE numero = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numero);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error incrementing warnings", e);
        }
    }
    
    // Get member by numero
    public Adherent getAdherentByNumero(String numero) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM adherent WHERE numero = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapAdherent(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching member", e);
        }
        return null;
    }
}
