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
 * The persistent class for the siac_t_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest_ts")
@NamedQuery(name="SiacTMovgestT.findAll", query="SELECT s FROM SiacTMovgestT s")
public class SiacTMovgestT extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_TS_MOVGESTTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOVGEST_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_TS_MOVGESTTSID_GENERATOR")
	@Column(name="movgest_ts_id")
	private Integer movgestTsId;

	/** The livello. */
	private Integer livello;

	/** The movgest ts code. */
	@Column(name="movgest_ts_code")
	private String movgestTsCode;

	/** The movgest ts desc. */
	@Column(name="movgest_ts_desc")
	private String movgestTsDesc;
	
	/** The siac t movgest id padre. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id_padre")
	private SiacTMovgestT siacTMovgestIdPadre;	
	
	
	@OneToMany(mappedBy="siacTMovgestT1")
	private List<SiacRMovgestT> siacRMovgestT1s;

	@OneToMany(mappedBy="siacTMovgestT2")
	private List<SiacRMovgestT> siacRMovgestT2s;

	

	/** The ordine. */
	private String ordine;

	//bi-directional many-to-one association to SiacRCartacontDetMovgestT
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRCartacontDetMovgestT> siacRCartacontDetMovgestTs;

	//bi-directional many-to-one association to SiacRCausaleMovgestT
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRCausaleMovgestT> siacRCausaleMovgestTs;

	//bi-directional many-to-one association to SiacRFondoEconMovgest
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRFondoEconMovgest> siacRFondoEconMovgests;

	//bi-directional many-to-one association to SiacRLiquidazioneMovgest
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRLiquidazioneMovgest> siacRLiquidazioneMovgests;

	//bi-directional many-to-one association to SiacRMovgestClass
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestClass> siacRMovgestClasses;

	//bi-directional many-to-one association to SiacRMovgestTsAttoAmm
	/** The siac r movgest ts atto amms. */
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsAttoAmm> siacRMovgestTsAttoAmms;

	//bi-directional many-to-one association to SiacRMovgestTsAttr
	/** The siac r movgest ts attrs. */
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsAttr> siacRMovgestTsAttrs;

	//bi-directional many-to-one association to SiacRMovgestTsProgramma
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsProgramma> siacRMovgestTsProgrammas;

	//bi-directional many-to-one association to SiacRMovgestTsSog
	/** The siac r movgest ts sogs. */
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSog> siacRMovgestTsSogs;

	//bi-directional many-to-one association to SiacRMovgestTsSogMod
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasse
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSogclasse> siacRMovgestTsSogclasses;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseMod
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods;

	//bi-directional many-to-one association to SiacRMovgestTsStato
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMovgestTsStato> siacRMovgestTsStatos;

	//bi-directional many-to-one association to SiacROrdinativoTsMovgestT
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacROrdinativoTsMovgestT> siacROrdinativoTsMovgestTs;

	//bi-directional many-to-one association to SiacRPredocMovgestT
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRPredocMovgestT> siacRPredocMovgestTs;

	//bi-directional many-to-one association to SiacRSubdocMovgestT
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRSubdocMovgestT> siacRSubdocMovgestTs;

	//bi-directional many-to-one association to SiacDMovgestTsTipo
	/** The siac d movgest ts tipo. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_tipo_id")
	private SiacDMovgestTsTipo siacDMovgestTsTipo;

	//bi-directional many-to-one association to SiacTMovgest
	/** The siac t movgest. */
	@ManyToOne
	@JoinColumn(name="movgest_id")
	private SiacTMovgest siacTMovgest;

	//bi-directional many-to-one association to SiacTMovgestTsDet
	/** The siac t movgest ts dets. */
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacTMovgestTsDet> siacTMovgestTsDets;
	
	//bi-directional many-to-one association to SiacDSiopeTipoDebito
	/** The siac d siope tipo debito. */
	@ManyToOne
	@JoinColumn(name="siope_tipo_debito_id")
	private SiacDSiopeTipoDebito siacDSiopeTipoDebito;
	
	// bi-directional many-to-one association to SiacDSiopeAssenzaMotivazione
	/** The siac d siope assenza motivazione. */
	@ManyToOne
	@JoinColumn(name="siope_assenza_motivazione_id")
	private SiacDSiopeAssenzaMotivazione siacDSiopeAssenzaMotivazione;
	
	//SIAC-8195
	//bi-directional many-to-one association to SiacTMovgestTsDetModFin
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods;
	
	@OneToMany(mappedBy="siacTMovgestT")
	private List<SiacRMutuoMovgestT> siacRMutuoMovgestTs;
	

	/**
	 * Instantiates a new siac t movgest t.
	 */
	public SiacTMovgestT() {
	}

	/**
	 * Gets the movgest ts id.
	 *
	 * @return the movgest ts id
	 */
	public Integer getMovgestTsId() {
		return this.movgestTsId;
	}

	/**
	 * Sets the movgest ts id.
	 *
	 * @param movgestTsId the new movgest ts id
	 */
	public void setMovgestTsId(Integer movgestTsId) {
		this.movgestTsId = movgestTsId;
	}

	/**
	 * Gets the livello.
	 *
	 * @return the livello
	 */
	public Integer getLivello() {
		return this.livello;
	}

	/**
	 * Sets the livello.
	 *
	 * @param livello the new livello
	 */
	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	/**
	 * Gets the movgest ts code.
	 *
	 * @return the movgest ts code
	 */
	public String getMovgestTsCode() {
		return this.movgestTsCode;
	}

	/**
	 * Sets the movgest ts code.
	 *
	 * @param movgestTsCode the new movgest ts code
	 */
	public void setMovgestTsCode(String movgestTsCode) {
		this.movgestTsCode = movgestTsCode;
	}

	/**
	 * Gets the movgest ts desc.
	 *
	 * @return the movgest ts desc
	 */
	public String getMovgestTsDesc() {
		return this.movgestTsDesc;
	}

	/**
	 * Sets the movgest ts desc.
	 *
	 * @param movgestTsDesc the new movgest ts desc
	 */
	public void setMovgestTsDesc(String movgestTsDesc) {
		this.movgestTsDesc = movgestTsDesc;
	}

