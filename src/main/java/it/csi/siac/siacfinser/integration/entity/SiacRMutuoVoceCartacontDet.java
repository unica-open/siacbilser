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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_mutuo_voce_cartacont_det database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_voce_cartacont_det")
@NamedQuery(name="SiacRMutuoVoceCartacontDet.findAll", query="SELECT s FROM SiacRMutuoVoceCartacontDet s")
public class SiacRMutuoVoceCartacontDet extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mvct_id id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_VOCE_CARTACONT_DET_MVCT_ID_SEQ_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_VOCE_CARTACONT_DET_MVCT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_VOCE_CARTACONT_DET_MVCT_ID_SEQ_GENERATOR")
	@Column(name="mvct_id")
	private Integer mvctId;


	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDetFin siacTCartacontDet;

	//bi-directional many-to-one association to SiacTMutuoVoce
	/** The siac t mutuo voce. */
	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoceFin siacTMutuoVoce;

	/**
	 * Instantiates a new siac r mutuo voce carta cont det.
	 */
	public SiacRMutuoVoceCartacontDet() {
	}


	public Integer getMvctId() {
		return mvctId;
	}

	public void setMvctId(Integer mvctId) {
		this.mvctId = mvctId;
	}

	public SiacTCartacontDetFin getSiacTCartacontDet() {
		return siacTCartacontDet;
	}


	public void setSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}


	public SiacTMutuoVoceFin getSiacTMutuoVoce() {
		return siacTMutuoVoce;
	}


	public void setSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mvctId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mvctId = uid;
		
	}

}