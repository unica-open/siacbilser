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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="siac_t_mutuo_num")
public class SiacTMutuoNum extends SiacTEnteBase {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_NUM_MUTUONUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MUTUO_NUM_MUTUO_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_NUM_MUTUONUMID_GENERATOR")
	@Column(name="mutuo_num_id")
	private Integer mutuoNumId;

	@Column(name="mutuo_numero")
	@Version
	private Integer mutuoNumero;
	
	@Override
	public Integer getUid() {
		return getMutuoNumId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoNumId(uid);
	}

	public Integer getMutuoNumId() {
		return mutuoNumId;
	}

	public void setMutuoNumId(Integer mutuoNumId) {
		this.mutuoNumId = mutuoNumId;
	}

	public Integer getMutuoNumero() {
		return mutuoNumero;
	}

	public void setMutuoNumero(Integer mutuoNumero) {
		this.mutuoNumero = mutuoNumero;
	}

}