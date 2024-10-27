package tn.esprit.spring.kaddem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Getter
@Entity
@Setter
public class Departement implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idDepart;
    private String nomDepart;
    @OneToMany(mappedBy="departement")
    @JsonIgnore
    private Set<Etudiant> etudiants;
    public Departement() {
    }

    public Departement(String nomDepart) {
        super();
        this.nomDepart = nomDepart;
    }

    public Departement(Integer idDepart, String nomDepart) {
        super();
        this.idDepart = idDepart;
        this.nomDepart = nomDepart;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public void setIdDepart(Integer idDepart) {
        this.idDepart = idDepart;
    }

    public void setNomDepart(String nomDepart) {
        this.nomDepart = nomDepart;
    }

}
