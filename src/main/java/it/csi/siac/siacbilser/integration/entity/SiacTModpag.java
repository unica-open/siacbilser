/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_modpag database table.
 * 
 */
@Entity
@Table(name="siac_t_modpag")
@NamedQuery(name="SiacTModpag.findAll", query="SELECT s FROM SiacTModpag s")
public class SiacTModpag extends SiacTEnteBaseExt {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The modpag id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MODPAG_MODPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MODPAG_MODPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODPAG_MODPAGID_GENERATOR")
	@Column(name="modpag_id")
	private Integer modpagId;

	/** The bic. */
	private String bic;

	/** The contocorrente. */
	private String contocorrente;

	@Column(name="contocorrente_intestazione")
	private String contocorrenteIntestazione;

	/** The data scadenza. */
	@Column(name="data_scadenza")
	private Date dataScadenza;

	/** The iban. */
	private String iban;


	/** The note. */
	private String note;

	@Column(name="quietanzante_nascita_data")
	private Date quietanzanteNascitaData;

	/** The quietanziante. */
	private String quietanziante;

	/** The quietanziante codice fiscale. */
	@Column(name="quietanziante_codice_fiscale")
	private String quietanzianteCodiceFiscale;

	@Column(name="quietanziante_nascita_luogo")
	private String quietanzianteNascitaLuogo;

	@Column(name="quietanziante_nascita_stato")
	private String quietanzianteNascitaStato;

	@Column(name="banca_denominazione")
	private String bancaDenominazione;
	
	// SIAC-4551
	@Column(name="per_stipendi")
	private Boolean perStipendi;

	//bi-directional many-to-one association to SiacRCartacontDetModpag
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRCartacontDetModpag> siacRCartacontDetModpags;

	//bi-directional many-to-one association to SiacRCausaleModpag
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRCausaleModpag> siacRCausaleModpags;

	//bi-directional many-to-one association to SiacRModpagOrdine
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRModpagOrdine> siacRModpagOrdines;

	//bi-directional many-to-one association to SiacRModpagStato
	/** The siac r modpag statos. */
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRModpagStato> siacRModpagStatos;

	//bi-directional many-to-one association to SiacROrdinativoModpag
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacROrdinativoModpag> siacROrdinativoModpags;

	//bi-directional many-to-one association to SiacRPredocModpag
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRPredocModpag> siacRPredocModpags;

	//bi-directional many-to-one association to SiacRSoggrelModpag
	/** The siac r soggrel modpags. */
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRSoggrelModpag> siacRSoggrelModpags;

	//bi-directional many-to-one association to SiacRSoggrelModpagMod
	/** The siac r soggrel modpag mods. */
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRSoggrelModpagMod> siacRSoggrelModpagMods;

	//bi-directional many-to-one association to SiacRSubdocModpag
	/** The siac r subdoc modpags. */
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRSubdocModpag> siacRSubdocModpags;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidaziones. */
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacTLiquidazione> siacTLiquidaziones;

	//bi-directional many-to-one association to SiacDAccreditoTipo
	/** The siac d accredito tipo. */
	@ManyToOne
	@JoinColumn(name="accredito_tipo_id")
	private SiacDAccreditoTipo siacDAccreditoTipo;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	//bi-directional many-to-one association to SiacTModpagMod
	/** The siac t modpag mods. */
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacTModpagMod> siacTModpagMods;

	/**
	 * Instantiates a new siac t modpag.
	 */
	public SiacTModpag() {
	}

	/**
	 * Gets the modpag id.
	 *
	 * @return the modpag id
	 */
	public Integer getModpagId() {
		return this.modpagId;
	}

	/**
	 * Sets the modpag id.
	 *
	 * @param modpagId the new modpag id
	 */
	public void setModpagId(Integer modpagId) {
		this.modpagId = modpagId;
	}

	/**
	 * Gets the bic.
	 *
	 * @return the bic
	 */
	public String getBic() {
		return this.bic;
	}

	/**
	 * Sets the bic.
	 *
	 * @param bic the new bic
	 */
	public void setBic(String bic) {
		this.bic = bic;
	}

	/**
	 * Gets the contocorrente.
	 *
	 * @return the contocorrente
	 */
	public String getContocorrente() {
		return this.contocorrente;
	}

	/**
	 * Sets the contocorrente.
	 *
	 * @param contocorrente the new contocorrente
	 */
	public void setContocorrente(String contocorrente) {
		this.contocorrente = contocorrente;
	}

	public String getContocorrenteIntestazione() {
		return this.contocorrenteIntestazione;
	}

	public void setContocorrenteIntestazione(String contocorrenteIntestazione) {
		this.contocorrenteIntestazione = contocorrenteIntestazione;
	}

