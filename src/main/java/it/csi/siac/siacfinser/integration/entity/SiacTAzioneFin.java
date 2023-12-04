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
 * The persistent class for the siac_t_azione database table.
 * 
 */
@Entity
@Table(name="siac_t_azione")
public class SiacTAzioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="azione_id")
	private Integer azioneId;

	@Column(name="azione_code")
	private String azioneCode;

	@Column(name="azione_desc")
	private String azioneDesc;

	@Column(name="azione_tipo_id")
	private Integer azioneTipoId;

	@Column(name="gruppo_azioni_id")
	private Integer gruppoAzioniId;

	private String nomeprocesso;

	private String nometask;

	private String urlapplicazione;

	private String verificauo;

	//bi-directional many-to-one association to SiacRRuoloOpAzioneFin
	@OneToMany(mappedBy="siacTAzione")
	private List<SiacRRuoloOpAzioneFin> siacRRuoloOpAziones;

	public SiacTAzioneFin() {
	}

	public Integer getAzioneId() {
		return this.azioneId;
	}

	public void setAzioneId(Integer azioneId) {
		this.azioneId = azioneId;
	}

	public String getAzioneCode() {
		return this.azioneCode;
	}

	public void setAzioneCode(String azioneCode) {
		this.azioneCode = azioneCode;
	}

	public String getAzioneDesc() {
		return this.azioneDesc;
	}

	public void setAzioneDesc(String azioneDesc) {
		this.azioneDesc = azioneDesc;
	}

	public Integer getAzioneTipoId() {
		return this.azioneTipoId;
	}

	public void setAzioneTipoId(Integer azioneTipoId) {
		this.azioneTipoId = azioneTipoId;
	}

	public Integer getGruppoAzioniId() {
		return this.gruppoAzioniId;
	}

	public void setGruppoAzioniId(Integer gruppoAzioniId) {
		this.gruppoAzioniId = gruppoAzioniId;
	}

	public String getNomeprocesso() {
		return this.nomeprocesso;
	}

	public void setNomeprocesso(String nomeprocesso) {
		this.nomeprocesso = nomeprocesso;
	}

	public String getNometask() {
		return this.nometask;
	}

	public void setNometask(String nometask) {
		this.nometask = nometask;
	}

	public String getUrlapplicazione() {
		return this.urlapplicazione;
	}

	public void setUrlapplicazione(String urlapplicazione) {
		this.urlapplicazione = urlapplicazione;
	}

	public String getVerificauo() {
		return this.verificauo;
	}

	public void setVerificauo(String verificauo) {
		this.verificauo = verificauo;
	}

	public List<SiacRRuoloOpAzioneFin> getSiacRRuoloOpAziones() {
		return this.siacRRuoloOpAziones;
	}

	public void setSiacRRuoloOpAziones(List<SiacRRuoloOpAzioneFin> siacRRuoloOpAziones) {
		this.siacRRuoloOpAziones = siacRRuoloOpAziones;
	}

	public SiacRRuoloOpAzioneFin addSiacRRuoloOpAzione(SiacRRuoloOpAzioneFin siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().add(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacTAzione(this);

		return siacRRuoloOpAzione;
	}

	public SiacRRuoloOpAzioneFin removeSiacRRuoloOpAzione(SiacRRuoloOpAzioneFin siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().remove(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacTAzione(null);

		return siacRRuoloOpAzione;
	}

	@Override
	public Integer getUid() {
		return this.azioneId;
	}

	@Override
	public void setUid(Integer uid) {
		this.azioneId = uid;
	}

}