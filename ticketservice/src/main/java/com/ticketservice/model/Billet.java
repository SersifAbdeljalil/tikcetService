package com.ticketservice.model;
import javax.persistence.*;
@Entity
@Table(name = "billets")
public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeBillet; // Ex: "VIP", "Standard", "Étudiant"
    private double prix;
    private int quantiteDisponible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Evenement evenement;

    // Constructeurs
    public Billet() {}

    public Billet(String typeBillet, double prix, int quantiteDisponible, Evenement evenement) {
        this.typeBillet = typeBillet;
        this.prix = prix;
        this.quantiteDisponible = quantiteDisponible;
        this.evenement = evenement;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
         this.id = id;
    }
    public String getTypeBillet() {
        return typeBillet;
    }
    public void setTypeBillet(String typeBillet) {
        this.typeBillet = typeBillet;
    }
    public double getPrix() {
        return prix;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
    public int getQuantiteDisponible() {
        return quantiteDisponible;
    }
    public void setQuantiteDisponible(int quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }
    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }
    public boolean verifierDisponibilite(int quantiteDemandee) {
        return this.quantiteDisponible >= quantiteDemandee;
    }
    public void decrementerStock(int quantite) {
        if(quantite <= this.quantiteDisponible) {
            this.quantiteDisponible -= quantite;
        }
    }
}