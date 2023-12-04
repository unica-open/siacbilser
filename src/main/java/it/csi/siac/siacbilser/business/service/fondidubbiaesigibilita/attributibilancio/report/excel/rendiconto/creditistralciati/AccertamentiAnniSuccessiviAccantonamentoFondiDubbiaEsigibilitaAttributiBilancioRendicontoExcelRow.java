/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.creditistralciati;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

/**
 * CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaRendicontoExcelRow
 * 
 * @author todescoa
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow {

	private static final long serialVersionUID = 1L;

	private Integer annoAccertamento;
	private BigDecimal numeroAccertamento;
	private String descrizioneAccertamento;
	private String titoloAccertamento;
	private String tipologiaAccertamento;
	private String categoriaAccertamento;
	private String strutturaAmministrativoContabileAccertamento;
	private String pianoDeiContiAccertamento;
	private String codRicorrente;
	
	private String annoProvvedimentoAccertamento;
	private Integer numeroProvvedimentoAccertamento;
	private String tipoProvvedimentoAccertamento;
	private String sacProvvedimentoAccertamento;
	
	
	private BigDecimal importoAttuale;
	
	 
	
	
	private String soggettoDesc;
	private String classeSoggettoDesc;
	
	
	private Boolean rilevanteGSA;
	

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


	
	
	public String getTitoloAccertamento() {
		return titoloAccertamento;
	}


	public void setTitoloAccertamento(String titoloAccertamento) {
		this.titoloAccertamento = titoloAccertamento;
	}


	public String getTipologiaAccertamento() {
		return tipologiaAccertamento;
	}


	public void setTipologiaAccertamento(String tipologiaAccertamento) {
		this.tipologiaAccertamento = tipologiaAccertamento;
	}


	public String getCategoriaAccertamento() {
		return categoriaAccertamento;
	}


	public void setCategoriaAccertamento(String categoriaAccertamento) {
		this.categoriaAccertamento = categoriaAccertamento;
	}


	public String getStrutturaAmministrativoContabileAccertamento() {
		return strutturaAmministrativoContabileAccertamento;
	}


	public void setStrutturaAmministrativoContabileAccertamento(String strutturaAmministrativoContabileAccertamento) {
		this.strutturaAmministrativoContabileAccertamento = strutturaAmministrativoContabileAccertamento;
	}



	public String getPianoDeiContiAccertamento() {
		return pianoDeiContiAccertamento;
	}


	public void setPianoDeiContiAccertamento(String pianoDeiContiAccertamento) {
		this.pianoDeiContiAccertamento = pianoDeiContiAccertamento;
	}


	public String getCodRicorrente() {
		return codRicorrente;
	}


	public void setCodRicorrente(String codRicorrente) {
		this.codRicorrente = codRicorrente;
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


	
	
	
	
	public BigDecimal getImportoAttuale() {
		return importoAttuale;
	}


	public void setImportoAttuale(BigDecimal importoAttuale) {
		this.importoAttuale = importoAttuale;
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


	
	public String getAnnoAccertamentoString() {
		return this.annoAccertamento != null? this.annoAccertamento.toString() : "" ;
	}
	
	public String getNumeroAccertamentoString() {
		return this.numeroAccertamento != null? this.numeroAccertamento.toString() : "" ;
	}
	
	public String getNumeroProvvedimentoAccertamentoString() {
		return this.numeroProvvedimentoAccertamento != null? this.numeroProvvedimentoAccertamento.toString() : "" ;
	}


	public Boolean getRilevanteGSA() {
		return rilevanteGSA;
	}


	public void setRilevanteGSA(Boolean rilevanteGSA) {
		this.rilevanteGSA = rilevanteGSA;
	}
	
	
	
	
}
