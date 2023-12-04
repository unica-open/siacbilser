/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_ordinativo_quietanza database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_quietanza")
public class SiacROrdinativoQuietanzaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_QUIETANZA_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_quietanza_ord_quietanza_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_QUIETANZA_ID_GENERATOR")
	@Column(name="ord_quietanza_id")
	private Integer ordQuietanzaId;


	@Column(name="ord_quietanza_cro")
	private String ordQuietanzaCro;

	@Column(name="ord_quietanza_data")
	private Timestamp ordQuietanzaData;

	@Column(name="ord_quietanza_importo")
	private BigDecimal ordQuietanzaImporto;

	@Column(name="ord_quietanza_numero")
	private Integer ordQuietanzaNumero;


	//bi-directional many-to-one association to SiacTOilRicevuta
	@ManyToOne
	@JoinColumn(name="oil_ricevuta_id")
	private SiacTOilRicevuta siacTOilRicevuta;

	//bi-directional many-to-one association to SiacTOrdinativo
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacROrdinativoQuietanzaFin() {
	}

	public Integer getOrdQuietanzaId() {
		return this.ordQuietanzaId;
	}

	public void setOrdQuietanzaId(Integer ordQuietanzaId) {
		this.ordQuietanzaId = ordQuietanzaId;
	}


	public String getOrdQuietanzaCro() {
		return this.ordQuietanzaCro;
	}

	public void setOrdQuietanzaCro(String ordQuietanzaCro) {
		this.ordQuietanzaCro = ordQuietanzaCro;
	}

	public Timestamp getOrdQuietanzaData() {
		return this.ordQuietanzaData;
	}

	public void setOrdQuietanzaData(Timestamp ordQuietanzaData) {
		this.ordQuietanzaData = ordQuietanzaData;
	}

	public BigDecimal getOrdQuietanzaImporto() {
		return this.ordQuietanzaImporto;
	}

	public void setOrdQuietanzaImporto(BigDecimal ordQuietanzaImporto) {
		this.ordQuietanzaImporto = ordQuietanzaImporto;
	}

	public Integer getOrdQuietanzaNumero() {
		return this.ordQuietanzaNumero;
	}

	public void setOrdQuietanzaNumero(Integer ordQuietanzaNumero) {
		this.ordQuietanzaNumero = ordQuietanzaNumero;
	}


	public SiacTOilRicevuta getSiacTOilRicevuta() {
		return this.siacTOilRicevuta;
	}

	public void setSiacTOilRicevuta(SiacTOilRicevuta siacTOilRicevuta) {
		this.siacTOilRicevuta = siacTOilRicevuta;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	@Override
	public Integer getUid() {
		return this.ordQuietanzaId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ordQuietanzaId = uid;
	}

}