/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_accredito_tipo_cassa_econ database table.
 * 
 */
@Entity
@Table(name="siac_d_accredito_tipo_cassa_econ")
@NamedQuery(name="SiacDAccreditoTipoCassaEcon.findAll", query="SELECT s FROM SiacDAccreditoTipoCassaEcon s")
public class SiacDAccreditoTipoCassaEcon extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ACCREDITO_TIPO_CASSA_ECON_CECACCREDITOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ACCREDITO_TIPO_CASSA_ECON_CEC_ACCREDITO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ACCREDITO_TIPO_CASSA_ECON_CECACCREDITOTIPOID_GENERATOR")
	@Column(name="cec_accredito_tipo_id")
	private Integer cecAccreditoTipoId;

	@Column(name="cec_accredito_tipo_code")
	private String cecAccreditoTipoCode;

	@Column(name="cec_accredito_tipo_desc")
	private String cecAccreditoTipoDesc;

	//bi-directional many-to-one association to SiacRAccreditoTipoCassaEcon
	@OneToMany(mappedBy="siacDAccreditoTipoCassaEcon")
	private List<SiacRAccreditoTipoCassaEcon> siacRAccreditoTipoCassaEcons;

	public SiacDAccreditoTipoCassaEcon() {
	}

	public Integer getCecAccreditoTipoId() {
		return this.cecAccreditoTipoId;
	}

	public void setCecAccreditoTipoId(Integer cecAccreditoTipoId) {
		this.cecAccreditoTipoId = cecAccreditoTipoId;
	}

	public String getCecAccreditoTipoCode() {
		return this.cecAccreditoTipoCode;
	}

	public void setCecAccreditoTipoCode(String cecAccreditoTipoCode) {
		this.cecAccreditoTipoCode = cecAccreditoTipoCode;
	}

	public String getCecAccreditoTipoDesc() {
		return this.cecAccreditoTipoDesc;
	}

	public void setCecAccreditoTipoDesc(String cecAccreditoTipoDesc) {
		this.cecAccreditoTipoDesc = cecAccreditoTipoDesc;
	}

	public List<SiacRAccreditoTipoCassaEcon> getSiacRAccreditoTipoCassaEcons() {
		return this.siacRAccreditoTipoCassaEcons;
	}

	public void setSiacRAccreditoTipoCassaEcons(List<SiacRAccreditoTipoCassaEcon> siacRAccreditoTipoCassaEcons) {
		this.siacRAccreditoTipoCassaEcons = siacRAccreditoTipoCassaEcons;
	}

	public SiacRAccreditoTipoCassaEcon addSiacRAccreditoTipoCassaEcon(SiacRAccreditoTipoCassaEcon siacRAccreditoTipoCassaEcon) {
		getSiacRAccreditoTipoCassaEcons().add(siacRAccreditoTipoCassaEcon);
		siacRAccreditoTipoCassaEcon.setSiacDAccreditoTipoCassaEcon(this);

		return siacRAccreditoTipoCassaEcon;
	}

	public SiacRAccreditoTipoCassaEcon removeSiacRAccreditoTipoCassaEcon(SiacRAccreditoTipoCassaEcon siacRAccreditoTipoCassaEcon) {
		getSiacRAccreditoTipoCassaEcons().remove(siacRAccreditoTipoCassaEcon);
		siacRAccreditoTipoCassaEcon.setSiacDAccreditoTipoCassaEcon(null);

		return siacRAccreditoTipoCassaEcon;
	}
	
	@Override
	public Integer getUid() {
		return cecAccreditoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cecAccreditoTipoId = uid;
		
	}

}