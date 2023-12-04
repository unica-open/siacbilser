/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.fcde;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.CustomConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.ExtendedBaseDadImpl;
import it.csi.siac.siacbilser.integration.dao.SiacTAccFondiDubbiaEsigBilRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTAccFondiDubbiaEsigRepository;
import it.csi.siac.siacbilser.integration.dao.fcde.AccantonamentoFondiDubbiaEsigibilitaDao;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaDad.
 *
 * @author Marchino Alessandro
 */
public abstract class AccantonamentoFondiDubbiaEsigibilitaBaseDad<C extends Capitolo<?, ?>, A extends AccantonamentoFondiDubbiaEsigibilitaBase<C>> extends ExtendedBaseDadImpl {
	
	@Autowired protected AccantonamentoFondiDubbiaEsigibilitaDao accantonamentoFondiDubbiaEsigibilitaDao;
	@Autowired protected SiacTAccFondiDubbiaEsigRepository siacTAccFondiDubbiaEsigRepository;
	@Autowired protected SiacTAccFondiDubbiaEsigBilRepository siacTAccFondiDubbiaEsigBilRepository;
	@Autowired private SimpleJDBCFunctionInvoker fi;

	/**
	 * Inserimento dell'accantonamento
	 * @param accantonamentoFondiDubbiaEsigibilita il fondo da inserire
	 * @return l'accantonamento inserito
	 */
	public A create(A accantonamentoFondiDubbiaEsigibilita) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsigibilita = buildSiacTAccFondiDubbiaEsig(accantonamentoFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilitaDao.create(siacTAccFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilita.setUid(siacTAccFondiDubbiaEsigibilita.getUid());
		
		return accantonamentoFondiDubbiaEsigibilita;
	}
	
	/**
	 * Update dell'accantonamento
	 * @param accantonamentoFondiDubbiaEsigibilita il fondo da aggiornare
	 * @return l'accantonamento aggiornato
	 */
	public A update(A accantonamentoFondiDubbiaEsigibilita) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsigibilita = buildSiacTAccFondiDubbiaEsig(accantonamentoFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilitaDao.update(siacTAccFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilita.setUid(siacTAccFondiDubbiaEsigibilita.getUid());
		return accantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * Lettura dell'accantonamento
	 * @param accantonamentoFondiDubbiaEsigibilita il fondo da leggere
	 * @return l'accantonamento
	 */
	@SuppressWarnings("unchecked")
	public A findById(A accantonamentoFondiDubbiaEsigibilita) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig = accantonamentoFondiDubbiaEsigibilitaDao.findById(accantonamentoFondiDubbiaEsigibilita.getUid());
		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byCodice(siacTAccFondiDubbiaEsig.getSiacDAccFondiDubbiaEsigTipo().getAfdeTipoCode());
		return (A) mapNotNull(
				siacTAccFondiDubbiaEsig,
				siacDAccFondiDubbiaEsigTipoEnum.getModelClass(),
				siacDAccFondiDubbiaEsigTipoEnum.getMapIdModelDetail(),
				Converters.byModelDetails(Utility.MDTL.byModelDetailClass(siacDAccFondiDubbiaEsigTipoEnum.getModelDetailClass())));
	}
	
	/**
	 * Elimina l'accantonamento.
	 *
	 * @param accantonamentoFondiDubbiaEsigibilita l'accantonamento da eliminare
	 */
	public void elimina(A accantonamentoFondiDubbiaEsigibilita) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig = accantonamentoFondiDubbiaEsigibilitaDao.findById(accantonamentoFondiDubbiaEsigibilita.getUid());
		accantonamentoFondiDubbiaEsigibilitaDao.logicalDelete(siacTAccFondiDubbiaEsig);
	}
	
	/**
	 * Elimina gli accantonamenti.
	 *
	 * @param accantonamentoFondiDubbiaEsigibilitaAttributiBilancio gli attributi contenenti gli accantonamenti da eliminare
	 */
	public void eliminaAll(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio) {
		List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = accantonamentoFondiDubbiaEsigibilitaDao.ricerca(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid(), false);
		for(SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig : siacTAccFondiDubbiaEsigs) {
			accantonamentoFondiDubbiaEsigibilitaDao.logicalDelete(siacTAccFondiDubbiaEsig);
		}
	}
	
	/**
	 * Ricerca dell'accantonamento.
	 * @param accantonamentoFondiDubbiaEsigibilitaAttributiBilancio gli attributi bilancio
	 * @param modelDetails i model detail da utilizzare
	 *
	 * @return la lista degli accantonamenti
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> ricercaAccantonamentoFondiDubbiaEsigibilita(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio) {
		SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil = siacTAccFondiDubbiaEsigBilRepository.findOne(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid());
		
		List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = accantonamentoFondiDubbiaEsigibilitaDao.ricerca(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid(), true);

		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byCodice(siacTAccFondiDubbiaEsigBil.getSiacDAccFondiDubbiaEsigTipo().getAfdeTipoCode());

		// Variabile d'appoggio per gestione generic
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> res = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaBase<?>>();
		res.addAll(convertiLista(siacTAccFondiDubbiaEsigs, siacDAccFondiDubbiaEsigTipoEnum.getModelClass(), siacDAccFondiDubbiaEsigTipoEnum.getMapIdModelDetail(), 
				Utility.MDTL.byModelDetailClass(siacDAccFondiDubbiaEsigTipoEnum.getModelDetailClass())));
		return res;
	}
	
	public Bilancio findBilancio(AccantonamentoFondiDubbiaEsigibilitaBase<?> afde) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig = siacTAccFondiDubbiaEsigRepository.findOne(afde.getUid());
		if(siacTAccFondiDubbiaEsig == null || siacTAccFondiDubbiaEsig.getSiacTBilElem() == null) {
			return null;
		}
		return mapNotNull(siacTAccFondiDubbiaEsig.getSiacTBilElem().getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio);
	}
	
	/**
	 * Crea l'istanza dell'entity a partire dall'istanza del model
	 *
	 * @param afde il model
	 * @return la entity
	 */
	protected abstract SiacTAccFondiDubbiaEsig buildSiacTAccFondiDubbiaEsig(A afde);
	
	/**
	 * Conta gli accantonamenti a partire dal capitolo
	 * @param cep il capitolo di entrata previsione
	 * @param modelDetails i dettagli del modello da ricavare
	 * @return il numero di accantonamenti correlati al capitolo
	 */
	@SuppressWarnings("unchecked")
	public <AFDE extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> AFDE findByCapitolo(Capitolo<?, ?> cep, TipoAccantonamentoFondiDubbiaEsigibilita tipo, ModelDetailEnum... modelDetails) {
		final String methodName = "findByCapitolo";
		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipo);
		
		Page<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = siacTAccFondiDubbiaEsigRepository.findByElemIdAndAfdeTipoCode(
				cep.getUid(),
				siacDAccFondiDubbiaEsigTipoEnum.getCodice(),
				new PageRequest(0, 1, new Sort(Sort.Direction.ASC, "siacTAccFondiDubbiaEsigBil.siacDAccFondiDubbiaEsigStato.afdeStatoPriorita")));
		
		if(siacTAccFondiDubbiaEsigs == null || !siacTAccFondiDubbiaEsigs.hasContent()) {
			log.debug(methodName, "Nessuna entity di tipo " + SiacTAccFondiDubbiaEsig.class.getSimpleName() + " per capitolo " + cep.getUid());
			return null;
		}
		// Prendo solo il primo
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig = siacTAccFondiDubbiaEsigs.getContent().get(0);
		return (AFDE) mapNotNull(siacTAccFondiDubbiaEsig, siacDAccFondiDubbiaEsigTipoEnum.getModelClass(), siacDAccFondiDubbiaEsigTipoEnum.getMapIdModelDetail(), Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Conta gli accantonamenti a partire dal capitolo
	 * @param cep il capitolo di entrata previsione
	 * @param modelDetails i dettagli del modello da ricavare
	 * @return il numero di accantonamenti correlati al capitolo
	 */
	@SuppressWarnings("unchecked")
	public <AFDE extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> AFDE findEquivalente(A afde, TipoAccantonamentoFondiDubbiaEsigibilita tipo) {
		final String methodName = "findEquivalente";
		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipo);
		Page<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = siacTAccFondiDubbiaEsigRepository.findEquivalenteByAccFdeIdAndAfdeTipoCode(
				afde.getUid(),
				siacDAccFondiDubbiaEsigTipoEnum.getCodice(),
				new PageRequest(0, 1, new Sort(Sort.Direction.ASC, "siacTAccFondiDubbiaEsigBil.siacDAccFondiDubbiaEsigStato.afdeStatoPriorita")));
		
		if(siacTAccFondiDubbiaEsigs == null || !siacTAccFondiDubbiaEsigs.hasContent()) {
			log.debug(methodName, "Nessuna SiacTAccFondiDubbiaEsig di tipo " + tipo + " per equivalente al SiacTAccFondiDubbiaEsig con id " + afde.getUid());
			return null;
		}
		// Prendo solo il primo
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig = siacTAccFondiDubbiaEsigs.getContent().get(0);
		
		return (AFDE) mapNotNull(
				siacTAccFondiDubbiaEsig,
				siacDAccFondiDubbiaEsigTipoEnum.getModelClass(),
				siacDAccFondiDubbiaEsigTipoEnum.getMapIdModelDetail(),
				Converters.byModelDetails(Utility.MDTL.byModelDetailClass(siacDAccFondiDubbiaEsigTipoEnum.getModelDetailClass())));
	}
	/**
	 * Conta gli accantonamenti a partire dal capitolo
	 * @param cep il capitolo di entrata previsione
	 * @param modelDetails i dettagli del modello da ricavare
	 * @return il numero di accantonamenti correlati al capitolo
	 */
	@SuppressWarnings("unchecked")
	public <AFDE extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> AFDE findEquivalenteByCapitolo(Capitolo<?, ?> cap, TipoAccantonamentoFondiDubbiaEsigibilita tipo) {
		final String methodName = "findEquivalenteByCapitolo";
		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipo);
		Page<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = siacTAccFondiDubbiaEsigRepository.findEquivalenteByElemIdAndAfdeTipoCode(
				cap.getUid(),
				siacDAccFondiDubbiaEsigTipoEnum.getCodice(),
				new PageRequest(0, 1, new Sort(Sort.Direction.ASC, "siacTAccFondiDubbiaEsigBil.siacDAccFondiDubbiaEsigStato.afdeStatoPriorita")));
		
		if(siacTAccFondiDubbiaEsigs == null || !siacTAccFondiDubbiaEsigs.hasContent()) {
			log.debug(methodName, "Nessuna SiacTAccFondiDubbiaEsig di tipo " + tipo + " per equivalente al Capitolo con id " + cap.getUid());
			return null;
		}
		// Prendo solo il primo
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig = siacTAccFondiDubbiaEsigs.getContent().get(0);
		
		return (AFDE) mapNotNull(
				siacTAccFondiDubbiaEsig,
				siacDAccFondiDubbiaEsigTipoEnum.getModelClass(),
				siacDAccFondiDubbiaEsigTipoEnum.getMapIdModelDetail(),
				Converters.byModelDetails(Utility.MDTL.byModelDetailClass(siacDAccFondiDubbiaEsigTipoEnum.getModelDetailClass())));
	}
	
