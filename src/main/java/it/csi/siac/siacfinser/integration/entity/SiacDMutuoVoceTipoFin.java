/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_mutuo_voce_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_voce_tipo")
public class SiacDMutuoVoceTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_VOCE_TIPO_MUTUO_VOCE_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_mutuo_voce_tipo_mut_voce_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_VOCE_TIPO_MUTUO_VOCE_TIPO_ID_GENERATOR")
	@Column(name="mut_voce_tipo_id")
	private Integer mutVoceTipoId;

	@Column(name="mut_voce_tipo_code")
	private String mutVoceTipoCode;

	@Column(name="mut_voce_tipo_desc")
	private String mutVoceTipoDesc;

	//bi-directional many-to-one association to SiacTMutuoVoceFin
	@OneToMany(mappedBy="siacDMutuoVoceTipo", cascade = {CascadeType.ALL})
	private List<SiacTMutuoVoceFin> siacTMutuoVoces;

	public SiacDMutuoVoceTipoFin() {
	}

	public Integer getMutVoceTipoId() {
		return this.mutVoceTipoId;
	}

	public void setMutVoceTipoId(Integer mutVoceTipoId) {
		this.mutVoceTipoId = mutVoceTipoId;
	}

	public String getMutVoceTipoCode() {
		return this.mutVoceTipoCode;
	}

	public void setMutVoceTipoCode(String mutVoceTipoCode) {
		this.mutVoceTipoCode = mutVoceTipoCode;
	}

	public String getMutVoceTipoDesc() {
		return this.mutVoceTipoDesc;
	}

	public void setMutVoceTipoDesc(String mutVoceTipoDesc) {
		this.mutVoceTipoDesc = mutVoceTipoDesc;
	}

	public List<SiacTMutuoVoceFin> getSiacTMutuoVoces() {
		return this.siacTMutuoVoces;
	}

	public void setSiacTMutuoVoces(List<SiacTMutuoVoceFin> siacTMutuoVoces) {
		this.siacTMutuoVoces = siacTMutuoVoces;
	}

	public SiacTMutuoVoceFin addSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		getSiacTMutuoVoces().add(siacTMutuoVoce);
		siacTMutuoVoce.setSiacDMutuoVoceTipo(this);

		return siacTMutuoVoce;
	}

	public SiacTMutuoVoceFin removeSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		getSiacTMutuoVoces().remove(siacTMutuoVoce);
		siacTMutuoVoce.setSiacDMutuoVoceTipo(null);

		return siacTMutuoVoce;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutVoceTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutVoceTipoId = uid;
	}
}