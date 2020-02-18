/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public abstract class MovimentoGestioneLigthAbstractDto implements Serializable {
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = -4331472399472457817L;
	
	protected Integer movgestTsId; 
	
	protected Integer idEnte;
	protected String annoEsercizio;
	protected Integer annoMovimento;
	protected BigDecimal numeroMovimento;
	protected String tipoMovimento;
	
	protected List<ModificaMovimentoGestioneEntrata>  listaModificheMovimentoGestioneEntrata;
	
	protected String statoCode;
	
	protected BigDecimal disponibilitaAIncassare;
	
	protected BigDecimal importoAttuale;
	
	protected Soggetto soggettoMovimento;
	
	protected String soggettoClasseCode;
	
	// SIAC-6695
	protected String motivazioneDisponibilitaAIncassare;
	
	
	//e' la lista di tutti i suoi sub carica da db:
	protected List<MovimentoGestioneSubLigthDto> listaSub;
	
	//liste delle quote associate (ricevute da front-end):
	protected ArrayList<SubOrdinativoIncasso> quoteRicevuteDaFrontEnd;

	
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

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public String getAnnoEsercizio() {
		return annoEsercizio;
	}

	public void setAnnoEsercizio(String annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	public BigDecimal getNumeroMovimento() {
		return numeroMovimento;
	}

	public void setNumeroMovimento(BigDecimal numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}

	public String getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public String getStatoCode() {
		return statoCode;
	}

	public void setStatoCode(String statoCode) {
		this.statoCode = statoCode;
	}

	public List<MovimentoGestioneSubLigthDto> getListaSub() {
		return listaSub;
	}

	public void setListaSub(List<MovimentoGestioneSubLigthDto> listaSub) {
		this.listaSub = listaSub;
	}

	public Integer getMovgestTsId() {
		return movgestTsId;
	}

	public void setMovgestTsId(Integer movgestTsId) {
		this.movgestTsId = movgestTsId;
	}

	public BigDecimal getDisponibilitaAIncassare() {
		return disponibilitaAIncassare;
	}

	public void setDisponibilitaAIncassare(BigDecimal disponibilitaAIncassare) {
		this.disponibilitaAIncassare = disponibilitaAIncassare;
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

	public Soggetto getSoggettoMovimento() {
		return soggettoMovimento;
	}

	public void setSoggettoMovimento(Soggetto soggettoMovimento) {
		this.soggettoMovimento = soggettoMovimento;
	}

	public String getSoggettoClasseCode() {
		return soggettoClasseCode;
	}

	public void setSoggettoClasseCode(String soggettoClasseCode) {
		this.soggettoClasseCode = soggettoClasseCode;
	}

	public BigDecimal getImportoAttuale() {
		return importoAttuale;
	}

	public void setImportoAttuale(BigDecimal importoAttuale) {
		this.importoAttuale = importoAttuale;
	}

	public List<ModificaMovimentoGestioneEntrata> getListaModificheMovimentoGestioneEntrata() {
		return listaModificheMovimentoGestioneEntrata;
	}

	public void setListaModificheMovimentoGestioneEntrata(
			List<ModificaMovimentoGestioneEntrata> listaModificheMovimentoGestioneEntrata) {
		this.listaModificheMovimentoGestioneEntrata = listaModificheMovimentoGestioneEntrata;
	}

	/**
	 * @return the motivazioneDisponibilitaAIncassare
	 */
	public String getMotivazioneDisponibilitaAIncassare() {
		return this.motivazioneDisponibilitaAIncassare;
	}

	/**
	 * @param motivazioneDisponibilitaAIncassare the motivazioneDisponibilitaAIncassare to set
	 */
	public void setMotivazioneDisponibilitaAIncassare(String motivazioneDisponibilitaAIncassare) {
		this.motivazioneDisponibilitaAIncassare = motivazioneDisponibilitaAIncassare;
	}
	
}
