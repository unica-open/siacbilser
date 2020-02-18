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
 * The persistent class for the siac_d_causale_sospensione database table.
 */
@Entity
@Table(name="siac_d_causale_sospensione")
@NamedQuery(name="SiacDCausaleSospensione.findAll", query="SELECT s FROM SiacDCausaleSospensione s")
public class SiacDCausaleSospensione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus sosp id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CAUSALE_TIPO_CAUSTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CAUSALE_TIPO_CAUS_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CAUSALE_TIPO_CAUSTIPOID_GENERATOR")
	@Column(name="caus_sosp_id")
	private Integer causSospId;

	/** The caus sosp code. */
	@Column(name="caus_sosp_code")
	private String causSospCode;

	/** The caus sosp desc. */
	@Column(name="caus_sosp_desc")
	private String causSospDesc;

	/**
	 * Instantiates a new siac d causale sosp.
	 */
	public SiacDCausaleSospensione() {
	}

	/**
	 * @return the causSospId
	 */
	public Integer getCausSospId() {
		return causSospId;
	}

	/**
	 * @param causSospId the causSospId to set
	 */
	public void setCausSospId(Integer causSospId) {
		this.causSospId = causSospId;
	}

	/**
	 * @return the causSospCode
	 */
	public String getCausSospCode() {
		return causSospCode;
	}

	/**
	 * @param causSospCode the causSospCode to set
	 */
	public void setCausSospCode(String causSospCode) {
		this.causSospCode = causSospCode;
	}

	/**
	 * @return the causSospDesc
	 */
	public String getCausSospDesc() {
		return causSospDesc;
	}

	/**
	 * @param causSospDesc the causSospDesc to set
	 */
	public void setCausSospDesc(String causSospDesc) {
		this.causSospDesc = causSospDesc;
	}

	@Override
	public Integer getUid() {
		return causSospId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causSospId = uid;
	}

}