/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_r_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_variazione_stato")
public class SiacRVariazioneStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="variazione_stato_id")
	private Integer variazioneStatoId;

	//bi-directional many-to-one association to SiacDVariazioneStatoFin
	@ManyToOne
	@JoinColumn(name="variazione_stato_tipo_id")
	private SiacDVariazioneStatoFin siacDVariazioneStato;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	//bi-directional many-to-one association to SiacTVariazioneFin
	@ManyToOne
	@JoinColumn(name="variazione_id")
	private SiacTVariazioneFin siacTVariazione;

	public SiacRVariazioneStatoFin() {
	}

	public Integer getVariazioneStatoId() {
		return this.variazioneStatoId;
	}

	public void setVariazioneStatoId(Integer variazioneStatoId) {
		this.variazioneStatoId = variazioneStatoId;
	}

	public SiacDVariazioneStatoFin getSiacDVariazioneStato() {
		return this.siacDVariazioneStato;
	}

	public void setSiacDVariazioneStato(SiacDVariazioneStatoFin siacDVariazioneStato) {
		this.siacDVariazioneStato = siacDVariazioneStato;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	public SiacTVariazioneFin getSiacTVariazione() {
		return this.siacTVariazione;
	}

	public void setSiacTVariazione(SiacTVariazioneFin siacTVariazione) {
		this.siacTVariazione = siacTVariazione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.variazioneStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.variazioneStatoId = uid;
	}
}