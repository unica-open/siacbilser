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
 * The persistent class for the siac_r_mutuo_voce_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_voce_movgest")
public class SiacRMutuoVoceMovgestFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_VOCE_MOVGEST_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_mutuo_voce_movgest_mut_voce_movgest_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_VOCE_MOVGEST_ID_GENERATOR")
	@Column(name="mut_voce_movgest_id")
	private Integer mutVoceMovgestId;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestTs;

	//bi-directional many-to-one association to SiacTMutuoVoceFin
	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoceFin siacTMutuoVoce;

	public SiacRMutuoVoceMovgestFin() {
	}

	public Integer getMutVoceMovgestId() {
		return this.mutVoceMovgestId;
	}

	public void setMutVoceMovgestId(Integer mutVoceMovgestId) {
		this.mutVoceMovgestId = mutVoceMovgestId;
	}

	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return this.siacTMovgestTs;
	}

	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestTs = siacTMovgestT;
	}

	public SiacTMutuoVoceFin getSiacTMutuoVoce() {
		return this.siacTMutuoVoce;
	}

	public void setSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutVoceMovgestId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutVoceMovgestId = uid;
	}
}