package tn.esprit.spring.kaddem;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.kaddem.controllers.UniversiteRestController;
import tn.esprit.spring.kaddem.services.IUniversiteService;

import java.util.NoSuchElementException;

class UniversiteRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUniversiteService universiteService;

    @InjectMocks
    private UniversiteRestController universiteRestController;

    @BeforeEach
    void setUp() {
        // Initialise les mocks
        MockitoAnnotations.openMocks(this);
        // Configure MockMvc pour simuler le controller
        mockMvc = MockMvcBuilders.standaloneSetup(universiteRestController).build();
    }

    @Test
    void testDeleteUniversite_NotFound() throws Exception {
        // Simuler l'exception pour un ID non existant
        doThrow(new NoSuchElementException("Aucune université trouvée avec l'ID 1"))
                .when(universiteService).deleteUniversite(1);

        // Effectuer la requête DELETE et vérifier la réponse
        mockMvc.perform(delete("/universite/remove-universite/{universite-id}", 1))
                .andExpect(status().isNotFound())  // Attente d'un statut HTTP 404
                .andExpect(content().string("Aucune université trouvée avec l'ID 1"));
    }
    @Test
    void testCountDepartementsInUniversite() throws Exception {
        // Simulate that the service returns 3 departments for the university with ID 1
        when(universiteService.countDepartementsInUniversite(1)).thenReturn(3);

        // Perform a GET request to /universite/count-departements/1
        mockMvc.perform(get("/universite/count-departements/{universite-id}", 1))
                .andExpect(status().isOk()) // Check that the HTTP status is 200
                .andExpect(content().string("3")); // Check that the response content is "3"

        // Verify that the service method was called exactly once
        verify(universiteService, times(1)).countDepartementsInUniversite(1);
    }
}
