/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_bil_elem_det_var database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_det_var")
public class SiacTBilElemDetVar extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem det var id. */
	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_DET_VAR_ELEMDETVARID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_BIL_ELEM_DET_VAR_ELEM_DET_VAR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_DET_VAR_ELEMDETVARID_GENERATOR")
	@Column(name="elem_det_var_id")
	private Integer elemDetVarId;

	/** The elem det flag. */
	@Column(name="elem_det_flag")
	private String elemDetFlag;

	/** The elem det importo. */
	@Column(name="elem_det_importo")
	private BigDecimal elemDetImporto;

	/** The siac d bil elem det tipo. */
	@ManyToOne
	@JoinColumn(name="elem_det_tipo_id")
	private SiacDBilElemDetTipo siacDBilElemDetTipo;

	/** The siac t periodo. */
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;

	//bi-directional many-to-one association to SiacRVariazioneStato
	/** The siac r variazione stato. */
	@ManyToOne
	@JoinColumn(name="variazione_stato_id")
	private SiacRVariazioneStato siacRVariazioneStato;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTBilElemDet
	/** The siac t bil elem det. */
	@ManyToOne
	@JoinColumn(name="elem_det_id")
	private SiacTBilElemDet siacTBilElemDet;
	//bi-directional many-to-one association to SiacTBilElemDetVarComp
	@OneToMany(mappedBy="siacTBilElemDetVar")
	private List<SiacTBilElemDetVarComp> siacTBilElemDetVarComps;

	/**
	 * Instantiates a new siac t bil elem det var.
	 */
	public SiacTBilElemDetVar() {
	}

	/**
	 * Gets the elem det var id.
	 *
	 * @return the elem det var id
	 */
	public Integer getElemDetVarId() {
		return this.elemDetVarId;
	}

	/**
	 * Sets the elem det var id.
	 *
	 * @param elemDetVarId the new elem det var id
	 */
	public void setElemDetVarId(Integer elemDetVarId) {
		this.elemDetVarId = elemDetVarId;
	}



	/**
	 * Gets the elem det flag.
	 *
	 * @return the elem det flag
	 */
	public String getElemDetFlag() {
		return this.elemDetFlag;
	}

	/**
	 * Sets the elem det flag.
	 *
	 * @param elemDetFlag the new elem det flag
	 */
	public void setElemDetFlag(String elemDetFlag) {
		this.elemDetFlag = elemDetFlag;
	}

	/**
	 * Gets the elem det importo.
	 *
	 * @return the elem det importo
	 */
	public BigDecimal getElemDetImporto() {
		return this.elemDetImporto;
	}

	/**
	 * Sets the elem det importo.
	 *
	 * @param elemDetImporto the new elem det importo
	 */
	public void setElemDetImporto(BigDecimal elemDetImporto) {
		this.elemDetImporto = elemDetImporto;
	}

	/**
	 * Gets the siac r variazione stato.
	 *
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato getSiacRVariazioneStato() {
		return this.siacRVariazioneStato;
	}

	/**
	 * Gets the siac d bil elem det tipo.
	 *
	 * @return the siacDBilElemDetTipo
	 */
	public SiacDBilElemDetTipo getSiacDBilElemDetTipo() {
		return siacDBilElemDetTipo;
	}

	/**
	 * Sets the siac d bil elem det tipo.
	 *
	 * @param siacDBilElemDetTipo the siacDBilElemDetTipo to set
	 */
	public void setSiacDBilElemDetTipo(SiacDBilElemDetTipo siacDBilElemDetTipo) {
		this.siacDBilElemDetTipo = siacDBilElemDetTipo;
	}

	/**
	 * Gets the siac t periodo.
	 *
	 * @return the siacTPeriodo
	 */
	public SiacTPeriodo getSiacTPeriodo() {
		return siacTPeriodo;
	}

	/**
	 * Sets the siac t periodo.
	 *
	 * @param siacTPeriodo the siacTPeriodo to set
	 */
	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * Sets the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the new siac r variazione stato
	 */
	public void setSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		this.siacRVariazioneStato = siacRVariazioneStato;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/**
	 * Gets the siac t bil elem det.
	 *
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet getSiacTBilElemDet() {
		return this.siacTBilElemDet;
	}

	/**
	 * Sets the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the new siac t bil elem det
	 */
	public void setSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		this.siacTBilElemDet = siacTBilElemDet;
	}

	/**
	 * @return the siacTBilElemDetVarComps
	 */
	public List<SiacTBilElemDetVarComp> getSiacTBilElemDetVarComps() {
		return this.siacTBilElemDetVarComps;
	}

	/**
	 * @param siacTBilElemDetVarComps the siacTBilElemDetVarComps to set
	 */
	public void setSiacTBilElemDetVarComps(List<SiacTBilElemDetVarComp> siacTBilElemDetVarComps) {
		this.siacTBilElemDetVarComps = siacTBilElemDetVarComps;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemDetVarId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemDetVarId = uid;
		
	}

	

}