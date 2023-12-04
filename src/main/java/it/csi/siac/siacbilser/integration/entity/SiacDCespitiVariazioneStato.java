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
@Table(name="siac_d_cespiti_variazione_stato")
@NamedQuery(name="SiacDCespitiVariazioneStato.findAll", query="SELECT s FROM SiacDCespitiVariazioneStato s")
public class SiacDCespitiVariazioneStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_d_cespiti_variazione_stato_ces_var_stato_idGENERATOR", allocationSize=1, sequenceName="siac_d_cespiti_variazione_stato_ces_var_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_d_cespiti_variazione_stato_ces_var_stato_idGENERATOR")
	@Column(name="ces_var_stato_id")
	private Integer cesVarStatoId;

	
	@Column(name="ces_var_stato_code")
	private String cesVarStatoCode;

	@Column(name="ces_var_stato_desc")
	private String cesVarStatoDesc;


	
	/**
	 * @return the cesVarStatoId
	 */
	public Integer getCesVarStatoId() {
		return cesVarStatoId;
	}

	/**
	 * @param cesVarStatoId the cesVarStatoId to set
	 */
	public void setCesVarStatoId(Integer cesVarStatoId) {
		this.cesVarStatoId = cesVarStatoId;
	}

	/**
	 * @return the cesVarStatoCode
	 */
	public String getCesVarStatoCode() {
		return cesVarStatoCode;
	}

	/**
	 * @param cesVarStatoCode the cesVarStatoCode to set
	 */
	public void setCesVarStatoCode(String cesVarStatoCode) {
		this.cesVarStatoCode = cesVarStatoCode;
	}

	/**
	 * @return the cesVarStatoDesc
	 */
	public String getCesVarStatoDesc() {
		return cesVarStatoDesc;
	}

	/**
	 * @param cesVarStatoDesc the cesVarStatoDesc to set
	 */
	public void setCesVarStatoDesc(String cesVarStatoDesc) {
		this.cesVarStatoDesc = cesVarStatoDesc;
	}

	@Override
	public Integer getUid() {
		return this.cesVarStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesVarStatoId = uid;
	}

}