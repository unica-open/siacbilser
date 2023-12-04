/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacattser.model.TipoAtto;

public class RicercaAccSubAccParamDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	

	private Integer annoEsercizio;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private Integer numeroUEB;
	private int annoAccertamento;
	private BigDecimal numeroAccertamento;
	private int annoProvvedimento;
	private int numeroProvvedimento;
	private TipoAtto tipoProvvedimento;
	private String codiceDebitore;
	private boolean disponibilitaAdIncassare;
	
	// Aggiunti x estendere la ricerca ai sub
	private String stato;
	private String cig;
	private String cup;
	private boolean competenzaCorrente;
	private boolean competenzaCompetenza;
	private boolean competenzaFuturi;
	//SIAC-6997
	private boolean competenzaResiduiRor;
	private Integer annoAccertamentoRiaccertato;
	private Integer numeroAccertamentoRiaccertato;
	private Integer annoAccertamentoOrigine;
	private Integer numeroAccertamentoOrigine;
	private String flagDaRiaccertamento;
	private String codiceClasseSoggetto;
	private boolean ricercaResiduiRorFlag;
	private Integer uidCapitolo;
	private Integer uidProvvedimento;
	private Integer uidStrutturaAmministrativoContabile;
	
	private String strutturaAmministativaCodice; // FIXME: RM old verificare se serve in altre chiamate	
	
	private String codiceProgetto;
	
	//SIAC-5253 introduciamo la possibilita' di escludere stati precisi:
	private List<String> statiDaEscludere;
	
	//SIAC-6997
	private Boolean reanno;
	private String strutturaSelezionataCompetente;
	
	//SIAC-7486
	private List<Integer> listStrutturaCompetenteInt;
	
	
	/**
	 * @return the reanno
	 */
	public Boolean getReanno() {
		return reanno;
	}
	/**
	 * @param reanno the reanno to set
	 */
	public void setReanno(Boolean reanno) {
		this.reanno = reanno;
	}
	/**
	 * @return the strutturaSelezionataCompetente
	 */
	public String getStrutturaSelezionataCompetente() {
		return strutturaSelezionataCompetente;
	}
	/**
	 * @param strutturaSelezionataCompetente the strutturaSelezionataCompetente to set
	 */
	public void setStrutturaSelezionataCompetente(String strutturaSelezionataCompetente) {
		this.strutturaSelezionataCompetente = strutturaSelezionataCompetente;
	}
	/**
	 * @return the codiceClasseSoggetto
	 */
	public String getCodiceClasseSoggetto() {
		return codiceClasseSoggetto;
	}
	/**
	 * @param codiceClasseSoggetto the codiceClasseSoggetto to set
	 */
	public void setCodiceClasseSoggetto(String codiceClasseSoggetto) {
		this.codiceClasseSoggetto = codiceClasseSoggetto;
	}
	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}
	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}
	/**
	 * @return the cig
	 */
	public String getCig() {
		return cig;
	}
	/**
	 * @param cig the cig to set
	 */
	public void setCig(String cig) {
		this.cig = cig;
	}
	/**
	 * @return the cup
	 */
	public String getCup() {
		return cup;
	}
	/**
	 * @param cup the cup to set
	 */
	public void setCup(String cup) {
		this.cup = cup;
	}
	/**
	 * @return the competenzaCorrente
	 */
	public boolean isCompetenzaCorrente() {
		return competenzaCorrente;
	}
	/**
	 * @param competenzaCorrente the competenzaCorrente to set
	 */
	public void setCompetenzaCorrente(boolean competenzaCorrente) {
		this.competenzaCorrente = competenzaCorrente;
	}
	/**
	 * @return the competenzaCompetenza
	 */
	public boolean isCompetenzaCompetenza() {
		return competenzaCompetenza;
	}
	/**
	 * @param competenzaCompetenza the competenzaCompetenza to set
	 */
	public void setCompetenzaCompetenza(boolean competenzaCompetenza) {
		this.competenzaCompetenza = competenzaCompetenza;
	}
	/**
	 * @return the competenzaFuturi
	 */
	public boolean isCompetenzaFuturi() {
		return competenzaFuturi;
	}
	/**
	 * @param competenzaFuturi the competenzaFuturi to set
	 */
	public void setCompetenzaFuturi(boolean competenzaFuturi) {
		this.competenzaFuturi = competenzaFuturi;
	}
	/**
	 * @return the annoAccertamentoRiaccertato
	 */
	public Integer getAnnoAccertamentoRiaccertato() {
		return annoAccertamentoRiaccertato;
	}
	/**
	 * @param annoAccertamentoRiaccertato the annoAccertamentoRiaccertato to set
	 */
	public void setAnnoAccertamentoRiaccertato(Integer annoAccertamentoRiaccertato) {
		this.annoAccertamentoRiaccertato = annoAccertamentoRiaccertato;
	}
	/**
	 * @return the numeroAccertamentoRiaccertato
	 */
	public Integer getNumeroAccertamentoRiaccertato() {
		return numeroAccertamentoRiaccertato;
	}
	/**
	 * @param numeroAccertamentoRiaccertato the numeroAccertamentoRiaccertato to set
	 */
	public void setNumeroAccertamentoRiaccertato(
			Integer numeroAccertamentoRiaccertato) {
		this.numeroAccertamentoRiaccertato = numeroAccertamentoRiaccertato;
	}
	/**
	 * @return the annoAccertamentoOrigine
	 */
	public Integer getAnnoAccertamentoOrigine() {
		return annoAccertamentoOrigine;
	}
	/**
	 * @param annoAccertamentoOrigine the annoAccertamentoOrigine to set
	 */
	public void setAnnoAccertamentoOrigine(Integer annoAccertamentoOrigine) {
		this.annoAccertamentoOrigine = annoAccertamentoOrigine;
	}
	/**
	 * @return the numeroAccertamentoOrigine
	 */
	public Integer getNumeroAccertamentoOrigine() {
		return numeroAccertamentoOrigine;
	}
	/**
	 * @param numeroAccertamentoOrigine the numeroAccertamentoOrigine to set
	 */
	public void setNumeroAccertamentoOrigine(Integer numeroAccertamentoOrigine) {
		this.numeroAccertamentoOrigine = numeroAccertamentoOrigine;
	}
	/**
	 * @return the flagDaRiaccertamento
	 */
	public String getFlagDaRiaccertamento() {
		return flagDaRiaccertamento;
	}
	/**
	 * @param flagDaRiaccertamento the flagDaRiaccertamento to set
	 */
	public void setFlagDaRiaccertamento(String flagDaRiaccertamento) {
		this.flagDaRiaccertamento = flagDaRiaccertamento;
	}
	/**
	 * @return the uidCapitolo
	 */
	public Integer getUidCapitolo() {
		return uidCapitolo;
	}
	/**
	 * @param uidCapitolo the uidCapitolo to set
	 */
	public void setUidCapitolo(Integer uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}
	/**
	 * @return the uidProvvedimento
	 */
	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}
	/**
	 * @param uidProvvedimento the uidProvvedimento to set
	 */
	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}
	/**
	 * @return the uidStrutturaAmministrativoContabile
	 */
	public Integer getUidStrutturaAmministrativoContabile() {
		return uidStrutturaAmministrativoContabile;
	}
	/**
	 * @param uidStrutturaAmministrativoContabile the uidStrutturaAmministrativoContabile to set
	 */
	public void setUidStrutturaAmministrativoContabile(
			Integer uidStrutturaAmministrativoContabile) {
		this.uidStrutturaAmministrativoContabile = uidStrutturaAmministrativoContabile;
	}
	public int getAnnoAccertamento() {
		return annoAccertamento;
	}
	public void setAnnoAccertamento(int annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	public BigDecimal getNumeroAccertamento() {
		return numeroAccertamento;
	}
	public void setNumeroAccertamento(BigDecimal numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	public String getCodiceDebitore() {
		return codiceDebitore;
	}
	public void setCodiceDebitore(String codiceDebitore) {
		this.codiceDebitore = codiceDebitore;
	}
	public boolean isDisponibilitaAdIncassare() {
		return disponibilitaAdIncassare;
	}
	public void setDisponibilitaAdIncassare(boolean disponibilitaAdIncassare) {
		this.disponibilitaAdIncassare = disponibilitaAdIncassare;
	}
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}
	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}
	public Integer getNumeroArticolo() {
		return numeroArticolo;
	}
	public void setNumeroArticolo(Integer numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}
	public Integer getNumeroUEB() {
		return numeroUEB;
	}
	public void setNumeroUEB(Integer numeroUEB) {
		this.numeroUEB = numeroUEB;
	}
	public int getAnnoProvvedimento() {
		return annoProvvedimento;
	}
	public void setAnnoProvvedimento(int annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}
	public int getNumeroProvvedimento() {
		return numeroProvvedimento;
	}
	public void setNumeroProvvedimento(int numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}
	public TipoAtto getTipoProvvedimento() {
		return tipoProvvedimento;
	}
	public void setTipoProvvedimento(TipoAtto tipoProvvedimento) {
		this.tipoProvvedimento = tipoProvvedimento;
	}
	public String getStrutturaAmministativaCodice() {
		return strutturaAmministativaCodice;
	}
	public void setStrutturaAmministativaCodice(String strutturaAmministativaCodice) {
		this.strutturaAmministativaCodice = strutturaAmministativaCodice;
	}
	public String getCodiceProgetto() {
		return codiceProgetto;
	}
	public void setCodiceProgetto(String codiceProgetto) {
		this.codiceProgetto = codiceProgetto;
	}
	public List<String> getStatiDaEscludere() {
		return statiDaEscludere;
	}
	public void setStatiDaEscludere(List<String> statiDaEscludere) {
		this.statiDaEscludere = statiDaEscludere;
	}
	/**
	 * @return the competenzaResiduiRor
	 */
	public boolean isCompetenzaResiduiRor() {
		return competenzaResiduiRor;
	}
	/**
	 * @param competenzaResiduiRor the competenzaResiduiRor to set
	 */
	public void setCompetenzaResiduiRor(boolean competenzaResiduiRor) {
		this.competenzaResiduiRor = competenzaResiduiRor;
	}
	/**
	 * @return the ricercaResiduiRorFlag
	 */
	public boolean isRicercaResiduiRorFlag() {
		return ricercaResiduiRorFlag;
	}
	/**
	 * @param ricercaResiduiRorFlag the ricercaResiduiRorFlag to set
	 */
	public void setRicercaResiduiRorFlag(boolean ricercaResiduiRorFlag) {
		this.ricercaResiduiRorFlag = ricercaResiduiRorFlag;
	}
	/**
	 * @return the listStrutturaCompetenteInt
	 */
	public List<Integer> getListStrutturaCompetenteInt() {
		return listStrutturaCompetenteInt;
	}
	/**
	 * @param listStrutturaCompetenteInt the listStrutturaCompetenteInt to set
	 */
	public void setListStrutturaCompetenteInt(List<Integer> listStrutturaCompetenteInt) {
		this.listStrutturaCompetenteInt = listStrutturaCompetenteInt;
	}
	
}