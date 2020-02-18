/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.Date;
import java.util.List;

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
 * The persistent class for the siac_t_modpag database table.
 * 
 */
@Entity
@Table(name="siac_t_modpag")
public class SiacTModpagFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MODPAG_MODPAG_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_modpag_modpag_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODPAG_MODPAG_ID_GENERATOR")
	@Column(name="modpag_id")
	private Integer modpagId;

	@Column(name="bic")
	private String bic;

	@Column(name="contocorrente")
	private String contocorrente;

//	@Column(name="data_scadenza")
//	private Timestamp dataScadenza;
	
	@Column(name="iban")
	private String iban;

	@Column(name="banca_denominazione")
	private String denominazioneBanca;

	@Column(name="note")
	private String note;

	@Column(name="quietanziante")
	private String quietanziante;
	
	// nuovi campi
	@Column(name="contocorrente_intestazione")
	private String contocorrenteIntestazione;
	
	@Column(name="quietanzante_nascita_data")
	private Date quietanzanteNascitaData;
	
	
	@Column(name="quietanziante_nascita_luogo")
	private String quietanzianteNascitaLuogo;
	
	
	@Column(name="quietanziante_nascita_stato")
	private String quietanzianteNascitaStato;
	
	// fine nuovi campi
	
	@Column(name="quietanziante_codice_fiscale")
	private String quietanzianteCodiceFiscale;

	//bi-directional many-to-one association to SiacRModpagStatoFin
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRModpagStatoFin> siacRModpagStatos;

	//bi-directional many-to-one association to SiacRSoggrelModpagFin
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRSoggrelModpagFin> siacRSoggrelModpags;

	//bi-directional many-to-one association to SiacRSoggrelModpagModFin
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods;

	//bi-directional many-to-one association to SiacDAccreditoTipoFin
	@ManyToOne
	@JoinColumn(name="accredito_tipo_id")
	private SiacDAccreditoTipoFin siacDAccreditoTipo;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTModpagModFin
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacTModpagModFin> siacTModpagMods;

	//bi-directional many-to-one association to SiacRCartacontDetModpagFin
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRCartacontDetModpagFin> siacRCartacontDetModpags;

	
	// aggancio il numeratore delle mdp
	//bi-directional many-to-one association to SiacRModpagOrdineFin
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRModpagOrdineFin> siacRModpagOrdines;
	
	
	@Column(name="per_stipendi")
	private Boolean perStipendi;

	
	public SiacTModpagFin() {
	}

	public Integer getModpagId() {
		return this.modpagId;
	}

	public void setModpagId(Integer modpagId) {
		this.modpagId = modpagId;
	}

	public String getBic() {
		return this.bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getContocorrente() {
		return this.contocorrente;
	}

	public void setContocorrente(String contocorrente) {
		this.contocorrente = contocorrente;
	}
	
	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getQuietanziante() {
		return this.quietanziante;
	}

	public void setQuietanziante(String quietanziante) {
		this.quietanziante = quietanziante;
	}

	public String getQuietanzianteCodiceFiscale() {
		return this.quietanzianteCodiceFiscale;
	}

	public void setQuietanzianteCodiceFiscale(String quietanzianteCodiceFiscale) {
		this.quietanzianteCodiceFiscale = quietanzianteCodiceFiscale;
	}

	public List<SiacRModpagStatoFin> getSiacRModpagStatos() {
		return this.siacRModpagStatos;
	}

	public void setSiacRModpagStatos(List<SiacRModpagStatoFin> siacRModpagStatos) {
		this.siacRModpagStatos = siacRModpagStatos;
	}

	public SiacRModpagStatoFin addSiacRModpagStato(SiacRModpagStatoFin siacRModpagStato) {
		getSiacRModpagStatos().add(siacRModpagStato);
		siacRModpagStato.setSiacTModpag(this);

		return siacRModpagStato;
	}

	public SiacRModpagStatoFin removeSiacRModpagStato(SiacRModpagStatoFin siacRModpagStato) {
		getSiacRModpagStatos().remove(siacRModpagStato);
		siacRModpagStato.setSiacTModpag(null);

		return siacRModpagStato;
	}

	public List<SiacRSoggrelModpagFin> getSiacRSoggrelModpags() {
		return this.siacRSoggrelModpags;
	}

	public void setSiacRSoggrelModpags(List<SiacRSoggrelModpagFin> siacRSoggrelModpags) {
		this.siacRSoggrelModpags = siacRSoggrelModpags;
	}

	public String getContocorrenteIntestazione() {
		return contocorrenteIntestazione;
	}

	public void setContocorrenteIntestazione(String contocorrenteIntestazione) {
		this.contocorrenteIntestazione = contocorrenteIntestazione;
	}

	public Date getQuietanzanteNascitaData() {
		return quietanzanteNascitaData;
	}

	public void setQuietanzanteNascitaData(Date quietanzanteNascitaData) {
		this.quietanzanteNascitaData = quietanzanteNascitaData;
	}

	public String getQuietanzianteNascitaLuogo() {
		return quietanzianteNascitaLuogo;
	}

	public void setQuietanzianteNascitaLuogo(String quietanzianteNascitaLuogo) {
		this.quietanzianteNascitaLuogo = quietanzianteNascitaLuogo;
	}

	public String getQuietanzianteNascitaStato() {
		return quietanzianteNascitaStato;
	}

	public void setQuietanzianteNascitaStato(String quietanzianteNascitaStato) {
		this.quietanzianteNascitaStato = quietanzianteNascitaStato;
	}

	public SiacRSoggrelModpagFin addSiacRSoggrelModpag(SiacRSoggrelModpagFin siacRSoggrelModpag) {
		getSiacRSoggrelModpags().add(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacTModpag(this);

		return siacRSoggrelModpag;
	}

	public SiacRSoggrelModpagFin removeSiacRSoggrelModpag(SiacRSoggrelModpagFin siacRSoggrelModpag) {
		getSiacRSoggrelModpags().remove(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacTModpag(null);

		return siacRSoggrelModpag;
	}

	public List<SiacRSoggrelModpagModFin> getSiacRSoggrelModpagMods() {
		return this.siacRSoggrelModpagMods;
	}

	public void setSiacRSoggrelModpagMods(List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods) {
		this.siacRSoggrelModpagMods = siacRSoggrelModpagMods;
	}

	public SiacRSoggrelModpagModFin addSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().add(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacTModpag(this);

		return siacRSoggrelModpagMod;
	}

	public SiacRSoggrelModpagModFin removeSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().remove(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacTModpag(null);

		return siacRSoggrelModpagMod;
	}

	public SiacDAccreditoTipoFin getSiacDAccreditoTipo() {
		return this.siacDAccreditoTipo;
	}

	public void setSiacDAccreditoTipo(SiacDAccreditoTipoFin siacDAccreditoTipo) {
		this.siacDAccreditoTipo = siacDAccreditoTipo;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public List<SiacTModpagModFin> getSiacTModpagMods() {
		return this.siacTModpagMods;
	}

	public void setSiacTModpagMods(List<SiacTModpagModFin> siacTModpagMods) {
		this.siacTModpagMods = siacTModpagMods;
	}

	public SiacTModpagModFin addSiacTModpagMod(SiacTModpagModFin siacTModpagMod) {
		getSiacTModpagMods().add(siacTModpagMod);
		siacTModpagMod.setSiacTModpag(this);

		return siacTModpagMod;
	}

	public SiacTModpagModFin removeSiacTModpagMod(SiacTModpagModFin siacTModpagMod) {
		getSiacTModpagMods().remove(siacTModpagMod);
		siacTModpagMod.setSiacTModpag(null);

		return siacTModpagMod;
	}

	public List<SiacRCartacontDetModpagFin> getSiacRCartacontDetModpags() {
		return this.siacRCartacontDetModpags;
	}

	public void setSiacRCartacontDetModpags(List<SiacRCartacontDetModpagFin> siacRCartacontDetModpags) {
		this.siacRCartacontDetModpags = siacRCartacontDetModpags;
	}

	public SiacRCartacontDetModpagFin addSiacRCartacontDetModpag(SiacRCartacontDetModpagFin siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().add(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTModpag(this);

		return siacRCartacontDetModpag;
	}

	public SiacRCartacontDetModpagFin removeSiacRCartacontDetModpag(SiacRCartacontDetModpagFin siacRCartacontDetModpag) {
		getSiacRCartacontDetModpags().remove(siacRCartacontDetModpag);
		siacRCartacontDetModpag.setSiacTModpag(null);

		return siacRCartacontDetModpag;
	}

	
	public List<SiacRModpagOrdineFin> getSiacRModpagOrdines() {
		return this.siacRModpagOrdines;
	}

	public void setSiacRModpagOrdines(List<SiacRModpagOrdineFin> siacRModpagOrdines) {
		this.siacRModpagOrdines = siacRModpagOrdines;
	}

	public SiacRModpagOrdineFin addSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
		getSiacRModpagOrdines().add(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTModpag(this);

		return siacRModpagOrdine;
	}

	public SiacRModpagOrdineFin removeSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
		getSiacRModpagOrdines().remove(siacRModpagOrdine);
		siacRModpagOrdine.setSiacTModpag(null);

		return siacRModpagOrdine;
	}
	
	
	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return this.modpagId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.modpagId = uid;
	}

	public String getDenominazioneBanca()
	{
		return denominazioneBanca;
	}

	public void setDenominazioneBanca(String denominazioneBanca)
	{
		this.denominazioneBanca = denominazioneBanca;
	}

	public Boolean getPerStipendi()
	{
		return perStipendi;
	}

	public void setPerStipendi(Boolean perStipendi)
	{
		this.perStipendi = perStipendi;
	}
}