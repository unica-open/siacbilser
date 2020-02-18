/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.SerializationUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoModAutomaticaPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.AccertamentoPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OrdinativoInReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RitenutaSpiltPerReintroitoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RitenuteReintroitoConStessoMovimentoDto;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.ric.OrdinativoKey;
import it.csi.siac.siacfinser.model.ric.ReintroitoRitenutaSplit;
import it.csi.siac.siacfinser.model.ric.ReintroitoRitenute;



public final class ReintroitoUtils implements Serializable   {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Messo a fattor comune il calcolo dell'importo netto per un ordinativo
	 * di pagamento in fase di reintroito
	 * @param ordinativoPagamentoRicaricato
	 * @return
	 */
	public final static BigDecimal calcolaImportoNettoPerReintroito(OrdinativoPagamento ordinativoPagamentoRicaricato){
		BigDecimal sommaImportiOrdinativiIncasso = sommaImportoOrdinativiIncasso(ordinativoPagamentoRicaricato);
		BigDecimal importoNetto = ordinativoPagamentoRicaricato.getImportoOrdinativo().subtract(sommaImportiOrdinativiIncasso);
		return importoNetto;
	}
	
	/**
	 * Semplice metodo di comodo che somma gli importi degli ordinativi di incasso collegati.
	 * 
	 * NOTA: esclude gli annullati che e' come se non ci fossero.
	 * 
	 * @param ordinativoPag
	 * @return
	 */
	public final static BigDecimal sommaImportoOrdinativiIncasso(OrdinativoPagamento ordinativoPag){
		BigDecimal sommaImportiOrdinativiIncasso = BigDecimal.ZERO;
		if(ordinativoPag!=null){
			List<Ordinativo> senzaAnnullati = CommonUtils.rimuoviOrdinativiAnnullati(ordinativoPag.getElencoOrdinativiCollegati());
			if(ordinativoPag!=null && !StringUtils.isEmpty(senzaAnnullati)){
				for(Ordinativo it: senzaAnnullati){
					if(it!=null){
						sommaImportiOrdinativiIncasso = sommaImportiOrdinativiIncasso.add(it.getImportoOrdinativo());
					}
				}
			}
		}
		return sommaImportiOrdinativiIncasso;
	}
	
	public final static String descrizionePerLiquidazione(ImpegnoPerReintroitoInfoDto impDestInfo,OrdinativoInReintroitoInfoDto datiReintroito){
		String descrImpOrSub = impDestInfo.getDescrizioneImpOSub();
		if(StringUtils.isEmpty(descrImpOrSub)){
			AttoAmministrativo provv = attoAmministrativoDaUsare(impDestInfo, datiReintroito);
			descrImpOrSub = provv.getAnno() + "/" + provv.getNumero() + " - " + provv.getTipoAtto().getDescrizione();
			StrutturaAmministrativoContabile sac = provv.getStrutturaAmmContabile();
			if(sac!=null && !StringUtils.isEmpty(sac.getCodice())){
				descrImpOrSub = descrImpOrSub + " - " + sac.getCodice();
			}
		}
		return descrImpOrSub;
	}
	
	public final static AttoAmministrativo attoAmministrativoDaUsare(ImpegnoPerReintroitoInfoDto impDestInfo, OrdinativoInReintroitoInfoDto datiReintroito){
		MovimentoGestione mg = impDestInfo.getImpegno();
		MovimentoGestione subMov = impDestInfo.getSubImpegno();
		if(subMov!=null && CommonUtils.maggioreDiZero(subMov.getNumero())){
			//in caso di sub indicato vince il sub:
			return attoAmministrativoDaUsare(subMov, datiReintroito);
		} else {
			return attoAmministrativoDaUsare(mg, datiReintroito);
		}
	}
	
