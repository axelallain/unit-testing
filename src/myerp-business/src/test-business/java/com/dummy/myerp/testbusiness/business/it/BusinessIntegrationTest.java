package com.dummy.myerp.testbusiness.business.it;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class) // MARCHE AUSSI SANS CETTE LIGNE, POURQUOI ? CAR JUNIT5 A REMPLACÉ RunWith PAR ExtendWith ?
@ContextConfiguration(locations = {"/bootstrapContext.xml"})
@Transactional
public class BusinessIntegrationTest extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Test
    public void getListEcritureComptableIT() {
        List<EcritureComptable> listEcriture = getBusinessProxy().getComptabiliteManager().getListEcritureComptable();
        System.out.println(listEcriture); // POUR VERIFIER LE CONTENU DE LA LISTE
        Assert.assertFalse(listEcriture.isEmpty());
    }

    @Test
    public void insertEcritureComptableIT() throws FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setId(80);

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));

        LocalDate dateEcriture = LocalDate.of(2020, Month.APRIL, 18);
        vEcritureComptable.setDate(Date.from(dateEcriture.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        vEcritureComptable.setLibelle("Integration test");

        vEcritureComptable.setReference("AC-2020/00007");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));

        manager.insertEcritureComptable(vEcritureComptable);
    }

    @Test
    public void updateEcritureComptableIT() throws FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setId(-5);

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));

        LocalDate dateEcriture = LocalDate.of(2016, Month.APRIL, 18);
        vEcritureComptable.setDate(Date.from(dateEcriture.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        vEcritureComptable.setLibelle("Integration test updated");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));

        manager.updateEcritureComptable(vEcritureComptable);
    }

    @Test
    public void deleteEcritureComptableIT() throws FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setId(-1);

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));

        LocalDate dateEcriture = LocalDate.of(2020, Month.APRIL, 18);
        vEcritureComptable.setDate(Date.from(dateEcriture.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        vEcritureComptable.setLibelle("Integration test");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));

        manager.deleteEcritureComptable(vEcritureComptable.getId());
    }

    @Test
    public void addReferenceIT() {

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setId(-1);

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));

        LocalDate dateEcriture = LocalDate.of(2016, Month.APRIL, 18);
        vEcritureComptable.setDate(Date.from(dateEcriture.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        vEcritureComptable.setLibelle("Cartouches d'imprimante");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));

        manager.addReference(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptable() throws FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));

        LocalDate dateEcriture = LocalDate.of(2020, Month.APRIL, 18);
        vEcritureComptable.setDate(Date.from(dateEcriture.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC" + "-" + 2020 + "/" + "00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptable(vEcritureComptable);
    }
}
