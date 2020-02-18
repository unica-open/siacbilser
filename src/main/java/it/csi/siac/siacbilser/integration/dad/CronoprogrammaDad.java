/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.CronoprogrammaDao;
import it.csi.siac.siacbilser.integration.dao.DettaglioCronoprogrammaDao;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTCronopElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTCronopRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCronopStato;
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCronopStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.BooleanToStringConverter;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;

/**
 * Dad per Cronoprogramma.
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CronoprogrammaDad extends ExtendedBaseDadImpl {
	
	/** The siac t cronop repository. */
	@Autowired
	private SiacTCronopRepository siacTCronopRepository;	
	
	/** The siac t cronop elem repository. */
	@Autowired
	private SiacTCronopElemRepository siacTCronopElemRepository;
		
	/** The cronoprogramma dao. */
	@Autowired
	private CronoprogrammaDao cronoprogrammaDao;
	
	/** The dettaglio cronoprogramma dao. */
	@Autowired
	private DettaglioCronoprogrammaDao dettaglioCronoprogrammaDao;
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Find cronoprogramma by id.
	 *
	 * @param uid the uid
	 * @return the cronoprogramma
	 */
	public Cronoprogramma findCronoprogrammaById(Integer uid) {
		final String methodName = "findCronoprogrammaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTCronop siacTCronop = siacTCronopRepository.findOne(uid);
		if(siacTCronop == null) {
			log.debug(methodName, "Impossibile trovare il cronoprogramma con id: " + uid);
		}
		return  mapNotNull(siacTCronop, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma);
	}
	
	/**
	 * Find cronoprogramma by id.
	 *
	 * @param uid the uid
	 * @return the cronoprogramma
	 */
	public Cronoprogramma findCronoprogrammaByIdModelDetail(Integer uid) {
		final String methodName = "findCronoprogrammaById";		
		SiacTCronop siacTCronop = siacTCronopRepository.findOne(uid);
		if(siacTCronop == null) {
			log.debug(methodName, "Impossibile trovare il cronoprogramma con id: " + uid);
		}
		return  mapNotNull(siacTCronop, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma_ModelDetail);
	}
	
	/**
	 * Find cronoprogramma by codice and stato operativo and codice e stato progetto.
	 *
	 * @param c the c
	 * @return the cronoprogramma
	 */
	public List<Cronoprogramma> findCronoprogrammiByCodiceAndStatoOperativoAndCodiceEStatoProgetto(Cronoprogramma c) {
		final String methodName = "findCronoprogrammaByCodiceAndStatoOperativoAndCodiceEStatoProgetto";	
		List<SiacTCronop> siacTCronop = siacTCronopRepository.findCronoprogrammaByCodiceAndStatoOperativoAndCodiceEStatoProgetto(c.getCodice(), 
				SiacDCronopStatoEnum.byStatoOperativo(c.getStatoOperativoCronoprogramma()).getCodice(),
				c.getProgetto().getCodice(),
				SiacDProgrammaStatoEnum.byStatoOperativo(c.getProgetto().getStatoOperativoProgetto()).getCodice(),
				c.getBilancio().getUid(),
				ente.getUid());
		
		if(siacTCronop == null || siacTCronop.isEmpty()) {
			log.debug(methodName, "Impossibile trovare il cronoprogramma con codice/stato: " + c.getCodice() + "/" + c.getStatoOperativoCronoprogramma());
		}
		return convertiLista(siacTCronop, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma);
	}
	
	/**
	 * Find cronoprogramma by codice and stato operativo and uid progetto.
	 *
	 * @param c the c
	 * @return the cronoprogramma
	 */
	public List<Cronoprogramma> findCronoprogrammiByCodiceAndStatoOperativoAndUidProgetto(Cronoprogramma c) {
		final String methodName = "findCronoprogrammaByCodiceAndStatoOperativoAndUidProgetto";	
		List<SiacTCronop> siacTCronop = siacTCronopRepository.findCronoprogrammaByCodiceAndStatoOperativoAndUidProgetto(c.getCodice(), 
				SiacDCronopStatoEnum.byStatoOperativo(c.getStatoOperativoCronoprogramma()).getCodice(),
				c.getProgetto().getUid(),
				c.getBilancio().getUid(),
				ente.getUid());
		
		if(siacTCronop == null || siacTCronop.isEmpty()) {
			log.debug(methodName, "Impossibile trovare il cronoprogramma con codice/stato: " + c.getCodice() + "/" + c.getStatoOperativoCronoprogramma() 
					+ " ui progetto:"+c.getProgetto().getUid());
		}
		return convertiLista(siacTCronop, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma);
	}
	
	/**
	 * Find uid cronoprogramma con stesso codice and stato operativo and uid progetto.
	 *
	 * @param c the c
	 * @return the integer
	 */
	public List<Integer> findUidCronoprogrammaConStessoCodiceNonAnnullatoAndUidProgettoAndBilancio(Cronoprogramma c) {
		List<Integer> siacTCronop = siacTCronopRepository.cronopIdNotInStato(
				c.getCodice(), 
				SiacDCronopStatoEnum.byStatoOperativo(StatoOperativoCronoprogramma.ANNULLATO).getCodice(),
				c.getProgetto().getUid(),
				ente.getUid(),
				c.getBilancio().getUid());
		
		return siacTCronop;
	}
	
	/**
	 * Inserisci anagrafica cronoprogramma.
	 *
	 * @param cronoprogramma the cronoprogramma
	 */
	public void inserisciAnagraficaCronoprogramma(Cronoprogramma cronoprogramma) {		
		SiacTCronop siacTCronop = buildSiacTCronop(cronoprogramma);	
		cronoprogrammaDao.create(siacTCronop);
		cronoprogramma.setUid(siacTCronop.getUid());
	}	
	
	/**
	 * Aggiorna anagrafica cronoprogramma.
	 *
	 * @param cronoprogramma the cronoprogramma
	 */
	public void aggiornaAnagraficaCronoprogramma(Cronoprogramma cronoprogramma) {
		SiacTCronop siacTCronop = buildSiacTCronop(cronoprogramma);
		cronoprogrammaDao.update(siacTCronop);
		cronoprogramma.setUid(siacTCronop.getUid());
	}	

	/**
	 * Builds the siac t cronop.
	 *
	 * @param cronoprogramma the cronoprogramma
	 * @return the siac t cronop
	 */
	private SiacTCronop buildSiacTCronop(Cronoprogramma cronoprogramma) {
		SiacTCronop siacTCronop = new SiacTCronop();		
		siacTCronop.setLoginOperazione(loginOperazione);
		cronoprogramma.setLoginOperazione(loginOperazione);
		map(cronoprogramma, siacTCronop, BilMapId.SiacTCronop_Cronoprogramma);
		return siacTCronop;
	}
	
	/**
	 * Ricerca i Cronoprogrammi collegati al Progetto con id passato come parametro.
	 *
	 * @param idProgetto the id progetto
	 * @return the list
	 */
	public List<Cronoprogramma> findCronoprogrammiByIdProgetto(Integer idProgetto) {
		List<SiacTCronop> siacTCronops = siacTCronopRepository.findSiacTCronopBySiacTProgettoId(idProgetto);
		
		return convertiLista(siacTCronops, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma);
	
	}
	
	/**
	 * Ricerca i Cronoprogrammi collegati al progetto e al bilancio.
	 *
	 * @param progetto the progetto
	 * @param bilancio the bilancio
	 * @return the list
	 */
	public List<Cronoprogramma> findCronoprogrammiByProgettoAndBilancio(Progetto progetto, Bilancio bilancio) {
		List<SiacTCronop> siacTCronops = siacTCronopRepository.findSiacTCronopBySiacTProgettoAndSiacTBilNotWithSiacDCronopStato(progetto.getUid(), bilancio.getUid(), SiacDCronopStatoEnum.Annullato.getCodice());
		
		return convertiLista(siacTCronops, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma);
	}

