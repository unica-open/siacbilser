/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_quadro_economico_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_quadro_economico_parte")
@NamedQuery(name="SiacDQuadroEconomicoParte.findAll", query="SELECT s FROM SiacDQuadroEconomicoParte s")
public class SiacDQuadroEconomicoParte extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_QUADRO_ECONOMICO_PARTEID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_QUADRO_ECONOMICO_PARTE_PARTE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_QUADRO_ECONOMICO_PARTEID_GENERATOR")
	@Column(name="parte_id")
	private Integer parteId;
	
	/** The doc parte code. */
	@Column(name="parte_code")
	private String parteCode;

	/** The doc stato desc. */
	@Column(name="parte_desc")
	private String parteDesc;
	
	@OneToMany(mappedBy="siacDQuadroEconomicoParte")
	private List<SiacTQuadroEconomico> siacTQuadroEconomicoList;
	
	
	/**
	 * Instantiates a new siac d doc stato.
	 */
	public SiacDQuadroEconomicoParte() {
	}
	
	/**
	 * @return the parteId
	 */
	public Integer getParteId() {
		return parteId;
	}

	/**
	 * @param parteId the parteId to set
	 */
	public void setParteId(Integer parteId) {
		this.parteId = parteId;
	}

	/**
	 * @return the parteCode
	 */
	public String getParteCode() {
		return parteCode;
	}


	/**
	 * @param parteCode the parteCode to set
	 */
	public void setParteCode(String parteCode) {
		this.parteCode = parteCode;
	}


	/**
	 * @return the parteDesc
	 */
	public String getParteDesc() {
		return parteDesc;
	}

	/**
	 * @param parteDesc the parteDesc to set
	 */
	public void setParteDesc(String parteDesc) {
		this.parteDesc = parteDesc;
	}

	/**
	 * @return the siacTQuadroEconomicoList
	 */
	public List<SiacTQuadroEconomico> getSiacTQuadroEconomicoList() {
		return siacTQuadroEconomicoList;
	}

	/**
	 * @param siacTQuadroEconomicoList the siacTQuadroEconomicoList to set
	 */
	public void setSiacTQuadroEconomicoList(List<SiacTQuadroEconomico> siacTQuadroEconomicoList) {
		this.siacTQuadroEconomicoList = siacTQuadroEconomicoList;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return parteId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.parteId = uid;
		
	}

}