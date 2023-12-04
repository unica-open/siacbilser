/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_report database table.
 * 
 */
@Entity
@Table(name="siac_t_report")
@NamedQuery(name="SiacTReport.findAll", query="SELECT s FROM SiacTReport s")
public class SiacTReport extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_REPORT_REPID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_REPORT_REP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REPORT_REPID_GENERATOR")
	@Column(name="rep_id")
	private Integer repId;

	@Column(name="rep_birt_codice")
	private String repBirtCodice;

	@Column(name="rep_codice")
	private String repCodice;

	@Column(name="rep_desc")
	private String repDesc;

	//bi-directional many-to-one association to SiacRReportImporti
	@OneToMany(mappedBy="siacTReport")
	private List<SiacRReportImporti> siacRReportImportis;

	public SiacTReport() {
	}

	public Integer getRepId() {
		return this.repId;
	}

	public void setRepId(Integer repId) {
		this.repId = repId;
	}

	public String getRepBirtCodice() {
		return this.repBirtCodice;
	}

	public void setRepBirtCodice(String repBirtCodice) {
		this.repBirtCodice = repBirtCodice;
	}

	public String getRepCodice() {
		return this.repCodice;
	}

	public void setRepCodice(String repCodice) {
		this.repCodice = repCodice;
	}

	public String getRepDesc() {
		return this.repDesc;
	}

	public void setRepDesc(String repDesc) {
		this.repDesc = repDesc;
	}

	public List<SiacRReportImporti> getSiacRReportImportis() {
		return this.siacRReportImportis;
	}

	public void setSiacRReportImportis(List<SiacRReportImporti> siacRReportImportis) {
		this.siacRReportImportis = siacRReportImportis;
	}

	public SiacRReportImporti addSiacRReportImporti(SiacRReportImporti siacRReportImporti) {
		getSiacRReportImportis().add(siacRReportImporti);
		siacRReportImporti.setSiacTReport(this);

		return siacRReportImporti;
	}

	public SiacRReportImporti removeSiacRReportImporti(SiacRReportImporti siacRReportImporti) {
		getSiacRReportImportis().remove(siacRReportImporti);
		siacRReportImporti.setSiacTReport(null);

		return siacRReportImporti;
	}

	@Override
	public Integer getUid() {
		return repId;
	}

	@Override
	public void setUid(Integer uid) {
		repId = uid;
	}

}