/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi.factory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacintegser.business.service.attiamministrativi.AttoAmministrativoEnteConfig;
import it.csi.siac.siacintegser.business.service.attiamministrativi.model.AttoAmministrativoElab;

/**
 * A factory for creating Atto amministrativo objects.
 * @author Nazha Ahmad
 * @author Alessandro Marchino
 * @version 1.0.0 (10-07-2015)
 * @version 1.1.0 (10-11-2015) - gestione modifica file del flusso
 *
 */
public class AttoAmministrativoFactory {

	private final DateFormat dateFormatData = new SimpleDateFormat("yyyyMMdd", Locale.ITALIAN);
	private final DateFormat dateFormatDateTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ITALIAN);
	
	protected LogSrvUtil log = new LogSrvUtil(this.getClass());

	/**
	 * New instance from flusso Atti Amministrativi
	 *
	 * @param line the line
	 * @param lineNumber 
	 * @param messaggi 
	 * @return the AttoAmministrativoIntegser
	 */
	public AttoAmministrativoElab newInstanceFromFlussoAttiAmministrativi(String line, int lineNumber, String codiceAccount) {
		if(StringUtils.isBlank(line)) {
			log.info("newInstanceFromFlussoAttiAmministrativi", String.format("Riga %d scartata: vuota", lineNumber));
			return null;
		}
		
		AttoAmministrativoElab a = new AttoAmministrativoElab();

		a.setTipoDiVariazione(line.substring(0, 0 + 1));
		a.setCodiceIstat(StringUtils.deleteWhitespace(line.substring(2, 2 + 6)));
		// controllo se la linea sia da scartare
		if("ENTE".equals(StringUtils.deleteWhitespace(a.getCodiceIstat()))) {
			// Vuol dire la linea e' da scartare
			log.info("newInstanceFromFlussoAttiAmministrativi", String.format("Riga %d scartata: Codice Istat = 'ENTE'", lineNumber));
			return null;
		}

		// CR-2547: chiave
		a.setAnnoAttoChiave(parseIntEventuallyNull("anno atto chiave", line.substring(9, 9 + 4)));
		a.setNumeroAttoChiave(parseIntEventuallyNull("numero atto chiave", line.substring(14, 14 + 5)));
		a.setTipoAttoChiave(creaTipoAtto(line.substring(20, 20 + 4)));
		a.setSacCentroDiResponsabilitaChiave(creaStrutturaAmministrativoContabile(line.substring(25, 25 + 10)));
		a.setSacCentroDiCostoChiave(creaStrutturaAmministrativoContabile(line.substring(36, 36 + 10)));
		
		a.setAnno(parseInt("anno atto", StringUtils.deleteWhitespace(line.substring(47, 47 + 4))));
		a.setNumero(parseInt("numero atto", StringUtils.deleteWhitespace(line.substring(52, 52 + 5))));

		String codiceTipoAtto = line.substring(58, 58 + 4).trim();
		if (AttoAmministrativoEnteConfig.ignoreTipoAD(a.getCodiceIstat()) &&
			"AD".equals(codiceTipoAtto)) {
			log.info("newInstanceFromFlussoAttiAmministrativi", String.format("Riga %d scartata: Codice tipo atto = 'AD' da ignorare", lineNumber));
			return null; // SIAC-7376
		}
		
		a.setTipoAtto(creaTipoAtto(codiceTipoAtto));

		a.setSacCentroDiResponsabilita(creaStrutturaAmministrativoContabile(line.substring(63, 63 + 10)));
		a.setSacCentroDiCosto(creaStrutturaAmministrativoContabile(line.substring(74, 74 + 10)));
		a.setDataCreazione(parseDateTime("Data Creazione", StringUtils.deleteWhitespace(line.substring(85, 85 + 14))));
		a.setDataProposta(parseDate("Data Proposta", StringUtils.deleteWhitespace(line.substring(100, 100 + 8))));
		a.setDataApprovazione(parseDateTime("Data Approvazione", StringUtils.deleteWhitespace( line.substring(109, 109 + 14))));
		a.setDataEsecutivita(parseDate("Data Esecutivita", StringUtils.deleteWhitespace( line.substring(124, 124 + 8))));
		a.setStatoOperativoInput(StringUtils.deleteWhitespace(line.substring(133, 133 + 1)));
		a.setStatoOperativo(StringUtils.deleteWhitespace(line.substring(133, 133 + 1)));
		a.setOggetto(StringUtils.trim(line.substring(135, 135 + 500)));
		a.setNote(StringUtils.trim(line.substring(636, 636 + 180)));
		
		// CR-2547
		a.setIdentificativo(StringUtils.trim(line.substring(817, 817 + 8)));
		a.setDirigenteResponsabile(StringUtils.trim(line.substring(826, 826 + 100)));
		a.setTrasparenza(StringUtils.trim(line.substring(927, 927 + 180)));   
		// line should have length 1107
		
		// CR-2778
		if(AttoAmministrativoEnteConfig.ignoreCDC(a.getCodiceIstat())) {
			a.setSacCentroDiCostoChiave(null);
			a.setSacCentroDiCosto(null);
		}
		
		return a;
	}

	protected TipoAtto creaTipoAtto(String str) {
		return creaCodifica(str, TipoAtto.class);
	}
	
	protected StrutturaAmministrativoContabile creaStrutturaAmministrativoContabile(String str) {
		return creaCodifica(str, StrutturaAmministrativoContabile.class);
	}
	
	private <C extends Codifica> C creaCodifica(String codice, Class<C> clazz) {
		if(StringUtils.isBlank(codice)) {
			return null;
		}
		C codifica;
		try {
			codifica = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Instantiation failed for class " + clazz.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("IllegalAccess for class " + clazz.getSimpleName(), e);
		}
		codifica.setCodice(StringUtils.deleteWhitespace(codice));
		return codifica;
	}
	
	protected Integer parseIntEventuallyNull( String nomeParametro,String str) {
		if (StringUtils.isNotBlank(str)) {
			return parseInt(nomeParametro, StringUtils.deleteWhitespace(str));
		}
		return null;
	}

	/**
	 * parse data to Date
	 * 
	 * @param nomeParametro
	 * @param dateString
	 * @return
	 */
	protected Date parseDate(String nomeParametro, String dateString) {
		if (StringUtils.isNotBlank(dateString)) {
			try {
				return dateFormatData.parse(dateString);
			} catch (ParseException nfe) {
				throw new IllegalArgumentException("Il parameto " + nomeParametro + " Ha un formato non valido " + dateString + ".", nfe);
			}
		}
		return null;
	}

	/**
	 * parse dateTime To Date
	 * 
	 * @param nomeParametro
	 * @param dateTimeString
	 * @return
	 */
	protected Date parseDateTime(String nomeParametro, String dateTimeString) {
		if (StringUtils.isNotBlank(dateTimeString)) {
			try {
				return dateFormatDateTime.parse(dateTimeString);
			} catch (ParseException nfe) {
				throw new IllegalArgumentException("Il parameto " + nomeParametro + " Ha un formato non valido " + dateTimeString + ".", nfe);
			}
		}
		return null;
	}

	/**
	 * Parses the int.
	 *
	 * @param nomeParamentro
	 *            the nome paramentro
	 * @param str
	 *            the str
	 * @return the integer
	 */
	protected Integer parseInt(String nomeParametro, String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Il parameto " + nomeParametro + " non e' un intero! valore del parametro: " + str + ".", nfe);
		}
	}
}
