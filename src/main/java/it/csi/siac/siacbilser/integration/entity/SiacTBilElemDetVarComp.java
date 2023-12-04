/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the siac_t_bil_elem_det_var_comp database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_det_var_comp")
@NamedQuery(name="SiacTBilElemDetVarComp.findAll", query="SELECT s FROM SiacTBilElemDetVarComp s")
public class SiacTBilElemDetVarComp extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_DET_VAR_COMP_ELEMDETVARCOMPID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_BIL_ELEM_DET_VAR_COMP_ELEM_DET_VAR_COMP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_DET_VAR_COMP_ELEMDETVARCOMPID_GENERATOR")
	@Column(name="elem_det_var_comp_id")
	private Integer elemDetVarCompId;

	@Column(name="elem_det_importo")
	private BigDecimal elemDetImporto;

	/** The elem det flag. */
	@Column(name="elem_det_flag")
	private String elemDetFlag;

	//bi-directional many-to-one association to SiacTBilElemDetVar
	@ManyToOne
	@JoinColumn(name="elem_det_var_id")
	private SiacTBilElemDetVar siacTBilElemDetVar;
	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@ManyToOne
	@JoinColumn(name="elem_det_comp_id")
	private SiacTBilElemDetComp siacTBilElemDetComp;

	/**
	 * Instantiates a new siac t bil elem det var comp.
	 */
	public SiacTBilElemDetVarComp() {
	}

	/**
	 * @return the elemDetVarCompId
	 */
	public Integer getElemDetVarCompId() {
		return this.elemDetVarCompId;
	}

	/**
	 * @param elemDetVarCompId the elemDetVarCompId to set
	 */
	public void setElemDetVarCompId(Integer elemDetVarCompId) {
		this.elemDetVarCompId = elemDetVarCompId;
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
	 * @return the elemDetFlag
	 */
	public String getElemDetFlag() {
		return this.elemDetFlag;
	}

	/**
	 * @param elemDetFlag the elemDetFlag to set
	 */
	public void setElemDetFlag(String elemDetFlag) {
		this.elemDetFlag = elemDetFlag;
	}

	/**
	 * @return the siacTBilElemDetVar
	 */
	public SiacTBilElemDetVar getSiacTBilElemDetVar() {
		return this.siacTBilElemDetVar;
	}

	/**
	 * @param siacTBilElemDetVar the siacTBilElemDetVar to set
	 */
	public void setSiacTBilElemDetVar(SiacTBilElemDetVar siacTBilElemDetVar) {
		this.siacTBilElemDetVar = siacTBilElemDetVar;
	}

	/**
	 * @return the siacTBilElemDetComp
	 */
	public SiacTBilElemDetComp getSiacTBilElemDetComp() {
		return this.siacTBilElemDetComp;
	}

	/**
	 * @param siacTBilElemDetComp the siacTBilElemDetComp to set
	 */
	public void setSiacTBilElemDetComp(SiacTBilElemDetComp siacTBilElemDetComp) {
		this.siacTBilElemDetComp = siacTBilElemDetComp;
	}

	@Override
	public Integer getUid() {
		return elemDetVarCompId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetVarCompId = uid;
	}


}