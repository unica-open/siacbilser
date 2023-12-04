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
 * The persistent class for the siac_r_movgest_ts_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_stato")
@NamedQuery(name="SiacRMovgestTsStato.findAll", query="SELECT s FROM SiacRMovgestTsStato s")
public class SiacRMovgestTsStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_STATO_MOVGESTSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_TS_STATO_MOVGEST_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_STATO_MOVGESTSTATORID_GENERATOR")
	@Column(name="movgest_stato_r_id")
	private Integer movgestStatoRId;



	//bi-directional many-to-one association to SiacDMovgestStato
	/** The siac d movgest stato. */
	@ManyToOne
	@JoinColumn(name="movgest_stato_id")
	private SiacDMovgestStato siacDMovgestStato;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	/**
	 * Instantiates a new siac r movgest ts stato.
	 */
	public SiacRMovgestTsStato() {
	}

	/**
	 * Gets the movgest stato r id.
	 *
	 * @return the movgest stato r id
	 */
	public Integer getMovgestStatoRId() {
		return this.movgestStatoRId;
	}

	/**
	 * Sets the movgest stato r id.
	 *
	 * @param movgestStatoRId the new movgest stato r id
	 */
	public void setMovgestStatoRId(Integer movgestStatoRId) {
		this.movgestStatoRId = movgestStatoRId;
	}


	/**
	 * Gets the siac d movgest stato.
	 *
	 * @return the siac d movgest stato
	 */
	public SiacDMovgestStato getSiacDMovgestStato() {
		return this.siacDMovgestStato;
	}

	/**
	 * Sets the siac d movgest stato.
	 *
	 * @param siacDMovgestStato the new siac d movgest stato
	 */
	public void setSiacDMovgestStato(SiacDMovgestStato siacDMovgestStato) {
		this.siacDMovgestStato = siacDMovgestStato;
	}


	/**
	 * Gets the siac t movgest t.
	 *
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	/**
	 * Sets the siac t movgest t.
	 *
	 * @param siacTMovgestT the new siac t movgest t
	 */
	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movgestStatoRId = uid;
	}

}