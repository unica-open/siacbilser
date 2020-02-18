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
 * The persistent class for the siac_r_mutuo_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_stato")
public class SiacRMutuoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_STATO_MUTUO_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_mutuo_stato_mut_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_STATO_MUTUO_STATO_ID_GENERATOR")
	@Column(name="mut_stato_r_id")
	private Integer mutStatoRId;

	//bi-directional many-to-one association to SiacDMutuoStatoFin
	@ManyToOne
	@JoinColumn(name="mut_stato_id")
	private SiacDMutuoStatoFin siacDMutuoStato;

	//bi-directional many-to-one association to SiacTMutuoFin
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuoFin siacTMutuo;

	public SiacRMutuoStatoFin() {
	}

	public Integer getMutStatoRId() {
		return this.mutStatoRId;
	}

	public void setMutStatoRId(Integer mutStatoRId) {
		this.mutStatoRId = mutStatoRId;
	}

	public SiacDMutuoStatoFin getSiacDMutuoStato() {
		return this.siacDMutuoStato;
	}

	public void setSiacDMutuoStato(SiacDMutuoStatoFin siacDMutuoStato) {
		this.siacDMutuoStato = siacDMutuoStato;
	}

	public SiacTMutuoFin getSiacTMutuo() {
		return this.siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuoFin siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutStatoRId = uid;
	}
}