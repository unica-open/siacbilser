/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.VariazioneDiBilancio;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;


/**
 * The Class VariazioniStatoConverter.
 */
@Component
public class VariazioniStatoConverter extends DozerConverter<VariazioneDiBilancio, SiacTVariazione > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new variazioni stato converter.
	 */
	public VariazioniStatoConverter() {
		super(VariazioneDiBilancio.class, SiacTVariazione.class);
	}

	@Override
	public VariazioneDiBilancio convertFrom(SiacTVariazione src, VariazioneDiBilancio dest) {
		for (SiacRVariazioneStato siacRVariazioneStato : src.getSiacRVariazioneStatos()) {
			if(siacRVariazioneStato.getDataCancellazione()==null){
				//return new VariazioneStatoConverter().convertFrom(siacRVariazioneStato.getSiacDVariazioneStato(),null);
				StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio = SiacDVariazioneStatoEnum.byCodice(siacRVariazioneStato.getSiacDVariazioneStato().getVariazioneStatoTipoCode()).getStatoOperativoVariazioneDiBilancio();
				dest.setStatoOperativoVariazioneDiBilancio(statoOperativoVariazioneBilancio);
				return dest;
			}
			
		}
		return dest;
	}

	@Override
	public SiacTVariazione convertTo(VariazioneDiBilancio src, SiacTVariazione dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			dest = new SiacTVariazione();
		}
		if(src.getStatoOperativoVariazioneDiBilancio() == null) {
			return dest;
		}
		
		List<SiacRVariazioneStato> siacRVariazioneStatos = dest.getSiacRVariazioneStatos() == null ? new ArrayList<SiacRVariazioneStato>() : dest.getSiacRVariazioneStatos();
		SiacRVariazioneStato siacRVariazioneStato = siacRVariazioneStatos.isEmpty() ? new SiacRVariazioneStato() : siacRVariazioneStatos.remove(0);
	
		SiacDVariazioneStatoEnum variazioneStato =  SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(src.getStatoOperativoVariazioneDiBilancio());
		SiacDVariazioneStato siacDVariazioneStato = eef.getEntity(variazioneStato, dest.getSiacTEnteProprietario().getUid(), SiacDVariazioneStato.class); 
		
		
		log .debug(methodName, "setting siacDVariazioneStato to: "+siacDVariazioneStato.getVariazioneStatoTipoCode()+ " ["+siacDVariazioneStato.getUid()+"]");
		siacRVariazioneStato.setSiacDVariazioneStato(siacDVariazioneStato);
		siacRVariazioneStato.setSiacTVariazione(dest);		
		
		siacRVariazioneStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRVariazioneStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRVariazioneStatos.add(siacRVariazioneStato);
		dest.setSiacRVariazioneStatos(siacRVariazioneStatos);
		
		return dest;
	}



	

}
