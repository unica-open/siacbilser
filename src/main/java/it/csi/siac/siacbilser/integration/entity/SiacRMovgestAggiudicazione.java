/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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



/**
 * The persistent class for the siac_r_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_aggiudicazione")
public class SiacRMovgestAggiudicazione extends SiacTEnteBase {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_AGGIUDICAZIONEID_GENERATOR", allocationSize = 1, sequenceName="siac_r_movgest_aggiudicazione_movgest_aggiudicazione_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_AGGIUDICAZIONEID_GENERATOR")
	@Column(name="movgest_aggiudicazione_r_id")
	private Integer movgestAggiudicazioneRId;

	//bi-directional many-to-one association to SiacTMovgest
	@ManyToOne
	@JoinColumn(name="movgest_id_da")
	private SiacTMovgest siacTMovgestDa;
	
	@ManyToOne
	@JoinColumn(name="movgest_id_a")
	private SiacTMovgest siacTMovgestA;
	
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;
	
	@ManyToOne
	@JoinColumn(name="mod_id")
	private SiacTModifica siacTModifica;

	
	
	

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
	public SiacTMovgest getSiacTMovgestDa() {
		return siacTMovgestDa;
	}

	/**
	 * @param siacTMovgestDa the siacTMovgestDa to set
	 */
	public void setSiacTMovgestDa(SiacTMovgest siacTMovgestDa) {
		this.siacTMovgestDa = siacTMovgestDa;
	}

	/**
	 * @return the siacTMovgestA
	 */
	public SiacTMovgest getSiacTMovgestA() {
		return siacTMovgestA;
	}

	/**
	 * @param siacTMovgestA the siacTMovgestA to set
	 */
	public void setSiacTMovgestA(SiacTMovgest siacTMovgestA) {
		this.siacTMovgestA = siacTMovgestA;
	}

	/**
	 * @return the siacTAttoAmm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return siacTAttoAmm;
	}

	/**
	 * @param siacTAttoAmm the siacTAttoAmm to set
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	/**
	 * @return the siacTModifica
	 */
	public SiacTModifica getSiacTModifica() {
		return siacTModifica;
	}

	/**
	 * @param siacTModifica the siacTModifica to set
	 */
	public void setSiacTModifica(SiacTModifica siacTModifica) {
		this.siacTModifica = siacTModifica;
	}

}