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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_sistema_esterno_ente database table.
 * 
 */
@Entity
@Table(name="siac_r_sistema_esterno_ente")
@NamedQuery(name="SiacRSistemaEsternoEnte.findAll", query="SELECT s FROM SiacRSistemaEsternoEnte s")
public class SiacRSistemaEsternoEnte extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SISTEMA_ESTERNO_ENTE_EXTSYSENTEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SISTEMA_ESTERNO_ENTE_EXTSYS_ENTE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SISTEMA_ESTERNO_ENTE_EXTSYSENTEID_GENERATOR")
	@Column(name="extsys_ente_id")
	private Integer extsysEnteId;

	@Column(name="extsys_ente_code")
	private String extsysEnteCode;

	@Column(name="extsys_ente_desc")
	private String extsysEnteDesc;

	//bi-directional many-to-one association to SiacDSistemaEsterno
	@ManyToOne
	@JoinColumn(name="extsys_id")
	private SiacDSistemaEsterno siacDSistemaEsterno;

	public SiacRSistemaEsternoEnte() {
	}

	public Integer getExtsysEnteId() {
		return this.extsysEnteId;
	}

	public void setExtsysEnteId(Integer extsysEnteId) {
		this.extsysEnteId = extsysEnteId;
	}

	public String getExtsysEnteCode() {
		return this.extsysEnteCode;
	}

	public void setExtsysEnteCode(String extsysEnteCode) {
		this.extsysEnteCode = extsysEnteCode;
	}

	public String getExtsysEnteDesc() {
		return this.extsysEnteDesc;
	}

	public void setExtsysEnteDesc(String extsysEnteDesc) {
		this.extsysEnteDesc = extsysEnteDesc;
	}

	public SiacDSistemaEsterno getSiacDSistemaEsterno() {
		return this.siacDSistemaEsterno;
	}

	public void setSiacDSistemaEsterno(SiacDSistemaEsterno siacDSistemaEsterno) {
		this.siacDSistemaEsterno = siacDSistemaEsterno;
	}

	@Override
	public Integer getUid() {
		return this.extsysEnteId;
	}

	@Override
	public void setUid(Integer uid) {
		this.extsysEnteId = uid;
	}

}