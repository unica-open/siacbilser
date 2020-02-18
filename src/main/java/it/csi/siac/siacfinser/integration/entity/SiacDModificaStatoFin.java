/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_modifica_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_modifica_stato")
public class SiacDModificaStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MODIFICA_STATO_MOD_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_modifica_stato_mod_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MODIFICA_STATO_MOD_STATO_ID_GENERATOR")
	@Column(name="mod_stato_id")
	private Integer modStatoId;

	@Column(name="mod_stato_code")
	private String modStatoCode;

	@Column(name="mod_stato_desc")
	private String modStatoDesc;

	//bi-directional many-to-one association to SiacRModificaStatoFin
	@OneToMany(mappedBy="siacDModificaStato")
	private List<SiacRModificaStatoFin> siacRModificaStatos;

	public Integer getModStatoId() {
		return this.modStatoId;
	}

	public void setModStatoId(Integer modStatoId) {
		this.modStatoId = modStatoId;
	}

	public String getModStatoCode() {
		return this.modStatoCode;
	}

	public void setModStatoCode(String modStatoCode) {
		this.modStatoCode = modStatoCode;
	}

	public String getModStatoDesc() {
		return this.modStatoDesc;
	}

	public void setModStatoDesc(String modStatoDesc) {
		this.modStatoDesc = modStatoDesc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.modStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.modStatoId = uid;
	}
}