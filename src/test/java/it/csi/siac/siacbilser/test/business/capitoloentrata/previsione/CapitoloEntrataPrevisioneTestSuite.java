/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per CapitoloEntrataPrevisione.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ 
		InserisceGestioneCapitoloEntrataPrevisioneServiceTest.class,
		AggiornaGestioneCapitoloEntrataPrevisioneServiceTest.class,
		RicercaPuntualeCapitoloEntrataPrevisioneServiceTest.class,
 		RicercaSinteticaCapitoloEntrataPrevisioneServiceTest.class,
 		RicercaDettaglioCapitoloEntrataPrevisioneServiceTest.class,
 		RicercaPuntualeMovimentiCapitoloEntrataPrevisioneServiceTest.class,
 		RicercaMovimentiCapitoloEntrataPrevisioneServiceTest.class,
 		RicercaVariazioniCapitoloEntrataPrevisioneServiceTest.class,
 		VerificaAnnullabilitaCapitoloEntrataPrevisioneServiceTest.class,
 		AnnullaGestioneCapitoloEntrataPrevisioneServiceTest.class,
 		VerificaEliminabilitaCapitoloEntrataPrevisioneServiceTest.class,
 		EliminaGestioneCapitoloEntrataPrevisioneServiceTest.class
})

public class CapitoloEntrataPrevisioneTestSuite {
}
