/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

@Entity
@Table(name="siac_t_atto_allegato")
public class SiacTAllegatoAttoFin extends SiacTEnteBase {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ATTO_ALLEGATO_ATTOALID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ATTO_ALLEGATO_ATTOAL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ATTO_ALLEGATO_ATTOALID_GENERATOR")
	@Column(name="attoal_id")
	private Integer attoalId;

	@Column(name="attoal_altriallegati")
	private String attoalAltriallegati;

	@Column(name="attoal_annotazioni")
	private String attoalAnnotazioni;

	@Column(name="attoal_causale")
	private String attoalCausale;

	@Column(name="attoal_data_scadenza")
	private Date attoalDataScadenza;

	@Column(name="attoal_dati_sensibili")
	private String attoalDatiSensibili;

	@Column(name="attoal_note")
	private String attoalNote;

	@Column(name="attoal_pratica")
	private String attoalPratica;

	@Column(name="attoal_responsabile_amm")
	private String attoalResponsabileAmm;

	@Column(name="attoal_responsabile_con")
	private String attoalResponsabileCon;

	@Column(name="attoal_titolario_anno")
	private Integer attoalTitolarioAnno;

	@Column(name="attoal_titolario_numero")
	private String attoalTitolarioNumero;

	@Column(name="attoal_versione_invio_firma")
	private Integer attoalVersioneInvioFirma;

	@Column(name="attoal_data_invio_firma")
	private Date attoalDataInvioFirma;

	@Column(name="attoal_login_invio_firma")
	private String attoalLoginInvioFirma;
	
	@Column(name="attoal_flag_ritenute")
	private Boolean attoalFlagRitenute;

	//bi-directional many-to-one association to SiacTAttoAmm
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	

	public Boolean getAttoalFlagRitenute() {
		return attoalFlagRitenute;
	}

	public void setAttoalFlagRitenute(Boolean attoalFlagRitenute) {
		this.attoalFlagRitenute = attoalFlagRitenute;
	}

	@Override
	public Integer getUid() {
		return attoalId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalId = uid;
	}

}
