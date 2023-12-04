/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

public class InsAggOrdinativoPagamentoDGInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953900807356949373L;
	
	private HashMap<Integer, ImpegnoPerDoppiaGestioneInfoDto> mappaImpegni = new HashMap<Integer, ImpegnoPerDoppiaGestioneInfoDto>();

	public HashMap<Integer, ImpegnoPerDoppiaGestioneInfoDto> getMappaImpegni() {
		return mappaImpegni;
	}
	
	public void addImpegno(Impegno imp, SubImpegno subImp, Liquidazione liq, BigDecimal deltaImportoSubOrd){
		Integer idImpegno = imp.getUid();
		ImpegnoPerDoppiaGestioneInfoDto impegnoDaMappa = mappaImpegni.get(idImpegno);
		if(impegnoDaMappa==null){
			impegnoDaMappa = new ImpegnoPerDoppiaGestioneInfoDto();
			impegnoDaMappa.setImpegno(imp);
		}
		impegnoDaMappa.addDelta(deltaImportoSubOrd);
		if(subImp!=null){
			impegnoDaMappa.addSubImpegno(subImp, liq,deltaImportoSubOrd);
		} else {
			//ha liq dirette
			impegnoDaMappa.addLiquidazione(liq,deltaImportoSubOrd);
		}
		mappaImpegni.put(idImpegno, impegnoDaMappa);
	}
	
	public ArrayList<ImpegnoPerDoppiaGestioneInfoDto> getImpegni(){
		return StringUtilsFin.hashMapToArrayList(mappaImpegni);
	}
	
}