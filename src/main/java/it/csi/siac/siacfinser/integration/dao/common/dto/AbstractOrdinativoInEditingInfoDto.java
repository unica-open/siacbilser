/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

public class AbstractOrdinativoInEditingInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1243834148389832754L;
	
	private List<MovimentoGestioneLigthDto> listaDistintiAccertamentiAssociatiAQuote;
	private List<MovimentoGestioneSubLigthDto> listaDistintiSubAccertamentiAssociatiAQuote;
	
	public MovimentoGestioneLigthDto getByIdMovgest(Integer idMovgest){
		MovimentoGestioneLigthDto finded = null;
		if(idMovgest!=null && listaDistintiAccertamentiAssociatiAQuote!=null && listaDistintiAccertamentiAssociatiAQuote.size()>0){
			for(MovimentoGestioneLigthDto it: listaDistintiAccertamentiAssociatiAQuote){
				if(it!=null && it.getMovgestId()!=null && it.getMovgestId().intValue()==idMovgest.intValue()){
					finded = it;
					break;
				}
			}
		}
		return finded;
	}

	public List<MovimentoGestioneLigthDto> getListaDistintiAccertamentiAssociatiAQuote() {
		return listaDistintiAccertamentiAssociatiAQuote;
	}

	public void setListaDistintiAccertamentiAssociatiAQuote(
			List<MovimentoGestioneLigthDto> listaDistintiAccertamentiAssociatiAQuote) {
		this.listaDistintiAccertamentiAssociatiAQuote = listaDistintiAccertamentiAssociatiAQuote;
	}

	public List<MovimentoGestioneSubLigthDto> getListaDistintiSubAccertamentiAssociatiAQuote() {
		return listaDistintiSubAccertamentiAssociatiAQuote;
	}

	public void setListaDistintiSubAccertamentiAssociatiAQuote(
			List<MovimentoGestioneSubLigthDto> listaDistintiSubAccertamentiAssociatiAQuote) {
		this.listaDistintiSubAccertamentiAssociatiAQuote = listaDistintiSubAccertamentiAssociatiAQuote;
	}
	
}
