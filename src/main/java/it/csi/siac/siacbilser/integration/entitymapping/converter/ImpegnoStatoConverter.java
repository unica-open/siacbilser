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
import it.csi.siac.siacfinser.model.Impegno;

/**
 * The Class ImpegnoStatoConverter.
 */
@Component
public class ImpegnoStatoConverter extends ExtendedDozerConverter<Impegno, SiacTMovgest> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new impegno - stato converter.
	 */
	public ImpegnoStatoConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}
	@Override
	public Impegno convertFrom(SiacTMovgest src, Impegno dest) {
		aggiungiInformazioniStato(src, dest);
		return dest;
	}
	
	/**
	 * Aggiunge le informazioni del piano dei conti nel movimento di gestione
	 * @param src le testate/sub
	 * @param dest il movimento di gestione
	 */
	private void aggiungiInformazioniStato(SiacTMovgest src, Impegno dest) {
		List<SiacDMovgestStato> siacDMovgestStatos = siacTMovgestTRepository.findSiacDMovgestTsStatoByMovgestId(src.getUid());
		if(siacDMovgestStatos != null) {
			for(SiacDMovgestStato sdms : siacDMovgestStatos) {
				dest.setStatoOperativoMovimentoGestioneSpesa(sdms.getMovgestStatoCode());
				dest.setDescrizioneStatoOperativoMovimentoGestioneSpesa(sdms.getMovgestStatoDesc());
				return;
			}
		}
	}
	
	@Override
	public SiacTMovgest convertTo(Impegno src, SiacTMovgest dest) {
		return dest;
	}

}
