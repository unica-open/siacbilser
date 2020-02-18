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
 * The persistent class for the siac_d_soggetto_classe database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_classe")
@NamedQuery(name="SiacDSoggettoClasse.findAll", query="SELECT s FROM SiacDSoggettoClasse s")
public class SiacDSoggettoClasse extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto classe id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SOGGETTO_CLASSE_SOGGETTOCLASSEID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SOGGETTO_CLASSE_SOGGETTO_CLASSE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SOGGETTO_CLASSE_SOGGETTOCLASSEID_GENERATOR")
	@Column(name="soggetto_classe_id")
	private Integer soggettoClasseId;

	/** The ambito id. */
	@Column(name="ambito_id")
	private Integer ambitoId;	

	/** The soggetto classe code. */
	@Column(name="soggetto_classe_code")
	private String soggettoClasseCode;

	/** The soggetto classe desc. */
	@Column(name="soggetto_classe_desc")
	private String soggettoClasseDesc;


	//bi-directional many-to-one association to SiacDSoggettoClasseTipo
	/** The siac d soggetto classe tipo. */
	@ManyToOne
	@JoinColumn(name="soggetto_classe_tipo_id")
	private SiacDSoggettoClasseTipo siacDSoggettoClasseTipo;



	//bi-directional many-to-one association to SiacRMovgestTsSogclasse
	/** The siac r movgest ts sogclasses. */
	@OneToMany(mappedBy="siacDSoggettoClasse")
	private List<SiacRMovgestTsSogclasse> siacRMovgestTsSogclasses;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseMod
	/** The siac r movgest ts sogclasse mods1. */
	@OneToMany(mappedBy="siacDSoggettoClasse1")
	private List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods1;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseMod
	/** The siac r movgest ts sogclasse mods2. */
	@OneToMany(mappedBy="siacDSoggettoClasse2")
	private List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods2;

	//bi-directional many-to-one association to SiacRSoggettoClasse
	/** The siac r soggetto classes. */
	@OneToMany(mappedBy="siacDSoggettoClasse")
	private List<SiacRSoggettoClasse> siacRSoggettoClasses;

	//bi-directional many-to-one association to SiacRSoggettoClasseMod
	/** The siac r soggetto classe mods. */
	@OneToMany(mappedBy="siacDSoggettoClasse")
	private List<SiacRSoggettoClasseMod> siacRSoggettoClasseMods;

	/**
	 * Instantiates a new siac d soggetto classe.
	 */
	public SiacDSoggettoClasse() {
	}

	/**
	 * Gets the soggetto classe id.
	 *
	 * @return the soggetto classe id
	 */
	public Integer getSoggettoClasseId() {
		return this.soggettoClasseId;
	}

	/**
	 * Sets the soggetto classe id.
	 *
	 * @param soggettoClasseId the new soggetto classe id
	 */
	public void setSoggettoClasseId(Integer soggettoClasseId) {
		this.soggettoClasseId = soggettoClasseId;
	}

	/**
	 * Gets the ambito id.
	 *
	 * @return the ambito id
	 */
	public Integer getAmbitoId() {
		return this.ambitoId;
	}

	/**
	 * Sets the ambito id.
	 *
	 * @param ambitoId the new ambito id
	 */
	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	/**
	 * Gets the soggetto classe code.
	 *
	 * @return the soggetto classe code
	 */
	public String getSoggettoClasseCode() {
		return this.soggettoClasseCode;
	}

	/**
	 * Sets the soggetto classe code.
	 *
	 * @param soggettoClasseCode the new soggetto classe code
	 */
	public void setSoggettoClasseCode(String soggettoClasseCode) {
		this.soggettoClasseCode = soggettoClasseCode;
	}

	/**
	 * Gets the soggetto classe desc.
	 *
	 * @return the soggetto classe desc
	 */
	public String getSoggettoClasseDesc() {
		return this.soggettoClasseDesc;
	}

	/**
	 * Sets the soggetto classe desc.
	 *
	 * @param soggettoClasseDesc the new soggetto classe desc
	 */
	public void setSoggettoClasseDesc(String soggettoClasseDesc) {
		this.soggettoClasseDesc = soggettoClasseDesc;
	}

	/**
	 * Gets the siac d soggetto classe tipo.
	 *
	 * @return the siac d soggetto classe tipo
	 */
	public SiacDSoggettoClasseTipo getSiacDSoggettoClasseTipo() {
		return this.siacDSoggettoClasseTipo;
	}

	/**
	 * Sets the siac d soggetto classe tipo.
	 *
	 * @param siacDSoggettoClasseTipo the new siac d soggetto classe tipo
	 */
	public void setSiacDSoggettoClasseTipo(SiacDSoggettoClasseTipo siacDSoggettoClasseTipo) {
		this.siacDSoggettoClasseTipo = siacDSoggettoClasseTipo;
	}



	/**
	 * Gets the siac r movgest ts sogclasses.
	 *
	 * @return the siac r movgest ts sogclasses
	 */
	public List<SiacRMovgestTsSogclasse> getSiacRMovgestTsSogclasses() {
		return this.siacRMovgestTsSogclasses;
	}

	/**
	 * Sets the siac r movgest ts sogclasses.
	 *
	 * @param siacRMovgestTsSogclasses the new siac r movgest ts sogclasses
	 */
	public void setSiacRMovgestTsSogclasses(List<SiacRMovgestTsSogclasse> siacRMovgestTsSogclasses) {
		this.siacRMovgestTsSogclasses = siacRMovgestTsSogclasses;
	}

	/**
	 * Adds the siac r movgest ts sogclass.
	 *
	 * @param siacRMovgestTsSogclass the siac r movgest ts sogclass
	 * @return the siac r movgest ts sogclasse
	 */
	public SiacRMovgestTsSogclasse addSiacRMovgestTsSogclass(SiacRMovgestTsSogclasse siacRMovgestTsSogclass) {
		getSiacRMovgestTsSogclasses().add(siacRMovgestTsSogclass);
		siacRMovgestTsSogclass.setSiacDSoggettoClasse(this);

		return siacRMovgestTsSogclass;
	}

	/**
	 * Removes the siac r movgest ts sogclass.
	 *
	 * @param siacRMovgestTsSogclass the siac r movgest ts sogclass
	 * @return the siac r movgest ts sogclasse
	 */
	public SiacRMovgestTsSogclasse removeSiacRMovgestTsSogclass(SiacRMovgestTsSogclasse siacRMovgestTsSogclass) {
		getSiacRMovgestTsSogclasses().remove(siacRMovgestTsSogclass);
		siacRMovgestTsSogclass.setSiacDSoggettoClasse(null);

		return siacRMovgestTsSogclass;
	}

	/**
	 * Gets the siac r movgest ts sogclasse mods1.
	 *
	 * @return the siac r movgest ts sogclasse mods1
	 */
	public List<SiacRMovgestTsSogclasseMod> getSiacRMovgestTsSogclasseMods1() {
		return this.siacRMovgestTsSogclasseMods1;
	}

	/**
	 * Sets the siac r movgest ts sogclasse mods1.
	 *
	 * @param siacRMovgestTsSogclasseMods1 the new siac r movgest ts sogclasse mods1
	 */
	public void setSiacRMovgestTsSogclasseMods1(List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods1) {
		this.siacRMovgestTsSogclasseMods1 = siacRMovgestTsSogclasseMods1;
	}

	/**
	 * Adds the siac r movgest ts sogclasse mods1.
	 *
	 * @param siacRMovgestTsSogclasseMods1 the siac r movgest ts sogclasse mods1
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod addSiacRMovgestTsSogclasseMods1(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMods1) {
		getSiacRMovgestTsSogclasseMods1().add(siacRMovgestTsSogclasseMods1);
		siacRMovgestTsSogclasseMods1.setSiacDSoggettoClasse1(this);

		return siacRMovgestTsSogclasseMods1;
	}

	/**
	 * Removes the siac r movgest ts sogclasse mods1.
	 *
	 * @param siacRMovgestTsSogclasseMods1 the siac r movgest ts sogclasse mods1
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod removeSiacRMovgestTsSogclasseMods1(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMods1) {
		getSiacRMovgestTsSogclasseMods1().remove(siacRMovgestTsSogclasseMods1);
		siacRMovgestTsSogclasseMods1.setSiacDSoggettoClasse1(null);

		return siacRMovgestTsSogclasseMods1;
	}

	/**
	 * Gets the siac r movgest ts sogclasse mods2.
	 *
	 * @return the siac r movgest ts sogclasse mods2
	 */
	public List<SiacRMovgestTsSogclasseMod> getSiacRMovgestTsSogclasseMods2() {
		return this.siacRMovgestTsSogclasseMods2;
	}

	/**
	 * Sets the siac r movgest ts sogclasse mods2.
	 *
	 * @param siacRMovgestTsSogclasseMods2 the new siac r movgest ts sogclasse mods2
	 */
	public void setSiacRMovgestTsSogclasseMods2(List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods2) {
		this.siacRMovgestTsSogclasseMods2 = siacRMovgestTsSogclasseMods2;
	}

	/**
	 * Adds the siac r movgest ts sogclasse mods2.
	 *
	 * @param siacRMovgestTsSogclasseMods2 the siac r movgest ts sogclasse mods2
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod addSiacRMovgestTsSogclasseMods2(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMods2) {
		getSiacRMovgestTsSogclasseMods2().add(siacRMovgestTsSogclasseMods2);
		siacRMovgestTsSogclasseMods2.setSiacDSoggettoClasse2(this);

		return siacRMovgestTsSogclasseMods2;
	}

	/**
	 * Removes the siac r movgest ts sogclasse mods2.
	 *
	 * @param siacRMovgestTsSogclasseMods2 the siac r movgest ts sogclasse mods2
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod removeSiacRMovgestTsSogclasseMods2(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMods2) {
		getSiacRMovgestTsSogclasseMods2().remove(siacRMovgestTsSogclasseMods2);
		siacRMovgestTsSogclasseMods2.setSiacDSoggettoClasse2(null);

		return siacRMovgestTsSogclasseMods2;
	}

	/**
	 * Gets the siac r soggetto classes.
	 *
	 * @return the siac r soggetto classes
	 */
	public List<SiacRSoggettoClasse> getSiacRSoggettoClasses() {
		return this.siacRSoggettoClasses;
	}

	/**
	 * Sets the siac r soggetto classes.
	 *
	 * @param siacRSoggettoClasses the new siac r soggetto classes
	 */
	public void setSiacRSoggettoClasses(List<SiacRSoggettoClasse> siacRSoggettoClasses) {
		this.siacRSoggettoClasses = siacRSoggettoClasses;
	}

	/**
	 * Adds the siac r soggetto class.
	 *
	 * @param siacRSoggettoClass the siac r soggetto class
	 * @return the siac r soggetto classe
	 */
	public SiacRSoggettoClasse addSiacRSoggettoClass(SiacRSoggettoClasse siacRSoggettoClass) {
		getSiacRSoggettoClasses().add(siacRSoggettoClass);
		siacRSoggettoClass.setSiacDSoggettoClasse(this);

		return siacRSoggettoClass;
	}

	/**
	 * Removes the siac r soggetto class.
	 *
	 * @param siacRSoggettoClass the siac r soggetto class
	 * @return the siac r soggetto classe
	 */
	public SiacRSoggettoClasse removeSiacRSoggettoClass(SiacRSoggettoClasse siacRSoggettoClass) {
		getSiacRSoggettoClasses().remove(siacRSoggettoClass);
		siacRSoggettoClass.setSiacDSoggettoClasse(null);

		return siacRSoggettoClass;
	}

	/**
	 * Gets the siac r soggetto classe mods.
	 *
	 * @return the siac r soggetto classe mods
	 */
	public List<SiacRSoggettoClasseMod> getSiacRSoggettoClasseMods() {
		return this.siacRSoggettoClasseMods;
	}

	/**
	 * Sets the siac r soggetto classe mods.
	 *
	 * @param siacRSoggettoClasseMods the new siac r soggetto classe mods
	 */
	public void setSiacRSoggettoClasseMods(List<SiacRSoggettoClasseMod> siacRSoggettoClasseMods) {
		this.siacRSoggettoClasseMods = siacRSoggettoClasseMods;
	}

	/**
	 * Adds the siac r soggetto classe mod.
	 *
	 * @param siacRSoggettoClasseMod the siac r soggetto classe mod
	 * @return the siac r soggetto classe mod
	 */
	public SiacRSoggettoClasseMod addSiacRSoggettoClasseMod(SiacRSoggettoClasseMod siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().add(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacDSoggettoClasse(this);

		return siacRSoggettoClasseMod;
	}

	/**
	 * Removes the siac r soggetto classe mod.
	 *
	 * @param siacRSoggettoClasseMod the siac r soggetto classe mod
	 * @return the siac r soggetto classe mod
	 */
	public SiacRSoggettoClasseMod removeSiacRSoggettoClasseMod(SiacRSoggettoClasseMod siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().remove(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacDSoggettoClasse(null);

		return siacRSoggettoClasseMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoClasseId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoClasseId = uid;
		
	}

}