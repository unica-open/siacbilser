/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

@Entity
@Table(name="siac_d_relaz_entita")
public class SiacDRelazEntitaFin extends SiacTEnteBase {

	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="relaz_entita_id")
	private Integer relazEntitaId;
	
	@Column(name="relaz_entita_code")
	private String relazEntitaCode;

	@Column(name="relaz_entita_desc")
	private String relazEntitaDesc;


	//bi-directional many-to-one association to SiacDRelazTipoFin
	@OneToMany(mappedBy="siacDRelazEntita")
	private List<SiacDRelazTipoFin> siacDRelazTipos;

	public SiacDRelazEntitaFin() {
	}
	
	public Integer getRelazEntitaId() {
		return this.relazEntitaId;
	}

	public void setRelazEntitaId(Integer relazEntitaId) {
		this.relazEntitaId = relazEntitaId;
	}





	public String getRelazEntitaCode() {
		return this.relazEntitaCode;
	}

	public void setRelazEntitaCode(String relazEntitaCode) {
		this.relazEntitaCode = relazEntitaCode;
	}

	public String getRelazEntitaDesc() {
		return this.relazEntitaDesc;
	}

	public void setRelazEntitaDesc(String relazEntitaDesc) {
		this.relazEntitaDesc = relazEntitaDesc;
	}



	public List<SiacDRelazTipoFin> getSiacDRelazTipos() {
		return this.siacDRelazTipos;
	}

	public void setSiacDRelazTipos(List<SiacDRelazTipoFin> siacDRelazTipos) {
		this.siacDRelazTipos = siacDRelazTipos;
	}

	public SiacDRelazTipoFin addSiacDRelazTipo(SiacDRelazTipoFin siacDRelazTipo) {
		getSiacDRelazTipos().add(siacDRelazTipo);
		siacDRelazTipo.setSiacDRelazEntita(this);

		return siacDRelazTipo;
	}

	public SiacDRelazTipoFin removeSiacDRelazTipo(SiacDRelazTipoFin siacDRelazTipo) {
		getSiacDRelazTipos().remove(siacDRelazTipo);
		siacDRelazTipo.setSiacDRelazEntita(null);

		return siacDRelazTipo;
	}

	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return this.relazEntitaId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.relazEntitaId = uid;
	}

}
