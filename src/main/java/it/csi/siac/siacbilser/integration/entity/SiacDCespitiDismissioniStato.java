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
@Table(name="siac_d_cespiti_dismissioni_stato")
@NamedQuery(name="SiacDCespitiDismissioniStato.findAll", query="SELECT s FROM SiacDCespitiDismissioniStato s")
public class SiacDCespitiDismissioniStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_d_cespiti_dismissioni_stato_ces_dismissioni_stato_idGENERATOR", allocationSize=1, sequenceName="siac_d_cespiti_dismissioni_stato_ces_dismissioni_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_d_cespiti_dismissioni_stato_ces_dismissioni_stato_idGENERATOR")
	@Column(name="ces_dismissioni_stato_id")
	private Integer cesDismissioniStatoId;

	
	@Column(name="ces_dismissioni_stato_code")
	private String cesDismissioniStatoCode;

	@Column(name="ces_dismissioni_stato_desc")
	private String cesDismissioniStatoDesc;


	
	/**
	 * @return the cesDismissioniStatoId
	 */
	public Integer getCesDismissioniStatoId() {
		return cesDismissioniStatoId;
	}

	/**
	 * @param cesDismissioniStatoId the cesDismissioniStatoId to set
	 */
	public void setCesDismissioniStatoId(Integer cesDismissioniStatoId) {
		this.cesDismissioniStatoId = cesDismissioniStatoId;
	}

	/**
	 * @return the cesDismissioniStatoCode
	 */
	public String getCesDismissioniStatoCode() {
		return cesDismissioniStatoCode;
	}

	/**
	 * @param cesDismissioniStatoCode the cesDismissioniStatoCode to set
	 */
	public void setCesDismissioniStatoCode(String cesDismissioniStatoCode) {
		this.cesDismissioniStatoCode = cesDismissioniStatoCode;
	}

	/**
	 * @return the cesDismissioniStatoDesc
	 */
	public String getCesDismissioniStatoDesc() {
		return cesDismissioniStatoDesc;
	}

	/**
	 * @param cesDismissioniStatoDesc the cesDismissioniStatoDesc to set
	 */
	public void setCesDismissioniStatoDesc(String cesDismissioniStatoDesc) {
		this.cesDismissioniStatoDesc = cesDismissioniStatoDesc;
	}

	@Override
	public Integer getUid() {
		return this.cesDismissioniStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesDismissioniStatoId = uid;
	}

}