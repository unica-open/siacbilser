/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDModelloEnum;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_causale database table.
 * 
 */
@Entity
@Table(name="siac_d_causale")
@NamedQuery(name="SiacDCausale.findAll", query="SELECT s FROM SiacDCausale s")
public class SiacDCausale extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CAUSALE_CAUSID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CAUSALE_CAUS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CAUSALE_CAUSID_GENERATOR")
	@Column(name="caus_id")
	private Integer causId;

	/** The caus code. */
	@Column(name="caus_code")
	private String causCode;

	/** The caus desc. */
	@Column(name="caus_desc")
	private String causDesc;
	
	/** The date to extract. */
	@Transient
	private Date dateToExtract;

	//bi-directional many-to-one association to SiacDModello
	/** The siac d modello. */
	@ManyToOne
	@JoinColumn(name="model_id")
	private SiacDModello siacDModello;

	//bi-directional many-to-one association to SiacRCausaleAttoAmm
	/** The siac r causale atto amms. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleAttoAmm> siacRCausaleAttoAmms;

	//bi-directional many-to-one association to SiacRCausaleBilElem
	/** The siac r causale bil elems. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleBilElem> siacRCausaleBilElems;

	//bi-directional many-to-one association to SiacRCausaleClass
	/** The siac r causale classes. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleClass> siacRCausaleClasses;

	//bi-directional many-to-one association to SiacRCausaleModpag
	/** The siac r causale modpags. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleModpag> siacRCausaleModpags;

	//bi-directional many-to-one association to SiacRCausaleMovgestT
	/** The siac r causale movgest ts. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleMovgestT> siacRCausaleMovgestTs;

	//bi-directional many-to-one association to SiacRCausaleSoggetto
	/** The siac r causale soggettos. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleSoggetto> siacRCausaleSoggettos;

	//bi-directional many-to-one association to SiacRCausaleTipo
	/** The siac r causale tipos. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleTipo> siacRCausaleTipos;

	//bi-directional many-to-one association to SiacRDocOnere
	/** The siac r doc oneres. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDocOnere> siacRDocOneres;

	//bi-directional many-to-one association to SiacROnereCausale
	/** The siac r onere causales. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacROnereCausale> siacROnereCausales;

	//bi-directional many-to-one association to SiacRPredocCausale
	/** The siac r predoc causales. */
	@OneToMany(mappedBy="siacDCausale", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocCausale> siacRPredocCausales;
	
	//bi-directional many-to-one association to SiacDModello
		/** The siac d modello. */
	@ManyToOne
	@JoinColumn(name="dist_id")
	private SiacDDistinta siacDDistinta;

	@Transient
	private boolean onCascade;

	/**
	 * Instantiates a new siac d causale.
	 */
	public SiacDCausale() {
	}

	/**
	 * Gets the caus id.
	 *
	 * @return the caus id
	 */
	public Integer getCausId() {
		return this.causId;
	}

	/**
	 * Sets the caus id.
	 *
	 * @param causId the new caus id
	 */
	public void setCausId(Integer causId) {
		this.causId = causId;
	}

	/**
	 * Gets the caus code.
	 *
	 * @return the caus code
	 */
	public String getCausCode() {
		return this.causCode;
	}

	/**
	 * Sets the caus code.
	 *
	 * @param causCode the new caus code
	 */
	public void setCausCode(String causCode) {
		this.causCode = causCode;
	}

	/**
	 * Gets the caus desc.
	 *
	 * @return the caus desc
	 */
	public String getCausDesc() {
		return this.causDesc;
	}

	/**
	 * Sets the caus desc.
	 *
	 * @param causDesc the new caus desc
	 */
	public void setCausDesc(String causDesc) {
		this.causDesc = causDesc;
	}

	/**
	 * Gets the siac d modello.
	 *
	 * @return the siac d modello
	 */
	public SiacDModello getSiacDModello() {
		return this.siacDModello;
	}

	/**
	 * Sets the siac d modello.
	 *
	 * @param siacDModello the new siac d modello
	 */
	public void setSiacDModello(SiacDModello siacDModello) {
		this.siacDModello = siacDModello;
	}

	/**
	 * Gets the siac r causale atto amms.
	 *
	 * @return the siac r causale atto amms
	 */
	public List<SiacRCausaleAttoAmm> getSiacRCausaleAttoAmms() {
		return this.siacRCausaleAttoAmms;
	}

	/**
	 * Sets the siac r causale atto amms.
	 *
	 * @param siacRCausaleAttoAmms the new siac r causale atto amms
	 */
	public void setSiacRCausaleAttoAmms(List<SiacRCausaleAttoAmm> siacRCausaleAttoAmms) {
		this.siacRCausaleAttoAmms = siacRCausaleAttoAmms;
	}

	/**
	 * Adds the siac r causale atto amm.
	 *
	 * @param siacRCausaleAttoAmm the siac r causale atto amm
	 * @return the siac r causale atto amm
	 */
	public SiacRCausaleAttoAmm addSiacRCausaleAttoAmm(SiacRCausaleAttoAmm siacRCausaleAttoAmm) {
		getSiacRCausaleAttoAmms().add(siacRCausaleAttoAmm);
		siacRCausaleAttoAmm.setSiacDCausale(this);

		return siacRCausaleAttoAmm;
	}

	/**
	 * Removes the siac r causale atto amm.
	 *
	 * @param siacRCausaleAttoAmm the siac r causale atto amm
	 * @return the siac r causale atto amm
	 */
	public SiacRCausaleAttoAmm removeSiacRCausaleAttoAmm(SiacRCausaleAttoAmm siacRCausaleAttoAmm) {
		getSiacRCausaleAttoAmms().remove(siacRCausaleAttoAmm);
		siacRCausaleAttoAmm.setSiacDCausale(null);

		return siacRCausaleAttoAmm;
	}

	/**
	 * Gets the siac r causale bil elems.
	 *
	 * @return the siac r causale bil elems
	 */
	public List<SiacRCausaleBilElem> getSiacRCausaleBilElems() {
		return this.siacRCausaleBilElems;
	}

	/**
	 * Sets the siac r causale bil elems.
	 *
	 * @param siacRCausaleBilElems the new siac r causale bil elems
	 */
	public void setSiacRCausaleBilElems(List<SiacRCausaleBilElem> siacRCausaleBilElems) {
		this.siacRCausaleBilElems = siacRCausaleBilElems;
	}

	/**
	 * Adds the siac r causale bil elem.
	 *
	 * @param siacRCausaleBilElem the siac r causale bil elem
	 * @return the siac r causale bil elem
	 */
	public SiacRCausaleBilElem addSiacRCausaleBilElem(SiacRCausaleBilElem siacRCausaleBilElem) {
		getSiacRCausaleBilElems().add(siacRCausaleBilElem);
		siacRCausaleBilElem.setSiacDCausale(this);

		return siacRCausaleBilElem;
	}

	/**
	 * Removes the siac r causale bil elem.
	 *
	 * @param siacRCausaleBilElem the siac r causale bil elem
	 * @return the siac r causale bil elem
	 */
	public SiacRCausaleBilElem removeSiacRCausaleBilElem(SiacRCausaleBilElem siacRCausaleBilElem) {
		getSiacRCausaleBilElems().remove(siacRCausaleBilElem);
		siacRCausaleBilElem.setSiacDCausale(null);

		return siacRCausaleBilElem;
	}

	/**
	 * Gets the siac r causale classes.
	 *
	 * @return the siac r causale classes
	 */
	public List<SiacRCausaleClass> getSiacRCausaleClasses() {
		return this.siacRCausaleClasses;
	}

	/**
	 * Sets the siac r causale classes.
	 *
	 * @param siacRCausaleClasses the new siac r causale classes
	 */
	public void setSiacRCausaleClasses(List<SiacRCausaleClass> siacRCausaleClasses) {
		this.siacRCausaleClasses = siacRCausaleClasses;
	}

	/**
	 * Adds the siac r causale class.
	 *
	 * @param siacRCausaleClass the siac r causale class
	 * @return the siac r causale class
	 */
	public SiacRCausaleClass addSiacRCausaleClass(SiacRCausaleClass siacRCausaleClass) {
		getSiacRCausaleClasses().add(siacRCausaleClass);
		siacRCausaleClass.setSiacDCausale(this);

		return siacRCausaleClass;
	}

	/**
	 * Removes the siac r causale class.
	 *
	 * @param siacRCausaleClass the siac r causale class
	 * @return the siac r causale class
	 */
	public SiacRCausaleClass removeSiacRCausaleClass(SiacRCausaleClass siacRCausaleClass) {
		getSiacRCausaleClasses().remove(siacRCausaleClass);
		siacRCausaleClass.setSiacDCausale(null);

		return siacRCausaleClass;
	}

	/**
	 * Gets the siac r causale modpags.
	 *
	 * @return the siac r causale modpags
	 */
	public List<SiacRCausaleModpag> getSiacRCausaleModpags() {
		return this.siacRCausaleModpags;
	}

	/**
	 * Sets the siac r causale modpags.
	 *
	 * @param siacRCausaleModpags the new siac r causale modpags
	 */
	public void setSiacRCausaleModpags(List<SiacRCausaleModpag> siacRCausaleModpags) {
		this.siacRCausaleModpags = siacRCausaleModpags;
	}

	/**
	 * Adds the siac r causale modpag.
	 *
	 * @param siacRCausaleModpag the siac r causale modpag
	 * @return the siac r causale modpag
	 */
	public SiacRCausaleModpag addSiacRCausaleModpag(SiacRCausaleModpag siacRCausaleModpag) {
		getSiacRCausaleModpags().add(siacRCausaleModpag);
		siacRCausaleModpag.setSiacDCausale(this);

		return siacRCausaleModpag;
	}

	/**
	 * Removes the siac r causale modpag.
	 *
	 * @param siacRCausaleModpag the siac r causale modpag
	 * @return the siac r causale modpag
	 */
	public SiacRCausaleModpag removeSiacRCausaleModpag(SiacRCausaleModpag siacRCausaleModpag) {
		getSiacRCausaleModpags().remove(siacRCausaleModpag);
		siacRCausaleModpag.setSiacDCausale(null);

		return siacRCausaleModpag;
	}

	/**
	 * Gets the siac r causale movgest ts.
	 *
	 * @return the siac r causale movgest ts
	 */
	public List<SiacRCausaleMovgestT> getSiacRCausaleMovgestTs() {
		return this.siacRCausaleMovgestTs;
	}

	/**
	 * Sets the siac r causale movgest ts.
	 *
	 * @param siacRCausaleMovgestTs the new siac r causale movgest ts
	 */
	public void setSiacRCausaleMovgestTs(List<SiacRCausaleMovgestT> siacRCausaleMovgestTs) {
		this.siacRCausaleMovgestTs = siacRCausaleMovgestTs;
	}

	/**
	 * Adds the siac r causale movgest t.
	 *
	 * @param siacRCausaleMovgestT the siac r causale movgest t
	 * @return the siac r causale movgest t
	 */
	public SiacRCausaleMovgestT addSiacRCausaleMovgestT(SiacRCausaleMovgestT siacRCausaleMovgestT) {
		getSiacRCausaleMovgestTs().add(siacRCausaleMovgestT);
		siacRCausaleMovgestT.setSiacDCausale(this);

		return siacRCausaleMovgestT;
	}

	/**
	 * Removes the siac r causale movgest t.
	 *
	 * @param siacRCausaleMovgestT the siac r causale movgest t
	 * @return the siac r causale movgest t
	 */
	public SiacRCausaleMovgestT removeSiacRCausaleMovgestT(SiacRCausaleMovgestT siacRCausaleMovgestT) {
		getSiacRCausaleMovgestTs().remove(siacRCausaleMovgestT);
		siacRCausaleMovgestT.setSiacDCausale(null);

		return siacRCausaleMovgestT;
	}

	/**
	 * Gets the siac r causale soggettos.
	 *
	 * @return the siac r causale soggettos
	 */
	public List<SiacRCausaleSoggetto> getSiacRCausaleSoggettos() {
		return this.siacRCausaleSoggettos;
	}

	/**
	 * Sets the siac r causale soggettos.
	 *
	 * @param siacRCausaleSoggettos the new siac r causale soggettos
	 */
	public void setSiacRCausaleSoggettos(List<SiacRCausaleSoggetto> siacRCausaleSoggettos) {
		this.siacRCausaleSoggettos = siacRCausaleSoggettos;
	}

	/**
	 * Adds the siac r causale soggetto.
	 *
	 * @param siacRCausaleSoggetto the siac r causale soggetto
	 * @return the siac r causale soggetto
	 */
	public SiacRCausaleSoggetto addSiacRCausaleSoggetto(SiacRCausaleSoggetto siacRCausaleSoggetto) {
		getSiacRCausaleSoggettos().add(siacRCausaleSoggetto);
		siacRCausaleSoggetto.setSiacDCausale(this);

		return siacRCausaleSoggetto;
	}

	/**
	 * Removes the siac r causale soggetto.
	 *
	 * @param siacRCausaleSoggetto the siac r causale soggetto
	 * @return the siac r causale soggetto
	 */
	public SiacRCausaleSoggetto removeSiacRCausaleSoggetto(SiacRCausaleSoggetto siacRCausaleSoggetto) {
		getSiacRCausaleSoggettos().remove(siacRCausaleSoggetto);
		siacRCausaleSoggetto.setSiacDCausale(null);

		return siacRCausaleSoggetto;
	}

	/**
	 * Gets the siac r causale tipos.
	 *
	 * @return the siac r causale tipos
	 */
	public List<SiacRCausaleTipo> getSiacRCausaleTipos() {
		return this.siacRCausaleTipos;
	}

	/**
	 * Sets the siac r causale tipos.
	 *
	 * @param siacRCausaleTipos the new siac r causale tipos
	 */
	public void setSiacRCausaleTipos(List<SiacRCausaleTipo> siacRCausaleTipos) {
		this.siacRCausaleTipos = siacRCausaleTipos;
	}

	/**
	 * Adds the siac r causale tipo.
	 *
	 * @param siacRCausaleTipo the siac r causale tipo
	 * @return the siac r causale tipo
	 */
	public SiacRCausaleTipo addSiacRCausaleTipo(SiacRCausaleTipo siacRCausaleTipo) {
		getSiacRCausaleTipos().add(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausale(this);

		return siacRCausaleTipo;
	}

	/**
	 * Removes the siac r causale tipo.
	 *
	 * @param siacRCausaleTipo the siac r causale tipo
	 * @return the siac r causale tipo
	 */
	public SiacRCausaleTipo removeSiacRCausaleTipo(SiacRCausaleTipo siacRCausaleTipo) {
		getSiacRCausaleTipos().remove(siacRCausaleTipo);
		siacRCausaleTipo.setSiacDCausale(null);

		return siacRCausaleTipo;
	}

	/**
	 * Gets the siac r doc oneres.
	 *
	 * @return the siac r doc oneres
	 */
	public List<SiacRDocOnere> getSiacRDocOneres() {
		return this.siacRDocOneres;
	}

	/**
	 * Sets the siac r doc oneres.
	 *
	 * @param siacRDocOneres the new siac r doc oneres
	 */
	public void setSiacRDocOneres(List<SiacRDocOnere> siacRDocOneres) {
		this.siacRDocOneres = siacRDocOneres;
	}

	/**
	 * Adds the siac r doc onere.
	 *
	 * @param siacRDocOnere the siac r doc onere
	 * @return the siac r doc onere
	 */
	public SiacRDocOnere addSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		getSiacRDocOneres().add(siacRDocOnere);
		siacRDocOnere.setSiacDCausale(this);

		return siacRDocOnere;
	}

	/**
	 * Removes the siac r doc onere.
	 *
	 * @param siacRDocOnere the siac r doc onere
	 * @return the siac r doc onere
	 */
	public SiacRDocOnere removeSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		getSiacRDocOneres().remove(siacRDocOnere);
		siacRDocOnere.setSiacDCausale(null);

		return siacRDocOnere;
	}

	/**
	 * Gets the siac r onere causales.
	 *
	 * @return the siac r onere causales
	 */
	public List<SiacROnereCausale> getSiacROnereCausales() {
		return this.siacROnereCausales;
	}

	/**
	 * Sets the siac r onere causales.
	 *
	 * @param siacROnereCausales the new siac r onere causales
	 */
	public void setSiacROnereCausales(List<SiacROnereCausale> siacROnereCausales) {
		this.siacROnereCausales = siacROnereCausales;
	}

	/**
	 * Adds the siac r onere causale.
	 *
	 * @param siacROnereCausale the siac r onere causale
	 * @return the siac r onere causale
	 */
	public SiacROnereCausale addSiacROnereCausale(SiacROnereCausale siacROnereCausale) {
		getSiacROnereCausales().add(siacROnereCausale);
		siacROnereCausale.setSiacDCausale(this);

		return siacROnereCausale;
	}

	/**
	 * Removes the siac r onere causale.
	 *
	 * @param siacROnereCausale the siac r onere causale
	 * @return the siac r onere causale
	 */
	public SiacROnereCausale removeSiacROnereCausale(SiacROnereCausale siacROnereCausale) {
		getSiacROnereCausales().remove(siacROnereCausale);
		siacROnereCausale.setSiacDCausale(null);

		return siacROnereCausale;
	}

	/**
	 * Gets the siac r predoc causales.
	 *
	 * @return the siac r predoc causales
	 */
	public List<SiacRPredocCausale> getSiacRPredocCausales() {
		return this.siacRPredocCausales;
	}

	/**
	 * Sets the siac r predoc causales.
	 *
	 * @param siacRPredocCausales the new siac r predoc causales
	 */
	public void setSiacRPredocCausales(List<SiacRPredocCausale> siacRPredocCausales) {
		this.siacRPredocCausales = siacRPredocCausales;
	}

	/**
	 * Adds the siac r predoc causale.
	 *
	 * @param siacRPredocCausale the siac r predoc causale
	 * @return the siac r predoc causale
	 */
	public SiacRPredocCausale addSiacRPredocCausale(SiacRPredocCausale siacRPredocCausale) {
		getSiacRPredocCausales().add(siacRPredocCausale);
		siacRPredocCausale.setSiacDCausale(this);

		return siacRPredocCausale;
	}

	/**
	 * Removes the siac r predoc causale.
	 *
	 * @param siacRPredocCausale the siac r predoc causale
	 * @return the siac r predoc causale
	 */
	public SiacRPredocCausale removeSiacRPredocCausale(SiacRPredocCausale siacRPredocCausale) {
		getSiacRPredocCausales().remove(siacRPredocCausale);
		siacRPredocCausale.setSiacDCausale(null);

		return siacRPredocCausale;
	}
	
	

	/**
	 * Gets the date to extract.
	 *
	 * @return the dateToExtract
	 */
	public Date getDateToExtract() {
		return dateToExtract;
	}

	/**
	 * Sets the date to extract.
	 *
	 * @param dateToExtract the dateToExtract to set
	 */
	public void setDateToExtract(Date dateToExtract) {
		this.dateToExtract = dateToExtract;
	}
	
	public boolean isOnCascade() {
		return onCascade;
	}

	public void setOnCascade(boolean onCascade) {
		this.onCascade = onCascade;
	}
	
	public boolean isModello770(){
		return SiacDModelloEnum.CAUSALE_770.getCodice().equals(getSiacDModello().getModelCode());
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causId = uid;
	}
	
	/**
	 * Gets the siac d modello.
	 *
	 * @return the siac d modello
	 */
	public SiacDDistinta getSiacDDistinta() {
		return this.siacDDistinta;
	}

	/**
	 * Sets the siac d modello.
	 *
	 * @param siacDModello the new siac d modello
	 */
	public void setSiacDDistinta(SiacDDistinta siacDDistinta) {
		this.siacDDistinta = siacDDistinta;
	}



}