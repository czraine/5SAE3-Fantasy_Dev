package tn.esprit.spring.kaddem.services;

import tn.esprit.spring.kaddem.entities.Departement;

import java.util.List;

public interface IDepartementService {
    public List<Departement> retrieveAllDepartements();

    public Departement addDepartement (Departement d);

    public   Departement updateDepartement (Departement d);


    Departement retrieveDepartement(int departementId);

    public  void deleteDepartement(Integer idDepartement);

}
