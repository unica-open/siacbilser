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
 * The persistent class for the siac_d_commissioniestero database table.
 * 
 */
@Entity
@Table(name="siac_d_commissioniestero")
public class SiacDCommissioniesteroFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name="commest_tipo_id")
	private Integer commestTipoId;

	@Column(name="commest_tipo_code")
	private String commestTipoCode;

	@Column(name="commest_tipo_desc")
	private String commestTipoDesc;

	//bi-directional many-to-one association to SiacTCartacontEsteraFin
	@OneToMany(mappedBy="siacDCommissioniestero")
	private List<SiacTCartacontEsteraFin> siacTCartacontEsteras;

	public SiacDCommissioniesteroFin() {
	}

	public Integer getCommestTipoId() {
		return this.commestTipoId;
	}

	public void setCommestTipoId(Integer commestTipoId) {
		this.commestTipoId = commestTipoId;
	}

	public String getCommestTipoCode() {
		return this.commestTipoCode;
	}

	public void setCommestTipoCode(String commestTipoCode) {
		this.commestTipoCode = commestTipoCode;
	}

	public String getCommestTipoDesc() {
		return this.commestTipoDesc;
	}

	public void setCommestTipoDesc(String commestTipoDesc) {
		this.commestTipoDesc = commestTipoDesc;
	}

	public List<SiacTCartacontEsteraFin> getSiacTCartacontEsteras() {
		return this.siacTCartacontEsteras;
	}

	public void setSiacTCartacontEsteras(List<SiacTCartacontEsteraFin> siacTCartacontEsteras) {
		this.siacTCartacontEsteras = siacTCartacontEsteras;
	}

	public SiacTCartacontEsteraFin addSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		getSiacTCartacontEsteras().add(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDCommissioniestero(this);

		return siacTCartacontEstera;
	}

	public SiacTCartacontEsteraFin removeSiacTCartacontEstera(SiacTCartacontEsteraFin siacTCartacontEstera) {
		getSiacTCartacontEsteras().remove(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDCommissioniestero(null);

		return siacTCartacontEstera;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.commestTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.commestTipoId = uid;
	}

}