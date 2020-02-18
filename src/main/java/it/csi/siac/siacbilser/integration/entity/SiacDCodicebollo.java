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
 * The persistent class for the siac_d_codicebollo database table.
 * 
 */
@Entity
@Table(name="siac_d_codicebollo")
@NamedQuery(name="SiacDCodicebollo.findAll", query="SELECT s FROM SiacDCodicebollo s")
public class SiacDCodicebollo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The codbollo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CODICEBOLLO_CODBOLLOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CODICEBOLLO_CODBOLLO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CODICEBOLLO_CODBOLLOID_GENERATOR")
	@Column(name="codbollo_id")
	private Integer codbolloId;

	/** The codbollo code. */
	@Column(name="codbollo_code")
	private String codbolloCode;

	/** The codbollo desc. */
	@Column(name="codbollo_desc")
	private String codbolloDesc;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t docs. */
	@OneToMany(mappedBy="siacDCodicebollo")
	private List<SiacTDoc> siacTDocs;

	//bi-directional many-to-one association to SiacTOrdinativo
	@OneToMany(mappedBy="siacDCodicebollo")
	private List<SiacTOrdinativo> siacTOrdinativos;

	/**
	 * Instantiates a new siac d codicebollo.
	 */
	public SiacDCodicebollo() {
	}

	/**
	 * Gets the codbollo id.
	 *
	 * @return the codbollo id
	 */
	public Integer getCodbolloId() {
		return this.codbolloId;
	}

	/**
	 * Sets the codbollo id.
	 *
	 * @param codbolloId the new codbollo id
	 */
	public void setCodbolloId(Integer codbolloId) {
		this.codbolloId = codbolloId;
	}

	/**
	 * Gets the codbollo code.
	 *
	 * @return the codbollo code
	 */
	public String getCodbolloCode() {
		return this.codbolloCode;
	}

	/**
	 * Sets the codbollo code.
	 *
	 * @param codbolloCode the new codbollo code
	 */
	public void setCodbolloCode(String codbolloCode) {
		this.codbolloCode = codbolloCode;
	}

	/**
	 * Gets the codbollo desc.
	 *
	 * @return the codbollo desc
	 */
	public String getCodbolloDesc() {
		return this.codbolloDesc;
	}

	/**
	 * Sets the codbollo desc.
	 *
	 * @param codbolloDesc the new codbollo desc
	 */
	public void setCodbolloDesc(String codbolloDesc) {
		this.codbolloDesc = codbolloDesc;
	}

	/**
	 * Gets the siac t docs.
	 *
	 * @return the siac t docs
	 */
	public List<SiacTDoc> getSiacTDocs() {
		return this.siacTDocs;
	}

	/**
	 * Sets the siac t docs.
	 *
	 * @param siacTDocs the new siac t docs
	 */
	public void setSiacTDocs(List<SiacTDoc> siacTDocs) {
		this.siacTDocs = siacTDocs;
	}

	/**
	 * Adds the siac t doc.
	 *
	 * @param siacTDoc the siac t doc
	 * @return the siac t doc
	 */
	public SiacTDoc addSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().add(siacTDoc);
		siacTDoc.setSiacDCodicebollo(this);

		return siacTDoc;
	}

	/**
	 * Removes the siac t doc.
	 *
	 * @param siacTDoc the siac t doc
	 * @return the siac t doc
	 */
	public SiacTDoc removeSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().remove(siacTDoc);
		siacTDoc.setSiacDCodicebollo(null);

		return siacTDoc;
	}

	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDCodicebollo(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDCodicebollo(null);

		return siacTOrdinativo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return codbolloId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.codbolloId = uid;
		
	}

}