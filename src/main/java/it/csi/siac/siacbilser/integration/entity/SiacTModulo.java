/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_modulo database table.
 * 
 */
@Entity
@Table(name="siac_t_modulo")
@NamedQuery(name="SiacTModulo.findAll", query="SELECT s FROM SiacTModulo s")
public class SiacTModulo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MODULO_MODULOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_MODULO_MODULO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODULO_MODULOID_GENERATOR")
	@Column(name="modulo_id")
	private Integer moduloId;

	@Column(name="modulo_nome")
	private String moduloNome;

	//bi-directional many-to-one association to SiacRModuloTabella
	@OneToMany(mappedBy="siacTModulo")
	private List<SiacRModuloTabella> siacRModuloTabellas;

	public SiacTModulo() {
	}

	public Integer getModuloId() {
		return this.moduloId;
	}

	public void setModuloId(Integer moduloId) {
		this.moduloId = moduloId;
	}

	public List<SiacRModuloTabella> getSiacRModuloTabellas() {
		return this.siacRModuloTabellas;
	}

	public void setSiacRModuloTabellas(List<SiacRModuloTabella> siacRModuloTabellas) {
		this.siacRModuloTabellas = siacRModuloTabellas;
	}

	public SiacRModuloTabella addSiacRModuloTabella(SiacRModuloTabella siacRModuloTabella) {
		getSiacRModuloTabellas().add(siacRModuloTabella);
		siacRModuloTabella.setSiacTModulo(this);

		return siacRModuloTabella;
	}

	public SiacRModuloTabella removeSiacRModuloTabella(SiacRModuloTabella siacRModuloTabella) {
		getSiacRModuloTabellas().remove(siacRModuloTabella);
		siacRModuloTabella.setSiacTModulo(null);

		return siacRModuloTabella;
	}

	@Override
	public Integer getUid() {
		return moduloId;
	}

	@Override
	public void setUid(Integer uid) {
		moduloId = uid;
	}

}