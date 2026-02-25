package com.app.model;

public class Salle {

    private int id;
    private String nom;
    private int capacite;

    // ✅ خصائص جديدة مطلوبة
    private String type;
    private double prixHeure;
    private String equipements;
    private String localisation;

    // 🔹 constructor القديم (لا نحذفه)
    public Salle(int id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
    }

    // 🔹 constructor الجديد الكامل
    public Salle(int id, String nom, int capacite,
                 String type, double prixHeure,
                 String equipements, String localisation) {

        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.type = type;
        this.prixHeure = prixHeure;
        this.equipements = equipements;
        this.localisation = localisation;
    }

    // ===== GETTERS =====
    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getCapacite() { return capacite; }
    public String getType() { return type; }
    public double getPrixHeure() { return prixHeure; }
    public String getEquipements() { return equipements; }
    public String getLocalisation() { return localisation; }

    // ===== SETTERS =====
    public void setNom(String nom) { this.nom = nom; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public void setType(String type) { this.type = type; }
    public void setPrixHeure(double prixHeure) { this.prixHeure = prixHeure; }
    public void setEquipements(String equipements) { this.equipements = equipements; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
}