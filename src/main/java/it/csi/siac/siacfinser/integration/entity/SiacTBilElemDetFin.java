/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
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
 * The persistent class for the siac_t_bil_elem_det database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_det")
public class SiacTBilElemDetFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_DET_ELEM_DET_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_bil_elem_det_elem_det_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_DET_ELEM_DET_ID_GENERATOR")
	@Column(name="elem_det_id")
	private Integer elemDetId;

	@Column(name="elem_det_flag")
	private String elemDetFlag;

	@Column(name="elem_det_importo")
	private BigDecimal elemDetImporto;

	//bi-directional many-to-one association to SiacDBilElemDetTipoFin
	@ManyToOne
	@JoinColumn(name="elem_det_tipo_id")
	private SiacDBilElemDetTipoFin siacDBilElemDetTipo;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTPeriodoFin
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodoFin siacTPeriodo;

	//bi-directional many-to-one association to SiacTBilElemDetVarFin
	@OneToMany(mappedBy="siacTBilElemDet")
	private List<SiacTBilElemDetVarFin> siacTBilElemDetVars;

	public SiacTBilElemDetFin() {
	}

	public Integer getElemDetId() {
		return this.elemDetId;
	}

	public void setElemDetId(Integer elemDetId) {
		this.elemDetId = elemDetId;
	}

	public String getElemDetFlag() {
		return this.elemDetFlag;
	}

	public void setElemDetFlag(String elemDetFlag) {
		this.elemDetFlag = elemDetFlag;
	}

	public BigDecimal getElemDetImporto() {
		return this.elemDetImporto;
	}

	public void setElemDetImporto(BigDecimal elemDetImporto) {
		this.elemDetImporto = elemDetImporto;
	}

	public SiacDBilElemDetTipoFin getSiacDBilElemDetTipo() {
		return this.siacDBilElemDetTipo;
	}

	public void setSiacDBilElemDetTipo(SiacDBilElemDetTipoFin siacDBilElemDetTipo) {
		this.siacDBilElemDetTipo = siacDBilElemDetTipo;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTPeriodoFin getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	public void setSiacTPeriodo(SiacTPeriodoFin siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	public List<SiacTBilElemDetVarFin> getSiacTBilElemDetVars() {
		return this.siacTBilElemDetVars;
	}

	public void setSiacTBilElemDetVars(List<SiacTBilElemDetVarFin> siacTBilElemDetVars) {
		this.siacTBilElemDetVars = siacTBilElemDetVars;
	}

	public SiacTBilElemDetVarFin addSiacTBilElemDetVar(SiacTBilElemDetVarFin siacTBilElemDetVar) {
		getSiacTBilElemDetVars().add(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElemDet(this);

		return siacTBilElemDetVar;
	}

	public SiacTBilElemDetVarFin removeSiacTBilElemDetVar(SiacTBilElemDetVarFin siacTBilElemDetVar) {
		getSiacTBilElemDetVars().remove(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElemDet(null);

		return siacTBilElemDetVar;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemDetId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemDetId = uid;
	}
}