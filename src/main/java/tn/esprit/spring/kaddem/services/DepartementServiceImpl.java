package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class DepartementServiceImpl implements IDepartementService {
	@Autowired
	DepartementRepository departementRepository;

	public DepartementServiceImpl(DepartementRepository departementRepository) {
		this.departementRepository = departementRepository;
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
}
