package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.*;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class EquipeServiceImplMockito {
    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNom("John");
        etudiant.setPrenom("Doe");
        etudiant.setOption(Option.GAMIX);
    }

    @Test
    void retrieveAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getNom());
        assertEquals(Option.GAMIX, result.get(0).getOption());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void addEtudiant() {
        // Arrange
        Etudiant etudiantToAdd = new Etudiant("John", "Doe", Option.GAMIX);
        Integer departementId = 1; // Mock department ID
        Departement departement = new Departement();
        departement.setIdDepart(departementId);
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));
        when(etudiantRepository.save(etudiantToAdd)).thenReturn(etudiantToAdd);

        // Act
        Etudiant result = etudiantService.addEtudiant(etudiantToAdd, departementId);

        // Assert
        assertNotNull(result);
        assertEquals(etudiantToAdd.getNom(), result.getNom());
        assertEquals(etudiantToAdd.getPrenom(), result.getPrenom());
        assertEquals(Option.GAMIX, result.getOption());
        assertEquals(departement, result.getDepartement());
        verify(departementRepository, times(1)).findById(departementId);
        verify(etudiantRepository, times(1)).save(etudiantToAdd);
    }

    @Test
    void updateEtudiant() {
        // Given
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNom("John");
        etudiant.setPrenom("Doe");
        // Set other properties as needed

        // Mocking the findById method to return an Optional containing the etudiant
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

        // Mocking the save method to return the updated etudiant
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // When
        Etudiant result = etudiantService.updateEtudiant(etudiant);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getNom());
        assertEquals("Doe", result.getPrenom());
        assertEquals(1, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(1); // Verify findById was called
        verify(etudiantRepository, times(1)).save(etudiant); // Verify save was called
    }


    @Test
    void retrieveEtudiant() {
        when(etudiantRepository.findById(anyInt())).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.retrieveEtudiant(1);

        assertNotNull(result);
        assertEquals("John", result.getNom());
        assertEquals(Option.GAMIX, result.getOption());
        verify(etudiantRepository, times(1)).findById(1);
    }

    @Test
    void removeEtudiant() {
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        doNothing().when(etudiantRepository).delete(any(Etudiant.class));

        etudiantService.removeEtudiant(1);

        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    void assignEtudiantToDepartement() {
        Integer departementId = 1;
        Departement departement = new Departement();
        departement.setIdDepart(departementId);

        when(etudiantRepository.findById(anyInt())).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));

        etudiantService.assignEtudiantToDepartement(etudiant.getIdEtudiant(), departementId);

        assertEquals(departement, etudiant.getDepartement());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void addAndAssignEtudiantToEquipeAndContract() {
        Equipe equipe = new Equipe();
        equipe.setIdEquipe(1);

        Contrat contrat = new Contrat();
        contrat.setIdContrat(1L);

        when(contratRepository.findById(1L)).thenReturn(Optional.of(contrat));
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract(etudiant, 1L, 1);

        assertNotNull(result);
        assertEquals(etudiant, contrat.getEtudiant());
        assertTrue(equipe.getEtudiants().contains(etudiant));
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void findEtudiantsByNomOrPrenom() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);

        when(etudiantRepository.findByNomContainingOrPrenomContaining("John", "John")).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findEtudiantsByNomOrPrenom("John");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getNom());
        verify(etudiantRepository, times(1)).findByNomContainingOrPrenomContaining("John", "John");
    }

    @Test
    void getEtudiantsWithActiveContrats() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);

        when(etudiantRepository.findByContrats_ArchiveFalse()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.getEtudiantsWithActiveContrats();

        assertEquals(1, result.size());
        verify(etudiantRepository, times(1)).findByContrats_ArchiveFalse();
    }
}