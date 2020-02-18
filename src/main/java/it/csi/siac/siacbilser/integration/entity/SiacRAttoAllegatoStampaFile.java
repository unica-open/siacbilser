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
 * The persistent class for the siac_r_atto_allegato_stampa_file database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_stampa_file")
@NamedQuery(name="SiacRAttoAllegatoStampaFile.findAll", query="SELECT s FROM SiacRAttoAllegatoStampaFile s")
public class SiacRAttoAllegatoStampaFile extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_ALLEGATO_STAMPA_FILE_ATTOALSTFILEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTO_ALLEGATO_STAMPA_FILE_ATTOALST_FILE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_ALLEGATO_STAMPA_FILE_ATTOALSTFILEID_GENERATOR")
	@Column(name="attoalst_file_id")
	private Integer attoalstFileId;

	//bi-directional many-to-one association to SiacTAttoAllegatoStampa
	@ManyToOne
	@JoinColumn(name="attoalst_id")
	private SiacTAttoAllegatoStampa siacTAttoAllegatoStampa;

	//bi-directional many-to-one association to SiacTFile
	@ManyToOne
	@JoinColumn(name="file_id")
	private SiacTFile siacTFile;

	public SiacRAttoAllegatoStampaFile() {
	}

	public Integer getAttoalstFileId() {
		return this.attoalstFileId;
	}

	public void setAttoalstFileId(Integer attoalstFileId) {
		this.attoalstFileId = attoalstFileId;
	}

	public SiacTAttoAllegatoStampa getSiacTAttoAllegatoStampa() {
		return this.siacTAttoAllegatoStampa;
	}

	public void setSiacTAttoAllegatoStampa(SiacTAttoAllegatoStampa siacTAttoAllegatoStampa) {
		this.siacTAttoAllegatoStampa = siacTAttoAllegatoStampa;
	}

	public SiacTFile getSiacTFile() {
		return this.siacTFile;
	}

	public void setSiacTFile(SiacTFile siacTFile) {
		this.siacTFile = siacTFile;
	}

	@Override
	public Integer getUid() {
		return this.attoalstFileId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalstFileId = uid;
	}

}