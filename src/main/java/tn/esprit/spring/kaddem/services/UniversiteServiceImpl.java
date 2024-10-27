package tn.esprit.spring.kaddem.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@NoArgsConstructor
public class UniversiteServiceImpl implements IUniversiteService{
    UniversiteRepository universiteRepository;
    DepartementRepository departementRepository;

  public   List<Universite> retrieveAllUniversites(){
return (List<Universite>) universiteRepository.findAll();
    }

 public    Universite addUniversite (Universite  u){
return  (universiteRepository.save(u));
    }

 public    Universite updateUniversite (Universite  u){
     return  (universiteRepository.save(u));
    }

    public Universite retrieveUniversite(Integer idUniversite) {
        return universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new NoSuchElementException("Universite not found with id: " + idUniversite));
    }

    public  void deleteUniversite(Integer idUniversite){
        universiteRepository.delete(retrieveUniversite(idUniversite));
    }

    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement){
        Universite u= universiteRepository.findById(idUniversite).orElse(null);
        Departement d= departementRepository.findById(idDepartement).orElse(null);
        assert u != null;
        u.getDepartements().add(d);
        universiteRepository.save(u);
    }

    public Set<Departement> retrieveDepartementsByUniversite(Integer idUniversite){
Universite u=universiteRepository.findById(idUniversite).orElse(null);
        assert u != null;
        return u.getDepartements();
    }
}
