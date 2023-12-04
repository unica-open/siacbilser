/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_predoc_anagr database table.
 * 
 */
@Entity
@Table(name="siac_t_predoc_anagr")
@NamedQuery(name="SiacTPredocAnagr.findAll", query="SELECT s FROM SiacTPredocAnagr s")
public class SiacTPredocAnagr extends SiacTEnteBaseExt {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predocan id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PREDOC_ANAGR_PREDOCANID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PREDOC_ANAGR_PREDOCAN_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PREDOC_ANAGR_PREDOCANID_GENERATOR")
	@Column(name="predocan_id")
	private Integer predocanId;

	/** The predocan codice fiscale. */
	@Column(name="predocan_codice_fiscale")
	private String predocanCodiceFiscale;

	/** The predocan cognome. */
	@Column(name="predocan_cognome")
	private String predocanCognome;

	/** The predocan email. */
	@Column(name="predocan_email")
	private String predocanEmail;

	/** The predocan indirizzo. */
	@Column(name="predocan_indirizzo")
	private String predocanIndirizzo;

	/** The predocan indirizzo comune. */
	@Column(name="predocan_indirizzo_comune")
	private String predocanIndirizzoComune;

	/** The predocan indirizzo nazione. */
	@Column(name="predocan_indirizzo_nazione")
	private String predocanIndirizzoNazione;

	/** The predocan nascita comune. */
	@Column(name="predocan_nascita_comune")
	private String predocanNascitaComune;

	/** The predocan nascita data. */
	@Column(name="predocan_nascita_data")
	private Date predocanNascitaData;

	/** The predocan nascita nazione. */
	@Column(name="predocan_nascita_nazione")
	private String predocanNascitaNazione;

	/** The predocan nome. */
	@Column(name="predocan_nome")
	private String predocanNome;

	/** The predocan partita iva. */
	@Column(name="predocan_partita_iva")
	private String predocanPartitaIva;

	/** The predocan ragione sociale. */
	@Column(name="predocan_ragione_sociale")
	private String predocanRagioneSociale;

	/** The predocan sesso. */
	@Column(name="predocan_sesso")
	private String predocanSesso;

	/** The predocan telefono. */
	@Column(name="predocan_telefono")
	private String predocanTelefono;
	
	/** The predocan intestazioneconto. */
	@Column(name="predocan_intestazioneconto")
	private String predocanIntestazioneconto;
	
	/** The predocan abi. */
	@Column(name="predocan_abi")
	private String predocanAbi;

	/** The predocan cab. */
	@Column(name="predocan_cab")
	private String predocanCab;
	
	/** The predocan contocorrente. */
	@Column(name="predocan_contocorrente")
	private String predocanContocorrente;
	
	/** The predocan iban. */
	@Column(name="predocan_iban")
	private String predocanIban;
	
	/** The predocan bic. */
	@Column(name="predocan_bic")
	private String predocanBic;
	
	/** The predocan quietanziante. */
	@Column(name="predocan_quietanziante")
	private String predocanQuietanziante;
	
	/** The predocan quietanziante cf. */
	@Column(name="predocan_quietanziante_cf")
	private String predocanQuietanzianteCf;


	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	/**
	 * Instantiates a new siac t predoc anagr.
	 */
	public SiacTPredocAnagr() {
	}

	/**
	 * Gets the predocan id.
	 *
	 * @return the predocan id
	 */
	public Integer getPredocanId() {
		return this.predocanId;
	}

	/**
	 * Sets the predocan id.
	 *
	 * @param predocanId the new predocan id
	 */
	public void setPredocanId(Integer predocanId) {
		this.predocanId = predocanId;
	}

	/**
	 * Gets the predocan codice fiscale.
	 *
	 * @return the predocan codice fiscale
	 */
	public String getPredocanCodiceFiscale() {
		return this.predocanCodiceFiscale;
	}

	/**
	 * Sets the predocan codice fiscale.
	 *
	 * @param predocanCodiceFiscale the new predocan codice fiscale
	 */
	public void setPredocanCodiceFiscale(String predocanCodiceFiscale) {
		this.predocanCodiceFiscale = predocanCodiceFiscale;
	}

	/**
	 * Gets the predocan cognome.
	 *
	 * @return the predocan cognome
	 */
	public String getPredocanCognome() {
		return this.predocanCognome;
	}

	/**
	 * Sets the predocan cognome.
	 *
	 * @param predocanCognome the new predocan cognome
	 */
	public void setPredocanCognome(String predocanCognome) {
		this.predocanCognome = predocanCognome;
	}

	/**
	 * Gets the predocan email.
	 *
	 * @return the predocan email
	 */
	public String getPredocanEmail() {
		return this.predocanEmail;
	}

