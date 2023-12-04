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
 * The persistent class for the siac_r_prima_nota_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_prima_nota_stato")
@NamedQuery(name="SiacRPrimaNotaStato.findAll", query="SELECT s FROM SiacRPrimaNotaStato s")
public class SiacRPrimaNotaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PRIMA_NOTA_STATO_PNOTASTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PRIMA_NOTA_STATO_PNOTA_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PRIMA_NOTA_STATO_PNOTASTATORID_GENERATOR")
	@Column(name="pnota_stato_r_id")
	private Integer pnotaStatoRId;

	//bi-directional many-to-one association to SiacDPrimaNotaStato
	@ManyToOne
	@JoinColumn(name="pnota_stato_id")
	private SiacDPrimaNotaStato siacDPrimaNotaStato;

	//bi-directional many-to-one association to SiacTPrimaNota
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;

	public SiacRPrimaNotaStato() {
	}

	public Integer getPnotaStatoRId() {
		return this.pnotaStatoRId;
	}

	public void setPnotaStatoRId(Integer pnotaStatoRId) {
		this.pnotaStatoRId = pnotaStatoRId;
	}

	public SiacDPrimaNotaStato getSiacDPrimaNotaStato() {
		return this.siacDPrimaNotaStato;
	}

	public void setSiacDPrimaNotaStato(SiacDPrimaNotaStato siacDPrimaNotaStato) {
		this.siacDPrimaNotaStato = siacDPrimaNotaStato;
	}

	public SiacTPrimaNota getSiacTPrimaNota() {
		return this.siacTPrimaNota;
	}

	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.pnotaStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnotaStatoRId = uid;
		
	}

}