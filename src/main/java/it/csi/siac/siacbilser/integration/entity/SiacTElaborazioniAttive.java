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


/**
 * The persistent class for the siac_t_elaborazioni_asincrone database table.
 * 
 */
@Entity
@Table(name="siac_t_elaborazioni_attive")
public class SiacTElaborazioniAttive extends SiacTEnteBase {
	
	private static final long serialVersionUID = -7304164099954208804L;

	@Id
	@SequenceGenerator(name="SIAC_T_ELABORAZIONI_ASINCRONE_GENERATOR", allocationSize=1, sequenceName="siac_t_elaborazioni_attive_elab_id_seq" ) 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ELABORAZIONI_ASINCRONE_GENERATOR")
	@Column(name="elab_id")
	private Integer elabId;
	
	@Column(name="elab_service")
	private String elabService;
	
	@Column(name="elab_key")
	private String elabKey;

	/**
	 * Instantiates a new siac t doc num.
	 */
	public SiacTElaborazioniAttive() {
	}

	/**
	 * @return the elabId
	 */
	public Integer getElabId() {
		return elabId;
	}

	/**
	 * @param elabId the elabId to set
	 */
	public void setElabId(Integer elabId) {
		this.elabId = elabId;
	}

	/**
	 * @return the elabService
	 */
	public String getElabService() {
		return elabService;
	}

	/**
	 * @param elabService the elabService to set
	 */
	public void setElabService(String elabService) {
		this.elabService = elabService;
	}

	/**
	 * @return the elabKey
	 */
	public String getElabKey() {
		return elabKey;
	}

	/**
	 * @param elabKey the elabKey to set
	 */
	public void setElabKey(String elabKey) {
		this.elabKey = elabKey;
	}

	@Override
	public Integer getUid() {
		return elabId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elabId = uid;		
	}


}