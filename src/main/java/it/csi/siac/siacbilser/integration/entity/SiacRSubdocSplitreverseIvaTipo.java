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
 * The persistent class for the siac_r_subdoc_splitreverse_iva_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_splitreverse_iva_tipo")
@NamedQuery(name="SiacRSubdocSplitreverseIvaTipo.findAll", query="SELECT s FROM SiacRSubdocSplitreverseIvaTipo s")
public class SiacRSubdocSplitreverseIvaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_SPLITREVERSE_IVA_TIPO_SDCSRITID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_SPLITREVERSE_IVA_TIPO_SDCSRIT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_SPLITREVERSE_IVA_TIPO_SDCSRITID_GENERATOR")
	@Column(name="sdcsrit_id")
	private Integer sdcsritId;

	//bi-directional many-to-one association to SiacDSplitreverseIvaTipo
	@ManyToOne
	@JoinColumn(name="sriva_tipo_id")
	private SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo;

	//bi-directional many-to-one association to SiacTSubdoc
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	public SiacRSubdocSplitreverseIvaTipo() {
	}

	public Integer getSdcsritId() {
		return this.sdcsritId;
	}

	public void setSdcsritId(Integer sdcsritId) {
		this.sdcsritId = sdcsritId;
	}

	public SiacDSplitreverseIvaTipo getSiacDSplitreverseIvaTipo() {
		return this.siacDSplitreverseIvaTipo;
	}

	public void setSiacDSplitreverseIvaTipo(SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo) {
		this.siacDSplitreverseIvaTipo = siacDSplitreverseIvaTipo;
	}

	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		return this.sdcsritId;
	}

	@Override
	public void setUid(Integer uid) {
		this.sdcsritId = uid;
	}

}