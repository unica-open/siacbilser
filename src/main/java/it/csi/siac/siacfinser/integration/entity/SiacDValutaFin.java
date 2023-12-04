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
 * The persistent class for the siac_d_valuta database table.
 * 
 */
@Entity
@Table(name="siac_d_valuta")
public class SiacDValutaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="valuta_id")
	private Integer valutaId;

	@Column(name="valuta_code")
	private String valutaCode;

	@Column(name="valuta_desc")
	private String valutaDesc;

	//bi-directional many-to-one association to SiacTCartacontEsteraFin
	@OneToMany(mappedBy="siacDValuta")
	private List<SiacTCartacontEsteraFin> siacTCartacontEsteras;

	// bi-directional many-to-one association to SiacTSubdocIvaFin
	// @OneToMany(mappedBy="siacDValuta")
	// private List<SiacTSubdocIvaFin> siacTSubdocIvas;

	public SiacDValutaFin() {
	}

	public Integer getValutaId() {
		return this.valutaId;
	}

	public void setValutaId(Integer valutaId) {
		this.valutaId = valutaId;
	}

	public String getValutaCode() {
		return this.valutaCode;
	}

	public void setValutaCode(String valutaCode) {
		this.valutaCode = valutaCode;
	}

	public String getValutaDesc() {
		return this.valutaDesc;
	}

	public void setValutaDesc(String valutaDesc) {
		this.valutaDesc = valutaDesc;
	}

	public List<SiacTCartacontEsteraFin> getSiacTCartacontEsteras() {
		return this.siacTCartacontEsteras;
	}

	public void setSiacTCartacontEsteras(List<SiacTCartacontEsteraFin> siacTCartacontEsteras) {
		this.siacTCartacontEsteras = siacTCartacontEsteras;
	}

	public SiacTCartacontEsteraFin addSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		getSiacTCartacontEsteras().add(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDValuta(this);

		return siacTCartacontEstera;
	}

	public SiacTCartacontEsteraFin removeSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		getSiacTCartacontEsteras().remove(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDValuta(null);

		return siacTCartacontEstera;
	}

	// public List<SiacTSubdocIvaFin> getSiacTSubdocIvas() {
	//	return this.siacTSubdocIvas;
	// }

	// public void setSiacTSubdocIvas(List<SiacTSubdocIvaFin> siacTSubdocIvas) {
	//	this.siacTSubdocIvas = siacTSubdocIvas;
	// }

	// public SiacTSubdocIvaFin addSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
	//	getSiacTSubdocIvas().add(siacTSubdocIva);
	//	siacTSubdocIva.setSiacDValuta(this);
	//	return siacTSubdocIva;
	// }

	// public SiacTSubdocIvaFin removeSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
	//	getSiacTSubdocIvas().remove(siacTSubdocIva);
	//	siacTSubdocIva.setSiacDValuta(null);
	//	return siacTSubdocIva;
	// }

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.valutaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.valutaId = uid;
	}

}