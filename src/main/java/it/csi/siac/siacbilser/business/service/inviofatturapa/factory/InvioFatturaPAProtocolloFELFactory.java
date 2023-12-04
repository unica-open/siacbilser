/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.InformazioneType;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.ProtocolloFEL;

public final class InvioFatturaPAProtocolloFELFactory {
	
	private static final LogSrvUtil LOG = new LogSrvUtil(InvioFatturaPAProtocolloFELFactory.class);
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPAProtocolloFELFactory() {
		// Previene l'instanziazione
	}
	
	private enum InformazioniAggiuntiveEnum {
		PRINCIPALID_ARCHIVIAZIONE("PrincipalId_archiviazione"),
		ID_CLASSIFICAZIONE("Id_classificazione"),
		INDICE_CLASSIFICAZIONE_ESTESA("Indice_classificazione_estesa"),
		OGGETTO("Oggetto"),
		NUMERO_REG_PROTOCOLLO("Numero_reg_protocollo"),
		DATA_REG_PROTOCOLLO("Data_reg_protocollo"),
		ALTRO("altro");
		
		private String nome;
		
		private InformazioniAggiuntiveEnum(String nome) {
			this.nome = nome;
		}
		
		private static InformazioniAggiuntiveEnum byNome(String nome) {
			for (InformazioniAggiuntiveEnum info : InformazioniAggiuntiveEnum.values()) {
				if (info.nome.equalsIgnoreCase(nome)) {
					return info;
				}
			}
			return ALTRO;
		}
	}

	public static ProtocolloFEL init(FatturaFEL fatturaFEL, List<InformazioneType> informazioneTypes, Ente ente) {
		ProtocolloFEL protocolloFEL = new ProtocolloFEL();
		protocolloFEL.setEnte(ente);
		protocolloFEL.setFattura(fatturaFEL);
		if(informazioneTypes == null) {
			return protocolloFEL;
		}
		
		for (InformazioneType informazione : informazioneTypes) {
			InformazioniAggiuntiveEnum nome = InformazioniAggiuntiveEnum.byNome(informazione.getNome());
			switch (nome) {
			case PRINCIPALID_ARCHIVIAZIONE:
				protocolloFEL.setPrincipalIdArchiviazione(informazione.getValore());
				break;
			case ID_CLASSIFICAZIONE:
				protocolloFEL.setIdClassificazione(informazione.getValore());
				break;
			case INDICE_CLASSIFICAZIONE_ESTESA:
				protocolloFEL.setIndiceClassificazioneEstesa(informazione.getValore());
				break;
			case OGGETTO:
				protocolloFEL.setOggetto(informazione.getValore());
				break;
			case NUMERO_REG_PROTOCOLLO:
				impostaDatiProtocollo(protocolloFEL, informazione);
				break;
			case DATA_REG_PROTOCOLLO:
				protocolloFEL.setDataRegProtocollo(getDataRegProtocollo(informazione.getValore()));
				break;
			default:
				break;
			}
		}
		
		// SIAC-3101 : se l'anno di protocollo non e' settato ma la data lo e', prendo l'anno da tale data
		if(protocolloFEL.getAnnoProtocollo() == null && protocolloFEL.getDataRegProtocollo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(protocolloFEL.getDataRegProtocollo());
			int anno = cal.get(Calendar.YEAR);
			protocolloFEL.setAnnoProtocollo(String.valueOf(anno));
		}
		
		return protocolloFEL;
	}
	
	/**
	 * Impostazione dei deti di protocollo (registro, numero, anno)
	 * @param protocolloFEL il protocollo
	 * @param informazione l'informaziopne
	 */
	private static void impostaDatiProtocollo(ProtocolloFEL protocolloFEL, InformazioneType informazione) {
		String[] parts = getPartsRegProtocollo(informazione.getValore());
		protocolloFEL.setRegistroProtocollo(parts[0]);
		protocolloFEL.setNumeroProtocollo(parts[1]);
		protocolloFEL.setAnnoProtocollo(parts[2]);
	}

	/**
	 * Ottiene le parti necessarie per la compilazione dei dati di protocollo.
	 * 
	 * @param valoreInformazione il valore dell'informazione
	 * @return un array con i valori da impostare per i protocolli
	 */
	private static String[] getPartsRegProtocollo(String valoreInformazione) {
		final String methodName = "getPartsRegProtocollo";
		String[] chunks = valoreInformazione.split("/");
		if(chunks.length == 3) {
			LOG.debug(methodName, "Tre pezzi componenti i dati di protocollo: spezzo i dati");
			// Valori corretti
			return chunks;
		}
		
		LOG.debug(methodName, "Non ho tre dati distinti: copio i dati nel numero di protocollo");
		// Salvo i dati interamente come numero del protocollo
		String[] parts = new String[3];
		parts[0] = null;
		parts[1] = valoreInformazione;
		parts[2] = null;
		return parts;
	}
	
	/**
	 * Ottiee la data di protocollo da impostare.
	 * 
	 * @param valoreInformazione il valore dell'informazione
	 * @return la data di protocollo
	 */
	private static Date getDataRegProtocollo(String valoreInformazione) {
		final String methodName = "getDataRegProtocollo";
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
			return df.parse(valoreInformazione);
		} catch (ParseException e) {
			LOG.debug(methodName, "Errore di parsificazione per la stringa " + valoreInformazione + ": " + e.getMessage());
			// Valori non conformi
			return null;
		}
	}
}
