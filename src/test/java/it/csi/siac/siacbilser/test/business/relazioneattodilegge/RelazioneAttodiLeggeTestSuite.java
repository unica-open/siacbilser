/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.relazioneattodilegge;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per Relazione Atto di Legge.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ 
		InserisciGestioneRelazioneAttodiLeggeServiceTest.class,
		AggiornaGestioneRelazioneAttodiLeggeServiceTest.class,
		CancellaGestioneRelazioneAttodiLeggeServiceTest.class,
		RicercaGestioneRelazioneAttodiLeggeServiceTest.class
})
public class RelazioneAttodiLeggeTestSuite {
}
