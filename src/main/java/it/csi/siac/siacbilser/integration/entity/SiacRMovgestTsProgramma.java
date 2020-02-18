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
 * The persistent class for the siac_r_movgest_ts_programma database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_programma")
@NamedQuery(name="SiacRMovgestTsProgramma.findAll", query="SELECT s FROM SiacRMovgestTsProgramma s")
public class SiacRMovgestTsProgramma extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The movgest ts programma id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_PROGRAMMA_MOVGESTTSPROGRAMMAID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_TS_PROGRAMMA_MOVGEST_TS_PROGRAMMA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_PROGRAMMA_MOVGESTTSPROGRAMMAID_GENERATOR")
	@Column(name="movgest_ts_programma_id")
	private Integer movgestTsProgrammaId;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTProgramma
	/** The siac t programma. */
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgramma siacTProgramma;

	/**
	 * Instantiates a new siac r movgest ts programma.
	 */
	public SiacRMovgestTsProgramma() {
	}

	/**
	 * Gets the movgest ts programma id.
	 *
	 * @return the movgest ts programma id
	 */
	public Integer getMovgestTsProgrammaId() {
		return this.movgestTsProgrammaId;
	}

	/**
	 * Sets the movgest ts programma id.
	 *
	 * @param movgestTsProgrammaId the new movgest ts programma id
	 */
	public void setMovgestTsProgrammaId(Integer movgestTsProgrammaId) {
		this.movgestTsProgrammaId = movgestTsProgrammaId;
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

	/**
	 * Gets the siac t programma.
	 *
	 * @return the siac t programma
	 */
	public SiacTProgramma getSiacTProgramma() {
		return this.siacTProgramma;
	}

	/**
	 * Sets the siac t programma.
	 *
	 * @param siacTProgramma the new siac t programma
	 */
	public void setSiacTProgramma(SiacTProgramma siacTProgramma) {
		this.siacTProgramma = siacTProgramma;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsProgrammaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movgestTsProgrammaId = uid;
	}

}