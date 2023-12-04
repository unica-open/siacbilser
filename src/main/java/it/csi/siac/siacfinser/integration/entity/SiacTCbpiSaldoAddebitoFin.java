/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;

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

@Entity
@Table(name = "siac_t_cbpi_saldo_addebito")
public class SiacTCbpiSaldoAddebitoFin extends SiacTEnteBase
{
	private static final long serialVersionUID = 1180081530062697556L;

	@Id
	@SequenceGenerator(name = "SIAC_T_CBPI_SALDO_ADDEBITO_CBPISALDOA_ID_GENERATOR", allocationSize = 1, sequenceName = "siac_t_cbpi_saldo_addebito_cbpisaldoa_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIAC_T_CBPI_SALDO_ADDEBITO_CBPISALDOA_ID_GENERATOR")
	@Column(name = "cbpisaldoa_id")
	private Integer uid;

	@Column(name = "cbpisaldoa_data_addebito")
	private Date data;

	@Column(name = "cbpisaldoa_importo_spesa")
	private BigDecimal importoSpesa;

	@Column(name = "cbpisaldoa_importo_prelievo")
	private BigDecimal importoPrelievo;

	@ManyToOne
	@JoinColumn(name = "cbpisaldo_id")
	private SiacTCbpiSaldoFin saldo;

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

	public BigDecimal getImportoSpesa()
	{
		return importoSpesa;
	}

	public void setImportoSpesa(BigDecimal importoSpesa)
	{
		this.importoSpesa = importoSpesa;
	}

	public BigDecimal getImportoPrelievo()
	{
		return importoPrelievo;
	}

	public void setImportoPrelievo(BigDecimal importoPrelievo)
	{
		this.importoPrelievo = importoPrelievo;
	}

	public SiacTCbpiSaldoFin getSaldo()
	{
		return saldo;
	}

	public void setSaldo(SiacTCbpiSaldoFin saldo)
	{
		this.saldo = saldo;
	}

	public Date getData()
	{
		return data;
	}

	public void setData(Date data)
	{
		this.data = data;
	}
}