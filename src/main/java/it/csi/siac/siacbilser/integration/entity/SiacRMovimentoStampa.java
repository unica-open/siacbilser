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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_movimento_stampa database table.
 * 
 */
@Entity
@Table(name="siac_r_movimento_stampa")
@NamedQuery(name="SiacRMovimentoStampa.findAll", query="SELECT s FROM SiacRMovimentoStampa s")
public class SiacRMovimentoStampa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVIMENTO_STAMPA_MOVTSTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVIMENTO_STAMPA_MOVTST_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVIMENTO_STAMPA_MOVTSTRID_GENERATOR")
	@Column(name="movtst_r_id")
	private Integer movtstRId;

	//bi-directional many-to-one association to SiacTCassaEconStampa
	@ManyToOne
	@JoinColumn(name="cest_id")
	private SiacTCassaEconStampa siacTCassaEconStampa;

	//bi-directional many-to-one association to SiacTMovimento
	@ManyToOne
	@JoinColumn(name="movt_id")
	private SiacTMovimento siacTMovimento;

	public SiacRMovimentoStampa() {
	}

	public Integer getMovtstRId() {
		return this.movtstRId;
	}

	public void setMovtstRId(Integer movtstRId) {
		this.movtstRId = movtstRId;
	}

	
	public SiacTCassaEconStampa getSiacTCassaEconStampa() {
		return this.siacTCassaEconStampa;
	}

	public void setSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		this.siacTCassaEconStampa = siacTCassaEconStampa;
	}

	public SiacTMovimento getSiacTMovimento() {
		return this.siacTMovimento;
	}

	public void setSiacTMovimento(SiacTMovimento siacTMovimento) {
		this.siacTMovimento = siacTMovimento;
	}

	@Override
	public Integer getUid() {
		return this.movtstRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.movtstRId = uid;		
	}

}