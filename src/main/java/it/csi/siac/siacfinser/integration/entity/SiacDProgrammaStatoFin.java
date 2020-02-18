/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_programma_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_programma_stato")
public class SiacDProgrammaStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PROGRAMMA_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_programma_stato_programma_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PROGRAMMA_STATO_ID_GENERATOR")
	@Column(name="programma_stato_id")
	private Integer programmaStatoId;

	@Column(name="programma_stato_code")
	private String programmaStatoCode;

	@Column(name="programma_stato_desc")
	private String programmaStatoDesc;

	//bi-directional many-to-one association to SiacRProgrammaStatoFin
	@OneToMany(mappedBy="siacDProgrammaStato")
	private List<SiacRProgrammaStatoFin> siacRProgrammaStatos;

	public SiacDProgrammaStatoFin() {
	}

	public Integer getProgrammaStatoId() {
		return this.programmaStatoId;
	}

	public void setProgrammaStatoId(Integer programmaStatoId) {
		this.programmaStatoId = programmaStatoId;
	}

	public String getProgrammaStatoCode() {
		return this.programmaStatoCode;
	}

	public void setProgrammaStatoCode(String programmaStatoCode) {
		this.programmaStatoCode = programmaStatoCode;
	}

	public String getProgrammaStatoDesc() {
		return this.programmaStatoDesc;
	}

	public void setProgrammaStatoDesc(String programmaStatoDesc) {
		this.programmaStatoDesc = programmaStatoDesc;
	}

	public List<SiacRProgrammaStatoFin> getSiacRProgrammaStatos() {
		return this.siacRProgrammaStatos;
	}

	public void setSiacRProgrammaStatos(List<SiacRProgrammaStatoFin> siacRProgrammaStatos) {
		this.siacRProgrammaStatos = siacRProgrammaStatos;
	}

	public SiacRProgrammaStatoFin addSiacRProgrammaStato(SiacRProgrammaStatoFin siacRProgrammaStato) {
		getSiacRProgrammaStatos().add(siacRProgrammaStato);
		siacRProgrammaStato.setSiacDProgrammaStato(this);

		return siacRProgrammaStato;
	}

	public SiacRProgrammaStatoFin removeSiacRProgrammaStato(SiacRProgrammaStatoFin siacRProgrammaStato) {
		getSiacRProgrammaStatos().remove(siacRProgrammaStato);
		siacRProgrammaStato.setSiacDProgrammaStato(null);

		return siacRProgrammaStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.programmaStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.programmaStatoId = uid;
	}
}