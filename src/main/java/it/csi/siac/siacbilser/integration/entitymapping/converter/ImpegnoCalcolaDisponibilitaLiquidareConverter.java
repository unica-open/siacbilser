/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.helper.DisponibilitaLiquidareImpegnoHelper;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * The Class AccertamentoCalcolaDisponibilitaIncassareConverter.
 */
@Component
public class ImpegnoCalcolaDisponibilitaLiquidareConverter extends MovimentoGestioneCalcolaDisponibilitaBaseConverter<Impegno, SiacTMovgest> {

	@Autowired
	private ApplicationContext appCtx;
	private DisponibilitaLiquidareImpegnoHelper disponibilitaLiquidareImpegnoHelper;

	public ImpegnoCalcolaDisponibilitaLiquidareConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}
	
	@Override
	public Impegno convertFrom(SiacTMovgest src, Impegno dest) {
		SiacTMovgestT tmt = ottieniTestata(src);
		calcolaDisponibilita(dest, tmt.getUid());
		return dest;
	}

	protected void calcolaDisponibilita(Impegno dest, Integer uid) {
		
		if(disponibilitaLiquidareImpegnoHelper == null) {
			synchronized(this) {
				if(disponibilitaLiquidareImpegnoHelper == null) {
					disponibilitaLiquidareImpegnoHelper = new DisponibilitaLiquidareImpegnoHelper(appCtx);
					disponibilitaLiquidareImpegnoHelper.init();
				}
			}
		}
		
		Bilancio bilancio = Utility.BTL.get();
		
		if (bilancio == null) {
			throw new IllegalStateException("parametro bilancio non inizializzato per calcolo disponibilita' impegno");
		}
		
		Integer annoBilancio = bilancio.getAnno();
		
		DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = disponibilitaLiquidareImpegnoHelper.calcolaDisponibilitaALiquidare(uid,annoBilancio);
		
//		BigDecimal disponibilitaLiquidare = calcolaDisponibilita(uid, FUNCTION_NAME);
		dest.setDisponibilitaLiquidare(disponibilitaLiquidareContainer.getDisponibilita());
		// SIAC-6695
		dest.setMotivazioneDisponibilitaLiquidare(disponibilitaLiquidareContainer.getMotivazione());
	}
	
}
