package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = KaddemApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartementServiceImplTests {

    @Mock
    private DepartementRepository departementRepository;
    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }
    @Test
    @Order(1)
    public void testAddDepartement() {
        // Create a Departement entity for the test
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Mock the repository save method
        when(departementRepository.save(departement)).thenReturn(departement);

        // Call the service method
        Departement savedDepartement = departementService.addDepartement(departement);

        // Validate the response
        assertNotNull(savedDepartement);
        assertEquals("Informatique", savedDepartement.getNomDepart());

        // Verify that the save method was called once
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    @Order(2)
    public void testRetrieveDepartement() {
        // Mock an existing Departement
        Departement departement = new Departement(1, "Finance");
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Call the service method
        Departement retrievedDepartement = departementService.retrieveDepartement(1);

        // Validate the response
        assertNotNull(retrievedDepartement);
        assertEquals("Finance", retrievedDepartement.getNomDepart());

        // Verify that the findById method was called once
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    @Order(3)
    public void testDeleteDepartement() {
        // Mock an existing Departement
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Mock the findById method
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Call the service method
        departementService.deleteDepartement(1);

        // Verify that the delete method was called once with the correct Departement instance
        verify(departementRepository, times(1)).delete(departement);
    }

    @Test
    @Order(4)
    public void testUpdateDepartement() {
        // Mock an existing Departement
        Departement existingDepartement = new Departement(1, "Finance");
        when(departementRepository.findById(1)).thenReturn(Optional.of(existingDepartement));

        // Update the Departement's details
        existingDepartement.setNomDepart("Updated Finance");

        // Mock the repository save method
        when(departementRepository.save(existingDepartement)).thenReturn(existingDepartement);

        // Call the service method to update with both ID and Departement object
        Departement updatedDepartement = departementService.updateDepartement(1, existingDepartement);

        // Validate the updated fields
        assertNotNull(updatedDepartement);
        assertEquals("Updated Finance", updatedDepartement.getNomDepart());

        // Verify that the save method was called once
        verify(departementRepository, times(1)).save(existingDepartement);
    }

    @Test
    @Order(5)
    public void testRetrieveDepartmentsByPartialName() {
        // Mock some departments to match and some that don't
        Departement depart1 = new Departement(1, "Informatique");
        Departement depart2 = new Departement(2, "Finances");
        Departement depart3 = new Departement(3, "Informatique Appliquée");
        List<Departement> departments = Arrays.asList(depart1, depart2, depart3);

        // Mock the repository method to return matching departments
        when(departementRepository.findByNameContaining("info")).thenReturn(Arrays.asList(depart1, depart3));

        // Call the service method
        List<Departement> retrievedDepartments = departementService.retrieveDepartmentsByPartialName("info");

        // Validate that only the expected departments are retrieved
        assertNotNull(retrievedDepartments);
        assertTrue(retrievedDepartments.size() == 2);
        assertTrue(retrievedDepartments.contains(depart1));
        assertTrue(retrievedDepartments.contains(depart3));
        assertTrue(retrievedDepartments.stream().noneMatch(d -> d.getNomDepart().equals("Finances")));

        // Verify that the repository method was called once with the correct parameter
        verify(departementRepository, times(1)).findByNameContaining("info");
    }

    @Test
    @Order(6)
    public void testAffectEtudiantToDepartement() {
        // Mock an existing Etudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNomE("John");
        etudiant.setPrenomE("Doe");

        // Mock an existing Departement
        Departement departement = new Departement();
        departement.setIdDepart(1);
        departement.setNomDepart("Informatique");

        // Mock findById calls to return the existing Etudiant and Departement
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Mock the save calls
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);
        when(departementRepository.save(any(Departement.class))).thenReturn(departement);

        // Call the service method to assign the student to the department
        Departement updatedDepartement = departementService.affectEtudiantToDepartement(1, 1);

        // Validate that the department now has the student assigned
        assertNotNull(updatedDepartement);
        assertTrue(updatedDepartement.getEtudiants().contains(etudiant));

        // Verify the interactions with the repositories
        verify(etudiantRepository, times(1)).findById(1);
        verify(departementRepository, times(1)).findById(1);
        verify(etudiantRepository, times(1)).save(etudiant);
        verify(departementRepository, times(1)).save(departement);
    }
}







