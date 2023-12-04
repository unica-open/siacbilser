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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_iva_registrazione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_registrazione_tipo")
@NamedQuery(name="SiacDIvaRegistrazioneTipo.findAll", query="SELECT s FROM SiacDIvaRegistrazioneTipo s")
public class SiacDIvaRegistrazioneTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The reg tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_IVA_REGISTRAZIONE_TIPO_REGTIPOID_GENERATOR", sequenceName="SIAC_D_IVA_REGISTRAZIONE_TIPO_REG_TIPO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_REGISTRAZIONE_TIPO_REGTIPOID_GENERATOR")
	@Column(name="reg_tipo_id")
	private Integer regTipoId;

	/** The reg tipo code. */
	@Column(name="reg_tipo_code")
	private String regTipoCode;

	/** The reg tipo desc. */
	@Column(name="reg_tipo_desc")
	private String regTipoDesc;

	//bi-directional many-to-one association to SiacRIvaRegTipoDocFamTipo
	/** The siac r iva reg tipo doc fam tipos. */
	@OneToMany(mappedBy="siacDIvaRegistrazioneTipo")
	private List<SiacRIvaRegTipoDocFamTipo> siacRIvaRegTipoDocFamTipos;

	//bi-directional many-to-one association to SiacTSubdocIva
	@OneToMany(mappedBy="siacDIvaRegistrazioneTipo")
	private List<SiacTSubdocIva> siacTSubdocIvas;

	/**
	 * Instantiates a new siac d iva registrazione tipo.
	 */
	public SiacDIvaRegistrazioneTipo() {
	}

	/**
	 * Gets the reg tipo id.
	 *
	 * @return the reg tipo id
	 */
	public Integer getRegTipoId() {
		return this.regTipoId;
	}

	/**
	 * Sets the reg tipo id.
	 *
	 * @param regTipoId the new reg tipo id
	 */
	public void setRegTipoId(Integer regTipoId) {
		this.regTipoId = regTipoId;
	}

	/**
	 * Gets the reg tipo code.
	 *
	 * @return the reg tipo code
	 */
	public String getRegTipoCode() {
		return this.regTipoCode;
	}

	/**
	 * Sets the reg tipo code.
	 *
	 * @param regTipoCode the new reg tipo code
	 */
	public void setRegTipoCode(String regTipoCode) {
		this.regTipoCode = regTipoCode;
	}

	/**
	 * Gets the reg tipo desc.
	 *
	 * @return the reg tipo desc
	 */
	public String getRegTipoDesc() {
		return this.regTipoDesc;
	}

	/**
	 * Sets the reg tipo desc.
	 *
	 * @param regTipoDesc the new reg tipo desc
	 */
	public void setRegTipoDesc(String regTipoDesc) {
		this.regTipoDesc = regTipoDesc;
	}

	
	/**
	 * Gets the siac r iva reg tipo doc fam tipos.
	 *
	 * @return the siac r iva reg tipo doc fam tipos
	 */
	public List<SiacRIvaRegTipoDocFamTipo> getSiacRIvaRegTipoDocFamTipos() {
		return this.siacRIvaRegTipoDocFamTipos;
	}

	/**
	 * Sets the siac r iva reg tipo doc fam tipos.
	 *
	 * @param siacRIvaRegTipoDocFamTipos the new siac r iva reg tipo doc fam tipos
	 */
	public void setSiacRIvaRegTipoDocFamTipos(List<SiacRIvaRegTipoDocFamTipo> siacRIvaRegTipoDocFamTipos) {
		this.siacRIvaRegTipoDocFamTipos = siacRIvaRegTipoDocFamTipos;
	}

	/**
	 * Adds the siac r iva reg tipo doc fam tipo.
	 *
	 * @param siacRIvaRegTipoDocFamTipo the siac r iva reg tipo doc fam tipo
	 * @return the siac r iva reg tipo doc fam tipo
	 */
	public SiacRIvaRegTipoDocFamTipo addSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipo siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().add(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDIvaRegistrazioneTipo(this);

		return siacRIvaRegTipoDocFamTipo;
	}

	/**
	 * Removes the siac r iva reg tipo doc fam tipo.
	 *
	 * @param siacRIvaRegTipoDocFamTipo the siac r iva reg tipo doc fam tipo
	 * @return the siac r iva reg tipo doc fam tipo
	 */
	public SiacRIvaRegTipoDocFamTipo removeSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipo siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().remove(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDIvaRegistrazioneTipo(null);

		return siacRIvaRegTipoDocFamTipo;
	}

	public List<SiacTSubdocIva> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	public void setSiacTSubdocIvas(List<SiacTSubdocIva> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	public SiacTSubdocIva addSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacDIvaRegistrazioneTipo(this);

		return siacTSubdocIva;
	}

	public SiacTSubdocIva removeSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacDIvaRegistrazioneTipo(null);

		return siacTSubdocIva;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return regTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.regTipoId = uid;
	}
}