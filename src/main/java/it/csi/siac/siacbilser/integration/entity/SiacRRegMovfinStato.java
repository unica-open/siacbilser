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
 * The persistent class for the siac_r_reg_movfin_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_reg_movfin_stato")
@NamedQuery(name="SiacRRegMovfinStato.findAll", query="SELECT s FROM SiacRRegMovfinStato s")
public class SiacRRegMovfinStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_REG_MOVFIN_STATO_REGMOVFINSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_REG_MOVFIN_STATO_REGMOVFIN_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_REG_MOVFIN_STATO_REGMOVFINSTATORID_GENERATOR")
	@Column(name="regmovfin_stato_r_id")
	private Integer regmovfinStatoRId;


	//bi-directional many-to-one association to SiacDRegMovfinStato
	@ManyToOne
	@JoinColumn(name="regmovfin_stato_id")
	private SiacDRegMovfinStato siacDRegMovfinStato;

	//bi-directional many-to-one association to SiacTRegMovfin
	@ManyToOne
	@JoinColumn(name="regmovfin_id")
	private SiacTRegMovfin siacTRegMovfin;

	public SiacRRegMovfinStato() {
	}

	public Integer getRegmovfinStatoRId() {
		return this.regmovfinStatoRId;
	}

	public void setRegmovfinStatoRId(Integer regmovfinStatoRId) {
		this.regmovfinStatoRId = regmovfinStatoRId;
	}

	public SiacDRegMovfinStato getSiacDRegMovfinStato() {
		return this.siacDRegMovfinStato;
	}

	public void setSiacDRegMovfinStato(SiacDRegMovfinStato siacDRegMovfinStato) {
		this.siacDRegMovfinStato = siacDRegMovfinStato;
	}

	public SiacTRegMovfin getSiacTRegMovfin() {
		return this.siacTRegMovfin;
	}

	public void setSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		this.siacTRegMovfin = siacTRegMovfin;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.regmovfinStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.regmovfinStatoRId = uid;
		
	}
}