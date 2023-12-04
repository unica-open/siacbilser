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
 * The persistent class for the siac_r_movimento_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_movimento_modpag")
@NamedQuery(name="SiacRMovimentoModpag.findAll", query="SELECT s FROM SiacRMovimentoModpag s")
public class SiacRMovimentoModpag extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVIMENTO_MODPAG_MOVTMODPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVIMENTO_MODPAG_MOVT_MODPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVIMENTO_MODPAG_MOVTMODPAGID_GENERATOR")
	@Column(name="movt_modpag_id")
	private Integer movtModpagId;

	//bi-directional many-to-one association to SiacTModpag
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	//bi-directional many-to-one association to SiacTMovimento
	@ManyToOne
	@JoinColumn(name="movt_id")
	private SiacTMovimento siacTMovimento;

	public SiacRMovimentoModpag() {
	}

	public Integer getMovtModpagId() {
		return this.movtModpagId;
	}

	public void setMovtModpagId(Integer movtModpagId) {
		this.movtModpagId = movtModpagId;
	}

	public SiacTModpag getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpag siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	public SiacTMovimento getSiacTMovimento() {
		return this.siacTMovimento;
	}

	public void setSiacTMovimento(SiacTMovimento siacTMovimento) {
		this.siacTMovimento = siacTMovimento;
	}
	
	@Override
	public Integer getUid() {
		return movtModpagId;
	}

	@Override
	public void setUid(Integer uid) {
		this.movtModpagId = uid;
	}

}