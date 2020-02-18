/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_predoc database table.
 * 
 */
@Entity
@Table(name="siac_t_predoc")
@NamedQuery(name="SiacTPredoc.findAll", query="SELECT s FROM SiacTPredoc s")
public class SiacTPredoc extends SiacTEnteBaseExt {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PREDOC_PREDOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PREDOC_PREDOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PREDOC_PREDOCID_GENERATOR")
	@Column(name="predoc_id")
	private Integer predocId;

	/** The predoc codice iuv. */
	@Column(name="predoc_codice_iuv")
	private String predocCodiceIuv;

	/** The predoc data. */
	@Column(name="predoc_data")
	private Date predocData;

	/** The predoc data competenza. */
	@Column(name="predoc_data_competenza")
	private Date predocDataCompetenza;

	/** The predoc desc. */
	@Column(name="predoc_desc")
	private String predocDesc;

	/** The predoc importo. */
	@Column(name="predoc_importo")
	private BigDecimal predocImporto;

	/** The predoc note. */
	@Column(name="predoc_note")
	private String predocNote;

	/** The predoc numero. */
	@Column(name="predoc_numero")
	private String predocNumero;

	/** The predoc periodo competenza. */
	@Column(name="predoc_periodo_competenza")
	private String predocPeriodoCompetenza;
	
	// SIAC-4383
	/** The predoc data trasmissione */
	@Column(name="predoc_data_trasmissione")
	private Date predocDataTrasmissione;

	//bi-directional many-to-one association to SiacRPredocAttoAmm
	/** The siac r predoc atto amms. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocAttoAmm> siacRPredocAttoAmms;

	//bi-directional many-to-one association to SiacRPredocBilElem
	/** The siac r predoc bil elems. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocBilElem> siacRPredocBilElems;

	//bi-directional many-to-one association to SiacRPredocCausale
	/** The siac r predoc causales. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocCausale> siacRPredocCausales;

	//bi-directional many-to-one association to SiacRPredocClass
	/** The siac r predoc classes. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocClass> siacRPredocClasses;

	//bi-directional many-to-one association to SiacRPredocModpag
	/** The siac r predoc modpags. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocModpag> siacRPredocModpags;

	//bi-directional many-to-one association to SiacRPredocMovgestT
	/** The siac r predoc movgest ts. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocMovgestT> siacRPredocMovgestTs;

	//bi-directional many-to-one association to SiacRPredocProvCassa
	/** The siac r predoc prov cassas. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocProvCassa> siacRPredocProvCassas;

	//bi-directional many-to-one association to SiacRPredocSog
	/** The siac r predoc sogs. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocSog> siacRPredocSogs;

	//bi-directional many-to-one association to SiacRPredocStato
	/** The siac r predoc statos. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocStato> siacRPredocStatos;

	//bi-directional many-to-one association to SiacRPredocSubdoc
	/** The siac r predoc subdocs. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocSubdoc> siacRPredocSubdocs;
	
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRMutuoVocePredoc> siacRMutuoVocePredocs;

	//bi-directional many-to-one association to SiacDContotesoreria
	/** The siac d contotesoreria. */
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreria siacDContotesoreria;

	//bi-directional many-to-one association to SiacDDocFamTipo
	/** The siac d doc fam tipo. */
	@ManyToOne
	@JoinColumn(name="doc_fam_tipo_id")
	private SiacDDocFamTipo siacDDocFamTipo;

	//bi-directional many-to-one association to SiacTPredocAnagr
	/** The siac t predoc anagrs. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTPredocAnagr> siacTPredocAnagrs;

	// SIAC-5001: gestisto solo la persistenza?
	//bi-directional many-to-one association to SiacRElencoDocPredoc
	/** The siac r elenco doc predocs. */
	@OneToMany(mappedBy="siacTPredoc", cascade = {CascadeType.PERSIST})
	private List<SiacRElencoDocPredoc> siacRElencoDocPredocs;
	
	/**
	 * Instantiates a new siac t predoc.
	 */
	public SiacTPredoc() {
	}

