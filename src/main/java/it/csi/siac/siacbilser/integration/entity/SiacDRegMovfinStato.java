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
 * The persistent class for the siac_d_reg_movfin_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_reg_movfin_stato")
@NamedQuery(name="SiacDRegMovfinStato.findAll", query="SELECT s FROM SiacDRegMovfinStato s")
public class SiacDRegMovfinStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_REG_MOVFIN_STATO_REGMOVFINSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_REG_MOVFIN_STATO_REGMOVFIN_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_REG_MOVFIN_STATO_REGMOVFINSTATOID_GENERATOR")
	@Column(name="regmovfin_stato_id")
	private Integer regmovfinStatoId;

	@Column(name="regmovfin_stato_code")
	private String regmovfinStatoCode;

	@Column(name="regmovfin_stato_desc")
	private String regmovfinStatoDesc;

	//bi-directional many-to-one association to SiacRRegMovfinStato
	@OneToMany(mappedBy="siacDRegMovfinStato")
	private List<SiacRRegMovfinStato> siacRRegMovfinStatos;

	public SiacDRegMovfinStato() {
	}

	public Integer getRegmovfinStatoId() {
		return this.regmovfinStatoId;
	}

	public void setRegmovfinStatoId(Integer regmovfinStatoId) {
		this.regmovfinStatoId = regmovfinStatoId;
	}

	public String getRegmovfinStatoCode() {
		return this.regmovfinStatoCode;
	}

	public void setRegmovfinStatoCode(String regmovfinStatoCode) {
		this.regmovfinStatoCode = regmovfinStatoCode;
	}

	public String getRegmovfinStatoDesc() {
		return this.regmovfinStatoDesc;
	}

	public void setRegmovfinStatoDesc(String regmovfinStatoDesc) {
		this.regmovfinStatoDesc = regmovfinStatoDesc;
	}

	public List<SiacRRegMovfinStato> getSiacRRegMovfinStatos() {
		return this.siacRRegMovfinStatos;
	}

	public void setSiacRRegMovfinStatos(List<SiacRRegMovfinStato> siacRRegMovfinStatos) {
		this.siacRRegMovfinStatos = siacRRegMovfinStatos;
	}

	public SiacRRegMovfinStato addSiacRRegMovfinStato(SiacRRegMovfinStato siacRRegMovfinStato) {
		getSiacRRegMovfinStatos().add(siacRRegMovfinStato);
		siacRRegMovfinStato.setSiacDRegMovfinStato(this);

		return siacRRegMovfinStato;
	}

	public SiacRRegMovfinStato removeSiacRRegMovfinStato(SiacRRegMovfinStato siacRRegMovfinStato) {
		getSiacRRegMovfinStatos().remove(siacRRegMovfinStato);
		siacRRegMovfinStato.setSiacDRegMovfinStato(null);

		return siacRRegMovfinStato;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.regmovfinStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.regmovfinStatoId = uid;
		
	}

}