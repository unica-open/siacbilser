/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

public class RicercaMutuoParamDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2780892332493643462L;
		
	private String numeroMutuo = null;
	private String numeroRegistrazioneMutuo = null;
	
	private String codiceIsitutoMutuante  = null;
	
	private Integer annoProvvedimento = null;
	private Integer numeroProvvedimento = null;	
	private String codiceTipoProvvedimento = null;
	
	private Integer strutturaAmministrativoContabileDelProvvedimento;
	
	private Integer uidProvvedimento;
	
	public String getNumeroMutuo() {
		return numeroMutuo;
	}
	
	public void setNumeroMutuo(String numeroMutuo) {
		this.numeroMutuo = numeroMutuo;
	}
	
	public String getNumeroRegistrazioneMutuo() {
		return numeroRegistrazioneMutuo;
	}
	
	public void setNumeroRegistrazioneMutuo(String numeroRegistrazioneMutuo) {
		this.numeroRegistrazioneMutuo = numeroRegistrazioneMutuo;
	}

	public String getCodiceIsitutoMutuante() {
		return codiceIsitutoMutuante;
	}

	public void setCodiceIsitutoMutuante(String codiceIsitutoMutuante) {
		this.codiceIsitutoMutuante = codiceIsitutoMutuante;
	}

	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}

	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}

	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}

	public String getCodiceTipoProvvedimento() {
		return codiceTipoProvvedimento;
	}

	public void setCodiceTipoProvvedimento(String codiceTipoProvvedimento) {
		this.codiceTipoProvvedimento = codiceTipoProvvedimento;
	}

	public Integer getStrutturaAmministrativoContabileDelProvvedimento() {
		return strutturaAmministrativoContabileDelProvvedimento;
	}

	public void setStrutturaAmministrativoContabileDelProvvedimento(
			Integer strutturaAmministrativoContabileDelProvvedimento) {
		this.strutturaAmministrativoContabileDelProvvedimento = strutturaAmministrativoContabileDelProvvedimento;
	}

	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}

	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}
	
}
