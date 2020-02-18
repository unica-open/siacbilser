/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_t_parametro_azione_richiesta database table.
 * 
 */
@Entity
@Table(name="siac_t_parametro_azione_richiesta")
public class SiacTParametroAzioneRichiestaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="parametro_id")
	private Integer parametroId;

	@Column(name="azione_richiesta_id")
	private Integer azioneRichiestaId;

	private String nome;

	private String valore;

	public SiacTParametroAzioneRichiestaFin() {
	}

	public Integer getParametroId() {
		return this.parametroId;
	}

	public void setParametroId(Integer parametroId) {
		this.parametroId = parametroId;
	}

	public Integer getAzioneRichiestaId() {
		return this.azioneRichiestaId;
	}

	public void setAzioneRichiestaId(Integer azioneRichiestaId) {
		this.azioneRichiestaId = azioneRichiestaId;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.parametroId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.parametroId = uid;
	}
}