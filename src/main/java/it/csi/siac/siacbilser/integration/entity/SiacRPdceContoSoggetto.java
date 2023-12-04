/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
 * The persistent class for the siac_r_pdce_conto_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_pdce_conto_soggetto")
@NamedQuery(name="SiacRPdceContoSoggetto.findAll", query="SELECT s FROM SiacRPdceContoSoggetto s")
public class SiacRPdceContoSoggetto extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PDCE_CONTO_SOGGETTO_PDCECONTOSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PDCE_CONTO_SOGGETTO_PDCE_CONTO_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PDCE_CONTO_SOGGETTO_PDCECONTOSOGGETTOID_GENERATOR")
	@Column(name="pdce_conto_soggetto_id")
	private Integer pdceContoSoggettoId;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	public SiacRPdceContoSoggetto() {
	}

	public Integer getPdceContoSoggettoId() {
		return this.pdceContoSoggettoId;
	}

	public void setPdceContoSoggettoId(Integer pdceContoSoggettoId) {
		this.pdceContoSoggettoId = pdceContoSoggettoId;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		return pdceContoSoggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdceContoSoggettoId = uid;
	}

}