package tn.esprit.spring.kaddem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Equipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEquipe;

    private String nomEquipe;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Etudiant> etudiants = new HashSet<>(); // Initialize to avoid NullPointerException

    @OneToOne
    private DetailEquipe detailEquipe;

    // Default constructor
    public Equipe() {
        // Initialize the etudiants set here if preferred
        this.etudiants = new HashSet<>(); // Ensure it's initialized
    }

    public Equipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
        this.etudiants = new HashSet<>(); // Ensure it's initialized
    }

    public Equipe(String nomEquipe, Niveau niveau) {
        this.nomEquipe = nomEquipe;
        this.niveau = niveau;
        this.etudiants = new HashSet<>(); // Ensure it's initialized
    }

    public Equipe(Integer idEquipe, String nomEquipe, Niveau niveau) {
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
        this.niveau = niveau;
        this.etudiants = new HashSet<>(); // Ensure it's initialized
    }

    public Equipe(String nomEquipe, Niveau niveau, Set<Etudiant> etudiants, DetailEquipe detailEquipe) {
        this.nomEquipe = nomEquipe;
        this.niveau = niveau;
        this.etudiants = etudiants != null ? etudiants : new HashSet<>(); // Handle potential null
        this.detailEquipe = detailEquipe;
    }

    public Equipe(Integer idEquipe, String nomEquipe, Niveau niveau, Set<Etudiant> etudiants, DetailEquipe detailEquipe) {
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
        this.niveau = niveau;
        this.etudiants = etudiants != null ? etudiants : new HashSet<>(); // Handle potential null
        this.detailEquipe = detailEquipe;
    }

    public void addEtudiant(Etudiant etudiant) {
        if (etudiant != null) { // Check for null to avoid adding null references
            this.etudiants.add(etudiant);
            etudiant.getEquipes().add(this); // Maintain bidirectional relationship
        }
    }

    // Getters and Setters
    public Set<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants != null ? etudiants : new HashSet<>(); // Handle potential null
    }

    public DetailEquipe getDetailEquipe() {
        return detailEquipe;
    }

    public void setDetailEquipe(DetailEquipe detailEquipe) {
        this.detailEquipe = detailEquipe;
    }

    public Integer getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(Integer idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getNomEquipe() {
        return nomEquipe;
    }

    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }
}
