/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;

public class MovimentoGestioneLigthDto extends MovimentoGestioneLigthAbstractDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4331472399472457817L;
	
	private Integer movgestId;
	
	//e' la lista di tutti i suoi sub carica da db:
	private List<MovimentoGestioneSubLigthDto> listaSub;
	
	//liste delle quote associate (ricevute da front-end):
	private ArrayList<SubOrdinativoIncasso> quoteRicevuteDaFrontEnd;
	
//	private BigDecimal disponibilitaASubaccertare;
	

	public MovimentoGestioneLigthDto(Integer idEnte,String annoEsercizio,Integer annoMovimento,BigDecimal numeroMovimento,String tipoMovimento){
		setAnnoEsercizio(annoEsercizio);
		setAnnoMovimento(annoMovimento);
		setIdEnte(idEnte);
		setNumeroMovimento(numeroMovimento);
		setTipoMovimento(tipoMovimento);
	}
	
	public MovimentoGestioneSubLigthDto getSubByNumero(BigDecimal numeroSub){
		MovimentoGestioneSubLigthDto trovato = null;
		if(listaSub!=null && listaSub.size()>0 && numeroSub!=null){
			for(MovimentoGestioneSubLigthDto subIt : listaSub){
				if(subIt!=null && subIt.getNumeroMovimento()!=null){
					if(subIt.getNumeroMovimento().intValue()==numeroSub.intValue()){
						trovato = subIt;
						break;
					}
				}
			}
		}
		return trovato;
	}

	public List<MovimentoGestioneSubLigthDto> getListaSub() {
		return listaSub;
	}

	public void setListaSub(List<MovimentoGestioneSubLigthDto> listaSub) {
		this.listaSub = listaSub;
	}

	public Integer getMovgestId() {
		return movgestId;
	}

	public void setMovgestId(Integer movgestId) {
		this.movgestId = movgestId;
	}

	public ArrayList<SubOrdinativoIncasso> getQuoteRicevuteDaFrontEnd() {
		return quoteRicevuteDaFrontEnd;
	}

	public void setQuoteRicevuteDaFrontEnd(
			ArrayList<SubOrdinativoIncasso> quoteRicevuteDaFrontEnd) {
		this.quoteRicevuteDaFrontEnd = quoteRicevuteDaFrontEnd;
	}
	
	public void addQuotaRicevutaDaFrontEnd(SubOrdinativoIncasso quota){
		if(this.quoteRicevuteDaFrontEnd==null){
			this.quoteRicevuteDaFrontEnd = new ArrayList<SubOrdinativoIncasso>();
		}
		this.quoteRicevuteDaFrontEnd.add(quota);
	}

//	public BigDecimal getDisponibilitaASubaccertare() {
//		return disponibilitaASubaccertare;
//	}
//
//	public void setDisponibilitaASubaccertare(BigDecimal disponibilitaASubaccertare) {
//		this.disponibilitaASubaccertare = disponibilitaASubaccertare;
//	}
	
}
