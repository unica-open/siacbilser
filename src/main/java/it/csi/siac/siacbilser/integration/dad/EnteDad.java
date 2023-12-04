/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTEnteProprietarioRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTIndirizzoSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSistemaEsternoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnte;
import it.csi.siac.siacfin2ser.model.SistemaEsterno;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class EnteDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class EnteDad extends BaseDadImpl {
	
	/** The siac t bil repository. */
	@Autowired
	private SiacTEnteProprietarioRepository siacTEnteProprietarioRepository;
	
	/** The siac t soggetto repository */
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
		
	/**
	 * Gets the ente by uid.
	 *
	 * @param uid the uid
	 * @return the ente by uid
	 */
	public Ente getEnteByUid(Integer uid) {
		final String methodName = "getEnteByUid";
		log.debug(methodName, "ente.uid: "+ uid);
		
		// partita IVA dell'Ente:   siac_t_soggetto.partita_iva
		// indirizzo principale dell'Ente:   siac_t_indirizzo_soggetto.principale
		// legame soggetto-ente: siac_r_soggetto_ente_proprietario
		SiacTEnteProprietario siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(uid);
		return mapNotNull(siacTEnteProprietario, Ente.class, BilMapId.SiacTEnteProprietario_Ente);
	}
	
	/**
	 * Gets the ente by uid.
	 *
	 * @param uid the uid
	 * @return the ente by uid
	 */
	public Ente getEnteBaseByUid(Integer uid) {
		SiacTEnteProprietario siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(uid);
		return mapNotNull(siacTEnteProprietario, Ente.class, BilMapId.SiacTEnteProprietario_Ente_Base);
	}
	
	/**
	 * Gets the soggetto by ente.
	 *
	 * @param ente the ente
	 * @return the soggetto by ente
	 */
	public Soggetto getSoggettoByEnte(Ente ente) {
		final String methodName = "getSoggettoByEnte";
		log.debug(methodName, "ente.uid: " + ente.getUid());
		
		// partita IVA dell'Ente:   siac_t_soggetto.partita_iva
		// legame soggetto-ente: siac_r_soggetto_ente_proprietario
		
		List<SiacTSoggetto> siacTSoggettos = siacTSoggettoRepository.findSoggettoByEnteId(ente.getUid());
		log.debug(methodName, "siacTSoggettos.size: " + siacTSoggettos.size());
		for(SiacTSoggetto siacTSoggetto : siacTSoggettos) {
			// Considero il primo
			return mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
		}
		return null;
	}
	
	/**
	 * Gets the indirizzo soggetto principale by ente.
	 *
	 * @param ente the ente
	 * @return the indirizzo soggetto principale by ente
	 */
	public IndirizzoSoggetto getIndirizzoSoggettoPrincipaleIvaByEnte(Ente ente) {
		final String methodName = "getIndirizzoSoggettoPrincipaleIvaByEnte";
		log.debug(methodName, "ente.uid: " + ente.getUid());
		final Date now = new Date();
		
		// partita IVA dell'Ente:   siac_t_soggetto.partita_iva
		// indirizzo principale dell'Ente:   siac_t_indirizzo_soggetto.principale
		
		List<SiacTSoggetto> siacTSoggettos = siacTSoggettoRepository.findSoggettoByEnteId(ente.getUid());
		log.debug(methodName, "siacTSoggettos.size: " + siacTSoggettos.size());
		for(SiacTSoggetto siacTSoggetto : siacTSoggettos) {
			List<SiacTIndirizzoSoggetto> siacTIndirizzoSoggettos = siacTSoggetto.getSiacTIndirizzoSoggettos();
			log.debug(methodName, "siacTSoggetto.uid: " + siacTSoggetto.getUid() +
					" - siacTSoggetto.siacTIndirizzoSoggettos.size: " + siacTIndirizzoSoggettos.size());
			for(SiacTIndirizzoSoggetto siacTIndirizzoSoggetto : siacTIndirizzoSoggettos) {
				if("S".equalsIgnoreCase(siacTIndirizzoSoggetto.getPrincipale())
						&& (siacTIndirizzoSoggetto.getDataCancellazione() == null 
							|| siacTIndirizzoSoggetto.getDataCancellazione().after(now))) {
					// Considero il primo indirizzo principale
					return map(siacTIndirizzoSoggetto, IndirizzoSoggetto.class, BilMapId.SiacTIndirizzoSoggetto_IndirizzoSoggetto);
				}
			}
		}
		return null;
	}

	
	
	public List<Ente> getEntiByCodiceAndCodiceSistemaEsterno(String codiceEnte, SistemaEsterno sistemaEsterno) {
		SiacDSistemaEsternoEnum siacDSistemaEsternoEnum = SiacDSistemaEsternoEnum.bySistemaEsterno(sistemaEsterno);
		List<SiacTEnteProprietario> siacTEnteProprietarios = siacTEnteProprietarioRepository.findByExtsysEnteCodeAndExtsysCode(codiceEnte, siacDSistemaEsternoEnum.getCodice());
		return convertiLista(siacTEnteProprietarios, Ente.class, BilMapId.SiacTEnteProprietario_Ente);
	}
	
	/**
	 * Ottiene l'Ente SIAC a partire dal codice Ente del sistema esterno specificato come parametro.
	 *
	 * @param codiceEnteEsterno the codice ente esterno
	 * @param sistemaEsterno the sistema esterno
	 * @return the enti by codice and codice sistema esterno
	 */
	public Ente findEnteByCodiceAndSistemaEsterno(String codiceEnteEsterno, SistemaEsterno sistemaEsterno) {
		SiacDSistemaEsternoEnum siacDSistemaEsternoEnum = SiacDSistemaEsternoEnum.bySistemaEsterno(sistemaEsterno);
		List<SiacTEnteProprietario> siacTEnteProprietarios = siacTEnteProprietarioRepository.findByExtsysEnteCodeAndExtsysCode(codiceEnteEsterno, siacDSistemaEsternoEnum.getCodice());
		
		if(siacTEnteProprietarios==null || siacTEnteProprietarios.isEmpty()) {
			throw new IllegalStateException("Nessun un Ente corrispondente al codiceEnteEsterno: " 
					+ codiceEnteEsterno +" e sistemaEsterno: "+sistemaEsterno + ". Deve essercene uno solo." );
		}
		
		if(siacTEnteProprietarios.size()>1) {
			throw new IllegalStateException("Trovato piu' di un Ente corrispondente al codiceEnteEsterno: " 
					+ codiceEnteEsterno +" e sistemaEsterno: "+sistemaEsterno + ". Deve essercene uno solo." );
		}
		
		return map(siacTEnteProprietarios.get(0), Ente.class, BilMapId.SiacTEnteProprietario_Ente);
	}
	

	/**
	 * Ottiene l'Ente SIAC a partire dal codice Ente del sistema esterno specificato come parametro.
	 *
	 * @param codiceEnteEsterno the codice ente esterno
	 * @param sistemaEsterno the sistema esterno
	 * @return the enti by codice and codice sistema esterno
	 */
	public String findCodiceEnteEsternoByEnteAndSistemaEsterno(Ente ente, SistemaEsterno sistemaEsterno) {
		SiacDSistemaEsternoEnum siacDSistemaEsternoEnum = SiacDSistemaEsternoEnum.bySistemaEsterno(sistemaEsterno);
		List<String> extsysEnteCodes = siacTEnteProprietarioRepository.findExtsysEnteCodeByEnteUidAndExtsysCode(ente.getUid(), siacDSistemaEsternoEnum.getCodice());
		
		if(extsysEnteCodes==null || extsysEnteCodes.isEmpty()) {
			throw new IllegalStateException("Nessun codiceEnteEsterno corrispondente all'Ente con uid: " 
					+ ente.getUid() +" e sistemaEsterno: "+sistemaEsterno + ". Deve essercene uno solo." );
		}
		
		if(extsysEnteCodes.size()>1) {
			throw new IllegalStateException("Trovato piu' di un codiceEnteEsterno corrispondente all'Ente con uid: " 
					+ ente.getUid() +" e sistemaEsterno: "+sistemaEsterno + ". Deve essercene uno solo." );
		}
		
		return extsysEnteCodes.get(0);
		
	}

	public String caricaPartitaIva(Ente enteInput) {
		List<SiacTSoggetto> soggettoEnte = siacTSoggettoRepository.findSoggettoByEnteId(enteInput.getUid());
		if(soggettoEnte == null || soggettoEnte.isEmpty()) {
			throw new IllegalStateException("Nessun soggeetto corrispondente all'Ente con uid: " + enteInput.getUid() );
		}
		SiacTSoggetto siacTSoggetto = soggettoEnte.get(0);
		return siacTSoggetto.getPartitaIva();
	}
	
	//SIAC-8362
	 /**
     * @deprecated
     * This method is no longer used.
     * <p> See {@link it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum}.
     */	
	@Deprecated
	public Map<ParametroConfigurazioneEnte, String> caricaConfigurazionePerEnte(Ente ente) {
		return caricaConfigurazionePerEnte(ente, null);
	}
	
	 /**
     * @deprecated
     * This method is no longer used.
     * <p> See {@link it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum}.
     */	
	@Deprecated
	//SIAC-8362
	public Map<ParametroConfigurazioneEnte, String> caricaConfigurazionePerEnte(Ente ente, List<ParametroConfigurazioneEnte> parametri) {
		List<String> codes = new ArrayList<String>();
		if(parametri == null || parametri.isEmpty()) {
			codes =  ParametroConfigurazioneEnte.getAllCodes();
		} else {
			for (ParametroConfigurazioneEnte par : parametri) {
				codes.add(par.getCodice());
			}
		}
		
		
		List<Object[]> configs = siacTEnteProprietarioRepository.caricaConfigurazioniByEnte(ente.getUid(), null);
		Map<ParametroConfigurazioneEnte, String> mapPar = new HashMap<ParametroConfigurazioneEnte, String>();
		for (Object[] cc : configs) {
			if(cc.length <2) {
				continue;
			}
			String codice = (String) cc[0];
			String valore = (String) cc[1];
			ParametroConfigurazioneEnte fromCodice = ParametroConfigurazioneEnte.fromCodice(codice);
			if(fromCodice != null) {
				mapPar.put(fromCodice, valore);
			}
		}
		
		return mapPar;
	}

}
