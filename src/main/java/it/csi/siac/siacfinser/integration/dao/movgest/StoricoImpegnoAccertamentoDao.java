/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaStoricoImpegnoAccertamentoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStoricoImpAccFin;

public interface StoricoImpegnoAccertamentoDao extends Dao<SiacRMovgestTsStoricoImpAccFin, Integer> {

	public List<SiacRMovgestTsStoricoImpAccFin> ricercaStoricoImpegnoAccertamento(Integer enteUid, RicercaStoricoImpegnoAccertamentoParamDto prop, int numeroPagina, int numeroRisultatiPerPagina);

	public Integer contaStorico(Integer enteUid,RicercaStoricoImpegnoAccertamentoParamDto paramDto);
	
	public SiacRMovgestTsStoricoImpAccFin findSiacRMovgestTsStoricoImpAccCorrispondenteInAnnoBilancio(Integer movgestTsRStoricoId, String anno, boolean storicizzazioneSuSubAccertamento);
	
	
}
