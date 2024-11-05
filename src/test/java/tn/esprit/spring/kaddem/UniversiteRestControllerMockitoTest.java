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
import tn.esprit.spring.kaddem.entities.Departement;
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
    void testRetrieveAllUniversites() throws Exception {
        Universite universite1 = new Universite();
        universite1.setNomUniv("Université 1");

        Universite universite2 = new Universite();
        universite2.setNomUniv("Université 2");

        List<Universite> universites = Arrays.asList(universite1, universite2);

        when(universiteService.retrieveAllUniversites()).thenReturn(universites);

        mockMvc.perform(get("/universite/retrieve-all-universites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniv").value("Université 1"))
                .andExpect(jsonPath("$[1].nomUniv").value("Université 2"));

        verify(universiteService, times(1)).retrieveAllUniversites();
    }

    @Test
    void testRetrieveUniversite() throws Exception {
        Universite universite = new Universite(1, "Université 1");

        when(universiteService.retrieveUniversite(1)).thenReturn(universite);

        mockMvc.perform(get("/universite/retrieve-universite/{universite-id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniv").value("Université 1"));

        verify(universiteService, times(1)).retrieveUniversite(1);
    }

    @Test
    void testAddUniversite() throws Exception {
        Universite universite = new Universite();
        universite.setNomUniv("Université Mockito Test");

        when(universiteService.addUniversite(any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/universite/add-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(universite)))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.nomUniv").value("Université Mockito Test"));

        verify(universiteService, times(1)).addUniversite(any(Universite.class));
    }


    @Test
    void testUpdateUniversite() throws Exception {
        Universite universite = new Universite(1, "Université Modifiée");

        when(universiteService.updateUniversite(any(Universite.class))).thenReturn(universite);

        mockMvc.perform(put("/universite/update-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniv").value("Université Modifiée"));

        verify(universiteService, times(1)).updateUniversite(any(Universite.class));
    }

    @Test
    void testDeleteUniversite() throws Exception {
        mockMvc.perform(delete("/universite/remove-universite/{universite-id}", 1))
                .andExpect(status().isNoContent());

        verify(universiteService, times(1)).deleteUniversite(1);
    }


    @Test
    void testFindDepartementsByCriteria() throws Exception {
        Departement departement1 = new Departement();
        departement1.setNomDepart("Informatique");

        List<Departement> departements = Arrays.asList(departement1);

        when(universiteService.findDepartementsByCriteria(anyInt(), anyString(), anyInt()))
                .thenReturn(departements);

        mockMvc.perform(get("/universite/find-departements-criteria/{universite-id}", 1)
                        .param("departementName", "Informatique")
                        .param("minDepartements", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDepart").value("Informatique"));
    }

    @Test
    void testSearchUniversities() throws Exception {
        Universite universite = new Universite();
        universite.setNomUniv("Université Paris");

        List<Universite> universites = Arrays.asList(universite);

        when(universiteService.searchUniversities(anyString(), anyInt())).thenReturn(universites);

        mockMvc.perform(get("/universite/search-universities")
                        .param("nomUniv", "Paris")
                        .param("minDepartements", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniv").value("Université Paris"));
    }

    @Test
    void testAddMultipleDepartementsToUniversite() throws Exception {
        mockMvc.perform(put("/universite/add-departements/{universite-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Arrays.asList(1, 2))))
                .andExpect(status().isOk());

        verify(universiteService, times(1)).addMultipleDepartementsToUniversite(eq(1), anyList());
    }

    @Test
    void testRemoveDepartementsFromUniversite() throws Exception {
        mockMvc.perform(delete("/universite/remove-departements/{universite-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Arrays.asList(1, 2))))
                .andExpect(status().isNoContent());

        verify(universiteService, times(1)).removeDepartementsFromUniversite(eq(1), anyList());
    }

}
