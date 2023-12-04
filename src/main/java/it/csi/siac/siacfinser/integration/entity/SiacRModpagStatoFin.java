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
 * The persistent class for the siac_r_modpag_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_modpag_stato")
public class SiacRModpagStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MODPAG_STATO_ID_GENRATOR", allocationSize=1, sequenceName="siac_r_modpag_stato_modpag_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODPAG_STATO_ID_GENRATOR")
	@Column(name="modpag_stato_r_id")
	private Integer modpagStatoRId;

	//bi-directional many-to-one association to SiacDModpagStatoFin
	@ManyToOne
	@JoinColumn(name="modpag_stato_id")
	private SiacDModpagStatoFin siacDModpagStato;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;

	public SiacRModpagStatoFin() {
	}

	public Integer getModpagStatoRId() {
		return this.modpagStatoRId;
	}

	public void setModpagStatoRId(Integer modpagStatoRId) {
		this.modpagStatoRId = modpagStatoRId;
	}

	public SiacDModpagStatoFin getSiacDModpagStato() {
		return this.siacDModpagStato;
	}

	public void setSiacDModpagStato(SiacDModpagStatoFin siacDModpagStato) {
		this.siacDModpagStato = siacDModpagStato;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.modpagStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.modpagStatoRId = uid;
	}
}