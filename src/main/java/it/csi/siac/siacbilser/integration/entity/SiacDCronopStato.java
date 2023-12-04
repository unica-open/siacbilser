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
 * The persistent class for the siac_d_cronop_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_cronop_stato")
@NamedQuery(name="SiacDCronopStato.findAll", query="SELECT s FROM SiacDCronopStato s")
public class SiacDCronopStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_CRONOP_STATO_CRONOPSTATOID_GENERATOR", allocationSize=1 , sequenceName="SIAC_D_CRONOP_STATO_CRONOP_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CRONOP_STATO_CRONOPSTATOID_GENERATOR")
	@Column(name="cronop_stato_id")
	private Integer cronopStatoId;

	/** The cronop stato code. */
	@Column(name="cronop_stato_code")
	private String cronopStatoCode;

	/** The cronop stato desc. */
	@Column(name="cronop_stato_desc")
	private String cronopStatoDesc;

	//bi-directional many-to-one association to SiacRCronopStato
	/** The siac r cronop statos. */
	@OneToMany(mappedBy="siacDCronopStato")
	private List<SiacRCronopStato> siacRCronopStatos;

	/**
	 * Instantiates a new siac d cronop stato.
	 */
	public SiacDCronopStato() {
	}

	/**
	 * Gets the cronop stato id.
	 *
	 * @return the cronop stato id
	 */
	public Integer getCronopStatoId() {
		return this.cronopStatoId;
	}

	/**
	 * Sets the cronop stato id.
	 *
	 * @param cronopStatoId the new cronop stato id
	 */
	public void setCronopStatoId(Integer cronopStatoId) {
		this.cronopStatoId = cronopStatoId;
	}

	/**
	 * Gets the cronop stato code.
	 *
	 * @return the cronop stato code
	 */
	public String getCronopStatoCode() {
		return this.cronopStatoCode;
	}

	/**
	 * Sets the cronop stato code.
	 *
	 * @param cronopStatoCode the new cronop stato code
	 */
	public void setCronopStatoCode(String cronopStatoCode) {
		this.cronopStatoCode = cronopStatoCode;
	}

	/**
	 * Gets the cronop stato desc.
	 *
	 * @return the cronop stato desc
	 */
	public String getCronopStatoDesc() {
		return this.cronopStatoDesc;
	}

	/**
	 * Sets the cronop stato desc.
	 *
	 * @param cronopStatoDesc the new cronop stato desc
	 */
	public void setCronopStatoDesc(String cronopStatoDesc) {
		this.cronopStatoDesc = cronopStatoDesc;
	}
	
	/**
	 * Gets the siac r cronop statos.
	 *
	 * @return the siac r cronop statos
	 */
	public List<SiacRCronopStato> getSiacRCronopStatos() {
		return this.siacRCronopStatos;
	}

	/**
	 * Sets the siac r cronop statos.
	 *
	 * @param siacRCronopStatos the new siac r cronop statos
	 */
	public void setSiacRCronopStatos(List<SiacRCronopStato> siacRCronopStatos) {
		this.siacRCronopStatos = siacRCronopStatos;
	}

	/**
	 * Adds the siac r cronop stato.
	 *
	 * @param siacRCronopStato the siac r cronop stato
	 * @return the siac r cronop stato
	 */
	public SiacRCronopStato addSiacRCronopStato(SiacRCronopStato siacRCronopStato) {
		getSiacRCronopStatos().add(siacRCronopStato);
		siacRCronopStato.setSiacDCronopStato(this);

		return siacRCronopStato;
	}

	/**
	 * Removes the siac r cronop stato.
	 *
	 * @param siacRCronopStato the siac r cronop stato
	 * @return the siac r cronop stato
	 */
	public SiacRCronopStato removeSiacRCronopStato(SiacRCronopStato siacRCronopStato) {
		getSiacRCronopStatos().remove(siacRCronopStato);
		siacRCronopStato.setSiacDCronopStato(null);

		return siacRCronopStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopStatoId = uid;
	}

}