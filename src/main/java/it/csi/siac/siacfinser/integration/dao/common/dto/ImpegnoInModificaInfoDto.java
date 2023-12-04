/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubImpegno;

public class ImpegnoInModificaInfoDto <I extends MovimentoGestione> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	
	//DATI "OLD" DA CARICARE DA DB
	private SiacTMovgestFin siacTMovgest;
	private SiacTMovgestTsFin siacTMovgestTs;
	private boolean doppiaGestione=false;
	
	Integer chiaveCapitoloResiduo = null;
	
//	private BigDecimal importoAttualePrimaDellaModifica;
	private SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmmOld;
	
	//DATI "NEW"
//	private AttoAmministrativo attoNewCaricatoDaServizio;
	
	private SubMovgestInModificaInfoDto<I> infoSubValutati;
	
	//VINCOLI IN MODIFICA, SOLO PER IMPEGNO:
	private ModificaVincoliImpegnoInfoDto infoVincoliValutati;
	//////////////////////////////////////////
	
	//OTTIMIZZAZIONI MAGGIO 2016 - Lista di tutti i sub movimenti, con solo gli ids e i dati minimi veloci da caricare:
	private List<SubImpegno> tuttiISubSoloGliIds;
	private OttimizzazioneMovGestDto ottimizzazioneMovGest;
	//
	
	//OTTIMIZZAZIONE GENNAIO 2017 - Modifiche automatiche accertamenti 
	private List<Integer> modificheAutomaticheIds;
	
	
	private boolean flagSDF;
	
	private boolean flagFrazionabile;
	private boolean flagFrazionabileValorizzato;
	
	private boolean flagFattura;
	
	/**
	 * @return the flagFattura
	 */
	public boolean isFlagFattura() {
		return flagFattura;
	}
	/**
	 * @param flagFattura the flagFattura to set
	 */
	public void setFlagFattura(boolean flagFattura) {
		this.flagFattura = flagFattura;
	}
	public SiacTMovgestFin getSiacTMovgest() {
		return siacTMovgest;
	}
	public void setSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}
	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return siacTMovgestTs;
	}
	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}
	public SubMovgestInModificaInfoDto<I> getInfoSubValutati() {
		return infoSubValutati;
	}
	public void setInfoSubValutati(SubMovgestInModificaInfoDto<I> infoSubValutati) {
		this.infoSubValutati = infoSubValutati;
	}
	public boolean isDoppiaGestione() {
		return doppiaGestione;
	}
	public void setDoppiaGestione(boolean doppiaGestione) {
		this.doppiaGestione = doppiaGestione;
	}
	public SiacRMovgestTsAttoAmmFin getSiacRMovgestTsAttoAmmOld() {
		return siacRMovgestTsAttoAmmOld;
	}
	public void setSiacRMovgestTsAttoAmmOld(
			SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmmOld) {
		this.siacRMovgestTsAttoAmmOld = siacRMovgestTsAttoAmmOld;
	}
	public Integer getChiaveCapitoloResiduo() {
		return chiaveCapitoloResiduo;
	}
	public void setChiaveCapitoloResiduo(Integer chiaveCapitoloResiduo) {
		this.chiaveCapitoloResiduo = chiaveCapitoloResiduo;
	}
	public ModificaVincoliImpegnoInfoDto getInfoVincoliValutati() {
		return infoVincoliValutati;
	}
	public void setInfoVincoliValutati(
			ModificaVincoliImpegnoInfoDto infoVincoliValutati) {
		this.infoVincoliValutati = infoVincoliValutati;
	}
	public List<SubImpegno> getTuttiISubSoloGliIds() {
		return tuttiISubSoloGliIds;
	}
	public void setTuttiISubSoloGliIds(List<SubImpegno> tuttiISubSoloGliIds) {
		this.tuttiISubSoloGliIds = tuttiISubSoloGliIds;
	}
	public OttimizzazioneMovGestDto getOttimizzazioneMovGest() {
		return ottimizzazioneMovGest;
	}
	public void setOttimizzazioneMovGest(
			OttimizzazioneMovGestDto ottimizzazioneMovGest) {
		this.ottimizzazioneMovGest = ottimizzazioneMovGest;
	}
	public boolean isFlagSDF() {
		return flagSDF;
	}
	public void setFlagSDF(boolean flagSDF) {
		this.flagSDF = flagSDF;
	}
	public boolean isFlagFrazionabile() {
		return flagFrazionabile;
	}
	public void setFlagFrazionabile(boolean flagFrazionabile) {
		this.flagFrazionabile = flagFrazionabile;
	}
	public boolean isFlagFrazionabileValorizzato() {
		return flagFrazionabileValorizzato;
	}
	public void setFlagFrazionabileValorizzato(boolean flagFrazionabileValorizzato) {
		this.flagFrazionabileValorizzato = flagFrazionabileValorizzato;
	}
	public List<Integer> getModificheAutomaticheIds() {
		return modificheAutomaticheIds;
	}
	public void setModificheAutomaticheIds(List<Integer> modificheAutomaticheIds) {
		this.modificheAutomaticheIds = modificheAutomaticheIds;
	}
	
	public boolean isUnaModificaAutomatica(Integer id){
		if(id!=null && modificheAutomaticheIds!=null && modificheAutomaticheIds.contains(id)){
			return true;
		}
		return false;
	}
	
}
