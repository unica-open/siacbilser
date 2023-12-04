/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDQuadroEconomicoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRQuadroEconomicoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoStatoEnum;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.StatoOperativoQuadroEconomico;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
public class QuadroEconomicoStatoConverter extends ExtendedDozerConverter<QuadroEconomico, SiacTQuadroEconomico > {
	
	/** The eef. */
	@Autowired private EnumEntityFactory eef;

	/**
	 * Instantiates a new quadro economico stato converter.
	 */
	public QuadroEconomicoStatoConverter() {
		super(QuadroEconomico.class, SiacTQuadroEconomico.class);
	}

	@Override
	public QuadroEconomico convertFrom(SiacTQuadroEconomico src, QuadroEconomico dest) {
		if(src==null){
			return dest;
		}
		
		for (SiacRQuadroEconomicoStato siacRQuadroEconomicoStato : src.getSiacRQuadroEconomicoStatos()) {
			if(siacRQuadroEconomicoStato.getDataCancellazione()==null){
				StatoOperativoQuadroEconomico statoOperativoQuadroEconomico = SiacDQuadroEconomicoStatoEnum.byCodice(siacRQuadroEconomicoStato.getSiacDQuadroEconomicoStato().getQuadroEconomicoStatoCode()).getStatoOperativoQuadroEconomico();
				dest.setStatoOperativoQuadroEconomico(statoOperativoQuadroEconomico);
				break;
			}
		}
		return dest;
		
	}


	@Override
	public SiacTQuadroEconomico convertTo(QuadroEconomico src, SiacTQuadroEconomico dest) {
		final String methodName = "convertTo";
		if(dest== null) {
			return dest;
		}
		
		List<SiacRQuadroEconomicoStato> siacRQuadroEconomicoStatos = new ArrayList<SiacRQuadroEconomicoStato>();
		
		SiacRQuadroEconomicoStato siacRQuadroEconomicoStato = new SiacRQuadroEconomicoStato();
		
		SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum = SiacDQuadroEconomicoStatoEnum.byStatoOperativo(src.getStatoOperativoQuadroEconomico());
		
		SiacDQuadroEconomicoStato siacDQuadroEconomicoStato = eef.getEntity(siacDQuadroEconomicoStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDQuadroEconomicoStato.class); 
				
		log.debug(methodName, "setting siacDDocStato to: "+siacDQuadroEconomicoStato.getQuadroEconomicoStatoCode()+ " ["+siacDQuadroEconomicoStato.getUid()+"]");
		
		siacRQuadroEconomicoStato.setSiacDQuadroEconomicoStato(siacDQuadroEconomicoStato); //SiacDQuadroEconomicoStato(siacDQuadroEconomicoStato);
		siacRQuadroEconomicoStato.setSiacTQuadroEconomico(dest);
		
		siacRQuadroEconomicoStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRQuadroEconomicoStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRQuadroEconomicoStatos.add(siacRQuadroEconomicoStato);
		dest.setSiacRQuadroEconomicoStatos(siacRQuadroEconomicoStatos);
		return dest;
	}

}
