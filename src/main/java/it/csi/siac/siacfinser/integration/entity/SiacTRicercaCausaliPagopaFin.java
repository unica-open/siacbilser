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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_ricerca_causali_pagopa database table.
 * 
 */
@Entity
@Table(name="siac_t_ricerca_causali_pagopa")
public class SiacTRicercaCausaliPagopaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="Siac_T_Ricerca_Causali_Pa_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_ricerca_causali_pa_riccaus_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Siac_T_Ricerca_Causali_Pa_ID_GENERATOR")
	@Column(name="riccaus_id")
	private Integer riccausId;

	              
	@Column(name="riccaus_code")
	private String riccausCode;

	@Column(name="riccaus_desc")
	private String riccausDesc;

	public SiacTRicercaCausaliPagopaFin() {
	}


	/**
	 * @return the riccausId
	 */
	public Integer getRiccausId() {
		return riccausId;
	}

	/**
	 * @param riccausId the riccausId to set
	 */
	public void setRiccausId(Integer riccausId) {
		this.riccausId = riccausId;
	}





	/**
	 * @return the riccausCode
	 */
	public String getRiccausCode() {
		return riccausCode;
	}





	/**
	 * @param riccausCode the riccausCode to set
	 */
	public void setRiccausCode(String riccausCode) {
		this.riccausCode = riccausCode;
	}





	/**
	 * @return the riccausDesc
	 */
	public String getRiccausDesc() {
		return riccausDesc;
	}





	/**
	 * @param riccausDesc the riccausDesc to set
	 */
	public void setRiccausDesc(String riccausDesc) {
		this.riccausDesc = riccausDesc;
	}





	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.riccausId;
	}

	@Override
	public void setUid(Integer riccausId) {
		// TODO Auto-generated method stub
		this.riccausId = riccausId;
	}
}