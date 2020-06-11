package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import com.dummy.myerp.technical.exception.FunctionalException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) throws FunctionalException {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);

        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test
    public void isEquilibree() throws FunctionalException {
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

    @Test
    public void getTotalDebitWithTwoLines() throws FunctionalException {
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "20"));

        BigDecimal vRetour = vEcriture.getTotalDebit();

        BigDecimal expected = BigDecimal.valueOf(20);
        Assert.assertEquals(expected, vRetour);
    }

    @Test
    public void getTotalDebitWithTwoLinesAndNegativesValues() throws FunctionalException {
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "-20", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "-20"));

        BigDecimal vRetour = vEcriture.getTotalDebit();

        BigDecimal expected = BigDecimal.valueOf(-20);
        Assert.assertEquals(expected, vRetour);
    }

    @Test
    public void getTotalDebitWithTwoLinesAndNullValuesShouldReturnZero() throws FunctionalException {
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, null, null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, null));

        BigDecimal vRetour = vEcriture.getTotalDebit();

        BigDecimal expected = BigDecimal.valueOf(0);
        Assert.assertEquals(expected, vRetour);
    }

    @Test
    public void getTotalCreditWithTwoLines() throws FunctionalException {
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "30", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));

        BigDecimal vRetour = vEcriture.getTotalCredit();

        BigDecimal expected = BigDecimal.valueOf(30);
        Assert.assertEquals(expected, vRetour);
    }

    @Test
    public void getTotalCreditWithTwoLinesAndNegativesValues() throws FunctionalException {
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "-30", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "-30"));

        BigDecimal vRetour = vEcriture.getTotalCredit();

        BigDecimal expected = BigDecimal.valueOf(-30);
        Assert.assertEquals(expected, vRetour);
    }

    @Test
    public void getTotalCreditWithTwoLinesAndNullValuesShouldReturnZero() throws FunctionalException {
        EcritureComptable vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, null, null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, null));

        BigDecimal vRetour = vEcriture.getTotalCredit();

        BigDecimal expected = BigDecimal.valueOf(0);
        Assert.assertEquals(expected, vRetour);
    }
}