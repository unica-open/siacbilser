/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTBilRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDFaseOperativa;
import it.csi.siac.siacbilser.integration.entity.SiacRBilAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFaseOperativaEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacfin2ser.model.Periodo;

/**
 * The Class BilancioDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class BilancioDad extends BaseDadImpl  {
	
	/** The ente entity. */
	private SiacTEnteProprietario enteEntity;
	
	/** The siac t bil repository. */
	@Autowired
	private SiacTBilRepository siacTBilRepository;
		
	/** The siac t periodo repository. */
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
    /**
     * Sets the ente entity.
     *
     * @param ente the new ente entity
     */
    public void setEnteEntity(Ente ente) {
    	this.enteEntity = map(ente, SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
	}

	/**
	 * Controlla se il bilancio e' in fase esercizio provvisorio.
	 *
	 * @param anno l'anno di bilancio
	 * @return true, se la fase e' esercizio provvisiorio
	 */
	public boolean isFaseEsercizioProvvisiorio(int anno) {
		SiacTPeriodo periodoEntity = siacTPeriodoRepository.findByAnnoAndEnteProprietario(Integer.toString(anno), enteEntity);
		SiacDFaseOperativa fase = siacTBilRepository.getFase(periodoEntity, enteEntity);
		if (fase == null){
			return false;
		} 
		return SiacDFaseOperativaEnum.EsercizioProvvisorio.getCodice().equals(fase.getFaseOperativaCode());
	}
	
	
	/**
	 * Ricerca un bilancio tramite il suo uid.
	 *
	 * @param uid l'uid del bilancio da cercare
	 * 
	 * @return li bilancio trovato
	 */
	public Bilancio getBilancioByUid(Integer uid) {
		final String methodName = "getBilancioByUid";
		
		log.debug(methodName, "bilancio.uid: "+ uid);
		
		SiacTBil siacTBil = siacTBilRepository.findOne(uid);
		
		return mapNotNull(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);
		
	}
	
	
	/**
	 * Gets the bilancio by anno and fase.
	 *
	 * @param anno the anno
	 * @param faseBilancio the fase bilancio
	 * @return the bilancio by anno and fase
	 */
	public Bilancio getBilancioByAnnoAndFase(String anno, FaseBilancio faseBilancio) {
		final String methodName = "getBilancioByAnnoAndFase";
	
		log.debug(methodName, "anno: "+ anno + " faseBilancio:"+faseBilancio +" ente:"+enteEntity.getUid());
		//log.debug(methodName," faseOperativa :"+SiacDFaseOperativaEnum.byFaseBilancio(faseBilancio).getCodice() +" periodo bilancio:"+Periodo.ANNO.getCodice());

		//quarto parametro e' per anno solare ----->SY
		SiacTBil siacTBil = siacTBilRepository.getSiacTBilByAnnoAndFase(anno, enteEntity.getUid(), SiacDFaseOperativaEnum.byFaseBilancio(faseBilancio).getCodice(),Periodo.ANNO.getCodice());
		
		return mapNotNull(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);
		
	}
	
	/**
	 * Gets the bilancio by anno.
	 *
	 * @param anno the anno
	 * @return the bilancio by anno
	 */
	public Bilancio getBilancioByAnno(int anno) {
		final String methodName = "getBilancioByAnno";
		
		log.debug(methodName, "anno: "+ anno);
		
		SiacTBil siacTBil = siacTBilRepository.getSiacTBilByAnno("" + anno, enteEntity.getUid(), Periodo.ANNO.getCodice());
		
		return mapNotNull(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);
	}

	/**
	 * Gets the bilancio drom ente.
	 * trova l'anno dell'ultimo anno di  bilancio inserito in base all'ente e con fase operativa != PREVISIONE
	 * @param ente
	 * @return the bilancio by ente
	 */
	public String getAnnoBilancioByEnteAndFaseCodeNonPrevisione(Ente ente) {
		final String methodName = "getBilancioByEnte";

		log.debug(methodName, "Uid Ente " + ente.getUid());

		String annoBilancio  = siacTBilRepository.getMaxAnnoSiacTBilByEnteAndNotFaseOperativaCode(ente.getUid(),SiacDFaseOperativaEnum.Previsione.getCodice());

		return annoBilancio;
	}
	
	/**
	 * Gets the bilancio drom ente.
	 * trova l'anno dell'ultimo anno di  bilancio inserito in base all'ente e con fase operativa != PREVISIONE
	 * @param ente
	 * @return the bilancio by ente
	 */
	public Bilancio getBilancioByEnteAndFaseCodeNonPrevisione(Ente ente) {
		final String methodName = "getBilancioByEnte";

		log.debug(methodName, "Uid Ente " + ente.getUid());

		List<SiacTBil> siacTBils  = siacTBilRepository.getSiacTBilByEnteAndNotFaseOperativaCode(ente.getUid(),SiacDFaseOperativaEnum.Previsione.getCodice());
        
		if(!siacTBils.isEmpty()){
			return mapNotNull(siacTBils.get(0), Bilancio.class, BilMapId.SiacTBil_Bilancio);
		}

		return null;
	}
	

	/**
	 * Ottiene l'anno associato ad un Bilancio dato il suo uid.
	 * 
	 * @param bilId
	 * @return anno del bilancio
	 */
	public Integer getAnnoAssociatoABilancioId(Integer bilId) {
		final String methodName = "getAnnoByBilancioId";
		
		SiacTBil siacTBil = siacTBilRepository.findOne(bilId);
		try{
			Integer annoBilancio = Integer.parseInt(siacTBil.getSiacTPeriodo().getAnno());
			log.debug(methodName, "returning "+ annoBilancio);
			return annoBilancio;
		} catch(NullPointerException npe){
			throw new IllegalArgumentException("Impossibile determinare l'anno di bilancio assocato al Bilancio con uid:"+bilId + ". Controllare configurazione su DB.", npe);
		} catch(NumberFormatException nfe){
			throw new IllegalStateException("Impossibile determinare l'anno di bilancio assocato al Bilancio con uid:"+bilId + " (Formato non numerico). Controllare configurazione su DB.", nfe);
		}
	}
	
	/**
	 * Ottieni gli attributi del bilancio a partire dallo stesso
	 * @param bilancio il bilancio
	 * @return gli attributi
	 */
	public AttributiBilancio getAttributiDettaglioByBilancio(Bilancio bilancio) {
		SiacTBil siacTBil = siacTBilRepository.findOne(bilancio.getUid());
		if(siacTBil == null) {
			throw new IllegalArgumentException("Impossibile reperire il bilancio con uid: " + bilancio.getUid());
		}
		//todo
		boolean hasAttrs = false;
		if(siacTBil.getSiacRBilAttrs() != null) {
			for(Iterator<SiacRBilAttr> it = siacTBil.getSiacRBilAttrs().iterator(); it.hasNext() && !hasAttrs;) {
				SiacRBilAttr rba = it.next();
				hasAttrs = hasAttrs || rba != null;
			}
		}
		if(!hasAttrs){
			return null;
		}
		return mapNotNull(siacTBil, AttributiBilancio.class, BilMapId.SiacTBil_AttributiBilancio);
	}
	
	/**
	 * Ottieni gli attributi del bilancio a partire dallo stesso
	 * @param bilancio il bilancio
	 * @param attributiBilancio gli attributi da popolare
	 * @return gli attributi
	 */
	public AttributiBilancio updateAttributiDettaglioByBilancio(Bilancio bilancio, AttributiBilancio attributiBilancio) {
		SiacTBil siacTBil = siacTBilRepository.findOne(bilancio.getUid());
		if(siacTBil == null) {
			throw new IllegalArgumentException("Impossibile reperire il bilancio con uid: " + bilancio.getUid());
		}
		Date now = new Date();
		if(siacTBil.getSiacRBilAttrs() != null) {
			for(SiacRBilAttr rba : siacTBil.getSiacRBilAttrs()) {
				rba.setDataCancellazioneIfNotSet(now);
			}
		}
		siacTBilRepository.save(siacTBil);
		mapNotNull(attributiBilancio, siacTBil, BilMapId.SiacTBil_AttributiBilancio);
		if(siacTBil.getSiacRBilAttrs() != null) {
			for(SiacRBilAttr rba : siacTBil.getSiacRBilAttrs()) {
				rba.setDataModificaInserimento(now);
			}
		}
		siacTBilRepository.save(siacTBil);
		
		return attributiBilancio;
	}
	
	/**
	 * Ottiene il bilancio per l'anno
	 * @param bilancio
	 * @return
	 */
	public Bilancio getBilancioAnnoPrecedente(Bilancio bilancio) {
		List<SiacTBil> siacTBils = siacTBilRepository.findByBilIdAndDeltaAnno(bilancio.getUid(), -1);
		if(siacTBils == null || siacTBils.isEmpty()) {
			throw new IllegalArgumentException("Impossibile reperire il bilancio con anno -1 rispetto al bilancio con uid: " + bilancio.getUid());
		}
		
		return mapNotNull(siacTBils.get(0), Bilancio.class, BilMapId.SiacTBil_Bilancio);
	}
	
	//SIAC-5937
	/**
	 * Checks if is bilancio doppia gestione.
	 *
	 * @param bilancio the bilancio
	 * @return true, if is bilancio doppia gestione
	 */
	public boolean isBilancioDoppiaGestione(Bilancio bilancio) {
		SiacDFaseOperativa faseAttuale = siacTBilRepository.getFaseById(bilancio.getUid());
		boolean faseBilancioAttualeCongruenteDoppiaGestione = faseAttuale != null &&  FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(SiacDFaseOperativaEnum.byCodice(faseAttuale.getFaseOperativaCode()).getFaseBilancio());
		List<SiacTBil> siacTBilsSuccessivi = siacTBilRepository.findByBilIdAndDeltaAnno(bilancio.getUid(), 1);
		if(!faseBilancioAttualeCongruenteDoppiaGestione || siacTBilsSuccessivi == null || siacTBilsSuccessivi.size() != 1) {
			return false;
		}
		SiacDFaseOperativa faseBilancioSuccessivo = siacTBilRepository.getFaseById(siacTBilsSuccessivi.get(0).getUid());
		return faseBilancioSuccessivo != null &&  FaseBilancio.ESERCIZIO_PROVVISORIO.equals(SiacDFaseOperativaEnum.byCodice(faseBilancioSuccessivo.getFaseOperativaCode()).getFaseBilancio());
	}
	
	/**
	 * Ottiene il bilancio per l'anno
	 * @param bilancio
	 * @return
	 */
	public Bilancio getBilancioAnnoSuccessivo(Bilancio bilancio) {
		List<SiacTBil> siacTBils = siacTBilRepository.findByBilIdAndDeltaAnno(bilancio.getUid(), +1);
		if(siacTBils == null || siacTBils.isEmpty()) {			
			return null;
		}		
		return mapNotNull(siacTBils.get(0), Bilancio.class, BilMapId.SiacTBil_Bilancio);
	}
}
