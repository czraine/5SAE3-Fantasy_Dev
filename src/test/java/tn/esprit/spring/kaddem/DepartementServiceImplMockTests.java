package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartementServiceImplMockTests {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize Mockito annotations
    }

    @Test
    public void testAddDepartement() {
        // Create a mock Departement object
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Mock the repository save method
        when(departementRepository.save(departement)).thenReturn(departement);

        // Call the service method
        Departement savedDepartement = departementService.addDepartement(departement);

        // Validate the response
        assertNotNull(savedDepartement);
        assertEquals("Informatique", savedDepartement.getNomDepart());

        // Verify that the repository's save method was called exactly once
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    public void testRetrieveDepartement() {
        // Mock an existing Departement
        Departement departement = new Departement(1, "Finance");
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Call the service method
        Departement retrievedDepartement = departementService.retrieveDepartement(1);

        // Validate the response
        assertNotNull(retrievedDepartement);
        assertEquals("Finance", retrievedDepartement.getNomDepart());

        // Verify that the repository's findById method was called with the correct ID
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteDepartement() {
        // Mock an existing Departement ID for deletion
        int departementId = 1;

        // Create a mock Departement object to return when findById is called
        Departement mockDepartement = new Departement();
        mockDepartement.setIdDepart(departementId);

        // Mock the repository's findById method to return the mock Departement
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(mockDepartement));

        // Do nothing when the repository's deleteById method is called
        doNothing().when(departementRepository).delete(mockDepartement);

        // Call the service method to delete the Departement
        departementService.deleteDepartement(departementId);

        // Verify that the repository's deleteById method was called once with the correct Departement
        verify(departementRepository, times(1)).delete(mockDepartement);
    }

}
