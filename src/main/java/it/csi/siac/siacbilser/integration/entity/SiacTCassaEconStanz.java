/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_t_cassa_econ_stanz database table.
 * 
 */
@Entity
@Table(name="siac_t_cassa_econ_stanz")
@NamedQuery(name="SiacTCassaEconStanz.findAll", query="SELECT s FROM SiacTCassaEconStanz s")
public class SiacTCassaEconStanz extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CASSA_ECON_STANZ_CASSAECONSTID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_CASSA_ECON_STANZ_CASSAECONST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CASSA_ECON_STANZ_CASSAECONSTID_GENERATOR")
	@Column(name="cassaeconst_id")
	private Integer cassaeconstId;

	@Column(name="cassaecon_importo")
	private BigDecimal cassaeconImporto;

	//bi-directional many-to-one association to SiacDCassaEconModpagTipo
	@ManyToOne
	@JoinColumn(name="cassamodpag_tipo_id")
	private SiacDCassaEconModpagTipo siacDCassaEconModpagTipo;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	public SiacTCassaEconStanz() {
	}

	public Integer getCassaeconstId() {
		return this.cassaeconstId;
	}

	public void setCassaeconstId(Integer cassaeconstId) {
		this.cassaeconstId = cassaeconstId;
	}

	public BigDecimal getCassaeconImporto() {
		return this.cassaeconImporto;
	}

	public void setCassaeconImporto(BigDecimal cassaeconImporto) {
		this.cassaeconImporto = cassaeconImporto;
	}

	public SiacDCassaEconModpagTipo getSiacDCassaEconModpagTipo() {
		return this.siacDCassaEconModpagTipo;
	}

	public void setSiacDCassaEconModpagTipo(SiacDCassaEconModpagTipo siacDCassaEconModpagTipo) {
		this.siacDCassaEconModpagTipo = siacDCassaEconModpagTipo;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return this.siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cassaeconstId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cassaeconstId = uid;
		
	}

}