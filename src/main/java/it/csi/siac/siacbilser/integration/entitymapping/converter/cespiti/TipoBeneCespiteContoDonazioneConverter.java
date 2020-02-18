/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.Date;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.Conto;

/**
 * The Class TipoBeneCespitiCategoriaCespitiConverter.
 *
 * @author Anto
 */
public class TipoBeneCespiteContoDonazioneConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoDonazione();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING AD HOC
//		SiacTPdceConto siacTPdceConto = map(conto, SiacTPdceConto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
		SiacTPdceConto siacTPdceContoDonazione = new SiacTPdceConto();
		siacTPdceContoDonazione.setUid(conto.getUid());
		dest.setSiacTPdceContoDonazione(siacTPdceContoDonazione);
		dest.setPdceContoDonazioneCode(conto.getCodice());
		dest.setPdceContoDonazioneDesc(conto.getDescrizione());
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		return siacDCespitiBeneTipo.getSiacTPdceContoDonazione();
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING
		Conto contoDonazione = new Conto();
		contoDonazione.setUid(siacTPdceConto.getPdceContoId());
		contoDonazione.setCodice(siacTPdceConto.getPdceContoCode());
		contoDonazione.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoDonazione(contoDonazione);
	
	}
	
	


}
