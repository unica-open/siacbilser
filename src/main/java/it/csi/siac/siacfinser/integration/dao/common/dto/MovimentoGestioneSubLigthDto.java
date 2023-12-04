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

public class MovimentoGestioneSubLigthDto extends MovimentoGestioneLigthAbstractDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4331472399472457817L;
	
	private Integer movgestTsIdPadre; 
	private Integer movgestIdPadre; 
	
	//liste delle quote associate (ricevute da front-end):
	private ArrayList<SubOrdinativoIncasso> quoteRicevuteDaFrontEnd;

	public MovimentoGestioneSubLigthDto(Integer idEnte,String annoEsercizio,Integer annoMovimento,BigDecimal numeroMovimento,String tipoMovimento){
		setAnnoEsercizio(annoEsercizio);
		setAnnoMovimento(annoMovimento);
		setIdEnte(idEnte);
		setNumeroMovimento(numeroMovimento);
		setTipoMovimento(tipoMovimento);
	}

	public ArrayList<SubOrdinativoIncasso> getQuoteRicevuteDaFrontEnd() {
		return quoteRicevuteDaFrontEnd;
	}

	public void setQuoteRicevuteDaFrontEnd(ArrayList<SubOrdinativoIncasso> quoteRicevuteDaFrontEnd) {
		this.quoteRicevuteDaFrontEnd = quoteRicevuteDaFrontEnd;
	}
	
	public void addQuotaRicevutaDaFrontEnd(SubOrdinativoIncasso quota){
		if(this.quoteRicevuteDaFrontEnd==null){
			this.quoteRicevuteDaFrontEnd = new ArrayList<SubOrdinativoIncasso>();
		}
		this.quoteRicevuteDaFrontEnd.add(quota);
	}

	public Integer getMovgestTsIdPadre() {
		return movgestTsIdPadre;
	}

	public void setMovgestTsIdPadre(Integer movgestTsIdPadre) {
		this.movgestTsIdPadre = movgestTsIdPadre;
	}
	
	public static List<MovimentoGestioneSubLigthDto> getSubDelloStessoAccertamento(List<MovimentoGestioneSubLigthDto> listaDistintiSub, MovimentoGestioneSubLigthDto sub){
		List<MovimentoGestioneSubLigthDto> trovati = new ArrayList<MovimentoGestioneSubLigthDto>();
		if(listaDistintiSub!=null && listaDistintiSub.size()>0 && sub!=null && sub.getMovgestTsIdPadre()!=null){
			for(MovimentoGestioneSubLigthDto subIt : listaDistintiSub){
				if(subIt!=null && subIt.getMovgestTsIdPadre()!=null){
					if(sub.getMovgestTsIdPadre().intValue()==subIt.getMovgestTsIdPadre().intValue()){
						trovati.add(subIt);
					}
				}
			}
		}
		return trovati;
	}

	public Integer getMovgestIdPadre() {
		return movgestIdPadre;
	}

	public void setMovgestIdPadre(Integer movgestIdPadre) {
		this.movgestIdPadre = movgestIdPadre;
	}
	
}
