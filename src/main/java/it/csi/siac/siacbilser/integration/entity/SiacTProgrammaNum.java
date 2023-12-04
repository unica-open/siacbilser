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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the siac_t_programma_num database table.
 */
@Entity
@Table(name="siac_t_programma_num")
@NamedQuery(name="SiacTProgrammaNum.findAll", query="SELECT s FROM SiacTProgrammaNum s")
public class SiacTProgrammaNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The programma num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PROGRAMMA_NUM_PROGRAMMANUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PROGRAMMA_NUM_PROGRAMMA_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROGRAMMA_NUM_PROGRAMMANUMID_GENERATOR")
	@Column(name="programma_num_id")
	private Integer programmaNumId;

	/** The programma anno. */
	@Column(name="programma_anno")
	private Integer programmaAnno;

	/** The programma numero. */
	@Column(name="programma_numero")
	@Version
	private Integer programmaNumero;
	
	/**
	 * Instantiates a new siac t programma.
	 */
	public SiacTProgrammaNum() {
	}

	/**
	 * Gets the programma num id.
	 *
	 * @return the programma num id
	 */
	public Integer getProgrammaNumId() {
		return this.programmaNumId;
	}

	/**
	 * Sets the programma num id.
	 *
	 * @param programmaNumId the new programma num id
	 */
	public void setProgrammaNumId(Integer programmaNumId) {
		this.programmaNumId = programmaNumId;
	}

	/**
	 * Gets the programma anno.
	 *
	 * @return the programma anno
	 */
	public Integer getProgrammaAnno() {
		return this.programmaAnno;
	}

	/**
	 * Sets the programma anno.
	 *
	 * @param programmaAnno the new programma anno
	 */
	public void setProgrammaAnno(Integer programmaAnno) {
		this.programmaAnno = programmaAnno;
	}

	/**
	 * Gets the programma numero.
	 *
	 * @return the programma numero
	 */
	public Integer getProgrammaNumero() {
		return this.programmaNumero;
	}

	/**
	 * Sets the programma numero.
	 *
	 * @param programmaNumero the new programma numero
	 */
	public void setProgrammaNumero(Integer programmaNumero) {
		this.programmaNumero = programmaNumero;
	}
	
	@Override
	public Integer getUid() {
		return programmaNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.programmaNumId = uid;
	}

}