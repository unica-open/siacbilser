/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.ElabThresholdKey;

public enum SiacTElabThresholdEnum {

	EMETTE_ORDINATIVI_DI_PAGAMENTO_DA_ELENCO("EMETTITORE_PAGAMENTO", ElabThresholdKey.EMETTE_ORDINATIVI_DI_PAGAMENTO_DA_ELENCO),
	EMETTE_ORDINATIVI_DI_INCASSO_DA_ELENCO("EMETTITORE_INCASSO", ElabThresholdKey.EMETTE_ORDINATIVI_DI_INCASSO_DA_ELENCO),
	COMPLETA_ALLEGATO_ATTO("COMPLETA_ATTO_ALLEGATO", ElabThresholdKey.COMPLETA_ALLEGATO_ATTO),
	;
	private final String elthresCode;
	private final ElabThresholdKey elabThresholdKey;
	
	/**
	 * Costruttore
	 * @param elthresCode il codice della soglia
	 */
	private SiacTElabThresholdEnum(String elthresCode, ElabThresholdKey elabThresholdKey) {
		this.elthresCode = elthresCode;
		this.elabThresholdKey = elabThresholdKey;
	}

	/**
	 * @return the elthresCode
	 */
	public String getElthresCode() {
		return this.elthresCode;
	}

	/**
	 * @return the elabThresholdKey
	 */
	public ElabThresholdKey getElabThresholdKey() {
		return this.elabThresholdKey;
	}
	
	/**
	 * Ottiene l'enum via la chiave di soglia
	 * @param elabThresholdKey la chiave di soglia
	 * @return l'enum
	 */
	public static SiacTElabThresholdEnum byElabThresholdKey(ElabThresholdKey elabThresholdKey) {
		for(SiacTElabThresholdEnum stete : SiacTElabThresholdEnum.values()) {
			if(stete.elabThresholdKey.equals(elabThresholdKey)) {
				return stete;
			}
		}
		return null;
	}
}
