package model;

import java.sql.Date;

/**
 * Represents a book reservation in the library system
 * Manages queue-based reservation system for unavailable books
 */
public class Reservation {
    private int id;
    private String isbnLivre;
    private String numeroAdherent;
    private Date dateReservation;
    private String statut; // en_attente, attribuee, annulee, expiree
    private Date dateAttribution;
    private Date dateExpiration;

    // Constructor for new reservations (without ID)
    public Reservation(String isbnLivre, String numeroAdherent, Date dateReservation, String statut) {
        this.isbnLivre = isbnLivre;
        this.numeroAdherent = numeroAdherent;
        this.dateReservation = dateReservation;
        this.statut = statut;
    }

    // Full constructor (with all fields including ID)
    public Reservation(int id, String isbnLivre, String numeroAdherent, Date dateReservation, 
                      String statut, Date dateAttribution, Date dateExpiration) {
        this.id = id;
        this.isbnLivre = isbnLivre;
        this.numeroAdherent = numeroAdherent;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.dateAttribution = dateAttribution;
        this.dateExpiration = dateExpiration;
    }

    // Getters
    public int getId() { return id; }
    public String getIsbnLivre() { return isbnLivre; }
    public String getNumeroAdherent() { return numeroAdherent; }
    public Date getDateReservation() { return dateReservation; }
    public String getStatut() { return statut; }
    public Date getDateAttribution() { return dateAttribution; }
    public Date getDateExpiration() { return dateExpiration; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIsbnLivre(String isbnLivre) { this.isbnLivre = isbnLivre; }
    public void setNumeroAdherent(String numeroAdherent) { this.numeroAdherent = numeroAdherent; }
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setDateAttribution(Date dateAttribution) { this.dateAttribution = dateAttribution; }
    public void setDateExpiration(Date dateExpiration) { this.dateExpiration = dateExpiration; }

    // Helper methods
    public boolean isEnAttente() {
        return "en_attente".equals(statut);
    }

    public boolean isAttribuee() {
        return "attribuee".equals(statut);
    }

    public boolean isAnnulee() {
        return "annulee".equals(statut);
    }

    public boolean isExpiree() {
        return "expiree".equals(statut);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", isbnLivre='" + isbnLivre + '\'' +
                ", numeroAdherent='" + numeroAdherent + '\'' +
                ", dateReservation=" + dateReservation +
                ", statut='" + statut + '\'' +
                ", dateAttribution=" + dateAttribution +
                ", dateExpiration=" + dateExpiration +
                '}';
    }
}
