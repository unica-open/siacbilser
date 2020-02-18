/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.SiacTAccountFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRModificaStatoRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAccountFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;
import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;
import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

public class DatiOperazioneUtils {
	
	
	public final static String determinaUtenteLogin(DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		String loginOperazione = "";
		Integer id = Integer.parseInt(datiOperazione.getAccountCode());
		SiacTAccountFin account = siacTAccountRepository.findOne(id);
		loginOperazione = account.getAccountCode();
		return loginOperazione;
	}

	public final static <T extends SiacLoginMultiplo> T impostaDatiOperazioneLogin(T  entity , DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		//
		long currMillisec = datiOperazione.getCurrMillisec();
		
		String utenteLogin = determinaUtenteLogin(datiOperazione,siacTAccountRepository);
		
		Operazione operazione = datiOperazione.getOperazione();
		SiacTEnteProprietarioFin siacTEnteProprietario = datiOperazione.getSiacTEnteProprietario();
		//
		if(siacTEnteProprietario!=null){
			entity.setSiacTEnteProprietario(siacTEnteProprietario);
		}
		Date data = new Date(currMillisec);
		Timestamp time = new Timestamp(currMillisec);
		entity.setLoginModifica(utenteLogin);
		entity.setDataModifica(data);
		entity.setLoginOperazione(utenteLogin);
		if(Operazione.INSERIMENTO.equals(operazione)){
			entity.setLoginCreazione(utenteLogin);
			entity.setDataCreazione(data);
			entity.setDataInizioValidita(data);
		}
		if(Operazione.CANCELLAZIONE_LOGICA_RECORD.equals(operazione)){
			entity.setDataCancellazione(time);
			entity.setDataFineValidita(data);
			entity.setLoginCancellazione(utenteLogin);
		}
		if(Operazione.ANNULLA.equals(operazione)){
			entity.setDataFineValidita(data);
		}
		if(Operazione.SOSPENDI.equals(operazione)){
			entity.setDataFineValidita(data);
		}
		if(Operazione.BLOCCA.equals(operazione)){
			entity.setDataFineValidita(data);
		}
		if (entity.getLoginCreazione() == null || "".equalsIgnoreCase(entity.getLoginCreazione())) {
			entity.setLoginCreazione(utenteLogin);
		}
		if (entity.getDataCreazione() == null) {
			entity.setDataCreazione(data);
		}
		if (entity.getDataInizioValidita() == null) {
			entity.setDataInizioValidita(data);
		}
		return entity;
	}
	
	public final static <T extends SiacTEnteBase> T impostaDatiOperazioneLogin(T  entity , DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		//
		long currMillisec = datiOperazione.getCurrMillisec();
		String utenteLogin = determinaUtenteLogin(datiOperazione, siacTAccountRepository);
		Operazione operazione = datiOperazione.getOperazione();
		SiacTEnteProprietarioFin siacTEnteProprietario = datiOperazione.getSiacTEnteProprietario();
		//
		if(siacTEnteProprietario!=null){
			entity.setSiacTEnteProprietario(siacTEnteProprietario);
		}
		Date data = new Date(currMillisec);
		Timestamp time = new Timestamp(currMillisec);
		entity.setDataModifica(data);
		entity.setLoginOperazione(utenteLogin);
		
		if(Operazione.INSERIMENTO.equals(operazione)){
			entity.setDataCreazione(data);
			entity.setDataInizioValidita(data);
		}
		if(Operazione.CANCELLAZIONE_LOGICA_RECORD.equals(operazione)){
			entity.setDataCancellazione(time);
			entity.setDataFineValidita(data);
		}
		if(Operazione.ANNULLA.equals(operazione)){
			entity.setDataFineValidita(data);
		}
		if(Operazione.SOSPENDI.equals(operazione)){
			entity.setDataFineValidita(data);
		}
		if(Operazione.BLOCCA.equals(operazione)){
			entity.setDataFineValidita(data);
		}
		if (entity.getDataCreazione() == null) {
			entity.setDataCreazione(data);
		}
		if (entity.getDataInizioValidita() == null) {
			entity.setDataInizioValidita(data);
		}
		return entity;
	}
	
