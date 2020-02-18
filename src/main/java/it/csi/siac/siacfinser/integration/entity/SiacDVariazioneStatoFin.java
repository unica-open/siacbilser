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
 * The persistent class for the siac_d_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_variazione_stato")
public class SiacDVariazioneStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="variazione_stato_tipo_id")
	private Integer variazioneStatoTipoId;

	@Column(name="variazione_stato_tipo_code")
	private String variazioneStatoTipoCode;

	@Column(name="variazione_stato_tipo_desc")
	private String variazioneStatoTipoDesc;

	//bi-directional many-to-one association to SiacRVariazioneStatoFin
	@OneToMany(mappedBy="siacDVariazioneStato")
	private List<SiacRVariazioneStatoFin> siacRVariazioneStatos;

	public SiacDVariazioneStatoFin() {
	}

	public Integer getVariazioneStatoTipoId() {
		return this.variazioneStatoTipoId;
	}

	public void setVariazioneStatoTipoId(Integer variazioneStatoTipoId) {
		this.variazioneStatoTipoId = variazioneStatoTipoId;
	}

	public String getVariazioneStatoTipoCode() {
		return this.variazioneStatoTipoCode;
	}

	public void setVariazioneStatoTipoCode(String variazioneStatoTipoCode) {
		this.variazioneStatoTipoCode = variazioneStatoTipoCode;
	}

	public String getVariazioneStatoTipoDesc() {
		return this.variazioneStatoTipoDesc;
	}

	public void setVariazioneStatoTipoDesc(String variazioneStatoTipoDesc) {
		this.variazioneStatoTipoDesc = variazioneStatoTipoDesc;
	}

	public List<SiacRVariazioneStatoFin> getSiacRVariazioneStatos() {
		return this.siacRVariazioneStatos;
	}

	public void setSiacRVariazioneStatos(List<SiacRVariazioneStatoFin> siacRVariazioneStatos) {
		this.siacRVariazioneStatos = siacRVariazioneStatos;
	}

	public SiacRVariazioneStatoFin addSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		getSiacRVariazioneStatos().add(siacRVariazioneStato);
		siacRVariazioneStato.setSiacDVariazioneStato(this);

		return siacRVariazioneStato;
	}

	public SiacRVariazioneStatoFin removeSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		getSiacRVariazioneStatos().remove(siacRVariazioneStato);
		siacRVariazioneStato.setSiacDVariazioneStato(null);

		return siacRVariazioneStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.variazioneStatoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.variazioneStatoTipoId = uid;
	}
}