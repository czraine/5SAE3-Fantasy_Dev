package tn.esprit.spring.kaddem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Universite implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idUniv;
    private String nomUniv;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Departement> departements;
    public Universite() {
    }

    public Universite(String nomUniv) {
        super();
        this.nomUniv = nomUniv;
    }

    public Universite(Integer idUniv, String nomUniv) {
        super();
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }

    public void setIdUniv(Integer idUniv) {
        this.idUniv = idUniv;
    }

    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }

}
