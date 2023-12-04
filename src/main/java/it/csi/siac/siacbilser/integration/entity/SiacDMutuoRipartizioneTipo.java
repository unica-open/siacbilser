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
 * The persistent class for the siac_d_mutuo_ripartizione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_ripartizione_tipo")
@NamedQuery(name="SiacDMutuoRipartizioneTipo.findAll", query="SELECT s FROM SiacDMutuoRipartizioneTipo s")
public class SiacDMutuoRipartizioneTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_RIPARTIZIONE_TIPO_MUTUO_RIPARTIZIONE__IDGENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_RIPARTIZIONE_TIPO_MUTUO_RIPARTIZIONE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_RIPARTIZIONE_TIPO_MUTUO_RIPARTIZIONE__IDGENERATOR")
	@Column(name="mutuo_ripartizione_tipo_id")
	private Integer mutuoRipartizioneTipoId;

	@Column(name="mutuo_ripartizione_tipo_code")
	private String mutuoRipartizioneTipoCode;

	@Column(name="mutuo_ripartizione_tipo_desc")
	private String mutuoRipartizioneTipoDesc;

	
	public SiacDMutuoRipartizioneTipo() {
	}

	

	public Integer getMutuoRipartizioneTipoId() {
		return mutuoRipartizioneTipoId;
	}



	public void setMutuoRipartizioneTipoId(Integer mutuoRipartizioneTipoId) {
		this.mutuoRipartizioneTipoId = mutuoRipartizioneTipoId;
	}



	public String getMutuoRipartizioneTipoCode() {
		return mutuoRipartizioneTipoCode;
	}



	public void setMutuoRipartizioneTipoCode(String mutuoRipartizioneTipoCode) {
		this.mutuoRipartizioneTipoCode = mutuoRipartizioneTipoCode;
	}



	public String getMutuoRipartizioneTipoDesc() {
		return mutuoRipartizioneTipoDesc;
	}



	public void setMutuoRipartizioneTipoDesc(String mutuoRipartizioneTipoDesc) {
		this.mutuoRipartizioneTipoDesc = mutuoRipartizioneTipoDesc;
	}



	@Override
	public Integer getUid() {
		return getMutuoRipartizioneTipoId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoRipartizioneTipoId(uid);
	}

}