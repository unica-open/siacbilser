/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolobilancio;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per CapitoloService.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ CalcoloDisponibilitaCapitoloServiceTest.class,
			CalcolaTotaliStanziamentiPrevisioneServiceTest.class
})
public class CapitoloBilancioTestSuite {
}
