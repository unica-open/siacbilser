/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
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


/**
 * The persistent class for the siac_t_ordinativo database table.
 * 
 */
@Entity
@Table(name="siac_t_ordinativo")
@NamedQuery(name="SiacTOrdinativo.findAll", query="SELECT s FROM SiacTOrdinativo s")
public class SiacTOrdinativo extends SiacTEnteBaseExt {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ORDINATIVO_ORDID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ORDINATIVO_ORD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ORDINATIVO_ORDID_GENERATOR")
	@Column(name="ord_id")
	private Integer ordId;

	/** The ord anno. */
	@Column(name="ord_anno")
	private Integer ordAnno;

	/** The ord beneficiariomult. */
	@Column(name="ord_beneficiariomult")
	private Boolean ordBeneficiariomult;

	/** The ord cast cassa. */
	@Column(name="ord_cast_cassa")
	private BigDecimal ordCastCassa;

	/** The ord cast competenza. */
	@Column(name="ord_cast_competenza")
	private BigDecimal ordCastCompetenza;

	/** The ord cast emessi. */
	@Column(name="ord_cast_emessi")
	private BigDecimal ordCastEmessi;

	/** The ord desc. */
	@Column(name="ord_desc")
	private String ordDesc;

	/** The ord emissione data. */
	@Column(name="ord_emissione_data")
	private Date ordEmissioneData;

	/** The ord numero. */
	@Column(name="ord_numero")
	private BigDecimal ordNumero;

	/** The ord riduzione data. */
	@Column(name="ord_riduzione_data")
	private Date ordRiduzioneData;

	/** The ord spostamento data. */
	@Column(name="ord_spostamento_data")
	private Date ordSpostamentoData;

	/** The ord variazione data. */
	@Column(name="ord_variazione_data")
	private Date ordVariazioneData;

	//bi-directional many-to-one association to SiacROrdinativo
	/** The siac r ordinativos1. */
	@OneToMany(mappedBy="siacTOrdinativo1")
	private List<SiacROrdinativo> siacROrdinativos1;

	//bi-directional many-to-one association to SiacROrdinativo
	/** The siac r ordinativos2. */
	@OneToMany(mappedBy="siacTOrdinativo2")
	private List<SiacROrdinativo> siacROrdinativos2;

	//bi-directional many-to-one association to SiacROrdinativoAttoAmm
	/** The siac r ordinativo atto amms. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoAttoAmm> siacROrdinativoAttoAmms;

	//bi-directional many-to-one association to SiacROrdinativoAttr
	/** The siac r ordinativo attrs. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoAttr> siacROrdinativoAttrs;

	//bi-directional many-to-one association to SiacROrdinativoBilElem
	/** The siac r ordinativo bil elems. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoBilElem> siacROrdinativoBilElems;

	//bi-directional many-to-one association to SiacROrdinativoClass
	/** The siac r ordinativo classes. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoClass> siacROrdinativoClasses;

	//bi-directional many-to-one association to SiacROrdinativoModpag
	/** The siac r ordinativo modpags. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoModpag> siacROrdinativoModpags;

	//bi-directional many-to-one association to SiacROrdinativoProvCassa
	/** The siac r ordinativo prov cassas. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoProvCassa> siacROrdinativoProvCassas;

	//bi-directional many-to-one association to SiacROrdinativoSoggetto
	/** The siac r ordinativo soggettos. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoSoggetto> siacROrdinativoSoggettos;

	//bi-directional many-to-one association to SiacROrdinativoStato
	/** The siac r ordinativo statos. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoStato> siacROrdinativoStatos;

	//bi-directional many-to-one association to SiacDCodicebollo
	/** The siac d codicebollo. */
	@ManyToOne
	@JoinColumn(name="codbollo_id")
	private SiacDCodicebollo siacDCodicebollo;

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

	//bi-directional many-to-one association to SiacDOrdinativoTipo
	/** The siac d ordinativo tipo. */
	@ManyToOne
	@JoinColumn(name="ord_tipo_id")
	private SiacDOrdinativoTipo siacDOrdinativoTipo;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTOrdinativoT
	/** The siac t ordinativo ts. */
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacTOrdinativoT> siacTOrdinativoTs;

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

