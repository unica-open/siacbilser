/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneBeneficiario;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;

/**
 * The Class ConciliazionePerCapitoloSoggettoConverter.
 */
@Component
public class ConciliazionePerBeneficiarioSoggettoConverter extends ExtendedDozerConverter<ConciliazionePerBeneficiario, SiacRConciliazioneBeneficiario> {
	
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	
	/**
	 * Instantiates a new conciliazione per capitolo classificatore converter.
	 */
	public ConciliazionePerBeneficiarioSoggettoConverter() {
		super(ConciliazionePerBeneficiario.class, SiacRConciliazioneBeneficiario.class);
	}

	@Override
	public ConciliazionePerBeneficiario convertFrom(SiacRConciliazioneBeneficiario src, ConciliazionePerBeneficiario dest) {
		if(src.getSiacTSoggetto() == null) {
			return dest;
		}
		
		SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(src.getSiacTSoggetto().getUid());
		Soggetto soggetto = mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
		
		dest.setSoggetto(soggetto);
		return dest;
	}

	@Override
	public SiacRConciliazioneBeneficiario convertTo(ConciliazionePerBeneficiario src, SiacRConciliazioneBeneficiario dest) {
		if(src.getSoggetto() == null || src.getSoggetto().getUid() == 0) {
			return dest;
		}
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSoggetto().getUid());
		dest.setSiacTSoggetto(siacTSoggetto);
		
		return dest;
	}

}
