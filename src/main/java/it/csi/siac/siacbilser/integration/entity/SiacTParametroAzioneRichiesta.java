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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_parametro_azione_richiesta database table.
 * 
 */
@Entity
@Table(name="siac_t_parametro_azione_richiesta")
public class SiacTParametroAzioneRichiesta extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The parametro id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PARAMETRO_AZIONE_RICHIESTA_PARAMETROID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PARAMETRO_AZIONE_RICHIESTA_PARAMETRO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PARAMETRO_AZIONE_RICHIESTA_PARAMETROID_GENERATOR")
	@Column(name="parametro_id")
	private Integer parametroId;

	/** The nome. */
	private String nome;
	
	/** The valore. */
	private String valore;

	//bi-directional many-to-one association to SiacTAzioneRichiesta
	/** The siac t azione richiesta. */
	@ManyToOne
	@JoinColumn(name="azione_richiesta_id")
	private SiacTAzioneRichiesta siacTAzioneRichiesta;

	/**
	 * Instantiates a new siac t parametro azione richiesta.
	 */
	public SiacTParametroAzioneRichiesta() {
	}

	/**
	 * Gets the parametro id.
	 *
	 * @return the parametro id
	 */
	public Integer getParametroId() {
		return this.parametroId;
	}

	/**
	 * Sets the parametro id.
	 *
	 * @param parametroId the new parametro id
	 */
	public void setParametroId(Integer parametroId) {
		this.parametroId = parametroId;
	}

	

	/**
	 * Gets the nome.
	 *
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Sets the nome.
	 *
	 * @param nome the new nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}	

	/**
	 * Gets the valore.
	 *
	 * @return the valore
	 */
	public String getValore() {
		return this.valore;
	}

	/**
	 * Sets the valore.
	 *
	 * @param valore the new valore
	 */
	public void setValore(String valore) {
		this.valore = valore;
	}

	/**
	 * Gets the siac t azione richiesta.
	 *
	 * @return the siac t azione richiesta
	 */
	public SiacTAzioneRichiesta getSiacTAzioneRichiesta() {
		return this.siacTAzioneRichiesta;
	}

	/**
	 * Sets the siac t azione richiesta.
	 *
	 * @param siacTAzioneRichiesta the new siac t azione richiesta
	 */
	public void setSiacTAzioneRichiesta(SiacTAzioneRichiesta siacTAzioneRichiesta) {
		this.siacTAzioneRichiesta = siacTAzioneRichiesta;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return parametroId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.parametroId = uid;
		
	}

	

}