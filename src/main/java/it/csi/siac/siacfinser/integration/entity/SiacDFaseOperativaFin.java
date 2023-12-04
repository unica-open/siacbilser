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
 * The persistent class for the siac_d_fase_operativa database table.
 * 
 */
@Entity
@Table(name="siac_d_fase_operativa")
public class SiacDFaseOperativaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="fase_operativa_id")
	private Integer faseOperativaId;

	@Column(name="fase_operativa_code")
	private String faseOperativaCode;

	@Column(name="fase_operativa_desc")
	private String faseOperativaDesc;

	//bi-directional many-to-one association to SiacRFaseOperativaBilStatoFin
	@OneToMany(mappedBy="siacDFaseOperativa")
	private List<SiacRFaseOperativaBilStatoFin> siacRFaseOperativaBilStatos;

	public SiacDFaseOperativaFin() {
	}

	public Integer getFaseOperativaId() {
		return this.faseOperativaId;
	}

	public void setFaseOperativaId(Integer faseOperativaId) {
		this.faseOperativaId = faseOperativaId;
	}

	public String getFaseOperativaCode() {
		return this.faseOperativaCode;
	}

	public void setFaseOperativaCode(String faseOperativaCode) {
		this.faseOperativaCode = faseOperativaCode;
	}

	public String getFaseOperativaDesc() {
		return this.faseOperativaDesc;
	}

	public void setFaseOperativaDesc(String faseOperativaDesc) {
		this.faseOperativaDesc = faseOperativaDesc;
	}

	public List<SiacRFaseOperativaBilStatoFin> getSiacRFaseOperativaBilStatos() {
		return this.siacRFaseOperativaBilStatos;
	}

	public void setSiacRFaseOperativaBilStatos(List<SiacRFaseOperativaBilStatoFin> siacRFaseOperativaBilStatos) {
		this.siacRFaseOperativaBilStatos = siacRFaseOperativaBilStatos;
	}

	public SiacRFaseOperativaBilStatoFin addSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStatoFin siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().add(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDFaseOperativa(this);

		return siacRFaseOperativaBilStato;
	}

	public SiacRFaseOperativaBilStatoFin removeSiacRFaseOperativaBilStato(SiacRFaseOperativaBilStatoFin siacRFaseOperativaBilStato) {
		getSiacRFaseOperativaBilStatos().remove(siacRFaseOperativaBilStato);
		siacRFaseOperativaBilStato.setSiacDFaseOperativa(null);

		return siacRFaseOperativaBilStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.faseOperativaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.faseOperativaId = uid;
	}
}