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
 * The persistent class for the siac_t_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_t_soggetto")
@NamedQuery(name="SiacTSoggetto.findAll", query="SELECT s FROM SiacTSoggetto s")
public class SiacTSoggetto extends SiacTEnteBaseExt {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto id. */
	@Id
	@SequenceGenerator(name="SIAC_T_SOGGETTO_SOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_SOGGETTO_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SOGGETTO_SOGGETTOID_GENERATOR")
	@Column(name="soggetto_id")
	private Integer soggettoId;

	/** The codice fiscale. */
	@Column(name="codice_fiscale")
	private String codiceFiscale;

	/** The codice fiscale estero. */
	@Column(name="codice_fiscale_estero")
	private String codiceFiscaleEstero;

	/** The partita iva. */
	@Column(name="partita_iva")
	private String partitaIva;

	/** The soggetto code. */
	@Column(name="soggetto_code")
	private String soggettoCode;

	/** The soggetto desc. */
	@Column(name="soggetto_desc")
	private String soggettoDesc;
	
	@Column(name="email_PEC")
	private String emailPec;
	
	@Column(name="cod_Destinatario")
	private String codDestinatario;
	
	@Column(name="codice_pa")
	private String canalePA;	
	
	@Column(name = "istituto_di_credito")
	private Boolean istitutoDiCredito;

	//bi-directional many-to-one association to SiacRAttoAllegatoSog
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRAttoAllegatoSog> siacRAttoAllegatoSogs;

	//bi-directional many-to-one association to SiacRCartacontDetSoggetto
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRCartacontDetSoggetto> siacRCartacontDetSoggettos;

	//bi-directional many-to-one association to SiacRCausaleSoggetto
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRCausaleSoggetto> siacRCausaleSoggettos;

	//bi-directional many-to-one association to SiacRDocIvaSog
	/** The siac r doc iva sogs. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRDocIvaSog> siacRDocIvaSogs;

	//bi-directional many-to-one association to SiacRDocSog
	/** The siac r doc sogs. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRDocSog> siacRDocSogs;

	//bi-directional many-to-one association to SiacRFormaGiuridica
	/** The siac r forma giuridicas. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRFormaGiuridica> siacRFormaGiuridicas;

	//bi-directional many-to-one association to SiacRFormaGiuridicaMod
	/** The siac r forma giuridica mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRFormaGiuridicaMod> siacRFormaGiuridicaMods;

	//bi-directional many-to-one association to SiacRLiquidazioneSoggetto
	/** The siac r liquidazione soggettos. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRLiquidazioneSoggetto> siacRLiquidazioneSoggettos;

	//bi-directional many-to-one association to SiacRModpagOrdine
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRModpagOrdine> siacRModpagOrdines;

	//bi-directional many-to-one association to SiacRMovgestTsSog
	/** The siac r movgest ts sogs. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRMovgestTsSog> siacRMovgestTsSogs;

	//bi-directional many-to-one association to SiacRMovgestTsSogMod
	/** The siac r movgest ts sog mods1. */
	@OneToMany(mappedBy="siacTSoggetto1")
	private List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods1;

	//bi-directional many-to-one association to SiacRMovgestTsSogMod
	/** The siac r movgest ts sog mods2. */
	@OneToMany(mappedBy="siacTSoggetto2")
	private List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods2;

	//bi-directional many-to-one association to SiacROrdinativoSoggetto
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacROrdinativoSoggetto> siacROrdinativoSoggettos;

	//bi-directional many-to-one association to SiacRPredocSog
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRPredocSog> siacRPredocSogs;

	//bi-directional many-to-one association to SiacRSoggettoAttr
	/** The siac r soggetto attrs. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoAttr> siacRSoggettoAttrs;

	//bi-directional many-to-one association to SiacRSoggettoAttrMod
	/** The siac r soggetto attr mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoAttrMod> siacRSoggettoAttrMods;

	//bi-directional many-to-one association to SiacRSoggettoClasse
	/** The siac r soggetto classes. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoClasse> siacRSoggettoClasses;

	//bi-directional many-to-one association to SiacRSoggettoClasseMod
	/** The siac r soggetto classe mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoClasseMod> siacRSoggettoClasseMods;

	//bi-directional many-to-one association to SiacRSoggettoEnteProprietario
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoEnteProprietario> siacRSoggettoEnteProprietarios;

	//bi-directional many-to-one association to SiacRSoggettoOnere
	/** The siac r soggetto oneres. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoOnere> siacRSoggettoOneres;

	//bi-directional many-to-one association to SiacRSoggettoOnereMod
	/** The siac r soggetto onere mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoOnereMod> siacRSoggettoOnereMods;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	/** The siac r soggetto relazs1. */
	@OneToMany(mappedBy="siacTSoggetto1")
	private List<SiacRSoggettoRelaz> siacRSoggettoRelazs1;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	/** The siac r soggetto relazs2. */
	@OneToMany(mappedBy="siacTSoggetto2")
	private List<SiacRSoggettoRelaz> siacRSoggettoRelazs2;

	//bi-directional many-to-one association to SiacRSoggettoRelazMod
	/** The siac r soggetto relaz mods1. */
	@OneToMany(mappedBy="siacTSoggetto1")
	private List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods1;

	//bi-directional many-to-one association to SiacRSoggettoRelazMod
	/** The siac r soggetto relaz mods2. */
	@OneToMany(mappedBy="siacTSoggetto2")
	private List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods2;

	//bi-directional many-to-one association to SiacRSoggettoRuolo
	/** The siac r soggetto ruolos. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoRuolo> siacRSoggettoRuolos;

	//bi-directional many-to-one association to SiacRSoggettoStato
	/** The siac r soggetto statos. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoStato> siacRSoggettoStatos;

	//bi-directional many-to-one association to SiacRSoggettoTipo
	/** The siac r soggetto tipos. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoTipo> siacRSoggettoTipos;

	//bi-directional many-to-one association to SiacRSoggettoTipoMod
	/** The siac r soggetto tipo mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoTipoMod> siacRSoggettoTipoMods;

	//bi-directional many-to-one association to SiacRSubdocSog
	/** The siac r subdoc sogs. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSubdocSog> siacRSubdocSogs;

	//bi-directional many-to-one association to SiacTIndirizzoSoggetto
	/** The siac t indirizzo soggettos. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTIndirizzoSoggetto> siacTIndirizzoSoggettos;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoMod
	/** The siac t indirizzo soggetto mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTIndirizzoSoggettoMod> siacTIndirizzoSoggettoMods;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpags. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTModpag> siacTModpags;

	//bi-directional many-to-one association to SiacTModpagMod
	/** The siac t modpag mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTModpagMod> siacTModpagMods;

	//bi-directional many-to-one association to SiacTPersonaFisica
	/** The siac t persona fisicas. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTPersonaFisica> siacTPersonaFisicas;

	//bi-directional many-to-one association to SiacTPersonaFisicaMod
	/** The siac t persona fisica mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTPersonaFisicaMod> siacTPersonaFisicaMods;

	//bi-directional many-to-one association to SiacTPersonaGiuridica
	/** The siac t persona giuridicas. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTPersonaGiuridica> siacTPersonaGiuridicas;

	//bi-directional many-to-one association to SiacTPersonaGiuridicaMod
	/** The siac t persona giuridica mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTPersonaGiuridicaMod> siacTPersonaGiuridicaMods;

	//bi-directional many-to-one association to SiacTRecapitoSoggetto
	/** The siac t recapito soggettos. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTRecapitoSoggetto> siacTRecapitoSoggettos;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoMod
	/** The siac t recapito soggetto mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTRecapitoSoggettoMod> siacTRecapitoSoggettoMods;

	//bi-directional many-to-one association to SiacDAmbito
	/** The siac d ambito. */
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

	//bi-directional many-to-one association to SiacTSoggettoMod
	/** The siac t soggetto mods. */
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTSoggettoMod> siacTSoggettoMods;

	//bi-directional many-to-one association to SiacRConciliazioneBeneficiario
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRConciliazioneBeneficiario> siacRConciliazioneBeneficiarios;

	//bi-directional many-to-one association to SiacTRegistroPcc
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTRegistroPcc> siacTRegistroPccs;
	
	//bi-directional many-to-one association to SiacTRegistroPcc
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTCassaEcon> siacTCassaEcons;

	@Column(name="soggetto_fine_validita_durc")
	private Date dataFineValiditaDurc;

	@Column(name="soggetto_tipo_fonte_durc")
	private Character tipoFonteDurc;

