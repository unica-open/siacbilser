/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache;

import java.util.HashSet;
import java.util.Set;

import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;

/**
 * Inizializzatore della cache per il cig
 * @author Marchino Alessandro
 */
public class SamCacheInitializer implements CacheElementInitializer<String, Set<String>> {

	@Override
	public Set<String> initialize(String key) {
		return new HashSet<String>();
	}
	
}
