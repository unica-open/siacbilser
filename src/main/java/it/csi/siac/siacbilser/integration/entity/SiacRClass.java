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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_class database table.
 * 
 */
@Entity
@Table(name="siac_r_class")
public class SiacRClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The classif classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CLASS_CLASSIFCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CLASS_CLASSIF_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CLASS_CLASSIFCLASSIFID_GENERATOR")
	@Column(name="classif_classif_id")
	private Integer classifClassifId;
	

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class a. */
	@ManyToOne
	@JoinColumn(name="classif_a_id")
	private SiacTClass siacTClassA;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class b. */
	@ManyToOne
	@JoinColumn(name="classif_b_id")
	private SiacTClass siacTClassB;

	/**
	 * Instantiates a new siac r class.
	 */
	public SiacRClass() {
	}

	/**
	 * Gets the classif classif id.
	 *
	 * @return the classif classif id
	 */
	public Integer getClassifClassifId() {
		return this.classifClassifId;
	}

	/**
	 * Sets the classif classif id.
	 *
	 * @param classifClassifId the new classif classif id
	 */
	public void setClassifClassifId(Integer classifClassifId) {
		this.classifClassifId = classifClassifId;
	}



	/**
	 * Gets the siac t class a.
	 *
	 * @return the siac t class a
	 */
	public SiacTClass getSiacTClassA() {
		return this.siacTClassA;
	}

	/**
	 * Sets the siac t class a.
	 *
	 * @param siacTClass1 the new siac t class a
	 */
	public void setSiacTClassA(SiacTClass siacTClass1) {
		this.siacTClassA = siacTClass1;
	}

	/**
	 * Gets the siac t class b.
	 *
	 * @return the siac t class b
	 */
	public SiacTClass getSiacTClassB() {
		return this.siacTClassB;
	}

	/**
	 * Sets the siac t class b.
	 *
	 * @param siacTClass2 the new siac t class b
	 */
	public void setSiacTClassB(SiacTClass siacTClass2) {
		this.siacTClassB = siacTClass2;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return classifClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.classifClassifId = uid;
		
	}



}