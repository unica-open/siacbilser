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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * The persistent class for the siac_d_bil_elem_det_comp_macro_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_macro_tipo")
@NamedQuery(name="SiacDBilElemDetCompMacroTipoFin.findAll", query="SELECT s FROM SiacDBilElemDetCompMacroTipoFin s")
public class SiacDBilElemDetCompMacroTipoFin extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_MACRO_TIPO_ELEMDETCOMPMACROTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO__ELEM_DET_COMP_TIPO_AMBITO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_MACRO_TIPO_ELEMDETCOMPMACROTIPOID_GENERATOR")
	@Column(name="elem_det_comp_macro_tipo_id")
	private Integer elemDetCompMacroTipoId;
	
	@Column(name="elem_det_comp_macro_tipo_code")
	private String elemDetCompMacroTipoCode;

	@Column(name="elem_det_comp_macro_tipo_desc")
	private String elemDetCompMacroTipoDesc;

	//bi-directional many-to-one association to SiacDBilElemDetCompSottoTipo
	@OneToMany(mappedBy="siacDBilElemDetCompMacroTipo")
	private List<SiacDBilElemDetCompSottoTipoFin> siacDBilElemDetCompSottoTipos;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipoAmbito
	@OneToMany(mappedBy="siacDBilElemDetCompMacroTipo")
	private List<SiacDBilElemDetCompTipoAmbitoFin> siacDBilElemDetCompTipoAmbitos;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipoFonte
	@OneToMany(mappedBy="siacDBilElemDetCompMacroTipo")
	private List<SiacDBilElemDetCompTipoFonteFin> siacDBilElemDetCompTipoFontes;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipoFase
	@OneToMany(mappedBy="siacDBilElemDetCompMacroTipo")
	private List<SiacDBilElemDetCompTipoFaseFin> siacDBilElemDetCompTipoFases;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@OneToMany(mappedBy="siacDBilElemDetCompMacroTipo")
	private List<SiacDBilElemDetCompTipoFin> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp macro tipo.
	 */
	public SiacDBilElemDetCompMacroTipoFin() {
	}

	/**
	 * @return the elemDetCompMacroTipoId
	 */
	public Integer getElemDetCompMacroTipoId() {
		return this.elemDetCompMacroTipoId;
	}

	/**
	 * @param elemDetCompMacroTipoId the elemDetCompMacroTipoId to set
	 */
	public void setElemDetCompMacroTipoId(Integer elemDetCompMacroTipoId) {
		this.elemDetCompMacroTipoId = elemDetCompMacroTipoId;
	}

	/**
	 * @return the elemDetCompMacroTipoCode
	 */
	public String getElemDetCompMacroTipoCode() {
		return this.elemDetCompMacroTipoCode;
	}

	/**
	 * @param elemDetCompMacroTipoCode the elemDetCompMacroTipoCode to set
	 */
	public void setElemDetCompMacroTipoCode(String elemDetCompMacroTipoCode) {
		this.elemDetCompMacroTipoCode = elemDetCompMacroTipoCode;
	}

	/**
	 * @return the elemDetCompMacroTipoDesc
	 */
	public String getElemDetCompMacroTipoDesc() {
		return this.elemDetCompMacroTipoDesc;
	}

	/**
	 * @param elemDetCompMacroTipoDesc the elemDetCompMacroTipoDesc to set
	 */
	public void setElemDetCompMacroTipoDesc(String elemDetCompMacroTipoDesc) {
		this.elemDetCompMacroTipoDesc = elemDetCompMacroTipoDesc;
	}

	/**
	 * @return the siacDBilElemDetCompSottoTipos
	 */
	public List<SiacDBilElemDetCompSottoTipoFin> getSiacDBilElemDetCompSottoTipos() {
		return this.siacDBilElemDetCompSottoTipos;
	}

	/**
	 * @param siacDBilElemDetCompSottoTipos the siacDBilElemDetCompSottoTipos to set
	 */
	public void setSiacDBilElemDetCompSottoTipos(List<SiacDBilElemDetCompSottoTipoFin> siacDBilElemDetCompSottoTipos) {
		this.siacDBilElemDetCompSottoTipos = siacDBilElemDetCompSottoTipos;
	}

	/**
	 * @return the siacDBilElemDetCompTipoAmbitos
	 */
	public List<SiacDBilElemDetCompTipoAmbitoFin> getSiacDBilElemDetCompTipoAmbitos() {
		return this.siacDBilElemDetCompTipoAmbitos;
	}

	/**
	 * @param siacDBilElemDetCompTipoAmbitos the siacDBilElemDetCompTipoAmbitos to set
	 */
	public void setSiacDBilElemDetCompTipoAmbitos(List<SiacDBilElemDetCompTipoAmbitoFin> siacDBilElemDetCompTipoAmbitos) {
		this.siacDBilElemDetCompTipoAmbitos = siacDBilElemDetCompTipoAmbitos;
	}

	/**
	 * @return the siacDBilElemDetCompTipoFontes
	 */
	public List<SiacDBilElemDetCompTipoFonteFin> getSiacDBilElemDetCompTipoFontes() {
		return this.siacDBilElemDetCompTipoFontes;
	}

	/**
	 * @param siacDBilElemDetCompTipoFontes the siacDBilElemDetCompTipoFontes to set
	 */
	public void setSiacDBilElemDetCompTipoFontes(List<SiacDBilElemDetCompTipoFonteFin> siacDBilElemDetCompTipoFontes) {
		this.siacDBilElemDetCompTipoFontes = siacDBilElemDetCompTipoFontes;
	}

	/**
	 * @return the siacDBilElemDetCompTipoFases
	 */
	public List<SiacDBilElemDetCompTipoFaseFin> getSiacDBilElemDetCompTipoFases() {
		return this.siacDBilElemDetCompTipoFases;
	}

	/**
	 * @param siacDBilElemDetCompTipoFases the siacDBilElemDetCompTipoFases to set
	 */
	public void setSiacDBilElemDetCompTipoFases(List<SiacDBilElemDetCompTipoFaseFin> siacDBilElemDetCompTipoFases) {
		this.siacDBilElemDetCompTipoFases = siacDBilElemDetCompTipoFases;
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
		return elemDetCompMacroTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompMacroTipoId = uid;
	}


}