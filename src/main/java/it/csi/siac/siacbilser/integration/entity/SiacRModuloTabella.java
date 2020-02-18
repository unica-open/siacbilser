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
 * The persistent class for the siac_r_modulo_tabella database table.
 * 
 */
@Entity
@Table(name="siac_r_modulo_tabella")
@NamedQuery(name="SiacRModuloTabella.findAll", query="SELECT s FROM SiacRModuloTabella s")
public class SiacRModuloTabella extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MODULO_TABELLA_MODULOTABELLAID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_MODULO_TABELLA_MODULO_TABELLA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODULO_TABELLA_MODULOTABELLAID_GENERATOR")
	@Column(name="modulo_tabella_id")
	private Integer moduloTabellaId;

	//bi-directional many-to-one association to SiacTModulo
	@ManyToOne
	@JoinColumn(name="modulo_id")
	private SiacTModulo siacTModulo;

	//bi-directional many-to-one association to SiacTTabella
	@ManyToOne
	@JoinColumn(name="tabella_id")
	private SiacTTabella siacTTabella;

	public SiacRModuloTabella() {
	}

	public Integer getModuloTabellaId() {
		return this.moduloTabellaId;
	}

	public void setModuloTabellaId(Integer moduloTabellaId) {
		this.moduloTabellaId = moduloTabellaId;
	}

	public SiacTModulo getSiacTModulo() {
		return this.siacTModulo;
	}

	public void setSiacTModulo(SiacTModulo siacTModulo) {
		this.siacTModulo = siacTModulo;
	}

	public SiacTTabella getSiacTTabella() {
		return this.siacTTabella;
	}

	public void setSiacTTabella(SiacTTabella siacTTabella) {
		this.siacTTabella = siacTTabella;
	}

	@Override
	public Integer getUid() {
		return moduloTabellaId;
	}

	@Override
	public void setUid(Integer uid) {
		moduloTabellaId = uid;
	}

}