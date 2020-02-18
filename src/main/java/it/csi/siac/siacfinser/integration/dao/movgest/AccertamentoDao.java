/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.BaseDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioAccertamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccSubAccParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccertamentoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdAccertamentoSubAccertamento;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;

public interface AccertamentoDao extends BaseDao{

	public List<SiacTMovgestFin> ricercaAccertamenti( Integer enteUid, RicercaAccertamentoParamDto prs);
	
	public List<IdImpegnoSubimpegno>  ricercaAccertamentiSubAccertamenti(Integer enteUid, RicercaAccSubAccParamDto prs, boolean soloAccertamenti);
	
	public BigDecimal calcolaDisponibilitaAIncassare(Integer idMovGestTs);
	
	public BigDecimal calcolaDisponibilitaAPagarePerDodicesimi(Integer elemId);
	
	public ConsultaDettaglioAccertamentoDto consultaDettaglioAccertamento(Integer idMovGestTs);
	
	public List<CodificaImportoDto> calcolaDisponibilitaAIncassareMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs) ;
	
	public List<IdAccertamentoSubAccertamento>  ricercaAccertamentiSubAccertamentiPerOrdinativoIncasso(Integer enteUid, RicercaAccSubAccParamDto prs, boolean soloAccertamenti);
		
}
