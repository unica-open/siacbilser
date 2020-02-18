/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.cespiti.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommon.util.number.NumberUtils;
import it.csi.siac.siacintegser.business.service.util.DelimitedTextFileParser.LineMapper;

public class CespiteLineMapper implements LineMapper<Cespite>{

	private List<String> messages;
	private List<String> errors;
	
	@Override
	public Cespite mapValues(String[] values) {
		
		messages = new ArrayList<String>();
		errors = new ArrayList<String>();

		final int FIELDS_NUMBER = 14;
		
		if (values.length != FIELDS_NUMBER) {
			errors.add(String.format("Numero campi non coerente: %d", values.length));
			return null;
		}
		
		Cespite cespite = new Cespite();

		int i = 0;
		
		cespite.setCodice(values[i++]);
		cespite.setDescrizione(values[i++]);
		cespite.setTipoBeneCespite(new TipoBeneCespite());
		cespite.getTipoBeneCespite().setCodice(values[i++]);
		cespite.setClassificazioneGiuridicaCespite(ClassificazioneGiuridicaCespite.fromCodice(values[i++]));
		cespite.setFlagSoggettoTutelaBeniCulturali(toBoolean(StringUtils.upperCase(values[i++]), "SI", "NO"));
		//cespite.setNumeroInventario(values[i++]);
		cespite.setDataAccessoInventario(toDate(values[i++]));
		cespite.setDataCessazione(toDate(values[i++]));
		cespite.setValoreIniziale(NumberUtils.importoToBigDecimal(values[i++]));;
		cespite.setValoreAttuale(NumberUtils.importoToBigDecimal(values[i++]));
		cespite.setFlagStatoBene(toBoolean(values[i++], "1", "2"));
		cespite.setDescrizioneStato(values[i++]);
		cespite.setUbicazione(values[i++]);
		cespite.setNote(values[i++]);
		cespite.setFondoAmmortamento(NumberUtils.importoToBigDecimal(values[i++]));
		
		return cespite;
	}
	
	private Date toDate(String value) {
		
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		try {
			return DateUtils.parseDateStrictly(value,  new String[] { "dd/MM/yyyy" });
		}
		catch (ParseException e) {
			errors.add(String.format("'%s' data errata", value));
		}
		
		return null;
	}
	

	private Boolean toBoolean(String value, String trueValue, String falseValue) {

		try {
			return BooleanUtils.toBooleanObject(value, trueValue, falseValue, null);
		}
		catch (IllegalArgumentException e) {
			errors.add(String.format("'%s' valore errato: valori possibili %s e %s", value, trueValue, falseValue));
		}
		
		return null;
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}

	@Override
	public List<String> getErrors() {
		return errors;
	}
}
