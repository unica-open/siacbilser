/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;



/**
 * The persistent class for the siac_t_soggetto database table.
 * 
 ***
 * The persistent class for the siac_t_soggetto database table.
 * 
 *  ATTENZIONE ALLE RELAZIONI @OneToOne
 *  
 *  bisogna impostare il set del padre in questo caso il soggetto come
 *  suggerito dalla seguente discussione
 *  
 *  http://stackoverflow.com/questions/5325037/jpa-problem-one-to-one-association-cascade-persist
 * 
 */
@Entity
@Table(name="siac_t_soggetto")
public class SiacTSoggettoFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_SOGGETTO_SOGGETTO_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_soggetto_soggetto_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SOGGETTO_SOGGETTO_ID_GENERATOR")
	@Column(name="soggetto_id")
	private Integer soggettoId;

	//@Column(name="ambito_id")
	//private Integer ambitoId;

	@Column(name="codice_fiscale")
	private String codiceFiscale;

	@Column(name="codice_fiscale_estero")
	private String codiceFiscaleEstero;	

	@Column(name="partita_iva")
	private String partitaIva;

	@Column(name="soggetto_code")
	private String soggettoCode;

	@Column(name="soggetto_desc")
	private String soggettoDesc;
	
	@Column(name="email_PEC")
	private String emailPec;
	
	@Column(name="cod_Destinatario")
	private String codDestinatario;
	
	@Column(name="codice_pa")
	private String canalePA;

	//bi-directional many-to-one association to SiacTPersonaFisicaFin
	// @OneToOne(mappedBy="siacTSoggetto", optional=true, cascade = {CascadeType.ALL})
	@OneToMany(mappedBy="siacTSoggetto", cascade = {CascadeType.ALL})
	private List<SiacTPersonaFisicaFin> siacTPersonaFisica;

	//bi-directional many-to-one association to SiacTPersonaGiuridicaFin
	// @OneToOne(mappedBy="siacTSoggetto", optional=true, cascade = {CascadeType.ALL})
	@OneToMany(mappedBy="siacTSoggetto", cascade = {CascadeType.ALL})
	private List<SiacTPersonaGiuridicaFin> siacTPersonaGiuridica;

	//bi-directional many-to-one association to SiacDAmbitoFin
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbitoFin siacDAmbito;

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
	@OneToMany(mappedBy="siacTSoggetto1")
	private List<SiacRSoggettoRelazFin> siacRSoggettoRelazs1;	

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
	@OneToMany(mappedBy="siacTSoggetto2")
	private List<SiacRSoggettoRelazFin> siacRSoggettoRelazs2;	

	//bi-directional many-to-one association to SiacRFormaGiuridicaFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRFormaGiuridicaFin> siacRFormaGiuridicas;

	//bi-directional many-to-one association to SiacRMovgestTsSogFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRMovgestTsSogFin> siacRMovgestTsSogs;

	//bi-directional many-to-one association to SiacRSoggettoAttrFin
	@OneToMany(mappedBy="siacTSoggetto", cascade = {CascadeType.ALL})
	private List<SiacRSoggettoAttrFin> siacRSoggettoAttrs;

	//bi-directional many-to-one association to SiacRSoggettoAttrModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoAttrModFin> siacRSoggettoAttrMods;

	//bi-directional many-to-one association to SiacRSoggettoClasseFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoClasseFin> siacRSoggettoClasses;

	//bi-directional many-to-one association to SiacRSoggettoClasseModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoClasseModFin> siacRSoggettoClasseMods;

	//bi-directional many-to-one association to SiacRSoggettoOnereFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoOnereFin> siacRSoggettoOneres;

	//bi-directional many-to-one association to SiacRSoggettoOnereModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoOnereModFin> siacRSoggettoOnereMods;	

	//bi-directional many-to-one association to SiacRSoggettoRelazModFin
	@OneToMany(mappedBy="siacTSoggetto1")
	private List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods1;

	//bi-directional many-to-one association to SiacRSoggettoRelazModFin
	@OneToMany(mappedBy="siacTSoggetto2")
	private List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods2;

	//bi-directional many-to-one association to SiacRSoggettoRuoloFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoRuoloFin> siacRSoggettoRuolos;

	//bi-directional many-to-one association to SiacRSoggettoStatoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoStatoFin> siacRSoggettoStatos;

	//bi-directional many-to-one association to SiacRSoggettoTipoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSoggettoTipoFin> siacRSoggettoTipos;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTIndirizzoSoggettoFin> siacTIndirizzoSoggettos;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods;

	//bi-directional many-to-one association to SiacTModpagFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTModpagFin> siacTModpags;

	//bi-directional many-to-one association to SiacTModpagModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTModpagModFin> siacTModpagMods;	 

	//bi-directional many-to-one association to SiacTPersonaFisicaModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTPersonaFisicaModFin> siacTPersonaFisicaMods;	

	//bi-directional many-to-one association to SiacTPersonaGiuridicaModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTPersonaGiuridicaModFin> siacTPersonaGiuridicaMods;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTRecapitoSoggettoFin> siacTRecapitoSoggettos;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTRecapitoSoggettoModFin> siacTRecapitoSoggettoMods;	 

	//bi-directional many-to-one association to SiacTSoggettoModFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacTSoggettoModFin> siacTSoggettoMods;	

	//bi-directional many-to-one association to SiacRMutuoSoggettoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRMutuoSoggettoFin> siacRMutuoSoggettos;

	//bi-directional many-to-one association to SiacRCartacontDetSoggettoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRCartacontDetSoggettoFin> siacRCartacontDetSoggettos;

	//bi-directional many-to-one association to SiacRLiquidazioneSoggettoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRLiquidazioneSoggettoFin> siacRLiquidazioneSoggettos;


	//bi-directional many-to-one association to SiacROrdinativoSoggettoFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacROrdinativoSoggettoFin> siacROrdinativoSoggettos;

	//bi-directional many-to-one association to SiacRDocSogFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRDocSogFin> siacRDocSogs;

	//bi-directional many-to-one association to SiacRSubdocSogFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRSubdocSogFin> siacRSubdocSogs;

	
	// aggancio ill numeratore delle mdp
	
	//bi-directional many-to-one association to SiacRModpagOrdineFin
	@OneToMany(mappedBy="siacTSoggetto")
	private List<SiacRModpagOrdineFin> siacRModpagOrdines;
	
	
	
	@Column(name="soggetto_fine_validita_durc")
	private Date dataFineValiditaDurc;

	@Column(name="soggetto_tipo_fonte_durc")
	private Character tipoFonteDurc;

	@ManyToOne
	@JoinColumn(name="soggetto_fonte_durc_manuale_classif_id")
	private SiacTClassFin fonteDurc;

	@Column(name="soggetto_fonte_durc_automatica")
	private String fonteDurcAutomatica;

	@Column(name="soggetto_note_durc")
	private String noteDurc;

	@Column(name="login_inserimento_durc")
	private String loginInserimentoDurc;

	@Column(name="login_modifica_durc")
	private String loginModificaDurc;
	
	public List<SiacRModpagOrdineFin> getSiacRModpagOrdines() {
		return this.siacRModpagOrdines;
	}

	public void setSiacRModpagOrdines(List<SiacRModpagOrdineFin> siacRModpagOrdines) {
		this.siacRModpagOrdines = siacRModpagOrdines;
	}

	public SiacRModpagOrdineFin addSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
		getSiacRModpagOrdines().add(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTSoggetto(this);

		return siacRModpagOrdine;
	}

	public SiacRModpagOrdineFin removeSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
		getSiacRModpagOrdines().remove(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTSoggetto(null);

		return siacRModpagOrdine;
	}
	
	public SiacDAmbitoFin getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbitoFin siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	public SiacTSoggettoFin() {
	}

	public Integer getSoggettoId() {
		return this.soggettoId;
	}

	public void setSoggettoId(Integer soggettoId) {
		this.soggettoId = soggettoId;
	}	

	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCodiceFiscaleEstero() {
		return this.codiceFiscaleEstero;
	}

	public void setCodiceFiscaleEstero(String codiceFiscaleEstero) {
		this.codiceFiscaleEstero = codiceFiscaleEstero;
	}

	public String getPartitaIva() {
		return this.partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getSoggettoCode() {
		return this.soggettoCode;
	}

	public void setSoggettoCode(String soggettoCode) {
		this.soggettoCode = soggettoCode;
	}

	public String getSoggettoDesc() {
		return this.soggettoDesc;
	}

	//	public void setSoggettoDesc(String soggettoDesc) {
	//		this.soggettoDesc = soggettoDesc;
	//	}

	public void setSoggettoDesc(String soggettoDesc) {
		if(null==soggettoDesc){
			soggettoDesc = "";
		}
		this.soggettoDesc = soggettoDesc.toUpperCase();
	}

	public List<SiacRFormaGiuridicaFin> getSiacRFormaGiuridicas() {
		return siacRFormaGiuridicas;
	}

	public void setSiacRFormaGiuridicas(
			List<SiacRFormaGiuridicaFin> siacRFormaGiuridicas) {
		this.siacRFormaGiuridicas = siacRFormaGiuridicas;
	}

	public List<SiacRMovgestTsSogFin> getSiacRMovgestTsSogs() {
		return siacRMovgestTsSogs;
	}

	public void setSiacRMovgestTsSogs(List<SiacRMovgestTsSogFin> siacRMovgestTsSogs) {
		this.siacRMovgestTsSogs = siacRMovgestTsSogs;
	}

	public List<SiacRSoggettoAttrFin> getSiacRSoggettoAttrs() {
		return siacRSoggettoAttrs;
	}

	public void setSiacRSoggettoAttrs(List<SiacRSoggettoAttrFin> siacRSoggettoAttrs) {
		this.siacRSoggettoAttrs = siacRSoggettoAttrs;
	}

	public List<SiacRSoggettoAttrModFin> getSiacRSoggettoAttrMods() {
		return siacRSoggettoAttrMods;
	}

	public void setSiacRSoggettoAttrMods(
			List<SiacRSoggettoAttrModFin> siacRSoggettoAttrMods) {
		this.siacRSoggettoAttrMods = siacRSoggettoAttrMods;
	}

	public List<SiacRSoggettoClasseFin> getSiacRSoggettoClasses() {
		return siacRSoggettoClasses;
	}

	public void setSiacRSoggettoClasses(
			List<SiacRSoggettoClasseFin> siacRSoggettoClasses) {
		this.siacRSoggettoClasses = siacRSoggettoClasses;
	}

	public List<SiacRSoggettoClasseModFin> getSiacRSoggettoClasseMods() {
		return siacRSoggettoClasseMods;
	}

	public void setSiacRSoggettoClasseMods(
			List<SiacRSoggettoClasseModFin> siacRSoggettoClasseMods) {
		this.siacRSoggettoClasseMods = siacRSoggettoClasseMods;
	}

	public List<SiacRSoggettoOnereFin> getSiacRSoggettoOneres() {
		return siacRSoggettoOneres;
	}

	public void setSiacRSoggettoOneres(List<SiacRSoggettoOnereFin> siacRSoggettoOneres) {
		this.siacRSoggettoOneres = siacRSoggettoOneres;
	}

	public List<SiacRSoggettoOnereModFin> getSiacRSoggettoOnereMods() {
		return siacRSoggettoOnereMods;
	}

	public void setSiacRSoggettoOnereMods(
			List<SiacRSoggettoOnereModFin> siacRSoggettoOnereMods) {
		this.siacRSoggettoOnereMods = siacRSoggettoOnereMods;
	}

	public List<SiacRSoggettoRelazFin> getSiacRSoggettoRelazs1() {
		return siacRSoggettoRelazs1;
	}

	public void setSiacRSoggettoRelazs1(
			List<SiacRSoggettoRelazFin> siacRSoggettoRelazs1) {
		this.siacRSoggettoRelazs1 = siacRSoggettoRelazs1;
	}

	public List<SiacRSoggettoRelazModFin> getSiacRSoggettoRelazMods1() {
		return siacRSoggettoRelazMods1;
	}

	public void setSiacRSoggettoRelazMods1(
			List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods1) {
		this.siacRSoggettoRelazMods1 = siacRSoggettoRelazMods1;
	}

	public List<SiacRSoggettoRelazModFin> getSiacRSoggettoRelazMods2() {
		return siacRSoggettoRelazMods2;
	}

	public void setSiacRSoggettoRelazMods2(
			List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods2) {
		this.siacRSoggettoRelazMods2 = siacRSoggettoRelazMods2;
	}

	public List<SiacRSoggettoRuoloFin> getSiacRSoggettoRuolos() {
		return siacRSoggettoRuolos;
	}

	public void setSiacRSoggettoRuolos(List<SiacRSoggettoRuoloFin> siacRSoggettoRuolos) {
		this.siacRSoggettoRuolos = siacRSoggettoRuolos;
	}

	public List<SiacRSoggettoStatoFin> getSiacRSoggettoStatos() {
		return siacRSoggettoStatos;
	}

	public void setSiacRSoggettoStatos(List<SiacRSoggettoStatoFin> siacRSoggettoStatos) {
		this.siacRSoggettoStatos = siacRSoggettoStatos;
	}

	public List<SiacRSoggettoTipoFin> getSiacRSoggettoTipos() {
		return siacRSoggettoTipos;
	}

	public void setSiacRSoggettoTipos(List<SiacRSoggettoTipoFin> siacRSoggettoTipos) {
		this.siacRSoggettoTipos = siacRSoggettoTipos;
	}

	public List<SiacTIndirizzoSoggettoFin> getSiacTIndirizzoSoggettos() {
		return siacTIndirizzoSoggettos;
	}

	public void setSiacTIndirizzoSoggettos(
			List<SiacTIndirizzoSoggettoFin> siacTIndirizzoSoggettos) {
		this.siacTIndirizzoSoggettos = siacTIndirizzoSoggettos;
	}

	public List<SiacTIndirizzoSoggettoModFin> getSiacTIndirizzoSoggettoMods() {
		return siacTIndirizzoSoggettoMods;
	}

	public void setSiacTIndirizzoSoggettoMods(
			List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods) {
		this.siacTIndirizzoSoggettoMods = siacTIndirizzoSoggettoMods;
	}

	public List<SiacTModpagFin> getSiacTModpags() {
		return siacTModpags;
	}

	public void setSiacTModpags(List<SiacTModpagFin> siacTModpags) {
		this.siacTModpags = siacTModpags;
	}

	public List<SiacTModpagModFin> getSiacTModpagMods() {
		return siacTModpagMods;
	}

	public void setSiacTModpagMods(List<SiacTModpagModFin> siacTModpagMods) {
		this.siacTModpagMods = siacTModpagMods;
	}

	public List<SiacTPersonaFisicaModFin> getSiacTPersonaFisicaMods() {
		return siacTPersonaFisicaMods;
	}

	public void setSiacTPersonaFisicaMods(
			List<SiacTPersonaFisicaModFin> siacTPersonaFisicaMods) {
		this.siacTPersonaFisicaMods = siacTPersonaFisicaMods;
	}

	public List<SiacTPersonaGiuridicaModFin> getSiacTPersonaGiuridicaMods() {
		return siacTPersonaGiuridicaMods;
	}

	public void setSiacTPersonaGiuridicaMods(
			List<SiacTPersonaGiuridicaModFin> siacTPersonaGiuridicaMods) {
		this.siacTPersonaGiuridicaMods = siacTPersonaGiuridicaMods;
	}

	public List<SiacTRecapitoSoggettoFin> getSiacTRecapitoSoggettos() {
		return siacTRecapitoSoggettos;
	}

	public void setSiacTRecapitoSoggettos(
			List<SiacTRecapitoSoggettoFin> siacTRecapitoSoggettos) {
		this.siacTRecapitoSoggettos = siacTRecapitoSoggettos;
	}

	public List<SiacTRecapitoSoggettoModFin> getSiacTRecapitoSoggettoMods() {
		return siacTRecapitoSoggettoMods;
	}

	public void setSiacTRecapitoSoggettoMods(
			List<SiacTRecapitoSoggettoModFin> siacTRecapitoSoggettoMods) {
		this.siacTRecapitoSoggettoMods = siacTRecapitoSoggettoMods;
	}

	public List<SiacTSoggettoModFin> getSiacTSoggettoMods() {
		return siacTSoggettoMods;
	}

	public void setSiacTSoggettoMods(List<SiacTSoggettoModFin> siacTSoggettoMods) {
		this.siacTSoggettoMods = siacTSoggettoMods;
	}	 

	public List<SiacRSoggettoRelazFin> getSiacRSoggettoRelazs2() {
		return siacRSoggettoRelazs2;
	}

	public void setSiacRSoggettoRelazs2(
			List<SiacRSoggettoRelazFin> siacRSoggettoRelazs2) {
		this.siacRSoggettoRelazs2 = siacRSoggettoRelazs2;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoId = uid;
	}

	public List<SiacRMutuoSoggettoFin> getSiacRMutuoSoggettos() {
		return this.siacRMutuoSoggettos;
	}

	public void setSiacRMutuoSoggettos(List<SiacRMutuoSoggettoFin> siacRMutuoSoggettos) {
		this.siacRMutuoSoggettos = siacRMutuoSoggettos;
	}

	public SiacRMutuoSoggettoFin addSiacRMutuoSoggetto(SiacRMutuoSoggettoFin siacRMutuoSoggetto) {
		getSiacRMutuoSoggettos().add(siacRMutuoSoggetto);
		siacRMutuoSoggetto.setSiacTSoggetto(this);

		return siacRMutuoSoggetto;
	}

	public SiacRMutuoSoggettoFin removeSiacRMutuoSoggetto(SiacRMutuoSoggettoFin siacRMutuoSoggetto) {
		getSiacRMutuoSoggettos().remove(siacRMutuoSoggetto);
		siacRMutuoSoggetto.setSiacTSoggetto(null);

		return siacRMutuoSoggetto;
	}

	public List<SiacTPersonaFisicaFin> getSiacTPersonaFisica() {
		return siacTPersonaFisica;
	}

	public void setSiacTPersonaFisica(List<SiacTPersonaFisicaFin> siacTPersonaFisica) {
		this.siacTPersonaFisica = siacTPersonaFisica;
	}

	public List<SiacTPersonaGiuridicaFin> getSiacTPersonaGiuridica() {
		return siacTPersonaGiuridica;
	}

	public void setSiacTPersonaGiuridica(
			List<SiacTPersonaGiuridicaFin> siacTPersonaGiuridica) {
		this.siacTPersonaGiuridica = siacTPersonaGiuridica;
	}	

	public List<SiacRCartacontDetSoggettoFin> getSiacRCartacontDetSoggettos() {
		return this.siacRCartacontDetSoggettos;
	}

	public void setSiacRCartacontDetSoggettos(List<SiacRCartacontDetSoggettoFin> siacRCartacontDetSoggettos) {
		this.siacRCartacontDetSoggettos = siacRCartacontDetSoggettos;
	}

	public SiacRCartacontDetSoggettoFin addSiacRCartacontDetSoggetto(SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().add(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTSoggetto(this);

		return siacRCartacontDetSoggetto;
	}

	public SiacRCartacontDetSoggettoFin removeSiacRCartacontDetSoggetto(SiacRCartacontDetSoggettoFin siacRCartacontDetSoggetto) {
		getSiacRCartacontDetSoggettos().remove(siacRCartacontDetSoggetto);
		siacRCartacontDetSoggetto.setSiacTSoggetto(null);

		return siacRCartacontDetSoggetto;
	}

	public List<SiacRLiquidazioneSoggettoFin> getSiacRLiquidazioneSoggettos() {
		return this.siacRLiquidazioneSoggettos;
	}

	public void setSiacRLiquidazioneSoggettos(List<SiacRLiquidazioneSoggettoFin> siacRLiquidazioneSoggettos) {
		this.siacRLiquidazioneSoggettos = siacRLiquidazioneSoggettos;
	}

	public SiacRLiquidazioneSoggettoFin addSiacRLiquidazioneSoggetto(SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto) {
		getSiacRLiquidazioneSoggettos().add(siacRLiquidazioneSoggetto);
		siacRLiquidazioneSoggetto.setSiacTSoggetto(this);

		return siacRLiquidazioneSoggetto;
	}

	public SiacRLiquidazioneSoggettoFin removeSiacRLiquidazioneSoggetto(SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto) {
		getSiacRLiquidazioneSoggettos().remove(siacRLiquidazioneSoggetto);
		siacRLiquidazioneSoggetto.setSiacTSoggetto(null);

		return siacRLiquidazioneSoggetto;
	}


	public List<SiacROrdinativoSoggettoFin> getSiacROrdinativoSoggettos() {
		return this.siacROrdinativoSoggettos;
	}

	public void setSiacROrdinativoSoggettos(List<SiacROrdinativoSoggettoFin> siacROrdinativoSoggettos) {
		this.siacROrdinativoSoggettos = siacROrdinativoSoggettos;
	}

	public SiacROrdinativoSoggettoFin addSiacROrdinativoSoggetto(SiacROrdinativoSoggettoFin siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().add(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTSoggetto(this);

		return siacROrdinativoSoggetto;
	}

	public SiacROrdinativoSoggettoFin removeSiacROrdinativoSoggetto(SiacROrdinativoSoggettoFin siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().remove(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTSoggetto(null);

		return siacROrdinativoSoggetto;
	}

	public List<SiacRDocSogFin> getSiacRDocSogs() {
		return this.siacRDocSogs;
	}

	public void setSiacRDocSogs(List<SiacRDocSogFin> siacRDocSogs) {
		this.siacRDocSogs = siacRDocSogs;
	}

	//	 public SiacRDocSogFin addSiacRDocSog(SiacRDocSogFin siacRDocSog) {
	//	  getSiacRDocSogs().add(siacRDocSog);
	//	  siacRDocSog.setSiacTDoc(this);
	//	
	//	  return siacRDocSog;
	//	 }
	//
	//	 public SiacRDocSogFin removeSiacRDocSog(SiacRDocSogFin siacRDocSog) {
	//	  getSiacRDocSogs().remove(siacRDocSog);
	//	  siacRDocSog.setSiacTDoc(null);
	//	
	//	  return siacRDocSog;
	//	 }
	//	 

	public List<SiacRSubdocSogFin> getSiacRSubdocSogs() {
		return this.siacRSubdocSogs;
	}

	public void setSiacRSubdocSogs(List<SiacRSubdocSogFin> siacRSubdocSogs) {
		this.siacRSubdocSogs = siacRSubdocSogs;
	}

	public SiacRSubdocSogFin addSiacRSubdocSog(SiacRSubdocSogFin siacRSubdocSog) {
		getSiacRSubdocSogs().add(siacRSubdocSog);
		siacRSubdocSog.setSiacTSoggetto(this);

		return siacRSubdocSog;
	}

	public SiacRSubdocSogFin removeSiacRSubdocSog(SiacRSubdocSogFin siacRSubdocSog) {
		getSiacRSubdocSogs().remove(siacRSubdocSog);
		siacRSubdocSog.setSiacTSoggetto(null);

		return siacRSubdocSog;
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

	public SiacTClassFin getFonteDurc() {
		return fonteDurc;
	}

	public void setFonteDurc(SiacTClassFin fonteDurc) {
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
}