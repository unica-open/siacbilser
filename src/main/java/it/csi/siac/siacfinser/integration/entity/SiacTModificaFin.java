/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;
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
 * The persistent class for the siac_t_modifica database table.
 * 
 */
@Entity
@Table(name="siac_t_modifica")
public class SiacTModificaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MODIFICA_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_modifica_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODIFICA_MOD_ID_GENERATOR")
	@Column(name="mod_id")
	private Integer modId;

	@Column(name="mod_data")
	private Timestamp modData;

	@Column(name="mod_desc")
	private String modDesc;

	@Column(name="mod_num")
	private Integer modNum;

	//bi-directional many-to-one association to SiacRModificaStatoFin
	@OneToMany(mappedBy="siacTModifica")
	private List<SiacRModificaStatoFin> siacRModificaStatos;

	//bi-directional many-to-one association to SiacDModificaTipoFin
	@ManyToOne
	@JoinColumn(name="mod_tipo_id")
	private SiacDModificaTipoFin siacDModificaTipo;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	public SiacTModificaFin() {
	}

	public Integer getModId() {
		return this.modId;
	}

	public void setModId(Integer modId) {
		this.modId = modId;
	}

	public Timestamp getModData() {
		return this.modData;
	}

	public void setModData(Timestamp modData) {
		this.modData = modData;
	}

	public String getModDesc() {
		return this.modDesc;
	}

	public void setModDesc(String modDesc) {
		this.modDesc = modDesc;
	}

	public Integer getModNum() {
		return this.modNum;
	}

	public void setModNum(Integer modNum) {
		this.modNum = modNum;
	}

	public List<SiacRModificaStatoFin> getSiacRModificaStatos() {
		return this.siacRModificaStatos;
	}

	public void setSiacRModificaStatos(List<SiacRModificaStatoFin> siacRModificaStatos) {
		this.siacRModificaStatos = siacRModificaStatos;
	}

	public SiacRModificaStatoFin addSiacRModificaStato(SiacRModificaStatoFin siacRModificaStato) {
		getSiacRModificaStatos().add(siacRModificaStato);
		siacRModificaStato.setSiacTModifica(this);

		return siacRModificaStato;
	}

	public SiacRModificaStatoFin removeSiacRModificaStato(SiacRModificaStatoFin siacRModificaStato) {
		getSiacRModificaStatos().remove(siacRModificaStato);
		siacRModificaStato.setSiacTModifica(null);

		return siacRModificaStato;
	}

	public SiacDModificaTipoFin getSiacDModificaTipo() {
		return this.siacDModificaTipo;
	}

	public void setSiacDModificaTipo(SiacDModificaTipoFin siacDModificaTipo) {
		this.siacDModificaTipo = siacDModificaTipo;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}
	
	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.modId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.modId = uid;
	}
}