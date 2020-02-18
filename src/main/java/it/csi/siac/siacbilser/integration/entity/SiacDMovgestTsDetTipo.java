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
 * The persistent class for the siac_d_movgest_ts_det_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_ts_det_tipo")
@NamedQuery(name="SiacDMovgestTsDetTipo.findAll", query="SELECT s FROM SiacDMovgestTsDetTipo s")
public class SiacDMovgestTsDetTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts det tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MOVGEST_TS_DET_TIPO_MOVGESTTSDETTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MOVGEST_TS_DET_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MOVGEST_TS_DET_TIPO_MOVGESTTSDETTIPOID_GENERATOR")
	@Column(name="movgest_ts_det_tipo_id")
	private Integer movgestTsDetTipoId;

	/** The movgest ts det tipo code. */
	@Column(name="movgest_ts_det_tipo_code")
	private String movgestTsDetTipoCode;

	/** The movgest ts det tipo desc. */
	@Column(name="movgest_ts_det_tipo_desc")
	private String movgestTsDetTipoDesc;

	//bi-directional many-to-one association to SiacTMovgestTsDet
	/** The siac t movgest ts dets. */
	@OneToMany(mappedBy="siacDMovgestTsDetTipo")
	private List<SiacTMovgestTsDet> siacTMovgestTsDets;

	//bi-directional many-to-one association to SiacTMovgestTsDetMod
	@OneToMany(mappedBy="siacDMovgestTsDetTipo")
	private List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods;

	/**
	 * Instantiates a new siac d movgest ts det tipo.
	 */
	public SiacDMovgestTsDetTipo() {
	}

	/**
	 * Gets the movgest ts det tipo id.
	 *
	 * @return the movgest ts det tipo id
	 */
	public Integer getMovgestTsDetTipoId() {
		return this.movgestTsDetTipoId;
	}

	/**
	 * Sets the movgest ts det tipo id.
	 *
	 * @param movgestTsDetTipoId the new movgest ts det tipo id
	 */
	public void setMovgestTsDetTipoId(Integer movgestTsDetTipoId) {
		this.movgestTsDetTipoId = movgestTsDetTipoId;
	}

	/**
	 * Gets the movgest ts det tipo code.
	 *
	 * @return the movgest ts det tipo code
	 */
	public String getMovgestTsDetTipoCode() {
		return this.movgestTsDetTipoCode;
	}

	/**
	 * Sets the movgest ts det tipo code.
	 *
	 * @param movgestTsDetTipoCode the new movgest ts det tipo code
	 */
	public void setMovgestTsDetTipoCode(String movgestTsDetTipoCode) {
		this.movgestTsDetTipoCode = movgestTsDetTipoCode;
	}

	/**
	 * Gets the movgest ts det tipo desc.
	 *
	 * @return the movgest ts det tipo desc
	 */
	public String getMovgestTsDetTipoDesc() {
		return this.movgestTsDetTipoDesc;
	}

	/**
	 * Sets the movgest ts det tipo desc.
	 *
	 * @param movgestTsDetTipoDesc the new movgest ts det tipo desc
	 */
	public void setMovgestTsDetTipoDesc(String movgestTsDetTipoDesc) {
		this.movgestTsDetTipoDesc = movgestTsDetTipoDesc;
	}

	/**
	 * Gets the siac t movgest ts dets.
	 *
	 * @return the siac t movgest ts dets
	 */
	public List<SiacTMovgestTsDet> getSiacTMovgestTsDets() {
		return this.siacTMovgestTsDets;
	}

	/**
	 * Sets the siac t movgest ts dets.
	 *
	 * @param siacTMovgestTsDets the new siac t movgest ts dets
	 */
	public void setSiacTMovgestTsDets(List<SiacTMovgestTsDet> siacTMovgestTsDets) {
		this.siacTMovgestTsDets = siacTMovgestTsDets;
	}

	/**
	 * Adds the siac t movgest ts det.
	 *
	 * @param siacTMovgestTsDet the siac t movgest ts det
	 * @return the siac t movgest ts det
	 */
	public SiacTMovgestTsDet addSiacTMovgestTsDet(SiacTMovgestTsDet siacTMovgestTsDet) {
		getSiacTMovgestTsDets().add(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacDMovgestTsDetTipo(this);

		return siacTMovgestTsDet;
	}

	/**
	 * Removes the siac t movgest ts det.
	 *
	 * @param siacTMovgestTsDet the siac t movgest ts det
	 * @return the siac t movgest ts det
	 */
	public SiacTMovgestTsDet removeSiacTMovgestTsDet(SiacTMovgestTsDet siacTMovgestTsDet) {
		getSiacTMovgestTsDets().remove(siacTMovgestTsDet);
		siacTMovgestTsDet.setSiacDMovgestTsDetTipo(null);

		return siacTMovgestTsDet;
	}

	public List<SiacTMovgestTsDetMod> getSiacTMovgestTsDetMods() {
		return this.siacTMovgestTsDetMods;
	}

	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	public SiacTMovgestTsDetMod addSiacTMovgestTsDetMod(SiacTMovgestTsDetMod siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().add(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacDMovgestTsDetTipo(this);

		return siacTMovgestTsDetMod;
	}

	public SiacTMovgestTsDetMod removeSiacTMovgestTsDetMod(SiacTMovgestTsDetMod siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().remove(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacDMovgestTsDetTipo(null);

		return siacTMovgestTsDetMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsDetTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestTsDetTipoId = uid;
	}

}