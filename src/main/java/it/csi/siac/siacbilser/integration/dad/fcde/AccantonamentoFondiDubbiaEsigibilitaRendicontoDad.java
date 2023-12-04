/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.fcde;


import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.entitymapping.converter.fcde.AccantonamentoFondiDubbiaEsigibilitaRendicontoStanziamentiCapitoloConverter;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaRendicontoDad.
 *
 * @author 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccantonamentoFondiDubbiaEsigibilitaRendicontoDad extends AccantonamentoFondiDubbiaEsigibilitaBaseDad<CapitoloEntrataGestione, AccantonamentoFondiDubbiaEsigibilitaRendiconto> {
	
	@Override
	protected SiacTAccFondiDubbiaEsig buildSiacTAccFondiDubbiaEsig(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		final String methodName = "buildSiacTAccFondiDubbiaEsig";
		afde.setLoginOperazione(loginOperazione);
		
		log.debug(methodName, "Creazione della entity a partire dal model");
		SiacTAccFondiDubbiaEsig tafde = new SiacTAccFondiDubbiaEsig();
		tafde.setSiacTEnteProprietario(siacTEnteProprietario);

		map(afde, tafde, BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilitaRendiconto);
		return tafde;
	}
	
	/**
	 * Lettura dei capitoli equivalenti non collegati
	 * @param bilancio il bilancio
	 * @param attributiBilancioOld gli attributi precedenti di bilancio
	 * @param modelDetail i model detail
	 * @return i capitoli
	 */
	public List<CapitoloEntrataGestione> findCapitoliInBilancioEquivalentiNonCollegati(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancioOld, CapitoloEntrataGestioneModelDetail... modelDetail) {
		List<SiacTBilElem> siacTBilElems = siacTAccFondiDubbiaEsigRepository.findSiacTBilElemEquivalentiNonCollegatiJPA(
				attributiBilancio.getBilancio().getUid(),
				attributiBilancio.getUid(),
				attributiBilancioOld.getUid(),
				SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		return convertiLista(siacTBilElems, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione_ModelDetail, Converters.byModelDetails(modelDetail));
	}
	
	/**
	 * Ottiene i dettagli dell'accantonamento relativi alla stampa
	 * @param uidAttributiBilancio
	 */
	public List<Object[]> findDettagliAccantonamentoByUidAttributiBilancio(int uidAttributiBilancio) {
		return findDettagliAccantonamentoByUidAttributiBilancio(uidAttributiBilancio, "fnc_siac_acc_fondi_dubbia_esig_rendiconto_by_attr_bil");
	}

	/**
	 * Ottiene i dettagli dell'accantonamento relativi alla stampa dei crediti stralciati
	 * @param uidAttributiBilancio
	 */
	public List<Object[]> findDettagliCreditiStralciatiByUidAttributiBilancio(int uidAttributiBilancio) {
		return findDettagliAccantonamentoByUidAttributiBilancio(uidAttributiBilancio, "fnc_siac_acc_fondi_dubbia_esig_crediti_stralciati");
	}

	/**
	 * Ottiene i dettagli dell'accantonamento relativi alla stampa dei crediti stralciati per gli accertamenti sugli anni successivi
	 * @param uidAttributiBilancio
	 */
	public List<Object[]> findDettagliAccertamentiAnniSuccessiviByUidAttributiBilancio(int uidAttributiBilancio) {
		return findDettagliAccantonamentoByUidAttributiBilancio(uidAttributiBilancio, "fnc_siac_acc_fondi_dubbia_esig_anni_successivi");
	}

	/**
	 * SIAC-8395
	 * Ottiene i campi per l'allegato C (report Arconet)
	 * @param uidAttributiBilancio
	 */
	public List<Object[]> calcolaCampiReportAllegatoC(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, Ente ente) {
		return calcolaCampiReportAllegatoC(ente.getUid(), 
				Integer.toString(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getBilancio().getAnno()), 
				accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid(), "fnc_calcola_crediti_stralciati");
	}
	
	/**
	 * SIAC-8540
	 * 
	 * Adegua gli importi di accantonamento, accantonamento originale e residuo finale per l'accantonamento di rendiconto
	 * 
	 * @param <AccantonamentoFondiDubbiaEsigibilitaRendiconto> <b>afde</b> l'accantonamento di rendiconto
	 * @param <CapitoloEntrataGestione> <b>ceg</b> il capitolo di entrata associato all'accantonamento
	 * @return l'accantonamento adeguato
	 */
	@SuppressWarnings("unchecked")
	public AccantonamentoFondiDubbiaEsigibilitaRendiconto adeguaAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde, CapitoloEntrataGestione cap) {
		return adeguaAccantonamentoWithConverters(afde, cap, 
			// Converters da applicare
			AccantonamentoFondiDubbiaEsigibilitaRendicontoStanziamentiCapitoloConverter.class
		);
	}
	
}
