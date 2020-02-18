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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the siac_d_bil_elem_det_comp_sotto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_sotto_tipo")
@NamedQuery(name="SiacDBilElemDetCompSottoTipo.findAll", query="SELECT s FROM SiacDBilElemDetCompSottoTipo s")
public class SiacDBilElemDetCompSottoTipo extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_SOTTO_TIPO_ELEMDETCOMPSOTTOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_SOTTO__ELEM_DET_COMP_SOTTO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_SOTTO_TIPO_ELEMDETCOMPSOTTOTIPOID_GENERATOR")
	@Column(name="elem_det_comp_sotto_tipo_id")
	private Integer elemDetCompSottoTipoId;

	@Column(name="elem_det_comp_sotto_tipo_code")
	private String elemDetCompSottoTipoCode;

	@Column(name="elem_det_comp_sotto_tipo_desc")
	private String elemDetCompSottoTipoDesc;

	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_macro_tipo_id")
	private SiacDBilElemDetCompMacroTipo siacDBilElemDetCompMacroTipo;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@OneToMany(mappedBy="siacDBilElemDetCompSottoTipo")
	private List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompSottoTipo() {
	}

	/**
	 * @return the elemDetCompSottoTipoId
	 */
	public Integer getElemDetCompSottoTipoId() {
		return this.elemDetCompSottoTipoId;
	}

	/**
	 * @param elemDetCompSottoTipoId the elemDetCompSottoTipoId to set
	 */
	public void setElemDetCompSottoTipoId(Integer elemDetCompSottoTipoId) {
		this.elemDetCompSottoTipoId = elemDetCompSottoTipoId;
	}

	/**
	 * @return the elemDetCompSottoTipoCode
	 */
	public String getElemDetCompSottoTipoCode() {
		return this.elemDetCompSottoTipoCode;
	}

	/**
	 * @param elemDetCompSottoTipoCode the elemDetCompSottoTipoCode to set
	 */
	public void setElemDetCompSottoTipoCode(String elemDetCompSottoTipoCode) {
		this.elemDetCompSottoTipoCode = elemDetCompSottoTipoCode;
	}

	/**
	 * @return the elemDetCompSottoTipoDesc
	 */
	public String getElemDetCompSottoTipoDesc() {
		return this.elemDetCompSottoTipoDesc;
	}

	/**
	 * @param elemDetCompSottoTipoDesc the elemDetCompSottoTipoDesc to set
	 */
	public void setElemDetCompSottoTipoDesc(String elemDetCompSottoTipoDesc) {
		this.elemDetCompSottoTipoDesc = elemDetCompSottoTipoDesc;
	}

	/**
	 * @return the siacDBilElemDetCompMacroTipo
	 */
	public SiacDBilElemDetCompMacroTipo getSiacDBilElemDetCompMacroTipo() {
		return this.siacDBilElemDetCompMacroTipo;
	}

	/**
	 * @param siacDBilElemDetCompMacroTipo the siacDBilElemDetCompMacroTipo to set
	 */
	public void setSiacDBilElemDetCompMacroTipo(SiacDBilElemDetCompMacroTipo siacDBilElemDetCompMacroTipo) {
		this.siacDBilElemDetCompMacroTipo = siacDBilElemDetCompMacroTipo;
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
		return elemDetCompSottoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompSottoTipoId = uid;
	}


}