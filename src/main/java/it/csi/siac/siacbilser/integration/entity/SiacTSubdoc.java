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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_t_subdoc")
@NamedQuery(name="SiacTSubdoc.findAll", query="SELECT s FROM SiacTSubdoc s")
public class SiacTSubdoc extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_SUBDOC_SUBDOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_SUBDOC_SUBDOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SUBDOC_SUBDOCID_GENERATOR")
	@Column(name="subdoc_id")
	private Integer subdocId;

	@Column(name="subdoc_convalida_manuale")
	private String subdocConvalidaManuale;

	@Column(name="subdoc_data_pagamento_cec")
	private Date subdocDataPagamentoCec;

	@Column(name="subdoc_data_scadenza")
	private Date subdocDataScadenza;

	@Column(name="subdoc_desc")
	private String subdocDesc;

	@Column(name="subdoc_importo")
	private BigDecimal subdocImporto;

	@Column(name="subdoc_importo_da_dedurre")
	private BigDecimal subdocImportoDaDedurre;

	@Column(name="subdoc_nreg_iva")
	private String subdocNregIva;

	@Column(name="subdoc_numero")
	private Integer subdocNumero;

	@Column(name="subdoc_pagato_cec")
	private Boolean subdocPagatoCec;

	@Column(name="subdoc_splitreverse_importo")
	private BigDecimal subdocSplitreverseImporto;

	//bi-directional many-to-one association to SiacRCartacontDetSubdoc
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRCartacontDetSubdoc> siacRCartacontDetSubdocs;

	//bi-directional many-to-one association to SiacRElencoDocSubdoc
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRElencoDocSubdoc> siacRElencoDocSubdocs;

	//bi-directional many-to-one association to SiacRPredocSubdoc
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPredocSubdoc> siacRPredocSubdocs;

	//bi-directional many-to-one association to SiacRSubdoc
	/** The siac r subdocs a. */
	@OneToMany(mappedBy="siacTSubdocA", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdoc> siacRSubdocsA;

	//bi-directional many-to-one association to SiacRSubdoc
	/** The siac r subdocs b. */
	@OneToMany(mappedBy="siacTSubdocB", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdoc> siacRSubdocsB;

	//bi-directional many-to-one association to SiacRSubdocAttoAmm
	/** The siac r subdoc atto amms. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocAttoAmm> siacRSubdocAttoAmms;

	//bi-directional many-to-one association to SiacRSubdocAttr
	/** The siac r subdoc attrs. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocAttr> siacRSubdocAttrs;

	//bi-directional many-to-one association to SiacRSubdocClass
	/** The siac r subdoc classes. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocClass> siacRSubdocClasses;

	//bi-directional many-to-one association to SiacRSubdocLiquidazione
	/** The siac r subdoc liquidaziones. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocLiquidazione> siacRSubdocLiquidaziones;

	//bi-directional many-to-one association to SiacRSubdocModpag
	/** The siac r subdoc modpags. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocModpag> siacRSubdocModpags;

	//bi-directional many-to-one association to SiacRSubdocMovgestT
	/** The siac r subdoc movgest ts. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocMovgestT> siacRSubdocMovgestTs;

	//bi-directional many-to-one association to SiacRSubdocProvCassa
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocProvCassa> siacRSubdocProvCassas;

	//bi-directional many-to-one association to SiacRSubdocOrdinativoT
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocOrdinativoT> siacRSubdocOrdinativoTs;

	//bi-directional many-to-one association to SiacRSubdocSog
	/** The siac r subdoc sogs. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocSog> siacRSubdocSogs;
	
	//bi-directional many-to-one association to SiacRSubdocSubdocIva
	/** The siac r subdoc subdoc ivas. */
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRSubdocSubdocIva> siacRSubdocSubdocIvas;

	//bi-directional many-to-one association to SiacRSubdocSubdocIva
	/** The siac r subdoc subdoc ivas. */
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTSubdocSospensione> siacTSubdocSospensiones;

	//bi-directional many-to-one association to SiacDCommissioneTipo
	/** The siac d commissione tipo. */
	@ManyToOne
	@JoinColumn(name="comm_tipo_id")
	private SiacDCommissioneTipo siacDCommissioneTipo;

	//bi-directional many-to-one association to SiacDContotesoreria
	/** The siac d contotesoreria. */
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreria siacDContotesoreria;

	//bi-directional many-to-one association to SiacDDistinta
	/** The siac d distinta. */
	@ManyToOne
	@JoinColumn(name="dist_id")
	private SiacDDistinta siacDDistinta;

	//bi-directional many-to-one association to SiacDNoteTesoriere
	/** The siac d note tesoriere. */
	@ManyToOne
	@JoinColumn(name="notetes_id")
	private SiacDNoteTesoriere siacDNoteTesoriere;

	//bi-directional many-to-one association to SiacDSubdocTipo
	/** The siac d subdoc tipo. */
	@ManyToOne
	@JoinColumn(name="subdoc_tipo_id")
	private SiacDSubdocTipo siacDSubdocTipo;
	
	//bi-directional many-to-one association to SiacTRegistroPcc
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacTRegistroPcc> siacTRegistroPccs;
	
	//bi-directional many-to-one association to SiacRSubdocSplitreverseIvaTipo
	@OneToMany(mappedBy="siacTSubdoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocSplitreverseIvaTipo> siacRSubdocSplitreverseIvaTipos;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;
	
	//bi-directional many-to-one association to SiacRRichiestaEconSubdoc
	@OneToMany(mappedBy="siacTSubdoc")
	private List<SiacRRichiestaEconSubdoc> siacRRichiestaEconSubdocs;

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

	//bi-directional many-to-one association to SiacDSiopeScadenzaMotivo
	/** The siac d siope assenza motivazione. */
	@ManyToOne
	@JoinColumn(name="siope_scadenza_motivo_id")
	private SiacDSiopeScadenzaMotivo siacDSiopeScadenzaMotivo;

	public SiacTSubdoc() {
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if(this.subdocPagatoCec == null) {
			this.subdocPagatoCec = Boolean.FALSE;
		}
	}

	public Integer getSubdocId() {
		return this.subdocId;
	}

	public void setSubdocId(Integer subdocId) {
		this.subdocId = subdocId;
	}

	public String getSubdocConvalidaManuale() {
		return this.subdocConvalidaManuale;
	}

	public void setSubdocConvalidaManuale(String subdocConvalidaManuale) {
		this.subdocConvalidaManuale = subdocConvalidaManuale;
	}

	public Date getSubdocDataPagamentoCec() {
		return this.subdocDataPagamentoCec;
	}

	public void setSubdocDataPagamentoCec(Date subdocDataPagamentoCec) {
		this.subdocDataPagamentoCec = subdocDataPagamentoCec;
	}

	public void setSubdocDataPagamentoCecIfNotSet(Date subdocDataPagamentoCec) {
		if(this.subdocDataPagamentoCec == null) {
			this.subdocDataPagamentoCec = subdocDataPagamentoCec;
		}
	}

	public Date getSubdocDataScadenza() {
		return this.subdocDataScadenza;
	}

	public void setSubdocDataScadenza(Date subdocDataScadenza) {
		this.subdocDataScadenza = subdocDataScadenza;
	}

	public String getSubdocDesc() {
		return this.subdocDesc;
	}

	public void setSubdocDesc(String subdocDesc) {
		this.subdocDesc = subdocDesc;
	}

	public BigDecimal getSubdocImporto() {
		return this.subdocImporto;
	}

	public void setSubdocImporto(BigDecimal subdocImporto) {
		this.subdocImporto = subdocImporto;
	}

	public BigDecimal getSubdocImportoDaDedurre() {
		return this.subdocImportoDaDedurre;
	}

	public void setSubdocImportoDaDedurre(BigDecimal subdocImportoDaDedurre) {
		this.subdocImportoDaDedurre = subdocImportoDaDedurre;
	}

	public String getSubdocNregIva() {
		return this.subdocNregIva;
	}

	public void setSubdocNregIva(String subdocNregIva) {
		this.subdocNregIva = subdocNregIva;
	}

	public Integer getSubdocNumero() {
		return this.subdocNumero;
	}

	public void setSubdocNumero(Integer subdocNumero) {
		this.subdocNumero = subdocNumero;
	}

	public Boolean getSubdocPagatoCec() {
		return this.subdocPagatoCec;
	}

	public void setSubdocPagatoCec(Boolean subdocPagatoCec) {
		this.subdocPagatoCec = subdocPagatoCec;
	}

	public BigDecimal getSubdocSplitreverseImporto() {
		return this.subdocSplitreverseImporto;
	}

	public void setSubdocSplitreverseImporto(BigDecimal subdocSplitreverseImporto) {
		this.subdocSplitreverseImporto = subdocSplitreverseImporto;
	}

	/**
	 * Gets the siac r subdocs a.
	 *
	 * @return the siac r subdocs a
	 */
	public List<SiacRSubdoc> getSiacRSubdocsA() {
		return this.siacRSubdocsA;
	}

	/**
	 * Sets the siac r subdocs a.
	 *
	 * @param siacRSubdocs1 the new siac r subdocs a
	 */
	public void setSiacRSubdocsA(List<SiacRSubdoc> siacRSubdocs1) {
		this.siacRSubdocsA = siacRSubdocs1;
	}

	/**
	 * Adds the siac r subdocs1.
	 *
	 * @param siacRSubdocs1 the siac r subdocs1
	 * @return the siac r subdoc
	 */
	public SiacRSubdoc addSiacRSubdocs1(SiacRSubdoc siacRSubdocs1) {
		getSiacRSubdocsA().add(siacRSubdocs1);
		siacRSubdocs1.setSiacTSubdocB(this);

		return siacRSubdocs1;
	}

	/**
	 * Removes the siac r subdocs1.
	 *
	 * @param siacRSubdocs1 the siac r subdocs1
	 * @return the siac r subdoc
	 */
	public SiacRSubdoc removeSiacRSubdocs1(SiacRSubdoc siacRSubdocs1) {
		getSiacRSubdocsA().remove(siacRSubdocs1);
		siacRSubdocs1.setSiacTSubdocB(null);

		return siacRSubdocs1;
	}

	/**
	 * Gets the siac r subdocs b.
	 *
	 * @return the siac r subdocs b
	 */
	public List<SiacRSubdoc> getSiacRSubdocsB() {
		return this.siacRSubdocsB;
	}

	/**
	 * Sets the siac r subdocs b.
	 *
	 * @param siacRSubdocs2 the new siac r subdocs b
	 */
	public void setSiacRSubdocsB(List<SiacRSubdoc> siacRSubdocs2) {
		this.siacRSubdocsB = siacRSubdocs2;
	}

	/**
	 * Adds the siac r subdocs2.
	 *
	 * @param siacRSubdocs2 the siac r subdocs2
	 * @return the siac r subdoc
	 */
	public SiacRSubdoc addSiacRSubdocs2(SiacRSubdoc siacRSubdocs2) {
		getSiacRSubdocsB().add(siacRSubdocs2);
		siacRSubdocs2.setSiacTSubdocA(this);

		return siacRSubdocs2;
	}

	/**
	 * Removes the siac r subdocs2.
	 *
	 * @param siacRSubdocs2 the siac r subdocs2
	 * @return the siac r subdoc
	 */
	public SiacRSubdoc removeSiacRSubdocs2(SiacRSubdoc siacRSubdocs2) {
		getSiacRSubdocsB().remove(siacRSubdocs2);
		siacRSubdocs2.setSiacTSubdocA(null);

		return siacRSubdocs2;
	}

	/**
	 * Gets the siac r subdoc atto amms.
	 *
	 * @return the siac r subdoc atto amms
	 */
	public List<SiacRSubdocAttoAmm> getSiacRSubdocAttoAmms() {
		return this.siacRSubdocAttoAmms;
	}

	/**
	 * Sets the siac r subdoc atto amms.
	 *
	 * @param siacRSubdocAttoAmms the new siac r subdoc atto amms
	 */
	public void setSiacRSubdocAttoAmms(List<SiacRSubdocAttoAmm> siacRSubdocAttoAmms) {
		this.siacRSubdocAttoAmms = siacRSubdocAttoAmms;
	}

	/**
	 * Adds the siac r subdoc atto amm.
	 *
	 * @param siacRSubdocAttoAmm the siac r subdoc atto amm
	 * @return the siac r subdoc atto amm
	 */
	public SiacRSubdocAttoAmm addSiacRSubdocAttoAmm(SiacRSubdocAttoAmm siacRSubdocAttoAmm) {
		getSiacRSubdocAttoAmms().add(siacRSubdocAttoAmm);
		siacRSubdocAttoAmm.setSiacTSubdoc(this);

		return siacRSubdocAttoAmm;
	}

	/**
	 * Removes the siac r subdoc atto amm.
	 *
	 * @param siacRSubdocAttoAmm the siac r subdoc atto amm
	 * @return the siac r subdoc atto amm
	 */
	public SiacRSubdocAttoAmm removeSiacRSubdocAttoAmm(SiacRSubdocAttoAmm siacRSubdocAttoAmm) {
		getSiacRSubdocAttoAmms().remove(siacRSubdocAttoAmm);
		siacRSubdocAttoAmm.setSiacTSubdoc(null);

		return siacRSubdocAttoAmm;
	}

	/**
	 * Gets the siac r subdoc attrs.
	 *
	 * @return the siac r subdoc attrs
	 */
	public List<SiacRSubdocAttr> getSiacRSubdocAttrs() {
		return this.siacRSubdocAttrs;
	}

	/**
	 * Sets the siac r subdoc attrs.
	 *
	 * @param siacRSubdocAttrs the new siac r subdoc attrs
	 */
	public void setSiacRSubdocAttrs(List<SiacRSubdocAttr> siacRSubdocAttrs) {
		this.siacRSubdocAttrs = siacRSubdocAttrs;
	}

	/**
	 * Adds the siac r subdoc attr.
	 *
	 * @param siacRSubdocAttr the siac r subdoc attr
	 * @return the siac r subdoc attr
	 */
	public SiacRSubdocAttr addSiacRSubdocAttr(SiacRSubdocAttr siacRSubdocAttr) {
		getSiacRSubdocAttrs().add(siacRSubdocAttr);
		siacRSubdocAttr.setSiacTSubdoc(this);

		return siacRSubdocAttr;
	}

	/**
	 * Removes the siac r subdoc attr.
	 *
	 * @param siacRSubdocAttr the siac r subdoc attr
	 * @return the siac r subdoc attr
	 */
	public SiacRSubdocAttr removeSiacRSubdocAttr(SiacRSubdocAttr siacRSubdocAttr) {
		getSiacRSubdocAttrs().remove(siacRSubdocAttr);
		siacRSubdocAttr.setSiacTSubdoc(null);

		return siacRSubdocAttr;
	}

	/**
	 * Gets the siac r subdoc classes.
	 *
	 * @return the siac r subdoc classes
	 */
	public List<SiacRSubdocClass> getSiacRSubdocClasses() {
		return this.siacRSubdocClasses;
	}

	/**
	 * Sets the siac r subdoc classes.
	 *
	 * @param siacRSubdocClasses the new siac r subdoc classes
	 */
	public void setSiacRSubdocClasses(List<SiacRSubdocClass> siacRSubdocClasses) {
		this.siacRSubdocClasses = siacRSubdocClasses;
	}

	/**
	 * Adds the siac r subdoc class.
	 *
	 * @param siacRSubdocClass the siac r subdoc class
	 * @return the siac r subdoc class
	 */
	public SiacRSubdocClass addSiacRSubdocClass(SiacRSubdocClass siacRSubdocClass) {
		getSiacRSubdocClasses().add(siacRSubdocClass);
		siacRSubdocClass.setSiacTSubdoc(this);

		return siacRSubdocClass;
	}

	/**
	 * Removes the siac r subdoc class.
	 *
	 * @param siacRSubdocClass the siac r subdoc class
	 * @return the siac r subdoc class
	 */
	public SiacRSubdocClass removeSiacRSubdocClass(SiacRSubdocClass siacRSubdocClass) {
		getSiacRSubdocClasses().remove(siacRSubdocClass);
		siacRSubdocClass.setSiacTSubdoc(null);

		return siacRSubdocClass;
	}

	/**
	 * Gets the siac r subdoc liquidaziones.
	 *
	 * @return the siac r subdoc liquidaziones
	 */
	public List<SiacRSubdocLiquidazione> getSiacRSubdocLiquidaziones() {
		return this.siacRSubdocLiquidaziones;
	}

	/**
	 * Sets the siac r subdoc liquidaziones.
	 *
	 * @param siacRSubdocLiquidaziones the new siac r subdoc liquidaziones
	 */
	public void setSiacRSubdocLiquidaziones(List<SiacRSubdocLiquidazione> siacRSubdocLiquidaziones) {
		this.siacRSubdocLiquidaziones = siacRSubdocLiquidaziones;
	}

	/**
	 * Adds the siac r subdoc liquidazione.
	 *
	 * @param siacRSubdocLiquidazione the siac r subdoc liquidazione
	 * @return the siac r subdoc liquidazione
	 */
	public SiacRSubdocLiquidazione addSiacRSubdocLiquidazione(SiacRSubdocLiquidazione siacRSubdocLiquidazione) {
		getSiacRSubdocLiquidaziones().add(siacRSubdocLiquidazione);
		siacRSubdocLiquidazione.setSiacTSubdoc(this);

		return siacRSubdocLiquidazione;
	}

	/**
	 * Removes the siac r subdoc liquidazione.
	 *
	 * @param siacRSubdocLiquidazione the siac r subdoc liquidazione
	 * @return the siac r subdoc liquidazione
	 */
	public SiacRSubdocLiquidazione removeSiacRSubdocLiquidazione(SiacRSubdocLiquidazione siacRSubdocLiquidazione) {
		getSiacRSubdocLiquidaziones().remove(siacRSubdocLiquidazione);
		siacRSubdocLiquidazione.setSiacTSubdoc(null);

		return siacRSubdocLiquidazione;
	}

	/**
	 * Gets the siac r subdoc modpags.
	 *
	 * @return the siac r subdoc modpags
	 */
	public List<SiacRSubdocModpag> getSiacRSubdocModpags() {
		return this.siacRSubdocModpags;
	}

	/**
	 * Sets the siac r subdoc modpags.
	 *
	 * @param siacRSubdocModpags the new siac r subdoc modpags
	 */
	public void setSiacRSubdocModpags(List<SiacRSubdocModpag> siacRSubdocModpags) {
		this.siacRSubdocModpags = siacRSubdocModpags;
	}

	/**
	 * Adds the siac r subdoc modpag.
	 *
	 * @param siacRSubdocModpag the siac r subdoc modpag
	 * @return the siac r subdoc modpag
	 */
	public SiacRSubdocModpag addSiacRSubdocModpag(SiacRSubdocModpag siacRSubdocModpag) {
		getSiacRSubdocModpags().add(siacRSubdocModpag);
		siacRSubdocModpag.setSiacTSubdoc(this);

		return siacRSubdocModpag;
	}

	/**
	 * Removes the siac r subdoc modpag.
	 *
	 * @param siacRSubdocModpag the siac r subdoc modpag
	 * @return the siac r subdoc modpag
	 */
	public SiacRSubdocModpag removeSiacRSubdocModpag(SiacRSubdocModpag siacRSubdocModpag) {
		getSiacRSubdocModpags().remove(siacRSubdocModpag);
		siacRSubdocModpag.setSiacTSubdoc(null);

		return siacRSubdocModpag;
	}

	/**
	 * Gets the siac r subdoc movgest ts.
	 *
	 * @return the siac r subdoc movgest ts
	 */
	public List<SiacRSubdocMovgestT> getSiacRSubdocMovgestTs() {
		return this.siacRSubdocMovgestTs;
	}

	/**
	 * Sets the siac r subdoc movgest ts.
	 *
	 * @param siacRSubdocMovgestTs the new siac r subdoc movgest ts
	 */
	public void setSiacRSubdocMovgestTs(List<SiacRSubdocMovgestT> siacRSubdocMovgestTs) {
		this.siacRSubdocMovgestTs = siacRSubdocMovgestTs;
	}

	/**
	 * Adds the siac r subdoc movgest t.
	 *
	 * @param siacRSubdocMovgestT the siac r subdoc movgest t
	 * @return the siac r subdoc movgest t
	 */
	public SiacRSubdocMovgestT addSiacRSubdocMovgestT(SiacRSubdocMovgestT siacRSubdocMovgestT) {
		getSiacRSubdocMovgestTs().add(siacRSubdocMovgestT);
		siacRSubdocMovgestT.setSiacTSubdoc(this);

		return siacRSubdocMovgestT;
	}

	/**
	 * Removes the siac r subdoc movgest t.
	 *
	 * @param siacRSubdocMovgestT the siac r subdoc movgest t
	 * @return the siac r subdoc movgest t
	 */
	public SiacRSubdocMovgestT removeSiacRSubdocMovgestT(SiacRSubdocMovgestT siacRSubdocMovgestT) {
		getSiacRSubdocMovgestTs().remove(siacRSubdocMovgestT);
		siacRSubdocMovgestT.setSiacTSubdoc(null);

		return siacRSubdocMovgestT;
	}

	public List<SiacRSubdocOrdinativoT> getSiacRSubdocOrdinativoTs() {
		return this.siacRSubdocOrdinativoTs;
	}

	public void setSiacRSubdocOrdinativoTs(List<SiacRSubdocOrdinativoT> siacRSubdocOrdinativoTs) {
		this.siacRSubdocOrdinativoTs = siacRSubdocOrdinativoTs;
	}

	public SiacRSubdocOrdinativoT addSiacRSubdocOrdinativoT(SiacRSubdocOrdinativoT siacRSubdocOrdinativoT) {
		getSiacRSubdocOrdinativoTs().add(siacRSubdocOrdinativoT);
		siacRSubdocOrdinativoT.setSiacTSubdoc(this);

		return siacRSubdocOrdinativoT;
	}

	public SiacRSubdocOrdinativoT removeSiacRSubdocOrdinativoT(SiacRSubdocOrdinativoT siacRSubdocOrdinativoT) {
		getSiacRSubdocOrdinativoTs().remove(siacRSubdocOrdinativoT);
		siacRSubdocOrdinativoT.setSiacTSubdoc(null);

		return siacRSubdocOrdinativoT;
	}
	


	/**
 * Gets the siac r subdoc sogs.
 *
 * @return the siac r subdoc sogs
 */
	public List<SiacRSubdocSog> getSiacRSubdocSogs() {
		return this.siacRSubdocSogs;
	}

	/**
	 * Sets the siac r subdoc sogs.
	 *
	 * @param siacRSubdocSogs the new siac r subdoc sogs
	 */
	public void setSiacRSubdocSogs(List<SiacRSubdocSog> siacRSubdocSogs) {
		this.siacRSubdocSogs = siacRSubdocSogs;
	}

	/**
	 * Adds the siac r subdoc sog.
	 *
	 * @param siacRSubdocSog the siac r subdoc sog
	 * @return the siac r subdoc sog
	 */
	public SiacRSubdocSog addSiacRSubdocSog(SiacRSubdocSog siacRSubdocSog) {
		getSiacRSubdocSogs().add(siacRSubdocSog);
		siacRSubdocSog.setSiacTSubdoc(this);

		return siacRSubdocSog;
	}

	/**
	 * Removes the siac r subdoc sog.
	 *
	 * @param siacRSubdocSog the siac r subdoc sog
	 * @return the siac r subdoc sog
	 */
	public SiacRSubdocSog removeSiacRSubdocSog(SiacRSubdocSog siacRSubdocSog) {
		getSiacRSubdocSogs().remove(siacRSubdocSog);
		siacRSubdocSog.setSiacTSubdoc(null);

		return siacRSubdocSog;
	}
	

	/**
	 * Gets the siac d subdoc tipo.
	 *
	 * @return the siac d subdoc tipo
	 */
	public SiacDSubdocTipo getSiacDSubdocTipo() {
		return this.siacDSubdocTipo;
	}

	/**
	 * Sets the siac d subdoc tipo.
	 *
	 * @param siacDSubdocTipo the new siac d subdoc tipo
	 */
	public void setSiacDSubdocTipo(SiacDSubdocTipo siacDSubdocTipo) {
		this.siacDSubdocTipo = siacDSubdocTipo;
	}

	/**
	 * Gets the siac t doc.
	 *
	 * @return the siac t doc
	 */
	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	/**
	 * Sets the siac t doc.
	 *
	 * @param siacTDoc the new siac t doc
	 */
	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
	}	

	/**
	 * Gets the siac r subdoc subdoc ivas.
	 *
	 * @return the siac r subdoc subdoc ivas
	 */
	public List<SiacRSubdocSubdocIva> getSiacRSubdocSubdocIvas() {
		return this.siacRSubdocSubdocIvas;
	}

	/**
	 * Sets the siac r subdoc subdoc ivas.
	 *
	 * @param siacRSubdocSubdocIvas the new siac r subdoc subdoc ivas
	 */
	public void setSiacRSubdocSubdocIvas(List<SiacRSubdocSubdocIva> siacRSubdocSubdocIvas) {
		this.siacRSubdocSubdocIvas = siacRSubdocSubdocIvas;
	}

	/**
	 * Adds the siac r subdoc subdoc iva.
	 *
	 * @param siacRSubdocSubdocIva the siac r subdoc subdoc iva
	 * @return the siac r subdoc subdoc iva
	 */
	public SiacRSubdocSubdocIva addSiacRSubdocSubdocIva(SiacRSubdocSubdocIva siacRSubdocSubdocIva) {
		getSiacRSubdocSubdocIvas().add(siacRSubdocSubdocIva);
		siacRSubdocSubdocIva.setSiacTSubdoc(this);

		return siacRSubdocSubdocIva;
	}

	/**
	 * Removes the siac r subdoc subdoc iva.
	 *
	 * @param siacRSubdocSubdocIva the siac r subdoc subdoc iva
	 * @return the siac r subdoc subdoc iva
	 */
	public SiacRSubdocSubdocIva removeSiacRSubdocSubdocIva(SiacRSubdocSubdocIva siacRSubdocSubdocIva) {
		getSiacRSubdocSubdocIvas().remove(siacRSubdocSubdocIva);
		siacRSubdocSubdocIva.setSiacTSubdoc(null);

		return siacRSubdocSubdocIva;
	}

	/**
	 * Gets the siac t subdoc sospensiones.
	 *
	 * @return the siac t subdoc sospensiones
	 */
	public List<SiacTSubdocSospensione> getSiacTSubdocSospensiones() {
		return this.siacTSubdocSospensiones;
	}

	/**
	 * Sets the siac t subdoc sospensiones.
	 *
	 * @param siacTSubdocSospensiones the new siac t subdoc sospensiones
	 */
	public void setSiacTSubdocSospensiones(List<SiacTSubdocSospensione> siacTSubdocSospensiones) {
		this.siacTSubdocSospensiones = siacTSubdocSospensiones;
	}

	/**
	 * Adds the siac t subdoc sospensione.
	 *
	 * @param siacTSubdocSospensione the siac t subdoc sospensione
	 * @return the siac t subdoc sospensione
	 */
	public SiacTSubdocSospensione addSiacTSubdocSospensione(SiacTSubdocSospensione siacTSubdocSospensione) {
		getSiacTSubdocSospensiones().add(siacTSubdocSospensione);
		siacTSubdocSospensione.setSiacTSubdoc(this);

		return siacTSubdocSospensione;
	}

	/**
	 * Removes the siac t subdoc sospensione.
	 *
	 * @param siacTSubdocSospensione the siac t subdoc sospensione
	 * @return the siac t subdoc sospensione
	 */
	public SiacTSubdocSospensione removeSiacTSubdocSospensione(SiacTSubdocSospensione siacTSubdocSospensione) {
		getSiacTSubdocSospensiones().remove(siacTSubdocSospensione);
		siacTSubdocSospensione.setSiacTSubdoc(null);

		return siacTSubdocSospensione;
	}

	
	/**
	 * Gets the siac d commissione tipo.
	 *
	 * @return the siacDCommissioneTipo
	 */
	public SiacDCommissioneTipo getSiacDCommissioneTipo() {
		return siacDCommissioneTipo;
	}

	/**
	 * Sets the siac d commissione tipo.
	 *
	 * @param siacDCommissioneTipo the siacDCommissioneTipo to set
	 */
	public void setSiacDCommissioneTipo(SiacDCommissioneTipo siacDCommissioneTipo) {
		this.siacDCommissioneTipo = siacDCommissioneTipo;
	}

	/**
	 * Gets the siac d contotesoreria.
	 *
	 * @return the siacDContotesoreria
	 */
	public SiacDContotesoreria getSiacDContotesoreria() {
		return siacDContotesoreria;
	}

	/**
	 * Sets the siac d contotesoreria.
	 *
	 * @param siacDContotesoreria the siacDContotesoreria to set
	 */
	public void setSiacDContotesoreria(SiacDContotesoreria siacDContotesoreria) {
		this.siacDContotesoreria = siacDContotesoreria;
	}

	/**
	 * Gets the siac d distinta.
	 *
	 * @return the siacDDistinta
	 */
	public SiacDDistinta getSiacDDistinta() {
		return siacDDistinta;
	}

	/**
	 * Sets the siac d distinta.
	 *
	 * @param siacDDistinta the siacDDistinta to set
	 */
	public void setSiacDDistinta(SiacDDistinta siacDDistinta) {
		this.siacDDistinta = siacDDistinta;
	}

	/**
	 * Gets the siac d note tesoriere.
	 *
	 * @return the siacDNoteTesoriere
	 */
	public SiacDNoteTesoriere getSiacDNoteTesoriere() {
		return siacDNoteTesoriere;
	}

	/**
	 * Sets the siac d note tesoriere.
	 *
	 * @param siacDNoteTesoriere the siacDNoteTesoriere to set
	 */
	public void setSiacDNoteTesoriere(SiacDNoteTesoriere siacDNoteTesoriere) {
		this.siacDNoteTesoriere = siacDNoteTesoriere;
	}
	
	
	
	
	
	

	/**
	 * @return the siacRCartacontDetSubdocs
	 */
	public List<SiacRCartacontDetSubdoc> getSiacRCartacontDetSubdocs() {
		return siacRCartacontDetSubdocs;
	}

	/**
	 * @param siacRCartacontDetSubdocs the siacRCartacontDetSubdocs to set
	 */
	public void setSiacRCartacontDetSubdocs(List<SiacRCartacontDetSubdoc> siacRCartacontDetSubdocs) {
		this.siacRCartacontDetSubdocs = siacRCartacontDetSubdocs;
	}
	
	public SiacRCartacontDetSubdoc addSiacRCartacontDetSubdoc(SiacRCartacontDetSubdoc siacRCartacontDetSubdoc) {
		getSiacRCartacontDetSubdocs().add(siacRCartacontDetSubdoc);
		siacRCartacontDetSubdoc.setSiacTSubdoc(this);

		return siacRCartacontDetSubdoc;
	}

	/**
	 * @return the siacRElencoDocSubdocs
	 */
	public List<SiacRElencoDocSubdoc> getSiacRElencoDocSubdocs() {
		return siacRElencoDocSubdocs;
	}

	/**
	 * @param siacRElencoDocSubdocs the siacRElencoDocSubdocs to set
	 */
	public void setSiacRElencoDocSubdocs(List<SiacRElencoDocSubdoc> siacRElencoDocSubdocs) {
		this.siacRElencoDocSubdocs = siacRElencoDocSubdocs;
	}
	
	public SiacRElencoDocSubdoc addSiacRElencoDocSubdoc(SiacRElencoDocSubdoc siacRElencoDocSubdoc) {
		getSiacRElencoDocSubdocs().add(siacRElencoDocSubdoc);
		siacRElencoDocSubdoc.setSiacTSubdoc(this);

		return siacRElencoDocSubdoc;
	}

	/**
	 * @return the siacRPredocSubdocs
	 */
	public List<SiacRPredocSubdoc> getSiacRPredocSubdocs() {
		return siacRPredocSubdocs;
	}

	/**
	 * @param siacRPredocSubdocs the siacRPredocSubdocs to set
	 */
	public void setSiacRPredocSubdocs(List<SiacRPredocSubdoc> siacRPredocSubdocs) {
		this.siacRPredocSubdocs = siacRPredocSubdocs;
	}
	
	public SiacRPredocSubdoc addSiacRPredocSubdoc(SiacRPredocSubdoc siacRPredocSubdoc) {
		getSiacRPredocSubdocs().add(siacRPredocSubdoc);
		siacRPredocSubdoc.setSiacTSubdoc(this);

		return siacRPredocSubdoc;
	}

	/**
	 * @return the siacRSubdocProvCassas
	 */
	public List<SiacRSubdocProvCassa> getSiacRSubdocProvCassas() {
		return siacRSubdocProvCassas;
	}

	/**
	 * @param siacRSubdocProvCassas the siacRSubdocProvCassas to set
	 */
	public void setSiacRSubdocProvCassas(List<SiacRSubdocProvCassa> siacRSubdocProvCassas) {
		this.siacRSubdocProvCassas = siacRSubdocProvCassas;
	}
	
	public SiacRSubdocProvCassa addSiacRSubdocProvCassa(SiacRSubdocProvCassa siacRSubdocProvCassa) {
		getSiacRSubdocProvCassas().add(siacRSubdocProvCassa);
		siacRSubdocProvCassa.setSiacTSubdoc(this);

		return siacRSubdocProvCassa;
	}
	
	/**
	 * Gets the siac t registro pccs
	 *
	 * @return the siac t registro pccs
	 */
	public List<SiacTRegistroPcc> getSiacTRegistroPccs() {
		return this.siacTRegistroPccs;
	}

	/**
	 * Sets the siac t registro pccd.
	 *
	 * @param siacTRegistroPccs the new siac t registro pccs
	 */
	public void setSiacTRegistroPccs(List<SiacTRegistroPcc> siacTRegistroPccs) {
		this.siacTRegistroPccs = siacTRegistroPccs;
	}

	/**
	 * Adds the siac t registro pcc.
	 *
	 * @param siacTRegistroPcc the siac t registro pcc
	 * @return the siac t registro pcc
	 */
	public SiacTRegistroPcc addSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().add(siacTRegistroPcc);
		siacTRegistroPcc.setSiacTSubdoc(this);

		return siacTRegistroPcc;
	}

	/**
	 * Removes the siac t registro pcc.
	 *
	 * @param siacTRegistroPcc the siac t registro pcc
	 * @return the siac t registro pcc
	 */
	public SiacTRegistroPcc removeSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().remove(siacTRegistroPcc);
		siacTRegistroPcc.setSiacTSubdoc(null);

		return siacTRegistroPcc;
	}
	
	public List<SiacRSubdocSplitreverseIvaTipo> getSiacRSubdocSplitreverseIvaTipos() {
		return this.siacRSubdocSplitreverseIvaTipos;
	}

	public void setSiacRSubdocSplitreverseIvaTipos(List<SiacRSubdocSplitreverseIvaTipo> siacRSubdocSplitreverseIvaTipos) {
		this.siacRSubdocSplitreverseIvaTipos = siacRSubdocSplitreverseIvaTipos;
	}

	public SiacRSubdocSplitreverseIvaTipo addSiacRSubdocSplitreverseIvaTipo(SiacRSubdocSplitreverseIvaTipo siacRSubdocSplitreverseIvaTipo) {
		getSiacRSubdocSplitreverseIvaTipos().add(siacRSubdocSplitreverseIvaTipo);
		siacRSubdocSplitreverseIvaTipo.setSiacTSubdoc(this);

		return siacRSubdocSplitreverseIvaTipo;
	}

	public SiacRSubdocSplitreverseIvaTipo removeSiacRSubdocSplitreverseIvaTipo(SiacRSubdocSplitreverseIvaTipo siacRSubdocSplitreverseIvaTipo) {
		getSiacRSubdocSplitreverseIvaTipos().remove(siacRSubdocSplitreverseIvaTipo);
		siacRSubdocSplitreverseIvaTipo.setSiacTSubdoc(null);

		return siacRSubdocSplitreverseIvaTipo;
	}

	public List<SiacRRichiestaEconSubdoc> getSiacRRichiestaEconSubdocs() {
		return this.siacRRichiestaEconSubdocs;
	}

	public void setSiacRRichiestaEconSubdocs(List<SiacRRichiestaEconSubdoc> siacRRichiestaEconSubdocs) {
		this.siacRRichiestaEconSubdocs = siacRRichiestaEconSubdocs;
	}

	public SiacRRichiestaEconSubdoc addSiacRRichiestaEconSubdoc(SiacRRichiestaEconSubdoc siacRRichiestaEconSubdoc) {
		getSiacRRichiestaEconSubdocs().add(siacRRichiestaEconSubdoc);
		siacRRichiestaEconSubdoc.setSiacTSubdoc(this);

		return siacRRichiestaEconSubdoc;
	}

	public SiacRRichiestaEconSubdoc removeSiacRRichiestaEconSubdoc(SiacRRichiestaEconSubdoc siacRRichiestaEconSubdoc) {
		getSiacRRichiestaEconSubdocs().remove(siacRRichiestaEconSubdoc);
		siacRRichiestaEconSubdoc.setSiacTSubdoc(null);

		return siacRRichiestaEconSubdoc;
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
	 * Gets the siac d siope scadenza motivo.
	 *
	 * @return the siac d siope scadenza motivo
	 */
	public SiacDSiopeScadenzaMotivo getSiacDSiopeScadenzaMotivo() {
		return this.siacDSiopeScadenzaMotivo;
	}

	/**
	 * Sets the siac d siope scadenza motivo.
	 *
	 * @param siacDSiopeScadenzaMotivo the new siac d siope scadenza motivo
	 */
	public void setSiacDSiopeScadenzaMotivo(SiacDSiopeScadenzaMotivo siacDSiopeScadenzaMotivo) {
		this.siacDSiopeScadenzaMotivo = siacDSiopeScadenzaMotivo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocId = uid;
	}
}