package tn.esprit.spring.kaddem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Universite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUniv;

    private String nomUniv;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Departement> departements = new HashSet<>(); // Initialisation de la collection

    // Constructeurs
    public Universite() {
        this.departements = new HashSet<>(); // S'assurer que la collection est initialisée
    }

    public Universite(String nomUniv) {
        this.nomUniv = nomUniv;
        this.departements = new HashSet<>(); // S'assurer que la collection est initialisée
    }

    public Universite(Integer idUniv, String nomUniv) {
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
        this.departements = new HashSet<>(); // S'assurer que la collection est initialisée
    }

    // Getters et setters
    public Integer getIdUniv() {
        return idUniv;
    }

    public void setIdUniv(Integer idUniv) {
        this.idUniv = idUniv;
    }

    public String getNomUniv() {
        return nomUniv;
    }

    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }

    public Set<Departement> getDepartements() {
        return departements;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }
}
