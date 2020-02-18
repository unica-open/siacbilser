/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per CapitoloUscitaPrevisione.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		InserisceGestioneCapitoloUscitaPrevisioneServiceTest.class,
		AggiornaGestioneCapitoloUscitaPrevisioneServiceTest.class,
		RicercaPuntualeCapitoloUscitaPrevisioneServiceTest.class,
		RicercaSinteticaCapitoloUscitaPrevisioneServiceTest.class,
		RicercaDettaglioCapitoloUscitaPrevisioneServiceTest.class,
		RicercaMovimentiCapitoloUscitaPrevisioneServiceTest.class,
		RicercaPuntualeMovimentiCapitoloUscitaPrevisioneServiceTest.class,
		RicercaVariazioniCapitoloUscitaPrevisioneServiceTest.class,
		VerificaAnnullabilitaCapitoloUscitaPrevisioneServiceTest.class,
		AnnullaGestioneCapitoloUscitaPrevisioneServiceTest.class,
		VerificaEliminabilitaCapitoloUscitaPrevisioneServiceTest.class,
		EliminaGestioneCapitoloUscitaPrevisioneServiceTest.class 
})
public class CapitoloUscitaPrevisioneTestSuite {
}