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
 * The persistent class for the siac_d_giustificativo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_giustificativo_stato")
@NamedQuery(name="SiacDGiustificativoStato.findAll", query="SELECT s FROM SiacDGiustificativoStato s")
public class SiacDGiustificativoStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_GIUSTIFICATIVO_STATO_GIUSTSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_GIUSTIFICATIVO_STATO_GIUST_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_GIUSTIFICATIVO_STATO_GIUSTSTATOID_GENERATOR")
	@Column(name="giust_stato_id")
	private Integer giustStatoId;

	@Column(name="giust_stato_code")
	private String giustStatoCode;

	@Column(name="giust_stato_desc")
	private String giustStatoDesc;

	//bi-directional many-to-one association to SiacRGiustificativoStato
	@OneToMany(mappedBy="siacDGiustificativoStato")
	private List<SiacRGiustificativoStato> siacRGiustificativoStatos;

	public SiacDGiustificativoStato() {
	}

	public Integer getGiustStatoId() {
		return this.giustStatoId;
	}

	public void setGiustStatoId(Integer giustStatoId) {
		this.giustStatoId = giustStatoId;
	}

	public String getGiustStatoCode() {
		return this.giustStatoCode;
	}

	public void setGiustStatoCode(String giustStatoCode) {
		this.giustStatoCode = giustStatoCode;
	}

	public String getGiustStatoDesc() {
		return this.giustStatoDesc;
	}

	public void setGiustStatoDesc(String giustStatoDesc) {
		this.giustStatoDesc = giustStatoDesc;
	}

	public List<SiacRGiustificativoStato> getSiacRGiustificativoStatos() {
		return this.siacRGiustificativoStatos;
	}

	public void setSiacRGiustificativoStatos(List<SiacRGiustificativoStato> siacRGiustificativoStatos) {
		this.siacRGiustificativoStatos = siacRGiustificativoStatos;
	}

	public SiacRGiustificativoStato addSiacRGiustificativoStato(SiacRGiustificativoStato siacRGiustificativoStato) {
		getSiacRGiustificativoStatos().add(siacRGiustificativoStato);
		siacRGiustificativoStato.setSiacDGiustificativoStato(this);

		return siacRGiustificativoStato;
	}

	public SiacRGiustificativoStato removeSiacRGiustificativoStato(SiacRGiustificativoStato siacRGiustificativoStato) {
		getSiacRGiustificativoStatos().remove(siacRGiustificativoStato);
		siacRGiustificativoStato.setSiacDGiustificativoStato(null);

		return siacRGiustificativoStato;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.giustStatoId;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.giustStatoId = uid;
		
	}

}