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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * 
 */
@Entity
@Table(name = "siac_t_sepa")
public class SiacTSepaFin extends SiacTEnteBase
{
	private static final long serialVersionUID = -3693771536427429947L;

	@Id
	@SequenceGenerator(name = "SIAC_T_SEPA_SEPA_ID_GENERATOR", allocationSize = 1, sequenceName = "siac_t_sepa_sepa_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIAC_T_SEPA_SEPA_ID_GENERATOR")
	@Column(name = "sepa_id")
	private Integer uid;

	@Column(name = "sepa_iso_code")
	private String codiceIsoNazione;

	@Column(name = "sepa_iban_length")
	private Integer lunghezzaIban;

	public String getCodiceIsoNazione()
	{
		return codiceIsoNazione;
	}

	public void setCodiceIsoNazione(String codiceIsoNazione)
	{
		this.codiceIsoNazione = codiceIsoNazione;
	}

	public Integer getLunghezzaIban()
	{
		return lunghezzaIban;
	}

	public void setLunghezzaIban(Integer lunghezzaIban)
	{
		this.lunghezzaIban = lunghezzaIban;
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