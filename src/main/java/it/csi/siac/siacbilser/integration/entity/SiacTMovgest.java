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
 * The persistent class for the siac_t_movgest database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest")
@NamedQuery(name="SiacTMovgest.findAll", query="SELECT s FROM SiacTMovgest s")
public class SiacTMovgest extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_MOVGESTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOVGEST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_MOVGESTID_GENERATOR")
	@Column(name="movgest_id")
	private Integer movgestId;

	/** The movgest anno. */
	@Column(name="movgest_anno")
	private Integer movgestAnno;

	/** The movgest desc. */
	@Column(name="movgest_desc")
	private String movgestDesc;

	/** The movgest numero. */
	@Column(name="movgest_numero")
	private BigDecimal movgestNumero;

	//bi-directional many-to-one association to SiacRMovgestBilElem
	/** The siac r movgest bil elems. */
	@OneToMany(mappedBy="siacTMovgest")
	private List<SiacRMovgestBilElem> siacRMovgestBilElems;

	//bi-directional many-to-one association to SiacDMovgestTipo
	/** The siac d movgest tipo. */
	@ManyToOne
	@JoinColumn(name="movgest_tipo_id")
	private SiacDMovgestTipo siacDMovgestTipo;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest ts. */
	@OneToMany(mappedBy="siacTMovgest")
	private List<SiacTMovgestT> siacTMovgestTs;

	/**
	 * Instantiates a new siac t movgest.
	 */
	public SiacTMovgest() {
	}

	/**
	 * Gets the movgest id.
	 *
	 * @return the movgest id
	 */
	public Integer getMovgestId() {
		return this.movgestId;
	}

	/**
	 * Sets the movgest id.
	 *
	 * @param movgestId the new movgest id
	 */
	public void setMovgestId(Integer movgestId) {
		this.movgestId = movgestId;
	}

	/**
	 * Gets the movgest anno.
	 *
	 * @return the movgest anno
	 */
	public Integer getMovgestAnno() {
		return this.movgestAnno;
	}

	/**
	 * Sets the movgest anno.
	 *
	 * @param movgestAnno the new movgest anno
	 */
	public void setMovgestAnno(Integer movgestAnno) {
		this.movgestAnno = movgestAnno;
	}

	/**
	 * Gets the movgest desc.
	 *
	 * @return the movgest desc
	 */
	public String getMovgestDesc() {
		return this.movgestDesc;
	}

	/**
	 * Sets the movgest desc.
	 *
	 * @param movgestDesc the new movgest desc
	 */
	public void setMovgestDesc(String movgestDesc) {
		this.movgestDesc = movgestDesc;
	}

	/**
	 * Gets the movgest numero.
	 *
	 * @return the movgest numero
	 */
	public BigDecimal getMovgestNumero() {
		return this.movgestNumero;
	}

	/**
	 * Sets the movgest numero.
	 *
	 * @param movgestNumero the new movgest numero
	 */
	public void setMovgestNumero(BigDecimal movgestNumero) {
		this.movgestNumero = movgestNumero;
	}


	/**
	 * Gets the siac r movgest bil elems.
	 *
	 * @return the siac r movgest bil elems
	 */
	public List<SiacRMovgestBilElem> getSiacRMovgestBilElems() {
		return this.siacRMovgestBilElems;
	}

	/**
	 * Sets the siac r movgest bil elems.
	 *
	 * @param siacRMovgestBilElems the new siac r movgest bil elems
	 */
	public void setSiacRMovgestBilElems(List<SiacRMovgestBilElem> siacRMovgestBilElems) {
		this.siacRMovgestBilElems = siacRMovgestBilElems;
	}

	/**
	 * Adds the siac r movgest bil elem.
	 *
	 * @param siacRMovgestBilElem the siac r movgest bil elem
	 * @return the siac r movgest bil elem
	 */
	public SiacRMovgestBilElem addSiacRMovgestBilElem(SiacRMovgestBilElem siacRMovgestBilElem) {
		getSiacRMovgestBilElems().add(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTMovgest(this);

		return siacRMovgestBilElem;
	}

	/**
	 * Removes the siac r movgest bil elem.
	 *
	 * @param siacRMovgestBilElem the siac r movgest bil elem
	 * @return the siac r movgest bil elem
	 */
	public SiacRMovgestBilElem removeSiacRMovgestBilElem(SiacRMovgestBilElem siacRMovgestBilElem) {
		getSiacRMovgestBilElems().remove(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTMovgest(null);

		return siacRMovgestBilElem;
	}

	/**
	 * Gets the siac d movgest tipo.
	 *
	 * @return the siac d movgest tipo
	 */
	public SiacDMovgestTipo getSiacDMovgestTipo() {
		return this.siacDMovgestTipo;
	}

	/**
	 * Sets the siac d movgest tipo.
	 *
	 * @param siacDMovgestTipo the new siac d movgest tipo
	 */
	public void setSiacDMovgestTipo(SiacDMovgestTipo siacDMovgestTipo) {
		this.siacDMovgestTipo = siacDMovgestTipo;
	}

	/**
	 * Gets the siac t bil.
	 *
	 * @return the siac t bil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the new siac t bil
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/**
	 * Gets the siac t movgest ts.
	 *
	 * @return the siac t movgest ts
	 */
	public List<SiacTMovgestT> getSiacTMovgestTs() {
		return this.siacTMovgestTs;
	}

	/**
	 * Sets the siac t movgest ts.
	 *
	 * @param siacTMovgestTs the new siac t movgest ts
	 */
	public void setSiacTMovgestTs(List<SiacTMovgestT> siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}

	/**
	 * Adds the siac t movgest t.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT addSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		getSiacTMovgestTs().add(siacTMovgestT);
		siacTMovgestT.setSiacTMovgest(this);

		return siacTMovgestT;
	}

	/**
	 * Removes the siac t movgest t.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT removeSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		getSiacTMovgestTs().remove(siacTMovgestT);
		siacTMovgestT.setSiacTMovgest(null);

		return siacTMovgestT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestId = uid;
	}

}