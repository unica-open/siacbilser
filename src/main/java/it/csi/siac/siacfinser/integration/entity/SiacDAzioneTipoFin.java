/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_d_azione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_azione_tipo")
public class SiacDAzioneTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="azione_tipo_id")
	private Integer azioneTipoId;

	@Column(name="azione_tipo_code")
	private String azioneTipoCode;

	@Column(name="azione_tipo_desc")
	private String azioneTipoDesc;

	public SiacDAzioneTipoFin() {
	}

	public Integer getAzioneTipoId() {
		return this.azioneTipoId;
	}

	public void setAzioneTipoId(Integer azioneTipoId) {
		this.azioneTipoId = azioneTipoId;
	}

	public String getAzioneTipoCode() {
		return this.azioneTipoCode;
	}

	public void setAzioneTipoCode(String azioneTipoCode) {
		this.azioneTipoCode = azioneTipoCode;
	}

	public String getAzioneTipoDesc() {
		return this.azioneTipoDesc;
	}

	public void setAzioneTipoDesc(String azioneTipoDesc) {
		this.azioneTipoDesc = azioneTipoDesc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.azioneTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.azioneTipoId = uid;
	}
}