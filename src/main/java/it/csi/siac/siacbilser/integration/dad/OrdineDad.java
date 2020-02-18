/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.OrdineDao;
import it.csi.siac.siacbilser.integration.dao.SiacTOrdineRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOrdine;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdine;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Ordine;

/**
 * Data access delegate di un Ordine.
 *
 * @author Valentina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class OrdineDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private OrdineDao ordineDao;
	
	@Autowired
	private SiacTOrdineRepository siacTOrdineRepository;
	
	/**
	 * Inserisce ordine.
	 *
	 * @param ordine the ordine
	 * @return the ordine
	 */
	public Ordine inserisceOrdine(Ordine ordine) {
		String methodName = "inserisceOrdine";
		SiacTOrdine siacTOrdine = buildSiacTOrdine(ordine);
		siacTOrdine = ordineDao.create(siacTOrdine);
		ordine.setUid(siacTOrdine.getUid());
		log.debug(methodName, "Inserito ordine con uid: "+ordine.getUid());
		return ordine;
	}

	/**
	 * Aggiorna ordine.
	 *
	 * @param ordine the ordine
	 * @return the ordine
	 */
	public Ordine aggiornaOrdine(Ordine ordine) {
		SiacTOrdine siacTOrdine = buildSiacTOrdine(ordine);
		ordineDao.update(siacTOrdine);
		return ordine;
	}
	
	/**
	 * Elimina ordine.
	 *
	 * @param ordine the ordine
	 */
	public void eliminaOrdine(Ordine ordine) {
		String methodName = "eliminaOrdine";
		
		SiacTOrdine siacTOrdine = siacTOrdineRepository.findOne(ordine.getUid());
		siacTOrdine.setLoginOperazione(loginOperazione);
		
		Date now = new Date();
		siacTOrdine.setDataCancellazioneIfNotSet(now);
		
		if(siacTOrdine.getSiacRDocOrdines()!=null) {
			for(SiacRDocOrdine r : siacTOrdine.getSiacRDocOrdines()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		log.debug(methodName, "Eliminato ordine con uid: "+ordine.getUid());
	}
	
	/**
	 * Ricerca ordini documento.
	 *
	 * @param documento the documento
	 * @return the list
	 */
	public List<Ordine> ricercaOrdiniDocumento(DocumentoSpesa documento) {
		List<SiacTOrdine> siacTOrdines = siacTOrdineRepository.findOrdiniByDocumentoSpesa(documento.getUid());
		return convertiLista(siacTOrdines, Ordine.class, BilMapId.SiacTOrdine_Ordine);
	}
	
	/**
	 * Builds the siac t ordine.
	 *
	 * @param ordine the ordine
	 * @return the siac t ordine
	 */
	private SiacTOrdine buildSiacTOrdine(Ordine ordine) {
		SiacTOrdine siacTOrdine = new SiacTOrdine();
		siacTOrdine.setLoginOperazione(loginOperazione);
		ordine.setLoginOperazione(loginOperazione);
		ordine.setEnte(ente);
		map(ordine, siacTOrdine, BilMapId.SiacTOrdine_Ordine);		
		return siacTOrdine;
	}
	
}
