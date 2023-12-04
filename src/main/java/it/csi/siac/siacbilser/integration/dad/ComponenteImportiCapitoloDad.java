/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo.ComponenteImportiCapitoloDao;
import it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo.SiacTBilElemDetCompRepository;
import it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo.SiacTBilElemDetVarCompRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class ComponenteImportiCapitoloDad.
 */
@Component
@Transactional
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ComponenteImportiCapitoloDad extends ExtendedBaseDadImpl {

	private static final Integer NUM_ELEM_DET_COMP_DEF_STESSO_TIPO_ASSOCIABILI_A_CAPITOLO = Integer.valueOf(3);
	
	@Autowired private ComponenteImportiCapitoloDao componenteImportiCapitoloDao;
	@Autowired private SiacTBilElemDetCompRepository siacTBilElemDetCompRepository;
	@Autowired private SiacTBilElemDetVarCompRepository siacTBilElemDetVarCompRepository;
	
	
	
	/**
	 * Inserimento della componente importi capitolo
	 * @param componenteImportiCapitolo la componente importi capitolo
	 */
	public void inserisciComponenteImportiCapitolo(ComponenteImportiCapitolo componenteImportiCapitolo) {
		SiacTBilElemDetComp siacTBilElemDetComp = buildSiacTBilElemDetComp(componenteImportiCapitolo);
		componenteImportiCapitoloDao.create(siacTBilElemDetComp);
		componenteImportiCapitolo.setUid(siacTBilElemDetComp.getUid());
	}
	/**
	 * Aggiornamento della componente importi capitolo
	 * @param componenteImportiCapitolo la componente importi capitolo
	 */
	public void aggiornaComponenteImportiCapitolo(ComponenteImportiCapitolo componenteImportiCapitolo) {
		SiacTBilElemDetComp siacTBilElemDetComp = buildSiacTBilElemDetComp(componenteImportiCapitolo);
		componenteImportiCapitoloDao.update(siacTBilElemDetComp);
	}
	/**
	 * Ricerca della componente importi capitolo per uid
	 * @param uid l'uid
	 * @return la componente importi capitolo
	 */
	public ComponenteImportiCapitolo findComponenteImportiCapitoloByUid(Integer uid, ComponenteImportiCapitoloModelDetail... modelDetails) {
		SiacTBilElemDetComp siacTBilElemDetComp = siacTBilElemDetCompRepository.findComponenteLogicamenteValidaById(uid);
		return mapNotNull(siacTBilElemDetComp, ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	/**
	 * Ricerca delle componenti importi capitolo per uid
	 * @param uid l'uid
	 * @param modelDetails i model details
	 * @return le componenti importi capitolo
	 */
	public List<ComponenteImportiCapitolo> findComponenteImportiCapitoloByUidImportoCapitolo(Integer uid, ComponenteImportiCapitoloModelDetail... modelDetails) {
		List<SiacTBilElemDetComp> siacTBilElemDetComps = siacTBilElemDetCompRepository.findByElemDetId(uid);
		return convertiLista(siacTBilElemDetComps, ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	/**
	 * Ricerca delle componenti importi capitolo per capitolo e anno
	 * @param uidCapitolo l'uid del capitolo
	 * @param anno l'anno
	 * @param modelDetails i model details
	 * @return le componenti importi capitolo
	 */
	public List<ComponenteImportiCapitolo> findComponenteImportiCapitoloByUidCapitoloAnno(Integer uidCapitolo, Integer anno, ComponenteImportiCapitoloModelDetail... modelDetails) {
		List<SiacTBilElemDetComp> siacTBilElemDetComps = siacTBilElemDetCompRepository.findByElemIdAndAnno(uidCapitolo, anno.toString());
		return convertiLista(siacTBilElemDetComps, ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	/**
	 * Ricerca delle componenti importi capitolo per capitolo e anno
	 * @param uidCapitolo l'uid del capitolo
	 * @param modelDetails i model details
	 * @return le componenti importi capitolo
	 */
	public List<ComponenteImportiCapitolo> findComponenteImportiCapitoloByUidCapitolo(Integer uidCapitolo, ComponenteImportiCapitoloModelDetail... modelDetails) {
		List<SiacTBilElemDetComp> siacTBilElemDetComps = siacTBilElemDetCompRepository.findByElemId(uidCapitolo);
		return convertiLista(siacTBilElemDetComps, ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	/**
	 * Ricerca delle componenti importi capitolo per capitolo e anno
	 * @param uidCapitolo l'uid del capitolo
	 * @param uidVariazione l'uid della variazione
	 * @param modelDetails i model details
	 * @return le componenti importi capitolo
	 */
	public List<ComponenteImportiCapitolo> findComponenteImportiCapitoloByUidCapitoloVariazione(Integer uidCapitolo, Integer uidVariazione, ComponenteImportiCapitoloModelDetail... modelDetails) {
		List<SiacTBilElemDetComp> siacTBilElemDetComps = siacTBilElemDetCompRepository.findByElemIdAndVariazioneId(uidCapitolo, uidVariazione);
		return convertiLista(siacTBilElemDetComps, ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	/**
	 * Ricerca delle componenti importi capitolo per capitolo e anno
	 * @param uidCapitolo l'uid del capitolo
	 * @param anno l'anno
	 * @param uidTipoComponente l'uid del tipo componente
	 * @return le componenti importi capitolo
	 */
	public List<ComponenteImportiCapitolo> findComponenteImportiCapitoloByUidCapitoloAnnoTipoComponente(Integer uidCapitolo, Integer anno, Integer uidTipoComponente, ComponenteImportiCapitoloModelDetail... modelDetails) {
		List<SiacTBilElemDetComp> siacTBilElemDetComps = siacTBilElemDetCompRepository.findByElemIdAndAnnoAndElemDetCompTipoId(uidCapitolo, anno.toString(), uidTipoComponente);
		return convertiLista(siacTBilElemDetComps, ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	/**
	 * Builds the SiacTBilElemDetComp.
	 *
	 * @param componenteImportiCapitolo the componente importi capitolo
	 * @return the siac t bil elem det comp
	 */
	private SiacTBilElemDetComp buildSiacTBilElemDetComp(ComponenteImportiCapitolo componenteImportiCapitolo) {
		SiacTBilElemDetComp siacTBilElemDetComp = new SiacTBilElemDetComp();
		
		siacTBilElemDetComp.setLoginOperazione(loginOperazione);
		siacTBilElemDetComp.setSiacTEnteProprietario(siacTEnteProprietario);
		componenteImportiCapitolo.setLoginOperazione(loginOperazione);
		
		map(componenteImportiCapitolo, siacTBilElemDetComp, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo);
		return siacTBilElemDetComp;
	}
	/**
	 * Annullamento di una componente capitolo
	 * @param cic la componente da annullare
	 */
	public void annullaComponenteImportiCapitolo(ComponenteImportiCapitolo cic) {
		SiacTBilElemDetComp siacTBilElemDetComp = componenteImportiCapitoloDao.findById(cic.getUid());
		siacTBilElemDetComp.setDataCancellazioneIfNotSet(new Date());
	}
	
	/**
	 * Conta le componenti collegate a un dato capitolo di dato tipo
	 * @param uidCapitolo l'uid del capitolo
	 * @param uidTipoComponenteImportiCapitolo l'uid del tipo
	 * @return il numero di record collegati
	 */
	public Long countByCapitoloAndTipoComponenteImportiCapitolo(Integer uidCapitolo, Integer uidTipoComponenteImportiCapitolo) {
		return siacTBilElemDetCompRepository.countByElemIdAndElemDetCompTipoId(uidCapitolo, uidTipoComponenteImportiCapitolo);
	}
	
	/**
	 * Conta le variazioni afferenti alla componente
	 * @param uidComponente l'uid della componente
	 * @return il numero di variazioni collegati
	 */
	public Long countVariazioniByComponente(Integer uidComponente) {
		return siacTBilElemDetCompRepository.countVariazioniByElemDetCompId(uidComponente);
	}
	
	/**
	 * Calcolo del totale delle componenti
	 * @param bilId l'anno di bilancio
	 * @param annoEsercizio l'anno di esercizio
	 * @return le componenti
	 */
	public List<ComponenteImportiCapitolo> totaleComponenti(Integer bilId, int annoEsercizio) {
		List<ComponenteImportiCapitolo> res = new ArrayList<ComponenteImportiCapitolo>();
		List<Object[]> siacDBilElemDetCompTipos = siacTBilElemDetCompRepository.sumBySiacDBilElemDetCompTipo(bilId, Integer.toString(annoEsercizio));
		// Transform to entity manually and then to object via mapper
		for(Object[] objArray : siacDBilElemDetCompTipos) {
			SiacDBilElemDetCompTipo sdbedct = (SiacDBilElemDetCompTipo) objArray[0];
			BigDecimal importo = (BigDecimal) objArray[1];
			SiacTBilElemDetComp siacTBilElemDetComp = new SiacTBilElemDetComp();
			siacTBilElemDetComp.setSiacDBilElemDetCompTipo(sdbedct);
			siacTBilElemDetComp.setElemDetImporto(importo);
			res.add(map(siacTBilElemDetComp, ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo));
		}
		
		return res;
	}
	/**
	 * Conta le componenti di un dato tipo
	 * @param uidTipoComponenteImportiCapitolo l'uid del tipo
	 * @return il numero di record collegati
	 */
	public Long countByTipoComponenteImportiCapitolo(Integer uidTipoComponenteImportiCapitolo) {
		return siacTBilElemDetCompRepository.countByElemDetCompTipoId(uidTipoComponenteImportiCapitolo);
	}
	/**
	 * Annullamento dell'intera riga relativa alla componente importi capitolo
	 * @param componenteImportiCapitolo la componente la cui riga e' da annullare
	 */
	public void annullaRigaComponenteImportiCapitolo(ComponenteImportiCapitolo componenteImportiCapitolo) {
		Date now = new Date();
		List<SiacTBilElemDetComp> siacTBilElemDetComps = siacTBilElemDetCompRepository.findRowByElemDetCompId(componenteImportiCapitolo.getUid());
		for(SiacTBilElemDetComp stbedc : siacTBilElemDetComps) {
			stbedc.setDataCancellazioneIfNotSet(now);
		}
	}
	
	/**
	 * Carica componente associata.
	 *
	 * @param capitolo the capitolo
	 * @param componente the componente
	 * @return the componente importi capitolo
	 */ 
	public ComponenteImportiCapitolo caricaComponenteConStessoTipoCollegataAlCapitoloEAVariazioneDefONoVariazione(Capitolo<?, ?> capitolo, ComponenteImportiCapitolo componente) {
		List<SiacTBilElemDetComp> siacComps = siacTBilElemDetCompRepository.findComponenteOnElemIdWithSameTipo(capitolo.getUid(), componente.getUid());
		if(siacComps == null || siacComps.isEmpty() || siacComps.get(0) == null) {
			return null;
		}
		
		if(siacComps.size() > 1 ) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Sono presenti piu' record definitivi per la componente " + componente.getUid() +" del capitolo " + capitolo.getUid( ) + "su base dati."));
		}
		return mapNotNull(siacComps.get(0), ComponenteImportiCapitolo.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo);
	}
	
	/**
	 * Checks for more than one componente associata.
	 *
	 * @param cp the cp
	 * @param tc the tc
	 * @return true, if successful
	 */
	public boolean hasComponentiDefinitiveEProvvisorieAssociate(Capitolo<?, ?> cp, TipoComponenteImportiCapitolo tc) {
		Long count = siacTBilElemDetCompRepository.countSiacTBilElemDetCompByElemIdAndDetCompTipoId(cp.getUid(), tc.getUid());
		return count != null && count.intValue() > NUM_ELEM_DET_COMP_DEF_STESSO_TIPO_ASSOCIABILI_A_CAPITOLO.intValue();
		
	}
	
	/**
	 * Sposta il dettaglio di una variazione della compoente dalla componente ad esso associata ad una nuova componente
	 *
	 * @param dettaglio the dettaglio
	 * @param componenteNuova the componente
	 */
	//SIAC-7688
	public void spostaDettaglioVariazioneSuComponente(DettaglioVariazioneComponenteImportoCapitolo dettaglio, ComponenteImportiCapitolo componenteNuova) {
		SiacTBilElemDetVarComp siacTBilElemDetVarComp =  siacTBilElemDetVarCompRepository.findOne(dettaglio.getUid());
		SiacTBilElemDetComp siacTBilElemDetComp = buildSiacTBilElemDetComp(componenteNuova);
		
		siacTBilElemDetVarComp.setSiacTBilElemDetComp(siacTBilElemDetComp);
		siacTBilElemDetVarCompRepository.saveAndFlush(siacTBilElemDetVarComp);
		
		SiacTBilElemDetComp siacTBilElemDetCompOld = siacTBilElemDetCompRepository.findOne(dettaglio.getComponenteImportiCapitolo().getUid());
		siacTBilElemDetCompOld.setDataCancellazioneIfNotSet(new Date());
		siacTBilElemDetCompOld.setLoginOperazione(loginOperazione);
		
		dettaglio.setComponenteImportiCapitolo(componenteNuova);
	}
	
}
