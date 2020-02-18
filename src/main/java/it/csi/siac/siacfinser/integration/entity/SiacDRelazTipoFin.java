/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_d_relaz_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_relaz_tipo")
public class SiacDRelazTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="relaz_tipo_id")
	private Integer relazTipoId;

	@Column(name="relaz_tipo_code")
	private String relazTipoCode;

	@Column(name="relaz_tipo_desc")
	private String relazTipoDesc;

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
	@OneToMany(mappedBy="siacDRelazTipo")
	private List<SiacRSoggettoRelazFin> siacRSoggettoRelazs;

	//bi-directional many-to-one association to SiacRSoggettoRelazModFin
	@OneToMany(mappedBy="siacDRelazTipo")
	private List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods;
	
	//bi-directional many-to-one association to SiacDRelazEntitaFin
	@ManyToOne
	@JoinColumn(name="relaz_entita_id")
	private SiacDRelazEntitaFin siacDRelazEntita;

	public SiacDRelazTipoFin() {
	}

	public Integer getRelazTipoId() {
		return this.relazTipoId;
	}

	public void setRelazTipoId(Integer relazTipoId) {
		this.relazTipoId = relazTipoId;
	}

	public String getRelazTipoCode() {
		return this.relazTipoCode;
	}

	public void setRelazTipoCode(String relazTipoCode) {
		this.relazTipoCode = relazTipoCode;
	}

	public String getRelazTipoDesc() {
		return this.relazTipoDesc;
	}

	public void setRelazTipoDesc(String relazTipoDesc) {
		this.relazTipoDesc = relazTipoDesc;
	}

	public List<SiacRSoggettoRelazFin> getSiacRSoggettoRelazs() {
		return this.siacRSoggettoRelazs;
	}

	public void setSiacRSoggettoRelazs(List<SiacRSoggettoRelazFin> siacRSoggettoRelazs) {
		this.siacRSoggettoRelazs = siacRSoggettoRelazs;
	}

	public SiacRSoggettoRelazFin addSiacRSoggettoRelaz(SiacRSoggettoRelazFin siacRSoggettoRelaz) {
		getSiacRSoggettoRelazs().add(siacRSoggettoRelaz);
		siacRSoggettoRelaz.setSiacDRelazTipo(this);

		return siacRSoggettoRelaz;
	}

	public SiacRSoggettoRelazFin removeSiacRSoggettoRelaz(SiacRSoggettoRelazFin siacRSoggettoRelaz) {
		getSiacRSoggettoRelazs().remove(siacRSoggettoRelaz);
		siacRSoggettoRelaz.setSiacDRelazTipo(null);

		return siacRSoggettoRelaz;
	}

	public List<SiacRSoggettoRelazModFin> getSiacRSoggettoRelazMods() {
		return this.siacRSoggettoRelazMods;
	}

	public void setSiacRSoggettoRelazMods(List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods) {
		this.siacRSoggettoRelazMods = siacRSoggettoRelazMods;
	}

	public SiacRSoggettoRelazModFin addSiacRSoggettoRelazMod(SiacRSoggettoRelazModFin siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().add(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacDRelazTipo(this);

		return siacRSoggettoRelazMod;
	}

	public SiacRSoggettoRelazModFin removeSiacRSoggettoRelazMod(SiacRSoggettoRelazModFin siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().remove(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacDRelazTipo(null);

		return siacRSoggettoRelazMod;
	}

	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return this.relazTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.relazTipoId = uid;
	}
	
	public SiacDRelazEntitaFin getSiacDRelazEntita() {
		return this.siacDRelazEntita;
	}

	public void setSiacDRelazEntita(SiacDRelazEntitaFin siacDRelazEntita) {
		this.siacDRelazEntita = siacDRelazEntita;
	}
}