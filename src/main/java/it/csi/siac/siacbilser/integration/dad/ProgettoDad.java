/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dad.mapper.progetto.SiacTProgrammaProgettoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.util.MapperDecoratorHelper;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.ProgettoDao;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.dao.SiacTProgrammaNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTProgrammaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaStato;
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.SiacTProgrammaNum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoTotale;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per il Progetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProgettoDad extends ExtendedBaseDadImpl {
	
	/** The progetto dao. */
	@Autowired
	private ProgettoDao progettoDao;
	
	/** The siac t programma repository. */
	@Autowired
	private SiacTProgrammaRepository siacTProgrammaRepository;
	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassDao siacTClassDao;
	
	@Autowired
	private SiacTProgrammaNumRepository siacTProgrammaNumRepository;
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private MapperDecoratorHelper mapperDecoratorHelper;
	@Autowired
	private SiacTProgrammaProgettoMapper siacTProgrammaProgettoMapper;
	
	/**
	 * Persiste il Progetto su DB.
	 * 
	 * @param progetto il progetto da persistere
	 */
	public void inserisciProgetto(Progetto progetto) {
		SiacTProgramma siacTProgramma = buildSiacTProgramma(progetto);	
		progettoDao.create(siacTProgramma);
		progetto.setUid(siacTProgramma.getUid());
	}
	
	/**
	 * Aggiorna il Progetto sulla base dati.
	 * 
	 * @param progetto il progetto da aggiornare
	 */
	public void aggiornaProgetto(Progetto progetto) {
		SiacTProgramma siacTProgramma = buildSiacTProgramma(progetto);	
		progettoDao.update(siacTProgramma);
		progetto.setUid(siacTProgramma.getUid());
	}
	
	/**
	 * Effettua una ricerca puntuale per il Progetto.
	 *
	 * @param progetto il progetto da ricercare
	 * @return the progetto
	 */
	public Progetto ricercaPuntualeProgetto(Progetto progetto) {
		SiacTProgramma siacTProgramma = siacTProgrammaRepository.findByCodiceAndStatoOperativoProgettoAndEnteProprietarioId(progetto.getCodice(), 
				progetto.getStatoOperativoProgetto().getCodice(), progetto.getTipoProgetto().getCodice(), progetto.getBilancio().getUid(), progetto.getEnte().getUid());
		if(siacTProgramma == null) {
			return null;
		}
		return map(siacTProgramma, Progetto.class, BilMapId.SiacTProgramma_Progetto);
	}
	
	
	
	
	/**
	 * Ottiene un vincolo a partire dalla chiave fornita.
	 * 
	 * @param chiaveProgetto la chiave univoca del progetto
	 * 
	 * @return il progetto corrispondente alla chiave, se esistente
	 */
	
	public Progetto findProgettoById(Integer chiaveProgetto, ModelDetailEnum...progettoModelDetails) {
		final String methodName = "findProgettoById";
		log.debug(methodName, "Id: " + chiaveProgetto);
		SiacTProgramma siacTProgramma = siacTProgrammaRepository.findOne(chiaveProgetto);
		if(siacTProgramma == null) {
			throw new IllegalArgumentException("Impossibile trovare il vincolo con id: " + chiaveProgetto);
		}
		
		Progetto progetto = map(siacTProgramma, Progetto.class, BilMapId.SiacTProgramma_Progetto);
		
		siacTProgrammaProgettoMapper.decorate(siacTProgramma, progetto,mapperDecoratorHelper.getDecoratorsFromModelDetails(progettoModelDetails));
		
		return progetto;
	}
	
	/**
	 * Aggiorna lo stato operativo del Progetto a quello fornito in input.
	 * 
	 * @param progetto       il progetto il cui stato deve essere aggiornato
	 * @param statoOperativo lo stato da apporre al progetto
	 */
	public void aggiornaStatoOperativoProgetto(Progetto progetto, StatoOperativoProgetto statoOperativo) {
		SiacTProgramma siacTProgramma = siacTProgrammaRepository.findOne(progetto.getUid());
		List<SiacRProgrammaStato> siacRProgrammaStatos = siacTProgramma.getSiacRProgrammaStatos();
		Date now = new Date();
		for(SiacRProgrammaStato siacRProgrammaStato : siacRProgrammaStatos) {
			if(siacRProgrammaStato.getDataCancellazione() == null){
				siacRProgrammaStato.setDataCancellazione(now);
				siacRProgrammaStato.setDataFineValidita(now);
			}
		}
		
		SiacRProgrammaStato siacRProgrammaStato = new SiacRProgrammaStato();
		Integer enteId = siacTProgramma.getSiacTEnteProprietario().getUid();
		siacRProgrammaStato.setSiacDProgrammaStato(eef.getEntity(SiacDProgrammaStatoEnum.byStatoOperativo(statoOperativo), enteId, SiacDProgrammaStato.class));
		siacRProgrammaStato.setDataModificaInserimento(now);
		siacRProgrammaStato.setSiacTEnteProprietario(siacTProgramma.getSiacTEnteProprietario());
		siacRProgrammaStato.setSiacTProgramma(siacTProgramma);
		siacRProgrammaStato.setLoginOperazione(loginOperazione);
		siacRProgrammaStatos.add(siacRProgrammaStato);
	}
	
	/**
	 * Effettua una ricerca sintetica per il Progetto.
	 * 
	 * @param progetto             il Progetto con le informazioni da ricercare
	 * @param parametriPaginazione i parametri di paginazione
	 * @param progettoModelDetails 
	 * 
	 * @return una lista paginata contentente i Progetti relativi ai parametri di paginazione e alle chiavi di ricerca
	 */
	public ListaPaginata<Progetto> ricercaSinteticaProgetti(Progetto progetto, ParametriPaginazione parametriPaginazione, 
			ProgettoModelDetail[] progettoModelDetails) {

		TipoAmbito tipoAmbito = progetto.getTipoAmbito() == null ? new TipoAmbito() : progetto.getTipoAmbito();
		AttoAmministrativo attoAmministrativo = progetto.getAttoAmministrativo() == null ? new AttoAmministrativo() : progetto.getAttoAmministrativo();
		//String annoBil = String.valueOf(progetto.getBilancio().getAnno());
		
		Page<SiacTProgramma> ppp = progettoDao.ricercaSinteticaProgetto(
				progetto.getEnte().getUid(),
				progetto.getCodice(),
				progetto.getTipoProgetto(),
				tipoAmbito.getUid() == 0 ? null : tipoAmbito.getUid(), 
				progetto.getStrutturaAmministrativoContabile() != null && progetto.getStrutturaAmministrativoContabile().getUid() > 0 ? 
						progetto.getStrutturaAmministrativoContabile().getUid() : null,
				progetto.getRilevanteFPV(),
				SiacDProgrammaStatoEnum.byStatoOperativoEvenNull(progetto.getStatoOperativoProgetto()),
				progetto.getDescrizione(),
				attoAmministrativo.getUid() == 0 ? null : attoAmministrativo.getUid(), 
				progetto.getDataIndizioneGara(),
				progetto.getDataAggiudicazioneGara(),
				progetto.getInvestimentoInCorsoDiDefinizione(),
				progetto.getBilancio().getAnno() == 0 ? null : String.valueOf(progetto.getBilancio().getAnno()),
				toPageable(parametriPaginazione));

		return toListaPaginata(ppp, Progetto.class, BilMapId.SiacTProgramma_Progetto, progettoModelDetails);
	}
	
	/**
	 * Ricerca lo StatoOperativoProgetto associato al Progetto fornito in input.
	 * 
	 * @param progetto il Progetto il cui StatoOperativoProgetto deve essere reperito
	 * 
	 * @return lo StatoOperativoProgettoCorrispondente
	 */
	public StatoOperativoProgetto findStatoOperativoByProgetto(Progetto progetto) {
		SiacDProgrammaStato siacDProgrammaStato = siacTProgrammaRepository.findStatoByIdProgetto(progetto.getUid());
		
		return SiacDProgrammaStatoEnum.byCodice(siacDProgrammaStato.getProgrammaStatoCode()).getStatoOperativoProgetto();
	}
	
	/**
	 * Ricerca la lista dei TipiAmbito associati all'Ente.
	 * 
	 * @param anno l'anno di competenza della ricerca 
	 * 
	 * @return la lista
	 */
	public List<TipoAmbito> ricercaListaTipiAmbito(Integer anno) {
		List<SiacTClass> siacTClasses = siacTClassDao.findByClassTipoAndByAnnoAndByEnteProprietarioId(
			SiacDClassTipoEnum.TipoAmbito.getCodice(),
			anno,
			ente.getUid());
		
		return convertiLista(siacTClasses, TipoAmbito.class,  BilMapId.SiacTClass_ClassificatoreGenerico);
	}
	
	public TipoAmbito ricercaTipoAmbito(Integer anno, String codice) {
		SiacTClass siacTClass = siacTClassDao.findClassifByCodiceAndEnteAndTipoCode(
			codice,
			ente.getUid(),
			anno,
			SiacDClassTipoEnum.TipoAmbito.getCodice()
		);
		
		TipoAmbito tipoAmbito = new TipoAmbito();
		
		mapNotNull(siacTClass, tipoAmbito, BilMapId.SiacTClass_ClassificatoreGenerico);
		
		return tipoAmbito;
	}
	
	/**
	 * Costruttore della classe di Entity a partire dalla classe di Model.
	 * 
	 * @param progetto il Progetto da trasformare in Entity
	 * 
	 * @return il SiacTProgramma corrispondente al Progetto
	 */
	private SiacTProgramma buildSiacTProgramma(Progetto progetto) {
		SiacTProgramma siacTProgramma = new SiacTProgramma();		
		siacTProgramma.setLoginOperazione(loginOperazione);
		progetto.setLoginOperazione(loginOperazione);
		map(progetto, siacTProgramma, BilMapId.SiacTProgramma_Progetto);
		return siacTProgramma;
	}
	
	

	/**
	 * Calcolo FPV Spesa tramite la function.
	 *
	 * @param progetto the progetto
	 * @param anno the anno
	 * @return the list
	 */
	public List<FondoPluriennaleVincolatoUscitaCronoprogramma> calcoloFpvSpesa(Progetto progetto, Integer anno) {

		List<Object[]> listaFpvSpesa = progettoDao.calcoloFpvSpesa(progetto.getUid(), ""+anno);
		
		List<FondoPluriennaleVincolatoUscitaCronoprogramma> result = new ArrayList<FondoPluriennaleVincolatoUscitaCronoprogramma>();
		for (Object[] fpv : listaFpvSpesa) {
			FondoPluriennaleVincolatoUscitaCronoprogramma fpvuc = new FondoPluriennaleVincolatoUscitaCronoprogramma();
			
			//fpv[0]
			Missione missione = new Missione();
			missione.setCodice((String)fpv[0]);
			fpvuc.setMissione(missione);
			
			//fpv[1]
			Programma programma = new Programma();
			programma.setCodice((String)fpv[1]);
			fpvuc.setProgramma(programma);
			
			//fpv[2]
			TitoloSpesa titoloSpesa = new TitoloSpesa();
			titoloSpesa.setCodice((String)fpv[1]);
			fpvuc.setTitoloSpesa(titoloSpesa);
			
			//fpv[3]			
			fpvuc.setAnno(Integer.valueOf((String)fpv[3]));
			
			//fpv[4]
			fpvuc.setImporto((BigDecimal)fpv[4]);
			
			//fpv[5]
			fpvuc.setImportoFPV((BigDecimal)fpv[5]);
			
			
			result.add(fpvuc);
		}
		
		return result;
	}
	
	
	/**
	 * Calcolo FPV Entrata tramite la function.
	 *
	 * @param progetto the progetto
	 * @param anno the anno
	 * @return the list
	 */
	public List<FondoPluriennaleVincolatoEntrata> calcoloFpvEntrata(Progetto progetto, Integer anno) {

		List<Object[]> listaFpvEntrata = progettoDao.calcoloFpvEntrata(progetto.getUid(), ""+anno);
		
		List<FondoPluriennaleVincolatoEntrata> result = new ArrayList<FondoPluriennaleVincolatoEntrata>();
		for (Object[] fpv : listaFpvEntrata) {
			FondoPluriennaleVincolatoEntrata fpvec = new FondoPluriennaleVincolatoEntrata();

			
			//fpv[0]
			fpvec.setAnno(Integer.valueOf((String)fpv[0]));
			
			//fpv[1] ---> importo prevvisto entrata
			fpvec.setImporto((BigDecimal)fpv[1]);
			
			//fpv[2]
			
			fpvec.setFpvEntrataSpesaCorrente((BigDecimal)fpv[2]);
			
			//fpv[3]
			
			fpvec.setFpvEntrataSpesaContoCapitale((BigDecimal)fpv[3]);
			
			//fpv[4]
			fpvec.setTotale((BigDecimal)fpv[4]);
			
			//fpv[5]  --->IMPORTO ENTRATA  COMPLESSIVO
			fpvec.setImportoFPV((BigDecimal)fpv[5]);
	
			result.add(fpvec);
		}
		
		return result;
	}
	/**
	 * Calcolo FPV Totale(complessivo) tramite la function.
	 *
	 * @param progetto the progetto
	 * @param anno the anno
	 * @return the list
	 */
	public List<FondoPluriennaleVincolatoTotale> calcoloFpvTotale(Progetto progetto, Integer anno) {

		List<Object[]> listaFpvTotale = progettoDao.calcoloFpvTotale(progetto.getUid(), ""+anno);
		
		List<FondoPluriennaleVincolatoTotale> result = new ArrayList<FondoPluriennaleVincolatoTotale>();
		for (Object[] fpv : listaFpvTotale) {
			FondoPluriennaleVincolatoTotale fpvec = new FondoPluriennaleVincolatoTotale();
			
			
			//fpv[0]
			fpvec.setAnno(Integer.valueOf((String)fpv[0]));
			
			//fpv[1],fpv[2] ---> fpv entrata
			 FondoPluriennaleVincolatoCronoprogramma fpvEntrata=new FondoPluriennaleVincolatoCronoprogramma();
			 fpvEntrata.setImporto((BigDecimal)fpv[1]);
			 fpvEntrata.setImportoFPV((BigDecimal)fpv[2]);
			 fpvec.setFpvEntrata(fpvEntrata);
			
			
			//fpv[3],fpv[4]  ---->fpvSpesa
			 FondoPluriennaleVincolatoCronoprogramma fpvUscita=new FondoPluriennaleVincolatoCronoprogramma();
			 fpvUscita.setImporto((BigDecimal)fpv[3]);
			 fpvUscita.setImportoFPV((BigDecimal)fpv[4]);
			 fpvec.setFpvUscita(fpvUscita);
			
			//fpv[5]  --->IMPORTO ENTRATA  COMPLESSIVO
			 fpvec.setImportoFPV((BigDecimal)fpv[5]);
	
			result.add(fpvec);
		}
		
		return result;
	}
	
	/**
	 * Calcolo FPV Totale(complessivo) tramite la function.
	 *
	 * @param progetto the progetto
	 * @param anno the anno
	 * @return the list
	 */
	public List<ProspettoRiassuntivoCronoprogramma> calcoloProspettoRiassuntivoCronoprogrammaDiGestioneAggiorna(Progetto progetto, Integer anno) {

		List<Object[]> listaProspettoCronoprogrammaDiGestione = progettoDao.calcoloProspettoRiassuntivoCronoprogrammaDiGestioneAggiorna(progetto.getUid(), anno.toString());
		
		List<ProspettoRiassuntivoCronoprogramma> result = new ArrayList<ProspettoRiassuntivoCronoprogramma>();
		for (Object[] lp : listaProspettoCronoprogrammaDiGestione) {
			ProspettoRiassuntivoCronoprogramma lpec = new ProspettoRiassuntivoCronoprogramma();
			//lp[0]
			lpec.setAnno(Integer.valueOf((String)lp[0]));
			
			//lp[1]
			 lpec.setTotaliEntrate((BigDecimal)lp[1]);			
			
			//lp[2]
			 lpec.setTotaliSpese((BigDecimal)lp[2]);
	
			result.add(lpec);
		}
		
		return result;
	}

	public List<ProspettoRiassuntivoCronoprogramma> calcoloProspettoRiassuntivoCronoprogrammaDiGestioneConsulta(Progetto progetto, Integer anno) {

		List<Object[]> listaProspettoCronoprogrammaDiGestione = progettoDao.calcoloProspettoRiassuntivoCronoprogrammaDiGestioneConsulta(progetto.getUid(), anno.toString());
		
		List<ProspettoRiassuntivoCronoprogramma> result = new ArrayList<ProspettoRiassuntivoCronoprogramma>();
		for (Object[] lp : listaProspettoCronoprogrammaDiGestione) {
			ProspettoRiassuntivoCronoprogramma lpec = new ProspettoRiassuntivoCronoprogramma();
			//lp[0]
			lpec.setAnno(Integer.valueOf((String)lp[0]));
			
			//lp[1]
			 lpec.setTotaliEntrate((BigDecimal)lp[1]);			
			
			//lp[2]
			 lpec.setTotaliSpese((BigDecimal)lp[2]);
	
			result.add(lpec);
		}
		
		return result;
	}

	/**
	 * Stacca il codice del progetto
	 * @param anno l'anno del progetto
	 * @param ente l'ente
	 * @return il codice del progetto
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public String staccaCodiceProgetto(Integer anno, Ente ente) {
		final String methodName = "staccaCodiceProgetto";
		SiacTProgrammaNum siacTProgrammaNum = siacTProgrammaNumRepository.findByAnno(anno, ente.getUid());
		
		Date now = new Date();
		if(siacTProgrammaNum == null) {
			siacTProgrammaNum = new SiacTProgrammaNum();
			siacTProgrammaNum.setProgrammaAnno(anno);
			
			SiacTEnteProprietario step = new SiacTEnteProprietario();
			step.setUid(ente.getUid());
			siacTProgrammaNum.setSiacTEnteProprietario(step);
			
			siacTProgrammaNum.setDataCreazione(now);
			siacTProgrammaNum.setDataInizioValidita(now);
			siacTProgrammaNum.setLoginOperazione(loginOperazione);
			siacTProgrammaNum.setProgrammaNumero(1);
		}
		
		siacTProgrammaNum.setDataModifica(now);
		
		siacTProgrammaNumRepository.saveAndFlush(siacTProgrammaNum);
		Integer programmaNumero = siacTProgrammaNum.getProgrammaNumero();
		
		String result = anno + "/" + programmaNumero;
		log.info(methodName, "returning result: " + result + " per l'anno " + anno + " ed ente " + ente.getUid());
		return result;
	}

	/**
	 * Carica tipo progetto.
	 *
	 * @param progetto the progetto
	 * @return the tipo progetto
	 */
	public TipoProgetto caricaTipoProgetto(Progetto progetto) {
		 SiacDProgrammaTipo siacDProgrammaTipo = siacTProgrammaRepository.findSiacDProgrammaTipoByCronopId(progetto.getUid());
		 return siacDProgrammaTipo != null && SiacDProgrammaTipoEnum.byCodice(siacDProgrammaTipo.getProgrammaTipoCode()) != null ? SiacDProgrammaTipoEnum.byCodice(siacDProgrammaTipo.getProgrammaTipoCode()).getTipoProgetto() : null;
	}
	
	/**
	 * Ottiene il valore complessivo progetto.
	 *
	 * @param progetto the progetto
	 * @return the big decimal
	 */
	public BigDecimal caricaValoreComplessivoProgetto(Progetto progetto) {
		return siacTProgrammaRepository.findBigDecimalAttrByIdProgetto(progetto.getUid(), SiacTAttrEnum.ValoreComplessivoProgetto.getCodice());
	}
	
	/**
	 * Gets the bilancio progetto.
	 *
	 * @param progetto the progetto
	 * @return the bilancio progetto
	 */
	public Bilancio getBilancioProgetto(Progetto progetto) {
		SiacTBil siacTBil = siacTProgrammaRepository.findBilancioProgetto(progetto.getUid());
		return mapNotNull(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);
	}
	
	public Progetto ricercaProgetto(Progetto progetto, ModelDetailEnum... progettoModelDetails) {
		
		SiacTProgramma siacTProgramma  = progettoDao.findById(progetto.getUid());

		return siacTProgrammaProgettoMapper.map(siacTProgramma, mapperDecoratorHelper.getDecoratorsFromModelDetails(progettoModelDetails));
	}	
}
