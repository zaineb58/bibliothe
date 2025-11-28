package util;

import java.sql.Connection;
import java.sql.Statement;

public class DeleteAllData {
    public static void main(String[] args) {
        try {
            Connection conn = Connexion.getConnection();
            Statement stmt = conn.createStatement();
            
            System.out.println("Suppression de toutes les données...");
            
            // Supprimer les emprunts en premier (clés étrangères)
            int emprunts = stmt.executeUpdate("DELETE FROM emprunt");
            System.out.println("✓ " + emprunts + " emprunt(s) supprimé(s)");
            
            // Supprimer les adhérents
            int adherents = stmt.executeUpdate("DELETE FROM adherent");
            System.out.println("✓ " + adherents + " adhérent(s) supprimé(s)");
            
            // Supprimer les livres
            int livres = stmt.executeUpdate("DELETE FROM livres");
            System.out.println("✓ " + livres + " livre(s) supprimé(s)");
            
            System.out.println("\n✅ Toutes les données ont été supprimées avec succès!");
            
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la suppression: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
