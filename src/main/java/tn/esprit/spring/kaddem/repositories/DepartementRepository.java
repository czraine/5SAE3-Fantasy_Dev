package tn.esprit.spring.kaddem.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.kaddem.entities.Departement;

import java.util.List;

@Repository
public interface DepartementRepository extends CrudRepository<Departement,Integer> {

    // Custom query to find departments with names containing a specific string
    @Query("SELECT d FROM Departement d WHERE LOWER(d.nomDepart) LIKE LOWER(CONCAT('%', :namePart, '%'))")
    List<Departement> findByNameContaining(@Param("namePart") String namePart);

}
