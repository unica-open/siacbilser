/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.StringUtils;

public class EsitoControlliDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Errore> listaErrori;
	private List<Errore> listaWarning;
	
	public boolean presenzaErrori(){
		return !StringUtils.isEmpty(listaErrori);
	}
	
	public List<Errore> getListaErrori(){
		return listaErrori;
	}
	
	public List<Errore> getListaWarning() {
		return listaWarning;
	}
	
	public void addWarning(Errore w){
		if(listaWarning==null){
			listaWarning = new ArrayList<Errore>();
		}
		listaWarning.add(w);
	}
	
	public void addErrore(Errore e){
		if(listaErrori==null){
			listaErrori = new ArrayList<Errore>();
		}
		listaErrori.add(e);
	}
	
	public void setListaErrori(List<Errore> listaErrorie){
		if(listaErrori==null){
			listaErrori = new ArrayList<Errore>();
		}
		listaErrori = CommonUtils.toList(listaErrori,listaErrorie);
	}
	
	public void addWarning(List<Errore> warnings){
		if(listaWarning==null){
			listaWarning = new ArrayList<Errore>();
		}
		listaWarning = CommonUtils.toList(listaWarning,warnings);
	}

}
