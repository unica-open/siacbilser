/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;


import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
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
public class AccantonamentoFondiDubbiaEsigibilitaDad extends AccantonamentoFondiDubbiaEsigibilitaBaseDad<CapitoloEntrataPrevisione, AccantonamentoFondiDubbiaEsigibilita> {
	
	/**
	 * Ricerca sintetica dell'accantonamento.
	 * @param ente l'ente
	 * @param bilancio il bilancio da utilizzare
	 * @param modelDetails i model detail da utilizzare
	 * @param parametriPaginazione i parametri di paginazione
	 *
	 * @return la lista paginata degli accantonamenti
	 */
	public ListaPaginata<AccantonamentoFondiDubbiaEsigibilita> ricercaSintetica(Ente ente,
			Bilancio bilancio,
			AccantonamentoFondiDubbiaEsigibilitaModelDetail[] modelDetails,
			ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = accantonamentoFondiDubbiaEsigibilitaDao.ricercaSintetica(
				ente.getUid(),
				bilancio != null ? bilancio.getUid() : null,
				SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice(),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacTAccFondiDubbiaEsigs, AccantonamentoFondiDubbiaEsigibilita.class, BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita_ModelDetail, modelDetails);
	}
	
	/**
	 * Crea l'istanza dell'entity a partire dall'istanza del model
	 *
	 * @param afde il model
	 * @return la entity
	 */
	protected SiacTAccFondiDubbiaEsig buildSiacTAccFondiDubbiaEsig(AccantonamentoFondiDubbiaEsigibilita afde) {
		final String methodName = "buildSiacTAccFondiDubbiaEsig";
		afde.setLoginOperazione(loginOperazione);
		
		log.debug(methodName, "Creazione della entity a partire dal model");
		SiacTAccFondiDubbiaEsig tafde = new SiacTAccFondiDubbiaEsig();
		tafde.setSiacTEnteProprietario(siacTEnteProprietario);

		map(afde, tafde, BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita);
		return tafde;
	}
	
	/**
	 * Ottiene gli accantonamenti a partire dal bilancio
	 * @param bilancio il bilancio
	 * @return gli accantonamenti correlati al bilancio
	 */
	public List<AccantonamentoFondiDubbiaEsigibilita> findByBilancio(Bilancio bilancio, AccantonamentoFondiDubbiaEsigibilitaModelDetail... modelDetails) {
		//return siacTAccFondiDubbiaEsigRepository.countByElemId(Integer.valueOf(cep.getUid()));
		List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = siacTAccFondiDubbiaEsigRepository.findByBilIdAndElemTipoCode(bilancio.getUid(), SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice());
		
		return convertiLista(siacTAccFondiDubbiaEsigs, AccantonamentoFondiDubbiaEsigibilita.class, BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	/**
	 * Conta gli accantonamenti a partire dal capitolo
	 * @param cep il capitolo di entrata previsione
	 * @param modelDetails i dettagli del modello da ricavare
	 * @return il numero di accantonamenti correlati al capitolo
	 */
	public AccantonamentoFondiDubbiaEsigibilita findByCapitolo(CapitoloEntrataPrevisione cep, AccantonamentoFondiDubbiaEsigibilitaModelDetail... modelDetails) {
		final String methodName = "findByCapitolo";
		List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs = siacTAccFondiDubbiaEsigRepository.findByElemId(cep.getUid());
		
		if(siacTAccFondiDubbiaEsigs == null || siacTAccFondiDubbiaEsigs.isEmpty()) {
			log.debug(methodName, "Nessuna entity di tipo " + SiacTAccFondiDubbiaEsig.class.getSimpleName() + " per capitolo " + cep.getUid());
			return null;
		}
		
		// Prendo solo il primo
		return mapNotNull(siacTAccFondiDubbiaEsigs.get(0), AccantonamentoFondiDubbiaEsigibilita.class,
				BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita_ModelDetail, Converters.byModelDetails(modelDetails));
		
	}

	/**
	 * Conta gli elementi a partire dal bilancio
	 * @param bilancio il bilancio per cui contare i record
	 * @return il numero dei record
	 */
	public Long countByBilancio(Bilancio bilancio) {
		return siacTAccFondiDubbiaEsigRepository.countSiacRBilElemAccFondiDubbiaEsigByBilIdAndElemTipoCode(bilancio.getUid(), SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice());
	}
}
