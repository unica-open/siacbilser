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
 * The persistent class for the siac_t_eldoc database table.
 * 
 */
@Entity
@Table(name="siac_t_abi")
@NamedQuery(name="SiacTAbi.findAll", query="SELECT a FROM SiacTAbi a")
public class SiacTAbi extends SiacTEnteBase {
	
	
	private static final long serialVersionUID = -7304164099954208804L;


	@Id
	@SequenceGenerator(name="SIAC_T_ABI_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ABI_ABI_ID_SEQ") 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ABI_GENERATOR")
	@Column(name="abi_id")
	private Integer abiId;
	
	@Column(name="abi_code")
	private String abiCode;
	
	@Column(name="abi_desc")
	private String abiDesc;
	

	/**
	 * Instantiates a new siac t doc num.
	 */
	public SiacTAbi() {
	}

	/**
	 * @return the abiId
	 */
	public Integer getAbiId() {
		return abiId;
	}

	/**
	 * @param abiId the abiId to set
	 */
	public void setAbiId(Integer abiId) {
		this.abiId = abiId;
	}

	/**
	 * @return the abiCode
	 */
	public String getAbiCode() {
		return abiCode;
	}

	/**
	 * @param abiCode the abiCode to set
	 */
	public void setAbiCode(String abiCode) {
		this.abiCode = abiCode;
	}

	/**
	 * @return the abiDesc
	 */
	public String getAbiDesc() {
		return abiDesc;
	}

	/**
	 * @param abiDesc the abiDesc to set
	 */
	public void setAbiDesc(String abiDesc) {
		this.abiDesc = abiDesc;
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return abiId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.abiId = uid;		
	}


}