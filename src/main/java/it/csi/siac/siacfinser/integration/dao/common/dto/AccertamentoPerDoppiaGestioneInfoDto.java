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

public class AccertamentoPerDoppiaGestioneInfoDto extends OggettoPerDoppiaGestioneInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953900807356949373L;
	
	private Accertamento accertamento;	
	
	private HashMap<Integer, SubAccertamentoPerDoppiaGestioneInfoDto> mappaSub = new HashMap<Integer, SubAccertamentoPerDoppiaGestioneInfoDto>();

	public void addSubAccertamento(SubAccertamento subAcc, BigDecimal deltaImportoSubOrd){
		Integer idSubAccertamento = subAcc.getUid();
		SubAccertamentoPerDoppiaGestioneInfoDto accertamentoDaMappa = mappaSub.get(idSubAccertamento);
		if(accertamentoDaMappa==null){
			accertamentoDaMappa = new SubAccertamentoPerDoppiaGestioneInfoDto();
			accertamentoDaMappa.setSubAccertamento(subAcc);
		}
		accertamentoDaMappa.addDelta(deltaImportoSubOrd);
		mappaSub.put(idSubAccertamento, accertamentoDaMappa);
	}	
	
	public ArrayList<SubAccertamentoPerDoppiaGestioneInfoDto> getSubAccertamenti(){
		return StringUtils.hashMapToArrayList(mappaSub);
	}

	public Accertamento getAccertamento() {
		return accertamento;
	}

	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

}
