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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


/**
 * The persistent class for the siac_d_mutuo_periodo_rimborso database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_periodo_rimborso")
@NamedQuery(name="SiacDMutuoPeriodoRimborso.findAll", query="SELECT s FROM SiacDMutuoPeriodoRimborso s")
public class SiacDMutuoPeriodoRimborso extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_PERIODO_RIMBORSO_MUTUO_PERIODO_RIMBORSO_IDGENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_PERIODO_RIMBORSO_MUTUO_PERIODO_RIMBORSO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_PERIODO_RIMBORSO_MUTUO_PERIODO_RIMBORSO_IDGENERATOR")
	@Column(name="mutuo_periodo_rimborso_id")
	private Integer mutuoPeriodoRimborsoId;

	@Column(name="mutuo_periodo_rimborso_code")
	private String mutuoPeriodoRimborsoCode;

	@Column(name="mutuo_periodo_rimborso_desc")
	private String mutuoPeriodoRimborsoDesc;
	
	@Column(name="mutuo_periodo_numero_mesi")
	private Integer mutuoPeriodoNumeroMesi;

	
	public SiacDMutuoPeriodoRimborso() {
	}

	public Integer getMutuoPeriodoRimborsoId() {
		return this.mutuoPeriodoRimborsoId;
	}

	public void setMutuoPeriodoRimborsoId(Integer mutuoPeriodoRimborsoId) {
		this.mutuoPeriodoRimborsoId = mutuoPeriodoRimborsoId;
	}
	
	public String getMutuoPeriodoRimborsoCode() {
		return mutuoPeriodoRimborsoCode;
	}

	public void setMutuoPeriodoRimborsoCode(String mutuoPeriodoRimborsoCode) {
		this.mutuoPeriodoRimborsoCode = mutuoPeriodoRimborsoCode;
	}

	public String getMutuoPeriodoRimborsoDesc() {
		return mutuoPeriodoRimborsoDesc;
	}

	public void setMutuoPeriodoRimborsoDesc(String mutuoPeriodoRimborsoDesc) {
		this.mutuoPeriodoRimborsoDesc = mutuoPeriodoRimborsoDesc;
	}

	public Integer getMutuoPeriodoNumeroMesi() {
		return mutuoPeriodoNumeroMesi;
	}

	public void setMutuoPeriodoNumeroMesi(Integer mutuoPeriodoNumeroMesi) {
		this.mutuoPeriodoNumeroMesi = mutuoPeriodoNumeroMesi;
	}

	@Override
	public Integer getUid() {
		return getMutuoPeriodoRimborsoId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoPeriodoRimborsoId(uid);
	}

}