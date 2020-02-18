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
 * The persistent class for the siac_d_iva_registrazione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_registrazione_tipo")
public class SiacDIvaRegistrazioneTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="reg_tipo_id")
	private Integer regTipoId;

	@Column(name="reg_tipo_code")
	private String regTipoCode;

	@Column(name="reg_tipo_desc")
	private String regTipoDesc;

	//bi-directional many-to-one association to SiacRIvaRegTipoDocFamTipoFin
	@OneToMany(mappedBy="siacDIvaRegistrazioneTipo")
	private List<SiacRIvaRegTipoDocFamTipoFin> siacRIvaRegTipoDocFamTipos;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@OneToMany(mappedBy="siacDIvaRegistrazioneTipo")
	private List<SiacTSubdocIvaFin> siacTSubdocIvas;

	public SiacDIvaRegistrazioneTipoFin() {
	}

	public Integer getRegTipoId() {
		return this.regTipoId;
	}

	public void setRegTipoId(Integer regTipoId) {
		this.regTipoId = regTipoId;
	}

	public String getRegTipoCode() {
		return this.regTipoCode;
	}

	public void setRegTipoCode(String regTipoCode) {
		this.regTipoCode = regTipoCode;
	}

	public String getRegTipoDesc() {
		return this.regTipoDesc;
	}

	public void setRegTipoDesc(String regTipoDesc) {
		this.regTipoDesc = regTipoDesc;
	}

	public List<SiacRIvaRegTipoDocFamTipoFin> getSiacRIvaRegTipoDocFamTipos() {
		return this.siacRIvaRegTipoDocFamTipos;
	}

	public void setSiacRIvaRegTipoDocFamTipos(List<SiacRIvaRegTipoDocFamTipoFin> siacRIvaRegTipoDocFamTipos) {
		this.siacRIvaRegTipoDocFamTipos = siacRIvaRegTipoDocFamTipos;
	}

	public SiacRIvaRegTipoDocFamTipoFin addSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipoFin siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().add(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDIvaRegistrazioneTipo(this);

		return siacRIvaRegTipoDocFamTipo;
	}

	public SiacRIvaRegTipoDocFamTipoFin removeSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipoFin siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().remove(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDIvaRegistrazioneTipo(null);

		return siacRIvaRegTipoDocFamTipo;
	}

	public List<SiacTSubdocIvaFin> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	public void setSiacTSubdocIvas(List<SiacTSubdocIvaFin> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	public SiacTSubdocIvaFin addSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacDIvaRegistrazioneTipo(this);

		return siacTSubdocIva;
	}

	public SiacTSubdocIvaFin removeSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacDIvaRegistrazioneTipo(null);

		return siacTSubdocIva;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.regTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.regTipoId = uid;
	}

}