	@ManyToOne
	@JoinColumn(name="soggetto_fonte_durc_manuale_classif_id")
	private SiacTClass fonteDurc;

	@Column(name="soggetto_fonte_durc_automatica")
	private String fonteDurcAutomatica;

	@Column(name="soggetto_note_durc")
	private String noteDurc;
	
	@Column(name="login_inserimento_durc")
	private String loginInserimentoDurc;

	@Column(name="login_modifica_durc")
	private String loginModificaDurc;
	
	
	/**
	 * Instantiates a new siac t soggetto.
	 */
	public SiacTSoggetto() {
	}

	/**
	 * Gets the soggetto id.
	 *
	 * @return the soggetto id
	 */
	public Integer getSoggettoId() {
		return this.soggettoId;
	}

	/**
	 * Sets the soggetto id.
	 *
	 * @param soggettoId the new soggetto id
	 */
	public void setSoggettoId(Integer soggettoId) {
		this.soggettoId = soggettoId;
	}

	/**
	 * Gets the codice fiscale.
	 *
	 * @return the codice fiscale
	 */
	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	/**
	 * Sets the codice fiscale.
	 *
	 * @param codiceFiscale the new codice fiscale
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * Gets the codice fiscale estero.
	 *
	 * @return the codice fiscale estero
	 */
	public String getCodiceFiscaleEstero() {
		return this.codiceFiscaleEstero;
	}

	/**
	 * Sets the codice fiscale estero.
	 *
	 * @param codiceFiscaleEstero the new codice fiscale estero
	 */
	public void setCodiceFiscaleEstero(String codiceFiscaleEstero) {
		this.codiceFiscaleEstero = codiceFiscaleEstero;
	}

	/**
	 * Gets the partita iva.
	 *
	 * @return the partita iva
	 */
	public String getPartitaIva() {
		return this.partitaIva;
	}

	/**
	 * Sets the partita iva.
	 *
	 * @param partitaIva the new partita iva
	 */
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	/**
	 * Gets the soggetto code.
	 *
	 * @return the soggetto code
	 */
	public String getSoggettoCode() {
		return this.soggettoCode;
	}

	/**
	 * Sets the soggetto code.
	 *
	 * @param soggettoCode the new soggetto code
	 */
	public void setSoggettoCode(String soggettoCode) {
		this.soggettoCode = soggettoCode;
	}

	/**
	 * Gets the soggetto desc.
	 *
	 * @return the soggetto desc
	 */
	public String getSoggettoDesc() {
		return this.soggettoDesc;
	}

	/**
	 * Sets the soggetto desc.
	 *
	 * @param soggettoDesc the new soggetto desc
	 */
	public void setSoggettoDesc(String soggettoDesc) {
		this.soggettoDesc = soggettoDesc;
	}

	public List<SiacRAttoAllegatoSog> getSiacRAttoAllegatoSogs() {
		return this.siacRAttoAllegatoSogs;
	}

	public void setSiacRAttoAllegatoSogs(List<SiacRAttoAllegatoSog> siacRAttoAllegatoSogs) {
		this.siacRAttoAllegatoSogs = siacRAttoAllegatoSogs;
	}

	public SiacRAttoAllegatoSog addSiacRAttoAllegatoSog(SiacRAttoAllegatoSog siacRAttoAllegatoSog) {
		getSiacRAttoAllegatoSogs().add(siacRAttoAllegatoSog);
		siacRAttoAllegatoSog.setSiacTSoggetto(this);

		return siacRAttoAllegatoSog;
	}

	public SiacRAttoAllegatoSog removeSiacRAttoAllegatoSog(SiacRAttoAllegatoSog siacRAttoAllegatoSog) {
		getSiacRAttoAllegatoSogs().remove(siacRAttoAllegatoSog);
		siacRAttoAllegatoSog.setSiacTSoggetto(null);

		return siacRAttoAllegatoSog;
	}

	public List<SiacRCartacontDetSoggetto> getSiacRCartacontDetSoggettos() {
		return this.siacRCartacontDetSoggettos;
	}

	public void setSiacRCartacontDetSoggettos(List<SiacRCartacontDetSoggetto> siacRCartacontDetSoggettos) {
		this.siacRCartacontDetSoggettos = siacRCartacontDetSoggettos;
	}

