/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.consultazioneentita.FunctionEntitaConsultabile;
import it.csi.siac.siacbilser.business.service.consultazioneentita.FunctionEntitaConsultabileParam;
import it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter.FunctionParamsAdapter;
import it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter.FunctionParamsAdapterFactory;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Data access delegate di un EntitaConsultabile.
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class EntitaConsultabileDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private SimpleJDBCFunctionInvoker fi;
	
	private Ente ente;

	/**
	 * Ricerca sintetica Entita Consultabile
	 * 
	 * @param pr
	 * @param parametriPaginazione
	 * @return entita consultabili
	 */
	public ListaPaginata<EntitaConsultabile> ricercaSinteticaEntitaConsultabile(ParametriRicercaEntitaConsultabile pr, ParametriPaginazione parametriPaginazione) {
		
		FunctionEntitaConsultabile function = FunctionEntitaConsultabile.byParametriRicercaEntitaConsultabileClass(pr.getClass());
		
		FunctionParamsAdapter<ParametriRicercaEntitaConsultabile> functionParamsAdapter = FunctionParamsAdapterFactory.newInstance(function);
		Object[] functionParams = functionParamsAdapter.toFunctionParamsArray(pr, ente);
		
		ListaPaginata<EntitaConsultabile> records = executeConsultazioneFunction(
				function,
				parametriPaginazione,
				functionParams
				);
		return records;
	}
	
	
	/**
	 * Rcerca Figli Entita Consultabile
	 * 
	 * @param entitaPadre
	 * @param tipoEntiataConsultabileDaCercare
	 * @param list 
	 * @param parametriPaginazione
	 * @return entita consultabili
	 */
	public ListaPaginata<EntitaConsultabile> ricercaFigliEntitaConsultabile(
			EntitaConsultabile entitaPadre,
			TipoEntitaConsultabile tipoEntiataConsultabileDaCercare,
			Integer annoEsercizio,
			List<Object> listaParametriGenerici,
			ParametriPaginazione parametriPaginazione) {
		FunctionEntitaConsultabile function = FunctionEntitaConsultabile.byTipoEntitaConsultabileDiPartenzaEDaCercare(entitaPadre.getTipoEntitaConsultabile(), tipoEntiataConsultabileDaCercare);
		Map<FunctionEntitaConsultabileParam, Object> params = computeSQLParams(entitaPadre, annoEsercizio, listaParametriGenerici);
		
		ListaPaginata<EntitaConsultabile> records = executeConsultazioneFunction(
				function, 
				parametriPaginazione,
				function.getParams(params)
				);
		return records;
	}


	private Map<FunctionEntitaConsultabileParam, Object> computeSQLParams(EntitaConsultabile entitaPadre,
			Integer annoEsercizio, List<Object> listaParametriGenerici) {
		Map<FunctionEntitaConsultabileParam, Object> params = new HashMap<FunctionEntitaConsultabileParam, Object>();

		addSQLParamValueToMap(params, FunctionEntitaConsultabileParam.UID_PADRE, Integer.valueOf(entitaPadre.getUid()));
		addSQLParamValueToMap(params, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO, annoEsercizio);
		// Ulteriori parametri
		
		// SIAC-6193: parametri generici
		addSQLParamGenericiToMap(params, listaParametriGenerici);
		return params;
	}
	
	private void addSQLParamValueToMap(Map<FunctionEntitaConsultabileParam, Object> params, FunctionEntitaConsultabileParam param, Object value) {
		params.put(param, param.convert(value));
	}
	
	/**
	 * Aggiunge i parametri generici alla mappa
	 * @param params i parametri
	 * @param listaParametriGenerici la lista dei parametri generici
	 */
	private void addSQLParamGenericiToMap(Map<FunctionEntitaConsultabileParam, Object> params, List<Object> listaParametriGenerici) {
		Pattern p = Pattern.compile("FILTRO_GENERICO_(\\d+)");
		for(FunctionEntitaConsultabileParam param : FunctionEntitaConsultabileParam.values()) {
			Matcher matcher = p.matcher(param.name());
			if(matcher.matches() && matcher.groupCount() > 0) {
				String groupContent = matcher.group(1);
				Object value = retrieveValueForParametroGenerico(listaParametriGenerici, Integer.parseInt(groupContent), param.getType().getDefaultValue());
				params.put(param, value);
			}
		}
	}
	
	/**
	 * Recupera il valore del parametro generico per il dato indice
	 * @param listaParametriGenerici la lista dei parametri
	 * @param index l'indice
	 * @param defaultValue il valore di default
	 * @return il valore del parametro al dato indice
	 */
	private Object retrieveValueForParametroGenerico(List<Object> listaParametriGenerici, int index, Object defaultValue) {
		if(listaParametriGenerici == null || listaParametriGenerici.size() <= index) {
			return defaultValue;
		}
		return listaParametriGenerici.get(index);
	}
	
	/**
	 * Calcola l'importo dei Figli Entita Consultabile
	 * 
	 * @param entitaPadre
	 * @param tipoEntiataConsultabileDaCercare
	 * @param list 
	 * @return il totale
	 */
	public BigDecimal calcolaImportoFigliEntitaConsultabile(
			EntitaConsultabile entitaPadre,
			TipoEntitaConsultabile tipoEntiataConsultabileDaCercare,
			Integer annoEsercizio,
			List<Object> listaParametriGenerici) {
		final String methodName = "calcolaTotaleFigliEntitaConsultabile";
		FunctionEntitaConsultabile function = FunctionEntitaConsultabile.byTipoEntitaConsultabileDiPartenzaEDaCercare(entitaPadre.getTipoEntitaConsultabile(), tipoEntiataConsultabileDaCercare);
		if(!function.isTotaleImporti()) {
			return null;
		}
		Map<FunctionEntitaConsultabileParam, Object> params = computeSQLParams(entitaPadre, annoEsercizio, listaParametriGenerici);
		
		log.info(methodName, "Invoco la function "+function.getFunctionNameImporto());
		BigDecimal total = fi.invokeFunctionSingleResult(function.getFunctionNameImporto(), BigDecimal.class, function.getParams(params));
		return total;
	}
	
	private ListaPaginata<EntitaConsultabile> executeConsultazioneFunction(FunctionEntitaConsultabile functionEntitaConsultabile, ParametriPaginazione pp, Object... functionParams) {
		final String methodName = "executeConsultazioneFunction";
		
		log.info(methodName, "Invoco la function "+functionEntitaConsultabile.getFunctionName());
		List<Map<String, Object>> recordsMap = fi.invokeFunctionToMap(functionEntitaConsultabile.getFunctionName(), ArrayUtils.addAll(functionParams, pp.getElementiPerPagina(), pp.getNumeroPagina()));
		
		log.info(methodName, "Invoco la function per ottenere il totale elementi "+functionEntitaConsultabile.getFunctionNameTotal());
		Long total = fi.invokeFunctionSingleResult(functionEntitaConsultabile.getFunctionNameTotal(), Long.class, functionParams);
		log.info(methodName, "Totale elementi: " + total);
		
		ListaPaginata<EntitaConsultabile> result = mapToListaPaginata(recordsMap, total, pp, functionEntitaConsultabile.getTipoEntitaConsultabileDaCercare());
		return result;
	}

	private ListaPaginata<EntitaConsultabile> mapToListaPaginata(List<Map<String, Object>> recordsMap, 
																Long total, 
																ParametriPaginazione pp, 
																TipoEntitaConsultabile tipoEntitaConsultabileDaCercare) {
		final String methodName = "toListaPaginata";
		
		ListaPaginataImpl<EntitaConsultabile> result = new ListaPaginataImpl<EntitaConsultabile>();
		
		for (Map<String,Object> recordMap : recordsMap) {
			EntitaConsultabile ec = new EntitaConsultabile();
			ec.setCampi(recordMap);
			ec.setTipoEntitaConsultabile(tipoEntitaConsultabileDaCercare);
			
			result.add(ec);
		}
		result.setTotaleElementi(total.intValue());
		result.setTotalePagine((pp.getElementiPerPagina()==0)? 0 : (int)Math.ceil((double)result.getTotaleElementi()/(double)pp.getElementiPerPagina()));
		result.setPaginaCorrente(pp.getNumeroPagina());
		
		log.debug(methodName, "Totale elementi: "+ result.getTotaleElementi()
			 + " Totale pagine: "+ result.getTotalePagine()
			 + " Pagina corrente: "+ result.getPaginaCorrente()
			 + " Elementi per pagina: "+ pp.getElementiPerPagina()
			 );
		
		return result;
	}
	
	
	
	
	/*############################# VERSIONE JPA - INIZIO ################################
	 
	@Autowired
	private SimpleJPAFunctionInvoker functionInvoker;
	
	
	private ListaPaginata<EntitaConsultabile> executeConsultazioneFunction(FunctionEntitaConsultabile functionEntitaConsultabile, ParametriPaginazione pp, Object... functionParams) {
		final String methodName = "executeConsultazioneFunction";
		
		List<String> columns = getColumns(functionEntitaConsultabile.getFunctionNameColumnDesc());
		
		log.debug(methodName, "Invoco la function "+functionEntitaConsultabile.getFunctionName());
		List<Object[]> records = functionInvoker.invokeFunctionWithResultList(functionEntitaConsultabile.getFunctionName(), ArrayUtils.addAll(functionParams, pp.getElementiPerPagina(), pp.getNumeroPagina()));
		
		log.debug(methodName, "Invoco la function per ottenere il totale elementi "+functionEntitaConsultabile.getFunctionNameTotal());
		Long total = functionInvoker.invokeFunctionWithSingleResult(functionEntitaConsultabile.getFunctionNameTotal(), functionParams);
		log.debug(methodName, "Totale elementi" + total);
		
		ListaPaginata<EntitaConsultabile> result = mapToListaPaginata(columns, records, total, pp, functionEntitaConsultabile.getTipoEntitaConsultabileDaCercare());
		return result;
	}

	private List<String> getColumns(String columnDescFunctionName) {
		final String methodName = "getColumns";
		
		Object[] columnsObjs = functionInvoker.invokeFunctionWithSingleResult(columnDescFunctionName, 
										ente.getUid() //TODO valutare se eliminare l'ente come parametro di questa function! 
										              ////tanto le colonne di ritorno della function vera e propria (quella senza suffisso desc) non possono essere dinamiche.
										);
		
		log.debug(methodName, "Colonne ottenute: "+columnsObjs);
		
		List<String> columns = new ArrayList<String>();
		for (int i = 0; i < columnsObjs.length; i++) {
			columns.add((String)columnsObjs[i]);
		}
		
		return columns;
	}
	
	private ListaPaginata<EntitaConsultabile> mapToListaPaginata(List<String> columns, 
																List<Object[]> records, 
																Long total, 
																ParametriPaginazione pp, 
																TipoEntitaConsultabile tipoEntitaConsultabileDaCercare) {
		final String methodName = "toListaPaginata";
		
		ListaPaginataImpl<EntitaConsultabile> result = new ListaPaginataImpl<EntitaConsultabile>();
		
		for (Object[] record : records) {
			int i = 0;
			Map<String,Object> m = new HashMap<String,Object>();
			for (Object columnValue: record) {
				String key = columns.get(i);
				i++;
				m.put(key, columnValue);
				//log.debug(methodName, key+ ": "+columnValue + "("+(columnValue!=null?columnValue.getClass():"ND")+") ");
			}
			
			EntitaConsultabile ec = new EntitaConsultabile();
			ec.setCampi(m);
			ec.setTipoEntitaConsultabile(tipoEntitaConsultabileDaCercare);
			
			result.add(ec);
		}
		result.setTotaleElementi(total.intValue());
		result.setTotalePagine((pp.getElementiPerPagina()==0)? 0 : (int)Math.ceil((double)result.getTotaleElementi()/(double)pp.getElementiPerPagina()));
		result.setPaginaCorrente(pp.getNumeroPagina());
		
		log.debug(methodName, "Totale elementi: "+ result.getTotaleElementi()
			 + "Totale pagine: "+ result.getTotalePagine()
			 + "Pagina corrente: "+ result.getPaginaCorrente()
			 + "Elementi per pagina: "+ pp.getElementiPerPagina()
			 );
		
		return result;
	}
	*/

	
	/*############################# VERSIONE JPA - FINE ################################*/


	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	
}
