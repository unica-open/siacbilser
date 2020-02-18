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
 * The persistent class for the siac_r_programma_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_programma_stato")
@NamedQuery(name="SiacRProgrammaStato.findAll", query="SELECT s FROM SiacRProgrammaStato s")
public class SiacRProgrammaStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The programma stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PROGRAMMA_STATO_PROGRAMMASTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PROGRAMMA_STATO_PROGRAMMA_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PROGRAMMA_STATO_PROGRAMMASTATORID_GENERATOR")
	@Column(name="programma_stato_r_id")
	private Integer programmaStatoRId;

	//bi-directional many-to-one association to SiacDProgrammaStato
	/** The siac d programma stato. */
	@ManyToOne
	@JoinColumn(name="programma_stato_id")
	private SiacDProgrammaStato siacDProgrammaStato;

	//bi-directional many-to-one association to SiacTProgramma
	/** The siac t programma. */
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgramma siacTProgramma;

	/**
	 * Instantiates a new siac r programma stato.
	 */
	public SiacRProgrammaStato() {
	}

	/**
	 * Gets the programma stato r id.
	 *
	 * @return the programma stato r id
	 */
	public Integer getProgrammaStatoRId() {
		return this.programmaStatoRId;
	}

	/**
	 * Sets the programma stato r id.
	 *
	 * @param programmaStatoRId the new programma stato r id
	 */
	public void setProgrammaStatoRId(Integer programmaStatoRId) {
		this.programmaStatoRId = programmaStatoRId;
	}

	/**
	 * Gets the siac d programma stato.
	 *
	 * @return the siac d programma stato
	 */
	public SiacDProgrammaStato getSiacDProgrammaStato() {
		return this.siacDProgrammaStato;
	}

	/**
	 * Sets the siac d programma stato.
	 *
	 * @param siacDProgrammaStato the new siac d programma stato
	 */
	public void setSiacDProgrammaStato(SiacDProgrammaStato siacDProgrammaStato) {
		this.siacDProgrammaStato = siacDProgrammaStato;
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
		return programmaStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.programmaStatoRId = uid;
	}

}