	/**
	 * 
	 *  Messa a fattor comune la semplice logica per cui dato un certo movimento,
	 *  viene individuato come provvedimento da utilizzare quello di tale movimento
	 *  o quello indicato in input il tutto pilotato dal flag utilizzaProvvedimentoDaMovimenti
	 * 
	 * @param mg
	 * @param datiReintroito
	 * @return
	 */
	public final static AttoAmministrativo attoAmministrativoDaUsare(MovimentoGestione mg, OrdinativoInReintroitoInfoDto datiReintroito){
		//gestione atto amministrativo:
		AttoAmministrativo attoAmministrativo = null;
		if(datiReintroito.isUtilizzaProvvedimentoDaMovimenti()){
			attoAmministrativo = mg.getAttoAmministrativo();
		} else {
			attoAmministrativo = datiReintroito.getAttoAmministrativo();
		}
		//
		return attoAmministrativo;
	}
	
	
	public final static BigDecimal sommaImportiModifiche(List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaMod){
		BigDecimal somma = BigDecimal.ZERO;
		if(!StringUtils.isEmpty(listaMod)){
			for(AccertamentoModAutomaticaPerReintroitoInfoDto it : listaMod){
				if(it!=null && it.getModificaDaApportare()!=null){
					somma = somma.add(it.getModificaDaApportare());
				}
			}
		}
		return somma;
	}
	
	public final static List<AccertamentoModAutomaticaPerReintroitoInfoDto> conStessoMovKey(List<AccertamentoModAutomaticaPerReintroitoInfoDto> listaModInfo, MovimentoKey key){
		List<AccertamentoModAutomaticaPerReintroitoInfoDto> conStessaKey = new ArrayList<AccertamentoModAutomaticaPerReintroitoInfoDto>();
		if(!StringUtils.isEmpty(listaModInfo)){
			for(AccertamentoModAutomaticaPerReintroitoInfoDto modInfoIt: listaModInfo){
				if(modInfoIt!=null && sonoUguali(modInfoIt.getKey(), key)){
					conStessaKey.add(modInfoIt);
				}
			}
		}
		return conStessaKey;
	}
	
	public final static BigDecimal sommaImportiOrdinativiIncasso(List<RitenutaSpiltPerReintroitoInfoDto> lista){
		BigDecimal somma = BigDecimal.ZERO;
		if(!StringUtils.isEmpty(lista)){
			for(RitenutaSpiltPerReintroitoInfoDto it : lista){
				if(it!=null && it.getOrdinativoIncasso()!=null && it.getOrdinativoIncasso().getImportoOrdinativo()!=null){
					somma = somma.add(it.getOrdinativoIncasso().getImportoOrdinativo());
				}
			}
		}
		return somma;
	}
	
	public final static List<RitenuteReintroitoConStessoMovimentoDto> raggruppaConStessoAccertamento(List<RitenutaSpiltPerReintroitoInfoDto> lista){
		List<RitenuteReintroitoConStessoMovimentoDto> conStessoAccertamento = new ArrayList<RitenuteReintroitoConStessoMovimentoDto>();
		List<MovimentoKey> giaFatti = new ArrayList<MovimentoKey>();
		if(!StringUtils.isEmpty(lista)){
			for(RitenutaSpiltPerReintroitoInfoDto it : lista){
				if(it!=null && it.getAccertamento()!=null){
					AccertamentoPerReintroitoInfoDto accInfo = it.getAccertamento();
					MovimentoKey movKey = accInfo.getKey();
					if(!ReintroitoUtils.contenutoInLista(giaFatti, movKey)){
						List<RitenutaSpiltPerReintroitoInfoDto> listaStessoAccIterato = conStessoAccertamento(lista, movKey);
						RitenuteReintroitoConStessoMovimentoDto gruppoIt = new RitenuteReintroitoConStessoMovimentoDto();
						gruppoIt.setListaRitenute(listaStessoAccIterato);
						gruppoIt.setMovimentoKey(movKey);
						conStessoAccertamento.add(gruppoIt);
						//aggiungo alla lista dei gia controllati per non controllare due volte:
						giaFatti.add(movKey);
					}
				}
			}
		}
		return conStessoAccertamento;
	}
	
