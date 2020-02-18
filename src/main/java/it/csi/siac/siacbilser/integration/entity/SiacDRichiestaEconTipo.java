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
 * The persistent class for the siac_d_richiesta_econ_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_richiesta_econ_tipo")
@NamedQuery(name="SiacDRichiestaEconTipo.findAll", query="SELECT s FROM SiacDRichiestaEconTipo s")
public class SiacDRichiestaEconTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_RICHIESTA_ECON_TIPO_RICECONTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_RICHIESTA_ECON_TIPO_RICECON_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RICHIESTA_ECON_TIPO_RICECONTIPOID_GENERATOR")
	@Column(name="ricecon_tipo_id")
	private Integer riceconTipoId;

	@Column(name="ricecon_rendiconto")
	private String riceconRendiconto;

	@Column(name="ricecon_tipo_code")
	private String riceconTipoCode;

	@Column(name="ricecon_tipo_desc")
	private String riceconTipoDesc;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@OneToMany(mappedBy="siacDRichiestaEconTipo")
	private List<SiacTRichiestaEcon> siacTRichiestaEcons;

	public SiacDRichiestaEconTipo() {
	}

	public Integer getRiceconTipoId() {
		return this.riceconTipoId;
	}

	public void setRiceconTipoId(Integer riceconTipoId) {
		this.riceconTipoId = riceconTipoId;
	}

	public String getRiceconRendiconto() {
		return this.riceconRendiconto;
	}

	public void setRiceconRendiconto(String riceconRendiconto) {
		this.riceconRendiconto = riceconRendiconto;
	}

	public String getRiceconTipoCode() {
		return this.riceconTipoCode;
	}

	public void setRiceconTipoCode(String riceconTipoCode) {
		this.riceconTipoCode = riceconTipoCode;
	}

	public String getRiceconTipoDesc() {
		return this.riceconTipoDesc;
	}

	public void setRiceconTipoDesc(String riceconTipoDesc) {
		this.riceconTipoDesc = riceconTipoDesc;
	}

	public List<SiacTRichiestaEcon> getSiacTRichiestaEcons() {
		return this.siacTRichiestaEcons;
	}

	public void setSiacTRichiestaEcons(List<SiacTRichiestaEcon> siacTRichiestaEcons) {
		this.siacTRichiestaEcons = siacTRichiestaEcons;
	}

	public SiacTRichiestaEcon addSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		getSiacTRichiestaEcons().add(siacTRichiestaEcon);
		siacTRichiestaEcon.setSiacDRichiestaEconTipo(this);

		return siacTRichiestaEcon;
	}

	public SiacTRichiestaEcon removeSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		getSiacTRichiestaEcons().remove(siacTRichiestaEcon);
		siacTRichiestaEcon.setSiacDRichiestaEconTipo(null);

		return siacTRichiestaEcon;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.riceconTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.riceconTipoId = uid;
		
	}
}