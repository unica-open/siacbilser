/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_d_cespiti_classificazione_giuridica")
@NamedQuery(name="SiacDCespitiClassificazioneGiuridica.findAll", query="SELECT s FROM SiacDCespitiClassificazioneGiuridica s")
public class SiacDCespitiClassificazioneGiuridica extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CESPITI_CLASSIFICAZIONE_GIURIDICAID_GENERATOR", allocationSize=1, sequenceName="siac_d_cespiti_classificazione_giuridica_ces_class_giu_id_seq" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CESPITI_CLASSIFICAZIONE_GIURIDICAID_GENERATOR")

	@Column(name="ces_class_giu_id")
	private Integer cesClassGiuId;

	@Column(name="ces_class_giu_code")
	private String cesClassGiuCode;

	@Column(name="ces_class_giu_desc")
	private String cesClassGiuDesc;

	/**
	 * @return the cesClassGiuId
	 */
	public Integer getCesClassGiuId() {
		return cesClassGiuId;
	}

	/**
	 * @param cesClassGiuId the cesClassGiuId to set
	 */
	public void setCesClassGiuId(Integer cesClassGiuId) {
		this.cesClassGiuId = cesClassGiuId;
	}

	/**
	 * @return the cesClassGiuCode
	 */
	public String getCesClassGiuCode() {
		return cesClassGiuCode;
	}

	/**
	 * @param cesClassGiuCode the cesClassGiuCode to set
	 */
	public void setCesClassGiuCode(String cesClassGiuCode) {
		this.cesClassGiuCode = cesClassGiuCode;
	}

	/**
	 * @return the cesClassGiuDesc
	 */
	public String getCesClassGiuDesc() {
		return cesClassGiuDesc;
	}

	/**
	 * @param cesClassGiuDesc the cesClassGiuDesc to set
	 */
	public void setCesClassGiuDesc(String cesClassGiuDesc) {
		this.cesClassGiuDesc = cesClassGiuDesc;
	}

	@Override
	public Integer getUid() {
		return this.cesClassGiuId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesClassGiuId = uid;
	}

}