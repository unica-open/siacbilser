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
 * The persistent class for the siac_d_variazione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_variazione_tipo")
public class SiacDVariazioneTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="variazione_tipo_id")
	private Integer variazioneTipoId;

	@Column(name="variazione_tipo_code")
	private String variazioneTipoCode;

	@Column(name="variazione_tipo_desc")
	private String variazioneTipoDesc;

	//bi-directional many-to-one association to SiacTVariazioneFin
	@OneToMany(mappedBy="siacDVariazioneTipo")
	private List<SiacTVariazioneFin> siacTVariaziones;

	public SiacDVariazioneTipoFin() {
	}

	public Integer getVariazioneTipoId() {
		return this.variazioneTipoId;
	}

	public void setVariazioneTipoId(Integer variazioneTipoId) {
		this.variazioneTipoId = variazioneTipoId;
	}

	public String getVariazioneTipoCode() {
		return this.variazioneTipoCode;
	}

	public void setVariazioneTipoCode(String variazioneTipoCode) {
		this.variazioneTipoCode = variazioneTipoCode;
	}

	public String getVariazioneTipoDesc() {
		return this.variazioneTipoDesc;
	}

	public void setVariazioneTipoDesc(String variazioneTipoDesc) {
		this.variazioneTipoDesc = variazioneTipoDesc;
	}

	public List<SiacTVariazioneFin> getSiacTVariaziones() {
		return this.siacTVariaziones;
	}

	public void setSiacTVariaziones(List<SiacTVariazioneFin> siacTVariaziones) {
		this.siacTVariaziones = siacTVariaziones;
	}

	public SiacTVariazioneFin addSiacTVariazione(SiacTVariazioneFin siacTVariazione) {
		getSiacTVariaziones().add(siacTVariazione);
		siacTVariazione.setSiacDVariazioneTipo(this);

		return siacTVariazione;
	}

	public SiacTVariazioneFin removeSiacTVariazione(SiacTVariazioneFin siacTVariazione) {
		getSiacTVariaziones().remove(siacTVariazione);
		siacTVariazione.setSiacDVariazioneTipo(null);

		return siacTVariazione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.variazioneTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.variazioneTipoId = uid;
	}
}