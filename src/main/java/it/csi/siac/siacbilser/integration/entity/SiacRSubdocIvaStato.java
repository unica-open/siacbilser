/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_subdoc_iva_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_iva_stato")
@NamedQuery(name="SiacRSubdocIvaStato.findAll", query="SELECT s FROM SiacRSubdocIvaStato s")
public class SiacRSubdocIvaStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdociva stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_IVA_STATO_SUBDOCIVASTATORID_GENERATOR", sequenceName="SIAC_R_SUBDOC_IVA_STATO_SUBDOCIVA_STATO_R_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_IVA_STATO_SUBDOCIVASTATORID_GENERATOR")
	@Column(name="subdociva_stato_r_id")
	private Integer subdocivaStatoRId;

	//bi-directional many-to-one association to SiacDSubdocIvaStato
	/** The siac d subdoc iva stato. */
	@ManyToOne
	@JoinColumn(name="subdociva_stato_id")
	private SiacDSubdocIvaStato siacDSubdocIvaStato;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc iva. */
	@ManyToOne
	@JoinColumn(name="subdociva_id")
	private SiacTSubdocIva siacTSubdocIva;

	/**
	 * Instantiates a new siac r subdoc iva stato.
	 */
	public SiacRSubdocIvaStato() {
	}

	/**
	 * Gets the subdociva stato r id.
	 *
	 * @return the subdociva stato r id
	 */
	public Integer getSubdocivaStatoRId() {
		return this.subdocivaStatoRId;
	}

	/**
	 * Sets the subdociva stato r id.
	 *
	 * @param subdocivaStatoRId the new subdociva stato r id
	 */
	public void setSubdocivaStatoRId(Integer subdocivaStatoRId) {
		this.subdocivaStatoRId = subdocivaStatoRId;
	}

	/**
	 * Gets the siac d subdoc iva stato.
	 *
	 * @return the siac d subdoc iva stato
	 */
	public SiacDSubdocIvaStato getSiacDSubdocIvaStato() {
		return this.siacDSubdocIvaStato;
	}

	/**
	 * Sets the siac d subdoc iva stato.
	 *
	 * @param siacDSubdocIvaStato the new siac d subdoc iva stato
	 */
	public void setSiacDSubdocIvaStato(SiacDSubdocIvaStato siacDSubdocIvaStato) {
		this.siacDSubdocIvaStato = siacDSubdocIvaStato;
	}
	
	/**
	 * Gets the siac t subdoc iva.
	 *
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva getSiacTSubdocIva() {
		return this.siacTSubdocIva;
	}

	/**
	 * Sets the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the new siac t subdoc iva
	 */
	public void setSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		this.siacTSubdocIva = siacTSubdocIva;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocivaStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocivaStatoRId = uid;
	}


}