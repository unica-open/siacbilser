/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.previsione;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class AccantonamentoFondiDubbiaEsigibilitaExcelRow extends BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow {

	
	private BigDecimal incassi4;
	private BigDecimal accertamenti4;
	private BigDecimal incassi3;
	private BigDecimal accertamenti3;
	private BigDecimal incassi2;
	private BigDecimal accertamenti2;
	private BigDecimal incassi1;
	private BigDecimal accertamenti1;
	private BigDecimal incassi0;
	private BigDecimal accertamenti0;
	private BigDecimal mediaSempliceTotali;
	private BigDecimal mediaSempliceRapporti;
	private BigDecimal mediaPonderataTotali;
	private BigDecimal mediaPonderataRapporti;
	private BigDecimal mediaUtente;
	private BigDecimal percentualeMinima;
	private BigDecimal percentualeEffettiva;
	private BigDecimal stanziamento0;
	private BigDecimal stanziamento1;
	private BigDecimal stanziamento2;
	private BigDecimal accantonamentoFCDE0;
	private BigDecimal accantonamentoFCDE1;
	private BigDecimal accantonamentoFCDE2;

	
	/**
	 * @return the incassi4
	 */
	public BigDecimal getIncassi4() {
		return this.incassi4;
	}

	/**
	 * @param incassi4 the incassi4 to set
	 */
	public void setIncassi4(BigDecimal incassi4) {
		this.incassi4 = incassi4;
	}

	/**
	 * @return the accertamenti4
	 */
	public BigDecimal getAccertamenti4() {
		return this.accertamenti4;
	}

	/**
	 * @param accertamenti4 the accertamenti4 to set
	 */
	public void setAccertamenti4(BigDecimal accertamenti4) {
		this.accertamenti4 = accertamenti4;
	}

	/**
	 * @return the incassi3
	 */
	public BigDecimal getIncassi3() {
		return this.incassi3;
	}

	/**
	 * @param incassi3 the incassi3 to set
	 */
	public void setIncassi3(BigDecimal incassi3) {
		this.incassi3 = incassi3;
	}

	/**
	 * @return the accertamenti3
	 */
	public BigDecimal getAccertamenti3() {
		return this.accertamenti3;
	}

	/**
	 * @param accertamenti3 the accertamenti3 to set
	 */
	public void setAccertamenti3(BigDecimal accertamenti3) {
		this.accertamenti3 = accertamenti3;
	}

	/**
	 * @return the incassi2
	 */
	public BigDecimal getIncassi2() {
		return this.incassi2;
	}

	/**
	 * @param incassi2 the incassi2 to set
	 */
	public void setIncassi2(BigDecimal incassi2) {
		this.incassi2 = incassi2;
	}

	/**
	 * @return the accertamenti2
	 */
	public BigDecimal getAccertamenti2() {
		return this.accertamenti2;
	}

	/**
	 * @param accertamenti2 the accertamenti2 to set
	 */
	public void setAccertamenti2(BigDecimal accertamenti2) {
		this.accertamenti2 = accertamenti2;
	}

	/**
	 * @return the incassi1
	 */
	public BigDecimal getIncassi1() {
		return this.incassi1;
	}

	/**
	 * @param incassi1 the incassi1 to set
	 */
	public void setIncassi1(BigDecimal incassi1) {
		this.incassi1 = incassi1;
	}

	/**
	 * @return the accertamenti1
	 */
	public BigDecimal getAccertamenti1() {
		return this.accertamenti1;
	}

	/**
	 * @param accertamenti1 the accertamenti1 to set
	 */
	public void setAccertamenti1(BigDecimal accertamenti1) {
		this.accertamenti1 = accertamenti1;
	}

	/**
	 * @return the incassi0
	 */
	public BigDecimal getIncassi0() {
		return this.incassi0;
	}

	/**
	 * @param incassi0 the incassi0 to set
	 */
	public void setIncassi0(BigDecimal incassi0) {
		this.incassi0 = incassi0;
	}

	/**
	 * @return the accertamenti0
	 */
	public BigDecimal getAccertamenti0() {
		return this.accertamenti0;
	}

	/**
	 * @param accertamenti0 the accertamenti0 to set
	 */
	public void setAccertamenti0(BigDecimal accertamenti0) {
		this.accertamenti0 = accertamenti0;
	}

	/**
	 * @return the mediaSempliceTotali
	 */
	public BigDecimal getMediaSempliceTotali() {
		return this.mediaSempliceTotali;
	}

	/**
	 * @param mediaSempliceTotali the mediaSempliceTotali to set
	 */
	public void setMediaSempliceTotali(BigDecimal mediaSempliceTotali) {
		this.mediaSempliceTotali = mediaSempliceTotali;
	}

	/**
	 * @return the mediaSempliceRapporti
	 */
	public BigDecimal getMediaSempliceRapporti() {
		return this.mediaSempliceRapporti;
	}

	/**
	 * @param mediaSempliceRapporti the mediaSempliceRapporti to set
	 */
	public void setMediaSempliceRapporti(BigDecimal mediaSempliceRapporti) {
		this.mediaSempliceRapporti = mediaSempliceRapporti;
	}

	/**
	 * @return the mediaPonderataTotali
	 */
	public BigDecimal getMediaPonderataTotali() {
		return this.mediaPonderataTotali;
	}

	/**
	 * @param mediaPonderataTotali the mediaPonderataTotali to set
	 */
	public void setMediaPonderataTotali(BigDecimal mediaPonderataTotali) {
		this.mediaPonderataTotali = mediaPonderataTotali;
	}

	/**
	 * @return the mediaPonderataRapporti
	 */
	public BigDecimal getMediaPonderataRapporti() {
		return this.mediaPonderataRapporti;
	}

	/**
	 * @param mediaPonderataRapporti the mediaPonderataRapporti to set
	 */
	public void setMediaPonderataRapporti(BigDecimal mediaPonderataRapporti) {
		this.mediaPonderataRapporti = mediaPonderataRapporti;
	}

	/**
	 * @return the mediaUtente
	 */
	public BigDecimal getMediaUtente() {
		return this.mediaUtente;
	}

	/**
	 * @param mediaUtente the mediaUtente to set
	 */
	public void setMediaUtente(BigDecimal mediaUtente) {
		this.mediaUtente = mediaUtente;
	}

	/**
	 * @return the percentualeMinima
	 */
	public BigDecimal getPercentualeMinima() {
		return this.percentualeMinima;
	}

	/**
	 * @param percentualeMinima the percentualeMinima to set
	 */
	public void setPercentualeMinima(BigDecimal percentualeMinima) {
		this.percentualeMinima = percentualeMinima;
	}

	/**
	 * @return the percentualeEffettiva
	 */
	public BigDecimal getPercentualeEffettiva() {
		return this.percentualeEffettiva;
	}

	/**
	 * @param percentualeEffettiva the percentualeEffettiva to set
	 */
	public void setPercentualeEffettiva(BigDecimal percentualeEffettiva) {
		this.percentualeEffettiva = percentualeEffettiva;
	}

	/**
	 * @return the stanziamento0
	 */
	public BigDecimal getStanziamento0() {
		return this.stanziamento0;
	}

	/**
	 * @param stanziamento0 the stanziamento0 to set
	 */
	public void setStanziamento0(BigDecimal stanziamento0) {
		this.stanziamento0 = stanziamento0;
	}

	/**
	 * @return the stanziamento1
	 */
	public BigDecimal getStanziamento1() {
		return this.stanziamento1;
	}

	/**
	 * @param stanziamento1 the stanziamento1 to set
	 */
	public void setStanziamento1(BigDecimal stanziamento1) {
		this.stanziamento1 = stanziamento1;
	}

	/**
	 * @return the stanziamento2
	 */
	public BigDecimal getStanziamento2() {
		return this.stanziamento2;
	}

	/**
	 * @param stanziamento2 the stanziamento2 to set
	 */
	public void setStanziamento2(BigDecimal stanziamento2) {
		this.stanziamento2 = stanziamento2;
	}

	/**
	 * @return the accantonamentoFCDE0
	 */
	public BigDecimal getAccantonamentoFCDE0() {
		return this.accantonamentoFCDE0;
	}

	/**
	 * @param accantonamentoFCDE0 the accantonamentoFCDE0 to set
	 */
	public void setAccantonamentoFCDE0(BigDecimal accantonamentoFCDE0) {
		this.accantonamentoFCDE0 = accantonamentoFCDE0;
	}

	/**
	 * @return the accantonamentoFCDE1
	 */
	public BigDecimal getAccantonamentoFCDE1() {
		return this.accantonamentoFCDE1;
	}

	/**
	 * @param accantonamentoFCDE1 the accantonamentoFCDE1 to set
	 */
	public void setAccantonamentoFCDE1(BigDecimal accantonamentoFCDE1) {
		this.accantonamentoFCDE1 = accantonamentoFCDE1;
	}

	/**
	 * @return the accantonamentoFCDE2
	 */
	public BigDecimal getAccantonamentoFCDE2() {
		return this.accantonamentoFCDE2;
	}

	/**
	 * @param accantonamentoFCDE2 the accantonamentoFCDE2 to set
	 */
	public void setAccantonamentoFCDE2(BigDecimal accantonamentoFCDE2) {
		this.accantonamentoFCDE2 = accantonamentoFCDE2;
	}

	
}
