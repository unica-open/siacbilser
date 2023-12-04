/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_movgest_ts_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sog")
@NamedQuery(name="SiacRMovgestTsSog.findAll", query="SELECT s FROM SiacRMovgestTsSog s")
public class SiacRMovgestTsSog extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts sog id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOG_MOVGESTTSSOGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_TS_SOG_MOVGEST_TS_SOG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOG_MOVGESTTSSOGID_GENERATOR")
	@Column(name="movgest_ts_sog_id")
	private Integer movgestTsSogId;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	//bi-directional many-to-one association to SiacRMovgestTsSogMod
	@OneToMany(mappedBy="siacRMovgestTsSog")
	private List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods;

	/**
 	 * Instantiates a new siac r movgest ts sog.
 	 */
	public SiacRMovgestTsSog() {
	}

	/**
	 * Gets the movgest ts sog id.
	 *
	 * @return the movgest ts sog id
	 */
	public Integer getMovgestTsSogId() {
		return this.movgestTsSogId;
	}

	/**
	 * Sets the movgest ts sog id.
	 *
	 * @param movgestTsSogId the new movgest ts sog id
	 */
	public void setMovgestTsSogId(Integer movgestTsSogId) {
		this.movgestTsSogId = movgestTsSogId;
	}

	

	

	/**
	 * Gets the siac t movgest t.
	 *
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	/**
	 * Sets the siac t movgest t.
	 *
	 * @param siacTMovgestT the new siac t movgest t
	 */
	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public List<SiacRMovgestTsSogMod> getSiacRMovgestTsSogMods() {
		return this.siacRMovgestTsSogMods;
	}

	public void setSiacRMovgestTsSogMods(List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods) {
		this.siacRMovgestTsSogMods = siacRMovgestTsSogMods;
	}

	public SiacRMovgestTsSogMod addSiacRMovgestTsSogMod(SiacRMovgestTsSogMod siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().add(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRMovgestTsSog(this);

		return siacRMovgestTsSogMod;
	}

	public SiacRMovgestTsSogMod removeSiacRMovgestTsSogMod(SiacRMovgestTsSogMod siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().remove(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRMovgestTsSog(null);

		return siacRMovgestTsSogMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsSogId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movgestTsSogId = uid;
		
	}

}