	/**
	 * Sets the predocan email.
	 *
	 * @param predocanEmail the new predocan email
	 */
	public void setPredocanEmail(String predocanEmail) {
		this.predocanEmail = predocanEmail;
	}

	/**
	 * Gets the predocan indirizzo.
	 *
	 * @return the predocan indirizzo
	 */
	public String getPredocanIndirizzo() {
		return this.predocanIndirizzo;
	}

	/**
	 * Sets the predocan indirizzo.
	 *
	 * @param predocanIndirizzo the new predocan indirizzo
	 */
	public void setPredocanIndirizzo(String predocanIndirizzo) {
		this.predocanIndirizzo = predocanIndirizzo;
	}

	/**
	 * Gets the predocan indirizzo comune.
	 *
	 * @return the predocan indirizzo comune
	 */
	public String getPredocanIndirizzoComune() {
		return this.predocanIndirizzoComune;
	}

	/**
	 * Sets the predocan indirizzo comune.
	 *
	 * @param predocanIndirizzoComune the new predocan indirizzo comune
	 */
	public void setPredocanIndirizzoComune(String predocanIndirizzoComune) {
		this.predocanIndirizzoComune = predocanIndirizzoComune;
	}

	/**
	 * Gets the predocan indirizzo nazione.
	 *
	 * @return the predocan indirizzo nazione
	 */
	public String getPredocanIndirizzoNazione() {
		return this.predocanIndirizzoNazione;
	}

	/**
	 * Sets the predocan indirizzo nazione.
	 *
	 * @param predocanIndirizzoNazione the new predocan indirizzo nazione
	 */
	public void setPredocanIndirizzoNazione(String predocanIndirizzoNazione) {
		this.predocanIndirizzoNazione = predocanIndirizzoNazione;
	}

	/**
	 * Gets the predocan nascita comune.
	 *
	 * @return the predocan nascita comune
	 */
	public String getPredocanNascitaComune() {
		return this.predocanNascitaComune;
	}

	/**
	 * Sets the predocan nascita comune.
	 *
	 * @param predocanNascitaComune the new predocan nascita comune
	 */
	public void setPredocanNascitaComune(String predocanNascitaComune) {
		this.predocanNascitaComune = predocanNascitaComune;
	}

	/**
	 * Gets the predocan nascita data.
	 *
	 * @return the predocan nascita data
	 */
	public Date getPredocanNascitaData() {
		return this.predocanNascitaData;
	}

	/**
	 * Sets the predocan nascita data.
	 *
	 * @param predocanNascitaData the new predocan nascita data
	 */
	public void setPredocanNascitaData(Date predocanNascitaData) {
		this.predocanNascitaData = predocanNascitaData;
	}

	/**
	 * Gets the predocan nascita nazione.
	 *
	 * @return the predocan nascita nazione
	 */
	public String getPredocanNascitaNazione() {
		return this.predocanNascitaNazione;
	}

	/**
	 * Sets the predocan nascita nazione.
	 *
	 * @param predocanNascitaNazione the new predocan nascita nazione
	 */
	public void setPredocanNascitaNazione(String predocanNascitaNazione) {
		this.predocanNascitaNazione = predocanNascitaNazione;
	}

	/**
	 * Gets the predocan nome.
	 *
	 * @return the predocan nome
	 */
	public String getPredocanNome() {
		return this.predocanNome;
	}

	/**
	 * Sets the predocan nome.
	 *
	 * @param predocanNome the new predocan nome
	 */
	public void setPredocanNome(String predocanNome) {
		this.predocanNome = predocanNome;
	}

	/**
	 * Gets the predocan partita iva.
	 *
	 * @return the predocan partita iva
	 */
	public String getPredocanPartitaIva() {
		return this.predocanPartitaIva;
	}

	/**
	 * Sets the predocan partita iva.
	 *
	 * @param predocanPartitaIva the new predocan partita iva
	 */
	public void setPredocanPartitaIva(String predocanPartitaIva) {
		this.predocanPartitaIva = predocanPartitaIva;
	}

	/**
	 * Gets the predocan ragione sociale.
	 *
	 * @return the predocan ragione sociale
	 */
	public String getPredocanRagioneSociale() {
		return this.predocanRagioneSociale;
	}

	/**
	 * Sets the predocan ragione sociale.
	 *
	 * @param predocanRagioneSociale the new predocan ragione sociale
	 */
	public void setPredocanRagioneSociale(String predocanRagioneSociale) {
		this.predocanRagioneSociale = predocanRagioneSociale;
	}

	/**
	 * Gets the predocan sesso.
	 *
	 * @return the predocan sesso
	 */
	public String getPredocanSesso() {
		return this.predocanSesso;
	}

