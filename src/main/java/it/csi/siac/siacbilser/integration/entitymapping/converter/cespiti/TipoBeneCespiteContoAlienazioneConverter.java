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
public class TipoBeneCespiteContoAlienazioneConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoAlienazione();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		SiacTPdceConto siacTPdceContoAlienazione = new SiacTPdceConto();
		siacTPdceContoAlienazione.setUid(conto.getUid());
		dest.setSiacTPdceContoAlienazione(siacTPdceContoAlienazione);
		dest.setPdceContoAlienazioneCode(conto.getCodice());
		dest.setPdceContoAlienazioneDesc(conto.getDescrizione());
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		return siacDCespitiBeneTipo.getSiacTPdceContoAlienazione();
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING
		Conto contoAlienazione = new Conto();
		contoAlienazione.setUid(siacTPdceConto.getPdceContoId());
		contoAlienazione.setCodice(siacTPdceConto.getPdceContoCode());
		contoAlienazione.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoAlienazione(contoAlienazione);
	
	}
	
	


}
