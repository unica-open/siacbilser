/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovgestPkDto;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProgrammaAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAvanzovincoloFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTVincoloFin;
import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtils;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;


@Component
@Transactional
public class MovimentoGestioneFinDaoImpl extends AbstractDao<SiacTMovgestFin, Integer> implements MovimentoGestioneFinDao{

	
	@Autowired
	SiacTMovgestRepository siacTMovgestRepository;

	@Autowired
	SiacDMovgestStatoRepository siacDMovgestStatoRepository;
	
	@Autowired
	SiacDMovgestTipoRepository siacDMovgestTipoRepository;
	
	@Autowired
	SiacDMovgestTsDetTipoRepository siacDMovgestTsDetTipoRepository;
	
	@Autowired
	SiacDMovgestTsTipoRepository siacDMovgestTsTipoRepository;
	
	@Autowired
	SiacDProgrammaStatoRepository siacDProgrammaStatoRepository;
	
	@Autowired
	SiacRModificaStatoRepository siacRModificaStatoRepository;
	
	@Autowired
	SiacRMovgestBilElemRepository siacRMovgestBilElemRepository;
	
	@Autowired
	SiacRMovgestClassRepository siacRMovgestClassRepository;
	
	@Autowired
	SiacRMovgestOrdinativoRepository siacRMovgestOrdinativoRepository;
	
	@Autowired
	SiacRMovgestTipoClassTipRepository siacRMovgestTipoClassTipRepository;
	
	@Autowired
	SiacRMovgestTsAttoAmmRepository siacRMovgestTsAttoAmmRepository;
	
	@Autowired
	SiacRMovgestTsAttrRepository siacRMovgestTsAttrRepository;
	
	@Autowired
	SiacRMovgestTsProgrammaRepository siacRMovgestTsProgrammaRepository;
	
	@Autowired
	SiacRMovgestTsSogClasseModRepository siacRMovgestTsSogClasseModRepository;
	
	@Autowired
	SiacRMovgestTsSogClasseRepository siacRMovgestTsSogClasseRepository;
	
	@Autowired
	SiacRMovgestTsSogModRepository siacRMovgestTsSogModRepository;
	
	@Autowired
	SiacRMovgestTsSogRepository siacRMovgestTsSogRepository;
	
	@Autowired
	SiacRMovgestTsStatoRepository siacRMovgestTsStatoRepository;
	
	@Autowired
	SiacRProgrammaStatoRepository siacRProgrammaStatoRepository;
	
	@Autowired
	SiacTBilFinRepository siacTBilRepository;
	
	@Autowired
	SiacTMovgestTsDetModRepository siacTMovgestTsDetModRepository;
	
	@Autowired
	SiacTMovgestTsDetRepository siacTMovgestTsDetRepository;
	
	@Autowired
	SiacTProgrammaFinRepository siacTProgrammaRepository;
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di identificativi di SiacTMovgestFin vengono caricati tutti i SiacTMovgestFin
	 * referenziati dalle chiavi indicate 
	 */
	public List<SiacTMovgestFin> ricercaSiacTMovgestPkMassive(Integer enteUid, List<MovgestPkDto> chiavi) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestFin> listaRitorno = new ArrayList<SiacTMovgestFin>();
		
