/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleEpStato;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpStatoEnum;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;

/**
 * The Class CausaleEPStatoConverter.
 * 
 * @author Domenico
 */
@Component
public class CausaleEPStatoConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {
	
//	<a>this</a> <!-- tipoCausale -->
//	<b>this</b> <!-- siacDCausaleEpStato -->
	
	@Autowired
	private EnumEntityFactory eef;
	

	/**
	 * Instantiates a new causale ep stato converter.
	 */
	public CausaleEPStatoConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRCausaleEpStatos()!=null) {
			for (SiacRCausaleEpStato siacRCausaleEpStato : src.getSiacRCausaleEpStatos()) {
				if(siacRCausaleEpStato.getDataCancellazione()!=null
						|| !siacRCausaleEpStato.isDataValiditaCompresa(dest.getDataInizioValiditaFiltro())){
					continue;
				}
				
				SiacDCausaleEpStato siacDCausaleEpStato = siacRCausaleEpStato.getSiacDCausaleEpStato();
				StatoOperativoCausaleEP statoCausale = SiacDCausaleEpStatoEnum.byCodice(siacDCausaleEpStato.getCausaleEpStatoCode()).getStatoOperativoCausaleEP();
				log .debug(methodName, "setting statoCausale to: "+statoCausale+ " [SiacTCausaleEp.uid:"+src.getUid()+"]");
				dest.setStatoOperativoCausaleEP(statoCausale);
				break;
			}
		}
		
		return dest;
	}

	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
		final String methodName = "convertTo";
		
		if(src.getStatoOperativoCausaleEP() == null) {
			return dest;
		}
		
		SiacDCausaleEpStatoEnum stato =  SiacDCausaleEpStatoEnum.byStatoOperativo(src.getStatoOperativoCausaleEP());
		SiacDCausaleEpStato siacDCausaleEpStato = eef.getEntity(stato, dest.getSiacTEnteProprietario().getUid(), SiacDCausaleEpStato.class); 
		log.debug(methodName, "setting siacDCausaleEpStato to: "+siacDCausaleEpStato.getCausaleEpStatoCode()+ " [uid:"+siacDCausaleEpStato.getUid()+"]");
		
		
		SiacRCausaleEpStato siacRCausaleEpStato = new SiacRCausaleEpStato();
		siacRCausaleEpStato.setSiacDCausaleEpStato(siacDCausaleEpStato);
		
		siacRCausaleEpStato.setLoginOperazione(src.getLoginOperazione());
		siacRCausaleEpStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.setSiacRCausaleEpStatos(new ArrayList<SiacRCausaleEpStato>());
		dest.addSiacRCausaleEpStato(siacRCausaleEpStato);
		
		return dest;
	}



	

}