	/**
	 * Instantiates a new siac t ordinativo.
	 */
	public SiacTOrdinativo() {
	}

	/**
	 * Gets the ord id.
	 *
	 * @return the ord id
	 */
	public Integer getOrdId() {
		return this.ordId;
	}

	/**
	 * Sets the ord id.
	 *
	 * @param ordId the new ord id
	 */
	public void setOrdId(Integer ordId) {
		this.ordId = ordId;
	}

	/**
	 * Gets the ord anno.
	 *
	 * @return the ord anno
	 */
	public Integer getOrdAnno() {
		return this.ordAnno;
	}

	/**
	 * Sets the ord anno.
	 *
	 * @param ordAnno the new ord anno
	 */
	public void setOrdAnno(Integer ordAnno) {
		this.ordAnno = ordAnno;
	}

	/**
	 * Gets the ord beneficiariomult.
	 *
	 * @return the ord beneficiariomult
	 */
	public Boolean getOrdBeneficiariomult() {
		return this.ordBeneficiariomult;
	}

	/**
	 * Sets the ord beneficiariomult.
	 *
	 * @param ordBeneficiariomult the new ord beneficiariomult
	 */
	public void setOrdBeneficiariomult(Boolean ordBeneficiariomult) {
		this.ordBeneficiariomult = ordBeneficiariomult;
	}

	/**
	 * Gets the ord cast cassa.
	 *
	 * @return the ord cast cassa
	 */
	public BigDecimal getOrdCastCassa() {
		return this.ordCastCassa;
	}

	/**
	 * Sets the ord cast cassa.
	 *
	 * @param ordCastCassa the new ord cast cassa
	 */
	public void setOrdCastCassa(BigDecimal ordCastCassa) {
		this.ordCastCassa = ordCastCassa;
	}

	/**
	 * Gets the ord cast competenza.
	 *
	 * @return the ord cast competenza
	 */
	public BigDecimal getOrdCastCompetenza() {
		return this.ordCastCompetenza;
	}

	/**
	 * Sets the ord cast competenza.
	 *
	 * @param ordCastCompetenza the new ord cast competenza
	 */
	public void setOrdCastCompetenza(BigDecimal ordCastCompetenza) {
		this.ordCastCompetenza = ordCastCompetenza;
	}

	/**
	 * Gets the ord cast emessi.
	 *
	 * @return the ord cast emessi
	 */
	public BigDecimal getOrdCastEmessi() {
		return this.ordCastEmessi;
	}

	/**
	 * Sets the ord cast emessi.
	 *
	 * @param ordCastEmessi the new ord cast emessi
	 */
	public void setOrdCastEmessi(BigDecimal ordCastEmessi) {
		this.ordCastEmessi = ordCastEmessi;
	}

	/**
	 * Gets the ord desc.
	 *
	 * @return the ord desc
	 */
	public String getOrdDesc() {
		return this.ordDesc;
	}

	/**
	 * Sets the ord desc.
	 *
	 * @param ordDesc the new ord desc
	 */
	public void setOrdDesc(String ordDesc) {
		this.ordDesc = ordDesc;
	}

	/**
	 * Gets the ord emissione data.
	 *
	 * @return the ord emissione data
	 */
	public Date getOrdEmissioneData() {
		return this.ordEmissioneData;
	}

	/**
	 * Sets the ord emissione data.
	 *
	 * @param ordEmissioneData the new ord emissione data
	 */
	public void setOrdEmissioneData(Date ordEmissioneData) {
		this.ordEmissioneData = ordEmissioneData;
	}

	/**
	 * Gets the ord numero.
	 *
	 * @return the ord numero
	 */
	public BigDecimal getOrdNumero() {
		return this.ordNumero;
	}

	/**
	 * Sets the ord numero.
	 *
	 * @param ordNumero the new ord numero
	 */
	public void setOrdNumero(BigDecimal ordNumero) {
		this.ordNumero = ordNumero;
	}

	/**
	 * Gets the ord riduzione data.
	 *
	 * @return the ord riduzione data
	 */
	public Date getOrdRiduzioneData() {
		return this.ordRiduzioneData;
	}

