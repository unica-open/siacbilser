/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_vincolo database table.
 * 
 */
@Entity
@Table(name="siac_t_vincolo")
@NamedQuery(name="SiacTVincoloFin.findAll", query="SELECT s FROM SiacTVincoloFin s")
public class SiacTVincoloFin extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vincolo id. */
	@Id
	@SequenceGenerator(name="SIAC_T_VINCOLO_VINCOLOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_VINCOLO_VINCOLO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_VINCOLO_VINCOLOID_GENERATOR")
	@Column(name="vincolo_id")
	private Integer vincoloId;

	/** The vincolo code. */
	@Column(name="vincolo_code")
	private String vincoloCode;

	/** The vincolo desc. */
	@Column(name="vincolo_desc")
	private String vincoloDesc;

	//bi-directional many-to-one association to SiacRVincoloAttrFin
	/** The siac r vincolo attrs. */
	@OneToMany(mappedBy="siacTVincolo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRVincoloAttrFin> siacRVincoloAttrs;

	//bi-directional many-to-one association to SiacRVincoloBilElemFin
	/** The siac r vincolo bil elems. */
	@OneToMany(mappedBy="siacTVincolo")
	private List<SiacRVincoloBilElemFin> siacRVincoloBilElems;


	//bi-directional many-to-one association to SiacTPeriodoFin
	/** The siac t periodo. */
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodoFin siacTPeriodo;

	/**
	 * Instantiates a new siac t vincolo.
	 */
	public SiacTVincoloFin() {
	}

	/**
	 * Gets the vincolo id.
	 *
	 * @return the vincolo id
	 */
	public Integer getVincoloId() {
		return this.vincoloId;
	}

	/**
	 * Sets the vincolo id.
	 *
	 * @param vincoloId the new vincolo id
	 */
	public void setVincoloId(Integer vincoloId) {
		this.vincoloId = vincoloId;
	}

	/**
	 * Gets the vincolo code.
	 *
	 * @return the vincolo code
	 */
	public String getVincoloCode() {
		return this.vincoloCode;
	}

	/**
	 * Sets the vincolo code.
	 *
	 * @param vincoloCode the new vincolo code
	 */
	public void setVincoloCode(String vincoloCode) {
		this.vincoloCode = vincoloCode;
	}

	/**
	 * Gets the vincolo desc.
	 *
	 * @return the vincolo desc
	 */
	public String getVincoloDesc() {
		return this.vincoloDesc;
	}

	/**
	 * Sets the vincolo desc.
	 *
	 * @param vincoloDesc the new vincolo desc
	 */
	public void setVincoloDesc(String vincoloDesc) {
		this.vincoloDesc = vincoloDesc;
	}

	/**
	 * Gets the siac r vincolo attrs.
	 *
	 * @return the siac r vincolo attrs
	 */
	public List<SiacRVincoloAttrFin> getSiacRVincoloAttrs() {
		return this.siacRVincoloAttrs;
	}

	/**
	 * Sets the siac r vincolo attrs.
	 *
	 * @param siacRVincoloAttrs the new siac r vincolo attrs
	 */
	public void setSiacRVincoloAttrs(List<SiacRVincoloAttrFin> siacRVincoloAttrs) {
		this.siacRVincoloAttrs = siacRVincoloAttrs;
	}

	/**
	 * Adds the siac r vincolo attr.
	 *
	 * @param siacRVincoloAttr the siac r vincolo attr
	 * @return the siac r vincolo attr
	 */
	public SiacRVincoloAttrFin addSiacRVincoloAttr(SiacRVincoloAttrFin siacRVincoloAttr) {
		getSiacRVincoloAttrs().add(siacRVincoloAttr);
		siacRVincoloAttr.setSiacTVincolo(this);

		return siacRVincoloAttr;
	}

	/**
	 * Removes the siac r vincolo attr.
	 *
	 * @param siacRVincoloAttr the siac r vincolo attr
	 * @return the siac r vincolo attr
	 */
	public SiacRVincoloAttrFin removeSiacRVincoloAttr(SiacRVincoloAttrFin siacRVincoloAttr) {
		getSiacRVincoloAttrs().remove(siacRVincoloAttr);
		siacRVincoloAttr.setSiacTVincolo(null);

		return siacRVincoloAttr;
	}

	/**
	 * Gets the siac r vincolo bil elems.
	 *
	 * @return the siac r vincolo bil elems
	 */
	public List<SiacRVincoloBilElemFin> getSiacRVincoloBilElems() {
		return this.siacRVincoloBilElems;
	}

	/**
	 * Sets the siac r vincolo bil elems.
	 *
	 * @param siacRVincoloBilElems the new siac r vincolo bil elems
	 */
	public void setSiacRVincoloBilElems(List<SiacRVincoloBilElemFin> siacRVincoloBilElems) {
		this.siacRVincoloBilElems = siacRVincoloBilElems;
	}

	/**
	 * Adds the siac r vincolo bil elem.
	 *
	 * @param siacRVincoloBilElem the siac r vincolo bil elem
	 * @return the siac r vincolo bil elem
	 */
	public SiacRVincoloBilElemFin addSiacRVincoloBilElem(SiacRVincoloBilElemFin siacRVincoloBilElem) {
		getSiacRVincoloBilElems().add(siacRVincoloBilElem);
		siacRVincoloBilElem.setSiacTVincolo(this);

		return siacRVincoloBilElem;
	}

	/**
	 * Removes the siac r vincolo bil elem.
	 *
	 * @param siacRVincoloBilElem the siac r vincolo bil elem
	 * @return the siac r vincolo bil elem
	 */
	public SiacRVincoloBilElemFin removeSiacRVincoloBilElem(SiacRVincoloBilElemFin siacRVincoloBilElem) {
		getSiacRVincoloBilElems().remove(siacRVincoloBilElem);
		siacRVincoloBilElem.setSiacTVincolo(null);

		return siacRVincoloBilElem;
	}

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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return vincoloId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.vincoloId = uid;
	}

}