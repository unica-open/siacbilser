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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_bil_elem_det_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_det_tipo")
@NamedQuery(name="SiacDBilElemDetTipo.findAll", query="SELECT s FROM SiacDBilElemDetTipo s")
public class SiacDBilElemDetTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem det tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_DET_TIPO_ELEMDETTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_DET_TIPO_ELEM_DET_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_DET_TIPO_ELEMDETTIPOID_GENERATOR")
	@Column(name="elem_det_tipo_id")
	private Integer elemDetTipoId;

	/** The elem det tipo code. */
	@Column(name="elem_det_tipo_code")
	private String elemDetTipoCode;

	/** The elem det tipo desc. */
	@Column(name="elem_det_tipo_desc")
	private String elemDetTipoDesc;

	//bi-directional many-to-one association to SiacTBilElemDet
	/** The siac t bil elem dets. */
	@OneToMany(mappedBy="siacDBilElemDetTipo")
	private List<SiacTBilElemDet> siacTBilElemDets;

	/**
	 * Instantiates a new siac d bil elem det tipo.
	 */
	public SiacDBilElemDetTipo() {
	}

	/**
	 * Gets the elem det tipo id.
	 *
	 * @return the elem det tipo id
	 */
	public Integer getElemDetTipoId() {
		return this.elemDetTipoId;
	}

	/**
	 * Sets the elem det tipo id.
	 *
	 * @param elemDetTipoId the new elem det tipo id
	 */
	public void setElemDetTipoId(Integer elemDetTipoId) {
		this.elemDetTipoId = elemDetTipoId;
	}

	/**
	 * Gets the elem det tipo code.
	 *
	 * @return the elem det tipo code
	 */
	public String getElemDetTipoCode() {
		return this.elemDetTipoCode;
	}

	/**
	 * Sets the elem det tipo code.
	 *
	 * @param elemDetTipoCode the new elem det tipo code
	 */
	public void setElemDetTipoCode(String elemDetTipoCode) {
		this.elemDetTipoCode = elemDetTipoCode;
	}

	/**
	 * Gets the elem det tipo desc.
	 *
	 * @return the elem det tipo desc
	 */
	public String getElemDetTipoDesc() {
		return this.elemDetTipoDesc;
	}

	/**
	 * Sets the elem det tipo desc.
	 *
	 * @param elemDetTipoDesc the new elem det tipo desc
	 */
	public void setElemDetTipoDesc(String elemDetTipoDesc) {
		this.elemDetTipoDesc = elemDetTipoDesc;
	}

	/**
	 * Gets the siac t bil elem dets.
	 *
	 * @return the siac t bil elem dets
	 */
	public List<SiacTBilElemDet> getSiacTBilElemDets() {
		return this.siacTBilElemDets;
	}

	/**
	 * Sets the siac t bil elem dets.
	 *
	 * @param siacTBilElemDets the new siac t bil elem dets
	 */
	public void setSiacTBilElemDets(List<SiacTBilElemDet> siacTBilElemDets) {
		this.siacTBilElemDets = siacTBilElemDets;
	}

	/**
	 * Adds the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet addSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().add(siacTBilElemDet);
		siacTBilElemDet.setSiacDBilElemDetTipo(this);

		return siacTBilElemDet;
	}

	/**
	 * Removes the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet removeSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().remove(siacTBilElemDet);
		siacTBilElemDet.setSiacDBilElemDetTipo(null);

		return siacTBilElemDet;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemDetTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemDetTipoId = uid;
	}

}