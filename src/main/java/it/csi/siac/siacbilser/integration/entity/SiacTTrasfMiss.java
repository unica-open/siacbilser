/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the siac_t_trasf_miss database table.
 * 
 */
@Entity
@Table(name="siac_t_trasf_miss")
@NamedQuery(name="SiacTTrasfMiss.findAll", query="SELECT s FROM SiacTTrasfMiss s")
public class SiacTTrasfMiss extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_TRASF_MISS_TRAMISID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_TRASF_MISS_TRAMIS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_TRASF_MISS_TRAMISID_GENERATOR")
	@Column(name="tramis_id")
	private Integer tramisId;

	@Column(name="tramis_code")
	private String tramisCode;

	@Column(name="tramis_desc")
	private String tramisDesc;

	@Temporal(TemporalType.DATE)
	@Column(name="tramis_fine")
	private Date tramisFine;

	@Column(name="tramis_flagestero")
	private Boolean tramisFlagestero;

	@Temporal(TemporalType.DATE)
	@Column(name="tramis_inizio")
	private Date tramisInizio;

	@Column(name="tramis_luogo")
	private String tramisLuogo;

	@Column(name="unita_organizzativa")
	private String unitaOrganizzativa;

	//bi-directional many-to-one association to SiacRTrasfMissTrasporto
	@OneToMany(mappedBy="siacTTrasfMiss", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRTrasfMissTrasporto> siacRTrasfMissTrasportos;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	public SiacTTrasfMiss() {
	}

	public Integer getTramisId() {
		return this.tramisId;
	}

	public void setTramisId(Integer tramisId) {
		this.tramisId = tramisId;
	}

	public String getTramisCode() {
		return this.tramisCode;
	}

	public void setTramisCode(String tramisCode) {
		this.tramisCode = tramisCode;
	}

	public String getTramisDesc() {
		return this.tramisDesc;
	}

	public void setTramisDesc(String tramisDesc) {
		this.tramisDesc = tramisDesc;
	}

	public Date getTramisFine() {
		return this.tramisFine;
	}

	public void setTramisFine(Date tramisFine) {
		this.tramisFine = tramisFine;
	}

	public Boolean getTramisFlagestero() {
		return this.tramisFlagestero;
	}

	public void setTramisFlagestero(Boolean tramisFlagestero) {
		this.tramisFlagestero = tramisFlagestero;
	}

	public Date getTramisInizio() {
		return this.tramisInizio;
	}

	public void setTramisInizio(Date tramisInizio) {
		this.tramisInizio = tramisInizio;
	}

	public String getTramisLuogo() {
		return this.tramisLuogo;
	}

	public void setTramisLuogo(String tramisLuogo) {
		this.tramisLuogo = tramisLuogo;
	}

	public String getUnitaOrganizzativa() {
		return this.unitaOrganizzativa;
	}

	public void setUnitaOrganizzativa(String unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}

	public List<SiacRTrasfMissTrasporto> getSiacRTrasfMissTrasportos() {
		return this.siacRTrasfMissTrasportos;
	}

	public void setSiacRTrasfMissTrasportos(List<SiacRTrasfMissTrasporto> siacRTrasfMissTrasportos) {
		this.siacRTrasfMissTrasportos = siacRTrasfMissTrasportos;
	}

	public SiacRTrasfMissTrasporto addSiacRTrasfMissTrasporto(SiacRTrasfMissTrasporto siacRTrasfMissTrasporto) {
		getSiacRTrasfMissTrasportos().add(siacRTrasfMissTrasporto);
		siacRTrasfMissTrasporto.setSiacTTrasfMiss(this);

		return siacRTrasfMissTrasporto;
	}

	public SiacRTrasfMissTrasporto removeSiacRTrasfMissTrasporto(SiacRTrasfMissTrasporto siacRTrasfMissTrasporto) {
		getSiacRTrasfMissTrasportos().remove(siacRTrasfMissTrasporto);
		siacRTrasfMissTrasporto.setSiacTTrasfMiss(null);

		return siacRTrasfMissTrasporto;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}
	
	@Override
	public Integer getUid() {
		return tramisId;
	}

	@Override
	public void setUid(Integer uid) {
		this.tramisId = uid;
	}

}