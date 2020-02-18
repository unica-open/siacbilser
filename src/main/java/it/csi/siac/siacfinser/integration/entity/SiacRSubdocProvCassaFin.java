/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_subdoc_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_prov_cassa")
public class SiacRSubdocProvCassaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_provc_id")
	private Integer subdocProvcId;

	//bi-directional many-to-one association to SiacTProvCassaFin
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassaFin siacTProvCassa;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	public SiacRSubdocProvCassaFin() {
	}

	public Integer getSubdocProvcId() {
		return this.subdocProvcId;
	}

	public void setSubdocProvcId(Integer subdocProvcId) {
		this.subdocProvcId = subdocProvcId;
	}

	public SiacTProvCassaFin getSiacTProvCassa() {
		return this.siacTProvCassa;
	}

	public void setSiacTProvCassa(SiacTProvCassaFin siacTProvCassa) {
		this.siacTProvCassa = siacTProvCassa;
	}

	public SiacTSubdocFin getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocProvcId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocProvcId = uid;
	}
}