	/**
	 * Gets the data scadenza.
	 *
	 * @return the data scadenza
	 */
	public Date getDataScadenza() {
		return this.dataScadenza;
	}

	/**
	 * Sets the data scadenza.
	 *
	 * @param dataScadenza the new data scadenza
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * Gets the iban.
	 *
	 * @return the iban
	 */
	public String getIban() {
		return this.iban;
	}

	/**
	 * Sets the iban.
	 *
	 * @param iban the new iban
	 */
	public void setIban(String iban) {
		this.iban = iban;
	}

	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * Sets the note.
	 *
	 * @param note the new note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	public Date getQuietanzanteNascitaData() {
		return this.quietanzanteNascitaData;
	}

	public void setQuietanzanteNascitaData(Date quietanzanteNascitaData) {
		this.quietanzanteNascitaData = quietanzanteNascitaData;
	}

	/**
	 * Gets the quietanziante.
	 *
	 * @return the quietanziante
	 */
	public String getQuietanziante() {
		return this.quietanziante;
	}

	/**
	 * Sets the quietanziante.
	 *
	 * @param quietanziante the new quietanziante
	 */
	public void setQuietanziante(String quietanziante) {
		this.quietanziante = quietanziante;
	}

	/**
	 * Gets the quietanziante codice fiscale.
	 *
	 * @return the quietanziante codice fiscale
	 */
	public String getQuietanzianteCodiceFiscale() {
		return this.quietanzianteCodiceFiscale;
	}

	/**
	 * Sets the quietanziante codice fiscale.
	 *
	 * @param quietanzianteCodiceFiscale the new quietanziante codice fiscale
	 */
	public void setQuietanzianteCodiceFiscale(String quietanzianteCodiceFiscale) {
		this.quietanzianteCodiceFiscale = quietanzianteCodiceFiscale;
	}

	public String getQuietanzianteNascitaLuogo() {
		return this.quietanzianteNascitaLuogo;
	}

	public void setQuietanzianteNascitaLuogo(String quietanzianteNascitaLuogo) {
		this.quietanzianteNascitaLuogo = quietanzianteNascitaLuogo;
	}

	public String getQuietanzianteNascitaStato() {
		return this.quietanzianteNascitaStato;
	}

	public void setQuietanzianteNascitaStato(String quietanzianteNascitaStato) {
		this.quietanzianteNascitaStato = quietanzianteNascitaStato;
	}

	public String getBancaDenominazione() {
		return bancaDenominazione;
	}

	public void setBancaDenominazione(String bancaDenominazione) {
		this.bancaDenominazione = bancaDenominazione;
	}

	public Boolean getPerStipendi() {
		return perStipendi;
	}

	public void setPerStipendi(Boolean perStipendi) {
		this.perStipendi = perStipendi;
	}

	public List<SiacRCartacontDetModpag> getSiacRCartacontDetModpags() {
		return this.siacRCartacontDetModpags;
	}

	public void setSiacRCartacontDetModpags(List<SiacRCartacontDetModpag> siacRCartacontDetModpags) {
		this.siacRCartacontDetModpags = siacRCartacontDetModpags;
	}

