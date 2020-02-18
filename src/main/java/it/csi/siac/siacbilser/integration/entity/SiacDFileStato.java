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
 * The persistent class for the siac_d_file_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_file_stato")
@NamedQuery(name="SiacDFileStato.findAll", query="SELECT s FROM SiacDFileStato s")
public class SiacDFileStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_FILE_STATO_FILESTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_FILE_STATO_FILE_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_FILE_STATO_FILESTATOID_GENERATOR")
	@Column(name="file_stato_id")
	private Integer fileStatoId;

	@Column(name="file_stato_code")
	private String fileStatoCode;

	@Column(name="file_stato_desc")
	private String fileStatoDesc;



	//bi-directional many-to-one association to SiacRFileStato
	@OneToMany(mappedBy="siacDFileStato")
	private List<SiacRFileStato> siacRFileStatos;

	public SiacDFileStato() {
	}

	public Integer getFileStatoId() {
		return this.fileStatoId;
	}

	public void setFileStatoId(Integer fileStatoId) {
		this.fileStatoId = fileStatoId;
	}

	public String getFileStatoCode() {
		return this.fileStatoCode;
	}

	public void setFileStatoCode(String fileStatoCode) {
		this.fileStatoCode = fileStatoCode;
	}

	public String getFileStatoDesc() {
		return this.fileStatoDesc;
	}

	public void setFileStatoDesc(String fileStatoDesc) {
		this.fileStatoDesc = fileStatoDesc;
	}

	public List<SiacRFileStato> getSiacRFileStatos() {
		return this.siacRFileStatos;
	}

	public void setSiacRFileStatos(List<SiacRFileStato> siacRFileStatos) {
		this.siacRFileStatos = siacRFileStatos;
	}

	public SiacRFileStato addSiacRFileStato(SiacRFileStato siacRFileStato) {
		getSiacRFileStatos().add(siacRFileStato);
		siacRFileStato.setSiacDFileStato(this);

		return siacRFileStato;
	}

	public SiacRFileStato removeSiacRFileStato(SiacRFileStato siacRFileStato) {
		getSiacRFileStatos().remove(siacRFileStato);
		siacRFileStato.setSiacDFileStato(null);

		return siacRFileStato;
	}

	@Override
	public Integer getUid() {
		return fileStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.fileStatoId = uid;
	}

}