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
 * The persistent class for the siac_r_file_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_file_stato")
@NamedQuery(name="SiacRFileStato.findAll", query="SELECT s FROM SiacRFileStato s")
public class SiacRFileStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_FILE_STATO_FILESTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_FILE_STATO_FILE_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FILE_STATO_FILESTATORID_GENERATOR")
	@Column(name="file_stato_r_id")
	private Integer fileStatoRId;


	//bi-directional many-to-one association to SiacDFileStato
	@ManyToOne
	@JoinColumn(name="file_stato_id")
	private SiacDFileStato siacDFileStato;

	//bi-directional many-to-one association to SiacTFile
	@ManyToOne
	@JoinColumn(name="file_id")
	private SiacTFile siacTFile;

	public SiacRFileStato() {
	}

	public Integer getFileStatoRId() {
		return this.fileStatoRId;
	}

	public void setFileStatoRId(Integer fileStatoRId) {
		this.fileStatoRId = fileStatoRId;
	}

	public SiacDFileStato getSiacDFileStato() {
		return this.siacDFileStato;
	}

	public void setSiacDFileStato(SiacDFileStato siacDFileStato) {
		this.siacDFileStato = siacDFileStato;
	}

	public SiacTFile getSiacTFile() {
		return this.siacTFile;
	}

	public void setSiacTFile(SiacTFile siacTFile) {
		this.siacTFile = siacTFile;
	}

	@Override
	public Integer getUid() {
		return fileStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.fileStatoRId = uid;
	}

}