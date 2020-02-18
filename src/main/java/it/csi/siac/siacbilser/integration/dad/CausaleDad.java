/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.CausaleDao;
import it.csi.siac.siacbilser.integration.dao.SiacDCausaleRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDCausaleTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDModelloEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.TipoFamigliaCausale;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe di DAD per la Causale.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CausaleDad extends ExtendedBaseDadImpl {
	
	/** The causale dao. */
	@Autowired
	private CausaleDao causaleDao;

	/** The siac d causale repository. */
	@Autowired
	private SiacDCausaleRepository siacDCausaleRepository;
	
	@Autowired
	private SiacDCausaleTipoRepository siacDCausaleTipoRepository;
	
	/**
	 * Find causale spesa by id.
	 *
	 * @param uid the uid
	 * @return the causale spesa
	 */
	public CausaleSpesa findCausaleSpesaById(Integer uid) {
		final String methodName = "findCausaleSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacDCausale siacDCausale = causaleDao.findById(uid);
		if(siacDCausale == null) {
			log.info(methodName, "Impossibile trovare la causale con id: " + uid);
		}
		return  mapNotNull(siacDCausale, CausaleSpesa.class, BilMapId.SiacDCausale_CausaleSpesa);
	}
	
	/**
	 * Find causale entrata by id.
	 *
	 * @param uid the uid
	 * @return the causale entrata
	 */
	public CausaleEntrata findCausaleEntrataById(Integer uid) {
		final String methodName = "findCausaleEntratById";		
		log.debug(methodName, "uid: "+ uid);
		SiacDCausale siacDCausale = causaleDao.findById(uid);
		if(siacDCausale == null) {
			log.info(methodName, "Impossibile trovare la causale con id: " + uid);
		}
		return  mapNotNull(siacDCausale, CausaleEntrata.class, BilMapId.SiacDCausale_CausaleEntrata);
	}
	
	
	/**
	 * Inserisci causale spesa.
	 *
	 * @param causale the causale
	 */
	public void inserisciCausaleSpesa(CausaleSpesa causale) {
		SiacDCausale siacDCausale = buildSiacDCausale(causale);
		//siacDCausale.setLoginOperazione(loginOperazione);
		//SiacDCausaleFamTipoEnum.Spesa
		causaleDao.create(siacDCausale);
		causale.setUid(siacDCausale.getUid());		
	}
	
	/**
	 * Aggiorna anagrafica causale spesa.
	 *
	 * @param causale the causale
	 */
	public void aggiornaAnagraficaCausaleSpesa(CausaleSpesa causale) {
		SiacDCausale siacDCausale = buildSiacDCausale(causale);
		causaleDao.update(siacDCausale);
		causale.setUid(siacDCausale.getUid());
	}
	
	
	/**
	 * Cancella causale spesa.
	 *
	 * @param causale the causale
	 */
	public void cancellaCausale(Causale causale) {
		SiacDCausale siacDCausale = new SiacDCausale();
		siacDCausale.setUid(causale.getUid());
		causaleDao.delete(siacDCausale);
	}
	
	/**
	 * Annulla causale spesa.
	 *
	 * @param causale the causale
	 */
	public void annullaCausaleSpesa(CausaleSpesa causale) {
		SiacDCausale siacDCausale = causaleDao.findById(causale.getUid());
		siacDCausale.setDataFineValidita(causale.getDataFineValidita());
	}
	
	/**
	 * Annulla causale entrata.
	 *
	 * @param causale the causale
	 */
	public void annullaCausaleEntrata(CausaleEntrata causale) {
		SiacDCausale siacDCausale = causaleDao.findById(causale.getUid());
		siacDCausale.setDataFineValidita(causale.getDataFineValidita());
	}

	/**
	 * Inserisci causale entrata.
	 *
	 * @param causale the causale
	 */
	public void inserisciCausaleEntrata(CausaleEntrata causale) {
		SiacDCausale siacDCausale = buildSiacDCausale(causale);
		siacDCausale.setLoginOperazione(loginOperazione);
		//SiacDCausaleFamTipoEnum.Entrata
		causaleDao.create(siacDCausale);
		causale.setUid(siacDCausale.getUid());
	}
	
	/**
	 * Aggiorna anagrafica causale entrata.
	 *
	 * @param causale the causale
	 */
	public void aggiornaAnagraficaCausaleEntrata(CausaleEntrata causale) {
		SiacDCausale siacDCausale = buildSiacDCausale(causale);
		causaleDao.update(siacDCausale);
		causale.setUid(siacDCausale.getUid());
	}
	
	/**
	 * Builds the siac d causale.
	 *
	 * @param causale the causale
	 * @return the siac d causale
	 */
	private SiacDCausale buildSiacDCausale(CausaleSpesa causale) {		
		SiacDCausale siacDCausale = new SiacDCausale();
		siacDCausale.setLoginOperazione(loginOperazione);
		causale.setLoginOperazione(loginOperazione);
		map(causale, siacDCausale, BilMapId.SiacDCausale_CausaleSpesa);	
		return siacDCausale;
	}
	
	/**
	 * Builds the siac d causale.
	 *
	 * @param causale the causale
	 * @return the siac d causale
	 */
	private SiacDCausale buildSiacDCausale(CausaleEntrata causale) {
		SiacDCausale siacDCausale = new SiacDCausale();
		siacDCausale.setLoginOperazione(loginOperazione);
		causale.setLoginOperazione(loginOperazione);
		map(causale, siacDCausale, BilMapId.SiacDCausale_CausaleEntrata);		
		return siacDCausale;
	}

	
	/**
	 * Ricerca causali spesa by codice.
	 *
	 * @param codiceCausale the codice causale
	 * @return the list
	 */
	public List<CausaleSpesa> ricercaCausaliSpesaByCodiceETipo(String codiceCausale, TipoCausale tipoCausale){
		SiacDCausaleFamTipo siacDCausaleFamTipo = siacDCausaleTipoRepository.findCausaleFamTipoaByUidCausaleTipo(ente.getUid(),tipoCausale.getUid());
		List<SiacDCausale> siacDCausales = siacDCausaleRepository.findCausaleValidaByCodice(ente.getUid(), codiceCausale, siacDCausaleFamTipo.getCausFamTipoCode());
		
		if(siacDCausales == null) {
			return new ArrayList<CausaleSpesa>();
		}
	
		
		return convertiLista(siacDCausales,CausaleSpesa.class,BilMapId.SiacDCausale_CausaleSpesa);
		
		//List<CausaleSpesa> result = new ArrayList<CausaleSpesa>(siacDCausales.size());
//		for (SiacDCausale siacDCausale : siacDCausales) {
//			CausaleSpesa causaleToAdd = mapNotNull(siacDCausale, CausaleSpesa.class, BilMapId.SiacDCausale_CausaleSpesa);
//			result.add(causaleToAdd);
//		}
//		return result;
	}
	
	
	
	/**
	 * Ricerca causali entrata by codice.
	 *
	 * @param codiceCausale the codice causale
	 * @return the list
	 */
	public List<CausaleEntrata> ricercaCausaliEntrataByCodiceETipo(String codiceCausale, TipoCausale tipoCausale){
		SiacDCausaleFamTipo siacDCausaleFamTipo = siacDCausaleTipoRepository.findCausaleFamTipoaByUidCausaleTipo(ente.getUid(), tipoCausale.getUid());
		List<SiacDCausale> siacDCausales = siacDCausaleRepository.findCausaleValidaByCodice(ente.getUid(), codiceCausale, siacDCausaleFamTipo.getCausFamTipoCode());
		
		if(siacDCausales == null) {
			return new ArrayList<CausaleEntrata>();
		}
		
		List<CausaleEntrata> result = new ArrayList<CausaleEntrata>(siacDCausales.size());
		for (SiacDCausale siacDCausale : siacDCausales) {
			CausaleEntrata causaleToAdd = mapNotNull(siacDCausale, CausaleEntrata.class, BilMapId.SiacDCausale_CausaleEntrata);
			result.add(causaleToAdd);
		}
		return result;
	}
	
	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param ente the ente
	 * @param tipoOnere the tipo onere
	 * @return the list
	 */
	public List<Causale770> ricercaCausali770ByTipoOnere(Ente ente, TipoOnere tipoOnere) {
		
		List<SiacDCausale> siacDCausales = causaleDao.ricercaCausaliByEnteProprietarioAndTipoCausaleAndModello(ente.getUid(),
				tipoOnere != null ? tipoOnere.getUid() : null,
				SiacDModelloEnum.CAUSALE_770);
		if(siacDCausales == null) {
			return new ArrayList<Causale770>();
		}
		
		List<Causale770> result = new ArrayList<Causale770>(siacDCausales.size());
		
		for (SiacDCausale causale : siacDCausales) {
			Causale770 causaleToAdd = mapCausale770(causale);
			result.add(causaleToAdd);
		}
		return result;
	}


	/**
	 * Map causale770.
	 *
	 * @param causaleDB the causale db
	 * @return the causale770
	 */
	private Causale770 mapCausale770(SiacDCausale causaleDB) {
		
		Causale770 causaleToAdd = new Causale770();
		causaleToAdd.setUid(causaleDB.getUid());
		causaleToAdd.setCodice(causaleDB.getCausCode());
		causaleToAdd.setDescrizione(causaleDB.getCausDesc());
		
		return causaleToAdd;
	}
	
	/**
	 * Ricerca sintetica causale spesa.
	 *
	 * @param causale the causale
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<CausaleSpesa> ricercaSinteticaCausaleSpesa(CausaleSpesa causale,  ParametriPaginazione parametriPaginazione, TipoFamigliaCausale tipoFamigliaCausale) {
		
		Boolean statoOperativoCausale = null;
		if(StatoOperativoCausale.VALIDA.equals(causale.getStatoOperativoCausale())){
			statoOperativoCausale = Boolean.TRUE;
		}else if (StatoOperativoCausale.ANNULLATA.equals(causale.getStatoOperativoCausale())){
			statoOperativoCausale = Boolean.FALSE;
		}
		SiacDCausaleFamTipoEnum tipo = SiacDCausaleFamTipoEnum.byTipoFamigliaCausale(tipoFamigliaCausale);
		
		Page<SiacDCausale> lista = causaleDao.ricercaSinteticaCausale(
				
				causale.getEnte().getUid(),
				tipo,
				causale.getCodice(),
				causale.getDescrizione(),
				mapToUidIfNotZero(causale.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(causale.getTipoCausale()),
				statoOperativoCausale,
				mapToUidIfNotZero(causale.getCapitoloUscitaGestione()),
				mapToUidIfNotZero(causale.getImpegno()),
				mapToUidIfNotZero(causale.getSubImpegno()),
				mapToUidIfNotZero(causale.getSoggetto()),
			    mapToUidIfNotZero(causale.getAttoAmministrativo()),
			    mapToUidIfNotZero(causale.getModalitaPagamentoSoggetto()),
			    mapToUidIfNotZero(causale.getSedeSecondariaSoggetto()),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, CausaleSpesa.class, BilMapId.SiacDCausale_CausaleSpesa_Base);
		
	}
	
	
/**
 * Ricerca sintetica causale entrata.
 *
 * @param causale the causale
 * @param parametriPaginazione the parametri paginazione
 * @return the lista paginata
 */
public ListaPaginata<CausaleEntrata> ricercaSinteticaCausaleEntrata(CausaleEntrata causale,  ParametriPaginazione parametriPaginazione, TipoFamigliaCausale tipoFamigliaCausale) {
		
		Boolean statoOperativoCausale = null;
		if(StatoOperativoCausale.VALIDA.equals(causale.getStatoOperativoCausale())){
			statoOperativoCausale = Boolean.TRUE;
		}else if (StatoOperativoCausale.ANNULLATA.equals(causale.getStatoOperativoCausale())){
			statoOperativoCausale = Boolean.FALSE;
		}
		SiacDCausaleFamTipoEnum tipo = SiacDCausaleFamTipoEnum.byTipoFamigliaCausale(tipoFamigliaCausale);
		
		Page<SiacDCausale> lista = causaleDao.ricercaSinteticaCausale(
				
				causale.getEnte().getUid(),
				tipo,
				causale.getCodice(),
				causale.getDescrizione(),
				mapToUidIfNotZero(causale.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(causale.getTipoCausale()),
				statoOperativoCausale,
				mapToUidIfNotZero(causale.getCapitoloEntrataGestione()),
				mapToUidIfNotZero(causale.getAccertamento()),
				mapToUidIfNotZero(causale.getSubAccertamento()),
				mapToUidIfNotZero(causale.getSoggetto()),
			    mapToUidIfNotZero(causale.getAttoAmministrativo()),
			    null,
			    null,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, CausaleEntrata.class, BilMapId.SiacDCausale_CausaleEntrata_Base);
		
	}

	/**
	 * Dettaglio storico causale spesa.
	 *
	 * @param causale the causale
	 * @return the list
	 */
	public List<CausaleSpesa> dettaglioStoricoCausaleSpesa(CausaleSpesa causale) {
		String methodName="dettaglioStoricoCausaleSpesa";
		SiacDCausale siacDCausale = siacDCausaleRepository.findOne(causale.getUid());
		
		List<Date> dateStoricoCausale = siacDCausaleRepository.findDateStoricoCausale(causale.getUid());
		
		log.debug(methodName, "dateStoricoCausale: " + dateStoricoCausale);
		List<CausaleSpesa> result = new ArrayList<CausaleSpesa>();
		
		for(Date dateToExtract : dateStoricoCausale) {
		
			siacDCausale.setDateToExtract(dateToExtract);
			CausaleSpesa c = map(siacDCausale, CausaleSpesa.class, BilMapId.SiacDCausale_CausaleSpesa_Base);
			result.add(c);
		
		}
		
		return result;
	}
	
	
	/**
	 * Dettaglio storico causale entrata.
	 *
	 * @param causale the causale
	 * @return the list
	 */
	public List<CausaleEntrata> dettaglioStoricoCausaleEntrata(CausaleEntrata causale) {
		String methodName = "dettaglioStoricoCausaleEntrata";

		SiacDCausale siacDCausale = siacDCausaleRepository.findOne(causale.getUid());
		
		List<Date> dateStoricoCausale = siacDCausaleRepository.findDateStoricoCausale(causale.getUid());
		
		log.debug(methodName, "dateStoricoCausale: "+dateStoricoCausale);
		
		List<CausaleEntrata> result = new ArrayList<CausaleEntrata>();
		
		for(Date dateToExtract : dateStoricoCausale) {
		
			siacDCausale.setDateToExtract(dateToExtract);
			CausaleEntrata c = map(siacDCausale, CausaleEntrata.class, BilMapId.SiacDCausale_CausaleEntrata_Base);
			result.add(c);
		
		}
		
		return result;
	}


	public TipoCausale ricercaTipoCausaleVersamento() {
		SiacDCausaleTipo siacDCausaleTipo = siacDCausaleTipoRepository.findCausaleTipoByEnteECodice(ente.getUid(), "VER");
		//TipoCausale tipoCausale = map(siacDCausaleTipo, TipoCausale.class, BilMapId.SiacDCausaleTipo_TipoCausale);
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setUid(siacDCausaleTipo.getUid());
		return tipoCausale;
		
	}
	
	public TipoCausale ricercaTipoCausaleRitenuta() {
		SiacDCausaleTipo siacDCausaleTipo = siacDCausaleTipoRepository.findCausaleTipoByEnteECodice(ente.getUid(), "RIT");
		//TipoCausale tipoCausale = map(siacDCausaleTipo, TipoCausale.class, BilMapId.SiacDCausaleTipo_TipoCausale);
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setUid(siacDCausaleTipo.getUid());
		return tipoCausale;
		
	}

}
