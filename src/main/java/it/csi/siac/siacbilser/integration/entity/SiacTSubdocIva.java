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
 * The persistent class for the siac_t_subdoc_iva database table.
 * 
 */
@Entity
@Table(name="siac_t_subdoc_iva")
@NamedQuery(name="SiacTSubdocIva.findAll", query="SELECT s FROM SiacTSubdocIva s")
public class SiacTSubdocIva extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdociva id. */
	@Id
	@SequenceGenerator(name="SIAC_T_SUBDOC_IVA_SUBDOCIVAID_GENERATOR", sequenceName="SIAC_T_SUBDOC_IVA_SUBDOCIVA_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SUBDOC_IVA_SUBDOCIVAID_GENERATOR")
	@Column(name="subdociva_id")
	private Integer subdocivaId;

	/** The subdociva anno. */
	@Column(name="subdociva_anno")
	private String subdocivaAnno;

	/** The subdociva data cassadoc. */
	@Column(name="subdociva_data_cassadoc")
	private Date subdocivaDataCassadoc;

	/** The subdociva data emissione. */
	@Column(name="subdociva_data_emissione")
	private Date subdocivaDataEmissione;

	/** The subdociva data ordinativoadoc. */
	@Column(name="subdociva_data_ordinativoadoc")
	private Date subdocivaDataOrdinativoadoc;

	/** The subdociva data prot def. */
	@Column(name="subdociva_data_prot_def")
	private Date subdocivaDataProtDef;

	/** The subdociva data prot prov. */
	@Column(name="subdociva_data_prot_prov")
	private Date subdocivaDataProtProv;

	/** The subdociva data registrazione. */
	@Column(name="subdociva_data_registrazione")
	private Date subdocivaDataRegistrazione;

	/** The subdociva data scadenza. */
	@Column(name="subdociva_data_scadenza")
	private Date subdocivaDataScadenza;

	/** The subdociva desc. */
	@Column(name="subdociva_desc")
	private String subdocivaDesc;

	/** The subdociva importo valuta. */
	@Column(name="subdociva_importo_valuta")
	private BigDecimal subdocivaImportoValuta;

	/** The subdociva numero. */
	@Column(name="subdociva_numero")
	private Integer subdocivaNumero;

	/** The subdociva numordinativodoc. */
	@Column(name="subdociva_numordinativodoc")
	private String subdocivaNumordinativodoc;

	/** The subdociva prot def. */
	@Column(name="subdociva_prot_def")
	private String subdocivaProtDef;

	/** The subdociva prot prov. */
	@Column(name="subdociva_prot_prov")
	private String subdocivaProtProv;

	/** The subdociva soggetto codice. */
	@Column(name="subdociva_soggetto_codice")
	private String subdocivaSoggettoCodice;

	//bi-directional many-to-one association to SiacRIvamov
	/** The siac r ivamovs. */
	@OneToMany(mappedBy="siacTSubdocIva", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRIvamov> siacRIvamovs;

	//bi-directional many-to-one association to SiacRSubdocIva
	/** The siac r subdoc ivas figlio. */
	@OneToMany(mappedBy="siacTSubdocIvaFiglio", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocIva> siacRSubdocIvasFiglio;

	//bi-directional many-to-one association to SiacRSubdocIva
	/** The siac r subdoc ivas padre. */
	@OneToMany(mappedBy="siacTSubdocIvaPadre", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocIva> siacRSubdocIvasPadre;

	//bi-directional many-to-one association to SiacRSubdocIvaAttr
	/** The siac r subdoc iva attrs. */
	@OneToMany(mappedBy="siacTSubdocIva", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocIvaAttr> siacRSubdocIvaAttrs;

	//bi-directional many-to-one association to SiacRSubdocIvaStato
	/** The siac r subdoc iva statos. */
	@OneToMany(mappedBy="siacTSubdocIva", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocIvaStato> siacRSubdocIvaStatos;

	//bi-directional many-to-one association to SiacRSubdocSubdocIva
	/** The siac r subdoc subdoc ivas. */
	@OneToMany(mappedBy="siacTSubdocIva", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocSubdocIva> siacRSubdocSubdocIvas;

	//bi-directional many-to-one association to SiacDIvaRegistrazioneTipo
	/** The siac d iva registrazione tipo. */
	@ManyToOne
	@JoinColumn(name="reg_tipo_id")
	private SiacDIvaRegistrazioneTipo siacDIvaRegistrazioneTipo;

	//bi-directional many-to-one association to SiacDValuta
	/** The siac d valuta. */
	@ManyToOne
	@JoinColumn(name="valuta_id")
	private SiacDValuta siacDValuta;

	//bi-directional many-to-one association to SiacRDocIva
	/** The siac r doc iva. */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumn(name="dociva_r_id")	
	private SiacRDocIva siacRDocIva;

	//bi-directional many-to-one association to SiacTIvaAttivita
	@ManyToOne
	@JoinColumn(name="ivaatt_id")
	private SiacTIvaAttivita siacTIvaAttivita;

	//bi-directional many-to-one association to SiacTIvaRegistro
	/** The siac t iva registro. */
	@ManyToOne
	@JoinColumn(name="ivareg_id")
	private SiacTIvaRegistro siacTIvaRegistro;

	/**
	 * Instantiates a new siac t subdoc iva.
	 */
	public SiacTSubdocIva() {
	}

	/**
	 * Gets the subdociva id.
	 *
	 * @return the subdociva id
	 */
	public Integer getSubdocivaId() {
		return this.subdocivaId;
	}

	/**
	 * Sets the subdociva id.
	 *
	 * @param subdocivaId the new subdociva id
	 */
	public void setSubdocivaId(Integer subdocivaId) {
		this.subdocivaId = subdocivaId;
	}

	/**
	 * Gets the subdociva anno.
	 *
	 * @return the subdociva anno
	 */
	public String getSubdocivaAnno() {
		return this.subdocivaAnno;
	}

	/**
	 * Sets the subdociva anno.
	 *
	 * @param subdocivaAnno the new subdociva anno
	 */
	public void setSubdocivaAnno(String subdocivaAnno) {
		this.subdocivaAnno = subdocivaAnno;
	}

	/**
	 * Gets the subdociva data cassadoc.
	 *
	 * @return the subdociva data cassadoc
	 */
	public Date getSubdocivaDataCassadoc() {
		return this.subdocivaDataCassadoc;
	}

	/**
	 * Sets the subdociva data cassadoc.
	 *
	 * @param subdocivaDataCassadoc the new subdociva data cassadoc
	 */
	public void setSubdocivaDataCassadoc(Date subdocivaDataCassadoc) {
		this.subdocivaDataCassadoc = subdocivaDataCassadoc;
	}

	/**
	 * Gets the subdociva data emissione.
	 *
	 * @return the subdociva data emissione
	 */
	public Date getSubdocivaDataEmissione() {
		return this.subdocivaDataEmissione;
	}

	/**
	 * Sets the subdociva data emissione.
	 *
	 * @param subdocivaDataEmissione the new subdociva data emissione
	 */
	public void setSubdocivaDataEmissione(Date subdocivaDataEmissione) {
		this.subdocivaDataEmissione = subdocivaDataEmissione;
	}

	/**
	 * Gets the subdociva data ordinativoadoc.
	 *
	 * @return the subdociva data ordinativoadoc
	 */
	public Date getSubdocivaDataOrdinativoadoc() {
		return this.subdocivaDataOrdinativoadoc;
	}

	/**
	 * Sets the subdociva data ordinativoadoc.
	 *
	 * @param subdocivaDataOrdinativoadoc the new subdociva data ordinativoadoc
	 */
	public void setSubdocivaDataOrdinativoadoc(Date subdocivaDataOrdinativoadoc) {
		this.subdocivaDataOrdinativoadoc = subdocivaDataOrdinativoadoc;
	}

	/**
	 * Gets the subdociva data prot def.
	 *
	 * @return the subdociva data prot def
	 */
	public Date getSubdocivaDataProtDef() {
		return this.subdocivaDataProtDef;
	}

	/**
	 * Sets the subdociva data prot def.
	 *
	 * @param subdocivaDataProtDef the new subdociva data prot def
	 */
	public void setSubdocivaDataProtDef(Date subdocivaDataProtDef) {
		this.subdocivaDataProtDef = subdocivaDataProtDef;
	}

	/**
	 * Gets the subdociva data prot prov.
	 *
	 * @return the subdociva data prot prov
	 */
	public Date getSubdocivaDataProtProv() {
		return this.subdocivaDataProtProv;
	}

	/**
	 * Sets the subdociva data prot prov.
	 *
	 * @param subdocivaDataProtProv the new subdociva data prot prov
	 */
	public void setSubdocivaDataProtProv(Date subdocivaDataProtProv) {
		this.subdocivaDataProtProv = subdocivaDataProtProv;
	}

	/**
	 * Gets the subdociva data registrazione.
	 *
	 * @return the subdociva data registrazione
	 */
	public Date getSubdocivaDataRegistrazione() {
		return this.subdocivaDataRegistrazione;
	}

	/**
	 * Sets the subdociva data registrazione.
	 *
	 * @param subdocivaDataRegistrazione the new subdociva data registrazione
	 */
	public void setSubdocivaDataRegistrazione(Date subdocivaDataRegistrazione) {
		this.subdocivaDataRegistrazione = subdocivaDataRegistrazione;
	}

	/**
	 * Gets the subdociva data scadenza.
	 *
	 * @return the subdociva data scadenza
	 */
	public Date getSubdocivaDataScadenza() {
		return this.subdocivaDataScadenza;
	}

	/**
	 * Sets the subdociva data scadenza.
	 *
	 * @param subdocivaDataScadenza the new subdociva data scadenza
	 */
	public void setSubdocivaDataScadenza(Date subdocivaDataScadenza) {
		this.subdocivaDataScadenza = subdocivaDataScadenza;
	}

	/**
	 * Gets the subdociva desc.
	 *
	 * @return the subdociva desc
	 */
	public String getSubdocivaDesc() {
		return this.subdocivaDesc;
	}

	/**
	 * Sets the subdociva desc.
	 *
	 * @param subdocivaDesc the new subdociva desc
	 */
	public void setSubdocivaDesc(String subdocivaDesc) {
		this.subdocivaDesc = subdocivaDesc;
	}

	/**
	 * Gets the subdociva importo valuta.
	 *
	 * @return the subdociva importo valuta
	 */
	public BigDecimal getSubdocivaImportoValuta() {
		return this.subdocivaImportoValuta;
	}

	/**
	 * Sets the subdociva importo valuta.
	 *
	 * @param subdocivaImportoValuta the new subdociva importo valuta
	 */
	public void setSubdocivaImportoValuta(BigDecimal subdocivaImportoValuta) {
		this.subdocivaImportoValuta = subdocivaImportoValuta;
	}

	/**
	 * Gets the subdociva numero.
	 *
	 * @return the subdociva numero
	 */
	public Integer getSubdocivaNumero() {
		return this.subdocivaNumero;
	}

	/**
	 * Sets the subdociva numero.
	 *
	 * @param subdocivaNumero the new subdociva numero
	 */
	public void setSubdocivaNumero(Integer subdocivaNumero) {
		this.subdocivaNumero = subdocivaNumero;
	}

	/**
	 * Gets the subdociva numordinativodoc.
	 *
	 * @return the subdociva numordinativodoc
	 */
	public String getSubdocivaNumordinativodoc() {
		return this.subdocivaNumordinativodoc;
	}

	/**
	 * Sets the subdociva numordinativodoc.
	 *
	 * @param subdocivaNumordinativodoc the new subdociva numordinativodoc
	 */
	public void setSubdocivaNumordinativodoc(String subdocivaNumordinativodoc) {
		this.subdocivaNumordinativodoc = subdocivaNumordinativodoc;
	}

	/**
	 * Gets the subdociva prot def.
	 *
	 * @return the subdociva prot def
	 */
	public String getSubdocivaProtDef() {
		return this.subdocivaProtDef;
	}

	/**
	 * Sets the subdociva prot def.
	 *
	 * @param subdocivaProtDef the new subdociva prot def
	 */
	public void setSubdocivaProtDef(String subdocivaProtDef) {
		this.subdocivaProtDef = subdocivaProtDef;
	}

	/**
	 * Gets the subdociva prot prov.
	 *
	 * @return the subdociva prot prov
	 */
	public String getSubdocivaProtProv() {
		return this.subdocivaProtProv;
	}

	/**
	 * Sets the subdociva prot prov.
	 *
	 * @param subdocivaProtProv the new subdociva prot prov
	 */
	public void setSubdocivaProtProv(String subdocivaProtProv) {
		this.subdocivaProtProv = subdocivaProtProv;
	}

	/**
	 * Gets the subdociva soggetto codice.
	 *
	 * @return the subdociva soggetto codice
	 */
	public String getSubdocivaSoggettoCodice() {
		return this.subdocivaSoggettoCodice;
	}

	/**
	 * Sets the subdociva soggetto codice.
	 *
	 * @param subdocivaSoggettoCodice the new subdociva soggetto codice
	 */
	public void setSubdocivaSoggettoCodice(String subdocivaSoggettoCodice) {
		this.subdocivaSoggettoCodice = subdocivaSoggettoCodice;
	}

	/**
	 * Gets the siac r ivamovs.
	 *
	 * @return the siac r ivamovs
	 */
	public List<SiacRIvamov> getSiacRIvamovs() {
		return this.siacRIvamovs;
	}

	/**
	 * Sets the siac r ivamovs.
	 *
	 * @param siacRIvamovs the new siac r ivamovs
	 */
	public void setSiacRIvamovs(List<SiacRIvamov> siacRIvamovs) {
		this.siacRIvamovs = siacRIvamovs;
	}

	/**
	 * Adds the siac r ivamov.
	 *
	 * @param siacRIvamov the siac r ivamov
	 * @return the siac r ivamov
	 */
	public SiacRIvamov addSiacRIvamov(SiacRIvamov siacRIvamov) {
		getSiacRIvamovs().add(siacRIvamov);
		siacRIvamov.setSiacTSubdocIva(this);

		return siacRIvamov;
	}

	/**
	 * Removes the siac r ivamov.
	 *
	 * @param siacRIvamov the siac r ivamov
	 * @return the siac r ivamov
	 */
	public SiacRIvamov removeSiacRIvamov(SiacRIvamov siacRIvamov) {
		getSiacRIvamovs().remove(siacRIvamov);
		siacRIvamov.setSiacTSubdocIva(null);

		return siacRIvamov;
	}

	/**
	 * Gets the siac r subdoc ivas figlio.
	 *
	 * @return the siac r subdoc ivas figlio
	 */
	public List<SiacRSubdocIva> getSiacRSubdocIvasFiglio() {
		return this.siacRSubdocIvasFiglio;
	}

	/**
	 * Sets the siac r subdoc ivas figlio.
	 *
	 * @param siacRSubdocIvas1 the new siac r subdoc ivas figlio
	 */
	public void setSiacRSubdocIvasFiglio(List<SiacRSubdocIva> siacRSubdocIvas1) {
		this.siacRSubdocIvasFiglio = siacRSubdocIvas1;
	}

	/**
	 * Adds the siac r subdoc ivas figlio.
	 *
	 * @param siacRSubdocIvas1 the siac r subdoc ivas1
	 * @return the siac r subdoc iva
	 */
	public SiacRSubdocIva addSiacRSubdocIvasFiglio(SiacRSubdocIva siacRSubdocIvas1) {
		getSiacRSubdocIvasFiglio().add(siacRSubdocIvas1);
		siacRSubdocIvas1.setSiacTSubdocIvaFiglio(this);

		return siacRSubdocIvas1;
	}

	/**
	 * Removes the siac r subdoc ivas figlio.
	 *
	 * @param siacRSubdocIvas1 the siac r subdoc ivas1
	 * @return the siac r subdoc iva
	 */
	public SiacRSubdocIva removeSiacRSubdocIvasFiglio(SiacRSubdocIva siacRSubdocIvas1) {
		getSiacRSubdocIvasFiglio().remove(siacRSubdocIvas1);
		siacRSubdocIvas1.setSiacTSubdocIvaFiglio(null);

		return siacRSubdocIvas1;
	}

	/**
	 * Gets the siac r subdoc ivas padre.
	 *
	 * @return the siac r subdoc ivas padre
	 */
	public List<SiacRSubdocIva> getSiacRSubdocIvasPadre() {
		return this.siacRSubdocIvasPadre;
	}

	/**
	 * Sets the siac r subdoc ivas padre.
	 *
	 * @param siacRSubdocIvas2 the new siac r subdoc ivas padre
	 */
	public void setSiacRSubdocIvasPadre(List<SiacRSubdocIva> siacRSubdocIvas2) {
		this.siacRSubdocIvasPadre = siacRSubdocIvas2;
	}

	/**
	 * Adds the siac r subdoc ivas padre.
	 *
	 * @param siacRSubdocIvas2 the siac r subdoc ivas2
	 * @return the siac r subdoc iva
	 */
	public SiacRSubdocIva addSiacRSubdocIvasPadre(SiacRSubdocIva siacRSubdocIvas2) {
		getSiacRSubdocIvasPadre().add(siacRSubdocIvas2);
		siacRSubdocIvas2.setSiacTSubdocIvaPadre(this);

		return siacRSubdocIvas2;
	}

	/**
	 * Removes the siac r subdoc ivas padre.
	 *
	 * @param siacRSubdocIvas2 the siac r subdoc ivas2
	 * @return the siac r subdoc iva
	 */
	public SiacRSubdocIva removeSiacRSubdocIvasPadre(SiacRSubdocIva siacRSubdocIvas2) {
		getSiacRSubdocIvasPadre().remove(siacRSubdocIvas2);
		siacRSubdocIvas2.setSiacTSubdocIvaPadre(null);

		return siacRSubdocIvas2;
	}

	/**
	 * Gets the siac r subdoc iva attrs.
	 *
	 * @return the siac r subdoc iva attrs
	 */
	public List<SiacRSubdocIvaAttr> getSiacRSubdocIvaAttrs() {
		return this.siacRSubdocIvaAttrs;
	}

	/**
	 * Sets the siac r subdoc iva attrs.
	 *
	 * @param siacRSubdocIvaAttrs the new siac r subdoc iva attrs
	 */
	public void setSiacRSubdocIvaAttrs(List<SiacRSubdocIvaAttr> siacRSubdocIvaAttrs) {
		this.siacRSubdocIvaAttrs = siacRSubdocIvaAttrs;
	}

	/**
	 * Adds the siac r subdoc iva attr.
	 *
	 * @param siacRSubdocIvaAttr the siac r subdoc iva attr
	 * @return the siac r subdoc iva attr
	 */
	public SiacRSubdocIvaAttr addSiacRSubdocIvaAttr(SiacRSubdocIvaAttr siacRSubdocIvaAttr) {
		getSiacRSubdocIvaAttrs().add(siacRSubdocIvaAttr);
		siacRSubdocIvaAttr.setSiacTSubdocIva(this);

		return siacRSubdocIvaAttr;
	}

	/**
	 * Removes the siac r subdoc iva attr.
	 *
	 * @param siacRSubdocIvaAttr the siac r subdoc iva attr
	 * @return the siac r subdoc iva attr
	 */
	public SiacRSubdocIvaAttr removeSiacRSubdocIvaAttr(SiacRSubdocIvaAttr siacRSubdocIvaAttr) {
		getSiacRSubdocIvaAttrs().remove(siacRSubdocIvaAttr);
		siacRSubdocIvaAttr.setSiacTSubdocIva(null);

		return siacRSubdocIvaAttr;
	}

	/**
	 * Gets the siac r subdoc iva statos.
	 *
	 * @return the siac r subdoc iva statos
	 */
	public List<SiacRSubdocIvaStato> getSiacRSubdocIvaStatos() {
		return this.siacRSubdocIvaStatos;
	}

	/**
	 * Sets the siac r subdoc iva statos.
	 *
	 * @param siacRSubdocIvaStatos the new siac r subdoc iva statos
	 */
	public void setSiacRSubdocIvaStatos(List<SiacRSubdocIvaStato> siacRSubdocIvaStatos) {
		this.siacRSubdocIvaStatos = siacRSubdocIvaStatos;
	}

	/**
	 * Adds the siac r subdoc iva stato.
	 *
	 * @param siacRSubdocIvaStato the siac r subdoc iva stato
	 * @return the siac r subdoc iva stato
	 */
	public SiacRSubdocIvaStato addSiacRSubdocIvaStato(SiacRSubdocIvaStato siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().add(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacTSubdocIva(this);

		return siacRSubdocIvaStato;
	}

	/**
	 * Removes the siac r subdoc iva stato.
	 *
	 * @param siacRSubdocIvaStato the siac r subdoc iva stato
	 * @return the siac r subdoc iva stato
	 */
	public SiacRSubdocIvaStato removeSiacRSubdocIvaStato(SiacRSubdocIvaStato siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().remove(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacTSubdocIva(null);

		return siacRSubdocIvaStato;
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
		siacRSubdocSubdocIva.setSiacTSubdocIva(this);

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
		siacRSubdocSubdocIva.setSiacTSubdocIva(null);

		return siacRSubdocSubdocIva;
	}

	/**
	 * Gets the siac d iva registrazione tipo.
	 *
	 * @return the siac d iva registrazione tipo
	 */
	public SiacDIvaRegistrazioneTipo getSiacDIvaRegistrazioneTipo() {
		return this.siacDIvaRegistrazioneTipo;
	}

	/**
	 * Sets the siac d iva registrazione tipo.
	 *
	 * @param siacDIvaRegistrazioneTipo the new siac d iva registrazione tipo
	 */
	public void setSiacDIvaRegistrazioneTipo(SiacDIvaRegistrazioneTipo siacDIvaRegistrazioneTipo) {
		this.siacDIvaRegistrazioneTipo = siacDIvaRegistrazioneTipo;
	}

	/**
	 * Gets the siac d valuta.
	 *
	 * @return the siac d valuta
	 */
	public SiacDValuta getSiacDValuta() {
		return this.siacDValuta;
	}

	/**
	 * Sets the siac d valuta.
	 *
	 * @param siacDValuta the new siac d valuta
	 */
	public void setSiacDValuta(SiacDValuta siacDValuta) {
		this.siacDValuta = siacDValuta;
	}

	/**
	 * Gets the siac r doc iva.
	 *
	 * @return the siac r doc iva
	 */
	public SiacRDocIva getSiacRDocIva() {
		return this.siacRDocIva;
	}

	/**
	 * Sets the siac r doc iva.
	 *
	 * @param siacRDocIva the new siac r doc iva
	 */
	public void setSiacRDocIva(SiacRDocIva siacRDocIva) {
		this.siacRDocIva = siacRDocIva;
	}
	
	/**
	 * Gets the siac t iva attivita.
	 *
	 * @return the siac t iva attivita
	 */
	public SiacTIvaAttivita getSiacTIvaAttivita() {
		return this.siacTIvaAttivita;
	}

	/**
	 * Sets the siac t iva attivita.
	 *
	 * @param siacTIvaAttivita the new siac t iva attivita
	 */
	public void setSiacTIvaAttivita(SiacTIvaAttivita siacTIvaAttivita) {
		this.siacTIvaAttivita = siacTIvaAttivita;
	}

	/**
	 * Gets the siac t iva registro.
	 *
	 * @return the siac t iva registro
	 */
	public SiacTIvaRegistro getSiacTIvaRegistro() {
		return this.siacTIvaRegistro;
	}

	/**
	 * Sets the siac t iva registro.
	 *
	 * @param siacTIvaRegistro the new siac t iva registro
	 */
	public void setSiacTIvaRegistro(SiacTIvaRegistro siacTIvaRegistro) {
		this.siacTIvaRegistro = siacTIvaRegistro;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocivaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocivaId = uid;
	}
	
	

}