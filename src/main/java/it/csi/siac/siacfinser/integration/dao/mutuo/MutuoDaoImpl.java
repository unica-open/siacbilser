/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaMutuoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtils;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;

@Component
@Transactional
public class MutuoDaoImpl extends AbstractDao<SiacTMutuoFin, Integer> implements MutuoDao {

	@Autowired
	SiacTMutuoRepository siacTMutuoRepository;

	/**
	 * E' il metodo di "engine" di ricerca dei mutui.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override 
	public Query creaQueryRicercaMutui(Integer enteUid, RicercaMutuoParamDto prm, boolean soloCount){
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		Query query = null;
		// Parametri di input ricevuti dal servizio :
		String numeroRegistrazioneMutuo = prm.getNumeroRegistrazioneMutuo();
		
		String codiceMutuo = prm.getNumeroMutuo();
//		String codiceMutuo = "";
		if(codiceMutuo==null){
			codiceMutuo = "";
		}
		if(StringUtils.isEmpty(numeroRegistrazioneMutuo)){
			numeroRegistrazioneMutuo = null;
		}
		
		Integer uidStrutturaAmmProvvedimento = prm.getStrutturaAmministrativoContabileDelProvvedimento();
		Integer uidProvvedimento = prm.getUidProvvedimento();
		
		String codiceIsitutoMutuante = "";
		if(null!=prm.getCodiceIsitutoMutuante()){
			codiceIsitutoMutuante = prm.getCodiceIsitutoMutuante();
		}
			
		
		Integer annoProvvedimento = prm.getAnnoProvvedimento();
		Integer numeroProvvedimento = prm.getNumeroProvvedimento();
		String codiceTipoProvvedimento = null;
		if(!"null".equals(prm.getCodiceTipoProvvedimento()) && !StringUtils.isEmpty(prm.getCodiceTipoProvvedimento())){
			codiceTipoProvvedimento = prm.getCodiceTipoProvvedimento();
		}
		
		String str = "";
		if(soloCount==true){
			str = "Select count(*) FROM SiacTMutuoFin mutuo";
		} else {
			str = "Select mutuo FROM SiacTMutuoFin mutuo";
		}

		StringBuilder jpql = new StringBuilder(str);
		
		if(!StringUtils.isEmpty(codiceIsitutoMutuante)){
			jpql.append(", SiacRMutuoSoggettoFin rMutuoSoggetto");
		}

		if((null!=annoProvvedimento && annoProvvedimento!=0  && null!=numeroProvvedimento && numeroProvvedimento!=0) || null!=codiceTipoProvvedimento){
			jpql.append(", SiacRMutuoAttoAmmFin rMutuoAttoAmm");
		}
		
		// struttura amministrativa
		if(uidStrutturaAmmProvvedimento!=null){
			jpql.append(" left join rMutuoAttoAmm.siacTAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}
		
		jpql.append(" WHERE mutuo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId");
		param.put("enteProprietarioId", enteUid);
		
		if(!StringUtils.isEmpty(codiceMutuo)){
			jpql.append(" AND mutuo.mutCode = :codiceMutuo");
			param.put("codiceMutuo", codiceMutuo);
		}			
		
		if(null!=numeroRegistrazioneMutuo){
			jpql.append(" AND mutuo.mutNumRegistrazione = :numeroRegistrazioneMutuo");
			param.put("numeroRegistrazioneMutuo", numeroRegistrazioneMutuo);	
		}
		
		if(!StringUtils.isEmpty(codiceIsitutoMutuante)){
			jpql.append(" AND rMutuoSoggetto.siacTMutuo.mutId = mutuo.mutId");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMutuoSoggetto"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND rMutuoSoggetto.siacTSoggetto.soggettoCode = :codiceIsitutoMutuante");
			param.put("codiceIsitutoMutuante", codiceIsitutoMutuante); 
		} 
		
		if((null!=annoProvvedimento && annoProvvedimento!=0) || (null!=numeroProvvedimento && numeroProvvedimento!=0) || null!=codiceTipoProvvedimento
				|| uidProvvedimento != null){
			jpql.append(" AND rMutuoAttoAmm.siacTMutuo.mutId = mutuo.mutId");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMutuoAttoAmm"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
			
			if(uidProvvedimento!=null){
				//avendo l'uidProvvedimento vado diretto solo sui mutui con il preciso provvedimento richiesto:
				jpql.append(" AND rMutuoAttoAmm.siacTAttoAmm.attoammAnno.attoammId = :uidProvvedimento");
				param.put("uidProvvedimento", uidProvvedimento);
			} else {
				//questi filtri hanno senso solo in mutua esclusione con l'uidProvvedimento
				//perche' se ho gia' l'uidProvvedimento diventano superflui
				
				if(null!=annoProvvedimento && annoProvvedimento!=0){
					jpql.append(" AND rMutuoAttoAmm.siacTAttoAmm.attoammAnno = :annoProvvedimento");
					param.put("annoProvvedimento", annoProvvedimento.toString());
				}

				if(null!=numeroProvvedimento && numeroProvvedimento!=0){
					jpql.append(" AND rMutuoAttoAmm.siacTAttoAmm.attoammNumero = :numeroProvvedimento");
					param.put("numeroProvvedimento", numeroProvvedimento);
				}

				if(!StringUtils.isEmpty(codiceTipoProvvedimento)) {
					jpql.append(" AND rMutuoAttoAmm.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId = :codiceTipoProvvedimento");
					param.put("codiceTipoProvvedimento", Integer.valueOf(codiceTipoProvvedimento));
				}
				
				if(uidStrutturaAmmProvvedimento!=null){
					jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento ");
					param.put("uidStrutturaAmmProvvedimento", uidStrutturaAmmProvvedimento);
				}
				
			}
			
		}

		if(soloCount==false)
			jpql.append(" order by to_number(mutuo.mutCode, '999999999999') ");
			

		query = createQuery(jpql.toString(), param);			

		//Termino restituendo l'oggetto di ritorno: 
        return query;
	}

	/**
	 * Wrapper di creaQueryRicercaMutui per avere un'anteprima del numero atteso di risultati
	 */
	@Override
	public Long contaMutui(Integer enteUid, RicercaMutuoParamDto prm){
		Long conteggioMutui = new Long(0);
		Query query = creaQueryRicercaMutui(enteUid, prm, true);
		conteggioMutui = (Long)query.getSingleResult();
		//Termino restituendo l'oggetto di ritorno: 
        return conteggioMutui;
	}
	
