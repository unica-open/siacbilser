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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_programma_affidamento database table.
 * 
 */
@Entity
@Table(name="siac_d_programma_affidamento")
public class SiacDProgrammaAffidamento extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6754522842903264474L;

	/** The ambito id. */
	@Id
	@SequenceGenerator(name="SIAC_D_PROGRAMMAAFFIDAMENTO_PROGRAMMAAFFIDAMENTOID_GENERATOR", allocationSize=1, sequenceName="siac_d_programma_affidamento_programma_affidamento_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PROGRAMMAAFFIDAMENTO_PROGRAMMAAFFIDAMENTOID_GENERATOR")
	@Column(name="programma_affidamento_id")
	private Integer programmaAffidamentoId;

	/** The ambito code. */
	@Column(name="programma_affidamento_code")
	private String programmaAffidamentoCode;

	/** The ambito desc. */
	@Column(name="programma_affidamento_desc")
	private String programmaAffidamentoDesc;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggettos. */
	@OneToMany(mappedBy="siacDProgrammaAffidamento")
	private List<SiacTProgramma> siacTProgrammas;
	
	/**
	 * Instantiates a new siac d ambito.
	 */
	public SiacDProgrammaAffidamento() {
	}

	/**
	 * @return the programmaAffidamentoId
	 */
	public Integer getProgrammaAffidamentoId() {
		return programmaAffidamentoId;
	}


	/**
	 * @param programmaAffidamentoId the programmaAffidamentoId to set
	 */
	public void setProgrammaAffidamentoId(Integer programmaAffidamentoId) {
		this.programmaAffidamentoId = programmaAffidamentoId;
	}


	/**
	 * @return the programmaAffidamentoCode
	 */
	public String getProgrammaAffidamentoCode() {
		return programmaAffidamentoCode;
	}


	/**
	 * @param programmaAffidamentoCode the programmaAffidamentoCode to set
	 */
	public void setProgrammaAffidamentoCode(String programmaAffidamentoCode) {
		this.programmaAffidamentoCode = programmaAffidamentoCode;
	}


	/**
	 * @return the programmaAffidamentoDesc
	 */
	public String getProgrammaAffidamentoDesc() {
		return programmaAffidamentoDesc;
	}


	/**
	 * @param programmaAffidamentoDesc the programmaAffidamentoDesc to set
	 */
	public void setProgrammaAffidamentoDesc(String programmaAffidamentoDesc) {
		this.programmaAffidamentoDesc = programmaAffidamentoDesc;
	}


	/**
	 * Gets the siac t soggettos.
	 *
	 * @return the siac t soggettos
	 */
	public List<SiacTProgramma> getSiacTProgrammas() {
		return this.siacTProgrammas;
	}

	/**
	 * Sets the siac t soggettos.
	 *
	 * @param siacTSoggettos the new siac t soggettos
	 */
	public void setSiacTProgrammas(List<SiacTProgramma> siacTProgrammas) {
		this.siacTProgrammas = siacTProgrammas;
	}

	/**
	 * Adds the siac t soggetto.
	 *
	 * @param siacTSoggetto the siac t soggetto
	 * @return the siac t soggetto
	 */
	public SiacTProgramma addSiacTProgramma(SiacTProgramma siacTProgramma) {
		getSiacTProgrammas().add(siacTProgramma);
		siacTProgramma.setSiacDProgrammaAffidamento(this);

		return siacTProgramma;
	}

	/**
	 * Removes the siac t soggetto.
	 *
	 * @param siacTSoggetto the siac t soggetto
	 * @return the siac t soggetto
	 */
	public SiacTProgramma removeSiacTSoggetto(SiacTProgramma siacTProgramma) {
		getSiacTProgrammas().remove(siacTProgramma);
		siacTProgramma.setSiacDProgrammaAffidamento(null);
		return siacTProgramma;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return programmaAffidamentoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.programmaAffidamentoId = uid;
		
	}

}