	/**
	 * Conta gli accantonamenti a partire dal capitolo
	 * @param cap il capitolo di entrata previsione
	 * @return il numero di accantonamenti correlati al capitolo
	 */
	public Long countByCapitolo(Capitolo<?, ?> cap, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio) {
		return siacTAccFondiDubbiaEsigRepository.countByElemIdAndAfdeBilId(Integer.valueOf(cap.getUid()), accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid());
	}
	
	/**
	 * Ottiene i dettagli dell'accantonamento relativi alla stampa
	 * @param uidAttributiBilancio
	 */
	protected List<Object[]> findDettagliAccantonamentoByUidAttributiBilancio(int uidAttributiBilancio, String functionName) {
		final String methodName = "findDettagliAccantonamentoByUidAttributiBilancio";
		List<Object[]> result = fi.invokeFunctionToObjectArray(functionName, Integer.valueOf(uidAttributiBilancio));
		log.info(methodName, "Ottenuti dettagli accantonamento via function. " + (result == null ? "NULL" : "Numero righe: " + result.size()));
		return result;
	}
	
	public List<Object[]> trovaMediaConfronto(Capitolo<?, ?> cap) {
		return trovaMediaConfronto(cap, "fnc_siac_calcolo_media_di_confronto_acc_gestione", cap.getEnte() == null ? ente : cap.getEnte(), cap.getAnnoCapitolo());
	}
	
