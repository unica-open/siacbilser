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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


/**
 * The persistent class for the sirfel_t_dati_ritenuta database table.
 * 
 */
@Entity
@Table(name="sirfel_t_dati_ritenuta")
@NamedQuery(name="SirfelTDatiRitenuta.findAll", query="SELECT s FROM SirfelTDatiRitenuta s")
public class SirfelTDatiRitenuta extends SiacTBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIRFEL_T_DATI_RITENUTA_IDRITENUTA_GENERATOR", allocationSize=1, sequenceName="SIRFEL_T_DATI_RITENUTA_ID_RITENUTA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIRFEL_T_DATI_RITENUTA_IDRITENUTA_GENERATOR")
	@Column(name="id_ritenuta")
	private Integer idRitenuta;

	@Column(name="tipo")
	private String tipo;

	@Column(name="aliquota")
	private BigDecimal aliquota;

	@Column(name="importo")
	private BigDecimal importo;

	@Column(name = "causale_pagamento")
	private String causalePagamento;


	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id"),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura")
		})
	//@MapsId("id")
	private SirfelTFattura sirfelTFattura;
	
	
	
	public SirfelTDatiRitenuta() {
	}


	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}
	


	@Override
	public Integer getUid() {
		return this.idRitenuta;
	}

	@Override
	public void setUid(Integer uid) {
		this.idRitenuta = uid;
		
	}


	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}


	/**
	 * @return the aliquota
	 */
	public BigDecimal getAliquota() {
		return aliquota;
	}


	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}


	/**
	 * @return the causalePagamento
	 */
	public String getCausalePagamento() {
		return causalePagamento;
	}


	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	/**
	 * @param aliquota the aliquota to set
	 */
	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
	}


	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}


	/**
	 * @param causalePagamento the causalePagamento to set
	 */
	public void setCausalePagamento(String causalePagamento) {
		this.causalePagamento = causalePagamento;
	}


	/**
	 * @return the idRitenuta
	 */
	public Integer getIdRitenuta() {
		return idRitenuta;
	}


	/**
	 * @param idRitenuta the idRitenuta to set
	 */
	public void setIdRitenuta(Integer idRitenuta) {
		this.idRitenuta = idRitenuta;
	}


	

}