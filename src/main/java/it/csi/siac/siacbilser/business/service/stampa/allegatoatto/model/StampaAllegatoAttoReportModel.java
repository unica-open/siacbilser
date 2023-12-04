/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.Checklist;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaAllegatoAttoReportModel {
	
	private AllegatoAtto allegatoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileCDC;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileCDR;
	
	// SIAC-4201
	private BigDecimal totaleAtto = BigDecimal.ZERO;
	private BigDecimal totaleSplitIstituzionale = BigDecimal.ZERO;
	private BigDecimal totaleSplitCommerciale = BigDecimal.ZERO;
	private BigDecimal totaleReverseChange = BigDecimal.ZERO;
	private Boolean hasRitenute;
	private Soggetto soggettoProponente;
	
	// SIAC-4280
	private Date dataCompletamento;
	// SIAC-4800
	private Date dataInserimento;
	// SIAC-5446
	private Date dataScadenza;
	
	@XmlElementWrapper(name="elenchi")
	@XmlElement(name="elenco")
	private List<StampaAllegatoAttoElenco> listaStampaAllegatoAttoElenco = new ArrayList<StampaAllegatoAttoElenco>();
	
	// SIAC-8804
	private Boolean hasChecklist = Boolean.FALSE;
	private Checklist allegatoAttoChecklist;

	/**
	 * @return the allegatoAtto
	 */
	public AllegatoAtto getAllegatoAtto() {
		return allegatoAtto;
	}

	/**
	 * @param allegatoAtto the allegatoAtto to set
	 */
	public void setAllegatoAtto(AllegatoAtto allegatoAtto) {
		this.allegatoAtto = allegatoAtto;
	}

	/**
	 * @return the strutturaAmministrativoContabileCDC
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabileCDC() {
		return strutturaAmministrativoContabileCDC;
	}

	/**
	 * @param strutturaAmministrativoContabileCDC the strutturaAmministrativoContabileCDC to set
	 */
	public void setStrutturaAmministrativoContabileCDC(StrutturaAmministrativoContabile strutturaAmministrativoContabileCDC) {
		this.strutturaAmministrativoContabileCDC = strutturaAmministrativoContabileCDC;
	}

	/**
	 * @return the strutturaAmministrativoContabileCDR
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabileCDR() {
		return strutturaAmministrativoContabileCDR;
	}

	/**
	 * @param strutturaAmministrativoContabileCDR the strutturaAmministrativoContabileCDR to set
	 */
	public void setStrutturaAmministrativoContabileCDR(StrutturaAmministrativoContabile strutturaAmministrativoContabileCDR) {
		this.strutturaAmministrativoContabileCDR = strutturaAmministrativoContabileCDR;
	}

	/**
	 * @return the totaleAtto
	 */
	public BigDecimal getTotaleAtto() {
		return totaleAtto;
	}

	/**
	 * @param totaleAtto the totaleAtto to set
	 */
	public void setTotaleAtto(BigDecimal totaleAtto) {
		this.totaleAtto = totaleAtto;
	}
	
	/**
	 * Adds to totaleAtto
	 * 
	 * @param augend the augend
	 */
	public void addTotaleAtto(BigDecimal augend) {
		if(augend == null) {
			return;
		}
		this.totaleAtto = this.totaleAtto.add(augend);
	}

	/**
	 * @return the totaleSplitIstituzionale
	 */
	public BigDecimal getTotaleSplitIstituzionale() {
		return totaleSplitIstituzionale;
	}

	/**
	 * @param totaleSplitIstituzionale the totaleSplitIstituzionale to set
	 */
	public void setTotaleSplitIstituzionale(BigDecimal totaleSplitIstituzionale) {
		this.totaleSplitIstituzionale = totaleSplitIstituzionale;
	}
	
	/**
	 * Adds to totaleSplitIstituzionale
	 * 
	 * @param condition the condition under which the augend is to be added
	 * @param augend the augend
	 */
	public void addTotaleSplitIstituzionale(boolean condition, BigDecimal augend) {
		if(!condition || augend == null) {
			return;
		}
		this.totaleSplitIstituzionale = this.totaleSplitIstituzionale.add(augend);
	}

	/**
	 * @return the totaleSplitCommerciale
	 */
	public BigDecimal getTotaleSplitCommerciale() {
		return totaleSplitCommerciale;
	}

	/**
	 * @param totaleSplitCommerciale the totaleSplitCommerciale to set
	 */
	public void setTotaleSplitCommerciale(BigDecimal totaleSplitCommerciale) {
		this.totaleSplitCommerciale = totaleSplitCommerciale;
	}
	
	/**
	 * Adds to totaleSplitCommerciale
	 * 
	 * @param condition the condition under which the augend is to be added
	 * @param augend the augend
	 */
	public void addTotaleSplitCommerciale(boolean condition, BigDecimal augend) {
		if(!condition || augend == null) {
			return;
		}
		this.totaleSplitCommerciale = this.totaleSplitCommerciale.add(augend);
	}

	/**
	 * @return the totaleReverseChange
	 */
	public BigDecimal getTotaleReverseChange() {
		return totaleReverseChange;
	}

	/**
	 * @param totaleReverseChange the totaleReverseChange to set
	 */
	public void setTotaleReverseChange(BigDecimal totaleReverseChange) {
		this.totaleReverseChange = totaleReverseChange;
	}
	
	/**
	 * Adds to totaleReverseChange
	 * 
	 * @param condition the condition under which the augend is to be added
	 * @param augend the augend
	 */
	public void addTotaleReverseChange(boolean condition, BigDecimal augend) {
		if(!condition || augend == null) {
			return;
		}
		this.totaleReverseChange = this.totaleReverseChange.add(augend);
	}

	/**
	 * @return the hasRitenute
	 */
	public Boolean getHasRitenute() {
		return hasRitenute;
	}

	/**
	 * @param hasRitenute the hasRitenute to set
	 */
	public void setHasRitenute(Boolean hasRitenute) {
		this.hasRitenute = hasRitenute;
	}
	
	/**
	 * @return the soggettoProponente
	 */
	public Soggetto getSoggettoProponente() {
		return soggettoProponente;
	}

	/**
	 * @param soggettoProponente the soggettoProponente to set
	 */
	public void setSoggettoProponente(Soggetto soggettoProponente) {
		this.soggettoProponente = soggettoProponente;
	}

	/**
	 * @return the dataCompletamento
	 */
	public Date getDataCompletamento() {
		return dataCompletamento;
	}

	/**
	 * @param dataCompletamento the dataCompletamento to set
	 */
	public void setDataCompletamento(Date dataCompletamento) {
		this.dataCompletamento = dataCompletamento;
	}

	/**
	 * @return the dataInserimento
	 */
	public Date getDataInserimento() {
		return dataInserimento;
	}

	/**
	 * @param dataInserimento the dataInserimento to set
	 */
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	/**
	 * @return the listaStampaAllegatoAttoElenco
	 */
	@XmlTransient
	public List<StampaAllegatoAttoElenco> getListaStampaAllegatoAttoElenco() {
		return listaStampaAllegatoAttoElenco;
	}

	/**
	 * @param listaStampaAllegatoAttoElenco the listaStampaAllegatoAttoElenco to set
	 */
	public void setListaStampaAllegatoAttoElenco(List<StampaAllegatoAttoElenco> listaStampaAllegatoAttoElenco) {
		this.listaStampaAllegatoAttoElenco = listaStampaAllegatoAttoElenco != null ? listaStampaAllegatoAttoElenco : new ArrayList<StampaAllegatoAttoElenco>();
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @param dataScadenza the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Boolean getHasChecklist() {
		return hasChecklist;
	}

	public void setHasChecklist(Boolean hasChecklist) {
		this.hasChecklist = hasChecklist;
	}

	public Checklist getAllegatoAttoChecklist() {
		return allegatoAttoChecklist;
	}

	public void setAllegatoAttoChecklist(Checklist allegatoAttoChecklist) {
		this.allegatoAttoChecklist = allegatoAttoChecklist;
		setHasChecklist(Boolean.valueOf(allegatoAttoChecklist != null));
	}

}
