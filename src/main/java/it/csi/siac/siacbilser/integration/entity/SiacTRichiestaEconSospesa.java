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
 * The persistent class for the siac_t_richiesta_econ_sospesa database table.
 * 
 */
@Entity
@Table(name="siac_t_richiesta_econ_sospesa")
@NamedQuery(name="SiacTRichiestaEconSospesa.findAll", query="SELECT s FROM SiacTRichiestaEconSospesa s")
public class SiacTRichiestaEconSospesa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_RICHIESTA_ECON_SOSPESA_RICECONSID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_RICHIESTA_ECON_SOSPESA_RICECONS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_RICHIESTA_ECON_SOSPESA_RICECONSID_GENERATOR")
	@Column(name="ricecons_id")
	private Integer riceconsId;

	@Column(name="ricecons_numero")
	private Integer riceconsNumero;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	public SiacTRichiestaEconSospesa() {
	}

	public Integer getRiceconsId() {
		return this.riceconsId;
	}

	public void setRiceconsId(Integer riceconsId) {
		this.riceconsId = riceconsId;
	}

	public Integer getRiceconsNumero() {
		return this.riceconsNumero;
	}

	public void setRiceconsNumero(Integer riceconsNumero) {
		this.riceconsNumero = riceconsNumero;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}
	
	@Override
	public Integer getUid() {
		return riceconsId;
	}

	@Override
	public void setUid(Integer uid) {
		this.riceconsId = uid;
	}

}