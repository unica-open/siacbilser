/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_programma_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_programma_stato")
public class SiacRProgrammaStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PROGRAMMA_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_programma_stato_programma_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PROGRAMMA_STATO_ID_GENERATOR")
	@Column(name="programma_stato_r_id")
	private Integer programmaStatoRId;

	//bi-directional many-to-one association to SiacDProgrammaStatoFin
	@ManyToOne
	@JoinColumn(name="programma_stato_id")
	private SiacDProgrammaStatoFin siacDProgrammaStato;

	//bi-directional many-to-one association to SiacTProgrammaFin
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgrammaFin siacTProgramma;

	public SiacRProgrammaStatoFin() {
	}

	public Integer getProgrammaStatoRId() {
		return this.programmaStatoRId;
	}

	public void setProgrammaStatoRId(Integer programmaStatoRId) {
		this.programmaStatoRId = programmaStatoRId;
	}

	public SiacDProgrammaStatoFin getSiacDProgrammaStato() {
		return this.siacDProgrammaStato;
	}

	public void setSiacDProgrammaStato(SiacDProgrammaStatoFin siacDProgrammaStato) {
		this.siacDProgrammaStato = siacDProgrammaStato;
	}

	public SiacTProgrammaFin getSiacTProgramma() {
		return this.siacTProgramma;
	}

	public void setSiacTProgramma(SiacTProgrammaFin siacTProgramma) {
		this.siacTProgramma = siacTProgramma;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.programmaStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.programmaStatoRId = uid;
	}
}