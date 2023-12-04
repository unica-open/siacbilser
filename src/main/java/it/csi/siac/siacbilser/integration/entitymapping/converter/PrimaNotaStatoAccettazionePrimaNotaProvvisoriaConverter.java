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
import it.csi.siac.siacbilser.integration.entity.SiacDPnProvAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPnProvAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPnProvAccettazioneStatoEnum;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;

/**
 * The Class PrimaNotaClassificatoreGSAConverter.
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2018
 */
@Component
public class PrimaNotaStatoAccettazionePrimaNotaProvvisoriaConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	
	
	@Autowired
	private EnumEntityFactory eef;
	
	public PrimaNotaStatoAccettazionePrimaNotaProvvisoriaConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		
		if(src.getSiacRPnProvAccettazioneStatos() == null || src.getSiacRPnProvAccettazioneStatos().isEmpty() ) {
			return dest;
		}
		
		for (SiacRPnProvAccettazioneStato siacRPnProvAccettazioneStato : src.getSiacRPnProvAccettazioneStatos()) {
			if(siacRPnProvAccettazioneStato.getDataCancellazione() == null) {
				SiacDPnProvAccettazioneStatoEnum siacDPrimaNotaStatoEnum = SiacDPnProvAccettazioneStatoEnum.byCodice(siacRPnProvAccettazioneStato.getSiacDPnProvAccettazioneStato().getPnStaAccProvCode());
				StatoAccettazionePrimaNotaProvvisoria statoOperativoPrimaNota = siacDPrimaNotaStatoEnum.getStatoAccettazioneProv();
				dest.setStatoAccettazionePrimaNotaProvvisoria(statoOperativoPrimaNota);
				break;
			}
			
		}
		return dest;
	}


	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		if(src.getStatoAccettazionePrimaNotaProvvisoria() == null) {
			return dest;
		}
		
		SiacDPnProvAccettazioneStatoEnum siacDPrimaNotaStatoEnum = SiacDPnProvAccettazioneStatoEnum.byStatoAccettazioneProv(src.getStatoAccettazionePrimaNotaProvvisoria());
		SiacDPnProvAccettazioneStato siacDPrimaNotaStato = eef.getEntity(siacDPrimaNotaStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDPnProvAccettazioneStato.class);
		
		List<SiacRPnProvAccettazioneStato> siacRPnProvAccettazioneStatos = new ArrayList<SiacRPnProvAccettazioneStato>();
		SiacRPnProvAccettazioneStato siacRPnProvAccettazioneStato = new SiacRPnProvAccettazioneStato();
		
		siacRPnProvAccettazioneStato.setSiacDPnProvAccettazioneStato(siacDPrimaNotaStato);
		siacRPnProvAccettazioneStato.setSiacTPrimaNota(dest);
		siacRPnProvAccettazioneStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRPnProvAccettazioneStato.setLoginOperazione(dest.getLoginOperazione());
		
		siacRPnProvAccettazioneStatos.add(siacRPnProvAccettazioneStato);
		dest.setSiacRPnProvAccettazioneStatos(siacRPnProvAccettazioneStatos);
		return dest;
	}

}
