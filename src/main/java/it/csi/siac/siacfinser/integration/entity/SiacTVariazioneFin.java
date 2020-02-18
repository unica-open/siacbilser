/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_variazione database table.
 * 
 */
@Entity
@Table(name="siac_t_variazione")
public class SiacTVariazioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_VARIAZIONE_VARIAZIONE_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_variazione_variazione_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_VARIAZIONE_VARIAZIONE_ID_GENERATOR")
	@Column(name="variazione_id")
	private Integer variazioneId;

	@Column(name="variazione_data")
	private Timestamp variazioneData;

	@Column(name="variazione_desc")
	private String variazioneDesc;

	@Column(name="variazione_num")
	private Integer variazioneNum;

	//bi-directional many-to-one association to SiacRVariazioneAttrFin
	@OneToMany(mappedBy="siacTVariazione")
	private List<SiacRVariazioneAttrFin> siacRVariazioneAttrs;

	//bi-directional many-to-one association to SiacRVariazioneStatoFin
	@OneToMany(mappedBy="siacTVariazione")
	private List<SiacRVariazioneStatoFin> siacRVariazioneStatos;

	//bi-directional many-to-one association to SiacDVariazioneTipoFin
	@ManyToOne
	@JoinColumn(name="variazione_tipo_id")
	private SiacDVariazioneTipoFin siacDVariazioneTipo;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	public SiacTVariazioneFin() {
	}

	public Integer getVariazioneId() {
		return this.variazioneId;
	}

	public void setVariazioneId(Integer variazioneId) {
		this.variazioneId = variazioneId;
	}

	public Timestamp getVariazioneData() {
		return this.variazioneData;
	}

	public void setVariazioneData(Timestamp variazioneData) {
		this.variazioneData = variazioneData;
	}

	public String getVariazioneDesc() {
		return this.variazioneDesc;
	}

	public void setVariazioneDesc(String variazioneDesc) {
		this.variazioneDesc = variazioneDesc;
	}

	public Integer getVariazioneNum() {
		return this.variazioneNum;
	}

	public void setVariazioneNum(Integer variazioneNum) {
		this.variazioneNum = variazioneNum;
	}

	public List<SiacRVariazioneAttrFin> getSiacRVariazioneAttrs() {
		return this.siacRVariazioneAttrs;
	}

	public void setSiacRVariazioneAttrs(List<SiacRVariazioneAttrFin> siacRVariazioneAttrs) {
		this.siacRVariazioneAttrs = siacRVariazioneAttrs;
	}

	public SiacRVariazioneAttrFin addSiacRVariazioneAttr(SiacRVariazioneAttrFin siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().add(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTVariazione(this);

		return siacRVariazioneAttr;
	}

	public SiacRVariazioneAttrFin removeSiacRVariazioneAttr(SiacRVariazioneAttrFin siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().remove(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTVariazione(null);

		return siacRVariazioneAttr;
	}

	public List<SiacRVariazioneStatoFin> getSiacRVariazioneStatos() {
		return this.siacRVariazioneStatos;
	}

	public void setSiacRVariazioneStatos(List<SiacRVariazioneStatoFin> siacRVariazioneStatos) {
		this.siacRVariazioneStatos = siacRVariazioneStatos;
	}

	public SiacRVariazioneStatoFin addSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		getSiacRVariazioneStatos().add(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTVariazione(this);

		return siacRVariazioneStato;
	}

	public SiacRVariazioneStatoFin removeSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		getSiacRVariazioneStatos().remove(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTVariazione(null);

		return siacRVariazioneStato;
	}

	public SiacDVariazioneTipoFin getSiacDVariazioneTipo() {
		return this.siacDVariazioneTipo;
	}

	public void setSiacDVariazioneTipo(SiacDVariazioneTipoFin siacDVariazioneTipo) {
		this.siacDVariazioneTipo = siacDVariazioneTipo;
	}

	public SiacTBilFin getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.variazioneId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.variazioneId = uid;
	}
}