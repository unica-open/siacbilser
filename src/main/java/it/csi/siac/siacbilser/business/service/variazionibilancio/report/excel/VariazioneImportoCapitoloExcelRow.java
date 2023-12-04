/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio.report.excel;

import java.math.BigDecimal;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

/**
 * VariazioneImportoCapitolo.
 *
 * @author Elisa Chiari 
 * @version 1.0.0 - 17/07/2017
 * 
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class VariazioneImportoCapitoloExcelRow implements BaseExcelRow {
	
	
	private String statoVariazione;
	private String annoCapitolo;
	private String numeroCapitolo;
	private String numeroArticolo;
	private String tipoCapitolo;
	private String descrizioneCapitolo;
	private String descrizioneArticolo;
	private String missioneCapitolo;
	private String programmaCapitolo;
	private String titoloCapitoloSpesa;
	private String macroaggregatoCapitolo;
	private String titoloCapitoloEntrata;
	private String tipologiaCapitoloEntrata;
	private String categoriaTipologiaTitoloCapitoloEntrata;
	private String annoCompetenza;
	private BigDecimal stanziamentoCapitolo;
	private BigDecimal stanziamentoCassaCapitolo;
	private BigDecimal stanziamentoResiduoCapitolo;
	private BigDecimal stanziamentoCapitoloAnno1;
	private BigDecimal stanziamentoCapitoloAnno2;
	private BigDecimal stanziamentoVariazione;
	private BigDecimal stanziamentoCassaVariazione;
	private BigDecimal stanziamentoResiduoVariazione;
	private BigDecimal stanziamentoVariazioneAnno1;
	private BigDecimal stanziamentoVariazioneAnno2;
	//SIAC-6468
	private String tipologiaFinanziamento;
	private String strutturaAmministrativaResponsabile;

	private Integer variazioneNum;
	// SIAC-6883
//	private String variazioneAnno;
	
	/**
	 * @return the statoVariazione
	 */
	public String getStatoVariazione() {
		return statoVariazione;
	}
	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(String statoVariazione) {
		this.statoVariazione = statoVariazione;
	}
	/**
	 * @return the annoCapitolo
	 */
	public String getAnnoCapitolo() {
		return annoCapitolo;
	}
	/**
	 * @param annoCapitolo the annoCapitolo to set
	 */
	public void setAnnoCapitolo(String annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}
	/**
	 * @return the numeroCapitolo
	 */
	public String getNumeroCapitolo() {
		return numeroCapitolo;
	}
	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(String numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}
	/**
	 * @return the numeroArticolo
	 */
	public String getNumeroArticolo() {
		return numeroArticolo;
	}
	/**
	 * @param numeroArticolo the numeroArticolo to set
	 */
	public void setNumeroArticolo(String numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}
	/**
	 * @return the tipoCapitolo
	 */
	public String getTipoCapitolo() {
		return tipoCapitolo;
	}
	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(String tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}
	/**
	 * @return the descrizioneCapitolo
	 */
	public String getDescrizioneCapitolo() {
		return descrizioneCapitolo;
	}
	/**
	 * @param descrizioneCapitolo the descrizioneCapitolo to set
	 */
	public void setDescrizioneCapitolo(String descrizioneCapitolo) {
		this.descrizioneCapitolo = descrizioneCapitolo;
	}
	/**
	 * @return the descrizioneArticolo
	 */
	public String getDescrizioneArticolo() {
		return descrizioneArticolo;
	}
	/**
	 * @param descrizioneArticolo the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.descrizioneArticolo = descrizioneArticolo;
	}
	/**
	 * @return the missioneCapitolo
	 */
	public String getMissioneCapitolo() {
		return missioneCapitolo;
	}
	/**
	 * @param missioneCapitolo the missioneCapitolo to set
	 */
	public void setMissioneCapitolo(String missioneCapitolo) {
		this.missioneCapitolo = missioneCapitolo;
	}
	/**
	 * @return the programmaCapitolo
	 */
	public String getProgrammaCapitolo() {
		return programmaCapitolo;
	}
	/**
	 * @param programmaCapitolo the programmaCapitolo to set
	 */
	public void setProgrammaCapitolo(String programmaCapitolo) {
		this.programmaCapitolo = programmaCapitolo;
	}
	/**
	 * @return the titoloCapitoloSpesa
	 */
	public String getTitoloCapitoloSpesa() {
		return titoloCapitoloSpesa;
	}
	/**
	 * @param titoloCapitoloSpesa the titoloCapitoloSpesa to set
	 */
	public void setTitoloCapitoloSpesa(String titoloCapitoloSpesa) {
		this.titoloCapitoloSpesa = titoloCapitoloSpesa;
	}
	/**
	 * @return the macroaggregatoCapitolo
	 */
	public String getMacroaggregatoCapitolo() {
		return macroaggregatoCapitolo;
	}
	/**
	 * @param macroaggregatoCapitolo the macroaggregatoCapitolo to set
	 */
	public void setMacroaggregatoCapitolo(String macroaggregatoCapitolo) {
		this.macroaggregatoCapitolo = macroaggregatoCapitolo;
	}
	/**
	 * @return the titoloCapitoloEntrata
	 */
	public String getTitoloCapitoloEntrata() {
		return titoloCapitoloEntrata;
	}
	/**
	 * @param titoloCapitoloEntrata the titoloCapitoloEntrata to set
	 */
	public void setTitoloCapitoloEntrata(String titoloCapitoloEntrata) {
		this.titoloCapitoloEntrata = titoloCapitoloEntrata;
	}
	/**
	 * @return the tipologiaCapitoloEntrata
	 */
	public String getTipologiaCapitoloEntrata() {
		return tipologiaCapitoloEntrata;
	}
	/**
	 * @param tipologiaCapitoloEntrata the tipologiaCapitoloEntrata to set
	 */
	public void setTipologiaCapitoloEntrata(String tipologiaCapitoloEntrata) {
		this.tipologiaCapitoloEntrata = tipologiaCapitoloEntrata;
	}
	/**
	 * @return the categoriaTipologiaTitoloCapitoloEntrata
	 */
	public String getCategoriaTipologiaTitoloCapitoloEntrata() {
		return categoriaTipologiaTitoloCapitoloEntrata;
	}
	/**
	 * @param categoriaTipologiaTitoloCapitoloEntrata the categoriaTipologiaTitoloCapitoloEntrata to set
	 */
	public void setCategoriaTipologiaTitoloCapitoloEntrata(String categoriaTipologiaTitoloCapitoloEntrata) {
		this.categoriaTipologiaTitoloCapitoloEntrata = categoriaTipologiaTitoloCapitoloEntrata;
	}
	/**
	 * @return the annoCompetenza
	 */
	public String getAnnoCompetenza() {
		return annoCompetenza;
	}
	/**
	 * @param annoCompetenza the annoCompetenza to set
	 */
	public void setAnnoCompetenza(String annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}
	/**
	 * @return the stanziamentoCapitolo
	 */
	public BigDecimal getStanziamentoCapitolo() {
		return stanziamentoCapitolo;
	}
	/**
	 * @param stanziamentoCapitolo the stanziamentoCapitolo to set
	 */
	public void setStanziamentoCapitolo(BigDecimal stanziamentoCapitolo) {
		this.stanziamentoCapitolo = stanziamentoCapitolo;
	}
	/**
	 * @return the stanziamentoCassaCapitolo
	 */
	public BigDecimal getStanziamentoCassaCapitolo() {
		return stanziamentoCassaCapitolo;
	}
	/**
	 * @param stanziamentoCassaCapitolo the stanziamentoCassaCapitolo to set
	 */
	public void setStanziamentoCassaCapitolo(BigDecimal stanziamentoCassaCapitolo) {
		this.stanziamentoCassaCapitolo = stanziamentoCassaCapitolo;
	}
	/**
	 * @return the stanziamentoResiduoCapitolo
	 */
	public BigDecimal getStanziamentoResiduoCapitolo() {
		return stanziamentoResiduoCapitolo;
	}
	/**
	 * @param stanziamentoResiduoCapitolo the stanziamentoResiduoCapitolo to set
	 */
	public void setStanziamentoResiduoCapitolo(BigDecimal stanziamentoResiduoCapitolo) {
		this.stanziamentoResiduoCapitolo = stanziamentoResiduoCapitolo;
	}
	/**
	 * @return the stanziamentoCapitoloAnno1
	 */
	public BigDecimal getStanziamentoCapitoloAnno1() {
		return stanziamentoCapitoloAnno1;
	}
	/**
	 * @param stanziamentoCapitoloAnno1 the stanziamentoCapitoloAnno1 to set
	 */
	public void setStanziamentoCapitoloAnno1(BigDecimal stanziamentoCapitoloAnno1) {
		this.stanziamentoCapitoloAnno1 = stanziamentoCapitoloAnno1;
	}
	/**
	 * @return the stanziamentoCapitoloAnno2
	 */
	public BigDecimal getStanziamentoCapitoloAnno2() {
		return stanziamentoCapitoloAnno2;
	}
	/**
	 * @param stanziamentoCapitoloAnno2 the stanziamentoCapitoloAnno2 to set
	 */
	public void setStanziamentoCapitoloAnno2(BigDecimal stanziamentoCapitoloAnno2) {
		this.stanziamentoCapitoloAnno2 = stanziamentoCapitoloAnno2;
	}
	/**
	 * @return the stanziamentoVariazione
	 */
	public BigDecimal getStanziamentoVariazione() {
		return stanziamentoVariazione;
	}
	/**
	 * @param stanziamentoVariazione the stanziamentoVariazione to set
	 */
	public void setStanziamentoVariazione(BigDecimal stanziamentoVariazione) {
		this.stanziamentoVariazione = stanziamentoVariazione;
	}
	/**
	 * @return the stanziamentoCassaVariazione
	 */
	public BigDecimal getStanziamentoCassaVariazione() {
		return stanziamentoCassaVariazione;
	}
	/**
	 * @param stanziamentoCassaVariazione the stanziamentoCassaVariazione to set
	 */
	public void setStanziamentoCassaVariazione(BigDecimal stanziamentoCassaVariazione) {
		this.stanziamentoCassaVariazione = stanziamentoCassaVariazione;
	}
	/**
	 * @return the stanziamentoResiduoVariazione
	 */
	public BigDecimal getStanziamentoResiduoVariazione() {
		return stanziamentoResiduoVariazione;
	}
	/**
	 * @param stanziamentoResiduoVariazione the stanziamentoResiduoVariazione to set
	 */
	public void setStanziamentoResiduoVariazione(BigDecimal stanziamentoResiduoVariazione) {
		this.stanziamentoResiduoVariazione = stanziamentoResiduoVariazione;
	}
	/**
	 * @return the stanziamentoVariazioneAnno1
	 */
	public BigDecimal getStanziamentoVariazioneAnno1() {
		return stanziamentoVariazioneAnno1;
	}
	/**
	 * @param stanziamentoVariazioneAnno1 the stanziamentoVariazioneAnno1 to set
	 */
	public void setStanziamentoVariazioneAnno1(BigDecimal stanziamentoVariazioneAnno1) {
		this.stanziamentoVariazioneAnno1 = stanziamentoVariazioneAnno1;
	}
	/**
	 * @return the stanziamentoVariazioneAnno2
	 */
	public BigDecimal getStanziamentoVariazioneAnno2() {
		return stanziamentoVariazioneAnno2;
	}
	/**
	 * @param stanziamentoVariazioneAnno2 the stanziamentoVariazioneAnno2 to set
	 */
	public void setStanziamentoVariazioneAnno2(BigDecimal stanziamentoVariazioneAnno2) {
		this.stanziamentoVariazioneAnno2 = stanziamentoVariazioneAnno2;
	}

	/**
	 * @return the tipologiaFinanziamento
	 */
	public String getTipologiaFinanziamento() {
		return tipologiaFinanziamento;
	}
	/**
	 * @param tipologiaFinanziamento the tipologiaFinanziamento to set
	 */
	public void setTipologiaFinanziamento(String tipologiaFinanziamento) {
		this.tipologiaFinanziamento = tipologiaFinanziamento;
	}
	/**
	 * @return the strutturaAmministrativaResponsabile
	 */
	public String getStrutturaAmministrativaResponsabile() {
		return strutturaAmministrativaResponsabile;
	}
	/**
	 * @param strutturaAmministrativaResponsabile the strutturaAmministrativaResponsabile to set
	 */
	public void setStrutturaAmministrativaResponsabile(String strutturaAmministrativaResponsabile) {
		this.strutturaAmministrativaResponsabile = strutturaAmministrativaResponsabile;
	}
	/**
	 * @return the variazioneNum
	 */
	public Integer getVariazioneNum() {
		return variazioneNum;
	}
	/**
	 * @param variazioneNum the variazioneNum to set
	 */
	public void setVariazioneNum(Integer variazioneNum) {
		this.variazioneNum = variazioneNum;
	}
	
}
