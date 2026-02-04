package com.app.model;

public class Reservation {

	private int id;
    private int salleId;
    private String nomClient;
    private String date;

    public Reservation(int id, int salleId, String nomClient, String date) {
        this.id = id;
        this.salleId = salleId;
        this.nomClient = nomClient;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getSalleId() {
        return salleId;
    }

    public void setSalleId(int salleId) {
        this.salleId = salleId;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
