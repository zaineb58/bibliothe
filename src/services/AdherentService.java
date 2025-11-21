package services;

import DAO.AdherentDAO;
import model.Adherent;
import java.util.List;

public class AdherentService {
    private AdherentDAO dao = new AdherentDAO();

    public void AjouterAdherent(Adherent a) {
        dao.ajouterAdherent(a);
    }

    public void modifierAdherent(Adherent a) {
        dao.modifierAdherent(a);
    }

    public void supprimerAdherent(String numero) {
        dao.supprimerAdherent(numero);
    }

    public List<Adherent> getAll() {
        return dao.getAllAdherents();
    }

    public List<Adherent> chercherParNom(String nom) {
        return dao.chercherParNom(nom);
    }

    public List<Adherent> chercherParPrenom(String prenom) {
        return dao.chercherParPrenom(prenom);
    }

    public List<Adherent> chercherParNumero(String numero) {
        return dao.chercherParNumero(numero);
    }
}

