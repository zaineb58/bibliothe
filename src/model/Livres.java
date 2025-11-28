package model;

public class Livres {

    private String ISBN;
    private String titre;
    private String auteur;
    private String categorie;
    private boolean disponibilite;
    private int nombreCopies;
    private int copiesDisponibles;

    public Livres(String ISBN, String titre, String auteur, String categorie, boolean disponibilite) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.disponibilite = disponibilite;
        this.nombreCopies = 1;
        this.copiesDisponibles = 1;
    }

    public Livres(String ISBN, String titre, String auteur, String categorie, boolean disponibilite, int nombreCopies, int copiesDisponibles) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.disponibilite = disponibilite;
        this.nombreCopies = nombreCopies;
        this.copiesDisponibles = copiesDisponibles;
    }

    // Getters
    public String getISBN() { return ISBN; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public String getCategorie() { return categorie; }
    public boolean isDisponibilite() { return disponibilite; }
    public int getNombreCopies() { return nombreCopies; }
    public int getCopiesDisponibles() { return copiesDisponibles; }

    // Setters
    public void setISBN(String ISBN) { this.ISBN = ISBN; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setDisponibilite(boolean disponibilite) { this.disponibilite = disponibilite; }
    public void setNombreCopies(int nombreCopies) { this.nombreCopies = nombreCopies; }
    public void setCopiesDisponibles(int copiesDisponibles) { this.copiesDisponibles = copiesDisponibles; }

    @Override
    public String toString() {
        return "Livres{" +
               "ISBN='" + ISBN + '\'' +
               ", titre='" + titre + '\'' +
               ", auteur='" + auteur + '\'' +
               ", categorie='" + categorie + '\'' +
               ", disponibilite=" + disponibilite +
               ", nombreCopies=" + nombreCopies +
               ", copiesDisponibles=" + copiesDisponibles +
               '}';
    }
}
