/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.SiacTAccFondiDubbiaEsigBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaTipoMediaConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaBaseAttributiBilancioConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaBase<?>, SiacTAccFondiDubbiaEsig> {
	@Autowired private SiacTAccFondiDubbiaEsigBilRepository siacTAccFondiDubbiaEsigBilRepository;

	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	@SuppressWarnings("unchecked")
	public AccantonamentoFondiDubbiaEsigibilitaBaseAttributiBilancioConverter() {
		super((Class<AccantonamentoFondiDubbiaEsigibilitaBase<?>>)(Class<?>)AccantonamentoFondiDubbiaEsigibilitaBase.class, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaBase<?> convertFrom(SiacTAccFondiDubbiaEsig src, AccantonamentoFondiDubbiaEsigibilitaBase<?> dest) {
		if(dest == null || src == null || src.getSiacTAccFondiDubbiaEsigBil() == null || src.getSiacTAccFondiDubbiaEsigBil().getUid() == null) {
			return dest;
		}
		SiacTAccFondiDubbiaEsigBil tafdeb = initSiacTAccFondiDubbiaEsigBil(src.getSiacTAccFondiDubbiaEsigBil());
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = mapNotNull(tafdeb,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class,
				BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail,
				Converters.byModelDetails(Utility.MDTL.byModelDetailClass(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.class)));
		dest.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		return dest;
	}

	private SiacTAccFondiDubbiaEsigBil initSiacTAccFondiDubbiaEsigBil(SiacTAccFondiDubbiaEsigBil src) {
		if(src.getAfdeBilVersione() != null) {
			return src;
		}
		return siacTAccFondiDubbiaEsigBilRepository.findOne(src.getUid());
	}

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(AccantonamentoFondiDubbiaEsigibilitaBase<?> src, SiacTAccFondiDubbiaEsig dest) {
		if(dest == null || src == null || src.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() == null || src.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid() == 0) {
			return dest;
		}
		SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil = new SiacTAccFondiDubbiaEsigBil();
		siacTAccFondiDubbiaEsigBil.setUid(src.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid());
		dest.setSiacTAccFondiDubbiaEsigBil(siacTAccFondiDubbiaEsigBil);
		
		return dest;
	}

}
