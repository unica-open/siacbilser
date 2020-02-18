/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

public class InserisceElencoConDocumentiConQuoteServiceTest extends BaseJunit4TestCase{
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	
	@Test
	public void checkProvvisorioCassa(){
		final String methodName = "checkProvvisorioCassa";
		Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
		
		List<Integer> listaUid = new ArrayList<Integer>();
		listaUid.add(new Integer(5959));
		listaUid.add(new Integer(5959));
		listaUid.add(new Integer(5959));
		listaUid.add(new Integer(5959));
		listaUid.add(new Integer(5959));
		listaUid.add(new Integer(5959));
		listaUid.add(new Integer(5959));
		listaUid.add(new Integer(5959));
		
		List<BigDecimal> listaImporto = new ArrayList<BigDecimal>();
		listaImporto.add(new BigDecimal(10));
		listaImporto.add(new BigDecimal(12));
		listaImporto.add(new BigDecimal(17));
		listaImporto.add(new BigDecimal(12));
		listaImporto.add(new BigDecimal(15));
		listaImporto.add(new BigDecimal(1));
		listaImporto.add(new BigDecimal(1));
		listaImporto.add(new BigDecimal(1));
		
		
		int i = 0;
		for(Integer subdoc : listaUid) {
			if(!map.containsKey(subdoc)){
				map.put(subdoc, BigDecimal.ZERO);
			}
			map.put(subdoc, map.get(subdoc).add(listaImporto.get(i)));
			i++;
			log.info(methodName, map.get(subdoc) + " " + map.entrySet());
		}
		
		log.info(methodName, map.entrySet());
		
		
		for(Map.Entry<Integer, BigDecimal> entry : map.entrySet()){
			ProvvisorioDiCassa provv = new ProvvisorioDiCassa();
			provv.setUid(entry.getKey());
			BigDecimal importoDaRegolarizzareProvvisorio = provvisorioBilDad.calcolaImportoDaRegolarizzareProvvisorio(provv);
			if(importoDaRegolarizzareProvvisorio.compareTo(map.get(entry.getKey())) < 0){
				throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere minore o uguale all'importo da regolarizzare sul provvisorio di cassa backend"));
			}
		}
	}
}
