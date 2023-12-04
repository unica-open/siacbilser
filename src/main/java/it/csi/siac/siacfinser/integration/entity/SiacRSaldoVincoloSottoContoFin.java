/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_r_ordinativo database table.
 * 
 */
@Entity
@Table(name="siac_r_saldo_vincolo_sotto_conto")
public class SiacRSaldoVincoloSottoContoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SALDO_VINCOLO_SOTTO_CONTO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_saldo_vincolo_sotto_conto_saldo_vincolo_conto_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SALDO_VINCOLO_SOTTO_CONTO_ID_GENERATOR")
	@Column(name="saldo_vincolo_conto_id")
	private Integer saldoVincoloContoId;


	//bi-directional many-to-one association to SiacDRelazTipoFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreriaFin siacDContoTesoreria;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="vincolo_id")
	private SiacTVincoloFin siacTVincolo;
	
	@Column(name="saldo_inizale")
	private BigDecimal saldoIniziale;
	
	@Column(name="saldo_finale")
	private BigDecimal saldoFinale;
	
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;
	

	public SiacRSaldoVincoloSottoContoFin() {

	}

	public Integer getSaldoVincoloContoId() {
		return saldoVincoloContoId;
	}

	public void setSaldoVincoloContoId(Integer saldoVincoloContoId) {
		this.saldoVincoloContoId = saldoVincoloContoId;
	}

	public SiacDContotesoreriaFin getSiacDContoTesoreria() {
		return siacDContoTesoreria;
	}



	public void setSiacDContoTesoreria(SiacDContotesoreriaFin siacDContoTesoreria) {
		this.siacDContoTesoreria = siacDContoTesoreria;
	}



	public SiacTVincoloFin getSiacTVincolo() {
		return siacTVincolo;
	}



	public void setSiacTVincolo(SiacTVincoloFin siacTVincolo) {
		this.siacTVincolo = siacTVincolo;
	}



	public BigDecimal getSaldoIniziale() {
		return saldoIniziale;
	}



	public void setSaldoIniziale(BigDecimal saldoIniziale) {
		this.saldoIniziale = saldoIniziale;
	}



	public BigDecimal getSaldoFinale() {
		return saldoFinale;
	}



	public void setSaldoFinale(BigDecimal saldoFinale) {
		this.saldoFinale = saldoFinale;
	}



	public SiacTBilFin getSiacTBil() {
		return siacTBil;
	}



	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}



	@Override
	public Integer getUid() {
		return this.saldoVincoloContoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.saldoVincoloContoId = uid;
	}
}