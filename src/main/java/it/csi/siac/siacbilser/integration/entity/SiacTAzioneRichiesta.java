/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_azione_richiesta database table.
 * 
 */
@Entity
@Table(name="siac_t_azione_richiesta")
public class SiacTAzioneRichiesta extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The azione richiesta id. */
	@Id
	@SequenceGenerator(name="SIAC_T_AZIONE_RICHIESTA_AZIONERICHIESTAID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_AZIONE_RICHIESTA_AZIONE_RICHIESTA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_AZIONE_RICHIESTA_AZIONERICHIESTAID_GENERATOR")
	@Column(name="azione_richiesta_id")
	private Integer azioneRichiestaId;

	/** The attivita id. */
	@Column(name="attivita_id")
	private String attivitaId;

	/** The da cruscotto. */
	@Column(name="da_cruscotto")
	private String daCruscotto;

	/** The data. */
	private Date data;

	//bi-directional many-to-one association to SiacTAccount
	/** The siac t account. */
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccount siacTAccount;

	//bi-directional many-to-one association to SiacTAzione
	/** The siac t azione. */
	@ManyToOne
	@JoinColumn(name="azione_id")
	private SiacTAzione siacTAzione;

	//bi-directional many-to-one association to SiacTParametroAzioneRichiesta
	/** The siac t parametro azione richiestas. */
	@OneToMany(mappedBy="siacTAzioneRichiesta")
	private List<SiacTParametroAzioneRichiesta> siacTParametroAzioneRichiestas;

	/**
	 * Instantiates a new siac t azione richiesta.
	 */
	public SiacTAzioneRichiesta() {
	}

	/**
	 * Gets the azione richiesta id.
	 *
	 * @return the azione richiesta id
	 */
	public Integer getAzioneRichiestaId() {
		return this.azioneRichiestaId;
	}

	/**
	 * Sets the azione richiesta id.
	 *
	 * @param azioneRichiestaId the new azione richiesta id
	 */
	public void setAzioneRichiestaId(Integer azioneRichiestaId) {
		this.azioneRichiestaId = azioneRichiestaId;
	}

	/**
	 * Gets the attivita id.
	 *
	 * @return the attivita id
	 */
	public String getAttivitaId() {
		return this.attivitaId;
	}

	/**
	 * Sets the attivita id.
	 *
	 * @param attivitaId the new attivita id
	 */
	public void setAttivitaId(String attivitaId) {
		this.attivitaId = attivitaId;
	}

	/**
	 * Gets the da cruscotto.
	 *
	 * @return the da cruscotto
	 */
	public String getDaCruscotto() {
		return this.daCruscotto;
	}

	/**
	 * Sets the da cruscotto.
	 *
	 * @param daCruscotto the new da cruscotto
	 */
	public void setDaCruscotto(String daCruscotto) {
		this.daCruscotto = daCruscotto;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Date getData() {
		return this.data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * Gets the siac t account.
	 *
	 * @return the siac t account
	 */
	public SiacTAccount getSiacTAccount() {
		return this.siacTAccount;
	}

	/**
	 * Sets the siac t account.
	 *
	 * @param siacTAccount the new siac t account
	 */
	public void setSiacTAccount(SiacTAccount siacTAccount) {
		this.siacTAccount = siacTAccount;
	}

	/**
	 * Gets the siac t azione.
	 *
	 * @return the siac t azione
	 */
	public SiacTAzione getSiacTAzione() {
		return this.siacTAzione;
	}

	/**
	 * Sets the siac t azione.
	 *
	 * @param siacTAzione the new siac t azione
	 */
	public void setSiacTAzione(SiacTAzione siacTAzione) {
		this.siacTAzione = siacTAzione;
	}

	/**
	 * Gets the siac t parametro azione richiestas.
	 *
	 * @return the siac t parametro azione richiestas
	 */
	public List<SiacTParametroAzioneRichiesta> getSiacTParametroAzioneRichiestas() {
		return this.siacTParametroAzioneRichiestas;
	}

	/**
	 * Sets the siac t parametro azione richiestas.
	 *
	 * @param siacTParametroAzioneRichiestas the new siac t parametro azione richiestas
	 */
	public void setSiacTParametroAzioneRichiestas(List<SiacTParametroAzioneRichiesta> siacTParametroAzioneRichiestas) {
		this.siacTParametroAzioneRichiestas = siacTParametroAzioneRichiestas;
	}

	/**
	 * Adds the siac t parametro azione richiesta.
	 *
	 * @param siacTParametroAzioneRichiesta the siac t parametro azione richiesta
	 * @return the siac t parametro azione richiesta
	 */
	public SiacTParametroAzioneRichiesta addSiacTParametroAzioneRichiesta(SiacTParametroAzioneRichiesta siacTParametroAzioneRichiesta) {
		getSiacTParametroAzioneRichiestas().add(siacTParametroAzioneRichiesta);
		siacTParametroAzioneRichiesta.setSiacTAzioneRichiesta(this);

		return siacTParametroAzioneRichiesta;
	}

	/**
	 * Removes the siac t parametro azione richiesta.
	 *
	 * @param siacTParametroAzioneRichiesta the siac t parametro azione richiesta
	 * @return the siac t parametro azione richiesta
	 */
	public SiacTParametroAzioneRichiesta removeSiacTParametroAzioneRichiesta(SiacTParametroAzioneRichiesta siacTParametroAzioneRichiesta) {
		getSiacTParametroAzioneRichiestas().remove(siacTParametroAzioneRichiesta);
		siacTParametroAzioneRichiesta.setSiacTAzioneRichiesta(null);

		return siacTParametroAzioneRichiesta;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return azioneRichiestaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.azioneRichiestaId = uid;
	}

}