/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
 * The persistent class for the siac_t_iva_stampa_valore database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_stampa_valore")
@NamedQuery(name="SiacTIvaStampaValore.findAll", query="SELECT s FROM SiacTIvaStampaValore s")
public class SiacTIvaStampaValore extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_IVA_STAMPA_VALORE_IVASTVALID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_IVA_STAMPA_VALORE_IVAST_VAL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_STAMPA_VALORE_IVASTVALID_GENERATOR")
	@Column(name="ivast_val_id")
	private Integer ivastValId;

	private String flagincassati;

	private String flagpagati;

	private String flagstampadef;

	private String flagstampaprovv;

	private Date ultimadataprotocollodef;

	private Date ultimadataprotocolloprovv;

	private Integer ultimapaginastampadef;

	private Integer ultimapaginastampaprovv;

	private String ultimonumprotocollodef;

	private String ultimonumprotocolloprovv;

	//bi-directional many-to-one association to SiacTIvaStampa
	@ManyToOne
	@JoinColumn(name="ivast_id")
	private SiacTIvaStampa siacTIvaStampa;

	public SiacTIvaStampaValore() {
	}

	public Integer getIvastValId() {
		return this.ivastValId;
	}

	public void setIvastValId(Integer ivastValId) {
		this.ivastValId = ivastValId;
	}

	public String getFlagincassati() {
		return this.flagincassati;
	}

	public void setFlagincassati(String flagincassati) {
		this.flagincassati = flagincassati;
	}

	public String getFlagpagati() {
		return this.flagpagati;
	}

	public void setFlagpagati(String flagpagati) {
		this.flagpagati = flagpagati;
	}

	public String getFlagstampadef() {
		return this.flagstampadef;
	}

	public void setFlagstampadef(String flagstampadef) {
		this.flagstampadef = flagstampadef;
	}

	public String getFlagstampaprovv() {
		return this.flagstampaprovv;
	}

	public void setFlagstampaprovv(String flagstampaprovv) {
		this.flagstampaprovv = flagstampaprovv;
	}

//	public BigDecimal getTotimponibiledef() {
//		return this.totimponibiledef;
//	}
//
//	public void setTotimponibiledef(BigDecimal totimponibiledef) {
//		this.totimponibiledef = totimponibiledef;
//	}
//
//	public BigDecimal getTotimponibileprovv() {
//		return this.totimponibileprovv;
//	}
//
//	public void setTotimponibileprovv(BigDecimal totimponibileprovv) {
//		this.totimponibileprovv = totimponibileprovv;
//	}
//
//	public BigDecimal getTotivadef() {
//		return this.totivadef;
//	}
//
//	public void setTotivadef(BigDecimal totivadef) {
//		this.totivadef = totivadef;
//	}
//
//	public BigDecimal getTotivadetraibiledef() {
//		return this.totivadetraibiledef;
//	}
//
//	public void setTotivadetraibiledef(BigDecimal totivadetraibiledef) {
//		this.totivadetraibiledef = totivadetraibiledef;
//	}
//
//	public BigDecimal getTotivadetraibileprovv() {
//		return this.totivadetraibileprovv;
//	}
//
//	public void setTotivadetraibileprovv(BigDecimal totivadetraibileprovv) {
//		this.totivadetraibileprovv = totivadetraibileprovv;
//	}
//
//	public BigDecimal getTotivaindetraibiledef() {
//		return this.totivaindetraibiledef;
//	}
//
//	public void setTotivaindetraibiledef(BigDecimal totivaindetraibiledef) {
//		this.totivaindetraibiledef = totivaindetraibiledef;
//	}
//
//	public BigDecimal getTotivaindetraibileprovv() {
//		return this.totivaindetraibileprovv;
//	}
//
//	public void setTotivaindetraibileprovv(BigDecimal totivaindetraibileprovv) {
//		this.totivaindetraibileprovv = totivaindetraibileprovv;
//	}
//
//	public BigDecimal getTotivaprovv() {
//		return this.totivaprovv;
//	}
//
//	public void setTotivaprovv(BigDecimal totivaprovv) {
//		this.totivaprovv = totivaprovv;
//	}

	public Date getUltimadataprotocollodef() {
		return this.ultimadataprotocollodef;
	}

	public void setUltimadataprotocollodef(Date ultimadataprotocollodef) {
		this.ultimadataprotocollodef = ultimadataprotocollodef;
	}

	public Date getUltimadataprotocolloprovv() {
		return this.ultimadataprotocolloprovv;
	}

	public void setUltimadataprotocolloprovv(Date ultimadataprotocolloprovv) {
		this.ultimadataprotocolloprovv = ultimadataprotocolloprovv;
	}

	public Integer getUltimapaginastampadef() {
		return this.ultimapaginastampadef;
	}

	public void setUltimapaginastampadef(Integer ultimapaginastampadef) {
		this.ultimapaginastampadef = ultimapaginastampadef;
	}

	public Integer getUltimapaginastampaprovv() {
		return this.ultimapaginastampaprovv;
	}

	public void setUltimapaginastampaprovv(Integer ultimapaginastampaprovv) {
		this.ultimapaginastampaprovv = ultimapaginastampaprovv;
	}

	public String getUltimonumprotocollodef() {
		return this.ultimonumprotocollodef;
	}

	public void setUltimonumprotocollodef(String ultimonumprotocollodef) {
		this.ultimonumprotocollodef = ultimonumprotocollodef;
	}

	public String getUltimonumprotocolloprovv() {
		return this.ultimonumprotocolloprovv;
	}

	public void setUltimonumprotocolloprovv(String ultimonumprotocolloprovv) {
		this.ultimonumprotocolloprovv = ultimonumprotocolloprovv;
	}

	public SiacTIvaStampa getSiacTIvaStampa() {
		return this.siacTIvaStampa;
	}

	public void setSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		this.siacTIvaStampa = siacTIvaStampa;
	}

	@Override
	public Integer getUid() {
		return ivastValId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastValId=uid;
		
	}

}