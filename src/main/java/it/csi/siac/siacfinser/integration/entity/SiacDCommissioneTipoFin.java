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
 * The persistent class for the siac_d_commissione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_commissione_tipo")
public class SiacDCommissioneTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="comm_tipo_id")
	private Integer commTipoId;

	@Column(name="comm_tipo_code")
	private String commTipoCode;

	@Column(name="comm_tipo_desc")
	private String commTipoDesc;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@OneToMany(mappedBy="siacDCommissioneTipo")
	private List<SiacTOrdinativoFin> siacTOrdinativos;

	//bi-directional many-to-one association to SiacTSubdocFin
	@OneToMany(mappedBy="siacDCommissioneTipo")
	private List<SiacTSubdocFin> siacTSubdocs;

	public SiacDCommissioneTipoFin() {
	}

	public Integer getCommTipoId() {
		return this.commTipoId;
	}

	public void setCommTipoId(Integer commTipoId) {
		this.commTipoId = commTipoId;
	}

	public String getCommTipoCode() {
		return this.commTipoCode;
	}

	public void setCommTipoCode(String commTipoCode) {
		this.commTipoCode = commTipoCode;
	}

	public String getCommTipoDesc() {
		return this.commTipoDesc;
	}

	public void setCommTipoDesc(String commTipoDesc) {
		this.commTipoDesc = commTipoDesc;
	}

	public List<SiacTOrdinativoFin> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativoFin> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativoFin addSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDCommissioneTipo(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativoFin removeSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDCommissioneTipo(null);

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
		siacTSubdoc.setSiacDCommissioneTipo(this);

		return siacTSubdoc;
	}

	public SiacTSubdocFin removeSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacDCommissioneTipo(null);

		return siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.commTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.commTipoId = uid;
	}

}