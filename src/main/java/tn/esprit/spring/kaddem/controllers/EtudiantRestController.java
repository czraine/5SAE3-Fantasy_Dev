package tn.esprit.spring.kaddem.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	// Retrieve all students
	@GetMapping("/retrieve-all-etudiants")
	public ResponseEntity<List<Etudiant>> getEtudiants() {
		log.info("Fetching all students.");
		List<Etudiant> listEtudiants = etudiantService.retrieveAllEtudiants();
		return new ResponseEntity<>(listEtudiants, HttpStatus.OK);
	}

	// Retrieve a student by ID
	@GetMapping("/retrieve-etudiant/{etudiant-id}")
	public ResponseEntity<Etudiant> retrieveEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
		log.info("Fetching student with ID: {}", etudiantId);
		Etudiant etudiant = etudiantService.retrieveEtudiant(etudiantId);
		if (etudiant != null) {
			return new ResponseEntity<>(etudiant, HttpStatus.OK);
		} else {
			log.warn("Student with ID {} not found.", etudiantId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Add a new student and assign to a department
	@PostMapping("/add-etudiant")
	public ResponseEntity<Etudiant> addEtudiant(@RequestBody Map<String, Object> payload) {
		try {
			Etudiant etudiant = new Etudiant();
			etudiant.setNom((String) payload.get("nom"));
			etudiant.setPrenom((String) payload.get("prenom"));
			etudiant.setOption(Option.valueOf((String) payload.get("option")));

			Integer departementId = (Integer) payload.get("departementId");
			Etudiant addedEtudiant = etudiantService.addEtudiant(etudiant, departementId);
			log.info("Added student {} to department {}", addedEtudiant.getIdEtudiant(), departementId);
			return new ResponseEntity<>(addedEtudiant, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error adding student: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Remove a student by ID
	@DeleteMapping("/remove-etudiant/{etudiant-id}")
	public ResponseEntity<Void> removeEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
		log.info("Removing student with ID: {}", etudiantId);
		try {
			etudiantService.removeEtudiant(etudiantId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (EntityNotFoundException e) {
			log.warn("Student with ID {} not found for removal.", etudiantId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Update an existing student
	@PutMapping("/update-etudiant")
	public ResponseEntity<Etudiant> updateEtudiant(@RequestBody Etudiant e) {
		try {
			Etudiant updatedEtudiant = etudiantService.updateEtudiant(e);
			log.info("Updated student with ID: {}", updatedEtudiant.getIdEtudiant());
			return ResponseEntity.ok(updatedEtudiant);
		} catch (EntityNotFoundException ex) {
			log.warn("Error updating student: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception ex) {
			log.error("Unexpected error occurred while updating student: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Assign a student to a department
	@PutMapping(value="/affecter-etudiant-departement/{etudiantId}/{departementId}")
	public ResponseEntity<Void> affecterEtudiantToDepartement(@PathVariable("etudiantId") Integer etudiantId, @PathVariable("departementId") Integer departementId) {
		etudiantService.assignEtudiantToDepartement(etudiantId, departementId);
		log.info("Assigned student {} to department {}", etudiantId, departementId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Add and assign a student to a team and a contract
	@PostMapping("/add-assign-Etudiant/{idContrat}/{idEquipe}")
	public ResponseEntity<Etudiant> addEtudiantWithEquipeAndContract(@RequestBody Etudiant e, @PathVariable("idContrat") Integer idContrat, @PathVariable("idEquipe") Integer idEquipe) {
		Etudiant etudiant = etudiantService.addAndAssignEtudiantToEquipeAndContract(e, idContrat, idEquipe);
		log.info("Added student with contract {} and team {}", idContrat, idEquipe);
		return new ResponseEntity<>(etudiant, HttpStatus.CREATED);
	}

	// Get students by department
	@GetMapping(value = "/getEtudiantsByDepartement/{idDepartement}")
	public ResponseEntity<List<Etudiant>> getEtudiantsParDepartement(@PathVariable("idDepartement") Integer idDepartement) {
		log.info("Fetching students for department {}", idDepartement);
		List<Etudiant> etudiants = etudiantService.getEtudiantsByDepartement(idDepartement);
		return new ResponseEntity<>(etudiants, HttpStatus.OK);
	}
}
