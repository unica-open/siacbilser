/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pagopa_t_riconciliazione_det")
public class PagopaTRiconciliazioneDet extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAGOPA_T_RICONCILIAZIONE_DETPAGOPA_RIC_DET_ID_GENERATOR", allocationSize = 1, sequenceName = "pagopa_t_riconciliazione_det_pagopa_ric_det_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOPA_T_RICONCILIAZIONE_DETPAGOPA_RIC_DET_ID_GENERATOR")
	@Column(name = "pagopa_ric_det_id")
	private Integer pagopaRicDetId;

	@ManyToOne
	@JoinColumn(name = "pagopa_ric_id")
	private PagopaTRiconciliazione pagopaTRiconciliazione;

	@Column(name = "pagopa_det_anag_cognome")
	private String pagopaDetAnagCognome;

	@Column(name = "pagopa_det_anag_nome")
	private String pagopaDetAnagNome;

	@Column(name = "pagopa_det_anag_ragione_sociale")
	private String pagopaDetAnagRagioneSociale;

	@Column(name = "pagopa_det_anag_codice_fiscale")
	private String pagopaDetAnagCodiceFiscale;

	@Column(name = "pagopa_det_anag_indirizzo")
	private String pagopaDetAnagIndirizzo;

	@Column(name = "pagopa_det_anag_civico")
	private String pagopaDetAnagCivico;

	@Column(name = "pagopa_det_anag_cap")
	private String pagopaDetAnagCap;

	@Column(name = "pagopa_det_anag_localita")
	private String pagopaDetAnagLocalita;

	@Column(name = "pagopa_det_anag_provincia")
	private String pagopaDetAnagProvincia;

	@Column(name = "pagopa_det_anag_nazione")
	private String pagopaDetAnagNazione;

	@Column(name = "pagopa_det_anag_email")
	private String pagopaDetAnagEmail;

	@Column(name = "pagopa_det_causale_versamento_desc")
	private String pagopaDetCausaleVersamentoDesc;

	@Column(name = "pagopa_det_causale")
	private String pagopaDetCausale;

	@Column(name = "pagopa_det_data_pagamento")
	private Date pagopaDetDataPagamento;

	@Column(name = "pagopa_det_esito_pagamento")
	private String pagopaDetEsitoPagamento;

	@Column(name = "pagopa_det_importo_versamento")
	private BigDecimal pagopaDetImportoVersamento;

	@Column(name = "pagopa_det_indice_versamento")
	private Integer pagopaDetIndiceVersamento;

	@Column(name = "pagopa_det_transaction_id")
	private String pagopaDetTransactionId;

	@Column(name = "pagopa_det_versamento_id")
	private String pagopaDetVersamentoId;

	@Column(name = "pagopa_det_riscossione_id")
	private String pagopaDetRiscossioneId;

	public PagopaTRiconciliazioneDet() {
	}

	@Override
	public Integer getUid() {
		return pagopaRicDetId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaRicDetId = uid;
	}

	public Integer getPagopaRicDetId() {
		return pagopaRicDetId;
	}

	public void setPagopaRicDetId(Integer pagopaRicDetId) {
		this.pagopaRicDetId = pagopaRicDetId;
	}

	public PagopaTRiconciliazione getPagopaTRiconciliazione() {
		return pagopaTRiconciliazione;
	}

	public void setPagopaTRiconciliazione(PagopaTRiconciliazione pagopaTRiconciliazione) {
		this.pagopaTRiconciliazione = pagopaTRiconciliazione;
	}

	public String getPagopaDetAnagCognome() {
		return pagopaDetAnagCognome;
	}

	public void setPagopaDetAnagCognome(String pagopaDetAnagCognome) {
		this.pagopaDetAnagCognome = pagopaDetAnagCognome;
	}

	public String getPagopaDetAnagNome() {
		return pagopaDetAnagNome;
	}

	public void setPagopaDetAnagNome(String pagopaDetAnagNome) {
		this.pagopaDetAnagNome = pagopaDetAnagNome;
	}

	public String getPagopaDetAnagRagioneSociale() {
		return pagopaDetAnagRagioneSociale;
	}

	public void setPagopaDetAnagRagioneSociale(String pagopaDetAnagRagioneSociale) {
		this.pagopaDetAnagRagioneSociale = pagopaDetAnagRagioneSociale;
	}

	public String getPagopaDetAnagCodiceFiscale() {
		return pagopaDetAnagCodiceFiscale;
	}

	public void setPagopaDetAnagCodiceFiscale(String pagopaDetAnagCodiceFiscale) {
		this.pagopaDetAnagCodiceFiscale = pagopaDetAnagCodiceFiscale;
	}

	public String getPagopaDetAnagIndirizzo() {
		return pagopaDetAnagIndirizzo;
	}

	public void setPagopaDetAnagIndirizzo(String pagopaDetAnagIndirizzo) {
		this.pagopaDetAnagIndirizzo = pagopaDetAnagIndirizzo;
	}

	public String getPagopaDetAnagCivico() {
		return pagopaDetAnagCivico;
	}

	public void setPagopaDetAnagCivico(String pagopaDetAnagCivico) {
		this.pagopaDetAnagCivico = pagopaDetAnagCivico;
	}

	public String getPagopaDetAnagCap() {
		return pagopaDetAnagCap;
	}

	public void setPagopaDetAnagCap(String pagopaDetAnagCap) {
		this.pagopaDetAnagCap = pagopaDetAnagCap;
	}

	public String getPagopaDetAnagLocalita() {
		return pagopaDetAnagLocalita;
	}

	public void setPagopaDetAnagLocalita(String pagopaDetAnagLocalita) {
		this.pagopaDetAnagLocalita = pagopaDetAnagLocalita;
	}

	public String getPagopaDetAnagProvincia() {
		return pagopaDetAnagProvincia;
	}

	public void setPagopaDetAnagProvincia(String pagopaDetAnagProvincia) {
		this.pagopaDetAnagProvincia = pagopaDetAnagProvincia;
	}

	public String getPagopaDetAnagNazione() {
		return pagopaDetAnagNazione;
	}

	public void setPagopaDetAnagNazione(String pagopaDetAnagNazione) {
		this.pagopaDetAnagNazione = pagopaDetAnagNazione;
	}

	public String getPagopaDetAnagEmail() {
		return pagopaDetAnagEmail;
	}

	public void setPagopaDetAnagEmail(String pagopaDetAnagEmail) {
		this.pagopaDetAnagEmail = pagopaDetAnagEmail;
	}

	public String getPagopaDetCausaleVersamentoDesc() {
		return pagopaDetCausaleVersamentoDesc;
	}

	public void setPagopaDetCausaleVersamentoDesc(String pagopaDetCausaleVersamentoDesc) {
		this.pagopaDetCausaleVersamentoDesc = pagopaDetCausaleVersamentoDesc;
	}

	public String getPagopaDetCausale() {
		return pagopaDetCausale;
	}

	public void setPagopaDetCausale(String pagopaDetCausale) {
		this.pagopaDetCausale = pagopaDetCausale;
	}

	public Date getPagopaDetDataPagamento() {
		return pagopaDetDataPagamento;
	}

	public void setPagopaDetDataPagamento(Date pagopaDetDataPagamento) {
		this.pagopaDetDataPagamento = pagopaDetDataPagamento;
	}

	public String getPagopaDetEsitoPagamento() {
		return pagopaDetEsitoPagamento;
	}

	public void setPagopaDetEsitoPagamento(String pagopaDetEsitoPagamento) {
		this.pagopaDetEsitoPagamento = pagopaDetEsitoPagamento;
	}

	public BigDecimal getPagopaDetImportoVersamento() {
		return pagopaDetImportoVersamento;
	}

	public void setPagopaDetImportoVersamento(BigDecimal pagopaDetImportoVersamento) {
		this.pagopaDetImportoVersamento = pagopaDetImportoVersamento;
	}

	public Integer getPagopaDetIndiceVersamento() {
		return pagopaDetIndiceVersamento;
	}

	public void setPagopaDetIndiceVersamento(Integer pagopaDetIndiceVersamento) {
		this.pagopaDetIndiceVersamento = pagopaDetIndiceVersamento;
	}

	public String getPagopaDetTransactionId() {
		return pagopaDetTransactionId;
	}

	public void setPagopaDetTransactionId(String pagopaDetTransactionId) {
		this.pagopaDetTransactionId = pagopaDetTransactionId;
	}

	public String getPagopaDetVersamentoId() {
		return pagopaDetVersamentoId;
	}

	public void setPagopaDetVersamentoId(String pagopaDetVersamentoId) {
		this.pagopaDetVersamentoId = pagopaDetVersamentoId;
	}

	public String getPagopaDetRiscossioneId() {
		return pagopaDetRiscossioneId;
	}

	public void setPagopaDetRiscossioneId(String pagopaDetRiscossioneId) {
		this.pagopaDetRiscossioneId = pagopaDetRiscossioneId;
	}


}