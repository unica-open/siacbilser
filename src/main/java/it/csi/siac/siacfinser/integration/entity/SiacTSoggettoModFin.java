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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_soggetto_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_soggetto_mod")
public class SiacTSoggettoModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_SOGGETTO_MOD_SOGGETTO_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_soggetto_mod_sog_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SOGGETTO_MOD_SOGGETTO_MOD_ID_GENERATOR")
	@Column(name="sog_mod_id")
	private Integer sogModId;

//	@Column(name="ambito_id")
//	private Integer ambitoId;
	
	//bi-directional many-to-one association to SiacDAmbitoFin
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbitoFin siacDAmbito;

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
	
	
	@Column(name="login_inserimento_durc")
	private String loginInserimentoDurc;

	@Column(name="login_modifica_durc")
	private String loginModificaDurc;
	
	
	//bi-directional many-to-one association to SiacRSoggettoAttrModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoAttrModFin> siacRSoggettoAttrMods;

	//bi-directional many-to-one association to SiacRSoggettoClasseModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoClasseModFin> siacRSoggettoClasseMods;

	//bi-directional many-to-one association to SiacRSoggettoOnereModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoOnereModFin> siacRSoggettoOnereMods;

	//bi-directional many-to-one association to SiacRSoggettoRelazModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods;

	//bi-directional many-to-one association to SiacRSoggrelModpagModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods;

	//bi-directional many-to-one association to SiacTModpagModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacTModpagModFin> siacTModpagMods;

	//bi-directional many-to-one association to SiacTPersonaFisicaModFin
//	@OneToOne(mappedBy="siacTSoggettoMod", optional=true, cascade = {CascadeType.ALL})
	@OneToMany(mappedBy="siacTSoggettoMod", cascade = {CascadeType.ALL})
	private List<SiacTPersonaFisicaModFin> siacTPersonaFisicaMods;

	//bi-directional many-to-one association to SiacTPersonaGiuridicaModFin
//	@OneToOne(mappedBy="siacTSoggettoMod", optional=true, cascade = {CascadeType.ALL})
	@OneToMany(mappedBy="siacTSoggettoMod", cascade = {CascadeType.ALL})
	private List<SiacTPersonaGiuridicaModFin> siacTPersonaGiuridicaMods;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoModFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacTRecapitoSoggettoModFin> siacTRecapitoSoggettoMods;

	//bi-directional many-to-one association to SiacTModificaFin
