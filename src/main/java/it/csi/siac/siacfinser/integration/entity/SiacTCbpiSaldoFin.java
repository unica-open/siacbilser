/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

@Entity
@Table(name = "siac_t_cbpi_saldo")
public class SiacTCbpiSaldoFin extends SiacTEnteBase 
{
	private static final long serialVersionUID = 8809014172239534541L;

	@Id
	@SequenceGenerator(name = "SIAC_T_CBPI_SALDO_CBPISALDO_ID_GENERATOR", allocationSize = 1, sequenceName = "siac_t_cbpi_saldo_cbpisaldo_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIAC_T_CBPI_SALDO_CBPISALDO_ID_GENERATOR")
	@Column(name = "cbpisaldo_id")
	private Integer uid;

	@ManyToOne
	@JoinColumn(name = "bil_id")
	private SiacTBil bilancio;

	@Column(name = "cbpisaldo_importo_iniziale")
	private BigDecimal saldoIniziale;

	@ManyToOne
	@JoinColumn(name = "classif_id")
	private SiacTClassFin contoCorrente;

	@OneToMany(mappedBy = "saldo")
	private List<SiacTCbpiSaldoAddebitoFin> addebiti;

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

	public BigDecimal getSaldoIniziale()
	{
		return saldoIniziale;
	}

	public void setSaldoIniziale(BigDecimal saldoIniziale)
	{
		this.saldoIniziale = saldoIniziale;
	}

	public SiacTClassFin getContoCorrente()
	{
		return contoCorrente;
	}

	public void setContoCorrente(SiacTClassFin contoCorrente)
	{
		this.contoCorrente = contoCorrente;
	}

	public List<SiacTCbpiSaldoAddebitoFin> getAddebiti()
	{
		return addebiti;
	}

	public void setAddebiti(List<SiacTCbpiSaldoAddebitoFin> addebiti)
	{
		this.addebiti = addebiti;
	}

	public SiacTBil getBilancio()
	{
		return bilancio;
	}

	public void setBilancio(SiacTBil bilancio)
	{
		this.bilancio = bilancio;
	}
}
