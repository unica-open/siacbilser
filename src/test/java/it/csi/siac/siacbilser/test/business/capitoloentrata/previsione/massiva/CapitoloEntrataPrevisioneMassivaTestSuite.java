/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione.massiva;

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
		AggiornaMassivaCapitoloEntrataPrevisioneServiceTest.class,
		RicercaSinteticaMassivaCapitoloEntrataPrevisioneServiceTest.class,
		RicercaDettaglioMassivaCapitoloEntrataPrevisioneServiceTest.class
})
public class CapitoloEntrataPrevisioneMassivaTestSuite {
}
