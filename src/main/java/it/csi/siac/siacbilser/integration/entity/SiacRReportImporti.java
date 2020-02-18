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
 * The persistent class for the siac_r_report_importi database table.
 * 
 */
@Entity
@Table(name="siac_r_report_importi")
@NamedQuery(name="SiacRReportImporti.findAll", query="SELECT s FROM SiacRReportImporti s")
public class SiacRReportImporti extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_REPORT_IMPORTI_REPRIMPID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_REPORT_IMPORTI_REPRIMP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_REPORT_IMPORTI_REPRIMPID_GENERATOR")
	@Column(name="reprimp_id")
	private Integer reprimpId;

	@Column(name="posizione_stampa")
	private Integer posizioneStampa;

	//bi-directional many-to-one association to SiacTReport
	@ManyToOne
	@JoinColumn(name="rep_id")
	private SiacTReport siacTReport;

	//bi-directional many-to-one association to SiacTReportImporti
	@ManyToOne
	@JoinColumn(name="repimp_id")
	private SiacTReportImporti siacTReportImporti;

	public SiacRReportImporti() {
	}

	public Integer getReprimpId() {
		return this.reprimpId;
	}

	public void setReprimpId(Integer reprimpId) {
		this.reprimpId = reprimpId;
	}

	public Integer getPosizioneStampa() {
		return this.posizioneStampa;
	}

	public void setPosizioneStampa(Integer posizioneStampa) {
		this.posizioneStampa = posizioneStampa;
	}

	public SiacTReport getSiacTReport() {
		return this.siacTReport;
	}

	public void setSiacTReport(SiacTReport siacTReport) {
		this.siacTReport = siacTReport;
	}

	public SiacTReportImporti getSiacTReportImporti() {
		return this.siacTReportImporti;
	}

	public void setSiacTReportImporti(SiacTReportImporti siacTReportImporti) {
		this.siacTReportImporti = siacTReportImporti;
	}

	@Override
	public Integer getUid() {
		return reprimpId;
	}

	@Override
	public void setUid(Integer uid) {
		reprimpId = uid;
	}

}