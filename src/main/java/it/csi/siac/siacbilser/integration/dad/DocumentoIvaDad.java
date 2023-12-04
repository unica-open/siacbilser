/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.AttivitaIvaDao;
import it.csi.siac.siacbilser.integration.dao.SiacDIvaRegistrazioneTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDValutaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaAliquotaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaAttivitaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistrazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDValuta;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAliquota;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di DAD per i serviz&icirc; del Documento IVA.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 27/05/2014
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DocumentoIvaDad extends BaseDadImpl {
	
	/** The siac t iva aliquota repository. */
	@Autowired
	private SiacTIvaAliquotaRepository siacTIvaAliquotaRepository;
	
	/** The siac t iva attivita repository. */
	@Autowired
	private SiacTIvaAttivitaRepository siacTIvaAttivitaRepository;
	
	@Autowired
	private AttivitaIvaDao attivitaIvaDao;
	
	/** The siac d iva registrazione tipo repository. */
	@Autowired
	private SiacDIvaRegistrazioneTipoRepository siacDIvaRegistrazioneTipoRepository;
	
	/** The siac d valuta repository. */
	@Autowired
	private SiacDValutaRepository siacDValutaRepository;
	
	/** The ente. */
	private Ente ente;
	
	/**
	 * Sets the ente.
	 *
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	/**
	 * Effettua la ricerca delle aliquote Iva relative al dato ente.
	 *
	 * @return the list
	 * @returns la lista delle aliquote relative l'ente
	 */
	public List<AliquotaIva> ricercaAliquotaIva() {
		final String methodName = "ricercaAliquotaIva";
		List<SiacTIvaAliquota> siacTIvaAliquotaList = siacTIvaAliquotaRepository.findByEnteProprietario(ente.getUid());
		
		log.debug(methodName, siacTIvaAliquotaList == null ? "list is null" : ("list size: " + siacTIvaAliquotaList.size()));
		// Se non ho ottenuto nulla, restituisco una lista vuota
		if(siacTIvaAliquotaList == null) {
			return new ArrayList<AliquotaIva>();
		}
		// Converto la lista
		return convertiLista(siacTIvaAliquotaList, AliquotaIva.class, BilMapId.SiacTIvaAliquota_AliquotaIva);
	}

	/**
	 * Effettua la ricerca delle attivita Iva relative al dato ente.
	 *
	 * @param attivitaIva l'attivita da cui ottenere i dati di filtro
	 * @return the list
	 * @returns la lista delle attivita relative l'ente
	 */
	public List<AttivitaIva> ricercaAttivitaIva(AttivitaIva attivitaIva) {
		final String methodName = "ricercaAttivitaIva";
		List<SiacTIvaAttivita> siacTIvaAttivitaList = attivitaIvaDao.ricercaAttivitaIva(
				ente.getUid(), StringUtils.trimToEmpty(attivitaIva.getCodice()), StringUtils.trimToEmpty(attivitaIva.getDescrizione()));
		
		log.debug(methodName, siacTIvaAttivitaList == null ? "list is null" : ("list size: " + siacTIvaAttivitaList.size()));
		// Se non ho ottenuto nulla, restituisco una lista vuota
		if(siacTIvaAttivitaList == null) {
			return new ArrayList<AttivitaIva>();
		}
		// Converto la lista
		return convertiLista(siacTIvaAttivitaList, AttivitaIva.class, BilMapId.SiacTIvaAttivita_AttivitaIva_Base);
	}
	
	/**
	 * Effettua la ricerca delle attivita Iva relative al dato gruppo.
	 *
	 * @param gruppoAttivitaIva il gruppo collegato
	 * @return the list
	 * @returns la lista delle attivita relative al gruppo
	 */
	public List<AttivitaIva> ricercaAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		final String methodName = "ricercaAttivitaIva";
		List<SiacTIvaAttivita> siacTIvaAttivitaList = siacTIvaAttivitaRepository.findByGruppoAttivitaIva(gruppoAttivitaIva.getUid());
		
		log.debug(methodName, siacTIvaAttivitaList == null ? "list is null" : ("list size: " + siacTIvaAttivitaList.size()));
		// Se non ho ottenuto nulla, restituisco una lista vuota
		if(siacTIvaAttivitaList == null) {
			return new ArrayList<AttivitaIva>();
		}
		// Converto la lista
		return convertiLista(siacTIvaAttivitaList, AttivitaIva.class, BilMapId.SiacTIvaAttivita_AttivitaIva_Base);
	}
	
	/**
	 * Effettua la ricerca dei tipi di registrazione Iva relativi al dato ente.
	 *
	 * @param tipoRegistrazioneIva il tipo da cui ottenere i flags
	 * @return the list
	 * @returns la lista delle attivita relative l'ente
	 */
	public List<TipoRegistrazioneIva> ricercaTipoRegistrazioneIva(TipoRegistrazioneIva tipoRegistrazioneIva) {
		final String methodName = "ricercaAttivitaIva";
		
		TipoFamigliaDocumento tipoFamigliaDocumento = tipoRegistrazioneIva.getTipoFamigliaDocumento();
		
		List<SiacDIvaRegistrazioneTipo> siacDIvaRegistrazioneTipo = siacDIvaRegistrazioneTipoRepository.findByEnteProprietarioFilterByFlags(ente.getUid(), 
				tipoFamigliaDocumento!=null?tipoFamigliaDocumento.getCodice():"");
		
		log.debug(methodName, siacDIvaRegistrazioneTipo == null ? "list is null" : ("list size: " + siacDIvaRegistrazioneTipo.size()));
		// Se non ho ottenuto nulla, restituisco una lista vuota
		if(siacDIvaRegistrazioneTipo == null) {
			return new ArrayList<TipoRegistrazioneIva>();
		}
		// Converto la lista
		return convertiLista(siacDIvaRegistrazioneTipo, TipoRegistrazioneIva.class, BilMapId.SiacDIvaRegistrazioneTipo_TipoRegistrazioneIva);
	}
	
