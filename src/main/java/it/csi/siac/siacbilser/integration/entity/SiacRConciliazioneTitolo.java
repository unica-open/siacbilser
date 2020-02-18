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
 * The persistent class for the siac_r_conciliazione_titolo database table.
 * 
 */
@Entity
@Table(name="siac_r_conciliazione_titolo")
@NamedQuery(name="SiacRConciliazioneTitolo.findAll", query="SELECT s FROM SiacRConciliazioneTitolo s")
public class SiacRConciliazioneTitolo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CONCILIAZIONE_TITOLO_CONCTITID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CONCILIAZIONE_TITOLO_CONCTIT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CONCILIAZIONE_TITOLO_CONCTITID_GENERATOR")
	@Column(name="conctit_id")
	private Integer conctitId;

	//bi-directional many-to-one association to SiacDConciliazioneClasse
	@ManyToOne
	@JoinColumn(name="conccla_id")
	private SiacDConciliazioneClasse siacDConciliazioneClasse;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto;

	public SiacRConciliazioneTitolo() {
	}

	public Integer getConctitId() {
		return this.conctitId;
	}

	public void setConctitId(Integer conctitId) {
		this.conctitId = conctitId;
	}

	public SiacDConciliazioneClasse getSiacDConciliazioneClasse() {
		return this.siacDConciliazioneClasse;
	}

	public void setSiacDConciliazioneClasse(SiacDConciliazioneClasse siacDConciliazioneClasse) {
		this.siacDConciliazioneClasse = siacDConciliazioneClasse;
	}

	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	@Override
	public Integer getUid() {
		return this.conctitId;
	}

	@Override
	public void setUid(Integer uid) {
		this.conctitId = uid;
	}

}