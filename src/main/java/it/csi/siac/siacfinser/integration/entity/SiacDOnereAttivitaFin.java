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
 * The persistent class for the siac_d_onere_attivita database table.
 * 
 */
@Entity
@Table(name="siac_d_onere_attivita")
public class SiacDOnereAttivitaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="onere_att_id")
	private Integer onereAttId;

	@Column(name="onere_att_code")
	private String onereAttCode;

	@Column(name="onere_att_desc")
	private String onereAttDesc;

	//bi-directional many-to-one association to SiacRDocOnereFin
	@OneToMany(mappedBy="siacDOnereAttivita")
	private List<SiacRDocOnereFin> siacRDocOneres;

	//bi-directional many-to-one association to SiacROnereAttivitaFin
	@OneToMany(mappedBy="siacDOnereAttivita")
	private List<SiacROnereAttivitaFin> siacROnereAttivitas;

	public SiacDOnereAttivitaFin() {
	}

	public Integer getOnereAttId() {
		return this.onereAttId;
	}

	public void setOnereAttId(Integer onereAttId) {
		this.onereAttId = onereAttId;
	}

	public String getOnereAttCode() {
		return this.onereAttCode;
	}

	public void setOnereAttCode(String onereAttCode) {
		this.onereAttCode = onereAttCode;
	}

	public String getOnereAttDesc() {
		return this.onereAttDesc;
	}

	public void setOnereAttDesc(String onereAttDesc) {
		this.onereAttDesc = onereAttDesc;
	}

	public List<SiacRDocOnereFin> getSiacRDocOneres() {
		return this.siacRDocOneres;
	}

	public void setSiacRDocOneres(List<SiacRDocOnereFin> siacRDocOneres) {
		this.siacRDocOneres = siacRDocOneres;
	}

	public SiacRDocOnereFin addSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		getSiacRDocOneres().add(siacRDocOnere);
		siacRDocOnere.setSiacDOnereAttivita(this);

		return siacRDocOnere;
	}

	public SiacRDocOnereFin removeSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		getSiacRDocOneres().remove(siacRDocOnere);
		siacRDocOnere.setSiacDOnereAttivita(null);

		return siacRDocOnere;
	}

	public List<SiacROnereAttivitaFin> getSiacROnereAttivitas() {
		return this.siacROnereAttivitas;
	}

	public void setSiacROnereAttivitas(List<SiacROnereAttivitaFin> siacROnereAttivitas) {
		this.siacROnereAttivitas = siacROnereAttivitas;
	}

	public SiacROnereAttivitaFin addSiacROnereAttivita(SiacROnereAttivitaFin siacROnereAttivita) {
		getSiacROnereAttivitas().add(siacROnereAttivita);
		siacROnereAttivita.setSiacDOnereAttivita(this);

		return siacROnereAttivita;
	}

	public SiacROnereAttivitaFin removeSiacROnereAttivita(SiacROnereAttivitaFin siacROnereAttivita) {
		getSiacROnereAttivitas().remove(siacROnereAttivita);
		siacROnereAttivita.setSiacDOnereAttivita(null);

		return siacROnereAttivita;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.onereAttId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.onereAttId = uid;
	}

}