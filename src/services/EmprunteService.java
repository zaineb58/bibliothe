package services;

import DAO.EmpruntDAO;
import model.Emprunt;
import java.util.List;
import java.sql.Date;

public class EmprunteService {
    private EmpruntDAO dao = new EmpruntDAO();

    public void addEmprunt(Emprunt emprunt) {
        dao.enregistrerEmprunt(emprunt);
    }

    public void enregistrerRetour(String isbnLivre, String numeroAdherent, Date dateRetour) {
        dao.enregistrerRetour(isbnLivre, numeroAdherent, dateRetour);
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return dao.getEmpruntsEnCours();
    }
}
