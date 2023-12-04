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
public class TipoBeneCespiteContoPlusvalenzaConverter extends BaseTipoBeneCespiteContoConverter {

	@Override
	protected Conto getConto(TipoBeneCespite tipoBene) {
		return tipoBene.getContoPlusvalenza();
	}

	@Override
	protected void impostaSiacTPdceConto(SiacDCespitiBeneTipo dest, Conto conto) {
		SiacTPdceConto siacTPdceContoPlusvalenza = new SiacTPdceConto();
		siacTPdceContoPlusvalenza.setUid(conto.getUid());
		dest.setSiacTPdceContoPlusvalenza(siacTPdceContoPlusvalenza);
		dest.setPdceContoPlusvalenzaCode(conto.getCodice());
		dest.setPdceContoPlusvalenzaDesc(conto.getDescrizione());
		
	}

	@Override
	protected SiacTPdceConto getSiacTPdceConto(SiacDCespitiBeneTipo siacDCespitiBeneTipo, Date dataInizioValiditaFiltro) {
		return siacDCespitiBeneTipo.getSiacTPdceContoPlusvalenza();
	}

	@Override
	protected void impostaConto(TipoBeneCespite dest, SiacTPdceConto siacTPdceConto) {
		Conto contoPlusvalenza = new Conto();
		contoPlusvalenza.setUid(siacTPdceConto.getPdceContoId());
		contoPlusvalenza.setCodice(siacTPdceConto.getPdceContoCode());
		contoPlusvalenza.setDescrizione(siacTPdceConto.getPdceContoDesc());
		dest.setContoPlusvalenza(contoPlusvalenza);
	
	}
	
	


}
