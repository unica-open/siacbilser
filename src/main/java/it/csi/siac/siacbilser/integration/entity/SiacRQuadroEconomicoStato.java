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
 * The persistent class for the siac_r_quadro_economico_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_quadro_economico_stato")
@NamedQuery(name="SiacRQuadroEconomicoStato.findAll", query="SELECT s FROM SiacRQuadroEconomicoStato s")
public class SiacRQuadroEconomicoStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_QUADRO_ECONOMICO_STATO_QUADROECONOMICOID_GENERATOR", allocationSize=1, sequenceName="siac_r_quadro_economico_stato_quadro_economico_r_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_QUADRO_ECONOMICO_STATO_QUADROECONOMICOID_GENERATOR")
	@Column(name="quadro_economico_r_stato_id")
	private Integer quadroEconomicoRStatoId;

	//bi-directional many-to-one association to SiacTQuadroEconomico
	@ManyToOne
	@JoinColumn(name="quadro_economico_id")
	private SiacTQuadroEconomico siacTQuadroEconomico;

	//bi-directional many-to-one association to SiacDQuadroEconomicoStato
	@ManyToOne
	@JoinColumn(name="quadro_economico_stato_id")
	private SiacDQuadroEconomicoStato siacDQuadroEconomicoStato;

	public SiacRQuadroEconomicoStato() {
	}

	public Integer getQuadroEconomicoStatoId() {
		return this.quadroEconomicoRStatoId;
	}

	public void setQuadroEconomicoStatoId(Integer quadroEconomicoStatoId) {
		this.quadroEconomicoRStatoId = quadroEconomicoStatoId;
	}

	public SiacTQuadroEconomico getSiacTQuadroEconomico() {
		return this.siacTQuadroEconomico;
	}

	public void setSiacTQuadroEconomico(SiacTQuadroEconomico siacTQuadroEconomico) {
		this.siacTQuadroEconomico = siacTQuadroEconomico;
	}

	public SiacDQuadroEconomicoStato getSiacDQuadroEconomicoStato() {
		return this.siacDQuadroEconomicoStato;
	}

	public void setSiacDQuadroEconomicoStato(SiacDQuadroEconomicoStato siacDQuadroEconomicoStato) {
		this.siacDQuadroEconomicoStato = siacDQuadroEconomicoStato;
	}

	@Override
	public Integer getUid() {
		return quadroEconomicoRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.quadroEconomicoRStatoId = uid;
	}

}