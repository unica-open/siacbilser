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
 * The persistent class for the siac_r_programma_class database table.
 * 
 */
@Entity
@Table(name="siac_r_programma_class")
@NamedQuery(name="SiacRProgrammaClass.findAll", query="SELECT s FROM SiacRProgrammaClass s")
public class SiacRProgrammaClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The programma classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PROGRAMMA_CLASS_PROGRAMMACLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PROGRAMMA_CLASS_PROGRAMMA_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PROGRAMMA_CLASS_PROGRAMMACLASSIFID_GENERATOR")
	@Column(name="programma_classif_id")
	private Integer programmaClassifId;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTProgramma
	/** The siac t programma. */
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgramma siacTProgramma;

	/**
	 * Instantiates a new siac r programma class.
	 */
	public SiacRProgrammaClass() {
	}

	/**
	 * Gets the programma classif id.
	 *
	 * @return the programma classif id
	 */
	public Integer getProgrammaClassifId() {
		return this.programmaClassifId;
	}

	/**
	 * Sets the programma classif id.
	 *
	 * @param programmaClassifId the new programma classif id
	 */
	public void setProgrammaClassifId(Integer programmaClassifId) {
		this.programmaClassifId = programmaClassifId;
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
	 * Gets the siac t programma.
	 *
	 * @return the siac t programma
	 */
	public SiacTProgramma getSiacTProgramma() {
		return this.siacTProgramma;
	}

	/**
	 * Sets the siac t programma.
	 *
	 * @param siacTProgramma the new siac t programma
	 */
	public void setSiacTProgramma(SiacTProgramma siacTProgramma) {
		this.siacTProgramma = siacTProgramma;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return programmaClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.programmaClassifId = uid;
	}

}