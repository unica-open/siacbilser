/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


//SIAC-7349  - START - MR - SR200 - 09/04/2020 Dto per record da function calcolo impegnato
public class ImportiImpegnatoPerComponenteTriennioNoStanzDto implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4636867839581755734L;
	

	private Integer idComponente;
	private String descrizioneComponente;
	private Integer annoImpegnato;
	private BigDecimal importo;
	private String descrizioneMacrotipoComponente;
	
	
	
	
	public String getDescrizioneMacrotipoComponente() {
		return descrizioneMacrotipoComponente;
	}
	public void setDescrizioneMacrotipoComponente(String descrizioneMacrotipoComponente) {
		this.descrizioneMacrotipoComponente = descrizioneMacrotipoComponente;
	}
	public Integer getAnnoImpegnato() {
		return annoImpegnato;
	}
	public void setAnnoImpegnato(Integer annoImpegnato) {
		this.annoImpegnato = annoImpegnato;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Integer getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}
	public String getDescrizioneComponente() {
		return descrizioneComponente;
	}
	public void setDescrizioneComponente(String descrizioneComponente) {
		this.descrizioneComponente = descrizioneComponente;
	}
	
	
	

}
