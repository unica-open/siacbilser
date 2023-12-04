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
 * The persistent class for the siac_d_bil_elem_det_comp_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo")
@NamedQuery(name="SiacDBilElemDetCompTipoFin.findAll", query="SELECT s FROM SiacDBilElemDetCompTipoFin s")
public class SiacDBilElemDetCompTipoFin extends SiacTEnteBase {
	
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

	//SIAC-7349
	@Column(name="elem_det_comp_tipo_gest_aut")
	private Boolean elemDetCompTipoGestAut;
	
	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_macro_tipo_id")
	private SiacDBilElemDetCompMacroTipoFin siacDBilElemDetCompMacroTipo;
	//bi-directional many-to-one association to SiacDBilElemDetCompMacroTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_sotto_tipo_id")
	private SiacDBilElemDetCompSottoTipoFin siacDBilElemDetCompSottoTipo;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoAmbito
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_ambito_id")
	private SiacDBilElemDetCompTipoAmbitoFin siacDBilElemDetCompTipoAmbito;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoFonte
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_fonte_id")
	private SiacDBilElemDetCompTipoFonteFin siacDBilElemDetCompTipoFonte;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoFase
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_fase_id")
	private SiacDBilElemDetCompTipoFaseFin siacDBilElemDetCompTipoFase;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoDef
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_def_id")
	private SiacDBilElemDetCompTipoDefFin siacDBilElemDetCompTipoDef;
	//bi-directional many-to-one association to SiacTPeriodo
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodoFin siacTPeriodo;
	//bi-directional many-to-one association to SiacTBilElemDetComp
	@OneToMany(mappedBy="siacDBilElemDetCompTipo")
	private List<SiacTBilElemDetCompFin> siacTBilElemDetComps;
	// bi-directional many-to-one association to SiacDBilElemDetCompTipoStato
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_stato_id")
	private SiacDBilElemDetCompTipoStatoFin siacDBilElemDetCompTipoStato;

	//SIAC-7349
	//bi-directional many-to-one association to SiacDBilElemDetCompTipoImp
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_imp_id")
	private SiacDBilElemDetCompTipoImpFin siacDBilElemDetCompTipoImp;
	
