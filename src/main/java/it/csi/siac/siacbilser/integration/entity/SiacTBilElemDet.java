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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_bil_elem_det database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_det")
public class SiacTBilElemDet extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem det id. */
	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_DET_ELEMDETID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_BIL_ELEM_DET_ELEM_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_DET_ELEMDETID_GENERATOR")
	@Column(name="elem_det_id")
	private Integer elemDetId;
	

	/** The elem det flag. */
	@Column(name="elem_det_flag")
	private String elemDetFlag;

	/** The elem det importo. */
	@Column(name="elem_det_importo")
	private BigDecimal elemDetImporto;

	
	//bi-directional many-to-one association to SiacDBilElemDetTipo
	/** The siac d bil elem det tipo. */
	@ManyToOne
	@JoinColumn(name="elem_det_tipo_id")
	private SiacDBilElemDetTipo siacDBilElemDetTipo;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTPeriodo
	/** The siac t periodo. */
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;

	//bi-directional many-to-one association to SiacTBilElemDetVar
	/** The siac t bil elem det vars. */
	@OneToMany(mappedBy="siacTBilElemDet")
	private List<SiacTBilElemDetVar> siacTBilElemDetVars;
	//bi-directional many-to-one association to SiacTBilElemDetComp
	@OneToMany(mappedBy="siacTBilElemDet")
	private List<SiacTBilElemDetComp> siacTBilElemDetComps;

	/**
	 * Instantiates a new siac t bil elem det.
	 */
	public SiacTBilElemDet() {
	}

	/**
	 * Gets the elem det id.
	 *
	 * @return the elem det id
	 */
	public Integer getElemDetId() {
		return this.elemDetId;
	}

	/**
	 * Sets the elem det id.
	 *
	 * @param elemDetId the new elem det id
	 */
	public void setElemDetId(Integer elemDetId) {
		this.elemDetId = elemDetId;
	}



	/**
	 * Gets the elem det flag.
	 *
	 * @return the elem det flag
	 */
	public String getElemDetFlag() {
		return this.elemDetFlag;
	}

	/**
	 * Sets the elem det flag.
	 *
	 * @param elemDetFlag the new elem det flag
	 */
	public void setElemDetFlag(String elemDetFlag) {
		this.elemDetFlag = elemDetFlag;
	}

	/**
	 * Gets the elem det importo.
	 *
	 * @return the elem det importo
	 */
	public BigDecimal getElemDetImporto() {
		return this.elemDetImporto;
	}

	/**
	 * Sets the elem det importo.
	 *
	 * @param elemDetImporto the new elem det importo
	 */
	public void setElemDetImporto(BigDecimal elemDetImporto) {
		this.elemDetImporto = elemDetImporto;
	}

	

	/**
	 * Gets the siac d bil elem det tipo.
	 *
	 * @return the siac d bil elem det tipo
	 */
	public SiacDBilElemDetTipo getSiacDBilElemDetTipo() {
		return this.siacDBilElemDetTipo;
	}

	/**
	 * Sets the siac d bil elem det tipo.
	 *
	 * @param siacDBilElemDetTipo the new siac d bil elem det tipo
	 */
	public void setSiacDBilElemDetTipo(SiacDBilElemDetTipo siacDBilElemDetTipo) {
		this.siacDBilElemDetTipo = siacDBilElemDetTipo;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	

	/**
	 * Gets the siac t periodo.
	 *
	 * @return the siac t periodo
	 */
	public SiacTPeriodo getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	/**
	 * Sets the siac t periodo.
	 *
	 * @param siacTPeriodo the new siac t periodo
	 */
	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * Gets the siac t bil elem det vars.
	 *
	 * @return the siac t bil elem det vars
	 */
	public List<SiacTBilElemDetVar> getSiacTBilElemDetVars() {
		return this.siacTBilElemDetVars;
	}

	/**
	 * Sets the siac t bil elem det vars.
	 *
	 * @param siacTBilElemDetVars the new siac t bil elem det vars
	 */
	public void setSiacTBilElemDetVars(List<SiacTBilElemDetVar> siacTBilElemDetVars) {
		this.siacTBilElemDetVars = siacTBilElemDetVars;
	}

	/**
	 * Adds the siac t bil elem det var.
	 *
	 * @param siacTBilElemDetVar the siac t bil elem det var
	 * @return the siac t bil elem det var
	 */
	public SiacTBilElemDetVar addSiacTBilElemDetVar(SiacTBilElemDetVar siacTBilElemDetVar) {
		getSiacTBilElemDetVars().add(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElemDet(this);

		return siacTBilElemDetVar;
	}

	/**
	 * Removes the siac t bil elem det var.
	 *
	 * @param siacTBilElemDetVar the siac t bil elem det var
	 * @return the siac t bil elem det var
	 */
	public SiacTBilElemDetVar removeSiacTBilElemDetVar(SiacTBilElemDetVar siacTBilElemDetVar) {
		getSiacTBilElemDetVars().remove(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElemDet(null);

		return siacTBilElemDetVar;
	}

	/**
	 * @return the siacTBilElemDetComps
	 */
	public List<SiacTBilElemDetComp> getSiacTBilElemDetComps() {
		return this.siacTBilElemDetComps;
	}

	/**
	 * @param siacTBilElemDetComps the siacTBilElemDetComps to set
	 */
	public void setSiacTBilElemDetComps(List<SiacTBilElemDetComp> siacTBilElemDetComps) {
		this.siacTBilElemDetComps = siacTBilElemDetComps;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemDetId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemDetId = uid;
		
	}

}