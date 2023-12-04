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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


/**
 * The persistent class for the siac_r_doc_sirfel database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_sirfel")
@NamedQuery(name="SiacRDocSirfel.findAll", query="SELECT s FROM SiacRDocSirfel s")
public class SiacRDocSirfel extends SiacTBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_DOC_SIRFEL_DOCSIRFELID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_DOC_SIRFEL_DOCSIRFEL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_SIRFEL_DOCSIRFELID_GENERATOR")
	@Column(name="docsirfel_id")
	private Integer docsirfelId;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDoc siacTDoc;


	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id"),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura")
		})
//	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SiacRDocSirfel() {
	}

	public Integer getDocsirfelId() {
		return this.docsirfelId;
	}

	public void setDocsirfelId(Integer docsirfelId) {
		this.docsirfelId = docsirfelId;
	}

	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}

	/**
	 * Gets the siac t doc.
	 *
	 * @return the siac t doc
	 */
	public SiacTDoc getSiacTDoc() {
		return this.siacTDoc;
	}

	/**
	 * Sets the siac t doc.
	 *
	 * @param siacTDoc the new siac t doc
	 */
	public void setSiacTDoc(SiacTDoc siacTDoc) {
		this.siacTDoc = siacTDoc;
	}
	

	@Override
	public Integer getUid() {
		return docsirfelId;
	}

	@Override
	public void setUid(Integer uid) {
		this.docsirfelId = uid;
	}
}