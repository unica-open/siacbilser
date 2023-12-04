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
 * The persistent class for the siac_t_modulo database table.
 * 
 */
@Entity
@Table(name="siac_t_modulo")
public class SiacTModuloFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="modulo_id")
	private Integer moduloId;

	@Column(name="modulo_nome")
	private String moduloNome;

	//bi-directional many-to-one association to SiacRModuloTabellaFin
	@OneToMany(mappedBy="siacTModulo")
	private List<SiacRModuloTabellaFin> siacRModuloTabellas;

	public SiacTModuloFin() {
	}

	public Integer getModuloId() {
		return this.moduloId;
	}

	public void setModuloId(Integer moduloId) {
		this.moduloId = moduloId;
	}

	public String getModuloNome() {
		return this.moduloNome;
	}

	public void setModuloNome(String moduloNome) {
		this.moduloNome = moduloNome;
	}

	public List<SiacRModuloTabellaFin> getSiacRModuloTabellas() {
		return this.siacRModuloTabellas;
	}

	public void setSiacRModuloTabellas(List<SiacRModuloTabellaFin> siacRModuloTabellas) {
		this.siacRModuloTabellas = siacRModuloTabellas;
	}

	public SiacRModuloTabellaFin addSiacRModuloTabella(SiacRModuloTabellaFin siacRModuloTabella) {
		getSiacRModuloTabellas().add(siacRModuloTabella);
		siacRModuloTabella.setSiacTModulo(this);

		return siacRModuloTabella;
	}

	public SiacRModuloTabellaFin removeSiacRModuloTabella(SiacRModuloTabellaFin siacRModuloTabella) {
		getSiacRModuloTabellas().remove(siacRModuloTabella);
		siacRModuloTabella.setSiacTModulo(null);

		return siacRModuloTabella;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.moduloId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.moduloId = uid;
	}
}