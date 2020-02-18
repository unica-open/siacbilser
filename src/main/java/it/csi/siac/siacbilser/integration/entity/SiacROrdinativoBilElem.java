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
 * The persistent class for the siac_r_ordinativo_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_bil_elem")
@NamedQuery(name="SiacROrdinativoBilElem.findAll", query="SELECT s FROM SiacROrdinativoBilElem s")
public class SiacROrdinativoBilElem extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord bile elem id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_BIL_ELEM_ORDBILEELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_BIL_ELEM_ORD_BILE_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_BIL_ELEM_ORDBILEELEMID_GENERATOR")
	@Column(name="ord_bile_elem_id")
	private Integer ordBileElemId;


	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo. */
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativo siacTOrdinativo;

	/**
	 * Instantiates a new siac r ordinativo bil elem.
	 */
	public SiacROrdinativoBilElem() {
	}

	/**
	 * Gets the ord bile elem id.
	 *
	 * @return the ord bile elem id
	 */
	public Integer getOrdBileElemId() {
		return this.ordBileElemId;
	}

	/**
	 * Sets the ord bile elem id.
	 *
	 * @param ordBileElemId the new ord bile elem id
	 */
	public void setOrdBileElemId(Integer ordBileElemId) {
		this.ordBileElemId = ordBileElemId;
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
	 * Gets the siac t ordinativo.
	 *
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	/**
	 * Sets the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the new siac t ordinativo
	 */
	public void setSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordBileElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordBileElemId = uid;
	}

}