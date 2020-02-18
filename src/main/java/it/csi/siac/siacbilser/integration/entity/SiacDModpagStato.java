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
 * The persistent class for the siac_d_modpag_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_modpag_stato")
@NamedQuery(name="SiacDModpagStato.findAll", query="SELECT s FROM SiacDModpagStato s")
public class SiacDModpagStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The modpag stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MODPAG_STATO_MODPAGSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MODPAG_STATO_MODPAG_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MODPAG_STATO_MODPAGSTATOID_GENERATOR")
	@Column(name="modpag_stato_id")
	private Integer modpagStatoId;

	/** The modpag stato code. */
	@Column(name="modpag_stato_code")
	private String modpagStatoCode;

	/** The modpag stato desc. */
	@Column(name="modpag_stato_desc")
	private String modpagStatoDesc;


	//bi-directional many-to-one association to SiacRModpagStato
	/** The siac r modpag statos. */
	@OneToMany(mappedBy="siacDModpagStato")
	private List<SiacRModpagStato> siacRModpagStatos;

	/**
	 * Instantiates a new siac d modpag stato.
	 */
	public SiacDModpagStato() {
	}

	/**
	 * Gets the modpag stato id.
	 *
	 * @return the modpag stato id
	 */
	public Integer getModpagStatoId() {
		return this.modpagStatoId;
	}

	/**
	 * Sets the modpag stato id.
	 *
	 * @param modpagStatoId the new modpag stato id
	 */
	public void setModpagStatoId(Integer modpagStatoId) {
		this.modpagStatoId = modpagStatoId;
	}	


	/**
	 * Gets the modpag stato code.
	 *
	 * @return the modpag stato code
	 */
	public String getModpagStatoCode() {
		return this.modpagStatoCode;
	}

	/**
	 * Sets the modpag stato code.
	 *
	 * @param modpagStatoCode the new modpag stato code
	 */
	public void setModpagStatoCode(String modpagStatoCode) {
		this.modpagStatoCode = modpagStatoCode;
	}

	/**
	 * Gets the modpag stato desc.
	 *
	 * @return the modpag stato desc
	 */
	public String getModpagStatoDesc() {
		return this.modpagStatoDesc;
	}

	/**
	 * Sets the modpag stato desc.
	 *
	 * @param modpagStatoDesc the new modpag stato desc
	 */
	public void setModpagStatoDesc(String modpagStatoDesc) {
		this.modpagStatoDesc = modpagStatoDesc;
	}
	
	/**
	 * Gets the siac r modpag statos.
	 *
	 * @return the siac r modpag statos
	 */
	public List<SiacRModpagStato> getSiacRModpagStatos() {
		return this.siacRModpagStatos;
	}

	/**
	 * Sets the siac r modpag statos.
	 *
	 * @param siacRModpagStatos the new siac r modpag statos
	 */
	public void setSiacRModpagStatos(List<SiacRModpagStato> siacRModpagStatos) {
		this.siacRModpagStatos = siacRModpagStatos;
	}

	/**
	 * Adds the siac r modpag stato.
	 *
	 * @param siacRModpagStato the siac r modpag stato
	 * @return the siac r modpag stato
	 */
	public SiacRModpagStato addSiacRModpagStato(SiacRModpagStato siacRModpagStato) {
		getSiacRModpagStatos().add(siacRModpagStato);
		siacRModpagStato.setSiacDModpagStato(this);

		return siacRModpagStato;
	}

	/**
	 * Removes the siac r modpag stato.
	 *
	 * @param siacRModpagStato the siac r modpag stato
	 * @return the siac r modpag stato
	 */
	public SiacRModpagStato removeSiacRModpagStato(SiacRModpagStato siacRModpagStato) {
		getSiacRModpagStatos().remove(siacRModpagStato);
		siacRModpagStato.setSiacDModpagStato(null);

		return siacRModpagStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modpagStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modpagStatoId = uid;
		
	}

}