	public SiacRCartacontDetSoggetto addSiacRCartacontDetSoggetto(SiacRCartacontDetSoggetto siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().add(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTSoggetto(this);

		return siacRCartacontDetSoggetto;
	}

	public SiacRCartacontDetSoggetto removeSiacRCartacontDetSoggetto(SiacRCartacontDetSoggetto siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().remove(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTSoggetto(null);

		return siacRCartacontDetSoggetto;
	}

	public List<SiacRCausaleSoggetto> getSiacRCausaleSoggettos() {
		return this.siacRCausaleSoggettos;
	}

	public void setSiacRCausaleSoggettos(List<SiacRCausaleSoggetto> siacRCausaleSoggettos) {
		this.siacRCausaleSoggettos = siacRCausaleSoggettos;
	}

	public SiacRCausaleSoggetto addSiacRCausaleSoggetto(SiacRCausaleSoggetto siacRCausaleSoggetto) {
		getSiacRCausaleSoggettos().add(siacRCausaleSoggetto);
		siacRCausaleSoggetto.setSiacTSoggetto(this);

		return siacRCausaleSoggetto;
	}

	public SiacRCausaleSoggetto removeSiacRCausaleSoggetto(SiacRCausaleSoggetto siacRCausaleSoggetto) {
		getSiacRCausaleSoggettos().remove(siacRCausaleSoggetto);
		siacRCausaleSoggetto.setSiacTSoggetto(null);

		return siacRCausaleSoggetto;
	}

	/**
	 * Gets the siac r doc iva sogs.
	 *
	 * @return the siac r doc iva sogs
	 */
	public List<SiacRDocIvaSog> getSiacRDocIvaSogs() {
		return this.siacRDocIvaSogs;
	}

	/**
	 * Sets the siac r doc iva sogs.
	 *
	 * @param siacRDocIvaSogs the new siac r doc iva sogs
	 */
	public void setSiacRDocIvaSogs(List<SiacRDocIvaSog> siacRDocIvaSogs) {
		this.siacRDocIvaSogs = siacRDocIvaSogs;
	}

	/**
	 * Adds the siac r doc iva sog.
	 *
	 * @param siacRDocIvaSog the siac r doc iva sog
	 * @return the siac r doc iva sog
	 */
	public SiacRDocIvaSog addSiacRDocIvaSog(SiacRDocIvaSog siacRDocIvaSog) {
		getSiacRDocIvaSogs().add(siacRDocIvaSog);
		siacRDocIvaSog.setSiacTSoggetto(this);

		return siacRDocIvaSog;
	}

	/**
	 * Removes the siac r doc iva sog.
	 *
	 * @param siacRDocIvaSog the siac r doc iva sog
	 * @return the siac r doc iva sog
	 */
	public SiacRDocIvaSog removeSiacRDocIvaSog(SiacRDocIvaSog siacRDocIvaSog) {
		getSiacRDocIvaSogs().remove(siacRDocIvaSog);
		siacRDocIvaSog.setSiacTSoggetto(null);

		return siacRDocIvaSog;
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
		siacRDocSog.setSiacTSoggetto(this);

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
		siacRDocSog.setSiacTSoggetto(null);

		return siacRDocSog;
	}

	/**
	 * Gets the siac r forma giuridicas.
	 *
	 * @return the siac r forma giuridicas
	 */
	public List<SiacRFormaGiuridica> getSiacRFormaGiuridicas() {
		return this.siacRFormaGiuridicas;
	}

	/**
	 * Sets the siac r forma giuridicas.
	 *
	 * @param siacRFormaGiuridicas the new siac r forma giuridicas
	 */
	public void setSiacRFormaGiuridicas(List<SiacRFormaGiuridica> siacRFormaGiuridicas) {
		this.siacRFormaGiuridicas = siacRFormaGiuridicas;
	}

	/**
	 * Adds the siac r forma giuridica.
	 *
	 * @param siacRFormaGiuridica the siac r forma giuridica
	 * @return the siac r forma giuridica
	 */
	public SiacRFormaGiuridica addSiacRFormaGiuridica(SiacRFormaGiuridica siacRFormaGiuridica) {
		getSiacRFormaGiuridicas().add(siacRFormaGiuridica);
		siacRFormaGiuridica.setSiacTSoggetto(this);

		return siacRFormaGiuridica;
	}

	/**
	 * Removes the siac r forma giuridica.
	 *
	 * @param siacRFormaGiuridica the siac r forma giuridica
	 * @return the siac r forma giuridica
	 */
	public SiacRFormaGiuridica removeSiacRFormaGiuridica(SiacRFormaGiuridica siacRFormaGiuridica) {
		getSiacRFormaGiuridicas().remove(siacRFormaGiuridica);
		siacRFormaGiuridica.setSiacTSoggetto(null);

		return siacRFormaGiuridica;
	}

	/**
	 * Gets the siac r forma giuridica mods.
	 *
	 * @return the siac r forma giuridica mods
	 */
	public List<SiacRFormaGiuridicaMod> getSiacRFormaGiuridicaMods() {
		return this.siacRFormaGiuridicaMods;
	}

	/**
	 * Sets the siac r forma giuridica mods.
	 *
	 * @param siacRFormaGiuridicaMods the new siac r forma giuridica mods
	 */
	public void setSiacRFormaGiuridicaMods(List<SiacRFormaGiuridicaMod> siacRFormaGiuridicaMods) {
		this.siacRFormaGiuridicaMods = siacRFormaGiuridicaMods;
	}

	/**
	 * Adds the siac r forma giuridica mod.
	 *
	 * @param siacRFormaGiuridicaMod the siac r forma giuridica mod
	 * @return the siac r forma giuridica mod
	 */
	public SiacRFormaGiuridicaMod addSiacRFormaGiuridicaMod(SiacRFormaGiuridicaMod siacRFormaGiuridicaMod) {
		getSiacRFormaGiuridicaMods().add(siacRFormaGiuridicaMod);
		siacRFormaGiuridicaMod.setSiacTSoggetto(this);

		return siacRFormaGiuridicaMod;
	}

	/**
	 * Removes the siac r forma giuridica mod.
	 *
	 * @param siacRFormaGiuridicaMod the siac r forma giuridica mod
	 * @return the siac r forma giuridica mod
	 */
	public SiacRFormaGiuridicaMod removeSiacRFormaGiuridicaMod(SiacRFormaGiuridicaMod siacRFormaGiuridicaMod) {
		getSiacRFormaGiuridicaMods().remove(siacRFormaGiuridicaMod);
		siacRFormaGiuridicaMod.setSiacTSoggetto(null);

		return siacRFormaGiuridicaMod;
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
		siacRLiquidazioneSoggetto.setSiacTSoggetto(this);

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
		siacRLiquidazioneSoggetto.setSiacTSoggetto(null);

		return siacRLiquidazioneSoggetto;
	}

	public List<SiacRModpagOrdine> getSiacRModpagOrdines() {
		return this.siacRModpagOrdines;
	}

	public void setSiacRModpagOrdines(List<SiacRModpagOrdine> siacRModpagOrdines) {
		this.siacRModpagOrdines = siacRModpagOrdines;
	}

	public SiacRModpagOrdine addSiacRModpagOrdine(SiacRModpagOrdine siacRModpagOrdine) {
		getSiacRModpagOrdines().add(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTSoggetto(this);

		return siacRModpagOrdine;
	}

	public SiacRModpagOrdine removeSiacRModpagOrdine(SiacRModpagOrdine siacRModpagOrdine) {
		getSiacRModpagOrdines().remove(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTSoggetto(null);

		return siacRModpagOrdine;
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
		siacRMovgestTsSog.setSiacTSoggetto(this);

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
		siacRMovgestTsSog.setSiacTSoggetto(null);

		return siacRMovgestTsSog;
	}

	/**
	 * Gets the siac r movgest ts sog mods1.
	 *
	 * @return the siac r movgest ts sog mods1
	 */
	public List<SiacRMovgestTsSogMod> getSiacRMovgestTsSogMods1() {
		return this.siacRMovgestTsSogMods1;
	}

	/**
	 * Sets the siac r movgest ts sog mods1.
	 *
	 * @param siacRMovgestTsSogMods1 the new siac r movgest ts sog mods1
	 */
	public void setSiacRMovgestTsSogMods1(List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods1) {
		this.siacRMovgestTsSogMods1 = siacRMovgestTsSogMods1;
	}

	/**
	 * Adds the siac r movgest ts sog mods1.
	 *
	 * @param siacRMovgestTsSogMods1 the siac r movgest ts sog mods1
	 * @return the siac r movgest ts sog mod
	 */
	public SiacRMovgestTsSogMod addSiacRMovgestTsSogMods1(SiacRMovgestTsSogMod siacRMovgestTsSogMods1) {
		getSiacRMovgestTsSogMods1().add(siacRMovgestTsSogMods1);
		siacRMovgestTsSogMods1.setSiacTSoggetto1(this);

		return siacRMovgestTsSogMods1;
	}

	/**
	 * Removes the siac r movgest ts sog mods1.
	 *
	 * @param siacRMovgestTsSogMods1 the siac r movgest ts sog mods1
	 * @return the siac r movgest ts sog mod
	 */
	public SiacRMovgestTsSogMod removeSiacRMovgestTsSogMods1(SiacRMovgestTsSogMod siacRMovgestTsSogMods1) {
		getSiacRMovgestTsSogMods1().remove(siacRMovgestTsSogMods1);
		siacRMovgestTsSogMods1.setSiacTSoggetto1(null);

		return siacRMovgestTsSogMods1;
	}

	/**
	 * Gets the siac r movgest ts sog mods2.
	 *
	 * @return the siac r movgest ts sog mods2
	 */
	public List<SiacRMovgestTsSogMod> getSiacRMovgestTsSogMods2() {
		return this.siacRMovgestTsSogMods2;
	}

	/**
	 * Sets the siac r movgest ts sog mods2.
	 *
	 * @param siacRMovgestTsSogMods2 the new siac r movgest ts sog mods2
	 */
	public void setSiacRMovgestTsSogMods2(List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods2) {
		this.siacRMovgestTsSogMods2 = siacRMovgestTsSogMods2;
	}

	/**
	 * Adds the siac r movgest ts sog mods2.
	 *
	 * @param siacRMovgestTsSogMods2 the siac r movgest ts sog mods2
	 * @return the siac r movgest ts sog mod
	 */
	public SiacRMovgestTsSogMod addSiacRMovgestTsSogMods2(SiacRMovgestTsSogMod siacRMovgestTsSogMods2) {
		getSiacRMovgestTsSogMods2().add(siacRMovgestTsSogMods2);
		siacRMovgestTsSogMods2.setSiacTSoggetto2(this);

		return siacRMovgestTsSogMods2;
	}

	/**
	 * Removes the siac r movgest ts sog mods2.
	 *
	 * @param siacRMovgestTsSogMods2 the siac r movgest ts sog mods2
	 * @return the siac r movgest ts sog mod
	 */
	public SiacRMovgestTsSogMod removeSiacRMovgestTsSogMods2(SiacRMovgestTsSogMod siacRMovgestTsSogMods2) {
		getSiacRMovgestTsSogMods2().remove(siacRMovgestTsSogMods2);
		siacRMovgestTsSogMods2.setSiacTSoggetto2(null);

		return siacRMovgestTsSogMods2;
	}


	public List<SiacROrdinativoSoggetto> getSiacROrdinativoSoggettos() {
		return this.siacROrdinativoSoggettos;
	}

	public void setSiacROrdinativoSoggettos(List<SiacROrdinativoSoggetto> siacROrdinativoSoggettos) {
		this.siacROrdinativoSoggettos = siacROrdinativoSoggettos;
	}

	public SiacROrdinativoSoggetto addSiacROrdinativoSoggetto(SiacROrdinativoSoggetto siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().add(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTSoggetto(this);

		return siacROrdinativoSoggetto;
	}

	public SiacROrdinativoSoggetto removeSiacROrdinativoSoggetto(SiacROrdinativoSoggetto siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().remove(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTSoggetto(null);

		return siacROrdinativoSoggetto;
	}

	public List<SiacRPredocSog> getSiacRPredocSogs() {
		return this.siacRPredocSogs;
	}

	public void setSiacRPredocSogs(List<SiacRPredocSog> siacRPredocSogs) {
		this.siacRPredocSogs = siacRPredocSogs;
	}

	public SiacRPredocSog addSiacRPredocSog(SiacRPredocSog siacRPredocSog) {
		getSiacRPredocSogs().add(siacRPredocSog);
		siacRPredocSog.setSiacTSoggetto(this);

		return siacRPredocSog;
	}

	public SiacRPredocSog removeSiacRPredocSog(SiacRPredocSog siacRPredocSog) {
		getSiacRPredocSogs().remove(siacRPredocSog);
		siacRPredocSog.setSiacTSoggetto(null);

		return siacRPredocSog;
	}

	/**
	 * Gets the siac r soggetto attrs.
	 *
	 * @return the siac r soggetto attrs
	 */
	public List<SiacRSoggettoAttr> getSiacRSoggettoAttrs() {
		return this.siacRSoggettoAttrs;
	}

	/**
	 * Sets the siac r soggetto attrs.
	 *
	 * @param siacRSoggettoAttrs the new siac r soggetto attrs
	 */
	public void setSiacRSoggettoAttrs(List<SiacRSoggettoAttr> siacRSoggettoAttrs) {
		this.siacRSoggettoAttrs = siacRSoggettoAttrs;
	}

	/**
	 * Adds the siac r soggetto attr.
	 *
	 * @param siacRSoggettoAttr the siac r soggetto attr
	 * @return the siac r soggetto attr
	 */
	public SiacRSoggettoAttr addSiacRSoggettoAttr(SiacRSoggettoAttr siacRSoggettoAttr) {
		getSiacRSoggettoAttrs().add(siacRSoggettoAttr);
		siacRSoggettoAttr.setSiacTSoggetto(this);

		return siacRSoggettoAttr;
	}

	/**
	 * Removes the siac r soggetto attr.
	 *
	 * @param siacRSoggettoAttr the siac r soggetto attr
	 * @return the siac r soggetto attr
	 */
	public SiacRSoggettoAttr removeSiacRSoggettoAttr(SiacRSoggettoAttr siacRSoggettoAttr) {
		getSiacRSoggettoAttrs().remove(siacRSoggettoAttr);
		siacRSoggettoAttr.setSiacTSoggetto(null);

		return siacRSoggettoAttr;
	}

	/**
	 * Gets the siac r soggetto attr mods.
	 *
	 * @return the siac r soggetto attr mods
	 */
	public List<SiacRSoggettoAttrMod> getSiacRSoggettoAttrMods() {
		return this.siacRSoggettoAttrMods;
	}

	/**
	 * Sets the siac r soggetto attr mods.
	 *
	 * @param siacRSoggettoAttrMods the new siac r soggetto attr mods
	 */
	public void setSiacRSoggettoAttrMods(List<SiacRSoggettoAttrMod> siacRSoggettoAttrMods) {
		this.siacRSoggettoAttrMods = siacRSoggettoAttrMods;
	}

	/**
	 * Adds the siac r soggetto attr mod.
	 *
	 * @param siacRSoggettoAttrMod the siac r soggetto attr mod
	 * @return the siac r soggetto attr mod
	 */
	public SiacRSoggettoAttrMod addSiacRSoggettoAttrMod(SiacRSoggettoAttrMod siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().add(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTSoggetto(this);

		return siacRSoggettoAttrMod;
	}

	/**
	 * Removes the siac r soggetto attr mod.
	 *
	 * @param siacRSoggettoAttrMod the siac r soggetto attr mod
	 * @return the siac r soggetto attr mod
	 */
	public SiacRSoggettoAttrMod removeSiacRSoggettoAttrMod(SiacRSoggettoAttrMod siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().remove(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTSoggetto(null);

		return siacRSoggettoAttrMod;
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
		siacRSoggettoClass.setSiacTSoggetto(this);

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
		siacRSoggettoClass.setSiacTSoggetto(null);

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
		siacRSoggettoClasseMod.setSiacTSoggetto(this);

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
		siacRSoggettoClasseMod.setSiacTSoggetto(null);

		return siacRSoggettoClasseMod;
	}

	public List<SiacRSoggettoEnteProprietario> getSiacRSoggettoEnteProprietarios() {
		return this.siacRSoggettoEnteProprietarios;
	}

	public void setSiacRSoggettoEnteProprietarios(List<SiacRSoggettoEnteProprietario> siacRSoggettoEnteProprietarios) {
		this.siacRSoggettoEnteProprietarios = siacRSoggettoEnteProprietarios;
	}

	public SiacRSoggettoEnteProprietario addSiacRSoggettoEnteProprietario(SiacRSoggettoEnteProprietario siacRSoggettoEnteProprietario) {
		getSiacRSoggettoEnteProprietarios().add(siacRSoggettoEnteProprietario);
		siacRSoggettoEnteProprietario.setSiacTSoggetto(this);

		return siacRSoggettoEnteProprietario;
	}

	public SiacRSoggettoEnteProprietario removeSiacRSoggettoEnteProprietario(SiacRSoggettoEnteProprietario siacRSoggettoEnteProprietario) {
		getSiacRSoggettoEnteProprietarios().remove(siacRSoggettoEnteProprietario);
		siacRSoggettoEnteProprietario.setSiacTSoggetto(null);

		return siacRSoggettoEnteProprietario;
	}

	/**
	 * Gets the siac r soggetto oneres.
	 *
	 * @return the siac r soggetto oneres
	 */
	public List<SiacRSoggettoOnere> getSiacRSoggettoOneres() {
		return this.siacRSoggettoOneres;
	}

	/**
	 * Sets the siac r soggetto oneres.
	 *
	 * @param siacRSoggettoOneres the new siac r soggetto oneres
	 */
	public void setSiacRSoggettoOneres(List<SiacRSoggettoOnere> siacRSoggettoOneres) {
		this.siacRSoggettoOneres = siacRSoggettoOneres;
	}

	/**
	 * Adds the siac r soggetto onere.
	 *
	 * @param siacRSoggettoOnere the siac r soggetto onere
	 * @return the siac r soggetto onere
	 */
	public SiacRSoggettoOnere addSiacRSoggettoOnere(SiacRSoggettoOnere siacRSoggettoOnere) {
		getSiacRSoggettoOneres().add(siacRSoggettoOnere);
		siacRSoggettoOnere.setSiacTSoggetto(this);

		return siacRSoggettoOnere;
	}

	/**
	 * Removes the siac r soggetto onere.
	 *
	 * @param siacRSoggettoOnere the siac r soggetto onere
	 * @return the siac r soggetto onere
	 */
	public SiacRSoggettoOnere removeSiacRSoggettoOnere(SiacRSoggettoOnere siacRSoggettoOnere) {
		getSiacRSoggettoOneres().remove(siacRSoggettoOnere);
		siacRSoggettoOnere.setSiacTSoggetto(null);

		return siacRSoggettoOnere;
	}

	/**
	 * Gets the siac r soggetto onere mods.
	 *
	 * @return the siac r soggetto onere mods
	 */
	public List<SiacRSoggettoOnereMod> getSiacRSoggettoOnereMods() {
		return this.siacRSoggettoOnereMods;
	}

	/**
	 * Sets the siac r soggetto onere mods.
	 *
	 * @param siacRSoggettoOnereMods the new siac r soggetto onere mods
	 */
	public void setSiacRSoggettoOnereMods(List<SiacRSoggettoOnereMod> siacRSoggettoOnereMods) {
		this.siacRSoggettoOnereMods = siacRSoggettoOnereMods;
	}

	/**
	 * Adds the siac r soggetto onere mod.
	 *
	 * @param siacRSoggettoOnereMod the siac r soggetto onere mod
	 * @return the siac r soggetto onere mod
	 */
	public SiacRSoggettoOnereMod addSiacRSoggettoOnereMod(SiacRSoggettoOnereMod siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().add(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacTSoggetto(this);

		return siacRSoggettoOnereMod;
	}

	/**
	 * Removes the siac r soggetto onere mod.
	 *
	 * @param siacRSoggettoOnereMod the siac r soggetto onere mod
	 * @return the siac r soggetto onere mod
	 */
	public SiacRSoggettoOnereMod removeSiacRSoggettoOnereMod(SiacRSoggettoOnereMod siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().remove(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacTSoggetto(null);

		return siacRSoggettoOnereMod;
	}

	/**
	 * Gets the siac r soggetto relazs1.
	 *
	 * @return the siac r soggetto relazs1
	 */
	public List<SiacRSoggettoRelaz> getSiacRSoggettoRelazs1() {
		return this.siacRSoggettoRelazs1;
	}

	/**
	 * Sets the siac r soggetto relazs1.
	 *
	 * @param siacRSoggettoRelazs1 the new siac r soggetto relazs1
	 */
	public void setSiacRSoggettoRelazs1(List<SiacRSoggettoRelaz> siacRSoggettoRelazs1) {
		this.siacRSoggettoRelazs1 = siacRSoggettoRelazs1;
	}

	/**
	 * Adds the siac r soggetto relazs1.
	 *
	 * @param siacRSoggettoRelazs1 the siac r soggetto relazs1
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz addSiacRSoggettoRelazs1(SiacRSoggettoRelaz siacRSoggettoRelazs1) {
		getSiacRSoggettoRelazs1().add(siacRSoggettoRelazs1);
		siacRSoggettoRelazs1.setSiacTSoggetto1(this);

		return siacRSoggettoRelazs1;
	}

	/**
	 * Removes the siac r soggetto relazs1.
	 *
	 * @param siacRSoggettoRelazs1 the siac r soggetto relazs1
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz removeSiacRSoggettoRelazs1(SiacRSoggettoRelaz siacRSoggettoRelazs1) {
		getSiacRSoggettoRelazs1().remove(siacRSoggettoRelazs1);
		siacRSoggettoRelazs1.setSiacTSoggetto1(null);

		return siacRSoggettoRelazs1;
	}

	/**
	 * Gets the siac r soggetto relazs2.
	 *
	 * @return the siac r soggetto relazs2
	 */
	public List<SiacRSoggettoRelaz> getSiacRSoggettoRelazs2() {
		return this.siacRSoggettoRelazs2;
	}

	/**
	 * Sets the siac r soggetto relazs2.
	 *
	 * @param siacRSoggettoRelazs2 the new siac r soggetto relazs2
	 */
	public void setSiacRSoggettoRelazs2(List<SiacRSoggettoRelaz> siacRSoggettoRelazs2) {
		this.siacRSoggettoRelazs2 = siacRSoggettoRelazs2;
	}

	/**
	 * Adds the siac r soggetto relazs2.
	 *
	 * @param siacRSoggettoRelazs2 the siac r soggetto relazs2
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz addSiacRSoggettoRelazs2(SiacRSoggettoRelaz siacRSoggettoRelazs2) {
		getSiacRSoggettoRelazs2().add(siacRSoggettoRelazs2);
		siacRSoggettoRelazs2.setSiacTSoggetto2(this);

		return siacRSoggettoRelazs2;
	}

	/**
	 * Removes the siac r soggetto relazs2.
	 *
	 * @param siacRSoggettoRelazs2 the siac r soggetto relazs2
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz removeSiacRSoggettoRelazs2(SiacRSoggettoRelaz siacRSoggettoRelazs2) {
		getSiacRSoggettoRelazs2().remove(siacRSoggettoRelazs2);
		siacRSoggettoRelazs2.setSiacTSoggetto2(null);

		return siacRSoggettoRelazs2;
	}

	/**
	 * Gets the siac r soggetto relaz mods1.
	 *
	 * @return the siac r soggetto relaz mods1
	 */
	public List<SiacRSoggettoRelazMod> getSiacRSoggettoRelazMods1() {
		return this.siacRSoggettoRelazMods1;
	}

	/**
	 * Sets the siac r soggetto relaz mods1.
	 *
	 * @param siacRSoggettoRelazMods1 the new siac r soggetto relaz mods1
	 */
	public void setSiacRSoggettoRelazMods1(List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods1) {
		this.siacRSoggettoRelazMods1 = siacRSoggettoRelazMods1;
	}

	/**
	 * Adds the siac r soggetto relaz mods1.
	 *
	 * @param siacRSoggettoRelazMods1 the siac r soggetto relaz mods1
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod addSiacRSoggettoRelazMods1(SiacRSoggettoRelazMod siacRSoggettoRelazMods1) {
		getSiacRSoggettoRelazMods1().add(siacRSoggettoRelazMods1);
		siacRSoggettoRelazMods1.setSiacTSoggetto1(this);

		return siacRSoggettoRelazMods1;
	}

	/**
	 * Removes the siac r soggetto relaz mods1.
	 *
	 * @param siacRSoggettoRelazMods1 the siac r soggetto relaz mods1
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod removeSiacRSoggettoRelazMods1(SiacRSoggettoRelazMod siacRSoggettoRelazMods1) {
		getSiacRSoggettoRelazMods1().remove(siacRSoggettoRelazMods1);
		siacRSoggettoRelazMods1.setSiacTSoggetto1(null);

		return siacRSoggettoRelazMods1;
	}

	/**
	 * Gets the siac r soggetto relaz mods2.
	 *
	 * @return the siac r soggetto relaz mods2
	 */
	public List<SiacRSoggettoRelazMod> getSiacRSoggettoRelazMods2() {
		return this.siacRSoggettoRelazMods2;
	}

	/**
	 * Sets the siac r soggetto relaz mods2.
	 *
	 * @param siacRSoggettoRelazMods2 the new siac r soggetto relaz mods2
	 */
	public void setSiacRSoggettoRelazMods2(List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods2) {
		this.siacRSoggettoRelazMods2 = siacRSoggettoRelazMods2;
	}

	/**
	 * Adds the siac r soggetto relaz mods2.
	 *
	 * @param siacRSoggettoRelazMods2 the siac r soggetto relaz mods2
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod addSiacRSoggettoRelazMods2(SiacRSoggettoRelazMod siacRSoggettoRelazMods2) {
		getSiacRSoggettoRelazMods2().add(siacRSoggettoRelazMods2);
		siacRSoggettoRelazMods2.setSiacTSoggetto2(this);

		return siacRSoggettoRelazMods2;
	}

	/**
	 * Removes the siac r soggetto relaz mods2.
	 *
	 * @param siacRSoggettoRelazMods2 the siac r soggetto relaz mods2
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod removeSiacRSoggettoRelazMods2(SiacRSoggettoRelazMod siacRSoggettoRelazMods2) {
		getSiacRSoggettoRelazMods2().remove(siacRSoggettoRelazMods2);
		siacRSoggettoRelazMods2.setSiacTSoggetto2(null);

		return siacRSoggettoRelazMods2;
	}

	/**
	 * Gets the siac r soggetto ruolos.
	 *
	 * @return the siac r soggetto ruolos
	 */
	public List<SiacRSoggettoRuolo> getSiacRSoggettoRuolos() {
		return this.siacRSoggettoRuolos;
	}

	/**
	 * Sets the siac r soggetto ruolos.
	 *
	 * @param siacRSoggettoRuolos the new siac r soggetto ruolos
	 */
	public void setSiacRSoggettoRuolos(List<SiacRSoggettoRuolo> siacRSoggettoRuolos) {
		this.siacRSoggettoRuolos = siacRSoggettoRuolos;
	}

	/**
	 * Adds the siac r soggetto ruolo.
	 *
	 * @param siacRSoggettoRuolo the siac r soggetto ruolo
	 * @return the siac r soggetto ruolo
	 */
	public SiacRSoggettoRuolo addSiacRSoggettoRuolo(SiacRSoggettoRuolo siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().add(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacTSoggetto(this);

		return siacRSoggettoRuolo;
	}

	/**
	 * Removes the siac r soggetto ruolo.
	 *
	 * @param siacRSoggettoRuolo the siac r soggetto ruolo
	 * @return the siac r soggetto ruolo
	 */
	public SiacRSoggettoRuolo removeSiacRSoggettoRuolo(SiacRSoggettoRuolo siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().remove(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacTSoggetto(null);

		return siacRSoggettoRuolo;
	}

	/**
	 * Gets the siac r soggetto statos.
	 *
	 * @return the siac r soggetto statos
	 */
	public List<SiacRSoggettoStato> getSiacRSoggettoStatos() {
		return this.siacRSoggettoStatos;
	}

	/**
	 * Sets the siac r soggetto statos.
	 *
	 * @param siacRSoggettoStatos the new siac r soggetto statos
	 */
	public void setSiacRSoggettoStatos(List<SiacRSoggettoStato> siacRSoggettoStatos) {
		this.siacRSoggettoStatos = siacRSoggettoStatos;
	}

	/**
	 * Adds the siac r soggetto stato.
	 *
	 * @param siacRSoggettoStato the siac r soggetto stato
	 * @return the siac r soggetto stato
	 */
	public SiacRSoggettoStato addSiacRSoggettoStato(SiacRSoggettoStato siacRSoggettoStato) {
		getSiacRSoggettoStatos().add(siacRSoggettoStato);
		siacRSoggettoStato.setSiacTSoggetto(this);

		return siacRSoggettoStato;
	}

	/**
	 * Removes the siac r soggetto stato.
	 *
	 * @param siacRSoggettoStato the siac r soggetto stato
	 * @return the siac r soggetto stato
	 */
	public SiacRSoggettoStato removeSiacRSoggettoStato(SiacRSoggettoStato siacRSoggettoStato) {
		getSiacRSoggettoStatos().remove(siacRSoggettoStato);
		siacRSoggettoStato.setSiacTSoggetto(null);

		return siacRSoggettoStato;
	}

	/**
	 * Gets the siac r soggetto tipos.
	 *
	 * @return the siac r soggetto tipos
	 */
	public List<SiacRSoggettoTipo> getSiacRSoggettoTipos() {
		return this.siacRSoggettoTipos;
	}

	/**
	 * Sets the siac r soggetto tipos.
	 *
	 * @param siacRSoggettoTipos the new siac r soggetto tipos
	 */
	public void setSiacRSoggettoTipos(List<SiacRSoggettoTipo> siacRSoggettoTipos) {
		this.siacRSoggettoTipos = siacRSoggettoTipos;
	}

	/**
	 * Adds the siac r soggetto tipo.
	 *
	 * @param siacRSoggettoTipo the siac r soggetto tipo
	 * @return the siac r soggetto tipo
	 */
	public SiacRSoggettoTipo addSiacRSoggettoTipo(SiacRSoggettoTipo siacRSoggettoTipo) {
		getSiacRSoggettoTipos().add(siacRSoggettoTipo);
		siacRSoggettoTipo.setSiacTSoggetto(this);

		return siacRSoggettoTipo;
	}

	/**
	 * Removes the siac r soggetto tipo.
	 *
	 * @param siacRSoggettoTipo the siac r soggetto tipo
	 * @return the siac r soggetto tipo
	 */
	public SiacRSoggettoTipo removeSiacRSoggettoTipo(SiacRSoggettoTipo siacRSoggettoTipo) {
		getSiacRSoggettoTipos().remove(siacRSoggettoTipo);
		siacRSoggettoTipo.setSiacTSoggetto(null);

		return siacRSoggettoTipo;
	}

	/**
	 * Gets the siac r soggetto tipo mods.
	 *
	 * @return the siac r soggetto tipo mods
	 */
	public List<SiacRSoggettoTipoMod> getSiacRSoggettoTipoMods() {
		return this.siacRSoggettoTipoMods;
	}

	/**
	 * Sets the siac r soggetto tipo mods.
	 *
	 * @param siacRSoggettoTipoMods the new siac r soggetto tipo mods
	 */
	public void setSiacRSoggettoTipoMods(List<SiacRSoggettoTipoMod> siacRSoggettoTipoMods) {
		this.siacRSoggettoTipoMods = siacRSoggettoTipoMods;
	}

	/**
	 * Adds the siac r soggetto tipo mod.
	 *
	 * @param siacRSoggettoTipoMod the siac r soggetto tipo mod
	 * @return the siac r soggetto tipo mod
	 */
	public SiacRSoggettoTipoMod addSiacRSoggettoTipoMod(SiacRSoggettoTipoMod siacRSoggettoTipoMod) {
		getSiacRSoggettoTipoMods().add(siacRSoggettoTipoMod);
		siacRSoggettoTipoMod.setSiacTSoggetto(this);

		return siacRSoggettoTipoMod;
	}

	/**
	 * Removes the siac r soggetto tipo mod.
	 *
	 * @param siacRSoggettoTipoMod the siac r soggetto tipo mod
	 * @return the siac r soggetto tipo mod
	 */
	public SiacRSoggettoTipoMod removeSiacRSoggettoTipoMod(SiacRSoggettoTipoMod siacRSoggettoTipoMod) {
		getSiacRSoggettoTipoMods().remove(siacRSoggettoTipoMod);
		siacRSoggettoTipoMod.setSiacTSoggetto(null);

		return siacRSoggettoTipoMod;
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
		siacRSubdocSog.setSiacTSoggetto(this);

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
		siacRSubdocSog.setSiacTSoggetto(null);

		return siacRSubdocSog;
	}

	/**
	 * Gets the siac t indirizzo soggettos.
	 *
	 * @return the siac t indirizzo soggettos
	 */
	public List<SiacTIndirizzoSoggetto> getSiacTIndirizzoSoggettos() {
		return this.siacTIndirizzoSoggettos;
	}

	/**
	 * Sets the siac t indirizzo soggettos.
	 *
	 * @param siacTIndirizzoSoggettos the new siac t indirizzo soggettos
	 */
	public void setSiacTIndirizzoSoggettos(List<SiacTIndirizzoSoggetto> siacTIndirizzoSoggettos) {
		this.siacTIndirizzoSoggettos = siacTIndirizzoSoggettos;
	}

	/**
	 * Adds the siac t indirizzo soggetto.
	 *
	 * @param siacTIndirizzoSoggetto the siac t indirizzo soggetto
	 * @return the siac t indirizzo soggetto
	 */
	public SiacTIndirizzoSoggetto addSiacTIndirizzoSoggetto(SiacTIndirizzoSoggetto siacTIndirizzoSoggetto) {
		getSiacTIndirizzoSoggettos().add(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacTSoggetto(this);

		return siacTIndirizzoSoggetto;
	}

	/**
	 * Removes the siac t indirizzo soggetto.
	 *
	 * @param siacTIndirizzoSoggetto the siac t indirizzo soggetto
	 * @return the siac t indirizzo soggetto
	 */
	public SiacTIndirizzoSoggetto removeSiacTIndirizzoSoggetto(SiacTIndirizzoSoggetto siacTIndirizzoSoggetto) {
		getSiacTIndirizzoSoggettos().remove(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacTSoggetto(null);

		return siacTIndirizzoSoggetto;
	}

	/**
	 * Gets the siac t indirizzo soggetto mods.
	 *
	 * @return the siac t indirizzo soggetto mods
	 */
	public List<SiacTIndirizzoSoggettoMod> getSiacTIndirizzoSoggettoMods() {
		return this.siacTIndirizzoSoggettoMods;
	}

	/**
	 * Sets the siac t indirizzo soggetto mods.
	 *
	 * @param siacTIndirizzoSoggettoMods the new siac t indirizzo soggetto mods
	 */
	public void setSiacTIndirizzoSoggettoMods(List<SiacTIndirizzoSoggettoMod> siacTIndirizzoSoggettoMods) {
		this.siacTIndirizzoSoggettoMods = siacTIndirizzoSoggettoMods;
	}

	/**
	 * Adds the siac t indirizzo soggetto mod.
	 *
	 * @param siacTIndirizzoSoggettoMod the siac t indirizzo soggetto mod
	 * @return the siac t indirizzo soggetto mod
	 */
	public SiacTIndirizzoSoggettoMod addSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoMod siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().add(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTSoggetto(this);

		return siacTIndirizzoSoggettoMod;
	}

	/**
	 * Removes the siac t indirizzo soggetto mod.
	 *
	 * @param siacTIndirizzoSoggettoMod the siac t indirizzo soggetto mod
	 * @return the siac t indirizzo soggetto mod
	 */
	public SiacTIndirizzoSoggettoMod removeSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoMod siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().remove(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTSoggetto(null);

		return siacTIndirizzoSoggettoMod;
	}

	/**
	 * Gets the siac t modpags.
	 *
	 * @return the siac t modpags
	 */
	public List<SiacTModpag> getSiacTModpags() {
		return this.siacTModpags;
	}

	/**
	 * Sets the siac t modpags.
	 *
	 * @param siacTModpags the new siac t modpags
	 */
	public void setSiacTModpags(List<SiacTModpag> siacTModpags) {
		this.siacTModpags = siacTModpags;
	}

	/**
	 * Adds the siac t modpag.
	 *
	 * @param siacTModpag the siac t modpag
	 * @return the siac t modpag
	 */
	public SiacTModpag addSiacTModpag(SiacTModpag siacTModpag) {
		getSiacTModpags().add(siacTModpag);
		siacTModpag.setSiacTSoggetto(this);

		return siacTModpag;
	}

	/**
	 * Removes the siac t modpag.
	 *
	 * @param siacTModpag the siac t modpag
	 * @return the siac t modpag
	 */
	public SiacTModpag removeSiacTModpag(SiacTModpag siacTModpag) {
		getSiacTModpags().remove(siacTModpag);
		siacTModpag.setSiacTSoggetto(null);

		return siacTModpag;
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
		siacTModpagMod.setSiacTSoggetto(this);

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
		siacTModpagMod.setSiacTSoggetto(null);

		return siacTModpagMod;
	}

	/**
	 * Gets the siac t persona fisicas.
	 *
	 * @return the siac t persona fisicas
	 */
	public List<SiacTPersonaFisica> getSiacTPersonaFisicas() {
		return this.siacTPersonaFisicas;
	}

	/**
	 * Sets the siac t persona fisicas.
	 *
	 * @param siacTPersonaFisicas the new siac t persona fisicas
	 */
	public void setSiacTPersonaFisicas(List<SiacTPersonaFisica> siacTPersonaFisicas) {
		this.siacTPersonaFisicas = siacTPersonaFisicas;
	}

	/**
	 * Adds the siac t persona fisica.
	 *
	 * @param siacTPersonaFisica the siac t persona fisica
	 * @return the siac t persona fisica
	 */
	public SiacTPersonaFisica addSiacTPersonaFisica(SiacTPersonaFisica siacTPersonaFisica) {
		getSiacTPersonaFisicas().add(siacTPersonaFisica);
		siacTPersonaFisica.setSiacTSoggetto(this);

		return siacTPersonaFisica;
	}

	/**
	 * Removes the siac t persona fisica.
	 *
	 * @param siacTPersonaFisica the siac t persona fisica
	 * @return the siac t persona fisica
	 */
	public SiacTPersonaFisica removeSiacTPersonaFisica(SiacTPersonaFisica siacTPersonaFisica) {
		getSiacTPersonaFisicas().remove(siacTPersonaFisica);
		siacTPersonaFisica.setSiacTSoggetto(null);

		return siacTPersonaFisica;
	}

	/**
	 * Gets the siac t persona fisica mods.
	 *
	 * @return the siac t persona fisica mods
	 */
	public List<SiacTPersonaFisicaMod> getSiacTPersonaFisicaMods() {
		return this.siacTPersonaFisicaMods;
	}

	/**
	 * Sets the siac t persona fisica mods.
	 *
	 * @param siacTPersonaFisicaMods the new siac t persona fisica mods
	 */
	public void setSiacTPersonaFisicaMods(List<SiacTPersonaFisicaMod> siacTPersonaFisicaMods) {
		this.siacTPersonaFisicaMods = siacTPersonaFisicaMods;
	}

	/**
	 * Adds the siac t persona fisica mod.
	 *
	 * @param siacTPersonaFisicaMod the siac t persona fisica mod
	 * @return the siac t persona fisica mod
	 */
	public SiacTPersonaFisicaMod addSiacTPersonaFisicaMod(SiacTPersonaFisicaMod siacTPersonaFisicaMod) {
		getSiacTPersonaFisicaMods().add(siacTPersonaFisicaMod);
		siacTPersonaFisicaMod.setSiacTSoggetto(this);

		return siacTPersonaFisicaMod;
	}

	/**
	 * Removes the siac t persona fisica mod.
	 *
	 * @param siacTPersonaFisicaMod the siac t persona fisica mod
	 * @return the siac t persona fisica mod
	 */
	public SiacTPersonaFisicaMod removeSiacTPersonaFisicaMod(SiacTPersonaFisicaMod siacTPersonaFisicaMod) {
		getSiacTPersonaFisicaMods().remove(siacTPersonaFisicaMod);
		siacTPersonaFisicaMod.setSiacTSoggetto(null);

		return siacTPersonaFisicaMod;
	}

	/**
	 * Gets the siac t persona giuridicas.
	 *
	 * @return the siac t persona giuridicas
	 */
	public List<SiacTPersonaGiuridica> getSiacTPersonaGiuridicas() {
		return this.siacTPersonaGiuridicas;
	}

	/**
	 * Sets the siac t persona giuridicas.
	 *
	 * @param siacTPersonaGiuridicas the new siac t persona giuridicas
	 */
	public void setSiacTPersonaGiuridicas(List<SiacTPersonaGiuridica> siacTPersonaGiuridicas) {
		this.siacTPersonaGiuridicas = siacTPersonaGiuridicas;
	}

	/**
	 * Adds the siac t persona giuridica.
	 *
	 * @param siacTPersonaGiuridica the siac t persona giuridica
	 * @return the siac t persona giuridica
	 */
	public SiacTPersonaGiuridica addSiacTPersonaGiuridica(SiacTPersonaGiuridica siacTPersonaGiuridica) {
		getSiacTPersonaGiuridicas().add(siacTPersonaGiuridica);
		siacTPersonaGiuridica.setSiacTSoggetto(this);

		return siacTPersonaGiuridica;
	}

	/**
	 * Removes the siac t persona giuridica.
	 *
	 * @param siacTPersonaGiuridica the siac t persona giuridica
	 * @return the siac t persona giuridica
	 */
	public SiacTPersonaGiuridica removeSiacTPersonaGiuridica(SiacTPersonaGiuridica siacTPersonaGiuridica) {
		getSiacTPersonaGiuridicas().remove(siacTPersonaGiuridica);
		siacTPersonaGiuridica.setSiacTSoggetto(null);

		return siacTPersonaGiuridica;
	}

	/**
	 * Gets the siac t persona giuridica mods.
	 *
	 * @return the siac t persona giuridica mods
	 */
	public List<SiacTPersonaGiuridicaMod> getSiacTPersonaGiuridicaMods() {
		return this.siacTPersonaGiuridicaMods;
	}

	/**
	 * Sets the siac t persona giuridica mods.
	 *
	 * @param siacTPersonaGiuridicaMods the new siac t persona giuridica mods
	 */
	public void setSiacTPersonaGiuridicaMods(List<SiacTPersonaGiuridicaMod> siacTPersonaGiuridicaMods) {
		this.siacTPersonaGiuridicaMods = siacTPersonaGiuridicaMods;
	}

	/**
	 * Adds the siac t persona giuridica mod.
	 *
	 * @param siacTPersonaGiuridicaMod the siac t persona giuridica mod
	 * @return the siac t persona giuridica mod
	 */
	public SiacTPersonaGiuridicaMod addSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaMod siacTPersonaGiuridicaMod) {
		getSiacTPersonaGiuridicaMods().add(siacTPersonaGiuridicaMod);
		siacTPersonaGiuridicaMod.setSiacTSoggetto(this);

		return siacTPersonaGiuridicaMod;
	}

	/**
	 * Removes the siac t persona giuridica mod.
	 *
	 * @param siacTPersonaGiuridicaMod the siac t persona giuridica mod
	 * @return the siac t persona giuridica mod
	 */
	public SiacTPersonaGiuridicaMod removeSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaMod siacTPersonaGiuridicaMod) {
		getSiacTPersonaGiuridicaMods().remove(siacTPersonaGiuridicaMod);
		siacTPersonaGiuridicaMod.setSiacTSoggetto(null);

		return siacTPersonaGiuridicaMod;
	}

	/**
	 * Gets the siac t recapito soggettos.
	 *
	 * @return the siac t recapito soggettos
	 */
	public List<SiacTRecapitoSoggetto> getSiacTRecapitoSoggettos() {
		return this.siacTRecapitoSoggettos;
	}

	/**
	 * Sets the siac t recapito soggettos.
	 *
	 * @param siacTRecapitoSoggettos the new siac t recapito soggettos
	 */
	public void setSiacTRecapitoSoggettos(List<SiacTRecapitoSoggetto> siacTRecapitoSoggettos) {
		this.siacTRecapitoSoggettos = siacTRecapitoSoggettos;
	}

	/**
	 * Adds the siac t recapito soggetto.
	 *
	 * @param siacTRecapitoSoggetto the siac t recapito soggetto
	 * @return the siac t recapito soggetto
	 */
	public SiacTRecapitoSoggetto addSiacTRecapitoSoggetto(SiacTRecapitoSoggetto siacTRecapitoSoggetto) {
		getSiacTRecapitoSoggettos().add(siacTRecapitoSoggetto);
		siacTRecapitoSoggetto.setSiacTSoggetto(this);

		return siacTRecapitoSoggetto;
	}

	/**
	 * Removes the siac t recapito soggetto.
	 *
	 * @param siacTRecapitoSoggetto the siac t recapito soggetto
	 * @return the siac t recapito soggetto
	 */
	public SiacTRecapitoSoggetto removeSiacTRecapitoSoggetto(SiacTRecapitoSoggetto siacTRecapitoSoggetto) {
		getSiacTRecapitoSoggettos().remove(siacTRecapitoSoggetto);
		siacTRecapitoSoggetto.setSiacTSoggetto(null);

		return siacTRecapitoSoggetto;
	}

	/**
	 * Gets the siac t recapito soggetto mods.
	 *
	 * @return the siac t recapito soggetto mods
	 */
	public List<SiacTRecapitoSoggettoMod> getSiacTRecapitoSoggettoMods() {
		return this.siacTRecapitoSoggettoMods;
	}

	/**
	 * Sets the siac t recapito soggetto mods.
	 *
	 * @param siacTRecapitoSoggettoMods the new siac t recapito soggetto mods
	 */
	public void setSiacTRecapitoSoggettoMods(List<SiacTRecapitoSoggettoMod> siacTRecapitoSoggettoMods) {
		this.siacTRecapitoSoggettoMods = siacTRecapitoSoggettoMods;
	}

	/**
	 * Adds the siac t recapito soggetto mod.
	 *
	 * @param siacTRecapitoSoggettoMod the siac t recapito soggetto mod
	 * @return the siac t recapito soggetto mod
	 */
	public SiacTRecapitoSoggettoMod addSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoMod siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().add(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacTSoggetto(this);

		return siacTRecapitoSoggettoMod;
	}

	/**
	 * Removes the siac t recapito soggetto mod.
	 *
	 * @param siacTRecapitoSoggettoMod the siac t recapito soggetto mod
	 * @return the siac t recapito soggetto mod
	 */
	public SiacTRecapitoSoggettoMod removeSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoMod siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().remove(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacTSoggetto(null);

		return siacTRecapitoSoggettoMod;
	}

	/**
	 * Gets the siac d ambito.
	 *
	 * @return the siac d ambito
	 */
	public SiacDAmbito getSiacDAmbito() {
		return this.siacDAmbito;
	}

	/**
	 * Sets the siac d ambito.
	 *
	 * @param siacDAmbito the new siac d ambito
	 */
	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	/**
	 * Gets the siac t soggetto mods.
	 *
	 * @return the siac t soggetto mods
	 */
	public List<SiacTSoggettoMod> getSiacTSoggettoMods() {
		return this.siacTSoggettoMods;
	}

	/**
	 * Sets the siac t soggetto mods.
	 *
	 * @param siacTSoggettoMods the new siac t soggetto mods
	 */
	public void setSiacTSoggettoMods(List<SiacTSoggettoMod> siacTSoggettoMods) {
		this.siacTSoggettoMods = siacTSoggettoMods;
	}

	/**
	 * Adds the siac t soggetto mod.
	 *
	 * @param siacTSoggettoMod the siac t soggetto mod
	 * @return the siac t soggetto mod
	 */
	public SiacTSoggettoMod addSiacTSoggettoMod(SiacTSoggettoMod siacTSoggettoMod) {
		getSiacTSoggettoMods().add(siacTSoggettoMod);
		siacTSoggettoMod.setSiacTSoggetto(this);

		return siacTSoggettoMod;
	}

	/**
	 * Removes the siac t soggetto mod.
	 *
	 * @param siacTSoggettoMod the siac t soggetto mod
	 * @return the siac t soggetto mod
	 */
	public SiacTSoggettoMod removeSiacTSoggettoMod(SiacTSoggettoMod siacTSoggettoMod) {
		getSiacTSoggettoMods().remove(siacTSoggettoMod);
		siacTSoggettoMod.setSiacTSoggetto(null);

		return siacTSoggettoMod;
	}

	public List<SiacRConciliazioneBeneficiario> getSiacRConciliazioneBeneficiarios() {
		return this.siacRConciliazioneBeneficiarios;
	}

	public void setSiacRConciliazioneBeneficiarios(List<SiacRConciliazioneBeneficiario> siacRConciliazioneBeneficiarios) {
		this.siacRConciliazioneBeneficiarios = siacRConciliazioneBeneficiarios;
	}

	public SiacRConciliazioneBeneficiario addSiacRConciliazioneBeneficiario(SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario) {
		getSiacRConciliazioneBeneficiarios().add(siacRConciliazioneBeneficiario);
		siacRConciliazioneBeneficiario.setSiacTSoggetto(this);

		return siacRConciliazioneBeneficiario;
	}

	public SiacRConciliazioneBeneficiario removeSiacRConciliazioneBeneficiario(SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario) {
		getSiacRConciliazioneBeneficiarios().remove(siacRConciliazioneBeneficiario);
		siacRConciliazioneBeneficiario.setSiacTSoggetto(null);

		return siacRConciliazioneBeneficiario;
	}

	public List<SiacTRegistroPcc> getSiacTRegistroPccs() {
		return this.siacTRegistroPccs;
	}

	public void setSiacTRegistroPccs(List<SiacTRegistroPcc> siacTRegistroPccs) {
		this.siacTRegistroPccs = siacTRegistroPccs;
	}

	public SiacTRegistroPcc addSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().add(siacTRegistroPcc);
		siacTRegistroPcc.setSiacTSoggetto(this);

		return siacTRegistroPcc;
	}

	public SiacTRegistroPcc removeSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().remove(siacTRegistroPcc);
		siacTRegistroPcc.setSiacTSoggetto(null);

		return siacTRegistroPcc;
	}
	

	public List<SiacTCassaEcon> getSiacTCassaEcons() {
		return siacTCassaEcons;
	}

	public void setSiacTCassaEcons(List<SiacTCassaEcon> siacTCassaEcons) {
		this.siacTCassaEcons = siacTCassaEcons;
	}
	
	public SiacTCassaEcon addSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		getSiacTCassaEcons().add(siacTCassaEcon);
		siacTCassaEcon.setSiacTSoggetto(this);

		return siacTCassaEcon;
	}

	public SiacTCassaEcon removeSiacTRegistroPcc(SiacTCassaEcon siacTCassaEcon) {
		getSiacTCassaEcons().remove(siacTCassaEcon);
		siacTCassaEcon.setSiacTSoggetto(null);

		return siacTCassaEcon;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoId = uid;
	}

	public Date getDataFineValiditaDurc() {
		return dataFineValiditaDurc;
	}

	public void setDataFineValiditaDurc(Date dataFineValiditaDurc) {
		this.dataFineValiditaDurc = dataFineValiditaDurc;
	}

	public Character getTipoFonteDurc() {
		return tipoFonteDurc;
	}

	public void setTipoFonteDurc(Character tipoFonteDurc) {
		this.tipoFonteDurc = tipoFonteDurc;
	}

	public String getFonteDurcAutomatica() {
		return fonteDurcAutomatica;
	}

	public void setFonteDurcAutomatica(String fonteDurcAutomatica) {
		this.fonteDurcAutomatica = fonteDurcAutomatica;
	}

	public String getNoteDurc() {
		return noteDurc;
	}

	public void setNoteDurc(String noteDurc) {
		this.noteDurc = noteDurc;
	}

	public SiacTClass getFonteDurc() {
		return fonteDurc;
	}

	public void setFonteDurc(SiacTClass fonteDurc) {
		this.fonteDurc = fonteDurc;
	}

	public String getEmailPec() {
		return emailPec;
	}

	public void setEmailPec(String emailPec) {
		this.emailPec = emailPec;
	}

	public String getCodDestinatario() {
		return codDestinatario;
	}

	public void setCodDestinatario(String codDestinatario) {
		this.codDestinatario = codDestinatario;
	}

	public String getCanalePA() {
		return canalePA;
	}

	public void setCanalePA(String canalePA) {
		this.canalePA = canalePA;
	}

	/**
	 * @return the loginInserimentoDurc
	 */
	public String getLoginInserimentoDurc() {
		return loginInserimentoDurc;
	}

	/**
	 * @param loginInserimentoDurc the loginInserimentoDurc to set
	 */
	public void setLoginInserimentoDurc(String loginInserimentoDurc) {
		this.loginInserimentoDurc = loginInserimentoDurc;
	}

	/**
	 * @return the loginModificaDurc
	 */
	public String getLoginModificaDurc() {
		return loginModificaDurc;
	}

	/**
	 * @param loginModificaDurc the loginModificaDurc to set
	 */
	public void setLoginModificaDurc(String loginModificaDurc) {
		this.loginModificaDurc = loginModificaDurc;
	}



	public Boolean getIstitutoDiCredito() {
		return istitutoDiCredito;
	}

	public void setIstitutoDiCredito(Boolean istitutoDiCredito) {
		this.istitutoDiCredito = istitutoDiCredito;
	}

}