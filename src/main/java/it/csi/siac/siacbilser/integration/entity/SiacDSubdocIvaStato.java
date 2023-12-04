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
 * The persistent class for the siac_d_subdoc_iva_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_subdoc_iva_stato")
@NamedQuery(name="SiacDSubdocIvaStato.findAll", query="SELECT s FROM SiacDSubdocIvaStato s")
public class SiacDSubdocIvaStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdociva stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SUBDOC_IVA_STATO_SUBDOCIVASTATOID_GENERATOR", sequenceName="SIAC_D_SUBDOC_IVA_STATO_SUBDOCIVA_STATO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SUBDOC_IVA_STATO_SUBDOCIVASTATOID_GENERATOR")
	@Column(name="subdociva_stato_id")
	private Integer subdocivaStatoId;

	/** The subdociva stato code. */
	@Column(name="subdociva_stato_code")
	private String subdocivaStatoCode;

	/** The subdociva stato desc. */
	@Column(name="subdociva_stato_desc")
	private String subdocivaStatoDesc;

	//bi-directional many-to-one association to SiacRSubdocIvaStato
	/** The siac r subdoc iva statos. */
	@OneToMany(mappedBy="siacDSubdocIvaStato")
	private List<SiacRSubdocIvaStato> siacRSubdocIvaStatos;

	/**
	 * Instantiates a new siac d subdoc iva stato.
	 */
	public SiacDSubdocIvaStato() {
	}

	/**
	 * Gets the subdociva stato id.
	 *
	 * @return the subdociva stato id
	 */
	public Integer getSubdocivaStatoId() {
		return this.subdocivaStatoId;
	}

	/**
	 * Sets the subdociva stato id.
	 *
	 * @param subdocivaStatoId the new subdociva stato id
	 */
	public void setSubdocivaStatoId(Integer subdocivaStatoId) {
		this.subdocivaStatoId = subdocivaStatoId;
	}

	/**
	 * Gets the subdociva stato code.
	 *
	 * @return the subdociva stato code
	 */
	public String getSubdocivaStatoCode() {
		return this.subdocivaStatoCode;
	}

	/**
	 * Sets the subdociva stato code.
	 *
	 * @param subdocivaStatoCode the new subdociva stato code
	 */
	public void setSubdocivaStatoCode(String subdocivaStatoCode) {
		this.subdocivaStatoCode = subdocivaStatoCode;
	}

	/**
	 * Gets the subdociva stato desc.
	 *
	 * @return the subdociva stato desc
	 */
	public String getSubdocivaStatoDesc() {
		return this.subdocivaStatoDesc;
	}

	/**
	 * Sets the subdociva stato desc.
	 *
	 * @param subdocivaStatoDesc the new subdociva stato desc
	 */
	public void setSubdocivaStatoDesc(String subdocivaStatoDesc) {
		this.subdocivaStatoDesc = subdocivaStatoDesc;
	}

	/**
	 * Gets the siac r subdoc iva statos.
	 *
	 * @return the siac r subdoc iva statos
	 */
	public List<SiacRSubdocIvaStato> getSiacRSubdocIvaStatos() {
		return this.siacRSubdocIvaStatos;
	}

	/**
	 * Sets the siac r subdoc iva statos.
	 *
	 * @param siacRSubdocIvaStatos the new siac r subdoc iva statos
	 */
	public void setSiacRSubdocIvaStatos(List<SiacRSubdocIvaStato> siacRSubdocIvaStatos) {
		this.siacRSubdocIvaStatos = siacRSubdocIvaStatos;
	}

	/**
	 * Adds the siac r subdoc iva stato.
	 *
	 * @param siacRSubdocIvaStato the siac r subdoc iva stato
	 * @return the siac r subdoc iva stato
	 */
	public SiacRSubdocIvaStato addSiacRSubdocIvaStato(SiacRSubdocIvaStato siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().add(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacDSubdocIvaStato(this);

		return siacRSubdocIvaStato;
	}

	/**
	 * Removes the siac r subdoc iva stato.
	 *
	 * @param siacRSubdocIvaStato the siac r subdoc iva stato
	 * @return the siac r subdoc iva stato
	 */
	public SiacRSubdocIvaStato removeSiacRSubdocIvaStato(SiacRSubdocIvaStato siacRSubdocIvaStato) {
		getSiacRSubdocIvaStatos().remove(siacRSubdocIvaStato);
		siacRSubdocIvaStato.setSiacDSubdocIvaStato(null);

		return siacRSubdocIvaStato;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocivaStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocivaStatoId = uid;
	}

}