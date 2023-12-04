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
 * The persistent class for the siac_d_bil_elem_det_comp_tipo_fonte database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo_fonte")
@NamedQuery(name="SiacDBilElemDetCompTipoFonte.findAll", query="SELECT s FROM SiacDBilElemDetCompTipoFonte s")
public class SiacDBilElemDetCompTipoFonte extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_TIPO_FONTE_ELEMDETCOMPTIPOFONTEID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO_F_ELEM_DET_COMP_TIPO_FONTE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_TIPO_FONTE_ELEMDETCOMPTIPOFONTEID_GENERATOR")
	@Column(name="elem_det_comp_tipo_fonte_id")
	private Integer elemDetCompTipoFonteId;

	@Column(name="elem_det_comp_tipo_fonte_code")
	private String elemDetCompTipoFonteCode;

	@Column(name="elem_det_comp_tipo_fonte_desc")
	private String elemDetCompTipoFonteDesc;

	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_macro_tipo_id")
	private SiacDBilElemDetCompMacroTipo siacDBilElemDetCompMacroTipo;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@OneToMany(mappedBy="siacDBilElemDetCompTipoFonte")
	private List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipoFonte() {
	}


	/**
	 * @return the elemDetCompTipoFonteId
	 */
	public Integer getElemDetCompTipoFonteId() {
		return this.elemDetCompTipoFonteId;
	}


	/**
	 * @param elemDetCompTipoFonteId the elemDetCompTipoFonteId to set
	 */
	public void setElemDetCompTipoFonteId(Integer elemDetCompTipoFonteId) {
		this.elemDetCompTipoFonteId = elemDetCompTipoFonteId;
	}


	/**
	 * @return the elemDetCompTipoFonteCode
	 */
	public String getElemDetCompTipoFonteCode() {
		return this.elemDetCompTipoFonteCode;
	}


	/**
	 * @param elemDetCompTipoFonteCode the elemDetCompTipoFonteCode to set
	 */
	public void setElemDetCompTipoFonteCode(String elemDetCompTipoFonteCode) {
		this.elemDetCompTipoFonteCode = elemDetCompTipoFonteCode;
	}


	/**
	 * @return the elemDetCompTipoFonteDesc
	 */
	public String getElemDetCompTipoFonteDesc() {
		return this.elemDetCompTipoFonteDesc;
	}


	/**
	 * @param elemDetCompTipoFonteDesc the elemDetCompTipoFonteDesc to set
	 */
	public void setElemDetCompTipoFonteDesc(String elemDetCompTipoFonteDesc) {
		this.elemDetCompTipoFonteDesc = elemDetCompTipoFonteDesc;
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
		return elemDetCompTipoFonteId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompTipoFonteId = uid;
	}


}