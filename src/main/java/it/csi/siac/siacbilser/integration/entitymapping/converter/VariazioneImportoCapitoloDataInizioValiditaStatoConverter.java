/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacRVariazioneStatoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;

 /**
 * The Class AllegatoAttoDataInizioValiditaStatoConverter.
 */
@Component
public class VariazioneImportoCapitoloDataInizioValiditaStatoConverter extends DozerConverter<VariazioneImportoCapitolo, SiacTVariazione> {
	
	@Autowired
	private SiacRVariazioneStatoRepository siacRVariazioneStatoRepository;
	
	/**
	 * Instantiates a new VariazioneImportiCapitoloDataInizioValiditaStatoConverte. 
	 */
	public VariazioneImportoCapitoloDataInizioValiditaStatoConverter() {
		super(VariazioneImportoCapitolo.class, SiacTVariazione.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public VariazioneImportoCapitolo convertFrom(SiacTVariazione src, VariazioneImportoCapitolo dest) {
		
		List<SiacRVariazioneStato> siacRVariazioneStatos = siacRVariazioneStatoRepository.findRStatoByVariazioneIdOrderedyByDataInizioValidita(dest.getUid());
		
		Date dataInizioValiditaStato = null;
		
		for (SiacRVariazioneStato siacRVariazioneStato : siacRVariazioneStatos) {
			dataInizioValiditaStato = siacRVariazioneStato.getDataInizioValidita();
		}
		
		dest.setDataInizioValiditaStato(dataInizioValiditaStato);
		

		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVariazione convertTo(VariazioneImportoCapitolo src, SiacTVariazione dest) {
		return dest;
	}


}
