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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_modpag_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_modpag_mod")
@NamedQuery(name="SiacTModpagMod.findAll", query="SELECT s FROM SiacTModpagMod s")
public class SiacTModpagMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The modpag mod id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MODPAG_MOD_MODPAGMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MODPAG_MOD_MODPAG_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODPAG_MOD_MODPAGMODID_GENERATOR")
	@Column(name="modpag_mod_id")
	private Integer modpagModId;

	/** The bic. */
	private String bic;

	/** The contocorrente. */
	private String contocorrente;

	/** The data scadenza. */
	@Temporal(TemporalType.DATE)
	@Column(name="data_scadenza")
	private Date dataScadenza;

	/** The iban. */
	private String iban;

	/** The note. */
	private String note;

	/** The quietanziante. */
	private String quietanziante;

	/** The quietanziante codice fiscale. */
	@Column(name="quietanziante_codice_fiscale")
	private String quietanzianteCodiceFiscale;
	
	@Column(name="banca_denominazione")
	private String bancaDenominazione;

	/** The sog mod id. */
	@Column(name="sog_mod_id")
	private Integer sogModId;

	//bi-directional many-to-one association to SiacDAccreditoTipo
	/** The siac d accredito tipo. */
	@ManyToOne
	@JoinColumn(name="accredito_tipo_id")
	private SiacDAccreditoTipo siacDAccreditoTipo;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac t modpag mod.
	 */
	public SiacTModpagMod() {
	}

	/**
	 * Gets the modpag mod id.
	 *
	 * @return the modpag mod id
	 */
	public Integer getModpagModId() {
		return this.modpagModId;
	}

	/**
	 * Sets the modpag mod id.
	 *
	 * @param modpagModId the new modpag mod id
	 */
	public void setModpagModId(Integer modpagModId) {
		this.modpagModId = modpagModId;
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

	/**
	 * @return the bancaDenominazione
	 */
	public String getBancaDenominazione() {
		return bancaDenominazione;
	}

	/**
	 * @param bancaDenominazione the bancaDenominazione to set
	 */
	public void setBancaDenominazione(String bancaDenominazione) {
		this.bancaDenominazione = bancaDenominazione;
	}

	/**
	 * Gets the sog mod id.
	 *
	 * @return the sog mod id
	 */
	public Integer getSogModId() {
		return this.sogModId;
	}

	/**
	 * Sets the sog mod id.
	 *
	 * @param sogModId the new sog mod id
	 */
	public void setSogModId(Integer sogModId) {
		this.sogModId = sogModId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modpagModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modpagModId = uid;
	}

}