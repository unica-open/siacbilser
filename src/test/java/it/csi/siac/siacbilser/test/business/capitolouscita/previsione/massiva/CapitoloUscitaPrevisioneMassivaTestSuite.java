/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione.massiva;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per CapitoloUscitaPrevisione.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ 
		AggiornaMassivaCapitoloUscitaPrevisioneServiceTest.class,
		RicercaSinteticaMassivaCapitoloUscitaPrevisioneServiceTest.class,
		RicercaDettaglioMassivaCapitoloUscitaPrevisioneServiceTest.class
})
public class CapitoloUscitaPrevisioneMassivaTestSuite {
}
