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
 * The persistent class for the siac_d_prima_nota_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_prima_nota_stato")
@NamedQuery(name="SiacDPrimaNotaStato.findAll", query="SELECT s FROM SiacDPrimaNotaStato s")
public class SiacDPrimaNotaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PRIMA_NOTA_STATO_PNOTASTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PRIMA_NOTA_STATO_PNOTA_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PRIMA_NOTA_STATO_PNOTASTATOID_GENERATOR")
	@Column(name="pnota_stato_id")
	private Integer pnotaStatoId;

	@Column(name="pnota_stato_code")
	private String pnotaStatoCode;

	@Column(name="pnota_stato_desc")
	private String pnotaStatoDesc;

	//bi-directional many-to-one association to SiacRPrimaNotaStato
	@OneToMany(mappedBy="siacDPrimaNotaStato")
	private List<SiacRPrimaNotaStato> siacRPrimaNotaStatos;

	public SiacDPrimaNotaStato() {
	}

	public Integer getPnotaStatoId() {
		return this.pnotaStatoId;
	}

	public void setPnotaStatoId(Integer pnotaStatoId) {
		this.pnotaStatoId = pnotaStatoId;
	}

	public String getPnotaStatoCode() {
		return this.pnotaStatoCode;
	}

	public void setPnotaStatoCode(String pnotaStatoCode) {
		this.pnotaStatoCode = pnotaStatoCode;
	}

	public String getPnotaStatoDesc() {
		return this.pnotaStatoDesc;
	}

	public void setPnotaStatoDesc(String pnotaStatoDesc) {
		this.pnotaStatoDesc = pnotaStatoDesc;
	}

	public List<SiacRPrimaNotaStato> getSiacRPrimaNotaStatos() {
		return this.siacRPrimaNotaStatos;
	}

	public void setSiacRPrimaNotaStatos(List<SiacRPrimaNotaStato> siacRPrimaNotaStatos) {
		this.siacRPrimaNotaStatos = siacRPrimaNotaStatos;
	}

	public SiacRPrimaNotaStato addSiacRPrimaNotaStato(SiacRPrimaNotaStato siacRPrimaNotaStato) {
		getSiacRPrimaNotaStatos().add(siacRPrimaNotaStato);
		siacRPrimaNotaStato.setSiacDPrimaNotaStato(this);

		return siacRPrimaNotaStato;
	}

	public SiacRPrimaNotaStato removeSiacRPrimaNotaStato(SiacRPrimaNotaStato siacRPrimaNotaStato) {
		getSiacRPrimaNotaStatos().remove(siacRPrimaNotaStato);
		siacRPrimaNotaStato.setSiacDPrimaNotaStato(null);

		return siacRPrimaNotaStato;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.pnotaStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnotaStatoId = uid;
		
	}

}