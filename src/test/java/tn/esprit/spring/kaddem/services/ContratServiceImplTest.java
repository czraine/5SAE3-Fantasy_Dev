package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class ContratServiceImplTest {
    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mocks and injects them
    }
    @Test
    void retrieveAllContrats() {
        // Arrange
        List<Contrat> contrats = Arrays.asList(new Contrat(), new Contrat());
        when(contratRepository.findAll()).thenReturn(contrats);

        // Act
        List<Contrat> retrievedContrats = contratService.retrieveAllContrats();

        // Assert
        assertEquals(2, retrievedContrats.size(), "Should retrieve 2 contracts.");
        verify(contratRepository, times(1)).findAll();
    }


    @Test
    void updateContrat() {
        // Arrange
        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.CLOUD, false, 1500);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        // Act
        Contrat updatedContrat = contratService.updateContrat(contrat);

        // Assert
        assertNotNull(updatedContrat, "Updated contract should not be null.");
        assertEquals(1500, updatedContrat.getMontantContrat(), "Contract amount should be 1500.");
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void addContrat() {
        // Arrange
        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.CLOUD, false, 2000);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        // Act
        Contrat savedContrat = contratService.addContrat(contrat);

        // Assert
        assertNotNull(savedContrat, "Saved contract should not be null.");
        assertEquals(2000, savedContrat.getMontantContrat(), "Contract amount should be 2000.");
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void retrieveContrat() {
        // Arrange
        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.SECURITE, false, 3000);
        when(contratRepository.findById(1L)).thenReturn(Optional.of(contrat));

        // Act
        Contrat retrievedContrat = contratService.retrieveContrat(1L);

        // Assert
        assertNotNull(retrievedContrat, "Retrieved contract should not be null.");
        assertEquals(Specialite.SECURITE, retrievedContrat.getSpecialite(), "Contract specialty should be SECURITE.");
        verify(contratRepository, times(1)).findById(1L);
    }
    @Test
    void removeContrat() {
        // Arrange
        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.IA, false, 2500);
        when(contratRepository.findById(1L)).thenReturn(Optional.of(contrat));

        // Act
        contratService.removeContrat(1);

        // Assert
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void affectContratToEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant("John", "Doe");
        Contrat contrat = new Contrat();
        contrat.setIdContrat(1L);
        when(etudiantRepository.findByNomEAndPrenomE("John", "Doe")).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(1L)).thenReturn(contrat);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        // Act
        Contrat affectedContrat = contratService.affectContratToEtudiant(1L, "John", "Doe");

        // Assert
        assertNotNull(affectedContrat, "Contract should be affected to the student.");
        assertEquals("John", affectedContrat.getEtudiant().getNomE(), "Student's name should be John.");
        assertEquals("Doe", affectedContrat.getEtudiant().getPrenomE(), "Student's surname should be Doe.");
    }

    @Test
    void nbContratsValides() {
        // Arrange
        Date startDate = new Date(2023 - 1900, 0, 1); // Jan 1, 2023
        Date endDate = new Date(2023 - 1900, 11, 31); // Dec 31, 2023
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        // Act
        Integer validContracts = contratService.nbContratsValides(startDate, endDate);

        // Assert
        assertEquals(5, validContracts, "There should be 5 valid contracts.");
    }
    @Test
    void retrieveAndUpdateStatusContrat() {
        // Arrange
        Date now = new Date();
        Contrat expiredContrat = new Contrat(new Date(), new Date(now.getTime() - (15 * 24 * 60 * 60 * 1000L)), Specialite.RESEAUX, false, 1000);
        Contrat activeContrat = new Contrat(new Date(), new Date(now.getTime() + (30 * 24 * 60 * 60 * 1000L)), Specialite.CLOUD, false, 1500);
        List<Contrat> contrats = Arrays.asList(expiredContrat, activeContrat);
        when(contratRepository.findAll()).thenReturn(contrats);

        // Act
        contratService.retrieveAndUpdateStatusContrat();

        // Assert
        assertTrue(expiredContrat.getArchive(), "Expired contract should be archived.");
        assertFalse(activeContrat.getArchive(), "Active contract should not be archived.");
        verify(contratRepository, times(1)).save(expiredContrat);
        verify(contratRepository, never()).save(activeContrat);
    }

    @Test
    void getChiffreAffaireEntreDeuxDates() {
        // Arrange
        Date startDate = new Date(2023 - 1900, 0, 1); // Jan 1, 2023
        Date endDate = new Date(2023 - 1900, 11, 31); // Dec 31, 2023
        List<Contrat> contrats = Arrays.asList(
                new Contrat(new Date(123, 0, 1), new Date(123, 11, 31), Specialite.IA, false, 500),
                new Contrat(new Date(123, 0, 1), new Date(123, 11, 31), Specialite.CLOUD, false, 1000),
                new Contrat(new Date(123, 0, 1), new Date(123, 11, 31), Specialite.RESEAUX, false, 1500)
        );
        when(contratRepository.findAll()).thenReturn(contrats);

        // Act
        float revenue = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        assertEquals(3000, revenue, "The revenue should be 3000.");
    }

}