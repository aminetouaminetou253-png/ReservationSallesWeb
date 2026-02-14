package com.app.model;

public class Reservation {

    private int id;
    private int salleId;
    private String nomClient;
    private String date;
    private int duree;
    private String statut;
    private double coutTotal;

    public Reservation(int id, int salleId, String nomClient,
                       String date, int duree,
                       String statut, double coutTotal) {

        this.id = id;
        this.salleId = salleId;
        this.nomClient = nomClient;
        this.date = date;
        this.duree = duree;
        this.statut = statut;
        this.coutTotal = coutTotal;
    }

    public int getId() { return id; }
    public int getSalleId() { return salleId; }
    public String getNomClient() { return nomClient; }
    public String getDate() { return date; }
    public int getDuree() { return duree; }
    public String getStatut() { return statut; }
    public double getCoutTotal() { return coutTotal; }

    public void setStatut(String statut) { this.statut = statut; }
}
