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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_cassa_econ_tipo_modpag_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_cassa_econ_tipo_modpag_tipo")
@NamedQuery(name="SiacRCassaEconTipoModpagTipo.findAll", query="SELECT s FROM SiacRCassaEconTipoModpagTipo s")
public class SiacRCassaEconTipoModpagTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CASSA_ECON_TIPO_MODPAG_TIPO_CASSATMTID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_CASSA_ECON_TIPO_MODPAG_TIPO_CASSATMT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CASSA_ECON_TIPO_MODPAG_TIPO_CASSATMTID_GENERATOR")
	@Column(name="cassatmt_id")
	private Integer cassatmtId;

	//bi-directional many-to-one association to SiacDCassaEconModpagTipo
	@ManyToOne
	@JoinColumn(name="cassamodpag_tipo_id")
	private SiacDCassaEconModpagTipo siacDCassaEconModpagTipo;

	//bi-directional many-to-one association to SiacDCassaEconTipo
	@ManyToOne
	@JoinColumn(name="cassaecon_tipo_id")
	private SiacDCassaEconTipo siacDCassaEconTipo;

	public SiacRCassaEconTipoModpagTipo() {
	}

	public Integer getCassatmtId() {
		return this.cassatmtId;
	}

	public void setCassatmtId(Integer cassatmtId) {
		this.cassatmtId = cassatmtId;
	}

	public SiacDCassaEconModpagTipo getSiacDCassaEconModpagTipo() {
		return this.siacDCassaEconModpagTipo;
	}

	public void setSiacDCassaEconModpagTipo(SiacDCassaEconModpagTipo siacDCassaEconModpagTipo) {
		this.siacDCassaEconModpagTipo = siacDCassaEconModpagTipo;
	}

	public SiacDCassaEconTipo getSiacDCassaEconTipo() {
		return this.siacDCassaEconTipo;
	}

	public void setSiacDCassaEconTipo(SiacDCassaEconTipo siacDCassaEconTipo) {
		this.siacDCassaEconTipo = siacDCassaEconTipo;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cassatmtId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cassatmtId = uid;
		
	}


}