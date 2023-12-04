/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_accredito_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_accredito_tipo_oil")
public class SiacDAccreditoTipoOilFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="accredito_tipo_oil_id")
	private Integer accreditoTipoOilId;


	public SiacDAccreditoTipoOilFin() {
	}



	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.accreditoTipoOilId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.accreditoTipoOilId = uid;
	}
}