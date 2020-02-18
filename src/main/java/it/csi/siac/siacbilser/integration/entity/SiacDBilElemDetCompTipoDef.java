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
 * The persistent class for the siac_d_bil_elem_det_comp_tipo_def database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo_def")
@NamedQuery(name="SiacDBilElemDetCompTipoDef.findAll", query="SELECT s FROM SiacDBilElemDetCompTipoDef s")
public class SiacDBilElemDetCompTipoDef extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_TIPO_DEF_ELEMDETCOMPTIPODEFID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO_DEF_ELEM_DET_COMP_TIPO_DEF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_TIPO_DEF_ELEMDETCOMPTIPODEFID_GENERATOR")
	@Column(name="elem_det_comp_tipo_def_id")
	private Integer elemDetCompTipoDefId;

	@Column(name="elem_det_comp_tipo_def_code")
	private String elemDetCompTipoDefCode;

	@Column(name="elem_det_comp_tipo_def_desc")
	private String elemDetCompTipoDefDesc;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@OneToMany(mappedBy="siacDBilElemDetCompTipoDef")
	private List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipoDef() {
	}


	/**
	 * @return the elemDetCompTipoDefId
	 */
	public Integer getElemDetCompTipoDefId() {
		return this.elemDetCompTipoDefId;
	}


	/**
	 * @param elemDetCompTipoDefId the elemDetCompTipoDefId to set
	 */
	public void setElemDetCompTipoDefId(Integer elemDetCompTipoDefId) {
		this.elemDetCompTipoDefId = elemDetCompTipoDefId;
	}


	/**
	 * @return the elemDetCompTipoDefCode
	 */
	public String getElemDetCompTipoDefCode() {
		return this.elemDetCompTipoDefCode;
	}


	/**
	 * @param elemDetCompTipoDefCode the elemDetCompTipoDefCode to set
	 */
	public void setElemDetCompTipoDefCode(String elemDetCompTipoDefCode) {
		this.elemDetCompTipoDefCode = elemDetCompTipoDefCode;
	}


	/**
	 * @return the elemDetCompTipoDefDesc
	 */
	public String getElemDetCompTipoDefDesc() {
		return this.elemDetCompTipoDefDesc;
	}


	/**
	 * @param elemDetCompTipoDefDesc the elemDetCompTipoDefDesc to set
	 */
	public void setElemDetCompTipoDefDesc(String elemDetCompTipoDefDesc) {
		this.elemDetCompTipoDefDesc = elemDetCompTipoDefDesc;
	}


	/**
	 * @return the siacDBilElemDetCompTipos
	 */
	public List<SiacDBilElemDetCompTipo> getSiacDBilElemDetCompTipos() {
		return this.siacDBilElemDetCompTipos;
	}


	/**
	 * @param siacDBilElemDetCompTipos the siacDBilElemDetCompTipos to set
	 */
	public void setSiacDBilElemDetCompTipos(List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos) {
		this.siacDBilElemDetCompTipos = siacDBilElemDetCompTipos;
	}


	@Override
	public Integer getUid() {
		return elemDetCompTipoDefId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompTipoDefId = uid;
	}


}