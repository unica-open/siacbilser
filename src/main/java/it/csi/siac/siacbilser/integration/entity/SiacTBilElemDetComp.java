/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the siac_t_bil_elem_det_comp database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_det_comp")
@NamedQuery(name="SiacTBilElemDetComp.findAll", query="SELECT s FROM SiacTBilElemDetComp s")
public class SiacTBilElemDetComp extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_DET_COMP_ELEMDETCOMPID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_BIL_ELEM_DET_COMP_ELEM_DET_COMP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_DET_COMP_ELEMDETCOMPID_GENERATOR")
	@Column(name="elem_det_comp_id")
	private Integer elemDetCompId;

	@Column(name="elem_det_importo")
	private BigDecimal elemDetImporto;

	//bi-directional many-to-one association to SiacTBilElemDet
	@ManyToOne
	@JoinColumn(name="elem_det_id")
	private SiacTBilElemDet siacTBilElemDet;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_tipo_id")
	private SiacDBilElemDetCompTipo siacDBilElemDetCompTipo;
	//bi-directional many-to-one association to SiacTBilElemDetVarComp
	@OneToMany(mappedBy="siacTBilElemDetComp")
	private List<SiacTBilElemDetVarComp> siacTBilElemDetVarComps;

	/**
	 * Instantiates a new siac d bil elem det comp.
	 */
	public SiacTBilElemDetComp() {
	}

	/**
	 * @return the elemDetCompId
	 */
	public Integer getElemDetCompId() {
		return this.elemDetCompId;
	}

	/**
	 * @param elemDetCompId the elemDetCompId to set
	 */
	public void setElemDetCompId(Integer elemDetCompId) {
		this.elemDetCompId = elemDetCompId;
	}

	/**
	 * @return the elemDetImporto
	 */
	public BigDecimal getElemDetImporto() {
		return this.elemDetImporto;
	}

	/**
	 * @param elemDetImporto the elemDetImporto to set
	 */
	public void setElemDetImporto(BigDecimal elemDetImporto) {
		this.elemDetImporto = elemDetImporto;
	}

	/**
	 * @return the siacTBilElemDet
	 */
	public SiacTBilElemDet getSiacTBilElemDet() {
		return this.siacTBilElemDet;
	}

	/**
	 * @param siacTBilElemDet the siacTBilElemDet to set
	 */
	public void setSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		this.siacTBilElemDet = siacTBilElemDet;
	}

	/**
	 * @return the siacDBilElemDetCompTipo
	 */
	public SiacDBilElemDetCompTipo getSiacDBilElemDetCompTipo() {
		return this.siacDBilElemDetCompTipo;
	}

	/**
	 * @param siacDBilElemDetCompTipo the siacDBilElemDetCompTipo to set
	 */
	public void setSiacDBilElemDetCompTipo(SiacDBilElemDetCompTipo siacDBilElemDetCompTipo) {
		this.siacDBilElemDetCompTipo = siacDBilElemDetCompTipo;
	}

	/**
	 * @return the siacTBilElemDetVarComps
	 */
	public List<SiacTBilElemDetVarComp> getSiacTBilElemDetVarComps() {
		return this.siacTBilElemDetVarComps;
	}

	/**
	 * @param siacTBilElemDetVarComps the siacTBilElemDetVarComps to set
	 */
	public void setSiacTBilElemDetVarComps(List<SiacTBilElemDetVarComp> siacTBilElemDetVarComps) {
		this.siacTBilElemDetVarComps = siacTBilElemDetVarComps;
	}

	@Override
	public Integer getUid() {
		return elemDetCompId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompId = uid;
	}


}