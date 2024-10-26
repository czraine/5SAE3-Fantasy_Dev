package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	IUniversiteService universiteService;

	@GetMapping("/retrieve-all-universites")
	public List<Universite> getUniversites() {
		return universiteService.retrieveAllUniversites();
	}

	@GetMapping("/retrieve-universite/{universite-id}")
	public Universite retrieveUniversite(@PathVariable("universite-id") Integer universiteId) {
		return universiteService.retrieveUniversite(universiteId);
	}

	@PostMapping("/add-universite")
	public Universite addUniversite(@RequestBody Universite u) {
		return universiteService.addUniversite(u);
	}

	@DeleteMapping("/remove-universite/{universite-id}")
	public void removeUniversite(@PathVariable("universite-id") Integer universiteId) {
		universiteService.deleteUniversite(universiteId);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@PutMapping("/update-universite")
	public Universite updateUniversite(@RequestBody Universite u) {
		return universiteService.updateUniversite(u);
	}

	@PutMapping("/affecter-universite-departement/{universiteId}/{departementId}")
	public void affecterUniversiteToDepartement(@PathVariable("universiteId") Integer universiteId, @PathVariable("departementId") Integer departementId) {
		universiteService.assignUniversiteToDepartement(universiteId, departementId);
	}

	@GetMapping("/listerDepartementsUniversite/{idUniversite}")
	public Set<Departement> listerDepartementsUniversite(@PathVariable("idUniversite") Integer idUniversite) {
		return universiteService.retrieveDepartementsByUniversite(idUniversite);
	}

	@GetMapping("/count-departements/{universite-id}")
	public int countDepartementsInUniversite(@PathVariable("universite-id") Integer universiteId) {
		return universiteService.countDepartementsInUniversite(universiteId);
	}


	@GetMapping("/find-departements-criteria/{universite-id}")
	public List<Departement> findDepartementsByCriteria(@PathVariable("universite-id") Integer universiteId,
														@RequestParam String departementName,
														@RequestParam int minDepartements) {
		return universiteService.findDepartementsByCriteria(universiteId, departementName, minDepartements);
	}

	@DeleteMapping("/remove-departements/{universite-id}")
	public void removeDepartementsFromUniversite(@PathVariable("universite-id") Integer universiteId,
												 @RequestBody List<Integer> departementIds) {
		universiteService.removeDepartementsFromUniversite(universiteId, departementIds);
	}

	@PutMapping("/add-departements/{universite-id}")
	public void addMultipleDepartementsToUniversite(@PathVariable("universite-id") Integer universiteId,
													@RequestBody List<Integer> departementIds) {
		universiteService.addMultipleDepartementsToUniversite(universiteId, departementIds);
	}

	@GetMapping("/search-universities")
	public List<Universite> searchUniversities(@RequestParam String nomUniv, @RequestParam int minDepartements) {
		return universiteService.searchUniversities(nomUniv, minDepartements);
	}
}
