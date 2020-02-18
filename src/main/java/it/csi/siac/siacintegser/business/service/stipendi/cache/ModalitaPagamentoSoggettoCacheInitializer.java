/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.cache;

import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializationException;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacintegser.business.service.stipendi.model.StipendioParams;

/**
 * Cache initializer per 
 * @author Marchino Alessandro
 *
 */
public class ModalitaPagamentoSoggettoCacheInitializer implements CacheElementInitializer<String, ModalitaPagamentoSoggetto> {

	private final SoggettoDad soggettoDad;
	
	/**
	 * Costruttore
	 * @param soggettoDad il soggetto da injettare
	 */
	public ModalitaPagamentoSoggettoCacheInitializer(SoggettoDad soggettoDad) {
		this.soggettoDad = soggettoDad;
	}
	
	@Override
	public ModalitaPagamentoSoggetto initialize(String key) {
		// La chiave e' composta da <flag_F24>|<id_soggetto>
		String[] chunks = key.split("\\|");
		if(chunks.length != 2) {
			throw new CacheElementInitializationException(ErroreCore.FORMATO_NON_VALIDO.getErrore("chiave di cache per la Modalita' di pagamento soggetto", key).getTesto());
		}
		Boolean isF24 = Boolean.valueOf(chunks[0]);
		Integer idSoggetto = Integer.valueOf(chunks[1]);
		
		ModalitaPagamentoSoggetto res = null;
		// Nel caso di Oneri e Ritenute (parte spese) deve essere utilizzato, se esiste, il tipo di accredito F24 se non esiste si procede come per lo Stipendio Lordo,.
		if(Boolean.TRUE.equals(isF24)){
			res = soggettoDad.findModalitaPagamentoSoggettoByidAndTipoAccreditoPerStipendio(idSoggetto, StipendioParams.CODICE_TIPO_ACCREDITO_F24);
		}
		
		return res != null && res.getUid() != 0 ? res : soggettoDad.findModalitaPagamentoSoggettoByidPerStipendio(idSoggetto);
	}

}
