/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_r_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_aggiudicazione")
public class SiacRMovgestAggiudicazioneFin extends SiacTEnteBase {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3112042047157492007L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_AGGIUDICAZIONEID_GENERATOR", allocationSize = 1, sequenceName="siac_r_movgest_aggiudicazione_movgest_aggiudicazione_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_AGGIUDICAZIONEID_GENERATOR")
	@Column(name="movgest_aggiudicazione_r_id")
	private Integer movgestAggiudicazioneRId;

	//bi-directional many-to-one association to SiacTMovgestFin
	@ManyToOne
	@JoinColumn(name="movgest_id_da")
	private SiacTMovgestFin siacTMovgestDa;
	
	@ManyToOne
	@JoinColumn(name="movgest_id_a")
	private SiacTMovgestFin siacTMovgestA;
	
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;
	
	@ManyToOne
	@JoinColumn(name="mod_id")
	private SiacTModificaFin siacTModifica;

	
	
	

	@Override
	public Integer getUid() {
		return movgestAggiudicazioneRId;
	}

	@Override
	public void setUid(Integer uid) {
		movgestAggiudicazioneRId = uid;
	}

	/**
	 * @return the movgestAggiudicazioneRId
	 */
	public Integer getMovgestAggiudicazioneRId() {
		return movgestAggiudicazioneRId;
	}

	/**
	 * @param movgestAggiudicazioneRId the movgestAggiudicazioneRId to set
	 */
	public void setMovgestAggiudicazioneRId(Integer movgestAggiudicazioneRId) {
		this.movgestAggiudicazioneRId = movgestAggiudicazioneRId;
	}

	/**
	 * @return the siacTMovgestDa
	 */
	public SiacTMovgestFin getSiacTMovgestDa() {
		return siacTMovgestDa;
	}

	/**
	 * @param siacTMovgestDa the siacTMovgestDa to set
	 */
	public void setSiacTMovgestDa(SiacTMovgestFin siacTMovgestDa) {
		this.siacTMovgestDa = siacTMovgestDa;
	}

	/**
	 * @return the siacTMovgestA
	 */
	public SiacTMovgestFin getSiacTMovgestA() {
		return siacTMovgestA;
	}

	/**
	 * @param siacTMovgestA the siacTMovgestA to set
	 */
	public void setSiacTMovgestA(SiacTMovgestFin siacTMovgestA) {
		this.siacTMovgestA = siacTMovgestA;
	}

	/**
	 * @return the siacTAttoAmm
	 */
	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return siacTAttoAmm;
	}

	/**
	 * @param siacTAttoAmm the siacTAttoAmm to set
	 */
	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	/**
	 * @return the siacTModifica
	 */
	public SiacTModificaFin getSiacTModifica() {
		return siacTModifica;
	}

	/**
	 * @param siacTModifica the siacTModifica to set
	 */
	public void setSiacTModifica(SiacTModificaFin siacTModifica) {
		this.siacTModifica = siacTModifica;
	}

}