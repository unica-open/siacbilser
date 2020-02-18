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
 * The persistent class for the siac_d_atto_amm_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_amm_stato")
@NamedQuery(name="SiacDAttoAmmStato.findAll", query="SELECT s FROM SiacDAttoAmmStato s")
public class SiacDAttoAmmStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attoamm stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ATTO_AMM_STATO_ATTOAMMSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ATTO_AMM_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTO_AMM_STATO_ATTOAMMSTATOID_GENERATOR")
	@Column(name="attoamm_stato_id")
	private Integer attoammStatoId;

	/** The attoamm stato code. */
	@Column(name="attoamm_stato_code")
	private String attoammStatoCode;

	/** The attoamm stato desc. */
	@Column(name="attoamm_stato_desc")
	private String attoammStatoDesc;

	//bi-directional many-to-one association to SiacRAttoAmmStato
	/** The siac r atto amm statos. */
	@OneToMany(mappedBy="siacDAttoAmmStato")
	private List<SiacRAttoAmmStato> siacRAttoAmmStatos;
	
	//bi-directional many-to-one association to SiacRAttoAmmStatoInOut
	/** The siac r atto amm statos. */
	@OneToMany(mappedBy="siacDAttoAmmStato")
	private List<SiacRAttoAmmStatoInOut> siacRAttoAmmStatoInOuts;

	/**
	 * Instantiates a new siac d atto amm stato.
	 */
	public SiacDAttoAmmStato() {
	}

	/**
	 * Gets the attoamm stato id.
	 *
	 * @return the attoamm stato id
	 */
	public Integer getAttoammStatoId() {
		return this.attoammStatoId;
	}

	/**
	 * Sets the attoamm stato id.
	 *
	 * @param attoammStatoId the new attoamm stato id
	 */
	public void setAttoammStatoId(Integer attoammStatoId) {
		this.attoammStatoId = attoammStatoId;
	}

	/**
	 * Gets the attoamm stato code.
	 *
	 * @return the attoamm stato code
	 */
	public String getAttoammStatoCode() {
		return this.attoammStatoCode;
	}

	/**
	 * Sets the attoamm stato code.
	 *
	 * @param attoammStatoCode the new attoamm stato code
	 */
	public void setAttoammStatoCode(String attoammStatoCode) {
		this.attoammStatoCode = attoammStatoCode;
	}

	/**
	 * Gets the attoamm stato desc.
	 *
	 * @return the attoamm stato desc
	 */
	public String getAttoammStatoDesc() {
		return this.attoammStatoDesc;
	}

	/**
	 * Sets the attoamm stato desc.
	 *
	 * @param attoammStatoDesc the new attoamm stato desc
	 */
	public void setAttoammStatoDesc(String attoammStatoDesc) {
		this.attoammStatoDesc = attoammStatoDesc;
	}

	/**
	 * Gets the siac r atto amm statos.
	 *
	 * @return the siac r atto amm statos
	 */
	public List<SiacRAttoAmmStato> getSiacRAttoAmmStatos() {
		return this.siacRAttoAmmStatos;
	}

	/**
	 * Sets the siac r atto amm statos.
	 *
	 * @param siacRAttoAmmStatos the new siac r atto amm statos
	 */
	public void setSiacRAttoAmmStatos(List<SiacRAttoAmmStato> siacRAttoAmmStatos) {
		this.siacRAttoAmmStatos = siacRAttoAmmStatos;
	}

	/**
	 * Adds the siac r atto amm stato.
	 *
	 * @param siacRAttoAmmStato the siac r atto amm stato
	 * @return the siac r atto amm stato
	 */
	public SiacRAttoAmmStato addSiacRAttoAmmStato(SiacRAttoAmmStato siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().add(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacDAttoAmmStato(this);

		return siacRAttoAmmStato;
	}

	/**
	 * Removes the siac r atto amm stato.
	 *
	 * @param siacRAttoAmmStato the siac r atto amm stato
	 * @return the siac r atto amm stato
	 */
	public SiacRAttoAmmStato removeSiacRAttoAmmStato(SiacRAttoAmmStato siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().remove(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacDAttoAmmStato(null);

		return siacRAttoAmmStato;
	}
	
	/**
	 * Gets the siac r atto amm stato in outs.
	 *
	 * @return the siac r atto amm stato in outs
	 */
	public List<SiacRAttoAmmStatoInOut> getSiacRAttoAmmStatoInOuts() {
		return this.siacRAttoAmmStatoInOuts;
	}

	/**
	 * Sets the siac r atto amm stato in outs.
	 *
	 * @param siacRAttoAmmStatoInOuts the new siac r atto amm stato in outs
	 */
	public void setSiacRAttoAmmStatoInOuts(List<SiacRAttoAmmStatoInOut> siacRAttoAmmStatoInOuts) {
		this.siacRAttoAmmStatoInOuts = siacRAttoAmmStatoInOuts;
	}

	/**
	 * Adds the siac r atto amm stato in out.
	 *
	 * @param siacRAttoAmmStatoInOut the siac r atto amm stato in out
	 * @return the siac r atto amm stato in out
	 */
	public SiacRAttoAmmStatoInOut addSiacRAttoAmmStatoInOut(SiacRAttoAmmStatoInOut siacRAttoAmmStatoInOut) {
		getSiacRAttoAmmStatoInOuts().add(siacRAttoAmmStatoInOut);
		siacRAttoAmmStatoInOut.setSiacDAttoAmmStato(this);

		return siacRAttoAmmStatoInOut;
	}

	/**
	 * Removes the siac r atto amm stato in out.
	 *
	 * @param siacRAttoAmmStatoInOut the siac r atto amm stato in out
	 * @return the siac r atto amm stato in out
	 */
	public SiacRAttoAmmStatoInOut removeSiacRAttoAmmStatoInOut(SiacRAttoAmmStatoInOut siacRAttoAmmStatoInOut) {
		getSiacRAttoAmmStatoInOuts().remove(siacRAttoAmmStatoInOut);
		siacRAttoAmmStatoInOut.setSiacDAttoAmmStato(null);

		return siacRAttoAmmStatoInOut;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attoammStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		attoammStatoId = uid;
	}

}