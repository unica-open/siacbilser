/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per CapitoloEntrataGestione.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ 
		InserisceGestioneCapitoloEntrataGestioneServiceTest.class,
		AggiornaGestioneCapitoloEntrataGestioneServiceTest.class,
		RicercaPuntualeCapitoloEntrataGestioneServiceTest.class,
		RicercaSinteticaCapitoloEntrataGestioneServiceTest.class,
		RicercaDettaglioCapitoloEntrataGestioneServiceTest.class,
		RicercaAccertamentiCapitoloEntrataGestioneServiceTest.class,
		RicercaDocumentiCapitoloEntrataGestioneServiceTest.class,
		RicercaMovimentiCapitoloEntrataGestioneServiceTest.class,
		RicercaVariazioniCapitoloEntrataGestioneServiceTest.class,
		VerificaAnnullabilitaCapitoloEntrataGestioneServiceTest.class,
		AnnullaGestioneCapitoloEntrataGestioneServiceTest.class,
		VerificaEliminabilitaCapitoloEntrataGestioneServiceTest.class,
		EliminaGestioneCapitoloEntrataGestioneServiceTest.class
})
public class CapitoloEntrataGestioneTestSuite {
}
