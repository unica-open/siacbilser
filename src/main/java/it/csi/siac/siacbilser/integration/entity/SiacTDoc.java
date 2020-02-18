/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_doc database table.
 * 
 */
@Entity
@Table(name="siac_t_doc")
@NamedQuery(name="SiacTDoc.findAll", query="SELECT s FROM SiacTDoc s")
public class SiacTDoc extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_DOC_DOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_DOC_DOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_DOC_DOCID_GENERATOR")
	@Column(name="doc_id")
	private Integer docId;

	@Column(name="doc_anno")
	private Integer docAnno;

	@Column(name="doc_beneficiariomult")
	private Boolean docBeneficiariomult;

	@Column(name="doc_collegato_cec")
	private Boolean docCollegatoCec;

	@Column(name="doc_data_emissione")
	private Date docDataEmissione;

	@Column(name="doc_data_scadenza")
	private Date docDataScadenza;

	@Column(name="doc_desc")
	private String docDesc;

	@Column(name="doc_importo")
	private BigDecimal docImporto;

	@Column(name="doc_numero")
	private String docNumero;
	
	@Column(name="doc_contabilizza_genpcc")
	private Boolean docContabilizzaGenpcc;
	
	@Column(name="doc_sdi_lotto_siope")
	private String docSdiLottoSiope;
	
	@Column(name="stato_sdi")
	private String statoSDI;

	@Column(name="esito_stato_sdi")
	private String esitoStatoSDI;
	
	//SIAC 6677
	@Column(name="doc_data_operazione")
	private Date docDataOperazione;
	
	@Column(name="cod_avviso_pago_pa")
	private String codAvvisoPagoPA;
	

	@Column(name="iuv")
	private String iuv;
	
	
	@Column(name="doc_numero_prima_auto_iva")
	private String docNumeroPrimaAutoIva;
	
	

	//bi-directional many-to-one association to SiacRDoc
	/** The siac r docs padre. */
	@OneToMany(mappedBy="siacTDocPadre", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDoc> siacRDocsPadre;

	//bi-directional many-to-one association to SiacRDoc
	/** The siac r docs figlio. */
	@OneToMany(mappedBy="siacTDocFiglio", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDoc> siacRDocsFiglio;

	//bi-directional many-to-one association to SiacRDocAttr
	/** The siac r doc attrs. */
	@OneToMany(mappedBy="siacTDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDocAttr> siacRDocAttrs;

	//bi-directional many-to-one association to SiacRDocClass
	/** The siac r doc classes. */
	@OneToMany(mappedBy="siacTDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDocClass> siacRDocClasses;

	//bi-directional many-to-one association to SiacRDocIva
	/** The siac r doc ivas. */
	@OneToMany(mappedBy="siacTDoc")
	private List<SiacRDocIva> siacRDocIvas;
	
	//bi-directional many-to-one association to SiacRDocOnere
	/** The siac r doc oneres. */
	@OneToMany(mappedBy="siacTDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDocOnere> siacRDocOneres;

	//bi-directional many-to-one association to SiacRDocSog
	/** The siac r doc sogs. */
	@OneToMany(mappedBy="siacTDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDocSog> siacRDocSogs;

	//bi-directional many-to-one association to SiacRDocStato
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacTDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRDocStato> siacRDocStatos;
	
	//bi-directional many-to-one association to SiacRDocStato
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacTDoc") //NO Cascades!!
	private List<SiacRDocSirfel> siacRDocSirfels;

	//bi-directional many-to-one association to SiacDCodicebollo
	/** The siac d codicebollo. */
	@ManyToOne
	@JoinColumn(name="codbollo_id")
	private SiacDCodicebollo siacDCodicebollo;

	//bi-directional many-to-one association to SiacDDocTipo
	/** The siac d doc tipo. */
	@ManyToOne
	@JoinColumn(name="doc_tipo_id")
	private SiacDDocTipo siacDDocTipo;
	
	//bi-directional many-to-one association to SiacDPccCodice
	@ManyToOne
	@JoinColumn(name="pcccod_id")
	private SiacDPccCodice siacDPccCodice;

	//bi-directional many-to-one association to SiacDPccUfficio
	@ManyToOne
	@JoinColumn(name="pccuff_id")
	private SiacDPccUfficio siacDPccUfficio;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdocs. */
	@OneToMany(mappedBy="siacTDoc")
	private List<SiacTSubdoc> siacTSubdocs;

	//bi-directional many-to-one association to SiacTSubdocNum
	@OneToMany(mappedBy="siacTDoc")
	private List<SiacTSubdocNum> siacTSubdocNums;
	
	//bi-directional many-to-one association to SiacTRegistroPcc
	@OneToMany(mappedBy="siacTDoc")
	private List<SiacTRegistroPcc> siacTRegistroPccs;
	
	//bi-directional many-to-one association to SiacRDocOrdine
	@OneToMany(mappedBy="siacTDoc")
	private List<SiacRDocOrdine> siacRDocOrdines;
	
	
	//bi-directional many-to-one association to SiacTRegistrounicoDoc
	@OneToOne(mappedBy="siacTDoc", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private SiacTRegistrounicoDoc siacTRegistrounicoDoc;

	//bi-directional many-to-one association to SiacDSiopeDocumentoTipo
	/** The siac d siope tipo debito. */
	@ManyToOne
	@JoinColumn(name="siope_documento_tipo_id")
	private SiacDSiopeDocumentoTipo siacDSiopeDocumentoTipo;

	//bi-directional many-to-one association to SiacDSiopeDocumentoTipoAnalogico
	/** The siac d siope tipo debito. */
	@ManyToOne
	@JoinColumn(name="siope_documento_tipo_analogico_id")
	private SiacDSiopeDocumentoTipoAnalogico siacDSiopeDocumentoTipoAnalogico;
	
	public SiacTDoc() {
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if(this.docCollegatoCec == null) {
			this.docCollegatoCec = Boolean.FALSE;
		}
	}

	public Integer getDocId() {
		return this.docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getDocAnno() {
		return this.docAnno;
	}

	public void setDocAnno(Integer docAnno) {
		this.docAnno = docAnno;
	}

	public Boolean getDocBeneficiariomult() {
		return this.docBeneficiariomult;
	}

	public void setDocBeneficiariomult(Boolean docBeneficiariomult) {
		this.docBeneficiariomult = docBeneficiariomult;
	}

	public Boolean getDocCollegatoCec() {
		return this.docCollegatoCec;
	}

	public void setDocCollegatoCec(Boolean docCollegatoCec) {
		this.docCollegatoCec = docCollegatoCec;
	}

	public Date getDocDataEmissione() {
		return this.docDataEmissione;
	}

	public void setDocDataEmissione(Date docDataEmissione) {
		this.docDataEmissione = docDataEmissione;
	}

	public Date getDocDataScadenza() {
		return this.docDataScadenza;
	}

	public void setDocDataScadenza(Date docDataScadenza) {
		this.docDataScadenza = docDataScadenza;
	}

	public String getDocDesc() {
		return this.docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public BigDecimal getDocImporto() {
		return this.docImporto;
	}

	public void setDocImporto(BigDecimal docImporto) {
		this.docImporto = docImporto;
	}

	public String getDocNumero() {
		return this.docNumero;
	}

	public void setDocNumero(String docNumero) {
		this.docNumero = docNumero;
	}

	public Boolean getDocContabilizzaGenpcc() {
		return docContabilizzaGenpcc;
	}

	public void setDocContabilizzaGenpcc(Boolean docContabilizzaGenpcc) {
		this.docContabilizzaGenpcc = docContabilizzaGenpcc;
	}

	/**
	 * Gets the doc sdi lotto siope.
	 * @return the doc sdi lotto siope
	 */
	public String getDocSdiLottoSiope() {
		return docSdiLottoSiope;
	}

	/**
	 * Sets the doc sdi lotto siope.
	 * @param docSdiLottoSiope the new doc sdi lotto siope
	 */
	public void setDocSdiLottoSiope(String docSdiLottoSiope) {
		this.docSdiLottoSiope = docSdiLottoSiope;
	}

	/**
	 * Gets the siac r docs padre.
	 *
	 * @return the siac r docs padre
	 */
	public List<SiacRDoc> getSiacRDocsPadre() {
		return this.siacRDocsPadre;
	}

	/**
	 * Sets the siac r docs padre.
	 *
	 * @param siacRDocs1 the new siac r docs padre
	 */
	public void setSiacRDocsPadre(List<SiacRDoc> siacRDocs1) {
		this.siacRDocsPadre = siacRDocs1;
	}

	/**
	 * Adds the siac r docs padre.
	 *
	 * @param siacRDocs1 the siac r docs1
	 * @return the siac r doc
	 */
	public SiacRDoc addSiacRDocsPadre(SiacRDoc siacRDocs1) {		
		if(getSiacRDocsPadre()==null){
			setSiacRDocsPadre(new ArrayList<SiacRDoc>());
		}
		getSiacRDocsPadre().add(siacRDocs1);
		siacRDocs1.setSiacTDocPadre(this);

		return siacRDocs1;
	}

	/**
	 * Removes the siac r docs padre.
	 *
	 * @param siacRDocs1 the siac r docs1
	 * @return the siac r doc
	 */
	public SiacRDoc removeSiacRDocsPadre(SiacRDoc siacRDocs1) {
		getSiacRDocsPadre().remove(siacRDocs1);
		siacRDocs1.setSiacTDocPadre(null);

		return siacRDocs1;
	}

	/**
	 * Gets the siac r docs figlio.
	 *
	 * @return the siac r docs figlio
	 */
	public List<SiacRDoc> getSiacRDocsFiglio() {
		return this.siacRDocsFiglio;
	}

	/**
	 * Sets the siac r docs figlio.
	 *
	 * @param siacRDocs2 the new siac r docs figlio
	 */
	public void setSiacRDocsFiglio(List<SiacRDoc> siacRDocs2) {
		this.siacRDocsFiglio = siacRDocs2;
	}

	/**
	 * Adds the siac r docs figlio.
	 *
	 * @param siacRDocs2 the siac r docs2
	 * @return the siac r doc
	 */
	public SiacRDoc addSiacRDocsFiglio(SiacRDoc siacRDocs2) {
		if(getSiacRDocsFiglio()==null){
			setSiacRDocsFiglio(new ArrayList<SiacRDoc>());
		}
		getSiacRDocsFiglio().add(siacRDocs2);
		siacRDocs2.setSiacTDocFiglio(this);

		return siacRDocs2;
	}

	/**
	 * Removes the siac r docs figlio.
	 *
	 * @param siacRDocs2 the siac r docs2
	 * @return the siac r doc
	 */
	public SiacRDoc removeSiacRDocsFiglio(SiacRDoc siacRDocs2) {
		getSiacRDocsFiglio().remove(siacRDocs2);
		siacRDocs2.setSiacTDocFiglio(null);

		return siacRDocs2;
	}

	/**
	 * Gets the siac r doc attrs.
	 *
	 * @return the siac r doc attrs
	 */
	public List<SiacRDocAttr> getSiacRDocAttrs() {
		return this.siacRDocAttrs;
	}

	/**
	 * Sets the siac r doc attrs.
	 *
	 * @param siacRDocAttrs the new siac r doc attrs
	 */
	public void setSiacRDocAttrs(List<SiacRDocAttr> siacRDocAttrs) {
		this.siacRDocAttrs = siacRDocAttrs;
	}

	/**
	 * Adds the siac r doc attr.
	 *
	 * @param siacRDocAttr the siac r doc attr
	 * @return the siac r doc attr
	 */
	public SiacRDocAttr addSiacRDocAttr(SiacRDocAttr siacRDocAttr) {
		getSiacRDocAttrs().add(siacRDocAttr);
		siacRDocAttr.setSiacTDoc(this);

		return siacRDocAttr;
	}

	/**
	 * Removes the siac r doc attr.
	 *
	 * @param siacRDocAttr the siac r doc attr
	 * @return the siac r doc attr
	 */
	public SiacRDocAttr removeSiacRDocAttr(SiacRDocAttr siacRDocAttr) {
		getSiacRDocAttrs().remove(siacRDocAttr);
		siacRDocAttr.setSiacTDoc(null);

		return siacRDocAttr;
	}

	/**
	 * Gets the siac r doc classes.
	 *
	 * @return the siac r doc classes
	 */
	public List<SiacRDocClass> getSiacRDocClasses() {
		return this.siacRDocClasses;
	}

	/**
	 * Sets the siac r doc classes.
	 *
	 * @param siacRDocClasses the new siac r doc classes
	 */
	public void setSiacRDocClasses(List<SiacRDocClass> siacRDocClasses) {
		this.siacRDocClasses = siacRDocClasses;
	}

	/**
	 * Adds the siac r doc class.
	 *
	 * @param siacRDocClass the siac r doc class
	 * @return the siac r doc class
	 */
	public SiacRDocClass addSiacRDocClass(SiacRDocClass siacRDocClass) {
		getSiacRDocClasses().add(siacRDocClass);
		siacRDocClass.setSiacTDoc(this);

		return siacRDocClass;
	}

	/**
	 * Removes the siac r doc class.
	 *
	 * @param siacRDocClass the siac r doc class
	 * @return the siac r doc class
	 */
	public SiacRDocClass removeSiacRDocClass(SiacRDocClass siacRDocClass) {
		getSiacRDocClasses().remove(siacRDocClass);
		siacRDocClass.setSiacTDoc(null);

		return siacRDocClass;
	}
	
	/**
	 * Gets the siac r doc ivas.
	 *
	 * @return the siac r doc ivas
	 */
	public List<SiacRDocIva> getSiacRDocIvas() {
		return this.siacRDocIvas;
	}

	/**
	 * Sets the siac r doc ivas.
	 *
	 * @param siacRDocIvas the new siac r doc ivas
	 */
	public void setSiacRDocIvas(List<SiacRDocIva> siacRDocIvas) {
		this.siacRDocIvas = siacRDocIvas;
	}

	/**
	 * Adds the siac r doc iva.
	 *
	 * @param siacRDocIva the siac r doc iva
	 * @return the siac r doc iva
	 */
	public SiacRDocIva addSiacRDocIva(SiacRDocIva siacRDocIva) {
		getSiacRDocIvas().add(siacRDocIva);
		siacRDocIva.setSiacTDoc(this);

		return siacRDocIva;
	}

	/**
	 * Removes the siac r doc iva.
	 *
	 * @param siacRDocIva the siac r doc iva
	 * @return the siac r doc iva
	 */
	public SiacRDocIva removeSiacRDocIva(SiacRDocIva siacRDocIva) {
		getSiacRDocIvas().remove(siacRDocIva);
		siacRDocIva.setSiacTDoc(null);

		return siacRDocIva;
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
		siacRDocOnere.setSiacTDoc(this);

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
		siacRDocOnere.setSiacTDoc(null);

		return siacRDocOnere;
	}

	/**
	 * Gets the siac r doc sogs.
	 *
	 * @return the siac r doc sogs
	 */
	public List<SiacRDocSog> getSiacRDocSogs() {
		return this.siacRDocSogs;
	}

	/**
	 * Sets the siac r doc sogs.
	 *
	 * @param siacRDocSogs the new siac r doc sogs
	 */
	public void setSiacRDocSogs(List<SiacRDocSog> siacRDocSogs) {
		this.siacRDocSogs = siacRDocSogs;
	}

	/**
	 * Adds the siac r doc sog.
	 *
	 * @param siacRDocSog the siac r doc sog
	 * @return the siac r doc sog
	 */
	public SiacRDocSog addSiacRDocSog(SiacRDocSog siacRDocSog) {
		getSiacRDocSogs().add(siacRDocSog);
		siacRDocSog.setSiacTDoc(this);

		return siacRDocSog;
	}

	/**
	 * Removes the siac r doc sog.
	 *
	 * @param siacRDocSog the siac r doc sog
	 * @return the siac r doc sog
	 */
	public SiacRDocSog removeSiacRDocSog(SiacRDocSog siacRDocSog) {
		getSiacRDocSogs().remove(siacRDocSog);
		siacRDocSog.setSiacTDoc(null);

		return siacRDocSog;
	}

	/**
	 * Gets the siac r doc statos.
	 *
	 * @return the siac r doc statos
	 */
	public List<SiacRDocStato> getSiacRDocStatos() {
		return this.siacRDocStatos;
	}

	/**
	 * Sets the siac r doc statos.
	 *
	 * @param siacRDocStatos the new siac r doc statos
	 */
	public void setSiacRDocStatos(List<SiacRDocStato> siacRDocStatos) {
		this.siacRDocStatos = siacRDocStatos;
	}

	/**
	 * Adds the siac r doc stato.
	 *
	 * @param siacRDocStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRDocStato addSiacRDocStato(SiacRDocStato siacRDocStato) {
		getSiacRDocStatos().add(siacRDocStato);
		siacRDocStato.setSiacTDoc(this);

		return siacRDocStato;
	}

	/**
	 * Removes the siac r doc stato.
	 *
	 * @param siacRDocStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRDocStato removeSiacRDocStato(SiacRDocStato siacRDocStato) {
		getSiacRDocStatos().remove(siacRDocStato);
		siacRDocStato.setSiacTDoc(null);

		return siacRDocStato;
	}
	
	
	/**
	 * Gets the siac r doc sirfels.
	 *
	 * @return the siac r doc sirfels
	 */
	public List<SiacRDocSirfel> getSiacRDocSirfels() {
		return this.siacRDocSirfels;
	}

	/**
	 * Sets the siac r doc sirfels.
	 *
	 * @param siacRDocSirfels the new siac r doc sirfels
	 */
	public void setSiacRDocSirfels(List<SiacRDocSirfel> siacRDocSirfels) {
		this.siacRDocSirfels = siacRDocSirfels;
	}

	/**
	 * Adds the siac r doc sirfel.
	 *
	 * @param siacRDocSirfel the siac r doc sirfel
	 * @return the siac r doc sirfel
	 */
	public SiacRDocSirfel addSiacRDocSirfel(SiacRDocSirfel siacRDocSirfel) {
		getSiacRDocSirfels().add(siacRDocSirfel);
		siacRDocSirfel.setSiacTDoc(this);

		return siacRDocSirfel;
	}

	/**
	 * Removes the siac r doc sirfel.
	 *
	 * @param siacRDocSirfel the siac r doc sirfel
	 * @return the siac r doc sirfel
	 */
	public SiacRDocSirfel removeSiacRDocSirfel(SiacRDocSirfel siacRDocSirfel) {
		getSiacRDocSirfels().remove(siacRDocSirfel);
		siacRDocSirfel.setSiacTDoc(null);

		return siacRDocSirfel;
	}
	
	
	/**
	 * Gets the siac d codicebollo.
	 *
	 * @return the siac d codicebollo
	 */
	public SiacDCodicebollo getSiacDCodicebollo() {
		return this.siacDCodicebollo;
	}

	/**
	 * Sets the siac d codicebollo.
	 *
	 * @param siacDCodicebollo the new siac d codicebollo
	 */
	public void setSiacDCodicebollo(SiacDCodicebollo siacDCodicebollo) {
		this.siacDCodicebollo = siacDCodicebollo;
	}

	/**
	 * Gets the siac d doc tipo.
	 *
	 * @return the siac d doc tipo
	 */
	public SiacDDocTipo getSiacDDocTipo() {
		return this.siacDDocTipo;
	}

	/**
	 * Sets the siac d doc tipo.
	 *
	 * @param siacDDocTipo the new siac d doc tipo
	 */
	public void setSiacDDocTipo(SiacDDocTipo siacDDocTipo) {
		this.siacDDocTipo = siacDDocTipo;
	}


	/**
	 * Gets the siac d pcc codice.
	 *
	 * @return the siacDPccCodice
	 */
	public SiacDPccCodice getSiacDPccCodice() {
		return siacDPccCodice;
	}

	/**
	 * Sets the siac d pcc codice.
	 *
	 * @param siacDPccCodice the siacDPccCodice to set
	 */
	public void setSiacDPccCodice(SiacDPccCodice siacDPccCodice) {
		this.siacDPccCodice = siacDPccCodice;
	}
	
	/**
	 * Gets the siac d pcc ufficio.
	 *
	 * @return the siacDPccUfficio
	 */
	public SiacDPccUfficio getSiacDPccUfficio() {
		return siacDPccUfficio;
	}

	/**
	 * Sets the siac d pcc ufficio.
	 *
	 * @param siacDPccUfficio the siacDPccUfficio to set
	 */
	public void setSiacDPccUfficio(SiacDPccUfficio siacDPccUfficio) {
		this.siacDPccUfficio = siacDPccUfficio;
	}

	/**
	 * Gets the siac t subdocs.
	 *
	 * @return the siac t subdocs
	 */
	public List<SiacTSubdoc> getSiacTSubdocs() {
		return this.siacTSubdocs;
	}

	/**
	 * Sets the siac t subdocs.
	 *
	 * @param siacTSubdocs the new siac t subdocs
	 */
	public void setSiacTSubdocs(List<SiacTSubdoc> siacTSubdocs) {
		this.siacTSubdocs = siacTSubdocs;
	}

	/**
	 * Adds the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc addSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().add(siacTSubdoc);
		siacTSubdoc.setSiacTDoc(this);

		return siacTSubdoc;
	}

	/**
	 * Removes the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc removeSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacTDoc(null);

		return siacTSubdoc;
	}

	/**
	 * Gets the siac t subdoc nums.
	 *
	 * @return the siac t subdoc nums
	 */
	public List<SiacTSubdocNum> getSiacTSubdocNums() {
		return this.siacTSubdocNums;
	}

	/**
	 * Sets the siac t subdoc nums.
	 *
	 * @param siacTSubdocNums the new siac t subdoc nums
	 */
	public void setSiacTSubdocNums(List<SiacTSubdocNum> siacTSubdocNums) {
		this.siacTSubdocNums = siacTSubdocNums;
	}

	/**
	 * Adds the siac t subdoc num.
	 *
	 * @param siacTSubdocNum the siac t subdoc num
	 * @return the siac t subdoc num
	 */
	public SiacTSubdocNum addSiacTSubdocNum(SiacTSubdocNum siacTSubdocNum) {
		getSiacTSubdocNums().add(siacTSubdocNum);
		siacTSubdocNum.setSiacTDoc(this);

		return siacTSubdocNum;
	}

	/**
	 * Removes the siac t subdoc num.
	 *
	 * @param siacTSubdocNum the siac t subdoc num
	 * @return the siac t subdoc num
	 */
	public SiacTSubdocNum removeSiacTSubdocNum(SiacTSubdocNum siacTSubdocNum) {
		getSiacTSubdocNums().remove(siacTSubdocNum);
		siacTSubdocNum.setSiacTDoc(null);

		return siacTSubdocNum;
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
		siacTRegistroPcc.setSiacTDoc(this);

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
		siacTRegistroPcc.setSiacTDoc(null);

		return siacTRegistroPcc;
	}

	public List<SiacRDocOrdine> getSiacRDocOrdines() {
		return this.siacRDocOrdines;
	}

	public void setSiacRDocOrdines(List<SiacRDocOrdine> siacRDocOrdines) {
		this.siacRDocOrdines = siacRDocOrdines;
	}

	public SiacRDocOrdine addSiacRDocOrdine(SiacRDocOrdine siacRDocOrdine) {
		getSiacRDocOrdines().add(siacRDocOrdine);
		siacRDocOrdine.setSiacTDoc(this);

		return siacRDocOrdine;
	}

	public SiacRDocOrdine removeSiacRDocOrdine(SiacRDocOrdine siacRDocOrdine) {
		getSiacRDocOrdines().remove(siacRDocOrdine);
		siacRDocOrdine.setSiacTDoc(null);

		return siacRDocOrdine;
	}

	/**
	 * @return the siacTRegistrounicoDoc
	 */
	public SiacTRegistrounicoDoc getSiacTRegistrounicoDoc() {
		return siacTRegistrounicoDoc;
	}

	/**
	 * @param siacTRegistrounicoDoc the siacTRegistrounicoDoc to set
	 */
	public void setSiacTRegistrounicoDoc(SiacTRegistrounicoDoc siacTRegistrounicoDoc) {
		this.siacTRegistrounicoDoc = siacTRegistrounicoDoc;
	}

	/**
	 * Gets the siac d siope documento tipo.
	 *
	 * @return the siac d siope documento tipo
	 */
	public SiacDSiopeDocumentoTipo getSiacDSiopeDocumentoTipo() {
		return this.siacDSiopeDocumentoTipo;
	}

	/**
	 * Sets the siac d siope documento tipo.
	 *
	 * @param siacDSiopeDocumentoTipo the new siac d siope documento tipo
	 */
	public void setSiacDSiopeDocumentoTipo(SiacDSiopeDocumentoTipo siacDSiopeDocumentoTipo) {
		this.siacDSiopeDocumentoTipo = siacDSiopeDocumentoTipo;
	}

	/**
	 * Gets the siac d siope documento tipo analogico.
	 *
	 * @return the siac d siope documento tipo analogico
	 */
	public SiacDSiopeDocumentoTipoAnalogico getSiacDSiopeDocumentoTipoAnalogico() {
		return this.siacDSiopeDocumentoTipoAnalogico;
	}

	/**
	 * Sets the siac d siope documento tipo analogico.
	 *
	 * @param siacDSiopeDocumentoTipoAnalogico the new siac d siope documento tipo analogico
	 */
	public void setSiacDSiopeDocumentoTipoAnalogico(SiacDSiopeDocumentoTipoAnalogico siacDSiopeDocumentoTipoAnalogico) {
		this.siacDSiopeDocumentoTipoAnalogico = siacDSiopeDocumentoTipoAnalogico;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.docId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docId = uid;
		
	}

	public String getStatoSDI() {
		return statoSDI;
	}

	public void setStatoSDI(String statoSDI) {
		this.statoSDI = statoSDI;
	}

	public String getEsitoStatoSDI() {
		return esitoStatoSDI;
	}

	public void setEsitoStatoSDI(String esitoStatoSDI) {
		this.esitoStatoSDI = esitoStatoSDI;
	}

	public Date getDocDataOperazione() {
		return this.docDataOperazione;
	}

	public void setDocDataOperazione(Date docDataOperazione) {
		this.docDataOperazione = docDataOperazione;
	}
	
	public String getCodAvvisoPagoPA() {
		return this.codAvvisoPagoPA;
	}

	public void setCodAvvisoPagoPA(String codAvvisoPagoPA) {
		this.codAvvisoPagoPA = codAvvisoPagoPA;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	
	
	public String getDocNumeroPrimaAutoIva() {
		return docNumeroPrimaAutoIva;
	}

	public void setDocNumeroPrimaAutoIva(String docNumeroPrimaAutoIva) {
		this.docNumeroPrimaAutoIva = docNumeroPrimaAutoIva;
	}
	
}