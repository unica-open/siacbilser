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
public class ImportiImpegnatoPerComponenteDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3386995438619634404L;

	private Integer indiceAnnualita;
	
	private Integer elemDetCompId;
	
	private BigDecimal impegnatoDefinitivo;
	
	public Integer getIndiceAnnualita() {
		return indiceAnnualita;
	}
	public void setIndiceAnnualita(Integer indiceAnnualita) {
		this.indiceAnnualita = indiceAnnualita;
	}
	public Integer getElemDetCompId() {
		return elemDetCompId;
	}
	public void setElemDetCompId(Integer elemDetCompId) {
		this.elemDetCompId = elemDetCompId;
	}
	public BigDecimal getImpegnatoDefinitivo() {
		return impegnatoDefinitivo;
	}
	public void setImpegnatoDefinitivo(BigDecimal impegnatoDefinitivo) {
		this.impegnatoDefinitivo = impegnatoDefinitivo;
	}
	

}
