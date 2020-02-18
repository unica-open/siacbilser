/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.AttoAmministrativoModelDetail;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDAttoAmmStatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDAttoAmmTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTAttoAmmRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.dao.SiacTNumeroAttoRepository;
import it.csi.siac.siacbilser.integration.dao.provvedimento.SiacTAttoAmmDao;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmStato;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmStatoInOut;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTNumeroAtto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTNumeroAttoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.AttMapId;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.exception.AttoNonTrovatoException;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

// TODO: Auto-generated Javadoc
/**
 * The Class ProvvedimentoDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProvvedimentoDad extends ExtendedBaseDadImpl {
	
	/** The siac t atto amm repository. */
	@Autowired
	private SiacTAttoAmmRepository siacTAttoAmmRepository;
	
	/** The siac t atto amm dao. */
	@Autowired
	private SiacTAttoAmmDao siacTAttoAmmDao;
	
	/** The siac t class dao. */
	@Autowired
	private SiacTClassDao siacTClassDao;
	
	/** The siac r atto amm stato repository. */
	@Autowired
	private SiacDAttoAmmStatoRepository siacRAttoAmmStatoRepository;
	
	/** The siac d atto amm tipo repository. */
	@Autowired
	private SiacDAttoAmmTipoRepository siacDAttoAmmTipoRepository;
	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	/** The siac t numero atto repository. */
	@Autowired
	private SiacTNumeroAttoRepository siacTNumeroAttoRepository;
	
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Find provvedimento by id.
	 *
	 * @param id the id
	 * @return the atto amministrativo
	 */
	public AttoAmministrativo findProvvedimentoById(Integer id) {
		final String methodName = "findProvvedimentoById";
		log.debug(methodName, "uid provvedimento" + id);
		SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.findOne(id);
		if(siacTAttoAmm == null){
			log.debug(methodName, "ho trovato atto amministrativo null");
			return null;
		}
		log.debug(methodName, "siacTAttoAmm: " + siacTAttoAmm.getUid());
		AttoAmministrativo attoAmministrativo = buildAttoAmministrativo(siacTAttoAmm);
		log.debug(methodName, "attoAmministrativo: " + attoAmministrativo.getUid());
		return attoAmministrativo;
	}
	
	/**
	 * Find provvedimento by id.
	 *
	 * @param id the id
	 * @return the atto amministrativo
	 */
	public AttoAmministrativo findProvvedimentoByIdAndModelDetail(Integer id, AttoAmministrativoModelDetail... modelDetails) {
		final String methodName = "findProvvedimentoById";
		log.debug(methodName, "uid provvedimento" + id);
		SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.findOne(id);
		return mapNotNull(siacTAttoAmm, AttoAmministrativo.class, BilMapId.SiacTAttoAmm, Converters.byModelDetails(modelDetails));
	}

	/**
	 * Ricerca.
	 *
	 * @param ricerca the ricerca
	 * @return the list
	 */
	public List<AttoAmministrativo> ricerca(RicercaAtti ricerca/*, String loginOperazione*/) {
		
		List<AttoAmministrativo> elencoAttiLeggeTrovati = new ArrayList<AttoAmministrativo>();
		
		List<SiacTAttoAmm> result = siacTAttoAmmRepository.ricercaProvvedimento(siacTEnteProprietario,
				mapToString(ricerca.getAnnoAtto(), ""),
				ricerca.getOggetto(),
				(ricerca.getNumeroAtto() == null) ? Integer.valueOf(-1) : ricerca.getNumeroAtto(),
				ricerca.getNote(),
				(ricerca.getTipoAtto() != null && ricerca.getTipoAtto().getUid() != 0) ? ricerca.getTipoAtto().getUid() : -1,
				//loginOperazione,
				(ricerca.getUid() == null) ? Integer.valueOf(-1) : ricerca.getUid(),
				(ricerca.getStrutturaAmministrativoContabile() != null && ricerca.getStrutturaAmministrativoContabile().getUid() != 0) ? ricerca.getStrutturaAmministrativoContabile().getUid() : -1,
				ricerca.getStatoOperativo(),
				ricerca.isConDocumentoAssociato(),
				//SIAC-6929
				(ricerca.getBloccoRagioneria()==null) ? null : ricerca.getBloccoRagioneria(),
				(ricerca.getProvenienza()==null) ? null : (ricerca.getProvenienza().equals("MANUALE") ? "MANUALE" : String.valueOf(-1)) 
				);
		
		for (SiacTAttoAmm atto : result) {			
			elencoAttiLeggeTrovati.add(buildAttoAmministrativo(atto));
		}
		
		log.debug("ricerca", "dimensione result: "+result.size());
		return elencoAttiLeggeTrovati;
	
	}
	

	/**
	 * Creates the.
	 * NOTA: questo metodo andrebbe riscritto e ristrutturato!
	 * 
	 * @param attoAmministrativo the atto amministrativo
	 * @param strutturaAmministrativoContabile the struttura amministrativo contabile
	 * @param tipoAtto the tipo atto
	 * @return the atto amministrativo
	 */
	public AttoAmministrativo create(AttoAmministrativo attoAmministrativo,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile,
			TipoAtto tipoAtto) {
		
		// Correzioni dei dati nel caso sforino
		attoAmministrativo.setOggetto(StringUtils.abbreviate(attoAmministrativo.getOggetto(), 500));
		attoAmministrativo.setNote(StringUtils.abbreviate(attoAmministrativo.getNote(), 500));
		
		
		//creo l'atto amministrativo da salvare
		SiacTAttoAmm attoDaSalvare = new SiacTAttoAmm();
		attoAmministrativo.setLoginOperazione(loginOperazione);
		attoDaSalvare.setLoginOperazione(loginOperazione);
		map(attoAmministrativo, attoDaSalvare, AttMapId.SiacTAttoAmm);
		
		
		
		//recupero il tipo dell'atto amministrativo
		SiacDAttoAmmTipo siacDAttoAmmTipo = siacDAttoAmmTipoRepository.findOne(tipoAtto.getUid());
		
		if (Boolean.TRUE.equals(siacDAttoAmmTipo.getAttoammProgressivoAuto())) {
			attoDaSalvare.setAttoammNumero(staccaNumeroAtto(attoAmministrativo.getAnno(), siacDAttoAmmTipo, SiacTNumeroAttoEnum.PROVVEDIMENTO));
		}
		
		attoDaSalvare.setUid(null);
		attoDaSalvare.setSiacTEnteProprietario(siacTEnteProprietario);
		attoDaSalvare.setLoginOperazione(loginOperazione);
		attoDaSalvare.setSiacDAttoAmmTipo(siacDAttoAmmTipo);
		
		//creo la relazione con la struttura contabile
		if (strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getUid() != 0) {
			SiacRAttoAmmClass classificatore = new SiacRAttoAmmClass();
			classificatore.setUid(null);
			classificatore.setDataModificaInserimento(new Date());
			classificatore.setSiacTEnteProprietario(siacTEnteProprietario);
			classificatore.setLoginOperazione(loginOperazione);
			classificatore.setSiacTAttoAmm(attoDaSalvare);
			
			//recupero il classificatore della struttura contabile
			SiacTClass siacTClass = siacTClassDao.findById(strutturaAmministrativoContabile.getUid());
			classificatore.setSiacTClass(siacTClass);
			List<SiacRAttoAmmClass> siacRAttoAmmClasses = new ArrayList<SiacRAttoAmmClass>();
			siacRAttoAmmClasses.add(classificatore);
			attoDaSalvare.setSiacRAttoAmmClasses(siacRAttoAmmClasses);
			
			//recupero i dati della struttura contabile per restituirli completi nella response del servizio
			StrutturaAmministrativoContabile sac = classificatoriDad.ricercaStrutturaAmministrativoContabile(siacTClass.getClassifId());
			attoAmministrativo.setStrutturaAmmContabile(sac);
		}
		
		aggiornaStato(attoAmministrativo, attoDaSalvare);

		attoDaSalvare = siacTAttoAmmDao.create(attoDaSalvare);
		
		// FIXME: mettere a posto il mapping prima o poi
//		attoAmministrativo = map(attoDaSalvare, AttoAmministrativo.class, AttMapId.SiacTAttoAmm);
		attoAmministrativo.setNumero(attoDaSalvare.getAttoammNumero() != null ? attoDaSalvare.getAttoammNumero().intValue() : 0);
		attoAmministrativo.setUid(attoDaSalvare.getUid());
		
		return attoAmministrativo;
	}

	
	
	/**
	 * Update.
	 * NOTA: questo metodo andrebbe riscritto e ristrutturato!
	 * 
	 * @param attoAmministrativo the atto amministrativo
	 * @param strutturaAmministrativoContabile the struttura amministrativo contabile
	 * @param tipoAtto the tipo atto
	 * @return the atto amministrativo
	 * @throws AttoNonTrovatoException the atto non trovato exception
	 */
	public AttoAmministrativo update(AttoAmministrativo attoAmministrativo,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile,
			TipoAtto tipoAtto) throws AttoNonTrovatoException {
		
		//recupero l'atto amministrativo
		SiacTAttoAmm attoDaAggiornare = siacTAttoAmmRepository.findOne(attoAmministrativo.getUid());
		
		if (attoDaAggiornare == null) {
			throw new AttoNonTrovatoException();
		}
		
		
		//recupero il tipo dell'atto amministrativo
		SiacDAttoAmmTipo attoTipo = siacDAttoAmmTipoRepository.findOne(tipoAtto.getUid());
		attoDaAggiornare.setSiacDAttoAmmTipo(attoTipo);
		
		attoDaAggiornare.setAttoammNumero(attoAmministrativo.getNumero());
		// Correzioni dei dati nel caso sforino
		attoDaAggiornare.setAttoammNote(StringUtils.abbreviate(attoAmministrativo.getNote(), 500));
		attoDaAggiornare.setAttoammOggetto(StringUtils.abbreviate(attoAmministrativo.getOggetto(), 500));
		// SIAC-4285
		attoDaAggiornare.setParereRegolaritaContabile(attoAmministrativo.getParereRegolaritaContabile());

		// STILO
		attoDaAggiornare.setAttoammBlocco(attoAmministrativo.getBloccoRagioneria());
		attoDaAggiornare.setAttoammProvenienza(attoAmministrativo.getProvenienza());
		
		aggiornaStato(attoAmministrativo, attoDaAggiornare);

		aggiornaStrutturaAmministrativaContabile(attoDaAggiornare, strutturaAmministrativoContabile);
		
		attoDaAggiornare.setDataModificaAggiornamento(new Date());
		
		SiacTAttoAmm attoSalvato = siacTAttoAmmRepository.saveAndFlush(attoDaAggiornare);
		return buildAttoAmministrativo(attoSalvato);
	}
	
	
	/**
	 * Aggiorna stato impegni e accertamenti collegati.
	 *
	 * @param attoAmministrativo the atto amministrativo
	 * @param statoOperativoAtti the stato operativo atti
	 * 
	 * @return la lista degli uid delle registrazioni inserite
	 */
	public List<Integer> aggiornaStatoImpegniEAccertamentiCollegati(AttoAmministrativo attoAmministrativo, StatoOperativoAtti statoOperativoAtti, Boolean isEsecutivo) {
		final String methodName = "aggiornaStatoImpegniEAccertamentiCollegati";
		List<Integer> uidsRegistrazioni = aggiornaStatoMovgestTs(attoAmministrativo, statoOperativoAtti, isEsecutivo);
		if(uidsRegistrazioni == null) {
			log.debug(methodName, "Returning null list from dao...");
			return new ArrayList<Integer>();
		}
		log.debug(methodName, "uids RegistrazioneMovFin restituiti: "+uidsRegistrazioni);
		return uidsRegistrazioni;
	}

	protected List<Integer> aggiornaStatoMovgestTs(AttoAmministrativo attoAmministrativo,
			StatoOperativoAtti statoOperativoAtti, Boolean isEsecutivo) {
		return siacTAttoAmmDao.aggiornaStatoMovgestTs(attoAmministrativo.getUid(), statoOperativoAtti.name(), Boolean.TRUE.equals(isEsecutivo),  loginOperazione);
	}
	
	
