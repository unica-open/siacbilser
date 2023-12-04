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
public class TipoBeneCespiteContoMinusvalenzaConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoMinusValenza();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		SiacTPdceConto siacTPdceContoMinusvalenza = new SiacTPdceConto();
		siacTPdceContoMinusvalenza.setUid(conto.getUid());
		dest.setSiacTPdceContoMinusvalenza(siacTPdceContoMinusvalenza);
		dest.setPdceContoMinusvalenzaCode(conto.getCodice());
		dest.setPdceContoMinusvalenzaDesc(conto.getDescrizione());
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		return siacDCespitiBeneTipo.getSiacTPdceContoMinusvalenza();
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING
		Conto contoMinusvalenza = new Conto();
		contoMinusvalenza.setUid(siacTPdceConto.getPdceContoId());
		contoMinusvalenza.setCodice(siacTPdceConto.getPdceContoCode());
		contoMinusvalenza.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoMinusValenza(contoMinusvalenza);
	
	}
	
	


}
