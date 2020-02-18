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
 * The persistent class for the siac_d_movgest_ts_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_ts_tipo")
@NamedQuery(name="SiacDMovgestTsTipo.findAll", query="SELECT s FROM SiacDMovgestTsTipo s")
public class SiacDMovgestTsTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MOVGEST_TS_TIPO_MOVGESTTSTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MOVGEST_TS_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MOVGEST_TS_TIPO_MOVGESTTSTIPOID_GENERATOR")
	@Column(name="movgest_ts_tipo_id")
	private Integer movgestTsTipoId;

	/** The movgest ts tipo code. */
	@Column(name="movgest_ts_tipo_code")
	private String movgestTsTipoCode;

	/** The movgest ts tipo desc. */
	@Column(name="movgest_ts_tipo_desc")
	private String movgestTsTipoDesc;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest ts. */
	@OneToMany(mappedBy="siacDMovgestTsTipo")
	private List<SiacTMovgestT> siacTMovgestTs;

	/**
	 * Instantiates a new siac d movgest ts tipo.
	 */
	public SiacDMovgestTsTipo() {
	}

	/**
	 * Gets the movgest ts tipo id.
	 *
	 * @return the movgest ts tipo id
	 */
	public Integer getMovgestTsTipoId() {
		return this.movgestTsTipoId;
	}

	/**
	 * Sets the movgest ts tipo id.
	 *
	 * @param movgestTsTipoId the new movgest ts tipo id
	 */
	public void setMovgestTsTipoId(Integer movgestTsTipoId) {
		this.movgestTsTipoId = movgestTsTipoId;
	}

	/**
	 * Gets the movgest ts tipo code.
	 *
	 * @return the movgest ts tipo code
	 */
	public String getMovgestTsTipoCode() {
		return this.movgestTsTipoCode;
	}

	/**
	 * Sets the movgest ts tipo code.
	 *
	 * @param movgestTsTipoCode the new movgest ts tipo code
	 */
	public void setMovgestTsTipoCode(String movgestTsTipoCode) {
		this.movgestTsTipoCode = movgestTsTipoCode;
	}

	/**
	 * Gets the movgest ts tipo desc.
	 *
	 * @return the movgest ts tipo desc
	 */
	public String getMovgestTsTipoDesc() {
		return this.movgestTsTipoDesc;
	}

	/**
	 * Sets the movgest ts tipo desc.
	 *
	 * @param movgestTsTipoDesc the new movgest ts tipo desc
	 */
	public void setMovgestTsTipoDesc(String movgestTsTipoDesc) {
		this.movgestTsTipoDesc = movgestTsTipoDesc;
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
		siacTMovgestT.setSiacDMovgestTsTipo(this);

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
		siacTMovgestT.setSiacDMovgestTsTipo(null);

		return siacTMovgestT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestTsTipoId = uid;
	}

}