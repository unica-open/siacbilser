/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Basic;
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
 * The persistent class for the siac_r_soggetto_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_stato")
public class SiacRSoggettoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_stato_soggetto_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_STATO_ID_GENERATOR")
	@Column(name="soggetto_stato_r_id")
	private Integer soggettoStatoRId;

	//bi-directional many-to-one association to SiacDSoggettoStatoFin
	@ManyToOne
	@JoinColumn(name="soggetto_stato_id")
	private SiacDSoggettoStatoFin siacDSoggettoStato;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;
	
	@Basic
	@Column(name = "nota_operazione")
	private String notaOperazione;

	public SiacRSoggettoStatoFin() {
	}

	public Integer getSoggettoStatoRId() {
		return this.soggettoStatoRId;
	}

	public void setSoggettoStatoRId(Integer soggettoStatoRId) {
		this.soggettoStatoRId = soggettoStatoRId;
	}

	public SiacDSoggettoStatoFin getSiacDSoggettoStato() {
		return this.siacDSoggettoStato;
	}

	public void setSiacDSoggettoStato(SiacDSoggettoStatoFin siacDSoggettoStato) {
		this.siacDSoggettoStato = siacDSoggettoStato;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoStatoRId = uid;
	}

	public String getNotaOperazione() {
		return notaOperazione;
	}

	public void setNotaOperazione(String notaOperazione) {
		this.notaOperazione = notaOperazione;
	}
}