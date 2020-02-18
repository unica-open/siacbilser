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
 * The persistent class for the siac_r_mutuo_voce_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_voce_subdoc")
@NamedQuery(name="SiacRMutuoVoceSubdoc.findAll", query="SELECT s FROM SiacRMutuoVoceSubdoc s")
public class SiacRMutuoVoceSubdoc extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_VOCE_SUBDOC_MVSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_VOCE_SUBDOC_MVS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_VOCE_SUBDOC_MVSID_GENERATOR")
	@Column(name="mvs_id")
	private Integer mvsId;

	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoce siacTMutuoVoce;
	
	//bi-directional many-to-one association to SiacTSubdoc
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	public SiacRMutuoVoceSubdoc() {
	}

	public Integer getMvsId() {
		return mvsId;
	}

	public void setMvsId(Integer mvsId) {
		this.mvsId = mvsId;
	}

	public SiacTMutuoVoce getSiacTMutuoVoce() {
		return siacTMutuoVoce;
	}

	public void setSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		return mvsId;
	}

	@Override
	public void setUid(Integer uid) {
		mvsId = uid;
	}

}