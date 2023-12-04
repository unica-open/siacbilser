/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_bil_elem_class database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_previsione_impacc")
public class SiacRBilElemPrevisioneImpacc extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_PREVISIONE_IMPACC_BILELEMPREVID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_PREVISIONE_IMPACC_BIL_ELEM_PREV_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_PREVISIONE_IMPACC_BILELEMPREVID_GENERATOR")
	@Column(name="bil_elem_prev_id")
	private Integer bilElemPrevId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;
	
	@Column(name="importo_prev_anno1")
	private BigDecimal importoPrevAnno1;
	
	@Column(name="importo_prev_anno2")
	private BigDecimal importoPrevAnno2;
	
	@Column(name="importo_prev_anno3")
	private BigDecimal importoPrevAnno3;
	
	@Column(name="importo_prev_note")
	private String importoPrevNote;

	
	/**
	 * Instantiates a new siac r bil elem class.
	 */
	public SiacRBilElemPrevisioneImpacc() {
	}

	public Integer getBilElemPrevId() {
		return bilElemPrevId;
	}



	public void setBilElemPrevId(Integer bilElemPrevId) {
		this.bilElemPrevId = bilElemPrevId;
	}



	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}


	
	
	public BigDecimal getImportoPrevAnno1() {
		return importoPrevAnno1;
	}

	public void setImportoPrevAnno1(BigDecimal importoPrevAnno1) {
		this.importoPrevAnno1 = importoPrevAnno1;
	}

	public BigDecimal getImportoPrevAnno2() {
		return importoPrevAnno2;
	}

	public void setImportoPrevAnno2(BigDecimal importoPrevAnno2) {
		this.importoPrevAnno2 = importoPrevAnno2;
	}

	public BigDecimal getImportoPrevAnno3() {
		return importoPrevAnno3;
	}

	public void setImportoPrevAnno3(BigDecimal importoPrevAnno3) {
		this.importoPrevAnno3 = importoPrevAnno3;
	}

	public String getImportoPrevNote() {
		return importoPrevNote;
	}

	public void setImportoPrevNote(String importoPrevNote) {
		this.importoPrevNote = importoPrevNote;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilElemPrevId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilElemPrevId = uid;
	}

}