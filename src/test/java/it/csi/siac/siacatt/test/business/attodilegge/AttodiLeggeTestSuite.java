/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.attodilegge;

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
		InserisciGestioneAttodiLeggeServiceTest.class,
		AggiornaGestioneAttodiLeggeServiceTest.class,
		RicercaGestioneAttodiLeggeServiceTest.class,
		CancellaGestioneAttodiLeggeServiceTest.class
})
public class AttodiLeggeTestSuite {
}
