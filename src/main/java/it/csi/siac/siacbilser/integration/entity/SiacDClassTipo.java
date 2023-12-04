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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_class_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_class_tipo")
public class SiacDClassTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The classif tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CLASS_TIPO_CLASSIFTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CLASS_TIPO_CLASSIF_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CLASS_TIPO_CLASSIFTIPOID_GENERATOR")
	@Column(name="classif_tipo_id")
	private Integer classifTipoId;

	/** The classif tipo code. */
	@Column(name="classif_tipo_code")
	private String classifTipoCode;

	/** The classif tipo desc. */
	@Column(name="classif_tipo_desc")
	private String classifTipoDesc;

	//bi-directional many-to-one association to SiacRAttrClassTipo
	/** The siac r attr class tipos. */
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacRAttrClassTipo> siacRAttrClassTipos;

	//bi-directional many-to-one association to SiacRBilElemTipoClassTip
	/** The siac r bil elem tipo class tips. */
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacRBilElemTipoClassTip> siacRBilElemTipoClassTips;

	//bi-directional many-to-one association to SiacRBilElemTipoClassTipElemCode
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacRBilElemTipoClassTipElemCode> siacRBilElemTipoClassTipElemCodes;

	//bi-directional many-to-one association to SiacRMovgestTipoClassTip
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacRMovgestTipoClassTip> siacRMovgestTipoClassTips;

	//bi-directional many-to-one association to SiacROrdinativoTipoClassTip
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacROrdinativoTipoClassTip> siacROrdinativoTipoClassTips;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t classes. */
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacTClass> siacTClasses;

	/**
	 * Instantiates a new siac d class tipo.
	 */
	public SiacDClassTipo() {
	}

	/**
	 * Gets the classif tipo id.
	 *
	 * @return the classif tipo id
	 */
	public Integer getClassifTipoId() {
		return this.classifTipoId;
	}

	/**
	 * Sets the classif tipo id.
	 *
	 * @param classifTipoId the new classif tipo id
	 */
	public void setClassifTipoId(Integer classifTipoId) {
		this.classifTipoId = classifTipoId;
	}

	/**
	 * Gets the classif tipo code.
	 *
	 * @return the classif tipo code
	 */
	public String getClassifTipoCode() {
		return this.classifTipoCode;
	}

	/**
	 * Sets the classif tipo code.
	 *
	 * @param classifTipoCode the new classif tipo code
	 */
	public void setClassifTipoCode(String classifTipoCode) {
		this.classifTipoCode = classifTipoCode;
	}

	/**
	 * Gets the classif tipo desc.
	 *
	 * @return the classif tipo desc
	 */
	public String getClassifTipoDesc() {
		return this.classifTipoDesc;
	}

	/**
	 * Sets the classif tipo desc.
	 *
	 * @param classifTipoDesc the new classif tipo desc
	 */
	public void setClassifTipoDesc(String classifTipoDesc) {
		this.classifTipoDesc = classifTipoDesc;
	}

	/**
	 * Gets the siac r attr class tipos.
	 *
	 * @return the siac r attr class tipos
	 */
	public List<SiacRAttrClassTipo> getSiacRAttrClassTipos() {
		return this.siacRAttrClassTipos;
	}

	/**
	 * Sets the siac r attr class tipos.
	 *
	 * @param siacRAttrClassTipos the new siac r attr class tipos
	 */
	public void setSiacRAttrClassTipos(List<SiacRAttrClassTipo> siacRAttrClassTipos) {
		this.siacRAttrClassTipos = siacRAttrClassTipos;
	}

	/**
	 * Adds the siac r attr class tipo.
	 *
	 * @param siacRAttrClassTipo the siac r attr class tipo
	 * @return the siac r attr class tipo
	 */
	public SiacRAttrClassTipo addSiacRAttrClassTipo(SiacRAttrClassTipo siacRAttrClassTipo) {
		getSiacRAttrClassTipos().add(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacDClassTipo(this);

		return siacRAttrClassTipo;
	}

	/**
	 * Removes the siac r attr class tipo.
	 *
	 * @param siacRAttrClassTipo the siac r attr class tipo
	 * @return the siac r attr class tipo
	 */
	public SiacRAttrClassTipo removeSiacRAttrClassTipo(SiacRAttrClassTipo siacRAttrClassTipo) {
		getSiacRAttrClassTipos().remove(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacDClassTipo(null);

		return siacRAttrClassTipo;
	}

	/**
	 * Gets the siac r bil elem tipo class tips.
	 *
	 * @return the siac r bil elem tipo class tips
	 */
	public List<SiacRBilElemTipoClassTip> getSiacRBilElemTipoClassTips() {
		return this.siacRBilElemTipoClassTips;
	}

	/**
	 * Sets the siac r bil elem tipo class tips.
	 *
	 * @param siacRBilElemTipoClassTips the new siac r bil elem tipo class tips
	 */
	public void setSiacRBilElemTipoClassTips(List<SiacRBilElemTipoClassTip> siacRBilElemTipoClassTips) {
		this.siacRBilElemTipoClassTips = siacRBilElemTipoClassTips;
	}

	/**
	 * Adds the siac r bil elem tipo class tip.
	 *
	 * @param siacRBilElemTipoClassTip the siac r bil elem tipo class tip
	 * @return the siac r bil elem tipo class tip
	 */
	public SiacRBilElemTipoClassTip addSiacRBilElemTipoClassTip(SiacRBilElemTipoClassTip siacRBilElemTipoClassTip) {
		getSiacRBilElemTipoClassTips().add(siacRBilElemTipoClassTip);
		siacRBilElemTipoClassTip.setSiacDClassTipo(this);

		return siacRBilElemTipoClassTip;
	}

	/**
	 * Removes the siac r bil elem tipo class tip.
	 *
	 * @param siacRBilElemTipoClassTip the siac r bil elem tipo class tip
	 * @return the siac r bil elem tipo class tip
	 */
	public SiacRBilElemTipoClassTip removeSiacRBilElemTipoClassTip(SiacRBilElemTipoClassTip siacRBilElemTipoClassTip) {
		getSiacRBilElemTipoClassTips().remove(siacRBilElemTipoClassTip);
		siacRBilElemTipoClassTip.setSiacDClassTipo(null);

		return siacRBilElemTipoClassTip;
	}

	public List<SiacRBilElemTipoClassTipElemCode> getSiacRBilElemTipoClassTipElemCodes() {
		return this.siacRBilElemTipoClassTipElemCodes;
	}

	public void setSiacRBilElemTipoClassTipElemCodes(List<SiacRBilElemTipoClassTipElemCode> siacRBilElemTipoClassTipElemCodes) {
		this.siacRBilElemTipoClassTipElemCodes = siacRBilElemTipoClassTipElemCodes;
	}

	public SiacRBilElemTipoClassTipElemCode addSiacRBilElemTipoClassTipElemCode(SiacRBilElemTipoClassTipElemCode siacRBilElemTipoClassTipElemCode) {
		getSiacRBilElemTipoClassTipElemCodes().add(siacRBilElemTipoClassTipElemCode);
		siacRBilElemTipoClassTipElemCode.setSiacDClassTipo(this);

		return siacRBilElemTipoClassTipElemCode;
	}

	public SiacRBilElemTipoClassTipElemCode removeSiacRBilElemTipoClassTipElemCode(SiacRBilElemTipoClassTipElemCode siacRBilElemTipoClassTipElemCode) {
		getSiacRBilElemTipoClassTipElemCodes().remove(siacRBilElemTipoClassTipElemCode);
		siacRBilElemTipoClassTipElemCode.setSiacDClassTipo(null);

		return siacRBilElemTipoClassTipElemCode;
	}

	public List<SiacRMovgestTipoClassTip> getSiacRMovgestTipoClassTips() {
		return this.siacRMovgestTipoClassTips;
	}

	public void setSiacRMovgestTipoClassTips(List<SiacRMovgestTipoClassTip> siacRMovgestTipoClassTips) {
		this.siacRMovgestTipoClassTips = siacRMovgestTipoClassTips;
	}

	public SiacRMovgestTipoClassTip addSiacRMovgestTipoClassTip(SiacRMovgestTipoClassTip siacRMovgestTipoClassTip) {
		getSiacRMovgestTipoClassTips().add(siacRMovgestTipoClassTip);
		siacRMovgestTipoClassTip.setSiacDClassTipo(this);

		return siacRMovgestTipoClassTip;
	}

	public SiacRMovgestTipoClassTip removeSiacRMovgestTipoClassTip(SiacRMovgestTipoClassTip siacRMovgestTipoClassTip) {
		getSiacRMovgestTipoClassTips().remove(siacRMovgestTipoClassTip);
		siacRMovgestTipoClassTip.setSiacDClassTipo(null);

		return siacRMovgestTipoClassTip;
	}

	public List<SiacROrdinativoTipoClassTip> getSiacROrdinativoTipoClassTips() {
		return this.siacROrdinativoTipoClassTips;
	}

	public void setSiacROrdinativoTipoClassTips(List<SiacROrdinativoTipoClassTip> siacROrdinativoTipoClassTips) {
		this.siacROrdinativoTipoClassTips = siacROrdinativoTipoClassTips;
	}

	public SiacROrdinativoTipoClassTip addSiacROrdinativoTipoClassTip(SiacROrdinativoTipoClassTip siacROrdinativoTipoClassTip) {
		getSiacROrdinativoTipoClassTips().add(siacROrdinativoTipoClassTip);
		siacROrdinativoTipoClassTip.setSiacDClassTipo(this);

		return siacROrdinativoTipoClassTip;
	}

	public SiacROrdinativoTipoClassTip removeSiacROrdinativoTipoClassTip(SiacROrdinativoTipoClassTip siacROrdinativoTipoClassTip) {
		getSiacROrdinativoTipoClassTips().remove(siacROrdinativoTipoClassTip);
		siacROrdinativoTipoClassTip.setSiacDClassTipo(null);

		return siacROrdinativoTipoClassTip;
	}

	/**
	 * Gets the siac t classes.
	 *
	 * @return the siac t classes
	 */
	public List<SiacTClass> getSiacTClasses() {
		return this.siacTClasses;
	}

	/**
	 * Sets the siac t classes.
	 *
	 * @param siacTClasses the new siac t classes
	 */
	public void setSiacTClasses(List<SiacTClass> siacTClasses) {
		this.siacTClasses = siacTClasses;
	}

	/**
	 * Adds the siac t class.
	 *
	 * @param siacTClass the siac t class
	 * @return the siac t class
	 */
	public SiacTClass addSiacTClass(SiacTClass siacTClass) {
		getSiacTClasses().add(siacTClass);
		siacTClass.setSiacDClassTipo(this);

		return siacTClass;
	}

	/**
	 * Removes the siac t class.
	 *
	 * @param siacTClass the siac t class
	 * @return the siac t class
	 */
	public SiacTClass removeSiacTClass(SiacTClass siacTClass) {
		getSiacTClasses().remove(siacTClass);
		siacTClass.setSiacDClassTipo(null);

		return siacTClass;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return classifTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.classifTipoId = uid;
	}

}