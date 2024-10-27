package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService{
	EquipeRepository equipeRepository;


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

	public Equipe retrieveEquipe(Integer equipeId){
		return equipeRepository.findById(equipeId).orElseThrow(() -> new NoSuchElementException("Team not found with id: " + equipeId));

	}

	public Equipe updateEquipe(Equipe e){
	return (	equipeRepository.save(e));
	}

	public void evoluerEquipes() {
		List<Equipe> equipes = (List<Equipe>) equipeRepository.findAll();
		Date currentDate = new Date();

		for (Equipe equipe : equipes) {
			if (equipe.getNiveau() == Niveau.JUNIOR || equipe.getNiveau() == Niveau.SENIOR) {
				long activeContractCount = equipe.getEtudiants().stream()
						.filter(etudiant -> etudiant.getContrats().stream()
								.anyMatch(contrat -> !contrat.getArchive()
										&& (currentDate.getTime() - contrat.getDateFinContrat().getTime()) / (1000L * 60 * 60 * 24 * 365) > 1))
						.count();

				if (activeContractCount >= 3) {
					if (equipe.getNiveau() == Niveau.JUNIOR) {
						equipe.setNiveau(Niveau.SENIOR);
					} else if (equipe.getNiveau() == Niveau.SENIOR) {
						equipe.setNiveau(Niveau.EXPERT);
					}
					equipeRepository.save(equipe);
				}
			}
		}
	}
}