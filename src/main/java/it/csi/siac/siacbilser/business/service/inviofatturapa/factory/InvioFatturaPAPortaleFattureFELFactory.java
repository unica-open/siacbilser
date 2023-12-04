/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.DatiPortaleFattureType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.DatiPortaleFattureType.EstremiEsito.Utente;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.PortaleFattureFEL;



public final class InvioFatturaPAPortaleFattureFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPAPortaleFattureFELFactory() {
		// Previene l'instanziazione
	}

	public static PortaleFattureFEL init(FatturaFEL fatturaFEL, DatiPortaleFattureType datiPortaleFattureType, Ente ente) {
		PortaleFattureFEL portaleFattureFEL = new PortaleFattureFEL();
		portaleFattureFEL.setEnte(ente);
		portaleFattureFEL.setFattura(fatturaFEL);
		
		if(datiPortaleFattureType == null) {
			return portaleFattureFEL;
		}
		
		portaleFattureFEL.setIdentificativoFel(Long.valueOf(datiPortaleFattureType.getIdentificativoFEL() + ""));
		portaleFattureFEL.setIdentificativoSdi(Long.valueOf(datiPortaleFattureType.getIdentificativoSDI() + ""));
		portaleFattureFEL.setNomeFileFattura(datiPortaleFattureType.getNomeFileFattura());
		
		Utente utente = datiPortaleFattureType.getEstremiEsito().getUtente();
		if (utente != null) {
			portaleFattureFEL.setEsitoUtenteCodice(utente.getCodice());
			portaleFattureFEL.setEsitoUtenteNome(utente.getNome());
			portaleFattureFEL.setEsitoUtenteCognome(utente.getCognome());
		}
		
		setDataRicezione(portaleFattureFEL, datiPortaleFattureType);
		
		if(datiPortaleFattureType.getEstremiEsito() != null) {
			portaleFattureFEL.setEsitoStatoFattura(datiPortaleFattureType.getEstremiEsito().getStatoFattura());
			portaleFattureFEL.setDescrizioneRifiuto(datiPortaleFattureType.getEstremiEsito().getDescrizioneRifiuto());
			
			setEsitoDataOra(portaleFattureFEL, datiPortaleFattureType);
		}
		
		return portaleFattureFEL;
	}

	private static void setDataRicezione(PortaleFattureFEL portaleFattureFEL, DatiPortaleFattureType datiPortaleFattureType) {
		if(datiPortaleFattureType.getDataRicezione() != null) {
			portaleFattureFEL.setDataRicezione(datiPortaleFattureType.getDataRicezione().toGregorianCalendar().getTime());
		}
	}
	
	private static void setEsitoDataOra(PortaleFattureFEL portaleFattureFEL, DatiPortaleFattureType datiPortaleFattureType) {
		if(datiPortaleFattureType.getEstremiEsito().getDataOra() != null) {
			portaleFattureFEL.setEsitoDataOra(datiPortaleFattureType.getEstremiEsito().getDataOra().toGregorianCalendar().getTime());
		}
	}

}
