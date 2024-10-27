package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

@Slf4j
@Service
public class ContratServiceImpl implements IContratService{
@Autowired
ContratRepository contratRepository;
@Autowired
	EtudiantRepository etudiantRepository;
	public List<Contrat> retrieveAllContrats(){
		return (List<Contrat>) contratRepository.findAll();
	}

	public Contrat updateContrat (Contrat  ce){
		return contratRepository.save(ce);
	}

	public  Contrat addContrat (Contrat ce){
		return contratRepository.save(ce);
	}

	public Contrat retrieveContrat (long  idContrat){
		return contratRepository.findById(idContrat).orElse(null);
	}

	public  void removeContrat(Integer idContrat){
		Contrat c=retrieveContrat(idContrat);
		contratRepository.delete(c);
	}
/*

	public Contrat affectContratToEtudiant(long idContrat, String nomE, String prenomE) {
		// Fetch the student by name
		Etudiant e = etudiantRepository.findByNomAndPrenom(nomE, prenomE);

		// If student does not exist, create a new student
		if (e == null) {
			e = new Etudiant();
			e.setNom(nomE);
			e.setPrenom(prenomE);
			// You might want to set other necessary properties for the student here
			etudiantRepository.save(e); // Save the new student
		}

		// Fetch the contract by its ID
		Contrat ce = contratRepository.findByIdContrat(idContrat);
		// Check if contract exists
		if (ce == null) {
			return null; // Return null if contract not found
		}

		// Get all contracts of the student and count active (non-archived) ones
		long nbContratsActifs = e.getContrats().stream().filter(c -> !c.getArchive()).count();

		// If the student has less than 5 active contracts
		if (nbContratsActifs < 5) {
			// Assign the contract to the student
			ce.setEtudiant(e);
			// Save and return the updated contract
			return contratRepository.save(ce);
		}

		// If the student has 5 or more active contracts, do not assign
		return null; // Return null when the assignment is not made
	}
*/
	public 	Integer nbContratsValides(Date startDate, Date endDate){
		return contratRepository.getnbContratsValides(startDate, endDate);
	}

	public void retrieveAndUpdateStatusContrat(){
		List<Contrat>contrats=contratRepository.findAll();
		List<Contrat> contrats15j = new ArrayList<>();  // Initialize list
		List<Contrat> contratsAarchiver = new ArrayList<>();
		for (Contrat contrat : contrats) {
			Date dateSysteme = new Date();
			if (contrat.getArchive()==false) {
				long difference_In_Time = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
				long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
				if (difference_In_Days==15){
					contrats15j.add(contrat);
					System.out.println(" Contrat : " + contrat);

				}
				if (!contrat.getArchive() && dateSysteme.after(contrat.getDateFinContrat())) {
					contratsAarchiver.add(contrat);
					contrat.setArchive(true);
					contratRepository.save(contrat);
				}
			}
		}
	}
	public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate) {
		List<Contrat> contrats = contratRepository.findAll();
		float chiffreAffaireEntreDeuxDates = 0;

		for (Contrat contrat : contrats) {
			Date effectiveStartDate = contrat.getDateDebutContrat().after(startDate) ? contrat.getDateDebutContrat() : startDate;
			Date effectiveEndDate = contrat.getDateFinContrat().before(endDate) ? contrat.getDateFinContrat() : endDate;

			Calendar startCal = Calendar.getInstance();
			startCal.setTime(effectiveStartDate);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(effectiveEndDate);

			int diffYear = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH);

			if (diffMonth >= 0) {
				chiffreAffaireEntreDeuxDates += contrat.getMontantContrat(); // Use contract value directly
			}
		}

		return chiffreAffaireEntreDeuxDates;
	}


}
