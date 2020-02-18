/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.PrimaNotaDao;
import it.csi.siac.siacbilser.integration.dao.RegistrazioneMovFinDao;
import it.csi.siac.siacbilser.integration.dao.SiacDEventoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDEventoTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRPrimaNotaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovEpNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPrimaNotaNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPrimaNotaProgressivogiornaleNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPrimaNotaRateiRiscontiRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPrimaNotaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDAmbito;
import it.csi.siac.siacbilser.integration.entity.SiacDCollegamentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacDEventoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaRelTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRGsaClassifPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpNum;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaNum;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaProgressivogiornaleNum;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaRateiRisconti;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.PrimaNotaStatoOperativoConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.RateoRisconto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.Risconto;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoCollegamento;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * Data access delegate di una PrimaNota.
 * 
 * @author Valentina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
@Primary
public class PrimaNotaDad extends ExtendedBaseDadImpl {
	
	@Autowired
	protected SimpleJDBCFunctionInvoker fi;
	@Autowired
	protected PrimaNotaDao primaNotaDao;
	@Autowired
	private RegistrazioneMovFinDao registrazioneMovFinDao;
	@Autowired
	private SiacTPrimaNotaNumRepository siacTPrimaNotaNumRepository;
	@Autowired
	private SiacTPrimaNotaRepository siacTPrimaNotaRepository;
	@Autowired
	private SiacTPrimaNotaProgressivogiornaleNumRepository siacTPrimaNotaProgressivogiornaleNumRepository;
	@Autowired
	private SiacTMovEpNumRepository siacTMovEpNumRepository;
//	@Autowired
//	private SiacRPrimaNotaStatoRepository siacRPrimaNotaStatoRepository;
	@Autowired
	private SiacDEventoRepository siacDEventoRepository;
	@Autowired
	private SiacDEventoTipoRepository siacDEventoTipoRepository;
	@Autowired
	private SiacRPrimaNotaRepository siacRPrimaNotaRepository;
	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	@Autowired
	private SiacTPrimaNotaRateiRiscontiRepository siacTPrimaNotaRateiRiscontiRepository;
	@Autowired
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private EnumEntityFactory eef;
//	@Autowired
//	private PrimaNotaMovimentiEpConverter primaNotaMovimentiEpConverter;
	
	/**
	 * Inserisci primaNota.
	 *
	 * @param primaNota the primaNota
	 */
	public void inserisciPrimaNota(PrimaNota primaNota) {
		primaNota.setLoginCreazione(loginOperazione);
		SiacTPrimaNota siacTPrimaNota= buildSiacTPrimaNota(primaNota);
		siacTPrimaNota.setLoginModifica(loginOperazione);
		siacTPrimaNota.setLoginCreazione(loginOperazione);
		primaNotaDao.create(siacTPrimaNota);
		primaNota.setUid(siacTPrimaNota.getUid());
		//SIAC-5637
		
		aggiornaSoggettoPrimanota(primaNota.getUid());
	}
	
	/**
	 * aggiorna soggetto prima nota
	 * @param uidPrimaNota
	 */
	private void aggiornaSoggettoPrimanota(int uidPrimaNota) {
		final String methodName = "aggiornaSoggettoPrimanota";
		primaNotaDao.flush();
		List<Object[]> ris = fi.invokeFunctionToObjectArray("siac_fnc_aggiornasoggettoprimanota", Integer.valueOf(uidPrimaNota));		 

		log.debug(methodName, "elaborazione della funzione siac_fnc_aggiornasoggettoprimanota terminata con esito= "+ (ris!= null && !ris.isEmpty() ? ris.get(0).toString():"null"));		
		log.debug(methodName, "soggetto prima nota aggiornato");
	}

	
	/**
	 * Aggiorna primaNota.
	 *
	 * @param primaNota the primaNota
	 */
	public void aggiornaPrimaNota(PrimaNota primaNota) {
		primaNota.setLoginModifica(loginOperazione);
		SiacTPrimaNota siacTPrimaNota = buildSiacTPrimaNota(primaNota);
		siacTPrimaNota.setLoginModifica(loginOperazione);
		primaNotaDao.update(siacTPrimaNota);
		primaNota.setUid(siacTPrimaNota.getUid());
		//SIAC-5637
		
		aggiornaSoggettoPrimanota(primaNota.getUid());
	}
	
	
	/**
	 * Ottiene il numero progressivo nell'anno di una prima nota 
	 *
	 * @param annoPrimaNota l'anno
	 * @return numeroPrimaNota il numero della PrimaNota
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaNumeroPrimaNota(String annoPrimaNota, Ambito ambito) {
		final String methodName = "staccaNumeroPrimaNota";
		log.debug(methodName, loginOperazione);
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(ambito);
		SiacTPrimaNotaNum siacTPrimaNotaNum = siacTPrimaNotaNumRepository.findByAnnoAndAmbito(annoPrimaNota, siacDAmbitoEnum.getCodice(), ente.getUid());
		log.debug(methodName, "trovata siacTPrimaNotaNum " + (siacTPrimaNotaNum != null ? siacTPrimaNotaNum.getUid() : "null"));
		
		Date now = new Date();		
		if(siacTPrimaNotaNum == null) {			
			siacTPrimaNotaNum= new SiacTPrimaNotaNum();
			
			siacTPrimaNotaNum.setPnotaAnno(annoPrimaNota);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTPrimaNotaNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTPrimaNotaNum.setDataCreazione(now);
			siacTPrimaNotaNum.setDataInizioValidita(now);
			SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, ente.getUid(), SiacDAmbito.class);
			siacTPrimaNotaNum.setSiacDAmbito(siacDAmbito);
			//La numerazione parte da 1		
			siacTPrimaNotaNum.setPnotaNumero(1);
		}
		
		siacTPrimaNotaNum.setLoginOperazione(loginOperazione);	
		siacTPrimaNotaNum.setDataModifica(now);	
		
		siacTPrimaNotaNumRepository.saveAndFlush(siacTPrimaNotaNum);
		
		Integer numeroPrimaNota = siacTPrimaNotaNum.getPnotaNumero();
		log.info(methodName, "returning numeroPrimaNota: "+ numeroPrimaNota);
		return numeroPrimaNota;
	}
	
	
	/**
	 * 
	 * @param annoMovEp
	 * @return
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaNumeroMovimentoEP(Integer annoMovEp, Ambito ambito) {
		final String methodName = "staccaNumeroMovimentoEP";
		log.debug(methodName, loginOperazione);
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(ambito);
		SiacTMovEpNum siacTMovEpNum = siacTMovEpNumRepository.findByAnnoAndAmbito(annoMovEp, siacDAmbitoEnum.getCodice(), ente.getUid());
	
		Date now = new Date();		
		if(siacTMovEpNum == null) {			
			siacTMovEpNum= new SiacTMovEpNum();
			
			siacTMovEpNum.setMovepAnno(annoMovEp);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTMovEpNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTMovEpNum.setDataCreazione(now);
			siacTMovEpNum.setDataInizioValidita(now);
			
			SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, ente.getUid(), SiacDAmbito.class);
			siacTMovEpNum.setSiacDAmbito(siacDAmbito);
			
			//La numerazione parte da 1		
			siacTMovEpNum.setMovepCode(1);
		}
		
		siacTMovEpNum.setLoginOperazione(loginOperazione);	
		siacTMovEpNum.setDataModifica(now);	
		
		siacTMovEpNumRepository.saveAndFlush(siacTMovEpNum);
		
		Integer numeroMovEp = siacTMovEpNum.getMovepCode();
		log.info(methodName, "returning numero: "+ numeroMovEp);
		return numeroMovEp;
	}

	
	/**
	 * Builds the siacTPrimaNota
	 *
	 * @param conto the primaNota
	 * @return the siacTPrimaNota
	 */
	private SiacTPrimaNota buildSiacTPrimaNota(PrimaNota primaNota) {
		SiacTPrimaNota siacTPrimaNota = new SiacTPrimaNota();
		primaNota.setLoginOperazione(loginOperazione);
		siacTPrimaNota.setLoginOperazione(loginOperazione);
		primaNota.setEnte(ente);
		map(primaNota , siacTPrimaNota, GenMapId.SiacTPrimaNota_PrimaNota);
		return siacTPrimaNota;
	}
	

