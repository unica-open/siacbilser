/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Inizializzatore della cache per le note di credito
 * @author Marchino Alessandro
 */
public class NoteCreditoCacheInitializer implements CacheElementInitializer<Integer, String> {

	private final DocumentoSpesaDad documentoSpesaDad;

	/**
	 * Costruttore di default: wrappa il DAD del documento di spesa
	 * @param documentoSpesaDad
	 */
	public NoteCreditoCacheInitializer(DocumentoSpesaDad documentoSpesaDad) {
		this.documentoSpesaDad = documentoSpesaDad;
	}
	
	@Override
	public String initialize(Integer key) {
		List<DocumentoSpesa> noteCredito = documentoSpesaDad.ricercaNoteCreditoSpesaByDocumento(key);
		return ottieniStringaNoteCredito(noteCredito);
	}

	/**
	 * Ottiene la stringa delle note di credito a partire dalla lista dei documenti.
	 * <br>
	 * Formato: <code>documento.anno/documento.numero</code>
	 * @param noteCredito le note
	 * @return la lista delle note
	 */
	private String ottieniStringaNoteCredito(List<DocumentoSpesa> noteCredito) {
		if(noteCredito == null || noteCredito.isEmpty()) {
			return null;
		}
		List<String> chunks = new ArrayList<String>();
		for(DocumentoSpesa nc : noteCredito) {
			chunks.add(nc.getAnno() + "/" + nc.getNumero());
		}
		
		return StringUtils.join(chunks, ", ");
	}

}
