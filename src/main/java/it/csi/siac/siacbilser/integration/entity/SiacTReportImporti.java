/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

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
 * The persistent class for the siac_t_report_importi database table.
 * 
 */
@Entity
@Table(name="siac_t_report_importi")
@NamedQuery(name="SiacTReportImporti.findAll", query="SELECT s FROM SiacTReportImporti s")
public class SiacTReportImporti extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_REPORT_IMPORTI_REPIMPID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_REPORT_IMPORTI_REPIMP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REPORT_IMPORTI_REPIMPID_GENERATOR")
	@Column(name="repimp_id")
	private Integer repimpId;

	@Column(name="repimp_codice")
	private String repimpCodice;

	@Column(name="repimp_desc")
	private String repimpDesc;

	@Column(name="repimp_importo")
	private BigDecimal repimpImporto;

	@Column(name="repimp_modificabile")
	private String repimpModificabile;

	//bi-directional many-to-one association to SiacRReportImporti
	@OneToMany(mappedBy="siacTReportImporti")
	private List<SiacRReportImporti> siacRReportImportis;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTPeriodo
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;

	public SiacTReportImporti() {
	}

	public Integer getRepimpId() {
		return this.repimpId;
	}

	public void setRepimpId(Integer repimpId) {
		this.repimpId = repimpId;
	}

	public String getRepimpCodice() {
		return this.repimpCodice;
	}

	public void setRepimpCodice(String repimpCodice) {
		this.repimpCodice = repimpCodice;
	}

	public String getRepimpDesc() {
		return this.repimpDesc;
	}

	public void setRepimpDesc(String repimpDesc) {
		this.repimpDesc = repimpDesc;
	}

	public BigDecimal getRepimpImporto() {
		return this.repimpImporto;
	}

	public void setRepimpImporto(BigDecimal repimpImporto) {
		this.repimpImporto = repimpImporto;
	}

	public String getRepimpModificabile() {
		return this.repimpModificabile;
	}

	public void setRepimpModificabile(String repimpModificabile) {
		this.repimpModificabile = repimpModificabile;
	}

	public List<SiacRReportImporti> getSiacRReportImportis() {
		return this.siacRReportImportis;
	}

	public void setSiacRReportImportis(List<SiacRReportImporti> siacRReportImportis) {
		this.siacRReportImportis = siacRReportImportis;
	}

	public SiacRReportImporti addSiacRReportImporti(SiacRReportImporti siacRReportImporti) {
		getSiacRReportImportis().add(siacRReportImporti);
		siacRReportImporti.setSiacTReportImporti(this);

		return siacRReportImporti;
	}

	public SiacRReportImporti removeSiacRReportImporti(SiacRReportImporti siacRReportImporti) {
		getSiacRReportImportis().remove(siacRReportImporti);
		siacRReportImporti.setSiacTReportImporti(null);

		return siacRReportImporti;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTPeriodo getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	@Override
	public Integer getUid() {
		return repimpId;
	}

	@Override
	public void setUid(Integer uid) {
		repimpId = uid;
	}

}