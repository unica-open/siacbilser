/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.CausaleEPDao;
import it.csi.siac.siacbilser.integration.dao.SiacDEventoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTCausaleEpRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.entity.SiacDConciliazioneClasse;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.CausaleEPStatoConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Data access delegate di un CausaleEPDad.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CausaleEPDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private CausaleEPDao causaleEPDao;
	
	@Autowired
	private SiacTCausaleEpRepository siacTCausaleEpRepository;
	@Autowired
	private SiacDEventoRepository siacDEventoRepository;
	@Autowired
	private SiacTClassDao siacTClassDao;
	
	@Autowired
	private CausaleEPStatoConverter causaleEPStatoConverter;

	/**
	 * Inserisci causaleEP.
	 *
	 * @param causaleEP the causaleEP
	 */
	public void inserisciCausaleEP(CausaleEP causaleEP) {
		SiacTCausaleEp siacTCausaleEp = buildSiacTCausaleEP(causaleEP);
		siacTCausaleEp.setLoginCreazione(loginOperazione);
	    siacTCausaleEp.setLoginModifica(loginOperazione);
		causaleEPDao.create(siacTCausaleEp);
		causaleEP.setUid(siacTCausaleEp.getUid());
		causaleEP.setLoginCreazione(siacTCausaleEp.getLoginCreazione());
		causaleEP.setDataCreazione(siacTCausaleEp.getDataCreazione());
		causaleEP.setLoginModifica(siacTCausaleEp.getLoginModifica());
		causaleEP.setDataModifica(siacTCausaleEp.getDataModifica());
	}
	

	/**
	 * Aggiorna causaleEP.
	 *
	 * @param causaleEP the 
	 */
	public void aggiornaCausaleEP(CausaleEP causaleEP) {
		SiacTCausaleEp siacTCausaleEp = buildSiacTCausaleEP(causaleEP);
		siacTCausaleEp.setLoginModifica(loginOperazione);
		SiacTCausaleEp siacTCausaleEpInserito = causaleEPDao.update(siacTCausaleEp);
		causaleEP.setUid(siacTCausaleEpInserito.getUid());
		causaleEP.setLoginCreazione(siacTCausaleEpInserito.getLoginCreazione());
		causaleEP.setDataCreazione(siacTCausaleEpInserito.getDataCreazione());
		causaleEP.setLoginModifica(siacTCausaleEpInserito.getLoginModifica());
		causaleEP.setDataModifica(siacTCausaleEpInserito.getDataModifica());
	}
	

	/**
	 * Elimina causale ep.
	 *
	 * @param causaleEP the causale ep
	 */
	public void eliminaCausaleEP(CausaleEP causaleEP) {
		final String methodName = "eliminaCausaleEP";
		SiacTCausaleEp siacTCausaleEp = new SiacTCausaleEp();
		siacTCausaleEp.setUid(causaleEP.getUid());
		siacTCausaleEp.setLoginOperazione(loginOperazione);
		siacTCausaleEp.setLoginCancellazione(loginOperazione);
		causaleEPDao.delete(siacTCausaleEp);
		log.debug(methodName, "eliminata causaleEP con uid:"+siacTCausaleEp.getUid());
	}


	
	/**
	 * Aggiorna stato operativo causale ep.
	 *
	 * @param causaleEP the causale ep
	 * @param statoOperativoCausaleEP the stato operativo causale ep
	 */
	public void aggiornaStatoOperativoCausaleEP(CausaleEP causaleEP, StatoOperativoCausaleEP statoOperativoCausaleEP) {
		String methodName = "aggiornaStatoOperativoCausaleEP";
		
		SiacTCausaleEp siacTCausaleEp = new SiacTCausaleEp(); //causaleEPDao.findById(causaleEP.getUid());
		causaleEP.setLoginOperazione(loginOperazione);
		siacTCausaleEp.setLoginOperazione(loginOperazione);
		siacTCausaleEp.setLoginModifica(loginOperazione);
		siacTCausaleEp.setUid(causaleEP.getUid());
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(ente.getUid());
		siacTCausaleEp.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTCausaleEp.setDataInizioValidita(causaleEP.getDataInizioValidita());
		
		causaleEP.setStatoOperativoCausaleEP(statoOperativoCausaleEP);
		causaleEPStatoConverter.convertTo(causaleEP, siacTCausaleEp);
		
		causaleEPDao.updateStato(siacTCausaleEp);
		log.debug(methodName, "stato causaleEP aggiornato [uid:"+siacTCausaleEp.getUid()+"]");
		
	}


	
	
	/**
	 * Find causaleEP by id.
	 *
	 * @param causaleEP the causaleEP
	 * @return the causaleEP
	 */
	public CausaleEP findCausaleEPById(CausaleEP causaleEP) {
		SiacTCausaleEp siacTCausaleEp = causaleEPDao.findById(causaleEP.getUid());
		mapNotNull(siacTCausaleEp,causaleEP,GenMapId.SiacTCausaleEp_CausaleEP);
		return causaleEP;
		
	}
	
	/**
	 * Find causaleEP by id.
	 *
	 * @param causaleEP the causaleEP
	 * @param causaleEPModelDetails the causale ep model details
	 * @return the causaleEP
	 */
	public CausaleEP findCausaleEPById(CausaleEP causaleEP,  CausaleEPModelDetail... causaleEPModelDetails) {
		String methodName = "findCausaleEPById";
		SiacTCausaleEp siacTCausaleEp = causaleEPDao.findById(causaleEP.getUid());
		if(siacTCausaleEp == null) {
			log.warn(methodName, "Impossibile trovare CausaleEP con uid: " + causaleEP.getUid());
		}
		if(causaleEPModelDetails==null){
			mapNotNull(siacTCausaleEp,causaleEP,GenMapId.SiacTCausaleEp_CausaleEP);
			return causaleEP;
		}
		mapNotNull(siacTCausaleEp,causaleEP,GenMapId.SiacTCausaleEp_CausaleEP_Base, Converters.byModelDetails(causaleEPModelDetails));
		return causaleEP;
	}
	
	/**
	 * Find causaleEP by id model detail.
	 *
	 * @param causaleEP the causaleEP
	 * @param causaleEPModelDetails the causale ep model details
	 * @return the causaleEP
	 */
	public CausaleEP findCausaleEPByIdModelDetail(CausaleEP causaleEP,  CausaleEPModelDetail... causaleEPModelDetails) {
		String methodName = "findCausaleEPByIdModelDetail";
		SiacTCausaleEp siacTCausaleEp = causaleEPDao.findById(causaleEP.getUid());
		if(siacTCausaleEp == null) {
			log.warn(methodName, "Impossibile trovare CausaleEP con uid: " + causaleEP.getUid());
		}
		mapNotNull(siacTCausaleEp, causaleEP, GenMapId.SiacTCausaleEp_CausaleEP_Minimal_Without_Ente, Converters.byModelDetails(causaleEPModelDetails));
		return causaleEP;
	}
	
	/**
	 * Builds the siac t pdce causaleEP.
	 *
	 * @param causaleEP the causale ep
	 * @return the siac t pdce causaleEP
	 */
	private SiacTCausaleEp buildSiacTCausaleEP(CausaleEP causaleEP) {
		SiacTCausaleEp siacTCausaleEp = new SiacTCausaleEp();
		causaleEP.setLoginOperazione(loginOperazione);
		siacTCausaleEp.setLoginOperazione(loginOperazione);
		causaleEP.setEnte(ente);
		map(causaleEP,siacTCausaleEp, GenMapId.SiacTCausaleEp_CausaleEP);
		return siacTCausaleEp;
	}
	
	
	
	/**
	 * Ricerca sintetica causaleEP.
	 *
	 * @param causaleEP the causaleEP
	 * @param tipoEvento the tipo evento
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<CausaleEP> ricercaSinteticaCausaleEP(CausaleEP causaleEP, TipoEvento tipoEvento, ParametriPaginazione parametriPaginazione) {
		
		ContoTipoOperazione cto = CollectionUtils.isNotEmpty(causaleEP.getContiTipoOperazione()) ? causaleEP.getContiTipoOperazione().get(0) : null;
		causaleEP.setContiTipoOperazione(null);
		Conto conto = cto != null ? cto.getConto() : null;
		Evento evento = CollectionUtils.isNotEmpty(causaleEP.getEventi()) ? causaleEP.getEventi().get(0) : null;
		causaleEP.setEventi(null);
		
		Page<SiacTCausaleEp> lista = causaleEPDao.ricercaSintetica(
				ente.getUid(),
				mapToAnno(causaleEP.getDataInizioValiditaFiltro()),
				SiacDAmbitoEnum.byAmbitoEvenNull(causaleEP.getAmbito()),
				causaleEP.getCodice(),
				causaleEP.getDescrizione(),
				SiacDCausaleEpTipoEnum.byTipoCausaleEvenNull(causaleEP.getTipoCausale()),
				SiacDCausaleEpStatoEnum.byStatoOperativoEvenNull(causaleEP.getStatoOperativoCausaleEP()),
				mapToUidIfNotZero(conto),
				mapToUidIfNotZero(tipoEvento),
				mapToUidIfNotZero(evento),
				mapToUidIfNotZero(causaleEP.getElementoPianoDeiConti()),
				mapToUidIfNotZero(causaleEP.getSoggetto()),
				//SIAC-8169
				SiacDConciliazioneClasseEnum.byClasseDiConciliazioneEvenNull(cto != null ? cto.getClasseDiConciliazione() : null),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, causaleEP, GenMapId.SiacTCausaleEp_CausaleEP_Base);
		
	}
	
	/**
	 * Ricerca sintetica causaleEP.
	 *
	 * @param causaleEP the causaleEP
	 * @param tipoEvento the tipo evento
	 * @param parametriPaginazione the parametri paginazione
	 * @param modelDetails the model details
	 * @return the lista paginata
	 */
	public ListaPaginata<CausaleEP> ricercaSinteticaCausaleEP(CausaleEP causaleEP, TipoEvento tipoEvento, ParametriPaginazione parametriPaginazione, CausaleEPModelDetail... modelDetails) {
		
		ContoTipoOperazione cto = CollectionUtils.isNotEmpty(causaleEP.getContiTipoOperazione()) ? causaleEP.getContiTipoOperazione().get(0) : null;
		Conto conto = cto != null ? cto.getConto() : null;
		Evento evento = CollectionUtils.isNotEmpty(causaleEP.getEventi())  ? causaleEP.getEventi().get(0) : null;
		
		Page<SiacTCausaleEp> lista = causaleEPDao.ricercaSintetica(
				ente.getUid(),
				mapToAnno(causaleEP.getDataInizioValiditaFiltro()),
				SiacDAmbitoEnum.byAmbitoEvenNull(causaleEP.getAmbito()),
				causaleEP.getCodice(),
				causaleEP.getDescrizione(),
				SiacDCausaleEpTipoEnum.byTipoCausaleEvenNull(causaleEP.getTipoCausale()),
				SiacDCausaleEpStatoEnum.byStatoOperativoEvenNull(causaleEP.getStatoOperativoCausaleEP()),
				mapToUidIfNotZero(conto),
				mapToUidIfNotZero(tipoEvento),
				mapToUidIfNotZero(evento),
				mapToUidIfNotZero(causaleEP.getElementoPianoDeiConti()),
				mapToUidIfNotZero(causaleEP.getSoggetto()),
				//SIAC-8169
				SiacDConciliazioneClasseEnum.byClasseDiConciliazioneEvenNull(cto != null ? cto.getClasseDiConciliazione() : null),
				toPageable(parametriPaginazione));
		
		CausaleEP cep = new CausaleEP();
		cep.setDataInizioValiditaFiltro(causaleEP.getDataInizioValiditaFiltro());
		return toListaPaginata(lista, cep, GenMapId.SiacTCausaleEp_CausaleEP_Minimal_Without_Ente, modelDetails);
	}
	


	/**
	 * Ricerca eventi per tipo.
	 *
	 * @param tipoEvento the tipo evento
	 * @return the list
	 */
	public List<Evento> ricercaEventiPerTipo(TipoEvento tipoEvento) {
		List<SiacDEvento> siacDEventos = siacTCausaleEpRepository.findSiacDEventoBySiacDEventoTipo(tipoEvento.getUid(), ente.getUid());
		return convertiLista(siacDEventos, Evento.class, GenMapId.SiacDEvento_Evento);
		
	}

	
	/**
	 * Ricerca causale ep by codice.
	 *
	 * @param causaleEP the causale ep
	 * @return the causale ep
	 */
	public CausaleEP ricercaCausaleEPByCodice(CausaleEP causaleEP) {
		String ambitoCode = SiacDAmbitoEnum.byAmbito(causaleEP.getAmbito()).getCodice();
		SiacTCausaleEp siacTCausaleEp = siacTCausaleEpRepository.findCausaleEPByCodiceAndEnteAndAmbitoCode(causaleEP.getCodice(), ente.getUid(), ambitoCode);
		return mapNotNull(siacTCausaleEp, CausaleEP.class, GenMapId.SiacTCausaleEp_CausaleEP_Minimal); //non tornerà le R perchè non è specificata la dataInizioValidita
	}
	
	/**
	 * Fin evento by uid.
	 *
	 * @param evento the evento
	 * @return the evento
	 */
	public Evento finEventoByUid(Evento evento) {
		SiacDEvento siacDEvento = siacDEventoRepository.findOne(evento.getUid());
		return mapNotNull(siacDEvento, Evento.class, GenMapId.SiacDEvento_Evento);
	}
	

	public List<ClassificatoreEP> ricercaClassificatoriEP() {
		Calendar cal = Calendar.getInstance();
		int anno = cal.get(Calendar.YEAR);
		
		Collection<String> classificatoreTipoCodes = new HashSet<String>();
		classificatoreTipoCodes.add(TipologiaClassificatore.VALORE_BENE.name());
		classificatoreTipoCodes.add(TipologiaClassificatore.MODALITA_AQUISIZIONE_BENE.name());
		classificatoreTipoCodes.add(TipologiaClassificatore.TIPO_DOCUMENTO_COLLEGATO.name());
		classificatoreTipoCodes.add(TipologiaClassificatore.TIPO_ONERE_FISCALE.name());
		classificatoreTipoCodes.add(TipologiaClassificatore.RILEVANTE_IVA.name());
		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByEnteAndClassifTipoCodes(ente.getUid(), anno, classificatoreTipoCodes);
		return convertiLista(siacTClasses, ClassificatoreEP.class, BilMapId.SiacTClass_ClassificatoreGenerico);
	}
	
	/**
	 * Ricerca Causali con conti indicati nella lista
	 *
	 * @return the lista delle causali che contengono queli conti
	 */
	public List<CausaleEP> findCausaleEpIdByConti(CausaleEP causaleEP) {
		
		//id dei conti
		Collection <Integer> contiIds = new ArrayList<Integer>() ;
		if(causaleEP.getContiTipoOperazione() != null) {
			for (ContoTipoOperazione cto :causaleEP.getContiTipoOperazione()){
				if (cto!=null && cto.getConto()!=null && cto.getConto().getUid()!=0) {
					contiIds.add(cto.getConto().getUid());
				}
			}
		}
		
		//id degli eventi
		Collection <Integer> eventoIds = new ArrayList<Integer>() ;
		if(causaleEP.getEventi() != null) {
			for (Evento evento :causaleEP.getEventi()){
				if (evento!=null && evento.getUid()!=0) {
					eventoIds.add(evento.getUid());
				}
			}
		}
		
		
		List<SiacTCausaleEp> siacTCausaleEps = siacTCausaleEpRepository.findCausaleEpByContiEPAndContiFIN(
					contiIds,
					Long.valueOf((long) contiIds.size()),
					causaleEP.getElementoPianoDeiConti().getUid(), 
					eventoIds,
					SiacDAmbitoEnum.byAmbito(causaleEP.getAmbito()).getCodice(),
					ente.getUid());
		
		return convertiLista(siacTCausaleEps, CausaleEP.class, GenMapId.SiacTCausaleEp_CausaleEP_Minimal);
		
	}


	/**
	 *  Ricerca le causali valide nell'anno di bilancio legate ad un certo tipo di evento e legate ad un classificatore passato in input
	 * @param eventoTipoCode il tipoEvento della causale
	 * @param uidClassificatore uid del classificatore legato alla causale
	 * @param bilancio bilancio in cui la causale deve essere valida
	 * @return la lista delle causali trovate
	 */
	public List<CausaleEP> ricercaCausaleEPByTipoEventoEClassificatore(String eventoTipoCode, Integer uidClassificatore, Bilancio bilancio) {
		List<SiacTCausaleEp> siacTCausaleEps = siacTCausaleEpRepository.findCausaleEPByTipoEventoEClassificatore(eventoTipoCode, uidClassificatore);
		List<CausaleEP> listaCausali = new ArrayList<CausaleEP>();
		if(siacTCausaleEps == null){
			return listaCausali;
		}
		for(SiacTCausaleEp c : siacTCausaleEps){
			CausaleEP causale = new CausaleEP();
			Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
			causale.setDataInizioValiditaFiltro(inizioAnno);
			mapNotNull(c, causale, GenMapId.SiacTCausaleEp_CausaleEP);
			listaCausali.add(causale);
		}
		return listaCausali;
	}

	public List<CausaleEP> ricercaMinimaCausaleEP(CausaleEP causaleEP) {
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(causaleEP.getAmbito());
		SiacDCausaleEpStatoEnum siacDCausaleEpStatoEnum = SiacDCausaleEpStatoEnum.byStatoOperativo(causaleEP.getStatoOperativoCausaleEP());
		SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum = SiacDCausaleEpTipoEnum.byTipoCausale(causaleEP.getTipoCausale());
		Evento evento = causaleEP.getEventi().get(0);
		
		List<SiacTCausaleEp> siacTCausaleEps = siacTCausaleEpRepository.findCausaleEPByEnteProprietarioIdAndAnnoAndAmbitoCodeAndCausaleEpStatoCodeAndCausaleEpTipoCode(causaleEP.getEnte().getUid(),
				/*mapToAnno(causaleEP.getDataInizioValiditaFiltro()), */siacDAmbitoEnum.getCodice(), siacDCausaleEpStatoEnum.getCodice(), siacDCausaleEpTipoEnum.getCodice(), evento.getUid());
		return convertiLista(siacTCausaleEps, CausaleEP.class, GenMapId.SiacTCausaleEp_CausaleEP_Minimal_Without_Ente);
	}
	
	public List<ClasseDiConciliazione> findClasseConciliazioneByCausale(CausaleEP causaleEP) {
		List<SiacDConciliazioneClasse> siacDConciliazioneClasses = siacTCausaleEpRepository.findClasseDiConciliazioneByCausale(causaleEP.getUid());
		
		List<ClasseDiConciliazione> classiConciliazione = new ArrayList<ClasseDiConciliazione>();
		if(siacDConciliazioneClasses == null){
			// Ritorno una lista vuota
			return classiConciliazione;
		}
		for (SiacDConciliazioneClasse siacDConciliazioneClasse : siacDConciliazioneClasses) {
			SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byCodice(siacDConciliazioneClasse.getConcclaCode());
			classiConciliazione.add(siacDConciliazioneClasseEnum.getClasseDiConciliazione());
		}
		
		return classiConciliazione;
	}
	
}
