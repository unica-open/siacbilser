/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service.attiamministrativi.factory;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacintegser.business.service.attiamministrativi.AttoAmministrativoEnteConfig;
import it.csi.siac.siacintegser.business.service.attiamministrativi.factory.AttoAmministrativoFactory;
import it.csi.siac.siacintegser.business.service.attiamministrativi.model.AttoAmministrativoElab;

public class AttoAmministrativoStiloFactory extends AttoAmministrativoFactory {

	public AttoAmministrativoElab newInstanceFromFlussoAttiAmministrativi(String line, int lineNumber, String codiceAccount) {
		if(StringUtils.isBlank(line)) {
			log.info("newInstanceFromFlussoAttiAmministrativi", String.format("Riga %d scartata: vuota", lineNumber));
			return null;
		}
		
//		try {
//			log.warn("newInstanceFromFlussoAttiAmministrativi", "WAWA INIZIO SLEEP 61 s");
//			Thread.sleep(61000);
//			log.warn("newInstanceFromFlussoAttiAmministrativi", "WAWA FINE SLEEP 61 s");
//		}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		
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
		a.setTipoAtto(creaTipoAtto(line.substring(58, 58 + 4)));
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
		
		String b = line.substring(1108, 1108 + 1);
		a.setBloccoRagioneria("1".equals(b) ? Boolean.TRUE : "0".equals(b) ? Boolean.FALSE : null);
		
		// CR-2778
		if(AttoAmministrativoEnteConfig.ignoreCDC(a.getCodiceIstat())) {
			a.setSacCentroDiCostoChiave(null);
			a.setSacCentroDiCosto(null);
		}
		
		return a;
	}
}
