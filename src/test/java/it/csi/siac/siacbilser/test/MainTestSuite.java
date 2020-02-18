/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import it.csi.siac.siacbilser.test.business.capitolobilancio.CapitoloBilancioTestSuite;
import it.csi.siac.siacbilser.test.business.capitoloentrata.gestione.CapitoloEntrataGestioneTestSuite;
import it.csi.siac.siacbilser.test.business.capitoloentrata.previsione.CapitoloEntrataPrevisioneTestSuite;
import it.csi.siac.siacbilser.test.business.capitolouscita.gestione.CapitoloUscitaGestioneTestSuite;
import it.csi.siac.siacbilser.test.business.capitolouscita.previsione.CapitoloUscitaPrevisioneTestSuite;

/**
 * Suite con tutti i test.
 *
 * @author alagna
 * @version $Id: $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CapitoloBilancioTestSuite.class,
	CapitoloEntrataGestioneTestSuite.class,
	CapitoloEntrataPrevisioneTestSuite.class,
	CapitoloUscitaGestioneTestSuite.class,
	CapitoloUscitaPrevisioneTestSuite.class		
})
public class MainTestSuite {
}
