package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.services.IUniversiteService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/universite")
public class UniversiteRestController {

	private final IUniversiteService universiteService;

	@GetMapping("/retrieve-all-universites")
	public ResponseEntity<List<Universite>> getUniversites() {
		return ResponseEntity.ok(universiteService.retrieveAllUniversites());
	}

	@GetMapping("/retrieve-universite/{universiteId}")
	public ResponseEntity<Universite> retrieveUniversite(@PathVariable("universiteId") Integer universiteId) {
		return ResponseEntity.ok(universiteService.retrieveUniversite(universiteId));
	}

	@PostMapping("/add-universite")
	public ResponseEntity<Universite> addUniversite(@RequestBody Universite u) {
		return ResponseEntity.status(HttpStatus.CREATED).body(universiteService.addUniversite(u));
	}

	@DeleteMapping("/remove-universite/{universiteId}")
	public ResponseEntity<Void> removeUniversite(@PathVariable("universiteId") Integer universiteId) {
		universiteService.deleteUniversite(universiteId);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@PutMapping("/update-universite")
	public ResponseEntity<Universite> updateUniversite(@RequestBody Universite u) {
		return ResponseEntity.ok(universiteService.updateUniversite(u));
	}

	@PutMapping("/affecter-universite-departement/{universiteId}/{departementId}")
	public ResponseEntity<Void> affecterUniversiteToDepartement(
			@PathVariable Integer universiteId,
			@PathVariable Integer departementId) {
		universiteService.assignUniversiteToDepartement(universiteId, departementId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/lister-departements-universite/{idUniversite}")
	public ResponseEntity<Set<Departement>> listerDepartementsUniversite(@PathVariable Integer idUniversite) {
		return ResponseEntity.ok(universiteService.retrieveDepartementsByUniversite(idUniversite));
	}

	@GetMapping("/count-departements/{universiteId}")
	public ResponseEntity<Integer> countDepartementsInUniversite(@PathVariable Integer universiteId) {
		return ResponseEntity.ok(universiteService.countDepartementsInUniversite(universiteId));
	}

	@GetMapping("/find-departements-criteria/{universiteId}")
	public ResponseEntity<List<Departement>> findDepartementsByCriteria(
			@PathVariable Integer universiteId,
			@RequestParam String departementName,
			@RequestParam int minDepartements) {
		return ResponseEntity.ok(universiteService.findDepartementsByCriteria(universiteId, departementName, minDepartements));
	}

	@DeleteMapping("/remove-departements/{universiteId}")
	public ResponseEntity<Void> removeDepartementsFromUniversite(
			@PathVariable Integer universiteId,
			@RequestBody List<Integer> departementIds) {
		universiteService.removeDepartementsFromUniversite(universiteId, departementIds);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/add-departements/{universiteId}")
	public ResponseEntity<Void> addMultipleDepartementsToUniversite(
			@PathVariable Integer universiteId,
			@RequestBody List<Integer> departementIds) {
		universiteService.addMultipleDepartementsToUniversite(universiteId, departementIds);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/search-universities")
	public ResponseEntity<List<Universite>> searchUniversities(
			@RequestParam String nomUniv,
			@RequestParam int minDepartements) {
		return ResponseEntity.ok(universiteService.searchUniversities(nomUniv, minDepartements));
	}
}
