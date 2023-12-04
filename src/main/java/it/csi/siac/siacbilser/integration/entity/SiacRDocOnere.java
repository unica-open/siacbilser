/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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
 * The persistent class for the siac_r_doc_onere database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_onere")
@NamedQuery(name="SiacRDocOnere.findAll", query="SELECT s FROM SiacRDocOnere s")
public class SiacRDocOnere extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc onere id. */
	@Id
	@SequenceGenerator(name="SIAC_R_DOC_ONERE_DOCONEREID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_DOC_ONERE_DOC_ONERE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_ONERE_DOCONEREID_GENERATOR")
	@Column(name="doc_onere_id")
	private Integer docOnereId;

	/** The attivita fine. */
	@Column(name="attivita_fine")
	private Date attivitaFine;

	/** The attivita inizio. */
	@Column(name="attivita_inizio")
	private Date attivitaInizio;

	/** The importo carico ente. */
	@Column(name="importo_carico_ente")
	private BigDecimal importoCaricoEnte;

	/** The importo carico soggetto. */
	@Column(name="importo_carico_soggetto")
	private BigDecimal importoCaricoSoggetto;

	/** The importo imponibile. */
	@Column(name="importo_imponibile")
	private BigDecimal importoImponibile;
	
	/** The somma non soggetta. */
	@Column(name="somma_non_soggetta")
	private BigDecimal sommaNonSoggetta;

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacDOnere
	/** The siac d onere. */
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnere siacDOnere;

	//bi-directional many-to-one association to SiacDOnereAttivita
	/** The siac d onere attivita. */
	@ManyToOne
	@JoinColumn(name="onere_att_id")
	private SiacDOnereAttivita siacDOnereAttivita;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id", updatable=false)
	private SiacTDoc siacTDoc;
	
	//bi-directional many-to-one association to SiacTDoc
	@ManyToOne
	@JoinColumn(name="somma_non_soggetta_tipo_id")
	private SiacDSommaNonSoggettaTipo siacDSommaNonSoggettaTipo;

	//bi-directional many-to-one association to SiacRDocOnereOrdinativoT
	@OneToMany(mappedBy="siacRDocOnere")
	private List<SiacRDocOnereOrdinativoT> siacRDocOnereOrdinativoTs;

	//bi-directional many-to-one association to SiacTOrdinativoT
	@OneToMany(mappedBy="siacRDocOnere")
	private List<SiacTOrdinativoT> siacTOrdinativoTs;

	/**
	 * Instantiates a new siac r doc onere.
	 */
	public SiacRDocOnere() {
	}

	/**
	 * Gets the doc onere id.
	 *
	 * @return the doc onere id
	 */
	public Integer getDocOnereId() {
		return this.docOnereId;
	}

	/**
	 * Sets the doc onere id.
	 *
	 * @param docOnereId the new doc onere id
	 */
	public void setDocOnereId(Integer docOnereId) {
		this.docOnereId = docOnereId;
	}

	/**
	 * Gets the attivita fine.
	 *
	 * @return the attivita fine
	 */
	public Date getAttivitaFine() {
		return this.attivitaFine;
	}

	/**
	 * Sets the attivita fine.
	 *
	 * @param attivitaFine the new attivita fine
	 */
	public void setAttivitaFine(Date attivitaFine) {
		this.attivitaFine = attivitaFine;
	}

	/**
	 * Gets the attivita inizio.
	 *
	 * @return the attivita inizio
	 */
	public Date getAttivitaInizio() {
		return this.attivitaInizio;
	}

	/**
	 * Sets the attivita inizio.
	 *
	 * @param attivitaInizio the new attivita inizio
	 */
	public void setAttivitaInizio(Date attivitaInizio) {
		this.attivitaInizio = attivitaInizio;
	}

	/**
	 * Gets the importo carico ente.
	 *
	 * @return the importo carico ente
	 */
	public BigDecimal getImportoCaricoEnte() {
		return this.importoCaricoEnte;
	}

	/**
	 * Sets the importo carico ente.
	 *
	 * @param importoCaricoEnte the new importo carico ente
	 */
	public void setImportoCaricoEnte(BigDecimal importoCaricoEnte) {
		this.importoCaricoEnte = importoCaricoEnte;
	}

	/**
	 * Gets the importo carico soggetto.
	 *
	 * @return the importo carico soggetto
	 */
	public BigDecimal getImportoCaricoSoggetto() {
		return this.importoCaricoSoggetto;
	}

	/**
	 * Sets the importo carico soggetto.
	 *
	 * @param importoCaricoSoggetto the new importo carico soggetto
	 */
	public void setImportoCaricoSoggetto(BigDecimal importoCaricoSoggetto) {
		this.importoCaricoSoggetto = importoCaricoSoggetto;
	}

	/**
	 * Gets the importo imponibile.
	 *
	 * @return the importo imponibile
	 */
	public BigDecimal getImportoImponibile() {
		return this.importoImponibile;
	}

	/**
	 * Sets the importo imponibile.
	 *
	 * @param importoImponibile the new importo imponibile
	 */
	public void setImportoImponibile(BigDecimal importoImponibile) {
		this.importoImponibile = importoImponibile;
	}

	/**
	 * Gets the siac d causale.
	 *
	 * @return the siac d causale
	 */
	public SiacDCausale getSiacDCausale() {
		return this.siacDCausale;
	}

	/**
	 * Sets the siac d causale.
	 *
	 * @param siacDCausale the new siac d causale
	 */
	public void setSiacDCausale(SiacDCausale siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	/**
	 * Gets the siac d onere.
	 *
	 * @return the siac d onere
	 */
	public SiacDOnere getSiacDOnere() {
		return this.siacDOnere;
	}

	/**
	 * Sets the siac d onere.
	 *
	 * @param siacDOnere the new siac d onere
	 */
	public void setSiacDOnere(SiacDOnere siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	/**
	 * Gets the siac d onere attivita.
	 *
	 * @return the siac d onere attivita
	 */
	public SiacDOnereAttivita getSiacDOnereAttivita() {
		return this.siacDOnereAttivita;
	}

	/**
	 * Sets the siac d onere attivita.
	 *
	 * @param siacDOnereAttivita the new siac d onere attivita
	 */
	public void setSiacDOnereAttivita(SiacDOnereAttivita siacDOnereAttivita) {
		this.siacDOnereAttivita = siacDOnereAttivita;
	}

	/**
	 * Gets the siac t doc.
	 *
	 * @return the siac t doc
	 */
	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	/**
	 * Sets the siac t doc.
	 *
	 * @param siacTDoc the new siac t doc
	 */
	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
	}
	
	/**
	 * @return the sommaNonSoggetta
	 */
	public BigDecimal getSommaNonSoggetta() {
		return sommaNonSoggetta;
	}

	/**
	 * @param sommaNonSoggetta the sommaNonSoggetta to set
	 */
	public void setSommaNonSoggetta(BigDecimal sommaNonSoggetta) {
		this.sommaNonSoggetta = sommaNonSoggetta;
	}

	/**
	 * @return the siacDSommaNonSoggettaTipo
	 */
	public SiacDSommaNonSoggettaTipo getSiacDSommaNonSoggettaTipo() {
		return siacDSommaNonSoggettaTipo;
	}

	/**
	 * @param siacDSommaNonSoggettaTipo the siacDSommaNonSoggettaTipo to set
	 */
	public void setSiacDSommaNonSoggettaTipo(
			SiacDSommaNonSoggettaTipo siacDSommaNonSoggettaTipo) {
		this.siacDSommaNonSoggettaTipo = siacDSommaNonSoggettaTipo;
	}

	public List<SiacRDocOnereOrdinativoT> getSiacRDocOnereOrdinativoTs() {
		return this.siacRDocOnereOrdinativoTs;
	}

	public void setSiacRDocOnereOrdinativoTs(List<SiacRDocOnereOrdinativoT> siacRDocOnereOrdinativoTs) {
		this.siacRDocOnereOrdinativoTs = siacRDocOnereOrdinativoTs;
	}

	public SiacRDocOnereOrdinativoT addSiacRDocOnereOrdinativoT(SiacRDocOnereOrdinativoT siacRDocOnereOrdinativoT) {
		getSiacRDocOnereOrdinativoTs().add(siacRDocOnereOrdinativoT);
		siacRDocOnereOrdinativoT.setSiacRDocOnere(this);

		return siacRDocOnereOrdinativoT;
	}

	public SiacRDocOnereOrdinativoT removeSiacRDocOnereOrdinativoT(SiacRDocOnereOrdinativoT siacRDocOnereOrdinativoT) {
		getSiacRDocOnereOrdinativoTs().remove(siacRDocOnereOrdinativoT);
		siacRDocOnereOrdinativoT.setSiacRDocOnere(null);

		return siacRDocOnereOrdinativoT;
	}

	public List<SiacTOrdinativoT> getSiacTOrdinativoTs() {
		return this.siacTOrdinativoTs;
	}

	public void setSiacTOrdinativoTs(List<SiacTOrdinativoT> siacTOrdinativoTs) {
		this.siacTOrdinativoTs = siacTOrdinativoTs;
	}

	public SiacTOrdinativoT addSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
		getSiacTOrdinativoTs().add(siacTOrdinativoT);
		siacTOrdinativoT.setSiacRDocOnere(this);

		return siacTOrdinativoT;
	}

	public SiacTOrdinativoT removeSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
		getSiacTOrdinativoTs().remove(siacTOrdinativoT);
		siacTOrdinativoT.setSiacRDocOnere(null);

		return siacTOrdinativoT;
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docOnereId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docOnereId = uid;		
	}

}