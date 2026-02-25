package com.app.model;

public class Reservation {

    private int id;
    private int salleId;
    private String client;

    private String date;
    private int duree;
    private int nbParticipants;
    private String services;

    private String statut;   // EN_ATTENTE / VALIDEE / REFUSEE
    private double cout;

    public Reservation(int id, int salleId, String client,
                       String date, int duree,
                       int nbParticipants, String services,
                       String statut, double cout) {

        this.id = id;
        this.salleId = salleId;
        this.client = client;
        this.date = date;
        this.duree = duree;
        this.nbParticipants = nbParticipants;
        this.services = services;
        this.statut = statut;
        this.cout = cout;
    }

    public int getId() { return id; }
    public int getSalleId() { return salleId; }
    public String getClient() { return client; }
    public String getDate() { return date; }
    public int getDuree() { return duree; }
    public int getNbParticipants() { return nbParticipants; }
    public String getServices() { return services; }
    public String getStatut() { return statut; }
    public double getCout() { return cout; }

    public void setStatut(String statut) { this.statut = statut; }
}