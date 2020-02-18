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
 * The persistent class for the siac_d_commissione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_commissione_tipo")
@NamedQuery(name="SiacDCommissioneTipo.findAll", query="SELECT s FROM SiacDCommissioneTipo s")
public class SiacDCommissioneTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The comm tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_COMMISSIONE_TIPO_COMMTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_COMMISSIONE_TIPO_COMM_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_COMMISSIONE_TIPO_COMMTIPOID_GENERATOR")
	@Column(name="comm_tipo_id")
	private Integer commTipoId;

	/** The comm tipo code. */
	@Column(name="comm_tipo_code")
	private String commTipoCode;

	/** The comm tipo desc. */
	@Column(name="comm_tipo_desc")
	private String commTipoDesc;

	//bi-directional many-to-one association to SiacTOrdinativo
	@OneToMany(mappedBy="siacDCommissioneTipo")
	private List<SiacTOrdinativo> siacTOrdinativos;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdocs. */
	@OneToMany(mappedBy="siacDCommissioneTipo")
	private List<SiacTSubdoc> siacTSubdocs;

	/**
	 * Instantiates a new siac d commissione tipo.
	 */
	public SiacDCommissioneTipo() {
	}

	/**
	 * Gets the comm tipo id.
	 *
	 * @return the comm tipo id
	 */
	public Integer getCommTipoId() {
		return this.commTipoId;
	}

	/**
	 * Sets the comm tipo id.
	 *
	 * @param commTipoId the new comm tipo id
	 */
	public void setCommTipoId(Integer commTipoId) {
		this.commTipoId = commTipoId;
	}

	/**
	 * Gets the comm tipo code.
	 *
	 * @return the comm tipo code
	 */
	public String getCommTipoCode() {
		return this.commTipoCode;
	}

	/**
	 * Sets the comm tipo code.
	 *
	 * @param commTipoCode the new comm tipo code
	 */
	public void setCommTipoCode(String commTipoCode) {
		this.commTipoCode = commTipoCode;
	}

	/**
	 * Gets the comm tipo desc.
	 *
	 * @return the comm tipo desc
	 */
	public String getCommTipoDesc() {
		return this.commTipoDesc;
	}

	/**
	 * Sets the comm tipo desc.
	 *
	 * @param commTipoDesc the new comm tipo desc
	 */
	public void setCommTipoDesc(String commTipoDesc) {
		this.commTipoDesc = commTipoDesc;
	}	
	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDCommissioneTipo(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDCommissioneTipo(null);

		return siacTOrdinativo;
	}

	/**
	 * Gets the siac t subdocs.
	 *
	 * @return the siac t subdocs
	 */
	public List<SiacTSubdoc> getSiacTSubdocs() {
		return this.siacTSubdocs;
	}

	/**
	 * Sets the siac t subdocs.
	 *
	 * @param siacTSubdocs the new siac t subdocs
	 */
	public void setSiacTSubdocs(List<SiacTSubdoc> siacTSubdocs) {
		this.siacTSubdocs = siacTSubdocs;
	}

	/**
	 * Adds the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc addSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().add(siacTSubdoc);
		siacTSubdoc.setSiacDCommissioneTipo(this);
		return siacTSubdoc;
	}

	/**
	 * Removes the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc removeSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacDCommissioneTipo(null);
		return siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return commTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.commTipoId = uid;
		
	}

}