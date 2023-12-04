/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_cronop_elem_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_cronop_elem_bil_elem")
public class SiacRCronopElemBilElemFin extends SiacTEnteBase  {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop elem bil elem id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CRONOP_ELEM_BIL_ELEM_CRONOP_ELEM_BIL_ELEM_ID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CRONOP_ELEM_BIL_ELEM_CRONOP_ELEM_BIL_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CRONOP_ELEM_BIL_ELEM_CRONOP_ELEM_BIL_ELEM_ID_GENERATOR")
	@Column(name="cronop_elem_bil_elem_id")
	private Integer cronopElemBilElemId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elem. */
	@ManyToOne
	@JoinColumn(name="cronop_elem_id")
	private SiacTCronopElemFin siacTCronopElem;

	/**
	 * Instantiates a new siac r cronop elem bil elem.
	 */
	public SiacRCronopElemBilElemFin() {
	}

	/**
	 * Gets the cronop elem bil elem id.
	 *
	 * @return the cronop elem bil elem id
	 */
	public Integer getCronopElemBilElemId() {
		return this.cronopElemBilElemId;
	}

	/**
	 * Sets the cronop elem bil elem id.
	 *
	 * @param movgestTsProgrammaId the new cronop elem bil elem id
	 */
	public void setCronopElemBilElemId(Integer movgestTsProgrammaId) {
		this.cronopElemBilElemId = movgestTsProgrammaId;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/**
	 * Gets the siac t cronop elem.
	 *
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElemFin getSiacTCronopElem() {
		return this.siacTCronopElem;
	}

	/**
	 * Sets the siac t cronop elem.
	 *
	 * @param siacTCronopElem the new siac t cronop elem
	 */
	public void setSiacTCronopElem(SiacTCronopElemFin siacTCronopElem) {
		this.siacTCronopElem = siacTCronopElem;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopElemBilElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopElemBilElemId = uid;
		
	}

}