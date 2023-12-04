/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.model;

import java.io.Serializable;

/**
 * contiene i parametri utilizzati dal servizio carica stipendi
 *
 */
public final class StipendioParams implements Serializable{
	
	private static final long serialVersionUID = -3244195366652137108L;
	
	/** Do not instantiate */
	private StipendioParams() {
		// Non instanziare: classe di costanti
	}

	public static final String CODICE_ELABORAZIONE_INTERROTTA = "ELABORAZIONE_INTERROTTA";
	public static final String CODICE_ELABORAZIONE_PARZIALE = "ELABORAZIONE_PARZIALE";
	public static final int NUM_RISULTATI_PER_PAGINA = 50;
	public static final int GIORNO_MESE_DATA_SCADENZA = 15;
	public static final String CODICE_STIPENDIO_SCARTATO = "STIPENDIO_SCARTATO";
	public static final String CODICE_DESC_STIPENDIO_LORDO = "STIPENDI MESE ";
	public static final String CODICE_DESC_ONERI_RITENUTE = "RITENUTE/ONERI MESE ";
	public static final String CODICE_DESC_RITENUTE = "RITENUTE MESE ";
	public static final String CODICE_DOCUMENTO = "STI";
	//copiato codice come e' presente sulla tabella
	public static final String CODICE_TIPO_ACCREDITO_F24 = "F24";
	
	
}
