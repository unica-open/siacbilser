/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione.massiva;

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
		AggiornaMassivaCapitoloEntrataGestioneServiceTest.class,
		RicercaSinteticaMassivaCapitoloEntrataGestioneServiceTest.class,
		RicercaDettaglioMassivaCapitoloEntrataGestioneServiceTest.class
})
public class CapitoloEntrataGestioneMassivaTestSuite {
}
