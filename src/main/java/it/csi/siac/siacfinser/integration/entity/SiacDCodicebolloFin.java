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
 * The persistent class for the siac_d_codicebollo database table.
 * 
 */
@Entity
@Table(name="siac_d_codicebollo")
public class SiacDCodicebolloFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="codbollo_id")
	private Integer codbolloId;

	@Column(name="codbollo_code")
	private String codbolloCode;

	@Column(name="codbollo_desc")
	private String codbolloDesc;

	//bi-directional many-to-one association to SiacTDocFin
	@OneToMany(mappedBy="siacDCodicebollo")
	private List<SiacTDocFin> siacTDocs;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@OneToMany(mappedBy="siacDCodicebollo")
	private List<SiacTOrdinativoFin> siacTOrdinativos;

	public SiacDCodicebolloFin() {
	}

	public Integer getCodbolloId() {
		return this.codbolloId;
	}

	public void setCodbolloId(Integer codbolloId) {
		this.codbolloId = codbolloId;
	}

	public String getCodbolloCode() {
		return this.codbolloCode;
	}

	public void setCodbolloCode(String codbolloCode) {
		this.codbolloCode = codbolloCode;
	}

	public String getCodbolloDesc() {
		return this.codbolloDesc;
	}

	public void setCodbolloDesc(String codbolloDesc) {
		this.codbolloDesc = codbolloDesc;
	}

	public List<SiacTDocFin> getSiacTDocs() {
		return this.siacTDocs;
	}

	public void setSiacTDocs(List<SiacTDocFin> siacTDocs) {
		this.siacTDocs = siacTDocs;
	}

	public SiacTDocFin addSiacTDoc(SiacTDocFin siacTDoc) {
		getSiacTDocs().add(siacTDoc);
		siacTDoc.setSiacDCodicebollo(this);

		return siacTDoc;
	}

	public SiacTDocFin removeSiacTDoc(SiacTDocFin siacTDoc) {
		getSiacTDocs().remove(siacTDoc);
		siacTDoc.setSiacDCodicebollo(null);

		return siacTDoc;
	}

	public List<SiacTOrdinativoFin> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativoFin> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativoFin addSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDCodicebollo(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativoFin removeSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDCodicebollo(null);

		return siacTOrdinativo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.codbolloId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.codbolloId = uid;
	}
}