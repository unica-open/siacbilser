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
 * The persistent class for the siac_d_ordinativo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_ordinativo_tipo")
@NamedQuery(name="SiacDOrdinativoTipo.findAll", query="SELECT s FROM SiacDOrdinativoTipo s")
public class SiacDOrdinativoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ORDINATIVO_TIPO_ORDTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ORDINATIVO_TIPO_ORD_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ORDINATIVO_TIPO_ORDTIPOID_GENERATOR")
	@Column(name="ord_tipo_id")
	private Integer ordTipoId;

	

	/** The ord tipo code. */
	@Column(name="ord_tipo_code")
	private String ordTipoCode;

	/** The ord tipo desc. */
	@Column(name="ord_tipo_desc")
	private String ordTipoDesc;

	

	//bi-directional many-to-one association to SiacROrdinativoTipoClassTip
	@OneToMany(mappedBy="siacDOrdinativoTipo")
	private List<SiacROrdinativoTipoClassTip> siacROrdinativoTipoClassTips;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativos. */
	@OneToMany(mappedBy="siacDOrdinativoTipo")
	private List<SiacTOrdinativo> siacTOrdinativos;

	/**
	 * Instantiates a new siac d ordinativo tipo.
	 */
	public SiacDOrdinativoTipo() {
	}

	/**
	 * Gets the ord tipo id.
	 *
	 * @return the ord tipo id
	 */
	public Integer getOrdTipoId() {
		return this.ordTipoId;
	}

	/**
	 * Sets the ord tipo id.
	 *
	 * @param ordTipoId the new ord tipo id
	 */
	public void setOrdTipoId(Integer ordTipoId) {
		this.ordTipoId = ordTipoId;
	}

	

	/**
	 * Gets the ord tipo code.
	 *
	 * @return the ord tipo code
	 */
	public String getOrdTipoCode() {
		return this.ordTipoCode;
	}

	/**
	 * Sets the ord tipo code.
	 *
	 * @param ordTipoCode the new ord tipo code
	 */
	public void setOrdTipoCode(String ordTipoCode) {
		this.ordTipoCode = ordTipoCode;
	}

	/**
	 * Gets the ord tipo desc.
	 *
	 * @return the ord tipo desc
	 */
	public String getOrdTipoDesc() {
		return this.ordTipoDesc;
	}

	/**
	 * Sets the ord tipo desc.
	 *
	 * @param ordTipoDesc the new ord tipo desc
	 */
	public void setOrdTipoDesc(String ordTipoDesc) {
		this.ordTipoDesc = ordTipoDesc;
	}

	public List<SiacROrdinativoTipoClassTip> getSiacROrdinativoTipoClassTips() {
		return this.siacROrdinativoTipoClassTips;
	}

	public void setSiacROrdinativoTipoClassTips(List<SiacROrdinativoTipoClassTip> siacROrdinativoTipoClassTips) {
		this.siacROrdinativoTipoClassTips = siacROrdinativoTipoClassTips;
	}

	public SiacROrdinativoTipoClassTip addSiacROrdinativoTipoClassTip(SiacROrdinativoTipoClassTip siacROrdinativoTipoClassTip) {
		getSiacROrdinativoTipoClassTips().add(siacROrdinativoTipoClassTip);
		siacROrdinativoTipoClassTip.setSiacDOrdinativoTipo(this);

		return siacROrdinativoTipoClassTip;
	}

	public SiacROrdinativoTipoClassTip removeSiacROrdinativoTipoClassTip(SiacROrdinativoTipoClassTip siacROrdinativoTipoClassTip) {
		getSiacROrdinativoTipoClassTips().remove(siacROrdinativoTipoClassTip);
		siacROrdinativoTipoClassTip.setSiacDOrdinativoTipo(null);

		return siacROrdinativoTipoClassTip;
	}

	/**
	 * Gets the siac t ordinativos.
	 *
	 * @return the siac t ordinativos
	 */
	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	/**
	 * Sets the siac t ordinativos.
	 *
	 * @param siacTOrdinativos the new siac t ordinativos
	 */
	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	/**
	 * Adds the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the siac t ordinativo
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDOrdinativoTipo(this);

		return siacTOrdinativo;
	}

	/**
	 * Removes the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the siac t ordinativo
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDOrdinativoTipo(null);

		return siacTOrdinativo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordTipoId = uid;
	}

}