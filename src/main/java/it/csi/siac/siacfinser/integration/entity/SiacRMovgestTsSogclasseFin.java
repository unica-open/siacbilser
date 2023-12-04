/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_movgest_ts_sogclasse database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sogclasse")
public class SiacRMovgestTsSogclasseFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOGCLASSE_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_sogclasse_movgest_ts_sogclasse_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOGCLASSE_ID_GENERATOR")	
	@Column(name="movgest_ts_sogclasse_id")
	private Integer movgestTsSogclasseId;

	//bi-directional many-to-one association to SiacDSoggettoClasseFin
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id")
	private SiacDSoggettoClasseFin siacDSoggettoClasse;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseModFin
	@OneToMany(mappedBy="siacRMovgestTsSogclasse")
	private List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseMods;

	public SiacRMovgestTsSogclasseFin() {
	}

	public Integer getMovgestTsSogclasseId() {
		return this.movgestTsSogclasseId;
	}

	public void setMovgestTsSogclasseId(Integer movgestTsSogclasseId) {
		this.movgestTsSogclasseId = movgestTsSogclasseId;
	}

	public SiacDSoggettoClasseFin getSiacDSoggettoClasse() {
		return this.siacDSoggettoClasse;
	}

	public void setSiacDSoggettoClasse(SiacDSoggettoClasseFin siacDSoggettoClasse) {
		this.siacDSoggettoClasse = siacDSoggettoClasse;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public List<SiacRMovgestTsSogclasseModFin> getSiacRMovgestTsSogclasseMods() {
		return this.siacRMovgestTsSogclasseMods;
	}

	public void setSiacRMovgestTsSogclasseMods(List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseMods) {
		this.siacRMovgestTsSogclasseMods = siacRMovgestTsSogclasseMods;
	}

	public SiacRMovgestTsSogclasseModFin addSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().add(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRMovgestTsSogclasse(this);

		return siacRMovgestTsSogclasseMod;
	}

	public SiacRMovgestTsSogclasseModFin removeSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().remove(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRMovgestTsSogclasse(null);

		return siacRMovgestTsSogclasseMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsSogclasseId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsSogclasseId = uid;
	}
}