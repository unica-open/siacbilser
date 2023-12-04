/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportoDerivato;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;


/**
 * Convertitore di base per settare una lista di ImportiCapitolo in un ElementoBilancioDto di tipo 
 * Capitolo[Entrata/Uscita][Previsione/Gestione]Dto.
 *
 * @author Domenico
 * @param <IC> the generic type
 */
public class ImportiCapitoloBaseConverter<IC extends ImportiCapitolo> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/** The siac t periodo repository. */
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	/** The capitolo dao. */
	@Autowired
	private CapitoloDao capitoloDao;
	
	/** The importi capitolo repository. */
	@Autowired
	private SiacTBilElemDetRepository importiCapitoloRepository;
	
	
	/** The log. */
	private static LogSrvUtil log = new LogSrvUtil(ImportiCapitoloBaseConverter.class);
	
	
	
	/**
	 * Setta lista importi capitolo in siac t bil elem.
	 *
	 * @param listaImporti the lista importi
	 * @param elementoBilancio the elemento bilancio
	 */
	public void settaListaImportiCapitoloInSiacTBilElem(List<IC> listaImporti, SiacTBilElem elementoBilancio) {
		final String methodName = "settaListaImportiCapitoloInSiacTBilElem";
		List<SiacTBilElemDet> listaDettaglio = new ArrayList<SiacTBilElemDet>();

		for (IC importiCapitolo : listaImporti) {
		
			SiacTPeriodo periodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+importiCapitolo.getAnnoCompetenza(),  elementoBilancio.getSiacTEnteProprietario());			
			
			Map<String,BigDecimal> fieldsNameValueMap = Utility.getFieldNameValueMapByAnnotationImportoPersistente(importiCapitolo); //Utility.getFieldNameValueMapByType(importiCapitolo, BigDecimal.class);
			
			for (Entry<String, BigDecimal> fieldNameValue : fieldsNameValueMap.entrySet()) {
				
				String fieldName = fieldNameValue.getKey();
				BigDecimal fieldValue  = fieldNameValue.getValue();
				
				log.trace(methodName,"Mapping importo: "+ fieldName+" value: "+fieldValue);
				
				if(fieldValue!=null){
					
					SiacTBilElemDet deb = new SiacTBilElemDet();
					deb.setElemDetImporto(fieldValue);					
					SiacDBilElemDetTipo siacDBilElemDetTipo = eef.getEntity(SiacDBilElemDetTipoEnum.byImportiCapitoloModelFieldName(fieldName),elementoBilancio.getSiacTEnteProprietario().getEnteProprietarioId(),  SiacDBilElemDetTipo.class); //.getEntity();
					deb.setSiacDBilElemDetTipo(siacDBilElemDetTipo);					
					deb.setSiacTBilElem(elementoBilancio);
					deb.setSiacTEnteProprietario(elementoBilancio.getSiacTEnteProprietario());
					deb.setLoginOperazione(elementoBilancio.getLoginOperazione());
					deb.setSiacTPeriodo(periodo);
	
					listaDettaglio.add(deb);
				
				}
			}
			
			
		}
		
		elementoBilancio.setSiacTBilElemDets(listaDettaglio);		
	}
	


	// NOTA: di fatto questa conversione NON viene utilizzata ma si utilizza toImportiCapitolo!!! NON bello questo!
	/**
	 * Gets the lista importi capitolo from siac t bil elem.
	 *
	 * @param elementoBilancio the elemento bilancio
	 * @return the lista importi capitolo from siac t bil elem
	 */
	public List<IC> getListaImportiCapitoloFromSiacTBilElem(SiacTBilElem elementoBilancio){	
		
		//List<SiacTBilElemDet> bilElemDets = elementoBilancio.getSiacTBilElemDets();
				
		//IC ic = toImportiCapitolo(bilElemDets);		
		//List<IC> result = new ArrayList<IC>();
		//result.add(ic);
		
		return toImportiCapitoloList(elementoBilancio);//elementoBilancio.getSiacTBilElemDets());
		
	}
	

	/**
	 * To importi capitolo.
	 *
	 * @param bilElemDets the bil elem dets
	 * @param annoCompetenza the anno competenza
	 * @return the ic
	 */
	public IC toImportiCapitolo(List<SiacTBilElemDet> bilElemDets, Integer annoCompetenza, Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		return toImportiCapitolo(bilElemDets, instantiateNewIC(), annoCompetenza, importiDerivatiRichiesti, false);
	}


	/**
	 * To importi capitolo.
	 *
	 * @param bilElemDets the bil elem dets
	 * @param annoCompetenza the anno competenza
	 * @return the ic
	 */
	public IC toImportiCapitolo(List<SiacTBilElemDet> bilElemDets, Integer annoCompetenza, Set<ImportiCapitoloEnum> importiDerivatiRichiesti, boolean forzaPopolamentoImportaDerivati) {
		return toImportiCapitolo(bilElemDets, instantiateNewIC(), annoCompetenza, importiDerivatiRichiesti, forzaPopolamentoImportaDerivati);
	}
	
	
	
	
	/**
	 * To importi capitolo list.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @return the list
	 */
	private List<IC> toImportiCapitoloList(SiacTBilElem siacTBilElem) {
		final String methodName = "toImportiCapitoloList";
		
		List<SiacTBilElemDet> bilElemDets = siacTBilElem.getSiacTBilElemDets();
		
		List<IC> result = new ArrayList<IC>();
		
		for(SiacTBilElemDet det : bilElemDets) {
			String annoCompetenzaEntityStr = det.getSiacTPeriodo().getAnno();
			Integer annoCompetenzaEntity = annoCompetenzaEntityStr!=null?Integer.valueOf(annoCompetenzaEntityStr):null;
			if(annoCompetenzaEntity==null){ //NON dovrebbe mai succedere che su db sono memorizzati a null!
				continue;
			}
			
			IC ic = byAnnoCompetenza(result, annoCompetenzaEntity);
			
			BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(ic);
			
			log.trace(methodName,"importo:"+det.getElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode());			
			String importiCapitoloFieldName = SiacDBilElemDetTipoEnum.byCodice(det.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
			try{
				bwic.setPropertyValue(importiCapitoloFieldName, det.getElemDetImporto());
			} catch (BeansException be){
				if(ImportiCapitolo.class.equals(ic.getClass())){
					//Nel caso utilizzo ImportiCapitolo come classe vuol dire che non mi interessa tirare su TUTTI gli importi ma
					//mi bastano quelli della superclasse ImportiCapitolo.class quindi ignoro eventuali errori nel settaggio di tali importi.
					log.warn(methodName, "Cannot set importo:"+det.getElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode(),be);
				} else {
					throw be;
				}				
			}
		}
		return result;
	}


	/**
	 * Ricerca un ImportiCapitolo dentro importiCapitoloList che abbia l'annoCompetenza passato come parametro.
	 * Se non lo trova ne istanzia uno, lo inserisce nella lista e lo restituisce settandogli l'annoCompenteza.
	 *
	 * @param importiCapitoloList the importi capitolo list
	 * @param annoCompetenza the anno competenza
	 * @return the ic
	 */
	private IC byAnnoCompetenza(List<IC> importiCapitoloList, Integer annoCompetenza){
		for (IC ic : importiCapitoloList) {
			if(ic.getAnnoCompetenza()!=null && ic.getAnnoCompetenza().equals(annoCompetenza)){
				return ic;
			}		
		}
		
		IC ic = instantiateNewIC();				//ne istanzia uno nuovo.
		ic.setAnnoCompetenza(annoCompetenza);	//gli setta l'anno di competenza e 
		importiCapitoloList.add(ic); 			//Lo aggiunge nella lista.
		
		return ic;
		
	}
	
	/**
	 * Ottiene gli importi di un capitolo per l'anno di competenza.
	 *
	 * @param <ICT> the generic type
	 * @param uidCapitolo uid del capitolo
	 * @param annoCompetenza anno di comeptenza degli importi.
	 * @return the ict
	 */
	public <ICT extends ImportiCapitolo> ICT toImportiCapitolo(Integer uidCapitolo, Integer annoCompetenza, Set<ImportiCapitoloEnum> importiDerivatiRichiesti, boolean forzaPopolamentoImportaDerivati){
		SiacTBilElem siacTBilElem = capitoloDao.findById(uidCapitolo);
		if(siacTBilElem == null) {
			throw new IllegalArgumentException("Impossibile trovare il capitolo con uid:"+uidCapitolo);
		}
		return toImportiCapitolo(siacTBilElem, annoCompetenza, importiDerivatiRichiesti, forzaPopolamentoImportaDerivati);
	}
	
	/**
	 * To importi capitolo.
	 *
	 * @param <ICT> the generic type
	 * @param siacTBilElem the siac t bil elem
	 * @param annoCompetenza the anno competenza
	 * @return the ict
	 */
	public <ICT extends ImportiCapitolo> ICT toImportiCapitolo(SiacTBilElem siacTBilElem, Integer annoCompetenza, Set<ImportiCapitoloEnum> importiDerivatiRichiesti, boolean forzaPopolamentoImportaDerivati){
		final String methodName = "toImportiCapitolo";	
		
		log.trace(methodName, "importiCapitoloRepository.findBilElemDetsByBilElemIdAndAnno uid: "+siacTBilElem.getUid() + " anno: "+annoCompetenza );
		
		List<SiacTBilElemDet> bilElemDets = importiCapitoloRepository.findBilElemDetsByBilElemIdAndAnno(siacTBilElem.getUid(), ""+annoCompetenza);
		
		if(bilElemDets.isEmpty()){
			log.warn(methodName, "Non sono presenti importi per l'anno di competenza: " +annoCompetenza + "[capitolo uid: "+ siacTBilElem.getUid()+ "]" );
			return null;
		}
		
		String elemTipoCode = siacTBilElem.getSiacDBilElemTipo().getElemTipoCode();
		Class<?> capitoloClass = SiacDBilElemTipoEnum.byCodice(elemTipoCode).getCapitoloClass();
		
		ICT importiCapitolo = Utility.instantiateGenericType(capitoloClass, Capitolo.class, 0);
		
		return toImportiCapitolo(bilElemDets, importiCapitolo, annoCompetenza, importiDerivatiRichiesti, forzaPopolamentoImportaDerivati);
	}
	
	/**
	 * Filtra l'elenco di bilElemDets per annoCompetenza e li converte in ImportiCapitolo.
	 *
	 * @param <ICT> the generic type
	 * @param bilElemDets the bil elem dets
	 * @param ic the ic
	 * @param annoCompetenza the anno competenza
	 * @return the ict
	 */
	public <ICT extends ImportiCapitolo> ICT toImportiCapitolo(List<SiacTBilElemDet> bilElemDets, ICT ic, Integer annoCompetenza, Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		return toImportiCapitolo(bilElemDets, ic, annoCompetenza, importiDerivatiRichiesti, false);
	}

	/**
	 * Filtra l'elenco di bilElemDets per annoCompetenza e li converte in ImportiCapitolo.
	 *
	 * @param <ICT> the generic type
	 * @param bilElemDets the bil elem dets
	 * @param ic the ic
	 * @param annoCompetenza the anno competenza
	 * @return the ict
	 */
	public <ICT extends ImportiCapitolo> ICT toImportiCapitolo(List<SiacTBilElemDet> bilElemDets, ICT ic, Integer annoCompetenza, Set<ImportiCapitoloEnum> importiDerivatiRichiesti, boolean forzaPopolamentoImportaDerivati) {
		final String methodName = "toImportiCapitolo";
		BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(ic);
		
		Integer annoCompetenzaDelCapitolo = null;
		SiacTBilElem siacTBilElem = null;
		
		for(SiacTBilElemDet det : bilElemDets) {
			String annoCompetenzaEntityStr = det.getSiacTPeriodo().getAnno();
			Integer annoCompetenzaEntity = annoCompetenzaEntityStr!=null? Integer.valueOf(annoCompetenzaEntityStr):null;
			if(annoCompetenzaEntity==null || !annoCompetenza.equals(annoCompetenzaEntity)){
				continue;
			}
			if(siacTBilElem==null){
				siacTBilElem = det.getSiacTBilElem();				
			}
			ic.setAnnoCompetenza(annoCompetenzaEntity);			
			
			
			log.debug(methodName,"importo:"+det.getElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode());			
			String importiCapitoloFieldName = SiacDBilElemDetTipoEnum.byCodice(det.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
			try{
				bwic.setPropertyValue(importiCapitoloFieldName, det.getElemDetImporto());
			} catch (BeansException be){
				if(ImportiCapitolo.class.equals(ic.getClass())){
					//Nel caso utilizzo ImportiCapitolo come classe vuol dire che non mi interessa tirare su TUTTI gli importi ma
					//mi bastano quelli della superclasse ImportiCapitolo.class quindi ignoro eventuali errori nel settaggio di tali importi.
					log.warn(methodName, "Cannot set importo:"+det.getElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode()+". "+be.getMessage());
				} else {
					throw be;
				}				
			}
			
			
		}
		
		if(siacTBilElem!=null) {
			String annoCompetenzaDelCapitoloStr = siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno();
			annoCompetenzaDelCapitolo = annoCompetenzaDelCapitoloStr!=null?Integer.valueOf(annoCompetenzaDelCapitoloStr):null;		
			if((annoCompetenzaDelCapitolo!=null && annoCompetenzaDelCapitolo.equals(annoCompetenza)) || forzaPopolamentoImportaDerivati) {
				popolaImportiDerivati(siacTBilElem.getUid(), ic, importiDerivatiRichiesti);
			}
		}
		
		
		return ic;
	}
	
	
	/**
	 * Popola gli importi annotati come ImportoDerivato ovvero derivanti da un calcolo di
	 * una function su database.
	 *
	 * @param <ICT> the generic type
	 * @param uidCapitolo the uid capitolo
	 * @param ic the ic
	 * @param importiDerivatiRichiesti il nome dei field degli importi richiesti
	 */
	private <ICT extends ImportiCapitolo> void popolaImportiDerivati(final Integer uidCapitolo, ICT ic, final Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		ReflectionUtils.doWithFields(ic.getClass(), new PopolaImportiDerivatiFieldCallback<ICT>(importiDerivatiRichiesti, uidCapitolo, capitoloDao, ic));
	}
	
	/**
	 * Instantiate new ic.
	 *
	 * @return the ic
	 */
	protected IC instantiateNewIC() {
		return Utility.instantiateGenericType(this.getClass(), ImportiCapitoloBaseConverter.class, 0);
	}
	
	/**
	 * Callback per il popolamento degli importi derivati
	 * @author Marchino Alessandro
	 * @param <ICT> la tipizzazione dell'importo capitolo
	 */
	private static class PopolaImportiDerivatiFieldCallback<ICT extends ImportiCapitolo> implements FieldCallback {
		private final LogSrvUtil log = new LogSrvUtil(getClass());
		
		private final Set<ImportiCapitoloEnum> importiDerivatiRichiesti;
		private final Integer uidCapitolo;
		private final CapitoloDao capitoloDao;
		private final BeanWrapper beanWrapper;
		
		/**
		 * Costruttore
		 * @param importiDerivatiRichiesti
		 * @param uidCapitolo
		 * @param capitoloDao
		 * @param beanWrapper
		 */
		PopolaImportiDerivatiFieldCallback(Set<ImportiCapitoloEnum> importiDerivatiRichiesti, Integer uidCapitolo,
				CapitoloDao capitoloDao, ICT ic) {
			this.importiDerivatiRichiesti = importiDerivatiRichiesti;
			this.uidCapitolo = uidCapitolo;
			this.capitoloDao = capitoloDao;
			this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(ic);
		}
		
		@Override
		public void doWith(Field f) throws IllegalAccessException {
			final String methodName = "doWith";
			
			ImportoDerivato annotation = f.getAnnotation(ImportoDerivato.class);
			if(annotation !=null && annotation.value()!=null) {
				
				if( (importiDerivatiRichiesti != null //sono stati specificati gli importi richiesti
						&& !importiDerivatiRichiesti.contains(ImportiCapitoloEnum.valueOf(f.getName()))) //e questo field non e' nella lista
						
						|| //oppure 
						
						(importiDerivatiRichiesti==null  //NON sono stati specificati gli importi richiesti
						&& !annotation.calcolareDiDefault() )// e il campo e' annotato come da NON caricare di default 
					) {
					log.debug(methodName, "field saltato: "+f.getName()+ " for uidCapitolo: "+uidCapitolo);
					return;
				}
				
				log.info(methodName, "invoking function " +annotation.value().getFunctionName() + " for uidCapitolo: "+uidCapitolo);
				BigDecimal importo = capitoloDao.findImportoDerivato(uidCapitolo, annotation.value().getFunctionName());
				
				try{
					beanWrapper.setPropertyValue(f.getName(), importo);
				} catch(BeansException be){
					log.warn(methodName, "Cannot set importo derivato:"+f.getName(), be);
				}
			}
			
		}
	}
	
}
