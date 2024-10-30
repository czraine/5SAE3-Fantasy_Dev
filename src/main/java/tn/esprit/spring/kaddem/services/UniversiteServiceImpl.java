package tn.esprit.spring.kaddem.services;

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
import java.util.stream.StreamSupport;

@Service
public class UniversiteServiceImpl implements IUniversiteService {

    private static final String UNIVERSITE_NOT_FOUND = "Universite not found";

    private final UniversiteRepository universiteRepository;
    private final DepartementRepository departementRepository;

    public UniversiteServiceImpl(UniversiteRepository universiteRepository, DepartementRepository departementRepository) {
        this.universiteRepository = universiteRepository;
        this.departementRepository = departementRepository;
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
                .orElseThrow(() -> new NoSuchElementException(UNIVERSITE_NOT_FOUND + " with ID " + idUniversite));
    }

    @Override
    public void deleteUniversite(Integer idUniversite) {
        Universite universite = retrieveUniversite(idUniversite);
        universiteRepository.delete(universite);
    }

    @Override
    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement) {
        Universite u = universiteRepository.findById(idUniversite).orElseThrow(() -> new NoSuchElementException(UNIVERSITE_NOT_FOUND));
        Departement d = departementRepository.findById(idDepartement).orElseThrow(() -> new NoSuchElementException("Departement not found"));
        u.getDepartements().add(d);
        universiteRepository.save(u);
    }

    @Override
    public Set<Departement> retrieveDepartementsByUniversite(Integer idUniversite) {
        Universite u = universiteRepository.findById(idUniversite).orElseThrow(() -> new NoSuchElementException(UNIVERSITE_NOT_FOUND));
        return u.getDepartements();
    }

    @Override
    @Transactional
    public int countDepartementsInUniversite(Integer idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new NoSuchElementException(UNIVERSITE_NOT_FOUND));
        return universite.getDepartements().size();
    }

    @Override
    public List<Universite> searchUniversities(String nomUniv, int minDepartements) {
        return StreamSupport.stream(universiteRepository.findAll().spliterator(), false)
                .filter(u -> u.getNomUniv().contains(nomUniv) && u.getDepartements().size() >= minDepartements)
                .toList();

    }

    @Override
    public List<Departement> findDepartementsByCriteria(Integer universiteId, String departementName, int minDepartements) {
        Universite universite = universiteRepository.findById(universiteId)
                .orElseThrow(() -> new NoSuchElementException(UNIVERSITE_NOT_FOUND));

        return universite.getDepartements().stream()
                .filter(d -> d.getNomDepart().contains(departementName) && universite.getDepartements().size() >= minDepartements)
                .toList();
    }

    @Override
    @Transactional
    public void removeDepartementsFromUniversite(Integer universiteId, List<Integer> departementIds) {
        Universite universite = universiteRepository.findById(universiteId)
                .orElseThrow(() -> new NoSuchElementException(UNIVERSITE_NOT_FOUND));

        Set<Departement> departements = universite.getDepartements();
        departements.removeIf(departement -> departementIds.contains(departement.getIdDepart()));
        universiteRepository.save(universite);
    }

    @Override
    @Transactional
    public void addMultipleDepartementsToUniversite(Integer idUniversite, List<Integer> departementIds) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new NoSuchElementException(UNIVERSITE_NOT_FOUND));

        if (universite.getDepartements() == null) {
            universite.setDepartements(new HashSet<>());
        }

        departementIds.forEach(departementId -> {
            Departement departement = departementRepository.findById(departementId)
                    .orElseThrow(() -> new NoSuchElementException("Departement not found"));
            universite.getDepartements().add(departement);
        });

        universiteRepository.save(universite);
    }
}
