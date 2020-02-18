/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_note_tesoriere database table.
 * 
 */
@Entity
@Table(name="siac_d_note_tesoriere")
public class SiacDNoteTesoriereFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="notetes_id")
	private Integer notetesId;

	@Column(name="notetes_code")
	private String notetesCode;

	@Column(name="notetes_desc")
	private String notetesDesc;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@OneToMany(mappedBy="siacDNoteTesoriere")
	private List<SiacTOrdinativoFin> siacTOrdinativos;

	//bi-directional many-to-one association to SiacTSubdocFin
	@OneToMany(mappedBy="siacDNoteTesoriere")
	private List<SiacTSubdocFin> siacTSubdocs;

	public SiacDNoteTesoriereFin() {
	}

	public Integer getNotetesId() {
		return this.notetesId;
	}

	public void setNotetesId(Integer notetesId) {
		this.notetesId = notetesId;
	}

	public String getNotetesCode() {
		return this.notetesCode;
	}

	public void setNotetesCode(String notetesCode) {
		this.notetesCode = notetesCode;
	}

	public String getNotetesDesc() {
		return this.notetesDesc;
	}

	public void setNotetesDesc(String notetesDesc) {
		this.notetesDesc = notetesDesc;
	}

	public List<SiacTOrdinativoFin> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativoFin> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativoFin addSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDNoteTesoriere(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativoFin removeSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDNoteTesoriere(null);

		return siacTOrdinativo;
	}

	public List<SiacTSubdocFin> getSiacTSubdocs() {
		return this.siacTSubdocs;
	}

	public void setSiacTSubdocs(List<SiacTSubdocFin> siacTSubdocs) {
		this.siacTSubdocs = siacTSubdocs;
	}

	public SiacTSubdocFin addSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		getSiacTSubdocs().add(siacTSubdoc);
		siacTSubdoc.setSiacDNoteTesoriere(this);

		return siacTSubdoc;
	}

	public SiacTSubdocFin removeSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacDNoteTesoriere(null);

		return siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.notetesId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.notetesId = uid;
	}

}