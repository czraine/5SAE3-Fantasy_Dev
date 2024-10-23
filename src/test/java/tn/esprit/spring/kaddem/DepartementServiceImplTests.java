package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

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

    @InjectMocks
    private DepartementServiceImpl departementService;

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




}
