/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDOnereRepository;
import it.csi.siac.siacbilser.integration.dao.TipoOnereDao;
import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacDSommaNonSoggettaTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoOnereModelDetail;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per il Tipo Onere.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TipoOnereDad extends ExtendedBaseDadImpl {
	
	/** The siac d onere repository. */
	@Autowired
	private SiacDOnereRepository siacDOnereRepository;
	
	@Autowired
	private TipoOnereDao tipoOnereDao;

	private Richiedente richiedente;

	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param ente the ente
	 * @param naturaOnere the natura onere
	 * @return the list
	 */
	public List<TipoOnere> ricercaTipiOnere(Ente ente, NaturaOnere naturaOnere) {
		
		Date inizioDataAttuale =  new Date();
		inizioDataAttuale = DateUtils.setHours(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setMinutes(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setSeconds(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setMilliseconds(inizioDataAttuale, 0);
		
		List<SiacDOnere> elencoTipiOnereDB = siacDOnereRepository.findTipiOnereValidiByNatureOnereEEnte(ente.getUid(), naturaOnere.getUid(), inizioDataAttuale);
		if(elencoTipiOnereDB == null) {
			return new ArrayList<TipoOnere>();
		}
		
		List<TipoOnere> elencoTipoOnereReturn = new ArrayList<TipoOnere>(elencoTipiOnereDB.size());
		
		for (SiacDOnere tipoOnere : elencoTipiOnereDB) {
			
			TipoOnere tipoOnereToAdd = map(tipoOnere, TipoOnere.class, BilMapId.SiacDOnere_TipoOnere); //mapNaturaOnere(tipoOnere);
			elencoTipoOnereReturn.add(tipoOnereToAdd);
		}
		return elencoTipoOnereReturn;
	}
	
	
	/**
	 * Effettua la ricerca dettaglio di un tipo onere
	 *
	 * @param tipoOnere the tipo onere
	 * @return the list
	 */
	public TipoOnere ricercaDettaglioTipoOnere(TipoOnere tipoOnere) {
		final String methodName = "ricercaDettaglioTipoOnere";
		SiacDOnere siacDOnere = siacDOnereRepository.findOne(tipoOnere.getUid());
		if(siacDOnere == null) {
			log.warn(methodName, "Impossibile trovare TipoOnere con id: " + tipoOnere.getUid());
		}
		return mapNotNull(siacDOnere,TipoOnere.class, BilMapId.SiacDOnere_TipoOnere);
	}
	
	/**
	 * Effettua la ricerca dettaglio di un tipo onere, permettendo di specificare il dettaglio di Modello da caricare.
	 *
	 * @param tipoOnere the tipo onere
	 * @return the list
	 */
	public TipoOnere ricercaDettaglioTipoOnere(TipoOnere tipoOnere, TipoOnereModelDetail... modelDetails) {
		final String methodName = "ricercaDettaglioTipoOnere";
		SiacDOnere siacDOnere = siacDOnereRepository.findOne(tipoOnere.getUid());
		if(siacDOnere == null) {
			log.warn(methodName, "Impossibile trovare TipoOnere con id: " + tipoOnere.getUid());
		}
		TipoOnere result = new TipoOnere();
		//Aggiunto allo scopo di avere il Richiedente per effettuare le ricerche impegno per chiave se necessario. 
		//(vedi converter TipoOnereCausaliImpegnoAccertamentoConverter);
		result.addObject(richiedente);
		return mapNotNull(siacDOnere, result, BilMapId.SiacDOnere_TipoOnere_Base, Converters.byModelDetails(modelDetails));
	}
	
	
	public  ListaPaginata<TipoOnere> ricercaSinteticaTipiOnere(TipoOnere tipoOnere, Boolean corsoDiValidita, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacDOnere> result = tipoOnereDao.ricercaSinteticaTipiOnere(
				tipoOnere.getNaturaOnere()!= null ? tipoOnere.getNaturaOnere().getUid() : null,
				tipoOnere.getEnte().getUid(),
				corsoDiValidita,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(result, TipoOnere.class, BilMapId.SiacDOnere_TipoOnere_Base);
	}
	
	public List<TipoOnere> identificaDettaglioOnerePerAliquotaFatturaFEL(BigDecimal aliquotaIva) {
		String codiceSplitReverse = "SP"; /*vedere cosa utilizzare per non schiantare il codice così*/ 
		List<SiacDOnere> siacDOneres = siacDOnereRepository.findSiacRDocOnereByAliquotaNaturaOnereETipoIva(aliquotaIva, codiceSplitReverse, TipoIvaSplitReverse.SPLIT_ISTITUZIONALE.getCodice(), 
																			ente.getUid(), SiacTAttrEnum.AliquotaCaricoSogg.getCodice());
		return convertiLista(siacDOneres, TipoOnere.class, BilMapId.SiacDOnere_TipoOnere);
	}
	
	
	public TipoOnere inserisceTipoOnere(TipoOnere tipoOnere){
		SiacDOnere siacDOnere = buildSiacDOnere(tipoOnere);
		tipoOnereDao.create(siacDOnere);
		tipoOnere.setUid(siacDOnere.getUid());
		return tipoOnere;
	}

	public TipoOnere aggiornaTipoOnere(TipoOnere tipoOnere) {
		SiacDOnere siacDOnere = buildSiacDOnere(tipoOnere);
		tipoOnereDao.update(siacDOnere);
		return tipoOnere;
	}
	
	private SiacDOnere buildSiacDOnere(TipoOnere tipoOnere) {
		SiacDOnere siacDOnere =  new SiacDOnere();
		siacDOnere.setLoginOperazione(loginOperazione);
		tipoOnere.setLoginOperazione(loginOperazione);
		log.debug("buidSiacDOnere", " siacDOnere.getLoginOperazione() - prima del map" + siacDOnere.getLoginOperazione());
		map(tipoOnere, siacDOnere, BilMapId.SiacDOnere_TipoOnere);
		log.debug("buidSiacDOnere", " siacDOnere.getLoginOperazione() - dopo il map" + siacDOnere.getLoginOperazione());
		return siacDOnere;
	}

	/**
	 * Dettaglio storico tipo onere.
	 *
	 * @param tipoOnere the tipo onere
	 * @return the list
	 */
	@Transactional(readOnly=true)
	public List<TipoOnere> dettaglioStoricoTipoOnere(TipoOnere tipoOnere) {
		String methodName="dettaglioStoricoTipoOnere";
		SiacDOnere siacDOnere = siacDOnereRepository.findOne(tipoOnere.getUid());
		
		List<Date> dateStorico = siacDOnereRepository.findDateStoricoTipoOnere(tipoOnere.getUid());
		
		log.debug(methodName, "dateStorico: " + dateStorico);
		List<TipoOnere> result = new ArrayList<TipoOnere>();
		
		TipoOnere succTipoOnere = null;
		for(Date dateToExtract : dateStorico) {
			siacDOnere.setDateToExtract(dateToExtract);
			TipoOnere c = map(siacDOnere, TipoOnere.class, BilMapId.SiacDOnere_TipoOnere);
			c.setDataInizioValidita(dateToExtract);
			c.setDataCreazione(dateToExtract);
			result.add(c);
			
			if(succTipoOnere != null) {
				c.setDataFineValidita(succTipoOnere.getDataInizioValidita());
			} else {
				c.setDataFineValidita(null);
			}
			succTipoOnere = c;
		
		}
		
		return result;
	}


	public TipoOnere findTipoOnereByCodice(String codice) {
		SiacDOnere siacDOnere = siacDOnereRepository.findTipoOnereByCodiceEEnte(codice, ente.getUid());
		return mapNotNull(siacDOnere, TipoOnere.class, BilMapId.SiacDOnere_TipoOnere);
	}


	public void setRichiedente(Richiedente richiedente) {
		this.richiedente = richiedente;
	}


	public List<CodiceSommaNonSoggetta> ricercaSommeNonSoggetteByTipoOnere(TipoOnere tipoOnere) {
		List<SiacDSommaNonSoggettaTipo> siacDSommaNonSoggettaTipos = siacDOnereRepository.findSommeNonSoggetteByTipoOnere(tipoOnere.getUid());
		return convertiLista(siacDSommaNonSoggettaTipos, CodiceSommaNonSoggetta.class, BilMapId.SiacDSommaNonSoggettaTipo_CodiceSommaNonSoggetta);
	}

}
