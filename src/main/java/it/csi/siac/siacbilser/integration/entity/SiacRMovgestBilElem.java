/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_movgest_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_bil_elem")
@NamedQuery(name="SiacRMovgestBilElem.findAll", query="SELECT s FROM SiacRMovgestBilElem s")
public class SiacRMovgestBilElem extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest atto amm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_BIL_ELEM_MOVGESTATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_BIL_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_BIL_ELEM_MOVGESTATTOAMMID_GENERATOR")
	@Column(name="movgest_atto_amm_id")
	private Integer movgestAttoAmmId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTMovgest
	/** The siac t movgest. */
	@ManyToOne
	@JoinColumn(name="movgest_id")
	private SiacTMovgest siacTMovgest;
	
	//SIAC-7349
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_id")
	private SiacDBilElemDetCompTipo siacDBilElemDetCompTipo;
	

	/**
	 * Instantiates a new siac r movgest bil elem.
	 */
	public SiacRMovgestBilElem() {
	}

	/**
	 * Gets the movgest atto amm id.
	 *
	 * @return the movgest atto amm id
	 */
	public Integer getMovgestAttoAmmId() {
		return this.movgestAttoAmmId;
	}

	/**
	 * Sets the movgest atto amm id.
	 *
	 * @param movgestAttoAmmId the new movgest atto amm id
	 */
	public void setMovgestAttoAmmId(Integer movgestAttoAmmId) {
		this.movgestAttoAmmId = movgestAttoAmmId;
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
	 * Gets the siac t movgest.
	 *
	 * @return the siac t movgest
	 */
	public SiacTMovgest getSiacTMovgest() {
		return this.siacTMovgest;
	}

	/**
	 * Sets the siac t movgest.
	 *
	 * @param siacTMovgest the new siac t movgest
	 */
	public void setSiacTMovgest(SiacTMovgest siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestAttoAmmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestAttoAmmId = uid;
	}

	/**
	 * @return the siacDBilElemDetCompTipo
	 */
	public SiacDBilElemDetCompTipo getSiacDBilElemDetCompTipo() {
		return siacDBilElemDetCompTipo;
	}

	/**
	 * @param siacDBilElemDetCompTipo the siacDBilElemDetCompTipo to set
	 */
	public void setSiacDBilElemDetCompTipo(SiacDBilElemDetCompTipo siacDBilElemDetCompTipo) {
		this.siacDBilElemDetCompTipo = siacDBilElemDetCompTipo;
	}

}