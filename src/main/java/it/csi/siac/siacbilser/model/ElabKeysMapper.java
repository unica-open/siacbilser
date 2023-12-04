/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model;

import it.csi.siac.siacbilser.business.utility.ElaborazioneEnum;

public enum ElabKeysMapper {
	
	ALLEGATO_ATTO_AGGIORNA(ElaborazioneEnum.ALLEGATO_ATTO_AGGIORNA, ElabKeys.COMPLETA_ALLEGATO_ATTO),
	// B(ElaborazioneEnum.B, ElabKeys.EMISSIONE_ORDINATIVI_PAGAMENTO, ElabKeys.EMISSIONE_ORDINATIVI_INCASSO) // test
	;
	
	private ElaborazioneEnum elaborazione;
	private ElabKeys[] elabKeysList;
	
	private static final ElabKeysMapper[] values = ElabKeysMapper.values();
	
	ElabKeysMapper(ElaborazioneEnum elaborazione, ElabKeys...elabKeysList) {
		setElaborazione(elaborazione);
		setElabKeysList(elabKeysList);
	}
	
	public ElabKeys[] getElabKeysList() {
		return elabKeysList;
	}

	public void setElabKeysList(ElabKeys[] elabKeysList) {
		this.elabKeysList = elabKeysList;
	}

	public ElaborazioneEnum getElaborazione() {
		return elaborazione;
	}

	public void setElaborazione(ElaborazioneEnum elaborazione) {
		this.elaborazione = elaborazione;
	}
	
	public static ElabKeysMapper valueOf(ElaborazioneEnum elaborazione) {
		
		for (ElabKeysMapper ekm : values) {
			if (elaborazione.equals(ekm.getElaborazione())) {
				return ekm;
			}
		}
		
		throw new IllegalArgumentException("No ElabKeysMapper found with ElaborazioneEnum " + elaborazione.name());
	}
}
