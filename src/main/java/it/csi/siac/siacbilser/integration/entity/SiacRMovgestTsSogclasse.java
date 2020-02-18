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
 * The persistent class for the siac_r_movgest_ts_sogclasse database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sogclasse")
@NamedQuery(name="SiacRMovgestTsSogclasse.findAll", query="SELECT s FROM SiacRMovgestTsSogclasse s")
public class SiacRMovgestTsSogclasse extends SiacTEnteBase {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts sogclasse id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOGCLASSE_MOVGESTTSSOGCLASSEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_TS_SOGCLASSE_MOVGEST_TS_SOGCLASSE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOGCLASSE_MOVGESTTSSOGCLASSEID_GENERATOR")
	@Column(name="movgest_ts_sogclasse_id")
	private Integer movgestTsSogclasseId;

	

	//bi-directional many-to-one association to SiacDSoggettoClasse
	/** The siac d soggetto classe. */
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id")
	private SiacDSoggettoClasse siacDSoggettoClasse;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseMod
	/** The siac r movgest ts sogclasse mods. */
	@OneToMany(mappedBy="siacRMovgestTsSogclasse")
	private List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods;

	/**
	 * Instantiates a new siac r movgest ts sogclasse.
	 */
	public SiacRMovgestTsSogclasse() {
	}

	/**
	 * Gets the movgest ts sogclasse id.
	 *
	 * @return the movgest ts sogclasse id
	 */
	public Integer getMovgestTsSogclasseId() {
		return this.movgestTsSogclasseId;
	}

	/**
	 * Sets the movgest ts sogclasse id.
	 *
	 * @param movgestTsSogclasseId the new movgest ts sogclasse id
	 */
	public void setMovgestTsSogclasseId(Integer movgestTsSogclasseId) {
		this.movgestTsSogclasseId = movgestTsSogclasseId;
	}

	/**
	 * Gets the siac d soggetto classe.
	 *
	 * @return the siac d soggetto classe
	 */
	public SiacDSoggettoClasse getSiacDSoggettoClasse() {
		return this.siacDSoggettoClasse;
	}

	/**
	 * Sets the siac d soggetto classe.
	 *
	 * @param siacDSoggettoClasse the new siac d soggetto classe
	 */
	public void setSiacDSoggettoClasse(SiacDSoggettoClasse siacDSoggettoClasse) {
		this.siacDSoggettoClasse = siacDSoggettoClasse;
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
	 * Gets the siac r movgest ts sogclasse mods.
	 *
	 * @return the siac r movgest ts sogclasse mods
	 */
	public List<SiacRMovgestTsSogclasseMod> getSiacRMovgestTsSogclasseMods() {
		return this.siacRMovgestTsSogclasseMods;
	}

	/**
	 * Sets the siac r movgest ts sogclasse mods.
	 *
	 * @param siacRMovgestTsSogclasseMods the new siac r movgest ts sogclasse mods
	 */
	public void setSiacRMovgestTsSogclasseMods(List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods) {
		this.siacRMovgestTsSogclasseMods = siacRMovgestTsSogclasseMods;
	}

	/**
	 * Adds the siac r movgest ts sogclasse mod.
	 *
	 * @param siacRMovgestTsSogclasseMod the siac r movgest ts sogclasse mod
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod addSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().add(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRMovgestTsSogclasse(this);

		return siacRMovgestTsSogclasseMod;
	}

	/**
	 * Removes the siac r movgest ts sogclasse mod.
	 *
	 * @param siacRMovgestTsSogclasseMod the siac r movgest ts sogclasse mod
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod removeSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().remove(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRMovgestTsSogclasse(null);

		return siacRMovgestTsSogclasseMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsSogclasseId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movgestTsSogclasseId = uid;
		
	}

}