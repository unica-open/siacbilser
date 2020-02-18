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
 * The persistent class for the siac_r_movgest_ts_sogclasse_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sogclasse_mod")
@NamedQuery(name="SiacRMovgestTsSogclasseMod.findAll", query="SELECT s FROM SiacRMovgestTsSogclasseMod s")
public class SiacRMovgestTsSogclasseMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts sogclasse mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOGCLASSE_MOD_MOVGESTTSSOGCLASSEMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_TS_SOGCLASSE_MOD_MOVGEST_TS_SOGCLASSE_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOGCLASSE_MOD_MOVGESTTSSOGCLASSEMODID_GENERATOR")
	@Column(name="movgest_ts_sogclasse_mod_id")
	private Integer movgestTsSogclasseModId;



	//bi-directional many-to-one association to SiacDSoggettoClasse
	/** The siac d soggetto classe1. */
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id_new")
	private SiacDSoggettoClasse siacDSoggettoClasse1;

	//bi-directional many-to-one association to SiacDSoggettoClasse
	/** The siac d soggetto classe2. */
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id_old")
	private SiacDSoggettoClasse siacDSoggettoClasse2;

	//bi-directional many-to-one association to SiacRModificaStato
	/** The siac r modifica stato. */
	@ManyToOne
	@JoinColumn(name="mod_stato_r_id")
	private SiacRModificaStato siacRModificaStato;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasse
	/** The siac r movgest ts sogclasse. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_sogclasse_id")
	private SiacRMovgestTsSogclasse siacRMovgestTsSogclasse;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	/**
	 * Instantiates a new siac r movgest ts sogclasse mod.
	 */
	public SiacRMovgestTsSogclasseMod() {
	}

	/**
	 * Gets the movgest ts sogclasse mod id.
	 *
	 * @return the movgest ts sogclasse mod id
	 */
	public Integer getMovgestTsSogclasseModId() {
		return this.movgestTsSogclasseModId;
	}

	/**
	 * Sets the movgest ts sogclasse mod id.
	 *
	 * @param movgestTsSogclasseModId the new movgest ts sogclasse mod id
	 */
	public void setMovgestTsSogclasseModId(Integer movgestTsSogclasseModId) {
		this.movgestTsSogclasseModId = movgestTsSogclasseModId;
	}
	
	/**
	 * Gets the siac d soggetto classe1.
	 *
	 * @return the siac d soggetto classe1
	 */
	public SiacDSoggettoClasse getSiacDSoggettoClasse1() {
		return this.siacDSoggettoClasse1;
	}

	/**
	 * Sets the siac d soggetto classe1.
	 *
	 * @param siacDSoggettoClasse1 the new siac d soggetto classe1
	 */
	public void setSiacDSoggettoClasse1(SiacDSoggettoClasse siacDSoggettoClasse1) {
		this.siacDSoggettoClasse1 = siacDSoggettoClasse1;
	}

	/**
	 * Gets the siac d soggetto classe2.
	 *
	 * @return the siac d soggetto classe2
	 */
	public SiacDSoggettoClasse getSiacDSoggettoClasse2() {
		return this.siacDSoggettoClasse2;
	}

	/**
	 * Sets the siac d soggetto classe2.
	 *
	 * @param siacDSoggettoClasse2 the new siac d soggetto classe2
	 */
	public void setSiacDSoggettoClasse2(SiacDSoggettoClasse siacDSoggettoClasse2) {
		this.siacDSoggettoClasse2 = siacDSoggettoClasse2;
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
	 * Gets the siac r movgest ts sogclasse.
	 *
	 * @return the siac r movgest ts sogclasse
	 */
	public SiacRMovgestTsSogclasse getSiacRMovgestTsSogclasse() {
		return this.siacRMovgestTsSogclasse;
	}

	/**
	 * Sets the siac r movgest ts sogclasse.
	 *
	 * @param siacRMovgestTsSogclasse the new siac r movgest ts sogclasse
	 */
	public void setSiacRMovgestTsSogclasse(SiacRMovgestTsSogclasse siacRMovgestTsSogclasse) {
		this.siacRMovgestTsSogclasse = siacRMovgestTsSogclasse;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {		
		return movgestTsSogclasseModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movgestTsSogclasseModId = uid;
		
	}

}