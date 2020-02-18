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
 * The persistent class for the siac_d_iva_chiusura_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_chiusura_tipo")
@NamedQuery(name="SiacDIvaChiusuraTipo.findAll", query="SELECT s FROM SiacDIvaChiusuraTipo s")
public class SiacDIvaChiusuraTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivachi tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_IVA_CHIUSURA_TIPO_IVACHITIPOID_GENERATOR", sequenceName="SIAC_D_IVA_CHIUSURA_TIPO_IVACHI_TIPO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_CHIUSURA_TIPO_IVACHITIPOID_GENERATOR")
	@Column(name="ivachi_tipo_id")
	private Integer ivachiTipoId;

	/** The ivachi tipo code. */
	@Column(name="ivachi_tipo_code")
	private String ivachiTipoCode;

	/** The ivachi tipo desc. */
	@Column(name="ivachi_tipo_desc")
	private String ivachiTipoDesc;

	//bi-directional many-to-one association to SiacRIvaGruppoChiusura
	/** The siac r iva gruppo chiusuras. */
	@OneToMany(mappedBy="siacDIvaChiusuraTipo")
	private List<SiacRIvaGruppoChiusura> siacRIvaGruppoChiusuras;

	/**
	 * Instantiates a new siac d iva chiusura tipo.
	 */
	public SiacDIvaChiusuraTipo() {
	}

	/**
	 * Gets the ivachi tipo id.
	 *
	 * @return the ivachi tipo id
	 */
	public Integer getIvachiTipoId() {
		return this.ivachiTipoId;
	}

	/**
	 * Sets the ivachi tipo id.
	 *
	 * @param ivachiTipoId the new ivachi tipo id
	 */
	public void setIvachiTipoId(Integer ivachiTipoId) {
		this.ivachiTipoId = ivachiTipoId;
	}

	/**
	 * Gets the ivachi tipo code.
	 *
	 * @return the ivachi tipo code
	 */
	public String getIvachiTipoCode() {
		return this.ivachiTipoCode;
	}

	/**
	 * Sets the ivachi tipo code.
	 *
	 * @param ivachiTipoCode the new ivachi tipo code
	 */
	public void setIvachiTipoCode(String ivachiTipoCode) {
		this.ivachiTipoCode = ivachiTipoCode;
	}

	/**
	 * Gets the ivachi tipo desc.
	 *
	 * @return the ivachi tipo desc
	 */
	public String getIvachiTipoDesc() {
		return this.ivachiTipoDesc;
	}

	/**
	 * Sets the ivachi tipo desc.
	 *
	 * @param ivachiTipoDesc the new ivachi tipo desc
	 */
	public void setIvachiTipoDesc(String ivachiTipoDesc) {
		this.ivachiTipoDesc = ivachiTipoDesc;
	}

	/**
	 * Gets the siac r iva gruppo chiusuras.
	 *
	 * @return the siac r iva gruppo chiusuras
	 */
	public List<SiacRIvaGruppoChiusura> getSiacRIvaGruppoChiusuras() {
		return this.siacRIvaGruppoChiusuras;
	}

	/**
	 * Sets the siac r iva gruppo chiusuras.
	 *
	 * @param siacRIvaGruppoChiusuras the new siac r iva gruppo chiusuras
	 */
	public void setSiacRIvaGruppoChiusuras(List<SiacRIvaGruppoChiusura> siacRIvaGruppoChiusuras) {
		this.siacRIvaGruppoChiusuras = siacRIvaGruppoChiusuras;
	}

	/**
	 * Adds the siac r iva gruppo chiusura.
	 *
	 * @param siacRIvaGruppoChiusura the siac r iva gruppo chiusura
	 * @return the siac r iva gruppo chiusura
	 */
	public SiacRIvaGruppoChiusura addSiacRIvaGruppoChiusura(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura) {
		getSiacRIvaGruppoChiusuras().add(siacRIvaGruppoChiusura);
		siacRIvaGruppoChiusura.setSiacDIvaChiusuraTipo(this);

		return siacRIvaGruppoChiusura;
	}

	/**
	 * Removes the siac r iva gruppo chiusura.
	 *
	 * @param siacRIvaGruppoChiusura the siac r iva gruppo chiusura
	 * @return the siac r iva gruppo chiusura
	 */
	public SiacRIvaGruppoChiusura removeSiacRIvaGruppoChiusura(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura) {
		getSiacRIvaGruppoChiusuras().remove(siacRIvaGruppoChiusura);
		siacRIvaGruppoChiusura.setSiacDIvaChiusuraTipo(null);

		return siacRIvaGruppoChiusura;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivachiTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivachiTipoId = uid;
	}
}