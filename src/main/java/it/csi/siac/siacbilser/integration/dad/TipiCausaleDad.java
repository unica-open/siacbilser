/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDCausaleTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleFamTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.TipoCausale;

// TODO: Auto-generated Javadoc
/**
 * The Class TipiCausaleDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TipiCausaleDad extends BaseDadImpl {
	
	/** The siac d causale tipo repository. */
	@Autowired
	private SiacDCausaleTipoRepository siacDCausaleTipoRepository;
	
	/**
	 * Ricerca tipi causale entrata by ente.
	 *
	 * @param ente the ente
	 * @return the list
	 */
	public List<TipoCausale> ricercaTipiCausaleEntrataByEnte(Ente ente) {
		return ricercaTipiCausaleByEnte(ente, SiacDCausaleFamTipoEnum.Predoc_Entrata);
	}
	
	/**
	 * Ricerca tipi causale spesa by ente.
	 *
	 * @param ente the ente
	 * @return the list
	 */
	public List<TipoCausale> ricercaTipiCausaleSpesaByEnte(Ente ente) {
		return ricercaTipiCausaleByEnte(ente, SiacDCausaleFamTipoEnum.Predoc_Spesa);
	}
	
	
	/**
	 * Effettua la ricerca dei codici causale per un Ente (entrata o uscita).
	 *
	 * @param ente the ente
	 * @param fam the fam
	 * @return List&lt;TipoCausale&gt;
	 */
	private List<TipoCausale> ricercaTipiCausaleByEnte(Ente ente, SiacDCausaleFamTipoEnum fam) {
		List<SiacDCausaleTipo> elencoTipiCausale = siacDCausaleTipoRepository.findCausaleTipiByEnteEFan(ente.getUid(), fam.getCodice());
		if(elencoTipiCausale == null) {
			return new ArrayList<TipoCausale>();
		}
		
		List<TipoCausale> elencoTipoCausale = convertiLista(elencoTipiCausale, TipoCausale.class, BilMapId.SiacDCausaleTipo_TipoCausale);
		return elencoTipoCausale;
	}
}