		if(enteUid!=null && chiavi!=null && chiavi.size()>0){
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT mg FROM SiacTMovgestFin mg WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND ");
			param.put("enteProprietarioId", enteUid);
			
			jpql.append(" mg.siacDMovgestTipo.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND ");
			jpql.append(" mg.siacTBil.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND ");
			jpql.append(" mg.siacTBil.siacTPeriodo.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND ");
			
			jpql.append(" ( ");
			int i =0;
			for(MovgestPkDto it: chiavi){
				
				
				if(i>0){
					jpql.append(" OR ");
				}
				
				String numeroMovimentoParamName = "numeroMovimento" + i;
				String annoMovimentoParamName = "annoMovimento" + i;
				String annoEsercizioParamName = "annoEsercizio" + i;
				String tipoTMovGestParamName = "tipoTMovGest" + i;
				
				jpql.append(" ( ");
				jpql.append(" mg.movgestNumero = :"+numeroMovimentoParamName+" AND ");
				jpql.append(" mg.movgestAnno = :"+annoMovimentoParamName+" AND ");
				jpql.append(" mg.siacTBil.siacTPeriodo.anno = :"+annoEsercizioParamName+" AND ");
				jpql.append(" mg.siacDMovgestTipo.movgestTipoCode = :"+tipoTMovGestParamName+" AND ");
				jpql.append(" mg.siacDMovgestTipo.dataFineValidita IS NULL AND ");
				jpql.append(" mg.siacTBil.dataFineValidita IS NULL AND ");
				jpql.append(" mg.siacTBil.siacTPeriodo.dataFineValidita IS NULL ");
				jpql.append(" ) ");
				
				
				param.put(numeroMovimentoParamName, it.getNumeroMovimento());
				param.put(annoMovimentoParamName, it.getAnnoMovimento());
				param.put(annoEsercizioParamName, it.getAnnoEsercizio());
				param.put(tipoTMovGestParamName, it.getTipoTMovGest());
				
				
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public <ST extends SiacTBase>  List<ST> ricercaByMovGestTsMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String nomeEntity){
		return ricercaByMovGestTsMassive(listaSiacTMovgestTs, nomeEntity, Boolean.TRUE);
	}
	
	
	public <ST extends SiacTBase>  List<ST> ricercaByMovGestTsMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String nomeEntity,Boolean validi) {
		String nomeProperty = "siacTMovgestT";//quasi tutti hanno questo nome
		return ricercaByMovGestTsMassive(listaSiacTMovgestTs, nomeEntity, nomeProperty, validi);
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTMovgestTsFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTMovgestTsFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacRMovgestTsSogFin" verranno restituiti tutti i distinti record SiacRMovgestTsSogFin
	 * in relazione con i record di SiacTMovgestTsFin indicati
	 */
	public <ST extends SiacTBase>  List<ST> ricercaByMovGestTsMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String nomeEntity,String nomeProperty, Boolean validi) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaSiacTMovgestTs, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaByMovGestTsMassiveCORE(listaIt, nomeEntity, nomeProperty, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private <ST extends SiacTBase>  List<ST> ricercaByMovGestTsMassiveCORE(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String nomeEntity,String nomeProperty, Boolean validi) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs."+nomeProperty+".movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				//validi filtrai on-query
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<Integer> getIdsSiacRLiquidazioneMovgestFinByMovGestTsMassive(List<SiacTMovgestTsFin> listaInput) {
		List<Integer> listaRitorno = new ArrayList<Integer>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<Integer> risultatoParziale = getIdsSiacRLiquidazioneMovgestFinByMovGestTsMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
			}
		}
        return listaRitorno;
	}
	
	private List<Integer> getIdsSiacRLiquidazioneMovgestFinByMovGestTsMassiveCORE(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<Integer> listaRitorno = new ArrayList<Integer>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs.liqMovgestId FROM SiacRLiquidazioneMovgestFin rs WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//solo validi
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	
	//PROGRAMMA:
	public List<SiacRMovgestTsProgrammaFin> ricercaSiacRMovgestTsProgrammaByProgrammaMassive(List<SiacTProgrammaFin> listaSiacTProgrammaFin, String tipoMovimento) {
		return ricercaSiacRMovgestTsProgrammaByProgrammaMassive(listaSiacTProgrammaFin, true, tipoMovimento);
	}

	
	public List<SiacRMovgestTsProgrammaFin> ricercaSiacRMovgestTsProgrammaByProgrammaMassive(List<SiacTProgrammaFin> listaSiacTProgrammaFin, Boolean validi, String tipoMovimento) {
		List<SiacRMovgestTsProgrammaFin> listaRitorno = new ArrayList<SiacRMovgestTsProgrammaFin>();
		if(listaSiacTProgrammaFin!=null && listaSiacTProgrammaFin.size()>0){
			List<List<SiacTProgrammaFin>> esploso = StringUtils.esplodiInListe(listaSiacTProgrammaFin, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTProgrammaFin> listaIt : esploso){
					List<SiacRMovgestTsProgrammaFin> risultatoParziale = ricercaSiacRMovgestTsProgrammaByProgrammaMassiveCORE(listaIt, validi,tipoMovimento);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRMovgestTsProgrammaFin> ricercaSiacRMovgestTsProgrammaByProgrammaMassiveCORE(List<SiacTProgrammaFin> listaSiacTProgrammaFin,Boolean validi, String tipoMovimento) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRMovgestTsProgrammaFin> listaRitorno = new ArrayList<SiacRMovgestTsProgrammaFin>();
		
		if(listaSiacTProgrammaFin!=null && listaSiacTProgrammaFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRMovgestTsProgrammaFin rs WHERE ");
			
			jpql.append(" rs.siacTProgramma.programmaId IN ( ");
			int i =0;
			for(SiacTProgrammaFin it: listaSiacTProgrammaFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getProgrammaId());
				i++;
			}
			jpql.append(" ) ");
			
			if(!StringUtils.isEmpty(tipoMovimento)){
				jpql.append(" AND rs.siacTMovgestT.siacTMovgest.siacDMovgestTipo.movgestTipoCode = :tipoMovimento ");
				param.put("tipoMovimento", tipoMovimento);
			}
			
			if(validi!=null && validi){
				//validi filtrai on-query
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	//END PROGRAMMA

	public <SCMS extends SiacConModificaStato> List<SiacRModificaStatoFin> ricercaBySiacConModificaStatoMassive(List<SCMS> listaInput, Boolean validi) {
		List<SiacRModificaStatoFin> listaRitorno = new ArrayList<SiacRModificaStatoFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SCMS>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SCMS> listaIt : esploso){
					List<SiacRModificaStatoFin> risultatoParziale = ricercaBySiacConModificaStatoMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	public List<SiacTMovgestTsDetModFin> ricercaEscludendoModificheAutomatiche(List<SiacTMovgestTsFin> listaInput) {
		List<SiacTMovgestTsDetModFin> listaRitorno = new ArrayList<SiacTMovgestTsDetModFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<SiacTMovgestTsDetModFin> risultatoParziale = ricercaEscludendoModificheAutomaticheCore(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private  List<SiacTMovgestTsDetModFin> ricercaEscludendoModificheAutomaticheCore(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestTsDetModFin> listaRitorno = new ArrayList<SiacTMovgestTsDetModFin>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTMovgestTsDetModFin rs "
					+ "  WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//MODIFICA STATO:
			jpql.append(" AND ( ");
				
				jpql.append(" rs.siacRModificaStato.siacTModifica.modDesc NOT IN (:modDescParolas)");
				
				// SIAC-5219
				Collection<String> modDescParolas = new ArrayList<String>();
				modDescParolas.add(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_ORDINATIVO);
				modDescParolas.add(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
				modDescParolas.add(Constanti.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
				
				param.put("modDescParolas", modDescParolas);
				
			jpql.append(" ) ");
			
			//
			
			//validi filtrati on-query
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato.siacTModifica"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	
//	private  List<SiacTMovgestTsDetModFin> ricercaModificheAutomaticheCore(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {
//		Map<String,Object> param = new HashMap<String, Object>();
//		List<SiacTMovgestTsDetModFin> listaRitorno = new ArrayList<SiacTMovgestTsDetModFin>();
//		
//		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
//			
//			StringBuilder jpql = new StringBuilder("SELECT DISTINCT mod FROM SiacTModificaFin mod "
//					+ "  WHERE  mod.dataCancellazione IS NULL and ");
//			
//			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
//			int i =0;
//			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
//				if(i>0){
//					jpql.append(" , ");
//				}
//				String idParamName = "id" + i;
//				jpql.append("  :"+idParamName+" ");
//				param.put(idParamName, it.getMovgestTsId());
//				i++;
//			}
//			jpql.append(" ) ");
//			
//			//MODIFICA STATO:
//			jpql.append(" AND ( ");
//				
//				jpql.append(" rs.siacRModificaStato.siacTModifica.modDesc != :parolaUno AND rs.siacRModificaStato.siacTModifica.modDesc != :parolaDue");
//				
//				TODOs: Aggiungere MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO
//				param.put("parolaUno", Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_ORDINATIVO);
//				param.put("parolaDue", Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
//				
//			jpql.append(" ) ");
//			//
//			
//			//validi filtrati on-query
//			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
//			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato"));
//			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato.siacTModifica"));
//			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
//			
//			//LANCIO DELLA QUERY:
//			Query query =  createQuery(jpql.toString(), param);
//			listaRitorno = query.getResultList();
//			
//		}
//		//Termino restituendo l'oggetto di ritorno: 
//        return listaRitorno;
//	}
	
	
	public List<SiacTMovgestTsDetModFin> ricercaSoloModificheAutomatiche(List<SiacTMovgestTsFin> listaInput) {
		List<SiacTMovgestTsDetModFin> listaRitorno = new ArrayList<SiacTMovgestTsDetModFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<SiacTMovgestTsDetModFin> risultatoParziale = ricercaSoloModificheAutomaticheCore(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private  List<SiacTMovgestTsDetModFin> ricercaSoloModificheAutomaticheCore(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestTsDetModFin> listaRitorno = new ArrayList<SiacTMovgestTsDetModFin>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTMovgestTsDetModFin rs "
					+ "  WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//MODIFICA STATO:
			jpql.append(" AND ( ");
				jpql.append(" rs.siacRModificaStato.siacTModifica.modDesc IN (:modDescParolas)");
				
				// SIAC-5219
				Collection<String> modDescParolas = new ArrayList<String>();
				modDescParolas.add(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_ORDINATIVO);
				modDescParolas.add(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
				modDescParolas.add(Constanti.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
				
				param.put("modDescParolas", modDescParolas);
				
			jpql.append(" ) ");
			
			//
			
			//validi filtrati on-query
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato.siacTModifica"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<Integer> ricercaSoloModificheAutomaticheIds(List<SiacTMovgestTsFin> listaInput) {
		List<Integer> listaRitorno = new ArrayList<Integer>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<Integer> risultatoParziale = ricercaSoloModificheAutomaticheIdsCore(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
			}
		}
        return listaRitorno;
	}
	
	private  List<Integer> ricercaSoloModificheAutomaticheIdsCore(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<Integer> listaRitorno = new ArrayList<Integer>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs.movgestTsDetModId FROM SiacTMovgestTsDetModFin rs "
					+ "  WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//MODIFICA STATO:
			jpql.append(" AND ( ");
				jpql.append(" rs.siacRModificaStato.siacTModifica.modDesc IN (:modDescParolas) ");
				
				// SIAC-5219
				Collection<String> modDescParolas = new ArrayList<String>();
				modDescParolas.add(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_ORDINATIVO);
				modDescParolas.add(Constanti.MODIFICA_CONTESTUALE_INSERIMENTO_MANUALE_ORDINATIVO);
				modDescParolas.add(Constanti.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
				
				param.put("modDescParolas", modDescParolas);
			jpql.append(" ) ");
			//
			
			//validi filtrati on-query
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato.siacTModifica"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<Integer> ricercaModificheImportoInStatoIds(List<SiacTMovgestTsFin> listaInput, String statoCode) {
		List<Integer> listaRitorno = new ArrayList<Integer>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<Integer> risultatoParziale = ricercaModificheImportoInStatoIdsCore(listaIt,statoCode);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
			}
		}
        return listaRitorno;
	}
	
	private  List<Integer> ricercaModificheImportoInStatoIdsCore(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String statoCode) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<Integer> listaRitorno = new ArrayList<Integer>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs.movgestTsDetModId FROM SiacTMovgestTsDetModFin rs "
					+ "  WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//STATO CODE:
			jpql.append(" AND ").append("rs.siacRModificaStato.siacDModificaStato.modStatoCode = :statoCode ");
			param.put("statoCode", statoCode);
			
			
			//validi filtrati on-query
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato.siacTModifica"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	
	public List<BigDecimal> ricercaImportiSiacTMovgestTsDetModFinByIDs(List<Integer> listaInput) {
		List<BigDecimal> listaRitorno = new ArrayList<BigDecimal>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<Integer>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<Integer> listaIt : esploso){
					List<BigDecimal> risultatoParziale = ricercaImportiSiacTMovgestTsDetModFinByIDsCore(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
			}
		}
        return listaRitorno;
	}
	
	private  List<BigDecimal> ricercaImportiSiacTMovgestTsDetModFinByIDsCore(List<Integer> listaIds) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<BigDecimal> listaRitorno = new ArrayList<BigDecimal>();
		
		if(listaIds!=null && listaIds.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT rs.movgestTsDetImporto FROM SiacTMovgestTsDetModFin rs "
					+ "  WHERE ");
			
			jpql.append(" rs.movgestTsDetModId IN ( ");
			int i =0;
			for(Integer it: listaIds){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it);
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	private <SCMS extends SiacConModificaStato> List<SiacRModificaStatoFin> ricercaBySiacConModificaStatoMassiveCORE(List<SCMS> listaSiacTMovgestTsDetModFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRModificaStatoFin> listaRitorno = new ArrayList<SiacRModificaStatoFin>();
		
		if(listaSiacTMovgestTsDetModFin!=null && listaSiacTMovgestTsDetModFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRModificaStatoFin rs WHERE ");
			
			jpql.append(" rs.modStatoRId IN ( ");
			int i =0;
			for(SCMS it: listaSiacTMovgestTsDetModFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacRModificaStato().getModStatoRId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	/*
	public List<SiacRModificaStatoFin> ricercaBySiacRMovgestTsSogModFinMassive(List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogModFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRModificaStatoFin> listaRitorno = new ArrayList<SiacRModificaStatoFin>();
		
		if(listaSiacRMovgestTsSogModFin!=null && listaSiacRMovgestTsSogModFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRModificaStatoFin rs WHERE ");
			
			jpql.append(" rs.modStatoRId IN ( ");
			int i =0;
			for(SiacRMovgestTsSogModFin it: listaSiacRMovgestTsSogModFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacRModificaStato().getModStatoRId());
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}*/
	
	public List<SiacRMovgestTsSogclasseModFin> ricercaSiacRMovgestTsSogclasseModFinBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaInput, Boolean validi) {
		List<SiacRMovgestTsSogclasseModFin> listaRitorno = new ArrayList<SiacRMovgestTsSogclasseModFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRModificaStatoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRModificaStatoFin> listaIt : esploso){
					List<SiacRMovgestTsSogclasseModFin> risultatoParziale = ricercaSiacRMovgestTsSogclasseModFinBySiacRModificaStatoFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRMovgestTsSogclasseModFin> ricercaSiacRMovgestTsSogclasseModFinBySiacRModificaStatoFinMassiveCORE(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRMovgestTsSogclasseModFin> listaRitorno = new ArrayList<SiacRMovgestTsSogclasseModFin>();
		
		if(listaSiacRModificaStatoFin!=null && listaSiacRModificaStatoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRMovgestTsSogclasseModFin rs WHERE ");
			
			jpql.append(" rs.siacRModificaStato.modStatoRId IN ( ");
			int i =0;
			for(SiacRModificaStatoFin it: listaSiacRModificaStatoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getModStatoRId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRMovgestTsSogModFin> ricercaSiacRMovgestTsSogModFinBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaInput, Boolean validi) {
		List<SiacRMovgestTsSogModFin> listaRitorno = new ArrayList<SiacRMovgestTsSogModFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRModificaStatoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRModificaStatoFin> listaIt : esploso){
					List<SiacRMovgestTsSogModFin> risultatoParziale = ricercaSiacRMovgestTsSogModFinBySiacRModificaStatoFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRMovgestTsSogModFin> ricercaSiacRMovgestTsSogModFinBySiacRModificaStatoFinMassiveCORE(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRMovgestTsSogModFin> listaRitorno = new ArrayList<SiacRMovgestTsSogModFin>();
		
		if(listaSiacRModificaStatoFin!=null && listaSiacRModificaStatoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRMovgestTsSogModFin rs WHERE ");
			
			jpql.append(" rs.siacRModificaStato.modStatoRId IN ( ");
			int i =0;
			for(SiacRModificaStatoFin it: listaSiacRModificaStatoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getModStatoRId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	public List<SiacRProgrammaAttrFin> ricercaSiacRProgrammaAttrFinBySiacRMovgestTsProgrammaFinMassive(List<SiacRMovgestTsProgrammaFin> listaInput, Boolean validi){
		List<SiacRProgrammaAttrFin> listaRitorno = new ArrayList<SiacRProgrammaAttrFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRMovgestTsProgrammaFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRMovgestTsProgrammaFin> listaIt : esploso){
					List<SiacRProgrammaAttrFin> risultatoParziale = ricercaSiacRProgrammaAttrFinBySiacRMovgestTsProgrammaFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRProgrammaAttrFin> ricercaSiacRProgrammaAttrFinBySiacRMovgestTsProgrammaFinMassiveCORE(List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgrammaFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRProgrammaAttrFin> listaRitorno = new ArrayList<SiacRProgrammaAttrFin>();
		
		if(listaSiacRMovgestTsProgrammaFin!=null && listaSiacRMovgestTsProgrammaFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRProgrammaAttrFin rs WHERE ");
			
			jpql.append(" rs.siacTProgramma.programmaId IN ( ");
			int i =0;
			for(SiacRMovgestTsProgrammaFin it: listaSiacRMovgestTsProgrammaFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTProgramma().getProgrammaId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	public List<SiacTMovgestTsDetModFin> ricercaSiacTMovgestTsDetModFinBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaInput, Boolean validi) {
		List<SiacTMovgestTsDetModFin> listaRitorno = new ArrayList<SiacTMovgestTsDetModFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRModificaStatoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRModificaStatoFin> listaIt : esploso){
					List<SiacTMovgestTsDetModFin> risultatoParziale = ricercaSiacTMovgestTsDetModFinBySiacRModificaStatoFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTMovgestTsDetModFin> ricercaSiacTMovgestTsDetModFinBySiacRModificaStatoFinMassiveCORE(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestTsDetModFin> listaRitorno = new ArrayList<SiacTMovgestTsDetModFin>();
		
		if(listaSiacRModificaStatoFin!=null && listaSiacRModificaStatoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTMovgestTsDetModFin rs WHERE ");
			
			jpql.append(" rs.siacRModificaStato.modStatoRId IN ( ");
			int i =0;
			for(SiacRModificaStatoFin it: listaSiacRModificaStatoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getModStatoRId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTModificaFin> ricercaBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaInput, Boolean validi) {
		List<SiacTModificaFin> listaRitorno = new ArrayList<SiacTModificaFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRModificaStatoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRModificaStatoFin> listaIt : esploso){
					List<SiacTModificaFin> risultatoParziale = ricercaBySiacRModificaStatoFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTModificaFin> ricercaBySiacRModificaStatoFinMassiveCORE(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTModificaFin> listaRitorno = new ArrayList<SiacTModificaFin>();
		
		if(listaSiacRModificaStatoFin!=null && listaSiacRModificaStatoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTModificaFin rs WHERE ");
			
			jpql.append(" rs.modId IN ( ");
			int i =0;
			for(SiacRModificaStatoFin it: listaSiacRModificaStatoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTModifica().getModId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRMovgestTsFin> ricercaSiacRMovgestTsFinBySiacTAvanzovincoloFinMassive(List<SiacTAvanzovincoloFin> listaInput, Boolean validi) {
		List<SiacRMovgestTsFin> listaRitorno = new ArrayList<SiacRMovgestTsFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTAvanzovincoloFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTAvanzovincoloFin> listaIt : esploso){
					List<SiacRMovgestTsFin> risultatoParziale = ricercaSiacRMovgestTsFinBySiacTAvanzovincoloFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRMovgestTsFin> ricercaSiacRMovgestTsFinBySiacTAvanzovincoloFinMassiveCORE(List<SiacTAvanzovincoloFin> listaInput, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRMovgestTsFin> listaRitorno = new ArrayList<SiacRMovgestTsFin>();
		
		if(listaInput!=null && listaInput.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRMovgestTsFin rs WHERE ");
			
			jpql.append(" rs.siacTAvanzovincoloFin.avavId IN ( ");
			int i =0;
			for(SiacTAvanzovincoloFin it: listaInput){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "idAvav" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getAvavId());
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRMovgestTsFin> ricercaBySiacTMovgestTsFinMassive(List<SiacTMovgestTsFin> listaInput, Boolean validi) {
		List<SiacRMovgestTsFin> listaRitorno = new ArrayList<SiacRMovgestTsFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<SiacRMovgestTsFin> risultatoParziale = ricercaBySiacTMovgestTsFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRMovgestTsFin> ricercaBySiacTMovgestTsFinMassiveCORE(List<SiacTMovgestTsFin> listaSiacTMovgestTsFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRMovgestTsFin> listaRitorno = new ArrayList<SiacRMovgestTsFin>();
		
		if(listaSiacTMovgestTsFin!=null && listaSiacTMovgestTsFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRMovgestTsFin rs WHERE ");
			
			jpql.append(" rs.siacTMovgestTsB.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTsFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "idB" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" OR rs.siacTMovgestTsA.movgestTsId IN ( ");
			int j =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTsFin){
				if(j>0){
					jpql.append(" , ");
				}
				String idParamName = "idA" + j;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				j++;
			}
			jpql.append(" ) ");
			
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	



	
	/*
	public List<SiacRModificaStatoFin> ricercaBySiacRMovgestTsSogModFinMassive(List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogModFin, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRModificaStatoFin> listaRitorno = new ArrayList<SiacRModificaStatoFin>();
		
		if(listaSiacRMovgestTsSogModFin!=null && listaSiacRMovgestTsSogModFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRModificaStatoFin rs WHERE ");
			
			jpql.append(" rs.modStatoRId IN ( ");
			int i =0;
			for(SiacRMovgestTsSogModFin it: listaSiacRMovgestTsSogModFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacRModificaStato().getModStatoRId());
				i++;
			}
			jpql.append(" ) ");
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}*/
	

	public List<SiacTMovgestTsFin> ricercaSiacTMovgestTsFinBySiacTMovgestMassive(List<Integer> listaInput, Boolean validi) {
		List<SiacTMovgestTsFin> listaRitorno = new ArrayList<SiacTMovgestTsFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<Integer>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<Integer> listaIt : esploso){
					List<SiacTMovgestTsFin> risultatoParziale = ricercaSiacTMovgestTsFinBySiacTMovgestMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTMovgestTsFin> ricercaSiacTMovgestTsFinBySiacTMovgestMassiveCORE(List<Integer> listaMovgestIds, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestTsFin> listaRitorno = new ArrayList<SiacTMovgestTsFin>();
		
		if(listaMovgestIds!=null && listaMovgestIds.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTMovgestTsFin rs WHERE ");
			
			jpql.append(" rs.siacTMovgest.movgestId IN ( ");
			int i =0;
			for(Integer it: listaMovgestIds){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it);
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTMovgestTsFin> ricercaSiacTMovgestTsFinMassive(List<Integer> listaInput, Boolean validi) {
		List<SiacTMovgestTsFin> listaRitorno = new ArrayList<SiacTMovgestTsFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<Integer>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<Integer> listaIt : esploso){
					List<SiacTMovgestTsFin> risultatoParziale = ricercaSiacTMovgestTsFinMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTMovgestTsFin> ricercaSiacTMovgestTsFinMassiveCORE(List<Integer> listaMovgestTsIds, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestTsFin> listaRitorno = new ArrayList<SiacTMovgestTsFin>();
		
		if(listaMovgestTsIds!=null && listaMovgestTsIds.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTMovgestTsFin rs WHERE ");
			
			jpql.append(" rs.movgestTsId IN ( ");
			int i =0;
			for(Integer it: listaMovgestTsIds){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it);
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTMovgestTsFin> ricercaSubCoinvoltiMassive(List<Integer> listaInput, Boolean validi) {
		List<SiacTMovgestTsFin> listaRitorno = new ArrayList<SiacTMovgestTsFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<Integer>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<Integer> listaIt : esploso){
					List<SiacTMovgestTsFin> risultatoParziale = ricercaSubCoinvoltiMassiveCORE(listaIt, validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTMovgestTsFin> ricercaSubCoinvoltiMassiveCORE(List<Integer> listaMovgestTsIds, Boolean validi){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestTsFin> listaRitorno = new ArrayList<SiacTMovgestTsFin>();
		
		if(listaMovgestTsIds!=null && listaMovgestTsIds.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTMovgestTsFin rs WHERE ");
			
			jpql.append(" rs.movgestTsIdPadre IN ( ");
			int i =0;
			for(Integer it: listaMovgestTsIds){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it);
				i++;
			}
			jpql.append(" ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	
	public List<SiacRMovgestBilElemFin> ricercaSiacRMovgestBilElemMassive(List<SiacTMovgestFin> listaInput) {
		List<SiacRMovgestBilElemFin> listaRitorno = new ArrayList<SiacRMovgestBilElemFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestFin> listaIt : esploso){
					List<SiacRMovgestBilElemFin> risultatoParziale = ricercaSiacRMovgestBilElemMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}

	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTMovgestFin vengono restituiti TUTTI I DISTINTI SiacRMovgestBilElemFin in relazione
	 * con i SiacTMovgestFin indicati.
	 */
	private List<SiacRMovgestBilElemFin> ricercaSiacRMovgestBilElemMassiveCORE(List<SiacTMovgestFin> listaSiacTMovgest) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRMovgestBilElemFin> listaRitorno = new ArrayList<SiacRMovgestBilElemFin>();
		
		if(listaSiacTMovgest!=null && listaSiacTMovgest.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRMovgestBilElemFin rs WHERE ");
			
			jpql.append(" rs.siacTMovgest.movgestId IN ( ");
			int i =0;
			for(SiacTMovgestFin it: listaSiacTMovgest){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMovgestId());
				i++;
			}
			jpql.append(" ) ");
			
			//SOLO VALIDI:
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			//
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRVincoloBilElemFin> ricercaSiacRVincoloBilElemFinMassive(List<SiacTBilElemFin> listaInput) {
		List<SiacRVincoloBilElemFin> listaRitorno = new ArrayList<SiacRVincoloBilElemFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTBilElemFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTBilElemFin> listaIt : esploso){
					List<SiacRVincoloBilElemFin> risultatoParziale = ricercaSiacRVincoloBilElemFinMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}

	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTBilElemFin vengono restituiti TUTTI I DISTINTI SiacRVincoloBilElemFin in relazione
	 * con i SiacTBilElemFin indicati.
	 */
	private List<SiacRVincoloBilElemFin> ricercaSiacRVincoloBilElemFinMassiveCORE(List<SiacTBilElemFin> listaInput) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRVincoloBilElemFin> listaRitorno = new ArrayList<SiacRVincoloBilElemFin>();
		
		if(listaInput!=null && listaInput.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRVincoloBilElemFin rs WHERE ");
			
			jpql.append(" rs.siacTBilElem.elemId IN ( ");
			int i =0;
			for(SiacTBilElemFin it: listaInput){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getElemId());
				i++;
			}
			jpql.append(" ) ");
			
			//SOLO VALIDI:
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			//
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRVincoloAttrFin> ricercaSiacRVincoloAttrFinMassive(List<SiacTVincoloFin> listaInput) {
		List<SiacRVincoloAttrFin> listaRitorno = new ArrayList<SiacRVincoloAttrFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTVincoloFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTVincoloFin> listaIt : esploso){
					List<SiacRVincoloAttrFin> risultatoParziale = ricercaSiacRVincoloAttrFinMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}

	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTVincoloFin vengono restituiti TUTTI I DISTINTI SiacRVincoloAttrFin in relazione
	 * con i SiacTVincoloFin indicati.
	 */
	private List<SiacRVincoloAttrFin> ricercaSiacRVincoloAttrFinMassiveCORE(List<SiacTVincoloFin> listaInput) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRVincoloAttrFin> listaRitorno = new ArrayList<SiacRVincoloAttrFin>();
		
		if(listaInput!=null && listaInput.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRVincoloAttrFin rs WHERE ");
			
			jpql.append(" rs.siacTVincolo.vincoloId IN ( ");
			int i =0;
			for(SiacTVincoloFin it: listaInput){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getVincoloId());
				i++;
			}
			jpql.append(" ) ");
			
			//SOLO VALIDI:
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			//
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	
	/**
	 * Metodo di caricamento "massivo" degli oggetti SiacTMovgestFin a partire dall'elenco di identificativi
	 */
	@SuppressWarnings("unchecked")
	public List<SiacTMovgestFin> ricercaSiacTMovgestPerIN(String clausolaIN){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestFin> listaMov = new ArrayList<SiacTMovgestFin>();
		
		if(!StringUtils.isEmpty(clausolaIN)){
			StringBuilder jpql = new StringBuilder("SELECT mvg ")
			.append("FROM SiacTMovgestFin mvg ")
			.append("WHERE mvg.movgestId IN ( ")
			.append(clausolaIN).append(" ) ")
			.append("order by mvg.movgestAnno, mvg.movgestNumero ");

			//Creo la query effettiva
			Query query =  createQuery(jpql.toString(),param);
			listaMov = query.getResultList();
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaMov;
	}
	
	
	public List<SiacTMovgestTsDetFin> findImportoMassive(Integer enteProprietarioId, List<Integer> listaInput, String tipoImporto) {
		List<SiacTMovgestTsDetFin> listaRitorno = new ArrayList<SiacTMovgestTsDetFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<Integer>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<Integer> listaIt : esploso){
					List<SiacTMovgestTsDetFin> risultatoParziale = findImportoMassiveCORE(enteProprietarioId, listaIt, tipoImporto);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	@SuppressWarnings("unchecked")
	private List<SiacTMovgestTsDetFin> findImportoMassiveCORE(Integer enteProprietarioId, List<Integer> listaId, String tipoImporto){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMovgestTsDetFin> listaImporti = new ArrayList<SiacTMovgestTsDetFin>();
		
		Timestamp  dataInput = getNow();
		
		String condizioneDet = " ( (det.dataInizioValidita < :dataInput)  AND (det.dataFineValidita IS NULL OR :dataInput < det.dataFineValidita) )";
		
		String clausolaIn = "";
		int i=0;
		for(Integer idIt : listaId){
			if(i>0){
				clausolaIn = clausolaIn + " , ";
			}
			
			String paramName = "idMovgestTs" + i;
			clausolaIn = clausolaIn + ":" + paramName;
			param.put(paramName, idIt);
			i++;
		}
		
//		String jpql = "SELECT det.siacTMovgestT.movgestTsId, det.movgestTsDetImporto "
				
		String jpql = "SELECT det "
				+ " FROM SiacTMovgestTsDetFin det, SiacDMovgestTsDetTipoFin tipo "+
	      "  WHERE det.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
	       "AND det.siacDMovgestTsDetTipo.movgestTsDetTipoId = tipo.movgestTsDetTipoId "+
	       "AND tipo.movgestTsDetTipoCode = :tipoImporto "+
	       "AND  det.siacTMovgestT.movgestTsId IN (" + clausolaIn + ") AND "+condizioneDet;
		
		param.put("dataInput", dataInput);
		param.put("tipoImporto", tipoImporto);
		param.put("enteProprietarioId", enteProprietarioId);
		

		//Creo la query effettiva
		Query query =  createQuery(jpql,param);
		listaImporti = query.getResultList();
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaImporti;
	}
	
	
	/**
	 * Dato l'id di un capitolo va a verificare il flag sui trasferimenti vincolati
	 * @param idCapitoloUscitaGest
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public boolean flagTrasferimentiVincolati(Integer idCapitoloUscitaGest) {

		boolean flagTrasferimentiVincolati = false;
		try{
			
			Query query = entityManager.createNativeQuery(selectFlag);
			query.setParameter(1, idCapitoloUscitaGest);

			List<Object[]> result = query.getResultList();
			if(result!=null && !result.isEmpty())
				flagTrasferimentiVincolati = mapResultToFlagTrasferimentiVincolati(result,flagTrasferimentiVincolati);
			else
				flagTrasferimentiVincolati = false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return flagTrasferimentiVincolati;
	}

	/**
	 * mappa il flag TrasferimentiVincolati
	 * @param result
	 * @param flagTrasferimentiVincolati
	 * @return
	 */
	private boolean mapResultToFlagTrasferimentiVincolati(List<Object[]> result, Boolean flagTrasferimentiVincolati){
	
		if(!result.isEmpty()){
			
			for (int i = 0; i < result.size();) {
				
				String flag = String.valueOf(((Character)result.get(i)[8]));
				if(flag!=null && Constanti.TRUE.equalsIgnoreCase(flag)){
					flagTrasferimentiVincolati = Boolean.TRUE;
		    	}
	    		//deve avere un solo elemento, quindi break
	    		break;
			}
		}
		
		return flagTrasferimentiVincolati;
	}
	

	
	public SiacTMovgestFin findAccertamento(Integer enteProprietarioId, Integer anno,BigDecimal numero,String bilancio,Timestamp  dataInput){
		return findByEnteAnnoNumeroBilancioValido(enteProprietarioId, anno, numero, Constanti.MOVGEST_TIPO_ACCERTAMENTO, bilancio, dataInput);
	}
	
	public SiacTMovgestFin findImpegno(Integer enteProprietarioId, Integer anno,BigDecimal numero,String bilancio,Timestamp  dataInput){
		return findByEnteAnnoNumeroBilancioValido(enteProprietarioId, anno, numero, Constanti.MOVGEST_TIPO_IMPEGNO, bilancio, dataInput);
	}
	
	/**
	 * Wrapper di comodo per l'omonimo metodo nel repository
	 * ritorna null se non trova nulla
	 * @param enteProprietarioId
	 * @param anno
	 * @param numero
	 * @param tipoMovimento
	 * @param bilancio
	 * @param dataInput
	 * @return
	 */
	public SiacTMovgestFin findByEnteAnnoNumeroBilancioValido(Integer enteProprietarioId, Integer anno,BigDecimal numero, String tipoMovimento,String bilancio,Timestamp  dataInput){
		SiacTMovgestFin siacTMovgest = null;
		if(dataInput==null){
			dataInput = getNow();
		}
		List<SiacTMovgestFin> siacTMovgestS = siacTMovgestRepository.findByEnteAnnoNumeroBilancioValido(enteProprietarioId, anno , numero, tipoMovimento, bilancio, dataInput);
		if(siacTMovgestS!=null && siacTMovgestS.size()>0){
			siacTMovgest = CommonUtils.getFirst(siacTMovgestS);
		}
		return siacTMovgest;
	}

	private static String selectFlag = "select " + 
			" e.elem_id,e.elem_code,e.elem_code2,e.elem_desc,e.elem_desc2, " +
			" v.vincolo_id, v.vincolo_code,v.vincolo_desc, rva.\"boolean\" " +
			" from siac.siac_r_vincolo_bil_elem rve, siac.siac_t_vincolo v, siac.siac_t_bil_elem e, " +
			" siac.siac_r_vincolo_attr rva, " +
			" siac.siac_t_attr a " +
			" where rve.vincolo_id=v.vincolo_id " +
			" and e.elem_id=rve.elem_id " +
			" and rva.vincolo_id=v.vincolo_id " +
			" and a.attr_id=rva.attr_id " +
			" and rve.data_cancellazione is null " +
			" and v.data_cancellazione is null " +
			" and e.data_cancellazione is null " +
			" and rva.data_cancellazione is null " +
			" and a.data_cancellazione is null " +
			" and " +
			" date_trunc('day', now()) between date_trunc('day', rve.validita_inizio) " +
			" and coalesce(rve.validita_fine, date_trunc('day', now())) and rve.validita_fine " +
			" is null " +
			" and " +
			" date_trunc('day', now()) between date_trunc('day', v.validita_inizio) " +
			" and coalesce(v.validita_fine, date_trunc('day', now())) and v.validita_fine is " +
			" null " +
			" and " +
			" date_trunc('day', now()) between date_trunc('day', e.validita_inizio) " +
			" and coalesce(e.validita_fine, date_trunc('day', now())) and e.validita_fine is " +
			" null " +
			" and " +
			" date_trunc('day', now()) between date_trunc('day', rva.validita_inizio) " +
			" and coalesce(rva.validita_fine, date_trunc('day', now())) and rva.validita_fine " +
			" is null " +
			" and " +
			" date_trunc('day', now()) between date_trunc('day', a.validita_inizio) " +
			" and coalesce(a.validita_fine, date_trunc('day', now())) and a.validita_fine is " +
			" null " +
			" and a.attr_code='FlagTrasferimentiVincolati' " +
			" and rva.\"boolean\"='S' " +
			" and e.elem_id=?1";
	
	
	
	@Override
	public BigDecimal calcolaDisponibilita(Integer uid, String functionName) {
		final String methodName = "calcolaDisponibilita";
		Query query = entityManager.createNativeQuery("SELECT " + functionName + "(:uid)");
		query.setParameter("uid", uid);
		
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.debug(methodName, "Returning result: "+ result + " for uid: "+ uid + " and functionName: "+ functionName);
		return result;
	}
	
	@Override
	public BigDecimal calcolaDisponibilitaAvanzoVincolo(Integer avavId) {
		final String methodName = "calcolaDisponibilita";
		
		BigDecimal result = BigDecimal.ZERO;
		
		String sqlQuery = "with tot_vincolato as " +
		" ( " +
		" select a.avav_id , COALESCE( sum(a.movgest_ts_importo), 0) as importo " +
		" from siac_r_movgest_ts a, siac_t_avanzovincolo b, " +
		" siac_t_movgest_ts c, siac_r_movgest_ts_stato d, " +
		" siac_d_movgest_stato e " +
		" where " +
		" a.avav_id = :avavId " +
		" and a.avav_id=b.avav_id " +
		" and a.movgest_ts_b_id=c.movgest_ts_id " +
		" and c.movgest_ts_id=d.movgest_ts_id " +
		" and d.movgest_stato_id=e.movgest_stato_id " +
		" and e.movgest_stato_code <> 'A' " +
		" and a.data_cancellazione is null " +
		" and a.validita_fine is null " +
		" and d.data_cancellazione is null " +
		" and d.validita_fine is null " +
		" and c.data_cancellazione is null " +
		" and c.validita_fine is null " +
		" group by a.avav_id " +
		" ), " +
		" massimale as " +
		" (select x.avav_id, x.avav_importo_massimale as importo from siac_t_avanzovincolo x " +
		"  where x.avav_id= :avavId " +
		" ) " +
		" select m.importo - COALESCE(v.importo, 0) as disponibile from massimale  m " +
		" left join  tot_vincolato v  on ( m.avav_id = v.avav_id ) " ;
		
		
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("avavId", avavId);
		
		try{
			result = (BigDecimal) query.getSingleResult();
		}catch (Throwable t){
			result = BigDecimal.ZERO;
		}
		
		log.debug(methodName, "Returning result: "+ result + " for avavId: "+ avavId);
		return result;
	}
	
	
	
	/**
	 * nomeTabellaModifiche puo' essere:
	 * 
	 * "SiacTMovgestTsDetModFin"
	 * "SiacRMovgestTsSogModFin"
	 * "SiacRMovgestTsSogclasseModFin"
	 * 
	 * 
	 * @param listaInput
	 * @param nomeTabellaModifiche
	 * @return
	 */
	public List<Integer> ricercaNumeroModificheByMovgestTs(List<SiacTMovgestTsFin> listaInput, String nomeTabellaModifiche) {
		List<Integer> listaRitorno = new ArrayList<Integer>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<Integer> risultatoParziale = ricercaNumeroModificheByMovgestTsCore(listaIt,nomeTabellaModifiche);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
			}
		}
        return listaRitorno;
	}
	
	public  List<Integer> ricercaNumeroModificheByMovgestTsCore(List<SiacTMovgestTsFin> listaSiacTMovgestTs,String nomeTabellaModifiche) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<Integer> listaRitorno = new ArrayList<Integer>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT max(rs.siacRModificaStato.siacTModifica.modNum) FROM "+nomeTabellaModifiche+" rs "
					+ "  WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//validi filtrati on-query
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato.siacTModifica"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
		}
		
        return listaRitorno;
	}
	
	
	
	public  Integer ricercaMaxValueNumeroModificaByMovgestTs(List<SiacTMovgestTsFin> listaSiacTMovgestTs,String nomeTabellaModifiche) {
		Map<String,Object> param = new HashMap<String, Object>();
		Integer maxValue = 0;
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT max(rs.siacRModificaStato.siacTModifica.modNum) FROM "+nomeTabellaModifiche+" rs "
					+ "  WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append("  :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			//validi filtrati on-query
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato"));
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacRModificaStato.siacTModifica"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			maxValue = (Integer)query.getSingleResult();
			
		}
		
        return maxValue;
	}
	
	
	/**
	 * Richiama la fucntion per il riaccertamento dei vincoli 
	 */
	@Override
	public List<String> gestisciRelazioneModificaImportoEVincoli(
			Integer idModifica, String loginOperazione, String tipoOperazione) {
		final String methodName = "gestisciRelazioneModificaImportoEVincoli";
		
		log.info(methodName, "Calling functionName: siac.fnc_siac_riaccertamento for modificaId: "+ idModifica 
				+", loginOperazione:"+loginOperazione
				+", tipoOperazione:"+tipoOperazione);
		
		String sql = "SELECT * FROM fnc_siac_riaccertamento(:idModifica, :loginOperazione, :tipoOperazione )";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("idModifica", idModifica);
		query.setParameter("loginOperazione", loginOperazione);
		query.setParameter("tipoOperazione", tipoOperazione);
		
		@SuppressWarnings("unchecked")
		List<String> result = query.getResultList();
		log.debug(methodName, "returning result: "+result);
		
		return result;		
	}

}