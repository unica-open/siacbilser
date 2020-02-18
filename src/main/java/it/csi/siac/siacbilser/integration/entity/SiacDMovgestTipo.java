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

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_movgest_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_tipo")
@NamedQuery(name="SiacDMovgestTipo.findAll", query="SELECT s FROM SiacDMovgestTipo s")
public class SiacDMovgestTipo extends SiacTBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MOVGEST_TIPO_MOVGESTTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MOVGEST_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MOVGEST_TIPO_MOVGESTTIPOID_GENERATOR")
	@Column(name="movgest_tipo_id")
	private Integer movgestTipoId;

	/** The movgest tipo code. */
	@Column(name="movgest_tipo_code")
	private String movgestTipoCode;

	/** The movgest tipo desc. */
	@Column(name="movgest_tipo_desc")
	private String movgestTipoDesc;

	//bi-directional many-to-one association to SiacRMovgestTipoClassTip
	@OneToMany(mappedBy="siacDMovgestTipo")
	private List<SiacRMovgestTipoClassTip> siacRMovgestTipoClassTips;

	//bi-directional many-to-one association to SiacTMovgest
	/** The siac t movgests. */
	@OneToMany(mappedBy="siacDMovgestTipo")
	private List<SiacTMovgest> siacTMovgests;

	/**
	 * Instantiates a new siac d movgest tipo.
	 */
	public SiacDMovgestTipo() {
	}

	/**
	 * Gets the movgest tipo id.
	 *
	 * @return the movgest tipo id
	 */
	public Integer getMovgestTipoId() {
		return this.movgestTipoId;
	}

	/**
	 * Sets the movgest tipo id.
	 *
	 * @param movgestTipoId the new movgest tipo id
	 */
	public void setMovgestTipoId(Integer movgestTipoId) {
		this.movgestTipoId = movgestTipoId;
	}

	/**
	 * Gets the movgest tipo code.
	 *
	 * @return the movgest tipo code
	 */
	public String getMovgestTipoCode() {
		return this.movgestTipoCode;
	}

	/**
	 * Sets the movgest tipo code.
	 *
	 * @param movgestTipoCode the new movgest tipo code
	 */
	public void setMovgestTipoCode(String movgestTipoCode) {
		this.movgestTipoCode = movgestTipoCode;
	}

	/**
	 * Gets the movgest tipo desc.
	 *
	 * @return the movgest tipo desc
	 */
	public String getMovgestTipoDesc() {
		return this.movgestTipoDesc;
	}

	/**
	 * Sets the movgest tipo desc.
	 *
	 * @param movgestTipoDesc the new movgest tipo desc
	 */
	public void setMovgestTipoDesc(String movgestTipoDesc) {
		this.movgestTipoDesc = movgestTipoDesc;
	}

	public List<SiacRMovgestTipoClassTip> getSiacRMovgestTipoClassTips() {
		return this.siacRMovgestTipoClassTips;
	}

	public void setSiacRMovgestTipoClassTips(List<SiacRMovgestTipoClassTip> siacRMovgestTipoClassTips) {
		this.siacRMovgestTipoClassTips = siacRMovgestTipoClassTips;
	}

	public SiacRMovgestTipoClassTip addSiacRMovgestTipoClassTip(SiacRMovgestTipoClassTip siacRMovgestTipoClassTip) {
		getSiacRMovgestTipoClassTips().add(siacRMovgestTipoClassTip);
		siacRMovgestTipoClassTip.setSiacDMovgestTipo(this);

		return siacRMovgestTipoClassTip;
	}

	public SiacRMovgestTipoClassTip removeSiacRMovgestTipoClassTip(SiacRMovgestTipoClassTip siacRMovgestTipoClassTip) {
		getSiacRMovgestTipoClassTips().remove(siacRMovgestTipoClassTip);
		siacRMovgestTipoClassTip.setSiacDMovgestTipo(null);

		return siacRMovgestTipoClassTip;
	}

	/**
	 * Gets the siac t movgests.
	 *
	 * @return the siac t movgests
	 */
	public List<SiacTMovgest> getSiacTMovgests() {
		return this.siacTMovgests;
	}

	/**
	 * Sets the siac t movgests.
	 *
	 * @param siacTMovgests the new siac t movgests
	 */
	public void setSiacTMovgests(List<SiacTMovgest> siacTMovgests) {
		this.siacTMovgests = siacTMovgests;
	}

	/**
	 * Adds the siac t movgest.
	 *
	 * @param siacTMovgest the siac t movgest
	 * @return the siac t movgest
	 */
	public SiacTMovgest addSiacTMovgest(SiacTMovgest siacTMovgest) {
		getSiacTMovgests().add(siacTMovgest);
		siacTMovgest.setSiacDMovgestTipo(this);

		return siacTMovgest;
	}

	/**
	 * Removes the siac t movgest.
	 *
	 * @param siacTMovgest the siac t movgest
	 * @return the siac t movgest
	 */
	public SiacTMovgest removeSiacTMovgest(SiacTMovgest siacTMovgest) {
		getSiacTMovgests().remove(siacTMovgest);
		siacTMovgest.setSiacDMovgestTipo(null);

		return siacTMovgest;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestTipoId = uid;
	}

}