	/**
	 * Sets the ord riduzione data.
	 *
	 * @param ordRiduzioneData the new ord riduzione data
	 */
	public void setOrdRiduzioneData(Date ordRiduzioneData) {
		this.ordRiduzioneData = ordRiduzioneData;
	}

	/**
	 * Gets the ord spostamento data.
	 *
	 * @return the ord spostamento data
	 */
	public Date getOrdSpostamentoData() {
		return this.ordSpostamentoData;
	}

	/**
	 * Sets the ord spostamento data.
	 *
	 * @param ordSpostamentoData the new ord spostamento data
	 */
	public void setOrdSpostamentoData(Date ordSpostamentoData) {
		this.ordSpostamentoData = ordSpostamentoData;
	}

	/**
	 * Gets the ord variazione data.
	 *
	 * @return the ord variazione data
	 */
	public Date getOrdVariazioneData() {
		return this.ordVariazioneData;
	}

	/**
	 * Sets the ord variazione data.
	 *
	 * @param ordVariazioneData the new ord variazione data
	 */
	public void setOrdVariazioneData(Date ordVariazioneData) {
		this.ordVariazioneData = ordVariazioneData;
	}

	/**
	 * Gets the siac r ordinativos1.
	 *
	 * @return the siac r ordinativos1
	 */
	public List<SiacROrdinativo> getSiacROrdinativos1() {
		return this.siacROrdinativos1;
	}

	/**
	 * Sets the siac r ordinativos1.
	 *
	 * @param siacROrdinativos1 the new siac r ordinativos1
	 */
	public void setSiacROrdinativos1(List<SiacROrdinativo> siacROrdinativos1) {
		this.siacROrdinativos1 = siacROrdinativos1;
	}

	/**
	 * Adds the siac r ordinativos1.
	 *
	 * @param siacROrdinativos1 the siac r ordinativos1
	 * @return the siac r ordinativo
	 */
	public SiacROrdinativo addSiacROrdinativos1(SiacROrdinativo siacROrdinativos1) {
		getSiacROrdinativos1().add(siacROrdinativos1);
		siacROrdinativos1.setSiacTOrdinativo1(this);

		return siacROrdinativos1;
	}

	/**
	 * Removes the siac r ordinativos1.
	 *
	 * @param siacROrdinativos1 the siac r ordinativos1
	 * @return the siac r ordinativo
	 */
	public SiacROrdinativo removeSiacROrdinativos1(SiacROrdinativo siacROrdinativos1) {
		getSiacROrdinativos1().remove(siacROrdinativos1);
		siacROrdinativos1.setSiacTOrdinativo1(null);

		return siacROrdinativos1;
	}

	/**
	 * Gets the siac r ordinativos2.
	 *
	 * @return the siac r ordinativos2
	 */
	public List<SiacROrdinativo> getSiacROrdinativos2() {
		return this.siacROrdinativos2;
	}

	/**
	 * Sets the siac r ordinativos2.
	 *
	 * @param siacROrdinativos2 the new siac r ordinativos2
	 */
	public void setSiacROrdinativos2(List<SiacROrdinativo> siacROrdinativos2) {
		this.siacROrdinativos2 = siacROrdinativos2;
	}

	/**
	 * Adds the siac r ordinativos2.
	 *
	 * @param siacROrdinativos2 the siac r ordinativos2
	 * @return the siac r ordinativo
	 */
	public SiacROrdinativo addSiacROrdinativos2(SiacROrdinativo siacROrdinativos2) {
		getSiacROrdinativos2().add(siacROrdinativos2);
		siacROrdinativos2.setSiacTOrdinativo2(this);

		return siacROrdinativos2;
	}

	/**
	 * Removes the siac r ordinativos2.
	 *
	 * @param siacROrdinativos2 the siac r ordinativos2
	 * @return the siac r ordinativo
	 */
	public SiacROrdinativo removeSiacROrdinativos2(SiacROrdinativo siacROrdinativos2) {
		getSiacROrdinativos2().remove(siacROrdinativos2);
		siacROrdinativos2.setSiacTOrdinativo2(null);

		return siacROrdinativos2;
	}

