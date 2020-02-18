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
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

public class ImpegnoPerDoppiaGestioneInfoDto extends ImpSubImpPerDoppiaGestioneInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953900807356949373L;
	
	private Impegno impegno;	
	
	private HashMap<Integer, SubImpegnoPerDoppiaGestioneInfoDto> mappaSub = new HashMap<Integer, SubImpegnoPerDoppiaGestioneInfoDto>();
	
	public Impegno getImpegno() {
		return impegno;
	}

	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	public void addSubImpegno(SubImpegno subImp, Liquidazione liq, BigDecimal deltaImportoSubOrd){
		Integer idSubImpegno = subImp.getUid();
		SubImpegnoPerDoppiaGestioneInfoDto impegnoDaMappa = mappaSub.get(idSubImpegno);
		if(impegnoDaMappa==null){
			impegnoDaMappa = new SubImpegnoPerDoppiaGestioneInfoDto();
			impegnoDaMappa.setSubImpegno(subImp);
		}
		impegnoDaMappa.addDelta(deltaImportoSubOrd);
		impegnoDaMappa.addLiquidazione(liq, deltaImportoSubOrd);
		mappaSub.put(idSubImpegno, impegnoDaMappa);
	}	
	
	public ArrayList<SubImpegnoPerDoppiaGestioneInfoDto> getSubImpegni(){
		return StringUtils.hashMapToArrayList(mappaSub);
	}
	
	/**
	 * Restituisce le liquidazioni direttamente o indirettamente legate all'impegno (cioe' se non ne ha direte prende quelle dei suoi sub-impegni)
	 * @return
	 */
	public ArrayList<LiquidazionePerDoppiaGestioneInfoDto> getAllLiquidazioni(){
		ArrayList<LiquidazionePerDoppiaGestioneInfoDto> listaAllLiquidazioni = new ArrayList<LiquidazionePerDoppiaGestioneInfoDto>();
		ArrayList<SubImpegnoPerDoppiaGestioneInfoDto> listaSubImpegni = getSubImpegni();
		if(listaSubImpegni!=null && listaSubImpegni.size()>0){
			for(SubImpegnoPerDoppiaGestioneInfoDto subImpegno : listaSubImpegni){
				if(subImpegno!=null){
					ArrayList<LiquidazionePerDoppiaGestioneInfoDto> listaLiquidazioni = subImpegno.getLiquidazioni();
					listaAllLiquidazioni.addAll(listaLiquidazioni);
				}
			}
		} else {
			listaAllLiquidazioni.addAll(getLiquidazioni());
		}
		return listaAllLiquidazioni;
	}

}
