package tn.esprit.spring.kaddem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Etudiant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEtudiant;

    private String nom;

    private String prenom;

    @Enumerated(EnumType.STRING)
    private Option option;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Contrat> contrats = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    private Departement departement;

    @ManyToMany(mappedBy = "etudiants")
    @JsonIgnore
    private Set<Equipe> equipes = new HashSet<>();

    // Default constructor
    public Etudiant() {
        this.contrats = new HashSet<>();
        this.equipes = new HashSet<>();
    }

    // Constructors with parameters
    public Etudiant(String nom, String prenom) {
        this();
        this.nom = nom;
        this.prenom = prenom;
    }

    public Etudiant(String nom, String prenom, Option option) {
        this(nom, prenom);
        this.option = option;
    }

    public Etudiant(Integer idEtudiant, String nom, String prenom, Option option) {
        this(nom, prenom, option);
        this.idEtudiant = idEtudiant;
    }

    // Getters and Setters
    public Integer getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(Integer idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Set<Contrat> getContrats() {
        return contrats;
    }

    public void setContrats(Set<Contrat> contrats) {
        this.contrats = contrats != null ? contrats : new HashSet<>();
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Set<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(Set<Equipe> equipes) {
        this.equipes = equipes != null ? equipes : new HashSet<>();
    }

    // Override equals and hashCode for proper comparison in tests and collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Etudiant)) return false;
        Etudiant etudiant = (Etudiant) o;
        return Objects.equals(idEtudiant, etudiant.idEtudiant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtudiant);
    }

    // Override toString for easier debugging
    @Override
    public String toString() {
        return "Etudiant{" +
                "idEtudiant=" + idEtudiant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", option=" + option +
                ", departement=" + (departement != null ? departement.getIdDepart() : null) +
                '}';
    }
}
