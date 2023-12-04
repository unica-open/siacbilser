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
public class TipoBeneCespiteContoDecrementoConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoDecremento();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING AD HOC
//		SiacTPdceConto siacTPdceConto = map(conto, SiacTPdceConto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
		SiacTPdceConto siacTPdceContoDecremento = new SiacTPdceConto();
		siacTPdceContoDecremento.setUid(conto.getUid());
		dest.setSiacTPdceContoDecremento(siacTPdceContoDecremento);
		dest.setPdceContoDecrementoCode(conto.getCodice());
		dest.setPdceContoDecrementoDesc(conto.getDescrizione());
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		return siacDCespitiBeneTipo.getSiacTPdceContoDecremento();
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING
		Conto contoDecremento = new Conto();
		contoDecremento.setUid(siacTPdceConto.getPdceContoId());
		contoDecremento.setCodice(siacTPdceConto.getPdceContoCode());
		contoDecremento.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoDecremento(contoDecremento);
	
	}
	
	


}
