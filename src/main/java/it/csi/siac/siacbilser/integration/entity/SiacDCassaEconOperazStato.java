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
 * The persistent class for the siac_d_cassa_econ_operaz_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_cassa_econ_operaz_stato")
@NamedQuery(name="SiacDCassaEconOperazStato.findAll", query="SELECT s FROM SiacDCassaEconOperazStato s")
public class SiacDCassaEconOperazStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CASSA_ECON_OPERAZ_STATO_CASSAECONOPSTATOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_CASSA_ECON_OPERAZ_STATO_CASSAECONOP_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CASSA_ECON_OPERAZ_STATO_CASSAECONOPSTATOID_GENERATOR")
	@Column(name="cassaeconop_stato_id")
	private Integer cassaeconopStatoId;

	@Column(name="cassaeconop_stato_code")
	private String cassaeconopStatoCode;

	@Column(name="cassaeconop_stato_desc")
	private String cassaeconopStatoDesc;

	//bi-directional many-to-one association to SiacRCassaEconOperazStato
	@OneToMany(mappedBy="siacDCassaEconOperazStato")
	private List<SiacRCassaEconOperazStato> siacRCassaEconOperazStatos;

	public SiacDCassaEconOperazStato() {
	}

	public Integer getCassaeconopStatoId() {
		return this.cassaeconopStatoId;
	}

	public void setCassaeconopStatoId(Integer cassaeconopStatoId) {
		this.cassaeconopStatoId = cassaeconopStatoId;
	}

	public String getCassaeconopStatoCode() {
		return this.cassaeconopStatoCode;
	}

	public void setCassaeconopStatoCode(String cassaeconopStatoCode) {
		this.cassaeconopStatoCode = cassaeconopStatoCode;
	}

	public String getCassaeconopStatoDesc() {
		return this.cassaeconopStatoDesc;
	}

	public void setCassaeconopStatoDesc(String cassaeconopStatoDesc) {
		this.cassaeconopStatoDesc = cassaeconopStatoDesc;
	}

	public List<SiacRCassaEconOperazStato> getSiacRCassaEconOperazStatos() {
		return this.siacRCassaEconOperazStatos;
	}

	public void setSiacRCassaEconOperazStatos(List<SiacRCassaEconOperazStato> siacRCassaEconOperazStatos) {
		this.siacRCassaEconOperazStatos = siacRCassaEconOperazStatos;
	}

	public SiacRCassaEconOperazStato addSiacRCassaEconOperazStato(SiacRCassaEconOperazStato siacRCassaEconOperazStato) {
		getSiacRCassaEconOperazStatos().add(siacRCassaEconOperazStato);
		siacRCassaEconOperazStato.setSiacDCassaEconOperazStato(this);

		return siacRCassaEconOperazStato;
	}

	public SiacRCassaEconOperazStato removeSiacRCassaEconOperazStato(SiacRCassaEconOperazStato siacRCassaEconOperazStato) {
		getSiacRCassaEconOperazStatos().remove(siacRCassaEconOperazStato);
		siacRCassaEconOperazStato.setSiacDCassaEconOperazStato(null);

		return siacRCassaEconOperazStato;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cassaeconopStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cassaeconopStatoId = uid;
		
	}

}