	/**
	 * effettua la cancellazione logica del record indicato
	 * @param entity
	 * @param repository
	 * @param datiOperazione
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends SiacTEnteBase, E extends JpaRepository> T  cancellaRecord(T entity, E repository,DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		datiOperazione.setOperazione(Operazione.CANCELLAZIONE_LOGICA_RECORD);
		entity = impostaDatiOperazioneLogin(entity, datiOperazione,siacTAccountRepository);
		return (T) repository.saveAndFlush(entity);
	}
	
	public final static <T extends SiacTEnteBase, E extends JpaRepository> List<T> bonificaDataCancellazione(List<T> entities, E repository,DatiOperazioneDto datiOperazione){
		if(!StringUtils.isEmpty(entities)){
			for(T entity: entities){
				entity = bonificaDataCancellazione(entity, repository, datiOperazione);
			} 
		}
		return entities;
	}
	
	/**
	 * Se l'entita ha la data cancellazione vuota e non e' valido perche' ha una data fine validita minore del ts indicato 
	 * salva l'entita settandogli la data cancellazione alla data di fine validita
	 * 
	 * Ritorna l'oggetto cosi com'era se non ha fatto nulla altrimenti l'oggetto aggiornato.
	 * 
	 * @param entity
	 * @param repository
	 * @param datiOperazione
	 * @return
	 */
	public final static <T extends SiacTEnteBase, E extends JpaRepository> T  bonificaDataCancellazione(T entity, E repository,DatiOperazioneDto datiOperazione){
		Timestamp time = new Timestamp(datiOperazione.getCurrMillisec());
		if(entity!=null && !isValido(entity, time) && entity.getDataCancellazione()==null){
			entity.setDataCancellazione(TimingUtils.convertiDataInTimeStamp(entity.getDataFineValidita()));
			return (T) repository.saveAndFlush(entity);
		}
		return entity;
	}
	
	public final static <T extends SiacTEnteBase, E extends JpaRepository> void  cancellaRecords(List<T> entityList, E repository,DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		//invalido l'eventuale old:
		if(entityList!=null && entityList.size()>0){
			for(T it: entityList){
				cancellaRecord(it, repository, datiOperazione,siacTAccountRepository);
			}
		}
	}

	public final static <T extends SiacTEnteBase, E extends JpaRepository> void  cancellaRecordsOttimizzato(List<T> entityList, E repository,DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		//invalido l'eventuale old:
		if(entityList!=null && entityList.size()>0){
			List<T> listaDaCancellare = new ArrayList<T>();
			for(T it: entityList){
				datiOperazione.setOperazione(Operazione.CANCELLAZIONE_LOGICA_RECORD);
				it = impostaDatiOperazioneLogin(it, datiOperazione,siacTAccountRepository);
				listaDaCancellare.add(it);
			}
			
			repository.save(listaDaCancellare);
			repository.flush();
			
		}
	}
	
	
	
	
	/**
	 * effettua la cancellazione logica del record indicato
	 * @param entity
	 * @param repository
	 * @param datiOperazione
	 * @return
	 */
	public final static <T extends SiacTEnteBase, E extends JpaRepository> T  annullaRecord(T entity, E repository,DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		datiOperazione.setOperazione(Operazione.ANNULLA);
		entity = impostaDatiOperazioneLogin(entity, datiOperazione,siacTAccountRepository);
		return (T) repository.saveAndFlush(entity);
	}
	
	/**
	 * effettua la cancellazione logica del record indicato
	 * @param entity
	 * @param repository
	 * @param datiOperazione
	 * @return
	 */
	public final static <T extends SiacLoginMultiplo, E extends JpaRepository> T  cancellaRecord(T entity, E repository,DatiOperazioneDto datiOperazione,SiacTAccountFinRepository siacTAccountRepository){
		datiOperazione.setOperazione(Operazione.CANCELLAZIONE_LOGICA_RECORD);
		entity = impostaDatiOperazioneLogin(entity, datiOperazione,siacTAccountRepository);
		return (T) repository.saveAndFlush(entity);
	}
	
