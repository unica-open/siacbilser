/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.tefa.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.tefa.SiacTTefaTribImporti;
import it.csi.siac.siaccommon.util.fileparser.DelimitedTextFileParserExt.LineMapper;

public class TefaLineMapper implements LineMapper<SiacTTefaTribImporti>{

	private List<String> messages;
	private List<String> errors;
	
	@Override
	public SiacTTefaTribImporti mapValues(String[] values) {
		
		messages = new ArrayList<String>();
		errors = new ArrayList<String>();

//		final int FIELDS_NUMBER = 14;
//		
		if (values.length < 1) {
			errors.add("Riga vuota");
			return null;
		}
		
		SiacTTefaTribImporti x = new SiacTTefaTribImporti();
		
		

		int i = 0;
		
		if (! "D".equals(values[i])) {
			return null;
		}

		x.setTipoRecord(values[i++]);
		x.setDataRipart(values[i++]);
		x.setProgrRipart(values[i++]);
		x.setProvinciaCode(values[i++]);
		x.setEnteCode(values[i++]);
		x.setDataBonifico(values[i++]);
		x.setProgrTrasm(values[i++]);
		x.setProgrDelega(values[i++]);
		x.setProgrModello(values[i++]);
		x.setTipoModello(values[i++]);
		x.setComuneCode(values[i++]);
		x.setTributoCode(values[i++]);
		x.setValuta(values[i++]);
		x.setImportoVersatoDeb(new BigDecimal(values[i++]).divide(new BigDecimal(100)));
		x.setImportoCompensatoCred(new BigDecimal(values[i++]).divide(new BigDecimal(100)));
		x.setNumeroImmobili(values[i++]);
		x.setRateazione(values[i++]);
		x.setAnnoRif(values[i++]);
		
		
		return x;
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
