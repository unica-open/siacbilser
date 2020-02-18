/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache;

import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Inizializzatore della cache per il soggetto
 * @author Marchino Alessandro
 */
public class SoggettoCacheInitializer implements CacheElementInitializer<Integer, Soggetto> {

	private final SoggettoDad soggettoDad;
	
	/**
	 * Costruttore di default: wrappa il DAD del soggetto
	 * @param soggettoDad
	 */
	public SoggettoCacheInitializer(SoggettoDad soggettoDad) {
		this.soggettoDad = soggettoDad;
	}

	@Override
	public Soggetto initialize(Integer key) {
		return soggettoDad.findSoggettoByIdModpagWithIndirizzi(key);
	}
	
}
