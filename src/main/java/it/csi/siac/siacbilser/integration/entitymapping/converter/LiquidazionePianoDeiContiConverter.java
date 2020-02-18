/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
/**
 * The Class LiquidazioneSoggettoConverter.
 */
@Component
public class LiquidazionePianoDeiContiConverter extends ExtendedDozerConverter<Liquidazione, SiacTLiquidazione >{

	@Autowired
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;
	
	protected LiquidazionePianoDeiContiConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		if(src.getSiacRLiquidazioneClasses() == null){
			return dest;
		}
		List<String> listaCodici = Arrays.asList(TipologiaClassificatore.PDC.name(), TipologiaClassificatore.PDC_I.name(), TipologiaClassificatore.PDC_II.name(),
				TipologiaClassificatore.PDC_III.name(), TipologiaClassificatore.PDC_IV.name(), TipologiaClassificatore.PDC_V.name());
		SiacTClass siacTClass = siacTLiquidazioneRepository.findSiacTClassByLiqIdECodiciTipo(src.getUid(), listaCodici);
		if(siacTClass == null) {			
			return dest;
		}
		dest.setCodPdc(siacTClass.getClassifCode());
		dest.setDescPdc(siacTClass.getClassifDesc());
		
		return dest;
	}

	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		return dest;
	}

	
}
