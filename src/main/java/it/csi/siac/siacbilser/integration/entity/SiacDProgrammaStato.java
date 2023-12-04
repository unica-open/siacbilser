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
 * The persistent class for the siac_d_programma_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_programma_stato")
@NamedQuery(name="SiacDProgrammaStato.findAll", query="SELECT s FROM SiacDProgrammaStato s")
public class SiacDProgrammaStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The programma stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_PROGRAMMA_STATO_PROGRAMMASTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PROGRAMMA_STATO_PROGRAMMA_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PROGRAMMA_STATO_PROGRAMMASTATOID_GENERATOR")
	@Column(name="programma_stato_id")
	private Integer programmaStatoId;

	/** The programma stato code. */
	@Column(name="programma_stato_code")
	private String programmaStatoCode;

	/** The programma stato desc. */
	@Column(name="programma_stato_desc")
	private String programmaStatoDesc;
	
	//bi-directional many-to-one association to SiacRProgrammaStato
	/** The siac r programma statos. */
	@OneToMany(mappedBy="siacDProgrammaStato")
	private List<SiacRProgrammaStato> siacRProgrammaStatos;

	/**
	 * Instantiates a new siac d programma stato.
	 */
	public SiacDProgrammaStato() {
	}

	/**
	 * Gets the programma stato id.
	 *
	 * @return the programma stato id
	 */
	public Integer getProgrammaStatoId() {
		return this.programmaStatoId;
	}

	/**
	 * Sets the programma stato id.
	 *
	 * @param programmaStatoId the new programma stato id
	 */
	public void setProgrammaStatoId(Integer programmaStatoId) {
		this.programmaStatoId = programmaStatoId;
	}

	/**
	 * Gets the programma stato code.
	 *
	 * @return the programma stato code
	 */
	public String getProgrammaStatoCode() {
		return this.programmaStatoCode;
	}

	/**
	 * Sets the programma stato code.
	 *
	 * @param programmaStatoCode the new programma stato code
	 */
	public void setProgrammaStatoCode(String programmaStatoCode) {
		this.programmaStatoCode = programmaStatoCode;
	}

	/**
	 * Gets the programma stato desc.
	 *
	 * @return the programma stato desc
	 */
	public String getProgrammaStatoDesc() {
		return this.programmaStatoDesc;
	}

	/**
	 * Sets the programma stato desc.
	 *
	 * @param programmaStatoDesc the new programma stato desc
	 */
	public void setProgrammaStatoDesc(String programmaStatoDesc) {
		this.programmaStatoDesc = programmaStatoDesc;
	}

	/**
	 * Gets the siac r programma statos.
	 *
	 * @return the siac r programma statos
	 */
	public List<SiacRProgrammaStato> getSiacRProgrammaStatos() {
		return this.siacRProgrammaStatos;
	}

	/**
	 * Sets the siac r programma statos.
	 *
	 * @param siacRProgrammaStatos the new siac r programma statos
	 */
	public void setSiacRProgrammaStatos(List<SiacRProgrammaStato> siacRProgrammaStatos) {
		this.siacRProgrammaStatos = siacRProgrammaStatos;
	}

	/**
	 * Adds the siac r programma stato.
	 *
	 * @param siacRProgrammaStato the siac r programma stato
	 * @return the siac r programma stato
	 */
	public SiacRProgrammaStato addSiacRProgrammaStato(SiacRProgrammaStato siacRProgrammaStato) {
		getSiacRProgrammaStatos().add(siacRProgrammaStato);
		siacRProgrammaStato.setSiacDProgrammaStato(this);

		return siacRProgrammaStato;
	}

	/**
	 * Removes the siac r programma stato.
	 *
	 * @param siacRProgrammaStato the siac r programma stato
	 * @return the siac r programma stato
	 */
	public SiacRProgrammaStato removeSiacRProgrammaStato(SiacRProgrammaStato siacRProgrammaStato) {
		getSiacRProgrammaStatos().remove(siacRProgrammaStato);
		siacRProgrammaStato.setSiacDProgrammaStato(null);

		return siacRProgrammaStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return programmaStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.programmaStatoId = uid;
		
	}

}