/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconStampaDao;
import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconStampaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampaValore;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StampaGiornale;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.StampeCassaFileModelDetail;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Data access delegate di un Cointatore stampe cassa.
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class StampeCassaFileDad extends ExtendedBaseDadImpl {



	@Autowired
	private SiacTCassaEconStampaRepository siacTCassaEconStampaRepository;
	@Autowired
	private SiacTCassaEconStampaDao siacTCassaEconStampaDao;
	
	/**
	 * Ricerca la stampa cassa per uid
	 * @param uid l'uid della stampa
	 * @return la stampa corrispondente all'uid
	 */
	public StampeCassaFile findByUid(Integer uid) {
		SiacTCassaEconStampa siacTCassaEconStampa = siacTCassaEconStampaRepository.findOne(uid);
		return mapNotNull(siacTCassaEconStampa, StampeCassaFile.class, CecMapId.SiacTCassaEconStampa_StampeCassaFile_Base);
	}
	
	/**
	 * Ricerca la stampa cassa per uid, con model detail
	 * @param uid l'uid della stampa
	 * @param modelDetails i dettagli del modello da reperire
	 * @return la stampa corrispondente all'uid
	 */
	public StampeCassaFile findByUidModelDetail(Integer uid, StampeCassaFileModelDetail... modelDetails) {
		SiacTCassaEconStampa siacTCassaEconStampa = siacTCassaEconStampaRepository.findOne(uid);
		return mapNotNull(siacTCassaEconStampa, StampeCassaFile.class, CecMapId.SiacTCassaEconStampa_StampeCassaFile_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Ricerca stampe Cassa con parametri di ricerca periodo
	 *
	 * @param stampaCassaFile la stampa cassa
	 * 
	 * @return la lista delle stampe trovate
	 */
	public List<StampeCassaFile> findAllStampeByTipoDocumentoOrderByDataModifica(StampeCassaFile stampaCassaFile) {
		
		//Integer uidPeriodo = periodoDad.findPeriodoUid(stampaCassaFile.getPeriodo(), stampaCassaFile.getAnnoEsercizio());
		List<SiacTCassaEconStampa> lista = siacTCassaEconStampaRepository.findAllStampeByTipoDocumentoOrderByDataModifica(stampaCassaFile.getCassaEconomale().getUid(), stampaCassaFile.getTipoDocumento().getCodice(), stampaCassaFile.getEnte().getUid());
		return convertiLista(lista, StampeCassaFile.class, CecMapId.SiacTCassaEconStampa_StampeCassaFile_Base);
	}
	
	/**
	 * Ricerca stampe Cassa con parametri di ricerca periodo con model detail
	 *
	 * @param stampaCassaFile la stampa cassa
	 * @param modelDetails i model detail
	 * 
	 * @return la lista delle stampe trovate
	 */
	public List<StampeCassaFile> findAllStampeByTipoDocumentoAndStatoOrderByDataModificaModelDetail(StampeCassaFile stampaCassaFile, StampeCassaFileModelDetail... modelDetails) {
		
		//Integer uidPeriodo = periodoDad.findPeriodoUid(stampaCassaFile.getPeriodo(), stampaCassaFile.getAnnoEsercizio());
		List<SiacTCassaEconStampa> lista = siacTCassaEconStampaRepository.findAllStampeByCassaEconomaleStampaStatoAndCassaEconStampaStatoOrderByDataModifica(stampaCassaFile.getCassaEconomale().getUid(),
				stampaCassaFile.getTipoDocumento().getCodice(), SiacDCassaEconStampaStatoEnum.byTipoStampa(stampaCassaFile.getTipoStampa()).getCodice(), stampaCassaFile.getEnte().getUid(), stampaCassaFile.getBilancio().getUid());
		return convertiLista(lista, StampeCassaFile.class, CecMapId.SiacTCassaEconStampa_StampeCassaFile_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	
	
	/**
	 *Trova tutte le stampe che corrispondono al periodo richiesto, o per cui data inizio rientra nel periodo salvato
	 *
	 * @param stampaCassaFile la stampa cassa
	 * 
	 * @return la lista delle stampe trovate
	 */
	public List<StampeCassaFile> findAllStampeCorrispondentiPeriodo(StampeCassaFile stampaCassaFile) {
		
		//Integer uidPeriodo = periodoDad.findPeriodoUid(stampaCassaFile.getPeriodo(), stampaCassaFile.getAnnoEsercizio());
		List<SiacTCassaEconStampa> lista = siacTCassaEconStampaRepository.findAllStampeCorrispondentiPeriodoRendiconto(stampaCassaFile.getStampaRendiconto().getPeriodoDataInizio(),
				stampaCassaFile.getStampaRendiconto().getPeriodoDataFine(),
				stampaCassaFile.getCassaEconomale().getUid(),
				stampaCassaFile.getTipoDocumento().getCodice(),
				stampaCassaFile.getTipoStampa().getCodice(),
				stampaCassaFile.getEnte().getUid());
		return convertiLista(lista, StampeCassaFile.class, CecMapId.SiacTCassaEconStampa_StampeCassaFile_Base);
	}
	
	/**
	 * Stampa corrispondente all'ultimo numero rendiconto staccato
	 *
	 * @param stampaCassaFile la stampa cassa
	 * 
	 * @return la lista delle stampe trovate
	 */
	public StampaRendiconto findStampaUltimoNumeroRendiconto(StampeCassaFile stampaCassaFile) {
		
		//Integer uidPeriodo = periodoDad.findPeriodoUid(stampaCassaFile.getPeriodo(), stampaCassaFile.getAnnoEsercizio());
		Iterable<SiacDCassaEconStampaStatoEnum> cestStatos = Arrays.asList(SiacDCassaEconStampaStatoEnum.Definitiva);
		Collection<String> cestStatoCode = new HashSet<String>();
		for(SiacDCassaEconStampaStatoEnum sdcesse : cestStatos) {
			cestStatoCode.add(sdcesse.getCodice());
		}
		
		List<SiacTCassaEconStampaValore> lista = siacTCassaEconStampaRepository.findStampaUltimoNumeroRendiconto( stampaCassaFile.getCassaEconomale().getUid(), stampaCassaFile.getTipoDocumento().getCodice(), ente.getUid(), stampaCassaFile.getBilancio().getUid(), cestStatoCode);
		
		if(lista == null || lista.isEmpty()){
			return null;
		}
		
		SiacTCassaEconStampaValore siacTCassaEconStampaValore = lista.get(0);
		if (siacTCassaEconStampaValore.getRenNum() == null) {
			return null;
		}
		StampaRendiconto res = new StampaRendiconto();
		res.setNumeroRendiconto(siacTCassaEconStampaValore.getRenNum());
		res.setPeriodoDataInizio(siacTCassaEconStampaValore.getRenPeriodoinizio());
		res.setPeriodoDataFine(siacTCassaEconStampaValore.getRenPeriodofine());
		
		return res;
		
		
	}
	
	/**
	 * Ricerca l'ultimo numero di rendiconto stampato per la richiesta economale.
	 * @param richiestaEconomale
	 * @return
	 */
	public StampaRendiconto findStampaUltimoNumeroRendiconto(RichiestaEconomale richiestaEconomale) {
		// TODO: implementare in maniera corretta quando si avranno informazioni sul recupero dei dati di stampa del rendiconto
		return null;
//		Iterable<SiacDCassaEconStampaStatoEnum> cestStatos = Arrays.asList(SiacDCassaEconStampaStatoEnum.Definitiva);
//		Collection<String> cestStatoCodes = new HashSet<String>();
//		for(SiacDCassaEconStampaStatoEnum sdcesse : cestStatos) {
//			cestStatoCodes.add(sdcesse.getCodice());
//		}
//		
//		List<SiacTCassaEconStampaValore> lista = siacTCassaEconStampaRepository.findStampaUltimoNumeroRendiconto( stampaCassaFile.getCassaEconomale().getUid(), stampaCassaFile.getTipoDocumento().getCodice(), ente.getUid(), cestStatoCode);
//		
//		if(lista == null || lista.isEmpty()){
//			return null;
//		}
//		
//		SiacTCassaEconStampaValore siacTCassaEconStampaValore = lista.get(0);
//		if (siacTCassaEconStampaValore.getRenNum() == null) {
//			return null;
//		}
//		StampaRendiconto res = new StampaRendiconto();
//		res.setNumeroRendiconto(siacTCassaEconStampaValore.getRenNum());
//		res.setPeriodoDataInizio(siacTCassaEconStampaValore.getRenPeriodoinizio());
//		res.setPeriodoDataFine(siacTCassaEconStampaValore.getRenPeriodofine());
//		
//		return res;
//		// TODO Auto-generated method stub
//		return null;
	}
	
	/**
	 * Stampa corrispondente all'ultima stampa giornale
	 *
	 * @param stampaCassaFile la stampa cassa
	 * 
	 * @return la lista delle stampe trovate
	 */
	public StampaGiornale findUltimaStampaDefinitivaGiornaleCassa(StampeCassaFile stampaCassaFile) {

		List<SiacTCassaEconStampaValore> lista = siacTCassaEconStampaRepository.findUltimaStampaDefinitivaGiornaleCassa( stampaCassaFile.getCassaEconomale().getUid(), stampaCassaFile.getTipoDocumento().getCodice(), stampaCassaFile.getTipoStampa().getCodice(), ente.getUid(), stampaCassaFile.getBilancio().getUid());
		
		if(lista.isEmpty()){
			return null;
		}
		
		SiacTCassaEconStampaValore siacTCassaEconStampaValore = lista.get(0);
		if (siacTCassaEconStampaValore.getGioUltimadatastampadef() == null) {
			return null;
		}
		StampaGiornale res = new StampaGiornale();
		res.setDataUltimaStampa(siacTCassaEconStampaValore.getGioUltimadatastampadef());
		res.setUltimaPaginaStampataDefinitiva(siacTCassaEconStampaValore.getGioUltimapaginastampadef());
		res.setUltimoImportoEntrataCC(siacTCassaEconStampaValore.getGioUltimoimportoentrataCc());
		res.setUltimoImportoEntrataContanti(siacTCassaEconStampaValore.getGioUltimoimportoentrataContanti());
		res.setUltimoImportoUscitaCC(siacTCassaEconStampaValore.getGioUltimoimportouscitaCc());
		res.setUltimoImportoUscitaContanti(siacTCassaEconStampaValore.getGioUltimoimportouscitaContanti());
		
		return res;
		
		
	}
	
	/**
	 * Stampa corrispondente all'ultima stampa giornale
	 *
	 * @param stampaCassaFile la stampa cassa
	 * 
	 * @return la lista delle stampe trovate
	 */
	public StampeCassaFile findUltimaStampaDefinitivaPerBozzaGiornaleCassa(StampeCassaFile stampaCassaFile) {

		List<SiacTCassaEconStampaValore> lista = siacTCassaEconStampaRepository.findUltimaStampaDefinitivaPerBozzaGiornaleCassa( stampaCassaFile.getCassaEconomale().getUid(), stampaCassaFile.getTipoDocumento().getCodice(), stampaCassaFile.getTipoStampa().getCodice(), stampaCassaFile.getStampaGiornale().getDataUltimaStampa(), stampaCassaFile.getEnte().getUid(), stampaCassaFile.getBilancio().getUid());
		
		if(lista.isEmpty()){
			return null;
		}
		
		SiacTCassaEconStampaValore siacTCassaEconStampaValore = lista.get(0);
		if (siacTCassaEconStampaValore.getGioUltimadatastampadef() == null) {
			return null;
		}
		
		StampaGiornale res1 = new StampaGiornale();
		res1.setDataUltimaStampa(siacTCassaEconStampaValore.getGioUltimadatastampadef());
		res1.setUltimaPaginaStampataDefinitiva(siacTCassaEconStampaValore.getGioUltimapaginastampadef());
		res1.setUltimoImportoEntrataCC(siacTCassaEconStampaValore.getGioUltimoimportoentrataCc());
		res1.setUltimoImportoEntrataContanti(siacTCassaEconStampaValore.getGioUltimoimportoentrataContanti());
		res1.setUltimoImportoUscitaCC(siacTCassaEconStampaValore.getGioUltimoimportouscitaCc());
		res1.setUltimoImportoUscitaContanti(siacTCassaEconStampaValore.getGioUltimoimportouscitaContanti());
		stampaCassaFile.setStampaGiornale(res1);
		return stampaCassaFile;
		
		
	}
	
	/**
	 * Inserisci stampe Cassa.
	 *
	 * @param StampeCassaFile the StampeCassaFile
	 */
	public StampeCassaFile inserisciStampa(StampeCassaFile stampeCassaFile) {		
		SiacTCassaEconStampa siacTCassaEconStampa = buildSiacTCassaEconStampa(stampeCassaFile);	
		siacTCassaEconStampaDao.create(siacTCassaEconStampa);
		stampeCassaFile.setUid(siacTCassaEconStampa.getUid());		
		return stampeCassaFile;
	}
	

	/**
	 * Builds the siac t iva gruppo.
	 *
	 * @param gruppoAttivitaIva the gruppo attivita iva
	 * @return the siac t iva gruppo
	 */
	private SiacTCassaEconStampa buildSiacTCassaEconStampa(StampeCassaFile stampeCassaFile) {
		SiacTCassaEconStampa siacTCassaEconStampa = new SiacTCassaEconStampa();
		siacTCassaEconStampa.setLoginOperazione(loginOperazione);
		stampeCassaFile.setLoginOperazione(loginOperazione);
		map(stampeCassaFile, siacTCassaEconStampa, CecMapId.SiacTCassaEconStampa_StampeCassaFile_Base);		
		return siacTCassaEconStampa;
	}
	
	public ListaPaginata<StampeCassaFile> cercaStampeCassaEconomale(StampeCassaFile stampa, String nomeFile, Date dataStampaGiornaleDa, Date dataStampaGiornaleA,ParametriPaginazione parametriPaginazione) {
		
		SiacDCassaEconStampaTipoEnum siacDCassaEconStampaTipoEnum = SiacDCassaEconStampaTipoEnum.byTipoStampaIva(stampa.getTipoDocumento());
		Page<SiacTCassaEconStampa> siacTCassaEconStampas = siacTCassaEconStampaDao.ricercaStampeCassaEconomale(
				ente.getUid(),
				stampa.getBilancio().getUid(),
				stampa.getCassaEconomale().getUid(),
				siacDCassaEconStampaTipoEnum,
				stampa.getDataCreazione(),
				nomeFile,
				stampa.getTipoStampa() != null ? SiacDCassaEconStampaStatoEnum.byTipoStampa(stampa.getTipoStampa()): null,
				stampa.getStampaGiornale() != null ? stampa.getStampaGiornale().getDataUltimaStampa() : null,
				dataStampaGiornaleDa != null ? dataStampaGiornaleDa : null,
				dataStampaGiornaleA != null ? dataStampaGiornaleA : null,
				stampa.getStampaRendiconto() != null ? stampa.getStampaRendiconto().getPeriodoDataInizio() : null,
				stampa.getStampaRendiconto() != null ? stampa.getStampaRendiconto().getPeriodoDataFine() : null,
				stampa.getStampaRendiconto() != null ? stampa.getStampaRendiconto().getDataRendiconto() : null,
				stampa.getStampaRendiconto() != null ? stampa.getStampaRendiconto().getNumeroRendiconto() : null,
				stampa.getNumeroMovimento(),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacTCassaEconStampas, StampeCassaFile.class, CecMapId.SiacTCassaEconStampa_StampeCassaFile_Base);
		
	}



}
