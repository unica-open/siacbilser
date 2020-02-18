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
 * The persistent class for the siac_r_programma_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_programma_atto_amm")
@NamedQuery(name="SiacRProgrammaAttoAmm.findAll", query="SELECT s FROM SiacRProgrammaAttoAmm s")
public class SiacRProgrammaAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The programma atto amm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PROGRAMMA_ATTO_AMM_PROGRAMMAATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PROGRAMMA_ATTO_AMM_PROGRAMMA_ATTO_AMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PROGRAMMA_ATTO_AMM_PROGRAMMAATTOAMMID_GENERATOR")
	@Column(name="programma_atto_amm_id")
	private Integer programmaAttoAmmId;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTProgramma
	/** The siac t programma. */
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgramma siacTProgramma;

	/**
	 * Instantiates a new siac r programma atto amm.
	 */
	public SiacRProgrammaAttoAmm() {
	}

	/**
	 * Gets the programma atto amm id.
	 *
	 * @return the programma atto amm id
	 */
	public Integer getProgrammaAttoAmmId() {
		return this.programmaAttoAmmId;
	}

	/**
	 * Sets the programma atto amm id.
	 *
	 * @param programmaAttoAmmId the new programma atto amm id
	 */
	public void setProgrammaAttoAmmId(Integer programmaAttoAmmId) {
		this.programmaAttoAmmId = programmaAttoAmmId;
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
		return programmaAttoAmmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.programmaAttoAmmId = uid;
	}

}