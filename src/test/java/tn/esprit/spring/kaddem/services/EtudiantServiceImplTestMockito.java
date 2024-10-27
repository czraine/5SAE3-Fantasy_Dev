package tn.esprit.spring.kaddem.services;

import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceImplTestMockito {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Mock
    private ContratRepository contratRepository;
    @Mock
    private EquipeRepository equipeRepository;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNom("John");
        etudiant.setPrenom("Doe");
    }

    @Test
    void retrieveAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        assertEquals(1, result.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void addEtudiant() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);

        assertNotNull(result);
        assertEquals("John", result.getNom());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void updateEtudiant() {
        // Given
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNom("John");
        etudiant.setPrenom("Doe");

        // Mocking the save method
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // When
        Etudiant result = etudiantService.updateEtudiant(etudiant);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getNom());
        assertEquals("Doe", result.getPrenom());
        assertEquals(1, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant);
    }


    @Test
    void retrieveEtudiant() {
        when(etudiantRepository.findById(anyInt())).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.retrieveEtudiant(1);

        assertNotNull(result);
        assertEquals("John", result.getNom());
        verify(etudiantRepository, times(1)).findById(1);
    }

    @Test
    void removeEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        doNothing().when(etudiantRepository).deleteById(anyInt());

        // Act
        etudiantService.removeEtudiant(1);

        // Assert
        verify(etudiantRepository, times(1)).deleteById(1);
    }

/*
    @Test
    void assignEtudiantToDepartement() {
        Departement departement = new Departement();
        departement.setIdDepart(1);
        when(etudiantRepository.findById(anyInt())).thenReturn(Optional.of(etudiant));

        etudiantService.assignEtudiantToDepartement(etudiant.getIdEtudiant(), departement.getIdDepart());

        assertEquals(departement, etudiant.getDepartement());
        verify(etudiantRepository, times(1)).save(etudiant);
    }*/

/*
    @Test
    void addAndAssignEtudiantToEquipeAndContract() {
        // Create an Equipe and Contrat for the test
        Equipe equipe = new Equipe();
        equipe.setIdEquipe(1);

        Contrat contrat = new Contrat();
        contrat.setIdContrat(1L); // Assuming Contrat has an ID

        // Mock the repository behavior
        when(contratRepository.findById(1L)).thenReturn(java.util.Optional.of(contrat));
        when(equipeRepository.findById(1)).thenReturn(java.util.Optional.of(equipe));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Call the service method (make sure to provide the correct idContrat and idEquipe)
        etudiantService.addAndAssignEtudiantToEquipeAndContract(etudiant, 1L, 1);

        // Verify that the save method was called on the repository
        verify(etudiantRepository, times(1)).save(etudiant);
        // Verify that the contract's student is set
        assertEquals(etudiant, contrat.getEtudiant());
        // Verify that the student is added to the equipe
        assertTrue(equipe.getEtudiants().contains(etudiant));
    }
*/
    @Test
    void findEtudiantsByNomOrPrenom() {
        // Create a mock Etudiant object
        Etudiant etudiant = new Etudiant("John", "Doe");

        // Prepare the list containing the single Etudiant object
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);

        // When the repository method is called, return the list of Etudiant objects
        when(etudiantRepository.findByNomContainingOrPrenomContaining("John", "John")).thenReturn(etudiants);

        // Call the service method to retrieve a list of Etudiants
        List<Etudiant> result = etudiantService.findEtudiantsByNomOrPrenom("John");

        // Assert that the returned list size is correct and the content matches
        assertEquals(1, result.size());
        assertEquals(etudiant, result.get(0)); // Check the actual object if needed
        verify(etudiantRepository, times(1)).findByNomContainingOrPrenomContaining("John", "John");
    }

/*
    @Test
    void getEtudiantsWithActiveContrats() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);
        when(etudiantRepository.findEtudiantsWithActiveContracts()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.getEtudiantsWithActiveContrats();

        assertEquals(1, result.size());
        verify(etudiantRepository, times(1)).findEtudiantsWithActiveContracts();
    }*/

    @Test
    void isEtudiantInEquipe() {
        // Create an instance of Equipe
        Equipe equipe = new Equipe();
        equipe.setIdEquipe(1); // Set an ID for the equipe

        // Create an instance of Etudiant and assign the equipe
        Etudiant etudiant = new Etudiant("John", "Doe");
        etudiant.getEquipes().add(equipe); // Add the equipe to the etudiant's equipes

        // Mock the behavior of the repository
        when(etudiantRepository.findById(etudiant.getIdEtudiant())).thenReturn(Optional.of(etudiant));

        // Call the method under test
        boolean result = etudiantService.isEtudiantInEquipe(etudiant.getIdEtudiant(), equipe.getIdEquipe());

        // Verify the results
        assertTrue(result);
        verify(etudiantRepository, times(1)).findById(etudiant.getIdEtudiant());
    }

}
