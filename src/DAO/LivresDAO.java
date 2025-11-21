package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Livres;
import util.Connexion;

public class LivresDAO {
    // Ajouter un livre
    public void ajouterLivre(Livres livre) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "INSERT INTO livres (ISBN, titre, auteur, categorie, disponibilite) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);// protege de SQL Injection
            ps.setString(1, livre.getISBN());
            ps.setString(2, livre.getTitre());
            ps.setString(3, livre.getAuteur());
            ps.setString(4, livre.getCategorie());
            ps.setBoolean(5, livre.isDisponibilite());
            ps.executeUpdate();//pour exucuter la commande 
        } catch (Exception e) { e.printStackTrace(); }//ecrire le type derreur 
    }

    // Modifier un livre
    public void modifierLivre(Livres livre) {
        try (Connection conn = Connexion.getConnection()/* en peut la faire une seul fois dans main et jerrai la fermuture aussi ajouter conn comme parametre dans chaque methode */) {
            String sql = "UPDATE livres SET titre=?, auteur=?, categorie=?, disponibilite=? WHERE ISBN=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getCategorie());
            ps.setBoolean(4, livre.isDisponibilite());
            ps.setString(5, livre.getISBN());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Supprimer un livre
    public void supprimerLivre(String ISBN) {
        try (Connection conn = Connexion.getConnection()) {
            String sql = "DELETE FROM livres WHERE ISBN=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ISBN);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Rechercher par ISBN
    public Livres chercherParISBN(String ISBN) {
        Livres livre = null;
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM livres WHERE ISBN=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ISBN);
            ResultSet rs = ps.executeQuery();//exucuter et stockeé la resultat  /*executeUpdate()Sert à exécuter les requêtes SQL qui modifient la base de données.Pour les commandes INSERT, UPDATE, DELETE (ajouter, changer ou supprimer des données).Retourne un nombre entier : le nombre de lignes modifiées. */
            if (rs.next()) {
                livre = new Livres(
                    rs.getString("ISBN"),//recuperer qque chose du requete,
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getBoolean("disponibilite")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return livre;
    }

    // Rechercher par titre
    public List<Livres> chercherParTitre(String titre) {
        List<Livres> livresList = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM livres WHERE titre LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + titre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {// verifier que el est nest pas vide
                livresList.add(new Livres(
                    rs.getString("ISBN"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getBoolean("disponibilite")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return livresList;
    }

    // Rechercher par auteur
    public List<Livres> chercherParAuteur(String auteur) {
        List<Livres> livresList = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM livres WHERE auteur LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + auteur + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                livresList.add(new Livres(
                    rs.getString("ISBN"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getBoolean("disponibilite")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return livresList;
    }

    // Afficher tous les livres avec leur disponibilité
    public List<Livres> getAllLivres() {
        List<Livres> livresList = new ArrayList<>();
        try (Connection conn = Connexion.getConnection()) {
            String sql = "SELECT * FROM livres";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) { //rs.next() avancer le curseur vers la prochaine ligne du résultat.
                livresList.add(new Livres(
                    rs.getString("ISBN"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getBoolean("disponibilite")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return livresList;
    }
}