	/**
	 * Sets the predocan sesso.
	 *
	 * @param predocanSesso the new predocan sesso
	 */
	public void setPredocanSesso(String predocanSesso) {
		this.predocanSesso = predocanSesso;
	}

	/**
	 * Gets the predocan telefono.
	 *
	 * @return the predocan telefono
	 */
	public String getPredocanTelefono() {
		return this.predocanTelefono;
	}

	/**
	 * Sets the predocan telefono.
	 *
	 * @param predocanTelefono the new predocan telefono
	 */
	public void setPredocanTelefono(String predocanTelefono) {
		this.predocanTelefono = predocanTelefono;
	}
	
	

	/**
	 * Gets the predocan intestazioneconto.
	 *
	 * @return the predocanIntestazioneconto
	 */
	public String getPredocanIntestazioneconto() {
		return predocanIntestazioneconto;
	}

	/**
	 * Sets the predocan intestazioneconto.
	 *
	 * @param predocanIntestazioneconto the predocanIntestazioneconto to set
	 */
	public void setPredocanIntestazioneconto(String predocanIntestazioneconto) {
		this.predocanIntestazioneconto = predocanIntestazioneconto;
	}

	/**
	 * Gets the predocan abi.
	 *
	 * @return the predocanAbi
	 */
	public String getPredocanAbi() {
		return predocanAbi;
	}

	/**
	 * Sets the predocan abi.
	 *
	 * @param predocanAbi the predocanAbi to set
	 */
	public void setPredocanAbi(String predocanAbi) {
		this.predocanAbi = predocanAbi;
	}

	/**
	 * Gets the predocan cab.
	 *
	 * @return the predocanCab
	 */
	public String getPredocanCab() {
		return predocanCab;
	}

	/**
	 * Sets the predocan cab.
	 *
	 * @param predocanCab the predocanCab to set
	 */
	public void setPredocanCab(String predocanCab) {
		this.predocanCab = predocanCab;
	}

	/**
	 * Gets the predocan contocorrente.
	 *
	 * @return the predocanContocorrente
	 */
	public String getPredocanContocorrente() {
		return predocanContocorrente;
	}

	/**
	 * Sets the predocan contocorrente.
	 *
	 * @param predocanContocorrente the predocanContocorrente to set
	 */
	public void setPredocanContocorrente(String predocanContocorrente) {
		this.predocanContocorrente = predocanContocorrente;
	}

	/**
	 * Gets the predocan iban.
	 *
	 * @return the predocanIban
	 */
	public String getPredocanIban() {
		return predocanIban;
	}

	/**
	 * Sets the predocan iban.
	 *
	 * @param predocanIban the predocanIban to set
	 */
	public void setPredocanIban(String predocanIban) {
		this.predocanIban = predocanIban;
	}

	/**
	 * Gets the predocan bic.
	 *
	 * @return the predocanBic
	 */
	public String getPredocanBic() {
		return predocanBic;
	}

	/**
	 * Sets the predocan bic.
	 *
	 * @param predocanBic the predocanBic to set
	 */
	public void setPredocanBic(String predocanBic) {
		this.predocanBic = predocanBic;
	}

	/**
	 * Gets the predocan quietanziante.
	 *
	 * @return the predocanQuietanziante
	 */
	public String getPredocanQuietanziante() {
		return predocanQuietanziante;
	}

	/**
	 * Sets the predocan quietanziante.
	 *
	 * @param predocanQuietanziante the predocanQuietanziante to set
	 */
	public void setPredocanQuietanziante(String predocanQuietanziante) {
		this.predocanQuietanziante = predocanQuietanziante;
	}

	/**
	 * Gets the predocan quietanziante cf.
	 *
	 * @return the predocanQuietanzianteCf
	 */
	public String getPredocanQuietanzianteCf() {
		return predocanQuietanzianteCf;
	}

	/**
	 * Sets the predocan quietanziante cf.
	 *
	 * @param predocanQuietanzianteCf the predocanQuietanzianteCf to set
	 */
	public void setPredocanQuietanzianteCf(String predocanQuietanzianteCf) {
		this.predocanQuietanzianteCf = predocanQuietanzianteCf;
	}

	/**
	 * Gets the siac t predoc.
	 *
	 * @return the siac t predoc
	 */
	public SiacTPredoc getSiacTPredoc() {
		return this.siacTPredoc;
	}

	/**
	 * Sets the siac t predoc.
	 *
	 * @param siacTPredoc the new siac t predoc
	 */
	public void setSiacTPredoc(SiacTPredoc siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocanId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocanId = uid;
	}

}