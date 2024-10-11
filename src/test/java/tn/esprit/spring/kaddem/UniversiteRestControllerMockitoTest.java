package tn.esprit.spring.kaddem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.kaddem.controllers.UniversiteRestController;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.services.IUniversiteService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UniversiteRestControllerMockitoTest {

    private MockMvc mockMvc;

    @Mock
    private IUniversiteService universiteService;

    @InjectMocks
    private UniversiteRestController universiteRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(universiteRestController).build();
    }

    @Test
    void testAddUniversite() throws Exception {
        // Créer un objet Universite
        Universite universite = new Universite();
        universite.setNomUniv("Université Mockito Test");

        // Simuler la méthode du service
        when(universiteService.addUniversite(any(Universite.class))).thenReturn(universite);

        // Effectuer une requête POST
        mockMvc.perform(post("/universite/add-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniv").value("Université Mockito Test"));

        // Vérifier que la méthode du service a été appelée
        verify(universiteService, times(1)).addUniversite(any(Universite.class));
    }

    @Test
    void testRetrieveAllUniversites() throws Exception {
        // Créer une liste d'universités factices
        Universite universite1 = new Universite();
        universite1.setNomUniv("Université 1");

        Universite universite2 = new Universite();
        universite2.setNomUniv("Université 2");

        List<Universite> universites = Arrays.asList(universite1, universite2);

        // Simuler la méthode du service
        when(universiteService.retrieveAllUniversites()).thenReturn(universites);

        // Effectuer une requête GET
        mockMvc.perform(get("/universite/retrieve-all-universites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniv").value("Université 1"))
                .andExpect(jsonPath("$[1].nomUniv").value("Université 2"));

        // Vérifier que la méthode du service a été appelée
        verify(universiteService, times(1)).retrieveAllUniversites();
    }

    @Test
    void testDeleteUniversite() throws Exception {
        // Effectuer une requête DELETE
        mockMvc.perform(delete("/universite/remove-universite/{universite-id}", 1))
                .andExpect(status().isOk());

        // Vérifier que la méthode du service a été appelée
        verify(universiteService, times(1)).deleteUniversite(1);
    }

    @Test
    void testCountDepartementsInUniversite() throws Exception {
        // Simuler que le service retourne 5 départements pour l'université avec ID 2
        when(universiteService.countDepartementsInUniversite(2)).thenReturn(5);

        // Effectuer une requête GET
        mockMvc.perform(get("/universite/count-departements/{universite-id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifier que le statut HTTP est 200
                .andExpect(content().string("5")); // Vérifier que le contenu est "5"

        // Vérifier que la méthode du service a été appelée une seule fois
        verify(universiteService, times(1)).countDepartementsInUniversite(2);
    }
}
