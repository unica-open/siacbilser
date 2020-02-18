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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_doc_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_tipo")
@NamedQuery(name="SiacDDocTipo.findAll", query="SELECT s FROM SiacDDocTipo s")
public class SiacDDocTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_DOC_TIPO_DOCTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_DOC_TIPO_DOC_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DOC_TIPO_DOCTIPOID_GENERATOR")
	@Column(name="doc_tipo_id")
	private Integer docTipoId;	

	/** The doc tipo code. */
	@Column(name="doc_tipo_code")
	private String docTipoCode;

	/** The doc tipo desc. */
	@Column(name="doc_tipo_desc")
	private String docTipoDesc;

	//bi-directional many-to-one association to SiacDDocFamTipo
	/** The siac d doc fam tipo. */
	@ManyToOne
	@JoinColumn(name="doc_fam_tipo_id")
	private SiacDDocFamTipo siacDDocFamTipo;

	//bi-directional many-to-one association to SiacDDocGruppo
	/** The siac d doc gruppo. */
	@ManyToOne
	@JoinColumn(name="doc_gruppo_tipo_id")
	private SiacDDocGruppo siacDDocGruppo;

	//bi-directional many-to-one association to SiacRDocTipoAttr
	/** The siac r doc tipo attrs. */
	@OneToMany(mappedBy="siacDDocTipo")
	private List<SiacRDocTipoAttr> siacRDocTipoAttrs;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t docs. */
	@OneToMany(mappedBy="siacDDocTipo")
	private List<SiacTDoc> siacTDocs;

	//bi-directional many-to-one association to SiacTDocIva
	@OneToMany(mappedBy="siacDDocTipo")
	private List<SiacTDocIva> siacTDocIvas;

	/**
	 * Instantiates a new siac d doc tipo.
	 */
	public SiacDDocTipo() {
	}

	/**
	 * Gets the doc tipo id.
	 *
	 * @return the doc tipo id
	 */
	public Integer getDocTipoId() {
		return this.docTipoId;
	}

	/**
	 * Sets the doc tipo id.
	 *
	 * @param docTipoId the new doc tipo id
	 */
	public void setDocTipoId(Integer docTipoId) {
		this.docTipoId = docTipoId;
	}


	/**
	 * Gets the doc tipo code.
	 *
	 * @return the doc tipo code
	 */
	public String getDocTipoCode() {
		return this.docTipoCode;
	}

	/**
	 * Sets the doc tipo code.
	 *
	 * @param docTipoCode the new doc tipo code
	 */
	public void setDocTipoCode(String docTipoCode) {
		this.docTipoCode = docTipoCode;
	}

	/**
	 * Gets the doc tipo desc.
	 *
	 * @return the doc tipo desc
	 */
	public String getDocTipoDesc() {
		return this.docTipoDesc;
	}

	/**
	 * Sets the doc tipo desc.
	 *
	 * @param docTipoDesc the new doc tipo desc
	 */
	public void setDocTipoDesc(String docTipoDesc) {
		this.docTipoDesc = docTipoDesc;
	}

	

	/**
	 * Gets the siac d doc fam tipo.
	 *
	 * @return the siac d doc fam tipo
	 */
	public SiacDDocFamTipo getSiacDDocFamTipo() {
		return this.siacDDocFamTipo;
	}

	/**
	 * Sets the siac d doc fam tipo.
	 *
	 * @param siacDDocFamTipo the new siac d doc fam tipo
	 */
	public void setSiacDDocFamTipo(SiacDDocFamTipo siacDDocFamTipo) {
		this.siacDDocFamTipo = siacDDocFamTipo;
	}

	public SiacDDocGruppo getSiacDDocGruppo() {
		return this.siacDDocGruppo;
	}

	public void setSiacDDocGruppo(SiacDDocGruppo siacDDocGruppo) {
		this.siacDDocGruppo = siacDDocGruppo;
	}

	/**
	 * Gets the siac r doc tipo attrs.
	 *
	 * @return the siac r doc tipo attrs
	 */
	public List<SiacRDocTipoAttr> getSiacRDocTipoAttrs() {
		return this.siacRDocTipoAttrs;
	}

	/**
	 * Sets the siac r doc tipo attrs.
	 *
	 * @param siacRDocTipoAttrs the new siac r doc tipo attrs
	 */
	public void setSiacRDocTipoAttrs(List<SiacRDocTipoAttr> siacRDocTipoAttrs) {
		this.siacRDocTipoAttrs = siacRDocTipoAttrs;
	}

	/**
	 * Adds the siac r doc tipo attr.
	 *
	 * @param siacRDocTipoAttr the siac r doc tipo attr
	 * @return the siac r doc tipo attr
	 */
	public SiacRDocTipoAttr addSiacRDocTipoAttr(SiacRDocTipoAttr siacRDocTipoAttr) {
		getSiacRDocTipoAttrs().add(siacRDocTipoAttr);
		siacRDocTipoAttr.setSiacDDocTipo(this);

		return siacRDocTipoAttr;
	}

	/**
	 * Removes the siac r doc tipo attr.
	 *
	 * @param siacRDocTipoAttr the siac r doc tipo attr
	 * @return the siac r doc tipo attr
	 */
	public SiacRDocTipoAttr removeSiacRDocTipoAttr(SiacRDocTipoAttr siacRDocTipoAttr) {
		getSiacRDocTipoAttrs().remove(siacRDocTipoAttr);
		siacRDocTipoAttr.setSiacDDocTipo(null);

		return siacRDocTipoAttr;
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
		siacTDoc.setSiacDDocTipo(this);

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
		siacTDoc.setSiacDDocTipo(null);

		return siacTDoc;
	}

	public List<SiacTDocIva> getSiacTDocIvas() {
		return this.siacTDocIvas;
	}

	public void setSiacTDocIvas(List<SiacTDocIva> siacTDocIvas) {
		this.siacTDocIvas = siacTDocIvas;
	}

	public SiacTDocIva addSiacTDocIva(SiacTDocIva siacTDocIva) {
		getSiacTDocIvas().add(siacTDocIva);
		siacTDocIva.setSiacDDocTipo(this);

		return siacTDocIva;
	}

	public SiacTDocIva removeSiacTDocIva(SiacTDocIva siacTDocIva) {
		getSiacTDocIvas().remove(siacTDocIva);
		siacTDocIva.setSiacDDocTipo(null);

		return siacTDocIva;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docTipoId = uid;		
	}

}