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
 * The persistent class for the siac_t_movimento database table.
 * 
 */
@Entity
@Table(name="siac_t_movimento")
@NamedQuery(name="SiacTMovimento.findAll", query="SELECT s FROM SiacTMovimento s")
public class SiacTMovimento extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MOVIMENTO_MOVTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOVIMENTO_MOVT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVIMENTO_MOVTID_GENERATOR")
	@Column(name="movt_id")
	private Integer movtId;

	private String abi;

	@Column(name="assegno_numero")
	private String assegnoNumero;

	private String bic;

	private String cab;

	private String cin;

	private String contocorrente;

	@Column(name="contocorrente_intestazione")
	private String contocorrenteIntestazione;

	private String iban;

	@Column(name="movt_data")
	private Date movtData;

	@Column(name="movt_numero")
	private Integer movtNumero;

	@Column(name="movt_pagamento_dettaglio")
	private String movtPagamentoDettaglio;
	//bi-directional many-to-one association to SiacRMovimentoModpag
	@OneToMany(mappedBy="siacTMovimento", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRMovimentoModpag> siacRMovimentoModpags;

	//bi-directional many-to-one association to SiacRAccreditoTipoCassaEcon
	@ManyToOne
	@JoinColumn(name="cec_r_accredito_tipo_id")
	private SiacRAccreditoTipoCassaEcon siacRAccreditoTipoCassaEcon;

	//bi-directional many-to-one association to SiacTGiustificativo
	@ManyToOne
	@JoinColumn(name="gst_id")
	private SiacTGiustificativo siacTGiustificativo;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;
	
	//bi-directional many-to-one association to SiacDCassaEconModpagTipo
	@ManyToOne
	@JoinColumn(name="cassamodpag_tipo_id")
	private SiacDCassaEconModpagTipo siacDCassaEconModpagTipo;

	public SiacTMovimento() {
	}

	public Integer getMovtId() {
		return this.movtId;
	}

	public void setMovtId(Integer movtId) {
		this.movtId = movtId;
	}

	public String getAbi() {
		return this.abi;
	}

	public void setAbi(String abi) {
		this.abi = abi;
	}

	public String getAssegnoNumero() {
		return this.assegnoNumero;
	}

	public void setAssegnoNumero(String assegnoNumero) {
		this.assegnoNumero = assegnoNumero;
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

	public String getCin() {
		return this.cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getContocorrente() {
		return this.contocorrente;
	}

	public void setContocorrente(String contocorrente) {
		this.contocorrente = contocorrente;
	}

	public String getContocorrenteIntestazione() {
		return this.contocorrenteIntestazione;
	}

	public void setContocorrenteIntestazione(String contocorrenteIntestazione) {
		this.contocorrenteIntestazione = contocorrenteIntestazione;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Date getMovtData() {
		return this.movtData;
	}

	public void setMovtData( Date movtData) {
		this.movtData = movtData;
	}

	public Integer getMovtNumero() {
		return this.movtNumero;
	}

	public void setMovtNumero(Integer movtNumero) {
		this.movtNumero = movtNumero;
	}

	public String getMovtPagamentoDettaglio() {
		return this.movtPagamentoDettaglio;
	}

	public void setMovtPagamentoDettaglio(String movtPagamentoDettaglio) {
		this.movtPagamentoDettaglio = movtPagamentoDettaglio;
	}

	public List<SiacRMovimentoModpag> getSiacRMovimentoModpags() {
		return this.siacRMovimentoModpags;
	}

	public void setSiacRMovimentoModpags(List<SiacRMovimentoModpag> siacRMovimentoModpags) {
		this.siacRMovimentoModpags = siacRMovimentoModpags;
	}

	public SiacRMovimentoModpag addSiacRMovimentoModpag(SiacRMovimentoModpag siacRMovimentoModpag) {
		getSiacRMovimentoModpags().add(siacRMovimentoModpag);
		siacRMovimentoModpag.setSiacTMovimento(this);

		return siacRMovimentoModpag;
	}

	public SiacRMovimentoModpag removeSiacRMovimentoModpag(SiacRMovimentoModpag siacRMovimentoModpag) {
		getSiacRMovimentoModpags().remove(siacRMovimentoModpag);
		siacRMovimentoModpag.setSiacTMovimento(null);

		return siacRMovimentoModpag;
	}

	public SiacRAccreditoTipoCassaEcon getSiacRAccreditoTipoCassaEcon() {
		return this.siacRAccreditoTipoCassaEcon;
	}

	public void setSiacRAccreditoTipoCassaEcon(SiacRAccreditoTipoCassaEcon siacRAccreditoTipoCassaEcon) {
		this.siacRAccreditoTipoCassaEcon = siacRAccreditoTipoCassaEcon;
	}

	public SiacTGiustificativo getSiacTGiustificativo() {
		return this.siacTGiustificativo;
	}

	public void setSiacTGiustificativo(SiacTGiustificativo siacTGiustificativo) {
		this.siacTGiustificativo = siacTGiustificativo;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}
	
	/**
	 * @return the siacDCassaEconModpagTipo
	 */
	public SiacDCassaEconModpagTipo getSiacDCassaEconModpagTipo() {
		return siacDCassaEconModpagTipo;
	}

	/**
	 * @param siacDCassaEconModpagTipo the siacDCassaEconModpagTipo to set
	 */
	public void setSiacDCassaEconModpagTipo(
			SiacDCassaEconModpagTipo siacDCassaEconModpagTipo) {
		this.siacDCassaEconModpagTipo = siacDCassaEconModpagTipo;
	}

	@Override
	public Integer getUid() {
		return movtId;
	}

	@Override
	public void setUid(Integer uid) {
		this.movtId = uid;
	}

}