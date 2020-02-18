/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDLiquidazioneStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;

@Component
public class LiquidazioneStatoConverter extends DozerConverter<Liquidazione, SiacTLiquidazione> {
	
	private LogUtil log = new LogUtil(this.getClass());
	
	@Autowired
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;

	
	public LiquidazioneStatoConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		String methodName = "convertFrom";
		SiacTLiquidazione siacTLiquidazione = siacTLiquidazioneRepository.findOne(src.getUid());
		
		if(siacTLiquidazione.getSiacRLiquidazioneStatos()==null){
			log.debug(methodName , "siacTLiquidazione.getSiacRLiquidazioneStatos() == null");
			return dest;
		}
		
		for (SiacRLiquidazioneStato r : siacTLiquidazione.getSiacRLiquidazioneStatos()) {
			log.debug(methodName , "SiacRLiquidazioneStato con dataCancellazione: " + r.getDataCancellazione() + " e dataFineValidita: " + r.getDataFineValidita());
			if (r.getDataFineValidita() == null && r.getDataCancellazione() == null) {
				StatoOperativoLiquidazione statoOperativo = SiacDLiquidazioneStatoEnum.byCodice(r.getSiacDLiquidazioneStato().getLiqStatoCode()).getStatoOperativoLiquidazione();
				log.debug(methodName , "trovato statoOperativo: " + statoOperativo.name());
				dest.setStatoOperativoLiquidazione(statoOperativo);
				break;
			}
		}
		return dest;
	}

	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		
		return dest;
	}



	

}
