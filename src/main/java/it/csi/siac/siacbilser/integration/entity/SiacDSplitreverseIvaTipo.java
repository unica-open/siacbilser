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
 * The persistent class for the siac_d_splitreverse_iva_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_splitreverse_iva_tipo")
@NamedQuery(name="SiacDSplitreverseIvaTipo.findAll", query="SELECT s FROM SiacDSplitreverseIvaTipo s")
public class SiacDSplitreverseIvaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_SPLITREVERSE_IVA_TIPO_SRIVATIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SPLITREVERSE_IVA_TIPO_SRIVA_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SPLITREVERSE_IVA_TIPO_SRIVATIPOID_GENERATOR")
	@Column(name="sriva_tipo_id")
	private Integer srivaTipoId;

	@Column(name="sriva_tipo_code")
	private String srivaTipoCode;

	@Column(name="sriva_tipo_desc")
	private String srivaTipoDesc;

	//bi-directional many-to-one association to SiacROnereSplitreverseIvaTipo
	@OneToMany(mappedBy="siacDSplitreverseIvaTipo")
	private List<SiacROnereSplitreverseIvaTipo> siacROnereSplitreverseIvaTipos;
	
	//bi-directional many-to-one association to SiacRSubdocSplitreverseIvaTipo
	@OneToMany(mappedBy="siacDSplitreverseIvaTipo")
	private List<SiacRSubdocSplitreverseIvaTipo> siacRSubdocSplitreverseIvaTipos;

	public SiacDSplitreverseIvaTipo() {
	}

	public Integer getSrivaTipoId() {
		return this.srivaTipoId;
	}

	public void setSrivaTipoId(Integer srivaTipoId) {
		this.srivaTipoId = srivaTipoId;
	}

	public String getSrivaTipoCode() {
		return this.srivaTipoCode;
	}

	public void setSrivaTipoCode(String srivaTipoCode) {
		this.srivaTipoCode = srivaTipoCode;
	}

	public String getSrivaTipoDesc() {
		return this.srivaTipoDesc;
	}

	public void setSrivaTipoDesc(String srivaTipoDesc) {
		this.srivaTipoDesc = srivaTipoDesc;
	}

	public List<SiacROnereSplitreverseIvaTipo> getSiacROnereSplitreverseIvaTipos() {
		return this.siacROnereSplitreverseIvaTipos;
	}

	public void setSiacROnereSplitreverseIvaTipos(List<SiacROnereSplitreverseIvaTipo> siacROnereSplitreverseIvaTipos) {
		this.siacROnereSplitreverseIvaTipos = siacROnereSplitreverseIvaTipos;
	}

	public SiacROnereSplitreverseIvaTipo addSiacROnereSplitreverseIvaTipo(SiacROnereSplitreverseIvaTipo siacROnereSplitreverseIvaTipo) {
		getSiacROnereSplitreverseIvaTipos().add(siacROnereSplitreverseIvaTipo);
		siacROnereSplitreverseIvaTipo.setSiacDSplitreverseIvaTipo(this);

		return siacROnereSplitreverseIvaTipo;
	}

	public SiacROnereSplitreverseIvaTipo removeSiacROnereSplitreverseIvaTipo(SiacROnereSplitreverseIvaTipo siacROnereSplitreverseIvaTipo) {
		getSiacROnereSplitreverseIvaTipos().remove(siacROnereSplitreverseIvaTipo);
		siacROnereSplitreverseIvaTipo.setSiacDSplitreverseIvaTipo(null);

		return siacROnereSplitreverseIvaTipo;
	}

	public List<SiacRSubdocSplitreverseIvaTipo> getSiacRSubdocSplitreverseIvaTipos() {
		return this.siacRSubdocSplitreverseIvaTipos;
	}

	public void setSiacRSubdocSplitreverseIvaTipos(List<SiacRSubdocSplitreverseIvaTipo> siacRSubdocSplitreverseIvaTipos) {
		this.siacRSubdocSplitreverseIvaTipos = siacRSubdocSplitreverseIvaTipos;
	}

	public SiacRSubdocSplitreverseIvaTipo addSiacRSubdocSplitreverseIvaTipo(SiacRSubdocSplitreverseIvaTipo siacRSubdocSplitreverseIvaTipo) {
		getSiacRSubdocSplitreverseIvaTipos().add(siacRSubdocSplitreverseIvaTipo);
		siacRSubdocSplitreverseIvaTipo.setSiacDSplitreverseIvaTipo(this);

		return siacRSubdocSplitreverseIvaTipo;
	}

	public SiacRSubdocSplitreverseIvaTipo removeSiacRSubdocSplitreverseIvaTipo(SiacRSubdocSplitreverseIvaTipo siacRSubdocSplitreverseIvaTipo) {
		getSiacRSubdocSplitreverseIvaTipos().remove(siacRSubdocSplitreverseIvaTipo);
		siacRSubdocSplitreverseIvaTipo.setSiacDSplitreverseIvaTipo(null);

		return siacRSubdocSplitreverseIvaTipo;
	}

	@Override
	public Integer getUid() {
		return this.srivaTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.srivaTipoId = uid;
	}

}