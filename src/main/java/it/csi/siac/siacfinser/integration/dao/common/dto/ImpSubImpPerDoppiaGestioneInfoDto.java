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
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

public abstract class ImpSubImpPerDoppiaGestioneInfoDto extends OggettoPerDoppiaGestioneInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953900807356949373L;
	
	private HashMap<Integer, LiquidazionePerDoppiaGestioneInfoDto> mappaLiq = new HashMap<Integer, LiquidazionePerDoppiaGestioneInfoDto>();
	
	public void addLiquidazione(Liquidazione liq, BigDecimal deltaImportoSubOrd){
		Integer idLiquidazione = liq.getUid();
		LiquidazionePerDoppiaGestioneInfoDto liquidazioneDaMappa = mappaLiq.get(idLiquidazione);
		if(liquidazioneDaMappa==null){
			liquidazioneDaMappa = new LiquidazionePerDoppiaGestioneInfoDto();
			liquidazioneDaMappa.setLiquidazione(liq);
		}
		liquidazioneDaMappa.addDelta(deltaImportoSubOrd);
		mappaLiq.put(idLiquidazione, liquidazioneDaMappa);
	}	
	
	public ArrayList<LiquidazionePerDoppiaGestioneInfoDto> getLiquidazioni(){
		return StringUtils.hashMapToArrayList(mappaLiq);
	}

}
