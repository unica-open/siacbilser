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


/**
 * The persistent class for the siac_r_mutuo_voce_predoc database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_voce_predoc")
@NamedQuery(name="SiacRMutuoVocePredoc.findAll", query="SELECT s FROM SiacRMutuoVocePredoc s")
public class SiacRMutuoVocePredoc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_VOCE_PREDOC_MVPID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_VOCE_PREDOC_MVP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_VOCE_PREDOC_MVPID_GENERATOR")
	@Column(name="mvp_id")
	private Integer mvpId;

	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoce siacTMutuoVoce;
	
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;
	
	public SiacRMutuoVocePredoc() {
	}

	public Integer getMvpId() {
		return mvpId;
	}

	public void setMvpId(Integer mvpId) {
		this.mvpId = mvpId;
	}

	public SiacTMutuoVoce getSiacTMutuoVoce() {
		return siacTMutuoVoce;
	}

	public void setSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	public SiacTPredoc getSiacTPredoc() {
		return siacTPredoc;
	}

	public void setSiacTPredoc(SiacTPredoc siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	@Override
	public Integer getUid() {
		return mvpId;
	}

	@Override
	public void setUid(Integer uid) {
		mvpId = uid;
	}

}