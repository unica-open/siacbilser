/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_cespiti_mov_ep_det")
@NamedQuery(name="SiacRCespitiMovEpDet.findAll", query="SELECT s FROM SiacRCespitiMovEpDet s")
public class SiacRCespitiMovEpDet extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */

	private static final long serialVersionUID = 4124869530964685461L;

	/** The variazione stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CESPITI_MOV_EP_DET_CESMOVEPDETID_GENERATOR", allocationSize=1, sequenceName="siac_r_cespiti_mov_ep_det_ces_movep_det_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CESPITI_MOV_EP_DET_CESMOVEPDETID_GENERATOR")
	@Column(name="ces_movep_det_id")
	private Integer cesMovepDetId;

	//bi-directional many-to-one association to SiacDVariazioneStato
	/** The siac d variazione stato. */
	@ManyToOne
	@JoinColumn(name="ces_id")
	private SiacTCespiti siacTCespiti;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="movep_det_id")
	private SiacTMovEpDet siacTMovEpDet;
	
	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;
	
	@Column(name="ces_contestuale")
	private Boolean cesContestuale;
	
	@ManyToOne
	@JoinColumn(name="pnota_alienazione_id")
	private SiacTPrimaNota siacTPrimaNotaAlienazione;
	

	@Column(name="importo_su_prima_nota")
	private BigDecimal importoSuPrimaNota;
	
	/**
	 * Instantiates a new siac r variazione stato.
	 */
	public SiacRCespitiMovEpDet() {
	}


	/**
	 * Gets the siac d variazione stato.
	 *
	 * @return the siac d variazione stato
	 */
	public SiacTCespiti getSiacTCespiti() {
		return this.siacTCespiti;
	}

	/**
	 * Sets the siac d variazione stato.
	 *
	 * @param siacTCespiti the new siac T cespiti
	 */
	public void setSiacTCespiti(SiacTCespiti siacTCespiti) {
		this.siacTCespiti = siacTCespiti;
	}

	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTMovEpDet getSiacTMovEpDet() {
		return this.siacTMovEpDet;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTCespitiElabAmmortamentiDettDare the new siac T cespiti elab ammortamenti dett dare
	 */
	public void setSiacTMovEpDete(SiacTMovEpDet siacTMovEpDet) {
		this.siacTMovEpDet = siacTMovEpDet;
	}

	/**
	 * @return the siacTPrimaNota
	 */
	public SiacTPrimaNota getSiacTPrimaNota() {
		return siacTPrimaNota;
	}



	/**
	 * @param siacTPrimaNota the siacTPrimaNota to set
	 */
	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}
	
	/**
	 * @return the cesContestuale
	 */
	public Boolean getCesContestuale() {
		return cesContestuale;
	}


	/**
	 * @param cesContestuale the cesContestuale to set
	 */
	public void setCesContestuale(Boolean cesContestuale) {
		this.cesContestuale = cesContestuale;
	}


	/**
	 * @return the cesMovepDetId
	 */
	public Integer getCesMovepDetId() {
		return cesMovepDetId;
	}


	/**
	 * @param cesMovepDetId the cesMovepDetId to set
	 */
	public void setCesMovepDetId(Integer cesMovepDetId) {
		this.cesMovepDetId = cesMovepDetId;
	}


	/**
	 * @param siacTMovEpDet the siacTMovEpDet to set
	 */
	public void setSiacTMovEpDet(SiacTMovEpDet siacTMovEpDet) {
		this.siacTMovEpDet = siacTMovEpDet;
	}

	/**
	 * @return the siacTPrimaNotaAlienazione
	 */
	public SiacTPrimaNota getSiacTPrimaNotaAlienazione() {
		return siacTPrimaNotaAlienazione;
	}


	/**
	 * @param siacTPrimaNotaAlienazione the siacTPrimaNotaAlienazione to set
	 */
	public void setSiacTPrimaNotaAlienazione(SiacTPrimaNota siacTPrimaNotaAlienazione) {
		this.siacTPrimaNotaAlienazione = siacTPrimaNotaAlienazione;
	}
	
	/**
	 * @return the importoSuPrimaNota
	 */
	public BigDecimal getImportoSuPrimaNota() {
		return importoSuPrimaNota;
	}


	/**
	 * @param importoSuPrimaNota the importoSuPrimaNota to set
	 */
	public void setImportoSuPrimaNota(BigDecimal importoSuPrimaNota) {
		this.importoSuPrimaNota = importoSuPrimaNota;
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cesMovepDetId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cesMovepDetId = uid;
		
	}

}