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
 * The persistent class for the siac_r_giustificativo_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_giustificativo_movgest")
@NamedQuery(name="SiacRGiustificativoMovgest.findAll", query="SELECT s FROM SiacRGiustificativoMovgest s")
public class SiacRGiustificativoMovgest extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_GIUSTIFICATIVO_MOVGEST_GSTMOVGESTID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_GIUSTIFICATIVO_MOVGEST_gstmovgest_id_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GIUSTIFICATIVO_MOVGEST_GSTMOVGESTID_GENERATOR")
	@Column(name="gstmovgest_id")
	private Integer gstmovgestId;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTGiustificativoDet
	@ManyToOne
	@JoinColumn(name="gst_id")
	private SiacTGiustificativo siacTGiustificativo;

	public SiacRGiustificativoMovgest() {
	}

	public Integer getGstmovgestId() {
		return this.gstmovgestId;
	}

	public void setGstmovgestId(Integer gstmovgestId) {
		this.gstmovgestId = gstmovgestId;
	}

	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public SiacTGiustificativo getSiacTGiustificativo() {
		return this.siacTGiustificativo;
	}

	public void setSiacTGiustificativo(SiacTGiustificativo siacTGiustificativo) {
		this.siacTGiustificativo = siacTGiustificativo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.gstmovgestId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gstmovgestId = uid;
		
	}

}