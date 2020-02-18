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
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class SubImpegnoStatoConverter.
 */
@Component
public class SubImpegnoStatoConverter extends ExtendedDozerConverter<SubImpegno, SiacTMovgestT> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new subimpegno - stato converter.
	 */
	public SubImpegnoStatoConverter() {
		super(SubImpegno.class, SiacTMovgestT.class);
	}
	@Override
	public SubImpegno convertFrom(SiacTMovgestT src, SubImpegno dest) {
		aggiungiInformazioniStato(src, dest);
		return dest;
	}
	
	/**
	 * Aggiunge le informazioni del piano dei conti nel movimento di gestione
	 * @param src le testate/sub
	 * @param dest il movimento di gestione
	 */
	private void aggiungiInformazioniStato(SiacTMovgestT src, SubImpegno dest) {
		List<SiacDMovgestStato> siacDMovgestStatos = siacTMovgestTRepository.findSiacDMovgestTsStatoByMovgestTsId(src.getUid());
		if(siacDMovgestStatos != null) {
			for(SiacDMovgestStato sdms : siacDMovgestStatos) {
				dest.setStatoOperativoMovimentoGestioneSpesa(sdms.getMovgestStatoCode());
				dest.setDescrizioneStatoOperativoMovimentoGestioneSpesa(sdms.getMovgestStatoDesc());
				return;
			}
		}
	}
	
	@Override
	public SiacTMovgestT convertTo(SubImpegno src, SiacTMovgestT dest) {
		return dest;
	}

}
