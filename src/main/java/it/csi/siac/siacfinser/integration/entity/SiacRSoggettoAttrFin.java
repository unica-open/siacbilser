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


/**
 * The persistent class for the siac_r_soggetto_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_attr")
public class SiacRSoggettoAttrFin extends SiacRAttrBaseFin {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_ATTR_SOGGETTO_ATTR_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_attr_soggetto_attr_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_ATTR_SOGGETTO_ATTR_ID_GENERATOR")
	@Column(name="soggetto_attr_id")
	private Integer soggettoAttrId;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRSoggettoAttrFin() {
	}

	public Integer getSoggettoAttrId() {
		return this.soggettoAttrId;
	}

	public void setSoggettoAttrId(Integer soggettoAttrId) {
		this.soggettoAttrId = soggettoAttrId;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoAttrId = uid;
	}
}