/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_relaz_entita database table.
 * 
 */
@Entity
@Table(name="siac_d_relaz_entita")
@NamedQuery(name="SiacDRelazEntita.findAll", query="SELECT s FROM SiacDRelazEntita s")
public class SiacDRelazEntita extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_RELAZ_ENTITA_RELAZENTITAID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_RELAZ_ENTITA_RELAZ_ENTITA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RELAZ_ENTITA_RELAZENTITAID_GENERATOR")
	@Column(name="relaz_entita_id")
	private Integer relazEntitaId;

	@Column(name="relaz_entita_code")
	private String relazEntitaCode;

	@Column(name="relaz_entita_desc")
	private String relazEntitaDesc;

	//bi-directional many-to-one association to SiacDRelazTipo
	@OneToMany(mappedBy="siacDRelazEntita")
	private List<SiacDRelazTipo> siacDRelazTipos;

	public SiacDRelazEntita() {
	}

	public Integer getRelazEntitaId() {
		return this.relazEntitaId;
	}

	public void setRelazEntitaId(Integer relazEntitaId) {
		this.relazEntitaId = relazEntitaId;
	}

	public String getRelazEntitaCode() {
		return this.relazEntitaCode;
	}

	public void setRelazEntitaCode(String relazEntitaCode) {
		this.relazEntitaCode = relazEntitaCode;
	}

	public String getRelazEntitaDesc() {
		return this.relazEntitaDesc;
	}

	public void setRelazEntitaDesc(String relazEntitaDesc) {
		this.relazEntitaDesc = relazEntitaDesc;
	}

	public List<SiacDRelazTipo> getSiacDRelazTipos() {
		return this.siacDRelazTipos;
	}

	public void setSiacDRelazTipos(List<SiacDRelazTipo> siacDRelazTipos) {
		this.siacDRelazTipos = siacDRelazTipos;
	}

	public SiacDRelazTipo addSiacDRelazTipo(SiacDRelazTipo siacDRelazTipo) {
		getSiacDRelazTipos().add(siacDRelazTipo);
		siacDRelazTipo.setSiacDRelazEntita(this);

		return siacDRelazTipo;
	}

	public SiacDRelazTipo removeSiacDRelazTipo(SiacDRelazTipo siacDRelazTipo) {
		getSiacDRelazTipos().remove(siacDRelazTipo);
		siacDRelazTipo.setSiacDRelazEntita(null);

		return siacDRelazTipo;
	}

	@Override
	public Integer getUid() {
		return relazEntitaId;
	}

	@Override
	public void setUid(Integer uid) {
		relazEntitaId = uid;
	}

}