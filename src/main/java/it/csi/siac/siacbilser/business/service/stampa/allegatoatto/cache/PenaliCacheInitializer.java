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
 * Inizializzatore della cache per le penali
 * @author Marchino Alessandro
 */
public class PenaliCacheInitializer implements CacheElementInitializer<Integer, String> {

	private final DocumentoSpesaDad documentoSpesaDad;
	
	/**
	 * Costruttore di default: wrappa il DAD del documento di spesa
	 * @param documentoSpesaDad
	 */
	public PenaliCacheInitializer(DocumentoSpesaDad documentoSpesaDad) {
		this.documentoSpesaDad = documentoSpesaDad;
	}

	@Override
	public String initialize(Integer key) {
		List<DocumentoSpesa> penali = documentoSpesaDad.findPenaliByUidDocumento(key);
		return ottieniStringaPenali(penali);
	}
	
	/**
	 * Ottiene la stringa delle penali a partire dalla lista dei documenti.
	 * <br>
	 * Formato: <code>documento.anno/documento.numero</code>
	 * @param penali le penali
	 * @return la lista delle penali
	 */
	private String ottieniStringaPenali(List<DocumentoSpesa> penali) {
		if(penali == null || penali.isEmpty()) {
			return null;
		}
		List<String> chunks = new ArrayList<String>();
		for(DocumentoSpesa p : penali) {
			chunks.add(p.getAnno() + "/" + p.getNumero());
		}
		
		return StringUtils.join(chunks, ", ");
	}

}
