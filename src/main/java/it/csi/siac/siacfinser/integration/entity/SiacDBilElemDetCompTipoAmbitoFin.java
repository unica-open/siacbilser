/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * The persistent class for the siac_d_bil_elem_det_comp_tipo_ambito database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo_ambito")
@NamedQuery(name="SiacDBilElemDetCompTipoAmbitoFin.findAll", query="SELECT s FROM SiacDBilElemDetCompTipoAmbitoFin s")
public class SiacDBilElemDetCompTipoAmbitoFin extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_TIPO_AMBITO_ELEMDETCOMPTIPOAMBITOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO__ELEM_DET_COMP_TIPO_AMBITO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_TIPO_AMBITO_ELEMDETCOMPTIPOAMBITOID_GENERATOR")
	@Column(name="elem_det_comp_tipo_ambito_id")
	private Integer elemDetCompTipoAmbitoId;

	@Column(name="elem_det_comp_tipo_ambito_code")
	private String elemDetCompTipoAmbitoCode;

	@Column(name="elem_det_comp_tipo_ambito_desc")
	private String elemDetCompTipoAmbitoDesc;

	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_macro_tipo_id")
	private SiacDBilElemDetCompMacroTipoFin siacDBilElemDetCompMacroTipo;

	//bi-directional many-to-one association to SiacDBilElemDetTipoAmbito
	@OneToMany(mappedBy="siacDBilElemDetCompTipoAmbito")
	private List<SiacDBilElemDetCompTipoFin> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipoAmbitoFin() {
	}


	/**
	 * @return the elemDetCompTipoAmbitoId
	 */
	public Integer getElemDetCompTipoAmbitoId() {
		return this.elemDetCompTipoAmbitoId;
	}


	/**
	 * @param elemDetCompTipoAmbitoId the elemDetCompTipoAmbitoId to set
	 */
	public void setElemDetCompTipoAmbitoId(Integer elemDetCompTipoAmbitoId) {
		this.elemDetCompTipoAmbitoId = elemDetCompTipoAmbitoId;
	}


	/**
	 * @return the elemDetCompTipoAmbitoCode
	 */
	public String getElemDetCompTipoAmbitoCode() {
		return this.elemDetCompTipoAmbitoCode;
	}


	/**
	 * @param elemDetCompTipoAmbitoCode the elemDetCompTipoAmbitoCode to set
	 */
	public void setElemDetCompTipoAmbitoCode(String elemDetCompTipoAmbitoCode) {
		this.elemDetCompTipoAmbitoCode = elemDetCompTipoAmbitoCode;
	}


	/**
	 * @return the elemDetCompTipoAmbitoDesc
	 */
	public String getElemDetCompTipoAmbitoDesc() {
		return this.elemDetCompTipoAmbitoDesc;
	}


	/**
	 * @param elemDetCompTipoAmbitoDesc the elemDetCompTipoAmbitoDesc to set
	 */
	public void setElemDetCompTipoAmbitoDesc(String elemDetCompTipoAmbitoDesc) {
		this.elemDetCompTipoAmbitoDesc = elemDetCompTipoAmbitoDesc;
	}


	/**
	 * @return the siacDBilElemDetCompMacroTipo
	 */
	public SiacDBilElemDetCompMacroTipoFin getSiacDBilElemDetCompMacroTipo() {
		return this.siacDBilElemDetCompMacroTipo;
	}


	/**
	 * @param siacDBilElemDetCompMacroTipo the siacDBilElemDetCompMacroTipo to set
	 */
	public void setSiacDBilElemDetCompMacroTipo(SiacDBilElemDetCompMacroTipoFin siacDBilElemDetCompMacroTipo) {
		this.siacDBilElemDetCompMacroTipo = siacDBilElemDetCompMacroTipo;
	}


	/**
	 * @return the siacDBilElemDetCompTipos
	 */
	public List<SiacDBilElemDetCompTipoFin> getSiacDBilElemDetCompTipos() {
		return this.siacDBilElemDetCompTipos;
	}


	/**
	 * @param siacDBilElemDetCompTipos the siacDBilElemDetCompTipos to set
	 */
	public void setSiacDBilElemDetCompTipos(List<SiacDBilElemDetCompTipoFin> siacDBilElemDetCompTipos) {
		this.siacDBilElemDetCompTipos = siacDBilElemDetCompTipos;
	}


	@Override
	public Integer getUid() {
		return elemDetCompTipoAmbitoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompTipoAmbitoId = uid;
	}


}