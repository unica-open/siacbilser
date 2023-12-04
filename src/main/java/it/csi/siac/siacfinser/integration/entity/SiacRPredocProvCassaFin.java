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
 * The persistent class for the siac_r_predoc_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_prov_cassa")
public class SiacRPredocProvCassaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_PROV_CASSA_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_predoc_prov_cassa_predoc_provc_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_PROV_CASSA_ID_GENERATOR")
	@Column(name="predoc_provc_id")
	private Integer predocProvcId;

	//bi-directional many-to-one association to SiacTPredocFin
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredocFin siacTPredoc;

	//bi-directional many-to-one association to SiacTProvCassaFin
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassaFin siacTProvCassa;

	public SiacRPredocProvCassaFin() {
	}

	public Integer getPredocProvcId() {
		return this.predocProvcId;
	}

	public void setPredocProvcId(Integer predocProvcId) {
		this.predocProvcId = predocProvcId;
	}

	public SiacTPredocFin getSiacTPredoc() {
		return this.siacTPredoc;
	}

	public void setSiacTPredoc(SiacTPredocFin siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	public SiacTProvCassaFin getSiacTProvCassa() {
		return this.siacTProvCassa;
	}

	public void setSiacTProvCassa(SiacTProvCassaFin siacTProvCassa) {
		this.siacTProvCassa = siacTProvCassa;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.predocProvcId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.predocProvcId = uid;
	}
}