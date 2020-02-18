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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_file database table.
 * 
 */
@Entity
@Table(name="siac_t_file")
@NamedQuery(name="SiacTFile.findAll", query="SELECT s FROM SiacTFile s")
public class SiacTFile extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_FILE_FILEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_FILE_FILE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_FILE_FILEID_GENERATOR")
	@Column(name="file_id")
	private Integer fileId;

	private byte[] file;

	@Column(name="file_code")
	private String fileCode;

	@Column(name="file_name")
	private String fileName;

	@Column(name="file_note")
	private String fileNote;

	@Column(name="file_size")
	private Long fileSize;

	@Column(name="file_type")
	private String fileType;


	//bi-directional many-to-one association to SiacRFileStato
	@OneToMany(mappedBy="siacTFile")
	private List<SiacRFileStato> siacRFileStatos;

	//bi-directional many-to-one association to SiacRIvaStampaFile
	@OneToMany(mappedBy="siacTFile")
	private List<SiacRIvaStampaFile> siacRIvaStampaFiles;

	//bi-directional many-to-one association to SiacDFileTipo
	@ManyToOne
	@JoinColumn(name="file_tipo_id")
	private SiacDFileTipo siacDFileTipo;

	public SiacTFile() {
	}

	public Integer getFileId() {
		return this.fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public byte[] getFile() {
		return this.file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileCode() {
		return this.fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileNote() {
		return this.fileNote;
	}

	public void setFileNote(String fileNote) {
		this.fileNote = fileNote;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public List<SiacRFileStato> getSiacRFileStatos() {
		return this.siacRFileStatos;
	}

	public void setSiacRFileStatos(List<SiacRFileStato> siacRFileStatos) {
		this.siacRFileStatos = siacRFileStatos;
	}

	public SiacRFileStato addSiacRFileStato(SiacRFileStato siacRFileStato) {
		getSiacRFileStatos().add(siacRFileStato);
		siacRFileStato.setSiacTFile(this);

		return siacRFileStato;
	}

	public SiacRFileStato removeSiacRFileStato(SiacRFileStato siacRFileStato) {
		getSiacRFileStatos().remove(siacRFileStato);
		siacRFileStato.setSiacTFile(null);

		return siacRFileStato;
	}

	public List<SiacRIvaStampaFile> getSiacRIvaStampaFiles() {
		return this.siacRIvaStampaFiles;
	}

	public void setSiacRIvaStampaFiles(List<SiacRIvaStampaFile> siacRIvaStampaFiles) {
		this.siacRIvaStampaFiles = siacRIvaStampaFiles;
	}

	public SiacRIvaStampaFile addSiacRIvaStampaFile(SiacRIvaStampaFile siacRIvaStampaFile) {
		getSiacRIvaStampaFiles().add(siacRIvaStampaFile);
		siacRIvaStampaFile.setSiacTFile(this);

		return siacRIvaStampaFile;
	}

	public SiacRIvaStampaFile removeSiacRIvaStampaFile(SiacRIvaStampaFile siacRIvaStampaFile) {
		getSiacRIvaStampaFiles().remove(siacRIvaStampaFile);
		siacRIvaStampaFile.setSiacTFile(null);

		return siacRIvaStampaFile;
	}

	public SiacDFileTipo getSiacDFileTipo() {
		return this.siacDFileTipo;
	}

	public void setSiacDFileTipo(SiacDFileTipo siacDFileTipo) {
		this.siacDFileTipo = siacDFileTipo;
	}

	@Override
	public Integer getUid() {
		return fileId;
	}

	@Override
	public void setUid(Integer uid) {
		this.fileId = uid;
	}

}