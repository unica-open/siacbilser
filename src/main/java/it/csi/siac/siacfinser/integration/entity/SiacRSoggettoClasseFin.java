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
 * The persistent class for the siac_r_soggetto_classe database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_classe")
public class SiacRSoggettoClasseFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_CLASSE_SOGGETTO_CLASSE_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_classe_soggetto_classe_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_CLASSE_SOGGETTO_CLASSE_ID_GENERATOR")
	@Column(name="soggetto_classe_r_id")
	private Integer soggettoClasseRId;

	//bi-directional many-to-one association to SiacDSoggettoClasseFin
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id")
	private SiacDSoggettoClasseFin siacDSoggettoClasse;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRSoggettoClasseFin() {
	}

	public Integer getSoggettoClasseRId() {
		return this.soggettoClasseRId;
	}

	public void setSoggettoClasseRId(Integer soggettoClasseRId) {
		this.soggettoClasseRId = soggettoClasseRId;
	}

	public SiacDSoggettoClasseFin getSiacDSoggettoClasse() {
		return this.siacDSoggettoClasse;
	}

	public void setSiacDSoggettoClasse(SiacDSoggettoClasseFin siacDSoggettoClasse) {
		this.siacDSoggettoClasse = siacDSoggettoClasse;
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
		return this.soggettoClasseRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoClasseRId = uid;
	}
}