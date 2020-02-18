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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * 
 */
@Entity
@Table(name = "siac_t_cab")
public class SiacTCabFin extends SiacTEnteBase
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5694785473737221199L;

	@Id
	@SequenceGenerator(name = "SIAC_T_CAB_CAB_ID_GENERATOR", allocationSize = 1, sequenceName = "siac_t_cab_cab_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIAC_T_CAB_CAB_ID_GENERATOR")
	@Column(name = "cab_id")
	private Integer uid;

	@Column(name = "cab_desc")
	private String descrizione;

	@Column(name = "cab_code")
	private String codice;

	@ManyToOne
	@JoinColumn(name = "abi_id")
	private SiacTAbiFin abi;


	public String getDescrizione()
	{
		return descrizione;
	}

	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}

	public String getCodice()
	{
		return codice;
	}

	public void setCodice(String codice)
	{
		this.codice = codice;
	}

	public SiacTAbiFin getAbi()
	{
		return abi;
	}

	public void setAbi(SiacTAbiFin abi)
	{
		this.abi = abi;
	}

	@Override
	public Integer getUid()
	{
		return this.uid;
	}

	@Override
	public void setUid(Integer uid)
	{
		this.uid = uid;
	}
}