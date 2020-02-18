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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_distinta database table.
 * 
 */

@Entity
@Table(name="siac_d_distinta")

public class SiacDDistintaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_DISTINTA_DIST_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_distinta_dist_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DISTINTA_DIST_ID_GENERATOR")
	
	
	@Column(name="dist_id")
	private Integer distId;

	@Column(name="dist_code")
	private String distCode;

	@Column(name="dist_desc")
	private String distDesc;

	
	//bi-directional many-to-one association to SiacDDistintaTipoFin
	@ManyToOne
	@JoinColumn(name="dist_tipo_id")
	private SiacDDistintaTipoFin siacDDistintaTipo;
	
	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@OneToMany(mappedBy="siacDDistinta")
	private List<SiacTLiquidazioneFin> siacTLiquidaziones;

	public SiacDDistintaFin() {
	}

	public Integer getDistId() {
		return this.distId;
	}

	public void setDistId(Integer distId) {
		this.distId = distId;
	}


	public String getDistCode() {
		return this.distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getDistDesc() {
		return this.distDesc;
	}

	public void setDistDesc(String distDesc) {
		this.distDesc = distDesc;
	}

	
	public SiacDDistintaTipoFin getSiacDDistintaTipo() {
		return this.siacDDistintaTipo;
	}

	public void setSiacDDistintaTipo(SiacDDistintaTipoFin siacDDistintaTipo) {
		this.siacDDistintaTipo = siacDDistintaTipo;
	}

	@Override
	public Integer getUid() {
		return distId;
	}

	@Override
	public void setUid(Integer uid) {
		this.distId=uid;
	}

}