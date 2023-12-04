/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.ProrataEChiusuraGruppoIvaDao;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaProrataRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaProrata;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

/**
 * Data access delegate di un DocumentoSpesa .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProrataEChiusuraGruppoIvaDad extends ExtendedBaseDadImpl {
	
	/** The prorata e chiusura gruppo iva dao. */
	@Autowired
	private ProrataEChiusuraGruppoIvaDao prorataEChiusuraGruppoIvaDao;
		
	/** The siac t iva prorata repository. */
	@Autowired
	private SiacTIvaProrataRepository siacTIvaProrataRepository;
	
	/**
	 * Inserisci prorata e chiusura gruppo iva.
	 *
	 * @param prorata the prorata
	 */
	public void inserisciProrataEChiusuraGruppoIva(ProRataEChiusuraGruppoIva prorata) {
		SiacTIvaProrata siacTIvaProrata = buildSiacTIvaProrata(prorata);
		prorataEChiusuraGruppoIvaDao.create(siacTIvaProrata);
		prorata.setUid(siacTIvaProrata.getUid());
	}
	
	/**
	 * Aggiorna prorata e chiusura gruppo iva.
	 *
	 * @param prorata the prorata
	 */
	public void aggiornaProrataEChiusuraGruppoIva(ProRataEChiusuraGruppoIva prorata) {
		SiacTIvaProrata siacTIvaProrata = buildSiacTIvaProrata(prorata);
		prorataEChiusuraGruppoIvaDao.update(siacTIvaProrata);
		prorata.setUid(siacTIvaProrata.getUid());
	}
	
	/**
	 * Ricerca uid prorata legata al gruppo per un anno.
	 *
	 * @param prorata the prorata
	 * @param gruppo the gruppo
	 * @return the integer
	 */
	public Integer ricercaUidProrataLegataAlGruppoPerUnAnno(ProRataEChiusuraGruppoIva prorata, GruppoAttivitaIva gruppo) {	
		log.debug("ricercaUidProrataLegataAlGruppoPerUnAnno", "anno prorata: " +prorata.getAnnoEsercizio());
		log.debug("ricercaUidProrataLegataAlGruppoPerUnAnno", "uid gruppo: " +gruppo.getUid());
		SiacTIvaProrata siacTIvaProrata=siacTIvaProrataRepository.findByAnnoEGruppo(prorata.getAnnoEsercizio(), gruppo.getUid());
		
		if (siacTIvaProrata!=null){
			log.debug("ricercaUidProrataLegataAlGruppoPerUnAnno", "Ho trovato prorata con id: " +siacTIvaProrata.getIvaproId());
		return siacTIvaProrata.getIvaproId();
		} else{
			log.debug("ricercaUidProrataLegataAlGruppoPerUnAnno", "Non ho trovato prorata " );
		}	
		return null;
	}

	/**
	 * Builds the siac t iva prorata.
	 *
	 * @param prorata the prorata
	 * @return the siac t iva prorata
	 */
	private SiacTIvaProrata buildSiacTIvaProrata(ProRataEChiusuraGruppoIva prorata) {
		SiacTIvaProrata siacTIvaProrata = new SiacTIvaProrata();
		siacTIvaProrata.setLoginOperazione(loginOperazione);
		prorata.setLoginOperazione(loginOperazione);
		map(prorata, siacTIvaProrata, BilMapId.SiacTIvaProrata_ProrataEChiusuraGruppoIva);
		return siacTIvaProrata;
	}
	

}
