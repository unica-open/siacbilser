/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTOperazioneAsincronaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTOperazioneAsincrona;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entitymapping.CorMapId;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccorser.model.OperazioneAsincrona;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

 /**
 * The Class AllegatoAttoDataInizioValiditaStatoConverter.
 */
@Component
public class VariazioneImportoCapitoloUltimaAsincronaDefinizioneConverter extends ExtendedDozerConverter<VariazioneImportoCapitolo, SiacTVariazione> {
	
	@Autowired private SiacTOperazioneAsincronaRepository siacTOperazioneAsincronaRepository;
	
	/**
	 * Instantiates a new VariazioneImportiCapitoloDataInizioValiditaStatoConverte. 
	 */
	public VariazioneImportoCapitoloUltimaAsincronaDefinizioneConverter() {
		super(VariazioneImportoCapitolo.class, SiacTVariazione.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public VariazioneImportoCapitolo convertFrom(SiacTVariazione src, VariazioneImportoCapitolo dest) {
		Integer uidVariazione = src.getUid();
		Integer enteProprietarioId = src.getSiacTEnteProprietario().getUid();
		Pageable pageable = new PageRequest(0,1,new Sort(Direction.DESC,"opasAvvio"));
		Page<SiacTOperazioneAsincrona> siacTOperazioneAsincronasPage = siacTOperazioneAsincronaRepository.ottieniLaPiuRecenteOperazioneAsincronaByAzioneEUidVariazione(uidVariazione, AzioneConsentitaEnum.DEFINISCI_VARIAZIONE.getNomeAzione(), Arrays.asList("ELABORAZIONE_CONCORRENTE"), enteProprietarioId, pageable);
		List<SiacTOperazioneAsincrona> siacTOperazioneAsincronas = siacTOperazioneAsincronasPage != null? siacTOperazioneAsincronasPage.getContent() : null; 
		if(siacTOperazioneAsincronas == null || siacTOperazioneAsincronas.isEmpty()) {
			return dest;
		}
		dest.setUltimaAsincronaDefinizione(mapNotNull(siacTOperazioneAsincronas.get(0), OperazioneAsincrona.class, CorMapId.SiacTOperazioneAsincrona_OperazioneAsincrona_Bil));
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
