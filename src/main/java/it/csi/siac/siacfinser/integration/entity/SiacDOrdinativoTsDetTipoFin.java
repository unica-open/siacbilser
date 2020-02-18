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
 * The persistent class for the siac_d_ordinativo_ts_det_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_ordinativo_ts_det_tipo")
public class SiacDOrdinativoTsDetTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ord_ts_det_tipo_id")
	private Integer ordTsDetTipoId;

	@Column(name="ord_ts_det_tipo_code")
	private String ordTsDetTipoCode;

	@Column(name="ord_ts_det_tipo_desc")
	private String ordTsDetTipoDesc;

	//bi-directional many-to-one association to SiacTOrdinativoTsDetFin
	@OneToMany(mappedBy="siacDOrdinativoTsDetTipo")
	private List<SiacTOrdinativoTsDetFin> siacTOrdinativoTsDets;

	public SiacDOrdinativoTsDetTipoFin() {
	}

	public Integer getOrdTsDetTipoId() {
		return this.ordTsDetTipoId;
	}

	public void setOrdTsDetTipoId(Integer ordTsDetTipoId) {
		this.ordTsDetTipoId = ordTsDetTipoId;
	}

	public String getOrdTsDetTipoCode() {
		return this.ordTsDetTipoCode;
	}

	public void setOrdTsDetTipoCode(String ordTsDetTipoCode) {
		this.ordTsDetTipoCode = ordTsDetTipoCode;
	}

	public String getOrdTsDetTipoDesc() {
		return this.ordTsDetTipoDesc;
	}

	public void setOrdTsDetTipoDesc(String ordTsDetTipoDesc) {
		this.ordTsDetTipoDesc = ordTsDetTipoDesc;
	}

	public List<SiacTOrdinativoTsDetFin> getSiacTOrdinativoTsDets() {
		return this.siacTOrdinativoTsDets;
	}

	public void setSiacTOrdinativoTsDets(List<SiacTOrdinativoTsDetFin> siacTOrdinativoTsDets) {
		this.siacTOrdinativoTsDets = siacTOrdinativoTsDets;
	}

	public SiacTOrdinativoTsDetFin addSiacTOrdinativoTsDet(SiacTOrdinativoTsDetFin siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().add(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacDOrdinativoTsDetTipo(this);

		return siacTOrdinativoTsDet;
	}

	public SiacTOrdinativoTsDetFin removeSiacTOrdinativoTsDet(SiacTOrdinativoTsDetFin siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().remove(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacDOrdinativoTsDetTipo(null);

		return siacTOrdinativoTsDet;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordTsDetTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordTsDetTipoId = uid;
	}
}