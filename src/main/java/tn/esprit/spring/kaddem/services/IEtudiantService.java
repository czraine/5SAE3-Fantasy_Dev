package tn.esprit.spring.kaddem.services;

import tn.esprit.spring.kaddem.entities.Etudiant;

import java.util.List;

public interface IEtudiantService {
    public List<Etudiant> retrieveAllEtudiants();

    public Etudiant addEtudiant (Etudiant e);

    public Etudiant updateEtudiant (Etudiant e);

    public Etudiant retrieveEtudiant(Integer  idEtudiant);


    // Supprimer un étudiant
    void removeEtudiant(int id);

    public void assignEtudiantToDepartement (Integer etudiantId, Integer departementId);

    public Etudiant addAndAssignEtudiantToEquipeAndContract(Etudiant e, long idContrat, Integer idEquipe);

    public 	List<Etudiant> getEtudiantsByDepartement (Integer idDepartement);

    // Nouvelle fonctionnalité: Recherche d’étudiants par nom ou prénom
    List<Etudiant> findEtudiantsByNomOrPrenom(String nomOrPrenom);

    // Nouvelle fonctionnalité: Obtenir les étudiants avec contrat actif

    List<Etudiant> getEtudiantsWithActiveContrats();

    // Nouvelle fonctionnalité: Vérifier si un étudiant est dans une équipe

    // Nouvelle fonctionnalité: Vérifier si un étudiant est dans u

    // Nouvelle fonctionnalité: Vérifier si un étudiant est dans une
}
