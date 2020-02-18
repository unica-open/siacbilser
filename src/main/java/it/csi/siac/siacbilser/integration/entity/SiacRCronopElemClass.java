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
 * The persistent class for the siac_r_cronop_elem_class database table.
 * 
 */
@Entity
@Table(name="siac_r_cronop_elem_class")
@NamedQuery(name="SiacRCronopElemClass.findAll", query="SELECT s FROM SiacRCronopElemClass s")
public class SiacRCronopElemClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CRONOP_ELEM_CLASS_CRONOPCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CRONOP_ELEM_CLASS_CRONOP_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CRONOP_ELEM_CLASS_CRONOPCLASSIFID_GENERATOR")
	@Column(name="cronop_classif_id")
	private Integer cronopClassifId;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elem. */
	@ManyToOne
	@JoinColumn(name="cronop_elem_id")
	private SiacTCronopElem siacTCronopElem;

	/**
	 * Instantiates a new siac r cronop elem class.
	 */
	public SiacRCronopElemClass() {
	}

	/**
	 * Gets the cronop classif id.
	 *
	 * @return the cronop classif id
	 */
	public Integer getCronopClassifId() {
		return this.cronopClassifId;
	}

	/**
	 * Sets the cronop classif id.
	 *
	 * @param cronopClassifId the new cronop classif id
	 */
	public void setCronopClassifId(Integer cronopClassifId) {
		this.cronopClassifId = cronopClassifId;
	}

	/**
	 * Gets the siac t class.
	 *
	 * @return the siac t class
	 */
	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	/**
	 * Sets the siac t class.
	 *
	 * @param siacTClass the new siac t class
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	/**
	 * Gets the siac t cronop elem.
	 *
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElem getSiacTCronopElem() {
		return this.siacTCronopElem;
	}

	/**
	 * Sets the siac t cronop elem.
	 *
	 * @param siacTCronopElem the new siac t cronop elem
	 */
	public void setSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		this.siacTCronopElem = siacTCronopElem;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopClassifId = uid;
	}

}