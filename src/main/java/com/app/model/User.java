package com.app.model;

public class User {

    private String username;
    private String password;
    private String role;

    private String nom;
    private String prenom;
    private String entreprise;
    private String email;
    private String telephone;

    public User(String username, String password, String role,
                String nom, String prenom,
                String entreprise, String email, String telephone) {

        this.username = username;
        this.password = password;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
        this.entreprise = entreprise;
        this.email = email;
        this.telephone = telephone;
    }

    // ========= GETTERS =========
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEntreprise() { return entreprise; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }

    // ========= SETTERS =========
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}