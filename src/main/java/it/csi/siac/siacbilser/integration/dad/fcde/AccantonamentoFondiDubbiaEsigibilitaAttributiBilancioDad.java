/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.fcde;


import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.ExtendedBaseDadImpl;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTAccFondiDubbiaEsigBilNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTAccFondiDubbiaEsigBilRepository;
import it.csi.siac.siacbilser.integration.dao.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao;
import it.csi.siac.siacbilser.integration.entity.SiacDAccFondiDubbiaEsigTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBilNum;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaDad.
 *
 * @author 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad extends ExtendedBaseDadImpl {
	
	@Autowired private EnumEntityFactory enumEntityFactory;
	@Autowired private SiacTAccFondiDubbiaEsigBilRepository siacTAccFondiDubbiaEsigBilRepository;
	@Autowired private SiacTAccFondiDubbiaEsigBilNumRepository siacTAccFondiDubbiaEsigBilNumRepository;
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao;
	
	/**
	 * Inserisce un'accantonamento di default
	 * @param bilancio il bilancio per cui cercare
	 * @param statoAccantonamentoFondiDubbiaEsigibilita il tipo di accantonamento
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita il tipo di accantonamento
	 * @return l'accantonamento di default
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio initDefaultVersion(
			Bilancio bilancio,
			StatoAccantonamentoFondiDubbiaEsigibilita statoAccantonamentoFondiDubbiaEsigibilita,
			TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita,
			ModelDetailEnum... modelDetails) {
		
		// Dobbiamo aggiornare la tabella per lo stacca versione
		Integer versione = staccaVersione(bilancio, tipoAccantonamentoFondiDubbiaEsigibilita);
		
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio = new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		// SIAC-8681
		inizializzaDefaultVersionByType(versione, attributiBilancio, bilancio, statoAccantonamentoFondiDubbiaEsigibilita, tipoAccantonamentoFondiDubbiaEsigibilita);
		
		SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBils = buildSiacTAccFondiDubbiaEsigBil(attributiBilancio);
		
		siacTAccFondiDubbiaEsigBils = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao.create(siacTAccFondiDubbiaEsigBils);
		
		return map(siacTAccFondiDubbiaEsigBils,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class,
				BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail,
				Converters.byModelDetails(modelDetails));
	}
	
	// SIAC-8681
	private void inizializzaDefaultVersionByType(Integer versione, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio, 
			Bilancio bilancio, StatoAccantonamentoFondiDubbiaEsigibilita statoAccantonamentoFondiDubbiaEsigibilita, 
			TipoAccantonamentoFondiDubbiaEsigibilita tipo) {
		switch (tipo) {
			case PREVISIONE:
				initDefaultPrevisione(versione, attributiBilancio, bilancio, statoAccantonamentoFondiDubbiaEsigibilita);
				break;
			case GESTIONE:
				initDefaultGestione(versione, attributiBilancio, bilancio, statoAccantonamentoFondiDubbiaEsigibilita);
				break;
			case RENDICONTO:
				initDefaultRendiconto(versione, attributiBilancio, bilancio, statoAccantonamentoFondiDubbiaEsigibilita);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Ottiene l'accantonamento, se presente
	 * @param bilancio il bilancio per cui cercare
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita il tipo di accantonamento
	 * @param versione la versione
	 * @return l'accantonamento, se presente
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio getByBilancioAndTipoAndVersione(
			Bilancio bilancio,
			TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita,
			Integer versione,
			ModelDetailEnum... modelDetails) {
		
		Page<SiacTAccFondiDubbiaEsigBil> siacTAccFondiDubbiaEsigBils = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao.ricerca(
				bilancio.getUid(),
				SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipoAccantonamentoFondiDubbiaEsigibilita).getCodice(),
				null,
				versione,
				null,
				new PageRequest(0, 1, getSortVersione()));
		if(!siacTAccFondiDubbiaEsigBils.hasContent()) {
			return null;
		}
		return map(
				siacTAccFondiDubbiaEsigBils.getContent().get(0),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class,
				BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail,
				Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @param modelDetails the model details
	 * @return the accantonamento fondi dubbia esigibilita attributi bilancio
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio findById(Integer id, ModelDetailEnum... modelDetails) {
		SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil = siacTAccFondiDubbiaEsigBilRepository.findOne(id);
		return mapNotNull(
				siacTAccFondiDubbiaEsigBil,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class,
				BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail,
				Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Inserimento dell'accantonamento
	 * @param accantonamentoFondiDubbiaEsigibilitaAttributiBilancio gli attributi da inserire
	 * @return l'accantonamento inserito
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio create(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio) {
		SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigibilitaBil = buildSiacTAccFondiDubbiaEsigBil(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		siacTAccFondiDubbiaEsigibilitaBil = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao.create(siacTAccFondiDubbiaEsigibilitaBil);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.setUid(siacTAccFondiDubbiaEsigibilitaBil.getUid());
		return accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	}
	
	/**
	 * Update dell'accantonamento
	 * @param accantonamentoFondiDubbiaEsigibilitaAttributiBilancio il fondo da aggiornare
	 * @return l'accantonamento aggiornato
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio update(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio) {
		SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigibilitaBil = buildSiacTAccFondiDubbiaEsigBil(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		siacTAccFondiDubbiaEsigibilitaBil = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao.update(siacTAccFondiDubbiaEsigibilitaBil);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.setUid(siacTAccFondiDubbiaEsigibilitaBil.getUid());
		return accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	}
	
	protected SiacTAccFondiDubbiaEsigBil buildSiacTAccFondiDubbiaEsigBil(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio afdeab) {
		final String methodName = "buildSiacTAccFondiDubbiaEsigBil";
		afdeab.setLoginOperazione(loginOperazione);
		
		log.debug(methodName, "Creazione della entity a partire dal model");
		SiacTAccFondiDubbiaEsigBil tafdeb = new SiacTAccFondiDubbiaEsigBil();
		tafdeb.setSiacTEnteProprietario(siacTEnteProprietario);

		map(afdeab, tafdeb, BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		return tafdeb;
	}

	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio findPrevious(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita tipo, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail... modelDetails) {
		Page<SiacTAccFondiDubbiaEsigBil> previous = siacTAccFondiDubbiaEsigBilRepository.findPreviousByAfdeTipoCode(
				accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid(),
				SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipo).getCodice(),
				new PageRequest(0, 1, getSortVersioneRepository()));
		if(previous == null || !previous.hasContent()) {
			return null;
		}
		return map(previous.getContent().get(0), AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	//SIAC-8851
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio findPreviousGestione(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita tipo, StatoAccantonamentoFondiDubbiaEsigibilita stato, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail... modelDetails) {
		Page<SiacTAccFondiDubbiaEsigBil> previous = siacTAccFondiDubbiaEsigBilRepository.findPreviousByAfdeTipoCodeGestione(
				accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid(),
				SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipo).getCodice(),
				SiacDAccFondiDubbiaEsigStatoEnum.byStatoAccantonamentoFondiDubbiaEsigibilita(stato).getCodice(),
				new PageRequest(0, 1, getSortVersioneRepository()));
		if(previous == null || !previous.hasContent()) {
			return null;
		}
		return map(previous.getContent().get(0), AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio findPreviousOrCurrent(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita tipo, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail... modelDetails) {
		Page<SiacTAccFondiDubbiaEsigBil> previous = siacTAccFondiDubbiaEsigBilRepository.findPreviousOrCurrentByAfdeTipoCode(
				accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid(),
				SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipo).getCodice(),
				new PageRequest(0, 1, getSortVersioneRepository()));
		if(previous == null || !previous.hasContent()) {
			return null;
		}
		return map(previous.getContent().get(0), AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	/**
	 * Ricerca degli attributi
	 * @param bilancio
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita
	 * @param excludeCurrent
	 * @param versione
	 * @param accantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails
	 * @return
	 */
	public ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> ricercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
			AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamento,
			Boolean excludeCurrent,
			ParametriPaginazione parametriPaginazione,
			AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail... modelDetails) {
		
		Page<SiacTAccFondiDubbiaEsigBil> page = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao.ricerca(
				mapToUidIfNotZero(accantonamento.getBilancio()),
				accantonamento.getTipoAccantonamentoFondiDubbiaEsigibilita() != null ? SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(accantonamento.getTipoAccantonamentoFondiDubbiaEsigibilita()).getCodice() : null,
				accantonamento.getStatoAccantonamentoFondiDubbiaEsigibilita() != null ? SiacDAccFondiDubbiaEsigStatoEnum.byStatoAccantonamentoFondiDubbiaEsigibilita(accantonamento.getStatoAccantonamentoFondiDubbiaEsigibilita()).getCodice() : null,
				!Boolean.TRUE.equals(excludeCurrent) ? accantonamento.getVersione() : null,
				Boolean.TRUE.equals(excludeCurrent) ? accantonamento.getVersione() : null,
				toPageable(parametriPaginazione, getSortVersione()));
		
		return toListaPaginata(page,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class,
				BilMapId.SiacTAccFondiDubbiaEsigBil_AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio_ModelDetail,
				modelDetails);
	}
	
	/**
	 * Ottiene il numero di una nuova quota o subdocumento.
	 *
	 * @param uidDocumento the uid documento
	 * @return numero subdocumento
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaVersione(Bilancio bilancio, TipoAccantonamentoFondiDubbiaEsigibilita tipo) {
		final String methodName = "staccaVersione";
		log.debug(methodName, loginOperazione);
		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(tipo);
		SiacTAccFondiDubbiaEsigBilNum siacTAccFondiDubbiaEsigBilNum = siacTAccFondiDubbiaEsigBilNumRepository.findByBilIdAndAfdeTipoCode(bilancio.getUid(),
				siacDAccFondiDubbiaEsigTipoEnum.getCodice());
		
		Date now = new Date();
		if(siacTAccFondiDubbiaEsigBilNum == null) {
			siacTAccFondiDubbiaEsigBilNum = new SiacTAccFondiDubbiaEsigBilNum();

			SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo = enumEntityFactory.getEntity(siacDAccFondiDubbiaEsigTipoEnum, ente.getUid(), SiacDAccFondiDubbiaEsigTipo.class);
			siacTAccFondiDubbiaEsigBilNum.setSiacDAccFondiDubbiaEsigTipo(siacDAccFondiDubbiaEsigTipo);

			SiacTBil siacTBil = new SiacTBil();
			siacTBil.setUid(bilancio.getUid());
			siacTAccFondiDubbiaEsigBilNum.setSiacTBil(siacTBil);
			
			siacTAccFondiDubbiaEsigBilNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTAccFondiDubbiaEsigBilNum.setDataCreazione(now);
			siacTAccFondiDubbiaEsigBilNum.setDataInizioValidita(now);;
			siacTAccFondiDubbiaEsigBilNum.setDataModifica(now);
			// La numerazione parte da 1
			siacTAccFondiDubbiaEsigBilNum.setAfdeBilVersione(1);
		}
		siacTAccFondiDubbiaEsigBilNum.setLoginOperazione(loginOperazione);
		siacTAccFondiDubbiaEsigBilNum.setDataModifica(now);
		
		siacTAccFondiDubbiaEsigBilNumRepository.saveAndFlush(siacTAccFondiDubbiaEsigBilNum);
		Integer versione = siacTAccFondiDubbiaEsigBilNum.getAfdeBilVersione();
		log.info(methodName, "returning versione: "+ versione);
		return versione;
	}
	
	private Sort getSortVersioneRepository() {
		return new Sort(Sort.Direction.DESC, "siacTBil.siacTPeriodo.anno").and(new Sort(Sort.Direction.ASC, "siacDAccFondiDubbiaEsigStato.afdeStatoPriorita")).and(new Sort(Sort.Direction.DESC, "afdeBilVersione"));
	}
	private Sort getSortVersione() {
		return new Sort(Sort.Direction.ASC, "tafdeb.siacDAccFondiDubbiaEsigStato.afdeStatoPriorita").and(new Sort(Sort.Direction.DESC, "tafdeb.afdeBilVersione"));
	}
	
	// SIAC-8681
	private void initDefaultPrevisione(Integer versione, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio, Bilancio bilancio, StatoAccantonamentoFondiDubbiaEsigibilita statoAccantonamentoFondiDubbiaEsigibilita) {
		attributiBilancio.setUid(0);
		attributiBilancio.setBilancio(bilancio);
		attributiBilancio.setQuinquennioRiferimento(bilancio.getAnno() - 1);
		attributiBilancio.setAccantonamentoGraduale(new BigDecimal(100));
		attributiBilancio.setRiscossioneVirtuosa(Boolean.FALSE);
		attributiBilancio.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
		attributiBilancio.setStatoAccantonamentoFondiDubbiaEsigibilita(statoAccantonamentoFondiDubbiaEsigibilita);
		attributiBilancio.setRiscossioneVirtuosa(Boolean.FALSE);
		attributiBilancio.setVersione(versione);
		attributiBilancio.setDataInizioValidita(new Date());
		attributiBilancio.setDataCreazione(new Date());
		attributiBilancio.setDataModifica(new Date());
	}
	
	// SIAC-8681
	private void initDefaultGestione(Integer versione, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio, Bilancio bilancio, StatoAccantonamentoFondiDubbiaEsigibilita statoAccantonamentoFondiDubbiaEsigibilita) {
		attributiBilancio.setUid(0);
		attributiBilancio.setBilancio(bilancio);
		//SIAC-8754
		attributiBilancio.setQuinquennioRiferimento(bilancio.getAnno());
		attributiBilancio.setAccantonamentoGraduale(new BigDecimal(100));
		attributiBilancio.setRiscossioneVirtuosa(Boolean.FALSE);
		attributiBilancio.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);
		attributiBilancio.setStatoAccantonamentoFondiDubbiaEsigibilita(statoAccantonamentoFondiDubbiaEsigibilita);
		attributiBilancio.setRiscossioneVirtuosa(Boolean.FALSE);
		attributiBilancio.setVersione(versione);
		attributiBilancio.setDataInizioValidita(new Date());
		attributiBilancio.setDataCreazione(new Date());
		attributiBilancio.setDataModifica(new Date());
	}
	
	// SIAC-8681
	private void initDefaultRendiconto(Integer versione, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio, Bilancio bilancio, StatoAccantonamentoFondiDubbiaEsigibilita statoAccantonamentoFondiDubbiaEsigibilita) {
		attributiBilancio.setUid(0);
		attributiBilancio.setBilancio(bilancio);
		attributiBilancio.setQuinquennioRiferimento(bilancio.getAnno());
		attributiBilancio.setAccantonamentoGraduale(new BigDecimal(100));
		attributiBilancio.setRiscossioneVirtuosa(Boolean.FALSE);
		attributiBilancio.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		attributiBilancio.setStatoAccantonamentoFondiDubbiaEsigibilita(statoAccantonamentoFondiDubbiaEsigibilita);
		attributiBilancio.setRiscossioneVirtuosa(Boolean.FALSE);
		attributiBilancio.setVersione(versione);
		attributiBilancio.setDataInizioValidita(new Date());
		attributiBilancio.setDataCreazione(new Date());
		attributiBilancio.setDataModifica(new Date());
	}
}
