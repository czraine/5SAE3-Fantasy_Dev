package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UniversiteServiceImpl implements IUniversiteService {

    @Autowired
    UniversiteRepository universiteRepository;

    @Autowired
    DepartementRepository departementRepository;

    public UniversiteServiceImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<Universite> retrieveAllUniversites() {
        return (List<Universite>) universiteRepository.findAll();
    }

    @Override
    public Universite addUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    @Override
    public Universite updateUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    @Override
    public Universite retrieveUniversite(Integer idUniversite) {
        return universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new NoSuchElementException("Aucune université trouvée avec l'ID " + idUniversite));
    }

    @Override
    public void deleteUniversite(Integer idUniversite) {
        Universite universite = retrieveUniversite(idUniversite);
        universiteRepository.delete(universite);
    }

    @Override
    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement) {
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        Departement d = departementRepository.findById(idDepartement).orElse(null);
        u.getDepartements().add(d);
        universiteRepository.save(u);
    }

    @Override
    public Set<Departement> retrieveDepartementsByUniversite(Integer idUniversite) {
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        return u.getDepartements();
    }

    @Override
    @Transactional
    public int countDepartementsInUniversite(Integer idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new NoSuchElementException("Universite not found"));
        return universite.getDepartements().size();
    }

    // Méthode pour rechercher les universités selon les critères
    @Override
    public List<Universite> searchUniversities(String nomUniv, int minDepartements) {
        return ((List<Universite>) universiteRepository.findAll())
                .stream()
                .filter(u -> u.getNomUniv().contains(nomUniv) && u.getDepartements().size() >= minDepartements)
                .collect(Collectors.toList());
    }

    // Méthode pour trouver des départements selon des critères
    @Override
    public List<Departement> findDepartementsByCriteria(Integer universiteId, String departementName, int minDepartements) {
        Universite universite = universiteRepository.findById(universiteId)
                .orElseThrow(() -> new NoSuchElementException("Universite not found"));

        return universite.getDepartements().stream()
                .filter(d -> d.getNomDepart().contains(departementName) && universite.getDepartements().size() >= minDepartements)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeDepartementsFromUniversite(Integer universiteId, List<Integer> departementIds) {
        Universite universite = universiteRepository.findById(universiteId)
                .orElseThrow(() -> new NoSuchElementException("Universite not found"));

        Set<Departement> departements = universite.getDepartements();
        departements.removeIf(departement -> departementIds.contains(departement.getIdDepart()));
        universiteRepository.save(universite);
    }

    @Override
    @Transactional
    public void addMultipleDepartementsToUniversite(Integer idUniversite, List<Integer> departementIds) {
        Universite universite = universiteRepository.findById(idUniversite).orElseThrow(() -> new NoSuchElementException("Universite not found"));

        if (universite.getDepartements() == null) {
            universite.setDepartements(new HashSet<>()); // Initialiser le Set s'il est null
        }

        departementIds.forEach(departementId -> {
            Departement departement = departementRepository.findById(departementId).orElseThrow(() -> new NoSuchElementException("Departement not found"));
            universite.getDepartements().add(departement);
        });

        universiteRepository.save(universite);
    }

}
