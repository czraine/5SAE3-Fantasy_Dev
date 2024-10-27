package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class EquipeServiceImplTest {

    private EquipeServiceImpl equipeService;
    private EquipeRepository equipeRepository;

    @BeforeEach
    void setUp() {
        equipeRepository = new EquipeRepository() {
            List<Equipe> equipes = new ArrayList<>();

            @Override
            public List<Equipe> findAll() {
                return equipes;
            }

            @Override
            public Iterable<Equipe> findAllById(Iterable<Integer> integers) {
                return null;
            }

            @Override
            public long count() {
return 0 ;            }

            @Override
            public void deleteById(Integer integer) {
                System.out.println("done");

            }


            @Override
            public <S extends Equipe> S save(S s) {
                equipes.add(s);
                return s;
            }

            @Override
            public <S extends Equipe> Iterable<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void delete(Equipe equipe) {
                equipes.remove(equipe);
            }

            @Override
            public void deleteAllById(Iterable<? extends Integer> integers) {
                System.out.println("done");

            }

            @Override
            public void deleteAll(Iterable<? extends Equipe> entities) {
                System.out.println("done");

            }

            @Override
            public void deleteAll() {
                System.out.println("done");

            }


            @Override
            public java.util.Optional<Equipe> findById(Integer id) {
                return equipes.stream().filter(e -> e.getIdEquipe().equals(id)).findFirst();
            }

            @Override
            public boolean existsById(Integer integer) {
                return false;
            }
        };

        equipeService = new EquipeServiceImpl(equipeRepository);
    }

    @Test
    void testAddEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        Equipe savedEquipe = equipeService.addEquipe(equipe);
        assertNotNull(savedEquipe);
        assertEquals("Equipe A", savedEquipe.getNomEquipe());
    }

    @Test
    void testRetrieveAllEquipes() {
        Equipe equipe1 = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        Equipe equipe2 = new Equipe(2, "Equipe B", Niveau.SENIOR);
        equipeService.addEquipe(equipe1);
        equipeService.addEquipe(equipe2);
        List<Equipe> equipes = equipeService.retrieveAllEquipes();
        assertEquals(2, equipes.size());
    }

    @Test
    void testDeleteEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        equipeService.addEquipe(equipe);
        equipeService.deleteEquipe(1);
        assertEquals(0, equipeService.retrieveAllEquipes().size());
    }
}
