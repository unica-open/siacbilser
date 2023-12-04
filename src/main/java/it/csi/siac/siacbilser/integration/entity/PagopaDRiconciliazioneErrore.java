/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="pagopa_d_riconciliazione_errore")
public class PagopaDRiconciliazioneErrore extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="pagopa_d_riconciliazione_errore_pagopaRicErroreId_GENERATOR", allocationSize=1, 
						sequenceName="pagopa_d_riconciliazione_errore_pagopa_ric_errore_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pagopa_d_riconciliazione_errore_pagopaRicErroreId_GENERATOR")
	@Column(name="pagopa_ric_errore_id")
	private Integer pagopaRicErroreId;
	
	@Column(name="pagopa_ric_errore_code")
	private String pagopaRicErroreCode;

	@Column(name="pagopa_ric_errore_desc")
	private String pagopaRicErroreDesc;

	public PagopaDRiconciliazioneErrore() {
	}

	@Override
	public Integer getUid() {
		return pagopaRicErroreId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaRicErroreId = uid;
	}

	public String getPagopaRicErroreCode() {
		return pagopaRicErroreCode;
	}

	public void setPagopaRicErroreCode(String pagopaRicErroreCode) {
		this.pagopaRicErroreCode = pagopaRicErroreCode;
	}

	public String getPagopaRicErroreDesc() {
		return pagopaRicErroreDesc;
	}

	public void setPagopaRicErroreDesc(String pagopaRicErroreDesc) {
		this.pagopaRicErroreDesc = pagopaRicErroreDesc;
	}

	public Integer getPagopaRicErroreId() {
		return pagopaRicErroreId;
	}

	public void setPagopaRicErroreId(Integer pagopaRicErroreId) {
		this.pagopaRicErroreId = pagopaRicErroreId;
	}
	
	public String getCodiceDescrizione() {
		return String.format("%s - %s", pagopaRicErroreCode, pagopaRicErroreDesc);
	}

}