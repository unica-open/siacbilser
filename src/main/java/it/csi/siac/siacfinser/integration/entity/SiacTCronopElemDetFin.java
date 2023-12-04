/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_cronop_elem_det database table.
 * 
 */
@Entity
@Table(name="siac_t_cronop_elem_det")
public class SiacTCronopElemDetFin extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop elem det id. */
	@Id
	@SequenceGenerator(name="SIAC_T_CRONOP_ELEM_DET_CRONOPELEMDETID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CRONOP_ELEM_DET_CRONOP_ELEM_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CRONOP_ELEM_DET_CRONOPELEMDETID_GENERATOR")
	@Column(name="cronop_elem_det_id")
	private Integer cronopElemDetId;

	/** The anno entrata. */
	@Column(name="anno_entrata")
	private String annoEntrata;

	/** The cronop elem det desc. */
	@Column(name="cronop_elem_det_desc")
	private String cronopElemDetDesc;

	/** The cronop elem det importo. */
	@Column(name="cronop_elem_det_importo")
	private BigDecimal cronopElemDetImporto;

	/** The siac d bil elem det tipo. */
	@ManyToOne
	@JoinColumn(name="elem_det_tipo_id")
	private SiacDBilElemDetTipoFin siacDBilElemDetTipo;

	/** The siac t periodo. */
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodoFin siacTPeriodo;

	//bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elem. */
	@ManyToOne
	@JoinColumn(name="cronop_elem_id")
	private SiacTCronopElemFin siacTCronopElem;
	
	@Column(name="quadro_economico_det_importo")
	private BigDecimal quadroEconomicoDetImporto;

	/**
	 * Instantiates a new siac t cronop elem det.
	 */
	public SiacTCronopElemDetFin() {
	}

	/**
	 * Gets the cronop elem det id.
	 *
	 * @return the cronop elem det id
	 */
	public Integer getCronopElemDetId() {
		return this.cronopElemDetId;
	}

	/**
	 * Sets the cronop elem det id.
	 *
	 * @param cronopElemDetId the new cronop elem det id
	 */
	public void setCronopElemDetId(Integer cronopElemDetId) {
		this.cronopElemDetId = cronopElemDetId;
	}

	/**
	 * Gets the anno entrata.
	 *
	 * @return the anno entrata
	 */
	public String getAnnoEntrata() {
		return this.annoEntrata;
	}

	/**
	 * Sets the anno entrata.
	 *
	 * @param annoEntrata the new anno entrata
	 */
	public void setAnnoEntrata(String annoEntrata) {
		this.annoEntrata = annoEntrata;
	}

	/**
	 * Gets the cronop elem det desc.
	 *
	 * @return the cronop elem det desc
	 */
	public String getCronopElemDetDesc() {
		return this.cronopElemDetDesc;
	}

	/**
	 * Sets the cronop elem det desc.
	 *
	 * @param cronopElemDetDesc the new cronop elem det desc
	 */
	public void setCronopElemDetDesc(String cronopElemDetDesc) {
		this.cronopElemDetDesc = cronopElemDetDesc;
	}

	/**
	 * Gets the cronop elem det importo.
	 *
	 * @return the cronop elem det importo
	 */
	public BigDecimal getCronopElemDetImporto() {
		return this.cronopElemDetImporto;
	}

	/**
	 * Sets the cronop elem det importo.
	 *
	 * @param cronopElemDetImporto the new cronop elem det importo
	 */
	public void setCronopElemDetImporto(BigDecimal cronopElemDetImporto) {
		this.cronopElemDetImporto = cronopElemDetImporto;
	}

//	public Integer getElemDetTipoId() {
//		return this.elemDetTipoId;
//	}
//
//	public void setElemDetTipoId(Integer elemDetTipoId) {
//		this.elemDetTipoId = elemDetTipoId;
//	}
	
	/**
 * Gets the siac d bil elem det tipo.
 *
 * @return the siac d bil elem det tipo
 */
public SiacDBilElemDetTipoFin getSiacDBilElemDetTipo() {
		return this.siacDBilElemDetTipo;
	}

	/**
	 * Sets the siac d bil elem det tipo.
	 *
	 * @param siacDBilElemDetTipo the new siac d bil elem det tipo
	 */
	public void setSiacDBilElemDetTipo(SiacDBilElemDetTipoFin siacDBilElemDetTipo) {
		this.siacDBilElemDetTipo = siacDBilElemDetTipo;
	}

//	public Integer getPeriodoId() {
//		return this.periodoId;
//	}
//
//	public void setPeriodoId(Integer periodoId) {
//		this.periodoId = periodoId;
//	}
	
	/**
 * Gets the siac t periodo.
 *
 * @return the siac t periodo
 */
public SiacTPeriodoFin getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	/**
	 * Sets the siac t periodo.
	 *
	 * @param siacTPeriodo the new siac t periodo
	 */
	public void setSiacTPeriodo(SiacTPeriodoFin siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * Gets the siac t cronop elem.
	 *
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElemFin getSiacTCronopElem() {
		return this.siacTCronopElem;
	}

	/**
	 * Sets the siac t cronop elem.
	 *
	 * @param siacTCronopElem the new siac t cronop elem
	 */
	public void setSiacTCronopElem(SiacTCronopElemFin siacTCronopElem) {
		this.siacTCronopElem = siacTCronopElem;
	}
	
	/**
	 * @return the quadroEconomicoDetImporto
	 */
	public BigDecimal getQuadroEconomicoDetImporto() {
		return quadroEconomicoDetImporto;
	}

	/**
	 * @param quadroEconomicoDetImporto the quadroEconomicoDetImporto to set
	 */
	public void setQuadroEconomicoDetImporto(BigDecimal quadroEconomicoDetImporto) {
		this.quadroEconomicoDetImporto = quadroEconomicoDetImporto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopElemDetId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopElemDetId = uid;
		
	}

}