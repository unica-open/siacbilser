/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_dettaglio_pagamento database table.
 * 
 */
@Entity
@Table(name="sirfel_t_dettaglio_pagamento")
@NamedQuery(name="SirfelTDettaglioPagamento.findAll", query="SELECT s FROM SirfelTDettaglioPagamento s")
public class SirfelTDettaglioPagamento extends SirfelTBase<SirfelTDettaglioPagamentoPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
        @AttributeOverride(name="enteProprietarioId", column=@Column(name="ente_proprietario_id")),
        @AttributeOverride(name="idFattura", column=@Column(name="id_fattura")),
        @AttributeOverride(name="progressivoPagamento", column=@Column(name="progressivo_pagamento")),
        @AttributeOverride(name="progressivoDettaglio", column=@Column(name="progressivo_dettaglio"))
    })
	private SirfelTDettaglioPagamentoPK id;

	private String abi;

	private String beneficiario;

	private String bic;

	private String cab;

	@Column(name="cf_quietanzante")
	private String cfQuietanzante;

	@Column(name="codice_pagamento")
	private String codicePagamento;

	@Column(name="codice_ufficio_postale")
	private String codiceUfficioPostale;

	@Column(name="cognome_quietanzante")
	private String cognomeQuietanzante;

	@Column(name="data_decorrenza_penale")
	private Date dataDecorrenzaPenale;

	@Column(name="data_limite_pagamento_ant")
	private Date dataLimitePagamentoAnt;

	@Column(name="data_rif_termini_pagamento")
	private Date dataRifTerminiPagamento;

	@Column(name="data_scadenza_pagamento")
	private Date dataScadenzaPagamento;

	@Column(name="giorni_termini_pagamento")
	private Integer giorniTerminiPagamento;

	private String iban;

	@Column(name="importo_pagamento")
	private BigDecimal importoPagamento;

	@Column(name="istituto_finanziario")
	private String istitutoFinanziario;

	@Column(name="nome_quietanzante")
	private String nomeQuietanzante;

	@Column(name="penalita_pagamenti_ritardati")
	private BigDecimal penalitaPagamentiRitardati;

	@Column(name="sconto_pagamento_anticipato")
	private BigDecimal scontoPagamentoAnticipato;

	@Column(name="titolo_quietanzante")
	private String titoloQuietanzante;

	//bi-directional many-to-one association to SirfelDModalitaPagamento
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="modalita_pagamento", referencedColumnName="codice", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelDModalitaPagamento sirfelDModalitaPagamento;
	
	@Column(name="modalita_pagamento")
	private String modalitaPagamento;

	//bi-directional many-to-one association to SirfelTPagamento
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura", insertable=false, updatable=false),
		@JoinColumn(name="progressivo_pagamento", referencedColumnName="progressivo", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelTPagamento sirfelTPagamento;

	public SirfelTDettaglioPagamento() {
	}

	public SirfelTDettaglioPagamentoPK getId() {
		return this.id;
	}

	public void setId(SirfelTDettaglioPagamentoPK id) {
		this.id = id;
	}

	public String getAbi() {
		return this.abi;
	}

	public void setAbi(String abi) {
		this.abi = abi;
	}

	public String getBeneficiario() {
		return this.beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getBic() {
		return this.bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getCab() {
		return this.cab;
	}

	public void setCab(String cab) {
		this.cab = cab;
	}

	public String getCfQuietanzante() {
		return this.cfQuietanzante;
	}

	public void setCfQuietanzante(String cfQuietanzante) {
		this.cfQuietanzante = cfQuietanzante;
	}

	public String getCodicePagamento() {
		return this.codicePagamento;
	}

	public void setCodicePagamento(String codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	public String getCodiceUfficioPostale() {
		return this.codiceUfficioPostale;
	}

	public void setCodiceUfficioPostale(String codiceUfficioPostale) {
		this.codiceUfficioPostale = codiceUfficioPostale;
	}

	public String getCognomeQuietanzante() {
		return this.cognomeQuietanzante;
	}

	public void setCognomeQuietanzante(String cognomeQuietanzante) {
		this.cognomeQuietanzante = cognomeQuietanzante;
	}

	public Date getDataDecorrenzaPenale() {
		return this.dataDecorrenzaPenale;
	}

	public void setDataDecorrenzaPenale(Date dataDecorrenzaPenale) {
		this.dataDecorrenzaPenale = dataDecorrenzaPenale;
	}

	public Date getDataLimitePagamentoAnt() {
		return this.dataLimitePagamentoAnt;
	}

	public void setDataLimitePagamentoAnt(Date dataLimitePagamentoAnt) {
		this.dataLimitePagamentoAnt = dataLimitePagamentoAnt;
	}

	public Date getDataRifTerminiPagamento() {
		return this.dataRifTerminiPagamento;
	}

	public void setDataRifTerminiPagamento(Date dataRifTerminiPagamento) {
		this.dataRifTerminiPagamento = dataRifTerminiPagamento;
	}

	public Date getDataScadenzaPagamento() {
		return this.dataScadenzaPagamento;
	}

	public void setDataScadenzaPagamento(Date dataScadenzaPagamento) {
		this.dataScadenzaPagamento = dataScadenzaPagamento;
	}

	public Integer getGiorniTerminiPagamento() {
		return this.giorniTerminiPagamento;
	}

	public void setGiorniTerminiPagamento(Integer giorniTerminiPagamento) {
		this.giorniTerminiPagamento = giorniTerminiPagamento;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public BigDecimal getImportoPagamento() {
		return this.importoPagamento;
	}

	public void setImportoPagamento(BigDecimal importoPagamento) {
		this.importoPagamento = importoPagamento;
	}

	public String getIstitutoFinanziario() {
		return this.istitutoFinanziario;
	}

	public void setIstitutoFinanziario(String istitutoFinanziario) {
		this.istitutoFinanziario = istitutoFinanziario;
	}

	public String getNomeQuietanzante() {
		return this.nomeQuietanzante;
	}

	public void setNomeQuietanzante(String nomeQuietanzante) {
		this.nomeQuietanzante = nomeQuietanzante;
	}

	public BigDecimal getPenalitaPagamentiRitardati() {
		return this.penalitaPagamentiRitardati;
	}

	public void setPenalitaPagamentiRitardati(BigDecimal penalitaPagamentiRitardati) {
		this.penalitaPagamentiRitardati = penalitaPagamentiRitardati;
	}

	public BigDecimal getScontoPagamentoAnticipato() {
		return this.scontoPagamentoAnticipato;
	}

	public void setScontoPagamentoAnticipato(BigDecimal scontoPagamentoAnticipato) {
		this.scontoPagamentoAnticipato = scontoPagamentoAnticipato;
	}

	public String getTitoloQuietanzante() {
		return this.titoloQuietanzante;
	}

	public void setTitoloQuietanzante(String titoloQuietanzante) {
		this.titoloQuietanzante = titoloQuietanzante;
	}

	public SirfelDModalitaPagamento getSirfelDModalitaPagamento() {
		return this.sirfelDModalitaPagamento;
	}

	public void setSirfelDModalitaPagamento(SirfelDModalitaPagamento sirfelDModalitaPagamento) {
		this.sirfelDModalitaPagamento = sirfelDModalitaPagamento;
	}

	public SirfelTPagamento getSirfelTPagamento() {
		return this.sirfelTPagamento;
	}

	public void setSirfelTPagamento(SirfelTPagamento sirfelTPagamento) {
		this.sirfelTPagamento = sirfelTPagamento;
	}
	
	/**
	 * @return the modalitaPagamento
	 */
	public String getModalitaPagamento() {
		return modalitaPagamento;
	}

	/**
	 * @param modalitaPagamento the modalitaPagamento to set
	 */
	public void setModalitaPagamento(String modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}

	public Integer getProgressivoPagamento() {
		return getId() != null ? getId().getProgressivoPagamento() : null;
	}
	
	public Integer getProgressivoDettaglio() {
		return getId() != null ? getId().getProgressivoDettaglio() : null;
	}

}