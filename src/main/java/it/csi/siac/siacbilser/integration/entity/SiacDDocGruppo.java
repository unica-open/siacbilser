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
 * The persistent class for the siac_d_doc_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_gruppo")
@NamedQuery(name="SiacDDocGruppo.findAll", query="SELECT s FROM SiacDDocGruppo s")
public class SiacDDocGruppo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc gruppo tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_DOC_GRUPPO_DOCGRUPPOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_DOC_GRUPPO_DOC_GRUPPO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DOC_GRUPPO_DOCGRUPPOTIPOID_GENERATOR")
	@Column(name="doc_gruppo_tipo_id")
	private Integer docGruppoTipoId;

	/** The doc gruppo tipo code. */
	@Column(name="doc_gruppo_tipo_code")
	private String docGruppoTipoCode;

	/** The doc gruppo tipo desc. */
	@Column(name="doc_gruppo_tipo_desc")
	private String docGruppoTipoDesc;

	//bi-directional many-to-one association to SiacDDocTipo
	/** The siac d doc tipos. */
	@OneToMany(mappedBy="siacDDocGruppo")
	private List<SiacDDocTipo> siacDDocTipos;

	/**
	 * Instantiates a new siac d doc gruppo.
	 */
	public SiacDDocGruppo() {
	}

	/**
	 * Gets the doc gruppo tipo id.
	 *
	 * @return the doc gruppo tipo id
	 */
	public Integer getDocGruppoTipoId() {
		return this.docGruppoTipoId;
	}

	/**
	 * Sets the doc gruppo tipo id.
	 *
	 * @param docGruppoTipoId the new doc gruppo tipo id
	 */
	public void setDocGruppoTipoId(Integer docGruppoTipoId) {
		this.docGruppoTipoId = docGruppoTipoId;
	}



	/**
	 * Gets the doc gruppo tipo code.
	 *
	 * @return the doc gruppo tipo code
	 */
	public String getDocGruppoTipoCode() {
		return this.docGruppoTipoCode;
	}

	/**
	 * Sets the doc gruppo tipo code.
	 *
	 * @param docGruppoTipoCode the new doc gruppo tipo code
	 */
	public void setDocGruppoTipoCode(String docGruppoTipoCode) {
		this.docGruppoTipoCode = docGruppoTipoCode;
	}

	/**
	 * Gets the doc gruppo tipo desc.
	 *
	 * @return the doc gruppo tipo desc
	 */
	public String getDocGruppoTipoDesc() {
		return this.docGruppoTipoDesc;
	}

	/**
	 * Sets the doc gruppo tipo desc.
	 *
	 * @param docGruppoTipoDesc the new doc gruppo tipo desc
	 */
	public void setDocGruppoTipoDesc(String docGruppoTipoDesc) {
		this.docGruppoTipoDesc = docGruppoTipoDesc;
	}

	

	/**
	 * Gets the siac d doc tipos.
	 *
	 * @return the siac d doc tipos
	 */
	public List<SiacDDocTipo> getSiacDDocTipos() {
		return this.siacDDocTipos;
	}

	/**
	 * Sets the siac d doc tipos.
	 *
	 * @param siacDDocTipos the new siac d doc tipos
	 */
	public void setSiacDDocTipos(List<SiacDDocTipo> siacDDocTipos) {
		this.siacDDocTipos = siacDDocTipos;
	}

	/**
	 * Adds the siac d doc tipo.
	 *
	 * @param siacDDocTipo the siac d doc tipo
	 * @return the siac d doc tipo
	 */
	public SiacDDocTipo addSiacDDocTipo(SiacDDocTipo siacDDocTipo) {
		getSiacDDocTipos().add(siacDDocTipo);
		siacDDocTipo.setSiacDDocGruppo(this);

		return siacDDocTipo;
	}

	/**
	 * Removes the siac d doc tipo.
	 *
	 * @param siacDDocTipo the siac d doc tipo
	 * @return the siac d doc tipo
	 */
	public SiacDDocTipo removeSiacDDocTipo(SiacDDocTipo siacDDocTipo) {
		getSiacDDocTipos().remove(siacDDocTipo);
		siacDDocTipo.setSiacDDocGruppo(null);

		return siacDDocTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docGruppoTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docGruppoTipoId = uid;
		
	}

}