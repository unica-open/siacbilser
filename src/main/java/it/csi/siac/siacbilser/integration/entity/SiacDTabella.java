/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_tabella database table.
 * 
 */
@Entity
@Table(name="siac_d_tabella")
@NamedQuery(name="SiacDTabella.findAll", query="SELECT s FROM SiacDTabella s")
public class SiacDTabella extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_TABELLA_TABID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_TABELLA_TAB_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_TABELLA_TABID_GENERATOR")
	@Column(name="tab_id")
	private Integer tabId;

	@Column(name="tab_campo_pk_nome")
	private String tabCampoPkNome;

	@Column(name="tab_nome")
	private String tabNome;

	@Column(name="tab_oid")
	private Integer tabOid;

//	//bi-directional many-to-one association to SiacREventoTipoTabella
//	@OneToMany(mappedBy="siacDTabella")
//	private List<SiacREventoTipoTabella> siacREventoTipoTabellas;

	public SiacDTabella() {
	}

	public Integer getTabId() {
		return this.tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	public String getTabCampoPkNome() {
		return this.tabCampoPkNome;
	}

	public void setTabCampoPkNome(String tabCampoPkNome) {
		this.tabCampoPkNome = tabCampoPkNome;
	}

	public String getTabNome() {
		return this.tabNome;
	}

	public void setTabNome(String tabNome) {
		this.tabNome = tabNome;
	}

	public Integer getTabOid() {
		return this.tabOid;
	}

	public void setTabOid(Integer tabOid) {
		this.tabOid = tabOid;
	}

//	public List<SiacREventoTipoTabella> getSiacREventoTipoTabellas() {
//		return this.siacREventoTipoTabellas;
//	}
//
//	public void setSiacREventoTipoTabellas(List<SiacREventoTipoTabella> siacREventoTipoTabellas) {
//		this.siacREventoTipoTabellas = siacREventoTipoTabellas;
//	}
//
//	public SiacREventoTipoTabella addSiacREventoTipoTabella(SiacREventoTipoTabella siacREventoTipoTabella) {
//		getSiacREventoTipoTabellas().add(siacREventoTipoTabella);
//		siacREventoTipoTabella.setSiacDTabella(this);
//
//		return siacREventoTipoTabella;
//	}
//
//	public SiacREventoTipoTabella removeSiacREventoTipoTabella(SiacREventoTipoTabella siacREventoTipoTabella) {
//		getSiacREventoTipoTabellas().remove(siacREventoTipoTabella);
//		siacREventoTipoTabella.setSiacDTabella(null);
//
//		return siacREventoTipoTabella;
//	}

	@Override
	public Integer getUid() {
		return tabId;
	}

	@Override
	public void setUid(Integer uid) {
		this.tabId = uid;
	}

}