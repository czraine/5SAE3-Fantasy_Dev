package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService{
	EquipeRepository equipeRepository;

	///merge test
	public List<Equipe> retrieveAllEquipes(){
		return  (List<Equipe>) equipeRepository.findAll();
	}
	public Equipe addEquipe(Equipe e){
		return (equipeRepository.save(e));
	}

	public  void deleteEquipe(Integer idEquipe){
		Equipe e=retrieveEquipe(idEquipe);
		equipeRepository.delete(e);
	}

	public Equipe retrieveEquipe(Integer equipeId) {
		Optional<Equipe> optionalEquipe = equipeRepository.findById(equipeId);
        // or throw a custom exception if you prefer
        return optionalEquipe.orElse(null);
	}


	public Equipe updateEquipe(Equipe e){
		return (	equipeRepository.save(e));
	}


	public void evoluerEquipes() {
		List<Equipe> equipes = (List<Equipe>) equipeRepository.findAll();
		for (Equipe equipe : equipes) {
			if (isEligibleForEvolution(equipe)) {
				int nbEtudiantsAvecContratsActifs = countActiveContractStudents(equipe.getEtudiants());
				if (nbEtudiantsAvecContratsActifs >= 3) {
					evolveEquipeNiveau(equipe);
				}
			}
		}
	}

	private boolean isEligibleForEvolution(Equipe equipe) {
		Niveau niveau = equipe.getNiveau();
		return niveau.equals(Niveau.JUNIOR) || niveau.equals(Niveau.SENIOR);
	}

	private int countActiveContractStudents(Set<Etudiant> etudiants) {
		int nbEtudiantsAvecContratsActifs = 0;
		for (Etudiant etudiant : etudiants) {
			if (hasActiveContract(etudiant)) {
				nbEtudiantsAvecContratsActifs++;
				if (nbEtudiantsAvecContratsActifs >= 3) {
					break;
				}
			}
		}
		return nbEtudiantsAvecContratsActifs;
	}

	private boolean hasActiveContract(Etudiant etudiant) {
		Set<Contrat> contrats = etudiant.getContrats();
		Date currentDate = new Date();
		for (Contrat contrat : contrats) {
			if (!(!Boolean.TRUE.equals(!contrat.getArchive()) || !isContractActiveForMoreThanOneYear(contrat, currentDate))) {
				return true;
			}
		}
		return false;
	}

	private boolean isContractActiveForMoreThanOneYear(Contrat contrat, Date currentDate) {
		long differenceInTime = currentDate.getTime() - contrat.getDateFinContrat().getTime();
		long differenceInYears = differenceInTime / (1000L * 60 * 60 * 24 * 365);
		return differenceInYears > 1;
	}

	private void evolveEquipeNiveau(Equipe equipe) {
		if (equipe.getNiveau().equals(Niveau.JUNIOR)) {
			equipe.setNiveau(Niveau.SENIOR);
		} else if (equipe.getNiveau().equals(Niveau.SENIOR)) {
			equipe.setNiveau(Niveau.EXPERT);
		}
		equipeRepository.save(equipe);
	}

	public int getActiveContratsCount(Integer equipeId) {
		Equipe equipe = retrieveEquipe(equipeId);
		return (int) equipe.getEtudiants().stream()
				.flatMap(etudiant -> etudiant.getContrats().stream())
				.filter(contrat -> !contrat.getArchive())
				.count();
	}

	public List<Equipe> retrieveEquipesWithMinEtudiants(int minEtudiants) {
		List<Equipe> allEquipes = (List<Equipe>) equipeRepository.findAll();
		List<Equipe> list = new ArrayList<>();
		for (Equipe equipe : allEquipes) {
			if (equipe.getEtudiants() != null && equipe.getEtudiants().size() >= minEtudiants) {
				list.add(equipe);
			}
		}
		return list;
	}




}