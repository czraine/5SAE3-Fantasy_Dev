package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Charger le contexte Spring pour les tests
@ActiveProfiles("test") // Utilise le fichier application-test.properties pour configurer MySQL
class UniversiteServiceImplTest {

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private UniversiteServiceImpl universiteServiceImpl;

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données de test avant chaque test pour éviter des conflits
        universiteRepository.deleteAll();
    }

    @Test
    void testAddUniversite() {
        // Given: créer une instance de Universite
        Universite universite = new Universite();
        universite.setNomUniv("Université MySQL Test");

        // When: appel de la méthode addUniversite
        Universite result = universiteServiceImpl.addUniversite(universite);

        // Then: vérifier que l'université est bien enregistrée
        assertNotNull(result);
        assertEquals("Université MySQL Test", result.getNomUniv());
        assertNotNull(result.getIdUniv()); // Vérifier que l'ID est généré
    }

    @Test
    void testRetrieveAllUniversites() {
        // Given: ajouter plusieurs universités dans la base de données
        Universite universite1 = new Universite();
        universite1.setNomUniv("Université 1");

        Universite universite2 = new Universite();
        universite2.setNomUniv("Université 2");

        universiteRepository.save(universite1);
        universiteRepository.save(universite2);

        // When: appel de la méthode retrieveAllUniversites
        var universites = universiteServiceImpl.retrieveAllUniversites();

        // Then: vérifier que les universités sont bien récupérées
        assertEquals(2, universites.size());
    }

    @Test
    void testRetrieveUniversite() {
        // Given: ajouter une université dans la base de données
        Universite universite = new Universite();
        universite.setNomUniv("Université à récupérer");
        Universite savedUniversite = universiteRepository.save(universite);

        // When: appel de la méthode retrieveUniversite
        Universite foundUniversite = universiteServiceImpl.retrieveUniversite(savedUniversite.getIdUniv());

        // Then: vérifier que l'université récupérée est correcte
        assertNotNull(foundUniversite);
        assertEquals(savedUniversite.getNomUniv(), foundUniversite.getNomUniv());
    }

    @Test
    void testUpdateUniversite() {
        // Given: ajouter une université dans la base de données
        Universite universite = new Universite();
        universite.setNomUniv("Université à mettre à jour");
        Universite savedUniversite = universiteRepository.save(universite);

        // When: modifier l'université et appeler updateUniversite
        savedUniversite.setNomUniv("Université mise à jour");
        Universite updatedUniversite = universiteServiceImpl.updateUniversite(savedUniversite);

        // Then: vérifier que l'université a bien été mise à jour
        assertEquals("Université mise à jour", updatedUniversite.getNomUniv());
    }

    @Test
    void testDeleteUniversite() {
        // Given: ajouter une université dans la base de données
        Universite universite = new Universite();
        universite.setNomUniv("Université à supprimer");
        Universite savedUniversite = universiteRepository.save(universite);

        // When: appel de la méthode deleteUniversite
        universiteServiceImpl.deleteUniversite(savedUniversite.getIdUniv());

        // Then: vérifier que l'université a bien été supprimée
        assertFalse(universiteRepository.findById(savedUniversite.getIdUniv()).isPresent());
    }

    @Test
    void testCountDepartementsInUniversite() {
        // Given: créer une université avec deux départements
        Universite universite = new Universite();
        universite.setNomUniv("Université Test");

        Departement departement1 = new Departement();
        departement1.setNomDepart("Informatique");

        Departement departement2 = new Departement();
        departement2.setNomDepart("Mathématiques");

        Set<Departement> departements = new HashSet<>();
        departements.add(departement1);
        departements.add(departement2);
        universite.setDepartements(departements);

        universiteRepository.save(universite); // Sauvegarder l'université dans la base de données

        // When: appel de la méthode
        int nombreDepartements = universiteServiceImpl.countDepartementsInUniversite(universite.getIdUniv());

        // Then: vérifier que le nombre de départements est correct
        assertEquals(2, nombreDepartements); // Il devrait y avoir 2 départements
    }
}
