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
 * The persistent class for the siac_t_liquidazione database table.
 * 
 */
@Entity
@Table(name="siac_t_liquidazione")
@NamedQuery(name="SiacTLiquidazione.findAll", query="SELECT s FROM SiacTLiquidazione s")
public class SiacTLiquidazione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq id. */
	@Id
	@SequenceGenerator(name="SIAC_T_LIQUIDAZIONE_LIQID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_LIQUIDAZIONE_LIQ_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_LIQUIDAZIONE_LIQID_GENERATOR")
	@Column(name="liq_id")
	private Integer liqId;

	/** The liq anno. */
	@Column(name="liq_anno")
	private Integer liqAnno;

	/** The liq automatica. */
	@Column(name="liq_automatica")
	private String liqAutomatica;

	/** The liq convalida manuale. */
	@Column(name="liq_convalida_manuale")
	private String liqConvalidaManuale;

	/** The liq desc. */
	@Column(name="liq_desc")
	private String liqDesc;

	/** The liq emissione data. */
	@Column(name="liq_emissione_data")
	private Date liqEmissioneData;

	/** The liq importo. */
	@Column(name="liq_importo")
	private BigDecimal liqImporto;

	/** The liq numero. */
	@Column(name="liq_numero")
	private BigDecimal liqNumero;

	//bi-directional many-to-one association to SiacRLiquidazioneAttoAmm
	/** The siac r liquidazione atto amms. */
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneAttoAmm> siacRLiquidazioneAttoAmms;

	//bi-directional many-to-one association to SiacRLiquidazioneAttr
	/** The siac r liquidazione attrs. */
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneAttr> siacRLiquidazioneAttrs;

	//bi-directional many-to-one association to SiacRLiquidazioneClass
	/** The siac r liquidazione classes. */
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneClass> siacRLiquidazioneClasses;

	//bi-directional many-to-one association to SiacRLiquidazioneMovgest
	/** The siac r liquidazione movgests. */
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneMovgest> siacRLiquidazioneMovgests;

	//bi-directional many-to-one association to SiacRLiquidazioneOrd
	/** The siac r liquidazione ords. */
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneOrd> siacRLiquidazioneOrds;

	//bi-directional many-to-one association to SiacRLiquidazioneSoggetto
	/** The siac r liquidazione soggettos. */
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneSoggetto> siacRLiquidazioneSoggettos;

	//bi-directional many-to-one association to SiacRLiquidazioneStato
	/** The siac r liquidazione statos. */
	@OneToMany(mappedBy="siacTLiquidazione",cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRLiquidazioneStato> siacRLiquidazioneStatos;

	//bi-directional many-to-one association to SiacRSubdocLiquidazione
	/** The siac r subdoc liquidaziones. */
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRSubdocLiquidazione> siacRSubdocLiquidaziones;

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

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;
	
	@Column(name="soggetto_relaz_id")
	private Integer cessioneId;

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
	 * Instantiates a new siac t liquidazione.
	 */
	public SiacTLiquidazione() {
	}

	/**
	 * Gets the liq id.
	 *
	 * @return the liq id
	 */
	public Integer getLiqId() {
		return this.liqId;
	}

	/**
	 * Sets the liq id.
	 *
	 * @param liqId the new liq id
	 */
	public void setLiqId(Integer liqId) {
		this.liqId = liqId;
	}

	/**
	 * Gets the liq anno.
	 *
	 * @return the liq anno
	 */
	public Integer getLiqAnno() {
		return this.liqAnno;
	}

	/**
	 * Sets the liq anno.
	 *
	 * @param liqAnno the new liq anno
	 */
	public void setLiqAnno(Integer liqAnno) {
		this.liqAnno = liqAnno;
	}

	/**
	 * Gets the liq automatica.
	 *
	 * @return the liq automatica
	 */
	public String getLiqAutomatica() {
		return this.liqAutomatica;
	}

	/**
	 * Sets the liq automatica.
	 *
	 * @param liqAutomatica the new liq automatica
	 */
	public void setLiqAutomatica(String liqAutomatica) {
		this.liqAutomatica = liqAutomatica;
	}

	/**
	 * Gets the liq convalida manuale.
	 *
	 * @return the liq convalida manuale
	 */
	public String getLiqConvalidaManuale() {
		return this.liqConvalidaManuale;
	}

	/**
	 * Sets the liq convalida manuale.
	 *
	 * @param liqConvalidaManuale the new liq convalida manuale
	 */
	public void setLiqConvalidaManuale(String liqConvalidaManuale) {
		this.liqConvalidaManuale = liqConvalidaManuale;
	}

	/**
	 * Gets the liq desc.
	 *
	 * @return the liq desc
	 */
	public String getLiqDesc() {
		return this.liqDesc;
	}

	/**
	 * Sets the liq desc.
	 *
	 * @param liqDesc the new liq desc
	 */
	public void setLiqDesc(String liqDesc) {
		this.liqDesc = liqDesc;
	}

	/**
	 * Gets the liq emissione data.
	 *
	 * @return the liq emissione data
	 */
	public Date getLiqEmissioneData() {
		return this.liqEmissioneData;
	}

	/**
	 * Sets the liq emissione data.
	 *
	 * @param liqEmissioneData the new liq emissione data
	 */
	public void setLiqEmissioneData(Date liqEmissioneData) {
		this.liqEmissioneData = liqEmissioneData;
	}

	/**
	 * Gets the liq importo.
	 *
	 * @return the liq importo
	 */
	public BigDecimal getLiqImporto() {
		return this.liqImporto;
	}

	/**
	 * Sets the liq importo.
	 *
	 * @param liqImporto the new liq importo
	 */
	public void setLiqImporto(BigDecimal liqImporto) {
		this.liqImporto = liqImporto;
	}

	/**
	 * Gets the liq numero.
	 *
	 * @return the liq numero
	 */
	public BigDecimal getLiqNumero() {
		return this.liqNumero;
	}

	/**
	 * Sets the liq numero.
	 *
	 * @param liqNumero the new liq numero
	 */
	public void setLiqNumero(BigDecimal liqNumero) {
		this.liqNumero = liqNumero;
	}

	

	/**
	 * Gets the siac r liquidazione atto amms.
	 *
	 * @return the siac r liquidazione atto amms
	 */
	public List<SiacRLiquidazioneAttoAmm> getSiacRLiquidazioneAttoAmms() {
		return this.siacRLiquidazioneAttoAmms;
	}

	/**
	 * Sets the siac r liquidazione atto amms.
	 *
	 * @param siacRLiquidazioneAttoAmms the new siac r liquidazione atto amms
	 */
	public void setSiacRLiquidazioneAttoAmms(List<SiacRLiquidazioneAttoAmm> siacRLiquidazioneAttoAmms) {
		this.siacRLiquidazioneAttoAmms = siacRLiquidazioneAttoAmms;
	}

	/**
	 * Adds the siac r liquidazione atto amm.
	 *
	 * @param siacRLiquidazioneAttoAmm the siac r liquidazione atto amm
	 * @return the siac r liquidazione atto amm
	 */
	public SiacRLiquidazioneAttoAmm addSiacRLiquidazioneAttoAmm(SiacRLiquidazioneAttoAmm siacRLiquidazioneAttoAmm) {
		getSiacRLiquidazioneAttoAmms().add(siacRLiquidazioneAttoAmm);
		siacRLiquidazioneAttoAmm.setSiacTLiquidazione(this);

		return siacRLiquidazioneAttoAmm;
	}

	/**
	 * Removes the siac r liquidazione atto amm.
	 *
	 * @param siacRLiquidazioneAttoAmm the siac r liquidazione atto amm
	 * @return the siac r liquidazione atto amm
	 */
	public SiacRLiquidazioneAttoAmm removeSiacRLiquidazioneAttoAmm(SiacRLiquidazioneAttoAmm siacRLiquidazioneAttoAmm) {
		getSiacRLiquidazioneAttoAmms().remove(siacRLiquidazioneAttoAmm);
		siacRLiquidazioneAttoAmm.setSiacTLiquidazione(null);

		return siacRLiquidazioneAttoAmm;
	}

	/**
	 * Gets the siac r liquidazione attrs.
	 *
	 * @return the siac r liquidazione attrs
	 */
	public List<SiacRLiquidazioneAttr> getSiacRLiquidazioneAttrs() {
		return this.siacRLiquidazioneAttrs;
	}

	/**
	 * Sets the siac r liquidazione attrs.
	 *
	 * @param siacRLiquidazioneAttrs the new siac r liquidazione attrs
	 */
	public void setSiacRLiquidazioneAttrs(List<SiacRLiquidazioneAttr> siacRLiquidazioneAttrs) {
		this.siacRLiquidazioneAttrs = siacRLiquidazioneAttrs;
	}

	/**
	 * Adds the siac r liquidazione attr.
	 *
	 * @param siacRLiquidazioneAttr the siac r liquidazione attr
	 * @return the siac r liquidazione attr
	 */
	public SiacRLiquidazioneAttr addSiacRLiquidazioneAttr(SiacRLiquidazioneAttr siacRLiquidazioneAttr) {
		getSiacRLiquidazioneAttrs().add(siacRLiquidazioneAttr);
		siacRLiquidazioneAttr.setSiacTLiquidazione(this);

		return siacRLiquidazioneAttr;
	}

	/**
	 * Removes the siac r liquidazione attr.
	 *
	 * @param siacRLiquidazioneAttr the siac r liquidazione attr
	 * @return the siac r liquidazione attr
	 */
	public SiacRLiquidazioneAttr removeSiacRLiquidazioneAttr(SiacRLiquidazioneAttr siacRLiquidazioneAttr) {
		getSiacRLiquidazioneAttrs().remove(siacRLiquidazioneAttr);
		siacRLiquidazioneAttr.setSiacTLiquidazione(null);

		return siacRLiquidazioneAttr;
	}

	/**
	 * Gets the siac r liquidazione classes.
	 *
	 * @return the siac r liquidazione classes
	 */
	public List<SiacRLiquidazioneClass> getSiacRLiquidazioneClasses() {
		return this.siacRLiquidazioneClasses;
	}

	/**
	 * Sets the siac r liquidazione classes.
	 *
	 * @param siacRLiquidazioneClasses the new siac r liquidazione classes
	 */
	public void setSiacRLiquidazioneClasses(List<SiacRLiquidazioneClass> siacRLiquidazioneClasses) {
		this.siacRLiquidazioneClasses = siacRLiquidazioneClasses;
	}

	/**
	 * Adds the siac r liquidazione class.
	 *
	 * @param siacRLiquidazioneClass the siac r liquidazione class
	 * @return the siac r liquidazione class
	 */
	public SiacRLiquidazioneClass addSiacRLiquidazioneClass(SiacRLiquidazioneClass siacRLiquidazioneClass) {
		getSiacRLiquidazioneClasses().add(siacRLiquidazioneClass);
		siacRLiquidazioneClass.setSiacTLiquidazione(this);

		return siacRLiquidazioneClass;
	}

	/**
	 * Removes the siac r liquidazione class.
	 *
	 * @param siacRLiquidazioneClass the siac r liquidazione class
	 * @return the siac r liquidazione class
	 */
	public SiacRLiquidazioneClass removeSiacRLiquidazioneClass(SiacRLiquidazioneClass siacRLiquidazioneClass) {
		getSiacRLiquidazioneClasses().remove(siacRLiquidazioneClass);
		siacRLiquidazioneClass.setSiacTLiquidazione(null);

		return siacRLiquidazioneClass;
	}

	/**
	 * Gets the siac r liquidazione movgests.
	 *
	 * @return the siac r liquidazione movgests
	 */
	public List<SiacRLiquidazioneMovgest> getSiacRLiquidazioneMovgests() {
		return this.siacRLiquidazioneMovgests;
	}

	/**
	 * Sets the siac r liquidazione movgests.
	 *
	 * @param siacRLiquidazioneMovgests the new siac r liquidazione movgests
	 */
	public void setSiacRLiquidazioneMovgests(List<SiacRLiquidazioneMovgest> siacRLiquidazioneMovgests) {
		this.siacRLiquidazioneMovgests = siacRLiquidazioneMovgests;
	}

	/**
	 * Adds the siac r liquidazione movgest.
	 *
	 * @param siacRLiquidazioneMovgest the siac r liquidazione movgest
	 * @return the siac r liquidazione movgest
	 */
	public SiacRLiquidazioneMovgest addSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgest siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().add(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTLiquidazione(this);

		return siacRLiquidazioneMovgest;
	}

	/**
	 * Removes the siac r liquidazione movgest.
	 *
	 * @param siacRLiquidazioneMovgest the siac r liquidazione movgest
	 * @return the siac r liquidazione movgest
	 */
	public SiacRLiquidazioneMovgest removeSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgest siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().remove(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTLiquidazione(null);

		return siacRLiquidazioneMovgest;
	}

	/**
	 * Gets the siac r liquidazione ords.
	 *
	 * @return the siac r liquidazione ords
	 */
	public List<SiacRLiquidazioneOrd> getSiacRLiquidazioneOrds() {
		return this.siacRLiquidazioneOrds;
	}

	/**
	 * Sets the siac r liquidazione ords.
	 *
	 * @param siacRLiquidazioneOrds the new siac r liquidazione ords
	 */
	public void setSiacRLiquidazioneOrds(List<SiacRLiquidazioneOrd> siacRLiquidazioneOrds) {
		this.siacRLiquidazioneOrds = siacRLiquidazioneOrds;
	}

	/**
	 * Adds the siac r liquidazione ord.
	 *
	 * @param siacRLiquidazioneOrd the siac r liquidazione ord
	 * @return the siac r liquidazione ord
	 */
	public SiacRLiquidazioneOrd addSiacRLiquidazioneOrd(SiacRLiquidazioneOrd siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().add(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTLiquidazione(this);

		return siacRLiquidazioneOrd;
	}

	/**
	 * Removes the siac r liquidazione ord.
	 *
	 * @param siacRLiquidazioneOrd the siac r liquidazione ord
	 * @return the siac r liquidazione ord
	 */
	public SiacRLiquidazioneOrd removeSiacRLiquidazioneOrd(SiacRLiquidazioneOrd siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().remove(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTLiquidazione(null);

		return siacRLiquidazioneOrd;
	}

	/**
	 * Gets the siac r liquidazione soggettos.
	 *
	 * @return the siac r liquidazione soggettos
	 */
	public List<SiacRLiquidazioneSoggetto> getSiacRLiquidazioneSoggettos() {
		return this.siacRLiquidazioneSoggettos;
	}

	/**
	 * Sets the siac r liquidazione soggettos.
	 *
	 * @param siacRLiquidazioneSoggettos the new siac r liquidazione soggettos
	 */
	public void setSiacRLiquidazioneSoggettos(List<SiacRLiquidazioneSoggetto> siacRLiquidazioneSoggettos) {
		this.siacRLiquidazioneSoggettos = siacRLiquidazioneSoggettos;
	}

	/**
	 * Adds the siac r liquidazione soggetto.
	 *
	 * @param siacRLiquidazioneSoggetto the siac r liquidazione soggetto
	 * @return the siac r liquidazione soggetto
	 */
	public SiacRLiquidazioneSoggetto addSiacRLiquidazioneSoggetto(SiacRLiquidazioneSoggetto siacRLiquidazioneSoggetto) {
		getSiacRLiquidazioneSoggettos().add(siacRLiquidazioneSoggetto);
		siacRLiquidazioneSoggetto.setSiacTLiquidazione(this);

		return siacRLiquidazioneSoggetto;
	}

	/**
	 * Removes the siac r liquidazione soggetto.
	 *
	 * @param siacRLiquidazioneSoggetto the siac r liquidazione soggetto
	 * @return the siac r liquidazione soggetto
	 */
	public SiacRLiquidazioneSoggetto removeSiacRLiquidazioneSoggetto(SiacRLiquidazioneSoggetto siacRLiquidazioneSoggetto) {
		getSiacRLiquidazioneSoggettos().remove(siacRLiquidazioneSoggetto);
		siacRLiquidazioneSoggetto.setSiacTLiquidazione(null);

		return siacRLiquidazioneSoggetto;
	}

	/**
	 * Gets the siac r liquidazione statos.
	 *
	 * @return the siac r liquidazione statos
	 */
	public List<SiacRLiquidazioneStato> getSiacRLiquidazioneStatos() {
		return this.siacRLiquidazioneStatos;
	}

	/**
	 * Sets the siac r liquidazione statos.
	 *
	 * @param siacRLiquidazioneStatos the new siac r liquidazione statos
	 */
	public void setSiacRLiquidazioneStatos(List<SiacRLiquidazioneStato> siacRLiquidazioneStatos) {
		this.siacRLiquidazioneStatos = siacRLiquidazioneStatos;
	}

	/**
	 * Adds the siac r liquidazione stato.
	 *
	 * @param siacRLiquidazioneStato the siac r liquidazione stato
	 * @return the siac r liquidazione stato
	 */
	public SiacRLiquidazioneStato addSiacRLiquidazioneStato(SiacRLiquidazioneStato siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().add(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacTLiquidazione(this);

		return siacRLiquidazioneStato;
	}

	/**
	 * Removes the siac r liquidazione stato.
	 *
	 * @param siacRLiquidazioneStato the siac r liquidazione stato
	 * @return the siac r liquidazione stato
	 */
	public SiacRLiquidazioneStato removeSiacRLiquidazioneStato(SiacRLiquidazioneStato siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().remove(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacTLiquidazione(null);

		return siacRLiquidazioneStato;
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
		siacRSubdocLiquidazione.setSiacTLiquidazione(this);

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
		siacRSubdocLiquidazione.setSiacTLiquidazione(null);

		return siacRSubdocLiquidazione;
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
	 * Gets the siac t modpag.
	 *
	 * @return the siac t modpag
	 */
	public SiacTModpag getSiacTModpag() {
		return this.siacTModpag;
	}

	/**
	 * Sets the siac t modpag.
	 *
	 * @param siacTModpag the new siac t modpag
	 */
	public void setSiacTModpag(SiacTModpag siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	/**
	 * @return the cessioneId
	 */
	public Integer getCessioneId() {
		return cessioneId;
	}

	/**
	 * @param cessioneId the cessioneId to set
	 */
	public void setCessioneId(Integer cessioneId) {
		this.cessioneId = cessioneId;
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
		return liqId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqId = uid;
		
	}

}