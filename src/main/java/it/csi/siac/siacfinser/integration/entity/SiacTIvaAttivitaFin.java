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
 * The persistent class for the siac_t_iva_attivita database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_attivita")
public class SiacTIvaAttivitaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ivaatt_id")
	private Integer ivaattId;

	@Column(name="ivaatt_code")
	private String ivaattCode;

	@Column(name="ivaatt_desc")
	private String ivaattDesc;

//	//bi-directional many-to-one association to SiacRBilElemIvaAttivita
//	@OneToMany(mappedBy="siacTIvaAttivita")
//	private List<SiacRBilElemIvaAttivita> siacRBilElemIvaAttivitas;
//
//	//bi-directional many-to-one association to SiacRIvaAttAttr
//	@OneToMany(mappedBy="siacTIvaAttivita")
//	private List<SiacRIvaAttAttr> siacRIvaAttAttrs;
//
//	//bi-directional many-to-one association to SiacRIvaGruppoAttivita
//	@OneToMany(mappedBy="siacTIvaAttivita")
//	private List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@OneToMany(mappedBy="siacTIvaAttivita")
	private List<SiacTSubdocIvaFin> siacTSubdocIvas;

	public SiacTIvaAttivitaFin() {
	}

	public Integer getIvaattId() {
		return this.ivaattId;
	}

	public void setIvaattId(Integer ivaattId) {
		this.ivaattId = ivaattId;
	}

	public String getIvaattCode() {
		return this.ivaattCode;
	}

	public void setIvaattCode(String ivaattCode) {
		this.ivaattCode = ivaattCode;
	}

	public String getIvaattDesc() {
		return this.ivaattDesc;
	}

	public void setIvaattDesc(String ivaattDesc) {
		this.ivaattDesc = ivaattDesc;
	}

//	public List<SiacRBilElemIvaAttivita> getSiacRBilElemIvaAttivitas() {
//		return this.siacRBilElemIvaAttivitas;
//	}
//
//	public void setSiacRBilElemIvaAttivitas(List<SiacRBilElemIvaAttivita> siacRBilElemIvaAttivitas) {
//		this.siacRBilElemIvaAttivitas = siacRBilElemIvaAttivitas;
//	}
//
//	public SiacRBilElemIvaAttivita addSiacRBilElemIvaAttivita(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita) {
//		getSiacRBilElemIvaAttivitas().add(siacRBilElemIvaAttivita);
//		siacRBilElemIvaAttivita.setSiacTIvaAttivita(this);
//
//		return siacRBilElemIvaAttivita;
//	}
//
//	public SiacRBilElemIvaAttivita removeSiacRBilElemIvaAttivita(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita) {
//		getSiacRBilElemIvaAttivitas().remove(siacRBilElemIvaAttivita);
//		siacRBilElemIvaAttivita.setSiacTIvaAttivita(null);
//
//		return siacRBilElemIvaAttivita;
//	}

//	public List<SiacRIvaAttAttr> getSiacRIvaAttAttrs() {
//		return this.siacRIvaAttAttrs;
//	}
//
//	public void setSiacRIvaAttAttrs(List<SiacRIvaAttAttr> siacRIvaAttAttrs) {
//		this.siacRIvaAttAttrs = siacRIvaAttAttrs;
//	}
//
//	public SiacRIvaAttAttr addSiacRIvaAttAttr(SiacRIvaAttAttr siacRIvaAttAttr) {
//		getSiacRIvaAttAttrs().add(siacRIvaAttAttr);
//		siacRIvaAttAttr.setSiacTIvaAttivita(this);
//
//		return siacRIvaAttAttr;
//	}
//
//	public SiacRIvaAttAttr removeSiacRIvaAttAttr(SiacRIvaAttAttr siacRIvaAttAttr) {
//		getSiacRIvaAttAttrs().remove(siacRIvaAttAttr);
//		siacRIvaAttAttr.setSiacTIvaAttivita(null);
//
//		return siacRIvaAttAttr;
//	}

//	public List<SiacRIvaGruppoAttivita> getSiacRIvaGruppoAttivitas() {
//		return this.siacRIvaGruppoAttivitas;
//	}
//
//	public void setSiacRIvaGruppoAttivitas(List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas) {
//		this.siacRIvaGruppoAttivitas = siacRIvaGruppoAttivitas;
//	}
//
//	public SiacRIvaGruppoAttivita addSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
//		getSiacRIvaGruppoAttivitas().add(siacRIvaGruppoAttivita);
//		siacRIvaGruppoAttivita.setSiacTIvaAttivita(this);
//
//		return siacRIvaGruppoAttivita;
//	}
//
//	public SiacRIvaGruppoAttivita removeSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
//		getSiacRIvaGruppoAttivitas().remove(siacRIvaGruppoAttivita);
//		siacRIvaGruppoAttivita.setSiacTIvaAttivita(null);
//
//		return siacRIvaGruppoAttivita;
//	}


	public List<SiacTSubdocIvaFin> getSiacTSubdocIvas() {
		return this.siacTSubdocIvas;
	}

	public void setSiacTSubdocIvas(List<SiacTSubdocIvaFin> siacTSubdocIvas) {
		this.siacTSubdocIvas = siacTSubdocIvas;
	}

	public SiacTSubdocIvaFin addSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().add(siacTSubdocIva);
		siacTSubdocIva.setSiacTIvaAttivita(this);

		return siacTSubdocIva;
	}

	public SiacTSubdocIvaFin removeSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		getSiacTSubdocIvas().remove(siacTSubdocIva);
		siacTSubdocIva.setSiacTIvaAttivita(null);

		return siacTSubdocIva;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ivaattId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ivaattId = uid;
	}

}