	/**
	 * Gets the predoc id.
	 *
	 * @return the predoc id
	 */
	public Integer getPredocId() {
		return this.predocId;
	}

	/**
	 * Sets the predoc id.
	 *
	 * @param predocId the new predoc id
	 */
	public void setPredocId(Integer predocId) {
		this.predocId = predocId;
	}

	

	



	/**
	 * Gets the predoc codice iuv.
	 *
	 * @return the predoc codice iuv
	 */
	public String getPredocCodiceIuv() {
		return this.predocCodiceIuv;
	}

	/**
	 * Sets the predoc codice iuv.
	 *
	 * @param predocCodiceIuv the new predoc codice iuv
	 */
	public void setPredocCodiceIuv(String predocCodiceIuv) {
		this.predocCodiceIuv = predocCodiceIuv;
	}

	/**
	 * Gets the predoc data.
	 *
	 * @return the predoc data
	 */
	public Date getPredocData() {
		return this.predocData;
	}

	/**
	 * Sets the predoc data.
	 *
	 * @param predocData the new predoc data
	 */
	public void setPredocData(Date predocData) {
		this.predocData = predocData;
	}

	/**
	 * Gets the predoc data competenza.
	 *
	 * @return the predoc data competenza
	 */
	public Date getPredocDataCompetenza() {
		return this.predocDataCompetenza;
	}

	/**
	 * Sets the predoc data competenza.
	 *
	 * @param predocDataCompetenza the new predoc data competenza
	 */
	public void setPredocDataCompetenza(Date predocDataCompetenza) {
		this.predocDataCompetenza = predocDataCompetenza;
	}

	/**
	 * Gets the predoc desc.
	 *
	 * @return the predoc desc
	 */
	public String getPredocDesc() {
		return this.predocDesc;
	}

	/**
	 * Sets the predoc desc.
	 *
	 * @param predocDesc the new predoc desc
	 */
	public void setPredocDesc(String predocDesc) {
		this.predocDesc = predocDesc;
	}

	/**
	 * Gets the predoc importo.
	 *
	 * @return the predoc importo
	 */
	public BigDecimal getPredocImporto() {
		return this.predocImporto;
	}

	/**
	 * Sets the predoc importo.
	 *
	 * @param predocImporto the new predoc importo
	 */
	public void setPredocImporto(BigDecimal predocImporto) {
		this.predocImporto = predocImporto;
	}

	/**
	 * Gets the predoc note.
	 *
	 * @return the predoc note
	 */
	public String getPredocNote() {
		return this.predocNote;
	}

	/**
	 * Sets the predoc note.
	 *
	 * @param predocNote the new predoc note
	 */
	public void setPredocNote(String predocNote) {
		this.predocNote = predocNote;
	}

	/**
	 * Gets the predoc numero.
	 *
	 * @return the predoc numero
	 */
	public String getPredocNumero() {
		return this.predocNumero;
	}

	/**
	 * Sets the predoc numero.
	 *
	 * @param predocNumero the new predoc numero
	 */
	public void setPredocNumero(String predocNumero) {
		this.predocNumero = predocNumero;
	}

	/**
	 * Gets the predoc periodo competenza.
	 *
	 * @return the predoc periodo competenza
	 */
	public String getPredocPeriodoCompetenza() {
		return this.predocPeriodoCompetenza;
	}

	/**
	 * Sets the predoc periodo competenza.
	 *
	 * @param predocPeriodoCompetenza the new predoc periodo competenza
	 */
	public void setPredocPeriodoCompetenza(String predocPeriodoCompetenza) {
		this.predocPeriodoCompetenza = predocPeriodoCompetenza;
	}

	/**
	 * @return the predocDataTrasmissione
	 */
	public Date getPredocDataTrasmissione() {
		return predocDataTrasmissione;
	}

	/**
	 * @param predocDataTrasmissione the predocDataTrasmissione to set
	 */
	public void setPredocDataTrasmissione(Date predocDataTrasmissione) {
		this.predocDataTrasmissione = predocDataTrasmissione;
	}

