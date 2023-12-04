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
 * The persistent class for the siac_d_siope_assenza_motivazione database table.
 * 
 */
@Entity
@Table(name="siac_d_siope_tipo_debito")
public class SiacDSiopeTipoDebitoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="siope_tipo_debito_id")
	private Integer siopeTipoDebitoId;

	@Column(name="siope_tipo_debito_code")
	private String siopeTipoDebitoCode;

	@Column(name="siope_tipo_debito_desc")
	private String siopeTipoDebitoDesc;

	public Integer getSiopeTipoDebitoId() {
		return siopeTipoDebitoId;
	}

	public void setSiopeTipoDebitoId(Integer siopeTipoDebitoId) {
		this.siopeTipoDebitoId = siopeTipoDebitoId;
	}

	public String getSiopeTipoDebitoCode() {
		return siopeTipoDebitoCode;
	}

	public void setSiopeTipoDebitoCode(String siopeTipoDebitoCode) {
		this.siopeTipoDebitoCode = siopeTipoDebitoCode;
	}

	public String getSiopeTipoDebitoDesc() {
		return siopeTipoDebitoDesc;
	}

	public void setSiopeTipoDebitoDesc(String siopeTipoDebitoDesc) {
		this.siopeTipoDebitoDesc = siopeTipoDebitoDesc;
	}

	@Override
	public Integer getUid() {
		return this.siopeTipoDebitoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.siopeTipoDebitoId = uid;
	}
}