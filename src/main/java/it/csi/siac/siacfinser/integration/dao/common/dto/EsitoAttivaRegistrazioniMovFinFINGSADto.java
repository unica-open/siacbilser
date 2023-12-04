/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * The Class EsitoAttivaRegistrazioniGENGSADto.
 * @author elisa
 * @version 1.0.0 - 18/01/2018
 */
public class EsitoAttivaRegistrazioniMovFinFINGSADto implements Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6786428075042888631L;

	private RegistrazioneMovFin registrazioneMovFinFINInserita;
	
	private RegistrazioneMovFin registrazioneMovFinGSAInserita;
	
	private boolean avviatoServizioInserimentoPromaNota = true;
	
	private boolean condizioneAttivazioneRegistrazioniSoddisfatta = true;

	/**
	 * Gets the registrazione mov fin FIN inserita.
	 *
	 * @return the registrazioneMovFinGENInserita
	 */
	public RegistrazioneMovFin getRegistrazioneMovFinFINInserita() {
		return registrazioneMovFinFINInserita != null && Ambito.AMBITO_FIN.equals(registrazioneMovFinFINInserita.getAmbito())? registrazioneMovFinFINInserita : null;
	}

	/**
	 * Sets the registrazione mov fin FIN inserita.
	 *
	 * @param registrazioneMovFinFINInserita the new registrazione mov fin FIN inserita
	 */
	public void setRegistrazioneMovFinFINInserita(RegistrazioneMovFin registrazioneMovFinFINInserita) {
		this.registrazioneMovFinFINInserita = registrazioneMovFinFINInserita;
	}

	/**
	 * @return the registrazioneMovFinGSAInserita
	 */
	public RegistrazioneMovFin getRegistrazioneMovFinGSAInserita() {
		return registrazioneMovFinGSAInserita != null && Ambito.AMBITO_GSA.equals(registrazioneMovFinGSAInserita.getAmbito())? registrazioneMovFinGSAInserita : null;
	}

	/**
	 * @param registrazioneMovFinGSAInserita the registrazioneMovFinGSAInserita to set
	 */
	public void setRegistrazioneMovFinGSAInserita(RegistrazioneMovFin registrazioneMovFinGSAInserita) {
		this.registrazioneMovFinGSAInserita = registrazioneMovFinGSAInserita;
	}

	/**
	 * Checks if is avviato servizio inserimento proma nota.
	 *
	 * @return the avviatoServizioInserimentoPromaNota
	 */
	public boolean isAvviatoServizioInserimentoPromaNota() {
		return avviatoServizioInserimentoPromaNota;
	}

	/**
	 * Sets the avviato servizio inserimento proma nota.
	 *
	 * @param avviatoServizioInserimentoPromaNota the avviatoServizioInserimentoPromaNota to set
	 */
	public void setAvviatoServizioInserimentoPromaNota(boolean avviatoServizioInserimentoPromaNota) {
		this.avviatoServizioInserimentoPromaNota = avviatoServizioInserimentoPromaNota;
	}

	/**
	 * @return the condizioneAttivazioneRegistrazioniSoddisfatta
	 */
	public boolean isCondizioneAttivazioneRegistrazioniSoddisfatta() {
		return condizioneAttivazioneRegistrazioniSoddisfatta;
	}

	/**
	 * @param condizioneAttivazioneRegistrazioniSoddisfatta the condizioneAttivazioneRegistrazioniSoddisfatta to set
	 */
	public void setCondizioneAttivazioneRegistrazioniSoddisfatta(boolean condizioneAttivazioneRegistrazioniSoddisfatta) {
		this.condizioneAttivazioneRegistrazioniSoddisfatta = condizioneAttivazioneRegistrazioniSoddisfatta;
	}

	
}
