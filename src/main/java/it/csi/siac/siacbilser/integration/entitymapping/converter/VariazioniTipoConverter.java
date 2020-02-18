/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDVariazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneTipoEnum;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneDiBilancio;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Converte da TipoVariazione a SiacDVariazioneTipo e viceversa.
 * 
 * @author Domenico
 *
 */
@Component
public class VariazioniTipoConverter extends DozerConverter<VariazioneDiBilancio, SiacTVariazione> {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new variazioni tipo converter.
	 */
	public VariazioniTipoConverter() {
		super(VariazioneDiBilancio.class, SiacTVariazione.class);
	}

	@Override
	public VariazioneDiBilancio convertFrom(SiacTVariazione src, VariazioneDiBilancio dest) {
		final String methodName = "convertFrom";
		if(src.getSiacDVariazioneTipo() == null) {
			return null;
		}
		
		try{
			TipoVariazione tipoVariazione = SiacDVariazioneTipoEnum.byCodice(src.getSiacDVariazioneTipo().getVariazioneTipoCode()).getTipoVariazione();
			dest.setTipoVariazione(tipoVariazione);
		} catch (RuntimeException re){
			log.debug(methodName, "Cannot map "+src+" to TipoVariazione. Returning null. " + re.getMessage());
		}
		return dest;
	}

	@Override
	public SiacTVariazione convertTo(VariazioneDiBilancio src, SiacTVariazione dest) {
		final String methodName = "convertTo";
		try{
			//Occhio prevede che prima di questo converter sia già stato settato dest.getSiacTEnteProprietario().getUid()!!!
			SiacDVariazioneTipoEnum siacDVariazioneTipoEnum = SiacDVariazioneTipoEnum.byTipoVariazione(src.getTipoVariazione());
			SiacDVariazioneTipo siacDVariazioneTipo = eef.getEntity(siacDVariazioneTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDVariazioneTipo.class);
			dest.setSiacDVariazioneTipo(siacDVariazioneTipo);
		} catch (RuntimeException re){			
			log.debug(methodName, "Cannot map "+src+" to SiacDVariazioneTipo. Returning null. " + re.getMessage());
		}
		return dest;
	}

}
