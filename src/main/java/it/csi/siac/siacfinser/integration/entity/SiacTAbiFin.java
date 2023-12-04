/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * 
 */
@Entity
@Table(name = "siac_t_abi")
public class SiacTAbiFin extends SiacTEnteBase
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5694785473737221199L;

	@Id
	@SequenceGenerator(name = "SIAC_T_ABI_ABI_ID_GENERATOR", allocationSize = 1, sequenceName = "siac_t_abi_abi_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIAC_T_ABI_ABI_ID_GENERATOR")
	@Column(name = "abi_id")
	private Integer uid;

	@Column(name = "abi_desc")
	private String descrizione;

	@Column(name = "abi_code")
	private String codice;

	

	@OneToMany(mappedBy = "abi")
	private List<SiacTCabFin> elencoCab;



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

	public List<SiacTCabFin> getElencoCab()
	{
		return elencoCab;
	}

	public void setElencoCab(List<SiacTCabFin> elencoCab)
	{
		this.elencoCab = elencoCab;
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