package model;

import java.util.Date;

public class Emprunt {

    private String isbnLivre;
    private String numeroAdherent;
    private Date dateEmprunt;
    private Date dateRetour;

    // Constructor
    public Emprunt(String isbnLivre, String numeroAdherent, Date dateEmprunt, Date dateRetour) {
        this.isbnLivre = isbnLivre;
        this.numeroAdherent = numeroAdherent;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    // Getters
    public String getIsbnLivre() { return isbnLivre; }
    public String getNumeroAdherent() { return numeroAdherent; }
    public Date getDateEmprunt() { return dateEmprunt; }
    public Date getDateRetour() { return dateRetour; }

    // Setters
    public void setIsbnLivre(String isbnLivre) { this.isbnLivre = isbnLivre; }
    public void setNumeroAdherent(String numeroAdherent) { this.numeroAdherent = numeroAdherent; }
    public void setDateEmprunt(Date dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    public void setDateRetour(Date dateRetour) { this.dateRetour = dateRetour; }

    @Override
    public String toString() {
        return "Emprunt{" +
                "isbnLivre='" + isbnLivre + '\'' +
                ", numeroAdherent='" + numeroAdherent + '\'' +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetour=" + dateRetour +
                '}';
    }
}
