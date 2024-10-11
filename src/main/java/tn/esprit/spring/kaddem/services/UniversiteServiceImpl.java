package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UniversiteServiceImpl implements IUniversiteService{
@Autowired
    UniversiteRepository universiteRepository;
@Autowired
    DepartementRepository departementRepository;
    public UniversiteServiceImpl() {
        // TODO Auto-generated constructor stub
    }
  public   List<Universite> retrieveAllUniversites(){
return (List<Universite>) universiteRepository.findAll();
    }

 public    Universite addUniversite (Universite  u){
return  (universiteRepository.save(u));
    }

 public    Universite updateUniversite (Universite  u){
     return  (universiteRepository.save(u));
    }

    @Override
    public Universite retrieveUniversite(Integer idUniversite) {
        // Utilisation de orElseThrow pour lever une exception claire si l'université n'est pas trouvée
        return universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new NoSuchElementException("Aucune université trouvée avec l'ID " + idUniversite));
    }

    @Override
    public void deleteUniversite(Integer idUniversite) {
        Universite universite = retrieveUniversite(idUniversite);
        universiteRepository.delete(universite);
    }

    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement){
        Universite u= universiteRepository.findById(idUniversite).orElse(null);
        Departement d= departementRepository.findById(idDepartement).orElse(null);
        u.getDepartements().add(d);
        universiteRepository.save(u);
    }

    public Set<Departement> retrieveDepartementsByUniversite(Integer idUniversite){
Universite u=universiteRepository.findById(idUniversite).orElse(null);
return u.getDepartements();
    }
    public void setUniversiteRepository(UniversiteRepository universiteRepository) {
        this.universiteRepository = universiteRepository;
    }

    // Nouveau service métier pour calculer le nombre de départements
    @Override
    @Transactional // Ajout de cette annotation pour éviter LazyInitializationException
    public int countDepartementsInUniversite(Integer idUniversite) {
        // Récupérer l'université avec l'ID donné
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new NoSuchElementException("Universite not found"));

        // Le proxy lazy de `departements` sera initialisé ici
        return universite.getDepartements().size();
    }
}
