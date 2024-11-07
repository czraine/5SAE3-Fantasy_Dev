package tn.esprit.spring.kaddem.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.kaddem.entities.Etudiant;

import java.util.List;
import java.util.Set;
@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant, Integer> {
    List<Etudiant> findEtudiantsByDepartement_IdDepart(Integer idDepart);
    List<Etudiant> findByContrats_ArchiveFalse();
  /*  @Query("SELECT e FROM Etudiant e JOIN e.contrats c WHERE c.active = true")
    List<Etudiant> findEtudiantsWithActiveContracts();*/
    @Query("Select e From Etudiant e where e.nom = :nomE and e.prenom = :prenomE")
    List<Etudiant> findByNomAndPrenom(@Param("nomE") String nomE, @Param("prenomE") String prenomE);

    List<Etudiant> findByNomContainingOrPrenomContaining(String nom, String prenom);

}
