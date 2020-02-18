/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_accredito_tipo_cassa_econ database table.
 * 
 */
@Entity
@Table(name="siac_r_accredito_tipo_cassa_econ")
@NamedQuery(name="SiacRAccreditoTipoCassaEcon.findAll", query="SELECT s FROM SiacRAccreditoTipoCassaEcon s")
public class SiacRAccreditoTipoCassaEcon extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ACCREDITO_TIPO_CASSA_ECON_CECRACCREDITOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ACCREDITO_TIPO_CASSA_ECON_CEC_R_ACCREDITO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ACCREDITO_TIPO_CASSA_ECON_CECRACCREDITOTIPOID_GENERATOR")
	@Column(name="cec_r_accredito_tipo_id")
	private Integer cecRAccreditoTipoId;

	//bi-directional many-to-one association to SiacDAccreditoTipo
	@ManyToOne
	@JoinColumn(name="accredito_tipo_id")
	private SiacDAccreditoTipo siacDAccreditoTipo;

	//bi-directional many-to-one association to SiacDAccreditoTipoCassaEcon
	@ManyToOne
	@JoinColumn(name="cec_accredito_tipo_id")
	private SiacDAccreditoTipoCassaEcon siacDAccreditoTipoCassaEcon;

	//bi-directional many-to-one association to SiacTMovimento
	@OneToMany(mappedBy="siacRAccreditoTipoCassaEcon")
	private List<SiacTMovimento> siacTMovimentos;

	public SiacRAccreditoTipoCassaEcon() {
	}

	public Integer getCecRAccreditoTipoId() {
		return this.cecRAccreditoTipoId;
	}

	public void setCecRAccreditoTipoId(Integer cecRAccreditoTipoId) {
		this.cecRAccreditoTipoId = cecRAccreditoTipoId;
	}

	public SiacDAccreditoTipo getSiacDAccreditoTipo() {
		return this.siacDAccreditoTipo;
	}

	public void setSiacDAccreditoTipo(SiacDAccreditoTipo siacDAccreditoTipo) {
		this.siacDAccreditoTipo = siacDAccreditoTipo;
	}

	public SiacDAccreditoTipoCassaEcon getSiacDAccreditoTipoCassaEcon() {
		return this.siacDAccreditoTipoCassaEcon;
	}

	public void setSiacDAccreditoTipoCassaEcon(SiacDAccreditoTipoCassaEcon siacDAccreditoTipoCassaEcon) {
		this.siacDAccreditoTipoCassaEcon = siacDAccreditoTipoCassaEcon;
	}

	public List<SiacTMovimento> getSiacTMovimentos() {
		return this.siacTMovimentos;
	}

	public void setSiacTMovimentos(List<SiacTMovimento> siacTMovimentos) {
		this.siacTMovimentos = siacTMovimentos;
	}

	public SiacTMovimento addSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().add(siacTMovimento);
		siacTMovimento.setSiacRAccreditoTipoCassaEcon(this);

		return siacTMovimento;
	}

	public SiacTMovimento removeSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().remove(siacTMovimento);
		siacTMovimento.setSiacRAccreditoTipoCassaEcon(null);

		return siacTMovimento;
	}
	
	@Override
	public Integer getUid() {
		return cecRAccreditoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cecRAccreditoTipoId = uid;
	}

}