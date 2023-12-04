/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.factory;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.stipendi.model.Stipendio;
import it.csi.siac.siacintegser.business.service.stipendi.model.TipoRecord;
import it.csi.siac.siacintegser.business.service.stipendi.model.TipoVoce;

/**
 * A factory for creating Stipendio objects.
 * 
 * @author Nazha Ahmad
 * @author Marchino Alessandro
 * @version 1.0.1 - 24/11/2015 - SIAC-2638
 */
@Component
public class StipendioFactory {

	/**
	 * New instance from flusso stipendi.
	 *
	 * @param line
	 *            the line
	 * @return the stipendio
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	public Stipendio newInstanceFromFlussoStipendi(String line) {
		Stipendio s = new Stipendio();
		s.setFileLineContent(line);

		// SIAC-2638: non devo effettuare il trim degli zeri
		s.setVoceContabile(line.substring(10, 10 + 4));
		
		//Tipo Voce
		s.setTipoVoce(TipoVoce.fromString(line.substring(14, 14 + 1)));
		//Mese ed anno Elaborazione
		s.setMeseElaborazione(parseInt("mese elaborazione", line.substring(15, 15 + 2)));
		s.setAnnoElaborazione(parseInt("anno elaborazione", line.substring(17, 17 + 4)));
		//Imporo spesa o entrata
		s.setImportoSpesa(parseCurrency("importo spesa", line.substring(22, 22 + 17)));
		s.setImportoEntrata(parseCurrency("importo entrata", line.substring(39, 39 + 17)));
		//Anno bilancio
		Integer annoBilancio = parseInt("anno bilancio", line.substring(56, 56 + 4));
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(annoBilancio);
		s.setBilancio(bilancio);
		s.setAnnoDiRiferimento(annoBilancio);
		//Ente
		String codiceEnte = line.substring(60, 60 + 4);
		Ente ente = new Ente();
		ente.setCodice(codiceEnte);
		s.setEnte(ente);
		
		//CAPITOLO
		Capitolo<?, ?> capitolo;
		if (s.isSpesa()) {
			capitolo = new CapitoloUscitaGestione();
		} else {
			capitolo = new CapitoloEntrataGestione();
		}

		capitolo.setAnnoCapitolo(s.getBilancio().getAnno());
		capitolo.setNumeroCapitolo(parseInt("numero capitolo", replaceZero(line.substring(65, 65 + 6))));
		capitolo.setNumeroArticolo(parseInt("numero articolo", replaceZero(line.substring(71, 71 + 3))));
		s.setCapitolo(capitolo);
		
		s.initCapitoloInput();
		
		//Strutt. Amm. Contabile
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setCodice(replaceZero(line.substring(77, 77 + 3)));
		s.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);

		//Impegno
		Impegno impegno = new Impegno();
		impegno.setAnnoMovimento(parseInt("anno movimento", line.substring(153, 153 + 4)));
		impegno.setNumeroBigDecimal(new BigDecimal(parseInt("anno movimento", line.substring(157, 157 + 6))));
		s.setImpegno(impegno);

		//SubImpegno
		SubImpegno subImpegno = new SubImpegno();
		subImpegno.setAnnoMovimento(parseInt("anno movimento", line.substring(153, 153 + 4)));
		subImpegno.setNumeroBigDecimal(new BigDecimal(parseInt("anno movimento", line.substring(163, 163 + 3))));
		s.setSubImpegno(subImpegno);

		//Accertamento
		Accertamento accertamento = new Accertamento();
		accertamento.setAnnoMovimento(parseInt("anno movimento entrata", line.substring(166, 166 + 4)));
		accertamento.setNumeroBigDecimal(new BigDecimal(parseInt("anno movimento entrata", line.substring(170, 170 + 6))));
		s.setAccertamento(accertamento);

		//SubAcertamento
		SubAccertamento subAccertamento = new SubAccertamento();
		subAccertamento.setAnnoMovimento(parseInt("anno movimento entrata", line.substring(166, 166 + 4)));
		subAccertamento.setNumeroBigDecimal(new BigDecimal(parseInt("anno movimento entrata", line.substring(176, 176 + 3))));
		s.setSubAccertamento(subAccertamento);
		
		//Soggetto
		Soggetto soggetto = new Soggetto();
		String codiceSoggetto = line.substring(179, 179 + 6);
		soggetto.setCodiceSoggetto(replaceZero(codiceSoggetto));
		s.setSoggetto(soggetto);

		//Siope
		SiopeSpesa siopeSpesa = new SiopeSpesa();
		siopeSpesa.setCodice(line.substring(185, 185 + 4));
		s.setSiopeSpesa(siopeSpesa);

		s.impostaFlagTipo();
		s.setTipoRecord(TipoRecord.getTipoRecordfromStipendio(s));
		return s;

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
	private static Integer parseInt(String nomeParamentro, String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Il parameto " + nomeParamentro + " non e' un intero! valore del parametro: " + str + ".", nfe);
		}
	}

	/**
	 * Parses the currency.
	 *
	 * @param nomeParamentro
	 *            the nome paramentro
	 * @param str
	 *            the str
	 * @return the big decimal
	 */
	private static BigDecimal parseCurrency(String nomeParamentro, String str) {
		try {
			return new BigDecimal(str).divide(BigDecimal.valueOf(100));
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Il parameto " + nomeParamentro + " non e' un importo! valore del parametro: " + str + ".", nfe);
		}
	}


	/**
	 * rimuove gli zeri iniziali in una stringa 
	 * es  input---> 000codice    output ---> codice
	 * @param value
	 * @return la stringa senza zeri iniziali
	 */
	private String replaceZero(String value) {
		return value.replaceFirst("^0+(?!$)", "");
	}
	
}
