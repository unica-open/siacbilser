/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_doc_onere_ordinativo_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_onere_ordinativo_ts")
public class SiacRDocOnereOrdinativoTsFin extends SiacTEnteBase  {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_DOC_ONERE_ORDINATIVO_TS_DOCONEREORDTSID_GENERATOR_FIN", allocationSize = 1, sequenceName="SIAC_R_DOC_ONERE_ORDINATIVO_TS_DOC_ONERE_ORD_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_ONERE_ORDINATIVO_TS_DOCONEREORDTSID_GENERATOR_FIN")
	@Column(name="doc_onere_ord_ts_id")
	private Integer docOnereOrdTsId;

	//bi-directional many-to-one association to SiacRDocOnere
	@ManyToOne
	@JoinColumn(name="doc_onere_id")
	private SiacRDocOnereFin siacRDocOnere;

	//bi-directional many-to-one association to SiacTOrdinativoT
	@ManyToOne
	@JoinColumn(name="ord_ts_id")
	private SiacTOrdinativoTFin siacTOrdinativoT;

	public SiacRDocOnereOrdinativoTsFin() {
	}

	public Integer getDocOnereOrdTsId() {
		return this.docOnereOrdTsId;
	}

	public void setDocOnereOrdTsId(Integer docOnereOrdTsId) {
		this.docOnereOrdTsId = docOnereOrdTsId;
	}



	/**
	 * @return the siacRDocOnere
	 */
	public SiacRDocOnereFin getSiacRDocOnere() {
		return siacRDocOnere;
	}

	/**
	 * @param siacRDocOnere the siacRDocOnere to set
	 */
	public void setSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		this.siacRDocOnere = siacRDocOnere;
	}

	/**
	 * @return the siacTOrdinativoT
	 */
	public SiacTOrdinativoTFin getSiacTOrdinativoT() {
		return siacTOrdinativoT;
	}

	/**
	 * @param siacTOrdinativoT the siacTOrdinativoT to set
	 */
	public void setSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
		this.siacTOrdinativoT = siacTOrdinativoT;
	}

	@Override
	public Integer getUid() {
		return docOnereOrdTsId;
	}

	@Override
	public void setUid(Integer uid) {
		docOnereOrdTsId = uid;
	}

}