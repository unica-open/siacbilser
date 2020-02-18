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
 * The persistent class for the siac_d_ambito database table.
 * 
 */
@Entity
@Table(name="siac_d_ambito")
public class SiacDAmbitoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ambito_id")
	private Integer ambitoId;

	@Column(name="ambito_code")
	private String ambitoCode;

	@Column(name="ambito_desc")
	private String ambitoDesc;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTSoggettoFin> siacTSoggettos;

	public SiacDAmbitoFin() {
	}

	public Integer getAmbitoId() {
		return this.ambitoId;
	}

	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	public String getAmbitoCode() {
		return this.ambitoCode;
	}

	public void setAmbitoCode(String ambitoCode) {
		this.ambitoCode = ambitoCode;
	}

	public String getAmbitoDesc() {
		return this.ambitoDesc;
	}

	public void setAmbitoDesc(String ambitoDesc) {
		this.ambitoDesc = ambitoDesc;
	}

	public List<SiacTSoggettoFin> getSiacTSoggettos() {
		return this.siacTSoggettos;
	}

	public void setSiacTSoggettos(List<SiacTSoggettoFin> siacTSoggettos) {
		this.siacTSoggettos = siacTSoggettos;
	}

	public SiacTSoggettoFin addSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		getSiacTSoggettos().add(siacTSoggetto);
		siacTSoggetto.setSiacDAmbito(this);

		return siacTSoggetto;
	}

	public SiacTSoggettoFin removeSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		getSiacTSoggettos().remove(siacTSoggetto);
		siacTSoggetto.setSiacDAmbito(null);

		return siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ambitoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ambitoId = uid;
	}
}