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


/**
 * The persistent class for the siac_r_doc_onere_ordinativo_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_onere_ordinativo_ts")
@NamedQuery(name="SiacRDocOnereOrdinativoT.findAll", query="SELECT s FROM SiacRDocOnereOrdinativoT s")
public class SiacRDocOnereOrdinativoT extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_DOC_ONERE_ORDINATIVO_TS_DOCONEREORDTSID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_DOC_ONERE_ORDINATIVO_TS_DOC_ONERE_ORD_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_DOC_ONERE_ORDINATIVO_TS_DOCONEREORDTSID_GENERATOR")
	@Column(name="doc_onere_ord_ts_id")
	private Integer docOnereOrdTsId;

	//bi-directional many-to-one association to SiacRDocOnere
	@ManyToOne
	@JoinColumn(name="doc_onere_id")
	private SiacRDocOnere siacRDocOnere;

	//bi-directional many-to-one association to SiacTOrdinativoT
	@ManyToOne
	@JoinColumn(name="ord_ts_id")
	private SiacTOrdinativoT siacTOrdinativoT;

	public SiacRDocOnereOrdinativoT() {
	}

	public Integer getDocOnereOrdTsId() {
		return this.docOnereOrdTsId;
	}

	public void setDocOnereOrdTsId(Integer docOnereOrdTsId) {
		this.docOnereOrdTsId = docOnereOrdTsId;
	}

	public SiacRDocOnere getSiacRDocOnere() {
		return this.siacRDocOnere;
	}

	public void setSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		this.siacRDocOnere = siacRDocOnere;
	}

	public SiacTOrdinativoT getSiacTOrdinativoT() {
		return this.siacTOrdinativoT;
	}

	public void setSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
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