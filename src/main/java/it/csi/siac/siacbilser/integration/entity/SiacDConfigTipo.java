/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_ambito database table.
 * 
 */
@Entity
@Table(name="siac_d_ambito")
public class SiacDConfigTipo implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6194621562259296476L;

	/** The ambito id. */
	@Id
	@Column(name="config_tipo_code")
	private String configTipoCode;

	/** The ambito code. */
	@Column(name="config_tipo_desc")
	private String configTipoDesc;
	
	/** The data inizio validita. */
	@Basic
	@Column(name = "validita_inizio", updatable=false)
	private Date dataInizioValidita;

	/** The data fine validita. */
	@Basic
	@Column(name = "validita_fine")
	private Date dataFineValidita;

	/** The data creazione. */
	@Basic
	@Column(name = "data_creazione", updatable=false)
	private Date dataCreazione;

	/** The data modifica. */
	@Basic
	@Column(name = "data_modifica")
	private Date dataModifica;

	/** The data cancellazione. */
	@Basic
	@Column(name = "data_cancellazione")
	private Date dataCancellazione;

	/** The login operazione. */
	@Basic
	@Column(name = "login_operazione")
	private String loginOperazione;

	
	/**
	 * Instantiates a new siac t base.
	 */
	public SiacDConfigTipo() {
		super();
	}
	
	public String getConfigTipoCode() {
		return configTipoCode;
	}



	public void setConfigTipoCode(String configTipoCode) {
		this.configTipoCode = configTipoCode;
	}



	public String getConfigTipoDesc() {
		return configTipoDesc;
	}



	public void setConfigTipoDesc(String configTipoDesc) {
		this.configTipoDesc = configTipoDesc;
	}



	/**
	 * Gets the data cancellazione.
	 *
	 * @return the data cancellazione
	 */
	public Date getDataCancellazione() {
		return dataCancellazione;
	}

	/**
	 * Sets the data cancellazione.
	 *
	 * @param dataCancellazione the new data cancellazione
	 */
	public void setDataCancellazione(Date dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}
	
	/**
	 * Gets the data creazione.
	 *
	 * @return the data creazione
	 */
	public Date getDataCreazione() {
		return dataCreazione;
	}

	/**
	 * Sets the data creazione.
	 *
	 * @param dataCreazione the new data creazione
	 */
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	/**
	 * Gets the data modifica.
	 *
	 * @return the data modifica
	 */
	public Date getDataModifica() {
		return dataModifica;
	}

	/**
	 * Sets the data modifica.
	 *
	 * @param dataModifica the new data modifica
	 */
	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;		
	}
	
	/**
	 * Gets the login operazione.
	 *
	 * @return the login operazione
	 */
	public String getLoginOperazione() {
		return loginOperazione;
	}

	/**
	 * Sets the login operazione.
	 *
	 * @param loginOperazione the new login operazione
	 */
	public void setLoginOperazione(String loginOperazione) {
		this.loginOperazione = loginOperazione;
	}

	/**
	 * Gets the data inizio validita.
	 *
	 * @return the data inizio validita
	 */
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * Sets the data inizio validita.
	 *
	 * @param dataInizioValidita the new data inizio validita
	 */
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * Gets the data fine validita.
	 *
	 * @return the data fine validita
	 */
	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * Sets the data fine validita.
	 *
	 * @param dataFineValidita the new data fine validita
	 */
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	

}