/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_riepilogo_beni database table.
 * 
 */
@Entity
@Table(name="sirfel_t_riepilogo_beni")
@NamedQuery(name="SirfelTRiepilogoBeni.findAll", query="SELECT s FROM SirfelTRiepilogoBeni s")
public class SirfelTRiepilogoBeni extends SirfelTBase<SirfelTRiepilogoBeniPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelTRiepilogoBeniPK id;

	@Column(name="aliquota_iva")
	private BigDecimal aliquotaIva;

	private BigDecimal arrotondamento;

	@Column(name="esigibilita_iva")
	private String esigibilitaIva;

	@Column(name="imponibile_importo")
	private BigDecimal imponibileImporto;

	private BigDecimal imposta;

	@Column(name="spese_accessorie")
	private BigDecimal speseAccessorie;

	//bi-directional many-to-one association to SirfelDNatura
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="natura", referencedColumnName="codice", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelDNatura sirfelDNatura;

	private String natura;
	
	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SirfelTRiepilogoBeni() {
	}

	public SirfelTRiepilogoBeniPK getId() {
		return this.id;
	}

	public void setId(SirfelTRiepilogoBeniPK id) {
		this.id = id;
	}

	public BigDecimal getAliquotaIva() {
		return this.aliquotaIva;
	}

	public void setAliquotaIva(BigDecimal aliquotaIva) {
		this.aliquotaIva = aliquotaIva;
	}

	public BigDecimal getArrotondamento() {
		return this.arrotondamento;
	}

	public void setArrotondamento(BigDecimal arrotondamento) {
		this.arrotondamento = arrotondamento;
	}

	public String getEsigibilitaIva() {
		return this.esigibilitaIva;
	}

	public void setEsigibilitaIva(String esigibilitaIva) {
		this.esigibilitaIva = esigibilitaIva;
	}

	public BigDecimal getImponibileImporto() {
		return this.imponibileImporto;
	}

	public void setImponibileImporto(BigDecimal imponibileImporto) {
		this.imponibileImporto = imponibileImporto;
	}

	public BigDecimal getImposta() {
		return this.imposta;
	}

	public void setImposta(BigDecimal imposta) {
		this.imposta = imposta;
	}

	public BigDecimal getSpeseAccessorie() {
		return this.speseAccessorie;
	}

	public void setSpeseAccessorie(BigDecimal speseAccessorie) {
		this.speseAccessorie = speseAccessorie;
	}

	public SirfelDNatura getSirfelDNatura() {
		return this.sirfelDNatura;
	}

	public void setSirfelDNatura(SirfelDNatura sirfelDNatura) {
		this.sirfelDNatura = sirfelDNatura;
	}

	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}

	/**
	 * @return the natura
	 */
	public String getNatura() {
		return natura;
	}

	/**
	 * @param natura the natura to set
	 */
	public void setNatura(String natura) {
		this.natura = natura;
	}

	public Integer getProgressivo() {
		return getId() != null ? getId().getProgressivo() : null;
	}
}