	private List<Object[]> trovaMediaConfronto(Capitolo<?, ?> cap, String functionName, Ente ente, Integer annoBilancio) {
		final String methodName = "trovaMediaConfronto";
		List<Object[]> result = fi.invokeFunctionToObjectArray(functionName, cap.getUid(), ente.getUid(), annoBilancio);
		log.info(methodName, "Risultato ottenuto via function [" + functionName + "] - " + (CollectionUtils.isEmpty(result) ? 
				"NULL" : "media confronto: " + result.get(0) + ", tipo: " + result.get(1)));
		return result;
	}
	
	//SIAC-8395	
	protected List<Object[]> calcolaCampiReportAllegatoC(int enteProprietarioId, String anno, Integer afdeBilId, String functionName){
		final String methodName = "calcolaCampiReportAllegatoC";
		List<Object[]> result = fi.invokeFunctionToObjectArray(functionName, enteProprietarioId, anno, afdeBilId);
		log.info(methodName, "Ottenuti dettagli accantonamento via function. " + (result == null ? "NULL" : "Numero righe: " + result.size()));
		return result;
	}
	
	/**
	 * SIAC-8540
	 * 
	 * Metoto comune per l'adeguamento di un accantonamento attraverso dei converter
	 * 
	 * @return l'accantonamento adeguato
	 */
	protected <AFDE extends AccantonamentoFondiDubbiaEsigibilitaBase<?>, CAP extends Capitolo<?,?>> AFDE adeguaAccantonamentoWithConverters(AFDE afde, CAP cap, 
			Class<? extends CustomConverter>... converterClasses) {
		SiacTAccFondiDubbiaEsig stafde = initDefaultWrapperSiacTAccFondiDubbiaEsig(cap);
		return applyConverters(stafde, afde, converterClasses);
	}
	
	private SiacTAccFondiDubbiaEsig initDefaultWrapperSiacTAccFondiDubbiaEsig(Capitolo<?, ?> cap) {
		SiacTAccFondiDubbiaEsig stafde = new SiacTAccFondiDubbiaEsig();
		SiacTBilElem stbe = new SiacTBilElem();
		stbe.setUid(cap.getUid());
		stafde.setSiacTBilElem(stbe);
		return stafde;
	}
}
