/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.provvedimento;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Tests per Provvedimento.
 *
 * @author Lisa Davide
 * @version $Id: $
 */
@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ 
		InserisciGestioneProvvedimentoServiceTest.class,
		AggiornaGestioneProvvedimentoServiceTest.class,
		RicercaGestioneProvvedimentoServiceTest.class,
		VerificaAnnullabilitaGestioneProvvedimentoServiceTest.class
})
public class ProvvedimentoTestSuite {
}
