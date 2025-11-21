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
            String sql = "INSERT INTO adherent (numero, nom, prenom, datenaissance, Premium) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, adherent.getNumero());
            ps.setString(2, adherent.getNom());
            ps.setString(3, adherent.getPrenom());
            ps.setDate(4, new java.sql.Date(adherent.getDatenaissance().getTime()));
            ps.setBoolean(5, adherent.isPremium());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Modifier les infos d'un adhérent
    public void modifierAdherent(Adherent adherent) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "UPDATE adherent SET nom=?, prenom=?, datenaissance=?, Premium=? WHERE numero=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, adherent.getNom());
            ps.setString(2, adherent.getPrenom());
            ps.setDate(3, new java.sql.Date(adherent.getDatenaissance().getTime()));
            ps.setBoolean(4, adherent.isPremium());
            ps.setString(5, adherent.getNumero());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Supprimer un adhérent
    public void supprimerAdherent(String numero) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "DELETE FROM adherent WHERE numero=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numero);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
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
        } catch (Exception e) { e.printStackTrace(); }
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
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Afficher la liste des adhérents
    public List<Adherent> getAllAdherents() {
        List<Adherent> list = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM adherent";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapAdherent(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Utilitaire
    private Adherent mapAdherent(ResultSet rs) throws SQLException {
        return new Adherent(
            rs.getString("numero"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getDate("datenaissance"),
            rs.getBoolean("Premium")
        );
    }
}
