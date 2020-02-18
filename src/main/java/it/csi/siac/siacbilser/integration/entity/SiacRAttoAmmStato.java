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
 * The persistent class for the siac_r_atto_amm_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_amm_stato")
@NamedQuery(name="SiacRAttoAmmStato.findAll", query="SELECT s FROM SiacRAttoAmmStato s")
public class SiacRAttoAmmStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The att attoamm stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_AMM_STATO_ATTATTOAMMSTATOID_GENERATOR", allocationSize=1, sequenceName="siac_r_atto_amm_stato_att_attoamm_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_AMM_STATO_ATTATTOAMMSTATOID_GENERATOR")
	@Column(name="att_attoamm_stato_id")
	private Integer attAttoammStatoId;

	//bi-directional many-to-one association to SiacDAttoAmmStato
	/** The siac d atto amm stato. */
	@ManyToOne
	@JoinColumn(name="attoamm_stato_id")
	private SiacDAttoAmmStato siacDAttoAmmStato;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	/**
	 * Instantiates a new siac r atto amm stato.
	 */
	public SiacRAttoAmmStato() {
	}

	/**
	 * Gets the att attoamm stato id.
	 *
	 * @return the att attoamm stato id
	 */
	public Integer getAttAttoammStatoId() {
		return this.attAttoammStatoId;
	}

	/**
	 * Sets the att attoamm stato id.
	 *
	 * @param attAttoammStatoId the new att attoamm stato id
	 */
	public void setAttAttoammStatoId(Integer attAttoammStatoId) {
		this.attAttoammStatoId = attAttoammStatoId;
	}

	/**
	 * Gets the siac d atto amm stato.
	 *
	 * @return the siac d atto amm stato
	 */
	public SiacDAttoAmmStato getSiacDAttoAmmStato() {
		return this.siacDAttoAmmStato;
	}

	/**
	 * Sets the siac d atto amm stato.
	 *
	 * @param siacDAttoAmmStato the new siac d atto amm stato
	 */
	public void setSiacDAttoAmmStato(SiacDAttoAmmStato siacDAttoAmmStato) {
		this.siacDAttoAmmStato = siacDAttoAmmStato;
	}

	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTAttoAmm the new siac t atto amm
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attAttoammStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		attAttoammStatoId = uid;
		
	}

}