//	public Integer getMovgestTsIdPadre() {
//		return this.movgestTsIdPadre;
//	}
//
//	public void setMovgestTsIdPadre(Integer movgestTsIdPadre) {
//		this.movgestTsIdPadre = movgestTsIdPadre;
//	}
	
	

	/**
	 * Gets the ordine.
	 *
	 * @return the ordine
	 */
	public String getOrdine() {
		return this.ordine;
	}

	/**
	 * Gets the siac t movgest id padre.
	 *
	 * @return the siacTMovgestIdPadre
	 */
	public SiacTMovgestT getSiacTMovgestIdPadre() {
		return siacTMovgestIdPadre;
	}

	/**
	 * Sets the siac t movgest id padre.
	 *
	 * @param siacTMovgestIdPadre the siacTMovgestIdPadre to set
	 */
	public void setSiacTMovgestIdPadre(SiacTMovgestT siacTMovgestIdPadre) {
		this.siacTMovgestIdPadre = siacTMovgestIdPadre;
	}

	/**
	 * Sets the ordine.
	 *
	 * @param ordine the new ordine
	 */
	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public List<SiacRCartacontDetMovgestT> getSiacRCartacontDetMovgestTs() {
		return this.siacRCartacontDetMovgestTs;
	}

	public void setSiacRCartacontDetMovgestTs(List<SiacRCartacontDetMovgestT> siacRCartacontDetMovgestTs) {
		this.siacRCartacontDetMovgestTs = siacRCartacontDetMovgestTs;
	}

	public SiacRCartacontDetMovgestT addSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestT siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().add(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTMovgestT(this);

		return siacRCartacontDetMovgestT;
	}

	public SiacRCartacontDetMovgestT removeSiacRCartacontDetMovgestT(SiacRCartacontDetMovgestT siacRCartacontDetMovgestT) {
		getSiacRCartacontDetMovgestTs().remove(siacRCartacontDetMovgestT);
		siacRCartacontDetMovgestT.setSiacTMovgestT(null);

		return siacRCartacontDetMovgestT;
	}

	public List<SiacRCausaleMovgestT> getSiacRCausaleMovgestTs() {
		return this.siacRCausaleMovgestTs;
	}

	public void setSiacRCausaleMovgestTs(List<SiacRCausaleMovgestT> siacRCausaleMovgestTs) {
		this.siacRCausaleMovgestTs = siacRCausaleMovgestTs;
	}

	public SiacRCausaleMovgestT addSiacRCausaleMovgestT(SiacRCausaleMovgestT siacRCausaleMovgestT) {
		getSiacRCausaleMovgestTs().add(siacRCausaleMovgestT);
		siacRCausaleMovgestT.setSiacTMovgestT(this);

		return siacRCausaleMovgestT;
	}

	public SiacRCausaleMovgestT removeSiacRCausaleMovgestT(SiacRCausaleMovgestT siacRCausaleMovgestT) {
		getSiacRCausaleMovgestTs().remove(siacRCausaleMovgestT);
		siacRCausaleMovgestT.setSiacTMovgestT(null);

		return siacRCausaleMovgestT;
	}

	public List<SiacRFondoEconMovgest> getSiacRFondoEconMovgests() {
		return this.siacRFondoEconMovgests;
	}

	public void setSiacRFondoEconMovgests(List<SiacRFondoEconMovgest> siacRFondoEconMovgests) {
		this.siacRFondoEconMovgests = siacRFondoEconMovgests;
	}

	public SiacRFondoEconMovgest addSiacRFondoEconMovgest(SiacRFondoEconMovgest siacRFondoEconMovgest) {
		getSiacRFondoEconMovgests().add(siacRFondoEconMovgest);
		siacRFondoEconMovgest.setSiacTMovgestT(this);

		return siacRFondoEconMovgest;
	}

	public SiacRFondoEconMovgest removeSiacRFondoEconMovgest(SiacRFondoEconMovgest siacRFondoEconMovgest) {
		getSiacRFondoEconMovgests().remove(siacRFondoEconMovgest);
		siacRFondoEconMovgest.setSiacTMovgestT(null);

		return siacRFondoEconMovgest;
	}

	public List<SiacRLiquidazioneMovgest> getSiacRLiquidazioneMovgests() {
		return this.siacRLiquidazioneMovgests;
	}

	public void setSiacRLiquidazioneMovgests(List<SiacRLiquidazioneMovgest> siacRLiquidazioneMovgests) {
		this.siacRLiquidazioneMovgests = siacRLiquidazioneMovgests;
	}

	public SiacRLiquidazioneMovgest addSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgest siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().add(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTMovgestT(this);

		return siacRLiquidazioneMovgest;
	}

	public SiacRLiquidazioneMovgest removeSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgest siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().remove(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTMovgestT(null);

		return siacRLiquidazioneMovgest;
	}

	public List<SiacRMovgestClass> getSiacRMovgestClasses() {
		return this.siacRMovgestClasses;
	}

	public void setSiacRMovgestClasses(List<SiacRMovgestClass> siacRMovgestClasses) {
		this.siacRMovgestClasses = siacRMovgestClasses;
	}

	public SiacRMovgestClass addSiacRMovgestClass(SiacRMovgestClass siacRMovgestClass) {
		getSiacRMovgestClasses().add(siacRMovgestClass);
		siacRMovgestClass.setSiacTMovgestT(this);

		return siacRMovgestClass;
	}

	public SiacRMovgestClass removeSiacRMovgestClass(SiacRMovgestClass siacRMovgestClass) {
		getSiacRMovgestClasses().remove(siacRMovgestClass);
		siacRMovgestClass.setSiacTMovgestT(null);

		return siacRMovgestClass;
	}

	/**
	 * Gets the siac r movgest ts atto amms.
	 *
	 * @return the siac r movgest ts atto amms
	 */
	public List<SiacRMovgestTsAttoAmm> getSiacRMovgestTsAttoAmms() {
		return this.siacRMovgestTsAttoAmms;
	}

	/**
	 * Sets the siac r movgest ts atto amms.
	 *
	 * @param siacRMovgestTsAttoAmms the new siac r movgest ts atto amms
	 */
	public void setSiacRMovgestTsAttoAmms(List<SiacRMovgestTsAttoAmm> siacRMovgestTsAttoAmms) {
		this.siacRMovgestTsAttoAmms = siacRMovgestTsAttoAmms;
	}

	/**
	 * Adds the siac r movgest ts atto amm.
	 *
	 * @param siacRMovgestTsAttoAmm the siac r movgest ts atto amm
	 * @return the siac r movgest ts atto amm
	 */
	public SiacRMovgestTsAttoAmm addSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmm siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().add(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTMovgestT(this);

		return siacRMovgestTsAttoAmm;
	}

	/**
	 * Removes the siac r movgest ts atto amm.
	 *
	 * @param siacRMovgestTsAttoAmm the siac r movgest ts atto amm
	 * @return the siac r movgest ts atto amm
	 */
	public SiacRMovgestTsAttoAmm removeSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmm siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().remove(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTMovgestT(null);

		return siacRMovgestTsAttoAmm;
	}

	/**
	 * Gets the siac r movgest ts attrs.
	 *
	 * @return the siac r movgest ts attrs
	 */
	public List<SiacRMovgestTsAttr> getSiacRMovgestTsAttrs() {
		return this.siacRMovgestTsAttrs;
	}

	/**
	 * Sets the siac r movgest ts attrs.
	 *
	 * @param siacRMovgestTsAttrs the new siac r movgest ts attrs
	 */
	public void setSiacRMovgestTsAttrs(List<SiacRMovgestTsAttr> siacRMovgestTsAttrs) {
		this.siacRMovgestTsAttrs = siacRMovgestTsAttrs;
	}

	/**
	 * Adds the siac r movgest ts attr.
	 *
	 * @param siacRMovgestTsAttr the siac r movgest ts attr
	 * @return the siac r movgest ts attr
	 */
	public SiacRMovgestTsAttr addSiacRMovgestTsAttr(SiacRMovgestTsAttr siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().add(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTMovgestT(this);

		return siacRMovgestTsAttr;
	}

	/**
	 * Removes the siac r movgest ts attr.
	 *
	 * @param siacRMovgestTsAttr the siac r movgest ts attr
	 * @return the siac r movgest ts attr
	 */
	public SiacRMovgestTsAttr removeSiacRMovgestTsAttr(SiacRMovgestTsAttr siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().remove(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTMovgestT(null);

		return siacRMovgestTsAttr;
	}

	public List<SiacRMovgestTsProgramma> getSiacRMovgestTsProgrammas() {
		return this.siacRMovgestTsProgrammas;
	}

	public void setSiacRMovgestTsProgrammas(List<SiacRMovgestTsProgramma> siacRMovgestTsProgrammas) {
		this.siacRMovgestTsProgrammas = siacRMovgestTsProgrammas;
	}

	public SiacRMovgestTsProgramma addSiacRMovgestTsProgramma(SiacRMovgestTsProgramma siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().add(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTMovgestT(this);

		return siacRMovgestTsProgramma;
	}

	public SiacRMovgestTsProgramma removeSiacRMovgestTsProgramma(SiacRMovgestTsProgramma siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().remove(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTMovgestT(null);

		return siacRMovgestTsProgramma;
	}

	/**
	 * Gets the siac r movgest ts sogs.
	 *
	 * @return the siac r movgest ts sogs
	 */
	public List<SiacRMovgestTsSog> getSiacRMovgestTsSogs() {
		return this.siacRMovgestTsSogs;
	}

	/**
	 * Sets the siac r movgest ts sogs.
	 *
	 * @param siacRMovgestTsSogs the new siac r movgest ts sogs
	 */
	public void setSiacRMovgestTsSogs(List<SiacRMovgestTsSog> siacRMovgestTsSogs) {
		this.siacRMovgestTsSogs = siacRMovgestTsSogs;
	}

	/**
	 * Adds the siac r movgest ts sog.
	 *
	 * @param siacRMovgestTsSog the siac r movgest ts sog
	 * @return the siac r movgest ts sog
	 */
	public SiacRMovgestTsSog addSiacRMovgestTsSog(SiacRMovgestTsSog siacRMovgestTsSog) {
		getSiacRMovgestTsSogs().add(siacRMovgestTsSog);
		siacRMovgestTsSog.setSiacTMovgestT(this);

		return siacRMovgestTsSog;
	}

	/**
	 * Removes the siac r movgest ts sog.
	 *
	 * @param siacRMovgestTsSog the siac r movgest ts sog
	 * @return the siac r movgest ts sog
	 */
	public SiacRMovgestTsSog removeSiacRMovgestTsSog(SiacRMovgestTsSog siacRMovgestTsSog) {
		getSiacRMovgestTsSogs().remove(siacRMovgestTsSog);
		siacRMovgestTsSog.setSiacTMovgestT(null);

		return siacRMovgestTsSog;
	}

	public List<SiacRMovgestTsSogMod> getSiacRMovgestTsSogMods() {
		return this.siacRMovgestTsSogMods;
	}

	public void setSiacRMovgestTsSogMods(List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods) {
		this.siacRMovgestTsSogMods = siacRMovgestTsSogMods;
	}

	public SiacRMovgestTsSogMod addSiacRMovgestTsSogMod(SiacRMovgestTsSogMod siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().add(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacTMovgestT(this);

		return siacRMovgestTsSogMod;
	}

	public SiacRMovgestTsSogMod removeSiacRMovgestTsSogMod(SiacRMovgestTsSogMod siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().remove(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacTMovgestT(null);

		return siacRMovgestTsSogMod;
	}

	public List<SiacRMovgestTsSogclasse> getSiacRMovgestTsSogclasses() {
		return this.siacRMovgestTsSogclasses;
	}

	public void setSiacRMovgestTsSogclasses(List<SiacRMovgestTsSogclasse> siacRMovgestTsSogclasses) {
		this.siacRMovgestTsSogclasses = siacRMovgestTsSogclasses;
	}

	public SiacRMovgestTsSogclasse addSiacRMovgestTsSogclass(SiacRMovgestTsSogclasse siacRMovgestTsSogclass) {
		getSiacRMovgestTsSogclasses().add(siacRMovgestTsSogclass);
		siacRMovgestTsSogclass.setSiacTMovgestT(this);

		return siacRMovgestTsSogclass;
	}

	public SiacRMovgestTsSogclasse removeSiacRMovgestTsSogclass(SiacRMovgestTsSogclasse siacRMovgestTsSogclass) {
		getSiacRMovgestTsSogclasses().remove(siacRMovgestTsSogclass);
		siacRMovgestTsSogclass.setSiacTMovgestT(null);

		return siacRMovgestTsSogclass;
	}

	public List<SiacRMovgestTsSogclasseMod> getSiacRMovgestTsSogclasseMods() {
		return this.siacRMovgestTsSogclasseMods;
	}

	public void setSiacRMovgestTsSogclasseMods(List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods) {
		this.siacRMovgestTsSogclasseMods = siacRMovgestTsSogclasseMods;
	}

	public SiacRMovgestTsSogclasseMod addSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().add(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacTMovgestT(this);

		return siacRMovgestTsSogclasseMod;
	}

	public SiacRMovgestTsSogclasseMod removeSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().remove(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacTMovgestT(null);

		return siacRMovgestTsSogclasseMod;
	}

	public List<SiacRMovgestTsStato> getSiacRMovgestTsStatos() {
		return this.siacRMovgestTsStatos;
	}

	public void setSiacRMovgestTsStatos(List<SiacRMovgestTsStato> siacRMovgestTsStatos) {
		this.siacRMovgestTsStatos = siacRMovgestTsStatos;
	}

	public SiacRMovgestTsStato addSiacRMovgestTsStato(SiacRMovgestTsStato siacRMovgestTsStato) {
		getSiacRMovgestTsStatos().add(siacRMovgestTsStato);
		siacRMovgestTsStato.setSiacTMovgestT(this);

		return siacRMovgestTsStato;
	}

	public SiacRMovgestTsStato removeSiacRMovgestTsStato(SiacRMovgestTsStato siacRMovgestTsStato) {
		getSiacRMovgestTsStatos().remove(siacRMovgestTsStato);
		siacRMovgestTsStato.setSiacTMovgestT(null);

		return siacRMovgestTsStato;
	}

	public List<SiacROrdinativoTsMovgestT> getSiacROrdinativoTsMovgestTs() {
		return this.siacROrdinativoTsMovgestTs;
	}

	public void setSiacROrdinativoTsMovgestTs(List<SiacROrdinativoTsMovgestT> siacROrdinativoTsMovgestTs) {
		this.siacROrdinativoTsMovgestTs = siacROrdinativoTsMovgestTs;
	}

	public SiacROrdinativoTsMovgestT addSiacROrdinativoTsMovgestT(SiacROrdinativoTsMovgestT siacROrdinativoTsMovgestT) {
		getSiacROrdinativoTsMovgestTs().add(siacROrdinativoTsMovgestT);
		siacROrdinativoTsMovgestT.setSiacTMovgestT(this);

		return siacROrdinativoTsMovgestT;
	}

	public SiacROrdinativoTsMovgestT removeSiacROrdinativoTsMovgestT(SiacROrdinativoTsMovgestT siacROrdinativoTsMovgestT) {
		getSiacROrdinativoTsMovgestTs().remove(siacROrdinativoTsMovgestT);
		siacROrdinativoTsMovgestT.setSiacTMovgestT(null);

		return siacROrdinativoTsMovgestT;
	}

	public List<SiacRPredocMovgestT> getSiacRPredocMovgestTs() {
		return this.siacRPredocMovgestTs;
	}

	public void setSiacRPredocMovgestTs(List<SiacRPredocMovgestT> siacRPredocMovgestTs) {
		this.siacRPredocMovgestTs = siacRPredocMovgestTs;
	}

	public SiacRPredocMovgestT addSiacRPredocMovgestT(SiacRPredocMovgestT siacRPredocMovgestT) {
		getSiacRPredocMovgestTs().add(siacRPredocMovgestT);
		siacRPredocMovgestT.setSiacTMovgestT(this);

		return siacRPredocMovgestT;
	}

	public SiacRPredocMovgestT removeSiacRPredocMovgestT(SiacRPredocMovgestT siacRPredocMovgestT) {
		getSiacRPredocMovgestTs().remove(siacRPredocMovgestT);
		siacRPredocMovgestT.setSiacTMovgestT(null);

		return siacRPredocMovgestT;
	}

	public List<SiacRSubdocMovgestT> getSiacRSubdocMovgestTs() {
		return this.siacRSubdocMovgestTs;
	}

	public void setSiacRSubdocMovgestTs(List<SiacRSubdocMovgestT> siacRSubdocMovgestTs) {
		this.siacRSubdocMovgestTs = siacRSubdocMovgestTs;
	}

	public SiacRSubdocMovgestT addSiacRSubdocMovgestT(SiacRSubdocMovgestT siacRSubdocMovgestT) {
		getSiacRSubdocMovgestTs().add(siacRSubdocMovgestT);
		siacRSubdocMovgestT.setSiacTMovgestT(this);

		return siacRSubdocMovgestT;
	}

	public SiacRSubdocMovgestT removeSiacRSubdocMovgestT(SiacRSubdocMovgestT siacRSubdocMovgestT) {
		getSiacRSubdocMovgestTs().remove(siacRSubdocMovgestT);
		siacRSubdocMovgestT.setSiacTMovgestT(null);

		return siacRSubdocMovgestT;
	}

	/**
	 * Gets the siac d movgest ts tipo.
	 *
	 * @return the siac d movgest ts tipo
	 */
	public SiacDMovgestTsTipo getSiacDMovgestTsTipo() {
		return this.siacDMovgestTsTipo;
	}

	/**
	 * Sets the siac d movgest ts tipo.
	 *
	 * @param siacDMovgestTsTipo the new siac d movgest ts tipo
	 */
	public void setSiacDMovgestTsTipo(SiacDMovgestTsTipo siacDMovgestTsTipo) {
		this.siacDMovgestTsTipo = siacDMovgestTsTipo;
	}

	/**
	 * Gets the siac t movgest.
	 *
	 * @return the siac t movgest
	 */
	public SiacTMovgest getSiacTMovgest() {
		return this.siacTMovgest;
	}

	/**
	 * Sets the siac t movgest.
	 *
	 * @param siacTMovgest the new siac t movgest
	 */
	public void setSiacTMovgest(SiacTMovgest siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}

	/**
	 * Gets the siac t movgest ts dets.
	 *
	 * @return the siac t movgest ts dets
	 */
	public List<SiacTMovgestTsDet> getSiacTMovgestTsDets() {
		return this.siacTMovgestTsDets;
	}

	/**
	 * Sets the siac t movgest ts dets.
	 *
	 * @param siacTMovgestTsDets the new siac t movgest ts dets
	 */
	public void setSiacTMovgestTsDets(List<SiacTMovgestTsDet> siacTMovgestTsDets) {
		this.siacTMovgestTsDets = siacTMovgestTsDets;
	}

	/**
	 * Adds the siac t movgest ts det.
	 *
	 * @param siacTMovgestTsDet the siac t movgest ts det
	 * @return the siac t movgest ts det
	 */
	public SiacTMovgestTsDet addSiacTMovgestTsDet(SiacTMovgestTsDet siacTMovgestTsDet) {
		getSiacTMovgestTsDets().add(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacTMovgestT(this);

		return siacTMovgestTsDet;
	}

	/**
	 * Removes the siac t movgest ts det.
	 *
	 * @param siacTMovgestTsDet the siac t movgest ts det
	 * @return the siac t movgest ts det
	 */
	public SiacTMovgestTsDet removeSiacTMovgestTsDet(SiacTMovgestTsDet siacTMovgestTsDet) {
		getSiacTMovgestTsDets().remove(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacTMovgestT(null);

		return siacTMovgestTsDet;
	}

	/**
	 * Gets the siac d siope tipo debito.
	 *
	 * @return the siac d siope tipo debito
	 */
	public SiacDSiopeTipoDebito getSiacDSiopeTipoDebito() {
		return this.siacDSiopeTipoDebito;
	}

	/**
	 * Sets the siac d siope tipo debito.
	 *
	 * @param siacDSiopeTipoDebito the new siac d siope tipo debito
	 */
	public void setSiacDSiopeTipoDebito(SiacDSiopeTipoDebito siacDSiopeTipoDebito) {
		this.siacDSiopeTipoDebito = siacDSiopeTipoDebito;
	}

	/**
	 * Gets the siac d siope assenza motivazione.
	 *
	 * @return the siac d siope assenza motivazione
	 */
	public SiacDSiopeAssenzaMotivazione getSiacDSiopeAssenzaMotivazione() {
		return this.siacDSiopeAssenzaMotivazione;
	}

	/**
	 * Sets the siac d siope assenza motivazione.
	 *
	 * @param siacDSiopeAssenzaMotivazione the new siac d siope assenza motivazione
	 */
	public void setSiacDSiopeAssenzaMotivazione(SiacDSiopeAssenzaMotivazione siacDSiopeAssenzaMotivazione) {
		this.siacDSiopeAssenzaMotivazione = siacDSiopeAssenzaMotivazione;
	}

	/**
	 * @return the siacTMovgestTsDetMods
	 */
	public List<SiacTMovgestTsDetMod> getSiacTMovgestTsDetMods() {
		return siacTMovgestTsDetMods;
	}
	
	/**
	 * @param siacTMovgestTsDetMods the siacTMovgestTsDetMods to set
	 */
	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestTsId = uid;
		
	}

	public List<SiacRMovgestT> getSiacRMovgestT1s() {
		return siacRMovgestT1s;
	}

	public void setSiacRMovgestT1s(List<SiacRMovgestT> siacRMovgestT1s) {
		this.siacRMovgestT1s = siacRMovgestT1s;
	}

	public List<SiacRMovgestT> getSiacRMovgestT2s() {
		return siacRMovgestT2s;
	}

	public void setSiacRMovgestT2s(List<SiacRMovgestT> siacRMovgestT2s) {
		this.siacRMovgestT2s = siacRMovgestT2s;
	}

	public List<SiacRMutuoMovgestT> getSiacRMutuoMovgestTs() {
		return siacRMutuoMovgestTs;
	}

	public void setSiacRMutuoMovgestTs(List<SiacRMutuoMovgestT> siacRMutuoMovgestTs) {
		this.siacRMutuoMovgestTs = siacRMutuoMovgestTs;
	}

}