	/**
	 * Gets the siac r predoc atto amms.
	 *
	 * @return the siac r predoc atto amms
	 */
	public List<SiacRPredocAttoAmm> getSiacRPredocAttoAmms() {
		return this.siacRPredocAttoAmms;
	}

	/**
	 * Sets the siac r predoc atto amms.
	 *
	 * @param siacRPredocAttoAmms the new siac r predoc atto amms
	 */
	public void setSiacRPredocAttoAmms(List<SiacRPredocAttoAmm> siacRPredocAttoAmms) {
		this.siacRPredocAttoAmms = siacRPredocAttoAmms;
	}

	/**
	 * Adds the siac r predoc atto amm.
	 *
	 * @param siacRPredocAttoAmm the siac r predoc atto amm
	 * @return the siac r predoc atto amm
	 */
	public SiacRPredocAttoAmm addSiacRPredocAttoAmm(SiacRPredocAttoAmm siacRPredocAttoAmm) {
		getSiacRPredocAttoAmms().add(siacRPredocAttoAmm);
		siacRPredocAttoAmm.setSiacTPredoc(this);

		return siacRPredocAttoAmm;
	}

	/**
	 * Removes the siac r predoc atto amm.
	 *
	 * @param siacRPredocAttoAmm the siac r predoc atto amm
	 * @return the siac r predoc atto amm
	 */
	public SiacRPredocAttoAmm removeSiacRPredocAttoAmm(SiacRPredocAttoAmm siacRPredocAttoAmm) {
		getSiacRPredocAttoAmms().remove(siacRPredocAttoAmm);
		siacRPredocAttoAmm.setSiacTPredoc(null);

		return siacRPredocAttoAmm;
	}

	/**
	 * Gets the siac r predoc bil elems.
	 *
	 * @return the siac r predoc bil elems
	 */
	public List<SiacRPredocBilElem> getSiacRPredocBilElems() {
		return this.siacRPredocBilElems;
	}

	/**
	 * Sets the siac r predoc bil elems.
	 *
	 * @param siacRPredocBilElems the new siac r predoc bil elems
	 */
	public void setSiacRPredocBilElems(List<SiacRPredocBilElem> siacRPredocBilElems) {
		this.siacRPredocBilElems = siacRPredocBilElems;
	}

	/**
	 * Adds the siac r predoc bil elem.
	 *
	 * @param siacRPredocBilElem the siac r predoc bil elem
	 * @return the siac r predoc bil elem
	 */
	public SiacRPredocBilElem addSiacRPredocBilElem(SiacRPredocBilElem siacRPredocBilElem) {
		getSiacRPredocBilElems().add(siacRPredocBilElem);
		siacRPredocBilElem.setSiacTPredoc(this);

		return siacRPredocBilElem;
	}

	/**
	 * Removes the siac r predoc bil elem.
	 *
	 * @param siacRPredocBilElem the siac r predoc bil elem
	 * @return the siac r predoc bil elem
	 */
	public SiacRPredocBilElem removeSiacRPredocBilElem(SiacRPredocBilElem siacRPredocBilElem) {
		getSiacRPredocBilElems().remove(siacRPredocBilElem);
		siacRPredocBilElem.setSiacTPredoc(null);

		return siacRPredocBilElem;
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
		siacRPredocCausale.setSiacTPredoc(this);

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
		siacRPredocCausale.setSiacTPredoc(null);

		return siacRPredocCausale;
	}

	/**
	 * Gets the siac r predoc classes.
	 *
	 * @return the siac r predoc classes
	 */
	public List<SiacRPredocClass> getSiacRPredocClasses() {
		return this.siacRPredocClasses;
	}

	/**
	 * Sets the siac r predoc classes.
	 *
	 * @param siacRPredocClasses the new siac r predoc classes
	 */
	public void setSiacRPredocClasses(List<SiacRPredocClass> siacRPredocClasses) {
		this.siacRPredocClasses = siacRPredocClasses;
	}

