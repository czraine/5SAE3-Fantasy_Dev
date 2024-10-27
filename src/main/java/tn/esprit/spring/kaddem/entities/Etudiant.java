package tn.esprit.spring.kaddem.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@SuppressWarnings("SpellCheckingInspection")
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idEtudiant;
    private String nomE;
    private String prenomE;
    @Enumerated(EnumType.STRING)
    private Option op;
    @OneToMany(mappedBy="etudiant", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Contrat> contrats;
    @ManyToOne
    @JsonIgnore
    private Departement departement;
  //  @ManyToMany(cascade =CascadeType.ALL)
    @ManyToMany(mappedBy="etudiants")

    @JsonIgnore
    private List<Equipe> equipes ;


    public Etudiant(String nomE, String prenomE) {
        this.nomE = nomE;
        this.prenomE = prenomE;
    }

    public Etudiant(String nomE, String prenomE, Option op) {
        super();
        this.nomE = nomE;
        this.prenomE = prenomE;
        this.op = op;
    }

    public Etudiant(Integer idEtudiant, String nomE, String prenomE, Option op) {
        super();
        this.idEtudiant = idEtudiant;
        this.nomE = nomE;
        this.prenomE = prenomE;
        this.op = op;
    }

    public void setContrats(Set<Contrat> contrats) {
        this.contrats = contrats;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }

    public void setIdEtudiant(Integer idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public void setNomE(String nomE) {
        this.nomE = nomE;
    }

    public void setPrenomE(String prenomE) {
        this.prenomE = prenomE;
    }

    public void setOp(Option op) {
        this.op = op;
    }

}
