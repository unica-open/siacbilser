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
 * The persistent class for the siac_d_bil_elem_det_comp_tipo_imp database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo_imp")
@NamedQuery(name="SiacDBilElemDetCompTipoImp.findAll", query="SELECT s FROM SiacDBilElemDetCompTipoImp s")
public class SiacDBilElemDetCompTipoImp extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_TIPO_IMP_ELEMDETCOMPTIPOIMPID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO_IMP_ELEM_DET_COMP_TIPO_IMP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_TIPO_IMP_ELEMDETCOMPTIPOIMPID_GENERATOR")
	@Column(name="elem_det_comp_tipo_imp_id")
	private Integer elemDetCompTipoImpId;

	@Column(name="elem_det_comp_tipo_imp_code")
	private String elemDetCompTipoImpCode;

	@Column(name="elem_det_comp_tipo_imp_desc")
	private String elemDetCompTipoImpDesc;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@OneToMany(mappedBy="siacDBilElemDetCompTipoImp")
	private List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipoImp() {
	}


	 


	/**
	 * @return the elemDetCompTipoImpId
	 */
	public Integer getElemDetCompTipoImpId() {
		return elemDetCompTipoImpId;
	}





	/**
	 * @param elemDetCompTipoImpId the elemDetCompTipoImpId to set
	 */
	public void setElemDetCompTipoImpId(Integer elemDetCompTipoImpId) {
		this.elemDetCompTipoImpId = elemDetCompTipoImpId;
	}





	/**
	 * @return the elemDetCompTipoImpCode
	 */
	public String getElemDetCompTipoImpCode() {
		return elemDetCompTipoImpCode;
	}





	/**
	 * @param elemDetCompTipoImpCode the elemDetCompTipoImpCode to set
	 */
	public void setElemDetCompTipoImpCode(String elemDetCompTipoImpCode) {
		this.elemDetCompTipoImpCode = elemDetCompTipoImpCode;
	}





	/**
	 * @return the elemDetCompTipoImpDesc
	 */
	public String getElemDetCompTipoImpDesc() {
		return elemDetCompTipoImpDesc;
	}





	/**
	 * @param elemDetCompTipoImpDesc the elemDetCompTipoImpDesc to set
	 */
	public void setElemDetCompTipoImpDesc(String elemDetCompTipoImpDesc) {
		this.elemDetCompTipoImpDesc = elemDetCompTipoImpDesc;
	}





	/**
	 * @return the siacDBilElemDetCompTipos
	 */
	public List<SiacDBilElemDetCompTipo> getSiacDBilElemDetCompTipos() {
		return this.siacDBilElemDetCompTipos;
	}


	/**
	 * @param siacDBilElemDetCompTipos the siacDBilElemDetCompTipos to set
	 */
	public void setSiacDBilElemDetCompTipos(List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos) {
		this.siacDBilElemDetCompTipos = siacDBilElemDetCompTipos;
	}


	@Override
	public Integer getUid() {
		return elemDetCompTipoImpId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompTipoImpId = uid;
	}


}