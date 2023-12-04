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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_periodo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_periodo_tipo")
public class SiacDPeriodoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The periodo tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_PERIODO_TIPO_PERIODOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PERIODO_TIPO_PERIODO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PERIODO_TIPO_PERIODOTIPOID_GENERATOR")
	@Column(name="periodo_tipo_id")
	private Integer periodoTipoId;

	/** The periodo tipo code. */
	@Column(name="periodo_tipo_code")
	private String periodoTipoCode;

	/** The periodo tipo desc. */
	@Column(name="periodo_tipo_desc")
	private String periodoTipoDesc;

	/*//bi-directional many-to-one association to SiacTEnteProprietario
	@ManyToOne
	@JoinColumn(name="ente_proprietario_id")
	private SiacTEnteProprietario siacTEnteProprietario;*/

	//bi-directional many-to-one association to SiacTPeriodo
	/** The siac t periodos. */
	@OneToMany(mappedBy="siacDPeriodoTipo")
	private List<SiacTPeriodo> siacTPeriodos;

	/**
	 * Instantiates a new siac d periodo tipo.
	 */
	public SiacDPeriodoTipo() {
	}

	/**
	 * Gets the periodo tipo id.
	 *
	 * @return the periodo tipo id
	 */
	public Integer getPeriodoTipoId() {
		return this.periodoTipoId;
	}

	/**
	 * Sets the periodo tipo id.
	 *
	 * @param periodoTipoId the new periodo tipo id
	 */
	public void setPeriodoTipoId(Integer periodoTipoId) {
		this.periodoTipoId = periodoTipoId;
	}

	/**
	 * Gets the periodo tipo code.
	 *
	 * @return the periodo tipo code
	 */
	public String getPeriodoTipoCode() {
		return this.periodoTipoCode;
	}

	/**
	 * Sets the periodo tipo code.
	 *
	 * @param periodoTipoCode the new periodo tipo code
	 */
	public void setPeriodoTipoCode(String periodoTipoCode) {
		this.periodoTipoCode = periodoTipoCode;
	}

	/**
	 * Gets the periodo tipo desc.
	 *
	 * @return the periodo tipo desc
	 */
	public String getPeriodoTipoDesc() {
		return this.periodoTipoDesc;
	}

	/**
	 * Sets the periodo tipo desc.
	 *
	 * @param periodoTipoDesc the new periodo tipo desc
	 */
	public void setPeriodoTipoDesc(String periodoTipoDesc) {
		this.periodoTipoDesc = periodoTipoDesc;
	}

	/**
	 * Gets the siac t periodos.
	 *
	 * @return the siac t periodos
	 */
	public List<SiacTPeriodo> getSiacTPeriodos() {
		return this.siacTPeriodos;
	}

	/**
	 * Sets the siac t periodos.
	 *
	 * @param siacTPeriodos the new siac t periodos
	 */
	public void setSiacTPeriodos(List<SiacTPeriodo> siacTPeriodos) {
		this.siacTPeriodos = siacTPeriodos;
	}

	/**
	 * Adds the siac t periodo.
	 *
	 * @param siacTPeriodo the siac t periodo
	 * @return the siac t periodo
	 */
	public SiacTPeriodo addSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		getSiacTPeriodos().add(siacTPeriodo);
		siacTPeriodo.setSiacDPeriodoTipo(this);

		return siacTPeriodo;
	}

	/**
	 * Removes the siac t periodo.
	 *
	 * @param siacTPeriodo the siac t periodo
	 * @return the siac t periodo
	 */
	public SiacTPeriodo removeSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		getSiacTPeriodos().remove(siacTPeriodo);
		siacTPeriodo.setSiacDPeriodoTipo(null);

		return siacTPeriodo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return periodoTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.periodoTipoId = uid;
	}

}