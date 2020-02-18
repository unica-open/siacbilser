/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocIvaProtDefNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocIvaProtProvNumRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtDefNum;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtDefNumNoVersion;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtProvNum;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtProvNumNoVersion;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Data access delegate di un ContatoreRegistroIva.
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ContatoreRegistroIvaDad extends ExtendedBaseDadImpl {
	
	
	@Autowired
	private SiacTSubdocIvaProtProvNumRepository siacTSubdocIvaProtProvNumRepository;
	@Autowired
	private SiacTSubdocIvaProtDefNumRepository siacTSubdocIvaProtDefNumRepository;
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;

	
	/**
	 * Ottiene il numero di protocollo provvisorio di un subdocumento iva
	 *
	 * @param uidRegistroIva  uid del registro iva
	 * @param dataProtocolloProv data di protocollo provvisorio
	 * 
	 * @return  numeroProtocolloProv il numero protocollo provvisorio da assegnare al subdocumento iva
	 */
	//il comportamento diventa come il definitivo, cerco il periodo precedente
	public Integer staccaNumeroProtocolloProv(Integer uidRegistroIva, Date dataProtocolloProv) {
		final String methodName = "staccaNumeroProtocolloProv";
		log.debug(methodName, "uid registro iva " + uidRegistroIva);
		log.debug(methodName, "data protocollo provvisorio " + dataProtocolloProv);
		
		//il periodo (cioe' l'anno) deve essere determinato dalla data di protocollo, non dall'anno del subdocumento iva
//		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+annoEsercizio, ente.getUid()); //findByAnnoAndPeriodoTipoAndEnteProprietario(""+annoEsercizio, p.getCodice(), ente.getUid());
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dataProtocolloProv);
		int anno = gc.get(GregorianCalendar.YEAR);
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+anno, ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		SiacTSubdocIvaProtProvNum siacTSubdocIvaProtProvNum = siacTSubdocIvaProtProvNumRepository.findByIvaRegPeriodo(uidRegistroIva, siacTPeriodo.getUid());
		
		Date now = new Date();		
		if(siacTSubdocIvaProtProvNum == null) {		
			log.debug(methodName, "non esiste ancora un record per questo periodo, devo crearne uno nuovo ");
			siacTSubdocIvaProtProvNum = new SiacTSubdocIvaProtProvNum();
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTSubdocIvaProtProvNum.setSiacTEnteProprietario(siacTEnteProprietario);
			
			SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();
			siacTIvaRegistro.setUid(uidRegistroIva);
			siacTSubdocIvaProtProvNum.setSiacTIvaRegistro(siacTIvaRegistro);
	
			siacTSubdocIvaProtProvNum.setSiacTPeriodo(siacTPeriodo);	
	
			siacTSubdocIvaProtProvNum.setDataCreazione(now);
			siacTSubdocIvaProtProvNum.setDataInizioValidita(now);
			
			Integer numero = 1;//calcolaNumeroDaSiacTSubdocIvaProtProvNumPeriodoPiuRecente(p, annoEsercizio , uidRegistroIva);
			siacTSubdocIvaProtProvNum.setSubdocivaProtProv(numero); //La numerazione parte da 1		
		}else{
			log.debug(methodName, "esiste gia' un record per questo periodo, devo solo incrementere il contatore");
		}
		
		siacTSubdocIvaProtProvNum.setSubdocivaDataProtProv(dataProtocolloProv);
		siacTSubdocIvaProtProvNum.setLoginOperazione(loginOperazione);	
		siacTSubdocIvaProtProvNum.setDataModifica(now);	
		
		siacTSubdocIvaProtProvNumRepository.saveAndFlush(siacTSubdocIvaProtProvNum);
		
		Integer numeroProtocolloProv = siacTSubdocIvaProtProvNum.getSubdocivaProtProv();
		log.info(methodName, "returning numeroProtocolloProv: "+ numeroProtocolloProv);
		return numeroProtocolloProv;
	}

	/**
	 * Ottiene il numero di protocollo definitivo di un subdocumento iva
	 *
	 * @param uidRegistroIva  uid del registro iva
	 * @param dataProtocolloDef data di protocollo definitivo
	 * 
	 * @return  numeroProtocolloDef il numero protocollo definitivo da assegnare al subdocumento iva
	 */
	public Integer staccaNumeroProtocolloDef(Integer uidRegistroIva, Date dataProtocolloDef) {
		final String methodName = "staccaNumeroProtocolloDef";
		log.debug(methodName, "uid registro iva " + uidRegistroIva);
		log.debug(methodName, "data protocollo definitivo " + dataProtocolloDef);
		
//		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+annoEsercizio, ente.getUid()); //.findByAnnoAndPeriodoTipoAndEnteProprietario(""+annoEsercizio, p.getCodice(), ente.getUid());
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dataProtocolloDef);
		int anno = gc.get(GregorianCalendar.YEAR);
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+anno, ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		SiacTSubdocIvaProtDefNum siacTSubdocIvaProtDefNum = siacTSubdocIvaProtDefNumRepository.findByIvaRegPeriodo(uidRegistroIva, siacTPeriodo.getUid());
		
		Date now = new Date();		
		if(siacTSubdocIvaProtDefNum == null) {	
			log.debug(methodName, "non esiste ancora un record per questo periodo, devo crearne uno nuovo ");
			siacTSubdocIvaProtDefNum = new SiacTSubdocIvaProtDefNum();
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTSubdocIvaProtDefNum.setSiacTEnteProprietario(siacTEnteProprietario);
			
			SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();
			siacTIvaRegistro.setUid(uidRegistroIva);
			siacTSubdocIvaProtDefNum.setSiacTIvaRegistro(siacTIvaRegistro);
	
			siacTSubdocIvaProtDefNum.setSiacTPeriodo(siacTPeriodo);			
	
			siacTSubdocIvaProtDefNum.setDataCreazione(now);
			siacTSubdocIvaProtDefNum.setDataInizioValidita(now);
			
			Integer numero = 1; //calcolaNumeroDaSiacTSubdocIvaProtDefNumPeriodoPiuRecente(p, annoEsercizio , uidRegistroIva);
			siacTSubdocIvaProtDefNum.setSubdocivaProtDef(numero);
					
		}else{
			log.debug(methodName, "esiste gia' un record per questo periodo, devo solo incrementere il contatore");
		}
		
		siacTSubdocIvaProtDefNum.setSubdocivaDataProtDef(dataProtocolloDef);
		siacTSubdocIvaProtDefNum.setLoginOperazione(loginOperazione);	
		siacTSubdocIvaProtDefNum.setDataModifica(now);	
		
		siacTSubdocIvaProtDefNumRepository.saveAndFlush(siacTSubdocIvaProtDefNum);
		
		Integer numeroProtocolloDef = siacTSubdocIvaProtDefNum.getSubdocivaProtDef();
		log.info(methodName, "returning numeroProtocolloDef: "+ numeroProtocolloDef);
		return numeroProtocolloDef;
	}

	/**
	 * Restituisce la data di protocollo definitivo dell'ultimo subdocumento iva inserito, se presente, per un dato registro per l'anno della data di protocollo passato
	 * 
	 *  @param uidRegistroIva  uid del registro iva
	 *  @param anno  anno della data di protocollo
	 *  
	 *   @return la data di protocollo definitivo
	 */
	public Date findDataProtocolloDef(Integer uidRegistroIva, int anno){
		final String methodName = "findDataProtocolloDef";
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+anno, ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		Integer uidPeriodo = siacTPeriodo.getUid();
		
		log.debug(methodName, "ricerco con parametri uid registro: " + uidRegistroIva +", periodo: " + uidPeriodo);
		SiacTSubdocIvaProtDefNum siacTSubdocIvaProtDefNum = siacTSubdocIvaProtDefNumRepository.findByIvaRegPeriodo(uidRegistroIva, uidPeriodo);
		if(siacTSubdocIvaProtDefNum!=null){
			log.debug(methodName, "data trovata: " + siacTSubdocIvaProtDefNum.getSubdocivaDataProtDef());
			return siacTSubdocIvaProtDefNum.getSubdocivaDataProtDef();
		}
		log.debug(methodName, "non e' stato trovato nessun subdocumento iva con questi parametri");
		return null;
		
	}

	/**
	 * Restituisce la data di protocollo provvisorio dell'ultimo subdocumento iva inserito, se presente, per un dato registro per l'anno della data di protocollo passato
	 * 
	 *  @param uidRegistroIva  uid del registro iva
	 *  @param anno  anno della data di protocollo
	 *  
	 *  @return la data di protocollo provvisorio 
	 */
	public Date findDataProtocolloProvv(Integer uidRegistroIva,  int anno){
		final String methodName = "findDataProtocolloProvv";
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+anno, ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		Integer uidPeriodo = siacTPeriodo.getUid();
		
		log.debug(methodName, "ricerco con parametri uid registro: " + uidRegistroIva +", periodo: " + uidPeriodo);
		SiacTSubdocIvaProtProvNum siacTSubdocIvaProtProvNum = siacTSubdocIvaProtProvNumRepository.findByIvaRegPeriodo(uidRegistroIva, uidPeriodo);
		if(siacTSubdocIvaProtProvNum!=null){
			log.debugEnd(methodName, "data trovata: " + siacTSubdocIvaProtProvNum.getSubdocivaDataProtProv());
			return siacTSubdocIvaProtProvNum.getSubdocivaDataProtProv();
		}
		log.debug(methodName, "non e' stato trovato nessun subdocumento iva con questi parametri");
		return null;
	}
	
	/**
	 * Restituisce il numero di protocollo definitivo dell'ultimo subdocumento iva inserito, se presente, per registro e anno
	 * 
	 *  @param uidRegistroIva  uid del registro iva
	 *  @param anno  anno della data di protocollo
	 *  
	 *  @return il numero di protocollo definitivo
	 */
	public Integer findUltimoNumeroProtocolloDef(int uidRegistroIva, int anno/*, Periodo periodo*/){
		final String methodName = "findUltimoNumeroProtocolloDef";
		log.debug(methodName, "ricerco con parametri uid registro: " + uidRegistroIva + ", anno: " + anno + ", periodo: " + Periodo.ANNO.getCodice());
		SiacTSubdocIvaProtDefNum siacTSubdocIvaProtDefNum = siacTSubdocIvaProtDefNumRepository.findByIvaRegAnnoEsercizioPeriodoTipoCodiceEnte(uidRegistroIva, ""+anno, Periodo.ANNO.getCodice(), ente.getUid());
		if(siacTSubdocIvaProtDefNum!=null){
			log.debug(methodName, "trovato numero di protocollo definitivo:" + siacTSubdocIvaProtDefNum.getSubdocivaProtDef());
			return siacTSubdocIvaProtDefNum.getSubdocivaProtDef();		
		}
		log.debug(methodName, "non e' stato trovato nessun subdocumento iva con questi parametri");
		return null;
	}
	
	/**
	 * Restituisce il numero di protocollo provvisorio dell'ultimo subdocumento iva inserito, se presente, per registro e anno
	 * 
	 *  @param uidRegistroIva  uid del registro iva
	 *  @param anno  anno della data di protocollo
	 *  
	 *  @return il numero di protocollo provvisorio 
	 *  
	 */
	public Integer findUltimoNumeroProtocolloProvv(int uidRegistroIva, int anno/*, Periodo periodo*/){
		final String methodName = "findUltimoNumeroProtocolloProvv";
		log.debug(methodName, "ricerco con parametri uid registro: " + uidRegistroIva + ", anno: " + anno + ", periodo: " + Periodo.ANNO.getCodice());
		SiacTSubdocIvaProtProvNum siacTSubdocIvaProtProvNum = siacTSubdocIvaProtProvNumRepository.findByIvaRegAnnoEsercizioPeriodoTipoCodiceEnte(uidRegistroIva,
				""+anno, Periodo.ANNO.getCodice(), ente.getUid());
		if(siacTSubdocIvaProtProvNum!=null){
			log.debug(methodName, "trovato numero di protocollo provvisorio:" + siacTSubdocIvaProtProvNum.getSubdocivaProtProv());
			return siacTSubdocIvaProtProvNum.getSubdocivaProtProv();
		}
		log.debug(methodName, "non e' stato trovato nessun subdocumento iva con questi parametri");
		return null;
	}
	
	/** 
	 * Aggiorna la data e il numero di protocollo definitivo per un dato registroIva e anno con una nuova data e un nuovo numero passati in input
	 * 
	 * @param uidRegistroIva uid del registro iva
	 * @param nuovoUltimoProtDef nuovo numero protocollo da salvare
	 * @param nuovaDataProtocolloDef nuova data di protocollo da salvare
	 * @param anno anno del periodo da aggiornare 
	 */
	public void aggiornaDataENumProtDef(Integer uidRegistroIva, Integer nuovoUltimoProtDef , Date nuovaDataProtocolloDef, int anno) {
		final String methodName = "aggiornaDataProtocolloDef";
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(anno+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		SiacTSubdocIvaProtDefNumNoVersion siacTSubdocIvaProtDefNum = siacTSubdocIvaProtDefNumRepository.findByIvaRegPeriodoNoVersion(uidRegistroIva, siacTPeriodo.getUid());
		
		if(siacTSubdocIvaProtDefNum == null) {
			throw new IllegalArgumentException("Impossibile trovare il contatore registro iva definitivo");
		}
		//interessa salvare l'ultima data. quindi aggiorna solo se la data del doc è successiva a quella già salvata
		if(nuovaDataProtocolloDef.after(siacTSubdocIvaProtDefNum.getSubdocivaDataProtDef())){
			log.debug(methodName, "la nuova data è posteriore all'ultima salvata, la aggiorno");
			siacTSubdocIvaProtDefNum.setSubdocivaDataProtDef(nuovaDataProtocolloDef);
		}
		//idem per il numero, mi interessa il maggiore
		if(nuovoUltimoProtDef.compareTo(siacTSubdocIvaProtDefNum.getSubdocivaProtDef())>0){
			log.debug(methodName, "il nuovo numero è maggiore dell'ultimo salvato, lo aggiorno");
			siacTSubdocIvaProtDefNum.setSubdocivaProtDef(nuovoUltimoProtDef);
		}
		
	}
	
	/** 
	 * Aggiorna la data e il numero di protocollo provvisorio per un dato registroIva e anno con una nuova data e un nuovo numero passati in input
	 * 
	 * @param uidRegistroIva uid del registro iva
	 * @param nuovoUltimoProtProv nuovo numero protocollo da salvare
	 * @param nuovaDataProtocolloProv nuova data di protocollo da salvare
	 * @param anno anno del periodo da aggiornare 
	 */
	public void aggiornaDataENumProtProv(Integer uidRegistroIva, Integer nuovoUltimoProtProv, Date nuovaDataProtocolloProv, int anno) {
		final String methodName = "aggiornaDataENumProtProv";
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(anno+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		SiacTSubdocIvaProtProvNumNoVersion siacTSubdocIvaProtProvNum = siacTSubdocIvaProtProvNumRepository.findByIvaRegPeriodoNoVersion(uidRegistroIva, siacTPeriodo.getUid());
		
		if(siacTSubdocIvaProtProvNum == null) {
			throw new IllegalArgumentException("Impossibile trovare il contatore registro iva provvisorio");
		}
		//interessa salvare l'ultima data. quindi aggiorna solo se la data del doc è successiva a quella già salvata
		if(nuovaDataProtocolloProv.after(siacTSubdocIvaProtProvNum.getSubdocivaDataProtProv())){
			log.debug(methodName, "la nuova data è posteriore all'ultima salvata, la aggiorno");
			siacTSubdocIvaProtProvNum.setSubdocivaDataProtProv(nuovaDataProtocolloProv);
		}
		//idem per il numero, mi interessa il maggiore
		if(nuovoUltimoProtProv.compareTo(siacTSubdocIvaProtProvNum.getSubdocivaProtProv())>0){
			log.debug(methodName, "il nuovo numero è maggiore dell'ultimo salvato, lo aggiorno");
			siacTSubdocIvaProtProvNum.setSubdocivaProtProv(nuovoUltimoProtProv);
		}
		
	}

	public void inserisciContatoreProtProv(RegistroIva registroIva, Integer nuovoUltimoProtProv, Date dataProtProv, int anno) {
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(anno+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		
		Date now = new Date();
		SiacTSubdocIvaProtProvNum siacTSubdocIvaProtProvNum = new SiacTSubdocIvaProtProvNum();
		
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(ente.getUid());
		SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();
		siacTIvaRegistro.setUid(registroIva.getUid());
		
		siacTSubdocIvaProtProvNum.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTSubdocIvaProtProvNum.setSiacTIvaRegistro(siacTIvaRegistro);
		siacTSubdocIvaProtProvNum.setSiacTPeriodo(siacTPeriodo);	
		siacTSubdocIvaProtProvNum.setDataCreazione(now);
		siacTSubdocIvaProtProvNum.setDataInizioValidita(now);
		siacTSubdocIvaProtProvNum.setSubdocivaProtProv(nuovoUltimoProtProv); 
		siacTSubdocIvaProtProvNum.setSubdocivaDataProtProv(dataProtProv);
		siacTSubdocIvaProtProvNum.setLoginOperazione(loginOperazione);	
		siacTSubdocIvaProtProvNum.setDataModifica(now);
		
		siacTSubdocIvaProtProvNumRepository.saveAndFlush(siacTSubdocIvaProtProvNum);
		
	}
	
	public void inserisciContatoreProtDef(RegistroIva registroIva, Integer nuovoUltimoProtDef, Date dataProtDef, int anno) {
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(anno+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), anno));
		}
		
		Date now = new Date();
		SiacTSubdocIvaProtDefNum siacTSubdocIvaProtDefNum = new SiacTSubdocIvaProtDefNum();
		
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(ente.getUid());
		SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();
		siacTIvaRegistro.setUid(registroIva.getUid());
		
		siacTSubdocIvaProtDefNum.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTSubdocIvaProtDefNum.setSiacTIvaRegistro(siacTIvaRegistro);
		siacTSubdocIvaProtDefNum.setSiacTPeriodo(siacTPeriodo);			
		siacTSubdocIvaProtDefNum.setDataCreazione(now);
		siacTSubdocIvaProtDefNum.setDataInizioValidita(now);
		siacTSubdocIvaProtDefNum.setSubdocivaProtDef(nuovoUltimoProtDef);
		siacTSubdocIvaProtDefNum.setSubdocivaDataProtDef(dataProtDef);
		siacTSubdocIvaProtDefNum.setLoginOperazione(loginOperazione);	
		siacTSubdocIvaProtDefNum.setDataModifica(now);	
		
		siacTSubdocIvaProtDefNumRepository.saveAndFlush(siacTSubdocIvaProtDefNum);
		
	}

	
	public void aggiornaProtDef(RegistroIva registroIva, Integer nuovoUltimoProtDef, Bilancio bilancio) {
		String methodName = "aggiornaProtDef";
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(bilancio.getAnno()+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), bilancio.getAnno()));
		}
		log.debug(methodName, "Aggiorno il record per periodo " + siacTPeriodo.getUid() + " e registro " + registroIva.getUid() + ". Il nuovo numero di protocollo definitivo e' : " + nuovoUltimoProtDef);
		SiacTSubdocIvaProtDefNumNoVersion siacTSubdocIvaProtDefNum = siacTSubdocIvaProtDefNumRepository.findByIvaRegPeriodoNoVersion(registroIva.getUid(), siacTPeriodo.getUid());
		if(siacTSubdocIvaProtDefNum == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Numero di protocollo definitivo", " anno " + bilancio.getAnno() + " e registro " + registroIva.getUid()));
		}
		siacTSubdocIvaProtDefNum.setSubdocivaProtDef(nuovoUltimoProtDef);
		siacTSubdocIvaProtDefNum.setDataModificaAggiornamento(new Date());
	}
	
	public void aggiornaProtProv(RegistroIva registroIva, Integer nuovoUltimoProtProv, Bilancio bilancio) {
		String methodName = "aggiornaProtProv";
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(bilancio.getAnno()+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), bilancio.getAnno()+""));
		}
		log.debug(methodName, "Aggiorno il record per periodo " + siacTPeriodo.getUid() + "e registro " + registroIva.getUid() + ". Il nuovo numero di protocollo provvisorio e' : " + nuovoUltimoProtProv);
		SiacTSubdocIvaProtProvNumNoVersion siacTSubdocIvaProtProvNum = siacTSubdocIvaProtProvNumRepository.findByIvaRegPeriodoNoVersion(registroIva.getUid(), siacTPeriodo.getUid());
		if(siacTSubdocIvaProtProvNum == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Numero di protocollo provvisorio", " anno " + bilancio.getAnno() + " e registro " + registroIva.getUid()));
		}
		siacTSubdocIvaProtProvNum.setSubdocivaProtProv(nuovoUltimoProtProv);
		siacTSubdocIvaProtProvNum.setDataModificaAggiornamento(new Date());
	}
	
	public void aggiornaDataProtDef(RegistroIva registroIva, Date nuovaUltimaDataDef, Bilancio bilancio) {
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(bilancio.getAnno()+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), bilancio.getAnno()));
		}
		SiacTSubdocIvaProtDefNumNoVersion siacTSubdocIvaProtDefNum = siacTSubdocIvaProtDefNumRepository.findByIvaRegPeriodoNoVersion(registroIva.getUid(), siacTPeriodo.getUid());
		if(siacTSubdocIvaProtDefNum == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Numero di protocollo definitivo", " anno " + bilancio.getAnno() + " e registro " + registroIva.getUid()));
		}
		
		siacTSubdocIvaProtDefNum.setSubdocivaDataProtDef(nuovaUltimaDataDef);
		siacTSubdocIvaProtDefNum.setDataModificaAggiornamento(new Date());
	}
	
	public void aggiornaDataProtProv(RegistroIva registroIva, Date nuovaUltimaDataProv, Bilancio bilancio) {
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(bilancio.getAnno()+"", ente.getUid());
		if(siacTPeriodo == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Periodo " + Periodo.ANNO.getDescrizione(), bilancio.getAnno()+""));
		}
		SiacTSubdocIvaProtProvNumNoVersion siacTSubdocIvaProtProvNum = siacTSubdocIvaProtProvNumRepository.findByIvaRegPeriodoNoVersion(registroIva.getUid(), siacTPeriodo.getUid());
		if(siacTSubdocIvaProtProvNum == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Numero di protocollo provvisorio", " anno " + bilancio.getAnno() + " e registro " + registroIva.getUid()));
		}
		siacTSubdocIvaProtProvNum.setSubdocivaDataProtProv(nuovaUltimaDataProv);
		siacTSubdocIvaProtProvNum.setDataModificaAggiornamento(new Date());
	}
	

}
