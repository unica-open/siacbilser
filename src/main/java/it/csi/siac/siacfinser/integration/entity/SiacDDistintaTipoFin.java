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
 * The persistent class for the siac_d_distinta_tipo database table.
 * 
 */

@Entity
@Table(name="siac_d_distinta_tipo")

public class SiacDDistintaTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_DISTINTA_TIPO_DIST_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_distinta_tipo_dist_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DISTINTA_TIPO_DIST_ID_GENERATOR")
	
	
	@Column(name="dist_tipo_id")
	private Integer distTipoId;

	@Column(name="dist_tipo_code")
	private String distTipoCode;

	@Column(name="dist_tipo_desc")
	private String distTipoDesc;

	//bi-directional many-to-one association to SiacDDistintaFin
	@OneToMany(mappedBy="siacDDistintaTipo")
	private List<SiacDDistintaFin> siacDDistintas;

	public SiacDDistintaTipoFin() {
	}

	public Integer getDistTipoId() {
		return this.distTipoId;
	}

	public void setDistTipoId(Integer distTipoId) {
		this.distTipoId = distTipoId;
	}

	
	public String getDistTipoCode() {
		return this.distTipoCode;
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


	public List<SiacDDistintaFin> getSiacDDistintas() {
		return this.siacDDistintas;
	}

	public void setSiacDDistintas(List<SiacDDistintaFin> siacDDistintas) {
		this.siacDDistintas = siacDDistintas;
	}

	public SiacDDistintaFin addSiacDDistinta(SiacDDistintaFin siacDDistinta) {
		getSiacDDistintas().add(siacDDistinta);
		siacDDistinta.setSiacDDistintaTipo(this);

		return siacDDistinta;
	}

	public SiacDDistintaFin removeSiacDDistinta(SiacDDistintaFin siacDDistinta) {
		getSiacDDistintas().remove(siacDDistinta);
		siacDDistinta.setSiacDDistintaTipo(null);

		return siacDDistinta;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return distTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.distTipoId=uid;
	}

}