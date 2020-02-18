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
 * The persistent class for the siac_r_richiesta_econ_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_richiesta_econ_stato")
@NamedQuery(name="SiacRRichiestaEconStato.findAll", query="SELECT s FROM SiacRRichiestaEconStato s")
public class SiacRRichiestaEconStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_RICHIESTA_ECON_STATO_RICECONRSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_RICHIESTA_ECON_STATO_RICECON_R_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_RICHIESTA_ECON_STATO_RICECONRSTATOID_GENERATOR")
	@Column(name="ricecon_r_stato_id")
	private Integer riceconRStatoId;

	//bi-directional many-to-one association to SiacDRichiestaEconStato
	@ManyToOne
	@JoinColumn(name="ricecon_stato_id")
	private SiacDRichiestaEconStato siacDRichiestaEconStato;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	public SiacRRichiestaEconStato() {
	}

	public Integer getRiceconRStatoId() {
		return this.riceconRStatoId;
	}

	public void setRiceconRStatoId(Integer riceconRStatoId) {
		this.riceconRStatoId = riceconRStatoId;
	}

	public SiacDRichiestaEconStato getSiacDRichiestaEconStato() {
		return this.siacDRichiestaEconStato;
	}

	public void setSiacDRichiestaEconStato(SiacDRichiestaEconStato siacDRichiestaEconStato) {
		this.siacDRichiestaEconStato = siacDRichiestaEconStato;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}
	
	@Override
	public Integer getUid() {
		return riceconRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.riceconRStatoId = uid;
	}

}