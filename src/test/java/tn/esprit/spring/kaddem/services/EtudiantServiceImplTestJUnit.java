package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.*;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceImplTestJUnit {

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private DepartementRepository departementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void retrieveAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        assertNotNull(result);
        assertEquals(etudiants, result);
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void addEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant("John", "Doe", Option.GAMIX);
        Integer departementId = 1; // Mock department ID
        Departement departement = new Departement();
        departement.setIdDepart(departementId);
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Act
        Etudiant result = etudiantService.addEtudiant(etudiant, departementId);

        // Assert
        assertNotNull(result);
        assertEquals(etudiant.getNom(), result.getNom());
        assertEquals(etudiant.getPrenom(), result.getPrenom());
        assertEquals(Option.GAMIX, result.getOption());
        assertEquals(departement, result.getDepartement());
        verify(departementRepository, times(1)).findById(departementId);
        verify(etudiantRepository, times(1)).save(etudiant);
    }


    @Test
    void updateEtudiant() {
        Integer idEtudiant = 1;
        Departement departement = new Departement();
        departement.setIdDepart(1); // Assuming ID is 1 for this example
        Etudiant etudiant = new Etudiant(idEtudiant, "Jane", "Doe", Option.SE);
        etudiant.setDepartement(departement); // Set the department

        when(etudiantRepository.findById(idEtudiant)).thenReturn(Optional.of(etudiant));
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Updating the etudiant
        etudiant.setNom("Jane Updated");
        Etudiant result = etudiantService.updateEtudiant(etudiant);

        assertNotNull(result);
        assertEquals("Jane Updated", result.getNom()); // Assert updated name
        assertEquals(departement, result.getDepartement()); // Assert department is not null
        verify(etudiantRepository, times(1)).save(etudiant);
    }


    @Test
    void retrieveEtudiant() {
        Integer idEtudiant = 1;
        Etudiant etudiant = new Etudiant(idEtudiant, "John", "Doe", Option.GAMIX);
        when(etudiantRepository.findById(idEtudiant)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.retrieveEtudiant(idEtudiant);

        assertNotNull(result);
        assertEquals(etudiant, result);
        verify(etudiantRepository, times(1)).findById(idEtudiant);
    }

    @Test
    void removeEtudiant() {
        Integer idEtudiant = 1;
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(idEtudiant);
        when(etudiantRepository.findById(idEtudiant)).thenReturn(Optional.of(etudiant));

        etudiantService.removeEtudiant(idEtudiant);

        verify(etudiantRepository, times(1)).delete(etudiant); // Verify the delete method
    }

    @Test
    void assignEtudiantToDepartement() {
        Integer etudiantId = 1;
        Integer departementId = 1;
        Etudiant etudiant = new Etudiant(etudiantId, "John", "Doe", Option.GAMIX);
        Departement departement = new Departement();

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));

        etudiantService.assignEtudiantToDepartement(etudiantId, departementId);

        assertEquals(departement, etudiant.getDepartement());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void addAndAssignEtudiantToEquipeAndContract() {
        Etudiant etudiant = new Etudiant();
        Contrat contrat = new Contrat();
        Equipe equipe = new Equipe();
        long contratId = 1L;
        Integer equipeId = 1;

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(equipeRepository.findById(equipeId)).thenReturn(Optional.of(equipe));

        Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract(etudiant, contratId, equipeId);

        assertEquals(etudiant, contrat.getEtudiant());
        assertTrue(equipe.getEtudiants().contains(etudiant));
        verify(contratRepository, times(1)).findById(contratId);
        verify(equipeRepository, times(1)).findById(equipeId);
    }

    @Test
    void findEtudiantsByNomOrPrenom() {
        String search = "Test";
        List<Etudiant> etudiants = new ArrayList<>();
        when(etudiantRepository.findByNomContainingOrPrenomContaining(search, search)).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findEtudiantsByNomOrPrenom(search);

        assertEquals(etudiants, result);
        verify(etudiantRepository, times(1)).findByNomContainingOrPrenomContaining(search, search);
    }

    @Test
    void testGetEtudiantsWithActiveContrats() {
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        List<Etudiant> etudiantsList = new ArrayList<>(List.of(etudiant1, etudiant2));

        when(etudiantRepository.findByContrats_ArchiveFalse()).thenReturn(etudiantsList);

        List<Etudiant> result = etudiantService.getEtudiantsWithActiveContrats();

        assertEquals(2, result.size(), "Le nombre d'Etudiants avec des contrats actifs devrait être 2.");
        verify(etudiantRepository).findByContrats_ArchiveFalse();
    }
}
