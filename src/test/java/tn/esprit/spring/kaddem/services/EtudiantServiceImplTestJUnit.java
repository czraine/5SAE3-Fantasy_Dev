package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
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
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);

        assertNotNull(result);
        assertEquals(etudiant, result);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void updateEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.updateEtudiant(etudiant);

        assertNotNull(result);
        assertEquals(etudiant, result);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void retrieveEtudiant() {
        Integer idEtudiant = 1;
        Etudiant etudiant = new Etudiant();
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
        when(etudiantRepository.findById(idEtudiant)).thenReturn(Optional.of(etudiant));

        etudiantService.removeEtudiant(idEtudiant);

        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    void assignEtudiantToDepartement() {
        Integer etudiantId = 1;
        Integer departementId = 1;
        Etudiant etudiant = new Etudiant();
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
    void testAssignEtudiantToDepartement() {
        Integer etudiantId = 1;
        Integer departementId = 1;
        Etudiant etudiant = new Etudiant();
        Departement departement = new Departement();

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));

        etudiantService.assignEtudiantToDepartement(etudiantId, departementId);

        assertEquals(departement, etudiant.getDepartement()); // Check if the department is set
        verify(etudiantRepository, times(1)).save(etudiant);
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
        // Prepare mock data
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        List<Etudiant> etudiantsList = new ArrayList<>(); // Create an ArrayList of Etudiants
        etudiantsList.add(etudiant1);
        etudiantsList.add(etudiant2);

        // Simulate repository behavior
        when(etudiantRepository.findByContrats_ArchiveFalse()).thenReturn(etudiantsList); // Return as ArrayList

        // Call the service method
        List<Etudiant> result = etudiantService.getEtudiantsWithActiveContrats();

        // Validate
        assertEquals(2, result.size(), "Le nombre d'Etudiants avec des contrats actifs devrait être 2.");
        verify(etudiantRepository).findByContrats_ArchiveFalse(); // Verify that the repository was called
    }



    @Test
    void isEtudiantInEquipe() {
        Integer etudiantId = 1;
        Integer equipeId = 1;
        Etudiant etudiant = new Etudiant();
        Equipe equipe = new Equipe();
        equipe.setEtudiants(Set.of(etudiant));  // Use Set.of instead of List.of

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));
        when(equipeRepository.findById(equipeId)).thenReturn(Optional.of(equipe));

        boolean result = etudiantService.isEtudiantInEquipe(etudiantId, equipeId);

        assertTrue(result);
        verify(equipeRepository, times(1)).findById(equipeId);
    }

}
