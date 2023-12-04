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
public class TipoBeneCespiteContoAmmortamentoConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoAmmortamento();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		SiacTPdceConto siacTPdceContoAmmortamento = new SiacTPdceConto();
		siacTPdceContoAmmortamento.setUid(conto.getUid());
		dest.setSiacTPdceContoAmmortamento(siacTPdceContoAmmortamento);
		dest.setPdceContoAmmortamentoCode(conto.getCodice());
		dest.setPdceContoAmmortamentoDesc(conto.getDescrizione());
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		return siacDCespitiBeneTipo.getSiacTPdceContoAmmortamento();
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		Conto contoAmmortamento = new Conto();
		contoAmmortamento.setUid(siacTPdceConto.getPdceContoId());
		contoAmmortamento.setCodice(siacTPdceConto.getPdceContoCode());
		contoAmmortamento.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoAmmortamento(contoAmmortamento);
	
	}
	
	


}
