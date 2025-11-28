package services;

import DAO.LivresDAO;
import javafx.scene.Node;
import model.Livres;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class LivreServices {
    private final LivresDAO dao = new LivresDAO();

    public void addLivre(Livres livre) {
        dao.ajouterLivre(livre);
    }

    public void updateLivre(Livres livre) {
        dao.modifierLivre(livre);
    }

    public void modifierLivre(Livres livre) {
        dao.modifierLivre(livre);
    }

    public void deleteLivre(String ISBN) {
        dao.supprimerLivre(ISBN);
    }

    public Livres findByISBN(String ISBN) {
        return dao.chercherParISBN(ISBN);
    }

    public List<Livres> searchByTitre(String titre) {
        return dao.chercherParTitre(titre);
    }

    public List<Livres> searchByAuteur(String auteur) {
        return dao.chercherParAuteur(auteur);
    }

    public List<Livres> getAll() {
        return dao.getAllLivres();
    }

    public void deleteAll() {
        dao.supprimerTousLesLivres();
    }

    public List<Livres> rechercher(String isbn) {
        Livres livre = dao.chercherParISBN(isbn);
        if (livre != null) {
            return List.of(livre);
        }
        return List.of();
    }
}
