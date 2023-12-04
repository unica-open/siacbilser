/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;

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
 * The persistent class for the siac_t_cassa_econ_stampa_valore database table.
 * 
 */
@Entity
@Table(name="siac_t_cassa_econ_stampa_valore")
@NamedQuery(name="SiacTCassaEconStampaValore.findAll", query="SELECT s FROM SiacTCassaEconStampaValore s")
public class SiacTCassaEconStampaValore extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CASSA_ECON_STAMPA_VALORE_CESTVALID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CASSA_ECON_STAMPA_VALORE_CEST_VAL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CASSA_ECON_STAMPA_VALORE_CESTVALID_GENERATOR")
	@Column(name="cest_val_id")
	private Integer cestValId;

	
	@Column(name="gio_ultimadatastampadef")
	private Date gioUltimadatastampadef;

	@Column(name="gio_ultimapaginastampadef")
	private Integer gioUltimapaginastampadef;

	@Column(name="gio_ultimoimportoentrata_cc")
	private BigDecimal gioUltimoimportoentrataCc;

	@Column(name="gio_ultimoimportoentrata_contanti")
	private BigDecimal gioUltimoimportoentrataContanti;

	@Column(name="gio_ultimoimportouscita_cc")
	private BigDecimal gioUltimoimportouscitaCc;

	@Column(name="gio_ultimoimportouscita_contanti")
	private BigDecimal gioUltimoimportouscitaContanti;

	@Column(name="ren_data")
	private Date renData;

	@Column(name="ren_num")
	private Integer renNum;

	@Column(name="ren_periodofine")
	private Date renPeriodofine;

	@Column(name="ren_periodoinizio")
	private Date renPeriodoinizio;

	@Column(name="ric_nummovimento")
	private Integer ricNummovimento;


	//bi-directional many-to-one association to SiacTCassaEconStampa
	@ManyToOne
	@JoinColumn(name="cest_id")
	private SiacTCassaEconStampa siacTCassaEconStampa;

	public SiacTCassaEconStampaValore() {
	}

	public Integer getCestValId() {
		return this.cestValId;
	}

	public void setCestValId(Integer cestValId) {
		this.cestValId = cestValId;
	}

	public Date getGioUltimadatastampadef() {
		return this.gioUltimadatastampadef;
	}

	public void setGioUltimadatastampadef(Date gioUltimadatastampadef) {
		this.gioUltimadatastampadef = gioUltimadatastampadef;
	}

	public Integer getGioUltimapaginastampadef() {
		return this.gioUltimapaginastampadef;
	}

	public void setGioUltimapaginastampadef(Integer gioUltimapaginastampadef) {
		this.gioUltimapaginastampadef = gioUltimapaginastampadef;
	}

	public BigDecimal getGioUltimoimportoentrataCc() {
		return this.gioUltimoimportoentrataCc;
	}

	public void setGioUltimoimportoentrataCc(BigDecimal gioUltimoimportoentrataCc) {
		this.gioUltimoimportoentrataCc = gioUltimoimportoentrataCc;
	}

	public BigDecimal getGioUltimoimportoentrataContanti() {
		return this.gioUltimoimportoentrataContanti;
	}

	public void setGioUltimoimportoentrataContanti(BigDecimal gioUltimoimportoentrataContanti) {
		this.gioUltimoimportoentrataContanti = gioUltimoimportoentrataContanti;
	}

	public BigDecimal getGioUltimoimportouscitaCc() {
		return this.gioUltimoimportouscitaCc;
	}

	public void setGioUltimoimportouscitaCc(BigDecimal gioUltimoimportouscitaCc) {
		this.gioUltimoimportouscitaCc = gioUltimoimportouscitaCc;
	}

	public BigDecimal getGioUltimoimportouscitaContanti() {
		return this.gioUltimoimportouscitaContanti;
	}

	public void setGioUltimoimportouscitaContanti(BigDecimal gioUltimoimportouscitaContanti) {
		this.gioUltimoimportouscitaContanti = gioUltimoimportouscitaContanti;
	}

	public Date getRenData() {
		return this.renData;
	}

	public void setRenData(Date renData) {
		this.renData = renData;
	}

	public Integer getRenNum() {
		return this.renNum;
	}

	public void setRenNum(Integer renNum) {
		this.renNum = renNum;
	}

	public Date getRenPeriodofine() {
		return this.renPeriodofine;
	}

	public void setRenPeriodofine(Date renPeriodofine) {
		this.renPeriodofine = renPeriodofine;
	}

	public Date getRenPeriodoinizio() {
		return this.renPeriodoinizio;
	}

	public void setRenPeriodoinizio(Date renPeriodoinizio) {
		this.renPeriodoinizio = renPeriodoinizio;
	}

	public Integer getRicNummovimento() {
		return this.ricNummovimento;
	}

	public void setRicNummovimento(Integer ricNummovimento) {
		this.ricNummovimento = ricNummovimento;
	}

	public SiacTCassaEconStampa getSiacTCassaEconStampa() {
		return this.siacTCassaEconStampa;
	}

	public void setSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		this.siacTCassaEconStampa = siacTCassaEconStampa;
	}

	@Override
	public Integer getUid() {
		return this.cestValId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cestValId = uid;
		
	}

}