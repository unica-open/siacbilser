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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

/**
 * The persistent class for the siac_t_elab_threshold database table.
 * 
 */
@Entity
@Table(name="siac_t_elab_threshold")
public class SiacTElabThreshold extends SiacTBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elthres id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ELAB_THRESHOLD_ELTHRESID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ELAB_THRESHOLD_ELTHRES_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ELAB_THRESHOLD_ELTHRESID_GENERATOR")
	@Column(name="elthres_id")
	private Integer elthresId;

	/** The elthres code. */
	@Column(name="elthres_code")
	private String elthresCode;

	/** The elthres value. */
	@Column(name="elthres_value")
	private Long elthresValue;
	
	/**
	 * Instantiates a new siac t ente proprietario.
	 */
	public SiacTElabThreshold() {
	}

	/**
	 * Gets the elthres id.
	 *
	 * @return the elthres id
	 */
	public Integer getElthresId() {
		return this.elthresId;
	}

	/**
	 * Sets the elthres id.
	 *
	 * @param elthresId the new elthres id
	 */
	public void setElthresId(Integer elthresId) {
		this.elthresId = elthresId;
	}

	/**
	 * Gets the elthres code.
	 * @return the elthresCode
	 */
	public String getElthresCode() {
		return this.elthresCode;
	}

	/**
	 * Sets the elthres code.
	 * @param elthresCode the elthresCode to set
	 */
	public void setElthresCode(String elthresCode) {
		this.elthresCode = elthresCode;
	}

	/**
	 * Gets the elthres value.
	 * @return the elthresValue
	 */
	public Long getElthresValue() {
		return this.elthresValue;
	}

	/**
	 * Sets the elthres value.
	 * @param elthresValue the elthresValue to set
	 */
	public void setElthresValue(Long elthresValue) {
		this.elthresValue = elthresValue;
	}

	@Override
	public Integer getUid() {
		return elthresId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elthresId = uid;
	}

}