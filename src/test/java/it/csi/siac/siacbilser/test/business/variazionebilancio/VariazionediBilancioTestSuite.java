/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.variazionebilancio;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per Variazione di Bilancio.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ 
		InserisceGestioneVariazionediBilancioServiceTest.class,
		AggiornaGestioneVariazionediBilancioServiceTest.class,
		AnnullaGestioneVariazionediBilancioServiceTest.class,
		DefinisceGestioneVariazionediBilancioServiceTest.class,
		RicercaGestioneVariazionediBilancioServiceTest.class,
		RicercaGestioneVariazioneCapitoloEntrataGestioneServiceTest.class,
		RicercaGestioneVariazioneCapitoloEntrataPrevisioneServiceTest.class,
		RicercaGestioneVariazioneCapitoloUscitaGestioneServiceTest.class,
		RicercaGestioneVariazioneCapitoloUscitaPrevisioneServiceTest.class
})
public class VariazionediBilancioTestSuite {
}
