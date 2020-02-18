/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_t_recapito_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_t_recapito_soggetto")
public class SiacTRecapitoSoggettoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_RECAPITO_SOGGETTO_RECAPITO_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_recapito_soggetto_recapito_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_RECAPITO_SOGGETTO_RECAPITO_ID_GENERATOR")
	@Column(name="recapito_id")
	private Integer recapitoId;

	private String avviso;

	@Column(name="recapito_code")
	private String recapitoCode;

	@Column(name="recapito_desc")
	private String recapitoDesc;

	//bi-directional many-to-one association to SiacDRecapitoModoFin
	@ManyToOne
	@JoinColumn(name="recapito_modo_id")
	private SiacDRecapitoModoFin siacDRecapitoModo;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacTRecapitoSoggettoFin() {
	}

	public Integer getRecapitoId() {
		return this.recapitoId;
	}

	public void setRecapitoId(Integer recapitoId) {
		this.recapitoId = recapitoId;
	}

	public String getAvviso() {
		return this.avviso;
	}

	public void setAvviso(String avviso) {
		this.avviso = avviso;
	}

	public String getRecapitoCode() {
		return this.recapitoCode;
	}

	public void setRecapitoCode(String recapitoCode) {
		this.recapitoCode = recapitoCode;
	}

	public String getRecapitoDesc() {
		return this.recapitoDesc;
	}

	public void setRecapitoDesc(String recapitoDesc) {
		this.recapitoDesc = recapitoDesc;
	}

	public SiacDRecapitoModoFin getSiacDRecapitoModo() {
		return this.siacDRecapitoModo;
	}

	public void setSiacDRecapitoModo(SiacDRecapitoModoFin siacDRecapitoModo) {
		this.siacDRecapitoModo = siacDRecapitoModo;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.recapitoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.recapitoId = uid;
	}
}