package model;

import java.util.Date;

public class Adherent {

    private String numero;
    private String nom;
    private String prenom;
    private Date datenaissance;
    private boolean premium;

    // Constructor
    public Adherent(String numero, String nom, String prenom, Date datenaissance, boolean premium) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.datenaissance = datenaissance;
        this.premium = premium;
    }

    // Getters
    public String getNumero() { return numero; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Date getDatenaissance() { return datenaissance; }
    public boolean isPremium() { return premium; }

    // Setters
    public void setNumero(String numero) { this.numero = numero; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setDatenaissance(Date datenaissance) { this.datenaissance = datenaissance; }
    public void setPremium(boolean premium) { this.premium = premium; }

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
