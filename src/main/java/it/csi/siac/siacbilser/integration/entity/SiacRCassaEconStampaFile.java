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
 * The persistent class for the siac_r_cassa_econ_stampa_file database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_stampa_file")
@NamedQuery(name="SiacRCassaEconStampaFile.findAll", query="SELECT s FROM SiacRCassaEconStampaFile s")
public class SiacRCassaEconStampaFile extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_STAMPA_FILE_CESTFILEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CASSA_ECON_STAMPA_FILE_CEST_FILE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_STAMPA_FILE_CESTFILEID_GENERATOR")
	@Column(name="cest_file_id")
	private Integer cestFileId;

	//bi-directional many-to-one association to SiacTCassaEconStampa
	@ManyToOne
	@JoinColumn(name="cest_id")
	private SiacTCassaEconStampa siacTCassaEconStampa;

	//bi-directional many-to-one association to SiacTFile
	@ManyToOne
	@JoinColumn(name="file_id")
	private SiacTFile siacTFile;

	public SiacRCassaEconStampaFile() {
	}

	public Integer getCestFileId() {
		return this.cestFileId;
	}

	public void setCestFileId(Integer cestFileId) {
		this.cestFileId = cestFileId;
	}

	public SiacTCassaEconStampa getSiacTCassaEconStampa() {
		return this.siacTCassaEconStampa;
	}

	public void setSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		this.siacTCassaEconStampa = siacTCassaEconStampa;
	}

	public SiacTFile getSiacTFile() {
		return this.siacTFile;
	}

	public void setSiacTFile(SiacTFile siacTFile) {
		this.siacTFile = siacTFile;
	}

	@Override
	public Integer getUid() {
		return this.cestFileId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cestFileId = uid;		
	}

}