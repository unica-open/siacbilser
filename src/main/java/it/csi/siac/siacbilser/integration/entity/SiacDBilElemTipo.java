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
 * The persistent class for the siac_d_bil_elem_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_tipo")
public class SiacDBilElemTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_TIPO_ELEMTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_TIPO_ELEM_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_TIPO_ELEMTIPOID_GENERATOR")
	@Column(name="elem_tipo_id")
	private Integer elemTipoId;

	/** The elem tipo code. */
	@Column(name="elem_tipo_code")
	private String elemTipoCode;

	/** The elem tipo desc. */
	@Column(name="elem_tipo_desc")
	private String elemTipoDesc;

	//bi-directional many-to-one association to SiacRAttrBilElemTipo
	/** The siac r attr bil elem tipos. */
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRAttrBilElemTipo> siacRAttrBilElemTipos;

	//bi-directional many-to-one association to SiacRBilElemTipoAttrIdElemCode
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRBilElemTipoAttrIdElemCode> siacRBilElemTipoAttrIdElemCodes;

	//bi-directional many-to-one association to SiacRBilElemTipoClassTip
	/** The siac r bil elem tipo class tips. */
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRBilElemTipoClassTip> siacRBilElemTipoClassTips;

	//bi-directional many-to-one association to SiacRBilElemTipoClassTipElemCode
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRBilElemTipoClassTipElemCode> siacRBilElemTipoClassTipElemCodes;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elems. */
	@OneToMany(mappedBy="siacDBilElemTipo"/*, cascade = { CascadeType.PERSIST }*/)
	private List<SiacTBilElem> siacTBilElems;

	//bi-directional many-to-one association to SiacTCronopElem
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacTCronopElem> siacTCronopElems;
	
	// bi-directional many-to-one association to SiacRBilElemTipoCategoria
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRBilElemTipoCategoria> siacRBilElemTipoCategorias;

	/**
	 * Instantiates a new siac d bil elem tipo.
	 */
	public SiacDBilElemTipo() {
	}

	/**
	 * Gets the elem tipo id.
	 *
	 * @return the elem tipo id
	 */
	public Integer getElemTipoId() {
		return this.elemTipoId;
	}

	/**
	 * Sets the elem tipo id.
	 *
	 * @param elemTipoId the new elem tipo id
	 */
	public void setElemTipoId(Integer elemTipoId) {
		this.elemTipoId = elemTipoId;
	}

	/**
	 * Gets the elem tipo code.
	 *
	 * @return the elem tipo code
	 */
	public String getElemTipoCode() {
		return this.elemTipoCode;
	}

	/**
	 * Sets the elem tipo code.
	 *
	 * @param elemTipoCode the new elem tipo code
	 */
	public void setElemTipoCode(String elemTipoCode) {
		this.elemTipoCode = elemTipoCode;
	}

	/**
	 * Gets the elem tipo desc.
	 *
	 * @return the elem tipo desc
	 */
	public String getElemTipoDesc() {
		return this.elemTipoDesc;
	}

	/**
	 * Sets the elem tipo desc.
	 *
	 * @param elemTipoDesc the new elem tipo desc
	 */
	public void setElemTipoDesc(String elemTipoDesc) {
		this.elemTipoDesc = elemTipoDesc;
	}

	/**
	 * Gets the siac r attr bil elem tipos.
	 *
	 * @return the siac r attr bil elem tipos
	 */
	public List<SiacRAttrBilElemTipo> getSiacRAttrBilElemTipos() {
		return this.siacRAttrBilElemTipos;
	}

	/**
	 * Sets the siac r attr bil elem tipos.
	 *
	 * @param siacRAttrBilElemTipos the new siac r attr bil elem tipos
	 */
	public void setSiacRAttrBilElemTipos(List<SiacRAttrBilElemTipo> siacRAttrBilElemTipos) {
		this.siacRAttrBilElemTipos = siacRAttrBilElemTipos;
	}

	/**
	 * Adds the siac r attr bil elem tipo.
	 *
	 * @param siacRAttrBilElemTipo the siac r attr bil elem tipo
	 * @return the siac r attr bil elem tipo
	 */
	public SiacRAttrBilElemTipo addSiacRAttrBilElemTipo(SiacRAttrBilElemTipo siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().add(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacDBilElemTipo(this);

		return siacRAttrBilElemTipo;
	}

	/**
	 * Removes the siac r attr bil elem tipo.
	 *
	 * @param siacRAttrBilElemTipo the siac r attr bil elem tipo
	 * @return the siac r attr bil elem tipo
	 */
	public SiacRAttrBilElemTipo removeSiacRAttrBilElemTipo(SiacRAttrBilElemTipo siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().remove(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacDBilElemTipo(null);

		return siacRAttrBilElemTipo;
	}

	public List<SiacRBilElemTipoAttrIdElemCode> getSiacRBilElemTipoAttrIdElemCodes() {
		return this.siacRBilElemTipoAttrIdElemCodes;
	}

	public void setSiacRBilElemTipoAttrIdElemCodes(List<SiacRBilElemTipoAttrIdElemCode> siacRBilElemTipoAttrIdElemCodes) {
		this.siacRBilElemTipoAttrIdElemCodes = siacRBilElemTipoAttrIdElemCodes;
	}

	public SiacRBilElemTipoAttrIdElemCode addSiacRBilElemTipoAttrIdElemCode(SiacRBilElemTipoAttrIdElemCode siacRBilElemTipoAttrIdElemCode) {
		getSiacRBilElemTipoAttrIdElemCodes().add(siacRBilElemTipoAttrIdElemCode);
		siacRBilElemTipoAttrIdElemCode.setSiacDBilElemTipo(this);

		return siacRBilElemTipoAttrIdElemCode;
	}

	public SiacRBilElemTipoAttrIdElemCode removeSiacRBilElemTipoAttrIdElemCode(SiacRBilElemTipoAttrIdElemCode siacRBilElemTipoAttrIdElemCode) {
		getSiacRBilElemTipoAttrIdElemCodes().remove(siacRBilElemTipoAttrIdElemCode);
		siacRBilElemTipoAttrIdElemCode.setSiacDBilElemTipo(null);

		return siacRBilElemTipoAttrIdElemCode;
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
		siacRBilElemTipoClassTip.setSiacDBilElemTipo(this);

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
		siacRBilElemTipoClassTip.setSiacDBilElemTipo(null);

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
		siacRBilElemTipoClassTipElemCode.setSiacDBilElemTipo(this);

		return siacRBilElemTipoClassTipElemCode;
	}

	public SiacRBilElemTipoClassTipElemCode removeSiacRBilElemTipoClassTipElemCode(SiacRBilElemTipoClassTipElemCode siacRBilElemTipoClassTipElemCode) {
		getSiacRBilElemTipoClassTipElemCodes().remove(siacRBilElemTipoClassTipElemCode);
		siacRBilElemTipoClassTipElemCode.setSiacDBilElemTipo(null);

		return siacRBilElemTipoClassTipElemCode;
	}

	/**
	 * Gets the siac t bil elems.
	 *
	 * @return the siac t bil elems
	 */
	public List<SiacTBilElem> getSiacTBilElems() {
		return this.siacTBilElems;
	}

	/**
	 * Sets the siac t bil elems.
	 *
	 * @param siacTBilElems the new siac t bil elems
	 */
	public void setSiacTBilElems(List<SiacTBilElem> siacTBilElems) {
		this.siacTBilElems = siacTBilElems;
	}

	/**
	 * Adds the siac t bil elem.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @return the siac t bil elem
	 */
	public SiacTBilElem addSiacTBilElem(SiacTBilElem siacTBilElem) {
		getSiacTBilElems().add(siacTBilElem);
		siacTBilElem.setSiacDBilElemTipo(this);

		return siacTBilElem;
	}

	/**
	 * Removes the siac t bil elem.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @return the siac t bil elem
	 */
	public SiacTBilElem removeSiacTBilElem(SiacTBilElem siacTBilElem) {
		getSiacTBilElems().remove(siacTBilElem);
		siacTBilElem.setSiacDBilElemTipo(null);

		return siacTBilElem;
	}

	public List<SiacTCronopElem> getSiacTCronopElems() {
		return this.siacTCronopElems;
	}

	public void setSiacTCronopElems(List<SiacTCronopElem> siacTCronopElems) {
		this.siacTCronopElems = siacTCronopElems;
	}

	public SiacTCronopElem addSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		getSiacTCronopElems().add(siacTCronopElem);
		siacTCronopElem.setSiacDBilElemTipo(this);

		return siacTCronopElem;
	}

	public SiacTCronopElem removeSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		getSiacTCronopElems().remove(siacTCronopElem);
		siacTCronopElem.setSiacDBilElemTipo(null);

		return siacTCronopElem;
	}

	public List<SiacRBilElemTipoCategoria> getSiacRBilElemTipoCategorias() {
		return this.siacRBilElemTipoCategorias;
	}

	public void setSiacRBilElemTipoCategorias(List<SiacRBilElemTipoCategoria> siacRBilElemTipoCategorias) {
		this.siacRBilElemTipoCategorias = siacRBilElemTipoCategorias;
	}

	public SiacRBilElemTipoCategoria addSiacRBilElemTipoCategoria(SiacRBilElemTipoCategoria siacRBilElemTipoCategoria) {
		getSiacRBilElemTipoCategorias().add(siacRBilElemTipoCategoria);
		siacRBilElemTipoCategoria.setSiacDBilElemTipo(this);

		return siacRBilElemTipoCategoria;
	}

	public SiacRBilElemTipoCategoria removeSiacRBilElemTipoCategoria(SiacRBilElemTipoCategoria siacRBilElemTipoCategoria) {
		getSiacRBilElemTipoCategorias().remove(siacRBilElemTipoCategoria);
		siacRBilElemTipoCategoria.setSiacDBilElemTipo(null);

		return siacRBilElemTipoCategoria;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemTipoId = uid;		
	}

}