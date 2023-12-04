/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_bil_elem_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_saldo_vincolo_sotto_conto")
public class SiacRSaldoVincoloSottoConto extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SALDO_VINCOLO_SOTTO_CONTO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_saldo_vincolo_sotto_conto_saldo_vincolo_conto_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SALDO_VINCOLO_SOTTO_CONTO_ID_GENERATOR")
	@Column(name="saldo_vincolo_conto_id")
	private Integer saldoVincoloContoId;

	//bi-directional many-to-one association to SiacDRelazTipoFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreria siacDContoTesoreria;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="vincolo_id")
	private SiacTVincolo siacTVincolo;
	
	@Column(name="saldo_inizale")
	private BigDecimal saldoIniziale;
	
	@Column(name="saldo_finale")
	private BigDecimal saldoFinale;
	
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;
	

	public SiacRSaldoVincoloSottoConto() {

	}

	public Integer getSaldoVincoloContoId() {
		return saldoVincoloContoId;
	}

	public void setSaldoVincoloContoId(Integer saldoVincoloContoId) {
		this.saldoVincoloContoId = saldoVincoloContoId;
	}

	public SiacDContotesoreria getSiacDContoTesoreria() {
		return siacDContoTesoreria;
	}



	public void setSiacDContoTesoreria(SiacDContotesoreria siacDContoTesoreria) {
		this.siacDContoTesoreria = siacDContoTesoreria;
	}



	public SiacTVincolo getSiacTVincolo() {
		return siacTVincolo;
	}



	public void setSiacTVincolo(SiacTVincolo siacTVincolo) {
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



	public SiacTBil getSiacTBil() {
		return siacTBil;
	}



	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}



	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return saldoVincoloContoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.saldoVincoloContoId = uid;
	}

}