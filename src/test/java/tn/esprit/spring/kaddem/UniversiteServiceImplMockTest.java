package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UniversiteServiceImplMockTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteServiceImpl universiteServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUniversite() {
        // Given
        Universite universite = new Universite(1, "Université Mockito Test");

        // Simuler le comportement du repository
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        // When
        Universite result = universiteServiceImpl.addUniversite(universite);

        // Then
        assertNotNull(result);
        assertEquals("Université Mockito Test", result.getNomUniv());

        // Vérifier que la méthode save() a bien été appelée
        verify(universiteRepository, times(1)).save(any(Universite.class));
    }

    @Test
    void testRetrieveAllUniversites() {
        // Given: créer deux universités factices
        Universite universite1 = new Universite(1, "Université 1");
        Universite universite2 = new Universite(2, "Université 2");

        // Simuler le comportement du repository
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite1, universite2));

        // When
        List<Universite> universites = universiteServiceImpl.retrieveAllUniversites();

        // Then
        assertEquals(2, universites.size());
        assertEquals("Université 1", universites.get(0).getNomUniv());

        // Vérifier que la méthode findAll() a bien été appelée
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUniversite() {
        // Given: Simuler la suppression de l'université
        Universite universite = new Universite(1, "Université à Supprimer");
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        // When
        universiteServiceImpl.deleteUniversite(1);

        // Then: Vérifier que la méthode delete() a bien été appelée
        verify(universiteRepository, times(1)).delete(universite);
    }

    @Test
    void testCountDepartementsInUniversite() {
        // Given: créer une université simulée avec deux départements
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

        // Simuler la récupération de l'université avec l'ID 1
        when(universiteRepository.findById(anyInt())).thenReturn(Optional.of(universite));

        // When: appel de la méthode
        int nombreDepartements = universiteServiceImpl.countDepartementsInUniversite(1);

        // Then: vérifier que le nombre de départements est correct
        assertEquals(2, nombreDepartements); // Il devrait y avoir 2 départements

        // Vérifier que la méthode findById a été appelée une seule fois
        verify(universiteRepository, times(1)).findById(anyInt());
    }
}
