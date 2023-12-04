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


/**
 * The persistent class for the siac_t_tabella database table.
 * 
 */
@Entity
@Table(name="siac_t_tabella")
public class SiacTTabellaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="tabella_id")
	private Integer tabellaId;

	@Column(name="tabella_nome")
	private String tabellaNome;

	//bi-directional many-to-one association to SiacRModuloTabellaFin
	@OneToMany(mappedBy="siacTTabella")
	private List<SiacRModuloTabellaFin> siacRModuloTabellas;

	public SiacTTabellaFin() {
	}

	public Integer getTabellaId() {
		return this.tabellaId;
	}

	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	public String getTabellaNome() {
		return this.tabellaNome;
	}

	public void setTabellaNome(String tabellaNome) {
		this.tabellaNome = tabellaNome;
	}

	public List<SiacRModuloTabellaFin> getSiacRModuloTabellas() {
		return this.siacRModuloTabellas;
	}

	public void setSiacRModuloTabellas(List<SiacRModuloTabellaFin> siacRModuloTabellas) {
		this.siacRModuloTabellas = siacRModuloTabellas;
	}

	public SiacRModuloTabellaFin addSiacRModuloTabella(SiacRModuloTabellaFin siacRModuloTabella) {
		getSiacRModuloTabellas().add(siacRModuloTabella);
		siacRModuloTabella.setSiacTTabella(this);

		return siacRModuloTabella;
	}

	public SiacRModuloTabellaFin removeSiacRModuloTabella(SiacRModuloTabellaFin siacRModuloTabella) {
		getSiacRModuloTabellas().remove(siacRModuloTabella);
		siacRModuloTabella.setSiacTTabella(null);

		return siacRModuloTabella;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.tabellaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.tabellaId = uid;
	}
}