	/**
	 * Aggiorna lo stato di una prima nota
	 * @param uidPrimaNota uid della prima nota da aggiornare
	 * @param statoOperativoPrimaNota nuovo stato da salvare per la prima nota
	 */
	public void aggiornaStatoPrimaNota(Integer uidPrimaNota, StatoOperativoPrimaNota statoOperativoPrimaNota) {
		
//		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		SiacTPrimaNota siacTPrimaNota = em.find(SiacTPrimaNota.class, uidPrimaNota);
		if(siacTPrimaNota==null){
			throw new BusinessException("Impossibile trovare la PrimaNota con uid: "+ uidPrimaNota);
		}
		
		Date dataCancellazione = new Date();
		for(SiacRPrimaNotaStato r : siacTPrimaNota.getSiacRPrimaNotaStatos()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);					
		}
		Date now = new Date();
		SiacRPrimaNotaStato siacRPrimaNotaStato = new SiacRPrimaNotaStato();
		SiacDPrimaNotaStato siacDPrimaNotaStato = eef.getEntity(SiacDPrimaNotaStatoEnum.byStatoOperativo(statoOperativoPrimaNota), siacTPrimaNota.getSiacTEnteProprietario().getUid());
		siacRPrimaNotaStato.setSiacDPrimaNotaStato(siacDPrimaNotaStato);	
		siacRPrimaNotaStato.setSiacTPrimaNota(siacTPrimaNota);			
		siacRPrimaNotaStato.setSiacTEnteProprietario(siacTPrimaNota.getSiacTEnteProprietario());
		siacRPrimaNotaStato.setDataInizioValidita(now);
		siacRPrimaNotaStato.setDataCreazione(now);
		siacRPrimaNotaStato.setDataModifica(now);
		siacRPrimaNotaStato.setLoginOperazione(loginOperazione);		
		
//		siacRPrimaNotaStatoRepository.save(siacRPrimaNotaStato);
		em.persist(siacTPrimaNota);
		em.persist(siacRPrimaNotaStato);
		em.flush();
		em.clear();
	}
	
	
	/**
	 * Aggiorna una prima nota con i dati del libro giornale
	 * @param uidPrimaNota prima nota da aggiorare 
	 * @param dataRegistrazioneLibroGiornale data di registrazione del libro giornale
	 * @param numeroRegistrazioneLibroGiornale numero progressivo del libro giornale
	 */
	public void salvaDatiRegistrazioneLibroGiornale(Integer uidPrimaNota, Date dataRegistrazioneLibroGiornale, Integer numeroRegistrazioneLibroGiornale) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		if(siacTPrimaNota==null){
			throw new BusinessException("Impossibile trovare la PrimaNota con uid: "+ uidPrimaNota);
		}
		siacTPrimaNota.setPnotaDataregistrazionegiornale(dataRegistrazioneLibroGiornale);
		siacTPrimaNota.setPnotaProgressivogiornale(numeroRegistrazioneLibroGiornale);
		siacTPrimaNota.setDataModifica(new Date());
		siacTPrimaNotaRepository.save(siacTPrimaNota);
	}
	
	
	/**
	 * Cerca lo stato operativo di una prima nota
	 * 
	 * @param uidPrimaNota uid della prima nota
	 * @return  lo stato operativo trovato
	 */
	public StatoOperativoPrimaNota findStatoOperativoPrimaNota(Integer uidPrimaNota){
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		if(siacTPrimaNota==null){
			throw new BusinessException("Impossibile trovare la PrimaNota con uid: "+ uidPrimaNota);
		}
		
		PrimaNota primaNota = new PrimaNota(); 
		
		PrimaNotaStatoOperativoConverter primaNotaStatoOperativoConverter = new PrimaNotaStatoOperativoConverter();
		primaNotaStatoOperativoConverter.convertFrom(siacTPrimaNota, primaNota);
		
		return primaNota.getStatoOperativoPrimaNota();
		
	}
	
	
	/**
	 * Cerca i movimenti EP di una prima nota passata come parametro in input
	 * 
	 * @param uidPrimaNota
	 * @return lista dei mobìvimenti EP trovati
	 */
	public List<MovimentoEP> findMovimentiEPByPrimaNota(Integer uidPrimaNota){
		
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		
		if(siacTPrimaNota==null){
			throw new BusinessException("Impossibile trovare la PrimaNota con uid: "+ uidPrimaNota);
		}
		
		List<MovimentoEP> listaMovimentiEP = new ArrayList<MovimentoEP>();
		for(SiacTMovEp siacTMovEp: siacTPrimaNota.getSiacTMovEps()){
			if(siacTMovEp.getDataCancellazione() == null){
				MovimentoEP movimentoEp = map(siacTMovEp , MovimentoEP.class, GenMapId.SiacTMovEp_MovimentoEP_Medium);
				listaMovimentiEP.add(movimentoEp);
				
			}
		}
		return listaMovimentiEP;
	}
	
	
	/**
	 * Cerca il tipoCausale di una prima nota passata come parametro in input
	 * 
	 * @param uidPrimaNota
	 * @return il tipoCausale
	 */
	public TipoCausale findTipoCausalePrimaNota(Integer uidPrimaNota) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		if(siacTPrimaNota==null){
			throw new BusinessException("Impossibile trovare la PrimaNota con uid: "+ uidPrimaNota);
		}
		if(siacTPrimaNota.getSiacDCausaleEpTipo() == null){
			return null;
		}
		SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum = SiacDCausaleEpTipoEnum.byCodice(siacTPrimaNota.getSiacDCausaleEpTipo().getCausaleEpTipoCode());
		return siacDCausaleEpTipoEnum.getTipoCausale();
	}

	
	/**
	 * Ottiene l'anno di bilancio di una prima nota
	 * 
	 * @param uidPrimaNota uid della prima nota
	 * @return l'anno di bilancio
	 */
	public String findAnnoBilancio(Integer uidPrimaNota) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		if(siacTPrimaNota==null){
			throw new BusinessException("Impossibile trovare la PrimaNota con uid: "+ uidPrimaNota);
		}
		return siacTPrimaNota.getSiacTBil().getSiacTPeriodo().getAnno();
	}


	/**
	 * Ottiene un numero progressivo nell'anno di registrazione del libro giornale
	 * 
	 * @param annoPrimaNota anno della prima nota
	 * @return il numero di registrazione
	 */
	public Integer staccaNumeroRegistrazioneLibroGiornale(String annoPrimaNota, Ambito ambito) {
		final String methodName = "staccaNumeroRegistrazioneLibroGiornale";
		log.debug(methodName, loginOperazione);
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(ambito);
		SiacTPrimaNotaProgressivogiornaleNum siacTPrimaNotaProgressivogiornaleNum = siacTPrimaNotaProgressivogiornaleNumRepository.findByAnnoAndAmbito(annoPrimaNota, siacDAmbitoEnum.getCodice(), ente.getUid());
	
		Date now = new Date();		
		if(siacTPrimaNotaProgressivogiornaleNum == null) {			
			siacTPrimaNotaProgressivogiornaleNum= new SiacTPrimaNotaProgressivogiornaleNum();
			
			siacTPrimaNotaProgressivogiornaleNum.setPnotaAnno(annoPrimaNota);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTPrimaNotaProgressivogiornaleNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTPrimaNotaProgressivogiornaleNum.setDataCreazione(now);
			siacTPrimaNotaProgressivogiornaleNum.setDataInizioValidita(now);
			
			SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, ente.getUid(), SiacDAmbito.class);
			siacTPrimaNotaProgressivogiornaleNum.setSiacDAmbito(siacDAmbito);
			
			//La numerazione parte da 1		
			siacTPrimaNotaProgressivogiornaleNum.setPnotaProgressivogiornale(1);
		}
		
		siacTPrimaNotaProgressivogiornaleNum.setLoginOperazione(loginOperazione);	
		siacTPrimaNotaProgressivogiornaleNum.setDataModifica(now);	
		
		siacTPrimaNotaProgressivogiornaleNumRepository.saveAndFlush(siacTPrimaNotaProgressivogiornaleNum);
		
		Integer numeroProgressivoGiornale = siacTPrimaNotaProgressivogiornaleNum.getPnotaProgressivogiornale();
		log.info(methodName, "returning numeroProgressivoGiornale: "+ numeroProgressivoGiornale);
		return numeroProgressivoGiornale;
	}


	public PrimaNota findPrimaNotaByUid(Integer uidPrimaNota, GenMapId siacTPrimaNota_PrimaNota) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		PrimaNota primaNota = mapNotNull(siacTPrimaNota, PrimaNota.class, siacTPrimaNota_PrimaNota);
		return primaNota;
	}
	
	public PrimaNota findPrimaNotaDettaglioContoByUid(Integer uidPrimaNota) {
		return findPrimaNotaByUid(uidPrimaNota, GenMapId.SiacTPrimaNota_PrimaNota_DettaglioConto);
	}
	
	public PrimaNota findPrimaNotaByUid(Integer uidPrimaNota) {
		return findPrimaNotaByUid(uidPrimaNota, GenMapId.SiacTPrimaNota_PrimaNota);
	}
	
	public PrimaNota findPrimaNotaBaselByUid(Integer uidPrimaNota) {
		return findPrimaNotaByUid(uidPrimaNota, GenMapId.SiacTPrimaNota_PrimaNota_Base);
	}

	public PrimaNota findPrimaNotaByUid(Integer uidPrimaNota, PrimaNotaModelDetail... modelDetails) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(uidPrimaNota);
		PrimaNota primaNota = mapNotNull(siacTPrimaNota, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, Converters.byModelDetails(modelDetails));
		return primaNota;
	}

	public ListaPaginata<PrimaNota> ricercaSinteticaPrimeNote( Bilancio bilancio,
			PrimaNota primaNota, 
			List<Evento> eventi,
			CausaleEP causaleEP, 
			Conto conto, 
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa,
			Date dataRegistrazioneProvvisoriaA,
			BigDecimal importo,
			Missione missione,
			Programma programma,
			Cespite cespite,
			TipoEvento tipoEvento,
			ParametriPaginazione parametriPaginazione) {
		
		SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum = null;
		if(primaNota.getStatoOperativoPrimaNota() != null){
			siacDPrimaNotaStatoEnum = SiacDPrimaNotaStatoEnum.byCodice(primaNota.getStatoOperativoPrimaNota().getCodice());
		}
		SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum = null;
		if(primaNota.getTipoCausale() != null){
			siacDCausaleEpTipoEnum = SiacDCausaleEpTipoEnum.byCodice(primaNota.getTipoCausale().getCodice());
		}
		
		
		Page<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaSinteticaPrimaNota(
				ente.getUid(),
				bilancio.getUid(),
				SiacDAmbitoEnum.byAmbitoEvenNull(primaNota.getAmbito()),
				siacDCausaleEpTipoEnum,
				primaNota.getNumero(),
				primaNota.getNumeroRegistrazioneLibroGiornale(),
				siacDPrimaNotaStatoEnum,
				primaNota.getDescrizione(),
				projectToUid(eventi),
				causaleEP != null ? causaleEP.getUid() : null,
				conto != null ? conto.getUid() : null,
				dataRegistrazioneDa,
				dataRegistrazioneA,
				dataRegistrazioneProvvisoriaDa,
				dataRegistrazioneProvvisoriaA,
				importo,
				mapToUidIfNotZero(missione),
				mapToUidIfNotZero(programma),
				mapToUidIfNotZero(primaNota.getClassificatoreGSA()),				
				mapToUidIfNotZero(cespite),	
				mapToUidIfNotZero(tipoEvento),
				toPageable(parametriPaginazione)
				);
		
		return toListaPaginata(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota);
	}
	
	public ListaPaginata<PrimaNota> ricercaSinteticaPrimeNoteIntegrate(
			Bilancio bilancio, 
			PrimaNota primaNota, 
			Conto conto, 
			String tipoElenco,
			TipoEvento tipoEvento,
			Collection<Evento> eventi, 
			Integer annoMovimento, 
			String numeroMovimento,
			Integer numeroSubmovimento, 
			RegistrazioneMovFin registrazioneMovFin,
			CausaleEP causaleEP, 
			Date dataRegistrazioneDa, 
			Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa, 
			Date dataRegistrazioneProvvisoriaA,
			List<Integer> idsDocumenti, 
			
			//Nuovi filtri SIAC-4644
			AttoAmministrativo attoAmministrativo,
			//Nuovo SIAC-5799
			MovimentoGestione movimentoGestione,	
			MovimentoGestione subMovimentoGestione,					
			StrutturaAmministrativoContabile sac,
			
			Soggetto soggetto, 
			BigDecimal importoDocumentoDa, 
			BigDecimal importoDocumentoA,
			
			// SIAC-5291
			Capitolo<?, ?> capitolo,
			
			ParametriPaginazione parametriPaginazione) throws DadException {
		
		String methodName = "ricercaSinteticaPrimeNoteIntegrate";
		
		SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum = null;
		if(primaNota.getStatoOperativoPrimaNota() != null){
			siacDPrimaNotaStatoEnum = SiacDPrimaNotaStatoEnum.byCodiceEvenNull(primaNota.getStatoOperativoPrimaNota().getCodice());
		}
		
		boolean movimentoValorizzato = StringUtils.isNotBlank(numeroMovimento) && annoMovimento != null && annoMovimento != 0;
		log.debug(methodName, "movimento valorizzato? " + movimentoValorizzato);
		
		Set<Integer> idMovimento = findIdMovimenti(tipoEvento, eventi, annoMovimento, numeroMovimento, numeroSubmovimento);

		List<Integer> listaEventoTipoId = new ArrayList<Integer>();
		SiacDEventoFamTipoEnum siacDEventoFamTipoEnum = null;
		if("E".equals(tipoElenco)){
			siacDEventoFamTipoEnum = SiacDEventoFamTipoEnum.Entrata;
		}else if("S".equals(tipoElenco)){
			siacDEventoFamTipoEnum = SiacDEventoFamTipoEnum.Spesa;
		}
		if(siacDEventoFamTipoEnum != null && (tipoEvento == null || tipoEvento.getUid() == 0)){
			List<SiacDEventoTipoEnum> eventoTipoEnums = filtraTipoEventoEntrataSpesa(siacDEventoFamTipoEnum);
			listaEventoTipoId = popolaListaIdTipiEventoEntrataSpesa(eventoTipoEnums);
		}
		
		Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums = findTipiCollegamento(tipoEvento, eventi);
		/*
		String ris = "";
		String separatore = "";
		for(Integer id : idMovimento){
			log.info(methodName, ","+id);

			ris = ris+separatore+id;
			separatore = ",";
		}
		log.info(methodName, "campo_pk_id --> "+ris);
		*/
		
		Page<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaSinteticaNativaPrimaNotaIntegrata(
				ente.getUid(),
				bilancio.getUid(),
				siacDCollegamentoTipoEnums,
				SiacDAmbitoEnum.byAmbitoEvenNull(primaNota.getAmbito()),
				SiacDCausaleEpTipoEnum.Integrata,
				listaEventoTipoId,
				mapToUidIfNotZero(tipoEvento),
				projectToUid(eventi),
				primaNota.getNumero(),
				primaNota.getNumeroRegistrazioneLibroGiornale(),
				mapToUidIfNotZero(conto),
				idMovimento,
				siacDPrimaNotaStatoEnum,
				registrazioneMovFin != null ? mapToUidIfNotZero(registrazioneMovFin.getElementoPianoDeiContiAggiornato()) : null,
				primaNota.getDescrizione(),
				mapToUidIfNotZero(causaleEP),
				dataRegistrazioneDa,
				dataRegistrazioneA,
				dataRegistrazioneProvvisoriaDa,
				dataRegistrazioneProvvisoriaA,
				idsDocumenti,
				
				mapToUidIfNotZero(attoAmministrativo),
				//Nuovo SIAC-5799
				mapToUidIfNotZero(movimentoGestione),	
				mapToUidIfNotZero(subMovimentoGestione),					
				mapToUidIfNotZero(sac),
				
				mapToUidIfNotZero(soggetto), 
				importoDocumentoDa, 
				importoDocumentoA,	
				
				// SIAC-5291
				mapToUidIfNotZero(capitolo),
				// SIAC-5336
				mapToUidIfNotZero(primaNota.getClassificatoreGSA()),
						
				toPageable(parametriPaginazione)
				);
		
		return toListaPaginata(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota);
	}
	
	
	private void popolaListaIdMovimenti(List<Integer> idMovimento, Collection<SiacDCollegamentoTipo> siacDCollegamentoTipos, Integer annoMovimento, String numeroMovimento, Integer numeroSubmovimento) {
		String methodName= "popolaListaIdMovimenti";
		for(SiacDCollegamentoTipo siacDCollegamentoTipo : siacDCollegamentoTipos){
			SiacDCollegamentoTipoEnum tipoCollegamentoEnum = SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode());
			List<Integer> id = registrazioneMovFinDao.findListaIdMovimento(ente.getUid(), tipoCollegamentoEnum, annoMovimento, numeroMovimento, numeroSubmovimento);
			if(id != null){
				for(Integer i : id){
					
					if(i != null && !idMovimento.contains(i)){
						idMovimento.add(i);
						log.debug(methodName, "aggiunto idMovimento: " + i);
					}else{
						log.debug(methodName, "nessun movimento da aggiundere.");
					}
				}
			}
		}
	}


	protected List<SiacDEventoTipoEnum> filtraTipoEventoEntrataSpesa(SiacDEventoFamTipoEnum siacDEventoFamTipoEnum) {
		return filtraTipoEventoEntrataSpesa(siacDEventoFamTipoEnum, Arrays.asList(SiacDEventoTipoEnum.values()));
	}
	
	protected List<SiacDEventoTipoEnum> filtraTipoEventoEntrataSpesa(SiacDEventoFamTipoEnum siacDEventoFamTipoEnum, List<SiacDEventoTipoEnum> listaDaFiltrare) {
		String methodName = "filtraTipoEventoEntrataSpesa";
		log.debug(methodName, siacDEventoFamTipoEnum.getCodice());
		List<SiacDEventoTipoEnum> listaFiltrata =  new ArrayList<SiacDEventoTipoEnum>();
		
		
		for(SiacDEventoTipoEnum e : listaDaFiltrare){
			if(e.getSiacDEventoFamTipoEnum() != null && e.getSiacDEventoFamTipoEnum().equals(siacDEventoFamTipoEnum)){
				listaFiltrata.add(e);
			}
		}
		return listaFiltrata;
	}

	
	protected List<Integer> popolaListaIdTipiEventoEntrataSpesa(List<SiacDEventoTipoEnum> eventoTipoEnums) {
		List<Integer> listaId = new ArrayList<Integer>();
		for(SiacDEventoTipoEnum siacDEventoTipoEnum : eventoTipoEnums){
			SiacDEventoTipo siacDEventoTipo = eef.getEntity(siacDEventoTipoEnum, ente.getUid(), SiacDEventoTipo.class);
			listaId.add(siacDEventoTipo.getUid());
		}
		return listaId;
	}
	
	/**
	 * Popola id tipi evento by codici.
	 *
	 * @param codici the codici
	 * @return the list
	 */
	public List<TipoEvento> popolaTipiEventoByCodici(String... codici) {
		//carico il tipo dai codici
		List<SiacDEventoTipoEnum> eventoTipoEnums = SiacDEventoTipoEnum.byEventoTipoCodes(codici);
		List<TipoEvento> listaTipoEvento = new ArrayList<TipoEvento>();
		for(SiacDEventoTipoEnum siacDEventoTipoEnum : eventoTipoEnums){
			SiacDEventoTipo siacDEventoTipo = eef.getEntity(siacDEventoTipoEnum, ente.getUid(), SiacDEventoTipo.class);
			listaTipoEvento.add(map(siacDEventoTipo, TipoEvento.class, GenMapId.SiacDEventoTipo_TipoEvento));
		}
		return listaTipoEvento;
	}
	
	/**
	 * Popola id tipi evento by codici.
	 *
	 * @param tipoEvento the tipo evento
	 * @return the list
	 */
	public TipoEvento popolaTipoEventoByUid(TipoEvento tipoEvento) {
		if(tipoEvento == null || tipoEvento.getUid() == 0) {
			return null;
		}
		SiacDEventoTipo siacDEventoTipo = siacDEventoTipoRepository.findOne(tipoEvento.getUid());
		return map(siacDEventoTipo, TipoEvento.class, GenMapId.SiacDEventoTipo_TipoEvento);
	}


	private Collection<SiacDCollegamentoTipo> popolaListaTipoCollegamenti(List<SiacDEvento> siacDEventos) {
		String methodName = "popolaListaTipoCollegamenti";
		Map<Integer, SiacDCollegamentoTipo> siacDCollegamentoTipos = new HashMap<Integer, SiacDCollegamentoTipo>();
		
		for(SiacDEvento siacDEvento :siacDEventos){
			log.debug(methodName, "evento: " + siacDEvento.getEventoCode());
			SiacDCollegamentoTipo siacDCollegamentoTipo = siacDEvento.getSiacDCollegamentoTipo();
			siacDCollegamentoTipos.put(siacDCollegamentoTipo.getUid(), siacDCollegamentoTipo);
			log.debug(methodName, "siacDCollegamentoTipo" + siacDCollegamentoTipo.getCollegamentoTipoCode());
		}
		return siacDCollegamentoTipos.values();
	}


	public ListaPaginata<PrimaNota> ricercaSinteticaPrimeNoteIntegrateValidabili(
			Bilancio bilancio,
			PrimaNota primaNota,
			Conto conto,
			CausaleEP causaleEP,
			TipoEvento tipoEvento,
			Collection<Evento> eventi,
			Integer annoMovimento,
			String numeroMovimento,
			Integer numeroSubmovimento,
			RegistrazioneMovFin registrazioneMovFin,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			AttoAmministrativo attoAmministrativo,
			//Nuovo SIAC-5799
			MovimentoGestione movimentoGestione,	
			MovimentoGestione subMovimentoGestione,		
			StrutturaAmministrativoContabile strutturaAmministrativoContabile,
			// SIAC-5292
			Capitolo<?, ?> capitolo,
			ParametriPaginazione parametriPaginazione) throws DadException {
		
		Set<Integer> idMovimento = findIdMovimenti(tipoEvento, eventi, annoMovimento, numeroMovimento, numeroSubmovimento);
		Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums = findTipiCollegamento(tipoEvento, eventi);
		
		
		Page<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaSinteticaNativaPrimaNotaIntegrataValidabile(
				ente.getUid(),
				bilancio.getUid(),
				siacDCollegamentoTipoEnums,
				SiacDAmbitoEnum.byAmbitoEvenNull(primaNota.getAmbito()),
				SiacDCausaleEpTipoEnum.Integrata,
				mapToUidIfNotZero(tipoEvento),
				projectToUid(eventi),
				primaNota.getNumero(),
				primaNota.getNumeroRegistrazioneLibroGiornale(),
				mapToUidIfNotZero(conto),
				mapToUidIfNotZero(causaleEP),
				idMovimento,
				SiacDPrimaNotaStatoEnum.Provvisorio, //Lo stato e' obbligatoriamente PROVVISORIO
				registrazioneMovFin != null ? mapToUidIfNotZero(registrazioneMovFin.getElementoPianoDeiContiAggiornato()) : null,
				primaNota.getDescrizione(),
				mapToUidIfNotZero(primaNota.getSoggetto()),
				dataRegistrazioneDa,
				dataRegistrazioneA,
				mapToUidIfNotZero(attoAmministrativo),
				
				//Nuovo SIAC-5799
				mapToUidIfNotZero(movimentoGestione),	
				mapToUidIfNotZero(subMovimentoGestione),	
				mapToUidIfNotZero(strutturaAmministrativoContabile),

				mapToUidIfNotZero(capitolo),
				mapToUidIfNotZero(primaNota.getClassificatoreGSA()),
				toPageable(parametriPaginazione)
				);
		
		return toListaPaginata(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota);
	}


	protected Set<Integer> findIdMovimenti(TipoEvento tipoEvento, Collection<Evento> eventi, Integer annoMovimento, String numeroMovimento, Integer numeroSubmovimento) throws DadException {
		Set<Integer> res = new HashSet<Integer>();
		// Se non ci sono i dati del movimento o nessuno tra tipo evento ed evento e' valorizzato, non restituisco dati
		if(StringUtils.isBlank(numeroMovimento) || annoMovimento == null || annoMovimento.intValue() == 0 || (!entitaHasUid(tipoEvento) && !entitaHasUid(eventi))) {
			return res;
		}
		
		if(eventi != null) {
			// Ricerca per evento
			for(Evento evento : eventi) {
				if(entitaHasUid(evento)) {
					SiacDEvento siacDEvento = siacDEventoRepository.findOne(evento.getUid());
					SiacDCollegamentoTipo siacDCollegamentoTipo = siacDEvento.getSiacDCollegamentoTipo();
					SiacDCollegamentoTipoEnum tipoCollegamentoEnum = SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode());
					
					List<Integer> idMovimenti = registrazioneMovFinDao.findListaIdMovimento(ente.getUid(), tipoCollegamentoEnum, annoMovimento, numeroMovimento, numeroSubmovimento);
					res.addAll(idMovimenti);
				}
			}
			
		}
		
		if (res.isEmpty() && entitaHasUid(tipoEvento)) {
			List<SiacDCollegamentoTipo> siacDCollegamentoTipos = siacDEventoRepository.findSiacDCollegamentoTipoByEventoTipoId(tipoEvento.getUid());
			Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums = new HashSet<SiacDCollegamentoTipoEnum>();
			
			for(SiacDCollegamentoTipo siacDCollegamentoTipo : siacDCollegamentoTipos) {
				siacDCollegamentoTipoEnums.add(SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode()));
			}
			
			for(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum : siacDCollegamentoTipoEnums) {
				List<Integer> idMovimenti = registrazioneMovFinDao.findListaIdMovimento(ente.getUid(), siacDCollegamentoTipoEnum, annoMovimento, numeroMovimento, numeroSubmovimento);
				res.addAll(idMovimenti);
			}
		}
		if(res.isEmpty()) {
			throw new DadException("Nessun movimento corrispondente ai filtri utilizzati");
		}
		return res;
	}
	
	protected Set<SiacDCollegamentoTipoEnum> findTipiCollegamento(TipoEvento tipoEvento, Collection<Evento> eventi) {
		Set<SiacDCollegamentoTipoEnum> res = new HashSet<SiacDCollegamentoTipoEnum>();
		
		if(eventi != null) {
			// Ricerca per evento
			for(Evento evento : eventi) {
				if(entitaHasUid(evento)) {
					SiacDEvento siacDEvento = siacDEventoRepository.findOne(evento.getUid());
					SiacDCollegamentoTipo siacDCollegamentoTipo = siacDEvento.getSiacDCollegamentoTipo();
					if(siacDCollegamentoTipo != null) {
						SiacDCollegamentoTipoEnum tipoCollegamentoEnum = SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode());
						
						res.add(tipoCollegamentoEnum);
					}
				}
			}
			
		}
		if (res.isEmpty() && entitaHasUid(tipoEvento)) {
			List<SiacDCollegamentoTipo> siacDCollegamentoTipos = siacDEventoRepository.findSiacDCollegamentoTipoByEventoTipoId(tipoEvento.getUid());
			Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums = new HashSet<SiacDCollegamentoTipoEnum>();
			
			for(SiacDCollegamentoTipo siacDCollegamentoTipo : siacDCollegamentoTipos) {
				siacDCollegamentoTipoEnums.add(SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode()));
			}
			
			res.addAll(siacDCollegamentoTipoEnums);
		}
		return res;
	}
	
	/**
	 * Controlla se l'entit&agrave; fornita abbia l'uid valorizzato
	 * @param e l'entit&agrave; da controllare
	 * @return true se l'entit&agrave; &eacute; valorizzata con uid non zero
	 */
