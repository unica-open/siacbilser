/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.BaseDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioImpegnoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpSubParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpegnoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;
import it.csi.siac.siacfinser.integration.entity.ext.IdMovgestSubmovegest;

public interface ImpegnoDao extends BaseDao {

	
	public List<SiacTMovgestFin> ricercaImpegni(Integer enteUid, RicercaImpegnoParamDto prs);
	
	public List<IdImpegnoSubimpegno>  ricercaImpegniSubImpegni(Integer enteUid, RicercaImpSubParamDto prs, boolean soloImpegni);
	
	public List<IdMovgestSubmovegest> ricercaImpegniSubImpegniPerVociMutuo(Integer idMutuo, Integer enteUid, RicercaImpSubParamDto prs, boolean soloImpegni);
	
	public BigDecimal calcolaDisponibilitaALiquidare(Integer idMovGestTs);
	public List<CodificaImportoDto> calcolaDisponibilitaALiquidareMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs);
	
	public List<CodificaImportoDto> calcolaSommaImportiVociMutuo(List<Integer> idMovGestTsList);
	
	
	public BigDecimal calcolaDisponibilitaAImpegnarePerDodicesimi(Integer elemId);
	
	public ConsultaDettaglioImpegnoDto consultaDettaglioImpegno(Integer idMovGestTs);
	
	public List<CodificaImportoDto> calcolaDisponibileAPagareMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs);
	public BigDecimal calcolaDisponibileAPagare(Integer idMovGestTs);
	
}
