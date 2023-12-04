/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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


/**
 * The persistent class for the siac_t_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_t_acc_fondi_dubbia_esig")
@NamedQuery(name="SiacTAccFondiDubbiaEsig.findAll", query="SELECT s FROM SiacTAccFondiDubbiaEsig s")
public class SiacTAccFondiDubbiaEsig extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The acc fde id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ACC_FONDI_DUBBIA_ESIG_ACCFDEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ACC_FONDI_DUBBIA_ESIG_ACC_FDE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ACC_FONDI_DUBBIA_ESIG_ACCFDEID_GENERATOR")
	@Column(name="acc_fde_id")
	private Integer accFdeId;
	
	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi")
	private BigDecimal percAccFondi;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_1")
	private BigDecimal percAccFondi1;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_2")
	private BigDecimal percAccFondi2;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_3")
	private BigDecimal percAccFondi3;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_4")
	private BigDecimal percAccFondi4;

	/** The perc acc fondi. */
	@Column(name="perc_delta")
	private BigDecimal percDelta;
	
	// SIAC-7858
	/** The acc fde numeratore. */
	@Column(name="acc_fde_numeratore")
	private BigDecimal accFdeNumeratore;
	/** The acc fde numeratore. */
	@Column(name="acc_fde_numeratore_1")
	private BigDecimal accFdeNumeratore1;
	/** The acc fde numeratore 2. */
	@Column(name="acc_fde_numeratore_2")
	private BigDecimal accFdeNumeratore2;
	/** The acc fde numeratore 3. */
	@Column(name="acc_fde_numeratore_3")
	private BigDecimal accFdeNumeratore3;
	/** The acc fde numeratore 4. */
	@Column(name="acc_fde_numeratore_4")
	private BigDecimal accFdeNumeratore4;

	/** The acc fde denominatore. */
	@Column(name="acc_fde_denominatore")
	private BigDecimal accFdeDenominatore;
	/** The acc fde denominatore. */
	@Column(name="acc_fde_denominatore_1")
	private BigDecimal accFdeDenominatore1;
	/** The acc fde denominatore 2. */
	@Column(name="acc_fde_denominatore_2")
	private BigDecimal accFdeDenominatore2;
	/** The acc fde denominatore 3. */
	@Column(name="acc_fde_denominatore_3")
	private BigDecimal accFdeDenominatore3;
	/** The acc fde denominatore 4. */
	@Column(name="acc_fde_denominatore_4")
	private BigDecimal accFdeDenominatore4;

	/** The acc fde media utente. */
	@Column(name="acc_fde_media_utente")
	private BigDecimal accFdeMediaUtente;
	/** The acc fde media semplice totali. */
	@Column(name="acc_fde_media_semplice_totali")
	private BigDecimal accFdeMediaSempliceTotali;
	/** The acc fde media semplice rapporti. */
	@Column(name="acc_fde_media_semplice_rapporti")
	private BigDecimal accFdeMediaSempliceRapporti;
	/** The acc fde media ponderata totali. */
	@Column(name="acc_fde_media_ponderata_totali")
	private BigDecimal accFdeMediaPonderataTotali;
	/** The acc fde media ponderata rapporti. */
	@Column(name="acc_fde_media_ponderata_rapporti")
	private BigDecimal accFdeMediaPonderataRapporti;
	/** The acc fde media confronto. */
	@Column(name="acc_fde_media_confronto")
	private BigDecimal accFdeMediaConfronto;

	/** The acc fde note. */
	@Column(name="acc_fde_note")
	private String accFdeNote;
	
	//SIAC-8393 e SIAC-8394
	/** The acc fde accantonamento. */
	@Column(name="acc_fde_accantonamento_anno")
	private BigDecimal accFdeAccantonamento;
	/** The acc fde accantonamento 1. */
	@Column(name="acc_fde_accantonamento_anno1")
	private BigDecimal accFdeAccantonamento1;
	/** The acc fde accantonamento 2. */
	@Column(name="acc_fde_accantonamento_anno2")
	private BigDecimal accFdeAccantonamento2;

	/** The acc fde meta numeratore originale. */
	@Column(name="acc_fde_meta_numeratore_originale")
	private BigDecimal accFdeMetaNumeratoreOriginale;
	/** The acc fde meta numeratore 1 originale. */
	@Column(name="acc_fde_meta_numeratore_1_originale")
	private BigDecimal accFdeMetaNumeratore1Originale;
	/** The acc fde meta numeratore 2 originale. */
	@Column(name="acc_fde_meta_numeratore_2_originale")
	private BigDecimal accFdeMetaNumeratore2Originale;
	/** The acc fde meta numeratore 3 originale. */
	@Column(name="acc_fde_meta_numeratore_3_originale")
	private BigDecimal accFdeMetaNumeratore3Originale;
	/** The acc fde meta numeratore 4 originale. */
	@Column(name="acc_fde_meta_numeratore_4_originale")
	private BigDecimal accFdeMetaNumeratore4Originale;
	/** The acc fde meta denominatore originale. */
	@Column(name="acc_fde_meta_denominatore_originale")
	private BigDecimal accFdeMetaDenominatoreOriginale;
	/** The acc fde meta denominatore 1 originale. */
	@Column(name="acc_fde_meta_denominatore_1_originale")
	private BigDecimal accFdeMetaDenominatore1Originale;
	/** The acc fde meta denominatore 2 originale. */
	@Column(name="acc_fde_meta_denominatore_2_originale")
	private BigDecimal accFdeMetaDenominatore2Originale;
	/** The acc fde meta denominatore 3 originale. */
	@Column(name="acc_fde_meta_denominatore_3_originale")
	private BigDecimal accFdeMetaDenominatore3Originale;
	/** The acc fde meta denominatore 4 originale. */
	@Column(name="acc_fde_meta_denominatore_4_originale")
	private BigDecimal accFdeMetaDenominatore4Originale;
	/** The acc fde meta media utente originale. */
	@Column(name="acc_fde_meta_media_utente_originale")
	private BigDecimal accFdeMetaMediaUtenteOriginale;
	/** The acc fde accantonamento. */
	@Column(name="acc_fde_meta_accantonamento_anno_originale")
	private BigDecimal accFdeMetaAccantonamentoOriginale;
	/** The acc fde accantonamento 1. */
	@Column(name="acc_fde_meta_accantonamento_anno1_originale")
	private BigDecimal accFdeMetaAccantonamento1Originale;
	/** The acc fde accantonamento 2. */
	@Column(name="acc_fde_meta_accantonamento_anno2_originale")
	private BigDecimal accFdeMetaAccantonamento2Originale;
	/** The acc fde media tipo id. */
	@ManyToOne
	@JoinColumn(name="afde_tipo_media_id")
	private SiacDAccFondiDubbiaEsigTipoMedia siacDAccFondiDubbiaEsigTipoMedia;
	/** The afde tipo id. */
	@ManyToOne
	@JoinColumn(name="afde_tipo_id")
	private SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo;
	/** The bil elem id. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;
	/** The acc fde media conf id. */
	@ManyToOne
	@JoinColumn(name="afde_tipo_media_conf_id")
	private SiacDAccFondiDubbiaEsigTipoMediaConfronto siacDAccFondiDubbiaEsigTipoMediaConfronto;
	/** The afde bil id. */
	@ManyToOne
	@JoinColumn(name="afde_bil_id")
	private SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil;
	
	
	/**
	 * Instantiates a new siac t bil elem.
	 */
	public SiacTAccFondiDubbiaEsig() {
	}

	/**
	 * Gets the acc fde id.
	 *
	 * @return the accFdeId
	 */
	public Integer getAccFdeId() {
		return accFdeId;
	}

	/**
	 * Sets the acc fde id.
	 *
	 * @param accFdeId the accFdeId to set
	 */
	public void setAccFdeId(Integer accFdeId) {
		this.accFdeId = accFdeId;
	}

	/**
	 * Gets the perc acc fondi.
	 *
	 * @return the percAccFondi
	 */
	public BigDecimal getPercAccFondi() {
		return percAccFondi;
	}

	/**
	 * Sets the perc acc fondi.
	 *
	 * @param percAccFondi the percAccFondi to set
	 */
	public void setPercAccFondi(BigDecimal percAccFondi) {
		this.percAccFondi = percAccFondi;
	}

	/**
	 * Gets the perc acc fondi 1.
	 *
	 * @return the percAccFondi1
	 */
	public BigDecimal getPercAccFondi1() {
		return percAccFondi1;
	}

	/**
	 * Sets the perc acc fondi 1.
	 *
	 * @param percAccFondi1 the percAccFondi1 to set
	 */
	public void setPercAccFondi1(BigDecimal percAccFondi1) {
		this.percAccFondi1 = percAccFondi1;
	}

	/**
	 * Gets the perc acc fondi 2.
	 *
	 * @return the percAccFondi2
	 */
	public BigDecimal getPercAccFondi2() {
		return percAccFondi2;
	}

	/**
	 * Sets the perc acc fondi 2.
	 *
	 * @param percAccFondi2 the percAccFondi2 to set
	 */
	public void setPercAccFondi2(BigDecimal percAccFondi2) {
		this.percAccFondi2 = percAccFondi2;
	}

	/**
	 * Gets the perc acc fondi 3.
	 *
	 * @return the percAccFondi3
	 */
	public BigDecimal getPercAccFondi3() {
		return percAccFondi3;
	}

	/**
	 * Sets the perc acc fondi 3.
	 *
	 * @param percAccFondi3 the percAccFondi3 to set
	 */
	public void setPercAccFondi3(BigDecimal percAccFondi3) {
		this.percAccFondi3 = percAccFondi3;
	}

	/**
	 * Gets the perc acc fondi 4.
	 *
	 * @return the percAccFondi4
	 */
	public BigDecimal getPercAccFondi4() {
		return percAccFondi4;
	}

	/**
	 * Sets the perc acc fondi 4.
	 *
	 * @param percAccFondi4 the percAccFondi4 to set
	 */
	public void setPercAccFondi4(BigDecimal percAccFondi4) {
		this.percAccFondi4 = percAccFondi4;
	}

	/**
	 * Gets the perc delta.
	 *
	 * @return the percDelta
	 */
	public BigDecimal getPercDelta() {
		return percDelta;
	}

	/**
	 * Sets the perc delta.
	 *
	 * @param percDelta the percDelta to set
	 */
	public void setPercDelta(BigDecimal percDelta) {
		this.percDelta = percDelta;
	}

	/**
	 * Gets the acc fde numeratore.
	 *
	 * @return the accFdeNumeratore
	 */
	public BigDecimal getAccFdeNumeratore() {
		return this.accFdeNumeratore;
	}

	/**
	 * Sets the acc fde numeratore.
	 *
	 * @param accFdeNumeratore the accFdeNumeratore to set
	 */
	public void setAccFdeNumeratore(BigDecimal accFdeNumeratore) {
		this.accFdeNumeratore = accFdeNumeratore;
	}

	/**
	 * Gets the acc fde numeratore 1.
	 *
	 * @return the accFdeNumeratore1
	 */
	public BigDecimal getAccFdeNumeratore1() {
		return this.accFdeNumeratore1;
	}

	/**
	 * Sets the acc fde numeratore 1.
	 *
	 * @param accFdeNumeratore1 the accFdeNumeratore1 to set
	 */
	public void setAccFdeNumeratore1(BigDecimal accFdeNumeratore1) {
		this.accFdeNumeratore1 = accFdeNumeratore1;
	}

	/**
	 * Gets the acc fde numeratore 2.
	 *
	 * @return the accFdeNumeratore2
	 */
	public BigDecimal getAccFdeNumeratore2() {
		return this.accFdeNumeratore2;
	}

	/**
	 * Sets the acc fde numeratore 2.
	 *
	 * @param accFdeNumeratore2 the accFdeNumeratore2 to set
	 */
	public void setAccFdeNumeratore2(BigDecimal accFdeNumeratore2) {
		this.accFdeNumeratore2 = accFdeNumeratore2;
	}

	/**
	 * Gets the acc fde numeratore 3.
	 *
	 * @return the accFdeNumeratore3
	 */
	public BigDecimal getAccFdeNumeratore3() {
		return this.accFdeNumeratore3;
	}

	/**
	 * Sets the acc fde numeratore 3.
	 *
	 * @param accFdeNumeratore3 the accFdeNumeratore3 to set
	 */
	public void setAccFdeNumeratore3(BigDecimal accFdeNumeratore3) {
		this.accFdeNumeratore3 = accFdeNumeratore3;
	}

	/**
	 * Gets the acc fde numeratore 4.
	 *
	 * @return the accFdeNumeratore4
	 */
	public BigDecimal getAccFdeNumeratore4() {
		return this.accFdeNumeratore4;
	}

	/**
	 * Sets the acc fde numeratore 4.
	 *
	 * @param accFdeNumeratore4 the accFdeNumeratore4 to set
	 */
	public void setAccFdeNumeratore4(BigDecimal accFdeNumeratore4) {
		this.accFdeNumeratore4 = accFdeNumeratore4;
	}

	/**
	 * Gets the acc fde denominatore.
	 *
	 * @return the accFdeDenominatore
	 */
	public BigDecimal getAccFdeDenominatore() {
		return this.accFdeDenominatore;
	}

	/**
	 * Sets the acc fde denominatore.
	 *
	 * @param accFdeDenominatore the accFdeDenominatore to set
	 */
	public void setAccFdeDenominatore(BigDecimal accFdeDenominatore) {
		this.accFdeDenominatore = accFdeDenominatore;
	}

	/**
	 * Gets the acc fde denominatore 1.
	 *
	 * @return the accFdeDenominatore1
	 */
	public BigDecimal getAccFdeDenominatore1() {
		return this.accFdeDenominatore1;
	}

	/**
	 * Sets the acc fde denominatore 1.
	 *
	 * @param accFdeDenominatore1 the accFdeDenominatore1 to set
	 */
	public void setAccFdeDenominatore1(BigDecimal accFdeDenominatore1) {
		this.accFdeDenominatore1 = accFdeDenominatore1;
	}

	/**
	 * Gets the acc fde denominatore 2.
	 *
	 * @return the accFdeDenominatore2
	 */
	public BigDecimal getAccFdeDenominatore2() {
		return this.accFdeDenominatore2;
	}

	/**
	 * Sets the acc fde denominatore 2.
	 *
	 * @param accFdeDenominatore2 the accFdeDenominatore2 to set
	 */
	public void setAccFdeDenominatore2(BigDecimal accFdeDenominatore2) {
		this.accFdeDenominatore2 = accFdeDenominatore2;
	}

	/**
	 * Gets the acc fde denominatore 3.
	 *
	 * @return the accFdeDenominatore3
	 */
	public BigDecimal getAccFdeDenominatore3() {
		return this.accFdeDenominatore3;
	}

	/**
	 * Sets the acc fde denominatore 3.
	 *
	 * @param accFdeDenominatore3 the accFdeDenominatore3 to set
	 */
	public void setAccFdeDenominatore3(BigDecimal accFdeDenominatore3) {
		this.accFdeDenominatore3 = accFdeDenominatore3;
	}

	/**
	 * Gets the acc fde denominatore 4.
	 *
	 * @return the accFdeDenominatore4
	 */
	public BigDecimal getAccFdeDenominatore4() {
		return this.accFdeDenominatore4;
	}

	/**
	 * Sets the acc fde denominatore 4.
	 *
	 * @param accFdeDenominatore4 the accFdeDenominatore4 to set
	 */
	public void setAccFdeDenominatore4(BigDecimal accFdeDenominatore4) {
		this.accFdeDenominatore4 = accFdeDenominatore4;
	}

	/**
	 * Gets the acc fde media utente.
	 *
	 * @return the accFdeMediaUtente
	 */
	public BigDecimal getAccFdeMediaUtente() {
		return this.accFdeMediaUtente;
	}

	/**
	 * Sets the acc fde media utente.
	 *
	 * @param accFdeMediaUtente the accFdeMediaUtente to set
	 */
	public void setAccFdeMediaUtente(BigDecimal accFdeMediaUtente) {
		this.accFdeMediaUtente = accFdeMediaUtente;
	}

	/**
	 * Gets the acc fde media semplice totali.
	 *
	 * @return the accFdeMediaSempliceTotali
	 */
	public BigDecimal getAccFdeMediaSempliceTotali() {
		return this.accFdeMediaSempliceTotali;
	}

	/**
	 * Sets the acc fde media semplice totali.
	 *
	 * @param accFdeMediaSempliceTotali the accFdeMediaSempliceTotali to set
	 */
	public void setAccFdeMediaSempliceTotali(BigDecimal accFdeMediaSempliceTotali) {
		this.accFdeMediaSempliceTotali = accFdeMediaSempliceTotali;
	}

	/**
	 * Gets the acc fde media semplice rapporti.
	 *
	 * @return the accFdeMediaSempliceRapporti
	 */
	public BigDecimal getAccFdeMediaSempliceRapporti() {
		return this.accFdeMediaSempliceRapporti;
	}

	/**
	 * Sets the acc fde media semplice rapporti.
	 *
	 * @param accFdeMediaSempliceRapporti the accFdeMediaSempliceRapporti to set
	 */
	public void setAccFdeMediaSempliceRapporti(BigDecimal accFdeMediaSempliceRapporti) {
		this.accFdeMediaSempliceRapporti = accFdeMediaSempliceRapporti;
	}

	/**
	 * Gets the acc fde media ponderata totali.
	 *
	 * @return the accFdeMediaPonderataTotali
	 */
	public BigDecimal getAccFdeMediaPonderataTotali() {
		return this.accFdeMediaPonderataTotali;
	}

	/**
	 * Sets the acc fde media ponderata totali.
	 *
	 * @param accFdeMediaPonderataTotali the accFdeMediaPonderataTotali to set
	 */
	public void setAccFdeMediaPonderataTotali(BigDecimal accFdeMediaPonderataTotali) {
		this.accFdeMediaPonderataTotali = accFdeMediaPonderataTotali;
	}

	/**
	 * Gets the acc fde media ponderata rapporti.
	 *
	 * @return the accFdeMediaPonderataRapporti
	 */
	public BigDecimal getAccFdeMediaPonderataRapporti() {
		return this.accFdeMediaPonderataRapporti;
	}

	/**
	 * Sets the acc fde media ponderata rapporti.
	 *
	 * @param accFdeMediaPonderataRapporti the accFdeMediaPonderataRapporti to set
	 */
	public void setAccFdeMediaPonderataRapporti(BigDecimal accFdeMediaPonderataRapporti) {
		this.accFdeMediaPonderataRapporti = accFdeMediaPonderataRapporti;
	}

	/**
	 * @return the accFdeMediaConfronto
	 */
	public BigDecimal getAccFdeMediaConfronto() {
		return this.accFdeMediaConfronto;
	}

	/**
	 * @param accFdeMediaConfronto the accFdeMediaConfronto to set
	 */
	public void setAccFdeMediaConfronto(BigDecimal accFdeMediaConfronto) {
		this.accFdeMediaConfronto = accFdeMediaConfronto;
	}

	/**
	 * @return the accFdeNote
	 */
	public String getAccFdeNote() {
		return this.accFdeNote;
	}

	/**
	 * @param accFdeNote the accFdeNote to set
	 */
	public void setAccFdeNote(String accFdeNote) {
		this.accFdeNote = accFdeNote;
	}

	/**
	 * @return the accFdeAccantonamento
	 */
	public BigDecimal getAccFdeAccantonamento() {
		return accFdeAccantonamento;
	}

	/**
	 * @param accFdeAccantonamento the accFdeAccantonamento to set
	 */
	public void setAccFdeAccantonamento(BigDecimal accFdeAccantonamento) {
		this.accFdeAccantonamento = accFdeAccantonamento;
	}

	/**
	 * @return the accFdeAccantonamento1
	 */
	public BigDecimal getAccFdeAccantonamento1() {
		return accFdeAccantonamento1;
	}

	/**
	 * @param accFdeAccantonamento1 the accFdeAccantonamento1 to set
	 */
	public void setAccFdeAccantonamento1(BigDecimal accFdeAccantonamento1) {
		this.accFdeAccantonamento1 = accFdeAccantonamento1;
	}

	/**
	 * @return the accFdeAccantonamento2
	 */
	public BigDecimal getAccFdeAccantonamento2() {
		return accFdeAccantonamento2;
	}

	/**
	 * @param accFdeAccantonamento2 the accFdeAccantonamento2 to set
	 */
	public void setAccFdeAccantonamento2(BigDecimal accFdeAccantonamento2) {
		this.accFdeAccantonamento2 = accFdeAccantonamento2;
	}

	/**
	 * Gets the acc fde meta numeratore originale.
	 *
	 * @return the accFdeMetaNumeratoreOriginale
	 */
	public BigDecimal getAccFdeMetaNumeratoreOriginale() {
		return this.accFdeMetaNumeratoreOriginale;
	}

	/**
	 * Sets the acc fde meta numeratore originale.
	 *
	 * @param accFdeMetaNumeratoreOriginale the accFdeMetaNumeratoreOriginale to set
	 */
	public void setAccFdeMetaNumeratoreOriginale(BigDecimal accFdeMetaNumeratoreOriginale) {
		this.accFdeMetaNumeratoreOriginale = accFdeMetaNumeratoreOriginale;
	}

	/**
	 * Gets the acc fde meta numeratore 1 originale.
	 *
	 * @return the accFdeMetaNumeratore1Originale
	 */
	public BigDecimal getAccFdeMetaNumeratore1Originale() {
		return this.accFdeMetaNumeratore1Originale;
	}

	/**
	 * Sets the acc fde meta numeratore 1 originale.
	 *
	 * @param accFdeMetaNumeratore1Originale the accFdeMetaNumeratore1Originale to set
	 */
	public void setAccFdeMetaNumeratore1Originale(BigDecimal accFdeMetaNumeratore1Originale) {
		this.accFdeMetaNumeratore1Originale = accFdeMetaNumeratore1Originale;
	}

	/**
	 * Gets the acc fde meta numeratore 2 originale.
	 *
	 * @return the accFdeMetaNumeratore2Originale
	 */
	public BigDecimal getAccFdeMetaNumeratore2Originale() {
		return this.accFdeMetaNumeratore2Originale;
	}

	/**
	 * Sets the acc fde meta numeratore 2 originale.
	 *
	 * @param accFdeMetaNumeratore2Originale the accFdeMetaNumeratore2Originale to set
	 */
	public void setAccFdeMetaNumeratore2Originale(BigDecimal accFdeMetaNumeratore2Originale) {
		this.accFdeMetaNumeratore2Originale = accFdeMetaNumeratore2Originale;
	}

	/**
	 * Gets the acc fde meta numeratore 3 originale.
	 *
	 * @return the accFdeMetaNumeratore3Originale
	 */
	public BigDecimal getAccFdeMetaNumeratore3Originale() {
		return this.accFdeMetaNumeratore3Originale;
	}

	/**
	 * Sets the acc fde meta numeratore 3 originale.
	 *
	 * @param accFdeMetaNumeratore3Originale the accFdeMetaNumeratore3Originale to set
	 */
	public void setAccFdeMetaNumeratore3Originale(BigDecimal accFdeMetaNumeratore3Originale) {
		this.accFdeMetaNumeratore3Originale = accFdeMetaNumeratore3Originale;
	}

	/**
	 * Gets the acc fde meta numeratore 4 originale.
	 *
	 * @return the accFdeMetaNumeratore4Originale
	 */
	public BigDecimal getAccFdeMetaNumeratore4Originale() {
		return this.accFdeMetaNumeratore4Originale;
	}

	/**
	 * Sets the acc fde meta numeratore 4 originale.
	 *
	 * @param accFdeMetaNumeratore4Originale the accFdeMetaNumeratore4Originale to set
	 */
	public void setAccFdeMetaNumeratore4Originale(BigDecimal accFdeMetaNumeratore4Originale) {
		this.accFdeMetaNumeratore4Originale = accFdeMetaNumeratore4Originale;
	}

	/**
	 * Gets the acc fde meta denominatore originale.
	 *
	 * @return the accFdeMetaDenominatoreOriginale
	 */
	public BigDecimal getAccFdeMetaDenominatoreOriginale() {
		return this.accFdeMetaDenominatoreOriginale;
	}

	/**
	 * Sets the acc fde meta denominatore originale.
	 *
	 * @param accFdeMetaDenominatoreOriginale the accFdeMetaDenominatoreOriginale to set
	 */
	public void setAccFdeMetaDenominatoreOriginale(BigDecimal accFdeMetaDenominatoreOriginale) {
		this.accFdeMetaDenominatoreOriginale = accFdeMetaDenominatoreOriginale;
	}

	/**
	 * Gets the acc fde meta denominatore 1 originale.
	 *
	 * @return the accFdeMetaDenominatore1Originale
	 */
	public BigDecimal getAccFdeMetaDenominatore1Originale() {
		return this.accFdeMetaDenominatore1Originale;
	}

	/**
	 * Sets the acc fde meta denominatore 1 originale.
	 *
	 * @param accFdeMetaDenominatore1Originale the accFdeMetaDenominatore1Originale to set
	 */
	public void setAccFdeMetaDenominatore1Originale(BigDecimal accFdeMetaDenominatore1Originale) {
		this.accFdeMetaDenominatore1Originale = accFdeMetaDenominatore1Originale;
	}

	/**
	 * Gets the acc fde meta denominatore 2 originale.
	 *
	 * @return the accFdeMetaDenominatore2Originale
	 */
	public BigDecimal getAccFdeMetaDenominatore2Originale() {
		return this.accFdeMetaDenominatore2Originale;
	}

	/**
	 * Sets the acc fde meta denominatore 2 originale.
	 *
	 * @param accFdeMetaDenominatore2Originale the accFdeMetaDenominatore2Originale to set
	 */
	public void setAccFdeMetaDenominatore2Originale(BigDecimal accFdeMetaDenominatore2Originale) {
		this.accFdeMetaDenominatore2Originale = accFdeMetaDenominatore2Originale;
	}

	/**
	 * Gets the acc fde meta denominatore 3 originale.
	 *
	 * @return the accFdeMetaDenominatore3Originale
	 */
	public BigDecimal getAccFdeMetaDenominatore3Originale() {
		return this.accFdeMetaDenominatore3Originale;
	}

	/**
	 * Sets the acc fde meta denominatore 3 originale.
	 *
	 * @param accFdeMetaDenominatore3Originale the accFdeMetaDenominatore3Originale to set
	 */
	public void setAccFdeMetaDenominatore3Originale(BigDecimal accFdeMetaDenominatore3Originale) {
		this.accFdeMetaDenominatore3Originale = accFdeMetaDenominatore3Originale;
	}

	/**
	 * Gets the acc fde meta denominatore 4 originale.
	 *
	 * @return the accFdeMetaDenominatore4Originale
	 */
	public BigDecimal getAccFdeMetaDenominatore4Originale() {
		return this.accFdeMetaDenominatore4Originale;
	}

	/**
	 * Sets the acc fde meta denominatore 4 originale.
	 *
	 * @param accFdeMetaDenominatore4Originale the accFdeMetaDenominatore4Originale to set
	 */
	public void setAccFdeMetaDenominatore4Originale(BigDecimal accFdeMetaDenominatore4Originale) {
		this.accFdeMetaDenominatore4Originale = accFdeMetaDenominatore4Originale;
	}

	/**
	 * Gets the acc fde meta media utente originale.
	 *
	 * @return the accFdeMetaMediaUtenteOriginale
	 */
	public BigDecimal getAccFdeMetaMediaUtenteOriginale() {
		return this.accFdeMetaMediaUtenteOriginale;
	}

	/**
	 * Sets the acc fde meta media utente originale.
	 *
	 * @param accFdeMetaMediaUtenteOriginale the accFdeMetaMediaUtenteOriginale to set
	 */
	public void setAccFdeMetaMediaUtenteOriginale(BigDecimal accFdeMetaMediaUtenteOriginale) {
		this.accFdeMetaMediaUtenteOriginale = accFdeMetaMediaUtenteOriginale;
	}

	/**
	 * @return the accFdeMetaAccantonamentoOriginale
	 */
	public BigDecimal getAccFdeMetaAccantonamentoOriginale() {
		return accFdeMetaAccantonamentoOriginale;
	}

	/**
	 * @param accFdeMetaAccantonamentoOriginale the accFdeMetaAccantonamentoOriginale to set
	 */
	public void setAccFdeMetaAccantonamentoOriginale(BigDecimal accFdeMetaAccantonamentoOriginale) {
		this.accFdeMetaAccantonamentoOriginale = accFdeMetaAccantonamentoOriginale;
	}

	/**
	 * @return the accFdeMetaAccantonamento1Originale
	 */
	public BigDecimal getAccFdeMetaAccantonamento1Originale() {
		return accFdeMetaAccantonamento1Originale;
	}

	/**
	 * @param accFdeMetaAccantonamento1Originale the accFdeMetaAccantonamento1Originale to set
	 */
	public void setAccFdeMetaAccantonamento1Originale(BigDecimal accFdeMetaAccantonamento1Originale) {
		this.accFdeMetaAccantonamento1Originale = accFdeMetaAccantonamento1Originale;
	}

	/**
	 * @return the accFdeMetaAccantonamento2Originale
	 */
	public BigDecimal getAccFdeMetaAccantonamento2Originale() {
		return accFdeMetaAccantonamento2Originale;
	}

	/**
	 * @param accFdeMetaAccantonamento2Originale the accFdeMetaAccantonamento2Originale to set
	 */
	public void setAccFdeMetaAccantonamento2Originale(BigDecimal accFdeMetaAccantonamento2Originale) {
		this.accFdeMetaAccantonamento2Originale = accFdeMetaAccantonamento2Originale;
	}

	/**
	 * Gets the siac D acc fondi dubbia esig tipo media.
	 *
	 * @return the siacDAccFondiDubbiaEsigTipoMedia
	 */
	public SiacDAccFondiDubbiaEsigTipoMedia getSiacDAccFondiDubbiaEsigTipoMedia() {
		return this.siacDAccFondiDubbiaEsigTipoMedia;
	}

	/**
	 * Sets the siac D acc fondi dubbia esig tipo media.
	 *
	 * @param siacDAccFondiDubbiaEsigTipoMedia the siacDAccFondiDubbiaEsigTipoMedia to set
	 */
	public void setSiacDAccFondiDubbiaEsigTipoMedia(SiacDAccFondiDubbiaEsigTipoMedia siacDAccFondiDubbiaEsigTipoMedia) {
		this.siacDAccFondiDubbiaEsigTipoMedia = siacDAccFondiDubbiaEsigTipoMedia;
	}

	/**
	 * Gets the siac D acc fondi dubbia esig tipo.
	 *
	 * @return the siacDAccFondiDubbiaEsigTipo
	 */
	public SiacDAccFondiDubbiaEsigTipo getSiacDAccFondiDubbiaEsigTipo() {
		return this.siacDAccFondiDubbiaEsigTipo;
	}

	/**
	 * Sets the siac D acc fondi dubbia esig tipo.
	 *
	 * @param siacDAccFondiDubbiaEsigTipo the siacDAccFondiDubbiaEsigTipo to set
	 */
	public void setSiacDAccFondiDubbiaEsigTipo(SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo) {
		this.siacDAccFondiDubbiaEsigTipo = siacDAccFondiDubbiaEsigTipo;
	}

	/**
	 * Gets the siac T bil elem.
	 *
	 * @return the siacTBilElem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac T bil elem.
	 *
	 * @param siacTBilElem the siacTBilElem to set
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/**
	 * @return the siacDAccFondiDubbiaEsigTipoMediaConfronto
	 */
	public SiacDAccFondiDubbiaEsigTipoMediaConfronto getSiacDAccFondiDubbiaEsigTipoMediaConfronto() {
		return this.siacDAccFondiDubbiaEsigTipoMediaConfronto;
	}

	/**
	 * @param siacDAccFondiDubbiaEsigTipoMediaConfronto the siacDAccFondiDubbiaEsigTipoMediaConfronto to set
	 */
	public void setSiacDAccFondiDubbiaEsigTipoMediaConfronto(SiacDAccFondiDubbiaEsigTipoMediaConfronto siacDAccFondiDubbiaEsigTipoMediaConfronto) {
		this.siacDAccFondiDubbiaEsigTipoMediaConfronto = siacDAccFondiDubbiaEsigTipoMediaConfronto;
	}

	/**
	 * @return the siacTAccFondiDubbiaEsigBil
	 */
	public SiacTAccFondiDubbiaEsigBil getSiacTAccFondiDubbiaEsigBil() {
		return this.siacTAccFondiDubbiaEsigBil;
	}

	/**
	 * @param siacTAccFondiDubbiaEsigBil the siacTAccFondiDubbiaEsigBil to set
	 */
	public void setSiacTAccFondiDubbiaEsigBil(SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil) {
		this.siacTAccFondiDubbiaEsigBil = siacTAccFondiDubbiaEsigBil;
	}

	/**
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	@Override
	public Integer getUid() {
		return accFdeId;
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	@Override
	public void setUid(Integer uid) {
		this.accFdeId = uid;
		
	}

}