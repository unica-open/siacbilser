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
 * The persistent class for the siac_r_bil_elem_class_var database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_class_var")
public class SiacRBilElemClassVar extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem class var id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_CLASS_VAR_ELEMCLASSVARID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_CLASS_VAR_ELEM_CLASS_VAR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_CLASS_VAR_ELEMCLASSVARID_GENERATOR")
	@Column(name="elem_class_var_id")
	private Integer elemClassVarId;

	//bi-directional many-to-one association to SiacRBilElemClass
	/** The siac r bil elem class. */
	@ManyToOne
	@JoinColumn(name="elem_classif_id")
	private SiacRBilElemClass siacRBilElemClass;

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

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;
	
	
	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id_prec")
	private SiacTClass siacTClassPrec;
	

	/**
	 * Instantiates a new siac r bil elem class var.
	 */
	public SiacRBilElemClassVar() {
	}

	/**
	 * Gets the elem class var id.
	 *
	 * @return the elem class var id
	 */
	public Integer getElemClassVarId() {
		return this.elemClassVarId;
	}

	/**
	 * Sets the elem class var id.
	 *
	 * @param elemClassVarId the new elem class var id
	 */
	public void setElemClassVarId(Integer elemClassVarId) {
		this.elemClassVarId = elemClassVarId;
	}

	/**
	 * Gets the siac r bil elem class.
	 *
	 * @return the siac r bil elem class
	 */
	public SiacRBilElemClass getSiacRBilElemClass() {
		return this.siacRBilElemClass;
	}

	/**
	 * Sets the siac r bil elem class.
	 *
	 * @param siacRBilElemClass the new siac r bil elem class
	 */
	public void setSiacRBilElemClass(SiacRBilElemClass siacRBilElemClass) {
		this.siacRBilElemClass = siacRBilElemClass;
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
	 * @return the siacTClassPrec
	 */
	public SiacTClass getSiacTClassPrec() {
		return siacTClassPrec;
	}

	/**
	 * @param siacTClassPrec the siacTClassPrec to set
	 */
	public void setSiacTClassPrec(SiacTClass siacTClassPrec) {
		this.siacTClassPrec = siacTClassPrec;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemClassVarId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemClassVarId = uid;
	}

}