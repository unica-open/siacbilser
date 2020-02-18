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
 * The persistent class for the siac_d_bil_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_tipo")
@NamedQuery(name="SiacDBilTipo.findAll", query="SELECT s FROM SiacDBilTipo s")
public class SiacDBilTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_BIL_TIPO_BILTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_TIPO_BIL_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_TIPO_BILTIPOID_GENERATOR")
	@Column(name="bil_tipo_id")
	private Integer bilTipoId;

	/** The bil tipo code. */
	@Column(name="bil_tipo_code")
	private String bilTipoCode;

	/** The bil tipo desc. */
	@Column(name="bil_tipo_desc")
	private String bilTipoDesc;

	//bi-directional many-to-one association to SiacRBilTipoStatoOp
	/** The siac r bil tipo stato ops. */
	@OneToMany(mappedBy="siacDBilTipo")
	private List<SiacRBilTipoStatoOp> siacRBilTipoStatoOps;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bils. */
	@OneToMany(mappedBy="siacDBilTipo")
	private List<SiacTBil> siacTBils;

	/**
	 * Instantiates a new siac d bil tipo.
	 */
	public SiacDBilTipo() {
	}

	/**
	 * Gets the bil tipo id.
	 *
	 * @return the bil tipo id
	 */
	public Integer getBilTipoId() {
		return this.bilTipoId;
	}

	/**
	 * Sets the bil tipo id.
	 *
	 * @param bilTipoId the new bil tipo id
	 */
	public void setBilTipoId(Integer bilTipoId) {
		this.bilTipoId = bilTipoId;
	}

	/**
	 * Gets the bil tipo code.
	 *
	 * @return the bil tipo code
	 */
	public String getBilTipoCode() {
		return this.bilTipoCode;
	}

	/**
	 * Sets the bil tipo code.
	 *
	 * @param bilTipoCode the new bil tipo code
	 */
	public void setBilTipoCode(String bilTipoCode) {
		this.bilTipoCode = bilTipoCode;
	}

	/**
	 * Gets the bil tipo desc.
	 *
	 * @return the bil tipo desc
	 */
	public String getBilTipoDesc() {
		return this.bilTipoDesc;
	}

	/**
	 * Sets the bil tipo desc.
	 *
	 * @param bilTipoDesc the new bil tipo desc
	 */
	public void setBilTipoDesc(String bilTipoDesc) {
		this.bilTipoDesc = bilTipoDesc;
	}

	/**
	 * Gets the siac r bil tipo stato ops.
	 *
	 * @return the siac r bil tipo stato ops
	 */
	public List<SiacRBilTipoStatoOp> getSiacRBilTipoStatoOps() {
		return this.siacRBilTipoStatoOps;
	}

	/**
	 * Sets the siac r bil tipo stato ops.
	 *
	 * @param siacRBilTipoStatoOps the new siac r bil tipo stato ops
	 */
	public void setSiacRBilTipoStatoOps(List<SiacRBilTipoStatoOp> siacRBilTipoStatoOps) {
		this.siacRBilTipoStatoOps = siacRBilTipoStatoOps;
	}

	/**
	 * Adds the siac r bil tipo stato op.
	 *
	 * @param siacRBilTipoStatoOp the siac r bil tipo stato op
	 * @return the siac r bil tipo stato op
	 */
	public SiacRBilTipoStatoOp addSiacRBilTipoStatoOp(SiacRBilTipoStatoOp siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().add(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilTipo(this);

		return siacRBilTipoStatoOp;
	}

	/**
	 * Removes the siac r bil tipo stato op.
	 *
	 * @param siacRBilTipoStatoOp the siac r bil tipo stato op
	 * @return the siac r bil tipo stato op
	 */
	public SiacRBilTipoStatoOp removeSiacRBilTipoStatoOp(SiacRBilTipoStatoOp siacRBilTipoStatoOp) {
		getSiacRBilTipoStatoOps().remove(siacRBilTipoStatoOp);
		siacRBilTipoStatoOp.setSiacDBilTipo(null);

		return siacRBilTipoStatoOp;
	}

	/**
	 * Gets the siac t bils.
	 *
	 * @return the siac t bils
	 */
	public List<SiacTBil> getSiacTBils() {
		return this.siacTBils;
	}

	/**
	 * Sets the siac t bils.
	 *
	 * @param siacTBils the new siac t bils
	 */
	public void setSiacTBils(List<SiacTBil> siacTBils) {
		this.siacTBils = siacTBils;
	}

	/**
	 * Adds the siac t bil.
	 *
	 * @param siacTBil the siac t bil
	 * @return the siac t bil
	 */
	public SiacTBil addSiacTBil(SiacTBil siacTBil) {
		getSiacTBils().add(siacTBil);
		siacTBil.setSiacDBilTipo(this);

		return siacTBil;
	}

	/**
	 * Removes the siac t bil.
	 *
	 * @param siacTBil the siac t bil
	 * @return the siac t bil
	 */
	public SiacTBil removeSiacTBil(SiacTBil siacTBil) {
		getSiacTBils().remove(siacTBil);
		siacTBil.setSiacDBilTipo(null);

		return siacTBil;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilTipoId = uid;		
	}

}