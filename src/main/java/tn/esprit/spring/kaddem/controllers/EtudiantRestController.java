package tn.esprit.spring.kaddem.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Option;
import tn.esprit.spring.kaddem.services.IEtudiantService;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/etudiant")
public class EtudiantRestController {
	private static final Logger log = LoggerFactory.getLogger(EtudiantRestController.class);
	@Autowired
	IEtudiantService etudiantService;
	// http://localhost:8089/Kaddem/etudiant/retrieve-all-etudiants
	@GetMapping("/retrieve-all-etudiants")
	public List<Etudiant> getEtudiants() {
		List<Etudiant> listEtudiants = etudiantService.retrieveAllEtudiants();
		return listEtudiants;
	}
	// http://localhost:8089/Kaddem/etudiant/retrieve-etudiant/8
	@GetMapping("/retrieve-etudiant/{etudiant-id}")
	public Etudiant retrieveEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
		return etudiantService.retrieveEtudiant(etudiantId);
	}

	// http://localhost:8089/Kaddem/etudiant/add-etudiant
/*	@PostMapping("/add-etudiant")
	public Etudiant addEtudiant(@RequestBody Etudiant e) {
		Etudiant etudiant = etudiantService.addEtudiant(e);
		return etudiant;
	}*/
	@PostMapping("/add-etudiant")
	public Etudiant addEtudiant(@RequestBody Map<String, Object> payload) {
		// Create an Etudiant from the received data
		Etudiant etudiant = new Etudiant();
		etudiant.setNom((String) payload.get("nom"));
		etudiant.setPrenom((String) payload.get("prenom"));
		etudiant.setOption(Option.valueOf((String) payload.get("option"))); // Adjust according to your Option enum

		// Get the departementId
		Integer departementId = (Integer) payload.get("departementId");

		// Call the service to add the Etudiant and assign to Departement
		Etudiant addedEtudiant = etudiantService.addEtudiant(etudiant, departementId);

		return addedEtudiant;
	}


	// http://localhost:8089/Kaddem/etudiant/remove-etudiant/1
	@DeleteMapping("/remove-etudiant/{etudiant-id}")
	public void removeEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
		etudiantService.removeEtudiant(etudiantId);
	}

	// http://localhost:8089/Kaddem/etudiant/update-etudiant
	/*@PutMapping("/update-etudiant")
	public ResponseEntity<Etudiant> updateEtudiant(@RequestBody Etudiant e) {
		try {
			Etudiant updatedEtudiant = etudiantService.updateEtudiant(e);
			return ResponseEntity.ok(updatedEtudiant); // Return 200 OK with the updated student
		} catch (EntityNotFoundException ex) {
			// Handle the case where the student does not exist
			log.error("Error updating student: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 Not Found
		} catch (Exception ex) {
			// Handle other potential exceptions
			log.error("Unexpected error occurred while updating student: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 Internal Server Error
		}
	}*/


	//@PutMapping("/affecter-etudiant-departement")
	@PutMapping(value="/affecter-etudiant-departement/{etudiantId}/{departementId}")
	public void affecterEtudiantToDepartement(@PathVariable("etudiantId") Integer etudiantId, @PathVariable("departementId")Integer departementId){
		etudiantService.assignEtudiantToDepartement(etudiantId, departementId);
    }
//addAndAssignEtudiantToEquipeAndContract(Etudiant e, Integer idContrat, Integer idEquipe)
    /* Ajouter un étudiant tout en lui affectant un contrat et une équipe */
    @PostMapping("/add-assign-Etudiant/{idContrat}/{idEquipe}")
    @ResponseBody
    public Etudiant addEtudiantWithEquipeAndContract(@RequestBody Etudiant e, @PathVariable("idContrat") Integer idContrat, @PathVariable("idEquipe") Integer idEquipe) {
        Etudiant etudiant = etudiantService.addAndAssignEtudiantToEquipeAndContract(e,idContrat,idEquipe);
        return etudiant;
    }

	@GetMapping(value = "/getEtudiantsByDepartement/{idDepartement}")
	public List<Etudiant> getEtudiantsParDepartement(@PathVariable("idDepartement") Integer idDepartement) {

		return etudiantService.getEtudiantsByDepartement(idDepartement);
	}

}


