package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class DepartementServiceImpl implements IDepartementService {
	@Autowired
	DepartementRepository departementRepository;
	@Autowired
	EtudiantRepository etudiantRepository;

	public DepartementServiceImpl(DepartementRepository departementRepository) {
		this.departementRepository = departementRepository;
		this.etudiantRepository = etudiantRepository;
	}

	public List<Departement> retrieveAllDepartements() {
		return (List<Departement>) departementRepository.findAll();
	}

	public Departement addDepartement(Departement d) {
		return departementRepository.save(d);
	}


	@Override
	public Departement updateDepartement(Integer id, Departement departement) {
		// Check if the department exists
		Optional<Departement> existingDepartementOpt = departementRepository.findById(id);
		if (existingDepartementOpt.isPresent()) {
			Departement existingDepartement = existingDepartementOpt.get();
			// Update fields as necessary
			existingDepartement.setNomDepart(departement.getNomDepart());
			// Save and return the updated department
			return departementRepository.save(existingDepartement);
		} else {
			throw new NoSuchElementException("No Departement found with ID: " + id);  // Ensure this exception is thrown
		}
	}



	@Override
	public Departement retrieveDepartement(int departementId) {
		return departementRepository.findById(departementId)
				.orElseThrow(() -> new NoSuchElementException("No Departement found with ID: " + departementId));
	}

	public void deleteDepartement(Integer idDepartement) {
		Departement d = retrieveDepartement(idDepartement);
		departementRepository.delete(d);
	}
	@Override
	public List<Departement> retrieveDepartmentsByPartialName(String namePart) {
		return departementRepository.findByNameContaining(namePart);
	}
	public Departement affectEtudiantToDepartement(Integer etudiantId, Integer departementId) {
		// Retrieve the student by ID
		Optional<Etudiant> etudiantOpt = etudiantRepository.findById(etudiantId);
		Etudiant etudiant = etudiantOpt.orElseThrow(() -> new NoSuchElementException("No Etudiant found with ID: " + etudiantId));

		// Retrieve the department by ID
		Optional<Departement> departementOpt = departementRepository.findById(departementId);
		Departement departement = departementOpt.orElseThrow(() -> new NoSuchElementException("No Departement found with ID: " + departementId));

		// Assign the department to the student
		etudiant.setDepartement(departement);

		// Update the student's department in the database
		etudiantRepository.save(etudiant);

		// Optional: add the student to the department's list of students (if bidirectional relationship)
		departement.getEtudiants().add(etudiant);
		return departementRepository.save(departement); // Save and return updated department
	}
}

