/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the sirfel_t_cassa_previdenziale database table.
 * 
 */
@Entity
@Table(name="sirfel_t_cassa_previdenziale")
@NamedQuery(name="SirfelTCassaPrevidenziale.findAll", query="SELECT s FROM SirfelTCassaPrevidenziale s")
public class SirfelTCassaPrevidenziale extends SirfelTBase<SirfelTCassaPrevidenzialePK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelTCassaPrevidenzialePK id;

	@Column(name="aliquota_cassa")
	private BigDecimal aliquotaCassa;

	@Column(name="aliquota_iva")
	private BigDecimal aliquotaIva;

	@Column(name="imponibile_cassa")
	private BigDecimal imponibileCassa;

	@Column(name="importo_contributo_cassa")
	private BigDecimal importoContributoCassa;

	@Column(name="riferimento_amministrazione")
	private String riferimentoAmministrazione;

	private String ritenuta;

	//bi-directional many-to-one association to SirfelDNatura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="natura", referencedColumnName="codice", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelDNatura sirfelDNatura;
	
	private String natura;

	//bi-directional many-to-one association to SirfelDTipoCassa
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="tipo_cassa", referencedColumnName="codice", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelDTipoCassa sirfelDTipoCassa;
	
	@Column(name="tipo_cassa")
	private String tipoCassa;

	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;
	
	public SirfelTCassaPrevidenziale() {
	}

	public SirfelTCassaPrevidenzialePK getId() {
		return this.id;
	}

	public void setId(SirfelTCassaPrevidenzialePK id) {
		this.id = id;
	}

	public BigDecimal getAliquotaCassa() {
		return this.aliquotaCassa;
	}

	public void setAliquotaCassa(BigDecimal aliquotaCassa) {
		this.aliquotaCassa = aliquotaCassa;
	}

	public BigDecimal getAliquotaIva() {
		return this.aliquotaIva;
	}

	public void setAliquotaIva(BigDecimal aliquotaIva) {
		this.aliquotaIva = aliquotaIva;
	}

	public BigDecimal getImponibileCassa() {
		return this.imponibileCassa;
	}

	public void setImponibileCassa(BigDecimal imponibileCassa) {
		this.imponibileCassa = imponibileCassa;
	}

	public BigDecimal getImportoContributoCassa() {
		return this.importoContributoCassa;
	}

	public void setImportoContributoCassa(BigDecimal importoContributoCassa) {
		this.importoContributoCassa = importoContributoCassa;
	}

	public String getRiferimentoAmministrazione() {
		return this.riferimentoAmministrazione;
	}

	public void setRiferimentoAmministrazione(String riferimentoAmministrazione) {
		this.riferimentoAmministrazione = riferimentoAmministrazione;
	}

	public String getRitenuta() {
		return this.ritenuta;
	}

	public void setRitenuta(String ritenuta) {
		this.ritenuta = ritenuta;
	}

	public SirfelDNatura getSirfelDNatura() {
		return this.sirfelDNatura;
	}

	public void setSirfelDNatura(SirfelDNatura sirfelDNatura) {
		this.sirfelDNatura = sirfelDNatura;
	}

	public SirfelDTipoCassa getSirfelDTipoCassa() {
		return this.sirfelDTipoCassa;
	}

	public void setSirfelDTipoCassa(SirfelDTipoCassa sirfelDTipoCassa) {
		this.sirfelDTipoCassa = sirfelDTipoCassa;
	}

	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura1) {
		this.sirfelTFattura = sirfelTFattura1;
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

	/**
	 * @return the tipoCassa
	 */
	public String getTipoCassa() {
		return tipoCassa;
	}

	/**
	 * @param tipoCassa the tipoCassa to set
	 */
	public void setTipoCassa(String tipoCassa) {
		this.tipoCassa = tipoCassa;
	}

	public Integer getProgressivo() {
		return getId() != null ? getId().getProgressivo() : null;
	}

}