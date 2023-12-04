/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_mutuo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_variazione_tipo")
@NamedQuery(name="SiacDMutuoVariazioneTipo.findAll", query="SELECT s FROM SiacDMutuoVariazioneTipo s")
public class SiacDMutuoVariazioneTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_VARIAZIONE_TIPO_MUTUO_VARIAZIONE_TIPO_IDGENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_VARIAZIONE_TIPO_MUTUO_VARIAZIONE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_VARIAZIONE_TIPO_MUTUO_VARIAZIONE_TIPO_IDGENERATOR")
	@Column(name="mutuo_variazione_tipo_id")
	private Integer mutuoVariazioneTipoId;

	@Column(name="mutuo_variazione_tipo_code")
	private String mutuoVariazioneTipoCode;

	@Column(name="mutuo_variazione_tipo_desc")
	private String mutuoVariazioneTipoDesc;

	public SiacDMutuoVariazioneTipo() {
	}

	@Override
	public Integer getUid() {
		return getMutuoVariazioneTipoId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoVariazioneTipoId(uid);
	}

	public Integer getMutuoVariazioneTipoId() {
		return mutuoVariazioneTipoId;
	}

	public void setMutuoVariazioneTipoId(Integer mutuoVariazioneTipoId) {
		this.mutuoVariazioneTipoId = mutuoVariazioneTipoId;
	}

	public String getMutuoVariazioneTipoCode() {
		return mutuoVariazioneTipoCode;
	}

	public void setMutuoVariazioneTipoCode(String mutuoVariazioneTipoCode) {
		this.mutuoVariazioneTipoCode = mutuoVariazioneTipoCode;
	}

	public String getMutuoVariazioneTipoDesc() {
		return mutuoVariazioneTipoDesc;
	}

	public void setMutuoVariazioneTipoDesc(String mutuoVariazioneTipoDesc) {
		this.mutuoVariazioneTipoDesc = mutuoVariazioneTipoDesc;
	}

	
}