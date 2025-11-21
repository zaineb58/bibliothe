import model.Adherent;
import model.Livres;
import model.Emprunt;
import services.AdherentService;
import services.LivreServices;
import services.EmprunteService;

import java.util.Date;

public class Test {

    public static void main(String[] args) {

        System.out.println("===== TEST BIBLIOTHÈQUE =====");

        // ----- Test Adherent -----
        AdherentService adherentService = new AdherentService();

        Adherent adh = new Adherent("0011", "Wassim", "Bahloul", new Date(), true);
        System.out.println("\n-- Ajout Adherent --");
        adherentService.AjouterAdherent(adh);
        System.out.println("Adherent ajouté");

        System.out.println("\n-- Liste Adherents --");
        for (Adherent a : adherentService.getAll()) {
            System.out.println(a);
        }


        // ----- Test Livre -----
        LivreServices livreService = new LivreServices();;

        Livres livre = new Livres("ISBN0001", "Clean Code", "Robert Martin", "Informatique", true);
        System.out.println("\n-- Ajout Livre --");
        livreService.addLivre(livre);
        System.out.println("Livre ajouté");

        System.out.println("\n-- Liste Livres --");
        for (Livres l : livreService.getAll()) {
            System.out.println(l);
        }


        // ----- Test Emprunt -----
        EmprunteService empruntService = new EmprunteService();

        Emprunt emp = new Emprunt("ISBN0001", "0011", new Date(), null);
        System.out.println("\n-- Ajout Emprunt --");
        empruntService.addEmprunt(emp);
        System.out.println("Emprunt ajouté");

        System.out.println("\n-- Liste Emprunts --");
        for (Emprunt e : empruntService.getEmpruntsEnCours()) {
            System.out.println(e);
        }

        System.out.println("\n===== FIN TEST =====");
    }
}
