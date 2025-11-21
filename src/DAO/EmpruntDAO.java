package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Emprunt;
import util.Connexion;

public class EmpruntDAO {

    // Enregistrer un nouvel emprunt
    public void enregistrerEmprunt(Emprunt emprunt) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "INSERT INTO emprunt (isbnLivre, numeroAdherent, dateEmprunt, dateRetour) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, emprunt.getIsbnLivre());
            ps.setString(2, emprunt.getNumeroAdherent());
            ps.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));
            if (emprunt.getDateRetour() == null)
                ps.setNull(4, Types.DATE);
            else
                ps.setDate(4, new java.sql.Date(emprunt.getDateRetour().getTime()));
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Enregistrer le retour d’un livre (mise à jour de la date de retour)
    public void enregistrerRetour(String isbnLivre, String numeroAdherent, Date dateRetour) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "UPDATE emprunt SET dateRetour=? WHERE isbnLivre=? AND numeroAdherent=? AND dateRetour IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dateRetour.getTime()));
            ps.setString(2, isbnLivre);
            ps.setString(3, numeroAdherent);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Afficher la liste des emprunts en cours (dateRetour IS NULL)
    public List<Emprunt> getEmpruntsEnCours() {
        List<Emprunt> list = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM emprunt WHERE dateRetour IS NULL";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapEmprunt(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Méthode utilitaire pour mapper un ResultSet en objet Emprunt
    private Emprunt mapEmprunt(ResultSet rs) throws SQLException {
        return new Emprunt(
            rs.getString("isbnLivre"),
            rs.getString("numeroAdherent"),
            rs.getDate("dateEmprunt"),
            rs.getDate("dateRetour")
        );
    }
}
