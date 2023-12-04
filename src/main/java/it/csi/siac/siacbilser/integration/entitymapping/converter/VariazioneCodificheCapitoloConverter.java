/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTVariazioneRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClassVar;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemVar;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
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
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VariazioneCodificheCapitoloConverter extends
		ExtendedDozerConverter<List<DettaglioVariazioneCodificaCapitolo>, SiacTVariazione> {
	
	//Components..
	@Autowired
	private SiacTClassDao siacTClassDao;
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	@Autowired
	private SiacTVariazioneRepository siacTVariazioneRepository;
	
	//Fields..
	private Map<Integer, ClassificatoreGenerico> cacheClassificatoriGenerici = new HashMap<Integer, ClassificatoreGenerico>();
	private Map<Integer, List<ClassificatoreGerarchico>> cacheClassificatoriGerarchici = new HashMap<Integer, List<ClassificatoreGerarchico>>();

	
	
	/**
	 * Instantiates a new variazione codifiche capitolo converter.
	 */
	@SuppressWarnings("unchecked")
	public VariazioneCodificheCapitoloConverter() {
		
		super((Class<List<DettaglioVariazioneCodificaCapitolo>>) (Class<?>) List.class, 
				SiacTVariazione.class );
		
		this.cacheClassificatoriGenerici = new HashMap<Integer, ClassificatoreGenerico>();
		this.cacheClassificatoriGerarchici = new HashMap<Integer, List<ClassificatoreGerarchico>>();
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public List<DettaglioVariazioneCodificaCapitolo> convertFrom(SiacTVariazione src, List<DettaglioVariazioneCodificaCapitolo> dest) {
		dest = new ArrayList<DettaglioVariazioneCodificaCapitolo>();

		for (SiacRVariazioneStato siacRVariazioneStato : src.getSiacRVariazioneStatos()) {

			if (siacRVariazioneStato.getDataCancellazione() != null) {
				continue;
			}
			
			List<SiacRBilElemClassVar> findSiacRBilElemClassVarByVariazioneStatoId = siacTVariazioneRepository.findSiacRBilElemClassVarByVariazioneStatoId(siacRVariazioneStato.getVariazioneStatoId());
			
			
			for (SiacRBilElemClassVar siacRBilElemClassVar : findSiacRBilElemClassVarByVariazioneStatoId /*siacRVariazioneStato.getSiacRBilElemClassVars()*/) {
				
//				if (siacRBilElemClassVar.getDataCancellazione() != null) {
//					continue;
//				}
							
				DettaglioVariazioneCodificaCapitolo dettVc = byCapitolo(dest, siacRBilElemClassVar);
				
				caricaClassificatori(siacRBilElemClassVar, dettVc);
				caricaClassificatoriPrecedenti(siacRBilElemClassVar, dettVc);
				
			}
			
			break;

		}
		
		//log.debug(methodName, "elapsed: "+ (System.currentTimeMillis()- time) + " di cui passati a caricare classificatori: "+timeCaricaClassificatori +" e per caricare capitolo:"+timeCaricaCapitolo + " find: "+timeFindSiacRBilElemClassVar);

		return dest;
	}
	
//	private void caricaClassificatori(SiacRBilElemClassVar siacRBilElemClassVar, DettaglioVariazioneCodificaCapitolo dettVc){
//		final String methodName = "caricaClassificatori";
//		
//		long time = System.currentTimeMillis();
//		SiacTClass siacTClass = siacRBilElemClassVar.getSiacTClass();
//		String desc = "siacTClass.uid="+siacTClass.getUid() + " capitolo.uid: "+ dettVc.getCapitolo().getUid()+ " dettVc.uid:"+dettVc.getUid();
//		log.debug(methodName, desc );
//		SiacDClassTipoEnum classTipo = SiacDClassTipoEnum.byCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
//		
//		Codifica classificatore = classTipo.getCodificaInstance(); // Come se facesse ad esempio:  ElementoPianoDeiConti classificatore  = new ElementoPianoDeiConti();
//		if (classificatore instanceof ClassificatoreGerarchico) {
//			
//			Map<String, SiacTClass> classificatoriNellaGerarchia = ricercaClassificatoriNellaGerarchia(siacTClass);	
//			for(Entry<String, SiacTClass> entry : classificatoriNellaGerarchia.entrySet()){
//				String classifTipoCode = entry.getKey();
//				SiacTClass siacTClassAll = entry.getValue();								
//				SiacDClassTipoEnum classTipoG = SiacDClassTipoEnum.byCodice(classifTipoCode);
//				Codifica classificatoreG = classTipoG.getCodificaInstance();
//				map(siacTClassAll, classificatoreG, BilMapId.SiacTClass_ClassificatoreGerarchico);
//				classificatoreG.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
//				dettVc.getClassificatoriGerarchici().add((ClassificatoreGerarchico)classificatoreG);
//			}
//			
//			
//		} else {
//			map(siacTClass, classificatore, BilMapId.SiacTClass_ClassificatoreGenerico);
//			classificatore.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
//			dettVc.getClassificatoriGenerici().add((ClassificatoreGenerico)classificatore);
//		}
//		
//		
//		log.debug(methodName, desc+ ". Elapsed millis: "+ (System.currentTimeMillis()-time));
//	}
	
	
	private void caricaClassificatori(SiacRBilElemClassVar siacRBilElemClassVar, DettaglioVariazioneCodificaCapitolo dettVc){
		SiacTClass siacTClass = siacRBilElemClassVar.getSiacTClass();
		Date dataFineValidita = siacRBilElemClassVar.getDataFineValidita();
		caricaClassificatori(dettVc, siacTClass, dataFineValidita);
	}

	private void caricaClassificatori(DettaglioVariazioneCodificaCapitolo dettVc, SiacTClass siacTClass, Date dataFineValidita) {
		
		final String methodName = "caricaClassificatori";
		
		long time = System.currentTimeMillis();
		String desc = "siacTClass.uid="+siacTClass.getUid() + " capitolo.uid: "+ dettVc.getCapitolo().getUid()+ " dettVc.uid:"+dettVc.getUid();
		log.debug(methodName, desc );
		
		SiacDClassTipoEnum classTipo = SiacDClassTipoEnum.byCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
		
		if (classTipo.isGerarchico()) {
			List<ClassificatoreGerarchico> classificatoriGerarchici = caricaClassificatoreGerarchico(siacTClass);
			classificatoriGerarchici = cloneList(classificatoriGerarchici);
			for (ClassificatoreGerarchico cg : classificatoriGerarchici) {
				cg.setDataFineValidita(dataFineValidita);
			}
			dettVc.getClassificatoriGerarchici().addAll(classificatoriGerarchici);
			
		} else {
			ClassificatoreGenerico classificatoreGenerico = caricaClassificatoreGenerico(siacTClass);
			classificatoreGenerico = clone(classificatoreGenerico);
			classificatoreGenerico.setDataFineValidita(dataFineValidita);
			dettVc.getClassificatoriGenerici().add(classificatoreGenerico);
		}
		
		log.debug(methodName, desc+ ". Elapsed millis: "+ (System.currentTimeMillis()-time));
	}
	
	
	
	private ClassificatoreGenerico caricaClassificatoreGenerico(SiacTClass siacTClass) {
		final String methodName = "caricaClassificatoreGenerico";
		
		Integer key = siacTClass.getUid();
		cacheClassificatoriGenerici.clear();
		if(cacheClassificatoriGenerici.containsKey(key)){
			ClassificatoreGenerico resultFromCache = cacheClassificatoriGenerici.get(key);
			log.debug(methodName, "result from cache for siacTClass.uid: "+ key);
			return resultFromCache;
		}
		
		SiacDClassTipoEnum classTipo = SiacDClassTipoEnum.byCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
		Codifica classificatore = classTipo.getCodificaInstance(); // Come se facesse ad esempio:  ElementoPianoDeiConti classificatore  = new ElementoPianoDeiConti();
		ClassificatoreGenerico result = (ClassificatoreGenerico) classificatore;
		map(siacTClass, classificatore, BilMapId.SiacTClass_ClassificatoreGenerico);
		
		
		cacheClassificatoriGenerici.put(key, result);
		log.debug(methodName, "result loaded from DB for siacTClass.uid: "+ key);
		return result;
		
	}
	
	

	private List<ClassificatoreGerarchico> caricaClassificatoreGerarchico (SiacTClass siacTClass) {
		final String methodName = "caricaClassificatoreGerarchico";
		
		Integer key = siacTClass.getUid();
		cacheClassificatoriGerarchici.clear();
		if(cacheClassificatoriGerarchici.containsKey(key)){
			List<ClassificatoreGerarchico> resultFromCache = cacheClassificatoriGerarchici.get(key);
			log.debug(methodName, "result from cache for siacTClass.uid: "+ key);
			return resultFromCache;
		}
		
		
		List<ClassificatoreGerarchico> result = new ArrayList<ClassificatoreGerarchico>();
		Map<String, SiacTClass> classificatoriNellaGerarchia = ricercaClassificatoriNellaGerarchia(siacTClass);	
		for(Entry<String, SiacTClass> entry : classificatoriNellaGerarchia.entrySet()){
			String classifTipoCode = entry.getKey();
			SiacTClass siacTClassAll = entry.getValue();								
			SiacDClassTipoEnum classTipoG = SiacDClassTipoEnum.byCodice(classifTipoCode);
			Codifica classificatoreG = classTipoG.getCodificaInstance();
			map(siacTClassAll, classificatoreG, BilMapId.SiacTClass_ClassificatoreGerarchico);
			result.add((ClassificatoreGerarchico)classificatoreG);
		}
		
		cacheClassificatoriGerarchici.put(key, result);
		log.debug(methodName, "result loaded from DB for siacTClass.uid: "+ key);
		return result;
	}
	
	
	private <T extends Serializable> T  clone(T cg){
		return SerializationUtils.clone(cg);
	}

	private <T extends Serializable> List<T> cloneList(List<T> l) {
		List<T> result = new ArrayList<T>();
		for (T cg : l) {
			result.add(clone(cg));
		}
		return result;
	}

//	private void caricaClassificatoriPrecedenti(SiacRBilElemClassVar siacRBilElemClassVar, DettaglioVariazioneCodificaCapitolo dettVc) {
//		String methodName = "caricaClassificatoriPrecedenti";
//		
//		SiacTClass siacTClassPrec = siacRBilElemClassVar.getSiacTClassPrec();
//		if(siacTClassPrec==null){
//			log.warn(methodName, "Classificatore precedente non impostato. Esco. Potrebbe essere una variazione salvata prima"
//					+ " della modifica di VariazioneCodificheCapitoloConverter per SIAC-3956 del 7/10/2016. ");
//			return;
//		}
//		
//		SiacDClassTipoEnum classTipoPrec = SiacDClassTipoEnum.byCodice(siacTClassPrec.getSiacDClassTipo().getClassifTipoCode());
//		
//		Codifica classificatorePrec = classTipoPrec.getCodificaInstance(); // Come se facesse ad esempio:  ElementoPianoDeiConti classificatore  = new ElementoPianoDeiConti();
//		if (classificatorePrec instanceof ClassificatoreGerarchico) {
//			
//			Map<String, SiacTClass> classificatoriNellaGerarchia = ricercaClassificatoriNellaGerarchia(siacTClassPrec);	
//			for(Entry<String, SiacTClass> entry : classificatoriNellaGerarchia.entrySet()){
//				String classifTipoCode = entry.getKey();
//				SiacTClass siacTClassAll = entry.getValue();								
//				SiacDClassTipoEnum classTipoG = SiacDClassTipoEnum.byCodice(classifTipoCode);
//				Codifica classificatoreG = classTipoG.getCodificaInstance();
//				map(siacTClassAll, classificatoreG, BilMapId.SiacTClass_ClassificatoreGerarchico);
//				//classificatoreG.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
//				dettVc.getClassificatoriGerarchiciPrecedenti().add((ClassificatoreGerarchico)classificatoreG);
//			}
//			
//			
//		} else {
//			map(siacTClassPrec, classificatorePrec, BilMapId.SiacTClass_ClassificatoreGenerico);
//			//classificatorePrec.setDataFineValidita(siacRBilElemClassVar.getDataFineValidita());
//			dettVc.getClassificatoriGenericiPrecedenti().add((ClassificatoreGenerico)classificatorePrec);
//		}
//		
//	}
	
	
	private void caricaClassificatoriPrecedenti(SiacRBilElemClassVar siacRBilElemClassVar, DettaglioVariazioneCodificaCapitolo dettVc){
		final String methodName = "caricaClassificatori";
		
		SiacTClass siacTClassPrec = siacRBilElemClassVar.getSiacTClassPrec();
		if(siacTClassPrec==null){
			log.warn(methodName, "Classificatore precedente non impostato. Esco. Potrebbe essere una variazione salvata prima"
					+ " della modifica di VariazioneCodificheCapitoloConverter per SIAC-3956 del 7/10/2016. ");
			return;
		}
		
		caricaClassificatoriPrecedenti(dettVc, siacTClassPrec);
	}
	
	private void caricaClassificatoriPrecedenti(DettaglioVariazioneCodificaCapitolo dettVc, SiacTClass siacTClassPrec) {
		
		final String methodName = "caricaClassificatori";
		
		long time = System.currentTimeMillis();
		String desc = "siacTClass.uid="+siacTClassPrec.getUid() + " capitolo.uid: "+ dettVc.getCapitolo().getUid()+ " dettVc.uid:"+dettVc.getUid();
		log.debug(methodName, desc );
		
		SiacDClassTipoEnum classTipo = SiacDClassTipoEnum.byCodice(siacTClassPrec.getSiacDClassTipo().getClassifTipoCode());
		
		if (classTipo.isGerarchico()) {
			List<ClassificatoreGerarchico> classificatoriGerarchici = caricaClassificatoreGerarchico(siacTClassPrec);
//			for (ClassificatoreGerarchico cg : classificatoriGerarchici) {
//				cg.setDataFineValidita(dataFineValidita);
//			}
			dettVc.getClassificatoriGerarchiciPrecedenti().addAll(classificatoriGerarchici);
			
		} else {
			ClassificatoreGenerico classificatoreGenerico = caricaClassificatoreGenerico(siacTClassPrec);
//			classificatoreGenerico.setDataFineValidita(dataFineValidita);
			dettVc.getClassificatoriGenericiPrecedenti().add(classificatoreGenerico);
		}
		
		log.debug(methodName, desc+ ". Elapsed millis: "+ (System.currentTimeMillis()-time));
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
	 * By capitolo.
	 *
	 * @param dest the dest
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the dettaglio variazione codifica capitolo
	 */
	private DettaglioVariazioneCodificaCapitolo byCapitolo(List<DettaglioVariazioneCodificaCapitolo> dest, SiacRBilElemClassVar siacRBilElemClassVar) {
		SiacTBilElem siacTBilElem = siacRBilElemClassVar.getSiacTBilElem();
		
		for (DettaglioVariazioneCodificaCapitolo dettVc : dest) {
			if (siacTBilElem.getUid().equals(dettVc.getCapitolo().getUid())) {
				return dettVc;
			}
		}
		DettaglioVariazioneCodificaCapitolo dettVc = new DettaglioVariazioneCodificaCapitolo();
		dest.add(dettVc);
		
		popolaCapitoloVariazione(dettVc, siacRBilElemClassVar);

		return dettVc;
	}

	/**
	 * Trova il capitolo associato alla variazione, se presente, con i dati da variare. 
	 * Altrimenti con i dati originali.
	 * @param dettVc 
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the capitolo
	 */
	private void popolaCapitoloVariazione(DettaglioVariazioneCodificaCapitolo dettVc, SiacRBilElemClassVar siacRBilElemClassVar) {
		SiacTBilElem siacTBilElem = siacRBilElemClassVar.getSiacTBilElem();
		
		Capitolo capitolo = null;
		Capitolo capitoloPrec = null;
		
		//Cerco il capitolo nell'elenco dei capitoli presenti del dettaglio della variazione		
		List<SiacTBilElemVar> siacTBilElemVars = siacRBilElemClassVar.getSiacRVariazioneStato().getSiacTBilElemVars();
		if(siacTBilElemVars!=null) {
			for(SiacTBilElemVar siacTBilElemVar : siacTBilElemVars){
				if(siacTBilElemVar.getDataCancellazione()!=null){
					continue;
				}
				if(siacTBilElem.getUid().equals(siacTBilElemVar.getSiacTBilElem().getUid())) {
					capitolo = mapToCapitolo(siacTBilElemVar);
					capitoloPrec = mapToCapitolo(siacTBilElemVar.getSiacTBilElem());
					break;
				}
			}
		} 

		//Il capitolo non era presente nel dettaglio della variazione. Carico i dati di base del capitolo non variati.
		if(capitolo==null) {
			capitolo = mapToCapitolo(siacTBilElem);
			capitoloPrec = clone(capitolo); //mapToCapitolo(siacTBilElem); //voglio una nuova istanza //= capitolo
		}
		
		//Imposto il capitolo				
		dettVc.setCapitolo(capitolo);
		dettVc.setCapitoloPrecedente(capitoloPrec);	
	}
	
	/**
	 * Map to capitolo.
	 *
	 * @param siacTBilElemVar the siac t bil elem var
	 * @return the capitolo
	 */
	private Capitolo mapToCapitolo(SiacTBilElemVar siacTBilElemVar) {
		if(siacTBilElemVar==null){
			return null;
		}
		
		SiacTBilElem siacTBilElem = siacTBilElemVar.getSiacTBilElem();
		Capitolo capitolo = mapToCapitolo(siacTBilElem);
		
		capitolo.setDescrizione(siacTBilElemVar.getElemDesc());
		capitolo.setDescrizioneArticolo(siacTBilElemVar.getElemDesc2());
		
		return capitolo;
		
	}

	/**
	 * Map to capitolo.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @return the capitolo
	 */
	private Capitolo mapToCapitolo(SiacTBilElem siacTBilElem) {
		if(siacTBilElem==null){
			return null;
		}
		
		Capitolo capitolo = new Capitolo();
		map(siacTBilElem, capitolo, BilMapId.SiacTBilElem_Capitolo_Base);
		TipoCapitolo tipoCapitolo = SiacDBilElemTipoEnum.byCodice(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode()).getTipoCapitolo();
		capitolo.setTipoCapitolo(tipoCapitolo);
		
		return capitolo;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVariazione convertTo(List<DettaglioVariazioneCodificaCapitolo> src, SiacTVariazione dest) {		
		
		//Sicuramente presente l'elemento 0 perchè prima di questo mapper è già passato il mapper dello stato!
		SiacRVariazioneStato siacRVariazioneStato = dest.getSiacRVariazioneStatos().get(0);
		
		siacRVariazioneStato.setSiacRBilElemClassVars(new ArrayList<SiacRBilElemClassVar>());
		siacRVariazioneStato.setSiacTBilElemVars(new ArrayList<SiacTBilElemVar>());
		
		for(DettaglioVariazioneCodificaCapitolo dettVc : src){
			
			Capitolo c = dettVc.getCapitolo();
			SiacTBilElem siacTBilElem = new SiacTBilElem();
			siacTBilElem.setUid(c.getUid());
			
			for(ClassificatoreGenerico cg :dettVc.getClassificatoriGenerici()){
				SiacTClass siacTClass = map(cg, SiacTClass.class, BilMapId.SiacTClass_ClassificatoreGenerico);
				SiacTClass siacTClassAttuale = trovaVecchioClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato(siacTClass.getUid(), siacTBilElem.getUid());
				
				//la data fine validita contenuta in cg non si riferisce alla fine del Classificatore ma alla fine del legame tra la variazione ed il Classificatore.
				siacRVariazioneStato.addSiacTClass(siacTClass, siacTClassAttuale, siacTBilElem, cg.getDataFineValidita());
			}
			
			for(ClassificatoreGerarchico cg :dettVc.getClassificatoriGerarchici()){
				SiacTClass siacTClass = map(cg, SiacTClass.class, BilMapId.SiacTClass_ClassificatoreGerarchico);
				SiacTClass siacTClassAttuale = trovaVecchioClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato(siacTClass.getUid(), siacTBilElem.getUid());
				
				//la data fine validita contenuta in cg non si riferisce alla fine del Classificatore ma alla fine del legame tra la variazione ed il Classificatore.
				siacRVariazioneStato.addSiacTClass(siacTClass, siacTClassAttuale, siacTBilElem, cg.getDataFineValidita());
			}
			
			
			SiacTBilElemVar siacTBilElemVar = createSiacTBilElemVar(dest, c);
			siacRVariazioneStato.addSiacTBilElemVar(siacTBilElemVar);
			
		}
		
		return dest;
	
	}
	
	
	
	/**
	 * Restituisce la chiave di SiacRBilElemClass di un classificatore dello 
	 * stesso tipo/famiglia di classifIdNew associato al capitolo il cui id Ã¨ bilElemId.
	 *
	 * @param classifIdNew the classif id new
	 * @param bilElemId the bil elem id
	 * @return the integer
	 */
	private SiacTClass trovaVecchioClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato(Integer classifIdNew, Integer bilElemId) {		
		final String methodName = "trovaVecchioClassificatoreDellaStessaFamigliaOTipoDiQuelloPassato";
		
		String famigliaCode = siacTClassRepository.findCodiceFamigliaClassificatoreByClassifId(classifIdNew);
		if(famigliaCode == null){
			String classifTipoCode = siacTClassRepository.findCodiceTipoClassificatoreByClassifId(classifIdNew);
			SiacTClass result = siacTBilElemRepository.findTClassIdByElemIdAndTipoCode(bilElemId, classifTipoCode);
			log.info(methodName, "returning: "+(result!=null?result.getUid():"null") + " for classifIdNew: "+classifIdNew + " [Tipo: "+classifTipoCode+"]");
			return result;
			
		}
		SiacTClass result =  siacTBilElemRepository.findTClassIdByElemIdAndFamiglia(bilElemId, famigliaCode);
		log.info(methodName, "returning: "+ (result!=null?result.getUid():"null") + " for classifIdNew: "+classifIdNew + " [Famiglia: "+famigliaCode+"]");
		return result;
		
		
		
	}
	

	/**
	 * Creates the siac t bil elem var.
	 *
	 * @param dest the dest
	 * @param c the c
	 * @return the siac t bil elem var
	 */
	private SiacTBilElemVar createSiacTBilElemVar(SiacTVariazione dest, Capitolo c) {
		
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(c.getUid());
		if(siacTBilElem==null){
			throw new IllegalArgumentException("Impossibile trovare il capitolo da variare con uid: "+c.getUid());
		}
		
		
		SiacTBilElemVar siacTBilElemVar = new SiacTBilElemVar();
		siacTBilElemVar.setSiacTBilElem(siacTBilElem);			
		
		siacTBilElemVar.setElemCode(c.getNumeroArticolo()!=null?""+c.getNumeroCapitolo():siacTBilElem.getElemCode());
		siacTBilElemVar.setElemCode2(c.getNumeroArticolo()!=null?""+c.getNumeroArticolo():siacTBilElem.getElemCode2());
		siacTBilElemVar.setElemCode3(c.getNumeroUEB()!=null?""+c.getNumeroUEB():siacTBilElem.getElemCode3());
		siacTBilElemVar.setElemDesc(c.getDescrizione()!=null? c.getDescrizione() : siacTBilElem.getElemDesc());
		siacTBilElemVar.setElemDesc2(c.getDescrizioneArticolo()!=null? c.getDescrizioneArticolo() : siacTBilElem.getElemDesc2());
		
		//Salvo i valori attuali del capitolo per mantenerne lo storico.
		siacTBilElemVar.setElemCodePrec(siacTBilElem.getElemCode());
		siacTBilElemVar.setElemCode2Prec(siacTBilElem.getElemCode2());
		siacTBilElemVar.setElemCode3Prec(siacTBilElem.getElemCode3());
		siacTBilElemVar.setElemDescPrec(siacTBilElem.getElemDesc());
		siacTBilElemVar.setElemDesc2Prec(siacTBilElem.getElemDesc2());

		
		siacTBilElemVar.setElemTipoId(siacTBilElem.getSiacDBilElemTipo().getElemTipoId());
		siacTBilElemVar.setBilId(siacTBilElem.getSiacTBil().getBilId());
		siacTBilElemVar.setPeriodoId(siacTBilElem.getSiacTBil().getSiacTPeriodo().getUid());
		
		siacTBilElemVar.setOrdine(siacTBilElem.getOrdine());
		siacTBilElemVar.setLivello(siacTBilElem.getLivello());
		
		siacTBilElemVar.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacTBilElemVar.setLoginOperazione(dest.getLoginOperazione());
		return siacTBilElemVar;
	}
	


}
