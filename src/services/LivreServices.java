package services;

import DAO.LivresDAO;
import model.Livres;
import java.util.List;

public class LivreServices {
    private LivresDAO dao = new LivresDAO();

    public void addLivre(Livres livre) {
        dao.ajouterLivre(livre);
    }

    public void updateLivre(Livres livre) {
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
}