//	private void salvaStato(AttoAmministrativo src, SiacTAttoAmm dest) {
//		
//		SiacRAttoAmmStato siacRAttoAmmStato = new SiacRAttoAmmStato();
//		siacRAttoAmmStato.setDataModificaInserimento(new Date());
//		
//		SiacDAttoAmmStato siacDAttoAmmStato = siacRAttoAmmStatoRepository.ricercaStatoAttoAmm(src.getStatoOperativo(),ente.getUid());
//		siacRAttoAmmStato.setSiacDAttoAmmStato(siacDAttoAmmStato);
//		
//		siacRAttoAmmStato.setSiacTEnteProprietario(siacTEnteProprietario);
//		siacRAttoAmmStato.setLoginOperazione(loginOperazione);
//		
//		siacRAttoAmmStato.setSiacTAttoAmm(dest);
//		List<SiacRAttoAmmStato> stati = new ArrayList<SiacRAttoAmmStato>(1);
//		
//		stati.add(siacRAttoAmmStato);
//		dest.setSiacRAttoAmmStatos(stati);
//	}
	

	private void aggiornaStato(AttoAmministrativo src, SiacTAttoAmm dest) {
		
		if(StringUtils.isBlank(src.getStatoOperativo())){
			return; //parametro facoltativo. Non aggiorno nulla.
			
			//prima c'era solo questo:
//			if (siacDAttoAmmStato != null) {
//				dest.getSiacRAttoAmmStatos().get(0).setSiacDAttoAmmStato(siacDAttoAmmStato);
//			}
		}
		
		Date now = new Date();
		
		if(dest.getSiacRAttoAmmStatos()==null) {
			dest.setSiacRAttoAmmStatos(new ArrayList<SiacRAttoAmmStato>());
		}
		
		for(SiacRAttoAmmStato r : dest.getSiacRAttoAmmStatos()){
			r.setDataCancellazioneIfNotSet(now);
		}
		
		SiacRAttoAmmStato siacRAttoAmmStato = new SiacRAttoAmmStato();
		siacRAttoAmmStato.setDataModificaInserimento(now);
		
		SiacDAttoAmmStato siacDAttoAmmStato = siacRAttoAmmStatoRepository.ricercaStatoAttoAmm(src.getStatoOperativo(),ente.getUid());
		siacRAttoAmmStato.setSiacDAttoAmmStato(siacDAttoAmmStato);
		
		siacRAttoAmmStato.setSiacTEnteProprietario(siacTEnteProprietario);
		siacRAttoAmmStato.setLoginOperazione(loginOperazione);
		
		dest.addSiacRAttoAmmStato(siacRAttoAmmStato);
		

	}

	/**
	 * Aggiorna la struttura amministrativa contabile dell'atto amministrativo.
	 * NOTA: questo metodo andrebbe riscritto e ristrutturato!
	 * 
	 * @param siacTAttoAmm
	 * @param strutturaAmministrativoContabile
	 */
	private void aggiornaStrutturaAmministrativaContabile(SiacTAttoAmm siacTAttoAmm, StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		
		/*
		String methodName = "aggiornaStrutturaAmministrativaContabile";
		
		boolean eliminaStruttura = strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getDataFineValidita() != null;
		
		if(eliminaStruttura){ //se Impostato DataFineValidita elimina l'associazione con la strutturaAmministrativoContabile
			log.debug(methodName, "L'associazione con la strutturaAmministrativoContabile "
					+ "viene eliminata in quanto impostato dataFineValidita a: "+strutturaAmministrativoContabile.getDataFineValidita());
			if(attoDaAggiornare.getSiacRAttoAmmClasses()!=null){
				for(SiacRAttoAmmClass ra: attoDaAggiornare.getSiacRAttoAmmClasses()){
					entityManager.remove(ra);
				}
				attoDaAggiornare.getSiacRAttoAmmClasses().clear();
			}
			
		} else if(strutturaAmministrativoContabile != null){ //altrimenti aggiorna l'associazione con la strutturaAmministrativoContabile se valorizzata
			log.debug(methodName, "L'associazione con la strutturaAmministrativoContabile "
					+ "viene aggiornata con uid: "+strutturaAmministrativoContabile.getUid());
			
			SiacRAttoAmmClass r = new SiacRAttoAmmClass();
			r.setDataModificaInserimento(new Date());
			r.setLoginOperazione(loginOperazione);
			r.setSiacTAttoAmm(attoDaAggiornare);
			SiacTClass siacTClass = new SiacTClass();
			siacTClass.setUid(strutturaAmministrativoContabile.getUid());
			r.setSiacTClass(siacTClass);
			r.setSiacTEnteProprietario(siacTEnteProprietario);
			
			if(attoDaAggiornare.getSiacRAttoAmmClasses()!=null){
				log.debug(methodName, "pulisco la lista strutturaAmministrativoContabile");
				for(SiacRAttoAmmClass ra: attoDaAggiornare.getSiacRAttoAmmClasses()){
					entityManager.remove(ra);
				}
				attoDaAggiornare.getSiacRAttoAmmClasses().clear();
			} else {
				attoDaAggiornare.setSiacRAttoAmmClasses(new ArrayList<SiacRAttoAmmClass>());
				log.debug(methodName, "creo nuova lista vuota strutturaAmministrativoContabile");
			}
			attoDaAggiornare.getSiacRAttoAmmClasses().add(r);
			
		} else { //se NON valorizzata la strutturaAmministrativoContabile lascia invariato il dato su DB
			log.debug(methodName, "L'associazione con la strutturaAmministrativoContabile "
					+ "resta invariata.");
		}*/
		
		// Cancello le vecchie associazioni
		final Date now = new Date();
		if(siacTAttoAmm.getSiacRAttoAmmClasses() != null) {
			for(SiacRAttoAmmClass sraac : siacTAttoAmm.getSiacRAttoAmmClasses()) {
				sraac.setDataCancellazioneIfNotSet(now);
			}
		}
		if(strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getUid() != 0) {
			SiacTClass siacTClass = new SiacTClass();
			siacTClass.setUid(strutturaAmministrativoContabile.getUid());
			
			SiacRAttoAmmClass siacRAttoAmmClass = new SiacRAttoAmmClass();
			siacRAttoAmmClass.setDataModificaInserimento(now);
			siacRAttoAmmClass.setLoginOperazione(loginOperazione);
			siacRAttoAmmClass.setSiacTEnteProprietario(siacTEnteProprietario);
			// Lego i valori alla r
			siacRAttoAmmClass.setSiacTAttoAmm(siacTAttoAmm);
			siacRAttoAmmClass.setSiacTClass(siacTClass);
			
			// Injetto la r nella entity
			List<SiacRAttoAmmClass> siacRAttoAmmClasses = new ArrayList<SiacRAttoAmmClass>();
			siacRAttoAmmClasses.add(siacRAttoAmmClass);
			siacTAttoAmm.setSiacRAttoAmmClasses(siacRAttoAmmClasses);
		}
		
		/*
		SiacRAttoAmmClass classificatore = new SiacRAttoAmmClass();
		classificatore.setUid(null);
		classificatore.setDataModificaInserimento(new Date());
		classificatore.setSiacTEnteProprietario(siacTEnteProprietario);
		classificatore.setLoginOperazione(loginOperazione);
		classificatore.setSiacTAttoAmm(attoDaSalvare);
		
		//recupero il classificatore della struttura contabile
		SiacTClass siacTClass = siacTClassDao.findById(strutturaAmministrativoContabile.getUid());
		classificatore.setSiacTClass(siacTClass);
		List<SiacRAttoAmmClass> siacRAttoAmmClasses = new ArrayList<SiacRAttoAmmClass>();
		siacRAttoAmmClasses.add(classificatore);
		attoDaSalvare.setSiacRAttoAmmClasses(siacRAttoAmmClasses);
		*/
	}


	public StatoOperativoAtti findStatoOperativoAttoAmministrativo(AttoAmministrativo attoAmministrativo){
		SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.findOne(attoAmministrativo.getUid());
		
		if(siacTAttoAmm == null){
			throw new IllegalArgumentException("Impossibile trovare atto amministrativo con uid: "+attoAmministrativo.getUid());
		}
		
		List<SiacRAttoAmmStato> siacRAttoAmmStatos = siacTAttoAmm.getSiacRAttoAmmStatos();
		
		if(siacRAttoAmmStatos.isEmpty()){
			throw new IllegalArgumentException("Amministrativo con uid: "+attoAmministrativo.getUid() + " non associato ad uno stato.");
		}
		
		String attoammStatoCode = null;
		
		for(SiacRAttoAmmStato sraas : siacRAttoAmmStatos) {
			if(sraas.getDataCancellazione() == null) {
				attoammStatoCode = sraas.getSiacDAttoAmmStato().getAttoammStatoCode();
				break;
			}
		}
		
		
		return SiacDAttoAmmStatoEnum.byCodice(attoammStatoCode).getStatoOperativoAtti();
	}
	
	
	/**
	 * Verifica annullabilita.
	 *
	 * @param attoAmministrativo the atto amministrativo
	 * @return true, if successful
	 */
	public Boolean isAnnullabile(AttoAmministrativo attoAmministrativo) {
		final String methodName = "verificaAnnullabilita";
		Boolean result = isAnnullabileAttoAmm(attoAmministrativo);
		log.debug(methodName, "result: "+result);
		return result;
		
//		SiacTAttoAmm atto = siacTAttoAmmDao.findById(attoAmministrativo.getUid());
//		return atto.getSiacRAttoAmmStatos().get(0).getSiacDAttoAmmStato().getAttoammStatoCode().equals(StatoEntita.CANCELLATO.name());
		
		
	}

	protected Boolean isAnnullabileAttoAmm(AttoAmministrativo attoAmministrativo) {
		return siacTAttoAmmDao.isAnnullabileAttoAmm(attoAmministrativo.getUid());
	}
	
	/**
	 * Builds the atto amministrativo.
	 *
	 * @param attoSalvato the atto salvato
	 * @return the atto amministrativo
	 */
	private AttoAmministrativo buildAttoAmministrativo(SiacTAttoAmm attoSalvato) {
		AttoAmministrativo atto = map(attoSalvato, AttoAmministrativo.class, AttMapId.SiacTAttoAmm);
		atto.setEnte(map(attoSalvato.getSiacTEnteProprietario(), Ente.class, BilMapId.SiacTEnteProprietario_Ente_Base));
		
		if(attoSalvato.getSiacRAttoAmmStatos() != null && !attoSalvato.getSiacRAttoAmmStatos().isEmpty()){
			for(SiacRAttoAmmStato sraas : attoSalvato.getSiacRAttoAmmStatos()) {
				if(sraas.getDataCancellazione() == null) {
					atto.setStatoOperativo(sraas.getSiacDAttoAmmStato().getAttoammStatoCode());
					break;
				}
			}
		}
		
		if (attoSalvato.getSiacRAttoAmmClasses() != null) {
			for(SiacRAttoAmmClass sraac : attoSalvato.getSiacRAttoAmmClasses()) {
				log.debug("buildAttoAmministrativo", "uid atto: " + attoSalvato.getUid());
				if(sraac.getDataCancellazione() == null) {
					StrutturaAmministrativoContabile sac = classificatoriDad.ricercaStrutturaAmministrativoContabile(sraac.getSiacTClass().getClassifId());
					atto.setStrutturaAmmContabile(sac);
					break;
				}
			}
		}
		if (attoSalvato.getDataCancellazione() != null){
			atto.setStato(StatoEntita.CANCELLATO);
		} else {
			atto.setStato(StatoEntita.VALIDO);
		}
		if(attoSalvato.getSiacDAttoAmmTipo() != null) {
			TipoAtto tipoAtto = new TipoAtto();
			tipoAtto.setUid(attoSalvato.getSiacDAttoAmmTipo().getAttoammTipoId());
			tipoAtto.setCodice(attoSalvato.getSiacDAttoAmmTipo().getAttoammTipoCode());
			tipoAtto.setDescrizione(attoSalvato.getSiacDAttoAmmTipo().getAttoammTipoDesc());
			atto.setTipoAtto(tipoAtto);
		}
		
		if(attoSalvato.getSiacTAttoAllegatos() != null){
			
			List<AllegatoAtto> allegati = new ArrayList<AllegatoAtto>(); 
			for(SiacTAttoAllegato allegato : attoSalvato.getSiacTAttoAllegatos()){
				if(allegato.getDataCancellazione() == null){
					AllegatoAtto allegatoAtto = new AllegatoAtto();
					allegatoAtto.setUid(allegato.getUid());
					allegatoAtto.setDataScadenza(allegato.getAttoalDataScadenza());
					atto.setAllegatoAtto(allegatoAtto);
					if(allegato.getSiacRAttoAllegatoStatos() != null) {
						for(SiacRAttoAllegatoStato sraas : allegato.getSiacRAttoAllegatoStatos()) {
							if(sraas.getDataCancellazione() == null) {
								SiacDAttoAllegatoStato siacDAttoAllegatoStato = sraas.getSiacDAttoAllegatoStato();
								SiacDAttoAllegatoStatoEnum sdaase = SiacDAttoAllegatoStatoEnum.byCodice(siacDAttoAllegatoStato.getAttoalStatoCode());
								StatoOperativoAllegatoAtto soaa = sdaase.getStatoOperativoAllegatoAtto();
								allegatoAtto.setStatoOperativoAllegatoAtto(soaa);
							}
						}
					}
					allegati.add(allegatoAtto);
				}
			}
			for(AllegatoAtto a: allegati){
				if(!StatoOperativoAllegatoAtto.ANNULLATO.equals(a.getStatoOperativoAllegatoAtto())
						&& !StatoOperativoAllegatoAtto.RIFIUTATO.equals(a.getStatoOperativoAllegatoAtto())){
					atto.setAllegatoAtto(a);
					break;
				}
			}
			
		}
		
		return atto;
	}


	/**
	 * Stacca numero atto.
	 *
	 * @param anno the anno
	 * @param tipo the tipo
	 * @return the integer
	 */
	private Integer staccaNumeroAtto(int anno, SiacDAttoAmmTipo tipo, SiacTNumeroAttoEnum siacTNumeroAttoEnum) {
		final String methodName = "staccaNumeroVariazione";
		
		SiacTNumeroAtto siacTNumeroAtto = siacTNumeroAttoRepository.findByAnnoEnteTipoAttoTipoNumerazione(anno, ente.getUid(), tipo.getUid(), siacTNumeroAttoEnum.getCodice());
		
		Date now = new Date();		
		if(siacTNumeroAtto == null){
			//Nel caso il contatore non sia stato inizializzato per il bilancio in questione viene creato un nuovo record sulla tabella SiacTVariazioneNum
			siacTNumeroAtto = new SiacTNumeroAtto();
			siacTNumeroAtto.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTNumeroAtto.setDataCreazione(now);
			siacTNumeroAtto.setDataInizioValidita(now);
			siacTNumeroAtto.setLoginOperazione(loginOperazione);
			siacTNumeroAtto.setAnnoAtto(anno);
			siacTNumeroAtto.setSiacDAttoAmmTipo(tipo);
			siacTNumeroAtto.setTipoNumerazione(siacTNumeroAttoEnum.getCodice());
			
			Integer numeroAtto = obtainStartNumero(anno, tipo, siacTNumeroAttoEnum);
			//siacTNumeroAtto.setNumeroAtto(50000); //La numerazione parte da 50000
			siacTNumeroAtto.setNumeroAtto(numeroAtto);
		}
		
		siacTNumeroAtto.setDataModifica(now);
		
		
		siacTNumeroAttoRepository.saveAndFlush(siacTNumeroAtto);
		
		Integer numeroAtto = siacTNumeroAtto.getNumeroAtto();
		log.info(methodName, "returning numero atto: "+ numeroAtto);
		return numeroAtto;
	}

	private Integer obtainStartNumero(Integer anno, SiacDAttoAmmTipo tipo, SiacTNumeroAttoEnum siacTNumeroAttoEnum) {
		final String methodName = "obtainStartNumero";
		// Caso limite
		if(SiacTNumeroAttoEnum.ATTO_DI_LEGGE.equals(siacTNumeroAttoEnum)) {
			log.debug(methodName, "Atto di legge: parto da 50000");
			return Integer.valueOf(50000);
		}
		// Al momento scrivo il numero 50000 fisso. Potrebbe essere parametrizzato
		Integer maxNumeroSalvato = siacTAttoAmmRepository.findMaxAttoammNumeroByAttoammAnnoAndAttoammTipoIdAndEntePropretarioId(ente.getUid(), anno.toString(), tipo.getAttoammTipoId());
		log.debug(methodName, "Numero massimo su base dati per il tipoAtto " + tipo.getAttoammTipoCode() + " per l'anno " + anno + ": " + maxNumeroSalvato);
		int startNumero = Math.max(maxNumeroSalvato, 50000);
		log.debug(methodName, "Numero di start per il tipoAtto " + tipo.getAttoammTipoCode() + " per l'anno " + anno + ": " + startNumero);
		
		return startNumero;
	}


	/**
	 * Gets the elenco tipi.
	 *
	 * @return the elenco tipi
	 */
	public List<TipoAtto> getElencoTipi() {
		
		List<SiacDAttoAmmTipo> elencoDB = siacDAttoAmmTipoRepository.elencoTipi(ente.getUid());
		
		List<TipoAtto> elencoTipi = new ArrayList<TipoAtto>(elencoDB.size());
		
		for (SiacDAttoAmmTipo tipo : elencoDB) {
			TipoAtto tipoAtto = mapNotNull(tipo, TipoAtto.class, BilMapId.SiacDAttoAmmTipo_TipoAtto);
			elencoTipi.add(tipoAtto);
		}
		
		return elencoTipi;
	}
	
	
	public String getCodiceTipoDeterminaDiLiquidazione(){
		return SiacDAttoAmmTipoEnum.DeterminaDiLiquidazione.getCodice();
	}
	
	public String getCodiceTipoDeterminaDiIncasso(){
		return SiacDAttoAmmTipoEnum.DeterminaDiIncasso.getCodice();
	}
	
	/**
	 * Ricerca il tipo di atto per uid.
	 * 
	 * @param uidTipoAtto l'uid dell'atto da cercare
	 * 
	 * @return l'atto corrispondente all'uid, se presente
	 */
	public TipoAtto findTipoAttoByUid(Integer uidTipoAtto) {
		SiacDAttoAmmTipo siacDAttoAmmTipo = siacDAttoAmmTipoRepository.findOne(uidTipoAtto);
		return mapNotNull(siacDAttoAmmTipo, TipoAtto.class, BilMapId.SiacDAttoAmmTipo_TipoAtto);
	}
	
	/**
	 * Cerca un atto amministrativo a partire dall'anno,numero e tipo 
	 * @param attoAmministrativo
	 * @return
	 */
	public AttoAmministrativo findAttoAmministrativoByAnnoAndNumeroAndTipo(AttoAmministrativo attoAmministrativo){
		
		SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.ricercaAttoByAnnoNumeroTipoAndSAC( attoAmministrativo.getEnte().getUid(),
																							  String.valueOf(attoAmministrativo.getAnno()), 
																		 					  attoAmministrativo.getNumero(),
				                                                                              attoAmministrativo.getTipoAtto().getUid(),
				                                                                              attoAmministrativo.getStrutturaAmmContabile() != null ? attoAmministrativo.getStrutturaAmmContabile().getUid(): null);

		if(siacTAttoAmm == null) {
			return null;
		}
        return buildAttoAmministrativo(siacTAttoAmm);
	}
	
	
	/**
	 * Cerca un atto amministrativo a partire dall'anno,numero e tipo 
	 * @param attoAmministrativo
	 * @return
	 */
	public String findCodiceStatoOut(AttoAmministrativo attoAmministrativo){
		
		List<SiacRAttoAmmStatoInOut> r = siacTAttoAmmRepository.ricercaCodiceStatoOut( attoAmministrativo.getEnte().getUid(),attoAmministrativo.getStatoOperativo());

		//no-comment
		String codice = r != null && ! r.isEmpty() ? r.get(0).getOutAttoammStatoCode(): attoAmministrativo.getStatoOperativo();
        return codice;
	}
	
	
	
	/**
	 * Find tipo atto.
	 *
	 * @param codiceTipoAtto the codice tipo atto
	 * @return the tipo atto
	 */
	private TipoAtto findTipoAtto(SiacDAttoAmmTipoEnum siacDAttoAmmTipoEnum, Ente ente) {
		SiacDAttoAmmTipo siacDAttoAmmTipo = eef.getEntity(siacDAttoAmmTipoEnum, ente.getUid());
		return mapNotNull(siacDAttoAmmTipo, TipoAtto.class, BilMapId.SiacDAttoAmmTipo_TipoAtto);
	}
	
	/**
	 * Find tipo atto even null.
	 *
	 * @param siacDAttoAmmTipoEnum the siac d atto amm tipo enum
	 * @return the tipo atto
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	private TipoAtto findTipoAttoEvenNull(SiacDAttoAmmTipoEnum siacDAttoAmmTipoEnum, Ente ente) {
		String methodName = "findTipoAttoEvenNull";
		
		SiacDAttoAmmTipo siacDAttoAmmTipo;
		
		try {
			siacDAttoAmmTipo = eef.getEntity(siacDAttoAmmTipoEnum, ente.getUid());
		} catch(IllegalStateException ise) {
			log.debug(methodName, "tipoAtto "+ siacDAttoAmmTipoEnum + " non trovato. Returning null.");
			siacDAttoAmmTipo = null;
		}
		
		return mapNotNull(siacDAttoAmmTipo, TipoAtto.class, BilMapId.SiacDAttoAmmTipo_TipoAtto);
	}
	
	/**
	 * Gets the tipo atto alg.
	 *
	 * @return the tipo atto alg
	 */
	public TipoAtto getTipoAttoALG() {
		return findTipoAtto(SiacDAttoAmmTipoEnum.Alg, ente);
	}
	
	/**
	 * Gets the tipo atto alg.
	 *
	 * @return the tipo atto alg
	 */
	public TipoAtto getTipoAttoALG(Ente ente) {
		return findTipoAtto(SiacDAttoAmmTipoEnum.Alg, ente);
	}
	
	/**
	 * Gets the tipo atto alg even null.
	 *
	 * @return the tipo atto alg even null
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public TipoAtto getTipoAttoALGEvenNull() {
		return findTipoAttoEvenNull(SiacDAttoAmmTipoEnum.Alg, ente);
	}

	public Long countByAnnoAndNumeroAndTipoAndSACAndNotUid(AttoAmministrativo attoAmministrativo) {
		return siacTAttoAmmDao.countByEnteProprietarioIdAttoammAnnoAttoammNumeroAttoammTipoIdClassifIdNotAttoammId(ente.getUid(),
				attoAmministrativo.getAnno() + "",
				attoAmministrativo.getNumero(),
				attoAmministrativo.getTipoAtto() != null ? attoAmministrativo.getTipoAtto().getUid() : null,
				attoAmministrativo.getStrutturaAmmContabile() != null ? attoAmministrativo.getStrutturaAmmContabile().getUid() : null,
				attoAmministrativo.getUid());
	}

	public ListaPaginata<AttoAmministrativo> ricercaSintetica(RicercaAtti ricercaAtti, ParametriPaginazione parametriPaginazione, ModelDetail... modelDetails) {
		Page<SiacTAttoAmm> result;
		Pageable pageable = toPageable(parametriPaginazione);
		// Se c'e' l'uid faccio la ricerca puntuale
		if(ricercaAtti.getUid() != null && ricercaAtti.getUid().intValue() != 0) {
			SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.findOne(ricercaAtti.getUid());
			List<SiacTAttoAmm> siacTAttoAmms = new ArrayList<SiacTAttoAmm>();
			if(siacTAttoAmm != null) {
				siacTAttoAmms.add(siacTAttoAmm);
			}
			// Wrappo comunque per ottenere i dati di paginazione
			result = new PageImpl<SiacTAttoAmm>(siacTAttoAmms, pageable, siacTAttoAmms.size());
		} else {
			result = siacTAttoAmmDao.ricercaProvvedimento(siacTEnteProprietario.getUid(),
					mapToString(ricercaAtti.getAnnoAtto(), ""),
					ricercaAtti.getOggetto(),
					ricercaAtti.getNumeroAtto(),
					ricercaAtti.getNote(),
					(ricercaAtti.getTipoAtto() != null && ricercaAtti.getTipoAtto().getUid() != 0) ? ricercaAtti.getTipoAtto().getUid() : null,
					(ricercaAtti.getStrutturaAmministrativoContabile() != null && ricercaAtti.getStrutturaAmministrativoContabile().getUid() != 0) ? ricercaAtti.getStrutturaAmministrativoContabile().getUid() : null,
					ricercaAtti.getStatoOperativo(),
					Boolean.valueOf(ricercaAtti.isConDocumentoAssociato()), 
					pageable);
		}
		
		
		
		return toListaPaginata(result, AttoAmministrativo.class, BilMapId.SiacTAttoAmm, modelDetails);
	}
	

	public AttoAmministrativo findProvvedimentoByIdModelDetail(int uid, ModelDetail... modelDetails) {
		final String methodName = "findProvvedimentoByIdModelDetail";
		log.debug(methodName, "uid provvedimento" + uid);
		SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.findOne(uid);
		if(siacTAttoAmm == null){
			log.debug(methodName, "ho trovato atto amministrativo null");
			return null;
		}
		log.debug(methodName, "siacTAttoAmm: " + siacTAttoAmm.getUid());
		
		return map(siacTAttoAmm, AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo, Converters.byModelDetails(modelDetails));
	}

}
