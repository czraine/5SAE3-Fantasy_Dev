package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EquipeServiceImplMockito {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        Equipe savedEquipe = equipeService.addEquipe(equipe);

        assertEquals("Equipe A", savedEquipe.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testRetrieveAllEquipes() {
        Equipe equipe1 = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        Equipe equipe2 = new Equipe(2, "Equipe B", Niveau.SENIOR);

        when(equipeRepository.findAll()).thenReturn(Arrays.asList(equipe1, equipe2));

        assertEquals(2, equipeService.retrieveAllEquipes().size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void testDeleteEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);

        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));
        doNothing().when(equipeRepository).delete(equipe);

        equipeService.deleteEquipe(1);

        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
    void testRetrieveEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);

        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        Equipe retrievedEquipe = equipeService.retrieveEquipe(1);

        assertEquals("Equipe A", retrievedEquipe.getNomEquipe());
        verify(equipeRepository, times(1)).findById(1);
    }
}
