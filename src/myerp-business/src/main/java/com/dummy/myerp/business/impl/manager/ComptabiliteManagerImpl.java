package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    @Override
    public SequenceEcritureComptable getSequenceByJournalAndAnnee(String pJournal, Integer pAnnee) throws NotFoundException {
        return getDaoProxy().getComptabiliteDao().getSequenceByJournalAndAnnee(pJournal, pAnnee);
    }

    @Override
    public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException {
        return getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(pReference);
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester (terminé)
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        // TODO à implémenter (terminé)
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1
                3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
                4.  Enregistrer (insert/update) la valeur de la séquence en persitance
                    (table sequence_ecriture_comptable)
         */

        // Récupération de l'année de l'écriture
        Calendar date = Calendar.getInstance();
        date.setTime(pEcritureComptable.getDate());
        int annee = date.get(Calendar.YEAR);

        // Initialisation de la valeur de la séquence
        int valeurSequence;

        try {
            /* 1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable) */
            SequenceEcritureComptable sequenceEcritureComptable = getSequenceByJournalAndAnnee(pEcritureComptable.getJournal().getCode(), annee);

            /* 1.  test */
            /* 2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                1. Utiliser le numéro 1.
                    * Sinon :
                1. Utiliser la dernière valeur + 1 */
            if (sequenceEcritureComptable == null) {
                valeurSequence = 00001;
            } else {
                valeurSequence = sequenceEcritureComptable.getDerniereValeur() + 1; // 00001 + 1 = 2 OU 00002
            }

            /* 3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5) */
            pEcritureComptable.setReference(pEcritureComptable.getJournal().getCode() + "-" + annee + "/" + valeurSequence);
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);

            /* 4.  Enregistrer (insert/update) la valeur de la séquence en persitance (table sequence_ecriture_comptable) */
            SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
            vSequenceEcritureComptable.setJournal(pEcritureComptable.getJournal().getCode());
            vSequenceEcritureComptable.setAnnee(annee);
            vSequenceEcritureComptable.setDerniereValeur(valeurSequence);
            getDaoProxy().getComptabiliteDao().upsertSequenceEcritureComptable(vSequenceEcritureComptable);

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester (terminé)
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    // TODO tests à compléter (terminé)
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            System.out.println(vViolations); // JUSTE POUR DEBUG, A SUPPRIMER !!!
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.", new ConstraintViolationException("L'écriture comptable ne respecte pas les contraintes de validation", vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(), BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(), BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 1 || vNbrCredit < 1 || vNbrDebit < 1) {
            throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la référence (terminé)
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...

        // Mémo à supprimer : AA-2020/00001

        if (!pEcritureComptable.getReference().substring(0, 2).equals(pEcritureComptable.getJournal().getCode())) {
            throw new FunctionalException("Le code du journal trouvé dans la référence ne correspond pas au code du journal de l'écriture comptable");
        }

        String anneeEcriture = pEcritureComptable.getDate().toString().substring(25, 29);

        if (!pEcritureComptable.getReference().substring(3, 7).equals(anneeEcriture)) {
            System.out.println(anneeEcriture);
            System.out.println(pEcritureComptable.getReference().substring(3, 7));
            throw new FunctionalException("La date trouvée dans la référence ne correspond pas à la date de l'écriture comptable");
        }
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