	public final static List<RitenuteReintroitoConStessoMovimentoDto> raggruppaConStessoImpegno(List<RitenutaSpiltPerReintroitoInfoDto> lista){
		List<RitenuteReintroitoConStessoMovimentoDto> conStessoImpegno = new ArrayList<RitenuteReintroitoConStessoMovimentoDto>();
		List<MovimentoKey> giaFatti = new ArrayList<MovimentoKey>();
		if(!StringUtils.isEmpty(lista)){
			for(RitenutaSpiltPerReintroitoInfoDto it : lista){
				if(it!=null && it.getImpegno()!=null){
					ImpegnoPerReintroitoInfoDto impInfo = it.getImpegno();
					MovimentoKey movKey = impInfo.getKey();
					if(!ReintroitoUtils.contenutoInLista(giaFatti, movKey)){
						List<RitenutaSpiltPerReintroitoInfoDto> listaStessoImpIterato = conStessoImpegno(lista, movKey);
						RitenuteReintroitoConStessoMovimentoDto gruppoIt = new RitenuteReintroitoConStessoMovimentoDto();
						gruppoIt.setListaRitenute(listaStessoImpIterato);
						gruppoIt.setMovimentoKey(movKey);
						conStessoImpegno.add(gruppoIt);
						//aggiungo alla lista dei gia controllati per non controllare due volte:
						giaFatti.add(movKey);
					}
				}
			}
		}
		return conStessoImpegno;
	}
	
	public final static List<RitenutaSpiltPerReintroitoInfoDto> conStessoImpegno(List<RitenutaSpiltPerReintroitoInfoDto> lista, MovimentoKey impKey){
		List<RitenutaSpiltPerReintroitoInfoDto> conStessoImpegno = new ArrayList<RitenutaSpiltPerReintroitoInfoDto>();
		if(!StringUtils.isEmpty(lista)){
			for(RitenutaSpiltPerReintroitoInfoDto it : lista){
				if(it!=null && it.getImpegno()!=null && sonoUguali(it.getImpegno().getKey(), impKey)){
					conStessoImpegno.add(clone(it));
				}
			}
		}
		return conStessoImpegno;
	}
	
	public final static List<RitenutaSpiltPerReintroitoInfoDto> conStessoAccertamento(List<RitenutaSpiltPerReintroitoInfoDto> lista, MovimentoKey accKey){
		List<RitenutaSpiltPerReintroitoInfoDto> conStessoAccertamento = new ArrayList<RitenutaSpiltPerReintroitoInfoDto>();
		if(!StringUtils.isEmpty(lista)){
			for(RitenutaSpiltPerReintroitoInfoDto it : lista){
				if(it!=null && it.getAccertamento()!=null && sonoUguali(it.getAccertamento().getKey(), accKey)){
					conStessoAccertamento.add(clone(it));
				}
			}
		}
		return conStessoAccertamento;
	}
	
	public final static String buildNomeEntitaPerMessaggiDiErrore(OrdinativoKey oKey, boolean pagamento){
		String nomeEntita = "Ordinativo Incasso";
		if(pagamento){
			nomeEntita = "Ordinativo Pagamento";
		}
		return nomeEntita;
	}
	
	public final static String buildCodiceEntitaPerMessaggiDiErrore(OrdinativoKey oKey){
		String codiceEntita = oKey.getAnno() + " / " + oKey.getNumero();
		return codiceEntita;
	}
	
	public final static String buildNomeEntitaPerMessaggiDiErrore(MovimentoKey mKey, boolean impegno){
		String nomeEntita = "Accertamento";
		if(impegno){
			nomeEntita = "Impegno";
		}
		if(CommonUtils.maggioreDiZero(mKey.getNumeroSubMovimento())){
			nomeEntita = "Sub" + nomeEntita;
		}
		return nomeEntita;
	}
	
	public final static String buildCodiceEntitaPerMessaggiDiErrore(MovimentoKey mKey){
		String codiceEntita = mKey.getAnnoEsercizio() + " / " + mKey.getAnnoMovimento() + " / " + mKey.getNumeroMovimento();
		if(CommonUtils.maggioreDiZero(mKey.getNumeroSubMovimento())){
			codiceEntita = codiceEntita + " / " + mKey.getNumeroSubMovimento();
		}
		return codiceEntita;
	}
	
	public final static List<ReintroitoRitenutaSplit> buildSplits(ReintroitoRitenute ritenute,List<Ordinativo> ordinativiIncasso){
		List<ReintroitoRitenutaSplit> ritSplits = new ArrayList<ReintroitoRitenutaSplit>();
		if(ordinativiIncasso!=null && ritenute!=null){
			for(Ordinativo ordIt: ordinativiIncasso){
				ritSplits.add(buildSplit(ritenute, ordIt));
			}
		}
		return ritSplits;
	}
	
