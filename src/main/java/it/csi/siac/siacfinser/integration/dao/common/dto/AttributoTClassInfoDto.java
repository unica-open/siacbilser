/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacRClassBaseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontEsteraFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoModFin;

public class AttributoTClassInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OggettoDellAttributoTClass tipoOggetto;
	
	//OGGETTO PRINCIPALE
	private SiacTMovgestTsFin siacTMovgestTs;
	private SiacTLiquidazioneFin siacTLiquidazione;
	private SiacTOrdinativoFin siacTOrdinativo;
	private SiacTSoggettoFin siacTSoggetto;
	private SiacTSoggettoModFin siacTSoggettoMod;
	private SiacTCartacontFin siacTCartacont;
	private SiacTCartacontEsteraFin siacTCartacontEstera;
	private SiacTCartacontDetFin siacTCartacontDet;
	//
	
	private SiacRMovgestClassFin siacRMovgestClass;
	private SiacRLiquidazioneClassFin siacRLiquidazioneClass;
	private SiacROrdinativoClassFin siacROrdinativoClass;

	
	public <T extends SiacRClassBaseFin> T getRClassBase(){
		if(OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOggetto)){
			return (T) siacRMovgestClass;
		} else if(OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOggetto)){
			return (T) siacRLiquidazioneClass;
		} else if(OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOggetto)){
			return (T) siacROrdinativoClass;
		} else if(OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOggetto)){
			return null;
		} else if(OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOggetto)){
			return null;
		}
		return null;
	}
	
	public Integer getIdOggetto(){
		Integer id = null;
		if(OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOggetto)){
			id = siacTMovgestTs.getMovgestTsId();
		} else if(OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOggetto)){
			id = siacTLiquidazione.getLiqId();
		} else if(OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOggetto)){
			id = siacTOrdinativo.getOrdId();
		} else if(OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOggetto)){
			id = siacTSoggetto.getSoggettoId();
		} else if(OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOggetto)){
			id = siacTSoggettoMod.getSogModId();
		} else if(OggettoDellAttributoTClass.T_CARTACONT.equals(tipoOggetto)){
			id = siacTCartacont.getCartacId();
		} else if(OggettoDellAttributoTClass.T_CARTACONT_ESTERA.equals(tipoOggetto)){
			id = siacTCartacontEstera.getCartacestId();
		} else if(OggettoDellAttributoTClass.T_CARTACONT_DET.equals(tipoOggetto)){
			id = siacTCartacontDet.getCartacDetId();
		}
		return id;
	}
	
	
	public OggettoDellAttributoTClass getTipoOggetto() {
		return tipoOggetto;
	}
	public void setTipoOggetto(OggettoDellAttributoTClass tipoOggetto) {
		this.tipoOggetto = tipoOggetto;
	}
	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return siacTMovgestTs;
	}
	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}
	public SiacRMovgestClassFin getSiacRMovgestClass() {
		return siacRMovgestClass;
	}
	public void setSiacRMovgestClass(SiacRMovgestClassFin siacRMovgestClass) {
		this.siacRMovgestClass = siacRMovgestClass;
	}



	public SiacTLiquidazioneFin getSiacTLiquidazione() {
		return siacTLiquidazione;
	}



	public void setSiacTLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		this.siacTLiquidazione = siacTLiquidazione;
	}



	public SiacRLiquidazioneClassFin getSiacRLiquidazioneClass() {
		return siacRLiquidazioneClass;
	}



	public void setSiacRLiquidazioneClass(
			SiacRLiquidazioneClassFin siacRLiquidazioneClass) {
		this.siacRLiquidazioneClass = siacRLiquidazioneClass;
	}



	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return siacTOrdinativo;
	}



	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}



	public SiacROrdinativoClassFin getSiacROrdinativoClass() {
		return siacROrdinativoClass;
	}



	public void setSiacROrdinativoClass(SiacROrdinativoClassFin siacROrdinativoClass) {
		this.siacROrdinativoClass = siacROrdinativoClass;
	}


	public SiacTSoggettoFin getSiacTSoggetto() {
		return siacTSoggetto;
	}


	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}


	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return siacTSoggettoMod;
	}


	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}


	public SiacTCartacontFin getSiacTCartacont() {
		return siacTCartacont;
	}


	public void setSiacTCartacont(SiacTCartacontFin siacTCartacont) {
		this.siacTCartacont = siacTCartacont;
	}


	public SiacTCartacontEsteraFin getSiacTCartacontEstera() {
		return siacTCartacontEstera;
	}


	public void setSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		this.siacTCartacontEstera = siacTCartacontEstera;
	}


	public SiacTCartacontDetFin getSiacTCartacontDet() {
		return siacTCartacontDet;
	}


	public void setSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}
	
}
