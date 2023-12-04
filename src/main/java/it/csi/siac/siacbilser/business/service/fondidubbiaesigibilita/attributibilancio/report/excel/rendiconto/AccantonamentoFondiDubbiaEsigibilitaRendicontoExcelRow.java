/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto;

import java.math.BigDecimal;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

/**
 * AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelRow.
 *
 * @author interlogic
 * @version 1.0.0 - 05/05/2021
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelRow extends BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow {
	
	private static final long serialVersionUID = -3054701062542989405L;

	private BigDecimal incassiContoResidui4;
	private BigDecimal residui4;
	private BigDecimal incassiContoResidui3;
	private BigDecimal residui3;
	private BigDecimal incassiContoResidui2;
	private BigDecimal residui2;
	private BigDecimal incassiContoResidui1;
	private BigDecimal residui1;
	private BigDecimal incassiContoResidui0;
	private BigDecimal residui0;
	private BigDecimal mediaSempliceTotali;
	private BigDecimal mediaSempliceRapporti;
	private BigDecimal mediaPonderataTotali;
	private BigDecimal mediaPonderataRapporti;
	private BigDecimal mediaUtente;
	private BigDecimal percentualeMinima;
	private BigDecimal percentualeEffettiva;
	private BigDecimal residuiFinali;
	private BigDecimal residuiFinali1;
	private BigDecimal residuiFinali2;
	private BigDecimal accantonamentoFCDE;
	private BigDecimal accantonamentoFCDE1;
	private BigDecimal accantonamentoFCDE2;
	
	/**
	 * @return the incassiContoResidui4
	 */
	public BigDecimal getIncassiContoResidui4() {
		return this.incassiContoResidui4;
	}

	/**
	 * @param incassiContoResidui4 the incassiContoResidui4 to set
	 */
	public void setIncassiContoResidui4(BigDecimal incassiContoResidui4) {
		this.incassiContoResidui4 = incassiContoResidui4;
	}

	/**
	 * @return the residui4
	 */
	public BigDecimal getResidui4() {
		return this.residui4;
	}

	/**
	 * @param residui4 the residui4 to set
	 */
	public void setResidui4(BigDecimal residui4) {
		this.residui4 = residui4;
	}

	/**
	 * @return the incassiContoResidui3
	 */
	public BigDecimal getIncassiContoResidui3() {
		return this.incassiContoResidui3;
	}

	/**
	 * @param incassiContoResidui3 the incassiContoResidui3 to set
	 */
	public void setIncassiContoResidui3(BigDecimal incassiContoResidui3) {
		this.incassiContoResidui3 = incassiContoResidui3;
	}

	/**
	 * @return the residui3
	 */
	public BigDecimal getResidui3() {
		return this.residui3;
	}

	/**
	 * @param residui3 the residui3 to set
	 */
	public void setResidui3(BigDecimal residui3) {
		this.residui3 = residui3;
	}

	/**
	 * @return the incassiContoResidui2
	 */
	public BigDecimal getIncassiContoResidui2() {
		return this.incassiContoResidui2;
	}

	/**
	 * @param incassiContoResidui2 the incassiContoResidui2 to set
	 */
	public void setIncassiContoResidui2(BigDecimal incassiContoResidui2) {
		this.incassiContoResidui2 = incassiContoResidui2;
	}

	/**
	 * @return the residui2
	 */
	public BigDecimal getResidui2() {
		return this.residui2;
	}

	/**
	 * @param residui2 the residui2 to set
	 */
	public void setResidui2(BigDecimal residui2) {
		this.residui2 = residui2;
	}

	/**
	 * @return the incassiContoResidui1
	 */
	public BigDecimal getIncassiContoResidui1() {
		return this.incassiContoResidui1;
	}

	/**
	 * @param incassiContoResidui1 the incassiContoResidui1 to set
	 */
	public void setIncassiContoResidui1(BigDecimal incassiContoResidui1) {
		this.incassiContoResidui1 = incassiContoResidui1;
	}

	/**
	 * @return the residui1
	 */
	public BigDecimal getResidui1() {
		return this.residui1;
	}

	/**
	 * @param residui1 the residui1 to set
	 */
	public void setResidui1(BigDecimal residui1) {
		this.residui1 = residui1;
	}

	/**
	 * @return the incassiContoResidui0
	 */
	public BigDecimal getIncassiContoResidui0() {
		return this.incassiContoResidui0;
	}

	/**
	 * @param incassiContoResidui0 the incassiContoResidui0 to set
	 */
	public void setIncassiContoResidui0(BigDecimal incassiContoResidui0) {
		this.incassiContoResidui0 = incassiContoResidui0;
	}

	/**
	 * @return the residui0
	 */
	public BigDecimal getResidui0() {
		return this.residui0;
	}

	/**
	 * @param residui0 the residui0 to set
	 */
	public void setResidui0(BigDecimal residui0) {
		this.residui0 = residui0;
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
	 * @return the residuiFinali
	 */
	public BigDecimal getResiduiFinali() {
		return this.residuiFinali;
	}

	/**
	 * @param residuiFinali the residuiFinali to set
	 */
	public void setResiduiFinali(BigDecimal residuiFinali) {
		this.residuiFinali = residuiFinali;
	}
	
	/**
	 * @return the residuiFinali1
	 */
	public BigDecimal getResiduiFinali1() {
		return residuiFinali1;
	}
	
	/**
	 * @param residuiFinali1 the residuiFinali1 to set
	 */
	public void setResiduiFinali1(BigDecimal residuiFinali1) {
		this.residuiFinali1 = residuiFinali1;
	}
	
	/**
	 * @return the residuiFinali2
	 */
	public BigDecimal getResiduiFinali2() {
		return residuiFinali2;
	}
	
	/**
	 * @param residuiFinali2 the residuiFinali2 to set
	 */
	public void setResiduiFinali2(BigDecimal residuiFinali2) {
		this.residuiFinali2 = residuiFinali2;
	}

	/**
	 * @return the accantonamentoFCDE
	 */
	public BigDecimal getAccantonamentoFCDE() {
		return this.accantonamentoFCDE;
	}

	/**
	 * @param accantonamentoFCDE the accantonamentoFCDE to set
	 */
	public void setAccantonamentoFCDE(BigDecimal accantonamentoFCDE) {
		this.accantonamentoFCDE = accantonamentoFCDE;
	}

	/**
	 * @return the accantonamentoFCDE1
	 */
	public BigDecimal getAccantonamentoFCDE1() {
		return accantonamentoFCDE1;
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
		return accantonamentoFCDE2;
	}

	/**
	 * @param accantonamentoFCDE2 the accantonamentoFCDE2 to set
	 */
	public void setAccantonamentoFCDE2(BigDecimal accantonamentoFCDE2) {
		this.accantonamentoFCDE2 = accantonamentoFCDE2;
	}
	
}
