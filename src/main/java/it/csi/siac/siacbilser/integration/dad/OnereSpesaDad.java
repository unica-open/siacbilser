/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.DettaglioOnereDao;
import it.csi.siac.siacbilser.integration.dao.SiacRDocOnereRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;

/**
 * Data access delegate di un DettaglioOnere .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class OnereSpesaDad extends ExtendedBaseDadImpl {
	
	/** The dettaglio onere dao. */
	@Autowired
	private DettaglioOnereDao dettaglioOnereDao;
	
	/** The siac t subdoc repository. */
	@Autowired
	private SiacRDocOnereRepository siacRDocOnereRepository;
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	/**
	 * Ottiene un DettaglioOnere (o Quota) a partire dal suo uid.
	 *
	 * @param uid del dettaglioOnere
	 * @return the dettaglio onere
	 */
	public DettaglioOnere findDettaglioOnereById(Integer uid) {
		final String methodName = "findDettaglioOnereById";		
		log.debug(methodName, "uid: "+ uid);
		SiacRDocOnere siacTDoc = dettaglioOnereDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il DettaglioOnere con id: " + uid);
		}
		return  mapNotNull(siacTDoc, DettaglioOnere.class, BilMapId.SiacRDocOnere_DettaglioOnere);
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<DettaglioOnere> findOneryByIdDocumento(Integer idDocumento) {
		List<SiacRDocOnere> siacTSubdocs = siacRDocOnereRepository.findSiacRDocOnereByDocId(idDocumento);		
		return convertiLista(siacTSubdocs, DettaglioOnere.class, BilMapId.SiacRDocOnere_DettaglioOnere);
	
	}
	
	
	/**
	 * Inserisci anagrafica dettaglio onere.
	 *
	 * @param onere the onere
	 */
	public void inserisciAnagraficaDettaglioOnere(DettaglioOnere onere) {		
		SiacRDocOnere siacRDocOnere = buildSiacRDocOnere(onere);	
		dettaglioOnereDao.create(siacRDocOnere);
		onere.setUid(siacRDocOnere.getUid());
	}
	

	/**
	 * Aggiorna anagrafica dettaglio onere.
	 *
	 * @param onere the onere
	 */
	public void aggiornaAnagraficaDettaglioOnere(DettaglioOnere onere) {
		SiacRDocOnere siacRDocOnere = buildSiacRDocOnere(onere);	
		dettaglioOnereDao.update(siacRDocOnere);
		onere.setUid(siacRDocOnere.getUid());
	}	
	
	
	/**
	 * Builds the siac r doc onere.
	 *
	 * @param dettaglioOnere the dettaglio onere
	 * @return the siac r doc onere
	 */
	private SiacRDocOnere buildSiacRDocOnere(DettaglioOnere dettaglioOnere) {
		SiacRDocOnere siacRDocOnere = new SiacRDocOnere();
		siacRDocOnere.setLoginOperazione(loginOperazione);
		dettaglioOnere.setLoginOperazione(loginOperazione);
		dettaglioOnere.setEnte(ente);
//		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
//		siacTEnteProprietario.setUid(1);
//		siacRDocOnere.setSiacTEnteProprietario(siacTEnteProprietario);
	
		map(dettaglioOnere, siacRDocOnere, BilMapId.SiacRDocOnere_DettaglioOnere);		
		
//		SiacTDoc siacTDoc = siacTDocRepository.findOne(siacRDocOnere.getSiacTDoc().getUid());
//		siacRDocOnere.setSiacTDoc(siacTDoc);
		
		//siacTDoc.setSiacDSubdocTipo(eef.getEntity(SiacDSubdocTipoEnum.Spesa, dettaglioOnere.getEnte().getUid(), SiacDSubdocTipo.class));
		return siacRDocOnere;
	}
	
	
	/**
	 * Elimina dettaglio onere.
	 *
	 * @param dettaglioOnere the dettaglio onere
	 */
	public void eliminaDettaglioOnere(DettaglioOnere dettaglioOnere) {
		SiacRDocOnere siacRDocOnere = dettaglioOnereDao.findById(dettaglioOnere.getUid());
		dettaglioOnereDao.delete(siacRDocOnere);
	}
	
	/**
	 * 
	 * @param dettaglioOnereTrovato
	 * @return somma importi oneri stesso tipo split reverse
	 */
	public BigDecimal ivaSplitReverseCheckConnection(DettaglioOnere dettaglioOnereTrovato) {
		return siacRDocOnereRepository.sommaImportiOneriStessoTipoSR(dettaglioOnereTrovato.getDocumentoSpesa().getUid(), dettaglioOnereTrovato.getTipoOnere().getTipoIvaSplitReverse().getCodice());
	}
	
	/**
	 * 
	 * @param dettaglioOnereTrovato
	 * @return somma importi split reverse importo
	 */
	public BigDecimal ivaSplitReverseCheckConnectionImporto(DettaglioOnere dettaglioOnereTrovato) {
		return siacTSubdocRepository.sommaImportiSplitreverseImporto(dettaglioOnereTrovato.getDocumentoSpesa().getUid(), dettaglioOnereTrovato.getTipoOnere().getTipoIvaSplitReverse().getCodice());
	}
	
	/**
	 * Ottiene l'importo imponibile degli oneri collegati al documento
	 * @param uidDocumento l'uid del documento
	 * @return il totale degli imponibili
	 */
	public BigDecimal getImportoImponibileOneriCollegatiAlDocumento(Integer uidDocumento) {
		return siacRDocOnereRepository.sommaImportiImponibileOnereByDocId(uidDocumento);
	}

	/**
	 * Ottiene l'importo imponibile degli oneri collegati al documento
	 * @param uidDocumento l'uid del documento
	 * @param uidOnereDaEscludere l'onere da escludere nella somma
	 * @return il totale degli imponibili
	 */
	public BigDecimal getImportoImponibileOneriCollegatiAlDocumento(Integer uidDocumento, Integer uidOnereDaEscludere) {
		return siacRDocOnereRepository.sommaImportiImponibileOnereByDocIdExcludingDocOnereId(uidDocumento, uidOnereDaEscludere);
	}
	
	/**
	 * Ottiene l'importo imponibile degli oneri collegati al documento
	 * @param uidDocumento l'uid del documento
	 * @return il totale degli imponibili
	 */
	public BigDecimal getMassimoImportoImponibileOneriCollegatiAlDocumento(Integer uidDocumento) {
		return siacRDocOnereRepository.massimoImportiImponibileOnereByDocId(uidDocumento);
	}

}