	/**
	 * Gets the siac r ordinativo atto amms.
	 *
	 * @return the siac r ordinativo atto amms
	 */
	public List<SiacROrdinativoAttoAmm> getSiacROrdinativoAttoAmms() {
		return this.siacROrdinativoAttoAmms;
	}

	/**
	 * Sets the siac r ordinativo atto amms.
	 *
	 * @param siacROrdinativoAttoAmms the new siac r ordinativo atto amms
	 */
	public void setSiacROrdinativoAttoAmms(List<SiacROrdinativoAttoAmm> siacROrdinativoAttoAmms) {
		this.siacROrdinativoAttoAmms = siacROrdinativoAttoAmms;
	}

	/**
	 * Adds the siac r ordinativo atto amm.
	 *
	 * @param siacROrdinativoAttoAmm the siac r ordinativo atto amm
	 * @return the siac r ordinativo atto amm
	 */
	public SiacROrdinativoAttoAmm addSiacROrdinativoAttoAmm(SiacROrdinativoAttoAmm siacROrdinativoAttoAmm) {
		getSiacROrdinativoAttoAmms().add(siacROrdinativoAttoAmm);
		siacROrdinativoAttoAmm.setSiacTOrdinativo(this);

		return siacROrdinativoAttoAmm;
	}

	/**
	 * Removes the siac r ordinativo atto amm.
	 *
	 * @param siacROrdinativoAttoAmm the siac r ordinativo atto amm
	 * @return the siac r ordinativo atto amm
	 */
	public SiacROrdinativoAttoAmm removeSiacROrdinativoAttoAmm(SiacROrdinativoAttoAmm siacROrdinativoAttoAmm) {
		getSiacROrdinativoAttoAmms().remove(siacROrdinativoAttoAmm);
		siacROrdinativoAttoAmm.setSiacTOrdinativo(null);

		return siacROrdinativoAttoAmm;
	}

	/**
	 * Gets the siac r ordinativo attrs.
	 *
	 * @return the siac r ordinativo attrs
	 */
	public List<SiacROrdinativoAttr> getSiacROrdinativoAttrs() {
		return this.siacROrdinativoAttrs;
	}

	/**
	 * Sets the siac r ordinativo attrs.
	 *
	 * @param siacROrdinativoAttrs the new siac r ordinativo attrs
	 */
	public void setSiacROrdinativoAttrs(List<SiacROrdinativoAttr> siacROrdinativoAttrs) {
		this.siacROrdinativoAttrs = siacROrdinativoAttrs;
	}

	/**
	 * Adds the siac r ordinativo attr.
	 *
	 * @param siacROrdinativoAttr the siac r ordinativo attr
	 * @return the siac r ordinativo attr
	 */
	public SiacROrdinativoAttr addSiacROrdinativoAttr(SiacROrdinativoAttr siacROrdinativoAttr) {
		getSiacROrdinativoAttrs().add(siacROrdinativoAttr);
		siacROrdinativoAttr.setSiacTOrdinativo(this);

		return siacROrdinativoAttr;
	}

	/**
	 * Removes the siac r ordinativo attr.
	 *
	 * @param siacROrdinativoAttr the siac r ordinativo attr
	 * @return the siac r ordinativo attr
	 */
	public SiacROrdinativoAttr removeSiacROrdinativoAttr(SiacROrdinativoAttr siacROrdinativoAttr) {
		getSiacROrdinativoAttrs().remove(siacROrdinativoAttr);
		siacROrdinativoAttr.setSiacTOrdinativo(null);

		return siacROrdinativoAttr;
	}

	/**
	 * Gets the siac r ordinativo bil elems.
	 *
	 * @return the siac r ordinativo bil elems
	 */
	public List<SiacROrdinativoBilElem> getSiacROrdinativoBilElems() {
		return this.siacROrdinativoBilElems;
	}

	/**
	 * Sets the siac r ordinativo bil elems.
	 *
	 * @param siacROrdinativoBilElems the new siac r ordinativo bil elems
	 */
	public void setSiacROrdinativoBilElems(List<SiacROrdinativoBilElem> siacROrdinativoBilElems) {
		this.siacROrdinativoBilElems = siacROrdinativoBilElems;
	}