	/**
	 * Adds the siac r predoc class.
	 *
	 * @param siacRPredocClass the siac r predoc class
	 * @return the siac r predoc class
	 */
	public SiacRPredocClass addSiacRPredocClass(SiacRPredocClass siacRPredocClass) {
		getSiacRPredocClasses().add(siacRPredocClass);
		siacRPredocClass.setSiacTPredoc(this);

		return siacRPredocClass;
	}

	/**
	 * Removes the siac r predoc class.
	 *
	 * @param siacRPredocClass the siac r predoc class
	 * @return the siac r predoc class
	 */
	public SiacRPredocClass removeSiacRPredocClass(SiacRPredocClass siacRPredocClass) {
		getSiacRPredocClasses().remove(siacRPredocClass);
		siacRPredocClass.setSiacTPredoc(null);

		return siacRPredocClass;
	}

	/**
	 * Gets the siac r predoc modpags.
	 *
	 * @return the siac r predoc modpags
	 */
	public List<SiacRPredocModpag> getSiacRPredocModpags() {
		return this.siacRPredocModpags;
	}

	/**
	 * Sets the siac r predoc modpags.
	 *
	 * @param siacRPredocModpags the new siac r predoc modpags
	 */
	public void setSiacRPredocModpags(List<SiacRPredocModpag> siacRPredocModpags) {
		this.siacRPredocModpags = siacRPredocModpags;
	}

	/**
	 * Adds the siac r predoc modpag.
	 *
	 * @param siacRPredocModpag the siac r predoc modpag
	 * @return the siac r predoc modpag
	 */
	public SiacRPredocModpag addSiacRPredocModpag(SiacRPredocModpag siacRPredocModpag) {
		getSiacRPredocModpags().add(siacRPredocModpag);
		siacRPredocModpag.setSiacTPredoc(this);

		return siacRPredocModpag;
	}

	/**
	 * Removes the siac r predoc modpag.
	 *
	 * @param siacRPredocModpag the siac r predoc modpag
	 * @return the siac r predoc modpag
	 */
	public SiacRPredocModpag removeSiacRPredocModpag(SiacRPredocModpag siacRPredocModpag) {
		getSiacRPredocModpags().remove(siacRPredocModpag);
		siacRPredocModpag.setSiacTPredoc(null);

		return siacRPredocModpag;
	}

	/**
	 * Gets the siac r predoc movgest ts.
	 *
	 * @return the siac r predoc movgest ts
	 */
	public List<SiacRPredocMovgestT> getSiacRPredocMovgestTs() {
		return this.siacRPredocMovgestTs;
	}

	/**
	 * Sets the siac r predoc movgest ts.
	 *
	 * @param siacRPredocMovgestTs the new siac r predoc movgest ts
	 */
	public void setSiacRPredocMovgestTs(List<SiacRPredocMovgestT> siacRPredocMovgestTs) {
		this.siacRPredocMovgestTs = siacRPredocMovgestTs;
	}

	/**
	 * Adds the siac r predoc movgest t.
	 *
	 * @param siacRPredocMovgestT the siac r predoc movgest t
	 * @return the siac r predoc movgest t
	 */
	public SiacRPredocMovgestT addSiacRPredocMovgestT(SiacRPredocMovgestT siacRPredocMovgestT) {
		getSiacRPredocMovgestTs().add(siacRPredocMovgestT);
		siacRPredocMovgestT.setSiacTPredoc(this);

		return siacRPredocMovgestT;
	}

	/**
	 * Removes the siac r predoc movgest t.
	 *
	 * @param siacRPredocMovgestT the siac r predoc movgest t
	 * @return the siac r predoc movgest t
	 */
	public SiacRPredocMovgestT removeSiacRPredocMovgestT(SiacRPredocMovgestT siacRPredocMovgestT) {
		getSiacRPredocMovgestTs().remove(siacRPredocMovgestT);
		siacRPredocMovgestT.setSiacTPredoc(null);

		return siacRPredocMovgestT;
	}

