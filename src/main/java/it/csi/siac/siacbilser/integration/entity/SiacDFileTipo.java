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
 * The persistent class for the siac_d_file_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_file_tipo")
@NamedQuery(name="SiacDFileTipo.findAll", query="SELECT s FROM SiacDFileTipo s")
public class SiacDFileTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_FILE_TIPO_FILETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_FILE_TIPO_FILE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_FILE_TIPO_FILETIPOID_GENERATOR")
	@Column(name="file_tipo_id")
	private Integer fileTipoId;

	@Column(name="file_tipo_code")
	private String fileTipoCode;

	@Column(name="file_tipo_desc")
	private String fileTipoDesc;


	//bi-directional many-to-one association to SiacTAzione
	@ManyToOne
	@JoinColumn(name="azione_id")
	private SiacTAzione siacTAzione;

	//bi-directional many-to-one association to SiacTFile
	@OneToMany(mappedBy="siacDFileTipo")
	private List<SiacTFile> siacTFiles;

	public SiacDFileTipo() {
	}

	public Integer getFileTipoId() {
		return this.fileTipoId;
	}

	public void setFileTipoId(Integer fileTipoId) {
		this.fileTipoId = fileTipoId;
	}

	public String getFileTipoCode() {
		return this.fileTipoCode;
	}

	public void setFileTipoCode(String fileTipoCode) {
		this.fileTipoCode = fileTipoCode;
	}

	public String getFileTipoDesc() {
		return this.fileTipoDesc;
	}

	public void setFileTipoDesc(String fileTipoDesc) {
		this.fileTipoDesc = fileTipoDesc;
	}

	public SiacTAzione getSiacTAzione() {
		return this.siacTAzione;
	}

	public void setSiacTAzione(SiacTAzione siacTAzione) {
		this.siacTAzione = siacTAzione;
	}

	public List<SiacTFile> getSiacTFiles() {
		return this.siacTFiles;
	}

	public void setSiacTFiles(List<SiacTFile> siacTFiles) {
		this.siacTFiles = siacTFiles;
	}

	public SiacTFile addSiacTFile(SiacTFile siacTFile) {
		getSiacTFiles().add(siacTFile);
		siacTFile.setSiacDFileTipo(this);

		return siacTFile;
	}

	public SiacTFile removeSiacTFile(SiacTFile siacTFile) {
		getSiacTFiles().remove(siacTFile);
		siacTFile.setSiacDFileTipo(null);

		return siacTFile;
	}

	@Override
	public Integer getUid() {
		return fileTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.fileTipoId = uid;
	}

}