//	@ManyToOne
//	@JoinColumn(name="mod_id")
//	private SiacTModificaFin siacTModifica;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacRSoggettoTipoFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoTipoModFin> siacRSoggettoTipoMods;
	
	//bi-directional many-to-one association to SiacRFormaGiuridicaFin
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRFormaGiuridicaModFin> siacRFormaGiuridicaMods;
		 
	
	
	
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
	
	
	public SiacTSoggettoModFin() {
	}

	public Integer getSogModId() {
		return this.sogModId;
	}

	public void setSogModId(Integer sogModId) {
		this.sogModId = sogModId;
	}

	public SiacDAmbitoFin getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbitoFin siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
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

	public List<SiacRSoggettoAttrModFin> getSiacRSoggettoAttrMods() {
		return this.siacRSoggettoAttrMods;
	}

	public void setSiacRSoggettoAttrMods(List<SiacRSoggettoAttrModFin> siacRSoggettoAttrMods) {
		this.siacRSoggettoAttrMods = siacRSoggettoAttrMods;
	}

	public SiacRSoggettoAttrModFin addSiacRSoggettoAttrMod(SiacRSoggettoAttrModFin siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().add(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTSoggettoMod(this);

		return siacRSoggettoAttrMod;
	}

	public SiacRSoggettoAttrModFin removeSiacRSoggettoAttrMod(SiacRSoggettoAttrModFin siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().remove(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTSoggettoMod(null);

		return siacRSoggettoAttrMod;
	}

	public List<SiacRSoggettoClasseModFin> getSiacRSoggettoClasseMods() {
		return this.siacRSoggettoClasseMods;
	}

	public void setSiacRSoggettoClasseMods(List<SiacRSoggettoClasseModFin> siacRSoggettoClasseMods) {
		this.siacRSoggettoClasseMods = siacRSoggettoClasseMods;
	}

	public SiacRSoggettoClasseModFin addSiacRSoggettoClasseMod(SiacRSoggettoClasseModFin siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().add(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacTSoggettoMod(this);

		return siacRSoggettoClasseMod;
	}

	public SiacRSoggettoClasseModFin removeSiacRSoggettoClasseMod(SiacRSoggettoClasseModFin siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().remove(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacTSoggettoMod(null);

		return siacRSoggettoClasseMod;
	}

	public List<SiacRSoggettoOnereModFin> getSiacRSoggettoOnereMods() {
		return this.siacRSoggettoOnereMods;
	}

	public void setSiacRSoggettoOnereMods(List<SiacRSoggettoOnereModFin> siacRSoggettoOnereMods) {
		this.siacRSoggettoOnereMods = siacRSoggettoOnereMods;
	}

	public SiacRSoggettoOnereModFin addSiacRSoggettoOnereMod(SiacRSoggettoOnereModFin siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().add(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacTSoggettoMod(this);

		return siacRSoggettoOnereMod;
	}

	public SiacRSoggettoOnereModFin removeSiacRSoggettoOnereMod(SiacRSoggettoOnereModFin siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().remove(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacTSoggettoMod(null);

		return siacRSoggettoOnereMod;
	}

	public List<SiacRSoggettoRelazModFin> getSiacRSoggettoRelazMods() {
		return this.siacRSoggettoRelazMods;
	}

	public void setSiacRSoggettoRelazMods(List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods) {
		this.siacRSoggettoRelazMods = siacRSoggettoRelazMods;
	}

	public SiacRSoggettoRelazModFin addSiacRSoggettoRelazMod(SiacRSoggettoRelazModFin siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().add(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacTSoggettoMod(this);

		return siacRSoggettoRelazMod;
	}

	public SiacRSoggettoRelazModFin removeSiacRSoggettoRelazMod(SiacRSoggettoRelazModFin siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().remove(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacTSoggettoMod(null);

		return siacRSoggettoRelazMod;
	}

	public List<SiacRSoggrelModpagModFin> getSiacRSoggrelModpagMods() {
		return this.siacRSoggrelModpagMods;
	}

	public void setSiacRSoggrelModpagMods(List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods) {
		this.siacRSoggrelModpagMods = siacRSoggrelModpagMods;
	}

	public SiacRSoggrelModpagModFin addSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().add(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacTSoggettoMod(this);

		return siacRSoggrelModpagMod;
	}

	public SiacRSoggrelModpagModFin removeSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().remove(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacTSoggettoMod(null);

		return siacRSoggrelModpagMod;
	}

	public List<SiacTIndirizzoSoggettoModFin> getSiacTIndirizzoSoggettoMods() {
		return this.siacTIndirizzoSoggettoMods;
	}

	public void setSiacTIndirizzoSoggettoMods(List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods) {
		this.siacTIndirizzoSoggettoMods = siacTIndirizzoSoggettoMods;
	}

	public SiacTIndirizzoSoggettoModFin addSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().add(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTSoggettoMod(this);

		return siacTIndirizzoSoggettoMod;
	}

	public SiacTIndirizzoSoggettoModFin removeSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().remove(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTSoggettoMod(null);

		return siacTIndirizzoSoggettoMod;
	}

	public List<SiacTModpagModFin> getSiacTModpagMods() {
		return this.siacTModpagMods;
	}

	public void setSiacTModpagMods(List<SiacTModpagModFin> siacTModpagMods) {
		this.siacTModpagMods = siacTModpagMods;
	}

	public SiacTModpagModFin addSiacTModpagMod(SiacTModpagModFin siacTModpagMod) {
		getSiacTModpagMods().add(siacTModpagMod);
		siacTModpagMod.setSiacTSoggettoMod(this);

		return siacTModpagMod;
	}

	public SiacTModpagModFin removeSiacTModpagMod(SiacTModpagModFin siacTModpagMod) {
		getSiacTModpagMods().remove(siacTModpagMod);
		siacTModpagMod.setSiacTSoggettoMod(null);

		return siacTModpagMod;
	}

	

//	public SiacTPersonaFisicaModFin addSiacTPersonaFisicaMod(SiacTPersonaFisicaModFin siacTPersonaFisicaMod) {
//		getSiacTPersonaFisicaMods().add(siacTPersonaFisicaMod);
//		siacTPersonaFisicaMod.setSiacTSoggettoMod(this);
//
//		return siacTPersonaFisicaMod;
//	}
//
//	public SiacTPersonaFisicaModFin removeSiacTPersonaFisicaMod(SiacTPersonaFisicaModFin siacTPersonaFisicaMod) {
//		getSiacTPersonaFisicaMods().remove(siacTPersonaFisicaMod);
//		siacTPersonaFisicaMod.setSiacTSoggettoMod(null);
//
//		return siacTPersonaFisicaMod;
//	}

	

//	public SiacTPersonaGiuridicaModFin addSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod) {
//		getSiacTPersonaGiuridicaMods().add(siacTPersonaGiuridicaMod);
//		siacTPersonaGiuridicaMod.setSiacTSoggettoMod(this);
//
//		return siacTPersonaGiuridicaMod;
//	}
//
//	public SiacTPersonaGiuridicaModFin removeSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod) {
//		getSiacTPersonaGiuridicaMods().remove(siacTPersonaGiuridicaMod);
//		siacTPersonaGiuridicaMod.setSiacTSoggettoMod(null);
//
//		return siacTPersonaGiuridicaMod;
//	}

	public List<SiacTRecapitoSoggettoModFin> getSiacTRecapitoSoggettoMods() {
		return this.siacTRecapitoSoggettoMods;
	}

	public void setSiacTRecapitoSoggettoMods(List<SiacTRecapitoSoggettoModFin> siacTRecapitoSoggettoMods) {
		this.siacTRecapitoSoggettoMods = siacTRecapitoSoggettoMods;
	}

	public SiacTRecapitoSoggettoModFin addSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().add(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacTSoggettoMod(this);

		return siacTRecapitoSoggettoMod;
	}

	public SiacTRecapitoSoggettoModFin removeSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().remove(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacTSoggettoMod(null);

		return siacTRecapitoSoggettoMod;
	}

//	public SiacTModificaFin getSiacTModifica() {
//		return this.siacTModifica;
//	}
//
//	public void setSiacTModifica(SiacTModificaFin siacTModifica) {
//		this.siacTModifica = siacTModifica;
//	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.sogModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.sogModId = uid;
	}
	
	public List<SiacRFormaGiuridicaModFin> getSiacRFormaGiuridicaMods() {
		return siacRFormaGiuridicaMods;
	}

	public void setSiacRFormaGiuridicaMods(
			List<SiacRFormaGiuridicaModFin> siacRFormaGiuridicas) {
		this.siacRFormaGiuridicaMods = siacRFormaGiuridicas;
	}
	
	public List<SiacRSoggettoTipoModFin> getSiacRSoggettoTipoMods() {
		return siacRSoggettoTipoMods;
	}

	public void setSiacRSoggettoTipoMods(List<SiacRSoggettoTipoModFin> siacRSoggettoTipoMods) {
		this.siacRSoggettoTipoMods = siacRSoggettoTipoMods;
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

	public SiacTClassFin getFonteDurc() {
		return fonteDurc;
	}

	public void setFonteDurc(SiacTClassFin fonteDurc) {
		this.fonteDurc = fonteDurc;
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