//	private boolean entitaHasUid(Entita e) {
//		return e != null && e.getUid() != 0;
//	}

	
	public ListaPaginata<PrimaNota> ricercaPrimeNote(PrimaNota primaNota,
			Integer annoPrimaNota, 
			TipoEvento tipoEvento,
			ElementoPianoDeiConti elementoPianoDeiConti, 
			Integer annoMovimento,
			String numeroMovimento, 
			Integer numeroSubmovimento,
			ParametriPaginazione parametriPaginazione) {
		
		String methodName = "ricercaPrimeNote";
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(primaNota.getAmbito());
		SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum = SiacDCausaleEpTipoEnum.byTipoCausaleEvenNull(primaNota.getTipoCausale());
		
		boolean movimentoValorizzato = StringUtils.isNotBlank(numeroMovimento) && annoMovimento != null && annoMovimento != 0;
		log.debug(methodName, "movimento valorizzato? " + movimentoValorizzato);
		List<Integer> idMovimento = new ArrayList<Integer>();
		SiacDEventoTipo siacDEventoTipo = (tipoEvento != null) ? siacDEventoTipoRepository.findOne(tipoEvento.getUid()) : null; 
		
		if(siacDEventoTipo != null && movimentoValorizzato){
			List<SiacDEvento> siacDEventos = siacDEventoTipo.getSiacDEventos();
			
			if(siacDEventos != null && !siacDEventos.isEmpty()){
				log.debug(methodName, "trovati " + siacDEventos.size() + " eventi con tipo " + siacDEventoTipo.getEventoTipoCode());
				
				Collection<SiacDCollegamentoTipo> siacDCollegamentoTipos = popolaListaTipoCollegamenti(siacDEventos);
				log.debug(methodName, "trovati in tutto: " + siacDCollegamentoTipos.size() + " collegamenti.");
				
				popolaListaIdMovimenti(idMovimento, siacDCollegamentoTipos, annoMovimento, numeroMovimento, numeroSubmovimento);
				if(idMovimento == null || idMovimento.isEmpty()){
					String stringaSubmovimento =  numeroSubmovimento == null ? "" : " e numero submovimento "+ numeroSubmovimento;
					throw new IllegalArgumentException("Nessun movimento trovato per anno "+ annoMovimento +" e numero "+ numeroMovimento +stringaSubmovimento);
				}
				log.debug(methodName, "trovati in tutto: " + idMovimento.size() + " id movimento.");
			}	
		}
		Page<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaPrimeNote(ente.getUid(),
				annoPrimaNota, 
				siacDCausaleEpTipoEnum,
				siacDEventoTipo,
				siacDAmbitoEnum, 
				primaNota.getNumeroRegistrazioneLibroGiornale(), 
				idMovimento, 
				elementoPianoDeiConti != null ? elementoPianoDeiConti.getCodice() : null,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota);
	}
	
	public PrimaNota ricercaDettaglioPrimaNotaIntegrata(PrimaNota primaNota, RegistrazioneMovFin registrazioneMovFin, Documento<?,?> documento, Ambito ambito, Evento evento, Set<StatoOperativoPrimaNota> stati) {
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbitoEvenNull(ambito);
		String ambitoCode = siacDAmbitoEnum != null ? siacDAmbitoEnum.getCodice() : null;
		
		List<String> eventoCodes = new ArrayList<String>();
		if(evento != null && StringUtils.isNotBlank(evento.getCodice())){
			SiacDEventoEnum siacDEventoEnum = SiacDEventoEnum.byEventoCodice(evento.getCodice());
			List<SiacDEventoEnum> eventiDaConsiderare = SiacDEventoEnum.byCollegamentoTipoECassaEconomale(siacDEventoEnum.getSiacDCollegamentoTipoEnum(), siacDEventoEnum.isCassaEconomale());
			for(SiacDEventoEnum sdee: eventiDaConsiderare){
				eventoCodes.add(sdee.getCodice());
			}
		}
		List<SiacDPrimaNotaStatoEnum> statiPrimaNota = new ArrayList<SiacDPrimaNotaStatoEnum>();
		for(StatoOperativoPrimaNota s : stati){
			statiPrimaNota.add(SiacDPrimaNotaStatoEnum.byStatoOperativo(s));
		}
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.ricercaDettaglioPrimaNotaIntegrata(
				primaNota != null ? primaNota.getUid() : null,
				registrazioneMovFin	!= null ? registrazioneMovFin.getUid() : null,
				documento != null ? documento.getUid() : null,
				ambitoCode,
				eventoCodes, 
				statiPrimaNota);
		return  mapNotNull(siacTPrimaNota, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota);
	}


	public List<PrimaNota> ricercaPrimeNoteCollegateFiglie(PrimaNota primaNota) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(primaNota.getUid());
		
		List<PrimaNota> listaPrimaNotaFiglie = new ArrayList<PrimaNota>();
		if(siacTPrimaNota.getSiacRPrimaNotaPadre() != null){
			for(SiacRPrimaNota r : siacTPrimaNota.getSiacRPrimaNotaPadre()){
				if(r.getDataCancellazione() == null){
					PrimaNota pn = new PrimaNota();
					pn.setUid(r.getSiacTPrimaNotaFiglio().getUid());
					listaPrimaNotaFiglie.add(pn);
				}
			}
		}
		return listaPrimaNotaFiglie;
	}


	public void eliminaCollegamentoTraPrimeNote(PrimaNota primaNotaPadre,PrimaNota primaNotaFiglia) {
		List<SiacRPrimaNota> siacRPrimaNotas = siacRPrimaNotaRepository.findLegamePrimeNoteByUidPadreEUidFiglio(primaNotaPadre.getUid(), primaNotaFiglia.getUid());
		Date now = new Date();
		if(siacRPrimaNotas != null){
			for(SiacRPrimaNota r : siacRPrimaNotas){
				r.setDataCancellazioneIfNotSet(now);
				siacRPrimaNotaRepository.saveAndFlush(r);
			}
		}
		
	}


	public void collegaPrimeNote(PrimaNota primaNotaPadre, PrimaNota primaNotaFiglia) {
		
		SiacRPrimaNota siacRPrimaNota = new SiacRPrimaNota();
		
		SiacTPrimaNota siacTPrimaNotaFiglio = new SiacTPrimaNota();
		siacTPrimaNotaFiglio.setUid(primaNotaFiglia.getUid());
		SiacTPrimaNota siacTPrimaNotaPadre = new SiacTPrimaNota();
		siacTPrimaNotaPadre.setUid(primaNotaPadre.getUid());
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(ente.getUid());
		
		
		siacRPrimaNota.setSiacTEnteProprietario(siacTEnteProprietario);
		siacRPrimaNota.setSiacTPrimaNotaFiglio(siacTPrimaNotaFiglio);
		siacRPrimaNota.setSiacTPrimaNotaPadre(siacTPrimaNotaPadre);
		siacRPrimaNota.setNote(primaNotaFiglia.getNoteCollegamento());
		siacRPrimaNota.setLoginOperazione(loginOperazione);
		siacRPrimaNota.setDataModificaInserimento(new Date());
		
		if(primaNotaFiglia.getTipoRelazionePrimaNota() != null && primaNotaFiglia.getTipoRelazionePrimaNota().getUid() != 0){
			SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = map(primaNotaFiglia.getTipoRelazionePrimaNota(), SiacDPrimaNotaRelTipo.class, GenMapId.SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota);
			siacRPrimaNota.setSiacDPrimaNotaRelTipo(siacDPrimaNotaRelTipo);
		}
		
		siacRPrimaNotaRepository.saveAndFlush(siacRPrimaNota);	
		
	}


	public boolean isCollegamentoPresente(Integer uidPrimaNotaPadre, Integer uidPrimaNotaFiglia) {
		List<SiacRPrimaNota> siacRPrimaNotas = siacRPrimaNotaRepository.findLegamePrimeNoteByUidPadreEUidFiglio(uidPrimaNotaPadre, uidPrimaNotaFiglia);
		return siacRPrimaNotas != null && !siacRPrimaNotas.isEmpty();
	}


	public TipoRelazionePrimaNota findTipoRelazioneByCodice(String string) {
		SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = siacRPrimaNotaRepository.findTipoRelazioneByCodice(string, ente.getUid());
		return mapNotNull(siacDPrimaNotaRelTipo, TipoRelazionePrimaNota.class, GenMapId.SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota);
	}


	public List<PrimaNota> findPrimeNoteDefinitiveAnnullate(PrimaNota primaNota) {
		List<SiacTPrimaNota> list = siacTPrimaNotaRepository.findPrimeNoteDefinitiveAnnullateByPrimaNota(primaNota.getUid());
		return convertiLista(list, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_Minimal);
	}


	
	/**
	 * Inserisci Rateo.
	 *
	 * @param primaNota the primaNota
	 */
	public void inserisciRateo(Rateo rateo) {
		SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti= buildSiacTPrimaNotaRateiRisconti(rateo);
		Date now = new Date();
		siacTPrimaNotaRateiRisconti.setDataModificaInserimento(now);
		
		SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = siacRPrimaNotaRepository.findTipoRelazioneByCodice(TipoRelazionePrimaNota.CODICE_RATEO, ente.getUid());
		siacTPrimaNotaRateiRisconti.setSiacDPrimaNotaRelTipo(siacDPrimaNotaRelTipo);
		
		SiacTPrimaNotaRateiRisconti result = siacTPrimaNotaRateiRiscontiRepository.saveAndFlush(siacTPrimaNotaRateiRisconti);
		rateo.setUid(result.getUid());
	
	}

	/**
	 * Aggiorna Rateo.
	 *
	 * @param primaNota the primaNota
	 */
	public void aggiornaRateo(Rateo rateo) {
		SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti = buildSiacTPrimaNotaRateiRisconti(rateo);
		Date now = new Date();
		siacTPrimaNotaRateiRisconti.setDataModificaAggiornamento(now);
		siacTPrimaNotaRateiRisconti.setUid(rateo.getUid());
		
		SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = siacRPrimaNotaRepository.findTipoRelazioneByCodice(TipoRelazionePrimaNota.CODICE_RATEO, ente.getUid());
		siacTPrimaNotaRateiRisconti.setSiacDPrimaNotaRelTipo(siacDPrimaNotaRelTipo);
		
		siacTPrimaNotaRateiRiscontiRepository.saveAndFlush(siacTPrimaNotaRateiRisconti);
	}
	
	/**
	 * Inserisci Risconto.
	 *
	 * @param primaNota the primaNota
	 */
	public void inserisciRisconto(Risconto risconto){
		SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti= buildSiacTPrimaNotaRateiRisconti(risconto);
		Date now = new Date();
		siacTPrimaNotaRateiRisconti.setDataModificaInserimento(now);
		
		SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = siacRPrimaNotaRepository.findTipoRelazioneByCodice(TipoRelazionePrimaNota.CODICE_RISCONTO, ente.getUid());
		siacTPrimaNotaRateiRisconti.setSiacDPrimaNotaRelTipo(siacDPrimaNotaRelTipo);
		
		SiacTPrimaNotaRateiRisconti result = siacTPrimaNotaRateiRiscontiRepository.saveAndFlush(siacTPrimaNotaRateiRisconti);
		risconto.setUid(result.getUid());
	
	}

	/**
	 * Aggiorna Risconto.
	 *
	 * @param primaNota the primaNota
	 */
	public void aggiornaRisconto(Risconto risconto) {
		SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti = buildSiacTPrimaNotaRateiRisconti(risconto);
		Date now = new Date();
		siacTPrimaNotaRateiRisconti.setDataModificaAggiornamento(now);
		siacTPrimaNotaRateiRisconti.setUid(risconto.getUid());
		
		SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo = siacRPrimaNotaRepository.findTipoRelazioneByCodice(TipoRelazionePrimaNota.CODICE_RISCONTO, ente.getUid());
		siacTPrimaNotaRateiRisconti.setSiacDPrimaNotaRelTipo(siacDPrimaNotaRelTipo);
		
		siacTPrimaNotaRateiRiscontiRepository.saveAndFlush(siacTPrimaNotaRateiRisconti);
	}
	
	/**
	 * Builds the siacTPrimaNota
	 *
	 * @param conto the primaNota
	 * @return the siacTPrimaNota
	 */
	private SiacTPrimaNotaRateiRisconti buildSiacTPrimaNotaRateiRisconti(Rateo rateo) {
		
		SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti = new SiacTPrimaNotaRateiRisconti();
		rateo.setLoginOperazione(loginOperazione);
		siacTPrimaNotaRateiRisconti.setLoginOperazione(loginOperazione);
		rateo.setEnte(ente);
		map(rateo , siacTPrimaNotaRateiRisconti, GenMapId.SiacTPrimaNotaRateiRisconti_Rateo);
		return siacTPrimaNotaRateiRisconti;
	}
	
	/**
	 * Builds the siacTPrimaNota
	 *
	 * @param conto the primaNota
	 * @return the siacTPrimaNota
	 */
	private SiacTPrimaNotaRateiRisconti buildSiacTPrimaNotaRateiRisconti(Risconto risconto) {
		
		SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti = new SiacTPrimaNotaRateiRisconti();
		risconto.setLoginOperazione(loginOperazione);
		siacTPrimaNotaRateiRisconti.setLoginOperazione(loginOperazione);
		risconto.setEnte(ente);
		map(risconto , siacTPrimaNotaRateiRisconti, GenMapId.SiacTPrimaNotaRateiRisconti_Risconto);
		return siacTPrimaNotaRateiRisconti;
	}


	public Capitolo<?, ?> findCapitoloByPrimaNotaRateoRisconto(PrimaNota primaNota) {
		Capitolo<?, ?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		int idCapitolo = 0;
		SiacTRegMovfin siacTRegMovfin = siacTPrimaNotaRepository.findRegMovFinNonAnnullataByPrimaNota(primaNota.getUid());
		RegistrazioneMovFin registrazioneMovFin = map(siacTRegMovfin, RegistrazioneMovFin.class, GenMapId.SiacTRegMovfin_RegistrazioneMovFin_Base);
		idCapitolo = caricaCapitoloByMovimento(registrazioneMovFin);
		
		capitolo.setUid(idCapitolo);
		return capitolo;
	}


	/**
	 * @param registrazioneMovFin
	 * @return
	 */
	private int caricaCapitoloByMovimento(RegistrazioneMovFin registrazioneMovFin) {
		int idCapitolo = 0;
		Entita movimento = registrazioneMovFin.getMovimento();
		if(movimento instanceof MovimentoGestione) {
			TipoCollegamento tipoCollegamento = registrazioneMovFin.getEvento().getTipoCollegamento();
			return caricaCapitolo((MovimentoGestione)movimento, tipoCollegamento);
		}
		if(movimento instanceof Liquidazione) {
			return caricaCapitolo((Liquidazione)movimento);
		}
		if(movimento instanceof Subdocumento<?, ?>) {
			return caricaCapitolo((Subdocumento<?,?>)movimento);
		}
		return idCapitolo;
	}


	/**
	 * @param movimento
	 * @param tipoCollegamento
	 * @return
	 */
	private int caricaCapitolo(MovimentoGestione movimento, TipoCollegamento tipoCollegamento) {
		int idCapitolo;
		boolean isTestata = TipoCollegamento.ACCERTAMENTO.equals(tipoCollegamento) || TipoCollegamento.IMPEGNO.equals(tipoCollegamento);
		if(isTestata){
			idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestId(movimento.getUid());
		}else{
			idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestTsId(movimento.getUid());
		}
		return idCapitolo;
	}
	
	/**
	 * @param movimento
	 * @param tipoCollegamento
	 * @return
	 */
	private int caricaCapitolo(Liquidazione movimento) {
		return siacTLiquidazioneRepository.findIdCapitoloByLiquidazione(movimento.getUid());
	}
	
	/**
	 * @param movimento
	 * @param tipoCollegamento
	 * @return
	 */
	private int caricaCapitolo(Subdocumento<?, ?> movimento) {
		SiacTMovgestT siacTMovgestT = siacTSubdocRepository.findMovgestTSByIdSubdoc(movimento.getUid());
		int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestTsId(siacTMovgestT.getUid());
		return idCapitolo;
	}
	

	public ListaPaginata<Entita> ottieniEntitaCollegatePrimaNota(PrimaNota primaNota, TipoCollegamento tipoCollegamento, ModelDetail[] modelDetails, ParametriPaginazione parametriPaginazione) {
		final String methodName = "ottieniEntitaCollegatePrimaNota";
		List<Entita> res = new ArrayList<Entita>();
		SiacDCollegamentoTipoEnum sdcte = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		if(sdcte.getModelDetailClass() != null) {
			for(ModelDetail md : modelDetails) {
				if(!sdcte.getModelDetailClass().isInstance(md)) {
					String str = "Il model detail " + md + " [classe " + md.getClass().getSimpleName() + "] non corrisponde alla famiglia richiesta dal tipo di collegamento (" + sdcte.getModelDetailClass().getSimpleName() + ")";
					log.warn(methodName, str);
					throw new IllegalArgumentException(str);
				}
			}
		}
		
		Page<Integer> uids = primaNotaDao.ricercaUidSiacTBaseByPrimaNota(primaNota.getUid(), sdcte, toPageable(parametriPaginazione));
		
		for(Integer uid : uids) {
			SiacTBase siacTBase = registrazioneMovFinDao.ricercaMovimentoById(sdcte.getEntityClass().getSimpleName(), sdcte.getIdColumnName(), uid);
			if(siacTBase == null) {
				throw new IllegalStateException("Nessun movimento di tipo " + sdcte.getModelClass().getSimpleName() 
						+ " [uid: " + uid + "] collegato alla prima nota [uid: " + primaNota.getUid() + "]");
			}
			Entita movimento = sdcte.getModelInstance();
			if(sdcte.getModelDetailClass() != null) {
				map(siacTBase, movimento, sdcte.getMapIdModelDetail(), Converters.byModelDetails(modelDetails));
			} else {
				map(siacTBase, movimento, sdcte.getMapIdModelDetail());
			}
			res.add(movimento);
		}
		
		return toListaPaginata(res,uids.getTotalElements(), parametriPaginazione);
	}
	
	public BigDecimal ottieniImportoDareMovimenitiEPPrimaNota(PrimaNota primaNota){
		return ottieniImportoMovimentiEPPrimaNota(primaNota, SiacDOperazioneEpEnum.SegnoContoDare);
	}
	
	public BigDecimal ottieniImportoAvereMovimenitiEPPrimaNota(PrimaNota primaNota){
		return ottieniImportoMovimentiEPPrimaNota(primaNota, SiacDOperazioneEpEnum.SegnoContoAvere);
	}

	private BigDecimal ottieniImportoMovimentiEPPrimaNota(PrimaNota primaNota, SiacDOperazioneEpEnum siacDOperazioneEpEnum) {
		String methodName = "ottieniImportoMovimentiEPPrimaNota";
		BigDecimal result = siacTPrimaNotaRepository.findSumImportoMovimentiEP(primaNota.getUid(), siacDOperazioneEpEnum.getDescrizione());
		log.debug(methodName, "result: "+result + " primaNota.uid: "+primaNota.getUid() +" siacDOperazioneEpEnum:"+siacDOperazioneEpEnum);
		return result;
	}
	
	public BigDecimal ottieniImportoMovimentoPrimaNota(PrimaNota primaNota){
		return ottieniImportoMovimentiEPPrimaNota(primaNota, SiacDOperazioneEpEnum.SegnoContoAvere);
	}
	
	public Integer ottieniIdPrimaNotaRiscontoByRisconto(RateoRisconto risconto) {
		Integer uidPrimaNota = siacTPrimaNotaRateiRiscontiRepository.findSiacTPrimaNotaRateoRiscontoBySiacTPrimaNotaRateiRiscontiId(risconto.getUid(),SiacDCollegamentoTipoEnum.Risconto.getCollegamentoTipoCode());
		return uidPrimaNota ;
	}


	public void associaClassificatoreGSA(PrimaNota primaNota, ClassificatoreGSA classificatoreGSA) {
		SiacTPrimaNota siacTPrimaNota = em.find(SiacTPrimaNota.class, primaNota.getUid());
		if(siacTPrimaNota==null){
			throw new BusinessException("Impossibile trovare la PrimaNota con uid: " + primaNota.getUid());
		}
		SiacTGsaClassif siacTGsaClassif = em.find(SiacTGsaClassif.class, classificatoreGSA.getUid());
		if(siacTGsaClassif==null){
			throw new BusinessException("Impossibile trovare il ClassificatoreGSA con uid: " + classificatoreGSA.getUid());
		}
		
		Date now = new Date();
		
		SiacRGsaClassifPrimaNota siacRGsaClassifPrimaNota = new SiacRGsaClassifPrimaNota();
		siacRGsaClassifPrimaNota.setSiacTGsaClassif(siacTGsaClassif);
		siacRGsaClassifPrimaNota.setSiacTPrimaNota(siacTPrimaNota);
		siacRGsaClassifPrimaNota.setSiacTEnteProprietario(siacTPrimaNota.getSiacTEnteProprietario());
		siacRGsaClassifPrimaNota.setDataModificaInserimento(now);
		siacRGsaClassifPrimaNota.setLoginOperazione(loginOperazione);
		
		em.persist(siacRGsaClassifPrimaNota);
	}
	
	/**
	 * Aggiorna stato operativi prima nota.
	 *
	 * @param primaNota the prima nota
	 * @param statoOperativoPrimaNota the stato operativo prima nota
	 * @param statoAccettazionePrimaNotaDefinitiva the stato accettazione prima nota definitiva
	 * @return the prima nota
	 */
	public void aggiornaStatoOperativoPrimaNota(PrimaNota primaNota, StatoOperativoPrimaNota statoOperativoPrimaNota, StatoAccettazionePrimaNotaDefinitiva statoAccettazionePrimaNotaDefinitiva) {
		primaNotaDao.aggiornaStatoPrimaNota(primaNota.getUid(), loginOperazione, statoOperativoPrimaNota, statoAccettazionePrimaNotaDefinitiva);
		primaNota.setStatoOperativoPrimaNota(statoOperativoPrimaNota);
		primaNota.setStatoAccettazionePrimaNotaDefinitiva(statoAccettazionePrimaNotaDefinitiva);
	}
	
	/**
	 * Carica evento prima nota integrata.
	 *
	 * @param primaNota the prima nota
	 * @return the evento
	 */
	public Evento caricaEventoPrimaNotaIntegrata(PrimaNota primaNota) {
		List<SiacDEvento> siacDeventos = siacTPrimaNotaRepository.findEventoPrimaNotaIntegrata(primaNota.getUid());
		if(siacDeventos == null || siacDeventos.isEmpty()) {
			return null;
		}
		return mapNotNull(siacDeventos.get(0), Evento.class, GenMapId.SiacDEvento_Evento);
	}
	
	/**
	 * Carica evento prima nota libera.
	 *
	 * @param primaNota the prima nota
	 * @param dataInizioValiditaFiltro the data inizio validita filtro
	 * @return the evento
	 */
	public Evento caricaEventoPrimaNotaLibera(PrimaNota primaNota, Date dataInizioValiditaFiltro) {
		// deve essere coerente con quanto presente nella classe: CausaleEPEventoConverter
		List<SiacDEvento> siacDeventos = siacTPrimaNotaRepository.findEventoPrimaNotaLibera(primaNota.getUid(), dataInizioValiditaFiltro);
		if(siacDeventos == null || siacDeventos.isEmpty()) {
			return null;
		}
		return mapNotNull(siacDeventos.get(0), Evento.class, GenMapId.SiacDEvento_Evento);
	}
	
	public List<PrimaNota> ricercaPrimeNoteCollegateFiglie(PrimaNota primaNota, PrimaNotaModelDetail... modelDetails) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(primaNota.getUid());
		
		List<PrimaNota> listaPrimaNotaFiglie = new ArrayList<PrimaNota>();
		if(siacTPrimaNota.getSiacRPrimaNotaPadre() != null){
			for(SiacRPrimaNota r : siacTPrimaNota.getSiacRPrimaNotaPadre()){
				if(r.getDataCancellazione() == null){
					PrimaNota pn = mapNotNull(r.getSiacTPrimaNotaFiglio(), PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, Converters.byModelDetails(modelDetails));
					listaPrimaNotaFiglie.add(pn);
				}
			}
		}
		return listaPrimaNotaFiglie;
	}
	
	public List<PrimaNota> ricercaPrimeNoteCollegatePadre(PrimaNota primaNota, PrimaNotaModelDetail... modelDetails) {
		SiacTPrimaNota siacTPrimaNota = primaNotaDao.findById(primaNota.getUid());
		
		List<PrimaNota> listaPrimaNotaFiglie = new ArrayList<PrimaNota>();
		if(siacTPrimaNota.getSiacRPrimaNotaFiglio() != null){
			for(SiacRPrimaNota r : siacTPrimaNota.getSiacRPrimaNotaFiglio()){
				if(r.getDataCancellazione() == null){
					PrimaNota pn = mapNotNull(r.getSiacTPrimaNotaFiglio(), PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, Converters.byModelDetails(modelDetails));
					listaPrimaNotaFiglie.add(pn);
				}
			}
		}
		return listaPrimaNotaFiglie;
	}
}
