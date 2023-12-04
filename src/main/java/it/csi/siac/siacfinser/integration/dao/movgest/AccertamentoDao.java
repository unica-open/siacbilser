/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioAccertamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccSubAccParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccertamentoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdAccertamentoSubAccertamento;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;

public interface AccertamentoDao extends Dao<SiacTMovgestFin, Integer> {

	public List<SiacTMovgestFin> ricercaAccertamenti( Integer enteUid, RicercaAccertamentoParamDto prs);
	
	public List<IdImpegnoSubimpegno>  ricercaAccertamentiSubAccertamenti(Integer enteUid, RicercaAccSubAccParamDto prs, boolean soloAccertamenti);
	
	public BigDecimal calcolaDisponibilitaAIncassare(Integer idMovGestTs);
	
	public BigDecimal calcolaDisponibilitaAPagarePerDodicesimi(Integer elemId);
	
	public ConsultaDettaglioAccertamentoDto consultaDettaglioAccertamento(Integer idMovGestTs);
	
	public List<CodificaImportoDto> calcolaDisponibilitaAIncassareMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs) ;
	
	public List<IdAccertamentoSubAccertamento>  ricercaAccertamentiSubAccertamentiPerOrdinativoIncasso(Integer enteUid, RicercaAccSubAccParamDto prs, boolean soloAccertamenti);

	public List<IdImpegnoSubimpegno> ricercaAccertamentiSubAccertamentiROR(Integer enteUid, RicercaAccSubAccParamDto prs,boolean soloAccertamenti);
		
	public BigDecimal calcolaResiduoAttivo(Integer idMovGestTs, Integer idEnte);
	
	/**
	 * Dall'analisi:
	 * impegni sprovvisti di vincolo esplicito e assunti su un capitolo di spesa facenti parte del Vincolo tra Capitoli 
	 * a cui appartiene anche il capitolo di assunzione dell'accertamento. 
	 * <br>
	 * In pratica:
	 * Dal capitolo EG dell'accertamento  si ricavano i capitoli UG ad esso vincolati, 
	 * si selezionano gli impegni collegati a questi capitoli su componente diversa da fresco e avanzo, 
	 * escludendo sia quelli che hanno un vincolo esplicito con un altro accertamento, che quelli con vincolo esplicito con l'accertamento 
	 * (perche' gia' compresi nel caricamento delle associazioni da vincolo esplicito).
	 * 
	 * a seguito della SIAC-8122 si escludono i sub dall'estrazione
	 *
	 * @param bilElemId the bil elem id
	 * @param macroTipoCodes the macro tipo codes
	 * @param idEnte the id ente
	 * @return the list
	 */
	public List<SiacTMovgestTsFin> ricercaSiacTMovgestTsFinInVincoloImplicitosulCapitolo(Integer bilElemId,List<String> macroTipoCodes, Integer idEnte);

}
