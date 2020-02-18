/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTIvaStampaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.dao.StampaIvaDao;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaRegistroTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPeriodoTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.BooleanToStringConverter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

/**
 * Data access delegate di una StampaIva.
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class StampaIvaDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private StampaIvaDao stampaIvaDao;
	@Autowired
	private PeriodoDad periodoDad;
	@Autowired
	private SiacTIvaStampaRepository siacTIvaStampaRepository;
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;

	
	/**
	 * Inserisce una stampa iva.
	 *
	 * @param stampaIva la stampa iva da inserire
	 */
	public void inserisciStampaIva(StampaIva stampaIva) {
		SiacTIvaStampa siacTIvaStampa= buildSiacTIvaStampa(stampaIva);
		stampaIvaDao.create(siacTIvaStampa);
		stampaIva.setUid(siacTIvaStampa.getUid());
	}
	
	
	/**
	 * Aggiorna la stampa iva.
	 *
	 * @param stampaIva la stampa iva da aggiornare
	 */
	public void aggiornaStampaIva(StampaIva stampaIva) {
		SiacTIvaStampa siacTIvaStampa= buildSiacTIvaStampa(stampaIva);
		stampaIvaDao.update(siacTIvaStampa);
		stampaIva.setUid(siacTIvaStampa.getUid());
		
	}
	
	
	/**
	 * Ricerca stampe iva con parametri di ricerca periodo, registro iva e tipo di stampa iva
	 *
	 * @param stampaIva la stampa iva
	 * @param registro il registro
	 * 
	 * @return la lista delle stampe iva trovate
	 */
	public List<StampaIva> ricercaStampaIva(StampaIva stampaIva, RegistroIva registro) {
		// Imposto l'ente nel DAD
		periodoDad.setEnte(ente);
		Integer uidPeriodo = periodoDad.findPeriodoUid(stampaIva.getPeriodo(), stampaIva.getAnnoEsercizio());
		List<SiacTIvaStampa> lista = siacTIvaStampaRepository.findByRegistroEPeriodoETipoStampaIva(
				ente.getUid(), registro.getUid(), uidPeriodo,
				SiacDIvaStampaTipoEnum.byTipoStampaIva(stampaIva.getTipoStampaIva()).getCodice());
		
		return convertiLista(lista, StampaIva.class, BilMapId.SiacTIvaStampa_StampaIva);
	}
	
	public boolean presentiStampeByRegistroTipoStampaPeriodoETipoStampaIva(RegistroIva registroIva, TipoStampa tipoStampa, Periodo periodo, Integer annoEsercizio, TipoStampaIva tipoStampaIva) {
		final String methodName = "presentiStampeByRegistroTipoStampaPeriodoETipoStampaIva";
		SiacDIvaStampaTipoEnum siacDIvaStampaTipoEnum = SiacDIvaStampaTipoEnum.byTipoStampaIva(tipoStampaIva);
		SiacDIvaStampaStatoEnum siacDIvaStampaStatoEnum = SiacDIvaStampaStatoEnum.byTipoStampa(tipoStampa);
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndPeriodoTipoAndEnteProprietario(""+annoEsercizio, periodo.getCodice(), ente.getUid());
		Integer periodoId = siacTPeriodo.getUid();
		log.debug(methodName, "trovato periodo con uid: " + periodoId);
		List<SiacTIvaStampa> stampeIva =  siacTIvaStampaRepository.findByRegistroEPeriodoETipoStampaIvaEStato(registroIva.getUid(), periodoId, siacDIvaStampaTipoEnum.getCodice(), siacDIvaStampaStatoEnum.getCodice());
		return stampeIva != null && !stampeIva.isEmpty();
	}
	
	public ListaPaginata<StampaIva> ricercaSinteticaStampaIva(StampaIva stampaIva, RegistroIva registro, String nomeFile, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTIvaStampa> lista = stampaIvaDao.ricercaSinteticaStampaIva(
				ente.getUid(),
				
				//stampaIva.getFlagPagati()!=null?stampaIva.getFlagPagati():false,
				new BooleanToStringConverter().convertTo(stampaIva.getFlagPagati()),
						
				stampaIva.getTipoStampaIva()!=null?SiacDIvaStampaTipoEnum.byTipoStampaIva(stampaIva.getTipoStampaIva()):null,
				stampaIva.getAnnoEsercizio(),
				(registro!=null && registro.getGruppoAttivitaIva()!=null && registro.getGruppoAttivitaIva().getUid()!=0) ? registro.getGruppoAttivitaIva().getUid() : null,
				(registro!=null && registro.getTipoRegistroIva()!=null) ? SiacDIvaRegistroTipoEnum.byTipoRegistroIva(registro.getTipoRegistroIva()) : null,
				
				new BooleanToStringConverter().convertTo(stampaIva.getFlagIncassati()),
				//stampaIva.getFlagIncassati()!=null?stampaIva.getFlagIncassati():false,
						
				registro!=null && registro.getUid()!=0 ?registro.getUid():null,
				registro!=null?registro.getCodice():null,
				SiacDPeriodoTipoEnum.byPeriodoEvenNull(stampaIva.getPeriodo()),
				nomeFile,
				stampaIva.getDataCreazione(), 				
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, StampaIva.class, BilMapId.SiacTIvaStampa_StampaIva_FileBase);
	}
	
	
	
	/**
	 * Builds the siac t iva stampa.
	 *
	 * @param stampaIva the stampa iva
	 * @return the siac t iva stampa
	 */
	private SiacTIvaStampa buildSiacTIvaStampa(StampaIva stampaIva) {
		SiacTIvaStampa siacTIvaStampa = new SiacTIvaStampa();
		siacTIvaStampa.setLoginOperazione(loginOperazione);
		stampaIva.setLoginOperazione(loginOperazione);
		map(stampaIva, siacTIvaStampa, BilMapId.SiacTIvaStampa_StampaIva);		
		return siacTIvaStampa;
	}
	

	/**
	 * Gets the ultimo periodo stampato.
	 *
	 * @param registroIva the registro iva
	 * @param tipoStampa the tipo stampa
	 * @param periodo the periodo
	 * @param annoEsercizio the anno esercizio
	 * @param tipoStampaIva the tipo stampa iva
	 * @return the ultimo periodo stampato
	 */
	public Periodo getUltimoPeriodoStampato(RegistroIva registroIva, TipoStampa tipoStampa, String annoEsercizio, TipoStampaIva tipoStampaIva) {
		List<SiacTPeriodo> siacTPeriodos = siacTIvaStampaRepository.findByRegistroTipoStampaIvaEAnnoEStatoPagatiOIncassati(registroIva.getUid(), SiacDIvaStampaTipoEnum.byTipoStampaIva(tipoStampaIva).getCodice(), SiacDIvaStampaStatoEnum.byTipoStampa(tipoStampa).getCodice(), annoEsercizio);
		if(siacTPeriodos == null || siacTPeriodos.isEmpty()) {
			return null;
		}
		return Periodo.fromCodice(siacTPeriodos.get(0).getSiacDPeriodoTipo().getPeriodoTipoCode());
	}


}
