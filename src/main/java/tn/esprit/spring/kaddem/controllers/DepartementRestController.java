package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/departement")
public class DepartementRestController {

	private final IDepartementService departementService;

	// http://localhost:8089/Kaddem/departement/retrieve-all-departements
	@GetMapping("/retrieve-all-departements")
	public ResponseEntity<List<Departement>> getDepartements() {
		List<Departement> listDepartements = departementService.retrieveAllDepartements();
		return ResponseEntity.ok(listDepartements);
	}

	// http://localhost:8089/Kaddem/departement/retrieve-departement/8
	@GetMapping("/retrieve-departement/{departement-id}")
	public ResponseEntity<Departement> retrieveDepartement(@PathVariable("departement-id") Integer departementId) {
		Departement departement = departementService.retrieveDepartement(departementId);
		return ResponseEntity.ok(departement);
	}

	// http://localhost:8089/Kaddem/departement/add-departement
	@PostMapping("/add-departement")
	public ResponseEntity<Departement> addDepartement(@RequestBody Departement d) {
		Departement departement = departementService.addDepartement(d);
		return ResponseEntity.status(HttpStatus.CREATED).body(departement);
	}

	// http://localhost:8089/Kaddem/departement/remove-departement/1
	@DeleteMapping("/remove-departement/{departement-id}")
	public ResponseEntity<Void> removeDepartement(@PathVariable("departement-id") Integer departementId) {
		departementService.deleteDepartement(departementId);
		return ResponseEntity.noContent().build();
	}

	// http://localhost:8089/Kaddem/departement/update-departement/{departement-id}
	@PutMapping("/update-departement/{departement-id}")
	public ResponseEntity<Departement> updateDepartement(
			@PathVariable("departement-id") Integer departementId,
			@RequestBody Departement e) {
		// Update the Departement with the provided ID
		Departement updatedDepartement = departementService.updateDepartement(departementId, e);
		return ResponseEntity.ok(updatedDepartement);
	}
}
