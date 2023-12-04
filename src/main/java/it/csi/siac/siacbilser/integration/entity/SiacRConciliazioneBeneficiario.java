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
 * The persistent class for the siac_r_conciliazione_beneficiario database table.
 * 
 */
@Entity
@Table(name="siac_r_conciliazione_beneficiario")
@NamedQuery(name="SiacRConciliazioneBeneficiario.findAll", query="SELECT s FROM SiacRConciliazioneBeneficiario s")
public class SiacRConciliazioneBeneficiario extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CONCILIAZIONE_BENEFICIARIO_CONCBENID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CONCILIAZIONE_BENEFICIARIO_CONCBEN_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CONCILIAZIONE_BENEFICIARIO_CONCBENID_GENERATOR")
	@Column(name="concben_id")
	private Integer concbenId;

	//bi-directional many-to-one association to SiacTBilElem
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;
	
	//bi-directional many-to-one association to SiacDConciliazioneClasse
	@ManyToOne
	@JoinColumn(name="conccla_id")
	private SiacDConciliazioneClasse siacDConciliazioneClasse;

	public SiacRConciliazioneBeneficiario() {
	}

	public Integer getConcbenId() {
		return this.concbenId;
	}

	public void setConcbenId(Integer concbenId) {
		this.concbenId = concbenId;
	}

	public SiacTBilElem getSiacTBilElem() {
		return siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}
	
	public SiacDConciliazioneClasse getSiacDConciliazioneClasse() {
		return siacDConciliazioneClasse;
	}

	public void setSiacDConciliazioneClasse(
			SiacDConciliazioneClasse siacDConciliazioneClasse) {
		this.siacDConciliazioneClasse = siacDConciliazioneClasse;
	}
	
	@Override
	public Integer getUid() {
		return this.concbenId;
	}

	@Override
	public void setUid(Integer uid) {
		this.concbenId = uid;
	}

}