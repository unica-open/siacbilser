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
 * The persistent class for the siac_r_mutuo_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_atto_amm")
public class SiacRMutuoAttoAmmFin  extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_ATTO_AMM_R_MUTUO_AMM_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_mutuo_atto_amm_mut_atto_amm_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_ATTO_AMM_R_MUTUO_AMM_ID_GENERATOR")
	@Column(name="mut_atto_amm_id")
	private Integer mutAttoAmmId;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	//bi-directional many-to-one association to SiacTMutuoFin
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuoFin siacTMutuo;

	public SiacRMutuoAttoAmmFin() {
	}

	public Integer getMutAttoAmmId() {
		return this.mutAttoAmmId;
	}

	public void setMutAttoAmmId(Integer mutAttoAmmId) {
		this.mutAttoAmmId = mutAttoAmmId;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
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
		return this.mutAttoAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutAttoAmmId = uid;
	}
}