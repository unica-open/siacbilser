/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDMovgestStato;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * The Class AccertamentoStatoConverter.
 */
@Component
public class AccertamentoStatoConverter extends ExtendedDozerConverter<Accertamento, SiacTMovgest> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new accertamento - stato converter.
	 */
	public AccertamentoStatoConverter() {
		super(Accertamento.class, SiacTMovgest.class);
	}
	@Override
	public Accertamento convertFrom(SiacTMovgest src, Accertamento dest) {
		aggiungiInformazioniStato(src, dest);
		return dest;
	}
	
	/**
	 * Aggiunge le informazioni del piano dei conti nel movimento di gestione
	 * @param src le testate/sub
	 * @param dest il movimento di gestione
	 */
	private void aggiungiInformazioniStato(SiacTMovgest src, Accertamento dest) {
		List<SiacDMovgestStato> siacDMovgestStatos = siacTMovgestTRepository.findSiacDMovgestTsStatoByMovgestId(src.getUid());
		if(siacDMovgestStatos != null) {
			for(SiacDMovgestStato sdms : siacDMovgestStatos) {
				dest.setStatoOperativoMovimentoGestioneEntrata(sdms.getMovgestStatoCode());
				dest.setDescrizioneStatoOperativoMovimentoGestioneEntrata(sdms.getMovgestStatoDesc());
				return;
			}
		}
	}
	
	@Override
	public SiacTMovgest convertTo(Accertamento src, SiacTMovgest dest) {
		return dest;
	}

}
