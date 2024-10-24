package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UniversiteServiceImplJUnitTest {

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private UniversiteServiceImpl universiteServiceImpl;

    @BeforeEach
    void setUp() {
        universiteRepository.deleteAll();
        departementRepository.deleteAll();
    }

    @Test
    void testAddUniversite() {
        Universite universite = new Universite("Université JUnit Test");
        Universite result = universiteServiceImpl.addUniversite(universite);

        assertNotNull(result);
        assertEquals("Université JUnit Test", result.getNomUniv());
        assertNotNull(result.getIdUniv());
    }

    @Test
    void testCountDepartementsInUniversite() {
        Universite universite = new Universite("Université Test");
        Departement dep1 = new Departement("Informatique");
        Departement dep2 = new Departement("Mathématiques");

        dep1 = departementRepository.save(dep1); // Persisting the departments before associating
        dep2 = departementRepository.save(dep2);

        universiteServiceImpl.addUniversite(universite);
        universiteServiceImpl.addMultipleDepartementsToUniversite(universite.getIdUniv(), List.of(dep1.getIdDepart(), dep2.getIdDepart()));

        int count = universiteServiceImpl.countDepartementsInUniversite(universite.getIdUniv());
        assertEquals(2, count);
    }

    @Test
    void testAddMultipleDepartementsToUniversite() {
        Universite universite = new Universite("Université Test");
        universite = universiteServiceImpl.addUniversite(universite);

        Departement dep1 = new Departement("Informatique");
        Departement dep2 = new Departement("Mathématiques");

        // Persist the departments before adding them to the university
        dep1 = departementRepository.save(dep1);
        dep2 = departementRepository.save(dep2);

        universiteServiceImpl.addMultipleDepartementsToUniversite(universite.getIdUniv(), List.of(dep1.getIdDepart(), dep2.getIdDepart()));

        Universite updatedUniversite = universiteServiceImpl.retrieveUniversite(universite.getIdUniv());
        assertEquals(2, updatedUniversite.getDepartements().size());
    }

    @Test
    void testRemoveDepartementsFromUniversite() {
        Universite universite = new Universite("Université Test");
        universite = universiteServiceImpl.addUniversite(universite);

        Departement dep1 = new Departement("Informatique");
        Departement dep2 = new Departement("Mathématiques");

        // Persist departments and associate them
        dep1 = departementRepository.save(dep1);
        dep2 = departementRepository.save(dep2);

        universiteServiceImpl.addMultipleDepartementsToUniversite(universite.getIdUniv(), List.of(dep1.getIdDepart(), dep2.getIdDepart()));

        universiteServiceImpl.removeDepartementsFromUniversite(universite.getIdUniv(), List.of(dep1.getIdDepart()));

        Universite updatedUniversite = universiteServiceImpl.retrieveUniversite(universite.getIdUniv());
        assertEquals(1, updatedUniversite.getDepartements().size());
    }

    @Test
    void testFindDepartementsByCriteria() {
        Universite universite = new Universite("Université Test");
        universite = universiteServiceImpl.addUniversite(universite);

        Departement dep1 = new Departement("Informatique");
        Departement dep2 = new Departement("Mathématiques");

        dep1 = departementRepository.save(dep1);
        dep2 = departementRepository.save(dep2);

        universiteServiceImpl.addMultipleDepartementsToUniversite(universite.getIdUniv(), List.of(dep1.getIdDepart(), dep2.getIdDepart()));

        List<Departement> departements = universiteServiceImpl.findDepartementsByCriteria(universite.getIdUniv(), "Informatique", 1);

        assertEquals(1, departements.size());
        assertEquals("Informatique", departements.get(0).getNomDepart());
    }

    @Test
    void testSearchUniversities() {
        Universite universite1 = new Universite("Université de Paris");
        Universite universite2 = new Universite("Université de Lyon");

        universiteServiceImpl.addUniversite(universite1);
        universiteServiceImpl.addUniversite(universite2);

        List<Universite> result = universiteServiceImpl.searchUniversities("Université", 0);
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteUniversite() {
        Universite universite = new Universite("Université à Supprimer");
        universite = universiteServiceImpl.addUniversite(universite);

        universiteServiceImpl.deleteUniversite(universite.getIdUniv());

        Universite finalUniversite = universite;
        assertThrows(NoSuchElementException.class, () -> universiteServiceImpl.retrieveUniversite(finalUniversite.getIdUniv()));
    }
}
