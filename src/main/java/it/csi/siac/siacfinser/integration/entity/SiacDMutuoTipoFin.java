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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_mutuo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_tipo")
public class SiacDMutuoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_TIPO_MUTUO_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_mutuo_tipo_mut_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_TIPO_MUTUO_TIPO_ID_GENERATOR")
	@Column(name="mut_tipo_id")
	private Integer mutTipoId;

	@Column(name="mut_tipo_code")
	private String mutTipoCode;

	@Column(name="mut_tipo_desc")
	private String mutTipoDesc;

	//bi-directional many-to-one association to SiacTMutuoFin
	@OneToMany(mappedBy="siacDMutuoTipo")
	private List<SiacTMutuoFin> siacTMutuos;

	public SiacDMutuoTipoFin() {
	}

	public Integer getMutTipoId() {
		return this.mutTipoId;
	}

	public void setMutTipoId(Integer mutTipoId) {
		this.mutTipoId = mutTipoId;
	}

	public String getMutTipoCode() {
		return this.mutTipoCode;
	}

	public void setMutTipoCode(String mutTipoCode) {
		this.mutTipoCode = mutTipoCode;
	}

	public String getMutTipoDesc() {
		return this.mutTipoDesc;
	}

	public void setMutTipoDesc(String mutTipoDesc) {
		this.mutTipoDesc = mutTipoDesc;
	}

	public List<SiacTMutuoFin> getSiacTMutuos() {
		return this.siacTMutuos;
	}

	public void setSiacTMutuos(List<SiacTMutuoFin> siacTMutuos) {
		this.siacTMutuos = siacTMutuos;
	}

	public SiacTMutuoFin addSiacTMutuo(SiacTMutuoFin siacTMutuo) {
		getSiacTMutuos().add(siacTMutuo);
		siacTMutuo.setSiacDMutuoTipo(this);

		return siacTMutuo;
	}

	public SiacTMutuoFin removeSiacTMutuo(SiacTMutuoFin siacTMutuo) {
		getSiacTMutuos().remove(siacTMutuo);
		siacTMutuo.setSiacDMutuoTipo(null);

		return siacTMutuo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutTipoId = uid;
	}

}