	/**
	 * aggiorna il SiacDModificaStatoFin cone lo stato indicato per l'entity indicata
	 * @param entity
	 * @param repository
	 * @param datiOperazione
	 * @param nuovoStato
	 * @param siacRModificaStatoRepository
	 * @param siacTAccountRepository
	 * @return
	 */
	public final static <T extends SiacConModificaStato, E extends JpaRepository> T  cambiaModificaStato(T entity, E repository,DatiOperazioneDto datiOperazione
			,SiacDModificaStatoFin nuovoStato, SiacRModificaStatoRepository siacRModificaStatoRepository,SiacTAccountFinRepository siacTAccountRepository){
		entity.getSiacRModificaStato().setSiacDModificaStato(nuovoStato);
		//
		SiacRModificaStatoFin siacRModificaStato = entity.getSiacRModificaStato();
		datiOperazione.setOperazione(Operazione.MODIFICA);//consideriamo il cambio stato come un "update"
		siacRModificaStato =  impostaDatiOperazioneLogin(siacRModificaStato, datiOperazione,siacTAccountRepository);
		siacRModificaStato = siacRModificaStatoRepository.saveAndFlush(siacRModificaStato);
		entity.setSiacRModificaStato(siacRModificaStato);
		return entity;
	}
	
	/**
	 * Confronta se lo stato e' uguale a quello indicato
	 * @param entity
	 * @param statoDaCheckare
	 * @return
	 */
	public final static <T extends SiacConModificaStato> boolean  isInStato(T entity,String statoDaCheckare){
		SiacRModificaStatoFin siacRModificaStato = entity.getSiacRModificaStato();
		SiacDModificaStatoFin siacDModStato = siacRModificaStato.getSiacDModificaStato();
		if(statoDaCheckare.equalsIgnoreCase(siacDModStato.getModStatoCode())){
			return true;
		} else {
			return false;
		}
	}
	
	public final static <T extends SiacTBase> ArrayList<Integer> getListaId(List<T> lista){
		ArrayList<Integer> listaId = new ArrayList<Integer>();
		for(T iterato : lista){
			listaId.add(iterato.getUid());
		}
		return listaId;
	}
	
	public final static <T extends SiacTBase> T getById(List<T> lista, Integer id){
		if(lista!=null && lista.size()>0){
			for(T iterato : lista){
				if(iterato!=null && id!=null && iterato.getUid()!=null && iterato.getUid().intValue()==id.intValue()){
					return iterato;
				}
			}	
		}
		return null;
	}
	
