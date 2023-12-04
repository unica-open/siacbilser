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
import javax.persistence.Version;


/**
 * The persistent class for the siac_t_eldoc database table.
 * 
 */
@Entity
@Table(name="siac_t_doc_num")
@NamedQuery(name="SiacTDocNum.findAll", query="SELECT s FROM SiacTDocNum s")
public class SiacTDocNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_DOC_DOCNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_DOC_NUM_DOC_NUM_ID_SEQ") //"siac_t_doc_num_doc_num_id_seq" 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_DOC_DOCNUMID_GENERATOR")
	@Column(name="doc_num_id")
	private Integer docNumId;
	
	/** The doc numero. */
	@Version
	@Column(name="doc_numero")
	private Integer docNumero;

	/** The doc anno. */
	@Column(name="doc_anno")
	private Integer docAnno;

	
	//bi-directional many-to-one association to SiacDDocTipo
	/** The siac d doc tipo. */
	@ManyToOne
	@JoinColumn(name="doc_tipo_id")
	private SiacDDocTipo siacDDocTipo;
	
	/**
	 * @return the siacDDocTipo
	 */
	public SiacDDocTipo getSiacDDocTipo() {
		return siacDDocTipo;
	}

	/**
	 * @param siacDDocTipo the siacDDocTipo to set
	 */
	public void setSiacDDocTipo(SiacDDocTipo siacDDocTipo) {
		this.siacDDocTipo = siacDDocTipo;
	}

	/**
	 * Instantiates a new siac t doc num.
	 */
	public SiacTDocNum() {
	}

	/**
	 * @return the docNumId
	 */
	public Integer getDocNumId() {
		return docNumId;
	}

	/**
	 * @param docNumId the docNumId to set
	 */
	public void setDocNumId(Integer docNumId) {
		this.docNumId = docNumId;
	}

	/**
	 * @param docNumId the docNumId to set
	 */
	public void setEldocNumId(Integer docNumId) {
		this.docNumId = docNumId;
	}

	/**
	 * @return the docNumero
	 */
	public Integer getDocNumero() {
		return docNumero;
	}

	/**
	 * @param docNumero the docNumero to set
	 */
	public void setDocNumero(Integer docNumero) {
		this.docNumero = docNumero;
	}

	/**
	 * @return the docAnno
	 */
	public Integer getDocAnno() {
		return docAnno;
	}

	/**
	 * @param docAnno the docAnno to set
	 */
	public void setDocAnno(Integer docAnno) {
		this.docAnno = docAnno;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docNumId = uid;		
	}


}