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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_movgest_ts_sog_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sog_mod")
@NamedQuery(name="SiacRMovgestTsSogMod.findAll", query="SELECT s FROM SiacRMovgestTsSogMod s")
public class SiacRMovgestTsSogMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts sog mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOG_MOD_MOVGESTTSSOGMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_TS_SOG_MOD_MOVGEST_TS_SOG_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOG_MOD_MOVGESTTSSOGMODID_GENERATOR")
	@Column(name="movgest_ts_sog_mod_id")
	private Integer movgestTsSogModId;

	//bi-directional many-to-one association to SiacRModificaStato
	/** The siac r modifica stato. */
	@ManyToOne
	@JoinColumn(name="mod_stato_r_id")
	private SiacRModificaStato siacRModificaStato;

	//bi-directional many-to-one association to SiacRMovgestTsSog
	/** The siac r movgest ts sog. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_sog_id")
	private SiacRMovgestTsSog siacRMovgestTsSog;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto1. */
	@ManyToOne
	@JoinColumn(name="soggetto_id_old")
	private SiacTSoggetto siacTSoggetto1;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto2. */
	@ManyToOne
	@JoinColumn(name="soggetto_id_new")
	private SiacTSoggetto siacTSoggetto2;

	/**
	 * Instantiates a new siac r movgest ts sog mod.
	 */
	public SiacRMovgestTsSogMod() {
	}

	/**
	 * Gets the movgest ts sog mod id.
	 *
	 * @return the movgest ts sog mod id
	 */
	public Integer getMovgestTsSogModId() {
		return this.movgestTsSogModId;
	}

	/**
	 * Sets the movgest ts sog mod id.
	 *
	 * @param movgestTsSogModId the new movgest ts sog mod id
	 */
	public void setMovgestTsSogModId(Integer movgestTsSogModId) {
		this.movgestTsSogModId = movgestTsSogModId;
	}

	/**
	 * Gets the siac r modifica stato.
	 *
	 * @return the siac r modifica stato
	 */
	public SiacRModificaStato getSiacRModificaStato() {
		return this.siacRModificaStato;
	}

	/**
	 * Sets the siac r modifica stato.
	 *
	 * @param siacRModificaStato the new siac r modifica stato
	 */
	public void setSiacRModificaStato(SiacRModificaStato siacRModificaStato) {
		this.siacRModificaStato = siacRModificaStato;
	}

	/**
	 * Gets the siac r movgest ts sog.
	 *
	 * @return the siac r movgest ts sog
	 */
	public SiacRMovgestTsSog getSiacRMovgestTsSog() {
		return this.siacRMovgestTsSog;
	}

	/**
	 * Sets the siac r movgest ts sog.
	 *
	 * @param siacRMovgestTsSog the new siac r movgest ts sog
	 */
	public void setSiacRMovgestTsSog(SiacRMovgestTsSog siacRMovgestTsSog) {
		this.siacRMovgestTsSog = siacRMovgestTsSog;
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
	 * Gets the siac t soggetto1.
	 *
	 * @return the siac t soggetto1
	 */
	public SiacTSoggetto getSiacTSoggetto1() {
		return this.siacTSoggetto1;
	}

	/**
	 * Sets the siac t soggetto1.
	 *
	 * @param siacTSoggetto1 the new siac t soggetto1
	 */
	public void setSiacTSoggetto1(SiacTSoggetto siacTSoggetto1) {
		this.siacTSoggetto1 = siacTSoggetto1;
	}

	/**
	 * Gets the siac t soggetto2.
	 *
	 * @return the siac t soggetto2
	 */
	public SiacTSoggetto getSiacTSoggetto2() {
		return this.siacTSoggetto2;
	}

	/**
	 * Sets the siac t soggetto2.
	 *
	 * @param siacTSoggetto2 the new siac t soggetto2
	 */
	public void setSiacTSoggetto2(SiacTSoggetto siacTSoggetto2) {
		this.siacTSoggetto2 = siacTSoggetto2;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsSogModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movgestTsSogModId = uid;
		
	}

}