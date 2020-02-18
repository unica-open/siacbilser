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
 * The persistent class for the siac_r_predoc_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_stato")
@NamedQuery(name="SiacRPredocStato.findAll", query="SELECT s FROM SiacRPredocStato s")
public class SiacRPredocStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_STATO_PREDOCSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PREDOC_STATO_PREDOC_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_STATO_PREDOCSTATORID_GENERATOR")
	@Column(name="predoc_stato_r_id")
	private Integer predocStatoRId;

	//bi-directional many-to-one association to SiacDPredocStato
	/** The siac d predoc stato. */
	@ManyToOne
	@JoinColumn(name="predoc_stato_id")
	private SiacDPredocStato siacDPredocStato;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	/**
	 * Instantiates a new siac r predoc stato.
	 */
	public SiacRPredocStato() {
	}

	/**
	 * Gets the predoc stato r id.
	 *
	 * @return the predoc stato r id
	 */
	public Integer getPredocStatoRId() {
		return this.predocStatoRId;
	}

	/**
	 * Sets the predoc stato r id.
	 *
	 * @param predocStatoRId the new predoc stato r id
	 */
	public void setPredocStatoRId(Integer predocStatoRId) {
		this.predocStatoRId = predocStatoRId;
	}

	/**
	 * Gets the siac d predoc stato.
	 *
	 * @return the siac d predoc stato
	 */
	public SiacDPredocStato getSiacDPredocStato() {
		return this.siacDPredocStato;
	}

	/**
	 * Sets the siac d predoc stato.
	 *
	 * @param siacDPredocStato the new siac d predoc stato
	 */
	public void setSiacDPredocStato(SiacDPredocStato siacDPredocStato) {
		this.siacDPredocStato = siacDPredocStato;
	}

	/**
	 * Gets the siac t predoc.
	 *
	 * @return the siac t predoc
	 */
	public SiacTPredoc getSiacTPredoc() {
		return this.siacTPredoc;
	}

	/**
	 * Sets the siac t predoc.
	 *
	 * @param siacTPredoc the new siac t predoc
	 */
	public void setSiacTPredoc(SiacTPredoc siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocStatoRId = uid;
	}

}