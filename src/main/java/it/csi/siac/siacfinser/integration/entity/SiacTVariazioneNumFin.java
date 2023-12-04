/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_variazione_num database table.
 * 
 */
@Entity
@Table(name="siac_t_variazione_num")
public class SiacTVariazioneNumFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_VARIAZIONE_NUM_VARIAZIONE_NUM_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_variazione_num_variazione_num_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_VARIAZIONE_NUM_VARIAZIONE_NUM_ID_GENERATOR")
	@Column(name="variazione_num_id")
	private Integer variazioneNumId;

	@Column(name="variazione_num")
	private Integer variazioneNum;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	public SiacTVariazioneNumFin() {
	}

	public Integer getVariazioneNumId() {
		return this.variazioneNumId;
	}

	public void setVariazioneNumId(Integer variazioneNumId) {
		this.variazioneNumId = variazioneNumId;
	}

	public Integer getVariazioneNum() {
		return this.variazioneNum;
	}

	public void setVariazioneNum(Integer variazioneNum) {
		this.variazioneNum = variazioneNum;
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
		return this.variazioneNumId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.variazioneNumId = uid;
	}
}