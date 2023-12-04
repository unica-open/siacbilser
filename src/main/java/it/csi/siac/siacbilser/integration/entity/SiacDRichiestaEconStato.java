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
 * The persistent class for the siac_d_richiesta_econ_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_richiesta_econ_stato")
@NamedQuery(name="SiacDRichiestaEconStato.findAll", query="SELECT s FROM SiacDRichiestaEconStato s")
public class SiacDRichiestaEconStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_RICHIESTA_ECON_STATO_RICECONSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_RICHIESTA_ECON_STATO_RICECON_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RICHIESTA_ECON_STATO_RICECONSTATOID_GENERATOR")
	@Column(name="ricecon_stato_id")
	private Integer riceconStatoId;

	@Column(name="ricecon_stato_code")
	private String riceconStatoCode;

	@Column(name="ricecon_stato_desc")
	private String riceconStatoDesc;

	//bi-directional many-to-one association to SiacRRichiestaEconStato
	@OneToMany(mappedBy="siacDRichiestaEconStato")
	private List<SiacRRichiestaEconStato> siacRRichiestaEconStatos;

	public SiacDRichiestaEconStato() {
	}

	public Integer getRiceconStatoId() {
		return this.riceconStatoId;
	}

	public void setRiceconStatoId(Integer riceconStatoId) {
		this.riceconStatoId = riceconStatoId;
	}

	public String getRiceconStatoCode() {
		return this.riceconStatoCode;
	}

	public void setRiceconStatoCode(String riceconStatoCode) {
		this.riceconStatoCode = riceconStatoCode;
	}

	public String getRiceconStatoDesc() {
		return this.riceconStatoDesc;
	}

	public void setRiceconStatoDesc(String riceconStatoDesc) {
		this.riceconStatoDesc = riceconStatoDesc;
	}

	public List<SiacRRichiestaEconStato> getSiacRRichiestaEconStatos() {
		return this.siacRRichiestaEconStatos;
	}

	public void setSiacRRichiestaEconStatos(List<SiacRRichiestaEconStato> siacRRichiestaEconStatos) {
		this.siacRRichiestaEconStatos = siacRRichiestaEconStatos;
	}

	public SiacRRichiestaEconStato addSiacRRichiestaEconStato(SiacRRichiestaEconStato siacRRichiestaEconStato) {
		getSiacRRichiestaEconStatos().add(siacRRichiestaEconStato);
		siacRRichiestaEconStato.setSiacDRichiestaEconStato(this);

		return siacRRichiestaEconStato;
	}

	public SiacRRichiestaEconStato removeSiacRRichiestaEconStato(SiacRRichiestaEconStato siacRRichiestaEconStato) {
		getSiacRRichiestaEconStatos().remove(siacRRichiestaEconStato);
		siacRRichiestaEconStato.setSiacDRichiestaEconStato(null);

		return siacRRichiestaEconStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.riceconStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.riceconStatoId = uid;
		
	}
}