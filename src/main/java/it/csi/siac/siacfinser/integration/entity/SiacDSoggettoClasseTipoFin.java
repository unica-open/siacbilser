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
 * The persistent class for the siac_d_soggetto_classe_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_classe_tipo")
public class SiacDSoggettoClasseTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="soggetto_classe_tipo_id")
	private Integer soggettoClasseTipoId;

	@Column(name="ambito_id")
	private Integer ambitoId;

	@Column(name="soggetto_classe_tipo_code")
	private String soggettoClasseTipoCode;

	@Column(name="soggetto_classe_tipo_desc")
	private String soggettoClasseTipoDesc;

	//bi-directional many-to-one association to SiacDSoggettoClasseFin
	@OneToMany(mappedBy="siacDSoggettoClasseTipo")
	private List<SiacDSoggettoClasseFin> siacDSoggettoClasses;

	public SiacDSoggettoClasseTipoFin() {
	}

	public Integer getSoggettoClasseTipoId() {
		return this.soggettoClasseTipoId;
	}

	public void setSoggettoClasseTipoId(Integer soggettoClasseTipoId) {
		this.soggettoClasseTipoId = soggettoClasseTipoId;
	}

	public Integer getAmbitoId() {
		return this.ambitoId;
	}

	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	public String getSoggettoClasseTipoCode() {
		return this.soggettoClasseTipoCode;
	}

	public void setSoggettoClasseTipoCode(String soggettoClasseTipoCode) {
		this.soggettoClasseTipoCode = soggettoClasseTipoCode;
	}

	public String getSoggettoClasseTipoDesc() {
		return this.soggettoClasseTipoDesc;
	}

	public void setSoggettoClasseTipoDesc(String soggettoClasseTipoDesc) {
		this.soggettoClasseTipoDesc = soggettoClasseTipoDesc;
	}

	public List<SiacDSoggettoClasseFin> getSiacDSoggettoClasses() {
		return this.siacDSoggettoClasses;
	}

	public void setSiacDSoggettoClasses(List<SiacDSoggettoClasseFin> siacDSoggettoClasses) {
		this.siacDSoggettoClasses = siacDSoggettoClasses;
	}

	public SiacDSoggettoClasseFin addSiacDSoggettoClass(SiacDSoggettoClasseFin siacDSoggettoClass) {
		getSiacDSoggettoClasses().add(siacDSoggettoClass);
		siacDSoggettoClass.setSiacDSoggettoClasseTipo(this);

		return siacDSoggettoClass;
	}

	public SiacDSoggettoClasseFin removeSiacDSoggettoClass(SiacDSoggettoClasseFin siacDSoggettoClass) {
		getSiacDSoggettoClasses().remove(siacDSoggettoClass);
		siacDSoggettoClass.setSiacDSoggettoClasseTipo(null);

		return siacDSoggettoClass;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoClasseTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoClasseTipoId = uid;
	}
}