	/**
	 * Wrapper di creaQueryRicercaMutui per ottenere tutti i dati completi per la ricerca indicata
	 */
	@Override
	public List<SiacTMutuoFin> ricercaMutui(Integer enteUid, RicercaMutuoParamDto prm, int numeroPagina, int numeroRisultatiPerPagina){
		List<SiacTMutuoFin> lista = new ArrayList<SiacTMutuoFin>();
		Query query = creaQueryRicercaMutui(enteUid, prm, false);

		if(numeroPagina == 0 || numeroRisultatiPerPagina == 0){ 
			lista = query.getResultList();
		} else {
			int start = (numeroPagina - 1) * numeroRisultatiPerPagina;
			int end = start + numeroRisultatiPerPagina;
			
			if (end > query.getResultList().size())
				end = query.getResultList().size();
			
			lista = query.getResultList().subList(start, end);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return lista;
	}
	
	/**
	 * Carica un SiacTMutuoFin per chiave
	 */
	public SiacTMutuoFin ricercaMutuo(Integer codiceEnte, String numeroMutuo, Timestamp now) {
		SiacTMutuoFin mutuo = null;
		mutuo = siacTMutuoRepository.findMutuoByCode(codiceEnte, numeroMutuo);
		//Termino restituendo l'oggetto di ritorno: 
        return mutuo;
	}
	
	public List<SiacTMutuoVoceFin> ricercaSiacTMutuoVoceFinBySiacTMutuoFinMassive(List<SiacTMutuoFin> listaInput, Boolean validi) {
		List<SiacTMutuoVoceFin> listaRitorno = new ArrayList<SiacTMutuoVoceFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMutuoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMutuoFin> listaIt : esploso){
					List<SiacTMutuoVoceFin> risultatoParziale = ricercaSiacTMutuoVoceFinBySiacTMutuoFinMassiveCORE(listaIt, validi);
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
	
	private List<SiacTMutuoVoceFin> ricercaSiacTMutuoVoceFinBySiacTMutuoFinMassiveCORE(List<SiacTMutuoFin> listaSiacTMutuoFin, Boolean validi) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTMutuoVoceFin> listaRitorno = new ArrayList<SiacTMutuoVoceFin>();
		
		if(listaSiacTMutuoFin!=null && listaSiacTMutuoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTMutuoVoceFin rs WHERE ");
			
			jpql.append(" rs.siacTMutuo.mutId IN ( ");
			int i =0;
			for(SiacTMutuoFin it: listaSiacTMutuoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMutId());
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
	
	
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTMutuoFinMassive(List<SiacTMutuoFin> listaInput, String nomeEntity, Boolean validi) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMutuoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMutuoFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaBySiacTMutuoFinMassiveCORE(listaIt,nomeEntity, validi);
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
	 * Dato in input un elenco di SiacTMutuoFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTMutuoFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacRMutuoStatoFin" verranno restituiti tutti i distinti record SiacRMutuoStatoFin
	 * in relazione con i record di SiacTMutuoFin indicati
	 */
	private <ST extends SiacTBase>  List<ST> ricercaBySiacTMutuoFinMassiveCORE(List<SiacTMutuoFin> listaSiacTMutuoFin, String nomeEntity, Boolean validi) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTMutuoFin!=null && listaSiacTMutuoFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTMutuo.mutId IN ( ");
			int i =0;
			for(SiacTMutuoFin it: listaSiacTMutuoFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMutId());
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
	
	public <ST extends SiacTBase>  List<ST> ricercaByMutuoVoceMassive(List<SiacTMutuoVoceFin> listaInput, String nomeEntity, Boolean validi) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMutuoVoceFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMutuoVoceFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaByMutuoVoceMassiveCORE(listaIt,nomeEntity, validi);
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
	 * Dato in input un elenco di SiacTMutuoVoceFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTMutuoVoceFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacRMutuoVoceMovgestFin" verranno restituiti tutti i distinti record SiacRMutuoVoceMovgestFin
	 * in relazione con i record di SiacTMutuoVoceFin indicati
	 */
	private <ST extends SiacTBase>  List<ST> ricercaByMutuoVoceMassiveCORE(List<SiacTMutuoVoceFin> listaSiacTMutuoVoceFin, String nomeEntity, Boolean validi) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTMutuoVoceFin!=null && listaSiacTMutuoVoceFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTMutuoVoce.mutVoceId IN ( ");
			int i =0;
			for(SiacTMutuoVoceFin it: listaSiacTMutuoVoceFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMutVoceId());
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
	
}