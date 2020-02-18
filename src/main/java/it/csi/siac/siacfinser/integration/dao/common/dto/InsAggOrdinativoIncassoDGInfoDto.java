/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

public class InsAggOrdinativoIncassoDGInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3145799352842805961L;
	
	private HashMap<Integer, AccertamentoPerDoppiaGestioneInfoDto> mappaAccertamenti = new HashMap<Integer, AccertamentoPerDoppiaGestioneInfoDto>();

	public HashMap<Integer, AccertamentoPerDoppiaGestioneInfoDto> getMappaAccertamenti() {
		return mappaAccertamenti;
	}
	
	public void addAccertamento(Accertamento accertamento, SubAccertamento subAccertamento, BigDecimal deltaImportoSubOrd){
		Integer idAccertamento = accertamento.getUid();
		AccertamentoPerDoppiaGestioneInfoDto accertamentoDaMappa = mappaAccertamenti.get(idAccertamento);
		if(accertamentoDaMappa==null){
			accertamentoDaMappa = new AccertamentoPerDoppiaGestioneInfoDto();
			accertamentoDaMappa.setAccertamento(accertamento);
		}
		accertamentoDaMappa.addDelta(deltaImportoSubOrd);
		if(subAccertamento!=null){
			accertamentoDaMappa.addSubAccertamento(subAccertamento,deltaImportoSubOrd);
		}
		mappaAccertamenti.put(idAccertamento, accertamentoDaMappa);
	}
	
	public ArrayList<AccertamentoPerDoppiaGestioneInfoDto> getAccertamenti(){
		return StringUtils.hashMapToArrayList(mappaAccertamenti);
	}
	
}