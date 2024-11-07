//package tn.esprit.spring.kaddem;
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import tn.esprit.spring.kaddem.entities.Contrat;
//import tn.esprit.spring.kaddem.entities.Etudiant;
//import tn.esprit.spring.kaddem.entities.Specialite;
//import tn.esprit.spring.kaddem.repositories.ContratRepository;
//import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
//import tn.esprit.spring.kaddem.services.ContratServiceImpl;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@SpringBootTest
//public class ContactServiceTest {
//
//    @InjectMocks
//    private ContratServiceImpl contratService;
//
//    @Mock
//    private ContratRepository contratRepository;
//
//    @Mock
//    private EtudiantRepository etudiantRepository;
//
//    // Test adding a contract
//    @Test
//    @Order(1)
//    void testAddContrat() {
//        // Arrange
//        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.CLOUD, false, 2000);
//        when(contratRepository.save(contrat)).thenReturn(contrat);
//
//        // Act
//        Contrat savedContrat = contratService.addContrat(contrat);
//
//        // Assert
//        assertNotNull(savedContrat, "The saved contract should not be null.");
//        assertEquals(2000, savedContrat.getMontantContrat(), "The contract amount should be 2000.");
//        verify(contratRepository, times(1)).save(contrat);
//    }
//
//    // Test updating a contract
//    @Test
//    @Order(2)
//    void testUpdateContrat() {
//        // Arrange
//        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.RESEAUX, false, 1500);
//        when(contratRepository.save(contrat)).thenReturn(contrat);
//
//        // Act
//        Contrat updatedContrat = contratService.updateContrat(contrat);
//
//        // Assert
//        assertNotNull(updatedContrat, "The updated contract should not be null.");
//        assertEquals(1500, updatedContrat.getMontantContrat(), "The contract amount should be 1500.");
//        verify(contratRepository, times(1)).save(contrat);
//    }
//
//    // Test retrieving a contract by ID
//    @Test
//    @Order(3)
//    void testRetrieveContrat() {
//        // Arrange
//        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.SECURITE, false, 3000);
//        when(contratRepository.findById(1L)).thenReturn(Optional.of(contrat));
//
//        // Act
//        Contrat retrievedContrat = contratService.retrieveContrat(1);
//
//        // Assert
//        assertNotNull(retrievedContrat, "The retrieved contract should not be null.");
//        assertEquals(Specialite.SECURITE, retrievedContrat.getSpecialite(), "The contract specialty should be SECURITE.");
//        verify(contratRepository, times(1)).findById(1L);
//    }
//
//    // Test removing a contract
//    @Test
//    @Order(4)
//    void testRemoveContrat() {
//        // Arrange
//        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.IA, false, 2500);
//        when(contratRepository.findById(1L)).thenReturn(Optional.of(contrat));
//
//        // Act
//        contratService.removeContrat(1);
//
//        // Assert
//        verify(contratRepository, times(1)).delete(contrat);
//    }
//
//    // Test calculating the revenue between two dates
//    @Test
//    @Order(5)
//    void testChiffreAffaireEntreDeuxDates() {
//        // Arrange
//        Date startDate = new Date(2023 - 1900, 0, 1); // Jan 1, 2023
//        Date endDate = new Date(2023 - 1900, 11, 31); // Dec 31, 2023
//
//        List<Contrat> contrats = Arrays.asList(
//                new Contrat(new Date(123, 0, 1), new Date(123, 11, 31), Specialite.IA, false, 500),  // IA contract valid for the whole year
//                new Contrat(new Date(123, 0, 1), new Date(123, 11, 31), Specialite.CLOUD, false, 1000), // CLOUD contract valid for the whole year
//                new Contrat(new Date(123, 0, 1), new Date(123, 11, 31), Specialite.RESEAUX, false, 1500) // RESEAUX contract valid for the whole year
//        );
//
//        when(contratRepository.findAll()).thenReturn(contrats);
//
//        // Act
//        float revenue = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
//
//        // Assert
//        assertEquals(3000, revenue, "The calculated revenue should be 3000.");
//    }
//
//    // Test archiving contracts
//    @Test
//    @Order(6)
//    void testRetrieveAndUpdateStatusContrat() {
//        // Arrange
//        Date now = new Date();
//        Contrat expiredContrat = new Contrat(new Date(), new Date(now.getTime() - (15 * 24 * 60 * 60 * 1000L)), Specialite.RESEAUX, false, 1000);
//        Contrat activeContrat = new Contrat(new Date(), new Date(now.getTime() + (30 * 24 * 60 * 60 * 1000L)), Specialite.CLOUD, false, 1500);
//        List<Contrat> contrats = Arrays.asList(expiredContrat, activeContrat);
//        when(contratRepository.findAll()).thenReturn(contrats);
//
//        // Act
//        contratService.retrieveAndUpdateStatusContrat();
//
//        // Assert
//        assertTrue(expiredContrat.getArchive(), "Expired contract should be archived.");
//        assertFalse(activeContrat.getArchive(), "Active contract should not be archived.");
//        verify(contratRepository, times(1)).save(expiredContrat); // Verify that expiredContrat was saved
//        verify(contratRepository, never()).save(activeContrat); // Verify that activeContrat was not saved
//    }
//
//    // Test affecting a contract to a student
//
//    // Test counting valid contracts between two dates
//    @Test
//    @Order(7)
//    void testNbContratsValides() {
//        // Arrange
//        Date startDate = new Date(2023 - 1900, 0, 1); // Jan 1, 2023
//        Date endDate = new Date(2023 - 1900, 11, 31); // Dec 31, 2023
//        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);
//
//        // Act
//        Integer nbContratsValides = contratService.nbContratsValides(startDate, endDate);
//
//        // Assert
//        assertEquals(5, nbContratsValides, "There should be 5 valid contracts between the two dates.");
//    }
//
//}
