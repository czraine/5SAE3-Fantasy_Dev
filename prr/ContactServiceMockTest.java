package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito extension to support Spring

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Define the order of tests
@SpringBootTest // Load the Spring Application Context for Integration Tests
public class ContactServiceMockTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes @Mock and @InjectMocks fields
    }
    @Test
    void testRetrieveAllContrats() {
        // Mocking a list of contracts
        List<Contrat> contrats = new ArrayList<>();
        Contrat contrat1 = new Contrat(new Date(), new Date(), Specialite.CLOUD, false, 1000);
        contrats.add(contrat1);

        // Mock repository to return the contract list
        Mockito.when(contratRepository.findAll()).thenReturn(contrats);

        // Call the service method
        List<Contrat> result = contratService.retrieveAllContrats();

        // Assertions to verify result
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(Specialite.CLOUD, result.get(0).getSpecialite());

        // Verify repository interaction
        Mockito.verify(contratRepository, Mockito.times(1)).findAll();
    }
    @Test
    void testAffectContratToEtudiant() {
        // Create a mock Etudiant
        Etudiant etudiant = new Etudiant("Nom", "Prenom");
        Set<Contrat> contrats = new HashSet<>(); // Use HashSet or any Set implementation
        etudiant.setContrats(contrats);

        // Mocking repository to return the Etudiant
        Mockito.when(etudiantRepository.findByNomEAndPrenomE("Nom", "Prenom")).thenReturn(etudiant);

        // Mocking contract data
        Contrat contrat = new Contrat(/* your parameters here */);
        Mockito.when(contratRepository.findByIdContrat(1)).thenReturn(contrat);

        // Call the service method
        contratService.affectContratToEtudiant(1, "Nom", "Prenom");

        // Verify that save was called
        Mockito.verify(contratRepository, Mockito.times(1)).save(contrat);
    }
    @Test
    public void testAffectContratToEtudiantWithFiveActiveContracts() {
        // Step 1: Create a student and add five active contracts
        Etudiant etudiant = new Etudiant();
        etudiant.setNomE("John");
        etudiant.setPrenomE("Smith");

        // Create five active contracts for the student
        Set<Contrat> activeContracts = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Contrat contrat = new Contrat();
            contrat.setDateDebutContrat(new Date());
            contrat.setDateFinContrat(new Date(System.currentTimeMillis() + 86400000)); // 1 day later
            contrat.setSpecialite(Specialite.CLOUD);
            contrat.setArchive(false);
            contrat.setMontantContrat(1000 + i * 100); // Different amounts
            contrat.setEtudiant(etudiant);  // Associate contract with the student
            activeContracts.add(contrat);
        }
        etudiant.setContrats(activeContracts);  // Set the active contracts

        // Step 2: Create a new contract to be assigned
        Contrat newContrat = new Contrat();
        newContrat.setIdContrat(1L); // Simulating an existing contract ID
        newContrat.setDateDebutContrat(new Date());
        newContrat.setDateFinContrat(new Date(System.currentTimeMillis() + 86400000)); // 1 day later
        newContrat.setSpecialite(Specialite.IA);
        newContrat.setArchive(false);
        newContrat.setMontantContrat(1200);

        // Step 3: Mock repository behavior
        Mockito.when(etudiantRepository.findByNomEAndPrenomE("John", "Smith")).thenReturn(etudiant); // Existing student
        Mockito.when(contratRepository.findByIdContrat(1)).thenReturn(newContrat);

        // Step 4: Attempt to assign the contract
        Contrat mockedContrat = contratService.affectContratToEtudiant(1, "John", "Smith");

        // Step 5: Assert results
        Assertions.assertNull(mockedContrat, "Contract should not be assigned when the student has 5 active contracts.");

        // Step 6: Verify repository interactions
        verify(etudiantRepository).findByNomEAndPrenomE("John", "Smith"); // Ensure student lookup was called
        verify(contratRepository).findByIdContrat(1); // Ensure contract lookup was called
        verify(contratRepository, Mockito.never()).save(Mockito.any(Contrat.class)); // Ensure save was not called
    }
    @Test
    void testNbContratsValidesWithinDateRange() {
        // Define start and end dates
        Date startDate = new Date(2023 - 1900, 0, 1);  // January 1st, 2023
        Date endDate = new Date(2023 - 1900, 11, 31);  // December 31st, 2023

        // Mock repository to return a number of valid contracts
        Mockito.when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        // Call service method
        Integer validContracts = contratService.nbContratsValides(startDate, endDate);

        // Assertions
        Assertions.assertEquals(5, validContracts);

        // Mock scenario with no valid contracts
        Mockito.when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(0);
        validContracts = contratService.nbContratsValides(startDate, endDate);
        Assertions.assertEquals(0, validContracts);
    }



}
