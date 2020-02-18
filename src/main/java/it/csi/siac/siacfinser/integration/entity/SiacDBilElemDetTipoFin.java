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
 * The persistent class for the siac_d_bil_elem_det_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_tipo")
public class SiacDBilElemDetTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_TIPO_ELEM_DET_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_bil_elem_det_tipo_elem_det_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_TIPO_ELEM_DET_TIPO_ID_GENERATOR")
	@Column(name="elem_det_tipo_id")
	private Integer elemDetTipoId;

	@Column(name="elem_det_tipo_code")
	private String elemDetTipoCode;

	@Column(name="elem_det_tipo_desc")
	private String elemDetTipoDesc;

	//bi-directional many-to-one association to SiacTBilElemDetFin
	@OneToMany(mappedBy="siacDBilElemDetTipo")
	private List<SiacTBilElemDetFin> siacTBilElemDets;

	public SiacDBilElemDetTipoFin() {
	}

	public Integer getElemDetTipoId() {
		return this.elemDetTipoId;
	}

	public void setElemDetTipoId(Integer elemDetTipoId) {
		this.elemDetTipoId = elemDetTipoId;
	}

	public String getElemDetTipoCode() {
		return this.elemDetTipoCode;
	}

	public void setElemDetTipoCode(String elemDetTipoCode) {
		this.elemDetTipoCode = elemDetTipoCode;
	}

	public String getElemDetTipoDesc() {
		return this.elemDetTipoDesc;
	}

	public void setElemDetTipoDesc(String elemDetTipoDesc) {
		this.elemDetTipoDesc = elemDetTipoDesc;
	}

	public List<SiacTBilElemDetFin> getSiacTBilElemDets() {
		return this.siacTBilElemDets;
	}

	public void setSiacTBilElemDets(List<SiacTBilElemDetFin> siacTBilElemDets) {
		this.siacTBilElemDets = siacTBilElemDets;
	}

	public SiacTBilElemDetFin addSiacTBilElemDet(SiacTBilElemDetFin siacTBilElemDet) {
		getSiacTBilElemDets().add(siacTBilElemDet);
		siacTBilElemDet.setSiacDBilElemDetTipo(this);

		return siacTBilElemDet;
	}

	public SiacTBilElemDetFin removeSiacTBilElemDet(SiacTBilElemDetFin siacTBilElemDet) {
		getSiacTBilElemDets().remove(siacTBilElemDet);
		siacTBilElemDet.setSiacDBilElemDetTipo(null);

		return siacTBilElemDet;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemDetTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemDetTipoId = uid;
	}
}