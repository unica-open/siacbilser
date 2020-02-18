/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.SerializationUtils;

import it.csi.siac.siaccommon.util.number.NumberToText;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public final class CommonUtils {
	
	
	public final static BigDecimal sommaImportiAttualiNonAnnullati(List<SubAccertamento> elencoTuttiSub){
		BigDecimal somma = BigDecimal.ZERO;
		for(SubAccertamento itSub : elencoTuttiSub){
			if(itSub!=null && !Constanti.MOVGEST_STATO_ANNULLATO.equals(itSub.getStatoOperativoMovimentoGestioneEntrata())){
				somma = somma.add(itSub.getImportoAttuale());
			}
		}
		return somma;
	}
	
	public final static boolean maggioreDiZero(Number numero){
		return numero!=null && numero.intValue()>0;
	}
	
	/**
	 * Metodo che permette di effettuare la copia di un oggetto, cambiando
	 * l'allocazione di memoria dell'oggetto copiato
	 * 
	 * @param o
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T o) {
		return (T) SerializationUtils.deserialize(SerializationUtils.serialize(o));
	}
	
	
	public final static <T extends SiacTBase> boolean isValidoSiacTBase(T obj, Timestamp ts){
		@SuppressWarnings("unchecked")
		T validato = getValidoSiacTBase(toList(obj), ts);
		boolean valido = false;
		if(validato!=null){
			valido = true;
		}
		return valido;
	}
	
	public final static <T extends SiacTBase> T getValidoSiacTBase(List<T> lista, Timestamp ts){
		T valido = null;
		List<T> validi = soloValidiSiacTBase(lista, ts);
		if(validi!=null && validi.size()>0){
			valido = validi.get(0);
		}
		return valido;
	}
	
	/**
	 * data una lista di oggetti che estendono SiacTBase, restituisce solo quelli validi alla data indicata
	 * @param lista
	 * @param ts
	 * @return
	 */
	public final static <T extends SiacTBase> List<T> soloValidiSiacTBase(List<T> lista, Timestamp ts){
		List<T> validi = new ArrayList<T>();
		if(lista!=null && lista.size()>0){
			if(ts==null){
				ts = new Timestamp(System.currentTimeMillis());
			}
			for(T it : lista){
				Timestamp dataFineValidita = TimingUtils.convertiDataInTimeStamp(it.getDataFineValidita());
				//Timestamp dataInizioValidita = TimingUtils.convertiDataInTimeStamp(it.getDataInizioValidita());
//				if(ts.after(dataInizioValidita) && (dataFineValidita==null || dataFineValidita.after(ts))){
//					validi.add(it);
//				}
				if((dataFineValidita==null || dataFineValidita.after(ts))){
					validi.add(it);
				}
			}
		}
		return validi;
	}
	
	/**
	 * data una lista di oggetti che estendono Entita, restituisce solo quelli validi alla data indicata
	 * @param lista
	 * @param ts
	 * @return
	 */
	public final static <T extends Entita> List<T> soloValidi(List<T> lista, Timestamp ts){
		List<T> validi = new ArrayList<T>();
		if(lista!=null && lista.size()>0){
			for(T it : lista){
				if(ts==null){
					ts = new Timestamp(System.currentTimeMillis());
				}
				Timestamp dataFineValidita = TimingUtils.convertiDataInTimeStamp(it.getDataFineValidita());
				//Timestamp dataInizioValidita = TimingUtils.convertiDataInTimeStamp(it.getDataInizioValidita());
//				if(ts.after(dataInizioValidita) && (dataFineValidita==null || dataFineValidita.after(ts))){
//					validi.add(it);
//				}
				if((dataFineValidita==null || dataFineValidita.after(ts))){
					validi.add(it);
				}
			}
		}
		return validi;
	}
	
	/**
	 * Data un parametro lista di oggetti che estendono Entita e data una lista di id
	 * restituisce una lista composta dai soli elementi di tale lista che trovano riscontro
	 * tra gli id indicati
	 * @param lista
	 * @param ids
	 * @return
	 */
	public final static <T extends Entita> List<T> filtraByIds(List<T> lista, List<Integer> ids){
		List<T> listaFiltrati = null;
		if(lista!=null && lista.size()>0 && ids!=null && ids.size()>0){
			listaFiltrati = new ArrayList<T>();
			for(Integer idIt : ids){
				T trovato = getById(lista, idIt);
				if(trovato!=null){
					listaFiltrati.add(trovato);
				}
			}
		}
		return listaFiltrati;
	}
	
	/**
	 * 
	 * @param listaA
	 * @param listaB
	 * 
	 * Solo gli elementi contenuti in entrambi.
	 * 
	 * listaA comanda l'ordine con cui devono comparire.
	 * 
	 * @return
	 */
	public final static List<Integer> soloQuelliInEntrambe(List<Integer> listaA, List<Integer> listaB){
		List<Integer> listaFiltrati = new ArrayList<Integer>();
		if(StringUtils.isEmpty(listaA) || StringUtils.isEmpty(listaB)){
			//se una delle due liste e' vuota non esistono elementi contenute in entrambe
			return listaFiltrati;
		}
		for(Integer itA : listaA){
			if(itA!=null && !listaFiltrati.contains(itA)){
				for(Integer itB : listaB){
					if(itB!=null && !listaFiltrati.contains(itB)){
						if(itB.intValue()==itA.intValue()){
							listaFiltrati.add(itB);
						}
					}
				}
			}
		}
		return listaFiltrati;
	}
	
	/**
	 * Data una lista di oggetti che estendono Entita e dato un id
	 * restituisce l'elemento in tale lista che ha l'id indicato
	 * 
	 * Da usare quando si assume che in tale lista ci sia un solo elemento con tale id,
	 * oppure quando si e' interessati a prendere il primo elemento in lista con tale id
	 * 
	 * @param lista
	 * @param id
	 * @return
	 */
	public final static <T extends Entita> T getById(List<T> lista, Integer id){
		return getFirst(soloConIdIndicato(lista, id));
	}
	
	/**
	 * 
	 * Data una lista di oggetti che estendono Entita e dato un id
	 * restituisce un'altra lista composta dai soli elementi che hanno l'id indicato
	 * 
	 * @param lista
	 * @param id
	 * @return
	 */
	public final static <T extends Entita> List<T> soloConIdIndicato(List<T> lista, Integer id){
		List<T> listaRitorno = null;
		if(id!=null && lista!=null && lista.size()>0){
			listaRitorno = new ArrayList<T>();
			for(T iterato : lista){
				if(iterato!=null && id!=null && iterato.getUid()==id.intValue()){
					listaRitorno.add(clone(iterato));
				}
			}	
		}
		return listaRitorno;
	}
	
	public final static <T extends Entita> List<T> soloConIdZero(List<T> lista){
		return soloConIdIndicato(lista, 0);
	}
	
	public final static <T extends Entita> List<T> rimuoviConIdZero(List<T> lista){
		return removeById(lista, 0);
	}
	
	public final static <T extends Entita> List<T> removeById(List<T> lista, Integer id){
		List<T> listaRitorno = null;
		if(id!=null && lista!=null && lista.size()>0){
			listaRitorno = new ArrayList<T>();
			for(T iterato : lista){
				if(iterato!=null && iterato.getUid()!=id.intValue()){
					listaRitorno.add(clone(iterato));
				}
			}	
		}
		return listaRitorno;
	}
	
	public final static <T extends SiacTBase> T getByIdSiacTBase(List<T> lista, Integer id){
		if(lista!=null && lista.size()>0){
			for(T iterato : lista){
				if(iterato!=null && id!=null && iterato.getUid()==id.intValue()){
					return iterato;
				}
			}	
		}
		return null;
	}
	
	public final static <T extends SiacTBase> T getByIdSiacTBaseBil(List<T> lista, Integer id){
		if(lista!=null && lista.size()>0){
			for(T iterato : lista){
				if(iterato!=null && id!=null && iterato.getUid()==id.intValue()){
					return iterato;
				}
			}	
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public final static <T extends SiacTBase> boolean sonoUgualiSiacTBaseByUid(T lista1, T lista2){
		return sonoUgualiSiacTBaseByUid(toList(lista1), toList(lista2));
	}
	
	@SuppressWarnings("unchecked")
	public final static <T extends Entita> boolean sonoUgualiByUid(T lista1, T lista2){
		return sonoUgualiByUid(toList(lista1), toList(lista2));
	}
	
	/**
	 * Utility che di solito uso per testare che due liste (caricate magari da rami differenti)
	 * ma devono contenere gli STESSI PRECISI elementi effettivamente abbiano gli STESSI PRECISI elementi
	 * @param lista1
	 * @param lista2
	 * @return
	 */
	public final static <T extends SiacTBase> boolean sonoUgualiSiacTBaseByUid(List<T> lista1, List<T> lista2){
		
		int numeroNonNulli1 = StringUtils.numeroElementiNonNulli(lista1);
		int numeroNonNulli2 = StringUtils.numeroElementiNonNulli(lista2);
		
		if(numeroNonNulli1!=numeroNonNulli2){
			return false;
		}
		if(numeroNonNulli1==numeroNonNulli2 && numeroNonNulli1==0){
			//entrambi vuoti consideriamoli uguali
			return true;
		}
		
		List<T> listaUnoNonNulli = ordinaSiacTBaseByUidCrescente(lista1);
		List<T> listaDueNonNulli = ordinaSiacTBaseByUidCrescente(lista2);
		
		if(listaUnoNonNulli.size()!=listaDueNonNulli.size()){
			//arrivati a questo punto dovrebbero essere uguali, ma mettiamo questo controllo per ulteriore sicureazza
			return false;
		}
		
		boolean trovatoDiverso = false;
		for(int i=0;i<listaUnoNonNulli.size();i++){
			if(listaUnoNonNulli.get(i).getUid()!=listaDueNonNulli.get(i).getUid()){
				trovatoDiverso = true;
				break;
			}
		}
		
		if(trovatoDiverso){
			return false;
		} else {
			//uguali
			return true;
		}
		
	}
	
	/**
	 * Ritorna un elenco Ordinato e Pulito Da Elementi Nulli
	 * @param elenco
	 * @return
	 */
	public final static <T extends SiacTBase> List<T> ordinaSiacTBaseByUidCrescente(List<T> elenco){
		
		List<T> listaNonNulli = StringUtils.getElementiNonNulli(elenco);
		
		if(listaNonNulli!=null && listaNonNulli.size()>0){
			
			Collections.sort(listaNonNulli, new Comparator<T>() {
				
				@Override
				public int compare(T o1, T o2) {
					
					int numeroUno = o1.getUid();
					int numeroDue = o2.getUid();
					
					if(numeroUno>numeroDue){
						return 1;
					} else if(numeroDue>numeroUno){
						return -1;
					} else {
						return 0;
					}
						
				}
			});
		}
		return listaNonNulli;
	}

	
	/**
	 * Utility che di solito uso per testare che due liste (caricate magari da rami differenti)
	 * ma devono contenere gli STESSI PRECISI elementi effettivamente abbiano gli STESSI PRECISI elementi
	 * @param lista1
	 * @param lista2
	 * @return
	 */
	public final static <T extends Entita> boolean sonoUgualiByUid(List<T> lista1, List<T> lista2){
		
		int numeroNonNulli1 = StringUtils.numeroElementiNonNulli(lista1);
		int numeroNonNulli2 = StringUtils.numeroElementiNonNulli(lista2);
		
		if(numeroNonNulli1!=numeroNonNulli2){
			return false;
		}
		if(numeroNonNulli1==numeroNonNulli2 && numeroNonNulli1==0){
			//entrambi vuoti consideriamoli uguali
			return true;
		}
		
		List<T> listaUnoNonNulli = ordinaByUidCrescente(lista1);
		List<T> listaDueNonNulli = ordinaByUidCrescente(lista2);
		
		if(listaUnoNonNulli.size()!=listaDueNonNulli.size()){
			//arrivati a questo punto dovrebbero essere uguali, ma mettiamo questo controllo per ulteriore sicureazza
			return false;
		}
		
		boolean trovatoDiverso = false;
		for(int i=0;i<listaUnoNonNulli.size();i++){
			if(listaUnoNonNulli.get(i).getUid()!=listaDueNonNulli.get(i).getUid()){
				trovatoDiverso = true;
				break;
			}
		}
		
		if(trovatoDiverso){
			return false;
		} else {
			//uguali
			return true;
		}
		
	}
	
	public final static boolean sonoUgualiByIds(List<Integer> lista1, List<Integer> lista2){
		
		int numeroNonNulli1 = StringUtils.numeroElementiNonNulli(lista1);
		int numeroNonNulli2 = StringUtils.numeroElementiNonNulli(lista2);
		
		if(numeroNonNulli1!=numeroNonNulli2){
			return false;
		}
		if(numeroNonNulli1==numeroNonNulli2 && numeroNonNulli1==0){
			//entrambi vuoti consideriamoli uguali
			return true;
		}
		
		List<Integer> listaUnoNonNulli = ordinaByIdCrescente(lista1);
		List<Integer> listaDueNonNulli = ordinaByIdCrescente(lista2);
		
		if(listaUnoNonNulli.size()!=listaDueNonNulli.size()){
			//arrivati a questo punto dovrebbero essere uguali, ma mettiamo questo controllo per ulteriore sicureazza
			return false;
		}
		
		boolean trovatoDiverso = false;
		for(int i=0;i<listaUnoNonNulli.size();i++){
			if(listaUnoNonNulli.get(i).intValue()!=listaDueNonNulli.get(i).intValue()){
				trovatoDiverso = true;
				break;
			}
		}
		
		if(trovatoDiverso){
			return false;
		} else {
			//uguali
			return true;
		}
		
	}
	
	public final static <T extends SiacTBase> List<T> ritornaSoloDistintiByUid(List<T> elenco){
		List<T> listaDistinti = new ArrayList<T>();
		List<T> listaNonNulli = StringUtils.getElementiNonNulli(elenco);
		if(listaNonNulli!=null && listaNonNulli.size()>0){
			ArrayList<Integer> idGiaAggiunti = new ArrayList<Integer>();
			
			for(T it : listaNonNulli){
				Integer idIterato = it.getUid();
				if(idIterato!=null && idIterato.intValue()>0){
					if(!idGiaAggiunti.contains(idIterato)){
						idGiaAggiunti.add(idIterato);
						listaDistinti.add(it);
					}
				}
			}
			
			//per rimuovere quelli eventualmente in piu' creati dall'allocazione con il new della lista:
			listaDistinti = StringUtils.getElementiNonNulli(listaDistinti);
			//
		}
		return listaDistinti;
	}
	
	public final static <T extends it.csi.siac.siaccommonser.integration.entity.SiacTBase> List<T> ritornaSoloDistintiBilByUid(List<T> elenco){
		List<T> listaDistinti = new ArrayList<T>();
		List<T> listaNonNulli = StringUtils.getElementiNonNulli(elenco);
		if(listaNonNulli!=null && listaNonNulli.size()>0){
			ArrayList<Integer> idGiaAggiunti = new ArrayList<Integer>();
			
			for(T it : listaNonNulli){
				Integer idIterato = it.getUid();
				if(idIterato!=null && idIterato.intValue()>0){
					if(!idGiaAggiunti.contains(idIterato)){
						idGiaAggiunti.add(idIterato);
						listaDistinti.add(it);
					}
				}
			}
			
			//per rimuovere quelli eventualmente in piu' creati dall'allocazione con il new della lista:
			listaDistinti = StringUtils.getElementiNonNulli(listaDistinti);
			//
		}
		return listaDistinti;
	}
	
	/**
	 * Ritorna un elenco Ordinato e Pulito Da Elementi Nulli
	 * @param elenco
	 * @return
	 */
	public final static <T extends Entita> List<T> ordinaByUidCrescente(List<T> elenco){
		
		List<T> listaNonNulli = StringUtils.getElementiNonNulli(elenco);
		
		if(listaNonNulli!=null && listaNonNulli.size()>0){
			
			Collections.sort(listaNonNulli, new Comparator<T>() {
				
				@Override
				public int compare(T o1, T o2) {
					
					int numeroUno = o1.getUid();
					int numeroDue = o2.getUid();
					
					if(numeroUno>numeroDue){
						return 1;
					} else if(numeroDue>numeroUno){
						return -1;
					} else {
						return 0;
					}
						
				}
			});
		}
		return listaNonNulli;
	}
	
	public final static List<Integer> ordinaByIdCrescente(List<Integer> elenco){
		
		List<Integer> listaNonNulli = StringUtils.getElementiNonNulli(elenco);
		
		if(listaNonNulli!=null && listaNonNulli.size()>0){
			
			Collections.sort(listaNonNulli, new Comparator<Integer>() {
				
				@Override
				public int compare(Integer o1, Integer o2) {
					
					int numeroUno = o1.intValue();
					int numeroDue = o2.intValue();
					
					if(numeroUno>numeroDue){
						return 1;
					} else if(numeroDue>numeroUno){
						return -1;
					} else {
						return 0;
					}
						
				}
			});
		}
		return listaNonNulli;
	}
	
	/**
	 * Sostituisce nella lista l'elemento con lo stesso uid dell'elementoModificato indicato
	 * @param lista
	 * @param elementoModificato
	 * @return
	 */
	public final static <T extends Entita> List<T> replaceById(List<T> lista, T elementoModificato){
		if(elementoModificato==null){
			return lista;
		}
		int id = elementoModificato.getUid();
		if(lista!=null && lista.size()>0){
			List<T> listaRicostruita = new ArrayList<T>();
			for(T iterato : lista){
				if(iterato!=null){
					if(iterato.getUid()==id){
						listaRicostruita.add(elementoModificato);
					} else {
						listaRicostruita.add(iterato);
					}
				}
			}	
			return listaRicostruita;
		}
		return lista;
	}
	
	/**
	 * Data una lista di oggetti che estendono Entita restituisce una 
	 * semplice lista degli id di tali oggetti
	 * @param lista
	 * @return
	 */
	public final static <T extends Entita> List<Integer> getIdList(List<T> lista){
		List<Integer> listaId = null;
		if(lista!=null && lista.size()>0){
			listaId = new ArrayList<Integer>();
			for(T iterato : lista){
				if(iterato!=null){
					listaId.add(iterato.getUid());
				}
			}	
		}
		return listaId;
	}
	
	public final static <T extends SiacTBase> List<Integer> getIdListSiacTBase(List<T> lista){
		List<Integer> listaId = null;
		if(lista!=null && lista.size()>0){
			listaId = new ArrayList<Integer>();
			for(T iterato : lista){
				if(iterato!=null){
					listaId.add(iterato.getUid());
				}
			}	
		}
		return listaId;
	}
	
	public final static <T extends Entita> boolean contenutoInLista(List<T> lista, Integer id){
		T finded = getById(lista, id);
		if(finded!=null){
			return true;
		} else {
			return false;
		}
	}
	
//	public final static <T extends Object> List<T> buildListaOneItem(T oggetto){
//		List<T> l = new ArrayList<T>();
//		if(oggetto!=null){
//			l.add(oggetto);
//		}
//		return l;
//	}
	
	public final static <T extends Object> List<T> toList(T ... oggetto){
		List<T> l = new ArrayList<T>();
		if(oggetto!=null && oggetto.length>0){
			for(T it: oggetto){
				if(it!=null){
					l.add(it);
				}
			}
		}
		return l;
	}
	
	public final static <T extends Object> List<T> toList(List<T> ... liste){
		List<T> l = new ArrayList<T>();
		if(liste!=null && liste.length>0){
			for(List<T> listIt : liste){
				if(listIt!=null && listIt.size()>0){
					for(T it: listIt){
						if(it!=null){
							l.add(it);
						}
					}
				}
			}
		}
		return l;
	}
	
	public final static <T extends Object> List<T> addAllConNewAndSoloDistintiByUid(List<T> ... lista){
		List<T> listaAll = new ArrayList<T>();
		if(lista!=null && lista.length>0){
			for(int i=0;i<lista.length;i++){
				listaAll = addAllConNewAndSoloDistintiByUidBase(listaAll, lista[i]);
			}
			
		}
		return listaAll;
	}
	
	/**
	 * Crea una nuova lista che e' la somma delle due indicate SENZA DUPLICARE EVENTUALI ELEMENTI DOPPI
	 * 
	 * @param listaTo
	 * @param listaFrom
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final static <T extends Object> List<T> addAllConNewAndSoloDistintiByUidBase(List<T> listaTo,List<T> listaFrom){
		List listaAll = addAllConNew(listaTo, listaFrom);
		listaAll = ritornaSoloDistintiByUid(listaAll);
		return listaAll;
	}
	
	public final static <T extends Entita> List<T> addAllConNewAndSoloDistintiByUidEntita(List<T> listaTo,List<T> listaFrom){
		List<T> listaAll = addAllConNew(listaTo, listaFrom);
		listaAll = ritornaSoloDistintiByUidEntita(listaAll);
		return listaAll;
	}
	
	public final static <T extends Entita> List<T> ritornaSoloDistintiByUidEntita(List<T> elenco){
		List<T> listaDistinti = new ArrayList<T>();
		List<T> listaNonNulli = StringUtils.getElementiNonNulli(elenco);
		if(listaNonNulli!=null && listaNonNulli.size()>0){
			ArrayList<Integer> idGiaAggiunti = new ArrayList<Integer>();
			
			for(T it : listaNonNulli){
				Integer idIterato = it.getUid();
				if(idIterato!=null && idIterato.intValue()>0){
					if(!idGiaAggiunti.contains(idIterato)){
						idGiaAggiunti.add(idIterato);
						listaDistinti.add(it);
					}
				}
			}
			
			//per rimuovere quelli eventualmente in piu' creati dall'allocazione con il new della lista:
			listaDistinti = StringUtils.getElementiNonNulli(listaDistinti);
			//
		}
		return listaDistinti;
	}
	
	/**
	 * Metodo creato perche' addll modifica la listaTo il che in alcuni casi non va bene
	 */
	public final static <T extends Object> List<T> addAllConNew(List<T> listaTo,List<T> listaFrom){
		
		List<T> listaAll = new ArrayList<T>();
		
		if(listaTo!=null && listaTo.size()>0){
			for(T it : listaTo){
				if(it!=null){
					T nuovoOggetto = it;
					listaAll.add(nuovoOggetto);
				}
			}
		}
		if(listaFrom!=null && listaFrom.size()>0){
			for(T it : listaFrom){
				if(it!=null){
					T nuovoOggetto = it;
					listaAll.add(nuovoOggetto);
				}
			}
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaAll;
	}
	
	public final static <T extends Object> List<T> addAll(List<T> listaTo,List<T> listaFrom){
		if(listaTo==null){
			listaTo = new ArrayList<T>();
		}
		if(listaFrom !=null && listaFrom.size()>0){
			listaTo.addAll(listaFrom);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaTo;
	}
	
	/**
	 * dato un oggetto che estende Entita, restituisce l'oggetto stesso se valido alla data indicata, null in caso contrario
	 * @param obj
	 * @param ts
	 * @return
	 */
	public final static <T extends Entita> T soloValido(T obj, Timestamp ts){
		List<T> daValidare = new ArrayList<T>();
		daValidare.add(obj);
		List<T> soloValidi = soloValidi(daValidare, ts);
		T validato = null;
		if(soloValidi!=null && soloValidi.size()>0){
			validato = soloValidi.get(0);
		}
		return validato;
	}
	
	/**
	 * dato un oggetto che estende SiacTBase, restituisce l'oggetto stesso se valido alla data indicata, null in caso contrario
	 * @param obj
	 * @param ts
	 * @return
	 */
	public final static <T extends SiacTBase> T soloValidoSiacTBase(T obj, Timestamp ts){
		List<T> daValidare = new ArrayList<T>();
		daValidare.add(obj);
		List<T> soloValidi = soloValidiSiacTBase(daValidare, ts);
		T validato = null;
		if(soloValidi!=null && soloValidi.size()>0){
			validato = soloValidi.get(0);
		}
		return validato;
	}
	
	/**
	 * dato un oggetto che estende Entita restituisce true se e' valido alla data indicata, false in caso contrario
	 * @param obj
	 * @param ts
	 * @return
	 */
	public final static <T extends Entita> boolean isValido(T obj, Timestamp ts){
		T validato = soloValido(obj, ts);
		boolean valido = false;
		if(validato!=null){
			valido = true;
		}
		return valido;
	}
	
	public final static <T extends Object> boolean entrambiNullOrEntrambiIstanziati(T o1, T o2){
		if(o1 == null && o2 == null){
			return true;
		}
		if(o1 != null && o2 != null){
			return true;
		}
		return false;
	}
	
	public final static <T extends Object> boolean unoNullunoIstanziato(T o1, T o2){
		if(o1 == null && o2 != null){
			return true;
		}
		if(o1 != null && o2 == null){
			return true;
		}
		return false;
	}
	
	public final static <T extends Object> boolean entrambiNull(T o1, T o2){
		if(o1 == null && o2 == null){
			return true;
		}
		return false;
	}
	
	public final static <T extends Object> boolean entrambiDiversiDaNull(T o1, T o2){
		if(o1 != null && o2 != null){
			return true;
		}
		return false;
	}
	
	public final static <T extends Object> T getFirst(List<T> lista){
		if(lista!=null && lista.size()>0){
			return lista.get(0);
		}
		return null;
	}
	
	public final static <T extends Object> boolean entrambiVuotiOrEntrambiConElementi(List<T> o1, List<T> o2){
		if( (o1 == null || o1.size()==0) && (o2 == null || o2.size()==0) ){
			return true;
		}
		if(o1 != null && o1.size()>0 && o2 != null && o2.size()>0){
			return true;
		}
		return false;
	}
	
	public final static <T extends Object> boolean entrambiConElementi(List<T> o1, List<T> o2){
		if(o1 != null && o1.size()>0 && o2 != null && o2.size()>0){
			return true;
		}
		return false;
	}
	
	public final static Soggetto getSoggettoByCode(List<Soggetto> lista,String code){
		Soggetto finded = null;
		if(lista!=null && lista.size()>0){
			for(Soggetto it: lista){
				if(StringUtils.sonoUgualiTrimmed(it.getCodiceSoggetto(), code)){
					finded = clone(it);
					break;
				}
			}
		}
		return finded;
	}

	
	public static String convertiBigDecimalToImporto(BigDecimal importoDB) {
		String importoFormattato = null;
		if(importoDB!=null){
			
	
			DecimalFormat df = new DecimalFormat("#,###,##0.00");
			df = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ITALY);
			df.setParseBigDecimal(true);
			df.setMinimumFractionDigits(2);
			df.setMaximumFractionDigits(2);
			importoFormattato = df.format(importoDB);

		}else{
			importoFormattato = "0,00";
		}
		
		return importoFormattato;
	}
	
	
	
	public static String convertiNumeroInLettere(Number bd)
	{
		String bdStr = bd.toString();

		String intPart = org.apache.commons.lang3.StringUtils.substringBefore(bdStr, ".");

		String convertito = convertiNumeroInLettere(intPart);

		String decPart = org.apache.commons.lang3.StringUtils.substringAfter(bdStr, ".");

		if (org.apache.commons.lang3.StringUtils.isNotEmpty(decPart))
			convertito += "/" + decPart;

		return convertito;
	}
	
	/**
	 * Per la paginazione nei servizi di ricerca
	 * @param totale
	 * @param numPagina
	 * @param numRisultatiPerPagina
	 * @return
	 */
	public static  <T> List<T> getPaginata(List<T> totale, int numPagina, int numRisultatiPerPagina) {
		if (totale == null || numPagina > totale.size()) return new ArrayList<T>(0);
		if (numPagina == 0 || numRisultatiPerPagina == 0) return totale;
		int start = (numPagina - 1) * numRisultatiPerPagina;
		int end = start + numRisultatiPerPagina;
		if (end > totale.size()) end = totale.size();
		return new ArrayList<T>(totale.subList(start, end));
	}
	
	public static  String getIdsPaginatiInteger(List<Integer> totale, int numPagina, int numRisultatiPerPagina) {
		return getIdsPaginati(StringUtils.listaIntegerToString(totale), numPagina, numRisultatiPerPagina);
	}
	
	/**
	 * Ritorna una stringa gia usabile dentro una query in clausola IN
	 * 
	 * infatti torna gli id seperati da virgola...
	 * 
	 * 
	 * @param totale
	 * @param numPagina
	 * @param numRisultatiPerPagina
	 * @return
	 */
	public static  String getIdsPaginati(List<String> totale, int numPagina, int numRisultatiPerPagina) {
		if (totale == null || numPagina > totale.size()) return "";
		if (numPagina == 0 || numRisultatiPerPagina == 0) return "";
		int start = (numPagina - 1) * numRisultatiPerPagina;
		int end = start + numRisultatiPerPagina;
		if (end > totale.size()) end = totale.size();
		List<String> sublist = new ArrayList<String>(totale.subList(start, end));
		StringBuffer result = new StringBuffer();

    	boolean first = true;
    	for (String id : sublist) {
    		
    		if(!first){
    			result.append(",");
    		}
    		result.append(id);
    		
    		first = false;
			
		}

	   //Termino restituendo l'oggetto di ritorno: 
        return result.toString();
		
	}
	
	public static List<Integer> getIdsPaginatiStringToIntegerList(String idSeperatiDaVirgola, int numPagina, int numRisultatiPerPagina) {
		List<Integer> formatoLista = new ArrayList<Integer>();
		if(!StringUtils.isEmpty(idSeperatiDaVirgola)){
			String[] arrayIds = StringUtils.getArrayByToken(idSeperatiDaVirgola, ",");
			ArrayList<String> listaString = StringUtils.arrayToArrayList(arrayIds);
			formatoLista = StringUtils.listaStringToInteger(listaString);
		}
		return formatoLista;
	}
	
	public static List<Integer> getIdsPaginatiListIntegerToInteger(List<Integer> idsAll, int numPagina, int numRisultatiPerPagina) {
		List<Integer> paginaRichiesta = new ArrayList<Integer>();
		List<String> listaString = StringUtils.listaIntegerToString(idsAll);
		String idSeperatiDaVirgola = getIdsPaginati(listaString, numPagina, numRisultatiPerPagina);
		paginaRichiesta = getIdsPaginatiStringToIntegerList(idSeperatiDaVirgola, numPagina, numRisultatiPerPagina);
		return paginaRichiesta;
	}
	
	public static String convertiNumeroInLettere(String numerico){
		return NumberToText.spell(Integer.parseInt(numerico));
	}

	/**
	 * per centralizzare le println di test - committare sempre commentato
	 * @param s
	 */
	public static void println(String s){
		//System.out.println(s);
	}
	
	public static String convertiMillisecondiInData(long yourmilliseconds)  {		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss.SS");    
		Date resultdate = new Date(yourmilliseconds);

		return sdf.format(resultdate);
		
	}
	
	public final static List<Ordinativo> rimuoviOrdinativiDiPagamento(List<Ordinativo> lista){
		List<Ordinativo> filtrati = new ArrayList<Ordinativo>();
		if(!StringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(!instanceofPagamento(it)){
					filtrati.add(clone(it));
				}
			}
		}
		return filtrati;
	}
	
	public final static List<Ordinativo> rimuoviOrdinativiDiIncasso(List<Ordinativo> lista){
		List<Ordinativo> filtrati = new ArrayList<Ordinativo>();
		if(!StringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(!instanceofIncasso(it)){
					filtrati.add(clone(it));
				}
			}
		}
		return filtrati;
	}
	
	public final static boolean instanceofPagamento(Ordinativo it){
		if(it!=null){
			return (it instanceof OrdinativoPagamento) || (it instanceof SubOrdinativoPagamento);
		} else {
			return false;
		}
	}
	
	public final static boolean instanceofIncasso(Ordinativo it){
		if(it!=null){
			return (it instanceof OrdinativoIncasso) || (it instanceof SubOrdinativoIncasso);
		} else {
			return false;
		}
	}
	
	public final static List<Ordinativo> rimuoviOrdinativiAnnullati(List<Ordinativo> lista){
		List<Ordinativo> senzaAnnullati = new ArrayList<Ordinativo>();
		if(!StringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(it!=null && !Constanti.D_ORDINATIVO_STATO_ANNULLATO.equals(it.getStatoOperativoOrdinativo())){
					senzaAnnullati.add(clone(it));
				}
			}
		}
		return senzaAnnullati;
	}
	
	public final static List<Ordinativo> filtraPerTipoAssociazioneEmissione(List<Ordinativo> lista, TipoAssociazioneEmissione relType){
		List<Ordinativo> filtrati = new ArrayList<Ordinativo>();
		if(!StringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(it!=null && relType.equals(it.getTipoAssociazioneEmissione())){
					filtrati.add(clone(it));
				}
			}
		}
		return filtrati;
	}
	
	public final static boolean presenteTipoAssociazioneEmissione(List<Ordinativo> lista, TipoAssociazioneEmissione relType){
		boolean presenti = false;
		List<Ordinativo> filtrati = filtraPerTipoAssociazioneEmissione(lista, relType);
		if(!StringUtils.isEmpty(filtrati)){
			presenti = true;
		}
		return presenti;
	}
	
	
	public final static BigDecimal sommaImportiAttualiNonAnnullatiSubOrdPag(List<SubOrdinativoPagamento> elencoTuttiSub){
		BigDecimal somma = BigDecimal.ZERO;
		for(SubOrdinativoPagamento itSub : elencoTuttiSub){
			if(itSub!=null && !Constanti.D_ORDINATIVO_STATO_ANNULLATO.equals(itSub.getStatoOperativoOrdinativo())){
				somma = somma.add(itSub.getImportoAttuale());
			}
		}
		return somma;
	}
	
	public final static SubOrdinativoPagamento getFirstNonAnnullato(List<SubOrdinativoPagamento> elencoTuttiSub){
		SubOrdinativoPagamento primoNonAnnullato = null;
		for(SubOrdinativoPagamento itSub : elencoTuttiSub){
			if(itSub!=null && !Constanti.D_ORDINATIVO_STATO_ANNULLATO.equals(itSub.getStatoOperativoOrdinativo())){
				primoNonAnnullato = itSub;
				break;
			}
		}
		return primoNonAnnullato;
	}
	
	public final static List<SubOrdinativoPagamento> rimuoviSubOrdinativiPagAnnullati(List<SubOrdinativoPagamento> lista){
		List<SubOrdinativoPagamento> senzaAnnullati = new ArrayList<SubOrdinativoPagamento>();
		if(!StringUtils.isEmpty(lista)){
			for(SubOrdinativoPagamento it: lista){
				if(it!=null && !Constanti.D_ORDINATIVO_STATO_ANNULLATO.equals(it.getStatoOperativoOrdinativo())){
					senzaAnnullati.add(clone(it));
				}
			}
		}
		return senzaAnnullati;
	}
	
	public final static String riferimento(Liquidazione liquidazione){
		String riferimento = "";
		if(liquidazione!=null){
			riferimento = liquidazione.getAnnoLiquidazione() +"/"+ liquidazione.getNumeroLiquidazione();
		}
		return riferimento;
	}
	
	public final static String riferimento(Ordinativo ordinativo){
		String riferimento = "";
		if(ordinativo!=null){
			riferimento = ordinativo.getAnno() +"/"+ ordinativo.getNumero();
		}
		return riferimento;
	}
	public final static String riferimento(MovimentoGestione movimentoGestione){
		String riferimento = "";
		if(movimentoGestione!=null){
			riferimento = movimentoGestione.getAnnoMovimento() +"/"+ movimentoGestione.getNumero();
		}
		return riferimento;
	}
	
	/**
	 * statoCodOld puo' essere null nel caso di oggetto appena inserito
	 * @param statoCodOld
	 * @param statoCodNew
	 * @return
	 */
	public final static  boolean passaggioAStatoDefinitivo(String statoCodOld, String statoCodNew) {
		
		return statoCodNew == null || statoCodNew.equals(statoCodOld) ? false :
			Constanti.MOVGEST_STATO_DEFINITIVO.equals(statoCodNew) || Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(statoCodNew);
	}
	
	public final static String getCodiceLivelloByTipo(TipologiaGestioneLivelli tipoCode, Ente ente){
		String trovato = null;
		if(tipoCode!=null && ente!=null){
			Map<TipologiaGestioneLivelli, String> livelli = ente.getGestioneLivelli();
			if(livelli!=null && livelli.size()>0){
				Iterator<Entry<TipologiaGestioneLivelli, String>> it = livelli.entrySet().iterator();
			    while (it.hasNext()) {
			        Entry<TipologiaGestioneLivelli, String> pair = it.next();
			        if(tipoCode.equals(pair.getKey())){
			        	trovato = (String) pair.getValue();
			        	break;
			        }
			    }
			}
		}
		return trovato;
	}
	
	
	
}
