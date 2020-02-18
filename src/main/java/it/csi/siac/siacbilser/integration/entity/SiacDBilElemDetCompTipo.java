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
 * The persistent class for the siac_d_bil_elem_det_comp_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo")
@NamedQuery(name="SiacDBilElemDetCompTipo.findAll", query="SELECT s FROM SiacDBilElemDetCompTipo s")
public class SiacDBilElemDetCompTipo extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_TIPO_ELEMDETCOMPTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO_ELEM_DET_COMP_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_TIPO_ELEMDETCOMPTIPOID_GENERATOR")
	@Column(name="elem_det_comp_tipo_id")
	private Integer elemDetCompTipoId;

	@Column(name="elem_det_comp_tipo_code")
	private String elemDetCompTipoCode;

	@Column(name="elem_det_comp_tipo_desc")
	private String elemDetCompTipoDesc;

	@Column(name="elem_det_comp_tipo_gest_aut")
	private Boolean elemDetCompTipoGestAut;
	
	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_macro_tipo_id")
	private SiacDBilElemDetCompMacroTipo siacDBilElemDetCompMacroTipo;
	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_sotto_tipo_id")
	private SiacDBilElemDetCompSottoTipo siacDBilElemDetCompSottoTipo;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoAmbito
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_ambito_id")
	private SiacDBilElemDetCompTipoAmbito siacDBilElemDetCompTipoAmbito;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoFonte
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_fonte_id")
	private SiacDBilElemDetCompTipoFonte siacDBilElemDetCompTipoFonte;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoFase
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_fase_id")
	private SiacDBilElemDetCompTipoFase siacDBilElemDetCompTipoFase;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoDef
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_def_id")
	private SiacDBilElemDetCompTipoDef siacDBilElemDetCompTipoDef;
	//bi-directional many-to-one association to SiacTPeriodo
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;
	//bi-directional many-to-one association to SiacTBilElemDetComp
	@OneToMany(mappedBy="siacDBilElemDetCompTipo")
	private List<SiacTBilElemDetComp> siacTBilElemDetComps;
	// bi-directional many-to-one association to SiacDBilElemDetCompTipoStato
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_stato_id")
	private SiacDBilElemDetCompTipoStato siacDBilElemDetCompTipoStato;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipo() {
	}

	/**
	 * @return the elemDetCompTipoId
	 */
	public Integer getElemDetCompTipoId() {
		return this.elemDetCompTipoId;
	}

	/**
	 * @param elemDetCompTipoId the elemDetCompTipoId to set
	 */
	public void setElemDetCompTipoId(Integer elemDetCompTipoId) {
		this.elemDetCompTipoId = elemDetCompTipoId;
	}

	/**
	 * @return the elemDetCompTipoCode
	 */
	public String getElemDetCompTipoCode() {
		return this.elemDetCompTipoCode;
	}

	/**
	 * @param elemDetCompTipoCode the elemDetCompTipoCode to set
	 */
	public void setElemDetCompTipoCode(String elemDetCompTipoCode) {
		this.elemDetCompTipoCode = elemDetCompTipoCode;
	}

	/**
	 * @return the elemDetCompTipoDesc
	 */
	public String getElemDetCompTipoDesc() {
		return this.elemDetCompTipoDesc;
	}

	/**
	 * @param elemDetCompTipoDesc the elemDetCompTipoDesc to set
	 */
	public void setElemDetCompTipoDesc(String elemDetCompTipoDesc) {
		this.elemDetCompTipoDesc = elemDetCompTipoDesc;
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
	 * @return the siacDBilElemDetCompSottoTipo
	 */
	public SiacDBilElemDetCompSottoTipo getSiacDBilElemDetCompSottoTipo() {
		return this.siacDBilElemDetCompSottoTipo;
	}

	/**
	 * @param siacDBilElemDetCompSottoTipo the siacDBilElemDetCompSottoTipo to set
	 */
	public void setSiacDBilElemDetCompSottoTipo(SiacDBilElemDetCompSottoTipo siacDBilElemDetCompSottoTipo) {
		this.siacDBilElemDetCompSottoTipo = siacDBilElemDetCompSottoTipo;
	}

	/**
	 * @return the siacDBilElemDetCompTipoAmbito
	 */
	public SiacDBilElemDetCompTipoAmbito getSiacDBilElemDetCompTipoAmbito() {
		return this.siacDBilElemDetCompTipoAmbito;
	}

	/**
	 * @param siacDBilElemDetCompTipoAmbito the siacDBilElemDetCompTipoAmbito to set
	 */
	public void setSiacDBilElemDetCompTipoAmbito(SiacDBilElemDetCompTipoAmbito siacDBilElemDetCompTipoAmbito) {
		this.siacDBilElemDetCompTipoAmbito = siacDBilElemDetCompTipoAmbito;
	}

	/**
	 * @return the siacDBilElemDetCompTipoFonte
	 */
	public SiacDBilElemDetCompTipoFonte getSiacDBilElemDetCompTipoFonte() {
		return this.siacDBilElemDetCompTipoFonte;
	}

	/**
	 * @param siacDBilElemDetCompTipoFonte the siacDBilElemDetCompTipoFonte to set
	 */
	public void setSiacDBilElemDetCompTipoFonte(SiacDBilElemDetCompTipoFonte siacDBilElemDetCompTipoFonte) {
		this.siacDBilElemDetCompTipoFonte = siacDBilElemDetCompTipoFonte;
	}

	/**
	 * @return the siacDBilElemDetCompTipoFase
	 */
	public SiacDBilElemDetCompTipoFase getSiacDBilElemDetCompTipoFase() {
		return this.siacDBilElemDetCompTipoFase;
	}

	/**
	 * @param siacDBilElemDetCompTipoFase the siacDBilElemDetCompTipoFase to set
	 */
	public void setSiacDBilElemDetCompTipoFase(SiacDBilElemDetCompTipoFase siacDBilElemDetCompTipoFase) {
		this.siacDBilElemDetCompTipoFase = siacDBilElemDetCompTipoFase;
	}

	/**
	 * @return the siacDBilElemDetCompTipoDef
	 */
	public SiacDBilElemDetCompTipoDef getSiacDBilElemDetCompTipoDef() {
		return this.siacDBilElemDetCompTipoDef;
	}

	/**
	 * @param siacDBilElemDetCompTipoDef the siacDBilElemDetCompTipoDef to set
	 */
	public void setSiacDBilElemDetCompTipoDef(SiacDBilElemDetCompTipoDef siacDBilElemDetCompTipoDef) {
		this.siacDBilElemDetCompTipoDef = siacDBilElemDetCompTipoDef;
	}

	/**
	 * @return the siacTPeriodo
	 */
	public SiacTPeriodo getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	/**
	 * @param siacTPeriodo the siacTPeriodo to set
	 */
	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * @return the siacTBilElemDetComps
	 */
	public List<SiacTBilElemDetComp> getSiacTBilElemDetComps() {
		return this.siacTBilElemDetComps;
	}

	/**
	 * @param siacTBilElemDetComps the siacTBilElemDetComps to set
	 */
	public void setSiacTBilElemDetComps(List<SiacTBilElemDetComp> siacTBilElemDetComps) {
		this.siacTBilElemDetComps = siacTBilElemDetComps;
	}

	/**
	 * @return the siacDBilElemDetCompTipoStato
	 */
	public SiacDBilElemDetCompTipoStato getSiacDBilElemDetCompTipoStato() {
		return this.siacDBilElemDetCompTipoStato;
	}

	/**
	 * @param siacDBilElemDetCompTipoStato the siacDBilElemDetCompTipoStato to set
	 */
	public void setSiacDBilElemDetCompTipoStato(SiacDBilElemDetCompTipoStato siacDBilElemDetCompTipoStato) {
		this.siacDBilElemDetCompTipoStato = siacDBilElemDetCompTipoStato;
	}

	@Override
	public Integer getUid() {
		return elemDetCompTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompTipoId = uid;
	}

	/**
	 * @return the elemDetCompTipoGestAut
	 */
	public Boolean getElemDetCompTipoGestAut() {
		return elemDetCompTipoGestAut;
	}

	/**
	 * @param elemDetCompTipoGestAut the elemDetCompTipoGestAut to set
	 */
	public void setElemDetCompTipoGestAut(Boolean elemDetCompTipoGestAut) {
		this.elemDetCompTipoGestAut = elemDetCompTipoGestAut;
	}


}