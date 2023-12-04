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
 * The persistent class for the siac_t_iva_registro database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_registro")
public class SiacTIvaRegistroFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ivareg_id")
	private Integer ivaregId;

	@Column(name="ivareg_code")
	private String ivaregCode;

	@Column(name="ivareg_desc")
	private String ivaregDesc;

	//bi-directional many-to-one association to SiacRIvaRegistroGruppoFin
	@OneToMany(mappedBy="siacTIvaRegistro")
	private List<SiacRIvaRegistroGruppoFin> siacRIvaRegistroGruppos;

//	//bi-directional many-to-one association to SiacRIvaStampaRegistro
//	@OneToMany(mappedBy="siacTIvaRegistro")
//	private List<SiacRIvaStampaRegistro> siacRIvaStampaRegistros;

//	//bi-directional many-to-one association to SiacDIvaRegistroTipo
//	@ManyToOne
//	@JoinColumn(name="ivareg_tipo_id")
//	private SiacDIvaRegistroTipo siacDIvaRegistroTipo;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@OneToMany(mappedBy="siacTIvaRegistro")
	private List<SiacTSubdocIvaFin> siacTSubdocIvas;

	public SiacTIvaRegistroFin() {
	}

	public Integer getIvaregId() {
		return this.ivaregId;
	}

	public void setIvaregId(Integer ivaregId) {
		this.ivaregId = ivaregId;
	}

	public String getIvaregCode() {
		return this.ivaregCode;
	}

	public void setIvaregCode(String ivaregCode) {
		this.ivaregCode = ivaregCode;
	}

	public String getIvaregDesc() {
		return this.ivaregDesc;
	}

	public void setIvaregDesc(String ivaregDesc) {
		this.ivaregDesc = ivaregDesc;
	}

	public List<SiacRIvaRegistroGruppoFin> getSiacRIvaRegistroGruppos() {
		return this.siacRIvaRegistroGruppos;
	}

	public void setSiacRIvaRegistroGruppos(List<SiacRIvaRegistroGruppoFin> siacRIvaRegistroGruppos) {
		this.siacRIvaRegistroGruppos = siacRIvaRegistroGruppos;
	}

	public SiacRIvaRegistroGruppoFin addSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppoFin siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().add(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaRegistro(this);

		return siacRIvaRegistroGruppo;
	}

	public SiacRIvaRegistroGruppoFin removeSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppoFin siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().remove(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaRegistro(null);

		return siacRIvaRegistroGruppo;
	}

//	public List<SiacRIvaStampaRegistro> getSiacRIvaStampaRegistros() {
//		return this.siacRIvaStampaRegistros;
//	}

//	public void setSiacRIvaStampaRegistros(List<SiacRIvaStampaRegistro> siacRIvaStampaRegistros) {
//		this.siacRIvaStampaRegistros = siacRIvaStampaRegistros;
//	}
//
//	public SiacRIvaStampaRegistro addSiacRIvaStampaRegistro(SiacRIvaStampaRegistro siacRIvaStampaRegistro) {
//		getSiacRIvaStampaRegistros().add(siacRIvaStampaRegistro);
//		siacRIvaStampaRegistro.setSiacTIvaRegistro(this);
//
//		return siacRIvaStampaRegistro;
//	}
//
//	public SiacRIvaStampaRegistro removeSiacRIvaStampaRegistro(SiacRIvaStampaRegistro siacRIvaStampaRegistro) {
//		getSiacRIvaStampaRegistros().remove(siacRIvaStampaRegistro);
//		siacRIvaStampaRegistro.setSiacTIvaRegistro(null);
//
//		return siacRIvaStampaRegistro;
//	}

//	public SiacDIvaRegistroTipo getSiacDIvaRegistroTipo() {
//		return this.siacDIvaRegistroTipo;
//	}
//
//	public void setSiacDIvaRegistroTipo(SiacDIvaRegistroTipo siacDIvaRegistroTipo) {
//		this.siacDIvaRegistroTipo = siacDIvaRegistroTipo;
//	}

	public List<SiacTSubdocIvaFin> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	public void setSiacTSubdocIvas(List<SiacTSubdocIvaFin> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	public SiacTSubdocIvaFin addSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacTIvaRegistro(this);

		return siacTSubdocIva;
	}

	public SiacTSubdocIvaFin removeSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacTIvaRegistro(null);

		return siacTSubdocIva;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ivaregId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ivaregId = uid;
	}

}