//	/**
//	 * Effettua la ricerca dei tipi di registro Iva relativi al dato ente.
//	 * 
//	 * @param tipoRegistroIva il tipo da cui ottenere i filtri
//	 * 
//	 * @returns la lista delle attivita relative l'ente
//	 */
//	public List<TipoRegistroIva> ricercaTipoRegistroIva(TipoRegistroIva tipoRegistroIva) {
//		final String methodName = "ricercaTipoRegistroIva";
//		List<SiacDIvaRegistroTipo> siacDIvaRegistroTipo = siacDIvaRegistroTipoRepository.findByEnteProprietarioFilterByEsigibilitaTipo(ente.getUid(),
//				// Il tipo di esigibilità è facoltativo. Se non è presente, metto una stringa vuota
//				tipoRegistroIva.getTipoEsigibilitaIva() == null ? "" : tipoRegistroIva.getTipoEsigibilitaIva().getCodice());
//		
//		l.debug(methodName, siacDIvaRegistroTipo == null ? "list is null" : ("list size: " + siacDIvaRegistroTipo.size()));
//		// Se non ho ottenuto nulla, restituisco una lista vuota
//		if(siacDIvaRegistroTipo == null) {
//			return new ArrayList<TipoRegistroIva>();
//		}
//		// Converto la lista
//		return convertiLista(siacDIvaRegistroTipo, TipoRegistroIva.class, BilMapId.SiacDIvaRegistroTipo_TipoRegistroIva);
//	}
	
	/**
	 * Ricerca valuta.
	 *
	 * @return the list
	 */
	public List<Valuta> ricercaValuta() {
		final String methodName = "ricercaValuta";
		List<SiacDValuta> siacDValutas = siacDValutaRepository.findByEnteProprietario(ente.getUid());
		
		log.debug(methodName, siacDValutas == null ? "list is null" : ("list size: " + siacDValutas.size()));
		// Se non ho ottenuto nulla, restituisco una lista vuota
		if(siacDValutas == null) {
			return new ArrayList<Valuta>();
		}
		// Converto la lista
		return convertiLista(siacDValutas, Valuta.class, BilMapId.SiacDValuta_Valuta);
	}

}
