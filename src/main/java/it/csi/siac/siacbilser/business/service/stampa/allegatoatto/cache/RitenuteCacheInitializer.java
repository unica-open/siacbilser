/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Inizializzatore della cache per le ritenute
 * @author Marchino Alessandro
 */
public class RitenuteCacheInitializer implements CacheElementInitializer<Integer, String> {

	private final DocumentoSpesaDad documentoSpesaDad;
	
	/**
	 * Costruttore di default: wrappa il DAD del documento di spesa
	 * @param documentoSpesaDad
	 */
	public RitenuteCacheInitializer(DocumentoSpesaDad documentoSpesaDad) {
		this.documentoSpesaDad = documentoSpesaDad;
	}

	@Override
	public String initialize(Integer key) {
		DocumentoSpesa ds = new DocumentoSpesa();
		ds.setUid(key);
		List<DettaglioOnere> dettagliOnere = documentoSpesaDad.findRitenuteNonSplitReverseByDocumento(ds);
		return ottieniStringaRitenute(dettagliOnere);
	}
	
	/**
	 * Ottiene la stringa delle ritenute a partire dalla lista dei documenti.
	 * <br>
	 * Formato: <code>ritenuteDocumento.codOnere</code>
	 * @param ritenute le ritenute
	 * @return la lista delle ritenute
	 */
	private String ottieniStringaRitenute(List<DettaglioOnere> ritenute) {
		if(ritenute == null || ritenute.isEmpty()) {
			return null;
		}
		List<String> chunks = new ArrayList<String>();
		for(DettaglioOnere don : ritenute) {
			chunks.add(don.getTipoOnere().getCodice());
		}
		
		return StringUtils.join(chunks, ", ");
	}

}
