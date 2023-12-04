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
 * The persistent class for the siac_d_cassa_econ_modpag_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_cassa_econ_modpag_tipo")
@NamedQuery(name="SiacDCassaEconModpagTipo.findAll", query="SELECT s FROM SiacDCassaEconModpagTipo s")
public class SiacDCassaEconModpagTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CASSA_ECON_MODPAG_TIPO_CASSAMODPAGTIPOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_CASSA_ECON_MODPAG_TIPO_CASSAMODPAG_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CASSA_ECON_MODPAG_TIPO_CASSAMODPAGTIPOID_GENERATOR")
	@Column(name="cassamodpag_tipo_id")
	private Integer cassamodpagTipoId;

	@Column(name="cassamodpag_tipo_code")
	private String cassamodpagTipoCode;

	@Column(name="cassamodpag_tipo_desc")
	private String cassamodpagTipoDesc;

	//bi-directional many-to-one association to SiacRCassaEconTipoModpagTipo
	@OneToMany(mappedBy="siacDCassaEconModpagTipo")
	private List<SiacRCassaEconTipoModpagTipo> siacRCassaEconTipoModpagTipos;

	//bi-directional many-to-one association to SiacTCassaEconOperaz
	@OneToMany(mappedBy="siacDCassaEconModpagTipo")
	private List<SiacTCassaEconOperaz> siacTCassaEconOperazs;

	//bi-directional many-to-one association to SiacTCassaEconStanz
	@OneToMany(mappedBy="siacDCassaEconModpagTipo")
	private List<SiacTCassaEconStanz> siacTCassaEconStanzs;
	
	//bi-directional many-to-one association to SiacTMovimento
	@OneToMany(mappedBy="siacDCassaEconModpagTipo")
	private List<SiacTMovimento> siacTMovimentos;

	public SiacDCassaEconModpagTipo() {
	}

	public Integer getCassamodpagTipoId() {
		return this.cassamodpagTipoId;
	}

	public void setCassamodpagTipoId(Integer cassamodpagTipoId) {
		this.cassamodpagTipoId = cassamodpagTipoId;
	}

	public String getCassamodpagTipoCode() {
		return this.cassamodpagTipoCode;
	}

	public void setCassamodpagTipoCode(String cassamodpagTipoCode) {
		this.cassamodpagTipoCode = cassamodpagTipoCode;
	}

	public String getCassamodpagTipoDesc() {
		return this.cassamodpagTipoDesc;
	}

	public void setCassamodpagTipoDesc(String cassamodpagTipoDesc) {
		this.cassamodpagTipoDesc = cassamodpagTipoDesc;
	}

	public List<SiacRCassaEconTipoModpagTipo> getSiacRCassaEconTipoModpagTipos() {
		return this.siacRCassaEconTipoModpagTipos;
	}

	public void setSiacRCassaEconTipoModpagTipos(List<SiacRCassaEconTipoModpagTipo> siacRCassaEconTipoModpagTipos) {
		this.siacRCassaEconTipoModpagTipos = siacRCassaEconTipoModpagTipos;
	}

	public SiacRCassaEconTipoModpagTipo addSiacRCassaEconTipoModpagTipo(SiacRCassaEconTipoModpagTipo siacRCassaEconTipoModpagTipo) {
		getSiacRCassaEconTipoModpagTipos().add(siacRCassaEconTipoModpagTipo);
		siacRCassaEconTipoModpagTipo.setSiacDCassaEconModpagTipo(this);

		return siacRCassaEconTipoModpagTipo;
	}

	public SiacRCassaEconTipoModpagTipo removeSiacRCassaEconTipoModpagTipo(SiacRCassaEconTipoModpagTipo siacRCassaEconTipoModpagTipo) {
		getSiacRCassaEconTipoModpagTipos().remove(siacRCassaEconTipoModpagTipo);
		siacRCassaEconTipoModpagTipo.setSiacDCassaEconModpagTipo(null);

		return siacRCassaEconTipoModpagTipo;
	}

	public List<SiacTCassaEconOperaz> getSiacTCassaEconOperazs() {
		return this.siacTCassaEconOperazs;
	}

	public void setSiacTCassaEconOperazs(List<SiacTCassaEconOperaz> siacTCassaEconOperazs) {
		this.siacTCassaEconOperazs = siacTCassaEconOperazs;
	}

	public SiacTCassaEconOperaz addSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		getSiacTCassaEconOperazs().add(siacTCassaEconOperaz);
		siacTCassaEconOperaz.setSiacDCassaEconModpagTipo(this);

		return siacTCassaEconOperaz;
	}

	public SiacTCassaEconOperaz removeSiacTCassaEconOperaz(SiacTCassaEconOperaz siacTCassaEconOperaz) {
		getSiacTCassaEconOperazs().remove(siacTCassaEconOperaz);
		siacTCassaEconOperaz.setSiacDCassaEconModpagTipo(null);

		return siacTCassaEconOperaz;
	}

	public List<SiacTCassaEconStanz> getSiacTCassaEconStanzs() {
		return this.siacTCassaEconStanzs;
	}

	public void setSiacTCassaEconStanzs(List<SiacTCassaEconStanz> siacTCassaEconStanzs) {
		this.siacTCassaEconStanzs = siacTCassaEconStanzs;
	}

	public SiacTCassaEconStanz addSiacTCassaEconStanz(SiacTCassaEconStanz siacTCassaEconStanz) {
		getSiacTCassaEconStanzs().add(siacTCassaEconStanz);
		siacTCassaEconStanz.setSiacDCassaEconModpagTipo(this);

		return siacTCassaEconStanz;
	}

	public SiacTCassaEconStanz removeSiacTCassaEconStanz(SiacTCassaEconStanz siacTCassaEconStanz) {
		getSiacTCassaEconStanzs().remove(siacTCassaEconStanz);
		siacTCassaEconStanz.setSiacDCassaEconModpagTipo(null);

		return siacTCassaEconStanz;
	}
	
	public List<SiacTMovimento> getSiacTMovimentos() {
		return this.siacTMovimentos;
	}

	public void setSiacTMovimentos(List<SiacTMovimento> siacTMovimentos) {
		this.siacTMovimentos = siacTMovimentos;
	}

	public SiacTMovimento addSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().add(siacTMovimento);
		siacTMovimento.setSiacDCassaEconModpagTipo(this);

		return siacTMovimento;
	}

	public SiacTMovimento removeSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().remove(siacTMovimento);
		siacTMovimento.setSiacDCassaEconModpagTipo(null);

		return siacTMovimento;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cassamodpagTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cassamodpagTipoId = uid;
		
	}


}