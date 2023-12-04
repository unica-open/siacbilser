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
 * The persistent class for the siac_d_cassa_econ_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_cassa_econ_tipo")
@NamedQuery(name="SiacDCassaEconTipo.findAll", query="SELECT s FROM SiacDCassaEconTipo s")
public class SiacDCassaEconTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CASSA_ECON_TIPO_CASSAECONTIPOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_CASSA_ECON_TIPO_CASSAECON_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CASSA_ECON_TIPO_CASSAECONTIPOID_GENERATOR")
	@Column(name="cassaecon_tipo_id")
	private Integer cassaeconTipoId;

	@Column(name="cassaecon_tipo_code")
	private String cassaeconTipoCode;

	@Column(name="cassaecon_tipo_desc")
	private String cassaeconTipoDesc;

	//bi-directional many-to-one association to SiacRCassaEconTipoModpagTipo
	@OneToMany(mappedBy="siacDCassaEconTipo")
	private List<SiacRCassaEconTipoModpagTipo> siacRCassaEconTipoModpagTipos;

	//bi-directional many-to-one association to SiacTCassaEcon
	@OneToMany(mappedBy="siacDCassaEconTipo")
	private List<SiacTCassaEcon> siacTCassaEcons;

	public SiacDCassaEconTipo() {
	}

	public Integer getCassaeconTipoId() {
		return this.cassaeconTipoId;
	}

	public void setCassaeconTipoId(Integer cassaeconTipoId) {
		this.cassaeconTipoId = cassaeconTipoId;
	}

	public String getCassaeconTipoCode() {
		return this.cassaeconTipoCode;
	}

	public void setCassaeconTipoCode(String cassaeconTipoCode) {
		this.cassaeconTipoCode = cassaeconTipoCode;
	}

	public String getCassaeconTipoDesc() {
		return this.cassaeconTipoDesc;
	}

	public void setCassaeconTipoDesc(String cassaeconTipoDesc) {
		this.cassaeconTipoDesc = cassaeconTipoDesc;
	}

	public List<SiacRCassaEconTipoModpagTipo> getSiacRCassaEconTipoModpagTipos() {
		return this.siacRCassaEconTipoModpagTipos;
	}

	public void setSiacRCassaEconTipoModpagTipos(List<SiacRCassaEconTipoModpagTipo> siacRCassaEconTipoModpagTipos) {
		this.siacRCassaEconTipoModpagTipos = siacRCassaEconTipoModpagTipos;
	}

	public SiacRCassaEconTipoModpagTipo addSiacRCassaEconTipoModpagTipo(SiacRCassaEconTipoModpagTipo siacRCassaEconTipoModpagTipo) {
		getSiacRCassaEconTipoModpagTipos().add(siacRCassaEconTipoModpagTipo);
		siacRCassaEconTipoModpagTipo.setSiacDCassaEconTipo(this);

		return siacRCassaEconTipoModpagTipo;
	}

	public SiacRCassaEconTipoModpagTipo removeSiacRCassaEconTipoModpagTipo(SiacRCassaEconTipoModpagTipo siacRCassaEconTipoModpagTipo) {
		getSiacRCassaEconTipoModpagTipos().remove(siacRCassaEconTipoModpagTipo);
		siacRCassaEconTipoModpagTipo.setSiacDCassaEconTipo(null);

		return siacRCassaEconTipoModpagTipo;
	}

	public List<SiacTCassaEcon> getSiacTCassaEcons() {
		return this.siacTCassaEcons;
	}

	public void setSiacTCassaEcons(List<SiacTCassaEcon> siacTCassaEcons) {
		this.siacTCassaEcons = siacTCassaEcons;
	}

	public SiacTCassaEcon addSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		getSiacTCassaEcons().add(siacTCassaEcon);
		siacTCassaEcon.setSiacDCassaEconTipo(this);

		return siacTCassaEcon;
	}

	public SiacTCassaEcon removeSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		getSiacTCassaEcons().remove(siacTCassaEcon);
		siacTCassaEcon.setSiacDCassaEconTipo(null);

		return siacTCassaEcon;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cassaeconTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cassaeconTipoId = uid;
		
	}

}