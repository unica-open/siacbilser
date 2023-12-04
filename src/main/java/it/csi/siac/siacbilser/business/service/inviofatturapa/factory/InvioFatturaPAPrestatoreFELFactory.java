/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.CedentePrestatoreType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.ContattiType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiAnagraficiCedenteType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.IndirizzoType;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.csi.siac.sirfelser.model.RegimeFiscaleFEL;

public final class InvioFatturaPAPrestatoreFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPAPrestatoreFELFactory() {
		// Previene l'instanziazione
	}
	
	public static PrestatoreFEL init(CedentePrestatoreType cedentePrestatoreType, Ente ente) {
		PrestatoreFEL prestatoreFEL = new PrestatoreFEL();
		prestatoreFEL.setEnte(ente);
		
		if(cedentePrestatoreType == null) {
			return prestatoreFEL;
		}
		DatiAnagraficiCedenteType datiAnagrafici = cedentePrestatoreType.getDatiAnagrafici();
		IndirizzoType sede = cedentePrestatoreType.getSede();
		ContattiType contatti = cedentePrestatoreType.getContatti();

		prestatoreFEL.setCodicePaese(datiAnagrafici.getIdFiscaleIVA().getIdPaese());
		prestatoreFEL.setCodicePrestatore(datiAnagrafici.getIdFiscaleIVA().getIdCodice());
		prestatoreFEL.setDenominazionePrestatore(datiAnagrafici.getAnagrafica().getDenominazione());
		prestatoreFEL.setNomePrestatore(datiAnagrafici.getAnagrafica().getNome());
		prestatoreFEL.setCognomePrestatore(datiAnagrafici.getAnagrafica().getCognome());
		prestatoreFEL.setProvinciaAlboPrestatore(datiAnagrafici.getProvinciaAlbo());
		prestatoreFEL.setIndirizzoPrestatore(sede.getIndirizzo());
		prestatoreFEL.setNumeroCivicoPrestatore(sede.getNumeroCivico());
		prestatoreFEL.setCapPrestatore(sede.getCAP());
		prestatoreFEL.setComunePrestatore(sede.getComune());
		prestatoreFEL.setProvinciaPrestatore(sede.getProvincia());
		prestatoreFEL.setNazionePrestatore(sede.getNazione());
		
		if (contatti != null) {
			prestatoreFEL.setTelefonoPrestatore(contatti.getTelefono());
			prestatoreFEL.setFaxPrestatore(contatti.getFax());
			prestatoreFEL.setEmailPrestatore(contatti.getEmail());
		}
		setRegimeFiscale(prestatoreFEL, datiAnagrafici);
		return prestatoreFEL;
	}

	private static void setRegimeFiscale(PrestatoreFEL prestatoreFEL, DatiAnagraficiCedenteType datiAnagraficiCedenteType) {
		if(datiAnagraficiCedenteType.getRegimeFiscale() != null) {
			prestatoreFEL.setRegimeFiscale(RegimeFiscaleFEL.byCodice(datiAnagraficiCedenteType.getRegimeFiscale().value()));
		}
	}

	public static PrestatoreFEL cercaPrestatoreEsistente(List<PrestatoreFEL> prestatoriFELEsistenti, PrestatoreFEL prestatoreFEL) {
		for(PrestatoreFEL pfel : prestatoriFELEsistenti) {
			if(pfel != null && areEqual(pfel, prestatoreFEL)) {
				return pfel;
			}
		}
		return prestatoreFEL;
	}

	private static boolean areEqual(PrestatoreFEL p1, PrestatoreFEL p2) {
		if(p1 == null && p2 == null) {
			return true;
		}
		if(p1 == null || p2 == null) {
			return false;
		}
		if(p1.equals(p2)) {
			return true;
		}
		
		EqualsBuilder eb = new EqualsBuilder();
		append(p1.getCapPrestatore(), p2.getCapPrestatore(), eb);
		append(p1.getCodicePaese(), p2.getCodicePaese(), eb);
		append(p1.getCodicePrestatore(), p2.getCodicePrestatore(), eb);
		append(p1.getCognomePrestatore(), p2.getCognomePrestatore(), eb);
		append(p1.getComunePrestatore(), p2.getComunePrestatore(), eb);
		append(p1.getDenominazionePrestatore(), p2.getDenominazionePrestatore(), eb);
		append(p1.getEmailPrestatore(), p2.getEmailPrestatore(), eb);
		append(p1.getFaxPrestatore(), p2.getFaxPrestatore(), eb);
		append(p1.getIndirizzoPrestatore(), p2.getIndirizzoPrestatore(), eb);
		append(p1.getNazionePrestatore(), p2.getNazionePrestatore(), eb);
		append(p1.getNomePrestatore(), p2.getNomePrestatore(), eb);
		append(p1.getNumeroCivicoPrestatore(), p2.getNumeroCivicoPrestatore(), eb);
		append(p1.getProvinciaAlboPrestatore(), p2.getProvinciaAlboPrestatore(), eb);
		append(p1.getProvinciaPrestatore(), p2.getProvinciaPrestatore(), eb);
		append(p1.getRegimeFiscale(), p2.getRegimeFiscale(), eb);
		append(p1.getTelefonoPrestatore(), p2.getTelefonoPrestatore(), eb);
		
		return eb.isEquals();
	}
	
	private static void append(Object rhs, Object lhs, EqualsBuilder equalsBuilder) {
		equalsBuilder.append(lhs, rhs);
	}
	private static void append(String rhs, String lhs, EqualsBuilder equalsBuilder) {
		equalsBuilder.append(StringUtils.upperCase(lhs), StringUtils.upperCase(rhs));
	}

}
