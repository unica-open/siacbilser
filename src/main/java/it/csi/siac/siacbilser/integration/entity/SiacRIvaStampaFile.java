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
 * The persistent class for the siac_r_iva_stampa_file database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_stampa_file")
@NamedQuery(name="SiacRIvaStampaFile.findAll", query="SELECT s FROM SiacRIvaStampaFile s")
public class SiacRIvaStampaFile extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_IVA_STAMPA_FILE_IVASTFILEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_IVA_STAMPA_FILE_IVAST_FILE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_STAMPA_FILE_IVASTFILEID_GENERATOR")
	@Column(name="ivast_file_id")
	private Integer ivastFileId;

	//bi-directional many-to-one association to SiacTFile
	@ManyToOne
	@JoinColumn(name="file_id")
	private SiacTFile siacTFile;

	//bi-directional many-to-one association to SiacTIvaStampa
	@ManyToOne
	@JoinColumn(name="ivast_id")
	private SiacTIvaStampa siacTIvaStampa;

	public SiacRIvaStampaFile() {
	}

	public Integer getIvastFileId() {
		return this.ivastFileId;
	}

	public void setIvastFileId(Integer ivastFileId) {
		this.ivastFileId = ivastFileId;
	}


	public SiacTFile getSiacTFile() {
		return this.siacTFile;
	}

	public void setSiacTFile(SiacTFile siacTFile) {
		this.siacTFile = siacTFile;
	}

	public SiacTIvaStampa getSiacTIvaStampa() {
		return this.siacTIvaStampa;
	}

	public void setSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		this.siacTIvaStampa = siacTIvaStampa;
	}

	@Override
	public Integer getUid() {
		return ivastFileId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastFileId = uid;
	}

}