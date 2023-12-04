/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacRBilElemClassVarRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClassVar;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemVar;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;

/**
 * The Class VariazioneCodificheCapitoloConverter.
 * 
 * @author Domenico
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DettaglioVariazioneCodificaCapitoloAllConverter extends ExtendedDozerConverter<DettaglioVariazioneCodificaCapitolo, SiacTBilElemVar> {
	
	/** The siac t class dao. */
	@Autowired
	private SiacTClassDao siacTClassDao;
	
	@Autowired
	private SiacRBilElemClassVarRepository siacRBilElemClassVarRepository;

	/**
	 * Instantiates a new variazione codifiche capitolo converter.
	 */
	public DettaglioVariazioneCodificaCapitoloAllConverter() {
		
		super(DettaglioVariazioneCodificaCapitolo.class, SiacTBilElemVar.class );
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioVariazioneCodificaCapitolo convertFrom(SiacTBilElemVar src, DettaglioVariazioneCodificaCapitolo dest) {
		
		if (src.getSiacRVariazioneStato().getDataCancellazione() != null) {
			return dest;
		}
		
//		DettaglioVariazioneCodificaCapitolo dettVc = new DettaglioVariazioneCodificaCapitolo();
		popolaCapitoloVariazione(dest, src);
		
		List<SiacRBilElemClassVar> siacRBilElemClassVars = siacRBilElemClassVarRepository.findSiacRBilElemClassVarsBySiacBilElemId(src.getSiacTBilElem().getUid(), src.getSiacRVariazioneStato().getUid());
		
		for (SiacRBilElemClassVar siacRBilElemClassVar : siacRBilElemClassVars) {

			caricaClassificatori(siacRBilElemClassVar, dest);
			caricaClassificatoriPrecedenti(siacRBilElemClassVar, dest);
			
		}

		return dest;
	}
	
	private void caricaClassificatori(SiacRBilElemClassVar siacRBilElemClassVar, DettaglioVariazioneCodificaCapitolo dettVc){
		SiacTClass siacTClass = siacRBilElemClassVar.getSiacTClass();
		SiacDClassTipoEnum classTipo = SiacDClassTipoEnum.byCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
		
		Codifica classificatore = classTipo.getCodificaInstance(); // Come se facesse ad esempio:  ElementoPianoDeiConti classificatore  = new ElementoPianoDeiConti();
		if (classificatore instanceof ClassificatoreGerarchico) {
			
			Map<String, SiacTClass> classificatoriNellaGerarchia = ricercaClassificatoriNellaGerarchia(siacTClass);	
			for(Entry<String, SiacTClass> entry : classificatoriNellaGerarchia.entrySet()){
				String classifTipoCode = entry.getKey();
				SiacTClass siacTClassAll = entry.getValue();								
				SiacDClassTipoEnum classTipoG = SiacDClassTipoEnum.byCodice(classifTipoCode);
				Codifica classificatoreG = classTipoG.getCodificaInstance();
				map(siacTClassAll, classificatoreG, BilMapId.SiacTClass_ClassificatoreGerarchico);
				classificatoreG.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
				dettVc.getClassificatoriGerarchici().add((ClassificatoreGerarchico)classificatoreG);
			}
			
			
		} else {
			map(siacTClass, classificatore, BilMapId.SiacTClass_ClassificatoreGenerico);
			classificatore.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
			dettVc.getClassificatoriGenerici().add((ClassificatoreGenerico)classificatore);
		}
	}

	private void caricaClassificatoriPrecedenti(SiacRBilElemClassVar siacRBilElemClassVar, DettaglioVariazioneCodificaCapitolo dettVc) {
		SiacTClass siacTClassPrec = siacRBilElemClassVar.getSiacTClassPrec();
		SiacDClassTipoEnum classTipoPrec = SiacDClassTipoEnum.byCodice(siacTClassPrec.getSiacDClassTipo().getClassifTipoCode());
		
		Codifica classificatorePrec = classTipoPrec.getCodificaInstance(); // Come se facesse ad esempio:  ElementoPianoDeiConti classificatore  = new ElementoPianoDeiConti();
		if (classificatorePrec instanceof ClassificatoreGerarchico) {
			
			Map<String, SiacTClass> classificatoriNellaGerarchia = ricercaClassificatoriNellaGerarchia(siacTClassPrec);	
			for(Entry<String, SiacTClass> entry : classificatoriNellaGerarchia.entrySet()){
				String classifTipoCode = entry.getKey();
				SiacTClass siacTClassAll = entry.getValue();								
				SiacDClassTipoEnum classTipoG = SiacDClassTipoEnum.byCodice(classifTipoCode);
				Codifica classificatoreG = classTipoG.getCodificaInstance();
				map(siacTClassAll, classificatoreG, BilMapId.SiacTClass_ClassificatoreGerarchico);
				//classificatoreG.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
				dettVc.getClassificatoriGerarchiciPrecedenti().add((ClassificatoreGerarchico)classificatoreG);
			}
			
			
		} else {
			map(siacTClassPrec, classificatorePrec, BilMapId.SiacTClass_ClassificatoreGenerico);
			//classificatorePrec.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
			dettVc.getClassificatoriGenericiPrecedenti().add((ClassificatoreGenerico)classificatorePrec);
		}
		
	}

	/**
	 * Ricerca un classificatore gerarchico restituendo tutti i suoi padri in una mappa.
	 *
	 * @param siacTClass the siac t class
	 * @return the map
	 */
	private Map<String, SiacTClass> ricercaClassificatoriNellaGerarchia(SiacTClass siacTClass) {
		final String methodName = "ricercaClassificatoriNellaGerarchia";
		Map<String, SiacTClass> result = new HashMap<String, SiacTClass>();
		siacTClassDao.ricercaClassifRicorsivaByClassifId(result,siacTClass);		
		log.debug(methodName, "result: "+ result);
		return result;
	}



	/**
	 * Trova il capitolo associato alla variazione, se presente, con i dati da variare. 
	 * Altrimenti con i dati originali.
	 * @param dettVc 
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 */
	private void popolaCapitoloVariazione(DettaglioVariazioneCodificaCapitolo dettVc, SiacTBilElemVar siacTBilElemVar) {
		Capitolo<?,?> capitolo = mapToCapitolo(siacTBilElemVar);
		dettVc.setCapitolo(capitolo);
		
		Capitolo<?,?> capitoloPrec = mapToCapitoloPrec(siacTBilElemVar);
		dettVc.setCapitoloPrecedente(capitoloPrec);	
	}
	
	/**
	 * Map to capitolo.
	 *
	 * @param siacTBilElemVar the siac t bil elem var
	 * @return the capitolo
	 */
	private Capitolo<?,?> mapToCapitolo(SiacTBilElemVar siacTBilElemVar) {
		if(siacTBilElemVar==null){
			return null;
		}
		
		SiacTBilElem siacTBilElem = siacTBilElemVar.getSiacTBilElem();
		Capitolo<?,?> capitolo = mapToCapitolo(siacTBilElem);
		
		capitolo.setDescrizione(siacTBilElemVar.getElemDesc());
		capitolo.setDescrizioneArticolo(siacTBilElemVar.getElemDesc2());
		
		return capitolo;
		
	}
	
	/**
	 * Map to capitolo.
	 *
	 * @param siacTBilElemVar the siac t bil elem var
	 * @return the capitolo
	 */
	private Capitolo<?,?> mapToCapitoloPrec(SiacTBilElemVar siacTBilElemVar) {
		if(siacTBilElemVar==null){
			return null;
		}
		
		SiacTBilElem siacTBilElem = siacTBilElemVar.getSiacTBilElem();
		Capitolo<?,?> capitolo = mapToCapitolo(siacTBilElem);
		
		capitolo.setDescrizione(siacTBilElemVar.getElemDescPrec());
		capitolo.setDescrizioneArticolo(siacTBilElemVar.getElemDesc2Prec());
		
		return capitolo;
		
	}

	/**
	 * Map to capitolo.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @return the capitolo
	 */
	private Capitolo<?,?> mapToCapitolo(SiacTBilElem siacTBilElem) {
		if(siacTBilElem==null){
			return null;
		}
		
		@SuppressWarnings("rawtypes")
		Capitolo<?,?> capitolo = new Capitolo();
		map(siacTBilElem, capitolo, BilMapId.SiacTBilElem_Capitolo_Base);
		TipoCapitolo tipoCapitolo = SiacDBilElemTipoEnum.byCodice(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode()).getTipoCapitolo();
		capitolo.setTipoCapitolo(tipoCapitolo);
		
		return capitolo;
	}

	@Override
	public SiacTBilElemVar convertTo(DettaglioVariazioneCodificaCapitolo src, SiacTBilElemVar dest) {
		return dest;
	}
	
	
	
	

	


}
