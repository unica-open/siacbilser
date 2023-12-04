/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;

/**
 * The Class VariazioneImportoSingoloCapitoloAttoAmministrativoConverter.
 */
@Component
public class VariazioneImportoSingoloCapitoloAttoAmministrativoConverter extends ExtendedDozerConverter<VariazioneImportoSingoloCapitolo, SiacTVariazione> {

	/** Costruttore vuoto */
	public VariazioneImportoSingoloCapitoloAttoAmministrativoConverter() {
		super(VariazioneImportoSingoloCapitolo.class, SiacTVariazione.class);
	}

	@Override
	public VariazioneImportoSingoloCapitolo convertFrom(SiacTVariazione src, VariazioneImportoSingoloCapitolo dest) {
		final String methodName = "convertFrom";
		if(dest == null || src == null || src.getSiacRVariazioneStatos() == null) {
			log.debug(methodName, "Nessun dato da mappare");
			return dest;
		}
		
		for (SiacRVariazioneStato siacRVariazioneStato : src.getSiacRVariazioneStatos()) {
			
			// L'atto e' facoltativo quindi su db potrebbe essere null (oltre che con dataCancellazione valorizzata)
			if(siacRVariazioneStato.getDataCancellazione() == null && siacRVariazioneStato.getSiacTAttoAmm() != null){
				
				// SiacTAttoAmm_AttoAmministrativo
				
				SiacTAttoAmm siacTAttoAmm = siacRVariazioneStato.getSiacTAttoAmm();
				log.debug(methodName, "siacRVariazioneStato.siacTAttoAmm.uid: " + siacTAttoAmm.getUid());
				
				AttoAmministrativo attoAmministrativo = mapNotNull(siacTAttoAmm, AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
				dest.setAttoAmministrativo(attoAmministrativo);
			}
		}
		return dest;
	}

	@Override
	public SiacTVariazione convertTo(VariazioneImportoSingoloCapitolo src, SiacTVariazione dest) {
		// Non dovrebbe comunque essere utilizzato
		if(dest == null || src == null || src.getAttoAmministrativo() == null || src.getAttoAmministrativo().getUid() == 0) {
			return dest;
		}
		
		SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setAttoammId(src.getAttoAmministrativo().getUid());
		
		SiacRVariazioneStato siacRVariazioneStato = new SiacRVariazioneStato();
		siacRVariazioneStato.setSiacTAttoAmm(siacTAttoAmm);
		siacRVariazioneStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRVariazioneStato.setLoginOperazione(dest.getLoginOperazione());
		
		List<SiacRVariazioneStato> siacRVariazioneStatos = new ArrayList<SiacRVariazioneStato>();
		siacRVariazioneStatos.add(siacRVariazioneStato);
		dest.setSiacRVariazioneStatos(siacRVariazioneStatos);
		
		return dest;
	}

}
