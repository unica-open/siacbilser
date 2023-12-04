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
 * The persistent class for the siac_d_distinta_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_distinta_tipo")
@NamedQuery(name="SiacDDistintaTipo.findAll", query="SELECT s FROM SiacDDistintaTipo s")
public class SiacDDistintaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_DISTINTA_TIPO_DISTTIPOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_DISTINTA_TIPO_DIST_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DISTINTA_TIPO_DISTTIPOID_GENERATOR")
	@Column(name="dist_tipo_id")
	private Integer distTipoId;

	@Column(name="dist_tipo_code")
	private String distTipoCode;

	@Column(name="dist_tipo_desc")
	private String distTipoDesc;

	//bi-directional many-to-one association to SiacDDistinta
	@OneToMany(mappedBy="siacDDistintaTipo")
	private List<SiacDDistinta> siacDDistintas;

	public SiacDDistintaTipo() {
	}

	public Integer getDistTipoId() {
		return this.distTipoId;
	}

	public void setDistTipoId(Integer distTipoId) {
		this.distTipoId = distTipoId;
	}

	public void setDistTipoCode(String distTipoCode) {
		this.distTipoCode = distTipoCode;
	}

	public String getDistTipoDesc() {
		return this.distTipoDesc;
	}

	public void setDistTipoDesc(String distTipoDesc) {
		this.distTipoDesc = distTipoDesc;
	}

	public List<SiacDDistinta> getSiacDDistintas() {
		return this.siacDDistintas;
	}

	public void setSiacDDistintas(List<SiacDDistinta> siacDDistintas) {
		this.siacDDistintas = siacDDistintas;
	}

	public SiacDDistinta addSiacDDistinta(SiacDDistinta siacDDistinta) {
		getSiacDDistintas().add(siacDDistinta);
		siacDDistinta.setSiacDDistintaTipo(this);

		return siacDDistinta;
	}

	public SiacDDistinta removeSiacDDistinta(SiacDDistinta siacDDistinta) {
		getSiacDDistintas().remove(siacDDistinta);
		siacDDistinta.setSiacDDistintaTipo(null);

		return siacDDistinta;
	}

	@Override
	public Integer getUid() {
		return distTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		distTipoId = uid;
	}

}