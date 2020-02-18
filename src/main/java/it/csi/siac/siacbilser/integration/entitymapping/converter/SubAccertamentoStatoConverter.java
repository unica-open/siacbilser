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
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * The Class SubAccertamentoStatoConverter.
 */
@Component
public class SubAccertamentoStatoConverter extends ExtendedDozerConverter<SubAccertamento, SiacTMovgestT> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new subaccertamento - stato converter.
	 */
	public SubAccertamentoStatoConverter() {
		super(SubAccertamento.class, SiacTMovgestT.class);
	}
	@Override
	public SubAccertamento convertFrom(SiacTMovgestT src, SubAccertamento dest) {
		aggiungiInformazioniStato(src, dest);
		return dest;
	}
	
	/**
	 * Aggiunge le informazioni del piano dei conti nel movimento di gestione
	 * @param src le testate/sub
	 * @param dest il movimento di gestione
	 */
	private void aggiungiInformazioniStato(SiacTMovgestT src, SubAccertamento dest) {
		List<SiacDMovgestStato> siacDMovgestStatos = siacTMovgestTRepository.findSiacDMovgestTsStatoByMovgestTsId(src.getUid());
		if(siacDMovgestStatos != null) {
			for(SiacDMovgestStato sdms : siacDMovgestStatos) {
				dest.setStatoOperativoMovimentoGestioneEntrata(sdms.getMovgestStatoCode());
				dest.setDescrizioneStatoOperativoMovimentoGestioneEntrata(sdms.getMovgestStatoDesc());
				return;
			}
		}
	}
	
	@Override
	public SiacTMovgestT convertTo(SubAccertamento src, SiacTMovgestT dest) {
		return dest;
	}

}
