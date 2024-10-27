package tn.esprit.spring.kaddem.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class EtudiantServiceImpl implements IEtudiantService{
	EtudiantRepository etudiantRepository ;
	ContratRepository contratRepository;
	EquipeRepository equipeRepository;
    DepartementRepository departementRepository;
	public List<Etudiant> retrieveAllEtudiants(){
	return (List<Etudiant>) etudiantRepository.findAll();
	}

	public Etudiant addEtudiant (Etudiant e){
		return etudiantRepository.save(e);
	}

	public Etudiant updateEtudiant (Etudiant e){
		return etudiantRepository.save(e);
	}

	public Etudiant retrieveEtudiant(Integer  idEtudiant){
		return etudiantRepository.findById(idEtudiant).orElseThrow(() -> new NoSuchElementException("Student not found with id: " + idEtudiant));

	}

	public void removeEtudiant(Integer idEtudiant){
	Etudiant e=retrieveEtudiant(idEtudiant);
	etudiantRepository.delete(e);
	}
	public void assignEtudiantToDepartement(Integer etudiantId, Integer departementId) {
		Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
		Departement departement = departementRepository.findById(departementId).orElse(null);

		if (etudiant != null && departement != null) {
			etudiant.setDepartement(departement);
			etudiantRepository.save(etudiant);
		} else {
			// Handle case where either etudiant or departement is not found, e.g., log an error or throw an exception
			log.error("Etudiant or Departement not found with provided IDs.");
		}
	}

	@Transactional
	public Etudiant addAndAssignEtudiantToEquipeAndContract(Etudiant e, long idContrat, Integer idEquipe) {
		Contrat contrat = contratRepository.findById(idContrat).orElse(null);
		Equipe equipe = equipeRepository.findById(idEquipe).orElse(null);

		if (contrat != null && equipe != null) {
			contrat.setEtudiant(e);
			equipe.getEtudiants().add(e);
			return e;
		} else {
			// Handle case where contrat or equipe is not found, e.g., log an error or throw an exception
			log.error("Contrat or Equipe not found with provided IDs.");
			return null;
		}
	}


	public 	List<Etudiant> getEtudiantsByDepartement (Integer idDepartement){
return  etudiantRepository.findEtudiantsByDepartement_IdDepart((idDepartement));
	}
}
