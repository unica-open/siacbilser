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
 * The persistent class for the siac_d_doc_fam_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_fam_tipo")
@NamedQuery(name="SiacDDocFamTipo.findAll", query="SELECT s FROM SiacDDocFamTipo s")
public class SiacDDocFamTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc fam tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_DOC_FAM_TIPO_DOCFAMTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_DOC_FAM_TIPO_DOC_FAM_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DOC_FAM_TIPO_DOCFAMTIPOID_GENERATOR")
	@Column(name="doc_fam_tipo_id")
	private Integer docFamTipoId;

	/** The doc fam tipo code. */
	@Column(name="doc_fam_tipo_code")
	private String docFamTipoCode;

	/** The doc fam tipo desc. */
	@Column(name="doc_fam_tipo_desc")
	private String docFamTipoDesc;	

	//bi-directional many-to-one association to SiacDDocTipo
	/** The siac d doc tipos. */
	@OneToMany(mappedBy="siacDDocFamTipo")
	private List<SiacDDocTipo> siacDDocTipos;

	//bi-directional many-to-one association to SiacRIvaRegTipoDocFamTipo
	@OneToMany(mappedBy="siacDDocFamTipo")
	private List<SiacRIvaRegTipoDocFamTipo> siacRIvaRegTipoDocFamTipos;

	//bi-directional many-to-one association to SiacTPredoc
	@OneToMany(mappedBy="siacDDocFamTipo")
	private List<SiacTPredoc> siacTPredocs;

	/**
	 * Instantiates a new siac d doc fam tipo.
	 */
	public SiacDDocFamTipo() {
	}

	/**
	 * Gets the doc fam tipo id.
	 *
	 * @return the doc fam tipo id
	 */
	public Integer getDocFamTipoId() {
		return this.docFamTipoId;
	}

	/**
	 * Sets the doc fam tipo id.
	 *
	 * @param docFamTipoId the new doc fam tipo id
	 */
	public void setDocFamTipoId(Integer docFamTipoId) {
		this.docFamTipoId = docFamTipoId;
	}

	

	

	/**
	 * Gets the doc fam tipo code.
	 *
	 * @return the doc fam tipo code
	 */
	public String getDocFamTipoCode() {
		return this.docFamTipoCode;
	}

	/**
	 * Sets the doc fam tipo code.
	 *
	 * @param docFamTipoCode the new doc fam tipo code
	 */
	public void setDocFamTipoCode(String docFamTipoCode) {
		this.docFamTipoCode = docFamTipoCode;
	}

	/**
	 * Gets the doc fam tipo desc.
	 *
	 * @return the doc fam tipo desc
	 */
	public String getDocFamTipoDesc() {
		return this.docFamTipoDesc;
	}

	/**
	 * Sets the doc fam tipo desc.
	 *
	 * @param docFamTipoDesc the new doc fam tipo desc
	 */
	public void setDocFamTipoDesc(String docFamTipoDesc) {
		this.docFamTipoDesc = docFamTipoDesc;
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
		siacDDocTipo.setSiacDDocFamTipo(this);

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
		siacDDocTipo.setSiacDDocFamTipo(null);

		return siacDDocTipo;
	}

	public List<SiacRIvaRegTipoDocFamTipo> getSiacRIvaRegTipoDocFamTipos() {
		return this.siacRIvaRegTipoDocFamTipos;
	}

	public void setSiacRIvaRegTipoDocFamTipos(List<SiacRIvaRegTipoDocFamTipo> siacRIvaRegTipoDocFamTipos) {
		this.siacRIvaRegTipoDocFamTipos = siacRIvaRegTipoDocFamTipos;
	}

	public SiacRIvaRegTipoDocFamTipo addSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipo siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().add(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDDocFamTipo(this);

		return siacRIvaRegTipoDocFamTipo;
	}

	public SiacRIvaRegTipoDocFamTipo removeSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipo siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().remove(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDDocFamTipo(null);

		return siacRIvaRegTipoDocFamTipo;
	}

	public List<SiacTPredoc> getSiacTPredocs() {
		return this.siacTPredocs;
	}

	public void setSiacTPredocs(List<SiacTPredoc> siacTPredocs) {
		this.siacTPredocs = siacTPredocs;
	}

	public SiacTPredoc addSiacTPredoc(SiacTPredoc siacTPredoc) {
		getSiacTPredocs().add(siacTPredoc);
		siacTPredoc.setSiacDDocFamTipo(this);

		return siacTPredoc;
	}

	public SiacTPredoc removeSiacTPredoc(SiacTPredoc siacTPredoc) {
		getSiacTPredocs().remove(siacTPredoc);
		siacTPredoc.setSiacDDocFamTipo(null);

		return siacTPredoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docFamTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docFamTipoId = uid; 
		
	}

}