//	private SiacTPeriodo getSiacTPeriodoAssociatoABilancio() {		
//		SiacTBil siacTBil = siacTBilRepository.findOne(bilancio.getUid());
//		log.debug("getSiacTPeriodoAssociatoABilancio", "returning "+siacTBil.getSiacTPeriodo().getUid());
//		return siacTBil.getSiacTPeriodo();
//	}
	
	/**
 * Inserisce un dettaglio entrata cronoprogramma.
 *
 * @param dett the dett
	 * @param tipoCapitoloDettaglio 
 */
	public void inserisciDettaglioEntrataCronoprogramma(DettaglioEntrataCronoprogramma dett, TipoCapitolo tipoCapitoloDettaglio) {
		
		SiacTCronopElem siacTCronopElem = buildSiacTCronopElemEntrata(dett);
		SiacDBilElemTipoEnum byTipoCapitolo = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitoloDettaglio);
		siacTCronopElem.setSiacDBilElemTipo(eef.getEntity(byTipoCapitolo, ente.getUid(), SiacDBilElemTipo.class));
		dettaglioCronoprogrammaDao.create(siacTCronopElem);
		dett.setUid(siacTCronopElem.getUid());
	}
	
	/**
	 * Aggiorna un dettaglio entrata cronoprogramma.
	 *
	 * @param dett the dett
	 */
	public void aggiornaDettaglioEntrataCronoprogramma(DettaglioEntrataCronoprogramma dett, TipoCapitolo tipoCapitolo) {
		
		SiacTCronopElem siacTCronopElem = buildSiacTCronopElemEntrata(dett);
		SiacDBilElemTipoEnum siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo);
		siacTCronopElem.setSiacDBilElemTipo(eef.getEntity(siacDBilElemTipoEnum, ente.getUid(), SiacDBilElemTipo.class));
		dettaglioCronoprogrammaDao.update(siacTCronopElem);
		dett.setUid(siacTCronopElem.getUid());
	}
	
	
	
	/**
	 * Inserisce un dettaglio spesa cronoprogramma.
	 *
	 * @param dett the dett
	 * @param tipoCapitoloDettaglio 
	 */
	public void inserisciDettaglioUscitaCronoprogramma(DettaglioUscitaCronoprogramma dett, TipoCapitolo tipoCapitoloDettaglio) {		

		SiacTCronopElem siacTCronopElem = buildSiacTCronopElemUscita(dett);
		// Le spese non sono avanzi di amministrazione, per i dettagli
		siacTCronopElem.setCronopElemIsAvaAmm(Boolean.FALSE);
		SiacDBilElemTipoEnum siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitoloDettaglio);
		siacTCronopElem.setSiacDBilElemTipo(eef.getEntity(siacDBilElemTipoEnum, ente.getUid(), SiacDBilElemTipo.class));
		dettaglioCronoprogrammaDao.create(siacTCronopElem);
		dett.setUid(siacTCronopElem.getUid());
	}
	
	/**
	 * Aggiorna un dettaglio spesa cronoprogramma.
	 *
	 * @param dett the dett
	 * @param tipoCapitoloDettaglio 
	 */
	public void aggiornaDettaglioUscitaCronoprogramma(DettaglioUscitaCronoprogramma dett, TipoCapitolo tipoCapitoloDettaglio) {		

		SiacTCronopElem siacTCronopElem = buildSiacTCronopElemUscita(dett);
		SiacDBilElemTipoEnum siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitoloDettaglio);
		// Le spese non sono avanzi di amministrazione, per i dettagli
		siacTCronopElem.setCronopElemIsAvaAmm(Boolean.FALSE);
		siacTCronopElem.setSiacDBilElemTipo(eef.getEntity(siacDBilElemTipoEnum, ente.getUid(), SiacDBilElemTipo.class));
		dettaglioCronoprogrammaDao.update(siacTCronopElem);
		dett.setUid(siacTCronopElem.getUid());
	}

	
	/**
	 * Elimina il dettaglio entrata di un cronoprogramma.
	 *
	 * @param dett the dett
	 */
	public void cancellaDettaglioEntrataCronoprogramma(DettaglioEntrataCronoprogramma dett) {
		cancellaDettaglioCronoprogramma(dett.getUid());
		
	}
	
	/**
	 * Elimina il dettaglio uscita di un cronoprogramma.
	 *
	 * @param dett the dett
	 */
	public void cancellaDettaglioUscitaCronoprogramma(DettaglioUscitaCronoprogramma dett) {
		cancellaDettaglioCronoprogramma(dett.getUid());
		
	}

	/**
	 * Cancella dettaglio cronoprogramma.
	 *
	 * @param uidDettaglioCronoprogramma the uid dettaglio cronoprogramma
	 */
	private void cancellaDettaglioCronoprogramma(Integer uidDettaglioCronoprogramma) {
		SiacTCronopElem siacTCronopElem = siacTCronopElemRepository.findOne(uidDettaglioCronoprogramma);
		
		if(siacTCronopElem == null) {
			throw new IllegalArgumentException("Impossibile trovare il dettaglio cronoprogramma con uid: "+ uidDettaglioCronoprogramma);
		}
		
		dettaglioCronoprogrammaDao.delete(siacTCronopElem);
	}
	
	
	
	/**
	 * Builds the siac t cronop elem entrata.
	 *
	 * @param dett the dett
	 * @return the siac t cronop elem
	 */
	private SiacTCronopElem buildSiacTCronopElemEntrata(DettaglioEntrataCronoprogramma dett) {
		SiacTCronopElem siacTCronopElem = new SiacTCronopElem();
		siacTCronopElem.setLivello(1);
		siacTCronopElem.setLoginOperazione(loginOperazione);
		dett.setLoginOperazione(loginOperazione);
		map(dett, siacTCronopElem, BilMapId.SiacTCronopElem_DettaglioEntrataCronoprogramma);
		return siacTCronopElem;
	}
	
	/**
	 * Builds the siac t cronop elem uscita.
	 *
	 * @param dett the dett
	 * @return the siac t cronop elem
	 */
	private SiacTCronopElem buildSiacTCronopElemUscita(DettaglioUscitaCronoprogramma dett) {
		SiacTCronopElem siacTCronopElem = new SiacTCronopElem();
		siacTCronopElem.setLivello(1);
		siacTCronopElem.setLoginOperazione(loginOperazione);
		dett.setLoginOperazione(loginOperazione);
		map(dett, siacTCronopElem, BilMapId.SiacTCronopElem_DettaglioUscitaCronoprogramma);
		return siacTCronopElem;
	}

	

	

	/**
	 * Aggiorna lo stato operativo di un Cronoprogramma con quello passato come parametro.
	 *
	 * @param cronoprogramma the cronoprogramma
	 * @param statoOperativo the stato operativo
	 */
	public void aggiornaStatoOperativoCronoprogramma(Cronoprogramma cronoprogramma, StatoOperativoCronoprogramma statoOperativo) {
		SiacTCronop siacTCronop = siacTCronopRepository.findOne(cronoprogramma.getUid());
		List<SiacRCronopStato> siacRCronopStatos = siacTCronop.getSiacRCronopStatos();
		Date dataCancellazione = new Date();
		for (SiacRCronopStato siacRCronopStato : siacRCronopStatos) {			
			if(siacRCronopStato.getDataCancellazione()==null){
				siacRCronopStato.setDataCancellazione(dataCancellazione);
				siacRCronopStato.setDataFineValidita(dataCancellazione);
			}
		}
		SiacRCronopStato siacRCronopStato = new SiacRCronopStato();
		//Integer enteId = ente.getUid();
		Integer enteId = siacTCronop.getSiacTEnteProprietario().getUid();
		siacRCronopStato.setSiacDCronopStato(eef.getEntity(SiacDCronopStatoEnum.byStatoOperativo(statoOperativo),  enteId, SiacDCronopStato.class));
		siacRCronopStato.setDataModificaInserimento(new Date());
		siacRCronopStato.setSiacTEnteProprietario(siacTCronop.getSiacTEnteProprietario());
		siacRCronopStato.setSiacTCronop(siacTCronop);
		siacRCronopStato.setLoginOperazione(loginOperazione);
		siacRCronopStatos.add(siacRCronopStato);
		
	}

	/**
	 * Ricerca i dettagli di entrata associati ad un Cronoprogramma.
	 *
	 * @param c the c
	 * @return the list
	 */
	public List<DettaglioEntrataCronoprogramma> findDettagliEntrataCronoprogramma(Cronoprogramma c, TipoCapitolo tipoCapitolo) {
		List<SiacTCronopElem> siacTCronopElems = siacTCronopRepository.findCronopElemsByCronopIdAndElemTipoCode(c.getUid(), SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice());
		
		return convertiLista(siacTCronopElems, DettaglioEntrataCronoprogramma.class, BilMapId.SiacTCronopElem_DettaglioEntrataCronoprogramma);
		
	}

	/**
	 * Ricerca i dettagli di uscita associati ad un Cronoprogramma.
	 *
	 * @param c the c
	 * @return the list
	 */
	public List<DettaglioUscitaCronoprogramma> findDettagliUscitaCronoprogramma(Cronoprogramma c, TipoCapitolo tipoCapitolo) {
		List<SiacTCronopElem> siacTCronopElems = siacTCronopRepository.findCronopElemsByCronopIdAndElemTipoCode(c.getUid(), SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice());
		
		return convertiLista(siacTCronopElems, DettaglioUscitaCronoprogramma.class, BilMapId.SiacTCronopElem_DettaglioUscitaCronoprogramma);
	}

	//aggiunti il 24/03/2015 ahmad
	/**
	 * Aggiorna lo stato del flag usato per fpv del cronoprogramma .
	 *
	 * @param cronoprogramma the cronoprogramma
	 * 
	 */
	public void cambiaFlagUsatoPerFpv(Cronoprogramma cronoprogramma) {
		SiacTCronop siacTCronop = siacTCronopRepository.findOne(cronoprogramma.getUid());
		//Integer enteId = ente.getUid();
		siacTCronop.setUsatoPerFpv(cronoprogramma.getUsatoPerFpv());
		// se un crono e' usato per fpv, e' anche usato per fpv in provvisorio
		siacTCronop.setUsatoPerFpvProv(cronoprogramma.getUsatoPerFpvProv());
	    siacTCronop.setDataModifica(new Date());
		
	}

	public boolean isDaDefinire(Cronoprogramma cronoprogramma) {
		List<SiacRCronopAttr> siacRCronopAttrs = siacTCronopRepository.findCronopRAttrByCronopIdAndAttrCode(cronoprogramma.getUid(), Arrays.asList(SiacTAttrEnum.FlagCronoprogrammaDaDefinire.getCodice()));
		if(siacRCronopAttrs == null || siacRCronopAttrs.isEmpty()) {
			return false;
		}
		String bool = siacRCronopAttrs.get(0).getBoolean_();
		BooleanToStringConverter c = new BooleanToStringConverter();
		return Boolean.TRUE.equals(c.convertFrom(bool));
	}
	
	
	/**
	 * Calcolo FPV Entrata tramite la function.
	 *
	 * @param progetto the progetto
	 * @param anno the anno
	 * @return the list
	 */
	public List<FondoPluriennaleVincolatoEntrata> calcoloFpvEntrataPrevisione(Cronoprogramma cronoprogramma, Integer anno, FaseBilancio faseBilancio) {

		//List<Object[]> listaFpvEntrata = cronoprogrammaDao.calcoloFpvEntrataPrevisione(cronoprogramma.getUid());
		
		List<Object[]> listaFpvEntrata = new ArrayList<Object[]>();
		if(faseBilancio!=null && FaseBilancio.GESTIONE.equals(faseBilancio) ) {
			listaFpvEntrata = cronoprogrammaDao.calcoloFpvEntrataGestione(cronoprogramma.getUid());
		}else{
			listaFpvEntrata = cronoprogrammaDao.calcoloFpvEntrataPrevisione(cronoprogramma.getUid());
		}
		
		
		List<FondoPluriennaleVincolatoEntrata> result = new ArrayList<FondoPluriennaleVincolatoEntrata>();

		for (Object[] fpv : listaFpvEntrata) {
			FondoPluriennaleVincolatoEntrata fpvec = new FondoPluriennaleVincolatoEntrata();
			Integer annoFpv = Integer.valueOf((String)fpv[0]);

			//if(anno>=annoFpv){
			//if(anno.compareTo(annoFpv) <= 0){
				//fpv[0] ---> anno_out
				fpvec.setAnno(annoFpv);
				
				//fpv[1] ---> fpv_entrata_prevista
				fpvec.setImporto((BigDecimal)fpv[1]);
				
				//fpv[2] ----> fpv_entrata_spesa_corrente
				
				fpvec.setFpvEntrataSpesaCorrente((BigDecimal)fpv[2]);
				
				//fpv[3] ----> fpv_entrata_spesa_conto_capitale
				
				fpvec.setFpvEntrataSpesaContoCapitale((BigDecimal)fpv[3]);
				
				//fpv[4] ----> totale
				fpvec.setTotale((BigDecimal)fpv[4]);
				
				//fpv[5]  ---> fpv_entrata_complessivo
				fpvec.setImportoFPV((BigDecimal)fpv[5]);
		
				result.add(fpvec);
			//}
		}
		
		return result;
	}
	
	/**
	 * Calcolo FPV Spesa tramite la function.
	 *
	 * @param progetto the progetto
	 * @param anno the anno
	 * @param faseBilancio 
	 * @return the list
	 */
	public List<FondoPluriennaleVincolatoUscitaCronoprogramma> calcoloFpvSpesaPrevisione(Cronoprogramma cronoprogramma, Integer anno, FaseBilancio faseBilancio) {

		//SIAC-6937
		List<Object[]> listaFpvSpesa = new ArrayList<Object[]>();
		if(faseBilancio!=null && FaseBilancio.GESTIONE.equals(faseBilancio) ) {
			listaFpvSpesa = cronoprogrammaDao.calcoloFpvSpesaGestione(cronoprogramma.getUid());
		}else{
			listaFpvSpesa = cronoprogrammaDao.calcoloFpvSpesaPrevisione(cronoprogramma.getUid());
		}
		
		List<FondoPluriennaleVincolatoUscitaCronoprogramma> result = new ArrayList<FondoPluriennaleVincolatoUscitaCronoprogramma>();
		for (Object[] fpv : listaFpvSpesa) {
			FondoPluriennaleVincolatoUscitaCronoprogramma fpvuc = new FondoPluriennaleVincolatoUscitaCronoprogramma();
			Integer annoFpv = Integer.valueOf((String)fpv[3]);
			//if(anno>=annoFpv){
			//if(anno.compareTo(annoFpv) <= 0){

				//fpv[0] ---> missione
				Missione missione = new Missione();
				missione.setCodice((String)fpv[0]);
				fpvuc.setMissione(missione);
				
				//fpv[1] ---> programma
				Programma programma = new Programma();
				programma.setCodice((String)fpv[1]);
				fpvuc.setProgramma(programma);
				
				//fpv[2] ---> titolo
				TitoloSpesa titoloSpesa = new TitoloSpesa();
				titoloSpesa.setCodice((String)fpv[1]);
				fpvuc.setTitoloSpesa(titoloSpesa);
				
				//fpv[3] ---> anno_out			
				fpvuc.setAnno(annoFpv);
				
				//fpv[4] ---> spesa_prevista
				fpvuc.setImporto((BigDecimal)fpv[4]);
				
				//fpv[5] ---> fpv_spesa
				fpvuc.setImportoFPV((BigDecimal)fpv[5]);
				
				
				result.add(fpvuc);
			//}
		}
		
		return result;
	}
	
	/**
	 * Gets the tipo progetto by cronoprogramma.
	 *
	 * @param crono the crono
	 * @return the tipo progetto by cronoprogramma
	 */
	//SIAC-6255
	public TipoProgetto getTipoProgettoByCronoprogramma(Cronoprogramma crono) {
		SiacDProgrammaTipo siacDProgrammaTipo = siacTCronopRepository.findSiacDProgrammaTipoByCronopId(crono.getUid());
		if(siacDProgrammaTipo == null) {
			return null;
		}
		
		SiacDProgrammaTipoEnum siacDProgrammaTipoEnum = SiacDProgrammaTipoEnum.byCodice(siacDProgrammaTipo.getProgrammaTipoCode());
		return siacDProgrammaTipoEnum != null? siacDProgrammaTipoEnum.getTipoProgetto() : null;
	}
	
	
	/**
	 * Ricerca i Cronoprogrammi collegati al Progetto con id passato come parametro.
	 *
	 * @param progetto the progetto
	 * @param annoBilancioCronoProgrammi 
	 * @param annoBilancio 
	 * @return the list
	 */
	public List<Cronoprogramma> findCronoprogrammiByProgetto(Progetto progetto, Integer annoBilancioCronoProgrammi) {
		SiacDProgrammaTipoEnum siacDProgrammaTipoEnum = SiacDProgrammaTipoEnum.byTipoProgettoEvenNull(progetto.getTipoProgetto());
		List<SiacTCronop> siacTCronops = cronoprogrammaDao.ricercaCronoprogrammaByProgetto(progetto.getUid(),
				progetto.getCodice(),
				siacDProgrammaTipoEnum != null? siacDProgrammaTipoEnum.getCodice() : null,
						annoBilancioCronoProgrammi != null && annoBilancioCronoProgrammi != 0? annoBilancioCronoProgrammi.toString() : null,
						ente.getUid()				
						);
		
		return convertiLista(siacTCronops, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma);
	
	}
	
	public List<Cronoprogramma> findCronoprogrammiByProvvedimento(BigInteger attoammNumero, Integer attoammAnno, Integer attoammTipoId, Integer attoammSacId) {
		return convertiLista(siacTCronopRepository.findSiacTCronopBySiacTAttoAmm(attoammNumero.intValue(), String.valueOf(attoammAnno), attoammTipoId, attoammSacId, ente.getUid()), 
				Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma);
	}
	
	/**
	 * Aggiorna stato cronoprogrammi by provvedimento.
	 *
	 * @param attoAmministrativoAggiornato the atto amministrativo aggiornato
	 * @param statoOld the stato old
	 * @param statoNew the stato new
	 */
	public void aggiornaStatoCronoprogrammiByProvvedimento(AttoAmministrativo attoAmministrativoAggiornato,	StatoOperativoCronoprogramma statoOld, StatoOperativoCronoprogramma statoNew) {
		List<SiacTCronop> cronos = siacTCronopRepository.findCronopBySiacTAttoAmmIdAndStatoCode(attoAmministrativoAggiornato.getUid(), SiacDCronopStatoEnum.byStatoOperativo(statoOld).getCodice());
		if(cronos== null || cronos.isEmpty()) {
			return;
		}
		Date now = new Date();
		for (SiacTCronop siacTCronop : cronos) {
			if(siacTCronop.getSiacRCronopStatos() == null) {
				siacTCronop.setSiacRCronopStatos(new ArrayList<SiacRCronopStato>());
			}
			for (SiacRCronopStato siacRStato : siacTCronop.getSiacRCronopStatos()) {
				siacRStato.setDataCancellazioneIfNotSet(now);
			}
			SiacRCronopStato siacRStatoNew = new SiacRCronopStato();
			siacRStatoNew.setDataModificaInserimento(now);
			siacRStatoNew.setLoginOperazione(loginOperazione);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setEnteProprietarioId(ente.getUid());
			siacRStatoNew.setSiacTEnteProprietario(siacTEnteProprietario);
			
			SiacDCronopStatoEnum siacDCronopStatoNewEnum = SiacDCronopStatoEnum.byStatoOperativo(statoNew);
			SiacDCronopStato siacDCronopStato = eef.getEntity(siacDCronopStatoNewEnum, ente.getUid());
			siacRStatoNew.setSiacDCronopStato(siacDCronopStato);
			
			siacTCronop.addSiacRCronopStato(siacRStatoNew);
			
			siacTCronopRepository.saveAndFlush(siacTCronop);
		}
	}

	public List<String> findCronoprogrammiConSpeseMaggioriImportoProgetto(Progetto progetto) {
		if(progetto == null || progetto.getValoreComplessivo() == null) {
			return new ArrayList<String>();
		}
		return siacTCronopRepository.findCronopCodeConSpeseMaggioriImporto(progetto.getUid(), progetto.getValoreComplessivo());
	}

}
