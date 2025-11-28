package util;

import services.LivreServices;
import services.EmprunteService;

public class DeleteAllBooks {
    public static void main(String[] args) {
        EmprunteService empruntService = new EmprunteService();
        LivreServices livreService = new LivreServices();
        
        System.out.println("=== SUPPRESSION DE TOUS LES LIVRES ===");
        
        try {
            // D'abord supprimer tous les emprunts (pour éviter les problèmes de clé étrangère)
            System.out.println("Suppression des emprunts...");
            empruntService.supprimerTous();
            
            // Ensuite supprimer tous les livres
            System.out.println("Suppression des livres...");
            livreService.deleteAll();
            
            System.out.println("\n✅ TOUS LES LIVRES ET EMPRUNTS ONT ÉTÉ SUPPRIMÉS !");
            System.out.println("Vous pouvez maintenant ajouter de nouveaux livres via l'application.");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
