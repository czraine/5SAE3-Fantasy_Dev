package tn.esprit.spring.kaddem;

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

import java.text.SimpleDateFormat;
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
        Set<Contrat> contrats = new HashSet<>();
        etudiant.setContrats(contrats);

        Mockito.when(etudiantRepository.findByNomEAndPrenomE("Nom", "Prenom")).thenReturn(etudiant);

        Date dateDebutContrat = new GregorianCalendar(2024, Calendar.OCTOBER, 1).getTime();
        Date dateFinContrat = new GregorianCalendar(2025, Calendar.OCTOBER, 1).getTime();

        Specialite specialite = Specialite.IA;

        Contrat contrat = new Contrat(
                1L,
                dateDebutContrat,
                dateFinContrat,
                specialite,
                false,
                5000,
                etudiant
        );

        Mockito.when(contratRepository.findByIdContrat(1)).thenReturn(contrat);

        contratService.affectContratToEtudiant(1, "Nom", "Prenom");

        Mockito.verify(contratRepository, Mockito.times(1)).save(contrat);
    }

    @Test
    public void testAffectContratToEtudiantWithFiveActiveContracts() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomE("John");
        etudiant.setPrenomE("Smith");

        Set<Contrat> activeContracts = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Contrat contrat = new Contrat();
            contrat.setDateDebutContrat(new Date());
            contrat.setDateFinContrat(new Date(System.currentTimeMillis() + 86400000)); // 1 day later
            contrat.setSpecialite(Specialite.CLOUD);
            contrat.setArchive(false);
            contrat.setMontantContrat(1000 + i * 100);
            contrat.setEtudiant(etudiant);
            activeContracts.add(contrat);
        }
        etudiant.setContrats(activeContracts);

        Contrat newContrat = new Contrat();
        newContrat.setIdContrat(1L);
        newContrat.setDateDebutContrat(new Date());
        newContrat.setDateFinContrat(new Date(System.currentTimeMillis() + 86400000));
        newContrat.setSpecialite(Specialite.IA);
        newContrat.setArchive(false);
        newContrat.setMontantContrat(1200);

        Mockito.when(etudiantRepository.findByNomEAndPrenomE("John", "Smith")).thenReturn(etudiant);
        Mockito.when(contratRepository.findByIdContrat(1)).thenReturn(newContrat);

        Contrat mockedContrat = contratService.affectContratToEtudiant(1, "John", "Smith");

        Assertions.assertNull(mockedContrat, "Contract should not be assigned when the student has 5 active contracts.");

        verify(etudiantRepository).findByNomEAndPrenomE("John", "Smith");
        verify(contratRepository).findByIdContrat(1);
        verify(contratRepository, Mockito.never()).save(Mockito.any(Contrat.class));
    }
    @Test
    void testNbContratsValidesWithinDateRange() {
        Date startDate = new Date(2023 - 1900, 0, 1);
        Date endDate = new Date(2023 - 1900, 11, 31);

        Mockito.when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        Integer validContracts = contratService.nbContratsValides(startDate, endDate);

        Assertions.assertEquals(5, validContracts);

        Mockito.when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(0);
        validContracts = contratService.nbContratsValides(startDate, endDate);
        Assertions.assertEquals(0, validContracts);
    }


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @Test
    public void testGetChiffreAffaireEntreDeuxDates() throws Exception {
        // Prepare test data
        Date startDate = sdf.parse("2024-01-01");
        Date endDate = sdf.parse("2024-12-31");

        Contrat contrat1 = new Contrat();
        contrat1.setDateDebutContrat(sdf.parse("2024-02-01"));
        contrat1.setDateFinContrat(sdf.parse("2024-11-01"));
        contrat1.setMontantContrat(1000);

        Contrat contrat2 = new Contrat();
        contrat2.setDateDebutContrat(sdf.parse("2023-12-01"));
        contrat2.setDateFinContrat(sdf.parse("2024-03-01"));
        contrat2.setMontantContrat(500);

        Contrat contrat3 = new Contrat();
        contrat3.setDateDebutContrat(sdf.parse("2024-05-01"));
        contrat3.setDateFinContrat(sdf.parse("2024-08-01"));
        contrat3.setMontantContrat(1500);

        // Mock the repository response
        List<Contrat> contrats = Arrays.asList(contrat1, contrat2, contrat3);
        when(contratRepository.findAll()).thenReturn(contrats);

        // Call method under test
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert the total calculated value
        assertEquals(3000, result);
    }

    @Test
    public void testGetChiffreAffaireEntreDeuxDates_NoValidContracts() throws Exception {
        // Prepare test data
        Date startDate = sdf.parse("2024-01-01");
        Date endDate = sdf.parse("2024-12-31");

        Contrat contrat1 = new Contrat();
        contrat1.setDateDebutContrat(sdf.parse("2023-11-01"));
        contrat1.setDateFinContrat(sdf.parse("2023-12-01"));
        contrat1.setMontantContrat(1000);

        // Mock the repository response
        List<Contrat> contrats = Arrays.asList(contrat1);
        when(contratRepository.findAll()).thenReturn(contrats);

        // Call method under test
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert that the total is zero because no contracts fall within the range
        assertEquals(0, result);
    }



}
