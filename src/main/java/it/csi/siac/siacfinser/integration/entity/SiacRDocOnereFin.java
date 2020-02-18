/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_doc_onere database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_onere")
public class SiacRDocOnereFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_onere_id")
	private Integer docOnereId;

	@Column(name="attivita_fine")
	private Timestamp attivitaFine;

	@Column(name="attivita_inizio")
	private Timestamp attivitaInizio;

	@Column(name="importo_carico_ente")
	private BigDecimal importoCaricoEnte;

	@Column(name="importo_carico_soggetto")
	private BigDecimal importoCaricoSoggetto;

	@Column(name="importo_imponibile")
	private BigDecimal importoImponibile;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacDOnereFin
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnereFin siacDOnere;

	//bi-directional many-to-one association to SiacDOnereAttivitaFin
	@ManyToOne
	@JoinColumn(name="onere_att_id")
	private SiacDOnereAttivitaFin siacDOnereAttivita;

	//bi-directional many-to-one association to SiacTDocFin
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDocFin siacTDoc;

//	//bi-directional many-to-one association to SiacTOrdinativoTFin
//	@OneToMany(mappedBy="siacRDocOnere")
//	private List<SiacTOrdinativoTFin> siacTOrdinativoTs;

	public SiacRDocOnereFin() {
	}

	public Integer getDocOnereId() {
		return this.docOnereId;
	}

	public void setDocOnereId(Integer docOnereId) {
		this.docOnereId = docOnereId;
	}

	public Timestamp getAttivitaFine() {
		return this.attivitaFine;
	}

	public void setAttivitaFine(Timestamp attivitaFine) {
		this.attivitaFine = attivitaFine;
	}

	public Timestamp getAttivitaInizio() {
		return this.attivitaInizio;
	}

	public void setAttivitaInizio(Timestamp attivitaInizio) {
		this.attivitaInizio = attivitaInizio;
	}

	public BigDecimal getImportoCaricoEnte() {
		return this.importoCaricoEnte;
	}

	public void setImportoCaricoEnte(BigDecimal importoCaricoEnte) {
		this.importoCaricoEnte = importoCaricoEnte;
	}

	public BigDecimal getImportoCaricoSoggetto() {
		return this.importoCaricoSoggetto;
	}

	public void setImportoCaricoSoggetto(BigDecimal importoCaricoSoggetto) {
		this.importoCaricoSoggetto = importoCaricoSoggetto;
	}

	public BigDecimal getImportoImponibile() {
		return this.importoImponibile;
	}

	public void setImportoImponibile(BigDecimal importoImponibile) {
		this.importoImponibile = importoImponibile;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacDOnereFin getSiacDOnere() {
		return this.siacDOnere;
	}

	public void setSiacDOnere(SiacDOnereFin siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	public SiacDOnereAttivitaFin getSiacDOnereAttivita() {
		return this.siacDOnereAttivita;
	}

	public void setSiacDOnereAttivita(SiacDOnereAttivitaFin siacDOnereAttivita) {
		this.siacDOnereAttivita = siacDOnereAttivita;
	}

	public SiacTDocFin getSiacTDoc() {
		return this.siacTDoc;
	}

	public void setSiacTDoc(SiacTDocFin siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

//	public List<SiacTOrdinativoTFin> getSiacTOrdinativoTs() {
//		return this.siacTOrdinativoTs;
//	}
//
//	public void setSiacTOrdinativoTs(List<SiacTOrdinativoTFin> siacTOrdinativoTs) {
//		this.siacTOrdinativoTs = siacTOrdinativoTs;
//	}

//	public SiacTOrdinativoTFin addSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
//		getSiacTOrdinativoTs().add(siacTOrdinativoT);
//		siacTOrdinativoT.setSiacRDocOnere(this);
//
//		return siacTOrdinativoT;
//	}

//	public SiacTOrdinativoTFin removeSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
//		getSiacTOrdinativoTs().remove(siacTOrdinativoT);
//		siacTOrdinativoT.setSiacRDocOnere(null);
//
//		return siacTOrdinativoT;
//	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docOnereId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docOnereId = uid;
	}

}