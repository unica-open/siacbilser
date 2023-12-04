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
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaDad.
 *
 * @author 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccantonamentoFondiDubbiaEsigibilitaDad extends AccantonamentoFondiDubbiaEsigibilitaBaseDad<CapitoloEntrataPrevisione, AccantonamentoFondiDubbiaEsigibilita> {
	
	@Override
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
	 * Lettura dei capitoli equivalenti non collegati
	 * @param bilancio il bilancio
	 * @param attributiBilancioOld gli attributi precedenti di bilancio
	 * @param modelDetail i model detail
	 * @return i capitoli
	 */
	public List<CapitoloEntrataPrevisione> findCapitoliInBilancioEquivalentiNonCollegati(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancioOld, CapitoloEntrataPrevisioneModelDetail... modelDetail) {
		List<SiacTBilElem> siacTBilElems = siacTAccFondiDubbiaEsigRepository.findSiacTBilElemEquivalentiNonCollegatiJPA(
				attributiBilancio.getBilancio().getUid(),
				attributiBilancio.getUid(),
				attributiBilancioOld.getUid(),
				SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice());
		return convertiLista(siacTBilElems, CapitoloEntrataPrevisione.class, BilMapId.SiacTBilElem_CapitoloEntrataPrevisione_ModelDetail, Converters.byModelDetails(modelDetail));
	}
	
	/**
	 * Ottiene i dettagli dell'accantonamento relativi alla stampa
	 * @param uidAttributiBilancio
	 */
	public List<Object[]> findDettagliAccantonamentoByUidAttributiBilancio(int uidAttributiBilancio) {
		return findDettagliAccantonamentoByUidAttributiBilancio(uidAttributiBilancio, "fnc_siac_acc_fondi_dubbia_esig_by_attr_bil");
	}
}
