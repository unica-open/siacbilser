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
@Table(name="siac_d_mutuo_stato")
@NamedQuery(name="SiacDMutuoStato.findAll", query="SELECT s FROM SiacDMutuoStato s")
public class SiacDMutuoStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_STATO_MUTUO_STATO_IDGENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_STATO_MUTUO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_STATO_MUTUO_STATO_IDGENERATOR")
	@Column(name="mutuo_stato_id")
	private Integer mutuoStatoId;

	@Column(name="mutuo_stato_code")
	private String mutuoStatoCode;

	@Column(name="mutuo_stato_desc")
	private String mutuoStatoDesc;

	public SiacDMutuoStato() {
	}

	public Integer getMutuoStatoId() {
		return this.mutuoStatoId;
	}

	public void setMutuoStatoId(Integer mutuoStatoId) {
		this.mutuoStatoId = mutuoStatoId;
	}
	
	public String getMutuoStatoCode() {
		return mutuoStatoCode;
	}

	public void setMutuoStatoCode(String mutuoStatoCode) {
		this.mutuoStatoCode = mutuoStatoCode;
	}

	public String getMutuoStatoDesc() {
		return mutuoStatoDesc;
	}

	public void setMutuoStatoDesc(String mutuoStatoDesc) {
		this.mutuoStatoDesc = mutuoStatoDesc;
	}

	@Override
	public Integer getUid() {
		return getMutuoStatoId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoStatoId(uid);
	}

}