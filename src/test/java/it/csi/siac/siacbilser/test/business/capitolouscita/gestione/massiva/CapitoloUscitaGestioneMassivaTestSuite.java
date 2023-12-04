/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.gestione.massiva;

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
		AggiornaMassivaCapitoloUscitaGestioneServiceTest.class,
		RicercaSinteticaMassivaCapitoloUscitaGestioneServiceTest.class,
		RicercaDettaglioMassivaCapitoloUscitaGestioneServiceTest.class
})
public class CapitoloUscitaGestioneMassivaTestSuite {
}
