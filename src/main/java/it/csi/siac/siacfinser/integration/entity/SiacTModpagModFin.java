/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;
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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_modpag_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_modpag_mod")
public class SiacTModpagModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MODPAG_MOD_MODPAG_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_modpag_mod_modpag_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODPAG_MOD_MODPAG_MOD_ID_GENERATOR")
	@Column(name="modpag_mod_id")
	private Integer modpagModId;

	private String bic;

	private String contocorrente;

//	@Temporal(TemporalType.DATE)
//	@Column(name="data_scadenza")
//	private Date dataScadenza;

	private String iban;

	@Column(name="banca_denominazione")
	private String denominazioneBanca;

	private String note;

	private String quietanziante;

	// nuovi campi
	@Column(name="contocorrente_intestazione")
	private String contocorrenteIntestazione;
	
	@Column(name="quietanziante_nascita_data")
	private Timestamp quietanzanteNascitaData;
	
	
	@Column(name="quietanziante_nascita_luogo")
	private String quietanzianteNascitaLuogo;
	
	
	@Column(name="quietanziante_nascita_stato")
	private String quietanzianteNascitaStato;
	
	// fine nuovi campi
	
	@Column(name="quietanziante_codice_fiscale")
	private String quietanzianteCodiceFiscale;

	//bi-directional many-to-one association to SiacDAccreditoTipoFin
	@ManyToOne
	@JoinColumn(name="accredito_tipo_id")
	private SiacDAccreditoTipoFin siacDAccreditoTipo;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;

//	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;
	
	
	// aggancio il numeratore delle mdp
	//bi-directional many-to-one association to SiacRModpagOrdineFin
	@OneToMany(mappedBy="siacTModpag")
	private List<SiacRModpagOrdineFin> siacRModpagOrdines;

	
	@Column(name="per_stipendi")
	private Boolean perStipendi;


	public SiacTModpagModFin() {
	}

	public Integer getModpagModId() {
		return this.modpagModId;
	}

	public void setModpagModId(Integer modpagModId) {
		this.modpagModId = modpagModId;
	}

	public String getDenominazioneBanca()
	{
		return denominazioneBanca;
	}

	public void setDenominazioneBanca(String denominazioneBanca)
	{
		this.denominazioneBanca = denominazioneBanca;
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

	public SiacDAccreditoTipoFin getSiacDAccreditoTipo() {
		return this.siacDAccreditoTipo;
	}

	public void setSiacDAccreditoTipo(SiacDAccreditoTipoFin siacDAccreditoTipo) {
		this.siacDAccreditoTipo = siacDAccreditoTipo;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return this.modpagModId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.modpagModId = uid;
	}

	public String getContocorrenteIntestazione() {
		return contocorrenteIntestazione;
	}

	public void setContocorrenteIntestazione(String contocorrenteIntestazione) {
		this.contocorrenteIntestazione = contocorrenteIntestazione;
	}

	public Timestamp getQuietanzanteNascitaData() {
		return quietanzanteNascitaData;
	}

	public void setQuietanzanteNascitaData(Timestamp quietanzanteNascitaData) {
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
	
	public List<SiacRModpagOrdineFin> getSiacRModpagOrdines() {
		return this.siacRModpagOrdines;
	}

	public void setSiacRModpagOrdines(List<SiacRModpagOrdineFin> siacRModpagOrdines) {
		this.siacRModpagOrdines = siacRModpagOrdines;
	}

	public Boolean getPerStipendi()
	{
		return perStipendi;
	}

	public void setPerStipendi(Boolean perStipendi)
	{
		this.perStipendi = perStipendi;
	}

//	public SiacRModpagOrdineFin addSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
//		getSiacRModpagOrdines().add(siacRModpagOrdine);
//		siacRModpagOrdine.setSiacTModpag(this);
//
//		return siacRModpagOrdine;
//	}
//
//	public SiacRModpagOrdineFin removeSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
//		getSiacRModpagOrdines().remove(siacRModpagOrdine);
//		siacRModpagOrdine.setSiacTModpag(null);
//
//		return siacRModpagOrdine;
//	}
}