	public final static ReintroitoRitenutaSplit buildSplit(ReintroitoRitenute ritenute,Ordinativo ordinativiIncasso){
		ReintroitoRitenutaSplit ritIt = new ReintroitoRitenutaSplit();
		if(ordinativiIncasso!=null){
			ritIt.setAccertamentoDiDestinazione(ritenute.getAccertamentoDiDestinazione());
			ritIt.setImpegnoDiDestinazione(ritenute.getImpegnoDiDestinazione());
			ritIt.setOrdinativoIncasso(ReintroitoUtils.toOrdKey(ordinativiIncasso));
		}
		return ritIt;
	}
	
	public final static boolean contenutoInLista(List<MovimentoKey> lista, MovimentoKey key){
		MovimentoKey finded = getByKey(lista, key);
		if(!isEmpty(finded)){
			return true;
		} else {
			return false;
		}
	}
	
	public final static boolean contenutoInLista(List<OrdinativoKey> lista, OrdinativoKey key){
		OrdinativoKey finded = getByKey(lista, key);
		if(!isEmpty(finded)){
			return true;
		} else {
			return false;
		}
	}
	
	public final static Ordinativo getByKeyOrdinativo(List<Ordinativo> lista, OrdinativoKey key){
		Ordinativo finded = null;
		if(!StringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(sonoUguali(toOrdKey(it), key)){
					finded = it;
					break;
				}
			}
		 }
		 return finded;
	}
	
	public final static MovimentoKey getByKey(List<MovimentoKey> lista, MovimentoKey key){
		MovimentoKey finded = null;
		 if(!StringUtils.isEmpty(lista)){
			 for(MovimentoKey it: lista){
				 if(sonoUguali(it, key)){
					 finded = it;
					 break;
				 }
			 }
		 }
		 return finded;
	}
	
	public final static OrdinativoKey getByKey(List<OrdinativoKey> lista, OrdinativoKey key){
		 OrdinativoKey finded = null;
		 if(!StringUtils.isEmpty(lista)){
			 for(OrdinativoKey it: lista){
				 if(sonoUguali(it, key)){
					 finded = it;
					 break;
				 }
			 }
		 }
		 return finded;
	}
	
	public final static boolean sonoUguali(OrdinativoKey key1, OrdinativoKey key2){
		boolean uguali = false;
		if(!isEmpty(key1) && !isEmpty(key2)){
			if(StringUtils.sonoUguali(key1.getAnno(), key2.getAnno()) && 
				StringUtils.sonoUguali(key1.getNumero(), key2.getNumero()) && 
				StringUtils.sonoUguali(key1.getAnnoBilancio(), key2.getAnnoBilancio())){
				uguali = true;
			}
		}
		return uguali;
	}
	
	public final static boolean sonoUguali(MovimentoKey key1, MovimentoKey key2){
		boolean uguali = false;
		if(!isEmpty(key1) && !isEmpty(key2)){
			if(StringUtils.sonoUguali(key1.getAnnoMovimento(), key2.getAnnoMovimento()) && 
				StringUtils.sonoUguali(key1.getNumeroMovimento(), key2.getNumeroMovimento()) && 
				StringUtils.sonoUguali(key1.getAnnoEsercizio(), key2.getAnnoEsercizio())){
				
				//FIN QUI IL MOVIMENTO E' LO STESSO, VEDIAMO IL SUB EVENTUALE:
				
				boolean unoSiDueNo = isSub(key1) && !isSub(key2);
				boolean unoNoDueSi = !isSub(key1) && isSub(key2);
				if(unoSiDueNo || unoNoDueSi){
					//se uno e' sub e l'altro no --> non sono uguali
					return false;
				}
				
				boolean tuttiEDueSub = isSub(key1) && isSub(key2);
				if(tuttiEDueSub){
					//se tutti e due sono sub deve essere lo stesso sub:
					return StringUtils.sonoUguali(key1.getNumeroSubMovimento(), key2.getNumeroSubMovimento());
				}
				
				
				//se sono qui non sono sub nessuno dei due quindi sono uguali
				uguali = true;
			}
		}
		return uguali;
	}
	
	/**
	 * Data una lista di ordinativi completi ritorna una lista con le loro chiavi
	 * sotto forma di oggetti OrdinativoKey
	 * @param listOrd
	 * @return
	 */
	public final static List<OrdinativoKey> toOrdKeys(List<Ordinativo> listOrd){
		List<OrdinativoKey> listaKey = new ArrayList<OrdinativoKey>();
		if(!StringUtils.isEmpty(listOrd)){
			for(Ordinativo it: listOrd){
				if(it!=null){
					listaKey.add(toOrdKey(it));
				}
			}
		}
		return listaKey;
	}
	
	/**
	 * Costruisce un oggetto ordinativo key a partire da un ordinatio completo
	 * @param ordinativo
	 * @return
	 */
	public final static OrdinativoKey toOrdKey(Ordinativo ordinativo){
		OrdinativoKey oKey = null;
		if(ordinativo!=null){
			oKey = new OrdinativoKey();
			oKey.setAnno(ordinativo.getAnno());
			oKey.setNumero(ordinativo.getNumero());
			oKey.setAnnoBilancio(ordinativo.getAnnoBilancio());
		}
		return oKey;
	}
	
	/**
	 * Data una lista di ReintroitoRitenutaSplit costruisce e ritorna
	 * una lista degli ordinativi di incasso key presenti
	 * @param listaRitenute
	 * @return
	 */
	public final static List<OrdinativoKey> estraiListaOrdinativiIncasso(List<ReintroitoRitenutaSplit> listaRitenute){
		List<OrdinativoKey> ordinativi = new ArrayList<OrdinativoKey>();
		if(!StringUtils.isEmpty(listaRitenute)){
			for(ReintroitoRitenutaSplit it: listaRitenute){
				if(it!=null){
					ordinativi.add(it.getOrdinativoIncasso());
				}
			}
		}
		return ordinativi;
	}

	/**
	 * Mi costruisce una chiave univoca per i movimenti caricati in una cache di comodo
	 * @param ente
	 * @param movKey
	 * @return
	 */
	public final static String buildMovKey(Ente ente, MovimentoKey movKey){
		String elabKey = ente.getUid() + "-" 
				+ movKey.getAnnoEsercizio() + "-"
				+ movKey.getAnnoMovimento() + "-"
				+ movKey.getNumeroMovimento();
		if(CommonUtils.maggioreDiZero(movKey.getNumeroSubMovimento())){
			elabKey = elabKey + "-" + movKey.getNumeroSubMovimento();
		} else {
			elabKey = elabKey + "-" + "nosub";
		}
		return elabKey;
	}
	
	/**
	 * Costruisce la chiave per la gestione della concorrenza in siac_t_elaborazioni_attive
	 * per quanto riguarda il servizio di ReintroitoOrdinativoDiPagamento
	 * @param ente
	 * @param ordKey
	 * @return
	 */
	public final static String buildElabKeyReintroito(Ente ente, OrdinativoKey ordKey){
		String elabKey = ente.getUid() + "-" +
				ordKey.getAnno() +"-"
				+ ordKey.getNumero();
		return elabKey;
	}
	
	/**
	 * Un oggetto ReintroitoRitenute si considera vuoto se non ha accertamento ed impegno
	 * (entrambi devono esserci)
	 * @param ritenute
	 * @return
	 */
	public final static boolean isEmpty(ReintroitoRitenute ritenute){
		boolean vuoto = true;
		if(ritenute!=null){
			vuoto = isEmpty(ritenute.getImpegnoDiDestinazione()) || isEmpty(ritenute.getAccertamentoDiDestinazione());
		}
		return vuoto;
	}
	
	/**
	 * Un oggetto ReintroitoRitenutaSplit si considera vuoto se non ha accertamento, impegno ed ordinativo di incasso
	 * (devono esserci tutti)
	 * @param ritenutaSplit
	 * @return
	 */
	public final static boolean isEmpty(ReintroitoRitenutaSplit ritenutaSplit){
		boolean vuoto = true;
		if(ritenutaSplit!=null){
			vuoto = isEmpty(ritenutaSplit.getOrdinativoIncasso()) || isEmpty(ritenutaSplit.getImpegnoDiDestinazione()) || isEmpty(ritenutaSplit.getAccertamentoDiDestinazione());
		}
		return vuoto;
	}
	
	/**
	 * Non coerente significa che e' valorizzata parzialemente, non serve a nulla..
	 * @param ritenutaSplit
	 * @return
	 */
	public final static boolean isCorente(ReintroitoRitenutaSplit ritenutaSplit){
		boolean coerente = true;
		if(ritenutaSplit!=null){
			boolean almenoUnoVuoto = isEmpty(ritenutaSplit.getOrdinativoIncasso()) || isEmpty(ritenutaSplit.getImpegnoDiDestinazione()) || isEmpty(ritenutaSplit.getAccertamentoDiDestinazione());
			boolean almenoUnoValorizzato = !isEmpty(ritenutaSplit.getOrdinativoIncasso()) || !isEmpty(ritenutaSplit.getImpegnoDiDestinazione()) || !isEmpty(ritenutaSplit.getAccertamentoDiDestinazione());
			if(almenoUnoVuoto && almenoUnoValorizzato){
				coerente = false;
			}
		}
		return coerente;
	}
	
	/**
	 * Non basta che abbia elementi per non essere nulli, ma i singoli elementi devono essere popolati correttamente.
	 * @param ritenuteSplit
	 * @return
	 */
	public final static boolean isEmpty(List<ReintroitoRitenutaSplit> ritenuteSplit){
		boolean vuota = true;
		if(!StringUtils.isEmpty(ritenuteSplit)){
			for(ReintroitoRitenutaSplit it: ritenuteSplit){
				if(!isEmpty(it)){
					vuota = false;
					break;
				}
			}
		}
		return vuota;
	}
	
	/**
	 * Basta che uno non sia coerente per non considerare tutto coerente
	 * @param ritenuteSplit
	 * @return
	 */
	public final static boolean tuttiCoerenti(List<ReintroitoRitenutaSplit> ritenuteSplit){
		boolean coerenti = true;
		if(!StringUtils.isEmpty(ritenuteSplit)){
			for(ReintroitoRitenutaSplit it: ritenuteSplit){
				if(!isCorente(it)){
					coerenti = false;
					break;
				}
			}
		}
		return coerenti;
	}
	
	/**
	 *  * Metodo di comodo per i controlli formali, verifica se l'ordinativo passato e' stato popolato o meno.
	 * Si basa sulla presenza del numero e dell'anno
	 * @param ok
	 * @return
	 */
	public final static boolean isEmpty(OrdinativoKey ok){
		boolean vuoto = true;
		if(ok!=null && ok.getAnno()>0 && CommonUtils.maggioreDiZero(ok.getNumero()) && CommonUtils.maggioreDiZero(ok.getAnnoBilancio())){
			vuoto = false;
		}
		return vuoto;
	}
	
	/**
	 * Metodo di comodo per i controlli formali, verifica se il movimento passato e' stato popolato o meno.
	 * Si basa sulla presenza del numero e dell'anno
	 * @param mk
	 * @return
	 */
	public final static boolean isEmpty(MovimentoKey mk){
		boolean vuoto = true;
		if(mk!=null && mk.getAnnoMovimento()>0 && CommonUtils.maggioreDiZero(mk.getNumeroMovimento()) && CommonUtils.maggioreDiZero(mk.getAnnoEsercizio())){
			vuoto = false;
		}
		return vuoto;
	}
	
	public final static MovimentoKey subToMovKey(MovimentoKey subKey){
		MovimentoKey mk = null;
		if(subKey!=null){
			mk = clone(mk);
			mk.setNumeroSubMovimento(null);
		}
		return mk;
	}
	
	public final static boolean isSub(MovimentoKey mk){
		boolean isSub = false;
		if(!isEmpty(mk) && CommonUtils.maggioreDiZero(mk.getNumeroSubMovimento())){
			isSub = true;
		}
		return isSub;
	}
	
	/**
	 * Metodo che permette di effettuare la copia di un oggetto, cambiando
	 * l'allocazione di memoria dell'oggetto copiato
	 * 
	 * @param o
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T clone(T o) {
		return (T) SerializationUtils.deserialize(SerializationUtils.serialize(o));
	}

}
