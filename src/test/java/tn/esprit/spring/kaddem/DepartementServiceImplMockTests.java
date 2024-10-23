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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartementServiceImplMockTests {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddDepartement_Success() {
        // Arrange
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Act
        when(departementRepository.save(departement)).thenReturn(departement);
        Departement savedDepartement = departementService.addDepartement(departement);

        // Assert
        assertNotNull(savedDepartement);
        assertEquals("Informatique", savedDepartement.getNomDepart());
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    public void testRetrieveDepartement_Success() {
        // Arrange
        Departement departement = new Departement(1, "Finance");
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Act
        Departement retrievedDepartement = departementService.retrieveDepartement(1);

        // Assert
        assertNotNull(retrievedDepartement);
        assertEquals("Finance", retrievedDepartement.getNomDepart());
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    public void testRetrieveDepartement_NotFound() {
        // Arrange
        when(departementRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            departementService.retrieveDepartement(1);
        });

        assertEquals("No Departement found with ID: 1", exception.getMessage());
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteDepartement_Success() {
        // Arrange
        int departementId = 1;
        Departement departement = new Departement(departementId, "Finance");
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));
        doNothing().when(departementRepository).delete(departement);

        // Act
        departementService.deleteDepartement(departementId);

        // Assert
        verify(departementRepository, times(1)).findById(departementId);
        verify(departementRepository, times(1)).delete(departement);
    }

    @Test
    public void testUpdateDepartement_Success() {
        // Arrange
        Integer departementId = 1; // Specify the ID to be updated
        Departement existingDepartement = new Departement(departementId, "Informatique");
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(existingDepartement));
        when(departementRepository.save(existingDepartement)).thenReturn(existingDepartement);

        // Act
        existingDepartement.setNomDepart("Updated Informatique");
        Departement updatedDepartement = departementService.updateDepartement(departementId, existingDepartement);

        // Assert
        assertNotNull(updatedDepartement);
        assertEquals("Updated Informatique", updatedDepartement.getNomDepart());
        verify(departementRepository, times(1)).save(existingDepartement);
    }

    @Test
    void testUpdateDepartement_NotFound() {
        // Arrange: Mock the behavior of the repository
        Integer departementId = 1;
        when(departementRepository.findById(departementId)).thenReturn(Optional.empty());

        // Act & Assert: Execute the method and assert the exception
        assertThrows(NoSuchElementException.class, () -> {
            departementService.updateDepartement(departementId, new Departement());
        });

        // Verify that findById was called
        verify(departementRepository).findById(departementId);
    }
}
