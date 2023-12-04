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
public class TipoBeneCespiteContoIncrementoConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoIncremento();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING AD HOC
//		SiacTPdceConto siacTPdceConto = map(conto, SiacTPdceConto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
		SiacTPdceConto siacTPdceContoIncremento = new SiacTPdceConto();
		siacTPdceContoIncremento.setUid(conto.getUid());
		dest.setSiacTPdceContoIncremento(siacTPdceContoIncremento);
		dest.setPdceContoIncrementoCode(conto.getCodice());
		dest.setPdceContoIncrementoDesc(conto.getDescrizione());
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		return siacDCespitiBeneTipo.getSiacTPdceContoIncremento();
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		//QUESTO DEVE ESSERE FATTO DA UN MAPPING
		Conto contoIncremento = new Conto();
		contoIncremento.setUid(siacTPdceConto.getPdceContoId());
		contoIncremento.setCodice(siacTPdceConto.getPdceContoCode());
		contoIncremento.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoIncremento(contoIncremento);
	
	}
	
	


}
