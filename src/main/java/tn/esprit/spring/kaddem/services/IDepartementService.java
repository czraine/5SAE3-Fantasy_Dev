package tn.esprit.spring.kaddem.services;

import tn.esprit.spring.kaddem.entities.Departement;
import java.util.List;

public interface IDepartementService {
    List<Departement> retrieveAllDepartements();
    Departement addDepartement(Departement d);
    Departement updateDepartement(Integer id, Departement d); // Updated here
    Departement retrieveDepartement(int departementId);
    void deleteDepartement(Integer idDepartement);
}
