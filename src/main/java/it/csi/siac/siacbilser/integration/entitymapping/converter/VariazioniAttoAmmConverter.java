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

/**
 * The Class VariazioniAttoAmmConverter.
 */
@Component
public class VariazioniAttoAmmConverter extends ExtendedDozerConverter<AttoAmministrativo, SiacTVariazione > {

	/**
	 * Instantiates a new variazioni atto amm converter.
	 */
	public VariazioniAttoAmmConverter() {
		super(AttoAmministrativo.class, SiacTVariazione.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AttoAmministrativo convertFrom(SiacTVariazione src, AttoAmministrativo dest) {
		final String methodName = "convertFrom";
		
		for (SiacRVariazioneStato siacRVariazioneStato : src.getSiacRVariazioneStatos()) {
			
			//L'atto è facoltativo quindi su db potrebbe essere null (oltre che con dataCancellazione valorizzata)
			if(siacRVariazioneStato.getDataCancellazione()!=null){
				continue;
			}
			SiacTAttoAmm siacTAttoAmm = getSiacTAttoAmm(siacRVariazioneStato);	
			if(siacTAttoAmm==null){
				log.debug(methodName, "siacRVariazioneStato.siacTAttoAmm non valorizzato. Returning dest.");
				return dest;
			}
							
			log.debug(methodName, "siacRVariazioneStato.siacTAttoAmm.uid: "+siacTAttoAmm.getUid());		
			
			AttoAmministrativo attoAmministrativo = map(siacTAttoAmm, AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
			dest = attoAmministrativo;
			return dest;
			
		}
		return dest;
	}

	protected SiacTAttoAmm getSiacTAttoAmm(SiacRVariazioneStato siacRVariazioneStato) {
		return siacRVariazioneStato.getSiacTAttoAmm();
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVariazione convertTo(AttoAmministrativo src, SiacTVariazione dest) {
				
		SiacRVariazioneStato siacRVariazioneStato;
		
		if(dest== null) {
			dest = new SiacTVariazione();
			
			List<SiacRVariazioneStato> siacRVariazioneStatos = new ArrayList<SiacRVariazioneStato>();
			siacRVariazioneStato = new SiacRVariazioneStato();
			
			siacRVariazioneStatos.add(siacRVariazioneStato);
			dest.setSiacRVariazioneStatos(siacRVariazioneStatos);
		} else {
			siacRVariazioneStato = dest.getSiacRVariazioneStatos().get(0);
		}
						
		
		SiacTAttoAmm siacTAttoAmm = null;
		
		if(src!=null && src.getUid()!=0) {
			//l'atto è facoltativo quindi potrebbe essere passato null.
			siacTAttoAmm = new SiacTAttoAmm();
			siacTAttoAmm.setAttoammId(src.getUid());
		}
		setSiacTAttoAmm(siacRVariazioneStato, siacTAttoAmm);
		
		
		siacRVariazioneStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRVariazioneStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		
		
		return dest;
	}

	protected void setSiacTAttoAmm(SiacRVariazioneStato siacRVariazioneStato, SiacTAttoAmm siacTAttoAmm) {
		siacRVariazioneStato.setSiacTAttoAmm(siacTAttoAmm);
	}

	
	

	

}
