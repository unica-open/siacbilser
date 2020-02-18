/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_soggetto_classe database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_classe")
public class SiacDSoggettoClasseFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_SOGGETTO_CLASSE_SOGGETTOCLASSEID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SOGGETTO_CLASSE_SOGGETTO_CLASSE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SOGGETTO_CLASSE_SOGGETTOCLASSEID_GENERATOR")
	@Column(name="soggetto_classe_id")
	private Integer soggettoClasseId;

	@Column(name="ambito_id")
	private Integer ambitoId;

	@Column(name="soggetto_classe_code")
	private String soggettoClasseCode;

	@Column(name="soggetto_classe_desc")
	private String soggettoClasseDesc;

	//bi-directional many-to-one association to SiacDSoggettoClasseTipoFin
	@ManyToOne
	@JoinColumn(name="soggetto_classe_tipo_id")
	private SiacDSoggettoClasseTipoFin siacDSoggettoClasseTipo;

	//bi-directional many-to-one association to SiacRSoggettoClasseFin
	@OneToMany(mappedBy="siacDSoggettoClasse")
	private List<SiacRSoggettoClasseFin> siacRSoggettoClasses;

	//bi-directional many-to-one association to SiacRSoggettoClasseModFin
	@OneToMany(mappedBy="siacDSoggettoClasse")
	private List<SiacRSoggettoClasseModFin> siacRSoggettoClasseMods;

	public SiacDSoggettoClasseFin() {
	}

	public Integer getSoggettoClasseId() {
		return this.soggettoClasseId;
	}

	public void setSoggettoClasseId(Integer soggettoClasseId) {
		this.soggettoClasseId = soggettoClasseId;
	}

	public Integer getAmbitoId() {
		return this.ambitoId;
	}

	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	public String getSoggettoClasseCode() {
		return this.soggettoClasseCode;
	}

	public void setSoggettoClasseCode(String soggettoClasseCode) {
		this.soggettoClasseCode = soggettoClasseCode;
	}

	public String getSoggettoClasseDesc() {
		return this.soggettoClasseDesc;
	}

	public void setSoggettoClasseDesc(String soggettoClasseDesc) {
		this.soggettoClasseDesc = soggettoClasseDesc;
	}

	public SiacDSoggettoClasseTipoFin getSiacDSoggettoClasseTipo() {
		return this.siacDSoggettoClasseTipo;
	}

	public void setSiacDSoggettoClasseTipo(SiacDSoggettoClasseTipoFin siacDSoggettoClasseTipo) {
		this.siacDSoggettoClasseTipo = siacDSoggettoClasseTipo;
	}

	public List<SiacRSoggettoClasseFin> getSiacRSoggettoClasses() {
		return this.siacRSoggettoClasses;
	}

	public void setSiacRSoggettoClasses(List<SiacRSoggettoClasseFin> siacRSoggettoClasses) {
		this.siacRSoggettoClasses = siacRSoggettoClasses;
	}

	public SiacRSoggettoClasseFin addSiacRSoggettoClass(SiacRSoggettoClasseFin siacRSoggettoClass) {
		getSiacRSoggettoClasses().add(siacRSoggettoClass);
		siacRSoggettoClass.setSiacDSoggettoClasse(this);

		return siacRSoggettoClass;
	}

	public SiacRSoggettoClasseFin removeSiacRSoggettoClass(SiacRSoggettoClasseFin siacRSoggettoClass) {
		getSiacRSoggettoClasses().remove(siacRSoggettoClass);
		siacRSoggettoClass.setSiacDSoggettoClasse(null);

		return siacRSoggettoClass;
	}

	public List<SiacRSoggettoClasseModFin> getSiacRSoggettoClasseMods() {
		return this.siacRSoggettoClasseMods;
	}

	public void setSiacRSoggettoClasseMods(List<SiacRSoggettoClasseModFin> siacRSoggettoClasseMods) {
		this.siacRSoggettoClasseMods = siacRSoggettoClasseMods;
	}

	public SiacRSoggettoClasseModFin addSiacRSoggettoClasseMod(SiacRSoggettoClasseModFin siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().add(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacDSoggettoClasse(this);

		return siacRSoggettoClasseMod;
	}

	public SiacRSoggettoClasseModFin removeSiacRSoggettoClasseMod(SiacRSoggettoClasseModFin siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().remove(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacDSoggettoClasse(null);

		return siacRSoggettoClasseMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoClasseId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoClasseId = uid;
	}
}