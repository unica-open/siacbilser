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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


/**
 * The persistent class for the siac_d_mutuo_tipo_tasso database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_tipo_tasso")
@NamedQuery(name="SiacDMutuoTipoTasso.findAll", query="SELECT s FROM SiacDMutuoTipoTasso s")
public class SiacDMutuoTipoTasso extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_TIPO_TASSO_MUTUO_TIPO_TASSO_IDGENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_TIPO_TASSO_MUTUO_TIPO_TASSO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_TIPO_TASSO_MUTUO_TIPO_TASSO_IDGENERATOR")
	@Column(name="mutuo_tipo_tasso_id")
	private Integer mutuoTipoTassoId;

	@Column(name="mutuo_tipo_tasso_code")
	private String mutuoTipoTassoCode;

	@Column(name="mutuo_tipo_tasso_desc")
	private String mutuoTipoTassoDesc;

	
	public SiacDMutuoTipoTasso() {
	}

	public Integer getMutuoTipoTassoId() {
		return this.mutuoTipoTassoId;
	}

	public void setMutuoTipoTassoId(Integer mutuoTipoTassoId) {
		this.mutuoTipoTassoId = mutuoTipoTassoId;
	}
	
	public String getMutuoTipoTassoCode() {
		return mutuoTipoTassoCode;
	}

	public void setMutuoTipoTassoCode(String mutuoTipoTassoCode) {
		this.mutuoTipoTassoCode = mutuoTipoTassoCode;
	}

	public String getMutuoTipoTassoDesc() {
		return mutuoTipoTassoDesc;
	}

	public void setMutuoTipoTassoDesc(String mutuoTipoTassoDesc) {
		this.mutuoTipoTassoDesc = mutuoTipoTassoDesc;
	}

	@Override
	public Integer getUid() {
		return getMutuoTipoTassoId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoTipoTassoId(uid);
	}

}