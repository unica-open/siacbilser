/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AttoAmministrativoEnteConfig {

	private enum Ente { 
		REGP;
	}
	
	// CR-2778
	private static final Set<String> IGNORE_CDC = initConfig(Ente.REGP);
	
	
	public static boolean ignoreCDC(String ente) {
		return IGNORE_CDC.contains(ente);
	}
	
	// SIAC-7376
	private static final Set<String> IGNORE_TIPO_AD = initConfig(Ente.REGP);
	
	
	public static boolean ignoreTipoAD(String ente) {
		return IGNORE_TIPO_AD.contains(ente);
	}
	
	
	private static Set<String> initConfig(Ente... enti) {
		Set<String> temp = new HashSet<String>();
		
		for (Ente ente : enti) {
			temp.add(ente.name());
		}

		return Collections.unmodifiableSet(temp);
	}

}
