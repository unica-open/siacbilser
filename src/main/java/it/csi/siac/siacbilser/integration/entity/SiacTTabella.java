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
 * The persistent class for the siac_t_tabella database table.
 * 
 */
@Entity
@Table(name="siac_t_tabella")
@NamedQuery(name="SiacTTabella.findAll", query="SELECT s FROM SiacTTabella s")
public class SiacTTabella extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_TABELLA_TABELLAID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_TABELLA_TABELLA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_TABELLA_TABELLAID_GENERATOR")
	@Column(name="tabella_id")
	private Integer tabellaId;

	@Column(name="tabella_nome")
	private String tabellaNome;

	//bi-directional many-to-one association to SiacRModuloTabella
	@OneToMany(mappedBy="siacTTabella")
	private List<SiacRModuloTabella> siacRModuloTabellas;

	public SiacTTabella() {
	}

	public Integer getTabellaId() {
		return this.tabellaId;
	}

	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	public String getTabellaNome() {
		return this.tabellaNome;
	}

	public void setTabellaNome(String tabellaNome) {
		this.tabellaNome = tabellaNome;
	}

	public List<SiacRModuloTabella> getSiacRModuloTabellas() {
		return this.siacRModuloTabellas;
	}

	public void setSiacRModuloTabellas(List<SiacRModuloTabella> siacRModuloTabellas) {
		this.siacRModuloTabellas = siacRModuloTabellas;
	}

	public SiacRModuloTabella addSiacRModuloTabella(SiacRModuloTabella siacRModuloTabella) {
		getSiacRModuloTabellas().add(siacRModuloTabella);
		siacRModuloTabella.setSiacTTabella(this);

		return siacRModuloTabella;
	}

	public SiacRModuloTabella removeSiacRModuloTabella(SiacRModuloTabella siacRModuloTabella) {
		getSiacRModuloTabellas().remove(siacRModuloTabella);
		siacRModuloTabella.setSiacTTabella(null);

		return siacRModuloTabella;
	}

	@Override
	public Integer getUid() {
		return tabellaId;
	}

	@Override
	public void setUid(Integer uid) {
		tabellaId = uid;
	}

}