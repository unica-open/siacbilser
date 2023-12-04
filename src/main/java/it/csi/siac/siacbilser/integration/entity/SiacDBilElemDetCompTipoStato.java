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
 * The persistent class for the siac_d_bil_elem_det_comp_tipo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_comp_tipo_stato")
@NamedQuery(name="SiacDBilElemDetCompTipoStato.findAll", query="SELECT s FROM SiacDBilElemDetCompTipoStato s")
public class SiacDBilElemDetCompTipoStato extends SiacTEnteBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_COMP_TIPO_STATO_ELEMDETCOMPTIPOSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_COMP_TIPO__ELEM_DET_COMP_TIPO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_COMP_TIPO_STATO_ELEMDETCOMPTIPOSTATOID_GENERATOR")
	@Column(name="elem_det_comp_tipo_stato_id")
	private Integer elemDetCompTipoStatoId;

	@Column(name="elem_det_comp_tipo_stato_code")
	private String elemDetCompTipoStatoCode;

	@Column(name="elem_det_comp_tipo_stato_desc")
	private String elemDetCompTipoStatoDesc;

	//bi-directional many-to-one association to SiacDBilElemDetTipoStato
	@OneToMany(mappedBy="siacDBilElemDetCompTipoStato")
	private List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo.
	 */
	public SiacDBilElemDetCompTipoStato() {
	}

	/**
	 * @return the elemDetCompTipoStatoId
	 */
	public Integer getElemDetCompTipoStatoId() {
		return this.elemDetCompTipoStatoId;
	}

	/**
	 * @param elemDetCompTipoStatoId the elemDetCompTipoStatoId to set
	 */
	public void setElemDetCompTipoStatoId(Integer elemDetCompTipoStatoId) {
		this.elemDetCompTipoStatoId = elemDetCompTipoStatoId;
	}

	/**
	 * @return the elemDetCompTipoStatoCode
	 */
	public String getElemDetCompTipoStatoCode() {
		return this.elemDetCompTipoStatoCode;
	}

	/**
	 * @param elemDetCompTipoStatoCode the elemDetCompTipoStatoCode to set
	 */
	public void setElemDetCompTipoStatoCode(String elemDetCompTipoStatoCode) {
		this.elemDetCompTipoStatoCode = elemDetCompTipoStatoCode;
	}

	/**
	 * @return the elemDetCompTipoStatoDesc
	 */
	public String getElemDetCompTipoStatoDesc() {
		return this.elemDetCompTipoStatoDesc;
	}

	/**
	 * @param elemDetCompTipoStatoDesc the elemDetCompTipoStatoDesc to set
	 */
	public void setElemDetCompTipoStatoDesc(String elemDetCompTipoStatoDesc) {
		this.elemDetCompTipoStatoDesc = elemDetCompTipoStatoDesc;
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
		return elemDetCompTipoStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemDetCompTipoStatoId = uid;
	}


}