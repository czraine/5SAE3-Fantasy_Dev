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
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UniversiteRestControllerJUnitTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UniversiteRestController universiteRestController;

    @Mock
    private IUniversiteService universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(universiteRestController).build();
    }

    @Test
    void testAddUniversite() throws Exception {
        Universite universite = new Universite();
        universite.setNomUniv("Université JUnit Test");

        when(universiteService.addUniversite(any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/universite/add-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniv").value("Université JUnit Test"));

        verify(universiteService, times(1)).addUniversite(any(Universite.class));
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
    void testSearchUniversities() throws Exception {
        Universite universite1 = new Universite();
        universite1.setNomUniv("Université Paris");
        universite1.setDepartements(new HashSet<>(Arrays.asList(new Departement())));

        Universite universite2 = new Universite();
        universite2.setNomUniv("Université Lyon");
        universite2.setDepartements(new HashSet<>(Arrays.asList(new Departement(), new Departement())));

        when(universiteService.searchUniversities(anyString(), anyInt())).thenReturn(Arrays.asList(universite2));

        mockMvc.perform(get("/universite/search-universities")
                        .param("nomUniv", "Université")
                        .param("minDepartements", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniv").value("Université Lyon"));
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
                .andExpect(status().isOk());

        verify(universiteService, times(1)).removeDepartementsFromUniversite(eq(1), anyList());
    }


    @Test
    void testFindDepartementsByCriteria() throws Exception {
        Departement departement1 = new Departement();
        departement1.setNomDepart("Informatique");

        List<Departement> departements = Arrays.asList(departement1);

        when(universiteService.findDepartementsByCriteria(anyInt(), anyString(), anyInt())).thenReturn(departements);

        mockMvc.perform(get("/universite/find-departements-criteria/{universite-id}", 1)
                        .param("departementName", "Informatique")
                        .param("minDepartements", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDepart").value("Informatique"));
    }
}

