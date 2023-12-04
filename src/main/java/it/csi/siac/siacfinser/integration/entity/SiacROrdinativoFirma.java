/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

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
 * The persistent class for the siac_r_ordinativo_firma database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_firma")
public class SiacROrdinativoFirma extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_FIRMA_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_firma_ord_firma_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_FIRMA_ID_GENERATOR")
	@Column(name="ord_firma_id")
	private Integer ordFirmaId;

	@Column(name="ord_firma")
	private String ordFirma;

	@Column(name="ord_firma_data")
	private Timestamp ordFirmaData;



	//bi-directional many-to-one association to SiacTOilRicevuta
	@ManyToOne
	@JoinColumn(name="oil_ricevuta_id")
	private SiacTOilRicevuta siacTOilRicevuta;

	//bi-directional many-to-one association to SiacTOrdinativo
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacROrdinativoFirma() {
	}

	public Integer getOrdFirmaId() {
		return this.ordFirmaId;
	}

	public void setOrdFirmaId(Integer ordFirmaId) {
		this.ordFirmaId = ordFirmaId;
	}

	public String getOrdFirma() {
		return this.ordFirma;
	}

	public void setOrdFirma(String ordFirma) {
		this.ordFirma = ordFirma;
	}

	public Timestamp getOrdFirmaData() {
		return this.ordFirmaData;
	}

	public void setOrdFirmaData(Timestamp ordFirmaData) {
		this.ordFirmaData = ordFirmaData;
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
		// TODO Auto-generated method stub
		return this.ordFirmaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordFirmaId = uid;
	}

}