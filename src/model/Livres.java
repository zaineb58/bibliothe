package model;

public class Livres {

    private String ISBN;
    private String titre;
    private String auteur;
    private String categorie;
    private boolean disponibilite;

    public Livres(String ISBN, String titre, String auteur, String categorie, boolean disponibilite) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.disponibilite = disponibilite;
    }

    // Getters
    public String getISBN() { return ISBN; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public String getCategorie() { return categorie; }
    public boolean isDisponibilite() { return disponibilite; }

    // Setters
    public void setISBN(String ISBN) { this.ISBN = ISBN; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setDisponibilite(boolean disponibilite) { this.disponibilite = disponibilite; }

    @Override
    public String toString() {
        return "Livres{" +
               "ISBN='" + ISBN + '\'' +
               ", titre='" + titre + '\'' +
               ", auteur='" + auteur + '\'' +
               ", categorie='" + categorie + '\'' +
               ", disponibilite=" + disponibilite +
               '}';
    }
}
