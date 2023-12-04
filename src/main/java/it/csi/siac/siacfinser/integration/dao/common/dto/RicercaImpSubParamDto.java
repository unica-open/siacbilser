/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacattser.model.TipoAtto;
public class RicercaImpSubParamDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private Integer annoEsercizio;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private Integer numeroUEB;
	private Integer annoImpegno;
	private BigDecimal numeroImpegno;
	private String tipoImpegno;
	private String cig;
	private String cup;
	private String progetto;
	private int annoProvvedimento;
	private int numeroProvvedimento;
	private TipoAtto tipoProvvedimento;
	private String codiceCreditore;
	private boolean competenzaCorrente;
	private boolean competenzaCompetenza;
	private boolean competenzaFuturi;
	//SIAC-6997
	private boolean competenzaResiduiRor;
	private boolean ricercaResiduiRorFlag;
	private Integer annoImpegnoRiaccertato;
	private Integer numeroImpegnoRiaccertato;
	private Integer annoImpegnoOrigine;
	private Integer numeroImpegnoOrigine;
	private String codiceClasseSoggetto;
	private String flagDaRiaccertamento;
	private String stato;
	
	private Integer uidCapitolo;
	private Integer uidProvvedimento;
	private Integer uidStrutturaAmministrativoContabile; //ATTO AMMINISTRATIVO
	
	private Boolean isRicercaDaImpegno = Boolean.TRUE;
	
	private String codiceProgetto;
	
	//SIAC-6255
	private Integer uidCronoprogramma;
	
	//SIAC-5253 itroduciamo la possibilita' di escludere stati precisi:
	private List<String> statiDaEscludere;
	
	//SIAC-6997
	private Boolean reanno;
	private String strutturaSelezionataCompetente;
	
	//SIAC-7486
	private List<Integer> listStrutturaCompetenteInt;
	
	//SIAC-7349
	private Integer componenteBilancioUid;
	
	/**
	 * @return the isRicercaDaImpegno
	 */
	public Boolean getIsRicercaDaImpegno() {
		return isRicercaDaImpegno;
	}
	/**
	 * @param isRicercaDaImpegno the isRicercaDaImpegno to set
	 */
	public void setIsRicercaDaImpegno(Boolean isRicercaDaImpegno) {
		this.isRicercaDaImpegno = isRicercaDaImpegno;
	}
	
	
	
	/**
	 * @return the uidStrutturaAmministativa
	 */
	public Integer getUidStrutturaAmministrativoContabile() {
		return uidStrutturaAmministrativoContabile;
	}
	/**
	 * @param uidStrutturaAmministativa the uidStrutturaAmministativa to set
	 */
	public void setUidStrutturaAmministrativoContabile(Integer uidStrutturaAmministrativoContabile) {
		this.uidStrutturaAmministrativoContabile = uidStrutturaAmministrativoContabile;
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
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	public BigDecimal getNumeroImpegno() {
		return numeroImpegno;
	}
	public void setNumeroImpegno(BigDecimal numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	public String getTipoImpegno() {
		return tipoImpegno;
	}
	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}
	public String getCig() {
		return cig;
	}
	public void setCig(String cig) {
		this.cig = cig;
	}
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	public String getProgetto() {
		return progetto;
	}
	public void setProgetto(String progetto) {
		this.progetto = progetto;
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
	public String getCodiceCreditore() {
		return codiceCreditore;
	}
	public void setCodiceCreditore(String codiceCreditore) {
		this.codiceCreditore = codiceCreditore;
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
	 * @return the annoImpegnoRiaccertato
	 */
	public Integer getAnnoImpegnoRiaccertato() {
		return annoImpegnoRiaccertato;
	}
	/**
	 * @param annoImpegnoRiaccertato the annoImpegnoRiaccertato to set
	 */
	public void setAnnoImpegnoRiaccertato(Integer annoImpegnoRiaccertato) {
		this.annoImpegnoRiaccertato = annoImpegnoRiaccertato;
	}
	/**
	 * @return the numeroImpegnoRiaccertato
	 */
	public Integer getNumeroImpegnoRiaccertato() {
		return numeroImpegnoRiaccertato;
	}
	/**
	 * @param numeroImpegnoRiaccertato the numeroImpegnoRiaccertato to set
	 */
	public void setNumeroImpegnoRiaccertato(Integer numeroImpegnoRiaccertato) {
		this.numeroImpegnoRiaccertato = numeroImpegnoRiaccertato;
	}
	/**
	 * @return the annoImpegnoOrigine
	 */
	public Integer getAnnoImpegnoOrigine() {
		return annoImpegnoOrigine;
	}
	/**
	 * @param annoImpegnoOrigine the annoImpegnoOrigine to set
	 */
	public void setAnnoImpegnoOrigine(Integer annoImpegnoOrigine) {
		this.annoImpegnoOrigine = annoImpegnoOrigine;
	}
	/**
	 * @return the numeroImpegnoOrigine
	 */
	public Integer getNumeroImpegnoOrigine() {
		return numeroImpegnoOrigine;
	}
	/**
	 * @param numeroImpegnoOrigine the numeroImpegnoOrigine to set
	 */
	public void setNumeroImpegnoOrigine(Integer numeroImpegnoOrigine) {
		this.numeroImpegnoOrigine = numeroImpegnoOrigine;
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
	 * @return the uidCronoprogramma
	 */
	public Integer getUidCronoprogramma() {
		return uidCronoprogramma;
	}
	/**
	 * @param uidCronoprogramma the uidCronoprogramma to set
	 */
	public void setUidCronoprogramma(Integer uidCronoprogramma) {
		this.uidCronoprogramma = uidCronoprogramma;
	}
	/**
	 * @return the reanno
	 */
	public Boolean getReanno() {
		return reanno;
	}
	/**
	 * @return the strutturaSelezionataCompetente
	 */
	public String getStrutturaSelezionataCompetente() {
		return strutturaSelezionataCompetente;
	}
	/**
	 * @param reanno the reanno to set
	 */
	public void setReanno(Boolean reanno) {
		this.reanno = reanno;
	}
	/**
	 * @param strutturaSelezionataCompetente the strutturaSelezionataCompetente to set
	 */
	public void setStrutturaSelezionataCompetente(String strutturaSelezionataCompetente) {
		this.strutturaSelezionataCompetente = strutturaSelezionataCompetente;
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
	/**
	 * @return the componenteBilancioUid
	 */
	public Integer getComponenteBilancioUid() {
		return componenteBilancioUid;
	}
	/**
	 * @param componenteBilancioUid the componenteBilancioUid to set
	 */
	public void setComponenteBilancioUid(Integer componenteBilancioUid) {
		this.componenteBilancioUid = componenteBilancioUid;
	}
	
}
