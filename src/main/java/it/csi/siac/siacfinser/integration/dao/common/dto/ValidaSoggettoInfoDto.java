/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;


public class ValidaSoggettoInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1243834148389832754L;
	
	
	private Integer idSiacTSoggetto;
	private Integer idSiacTSoggettoMod;
	
	private String codiceSoggettoPadre;
	private String codiceSoggetto;
	
	public Integer getIdSoggettoOrMod(){
		if(idSiacTSoggettoMod!=null && idSiacTSoggettoMod.intValue()>0){
			return idSiacTSoggettoMod;
		} else {
			return idSiacTSoggetto;
		}
	}
	
	public Integer getIdSiacTSoggetto() {
		return idSiacTSoggetto;
	}
	public void setIdSiacTSoggetto(Integer idSiacTSoggetto) {
		this.idSiacTSoggetto = idSiacTSoggetto;
	}
	public Integer getIdSiacTSoggettoMod() {
		return idSiacTSoggettoMod;
	}
	public void setIdSiacTSoggettoMod(Integer idSiacTSoggettoMod) {
		this.idSiacTSoggettoMod = idSiacTSoggettoMod;
	}
	public String getCodiceSoggettoPadre() {
		return codiceSoggettoPadre;
	}
	public void setCodiceSoggettoPadre(String codiceSoggettoPadre) {
		this.codiceSoggettoPadre = codiceSoggettoPadre;
	}
	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}
	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}
	
}
