package model;

import java.util.Date;

public class Adherent {

    private String numero;
    private String nom;
    private String prenom;
    private Date datenaissance;
    private boolean premium;
    private Date dateAjout;
    private int signalements;

    // Constructor
    public Adherent(String numero, String nom, String prenom, Date datenaissance, boolean premium) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.datenaissance = datenaissance;
        this.premium = premium;
        this.dateAjout = new Date(); // Date d'ajout par défaut
        this.signalements = 0;
    }
    
    // Constructor with dateAjout
    public Adherent(String numero, String nom, String prenom, Date datenaissance, boolean premium, Date dateAjout) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.datenaissance = datenaissance;
        this.premium = premium;
        this.dateAjout = dateAjout;
        this.signalements = 0;
    }
    
    // Constructor with all fields
    public Adherent(String numero, String nom, String prenom, Date datenaissance, boolean premium, Date dateAjout, int signalements) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.datenaissance = datenaissance;
        this.premium = premium;
        this.dateAjout = dateAjout;
        this.signalements = signalements;
    }

    // Getters
    public String getNumero() { return numero; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Date getDatenaissance() { return datenaissance; }
    public boolean isPremium() { return premium; }
    public Date getDateAjout() { return dateAjout; }
    public int getSignalements() { return signalements; }

    // Setters
    public void setNumero(String numero) { this.numero = numero; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setDatenaissance(Date datenaissance) { this.datenaissance = datenaissance; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public void setDateAjout(Date dateAjout) { this.dateAjout = dateAjout; }
    public void setSignalements(int signalements) { this.signalements = signalements; }
    
    public void incrementSignalements() { this.signalements++; }

    @Override
    public String toString() {
        return "Adherent{" +
               "numero='" + numero + '\'' +
               ", nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               ", datenaissance=" + datenaissance +
               ", premium=" + premium +
               '}';
    }
}