	//SIAC-7349
	//bi-directional many-to-one association to SiacRMovgestBilElem
	@OneToMany(mappedBy="siacDBilElemDetCompTipo")
	private List<SiacRMovgestBilElemFin> siacRMovgestBilElems;
	
	
	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipoFin() {
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
	 * @return the siacDBilElemDetCompSottoTipo
	 */
	public SiacDBilElemDetCompSottoTipoFin getSiacDBilElemDetCompSottoTipo() {
		return this.siacDBilElemDetCompSottoTipo;
	}

	/**
	 * @param siacDBilElemDetCompSottoTipo the siacDBilElemDetCompSottoTipo to set
	 */
	public void setSiacDBilElemDetCompSottoTipo(SiacDBilElemDetCompSottoTipoFin siacDBilElemDetCompSottoTipo) {
		this.siacDBilElemDetCompSottoTipo = siacDBilElemDetCompSottoTipo;
	}

	/**
	 * @return the siacDBilElemDetCompTipoAmbito
	 */
	public SiacDBilElemDetCompTipoAmbitoFin getSiacDBilElemDetCompTipoAmbito() {
		return this.siacDBilElemDetCompTipoAmbito;
	}

	/**
	 * @param siacDBilElemDetCompTipoAmbito the siacDBilElemDetCompTipoAmbito to set
	 */
	public void setSiacDBilElemDetCompTipoAmbito(SiacDBilElemDetCompTipoAmbitoFin siacDBilElemDetCompTipoAmbito) {
		this.siacDBilElemDetCompTipoAmbito = siacDBilElemDetCompTipoAmbito;
	}

	/**
	 * @return the siacDBilElemDetCompTipoFonte
	 */
	public SiacDBilElemDetCompTipoFonteFin getSiacDBilElemDetCompTipoFonte() {
		return this.siacDBilElemDetCompTipoFonte;
	}

	/**
	 * @param siacDBilElemDetCompTipoFonte the siacDBilElemDetCompTipoFonte to set
	 */
	public void setSiacDBilElemDetCompTipoFonte(SiacDBilElemDetCompTipoFonteFin siacDBilElemDetCompTipoFonte) {
		this.siacDBilElemDetCompTipoFonte = siacDBilElemDetCompTipoFonte;
	}

	/**
	 * @return the siacDBilElemDetCompTipoFase
	 */
	public SiacDBilElemDetCompTipoFaseFin getSiacDBilElemDetCompTipoFase() {
		return this.siacDBilElemDetCompTipoFase;
	}

	/**
	 * @param siacDBilElemDetCompTipoFase the siacDBilElemDetCompTipoFase to set
	 */
	public void setSiacDBilElemDetCompTipoFase(SiacDBilElemDetCompTipoFaseFin siacDBilElemDetCompTipoFase) {
		this.siacDBilElemDetCompTipoFase = siacDBilElemDetCompTipoFase;
	}

	/**
	 * @return the siacDBilElemDetCompTipoDef
	 */
	public SiacDBilElemDetCompTipoDefFin getSiacDBilElemDetCompTipoDef() {
		return this.siacDBilElemDetCompTipoDef;
	}

	/**
	 * @param siacDBilElemDetCompTipoDef the siacDBilElemDetCompTipoDef to set
	 */
	public void setSiacDBilElemDetCompTipoDef(SiacDBilElemDetCompTipoDefFin siacDBilElemDetCompTipoDef) {
		this.siacDBilElemDetCompTipoDef = siacDBilElemDetCompTipoDef;
	}

	/**
	 * @return the siacTPeriodo
	 */
	public SiacTPeriodoFin getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	/**
	 * @param siacTPeriodo the siacTPeriodo to set
	 */
	public void setSiacTPeriodo(SiacTPeriodoFin siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * @return the siacTBilElemDetComps
	 */
	public List<SiacTBilElemDetCompFin> getSiacTBilElemDetComps() {
		return this.siacTBilElemDetComps;
	}

	/**
	 * @param siacTBilElemDetComps the siacTBilElemDetComps to set
	 */
	public void setSiacTBilElemDetComps(List<SiacTBilElemDetCompFin> siacTBilElemDetComps) {
		this.siacTBilElemDetComps = siacTBilElemDetComps;
	}

	/**
	 * @return the siacDBilElemDetCompTipoStato
	 */
	public SiacDBilElemDetCompTipoStatoFin getSiacDBilElemDetCompTipoStato() {
		return this.siacDBilElemDetCompTipoStato;
	}

	/**
	 * @param siacDBilElemDetCompTipoStato the siacDBilElemDetCompTipoStato to set
	 */
	public void setSiacDBilElemDetCompTipoStato(SiacDBilElemDetCompTipoStatoFin siacDBilElemDetCompTipoStato) {
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
	 * @return the siacDBilElemDetCompTipoImp
	 */
	public SiacDBilElemDetCompTipoImpFin getSiacDBilElemDetCompTipoImp() {
		return siacDBilElemDetCompTipoImp;
	}

	/**
	 * @param siacDBilElemDetCompTipoImp the siacDBilElemDetCompTipoImp to set
	 */
	public void setSiacDBilElemDetCompTipoImp(SiacDBilElemDetCompTipoImpFin siacDBilElemDetCompTipoImp) {
		this.siacDBilElemDetCompTipoImp = siacDBilElemDetCompTipoImp;
	}

	/**
	 * @return the siacRMovgestBilElems
	 */
	public List<SiacRMovgestBilElemFin> getSiacRMovgestBilElems() {
		return siacRMovgestBilElems;
	}

	/**
	 * @param siacRMovgestBilElems the siacRMovgestBilElems to set
	 */
	public void setSiacRMovgestBilElems(List<SiacRMovgestBilElemFin> siacRMovgestBilElems) {
		this.siacRMovgestBilElems = siacRMovgestBilElems;
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