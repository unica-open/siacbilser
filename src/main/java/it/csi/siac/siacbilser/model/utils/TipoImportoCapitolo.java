/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model.utils;

import it.csi.siac.siaccorser.model.Codifica;

/**
 * Classe di helper per l'importo capitolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/09/2019
 *
 */
public class TipoImportoCapitolo extends Codifica {

	/** Per la serializzazione */
	private static final long serialVersionUID = 915360607596873762L;

	public static enum Values {
		STANZIAMENTO("STA"),
		CASSA("SCA"),
		RESIDUO("STR"),
		FONDO_PLURIENNALE_VINCOLATO("FPV"),
		CASSA_INIZIALE("SCI"),
		STANZIAMENTO_INIZIALE("STI"),
		PROPOSTO("STP"),
		RESIDUO_INIZIALE("SRI"),
		ASSESTAMENTO("STASS"),
		CASSA_ASSESTAMENTO("STCASS"),
		RESIDUO_ASSESTAMENTO("STRASS"),
		MASSIMO_IMPEGNABILE("MI"),
		;
		private final String codice;
		private Values(String codice) {
			this.codice = codice;
		}
		/**
		 * @return the codice
		 */
		public String getCodice() {
			return this.codice;
		}
		
	}
}
