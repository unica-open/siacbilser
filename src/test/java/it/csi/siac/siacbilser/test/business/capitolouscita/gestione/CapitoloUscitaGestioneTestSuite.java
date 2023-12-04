/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.gestione;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per CapitoloUscitaGestione.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ 
		InserisceGestioneCapitoloUscitaGestioneServiceTest.class,
		AggiornaGestioneCapitoloUscitaGestioneServiceTest.class,
		RicercaPuntualeCapitoloUscitaGestioneServiceTest.class,
		RicercaSinteticaCapitoloUscitaGestioneServiceTest.class,
		RicercaDettaglioCapitoloUscitaGestioneServiceTest.class,
		RicercaImpegniCapitoloUscitaGestioneServiceTest.class,
		RicercaDocumentiCapitoloUscitaGestioneServiceTest.class,
		RicercaMovimentiCapitoloUscitaGestioneServiceTest.class,
		RicercaVariazioniCapitoloUscitaGestioneServiceTest.class,
		VerificaAnnullabilitaCapitoloUscitaGestioneServiceTest.class,
		AnnullaGestioneCapitoloUscitaGestioneServiceTest.class,
		VerificaEliminabilitaCapitoloUscitaGestioneServiceTest.class,
		EliminaGestioneCapitoloUscitaGestioneServiceTest.class
})
public class CapitoloUscitaGestioneTestSuite {
}
