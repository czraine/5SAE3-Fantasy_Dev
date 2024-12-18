package tn.esprit.spring.kaddem.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Service
@Slf4j
public class EtudiantServiceImpl implements IEtudiantService {
	@Autowired
	EtudiantRepository etudiantRepository;
	@Autowired
	ContratRepository contratRepository;
	@Autowired
	EquipeRepository equipeRepository;
	@Autowired
	DepartementRepository departementRepository;

	// Récupérer tous les étudiants
	@Override
	public List<Etudiant> retrieveAllEtudiants() {
		log.info("Retrieving all students.");
		return (List<Etudiant>) etudiantRepository.findAll();
	}


	// Ajouter un étudiant
	@Override
	public Etudiant addEtudiant(Etudiant e, Integer departementId) {
		Departement departement = departementRepository.findById(departementId)
				.orElseThrow(() -> new EntityNotFoundException("Departement not found"));
		e.setDepartement(departement); // Set the department before saving
		log.info("Adding a new student: {}", e);
		return etudiantRepository.save(e);
	}


	@Override
// Mettre à jour un étudiant
	public Etudiant updateEtudiant(Etudiant e) {
		Etudiant existingEtudiant = etudiantRepository.findById(e.getIdEtudiant())
				.orElseThrow(() -> new EntityNotFoundException("Etudiant not found"));
		existingEtudiant.setNom(e.getNom());
		existingEtudiant.setPrenom(e.getPrenom());
		existingEtudiant.setOption(e.getOption());
		existingEtudiant.setDepartement(e.getDepartement()); // Update the department if needed
		log.info("Updating student: {}", existingEtudiant);
		return etudiantRepository.save(existingEtudiant);
	}


	// Récupérer un étudiant par ID
	@Override
	public Etudiant retrieveEtudiant(Integer idEtudiant) {
		log.info("Retrieving student with ID: {}", idEtudiant);
		return etudiantRepository.findById(idEtudiant).orElse(null);
	}

	// Supprimer un étudiant
	@Override
	public void removeEtudiant(Integer idEtudiant) {
		// Add a log statement
		log.info("Removing student with ID: " + idEtudiant);

		Etudiant etudiant = etudiantRepository.findById(idEtudiant)
				.orElseThrow(() -> new RuntimeException("Etudiant not found"));
		etudiantRepository.delete(etudiant);
	}



	// Assigner un étudiant à un département
	@Override
	public void assignEtudiantToDepartement(Integer etudiantId, Integer departementId) {
		Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
		Departement departement = departementRepository.findById(departementId).orElse(null);
		if (etudiant != null && departement != null) {
			etudiant.setDepartement(departement);
			etudiantRepository.save(etudiant);
			log.info("Assigned student {} to department {}.", etudiantId, departementId);
		} else {
			log.warn("Failed to assign student to department.");
		}
	}

	// Ajouter et assigner un étudiant à une équipe et un contrat
	@Override
	@Transactional
	public Etudiant addAndAssignEtudiantToEquipeAndContract(Etudiant e, long idContrat, Integer idEquipe) {
		Contrat c = contratRepository.findById(idContrat).orElse(null);
		Equipe eq = equipeRepository.findById(idEquipe).orElse(null);

		if (c != null && eq != null) {
			// Assigner l'étudiant au contrat
			c.setEtudiant(e);

			// Ajouter l'étudiant à l'équipe
			eq.getEtudiants().add(e);

			// Sauvegarder les modifications pour le contrat et l'équipe si nécessaire
			contratRepository.save(c);
			equipeRepository.save(eq);

			log.info("Assigned student {} to contract {} and team {}.", e.getIdEtudiant(), idContrat, idEquipe);
		} else {
			log.warn("Contract or team not found for assignment.");
		}

		// Sauvegarder l'étudiant et retourner l'instance sauvegardée
		return etudiantRepository.save(e);
	}




	// Obtenir les étudiants d’un département
	@Override
	public List<Etudiant> getEtudiantsByDepartement(Integer idDepartement) {
		log.info("Retrieving students for department {}", idDepartement);
		return etudiantRepository.findEtudiantsByDepartement_IdDepart(idDepartement);
	}

	// Nouvelle fonctionnalité: Recherche d’étudiants par nom ou prénom
	@Override
	public List<Etudiant> findEtudiantsByNomOrPrenom(String nomOrPrenom) {
		log.info("Searching students by name or first name: {}", nomOrPrenom);
		return etudiantRepository.findByNomContainingOrPrenomContaining(nomOrPrenom, nomOrPrenom);
	}

	// Nouvelle fonctionnalité: Obtenir les étudiants avec contrat actif
	@Override
	public List<Etudiant> getEtudiantsWithActiveContrats() {
		// Log the method call
		log.info("Retrieving students with active contracts.");

		// Fetch students with active contracts
		List<Etudiant> etudiantsWithActiveContrats = etudiantRepository.findByContrats_ArchiveFalse();

		// Log the number of students found
		log.info("Number of students with active contracts: " + etudiantsWithActiveContrats.size());

		// Check if the list is empty and log an appropriate message
		if (etudiantsWithActiveContrats.isEmpty()) {
			log.warn("No students with active contracts found.");
		}

		// Return the list of students
		return etudiantsWithActiveContrats;
	}


/*
	// Nouvelle fonctionnalité: Vérifier si un étudiant est dans une équipe
	@Override
	public boolean isEtudiantInEquipe(Integer etudiantId, Integer equipeId) {
		Optional<Etudiant> etudiantOptional = etudiantRepository.findById(etudiantId);
		if (etudiantOptional.isPresent()) {
			Etudiant etudiant = etudiantOptional.get();
			return etudiant.getEquipes().stream().anyMatch(equipe -> equipe.getIdEquipe().equals(equipeId));
		}
		return false;
	}*/

}