	/**
	 * Adds the siac r ordinativo bil elem.
	 *
	 * @param siacROrdinativoBilElem the siac r ordinativo bil elem
	 * @return the siac r ordinativo bil elem
	 */
	public SiacROrdinativoBilElem addSiacROrdinativoBilElem(SiacROrdinativoBilElem siacROrdinativoBilElem) {
		getSiacROrdinativoBilElems().add(siacROrdinativoBilElem);
		siacROrdinativoBilElem.setSiacTOrdinativo(this);

		return siacROrdinativoBilElem;
	}

	/**
	 * Removes the siac r ordinativo bil elem.
	 *
	 * @param siacROrdinativoBilElem the siac r ordinativo bil elem
	 * @return the siac r ordinativo bil elem
	 */
	public SiacROrdinativoBilElem removeSiacROrdinativoBilElem(SiacROrdinativoBilElem siacROrdinativoBilElem) {
		getSiacROrdinativoBilElems().remove(siacROrdinativoBilElem);
		siacROrdinativoBilElem.setSiacTOrdinativo(null);

		return siacROrdinativoBilElem;
	}

	/**
	 * Gets the siac r ordinativo classes.
	 *
	 * @return the siac r ordinativo classes
	 */
	public List<SiacROrdinativoClass> getSiacROrdinativoClasses() {
		return this.siacROrdinativoClasses;
	}

	/**
	 * Sets the siac r ordinativo classes.
	 *
	 * @param siacROrdinativoClasses the new siac r ordinativo classes
	 */
	public void setSiacROrdinativoClasses(List<SiacROrdinativoClass> siacROrdinativoClasses) {
		this.siacROrdinativoClasses = siacROrdinativoClasses;
	}

	/**
	 * Adds the siac r ordinativo class.
	 *
	 * @param siacROrdinativoClass the siac r ordinativo class
	 * @return the siac r ordinativo class
	 */
	public SiacROrdinativoClass addSiacROrdinativoClass(SiacROrdinativoClass siacROrdinativoClass) {
		getSiacROrdinativoClasses().add(siacROrdinativoClass);
		siacROrdinativoClass.setSiacTOrdinativo(this);

		return siacROrdinativoClass;
	}

	/**
	 * Removes the siac r ordinativo class.
	 *
	 * @param siacROrdinativoClass the siac r ordinativo class
	 * @return the siac r ordinativo class
	 */
	public SiacROrdinativoClass removeSiacROrdinativoClass(SiacROrdinativoClass siacROrdinativoClass) {
		getSiacROrdinativoClasses().remove(siacROrdinativoClass);
		siacROrdinativoClass.setSiacTOrdinativo(null);

		return siacROrdinativoClass;
	}

	/**
	 * Gets the siac r ordinativo modpags.
	 *
	 * @return the siac r ordinativo modpags
	 */
	public List<SiacROrdinativoModpag> getSiacROrdinativoModpags() {
		return this.siacROrdinativoModpags;
	}

	/**
	 * Sets the siac r ordinativo modpags.
	 *
	 * @param siacROrdinativoModpags the new siac r ordinativo modpags
	 */
	public void setSiacROrdinativoModpags(List<SiacROrdinativoModpag> siacROrdinativoModpags) {
		this.siacROrdinativoModpags = siacROrdinativoModpags;
	}

	/**
	 * Adds the siac r ordinativo modpag.
	 *
	 * @param siacROrdinativoModpag the siac r ordinativo modpag
	 * @return the siac r ordinativo modpag
	 */
	public SiacROrdinativoModpag addSiacROrdinativoModpag(SiacROrdinativoModpag siacROrdinativoModpag) {
		getSiacROrdinativoModpags().add(siacROrdinativoModpag);
		siacROrdinativoModpag.setSiacTOrdinativo(this);

		return siacROrdinativoModpag;
	}

	/**
	 * Removes the siac r ordinativo modpag.
	 *
	 * @param siacROrdinativoModpag the siac r ordinativo modpag
	 * @return the siac r ordinativo modpag
	 */
	public SiacROrdinativoModpag removeSiacROrdinativoModpag(SiacROrdinativoModpag siacROrdinativoModpag) {
		getSiacROrdinativoModpags().remove(siacROrdinativoModpag);
		siacROrdinativoModpag.setSiacTOrdinativo(null);

		return siacROrdinativoModpag;
	}

