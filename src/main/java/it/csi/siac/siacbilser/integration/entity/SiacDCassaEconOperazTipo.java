/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_d_cassa_econ_operaz_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_cassa_econ_operaz_tipo")
@NamedQuery(name="SiacDCassaEconOperazTipo.findAll", query="SELECT s FROM SiacDCassaEconOperazTipo s")
public class SiacDCassaEconOperazTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CASSA_ECON_OPERAZ_TIPO_CASSAECONOPTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CASSA_ECON_OPERAZ_TIPO_CASSAECONOP_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CASSA_ECON_OPERAZ_TIPO_CASSAECONOPTIPOID_GENERATOR")
	@Column(name="cassaeconop_tipo_id")
	private Integer cassaeconopTipoId;

	@Column(name="cassaeconop_tipo_code")
	private String cassaeconopTipoCode;

	@Column(name="cassaeconop_tipo_desc")
	private String cassaeconopTipoDesc;

	@Column(name="cassaeconop_tipo_entrataspesa")
	private String cassaeconopTipoEntrataspesa;

	private String ingiornale;

	private String inrendiconto;

	//bi-directional many-to-one association to SiacRCassaEconOperazTipo
	@OneToMany(mappedBy="siacDCassaEconOperazTipo")
	private List<SiacRCassaEconOperazTipo> siacRCassaEconOperazTipos;

	//bi-directional many-to-one association to SiacRCassaEconOperazTipoCassa
	@OneToMany(mappedBy="siacDCassaEconOperazTipo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCassaEconOperazTipoCassa> siacRCassaEconOperazTipoCassas;

	public SiacDCassaEconOperazTipo() {
	}

	public Integer getCassaeconopTipoId() {
		return this.cassaeconopTipoId;
	}

	public void setCassaeconopTipoId(Integer cassaeconopTipoId) {
		this.cassaeconopTipoId = cassaeconopTipoId;
	}

	public String getCassaeconopTipoCode() {
		return this.cassaeconopTipoCode;
	}

	public void setCassaeconopTipoCode(String cassaeconopTipoCode) {
		this.cassaeconopTipoCode = cassaeconopTipoCode;
	}

	public String getCassaeconopTipoDesc() {
		return this.cassaeconopTipoDesc;
	}

	public void setCassaeconopTipoDesc(String cassaeconopTipoDesc) {
		this.cassaeconopTipoDesc = cassaeconopTipoDesc;
	}

	public String getCassaeconopTipoEntrataspesa() {
		return this.cassaeconopTipoEntrataspesa;
	}

	public void setCassaeconopTipoEntrataspesa(String cassaeconopTipoEntrataspesa) {
		this.cassaeconopTipoEntrataspesa = cassaeconopTipoEntrataspesa;
	}

	public String getIngiornale() {
		return this.ingiornale;
	}

	public void setIngiornale(String ingiornale) {
		this.ingiornale = ingiornale;
	}

	public String getInrendiconto() {
		return this.inrendiconto;
	}

	public void setInrendiconto(String inrendiconto) {
		this.inrendiconto = inrendiconto;
	}

	public List<SiacRCassaEconOperazTipo> getSiacRCassaEconOperazTipos() {
		return this.siacRCassaEconOperazTipos;
	}

	public void setSiacRCassaEconOperazTipos(List<SiacRCassaEconOperazTipo> siacRCassaEconOperazTipos) {
		this.siacRCassaEconOperazTipos = siacRCassaEconOperazTipos;
	}

	public SiacRCassaEconOperazTipo addSiacRCassaEconOperazTipo(SiacRCassaEconOperazTipo siacRCassaEconOperazTipo) {
		getSiacRCassaEconOperazTipos().add(siacRCassaEconOperazTipo);
		siacRCassaEconOperazTipo.setSiacDCassaEconOperazTipo(this);

		return siacRCassaEconOperazTipo;
	}

	public SiacRCassaEconOperazTipo removeSiacRCassaEconOperazTipo(SiacRCassaEconOperazTipo siacRCassaEconOperazTipo) {
		getSiacRCassaEconOperazTipos().remove(siacRCassaEconOperazTipo);
		siacRCassaEconOperazTipo.setSiacDCassaEconOperazTipo(null);

		return siacRCassaEconOperazTipo;
	}

	public List<SiacRCassaEconOperazTipoCassa> getSiacRCassaEconOperazTipoCassas() {
		return this.siacRCassaEconOperazTipoCassas;
	}

	public void setSiacRCassaEconOperazTipoCassas(List<SiacRCassaEconOperazTipoCassa> siacRCassaEconOperazTipoCassas) {
		this.siacRCassaEconOperazTipoCassas = siacRCassaEconOperazTipoCassas;
	}

	public SiacRCassaEconOperazTipoCassa addSiacRCassaEconOperazTipoCassa(SiacRCassaEconOperazTipoCassa siacRCassaEconOperazTipoCassa) {
		getSiacRCassaEconOperazTipoCassas().add(siacRCassaEconOperazTipoCassa);
		siacRCassaEconOperazTipoCassa.setSiacDCassaEconOperazTipo(this);

		return siacRCassaEconOperazTipoCassa;
	}

	public SiacRCassaEconOperazTipoCassa removeSiacRCassaEconOperazTipoCassa(SiacRCassaEconOperazTipoCassa siacRCassaEconOperazTipoCassa) {
		getSiacRCassaEconOperazTipoCassas().remove(siacRCassaEconOperazTipoCassa);
		siacRCassaEconOperazTipoCassa.setSiacDCassaEconOperazTipo(null);

		return siacRCassaEconOperazTipoCassa;
	}

	@Override
	public Integer getUid() {
		return this.cassaeconopTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cassaeconopTipoId = uid;
	}

}