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
 * The persistent class for the siac_r_onere_splitreverse_iva_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_onere_splitreverse_iva_tipo")
@NamedQuery(name="SiacROnereSplitreverseIvaTipo.findAll", query="SELECT s FROM SiacROnereSplitreverseIvaTipo s")
public class SiacROnereSplitreverseIvaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ONERE_SPLITREVERSE_IVA_TIPO_ONSRITID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ONERE_SPLITREVERSE_IVA_TIPO_ONSRIT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ONERE_SPLITREVERSE_IVA_TIPO_ONSRITID_GENERATOR")
	@Column(name="onsrit_id")
	private Integer onsritId;

	//bi-directional many-to-one association to SiacDSplitreverseIvaTipo
	@ManyToOne
	@JoinColumn(name="sriva_tipo_id")
	private SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo;
	
	// bi-directional many-to-one association to SiacDSplitreverseIvaTipo
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnere siacDOnere;

	public SiacROnereSplitreverseIvaTipo() {
	}

	public Integer getOnsritId() {
		return this.onsritId;
	}

	public void setOnsritId(Integer onsritId) {
		this.onsritId = onsritId;
	}

	public SiacDSplitreverseIvaTipo getSiacDSplitreverseIvaTipo() {
		return this.siacDSplitreverseIvaTipo;
	}

	public void setSiacDSplitreverseIvaTipo(SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo) {
		this.siacDSplitreverseIvaTipo = siacDSplitreverseIvaTipo;
	}

	public SiacDOnere getSiacDOnere() {
		return siacDOnere;
	}

	public void setSiacDOnere(SiacDOnere siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	@Override
	public Integer getUid() {
		return this.onsritId;
	}

	@Override
	public void setUid(Integer uid) {
		this.onsritId = uid;
	}

}