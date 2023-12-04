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

@Entity
@Table(name="siac_d_iva_aliquota_tipo")
@NamedQuery(name="SiacDIvaAliquotaTipo.findAll", query="SELECT s FROM SiacDIvaAliquotaTipo s")
public class SiacDIvaAliquotaTipo extends SiacTEnteBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5062177693736500483L;
	
	/** The ivaaliquota id. */
	@Id
	@SequenceGenerator(name="SIAC_D_IVA_ALIQUOTA_IVAALIQUOTATIPOID_GENERATOR", sequenceName="siac_d_iva_aliquota_tipo_ivaaliquota_tipo_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_ALIQUOTA_IVAALIQUOTATIPOID_GENERATOR")
	@Column(name="ivaaliquota_tipo_id")
	private Integer ivaaliquotaTipoId;
	
	/** The ivaaliquota code. */
	@Column(name="ivaaliquota_tipo_code")
	private String ivaaliquotaTipoCode;

	/** The ivaaliquota desc. */
	@Column(name="ivaaliquota_tipo_desc")
	private String ivaaliquotaTipoDesc;
	

	/**
	 * @return the ivaaliquotaTipoId
	 */
	public Integer getIvaaliquotaTipoId() {
		return ivaaliquotaTipoId;
	}

	/**
	 * @param ivaaliquotaTipoId the ivaaliquotaTipoId to set
	 */
	public void setIvaaliquotaTipoId(Integer ivaaliquotaTipoId) {
		this.ivaaliquotaTipoId = ivaaliquotaTipoId;
	}

	/**
	 * @return the ivaaliquotaTipoCode
	 */
	public String getIvaaliquotaTipoCode() {
		return ivaaliquotaTipoCode;
	}

	/**
	 * @param ivaaliquotaTipoCode the ivaaliquotaTipoCode to set
	 */
	public void setIvaaliquotaTipoCode(String ivaaliquotaTipoCode) {
		this.ivaaliquotaTipoCode = ivaaliquotaTipoCode;
	}

	/**
	 * @return the ivaaliquotaTipoDesc
	 */
	public String getIvaaliquotaTipoDesc() {
		return ivaaliquotaTipoDesc;
	}

	/**
	 * @param ivaaliquotaTipoDesc the ivaaliquotaTipoDesc to set
	 */
	public void setIvaaliquotaTipoDesc(String ivaaliquotaTipoDesc) {
		this.ivaaliquotaTipoDesc = ivaaliquotaTipoDesc;
	}

	@Override
	public Integer getUid() {
		return ivaaliquotaTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivaaliquotaTipoId = uid;
		
	}

}
