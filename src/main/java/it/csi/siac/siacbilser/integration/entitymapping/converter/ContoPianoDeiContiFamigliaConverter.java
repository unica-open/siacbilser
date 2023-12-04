/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRPdceContoSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.Conto;

@Component
public class ContoPianoDeiContiFamigliaConverter extends ExtendedDozerConverter<Conto, SiacTPdceConto> {
	

	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	
	public ContoPianoDeiContiFamigliaConverter() {
		super(Conto.class, SiacTPdceConto.class);
	}

	@Override
	public Conto convertFrom(SiacTPdceConto src, Conto dest) {
		
		if(src.getSiacRPdceContoSoggettos()!=null){
			for(SiacRPdceContoSoggetto siacRPdceContoClass : src.getSiacRPdceContoSoggettos()){
				if(siacRPdceContoClass.getDataCancellazione()!=null  
						|| !siacRPdceContoClass.isDataValiditaCompresa(dest.getDataInizioValiditaFiltro()) ){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(siacRPdceContoClass.getSiacTSoggetto().getUid());
				
				Soggetto soggetto = new Soggetto();
				map(siacTSoggetto, soggetto, BilMapId.SiacTSoggetto_Soggetto);
				dest.setSoggetto(soggetto);
							
			}	
		}
		
		return dest;
	}
	

	@Override
	public SiacTPdceConto convertTo(Conto src, SiacTPdceConto dest) {
		
		if(src.getSoggetto()==null || src.getSoggetto().getUid()==0){
			return dest;
		}
		
		dest.setSiacRPdceContoSoggettos(new ArrayList<SiacRPdceContoSoggetto>());
		
		SiacRPdceContoSoggetto siacRPdceContoSoggetto = new SiacRPdceContoSoggetto();
		
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSoggetto().getUid());
		siacRPdceContoSoggetto.setSiacTSoggetto(siacTSoggetto);
		siacRPdceContoSoggetto.setSiacTPdceConto(dest);
		
		siacRPdceContoSoggetto.setLoginOperazione(dest.getLoginOperazione());
		siacRPdceContoSoggetto.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPdceContoSoggetto(siacRPdceContoSoggetto);
		
		
		return dest;
	}



}
