/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.creditistralciati;

import java.math.BigDecimal;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

/**
 * CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaRendicontoExcelRow
 * 
 * @author todescoa
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow {

	private static final long serialVersionUID = 1L;

	private Integer annoAccertamento;
	private BigDecimal numeroAccertamento;
	private String descrizioneAccertamento;
	private String pianoDeiConti;
	
	private String annoProvvedimentoAccertamento;
	private Integer numeroProvvedimentoAccertamento;
	private String tipoProvvedimentoAccertamento;
	private String sacProvvedimentoAccertamento;
	
	private String soggettoDesc;
	private String classeSoggettoDesc;
	
	private String descrizioneModifica;
	private String motivoModifica;
	private BigDecimal importoModifica;
	
	private String annoProvvedimentoModifica;
	private Integer numeroProvvedimentoModifica;
	private String tipoProvvedimentoModifica;
	private String sacProvvedimentoModifica;
	
//	@Override
//	@SuppressWarnings("unchecked")
//	public Map<CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn, Object> getColumns() {
//		return getAsMap(CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn.values());
//	}

	public Integer getAnnoAccertamento() {
		return annoAccertamento;
	}

	public void setAnnoAccertamento(Integer annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}

	public BigDecimal getNumeroAccertamento() {
		return numeroAccertamento;
	}

	public void setNumeroAccertamento(BigDecimal numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}

	public String getDescrizioneAccertamento() {
		return descrizioneAccertamento;
	}

	public void setDescrizioneAccertamento(String descrizioneAccertamento) {
		this.descrizioneAccertamento = descrizioneAccertamento;
	}

	public String getPianoDeiConti() {
		return pianoDeiConti;
	}

	public void setPianoDeiConti(String pianoDeiConti) {
		this.pianoDeiConti = pianoDeiConti;
	}

	public String getAnnoProvvedimentoAccertamento() {
		return annoProvvedimentoAccertamento;
	}

	public void setAnnoProvvedimentoAccertamento(String annoProvvedimentoAccertamento) {
		this.annoProvvedimentoAccertamento = annoProvvedimentoAccertamento;
	}

	public Integer getNumeroProvvedimentoAccertamento() {
		return numeroProvvedimentoAccertamento;
	}

	public void setNumeroProvvedimentoAccertamento(Integer numeroProvvedimentoAccertamento) {
		this.numeroProvvedimentoAccertamento = numeroProvvedimentoAccertamento;
	}

	public String getTipoProvvedimentoAccertamento() {
		return tipoProvvedimentoAccertamento;
	}

	public void setTipoProvvedimentoAccertamento(String tipoProvvedimentoAccertamento) {
		this.tipoProvvedimentoAccertamento = tipoProvvedimentoAccertamento;
	}

	public String getSacProvvedimentoAccertamento() {
		return sacProvvedimentoAccertamento;
	}

	public void setSacProvvedimentoAccertamento(String sacProvvedimentoAccertamento) {
		this.sacProvvedimentoAccertamento = sacProvvedimentoAccertamento;
	}

	public String getSoggettoDesc() {
		return soggettoDesc;
	}

	public void setSoggettoDesc(String soggettoDesc) {
		this.soggettoDesc = soggettoDesc;
	}

	public String getClasseSoggettoDesc() {
		return classeSoggettoDesc;
	}

	public void setClasseSoggettoDesc(String classeSoggettoDesc) {
		this.classeSoggettoDesc = classeSoggettoDesc;
	}

	public String getDescrizioneModifica() {
		return descrizioneModifica;
	}

	public void setDescrizioneModifica(String descrizioneModifica) {
		this.descrizioneModifica = descrizioneModifica;
	}

	public String getMotivoModifica() {
		return motivoModifica;
	}

	public void setMotivoModifica(String motivoModifica) {
		this.motivoModifica = motivoModifica;
	}

	public BigDecimal getImportoModifica() {
		return importoModifica;
	}

	public void setImportoModifica(BigDecimal importoModifica) {
		this.importoModifica = importoModifica;
	}

	public String getAnnoProvvedimentoModifica() {
		return annoProvvedimentoModifica;
	}

	public void setAnnoProvvedimentoModifica(String annoProvvedimentoModifica) {
		this.annoProvvedimentoModifica = annoProvvedimentoModifica;
	}

	public Integer getNumeroProvvedimentoModifica() {
		return numeroProvvedimentoModifica;
	}

	public void setNumeroProvvedimentoModifica(Integer numeroProvvedimentoModifica) {
		this.numeroProvvedimentoModifica = numeroProvvedimentoModifica;
	}

	public String getTipoProvvedimentoModifica() {
		return tipoProvvedimentoModifica;
	}

	public void setTipoProvvedimentoModifica(String tipoProvvedimentoModifica) {
		this.tipoProvvedimentoModifica = tipoProvvedimentoModifica;
	}

	public String getSacProvvedimentoModifica() {
		return sacProvvedimentoModifica;
	}

	public void setSacProvvedimentoModifica(String sacProvvedimentoModifica) {
		this.sacProvvedimentoModifica = sacProvvedimentoModifica;
	}

	public String getAnnoAccertamentoString() {
		return this.annoAccertamento != null? this.annoAccertamento.toString() : "" ;
	}
	
	public String getNumeroAccertamentoString() {
		return this.numeroAccertamento != null? this.numeroAccertamento.toString() : "" ;
	}
	
	public String getNumeroProvvedimentoAccertamentoString() {
		return this.numeroProvvedimentoAccertamento != null? this.numeroProvvedimentoAccertamento.toString() : "" ;
	}
	
	public String getNumeroProvvedimentoModificaString() {
		return this.numeroProvvedimentoModifica != null? this.numeroProvvedimentoModifica.toString() : "" ;
	}
	
}
