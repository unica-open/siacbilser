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
 * The persistent class for the siac_r_movgest_ts_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sog")
public class SiacRMovgestTsSogFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOG_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_sog_movgest_ts_sog_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOG_ID_GENERATOR")
	@Column(name="movgest_ts_sog_id")
	private Integer movgestTsSogId;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacRMovgestTsSogModFin
	@OneToMany(mappedBy="siacRMovgestTsSog")
	private List<SiacRMovgestTsSogModFin> siacRMovgestTsSogMods;
	
	public SiacRMovgestTsSogFin() {
	}

	public Integer getMovgestTsSogId() {
		return this.movgestTsSogId;
	}

	public void setMovgestTsSogId(Integer movgestTsSogId) {
		this.movgestTsSogId = movgestTsSogId;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public List<SiacRMovgestTsSogModFin> getSiacRMovgestTsSogMods() {
		return this.siacRMovgestTsSogMods;
	}

	public void setSiacRMovgestTsSogMods(List<SiacRMovgestTsSogModFin> siacRMovgestTsSogMods) {
		this.siacRMovgestTsSogMods = siacRMovgestTsSogMods;
	}

	public SiacRMovgestTsSogModFin addSiacRMovgestTsSogMod(SiacRMovgestTsSogModFin siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().add(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRMovgestTsSog(this);

		return siacRMovgestTsSogMod;
	}

	public SiacRMovgestTsSogModFin removeSiacRMovgestTsSogMod(SiacRMovgestTsSogModFin siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().remove(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRMovgestTsSog(null);

		return siacRMovgestTsSogMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsSogId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsSogId = uid;
	}
}