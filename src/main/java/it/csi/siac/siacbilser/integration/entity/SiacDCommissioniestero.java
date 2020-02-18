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
 * The persistent class for the siac_d_commissioniestero database table.
 * 
 */
@Entity
@Table(name="siac_d_commissioniestero")
@NamedQuery(name="SiacDCommissioniestero.findAll", query="SELECT s FROM SiacDCommissioniestero s")
public class SiacDCommissioniestero extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_COMMISSIONIESTERO_COMMESTTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_COMMISSIONIESTERO_COMMEST_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_COMMISSIONIESTERO_COMMESTTIPOID_GENERATOR")
	@Column(name="commest_tipo_id")
	private Integer commestTipoId;

	@Column(name="commest_tipo_code")
	private String commestTipoCode;

	@Column(name="commest_tipo_desc")
	private String commestTipoDesc;

	//bi-directional many-to-one association to SiacTCartacontEstera
	@OneToMany(mappedBy="siacDCommissioniestero")
	private List<SiacTCartacontEstera> siacTCartacontEsteras;

	public SiacDCommissioniestero() {
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

	public List<SiacTCartacontEstera> getSiacTCartacontEsteras() {
		return this.siacTCartacontEsteras;
	}

	public void setSiacTCartacontEsteras(List<SiacTCartacontEstera> siacTCartacontEsteras) {
		this.siacTCartacontEsteras = siacTCartacontEsteras;
	}

	public SiacTCartacontEstera addSiacTCartacontEstera(SiacTCartacontEstera siacTCartacontEstera) {
		getSiacTCartacontEsteras().add(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDCommissioniestero(this);

		return siacTCartacontEstera;
	}

	public SiacTCartacontEstera removeSiacTCartacontEstera(SiacTCartacontEstera siacTCartacontEstera) {
		getSiacTCartacontEsteras().remove(siacTCartacontEstera);
		siacTCartacontEstera.setSiacDCommissioniestero(null);

		return siacTCartacontEstera;
	}

	@Override
	public Integer getUid() {
		return commestTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.commestTipoId = uid;
	}

}