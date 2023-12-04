/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_prov_cassa_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_prov_cassa_tipo")
public class SiacDProvCassaTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="provc_tipo_id")
	private Integer provcTipoId;

	@Column(name="provc_tipo_code")
	private String provcTipoCode;

	@Column(name="provc_tipo_desc")
	private String provcTipoDesc;

	//bi-directional many-to-one association to SiacTProvCassaFin
	@OneToMany(mappedBy="siacDProvCassaTipo")
	private List<SiacTProvCassaFin> siacTProvCassas;

	public SiacDProvCassaTipoFin() {
	}

	public Integer getProvcTipoId() {
		return this.provcTipoId;
	}

	public void setProvcTipoId(Integer provcTipoId) {
		this.provcTipoId = provcTipoId;
	}

	public String getProvcTipoCode() {
		return this.provcTipoCode;
	}

	public void setProvcTipoCode(String provcTipoCode) {
		this.provcTipoCode = provcTipoCode;
	}

	public String getProvcTipoDesc() {
		return this.provcTipoDesc;
	}

	public void setProvcTipoDesc(String provcTipoDesc) {
		this.provcTipoDesc = provcTipoDesc;
	}

	public List<SiacTProvCassaFin> getSiacTProvCassas() {
		return this.siacTProvCassas;
	}

	public void setSiacTProvCassas(List<SiacTProvCassaFin> siacTProvCassas) {
		this.siacTProvCassas = siacTProvCassas;
	}

	public SiacTProvCassaFin addSiacTProvCassa(SiacTProvCassaFin siacTProvCassa) {
		getSiacTProvCassas().add(siacTProvCassa);
		siacTProvCassa.setSiacDProvCassaTipo(this);

		return siacTProvCassa;
	}

	public SiacTProvCassaFin removeSiacTProvCassa(SiacTProvCassaFin siacTProvCassa) {
		getSiacTProvCassas().remove(siacTProvCassa);
		siacTProvCassa.setSiacDProvCassaTipo(null);

		return siacTProvCassa;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.provcTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.provcTipoId = uid;
	}
}