/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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


/**
 * The persistent class for the siac_t_atto_allegato database table.
 * 
 */
@Entity
@Table(name="siac_t_atto_allegato")
@NamedQuery(name="SiacTAttoAllegato.findAll", query="SELECT s FROM SiacTAttoAllegato s")
public class SiacTAttoAllegato extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ATTO_ALLEGATO_ATTOALID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ATTO_ALLEGATO_ATTOAL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ATTO_ALLEGATO_ATTOALID_GENERATOR")
	@Column(name="attoal_id")
	private Integer attoalId;

	@Column(name="attoal_altriallegati")
	private String attoalAltriallegati;

	@Column(name="attoal_annotazioni")
	private String attoalAnnotazioni;

	@Column(name="attoal_causale")
	private String attoalCausale;

	@Column(name="attoal_data_scadenza")
	private Date attoalDataScadenza;

	@Column(name="attoal_dati_sensibili")
	private String attoalDatiSensibili;

	@Column(name="attoal_note")
	private String attoalNote;

	@Column(name="attoal_pratica")
	private String attoalPratica;

	@Column(name="attoal_responsabile_amm")
	private String attoalResponsabileAmm;

	@Column(name="attoal_responsabile_con")
	private String attoalResponsabileCon;

	@Column(name="attoal_titolario_anno")
	private Integer attoalTitolarioAnno;

	@Column(name="attoal_titolario_numero")
	private String attoalTitolarioNumero;

	@Column(name="attoal_versione_invio_firma")
	private Integer attoalVersioneInvioFirma;

	@Column(name="attoal_data_invio_firma")
	private Date attoalDataInvioFirma;

	@Column(name="attoal_login_invio_firma")
	private String attoalLoginInvioFirma;
	
	@Column(name="attoal_flag_ritenute")
	private Boolean attoalFlagRitenute;

	//bi-directional many-to-one association to SiacTAttoAmm
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacRAttoAllegatoSog
	@OneToMany(mappedBy="siacTAttoAllegato", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRAttoAllegatoSog> siacRAttoAllegatoSogs;
	
	//bi-directional many-to-one association to SiacRAttoAllegatoElencoDoc
	@OneToMany(mappedBy="siacTAttoAllegato", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatoElencoDocs;

	//bi-directional many-to-one association to SiacRAttoAllegatoStato
	@OneToMany(mappedBy="siacTAttoAllegato", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRAttoAllegatoStato> siacRAttoAllegatoStatos;
	
	//bi-directional many-to-one association to SiacTAttoAllegatoStampa
	@OneToMany(mappedBy="siacTAttoAllegato")
	private List<SiacTAttoAllegatoStampa> siacTAttoAllegatoStampas;
	
	// SIAC-4799
	// bi-directional many-to-one association to SiacTCassaEconStampa
	@OneToMany(mappedBy="siacTAttoAllegato")
	private List<SiacTCassaEconStampa> siacTCassaEconStampas;

	public SiacTAttoAllegato() {
	}

	public Integer getAttoalId() {
		return this.attoalId;
	}

	public void setAttoalId(Integer attoalId) {
		this.attoalId = attoalId;
	}

	public String getAttoalAltriallegati() {
		return this.attoalAltriallegati;
	}

	public void setAttoalAltriallegati(String attoalAltriallegati) {
		this.attoalAltriallegati = attoalAltriallegati;
	}

	public String getAttoalAnnotazioni() {
		return this.attoalAnnotazioni;
	}

	public void setAttoalAnnotazioni(String attoalAnnotazioni) {
		this.attoalAnnotazioni = attoalAnnotazioni;
	}

	public String getAttoalCausale() {
		return this.attoalCausale;
	}

	public void setAttoalCausale(String attoalCausale) {
		this.attoalCausale = attoalCausale;
	}

	public Date getAttoalDataScadenza() {
		return this.attoalDataScadenza;
	}

	public void setAttoalDataScadenza( Date attoalDataScadenza) {
		this.attoalDataScadenza = attoalDataScadenza;
	}

	public String getAttoalDatiSensibili() {
		return this.attoalDatiSensibili;
	}

	public void setAttoalDatiSensibili(String attoalDatiSensibili) {
		this.attoalDatiSensibili = attoalDatiSensibili;
	}

	public String getAttoalNote() {
		return this.attoalNote;
	}

	public void setAttoalNote(String attoalNote) {
		this.attoalNote = attoalNote;
	}

	public String getAttoalPratica() {
		return this.attoalPratica;
	}

	public void setAttoalPratica(String attoalPratica) {
		this.attoalPratica = attoalPratica;
	}

	public String getAttoalResponsabileAmm() {
		return this.attoalResponsabileAmm;
	}

	public void setAttoalResponsabileAmm(String attoalResponsabileAmm) {
		this.attoalResponsabileAmm = attoalResponsabileAmm;
	}

	public String getAttoalResponsabileCon() {
		return this.attoalResponsabileCon;
	}

	public void setAttoalResponsabileCon(String attoalResponsabileCon) {
		this.attoalResponsabileCon = attoalResponsabileCon;
	}

	public Integer getAttoalTitolarioAnno() {
		return attoalTitolarioAnno;
	}

	public void setAttoalTitolarioAnno(Integer attoalTitolarioAnno) {
		this.attoalTitolarioAnno = attoalTitolarioAnno;
	}

	public String getAttoalTitolarioNumero() {
		return attoalTitolarioNumero;
	}

	public void setAttoalTitolarioNumero(String attoalTitolarioNumero) {
		this.attoalTitolarioNumero = attoalTitolarioNumero;
	}

	public Integer getAttoalVersioneInvioFirma() {
		return attoalVersioneInvioFirma;
	}

	public void setAttoalVersioneInvioFirma(Integer attoalVersioneInvioFirma) {
		this.attoalVersioneInvioFirma = attoalVersioneInvioFirma;
	}

	public Date getAttoalDataInvioFirma() {
		return attoalDataInvioFirma;
	}

	public void setAttoalDataInvioFirma(Date attoalDataInvioFirma) {
		this.attoalDataInvioFirma = attoalDataInvioFirma;
	}

	public String getAttoalLoginInvioFirma() {
		return attoalLoginInvioFirma;
	}

	public void setAttoalLoginInvioFirma(String attoalLoginInvioFirma) {
		this.attoalLoginInvioFirma = attoalLoginInvioFirma;
	}

	public Boolean getAttoalFlagRitenute() {
		return attoalFlagRitenute;
	}

	public void setAttoalFlagRitenute(Boolean attoalFlagRitenute) {
		this.attoalFlagRitenute = attoalFlagRitenute;
	}

	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	public List<SiacRAttoAllegatoSog> getSiacRAttoAllegatoSogs() {
		return this.siacRAttoAllegatoSogs;
	}

	public void setSiacRAttoAllegatoSogs(List<SiacRAttoAllegatoSog> siacRAttoAllegatoSogs) {
		this.siacRAttoAllegatoSogs = siacRAttoAllegatoSogs;
	}

	public SiacRAttoAllegatoSog addSiacRAttoAllegatoSog(SiacRAttoAllegatoSog siacRAttoAllegatoSog) {
		getSiacRAttoAllegatoSogs().add(siacRAttoAllegatoSog);
		siacRAttoAllegatoSog.setSiacTAttoAllegato(this);

		return siacRAttoAllegatoSog;
	}

	public SiacRAttoAllegatoSog removeSiacRAttoAllegatoSog(SiacRAttoAllegatoSog siacRAttoAllegatoSog) {
		getSiacRAttoAllegatoSogs().remove(siacRAttoAllegatoSog);
		siacRAttoAllegatoSog.setSiacTAttoAllegato(null);

		return siacRAttoAllegatoSog;
	}
	
	public List<SiacRAttoAllegatoElencoDoc> getSiacRAttoAllegatoElencoDocs() {
		return this.siacRAttoAllegatoElencoDocs;
	}

	public void setSiacRAttoAllegatoElencoDocs(List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatoElencoDocs) {
		this.siacRAttoAllegatoElencoDocs = siacRAttoAllegatoElencoDocs;
	}

	public SiacRAttoAllegatoElencoDoc addSiacRAttoAllegatoElencoDoc(SiacRAttoAllegatoElencoDoc siacRAttoAllegatoElencoDoc) {
		getSiacRAttoAllegatoElencoDocs().add(siacRAttoAllegatoElencoDoc);
		siacRAttoAllegatoElencoDoc.setSiacTAttoAllegato(this);

		return siacRAttoAllegatoElencoDoc;
	}

	public SiacRAttoAllegatoElencoDoc removeSiacRAttoAllegatoElencoDoc(SiacRAttoAllegatoElencoDoc siacRAttoAllegatoElencoDoc) {
		getSiacRAttoAllegatoElencoDocs().remove(siacRAttoAllegatoElencoDoc);
		siacRAttoAllegatoElencoDoc.setSiacTAttoAllegato(null);

		return siacRAttoAllegatoElencoDoc;
	}
	
	public List<SiacRAttoAllegatoStato> getSiacRAttoAllegatoStatos() {
		return this.siacRAttoAllegatoStatos;
	}

	public void setSiacRAttoAllegatoStatos(List<SiacRAttoAllegatoStato> siacRAttoAllegatoStatos) {
		this.siacRAttoAllegatoStatos = siacRAttoAllegatoStatos;
	}

	public SiacRAttoAllegatoStato addSiacRAttoAllegatoStato(SiacRAttoAllegatoStato siacRAttoAllegatoStato) {
		getSiacRAttoAllegatoStatos().add(siacRAttoAllegatoStato);
		siacRAttoAllegatoStato.setSiacTAttoAllegato(this);

		return siacRAttoAllegatoStato;
	}

	public SiacRAttoAllegatoStato removeSiacRAttoAllegatoStato(SiacRAttoAllegatoStato siacRAttoAllegatoStato) {
		getSiacRAttoAllegatoStatos().remove(siacRAttoAllegatoStato);
		siacRAttoAllegatoStato.setSiacTAttoAllegato(null);

		return siacRAttoAllegatoStato;
	}

	public List<SiacTAttoAllegatoStampa> getSiacTAttoAllegatoStampas() {
		return this.siacTAttoAllegatoStampas;
	}

	public void setSiacTAttoAllegatoStampas(List<SiacTAttoAllegatoStampa> siacTAttoAllegatoStampas) {
		this.siacTAttoAllegatoStampas = siacTAttoAllegatoStampas;
	}

	public SiacTAttoAllegatoStampa addSiacTAttoAllegatoStampa(SiacTAttoAllegatoStampa siacTAttoAllegatoStampa) {
		getSiacTAttoAllegatoStampas().add(siacTAttoAllegatoStampa);
		siacTAttoAllegatoStampa.setSiacTAttoAllegato(this);

		return siacTAttoAllegatoStampa;
	}

	public SiacTAttoAllegatoStampa removeSiacTAttoAllegatoStampa(SiacTAttoAllegatoStampa siacTAttoAllegatoStampa) {
		getSiacTAttoAllegatoStampas().remove(siacTAttoAllegatoStampa);
		siacTAttoAllegatoStampa.setSiacTAttoAllegato(null);

		return siacTAttoAllegatoStampa;
	}
	
	public List<SiacTCassaEconStampa> getSiacTCassaEconStampas() {
		return siacTCassaEconStampas;
	}

	public void setSiacTCassaEconStampas(List<SiacTCassaEconStampa> siacTCassaEconStampas) {
		this.siacTCassaEconStampas = siacTCassaEconStampas;
	}
	
	public SiacTCassaEconStampa addSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		getSiacTCassaEconStampas().add(siacTCassaEconStampa);
		siacTCassaEconStampa.setSiacTAttoAllegato(this);

		return siacTCassaEconStampa;
	}

	public SiacTCassaEconStampa removeSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		getSiacTCassaEconStampas().remove(siacTCassaEconStampa);
		siacTCassaEconStampa.setSiacTAttoAllegato(null);

		return siacTCassaEconStampa;
	}

	@Override
	public Integer getUid() {
		return attoalId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalId = uid;
	}

}