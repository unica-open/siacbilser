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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_bil_elem_class database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_class")
public class SiacRBilElemClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_CLASS_ELEMCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_CLASS_ELEM_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_CLASS_ELEMCLASSIFID_GENERATOR")
	@Column(name="elem_classif_id")
	private Integer elemClassifId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacRBilElemClassVar
	/** The siac r bil elem class vars. */
	@OneToMany(mappedBy="siacRBilElemClass")
	private List<SiacRBilElemClassVar> siacRBilElemClassVars;

	/**
	 * Instantiates a new siac r bil elem class.
	 */
	public SiacRBilElemClass() {
	}

	/**
	 * Gets the elem classif id.
	 *
	 * @return the elem classif id
	 */
	public Integer getElemClassifId() {
		return this.elemClassifId;
	}

	/**
	 * Sets the elem classif id.
	 *
	 * @param elemClassifId the new elem classif id
	 */
	public void setElemClassifId(Integer elemClassifId) {
		this.elemClassifId = elemClassifId;
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
	 * Gets the siac r bil elem class vars.
	 *
	 * @return the siac r bil elem class vars
	 */
	public List<SiacRBilElemClassVar> getSiacRBilElemClassVars() {
		return this.siacRBilElemClassVars;
	}

	/**
	 * Sets the siac r bil elem class vars.
	 *
	 * @param siacRBilElemClassVars the new siac r bil elem class vars
	 */
	public void setSiacRBilElemClassVars(List<SiacRBilElemClassVar> siacRBilElemClassVars) {
		this.siacRBilElemClassVars = siacRBilElemClassVars;
	}

	/**
	 * Adds the siac r bil elem class var.
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the siac r bil elem class var
	 */
	public SiacRBilElemClassVar addSiacRBilElemClassVar(SiacRBilElemClassVar siacRBilElemClassVar) {
		getSiacRBilElemClassVars().add(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacRBilElemClass(this);

		return siacRBilElemClassVar;
	}

	/**
	 * Removes the siac r bil elem class var.
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the siac r bil elem class var
	 */
	public SiacRBilElemClassVar removeSiacRBilElemClassVar(SiacRBilElemClassVar siacRBilElemClassVar) {
		getSiacRBilElemClassVars().remove(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacRBilElemClass(null);

		return siacRBilElemClassVar;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemClassifId = uid;
	}

}