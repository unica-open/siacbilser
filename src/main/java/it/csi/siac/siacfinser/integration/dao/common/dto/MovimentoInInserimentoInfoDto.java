/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class MovimentoInInserimentoInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//vale solo per inserisci impegno:
	private ArrayList<MovGestInfoDto> elencoInfoAccertamentiCoinvoltiNeiVincoliDaInserire = new ArrayList<MovGestInfoDto>();
	
	
	public MovGestInfoDto findInfoAccertamento(int idMovGest){
		MovGestInfoDto trovato = null;
		if(elencoInfoAccertamentiCoinvoltiNeiVincoliDaInserire!=null && elencoInfoAccertamentiCoinvoltiNeiVincoliDaInserire.size()>0){
			for(MovGestInfoDto it: elencoInfoAccertamentiCoinvoltiNeiVincoliDaInserire){
				if(it.getMovGestId()==idMovGest){
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}


	public ArrayList<MovGestInfoDto> getElencoInfoAccertamentiCoinvolti() {
		return elencoInfoAccertamentiCoinvoltiNeiVincoliDaInserire;
	}


	public void setElencoInfoAccertamentiCoinvolti(
			ArrayList<MovGestInfoDto> elencoInfoAccertamentiCoinvolti) {
		this.elencoInfoAccertamentiCoinvoltiNeiVincoliDaInserire = elencoInfoAccertamentiCoinvolti;
	}
	
	
}