	public SiacRCartacontDetModpag addSiacRCartacontDetModpag(SiacRCartacontDetModpag siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().add(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTModpag(this);

		return siacRCartacontDetModpag;
	}

	public SiacRCartacontDetModpag removeSiacRCartacontDetModpag(SiacRCartacontDetModpag siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().remove(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTModpag(null);

		return siacRCartacontDetModpag;
	}

	public List<SiacRCausaleModpag> getSiacRCausaleModpags() {
		return this.siacRCausaleModpags;
	}

	public void setSiacRCausaleModpags(List<SiacRCausaleModpag> siacRCausaleModpags) {
		this.siacRCausaleModpags = siacRCausaleModpags;
	}

	public SiacRCausaleModpag addSiacRCausaleModpag(SiacRCausaleModpag siacRCausaleModpag) {
		getSiacRCausaleModpags().add(siacRCausaleModpag);
		siacRCausaleModpag.setSiacTModpag(this);

		return siacRCausaleModpag;
	}

	public SiacRCausaleModpag removeSiacRCausaleModpag(SiacRCausaleModpag siacRCausaleModpag) {
		getSiacRCausaleModpags().remove(siacRCausaleModpag);
		siacRCausaleModpag.setSiacTModpag(null);

		return siacRCausaleModpag;
	}

	public List<SiacRModpagOrdine> getSiacRModpagOrdines() {
		return this.siacRModpagOrdines;
	}

	public void setSiacRModpagOrdines(List<SiacRModpagOrdine> siacRModpagOrdines) {
		this.siacRModpagOrdines = siacRModpagOrdines;
	}

	public SiacRModpagOrdine addSiacRModpagOrdine(SiacRModpagOrdine siacRModpagOrdine) {
		getSiacRModpagOrdines().add(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTModpag(this);

		return siacRModpagOrdine;
	}

	public SiacRModpagOrdine removeSiacRModpagOrdine(SiacRModpagOrdine siacRModpagOrdine) {
		getSiacRModpagOrdines().remove(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTModpag(null);

		return siacRModpagOrdine;
	}


	/**
	 * Gets the siac r modpag statos.
	 *
	 * @return the siac r modpag statos
	 */
	public List<SiacRModpagStato> getSiacRModpagStatos() {
		return this.siacRModpagStatos;
	}

	/**
	 * Sets the siac r modpag statos.
	 *
	 * @param siacRModpagStatos the new siac r modpag statos
	 */
	public void setSiacRModpagStatos(List<SiacRModpagStato> siacRModpagStatos) {
		this.siacRModpagStatos = siacRModpagStatos;
	}

	/**
	 * Adds the siac r modpag stato.
	 *
	 * @param siacRModpagStato the siac r modpag stato
	 * @return the siac r modpag stato
	 */
	public SiacRModpagStato addSiacRModpagStato(SiacRModpagStato siacRModpagStato) {
		getSiacRModpagStatos().add(siacRModpagStato);
		siacRModpagStato.setSiacTModpag(this);

		return siacRModpagStato;
	}

	/**
	 * Removes the siac r modpag stato.
	 *
	 * @param siacRModpagStato the siac r modpag stato
	 * @return the siac r modpag stato
	 */
	public SiacRModpagStato removeSiacRModpagStato(SiacRModpagStato siacRModpagStato) {
		getSiacRModpagStatos().remove(siacRModpagStato);
		siacRModpagStato.setSiacTModpag(null);

		return siacRModpagStato;
	}

	public List<SiacROrdinativoModpag> getSiacROrdinativoModpags() {
		return this.siacROrdinativoModpags;
	}

	public void setSiacROrdinativoModpags(List<SiacROrdinativoModpag> siacROrdinativoModpags) {
		this.siacROrdinativoModpags = siacROrdinativoModpags;
	}

	public SiacROrdinativoModpag addSiacROrdinativoModpag(SiacROrdinativoModpag siacROrdinativoModpag) {
		getSiacROrdinativoModpags().add(siacROrdinativoModpag);
		siacROrdinativoModpag.setSiacTModpag(this);

		return siacROrdinativoModpag;
	}

	public SiacROrdinativoModpag removeSiacROrdinativoModpag(SiacROrdinativoModpag siacROrdinativoModpag) {
		getSiacROrdinativoModpags().remove(siacROrdinativoModpag);
		siacROrdinativoModpag.setSiacTModpag(null);

		return siacROrdinativoModpag;
	}

	public List<SiacRPredocModpag> getSiacRPredocModpags() {
		return this.siacRPredocModpags;
	}

	public void setSiacRPredocModpags(List<SiacRPredocModpag> siacRPredocModpags) {
		this.siacRPredocModpags = siacRPredocModpags;
	}

	public SiacRPredocModpag addSiacRPredocModpag(SiacRPredocModpag siacRPredocModpag) {
		getSiacRPredocModpags().add(siacRPredocModpag);
		siacRPredocModpag.setSiacTModpag(this);

		return siacRPredocModpag;
	}

	public SiacRPredocModpag removeSiacRPredocModpag(SiacRPredocModpag siacRPredocModpag) {
		getSiacRPredocModpags().remove(siacRPredocModpag);
		siacRPredocModpag.setSiacTModpag(null);

		return siacRPredocModpag;
	}

	/**
	 * Gets the siac r soggrel modpags.
	 *
	 * @return the siac r soggrel modpags
	 */
	public List<SiacRSoggrelModpag> getSiacRSoggrelModpags() {
		return this.siacRSoggrelModpags;
	}

	/**
	 * Sets the siac r soggrel modpags.
	 *
	 * @param siacRSoggrelModpags the new siac r soggrel modpags
	 */
	public void setSiacRSoggrelModpags(List<SiacRSoggrelModpag> siacRSoggrelModpags) {
		this.siacRSoggrelModpags = siacRSoggrelModpags;
	}

	/**
	 * Adds the siac r soggrel modpag.
	 *
	 * @param siacRSoggrelModpag the siac r soggrel modpag
	 * @return the siac r soggrel modpag
	 */
	public SiacRSoggrelModpag addSiacRSoggrelModpag(SiacRSoggrelModpag siacRSoggrelModpag) {
		getSiacRSoggrelModpags().add(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacTModpag(this);

		return siacRSoggrelModpag;
	}

	/**
	 * Removes the siac r soggrel modpag.
	 *
	 * @param siacRSoggrelModpag the siac r soggrel modpag
	 * @return the siac r soggrel modpag
	 */
	public SiacRSoggrelModpag removeSiacRSoggrelModpag(SiacRSoggrelModpag siacRSoggrelModpag) {
		getSiacRSoggrelModpags().remove(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacTModpag(null);

		return siacRSoggrelModpag;
	}

	/**
	 * Gets the siac r soggrel modpag mods.
	 *
	 * @return the siac r soggrel modpag mods
	 */
	public List<SiacRSoggrelModpagMod> getSiacRSoggrelModpagMods() {
		return this.siacRSoggrelModpagMods;
	}

	/**
	 * Sets the siac r soggrel modpag mods.
	 *
	 * @param siacRSoggrelModpagMods the new siac r soggrel modpag mods
	 */
	public void setSiacRSoggrelModpagMods(List<SiacRSoggrelModpagMod> siacRSoggrelModpagMods) {
		this.siacRSoggrelModpagMods = siacRSoggrelModpagMods;
	}

	/**
	 * Adds the siac r soggrel modpag mod.
	 *
	 * @param siacRSoggrelModpagMod the siac r soggrel modpag mod
	 * @return the siac r soggrel modpag mod
	 */
	public SiacRSoggrelModpagMod addSiacRSoggrelModpagMod(SiacRSoggrelModpagMod siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().add(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacTModpag(this);

		return siacRSoggrelModpagMod;
	}

	/**
	 * Removes the siac r soggrel modpag mod.
	 *
	 * @param siacRSoggrelModpagMod the siac r soggrel modpag mod
	 * @return the siac r soggrel modpag mod
	 */
	public SiacRSoggrelModpagMod removeSiacRSoggrelModpagMod(SiacRSoggrelModpagMod siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().remove(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacTModpag(null);

		return siacRSoggrelModpagMod;
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
		siacRSubdocModpag.setSiacTModpag(this);

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
		siacRSubdocModpag.setSiacTModpag(null);

		return siacRSubdocModpag;
	}

	/**
	 * Gets the siac t liquidaziones.
	 *
	 * @return the siac t liquidaziones
	 */
	public List<SiacTLiquidazione> getSiacTLiquidaziones() {
		return this.siacTLiquidaziones;
	}

	/**
	 * Sets the siac t liquidaziones.
	 *
	 * @param siacTLiquidaziones the new siac t liquidaziones
	 */
	public void setSiacTLiquidaziones(List<SiacTLiquidazione> siacTLiquidaziones) {
		this.siacTLiquidaziones = siacTLiquidaziones;
	}

	/**
	 * Adds the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione addSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().add(siacTLiquidazione);
		siacTLiquidazione.setSiacTModpag(this);

		return siacTLiquidazione;
	}

	/**
	 * Removes the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione removeSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().remove(siacTLiquidazione);
		siacTLiquidazione.setSiacTModpag(null);

		return siacTLiquidazione;
	}

	/**
	 * Gets the siac d accredito tipo.
	 *
	 * @return the siac d accredito tipo
	 */
	public SiacDAccreditoTipo getSiacDAccreditoTipo() {
		return this.siacDAccreditoTipo;
	}

	/**
	 * Sets the siac d accredito tipo.
	 *
	 * @param siacDAccreditoTipo the new siac d accredito tipo
	 */
	public void setSiacDAccreditoTipo(SiacDAccreditoTipo siacDAccreditoTipo) {
		this.siacDAccreditoTipo = siacDAccreditoTipo;
	}

	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/**
	 * Gets the siac t modpag mods.
	 *
	 * @return the siac t modpag mods
	 */
	public List<SiacTModpagMod> getSiacTModpagMods() {
		return this.siacTModpagMods;
	}

	/**
	 * Sets the siac t modpag mods.
	 *
	 * @param siacTModpagMods the new siac t modpag mods
	 */
	public void setSiacTModpagMods(List<SiacTModpagMod> siacTModpagMods) {
		this.siacTModpagMods = siacTModpagMods;
	}

	/**
	 * Adds the siac t modpag mod.
	 *
	 * @param siacTModpagMod the siac t modpag mod
	 * @return the siac t modpag mod
	 */
	public SiacTModpagMod addSiacTModpagMod(SiacTModpagMod siacTModpagMod) {
		getSiacTModpagMods().add(siacTModpagMod);
		siacTModpagMod.setSiacTModpag(this);

		return siacTModpagMod;
	}

	/**
	 * Removes the siac t modpag mod.
	 *
	 * @param siacTModpagMod the siac t modpag mod
	 * @return the siac t modpag mod
	 */
	public SiacTModpagMod removeSiacTModpagMod(SiacTModpagMod siacTModpagMod) {
		getSiacTModpagMods().remove(siacTModpagMod);
		siacTModpagMod.setSiacTModpag(null);

		return siacTModpagMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modpagId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modpagId =uid;
		
	}

}