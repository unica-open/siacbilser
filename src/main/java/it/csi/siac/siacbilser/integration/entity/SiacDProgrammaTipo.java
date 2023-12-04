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
 * The persistent class for the siac_d_programma_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_programma_tipo")
public class SiacDProgrammaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6754522842903264474L;

	/** The ambito id. */
	@Id
	@SequenceGenerator(name="SIAC_D_PROGRAMMATIPO_PROGRAMMATIPOID_GENERATOR", allocationSize=1, sequenceName="siac_d_programma_tipo_programma_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PROGRAMMATIPO_PROGRAMMATIPOID_GENERATOR")
	@Column(name="programma_tipo_id")
	private Integer programmaTipoId;

	/** The ambito code. */
	@Column(name="programma_tipo_code")
	private String programmaTipoCode;

	/** The ambito desc. */
	@Column(name="programma_tipo_desc")
	private String programmaTipoDesc;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggettos. */
	@OneToMany(mappedBy="siacDProgrammaTipo")
	private List<SiacTProgramma> siacTProgrammas;
	
	/**
	 * Instantiates a new siac d ambito.
	 */
	public SiacDProgrammaTipo() {
	}

	/**
	 * @return the programmaTipoId
	 */
	public Integer getProgrammaTipoId() {
		return programmaTipoId;
	}


	/**
	 * @param programmaTipoId the programmaTipoId to set
	 */
	public void setProgrammaTipoId(Integer programmaTipoId) {
		this.programmaTipoId = programmaTipoId;
	}


	/**
	 * @return the programmaTipoCode
	 */
	public String getProgrammaTipoCode() {
		return programmaTipoCode;
	}


	/**
	 * @param programmaTipoCode the programmaTipoCode to set
	 */
	public void setProgrammaTipoCode(String programmaTipoCode) {
		this.programmaTipoCode = programmaTipoCode;
	}


	/**
	 * @return the programmaTipoDesc
	 */
	public String getProgrammaTipoDesc() {
		return programmaTipoDesc;
	}


	/**
	 * @param programmaTipoDesc the programmaTipoDesc to set
	 */
	public void setProgrammaTipoDesc(String programmaTipoDesc) {
		this.programmaTipoDesc = programmaTipoDesc;
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
	 * @param siacTProgrammas the new siac T programmas
	 */
	public void setSiacTProgrammas(List<SiacTProgramma> siacTProgrammas) {
		this.siacTProgrammas = siacTProgrammas;
	}

	/**
	 * Adds the siac t soggetto.
	 *
	 * @param siacTProgramma the siac T programma
	 * @return the siac t soggetto
	 */
	public SiacTProgramma addSiacTProgramma(SiacTProgramma siacTProgramma) {
		getSiacTProgrammas().add(siacTProgramma);
		siacTProgramma.setSiacDProgrammaTipo(this);

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
		siacTProgramma.setSiacDProgrammaTipo(null);
		return siacTProgramma;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return programmaTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.programmaTipoId = uid;
		
	}

}