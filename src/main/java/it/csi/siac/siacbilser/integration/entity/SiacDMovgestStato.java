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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_movgest_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_movgest_stato")
@NamedQuery(name="SiacDMovgestStato.findAll", query="SELECT s FROM SiacDMovgestStato s")
public class SiacDMovgestStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MOVGEST_STATO_MOVGESTSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MOVGEST_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MOVGEST_STATO_MOVGESTSTATOID_GENERATOR")
	@Column(name="movgest_stato_id")
	private Integer movgestStatoId;

	/** The movgest stato code. */
	@Column(name="movgest_stato_code")
	private String movgestStatoCode;

	/** The movgest stato desc. */
	@Column(name="movgest_stato_desc")
	private String movgestStatoDesc;

	//bi-directional many-to-one association to SiacRMovgestTsStato
	/** The siac r movgest statos. */
	@OneToMany(mappedBy="siacDMovgestStato")
	private List<SiacRMovgestTsStato> siacRMovgestTsStatos;

	/**
	 * Instantiates a new siac d movgest stato.
	 */
	public SiacDMovgestStato() {
	}

	/**
	 * Gets the movgest stato id.
	 *
	 * @return the movgest stato id
	 */
	public Integer getMovgestStatoId() {
		return this.movgestStatoId;
	}

	/**
	 * Sets the movgest stato id.
	 *
	 * @param movgestStatoId the new movgest stato id
	 */
	public void setMovgestStatoId(Integer movgestStatoId) {
		this.movgestStatoId = movgestStatoId;
	}

	/**
	 * Gets the movgest stato code.
	 *
	 * @return the movgest stato code
	 */
	public String getMovgestStatoCode() {
		return this.movgestStatoCode;
	}

	/**
	 * Sets the movgest stato code.
	 *
	 * @param movgestStatoCode the new movgest stato code
	 */
	public void setMovgestStatoCode(String movgestStatoCode) {
		this.movgestStatoCode = movgestStatoCode;
	}

	/**
	 * Gets the movgest stato desc.
	 *
	 * @return the movgest stato desc
	 */
	public String getMovgestStatoDesc() {
		return this.movgestStatoDesc;
	}

	/**
	 * Sets the movgest stato desc.
	 *
	 * @param movgestStatoDesc the new movgest stato desc
	 */
	public void setMovgestStatoDesc(String movgestStatoDesc) {
		this.movgestStatoDesc = movgestStatoDesc;
	}

	public List<SiacRMovgestTsStato> getSiacRMovgestTsStatos() {
		return this.siacRMovgestTsStatos;
	}

	public void setSiacRMovgestTsStatos(List<SiacRMovgestTsStato> siacRMovgestTsStatos) {
		this.siacRMovgestTsStatos = siacRMovgestTsStatos;
	}

	public SiacRMovgestTsStato addSiacRMovgestTsStato(SiacRMovgestTsStato siacRMovgestTsStato) {
		getSiacRMovgestTsStatos().add(siacRMovgestTsStato);
		siacRMovgestTsStato.setSiacDMovgestStato(this);

		return siacRMovgestTsStato;
	}

	public SiacRMovgestTsStato removeSiacRMovgestTsStato(SiacRMovgestTsStato siacRMovgestTsStato) {
		getSiacRMovgestTsStatos().remove(siacRMovgestTsStato);
		siacRMovgestTsStato.setSiacDMovgestStato(null);

		return siacRMovgestTsStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestStatoId = uid;
	}

}