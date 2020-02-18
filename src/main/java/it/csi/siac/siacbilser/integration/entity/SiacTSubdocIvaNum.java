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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_subdoc_iva_num database table.
 * 
 */
@Entity
@Table(name="siac_t_subdoc_iva_num")
@NamedQuery(name="SiacTSubdocIvaNum.findAll", query="SELECT s FROM SiacTSubdocIvaNum s")
public class SiacTSubdocIvaNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_SUBDOC_SUBDOCIVANUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_SUBDOC_IVA_NUM_SUBDOCIVA_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SUBDOC_SUBDOCIVANUMID_GENERATOR")
	@Column(name="subdociva_num_id")
	private Integer subdocNumId;

	/** The subdociva anno. */
	@Column(name="subdociva_anno")
	private Integer subdocivaAnno;

	/** The subdociva numero. */
	@Version
	@Column(name="subdociva_numero")
	private Integer subdocivaNumero;

	
	/**
	 * Instantiates a new siac t subdoc iva num.
	 */
	public SiacTSubdocIvaNum() {
	}

	/**
	 * Gets the subdoc num id.
	 *
	 * @return the subdoc num id
	 */
	public Integer getSubdocNumId() {
		return this.subdocNumId;
	}

	/**
	 * Sets the subdoc num id.
	 *
	 * @param subdocId the new subdoc num id
	 */
	public void setSubdocNumId(Integer subdocId) {
		this.subdocNumId = subdocId;
	}

	/**
	 * Gets the subdociva anno.
	 *
	 * @return the subdocivaAnno
	 */
	public Integer getSubdocivaAnno() {
		return subdocivaAnno;
	}

	/**
	 * Sets the subdociva anno.
	 *
	 * @param subdocivaAnno the subdocivaAnno to set
	 */
	public void setSubdocivaAnno(Integer subdocivaAnno) {
		this.subdocivaAnno = subdocivaAnno;
	}

	/**
	 * Gets the subdociva numero.
	 *
	 * @return the subdocivaNumero
	 */
	public Integer getSubdocivaNumero() {
		return subdocivaNumero;
	}

	/**
	 * Sets the subdociva numero.
	 *
	 * @param subdocivaNumero the subdocivaNumero to set
	 */
	public void setSubdocivaNumero(Integer subdocivaNumero) {
		this.subdocivaNumero = subdocivaNumero;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocNumId = uid;		
	}


}