	public final static <T extends SiacTBase> boolean hannoLoStessoUid(T o1, T o2){
		if(!CommonUtils.entrambiDiversiDaNull(o1, o2)){
			return false;
		}
		if(o1.getUid().intValue()==o2.getUid().intValue()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * presuppone che in lista ci sia uno solo valido
	 * @param lista
	 * @param ts
	 * @return
	 */
	public final static <T extends SiacTBase> T getValido(List<T> lista, Timestamp ts){
		List<T> trovatoL = soloValidi(lista, ts);
		if(trovatoL!=null && trovatoL.size()>0){
			return trovatoL.get(0);
		}
		return null;
	}
	
	/**
	 * Se timestamp e' passato a null --> si presuppone che sia l'istante corrente
	 * @param lista
	 * @param ts
	 * @return
	 */
	public final static <T extends SiacTBase> List<T> soloValidi(List<T> lista, Timestamp ts){
		List<T> validi = new ArrayList<T>();
		if(ts==null){
			//Se timestamp e' passato a null --> si presuppone che sia l'istante corrente
			ts = new Timestamp(System.currentTimeMillis());
		}
		if(lista!=null){
			for(T it : lista){
				//System.out.println("it: " + it.getUid());
				
				if(isValido(it, ts)){
					validi.add(it);
				}
			}
		}
		return validi;
	}
	
	/**
	 * 
	 * validi = true     --> ritorna solo validi
	 * validi = false    --> ritorna solo non validi
	 * validi = null     --> ritorna tutti
	 * 
	 * @param lista
	 * @param ts
	 * @param validi
	 * @return
	 */
	public final static <T extends SiacTBase> List<T> filtraByValidita(List<T> lista, Timestamp ts, Boolean validi){
		List<T> listaRitorno = new ArrayList<T>();
		if(validi!=null && lista!=null){
			if(validi){
				listaRitorno = soloValidi(lista, ts);
			} else {
				listaRitorno = soloNonValidi(lista, ts);
			}
		} else {
			listaRitorno = lista;
		}
		return listaRitorno;
	}
	
	/**
	 * Se timestamp e' passato a null --> si presuppone che sia l'istante corrente
	 * @param lista
	 * @param ts
	 * @return
	 */
	public final static <T extends SiacTBase> List<T> soloNonValidi(List<T> lista, Timestamp ts){
		List<T> nonValidi = new ArrayList<T>();
		if(ts==null){
			//Se timestamp e' passato a null --> si presuppone che sia l'istante corrente
			ts = new Timestamp(System.currentTimeMillis());
		}
		if(lista!=null){
			for(T it : lista){
				//System.out.println("it: " + it.getUid());
				
				if(!isValido(it, ts)){
					nonValidi.add(it);
				}
			}
		}
		return nonValidi;
	}
	
	public final static <T extends SiacTBase> boolean isValido(T oggetto, Timestamp ts){
		if(ts==null){
			//Se timestamp e' passato a null --> si presuppone che sia l'istante corrente
			ts = new Timestamp(System.currentTimeMillis());
		}
		
		Date dataCancellazione = oggetto.getDataCancellazione();
		if(dataCancellazione!=null && ts.after(dataCancellazione)){
			//e' cancellato
			return false;
		}
		
		Timestamp dataFineValidita = TimingUtils.convertiDataInTimeStamp(oggetto.getDataFineValidita());
		Timestamp dataInizioValidita = TimingUtils.convertiDataInTimeStamp(oggetto.getDataInizioValidita());
		if(ts.after(dataInizioValidita) && (dataFineValidita==null || dataFineValidita.after(ts))){
			return true;
		}
		return false;
	}
	
	public final static <T extends SiacTBase> boolean tuttiPresentiNellAltraLista(List<T> o1, List<T> o2){
		if(o1 != null && o1.size()>0 && o2 != null && o2.size()>0){
			for(T it : o1){
				T finded = DatiOperazioneUtils.getById(o2, it.getUid());
				if(finded==null){
					return false;
				}
			}
			for(T it : o2){
				T dentroListaNew = DatiOperazioneUtils.getById(o1, it.getUid());
				if(dentroListaNew==null){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * trova l'elemento con lo stesso uuid e lo sostituisce
	 * @param lista
	 * @param oggettoAggiornato
	 * @return
	 */
	public final static <T extends SiacTBase> List<T> replaceInList(List<T> lista, T oggettoAggiornato){
		if(lista != null && lista.size()>0 && oggettoAggiornato != null){
			Integer id = oggettoAggiornato.getUid();
			T finded = null;
			List<T> listaPrimaDiFinded = new ArrayList<T>();
			List<T> listaDopoDiFinded = new ArrayList<T>();
			for(T iterato : lista){
				if(iterato!=null && id!=null && iterato.getUid()!=null && iterato.getUid().intValue()==id.intValue()){
					finded = iterato;
				} else {
					if(finded!=null){
						listaDopoDiFinded.add(iterato);
					} else {
						listaPrimaDiFinded.add(iterato);
					}
				}
			}
			if(finded!=null){
				List<T> ricostruita = new ArrayList<T>();
				if(listaPrimaDiFinded!=null && listaPrimaDiFinded.size()>0){
					ricostruita.addAll(listaPrimaDiFinded);
				}
				ricostruita.add(finded);
				if(listaDopoDiFinded!=null && listaDopoDiFinded.size()>0){
					ricostruita.addAll(listaDopoDiFinded);
				}
				return ricostruita;
			}
		}
		return lista;
	}
	
	/**
	 * Considera anche l'essere entrambi null una condizione di uguaglianza
	 * @param s1
	 * @param s2
	 * @return
	 */
	public final static <T extends SiacTBase> boolean sonoUgualiAncheNull(T s1, T s2){
		if(s1==null && s2!=null){
			return false;
		}
		if(s1!=null && s2==null){
			return false;
		}
		if(s1==null && s2==null){
			return true;
		}
		if(s1.getUid()==null && s2.getUid()!=null){
			return false;
		}
		if(s1.getUid()!=null && s2.getUid()==null){
			return false;
		}
		if(s1.getUid()==null && s2.getUid()==null){
			return true;
		}
		if(s1.getUid().intValue()==s2.getUid().intValue()){
			return true;
		} else {
			return false;
		}
	}
	
}
