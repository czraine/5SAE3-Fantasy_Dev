package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceImplMockitoTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private UniversiteServiceImpl universiteServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUniversite() {
        Universite universite = new Universite(1, "Université Mockito Test");

        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite result = universiteServiceImpl.addUniversite(universite);

        assertNotNull(result);
        assertEquals("Université Mockito Test", result.getNomUniv());

        verify(universiteRepository, times(1)).save(any(Universite.class));
    }

    @Test
    void testRetrieveAllUniversites() {
        Universite universite1 = new Universite(1, "Université 1");
        Universite universite2 = new Universite(2, "Université 2");

        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite1, universite2));

        List<Universite> universites = universiteServiceImpl.retrieveAllUniversites();

        assertEquals(2, universites.size());
        assertEquals("Université 1", universites.get(0).getNomUniv());

        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveUniversite() {
        Universite universite = new Universite(1, "Université 1");

        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        Universite result = universiteServiceImpl.retrieveUniversite(1);

        assertNotNull(result);
        assertEquals("Université 1", result.getNomUniv());

        verify(universiteRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteUniversite() {
        Universite universite = new Universite(1, "Université à Supprimer");
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        universiteServiceImpl.deleteUniversite(1);

        verify(universiteRepository, times(1)).delete(universite);
    }

    @Test
    void testCountDepartementsInUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université Test");

        Departement departement1 = new Departement();
        departement1.setNomDepart("Informatique");

        Departement departement2 = new Departement();
        departement2.setNomDepart("Mathématiques");

        Set<Departement> departements = new HashSet<>();
        departements.add(departement1);
        departements.add(departement2);
        universite.setDepartements(departements);

        when(universiteRepository.findById(anyInt())).thenReturn(Optional.of(universite));

        int nombreDepartements = universiteServiceImpl.countDepartementsInUniversite(1);

        assertEquals(2, nombreDepartements);

        verify(universiteRepository, times(1)).findById(anyInt());
    }

    @Test
    void testFindDepartementsByCriteria() {
        Universite universite = new Universite();
        universite.setNomUniv("Université Test");

        Departement departement1 = new Departement();
        departement1.setNomDepart("Informatique");

        Departement departement2 = new Departement();
        departement2.setNomDepart("Mathématiques");

        universite.setDepartements(new HashSet<>(Arrays.asList(departement1, departement2)));
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        List<Departement> departements = universiteServiceImpl.findDepartementsByCriteria(1, "Informatique", 1);

        assertEquals(1, departements.size());
        assertEquals("Informatique", departements.get(0).getNomDepart());
    }

    @Test
    void testAddMultipleDepartementsToUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université Test");
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        Departement departement1 = new Departement();
        departement1.setIdDepart(1);
        departement1.setNomDepart("Informatique");

        Departement departement2 = new Departement();
        departement2.setIdDepart(2);
        departement2.setNomDepart("Mathématiques");

        when(departementRepository.findById(1)).thenReturn(Optional.of(departement1));
        when(departementRepository.findById(2)).thenReturn(Optional.of(departement2));

        universiteServiceImpl.addMultipleDepartementsToUniversite(1, Arrays.asList(1, 2));

        assertEquals(2, universite.getDepartements().size());
    }

    @Test
    void testRemoveDepartementsFromUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université Test");

        Departement departement1 = new Departement();
        departement1.setIdDepart(1);
        departement1.setNomDepart("Informatique");

        Departement departement2 = new Departement();
        departement2.setIdDepart(2);
        departement2.setNomDepart("Mathématiques");

        universite.setDepartements(new HashSet<>(Arrays.asList(departement1, departement2)));
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        universiteServiceImpl.removeDepartementsFromUniversite(1, Arrays.asList(1));

        assertEquals(1, universite.getDepartements().size());
    }

    @Test
    void testSearchUniversities() {
        Universite universite1 = new Universite();
        universite1.setNomUniv("Université Paris");
        universite1.setDepartements(new HashSet<>(Arrays.asList(new Departement())));

        Universite universite2 = new Universite();
        universite2.setNomUniv("Université Lyon");
        universite2.setDepartements(new HashSet<>(Arrays.asList(new Departement(), new Departement())));

        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite1, universite2));

        List<Universite> result = universiteServiceImpl.searchUniversities("Université", 2);

        assertEquals(1, result.size());
        assertEquals("Université Lyon", result.get(0).getNomUniv());
    }

}
