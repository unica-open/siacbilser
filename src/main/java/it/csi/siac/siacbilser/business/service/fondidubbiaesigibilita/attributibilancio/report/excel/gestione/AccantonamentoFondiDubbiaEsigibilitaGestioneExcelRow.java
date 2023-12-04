/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.gestione;

import java.math.BigDecimal;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

/**
 * AccantonamentoFondiDubbiaEsigibilitaGestioneExcelRow.
 *
 * @author interlogic
 * @version 1.0.0 - 10/05/2021
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class AccantonamentoFondiDubbiaEsigibilitaGestioneExcelRow extends BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow {
	
	private static final long serialVersionUID = 5302226045817091988L;
	
	private BigDecimal incassoContoCompetenza;
	private BigDecimal accertatoContoCompetenza;
	//SIAC-8768
	private BigDecimal stanziato;
	private BigDecimal stanziato1;
	private BigDecimal stanziato2;
	private BigDecimal stanziatoSenzaVariazioni;
	private BigDecimal stanziatoSenzaVariazioni1;
	private BigDecimal stanziatoSenzaVariazioni2;
	private BigDecimal deltaVariazioni;
	private BigDecimal deltaVariazioni1;
	private BigDecimal deltaVariazioni2;
	
	
//	private BigDecimal massimoStanziatoAccertato0;
//	private BigDecimal massimoStanziatoAccertato1;
//	private BigDecimal massimoStanziatoAccertato2;
	private BigDecimal percentualeIncassoGestione;
	private BigDecimal percentualeAccantonamento;
	private String tipoAccantonamentoPrecedente;
	private BigDecimal percentualeAccantonamentoPrecedente;
	private BigDecimal percentualeMinimaAccantonamento;
	private BigDecimal percentualeEffettivaAccantonamento;
	private BigDecimal accantonamentoFcde0;
	private BigDecimal accantonamentoFcde1;
	private BigDecimal accantonamentoFcde2;
	
//	@Override
//	@SuppressWarnings("unchecked")
//	public Map<AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn, Object> getColumns() {
//		return getAsMap(AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn.values());
//	}

	/**
	 * @return the incassoContoCompetenza
	 */
	public BigDecimal getIncassoContoCompetenza() {
		return this.incassoContoCompetenza;
	}

	/**
	 * @param incassoContoCompetenza the incassoContoCompetenza to set
	 */
	public void setIncassoContoCompetenza(BigDecimal incassoContoCompetenza) {
		this.incassoContoCompetenza = incassoContoCompetenza;
	}

	/**
	 * @return the accertatoContoCompetenza
	 */
	public BigDecimal getAccertatoContoCompetenza() {
		return this.accertatoContoCompetenza;
	}

	/**
	 * @param accertatoContoCompetenza the accertatoContoCompetenza to set
	 */
	public void setAccertatoContoCompetenza(BigDecimal accertatoContoCompetenza) {
		this.accertatoContoCompetenza = accertatoContoCompetenza;
	}
	
	
	

	
	public BigDecimal getStanziato() {
		return stanziato;
	}

	public void setStanziato(BigDecimal stanziato) {
		this.stanziato = stanziato;
	}

	public BigDecimal getStanziato1() {
		return stanziato1;
	}

	public void setStanziato1(BigDecimal stanziato1) {
		this.stanziato1 = stanziato1;
	}

	public BigDecimal getStanziato2() {
		return stanziato2;
	}

	public void setStanziato2(BigDecimal stanziato2) {
		this.stanziato2 = stanziato2;
	}

	public BigDecimal getStanziatoSenzaVariazioni() {
		return stanziatoSenzaVariazioni;
	}

	public void setStanziatoSenzaVariazioni(BigDecimal stanziatoSenzaVariazioni) {
		this.stanziatoSenzaVariazioni = stanziatoSenzaVariazioni;
	}

	public BigDecimal getStanziatoSenzaVariazioni1() {
		return stanziatoSenzaVariazioni1;
	}

	public void setStanziatoSenzaVariazioni1(BigDecimal stanziatoSenzaVariazioni1) {
		this.stanziatoSenzaVariazioni1 = stanziatoSenzaVariazioni1;
	}

	public BigDecimal getStanziatoSenzaVariazioni2() {
		return stanziatoSenzaVariazioni2;
	}

	public void setStanziatoSenzaVariazioni2(BigDecimal stanziatoSenzaVariazioni2) {
		this.stanziatoSenzaVariazioni2 = stanziatoSenzaVariazioni2;
	}

	public BigDecimal getDeltaVariazioni() {
		return deltaVariazioni;
	}

	public void setDeltaVariazioni(BigDecimal deltaVariazioni) {
		this.deltaVariazioni = deltaVariazioni;
	}

	public BigDecimal getDeltaVariazioni1() {
		return deltaVariazioni1;
	}

	public void setDeltaVariazioni1(BigDecimal deltaVariazioni1) {
		this.deltaVariazioni1 = deltaVariazioni1;
	}

	public BigDecimal getDeltaVariazioni2() {
		return deltaVariazioni2;
	}

	public void setDeltaVariazioni2(BigDecimal deltaVariazioni2) {
		this.deltaVariazioni2 = deltaVariazioni2;
	}

	/**
	 * @return the percentualeIncassoGestione
	 */
	public BigDecimal getPercentualeIncassoGestione() {
		return this.percentualeIncassoGestione;
	}

	/**
	 * @param percentualeIncassoGestione the percentualeIncassoGestione to set
	 */
	public void setPercentualeIncassoGestione(BigDecimal percentualeIncassoGestione) {
		this.percentualeIncassoGestione = percentualeIncassoGestione;
	}

	/**
	 * @return the percentualeAccantonamento
	 */
	public BigDecimal getPercentualeAccantonamento() {
		return this.percentualeAccantonamento;
	}

	/**
	 * @param percentualeAccantonamento the percentualeAccantonamento to set
	 */
	public void setPercentualeAccantonamento(BigDecimal percentualeAccantonamento) {
		this.percentualeAccantonamento = percentualeAccantonamento;
	}

	/**
	 * @return the tipoAccantonamentoPrecedente
	 */
	public String getTipoAccantonamentoPrecedente() {
		return this.tipoAccantonamentoPrecedente;
	}

	/**
	 * @param tipoAccantonamentoPrecedente the tipoAccantonamentoPrecedente to set
	 */
	public void setTipoAccantonamentoPrecedente(String tipoAccantonamentoPrecedente) {
		this.tipoAccantonamentoPrecedente = tipoAccantonamentoPrecedente;
	}

	/**
	 * @return the percentualeAccantonamentoPrecedente
	 */
	public BigDecimal getPercentualeAccantonamentoPrecedente() {
		return this.percentualeAccantonamentoPrecedente;
	}

	/**
	 * @param percentualeAccantonamentoPrecedente the percentualeAccantonamentoPrecedente to set
	 */
	public void setPercentualeAccantonamentoPrecedente(BigDecimal percentualeAccantonamentoPrecedente) {
		this.percentualeAccantonamentoPrecedente = percentualeAccantonamentoPrecedente;
	}

	/**
	 * @return the percentualeMinimaAccantonamento
	 */
	public BigDecimal getPercentualeMinimaAccantonamento() {
		return this.percentualeMinimaAccantonamento;
	}

	/**
	 * @param percentualeMinimaAccantonamento the percentualeMinimaAccantonamento to set
	 */
	public void setPercentualeMinimaAccantonamento(BigDecimal percentualeMinimaAccantonamento) {
		this.percentualeMinimaAccantonamento = percentualeMinimaAccantonamento;
	}

	/**
	 * @return the percentualeEffettivaAccantonamento
	 */
	public BigDecimal getPercentualeEffettivaAccantonamento() {
		return this.percentualeEffettivaAccantonamento;
	}

	/**
	 * @param percentualeEffettivaAccantonamento the percentualeEffettivaAccantonamento to set
	 */
	public void setPercentualeEffettivaAccantonamento(BigDecimal percentualeEffettivaAccantonamento) {
		this.percentualeEffettivaAccantonamento = percentualeEffettivaAccantonamento;
	}

	/**
	 * @return the accantonamentoFcde0
	 */
	public BigDecimal getAccantonamentoFcde0() {
		return this.accantonamentoFcde0;
	}

	/**
	 * @param accantonamentoFcde0 the accantonamentoFcde0 to set
	 */
	public void setAccantonamentoFcde0(BigDecimal accantonamentoFcde0) {
		this.accantonamentoFcde0 = accantonamentoFcde0;
	}

	/**
	 * @return the accantonamentoFcde1
	 */
	public BigDecimal getAccantonamentoFcde1() {
		return this.accantonamentoFcde1;
	}

	/**
	 * @param accantonamentoFcde1 the accantonamentoFcde1 to set
	 */
	public void setAccantonamentoFcde1(BigDecimal accantonamentoFcde1) {
		this.accantonamentoFcde1 = accantonamentoFcde1;
	}

	/**
	 * @return the accantonamentoFcde2
	 */
	public BigDecimal getAccantonamentoFcde2() {
		return this.accantonamentoFcde2;
	}

	/**
	 * @param accantonamentoFcde2 the accantonamentoFcde2 to set
	 */
	public void setAccantonamentoFcde2(BigDecimal accantonamentoFcde2) {
		this.accantonamentoFcde2 = accantonamentoFcde2;
	}
}
