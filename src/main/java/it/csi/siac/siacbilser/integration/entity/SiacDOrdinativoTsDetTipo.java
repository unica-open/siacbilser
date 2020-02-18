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
 * The persistent class for the siac_d_ordinativo_ts_det_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_ordinativo_ts_det_tipo")
@NamedQuery(name="SiacDOrdinativoTsDetTipo.findAll", query="SELECT s FROM SiacDOrdinativoTsDetTipo s")
public class SiacDOrdinativoTsDetTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord ts det tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ORDINATIVO_TS_DET_TIPO_ORDTSDETTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ORDINATIVO_TS_DET_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ORDINATIVO_TS_DET_TIPO_ORDTSDETTIPOID_GENERATOR")
	@Column(name="ord_ts_det_tipo_id")
	private Integer ordTsDetTipoId;

	/** The ord ts det tipo code. */
	@Column(name="ord_ts_det_tipo_code")
	private String ordTsDetTipoCode;

	/** The ord ts det tipo desc. */
	@Column(name="ord_ts_det_tipo_desc")
	private String ordTsDetTipoDesc;

	//bi-directional many-to-one association to SiacTOrdinativoTsDet
	/** The siac t ordinativo ts dets. */
	@OneToMany(mappedBy="siacDOrdinativoTsDetTipo")
	private List<SiacTOrdinativoTsDet> siacTOrdinativoTsDets;

	/**
	 * Instantiates a new siac d ordinativo ts det tipo.
	 */
	public SiacDOrdinativoTsDetTipo() {
	}

	/**
	 * Gets the ord ts det tipo id.
	 *
	 * @return the ord ts det tipo id
	 */
	public Integer getOrdTsDetTipoId() {
		return this.ordTsDetTipoId;
	}

	/**
	 * Sets the ord ts det tipo id.
	 *
	 * @param ordTsDetTipoId the new ord ts det tipo id
	 */
	public void setOrdTsDetTipoId(Integer ordTsDetTipoId) {
		this.ordTsDetTipoId = ordTsDetTipoId;
	}

	/**
	 * Gets the ord ts det tipo code.
	 *
	 * @return the ord ts det tipo code
	 */
	public String getOrdTsDetTipoCode() {
		return this.ordTsDetTipoCode;
	}

	/**
	 * Sets the ord ts det tipo code.
	 *
	 * @param ordTsDetTipoCode the new ord ts det tipo code
	 */
	public void setOrdTsDetTipoCode(String ordTsDetTipoCode) {
		this.ordTsDetTipoCode = ordTsDetTipoCode;
	}

	/**
	 * Gets the ord ts det tipo desc.
	 *
	 * @return the ord ts det tipo desc
	 */
	public String getOrdTsDetTipoDesc() {
		return this.ordTsDetTipoDesc;
	}

	/**
	 * Sets the ord ts det tipo desc.
	 *
	 * @param ordTsDetTipoDesc the new ord ts det tipo desc
	 */
	public void setOrdTsDetTipoDesc(String ordTsDetTipoDesc) {
		this.ordTsDetTipoDesc = ordTsDetTipoDesc;
	}

	/**
	 * Gets the siac t ordinativo ts dets.
	 *
	 * @return the siac t ordinativo ts dets
	 */
	public List<SiacTOrdinativoTsDet> getSiacTOrdinativoTsDets() {
		return this.siacTOrdinativoTsDets;
	}

	/**
	 * Sets the siac t ordinativo ts dets.
	 *
	 * @param siacTOrdinativoTsDets the new siac t ordinativo ts dets
	 */
	public void setSiacTOrdinativoTsDets(List<SiacTOrdinativoTsDet> siacTOrdinativoTsDets) {
		this.siacTOrdinativoTsDets = siacTOrdinativoTsDets;
	}

	/**
	 * Adds the siac t ordinativo ts det.
	 *
	 * @param siacTOrdinativoTsDet the siac t ordinativo ts det
	 * @return the siac t ordinativo ts det
	 */
	public SiacTOrdinativoTsDet addSiacTOrdinativoTsDet(SiacTOrdinativoTsDet siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().add(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacDOrdinativoTsDetTipo(this);

		return siacTOrdinativoTsDet;
	}

	/**
	 * Removes the siac t ordinativo ts det.
	 *
	 * @param siacTOrdinativoTsDet the siac t ordinativo ts det
	 * @return the siac t ordinativo ts det
	 */
	public SiacTOrdinativoTsDet removeSiacTOrdinativoTsDet(SiacTOrdinativoTsDet siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().remove(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacDOrdinativoTsDetTipo(null);

		return siacTOrdinativoTsDet;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordTsDetTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		ordTsDetTipoId = uid;
	}

}