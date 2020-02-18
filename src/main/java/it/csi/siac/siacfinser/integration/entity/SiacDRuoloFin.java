/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_ruolo database table.
 * 
 */
@Entity
@Table(name="siac_d_ruolo")
public class SiacDRuoloFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ruolo_id")
	private Integer ruoloId;

	@Column(name="ruolo_code")
	private String ruoloCode;

	@Column(name="ruolo_desc")
	private String ruoloDesc;

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
	@OneToMany(mappedBy="siacDRuolo1")
	private List<SiacRSoggettoRelazFin> siacRSoggettoRelazs1;

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
	@OneToMany(mappedBy="siacDRuolo2")
	private List<SiacRSoggettoRelazFin> siacRSoggettoRelazs2;

	//bi-directional many-to-one association to SiacRSoggettoRelazModFin
	@OneToMany(mappedBy="siacDRuolo1")
	private List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods1;

	//bi-directional many-to-one association to SiacRSoggettoRelazModFin
	@OneToMany(mappedBy="siacDRuolo2")
	private List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods2;

	//bi-directional many-to-one association to SiacRSoggettoRuoloFin
	@OneToMany(mappedBy="siacDRuolo")
	private List<SiacRSoggettoRuoloFin> siacRSoggettoRuolos;

	public SiacDRuoloFin() {
	}

	public Integer getRuoloId() {
		return this.ruoloId;
	}

	public void setRuoloId(Integer ruoloId) {
		this.ruoloId = ruoloId;
	}

	public String getRuoloCode() {
		return this.ruoloCode;
	}

	public void setRuoloCode(String ruoloCode) {
		this.ruoloCode = ruoloCode;
	}

	public String getRuoloDesc() {
		return this.ruoloDesc;
	}

	public void setRuoloDesc(String ruoloDesc) {
		this.ruoloDesc = ruoloDesc;
	}

	public List<SiacRSoggettoRelazFin> getSiacRSoggettoRelazs1() {
		return this.siacRSoggettoRelazs1;
	}

	public void setSiacRSoggettoRelazs1(List<SiacRSoggettoRelazFin> siacRSoggettoRelazs1) {
		this.siacRSoggettoRelazs1 = siacRSoggettoRelazs1;
	}

	public SiacRSoggettoRelazFin addSiacRSoggettoRelazs1(SiacRSoggettoRelazFin siacRSoggettoRelazs1) {
		getSiacRSoggettoRelazs1().add(siacRSoggettoRelazs1);
		siacRSoggettoRelazs1.setSiacDRuolo1(this);

		return siacRSoggettoRelazs1;
	}

	public SiacRSoggettoRelazFin removeSiacRSoggettoRelazs1(SiacRSoggettoRelazFin siacRSoggettoRelazs1) {
		getSiacRSoggettoRelazs1().remove(siacRSoggettoRelazs1);
		siacRSoggettoRelazs1.setSiacDRuolo1(null);

		return siacRSoggettoRelazs1;
	}

	public List<SiacRSoggettoRelazFin> getSiacRSoggettoRelazs2() {
		return this.siacRSoggettoRelazs2;
	}

	public void setSiacRSoggettoRelazs2(List<SiacRSoggettoRelazFin> siacRSoggettoRelazs2) {
		this.siacRSoggettoRelazs2 = siacRSoggettoRelazs2;
	}

	public SiacRSoggettoRelazFin addSiacRSoggettoRelazs2(SiacRSoggettoRelazFin siacRSoggettoRelazs2) {
		getSiacRSoggettoRelazs2().add(siacRSoggettoRelazs2);
		siacRSoggettoRelazs2.setSiacDRuolo2(this);

		return siacRSoggettoRelazs2;
	}

	public SiacRSoggettoRelazFin removeSiacRSoggettoRelazs2(SiacRSoggettoRelazFin siacRSoggettoRelazs2) {
		getSiacRSoggettoRelazs2().remove(siacRSoggettoRelazs2);
		siacRSoggettoRelazs2.setSiacDRuolo2(null);

		return siacRSoggettoRelazs2;
	}

	public List<SiacRSoggettoRelazModFin> getSiacRSoggettoRelazMods1() {
		return this.siacRSoggettoRelazMods1;
	}

	public void setSiacRSoggettoRelazMods1(List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods1) {
		this.siacRSoggettoRelazMods1 = siacRSoggettoRelazMods1;
	}

	public SiacRSoggettoRelazModFin addSiacRSoggettoRelazMods1(SiacRSoggettoRelazModFin siacRSoggettoRelazMods1) {
		getSiacRSoggettoRelazMods1().add(siacRSoggettoRelazMods1);
		siacRSoggettoRelazMods1.setSiacDRuolo1(this);

		return siacRSoggettoRelazMods1;
	}

	public SiacRSoggettoRelazModFin removeSiacRSoggettoRelazMods1(SiacRSoggettoRelazModFin siacRSoggettoRelazMods1) {
		getSiacRSoggettoRelazMods1().remove(siacRSoggettoRelazMods1);
		siacRSoggettoRelazMods1.setSiacDRuolo1(null);

		return siacRSoggettoRelazMods1;
	}

	public List<SiacRSoggettoRelazModFin> getSiacRSoggettoRelazMods2() {
		return this.siacRSoggettoRelazMods2;
	}

	public void setSiacRSoggettoRelazMods2(List<SiacRSoggettoRelazModFin> siacRSoggettoRelazMods2) {
		this.siacRSoggettoRelazMods2 = siacRSoggettoRelazMods2;
	}

	public SiacRSoggettoRelazModFin addSiacRSoggettoRelazMods2(SiacRSoggettoRelazModFin siacRSoggettoRelazMods2) {
		getSiacRSoggettoRelazMods2().add(siacRSoggettoRelazMods2);
		siacRSoggettoRelazMods2.setSiacDRuolo2(this);

		return siacRSoggettoRelazMods2;
	}

	public SiacRSoggettoRelazModFin removeSiacRSoggettoRelazMods2(SiacRSoggettoRelazModFin siacRSoggettoRelazMods2) {
		getSiacRSoggettoRelazMods2().remove(siacRSoggettoRelazMods2);
		siacRSoggettoRelazMods2.setSiacDRuolo2(null);

		return siacRSoggettoRelazMods2;
	}

	public List<SiacRSoggettoRuoloFin> getSiacRSoggettoRuolos() {
		return this.siacRSoggettoRuolos;
	}

	public void setSiacRSoggettoRuolos(List<SiacRSoggettoRuoloFin> siacRSoggettoRuolos) {
		this.siacRSoggettoRuolos = siacRSoggettoRuolos;
	}

	public SiacRSoggettoRuoloFin addSiacRSoggettoRuolo(SiacRSoggettoRuoloFin siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().add(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacDRuolo(this);

		return siacRSoggettoRuolo;
	}

	public SiacRSoggettoRuoloFin removeSiacRSoggettoRuolo(SiacRSoggettoRuoloFin siacRSoggettoRuolo) {
		getSiacRSoggettoRuolos().remove(siacRSoggettoRuolo);
		siacRSoggettoRuolo.setSiacDRuolo(null);

		return siacRSoggettoRuolo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ruoloId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ruoloId = uid;
	}
}