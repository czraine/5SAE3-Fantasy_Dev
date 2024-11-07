package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.services.IContratService;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/contrat")
public class ContratRestController {
	IContratService contratService;
	@GetMapping("/retrieve-all-contrats")
	public List<Contrat> getContrats() {
		return contratService.retrieveAllContrats();
	}
	@GetMapping("/retrieve-contrat/{contrat-id}")
	public Contrat retrieveContrat(@PathVariable("contrat-id") Integer contratId) {
		return contratService.retrieveContrat(contratId);
	}

	// http://localhost:8089/Kaddem/econtrat/add-contrat
	@PostMapping("/add-contrat")
	public Contrat addContrat(@RequestBody Contrat c) {
		return contratService.addContrat(c);
	}

	// http://localhost:8089/Kaddem/contrat/remove-contrat/1
	@DeleteMapping("/remove-contrat/{contrat-id}")
	public void removeContrat(@PathVariable("contrat-id") Integer contratId) {
		contratService.removeContrat(contratId);
	}

	// http://localhost:8089/Kaddem/contrat/update-contrat
	@PutMapping("/update-contrat")
	public Contrat updateContrat(@RequestBody Contrat c) {
		return contratService.updateContrat(c);
	}



	@PutMapping(value = "/assignContratToEtudiant/{idContrat}/{nomE}/{prenomE}")
	public Contrat assignContratToEtudiant(
			@PathVariable Integer idContrat,
			@PathVariable String nomE,
			@PathVariable String prenomE) {
		return contratService.affectContratToEtudiant(idContrat, nomE, prenomE);
	}

		@GetMapping(value = "/getnbContratsValides/{startDate}/{endDate}")
		public Integer getnbContratsValides(@PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
										  @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

			return contratService.nbContratsValides(startDate, endDate);
		}

    //Only no-arg methods may be annotated with @Scheduled
    @Scheduled(cron="0 0 13 * * *")//(cron="0 0 13 * * ?")(fixedRate =21600)
	@PutMapping(value = "/majStatusContrat")
	public void majStatusContrat (){
		contratService.retrieveAndUpdateStatusContrat();

	}

	//public float getChiffreAffaireEntreDeuxDate(Date startDate, Date endDate)

	@GetMapping("/calculChiffreAffaireEntreDeuxDate/{startDate}/{endDate}")
	public float calculChiffreAffaireEntreDeuxDates(
			@PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			@PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

		return contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
	}

}


