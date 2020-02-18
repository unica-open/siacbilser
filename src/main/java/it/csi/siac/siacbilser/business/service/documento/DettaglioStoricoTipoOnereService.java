/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoOnereDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


/**
 * The Class DettaglioStoricoTipoOnereService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DettaglioStoricoTipoOnereService extends CheckedAccountBaseService<DettaglioStoricoTipoOnere, DettaglioStoricoTipoOnereResponse> {
	
	/** The causale dad. */
	@Autowired
	private TipoOnereDad tipoOnereDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getTipoOnere(), "tipo onere");
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public DettaglioStoricoTipoOnereResponse executeService(DettaglioStoricoTipoOnere serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		String methodName = "execute";
		
		List<TipoOnere> tipiOnereStorico = tipoOnereDad.dettaglioStoricoTipoOnere(req.getTipoOnere());
		res.setTipiOnereStorico(tipiOnereStorico);
		
		log.debug(methodName, "Grouping by Tipo Onere");
		List<TipoOnere> tipiOnere = groupByTipoOnere(tipiOnereStorico);
		res.setTipiOnere(tipiOnere);
		
			
		log.debug(methodName, "Grouping by Causali Entrata");
		List<CausaleEntrata> causaliEntrata = groupByCausaliEntrata(tipiOnereStorico);
		res.setCausaliEntrata(causaliEntrata);
				
		log.debug(methodName, "Grouping by Causali Spesa");
		List<CausaleSpesa> causaliSpesa = groupByCausaliSpesa(tipiOnereStorico);
		res.setCausaliSpesa(causaliSpesa);
		
		log.debug(methodName, "Grouping by Soggetto");
		List<Soggetto> soggetti = groupBySoggetto(tipiOnereStorico);
		res.setSoggetti(soggetti);
		
		
	}

	

	private List<Soggetto> groupBySoggetto(List<TipoOnere> tipiOnereStorico) {
		final String methodName = "groupBySoggetto";
		
		Map<Integer, Soggetto> map = new HashMap<Integer, Soggetto>();
		
		List<TipoOnere> tipoOnereCloned = cloneTipoOnereStorico(tipiOnereStorico);
		ListIterator<TipoOnere> iterator = tipoOnereCloned.listIterator(tipoOnereCloned.size());
		
		String prevKey = null;
		
		while(iterator.hasPrevious()) {
			TipoOnere tipoOnere = iterator.previous();
			log.debug(methodName, "validita' inizio: "+tipoOnere.getDataInizioValidita() + " fine: " + tipoOnere.getDataFineValidita()); 
		
			String key = computeSoggettoKey(tipoOnere.getCausaliSpesa());
		
			if(prevKey == null || !prevKey.equals(key)) {
				
				//Popolo la mappa e imposto la data inizio validita per i soggetti appena inserite in mappa
				for(CausaleSpesa c : tipoOnere.getCausaliSpesa()) {
					if(c.getSoggetto() == null){
						continue;
					}
					Soggetto prev = map.put(c.getSoggetto().getUid(), c.getSoggetto());
					if(prev == null) {
						c.getSoggetto().setDataInizioValidita(tipoOnere.getDataInizioValidita());
						c.getSoggetto().setDataFineValidita(null);
						
						c.getSoggetto().setModalitaPagamentoList(c.getModalitaPagamentoSoggetto()!=null?Arrays.asList(c.getModalitaPagamentoSoggetto()):null);
						c.getSoggetto().setSediSecondarie(c.getSedeSecondariaSoggetto()!=null?Arrays.asList(c.getSedeSecondariaSoggetto()):null);
					}
				}
				
				//per le causali in mappa che non sono piu' presenti nella lista CausaliSpesa/CausaliEntrata imposto la dataFineValidita
				for(Soggetto sMap : map.values()) {
					if(sMap.getDataFineValidita()!=null){
						//la data fine validita' era gia' stata valorizzata
						continue;
					}
					boolean trovato = false;
					for(CausaleSpesa c : tipoOnere.getCausaliSpesa()){
						if(c.getSoggetto() == null){
							continue;
						}
						if(sMap.getUid() == c.getSoggetto().getUid()){
							trovato = true;
							break;
						}
					}
					if(!trovato){
						sMap.setDataFineValidita(tipoOnere.getDataInizioValidita());
					}
				}
				
			}
		
			prevKey = key;
		}
		
		return new ArrayList<Soggetto>(map.values());
	}
	
	

	private List<CausaleSpesa> groupByCausaliSpesa(List<TipoOnere> tipiOnereStorico) {
		return  groupByCausali(tipiOnereStorico, CausaleSpesa.class);
	}
	
	private List<CausaleEntrata> groupByCausaliEntrata(List<TipoOnere> tipiOnereStorico) {
		return  groupByCausali(tipiOnereStorico, CausaleEntrata.class);
	}	

	private <C extends Causale> List<C> groupByCausali(List<TipoOnere> tipiOnereStorico, Class<C> causaleClass) {
		final String methodName = "groupByCausali";
		
		Map<Integer, C> map = new HashMap<Integer, C>();
		
		List<TipoOnere> tipoOnereCloned = cloneTipoOnereStorico(tipiOnereStorico);
		ListIterator<TipoOnere> iterator = tipoOnereCloned.listIterator(tipoOnereCloned.size());
		
		String prevKey = null;
		
		while(iterator.hasPrevious()) {
			TipoOnere tipoOnere = iterator.previous();
			log.debug(methodName, "validita' inizio: "+tipoOnere.getDataInizioValidita() + " fine: " + tipoOnere.getDataFineValidita()); 
			
			String key = computeCodificaListKey(tipoOnere.getCausali(causaleClass));
			
			if(prevKey == null || !prevKey.equals(key)) {
				
				//Popolo la mappa e imposto la data inizio validita per le causali appena inserite in mappa
				for(C c : tipoOnere.getCausali(causaleClass)) {
					if(!isCausaleValorizzata(c)){
						continue;
					}
					C prev = map.put(c.getUid(), c);
					if(prev == null){
						c.setDataInizioValidita(tipoOnere.getDataInizioValidita());
						c.setDataFineValidita(null);
					}
				}
				
				//per le causali in mappa che non sono piu' presenti nella lista CausaliSpesa/CausaliEntrata imposto la dataFineValidita
				for(C cMap : map.values()) {
					if(cMap.getDataFineValidita()!=null){
						//la data fine validita' era gia' stata valorizzata
						continue;
					}
					boolean trovato = false;
					for(C c : tipoOnere.getCausali(causaleClass)){
						if(cMap.getUid() == c.getUid()){
							trovato = true;
							break;
						}
					}
					if(!trovato){
						cMap.setDataFineValidita(tipoOnere.getDataInizioValidita());
					}
				}
				
			}
			
			prevKey = key;
		}
		
		return new ArrayList<C>(map.values());
	}

	private <C extends Causale> boolean isCausaleValorizzata(C causale) {
		final String methodName = "isCausaleValorizzata";
		boolean result = false;
		boolean movimentoGestioneValorizzato;
		boolean subMovimentoGestioneValorizzato;
		boolean capitoloValorizzato;
		
		if(causale instanceof CausaleSpesa){
			CausaleSpesa cs = (CausaleSpesa)causale;
			movimentoGestioneValorizzato = (cs.getImpegno() != null && cs.getImpegno().getUid() != 0);
			subMovimentoGestioneValorizzato = (cs.getSubImpegno() != null && cs.getSubImpegno().getUid() != 0);
			capitoloValorizzato = (cs.getCapitoloUscitaGestione() != null && cs.getCapitoloUscitaGestione().getUid() != 0);
			
			result = capitoloValorizzato || movimentoGestioneValorizzato ||	subMovimentoGestioneValorizzato;
			log.debug(methodName, "capitoloValorizzato? " + capitoloValorizzato + "subMovimentoGestioneValorizzato? " + subMovimentoGestioneValorizzato + "subMovimentoGestioneValorizzato? " + subMovimentoGestioneValorizzato);
			
			
		} else if(causale instanceof CausaleEntrata) {
			CausaleEntrata ce = (CausaleEntrata)causale;
			movimentoGestioneValorizzato = (ce.getAccertamento() != null && ce.getAccertamento().getUid() != 0);
			subMovimentoGestioneValorizzato = (ce.getSubAccertamento() != null && ce.getSubAccertamento().getUid() != 0);
			capitoloValorizzato =(ce.getCapitoloEntrataGestione() != null && ce.getCapitoloEntrataGestione().getUid() != 0) ;
			boolean distintaValorizzata = ce.getDistinta() != null && ce.getDistinta().getUid() != 0;
			
			result = capitoloValorizzato ||	movimentoGestioneValorizzato || subMovimentoGestioneValorizzato || distintaValorizzata;
			log.debug(methodName, "capitoloValorizzato? " + capitoloValorizzato + "subMovimentoGestioneValorizzato? " + subMovimentoGestioneValorizzato + "subMovimentoGestioneValorizzato? " + subMovimentoGestioneValorizzato + "distintaValorizzata? " + distintaValorizzata);
		}
		//if(result) {
			log.debug(methodName, "isCausaleValorizzata? "+ result + " [uid:"+causale.getUid()+"]");
		//}
		return result;
	}

	private List<TipoOnere> groupByTipoOnere(List<TipoOnere> tipiOnereStorico) {
		String methodName = "groupByTipoOnere";
		List<TipoOnere> result = new ArrayList<TipoOnere>();
		
		
		List<TipoOnere> tipoOnereCloned = cloneTipoOnereStorico(tipiOnereStorico);
		ListIterator<TipoOnere> iterator = tipoOnereCloned.listIterator(tipoOnereCloned.size());
		
		String prevKey = null;
		TipoOnere prevTipoOnere = null;
		
		while(iterator.hasPrevious()) {
			TipoOnere tipoOnere = iterator.previous();
			log.debug(methodName, "validita' inizio: "+tipoOnere.getDataInizioValidita() + " fine: " + tipoOnere.getDataFineValidita()); 
			
			String key = computeKeyTipoOnere(tipoOnere);
			
			if(prevKey == null || !prevKey.equals(key)) {
				result.add(tipoOnere);
				tipoOnere.setDataFineValidita(null);
				
				if(prevTipoOnere!=null){
					prevTipoOnere.setDataFineValidita(tipoOnere.getDataInizioValidita());
				}
				prevTipoOnere = tipoOnere;
			}
			prevKey = key;
		}
		
		Collections.reverse(result);
		return result;
	}

	private List<TipoOnere> cloneTipoOnereStorico(List<TipoOnere> tipiOnereStorico) {
		@SuppressWarnings("unchecked")
		List<TipoOnere> tipoOnereStoricoCloned = (List<TipoOnere>) SerializationUtils.clone((Serializable) tipiOnereStorico);
		return tipoOnereStoricoCloned;
	}

	private String computeKeyTipoOnere(TipoOnere tipoOnere) {
		String methodName = "computeKeyTipoOnere";
		
		String causali770 = computeCodificaListKey(tipoOnere.getCausali770());
		String attivitaOnere = computeCodificaListKey(tipoOnere.getAttivitaOnere());
		
		String key = String.format("%f - %f - %s - %s - %s", tipoOnere.getAliquotaCaricoSoggettoNotNull(), 
				                                 tipoOnere.getAliquotaCaricoEnteNotNull(),
				                                 tipoOnere.getQuadro770(),
				                                 causali770,
				                                 attivitaOnere
				                                 );
		
		log.debug(methodName, "key: "+ key);
		return key;
	}
	
	private <C extends Codifica> String computeCodificaListKey(List<C> codifiche) {
		String methodName = "computeCodificaListKey";
		Map<String, String> codificheMap = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		for(C codifica : codifiche) {
			sb.setLength(0);
			if(codifica instanceof CausaleSpesa) {
				CausaleSpesa cs = (CausaleSpesa)codifica;

				String c = sb.append(cs.getCapitoloUscitaGestione() != null ? cs.getCapitoloUscitaGestione().getUid() : "null")
						.append(" ").append(cs.getImpegno() != null ? cs.getImpegno().getUid() : "null").append(" ")
						.append(cs.getSubImpegno() != null ? cs.getSubImpegno().getUid() : "null")
						.toString();

				codificheMap.put(c, c);
			} else if (codifica instanceof CausaleEntrata) {
				CausaleEntrata cs = (CausaleEntrata)codifica;

				String c = sb.append(cs.getCapitoloEntrataGestione() != null ? cs.getCapitoloEntrataGestione().getUid() : "null")
						.append(" ").append(cs.getAccertamento() != null ? cs.getAccertamento().getUid() : "null").append(" ")
						.append(cs.getSubAccertamento() != null ? cs.getSubAccertamento().getUid() : "null")
						.append(cs.getDistinta() != null ? cs.getDistinta().getUid() : "null")
						.toString();

				codificheMap.put(c, c);
			} else {
				codificheMap.put(codifica.getCodice(), codifica.getCodice());
			}
			
		}
		StringBuilder keyBuilder = new StringBuilder();
		List<String> values = new ArrayList<String> (codificheMap.values());
		Collections.sort(values);
		for(String value : values){
			keyBuilder.append(",").append(value);
		}
		
		String key = keyBuilder.toString();
		log.debug(methodName, "key: "+ key);
		return key;
	}
	
	private String computeSoggettoKey(List<CausaleSpesa> causaliSpesa) {
		String methodName = "computeSoggettoKey";
		Map<Integer, Integer> uidMap = new HashMap<Integer, Integer>();
		for (CausaleSpesa causaleSpesa : causaliSpesa) {
			if(causaleSpesa.getSoggetto() == null){
				continue;
			}
			uidMap.put(causaleSpesa.getSoggetto().getUid(), causaleSpesa.getSoggetto().getUid());
		}
		StringBuilder keyBuilder = new StringBuilder();
		List<Integer> values = new ArrayList<Integer> (uidMap.values());
		Collections.sort(values);
		for(Integer value : values){
			keyBuilder.append(",").append(value);
		}
		String key = keyBuilder.toString();
		log.debug(methodName, "key: "+ key);
		return key;
	}

}

