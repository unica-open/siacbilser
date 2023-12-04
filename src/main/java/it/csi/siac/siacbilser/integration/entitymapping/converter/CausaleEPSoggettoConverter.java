/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * The Class CausaleEPSoggettoConverter.
 *
 * @author Domenico
 */
@Component
public class CausaleEPSoggettoConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {
	
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	

	public CausaleEPSoggettoConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRCausaleEpSoggettos()!=null){
			for(SiacRCausaleEpSoggetto r : src.getSiacRCausaleEpSoggettos()){
				if(r.getDataCancellazione()!=null
						|| !r.isDataValiditaCompresa(dest.getDataInizioValiditaFiltro())){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(r.getSiacTSoggetto().getUid());
				
				Soggetto soggetto = new Soggetto();
				map(siacTSoggetto, soggetto, BilMapId.SiacTSoggetto_Soggetto);
				dest.setSoggetto(soggetto);
				
				log.debug(methodName, "impostato Soggetto [uid:"+soggetto.getUid()+"]");
				
			}
		}
		
		return dest;
	}

	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
		final String methodName = "convertTo";
		
		if(src.getSoggetto()==null || src.getSoggetto().getUid()==0){
			return dest;
		}
		
		SiacRCausaleEpSoggetto siacRCausaleEpSoggetto = new SiacRCausaleEpSoggetto();
		
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSoggetto().getUid());
		siacRCausaleEpSoggetto.setSiacTSoggetto(siacTSoggetto);
		
		log.debug(methodName, "aggiunto Soggetto [uid:"+src.getSoggetto().getUid()+"]");
		
		siacRCausaleEpSoggetto.setLoginOperazione(src.getLoginOperazione());
		siacRCausaleEpSoggetto.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.setSiacRCausaleEpSoggettos(new ArrayList<SiacRCausaleEpSoggetto>());
		dest.addSiacRCausaleEpSoggetto(siacRCausaleEpSoggetto);
		
		return dest;
	}



	

}