	/**
	 * Gets the siac r ordinativo prov cassas.
	 *
	 * @return the siac r ordinativo prov cassas
	 */
	public List<SiacROrdinativoProvCassa> getSiacROrdinativoProvCassas() {
		return this.siacROrdinativoProvCassas;
	}

	/**
	 * Sets the siac r ordinativo prov cassas.
	 *
	 * @param siacROrdinativoProvCassas the new siac r ordinativo prov cassas
	 */
	public void setSiacROrdinativoProvCassas(List<SiacROrdinativoProvCassa> siacROrdinativoProvCassas) {
		this.siacROrdinativoProvCassas = siacROrdinativoProvCassas;
	}

	/**
	 * Adds the siac r ordinativo prov cassa.
	 *
	 * @param siacROrdinativoProvCassa the siac r ordinativo prov cassa
	 * @return the siac r ordinativo prov cassa
	 */
	public SiacROrdinativoProvCassa addSiacROrdinativoProvCassa(SiacROrdinativoProvCassa siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().add(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTOrdinativo(this);

		return siacROrdinativoProvCassa;
	}

	/**
	 * Removes the siac r ordinativo prov cassa.
	 *
	 * @param siacROrdinativoProvCassa the siac r ordinativo prov cassa
	 * @return the siac r ordinativo prov cassa
	 */
	public SiacROrdinativoProvCassa removeSiacROrdinativoProvCassa(SiacROrdinativoProvCassa siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().remove(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTOrdinativo(null);

		return siacROrdinativoProvCassa;
	}

	/**
	 * Gets the siac r ordinativo soggettos.
	 *
	 * @return the siac r ordinativo soggettos
	 */
	public List<SiacROrdinativoSoggetto> getSiacROrdinativoSoggettos() {
		return this.siacROrdinativoSoggettos;
	}

	/**
	 * Sets the siac r ordinativo soggettos.
	 *
	 * @param siacROrdinativoSoggettos the new siac r ordinativo soggettos
	 */
	public void setSiacROrdinativoSoggettos(List<SiacROrdinativoSoggetto> siacROrdinativoSoggettos) {
		this.siacROrdinativoSoggettos = siacROrdinativoSoggettos;
	}

	/**
	 * Adds the siac r ordinativo soggetto.
	 *
	 * @param siacROrdinativoSoggetto the siac r ordinativo soggetto
	 * @return the siac r ordinativo soggetto
	 */
	public SiacROrdinativoSoggetto addSiacROrdinativoSoggetto(SiacROrdinativoSoggetto siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().add(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTOrdinativo(this);

		return siacROrdinativoSoggetto;
	}

	/**
	 * Removes the siac r ordinativo soggetto.
	 *
	 * @param siacROrdinativoSoggetto the siac r ordinativo soggetto
	 * @return the siac r ordinativo soggetto
	 */
	public SiacROrdinativoSoggetto removeSiacROrdinativoSoggetto(SiacROrdinativoSoggetto siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().remove(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTOrdinativo(null);

		return siacROrdinativoSoggetto;
	}

	/**
	 * Gets the siac r ordinativo statos.
	 *
	 * @return the siac r ordinativo statos
	 */
	public List<SiacROrdinativoStato> getSiacROrdinativoStatos() {
		return this.siacROrdinativoStatos;
	}

	/**
	 * Sets the siac r ordinativo statos.
	 *
	 * @param siacROrdinativoStatos the new siac r ordinativo statos
	 */
	public void setSiacROrdinativoStatos(List<SiacROrdinativoStato> siacROrdinativoStatos) {
		this.siacROrdinativoStatos = siacROrdinativoStatos;
	}

	/**
	 * Adds the siac r ordinativo stato.
	 *
	 * @param siacROrdinativoStato the siac r ordinativo stato
	 * @return the siac r ordinativo stato
	 */
	public SiacROrdinativoStato addSiacROrdinativoStato(SiacROrdinativoStato siacROrdinativoStato) {
		getSiacROrdinativoStatos().add(siacROrdinativoStato);
		siacROrdinativoStato.setSiacTOrdinativo(this);

		return siacROrdinativoStato;
	}

	/**
	 * Removes the siac r ordinativo stato.
	 *
	 * @param siacROrdinativoStato the siac r ordinativo stato
	 * @return the siac r ordinativo stato
	 */
	public SiacROrdinativoStato removeSiacROrdinativoStato(SiacROrdinativoStato siacROrdinativoStato) {
		getSiacROrdinativoStatos().remove(siacROrdinativoStato);
		siacROrdinativoStato.setSiacTOrdinativo(null);

		return siacROrdinativoStato;
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
	 * Gets the siac d commissione tipo.
	 *
	 * @return the siac d commissione tipo
	 */
	public SiacDCommissioneTipo getSiacDCommissioneTipo() {
		return this.siacDCommissioneTipo;
	}

	/**
	 * Sets the siac d commissione tipo.
	 *
	 * @param siacDCommissioneTipo the new siac d commissione tipo
	 */
	public void setSiacDCommissioneTipo(SiacDCommissioneTipo siacDCommissioneTipo) {
		this.siacDCommissioneTipo = siacDCommissioneTipo;
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
	 * Gets the siac d distinta.
	 *
	 * @return the siac d distinta
	 */
	public SiacDDistinta getSiacDDistinta() {
		return this.siacDDistinta;
	}

	/**
	 * Sets the siac d distinta.
	 *
	 * @param siacDDistinta the new siac d distinta
	 */
	public void setSiacDDistinta(SiacDDistinta siacDDistinta) {
		this.siacDDistinta = siacDDistinta;
	}

	/**
	 * Gets the siac d note tesoriere.
	 *
	 * @return the siac d note tesoriere
	 */
	public SiacDNoteTesoriere getSiacDNoteTesoriere() {
		return this.siacDNoteTesoriere;
	}

	/**
	 * Sets the siac d note tesoriere.
	 *
	 * @param siacDNoteTesoriere the new siac d note tesoriere
	 */
	public void setSiacDNoteTesoriere(SiacDNoteTesoriere siacDNoteTesoriere) {
		this.siacDNoteTesoriere = siacDNoteTesoriere;
	}

	/**
	 * Gets the siac d ordinativo tipo.
	 *
	 * @return the siac d ordinativo tipo
	 */
	public SiacDOrdinativoTipo getSiacDOrdinativoTipo() {
		return this.siacDOrdinativoTipo;
	}

	/**
	 * Sets the siac d ordinativo tipo.
	 *
	 * @param siacDOrdinativoTipo the new siac d ordinativo tipo
	 */
	public void setSiacDOrdinativoTipo(SiacDOrdinativoTipo siacDOrdinativoTipo) {
		this.siacDOrdinativoTipo = siacDOrdinativoTipo;
	}

	/**
	 * Gets the siac t bil.
	 *
	 * @return the siac t bil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the new siac t bil
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/**
	 * Gets the siac t ordinativo ts.
	 *
	 * @return the siac t ordinativo ts
	 */
	public List<SiacTOrdinativoT> getSiacTOrdinativoTs() {
		return this.siacTOrdinativoTs;
	}

	/**
	 * Sets the siac t ordinativo ts.
	 *
	 * @param siacTOrdinativoTs the new siac t ordinativo ts
	 */
	public void setSiacTOrdinativoTs(List<SiacTOrdinativoT> siacTOrdinativoTs) {
		this.siacTOrdinativoTs = siacTOrdinativoTs;
	}

	/**
	 * Adds the siac t ordinativo t.
	 *
	 * @param siacTOrdinativoT the siac t ordinativo t
	 * @return the siac t ordinativo t
	 */
	public SiacTOrdinativoT addSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
		getSiacTOrdinativoTs().add(siacTOrdinativoT);
		siacTOrdinativoT.setSiacTOrdinativo(this);

		return siacTOrdinativoT;
	}

	/**
	 * Removes the siac t ordinativo t.
	 *
	 * @param siacTOrdinativoT the siac t ordinativo t
	 * @return the siac t ordinativo t
	 */
	public SiacTOrdinativoT removeSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
		getSiacTOrdinativoTs().remove(siacTOrdinativoT);
		siacTOrdinativoT.setSiacTOrdinativo(null);

		return siacTOrdinativoT;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordId = uid;
	}

}