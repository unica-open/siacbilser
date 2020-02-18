/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatoIva;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatoIvaAcquisti;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaSpesaDad;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoStampa;

public abstract class StampaLiquidazioneIvaBaseAcquistiDataCompiler extends StampaLiquidazioneIvaBaseDataCompiler<SubdocumentoIvaSpesa> {
	
	@Autowired
	protected SubdocumentoIvaSpesaDad subdocumentoIvaSpesaDad;
	
	/**
	 * Popola la sezione 1 della pagina.
	 */
	@Override
	protected void popolaSezione1() {
		final String methodName = "popolaSezione1";
		/*
		 * La lista di elementi è composta dal numero dei Registri Iva con i corrispondenti codici Aliquota Iva utilizzati nell’anno di esercizio
		 * e nel periodo forniti in input al servizio per tutti i Registri Iva di tipo [tipoRegistroIva] del Gruppo selezionato.
		 * 
		 * Per ogni registro iva si riportano i corrispondenti valori di  codice Iva, % aliquota, Tipo operazione Iva e descrizione.
		 */
		List<StampaLiquidazioneIvaDatoIva> listaDatiIva = new ArrayList<StampaLiquidazioneIvaDatoIva>();
		Map<String, StampaLiquidazioneIvaDatoIvaAcquisti> mappaRighe = new HashMap<String, StampaLiquidazioneIvaDatoIvaAcquisti>();
		
		for(SubdocumentoIvaSpesa si : listaSubdocumentoIva) {
			log.debug(methodName, "Impostazione delle righe per il subdocumento iva " + si.getUid() + ". Numero delle aliquote per il subdocumento iva: " + si.getListaAliquotaSubdocumentoIva().size());
			
			// Costruisco la riga
			for(AliquotaSubdocumentoIva asi : si.getListaAliquotaSubdocumentoIva()) {
				final int uidAliquota = asi.getUid();
				
				RegistroIva ri = si.getRegistroIva();
				AliquotaIva ai = asi.getAliquotaIva();

				String keyRegistroAliquotaPeriodo = computeKeyRegistroAliquotaPeriodo(ri, ai, handler.getPeriodo());
				
				if(mappaRighe.containsKey(keyRegistroAliquotaPeriodo)){
					StampaLiquidazioneIvaDatoIvaAcquisti riga = mappaRighe.get(keyRegistroAliquotaPeriodo);
					riga.setImponibile(riga.getImponibile().add(asi.getImponibile()));
					riga.setImposta(riga.getImposta().add(asi.getImposta()));
					
				}else{
					StampaLiquidazioneIvaDatoIvaAcquisti riga = new StampaLiquidazioneIvaDatoIvaAcquisti();
					TipoStampa tipoStampa = mapTipoStampaRegistro.get(ri.getUid());
					riga.setTipoStampaRegistro(tipoStampa);
					
					// A1 - Codice - Registro Iva
					// B1 - Descrizione - Registro Iva
					riga.setRegistroIva(ri);
					log.debug(methodName, uidAliquota + " - A1 - registro - codice - " + ri.getCodice());
					log.debug(methodName, uidAliquota + " - B1 - registro - descrizione - " + ri.getDescrizione());
					log.debug(methodName, uidAliquota + " registro - tipo stampa gia' effettuata - " + tipoStampa);
					
					// C1 - codice - Aliquota Iva
					// D1 - percAliquota - Aliquota Iva
					// E1 - Tipo Operazione Iva (enum) - Aliquota Iva
					// F1 - descrizione - Aliquota Iva
					riga.setAliquotaSubdocumentoIva(asi);
					log.debug(methodName, uidAliquota + " - C1 - aliquotaIva - codice - " + asi.getAliquotaIva().getCodice());
					log.debug(methodName, uidAliquota + " - D1 - aliquotaIva - percAliquota - " + asi.getAliquotaIva().getPercentualeAliquota());
					log.debug(methodName, uidAliquota + " - E1 - aliquotaIva - tipoOperazioneIva - " + asi.getAliquotaIva().getTipoOperazioneIva());
					log.debug(methodName, uidAliquota + " - F1 - aliquotaIva - descrizione - " + asi.getAliquotaIva().getDescrizione());
					
					// G1 - Imponibile: è il valore dell’imponibile per lo specifico Registro Iva  per quel periodo e per quel codice aliquota.
					// Il calcolo è il seguente:
					// Per ogni Registro Iva trovato e per ogni codice Iva dello specifico periodo e anno di esercizio passati in input al servizio
					// si seleziona dall’entità “Progressivi Iva” il valore del campo “totImponibileDef”
					riga.setImponibile(asi.getImponibile());
					
					// H1 - IVA: è il valore dell’imposta da pagare per lo specifico Registro Iva  per quel periodo e per quel codice aliquota.
					// Il calcolo è il seguente:
					// Per ogni Registro Iva trovato e per ogni codice Iva dello specifico periodo e anno di esercizio passati in input al servizio
					// si seleziona dall’entità “Progressivi Iva” il valore del campo “totIvaDef”
//					BigDecimal imposta = obtainImpostaFromProgressiviIvaAndAliquotaIva(pi, ai);
					riga.setImposta(asi.getImposta());
					
					// Aggiungo la riga nella lista
					mappaRighe.put(keyRegistroAliquotaPeriodo, riga);
				}
				
			}
		}
		for(StampaLiquidazioneIvaDatoIvaAcquisti riga: mappaRighe.values()){
			if(!isRigaToSkip(riga.getImponibile(), riga.getImposta())){
				listaDatiIva.add(riga);
			}
		}
		pagina.setListaDatiIva(listaDatiIva);
	}
	
}
