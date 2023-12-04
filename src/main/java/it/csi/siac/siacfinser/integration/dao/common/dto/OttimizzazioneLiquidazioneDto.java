/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneSoggettoFin;

public class OttimizzazioneLiquidazioneDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	private List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto;
	
	//UTILITIES:
	
	public List<SiacRLiquidazioneSoggettoFin> filtraSiacRLiquidazioneSoggettoByLiqId(Integer idLiquidazione){
		List<SiacRLiquidazioneSoggettoFin> filtrati = new ArrayList<SiacRLiquidazioneSoggettoFin>();
		if(idLiquidazione!=null && distintiSiacRLiquidazioneSoggetto!=null && distintiSiacRLiquidazioneSoggetto.size()>0){
			for(SiacRLiquidazioneSoggettoFin it : distintiSiacRLiquidazioneSoggetto){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}

	public List<SiacRLiquidazioneSoggettoFin> getDistintiSiacRLiquidazioneSoggetto() {
		return distintiSiacRLiquidazioneSoggetto;
	}

	public void setDistintiSiacRLiquidazioneSoggetto(
			List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto) {
		this.distintiSiacRLiquidazioneSoggetto = distintiSiacRLiquidazioneSoggetto;
	}
	
	
}
