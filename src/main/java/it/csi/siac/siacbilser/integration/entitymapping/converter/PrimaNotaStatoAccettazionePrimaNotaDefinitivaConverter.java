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
import it.csi.siac.siacbilser.integration.entity.SiacDPnDefAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPnDefAccettazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPnDefAccettazioneStatoEnum;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;

/**
 * The Class PrimaNotaClassificatoreGSAConverter.
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2018
 */
@Component
public class PrimaNotaStatoAccettazionePrimaNotaDefinitivaConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	
	
	@Autowired
	private EnumEntityFactory eef;
	
	public PrimaNotaStatoAccettazionePrimaNotaDefinitivaConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		
		if(src.getSiacRPnDefAccettazioneStatos() == null || src.getSiacRPnDefAccettazioneStatos().isEmpty() ) {
			return dest;
		}
		
		for (SiacRPnDefAccettazioneStato siacRPnDefAccettazioneStato : src.getSiacRPnDefAccettazioneStatos()) {
			if(siacRPnDefAccettazioneStato.getDataCancellazione() == null) {
				SiacDPnDefAccettazioneStatoEnum siacDPrimaNotaStatoEnum = SiacDPnDefAccettazioneStatoEnum.byCodice(siacRPnDefAccettazioneStato.getSiacDPnDefAccettazioneStato().getPnStaAccDefCode());
				StatoAccettazionePrimaNotaDefinitiva statoOperativoPrimaNota = siacDPrimaNotaStatoEnum.getStatoAccettazioneDef();
				dest.setStatoAccettazionePrimaNotaDefinitiva(statoOperativoPrimaNota);
				break;
			}
			
		}
		return dest;
	}


	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		if(src.getStatoAccettazionePrimaNotaDefinitiva() == null) {
			return dest;
		}
		SiacDPnDefAccettazioneStatoEnum siacDPrimaNotaStatoEnum = SiacDPnDefAccettazioneStatoEnum.byStatoAccettazioneDef(src.getStatoAccettazionePrimaNotaDefinitiva());
		SiacDPnDefAccettazioneStato siacDPrimaNotaStato = eef.getEntity(siacDPrimaNotaStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDPnDefAccettazioneStato.class);
		
		List<SiacRPnDefAccettazioneStato> siacRPnDefAccettazioneStatos = new ArrayList<SiacRPnDefAccettazioneStato>();
		SiacRPnDefAccettazioneStato siacRPnDefAccettazioneStato = new SiacRPnDefAccettazioneStato();
		
		siacRPnDefAccettazioneStato.setSiacDPnDefAccettazioneStato(siacDPrimaNotaStato);
		siacRPnDefAccettazioneStato.setSiacTPrimaNota(dest);
		siacRPnDefAccettazioneStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRPnDefAccettazioneStato.setLoginOperazione(dest.getLoginOperazione());
		
		siacRPnDefAccettazioneStatos.add(siacRPnDefAccettazioneStato);
		dest.setSiacRPnDefAccettazioneStatos(siacRPnDefAccettazioneStatos);
		return dest;
	}

}
