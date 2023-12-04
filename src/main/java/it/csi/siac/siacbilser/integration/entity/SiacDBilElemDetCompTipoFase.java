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
 * The persistent class for the siac_d_bil_elem_det_comp_tipo_fase database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo_fase")
@NamedQuery(name="SiacDBilElemDetCompTipoFase.findAll", query="SELECT s FROM SiacDBilElemDetCompTipoFase s")
public class SiacDBilElemDetCompTipoFase extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_TIPO_FASE_ELEMDETCOMPTIPOFASEID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO_FA_ELEM_DET_COMP_TIPO_FASE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_TIPO_FASE_ELEMDETCOMPTIPOFASEID_GENERATOR")
	@Column(name="elem_det_comp_tipo_fase_id")
	private Integer elemDetCompTipoFaseId;

	@Column(name="elem_det_comp_tipo_fase_code")
	private String elemDetCompTipoFaseCode;

	@Column(name="elem_det_comp_tipo_fase_desc")
	private String elemDetCompTipoFaseDesc;

	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_macro_tipo_id")
	private SiacDBilElemDetCompMacroTipo siacDBilElemDetCompMacroTipo;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@OneToMany(mappedBy="siacDBilElemDetCompTipoFase")
	private List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipoFase() {
	}


	/**
	 * @return the elemDetCompTipoFaseId
	 */
	public Integer getElemDetCompTipoFaseId() {
		return this.elemDetCompTipoFaseId;
	}


	/**
	 * @param elemDetCompTipoFaseId the elemDetCompTipoFaseId to set
	 */
	public void setElemDetCompTipoFaseId(Integer elemDetCompTipoFaseId) {
		this.elemDetCompTipoFaseId = elemDetCompTipoFaseId;
	}


	/**
	 * @return the elemDetCompTipoFaseCode
	 */
	public String getElemDetCompTipoFaseCode() {
		return this.elemDetCompTipoFaseCode;
	}


	/**
	 * @param elemDetCompTipoFaseCode the elemDetCompTipoFaseCode to set
	 */
	public void setElemDetCompTipoFaseCode(String elemDetCompTipoFaseCode) {
		this.elemDetCompTipoFaseCode = elemDetCompTipoFaseCode;
	}


	/**
	 * @return the elemDetCompTipoFaseDesc
	 */
	public String getElemDetCompTipoFaseDesc() {
		return this.elemDetCompTipoFaseDesc;
	}


	/**
	 * @param elemDetCompTipoFaseDesc the elemDetCompTipoFaseDesc to set
	 */
	public void setElemDetCompTipoFaseDesc(String elemDetCompTipoFaseDesc) {
		this.elemDetCompTipoFaseDesc = elemDetCompTipoFaseDesc;
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
		return elemDetCompTipoFaseId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompTipoFaseId = uid;
	}


}