/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_nazione database table.
 * 
 */
@Entity
@Table(name="siac_t_nazione")
public class SiacTNazioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="nazione_id")
	private Integer nazioneId;

	@Column(name="nazione_code")
	private String nazioneCode;

	@Column(name="nazione_desc")
	private String nazioneDesc;

	//bi-directional many-to-one association to SiacTComuneFin
	@OneToMany(mappedBy="siacTNazione")
	private List<SiacTComuneFin> siacTComunes;

	public SiacTNazioneFin() {
	}

	public Integer getNazioneId() {
		return this.nazioneId;
	}

	public void setNazioneId(Integer nazioneId) {
		this.nazioneId = nazioneId;
	}

	public String getNazioneCode() {
		return this.nazioneCode;
	}

	public void setNazioneCode(String nazioneCode) {
		this.nazioneCode = nazioneCode;
	}

	public String getNazioneDesc() {
		return this.nazioneDesc;
	}

	public void setNazioneDesc(String nazioneDesc) {
		this.nazioneDesc = nazioneDesc;
	}

	public List<SiacTComuneFin> getSiacTComunes() {
		return this.siacTComunes;
	}

	public void setSiacTComunes(List<SiacTComuneFin> siacTComunes) {
		this.siacTComunes = siacTComunes;
	}

	public SiacTComuneFin addSiacTComune(SiacTComuneFin siacTComune) {
		getSiacTComunes().add(siacTComune);
		siacTComune.setSiacTNazione(this);

		return siacTComune;
	}

	public SiacTComuneFin removeSiacTComune(SiacTComuneFin siacTComune) {
		getSiacTComunes().remove(siacTComune);
		siacTComune.setSiacTNazione(null);

		return siacTComune;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.nazioneId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.nazioneId = uid;
	}
}