	/**
	 * Gets the siac r predoc prov cassas.
	 *
	 * @return the siac r predoc prov cassas
	 */
	public List<SiacRPredocProvCassa> getSiacRPredocProvCassas() {
		return this.siacRPredocProvCassas;
	}

	/**
	 * Sets the siac r predoc prov cassas.
	 *
	 * @param siacRPredocProvCassas the new siac r predoc prov cassas
	 */
	public void setSiacRPredocProvCassas(List<SiacRPredocProvCassa> siacRPredocProvCassas) {
		this.siacRPredocProvCassas = siacRPredocProvCassas;
	}

	/**
	 * Adds the siac r predoc prov cassa.
	 *
	 * @param siacRPredocProvCassa the siac r predoc prov cassa
	 * @return the siac r predoc prov cassa
	 */
	public SiacRPredocProvCassa addSiacRPredocProvCassa(SiacRPredocProvCassa siacRPredocProvCassa) {
		getSiacRPredocProvCassas().add(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTPredoc(this);

		return siacRPredocProvCassa;
	}

	/**
	 * Removes the siac r predoc prov cassa.
	 *
	 * @param siacRPredocProvCassa the siac r predoc prov cassa
	 * @return the siac r predoc prov cassa
	 */
	public SiacRPredocProvCassa removeSiacRPredocProvCassa(SiacRPredocProvCassa siacRPredocProvCassa) {
		getSiacRPredocProvCassas().remove(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTPredoc(null);

		return siacRPredocProvCassa;
	}

	/**
	 * Gets the siac r predoc sogs.
	 *
	 * @return the siac r predoc sogs
	 */
	public List<SiacRPredocSog> getSiacRPredocSogs() {
		return this.siacRPredocSogs;
	}

	/**
	 * Sets the siac r predoc sogs.
	 *
	 * @param siacRPredocSogs the new siac r predoc sogs
	 */
	public void setSiacRPredocSogs(List<SiacRPredocSog> siacRPredocSogs) {
		this.siacRPredocSogs = siacRPredocSogs;
	}

	/**
	 * Adds the siac r predoc sog.
	 *
	 * @param siacRPredocSog the siac r predoc sog
	 * @return the siac r predoc sog
	 */
	public SiacRPredocSog addSiacRPredocSog(SiacRPredocSog siacRPredocSog) {
		getSiacRPredocSogs().add(siacRPredocSog);
		siacRPredocSog.setSiacTPredoc(this);

		return siacRPredocSog;
	}

	/**
	 * Removes the siac r predoc sog.
	 *
	 * @param siacRPredocSog the siac r predoc sog
	 * @return the siac r predoc sog
	 */
	public SiacRPredocSog removeSiacRPredocSog(SiacRPredocSog siacRPredocSog) {
		getSiacRPredocSogs().remove(siacRPredocSog);
		siacRPredocSog.setSiacTPredoc(null);

		return siacRPredocSog;
	}

	/**
	 * Gets the siac r predoc statos.
	 *
	 * @return the siac r predoc statos
	 */
	public List<SiacRPredocStato> getSiacRPredocStatos() {
		return this.siacRPredocStatos;
	}

	/**
	 * Sets the siac r predoc statos.
	 *
	 * @param siacRPredocStatos the new siac r predoc statos
	 */
	public void setSiacRPredocStatos(List<SiacRPredocStato> siacRPredocStatos) {
		this.siacRPredocStatos = siacRPredocStatos;
	}

	/**
	 * Adds the siac r predoc stato.
	 *
	 * @param siacRPredocStato the siac r predoc stato
	 * @return the siac r predoc stato
	 */
	public SiacRPredocStato addSiacRPredocStato(SiacRPredocStato siacRPredocStato) {
		getSiacRPredocStatos().add(siacRPredocStato);
		siacRPredocStato.setSiacTPredoc(this);

		return siacRPredocStato;
	}

	/**
	 * Removes the siac r predoc stato.
	 *
	 * @param siacRPredocStato the siac r predoc stato
	 * @return the siac r predoc stato
	 */
	public SiacRPredocStato removeSiacRPredocStato(SiacRPredocStato siacRPredocStato) {
		getSiacRPredocStatos().remove(siacRPredocStato);
		siacRPredocStato.setSiacTPredoc(null);

		return siacRPredocStato;
	}

	/**
	 * Gets the siac r predoc subdocs.
	 *
	 * @return the siac r predoc subdocs
	 */
	public List<SiacRPredocSubdoc> getSiacRPredocSubdocs() {
		return this.siacRPredocSubdocs;
	}

	/**
	 * Sets the siac r predoc subdocs.
	 *
	 * @param siacRPredocSubdocs the new siac r predoc subdocs
	 */
	public void setSiacRPredocSubdocs(List<SiacRPredocSubdoc> siacRPredocSubdocs) {
		this.siacRPredocSubdocs = siacRPredocSubdocs;
	}

	/**
	 * Adds the siac r predoc subdoc.
	 *
	 * @param siacRPredocSubdoc the siac r predoc subdoc
	 * @return the siac r predoc subdoc
	 */
	public SiacRPredocSubdoc addSiacRPredocSubdoc(SiacRPredocSubdoc siacRPredocSubdoc) {
		getSiacRPredocSubdocs().add(siacRPredocSubdoc);
		siacRPredocSubdoc.setSiacTPredoc(this);

		return siacRPredocSubdoc;
	}

	/**
	 * Removes the siac r predoc subdoc.
	 *
	 * @param siacRPredocSubdoc the siac r predoc subdoc
	 * @return the siac r predoc subdoc
	 */
	public SiacRPredocSubdoc removeSiacRPredocSubdoc(SiacRPredocSubdoc siacRPredocSubdoc) {
		getSiacRPredocSubdocs().remove(siacRPredocSubdoc);
		siacRPredocSubdoc.setSiacTPredoc(null);

		return siacRPredocSubdoc;
	}
	
	/**
	 * Gets the siac r mutuo voce predocs.
	 *
	 * @return the siac r mutuo voce predocs
	 */
	public List<SiacRMutuoVocePredoc> getSiacRMutuoVocePredocs() {
		return this.siacRMutuoVocePredocs;
	}

	/**
	 * Sets the siac r mutuo voce predocs.
	 *
	 * @param SiacRMutuoVocePredoc the new siac r mutuo voce predocs
	 */
	public void setSiacRMutuoVocePredocs(List<SiacRMutuoVocePredoc> siacRMutuoVocePredocs) {
		this.siacRMutuoVocePredocs = siacRMutuoVocePredocs;
	}

	/**
	 * Adds the siac r mutuo voce predoc.
	 *
	 * @param SiacRMutuoVocePredoc the siac r mutuo voce predoc
	 * @return the siac r mutuo voce predoc
	 */
	public SiacRMutuoVocePredoc addSiacRMutuoVocePredoc(SiacRMutuoVocePredoc siacRMutuoVocePredoc) {
		getSiacRMutuoVocePredocs().add(siacRMutuoVocePredoc);
		siacRMutuoVocePredoc.setSiacTPredoc(this);

		return siacRMutuoVocePredoc;
	}

	/**
	 * Removes the siac r mutuo voce predoc.
	 *
	 * @param siacRPredocSubdoc the siac r mutuo voce predoc
	 * @return the siac r mutuo voce predoc
	 */
	public SiacRMutuoVocePredoc removeSiacRMutuoVocePredoc(SiacRMutuoVocePredoc siacRMutuoVocePredoc) {
		getSiacRMutuoVocePredocs().remove(siacRMutuoVocePredoc);
		siacRMutuoVocePredoc.setSiacTPredoc(null);

		return siacRMutuoVocePredoc;
	}

	/**
	 * Gets the siac d contotesoreria.
	 *
	 * @return the siac d contotesoreria
	 */
	public SiacDContotesoreria getSiacDContotesoreria() {
		return this.siacDContotesoreria;
	}

	/**
	 * Sets the siac d contotesoreria.
	 *
	 * @param siacDContotesoreria the new siac d contotesoreria
	 */
	public void setSiacDContotesoreria(SiacDContotesoreria siacDContotesoreria) {
		this.siacDContotesoreria = siacDContotesoreria;
	}

	/**
	 * Gets the siac d doc fam tipo.
	 *
	 * @return the siac d doc fam tipo
	 */
	public SiacDDocFamTipo getSiacDDocFamTipo() {
		return this.siacDDocFamTipo;
	}

	/**
	 * Sets the siac d doc fam tipo.
	 *
	 * @param siacDDocFamTipo the new siac d doc fam tipo
	 */
	public void setSiacDDocFamTipo(SiacDDocFamTipo siacDDocFamTipo) {
		this.siacDDocFamTipo = siacDDocFamTipo;
	}

	/**
	 * Gets the siac t predoc anagrs.
	 *
	 * @return the siac t predoc anagrs
	 */
	public List<SiacTPredocAnagr> getSiacTPredocAnagrs() {
		return this.siacTPredocAnagrs;
	}

	/**
	 * Sets the siac t predoc anagrs.
	 *
	 * @param siacTPredocAnagrs the new siac t predoc anagrs
	 */
	public void setSiacTPredocAnagrs(List<SiacTPredocAnagr> siacTPredocAnagrs) {
		this.siacTPredocAnagrs = siacTPredocAnagrs;
	}

	/**
	 * Adds the siac t predoc anagr.
	 *
	 * @param siacTPredocAnagr the siac t predoc anagr
	 * @return the siac t predoc anagr
	 */
	public SiacTPredocAnagr addSiacTPredocAnagr(SiacTPredocAnagr siacTPredocAnagr) {
		getSiacTPredocAnagrs().add(siacTPredocAnagr);
		siacTPredocAnagr.setSiacTPredoc(this);

		return siacTPredocAnagr;
	}

	/**
	 * Removes the siac t predoc anagr.
	 *
	 * @param siacTPredocAnagr the siac t predoc anagr
	 * @return the siac t predoc anagr
	 */
	public SiacTPredocAnagr removeSiacTPredocAnagr(SiacTPredocAnagr siacTPredocAnagr) {
		getSiacTPredocAnagrs().remove(siacTPredocAnagr);
		siacTPredocAnagr.setSiacTPredoc(null);

		return siacTPredocAnagr;
	}
	
	/**
	 * Gets the siac r elenco doc predocs.
	 *
	 * @return the siac r elenco doc predocs
	 */
	public List<SiacRElencoDocPredoc> getSiacRElencoDocPredocs() {
		return this.siacRElencoDocPredocs;
	}

	/**
	 * Sets the siac e elenco doc predocs.
	 *
	 * @param siacRElencoDocPredocs the new siac r elenco doc predocs
	 */
	public void setSiacRElencoDocPredocs(List<SiacRElencoDocPredoc> siacRElencoDocPredocs) {
		this.siacRElencoDocPredocs = siacRElencoDocPredocs;
	}

	/**
	 * Adds the siac r elenco doc predoc.
	 *
	 * @param siacRElencoDocPredoc the siac r elenco doc predoc
	 * @return the siac r elenco doc predoc
	 */
	public SiacRElencoDocPredoc addSiacRElencoDocPredoc(SiacRElencoDocPredoc siacRElencoDocPredoc) {
		getSiacRElencoDocPredocs().add(siacRElencoDocPredoc);
		siacRElencoDocPredoc.setSiacTPredoc(this);

		return siacRElencoDocPredoc;
	}

	/**
	 * Removes the siac r elenco doc predoc.
	 *
	 * @param siacRElencoDocPredoc the siac r elenco doc predoc
	 * @return the siac r elenco doc predoc
	 */
	public SiacRElencoDocPredoc removeSiacRElencoDocPredoc(SiacRElencoDocPredoc siacRElencoDocPredoc) {
		getSiacRElencoDocPredocs().remove(siacRElencoDocPredoc);
		siacRElencoDocPredoc.setSiacTPredoc(null);

		return siacRElencoDocPredoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocId = uid;
	}

}