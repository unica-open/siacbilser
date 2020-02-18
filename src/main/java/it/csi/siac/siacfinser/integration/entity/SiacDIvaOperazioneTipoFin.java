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
 * The persistent class for the siac_d_iva_operazione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_operazione_tipo")
public class SiacDIvaOperazioneTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ivaop_tipo_id")
	private Integer ivaopTipoId;

	@Column(name="ivaop_tipo_code")
	private String ivaopTipoCode;

	@Column(name="ivaop_tipo_desc")
	private String ivaopTipoDesc;

	//bi-directional many-to-one association to SiacTIvaAliquotaFin
	@OneToMany(mappedBy="siacDIvaOperazioneTipo")
	private List<SiacTIvaAliquotaFin> siacTIvaAliquotas;

	public SiacDIvaOperazioneTipoFin() {
	}

	public Integer getIvaopTipoId() {
		return this.ivaopTipoId;
	}

	public void setIvaopTipoId(Integer ivaopTipoId) {
		this.ivaopTipoId = ivaopTipoId;
	}

	public String getIvaopTipoCode() {
		return this.ivaopTipoCode;
	}

	public void setIvaopTipoCode(String ivaopTipoCode) {
		this.ivaopTipoCode = ivaopTipoCode;
	}

	public String getIvaopTipoDesc() {
		return this.ivaopTipoDesc;
	}

	public void setIvaopTipoDesc(String ivaopTipoDesc) {
		this.ivaopTipoDesc = ivaopTipoDesc;
	}

	public List<SiacTIvaAliquotaFin> getSiacTIvaAliquotas() {
		return this.siacTIvaAliquotas;
	}

	public void setSiacTIvaAliquotas(List<SiacTIvaAliquotaFin> siacTIvaAliquotas) {
		this.siacTIvaAliquotas = siacTIvaAliquotas;
	}

	public SiacTIvaAliquotaFin addSiacTIvaAliquota(SiacTIvaAliquotaFin siacTIvaAliquota) {
		getSiacTIvaAliquotas().add(siacTIvaAliquota);
		siacTIvaAliquota.setSiacDIvaOperazioneTipo(this);

		return siacTIvaAliquota;
	}

	public SiacTIvaAliquotaFin removeSiacTIvaAliquota(SiacTIvaAliquotaFin siacTIvaAliquota) {
		getSiacTIvaAliquotas().remove(siacTIvaAliquota);
		siacTIvaAliquota.setSiacDIvaOperazioneTipo(null);

		return siacTIvaAliquota;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ivaopTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ivaopTipoId = uid;
	}

}