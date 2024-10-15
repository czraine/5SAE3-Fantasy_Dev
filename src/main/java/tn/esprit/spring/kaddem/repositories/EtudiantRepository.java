package tn.esprit.spring.kaddem.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.kaddem.entities.Etudiant;

import java.util.List;

@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant, Integer> {
    public List<Etudiant> findEtudiantsByDepartement_IdDepart(Integer idDepart);

    @Query("Select e From Etudiant e where e.nomE = :nomE and e.prenomE = :prenomE")
    public Etudiant findByNomEAndPrenomE(@Param("nomE") String nomE, @Param("prenomE") String prenomE);

    // Updated method signature
    List<Etudiant> findByNomEContainingOrPrenomEContaining(String nom, String prenom);

    // Méthode pour récupérer les étudiants ayant un contrat actif
    @Query("SELECT e FROM Etudiant e JOIN e.Contrats c WHERE c.archive = true")
    List<Etudiant> findEtudiantsWithActiveContracts();
}
