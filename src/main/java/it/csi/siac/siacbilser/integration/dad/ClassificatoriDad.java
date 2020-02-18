/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Constants;
import it.csi.siac.siacbilser.integration.dao.SiacDBilElemCategoriaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipoVincolo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonser.util.dozer.MapId;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.TipoAvviso;
import it.csi.siac.siacfin2ser.model.TipoImpresa;

// TODO: Auto-generated Javadoc
/**
 * The Class ClassificatoriDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ClassificatoriDad extends ExtendedBaseDadImpl {
	
	/** The siac t class dao. */
	@Autowired
	private SiacTClassDao siacTClassDao;
	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	@Autowired
	private SiacDBilElemCategoriaRepository siacDBilElemCategoriaRepository;
	
	/**
	 *  
	 * Ricerca un classificatore e ne restituisce la sua istanza a partire dal suoi id.
	 *
	 * @param <T> the generic type
	 * @param classifId the classif id
	 * @return the t
	 */
	public <T extends Codifica> T ricercaClassificatoreById(Integer classifId) {
		if(classifId==null){
			return null;
		}
		SiacTClass classif = siacTClassRepository.findOne(classifId);
		
		return toClassificatore(classif);
	}

	
	/**
	 * Ricerca classificatore gerarchico by id.
	 *
	 * @param <T> the generic type
	 * @param classifId the classif id
	 * @return the t
	 */
	private <T extends ClassificatoreGerarchico> T ricercaClassificatoreGerarchicoById(Integer classifId){//, SiacDClassTipoEnum tipo) {
		return ricercaClassificatoreById(classifId);//, tipo, BilMapId.SiacTClass_ClassificatoreGerarchico);		
	}
	
	/**
	 * Ricerca classificatore generico by id.
	 *
	 * @param <T> the generic type
	 * @param classifId the classif id
	 * @return the t
	 */
	private <T extends ClassificatoreGenerico> T ricercaClassificatoreGenericoById(Integer classifId){//, SiacDClassTipoEnum tipo) {
		return ricercaClassificatoreById(classifId);//, tipo, BilMapId.SiacTClass_ClassificatoreGenerico);		
	}
	
	
	/**
	 * To classificatore.
	 *
	 * @param <T> the generic type
	 * @param classif the classif
	 * @return the t
	 */
	private <T extends Codifica> T toClassificatore(SiacTClass classif) {
		final String methodName = "toClassificatore";
		
		if(classif==null) {
			return null;
		}
		
		SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classif.getSiacDClassTipo().getClassifTipoCode());
				
		T classificatore = tipo.getCodificaInstance();
		
		map(classif,classificatore,tipo.getMapId());
		log.debug(methodName, "Classificatore di tipo (Dopo Dozer): "+classificatore.getClass().getSimpleName());
		
		
		TipoClassificatore tc = new TipoClassificatore();
		tc.setCodice(tipo.getCodice());		
		setTipoClassificatore(classificatore, tc);
		
		return classificatore;
	}

	/**
	 * Sets the tipo classificatore.
	 *
	 * @param <T> the generic type
	 * @param classificatore the classificatore
	 * @param tc the tc
	 */
	private <T extends Codifica> void setTipoClassificatore(T classificatore, TipoClassificatore tc) {
		if(classificatore instanceof ClassificatoreGerarchico ){
			((ClassificatoreGerarchico)classificatore).setTipoClassificatore(tc);
		} else if (classificatore instanceof ClassificatoreGenerico){
			((ClassificatoreGenerico)classificatore).setTipoClassificatore(tc);
		}
	}
	
	
	/**
	 * Ricerca padre classificatore.
	 *
	 * @param <TP> the generic type
	 * @param <TF> the generic type
	 * @param classifFiglio the classif figlio
	 * @return the tp
	 * @deprecated non gestisce correttamente la data di scadenza. Utilizzare {@link #ricercaPadreClassificatore(ClassificatoreGerarchico, Integer)}
	 */
	@Deprecated
	public <TP extends ClassificatoreGerarchico,TF extends ClassificatoreGerarchico> TP ricercaPadreClassificatore(TF classifFiglio){//, SiacDClassTipoEnum tipoFiglio, SiacDClassTipoEnum tipoPadre, SiacDClassFamEnum famigliaClass ) {
		if(classifFiglio==null){
			return null;
		}
//		SiacTClass classPadre = siacTClassDao.findPadreClassificatore(classifFiglio.getCodice(), tipoFiglio, famigliaClass);
		SiacTClass classPadre = siacTClassRepository.findPadreClassificatoreByClassifId(classifFiglio.getUid());
		if(classPadre==null){
			throw new IllegalStateException("Il classificatore con id "+classifFiglio.getUid() + " non ha padre.");
		}
		
		SiacDClassTipoEnum tipoPadre = SiacDClassTipoEnum.byCodice(classPadre.getSiacDClassTipo().getClassifTipoCode());
		
		TP classificatore = tipoPadre.getCodificaInstance();
		map(classPadre,classificatore,BilMapId.SiacTClass_ClassificatoreGerarchico);
		
		TipoClassificatore tc = new TipoClassificatore();
		tc.setCodice(tipoPadre.getCodice());
		classificatore.setTipoClassificatore(tc);
		
		return classificatore;		
	}
	
	/**
	 * Ricerca padre classificatore.
	 *
	 * @param <TP> the generic type
	 * @param <TF> the generic type
	 * @param classifFiglio the classif figlio
	 * @param annoEsercizio the anno esercizio
	 * @return the tp
	 */
	public <TP extends ClassificatoreGerarchico, TF extends ClassificatoreGerarchico> TP ricercaPadreClassificatore(TF classifFiglio, Integer annoEsercizio){
		if(classifFiglio == null || annoEsercizio == null){
			return null;
		}
		List<SiacTClass> classifsPadre = siacTClassRepository.findPadreClassificatoreByClassifIdAndAnnoEsercizio(classifFiglio.getUid(), annoEsercizio.toString());
		if(classifsPadre == null || classifsPadre.isEmpty()){
			throw new IllegalStateException("Il classificatore con id " + classifFiglio.getUid() + " non ha padre.");
		}
		if(classifsPadre.size() > 1) {
			throw new IllegalStateException("Il classificatore con id " + classifFiglio.getUid() + " ha piu' di un padre.");
		}
		
		SiacTClass classPadre = classifsPadre.get(0);
		SiacDClassTipoEnum tipoPadre = SiacDClassTipoEnum.byCodice(classPadre.getSiacDClassTipo().getClassifTipoCode());
		
		TP classificatore = tipoPadre.getCodificaInstance();
		map(classPadre,classificatore,BilMapId.SiacTClass_ClassificatoreGerarchico);
		
		TipoClassificatore tc = new TipoClassificatore();
		tc.setCodice(tipoPadre.getCodice());
		classificatore.setTipoClassificatore(tc);
		
		return classificatore;
	}


	
	/**
	 * Ricerca padre programma.
	 *
	 * @param programma the programma
	 * @return the missione
	 */
	public Missione ricercaPadreProgramma(Programma programma) {		
		return ricercaPadreClassificatore(programma);//, SiacDClassTipoEnum.Programma, SiacDClassTipoEnum.Missione, SiacDClassFamEnum.SpesaMissioniprogrammi);	
	}	


	/**
	 * Ricerca padre macroaggregato.
	 *
	 * @param macroaggregato the macroaggregato
	 * @return the titolo spesa
	 */
	public TitoloSpesa ricercaPadreMacroaggregato(Macroaggregato macroaggregato) {		
		return ricercaPadreClassificatore(macroaggregato);//, SiacDClassTipoEnum.Macroaggregato, SiacDClassTipoEnum.TitoloSpesa, SiacDClassFamEnum.SpesaTitolimacroaggregati);	
	}
	
	
	/**
	 * Ricerca padre categoria tipologia titolo.
	 *
	 * @param categoriaTipologiaTitolo the categoria tipologia titolo
	 * @return the tipologia titolo
	 */
	public TipologiaTitolo ricercaPadreCategoriaTipologiaTitolo(CategoriaTipologiaTitolo categoriaTipologiaTitolo) {		
		return ricercaPadreClassificatore(categoriaTipologiaTitolo);//, SiacDClassTipoEnum.Categoria, SiacDClassTipoEnum.Tipologia, SiacDClassFamEnum.EntrataTitolitipologiecategorie);	
	}	
	
	/**
	 * Ricerca padre tipologia titolo.
	 *
	 * @param tipologiaTitolo the tipologia titolo
	 * @return the titolo entrata
	 */
	public TitoloEntrata ricercaPadreTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {		
		return ricercaPadreClassificatore(tipologiaTitolo);//, SiacDClassTipoEnum.Tipologia, SiacDClassTipoEnum.TitoloEntrata, SiacDClassFamEnum.EntrataTitolitipologiecategorie);	
	}
	
	/**
	 * Ricerca padre struttura amministrativo contabile.
	 *
	 * @param strutturaAmministrativoContabile the struttura amministrativo contabile
	 * @param annoEsercizio 
	 * @return the struttura amministrativo contabile
	 */
	public StrutturaAmministrativoContabile ricercaPadreStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile, Integer annoEsercizio) {
		return ricercaPadreClassificatore(strutturaAmministrativoContabile, annoEsercizio);	
	}
	
	/**
	 * Ricerca programma.
	 *
	 * @param classifId the classif id
	 * @return the programma
	 */
	public Programma ricercaProgramma(Integer classifId){
		return ricercaClassificatoreGerarchicoById(classifId);//, SiacDClassTipoEnum.Programma);
	}
	
	/**
	 * Ricerca titolo spesa.
	 *
	 * @param classifId the classif id
	 * @return the programma
	 */
	public Programma ricercaTitoloSpesa(Integer classifId){
		return ricercaClassificatoreGerarchicoById(classifId);//, SiacDClassTipoEnum.TitoloSpesa);
	}
	
	/**
	 * Ricerca struttura amministrativo contabile.
	 *
	 * @param classifId the classif id
	 * @return the struttura amministrativo contabile
	 */
	public StrutturaAmministrativoContabile ricercaStrutturaAmministrativoContabile(Integer classifId){
		return ricercaClassificatoreGerarchicoById(classifId);//, SiacDClassTipoEnum.Cdc);
	}
	
	/**
	 * Ricerca elemento piano dei conti.
	 *
	 * @param classifId the classif id
	 * @return the elemento piano dei conti
	 */
	public ElementoPianoDeiConti ricercaElementoPianoDeiConti(Integer classifId){
		return ricercaClassificatoreGerarchicoById(classifId);//, SiacDClassTipoEnum.PrimoLivelloPdc); //Il tipo viene passato solo per avere dinamicamente una new di ElementoPianoDeiConti
	}
	
	/**
	 * Ricerca mappa classificatori.
	 *
	 * @param classifGenericoOGerarchico the classif generico o gerarchico
	 * @return the map
	 */
	public Map<TipologiaClassificatore, Integer> ricercaMappaClassificatori(Codifica classifGenericoOGerarchico){
		Integer classifId = classifGenericoOGerarchico!=null?classifGenericoOGerarchico.getUid():null;
		
		return ricercaMappaClassificatori(classifId);
	}
	
	/**
	 * Ricerca mappa classificatori.
	 *
	 * @param classifGenerico the classif generico
	 * @return the map
	 */
	public Map<TipologiaClassificatore, Integer> ricercaMappaClassificatori(ClassificatoreGenerico classifGenerico){
		Integer classifId = classifGenerico!=null?classifGenerico.getUid():null;
		
		return ricercaMappaClassificatori(classifId);
	}
	
	/**
	 * Ricerca mappa classificatori.
	 *
	 * @param classifGerarchico the classif gerarchico
	 * @return the map
	 */
	public Map<TipologiaClassificatore, Integer> ricercaMappaClassificatori(ClassificatoreGerarchico classifGerarchico){
		Integer classifId = classifGerarchico!=null?classifGerarchico.getUid():null;
		
		return ricercaMappaClassificatori(classifId);
	}
	
	/**
	 * Ricerca tipo ambito.
	 *
	 * @param classifId the classif id
	 * @return the tipo ambito
	 */
	public TipoAmbito ricercaTipoAmbito(Integer classifId){
		return ricercaClassificatoreGenericoById(classifId);//, SiacDClassTipoEnum.TipoAmbito); 
	}
	
	/**
	 * Ricerca tipo finanziamento.
	 *
	 * @param classifId the classif id
	 * @return the tipo finanziamento
	 */
	public TipoFinanziamento ricercaTipoFinanziamento(Integer classifId){
		return ricercaClassificatoreGenericoById(classifId);//, SiacDClassTipoEnum.TipoFinanziamento); 
	}
	
	/**
	 * Ricerca tipo fondo.
	 *
	 * @param classifId the classif id
	 * @return the tipo fondo
	 */
	public TipoFondo ricercaTipoFondo(Integer classifId){
		return ricercaClassificatoreGenericoById(classifId);//, SiacDClassTipoEnum.TipoFondo); 
	}
	
	/**
	 * Ricerca tipo vincolo.
	 *
	 * @param classifId the classif id
	 * @return the tipo vincolo
	 */
	public TipoVincolo ricercaTipoVincolo(Integer classifId){
		return ricercaClassificatoreGenericoById(classifId);//, SiacDClassTipoEnum.TipoVincolo); 
	}
	
	
	
	
	/**
	 * Ricerca un classificatore gerarchico restituendo tutti i suoi padri in una mappa.
	 *
	 * @param classifId the classif id
	 * @return the map
	 */
	public Map<TipologiaClassificatore, Integer> ricercaMappaClassificatori(Integer classifId){
		
		Map<TipologiaClassificatore, Integer> result = new HashMap<TipologiaClassificatore, Integer>();
		
		if(classifId==null){
			return result;
		}
		
		Map<String, SiacTClass> classMap = siacTClassDao.ricercaClassificatoriMapByClassId(classifId);
		
		for(Entry<String, SiacTClass> entry: classMap.entrySet()){
			String codiceTipo = entry.getKey();
			SiacTClass classif = entry.getValue();
			
			SiacDClassTipoEnum tipoEnum = SiacDClassTipoEnum.byCodice(codiceTipo);
			
			result.put(tipoEnum.getTipologiaClassificatore(), classif.getUid());
		}
	
		return result;
	}

	/**
	 * Restituisce i classificatori associati a quello specificato con l'id passato per parametro
	 * Vedi tabella: siac_r_class.
	 *
	 * @param classifId id del classificatore
	 * @return the list
	 */
	public List<Codifica> ricercaClassificatoriBByClassificatoreA(Integer classifId) {
		
		List<SiacTClass> siacTClasses = siacTClassRepository.findClassifBByClassifA(classifId);
		
		return toClassificatori(siacTClasses);
	}
	
	public List<Codifica> ricercaClassificatoriBByClassificatoreB(Integer classifId) {
		
		List<SiacTClass> siacTClasses = siacTClassRepository.findClassifBByClassifB(classifId);
		
		return toClassificatori(siacTClasses);
	}
	
	private List<Codifica> toClassificatori(List<SiacTClass> siacTClasses) {
		List<Codifica> result = new ArrayList<Codifica>();
		
		for (SiacTClass siacTClass : siacTClasses) {
			Codifica classificatore = toClassificatore(siacTClass);
			result.add(classificatore);
		}
		
		return result;
	}
	
	
	
	/**
	 * Ricerca classificatori tipo impresa by ente.
	 *
	 * @param uidEnte the uid ente
	 * @return the list
	 */
	public List<TipoImpresa> ricercaClassificatoriTipoImpresaByEnte(Integer uidEnte, Integer anno) {		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByEnteAndTipoCode(uidEnte, anno, SiacDClassTipoEnum.TipoImpresa.getCodice() );
		return convertiLista(siacTClasses, TipoImpresa.class,  BilMapId.SiacTClass_ClassificatoreGenerico);
	}
	
	
	/**
	 * Ricerca classificatori tipo avviso by ente.
	 *
	 * @param uidEnte the uid ente
	 * @return the list
	 */
	public List<TipoAvviso> ricercaClassificatoriTipoAvvisoByEnte(Integer uidEnte, Integer anno) {		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByEnteAndTipoCode(uidEnte, anno, SiacDClassTipoEnum.TipoAvviso.getCodice() );
		return convertiLista(siacTClasses, TipoAvviso.class,  BilMapId.SiacTClass_ClassificatoreGenerico);
	}


	public List<String> findDescrizioniTipoClassificatoreByTipoAndEnte(Collection<TipologiaClassificatore> tc, Integer anno, Integer uidEnte) {
		Collection<SiacDClassTipoEnum> siacDClassTipoEnums = SiacDClassTipoEnum.byTipologiaClassificatore(tc);
		
		Collection<String> codiciTipoClassificatore = new HashSet<String>();
		for(SiacDClassTipoEnum sdcte : siacDClassTipoEnums) {
			codiciTipoClassificatore.add(sdcte.getCodice());
		}
		
		List<SiacDClassTipo> siacDClassTipos = siacTClassDao.findClassTipoByEnteAndAnnoAndTipoCode(uidEnte, anno, codiciTipoClassificatore);
		List<String> result = new ArrayList<String>();
		if(siacDClassTipos != null && !siacDClassTipos.isEmpty()) {
			for(SiacDClassTipo sdct : siacDClassTipos) {
				result.add(sdct.getClassifTipoDesc());
			}
		}
		return result;
	}


	public Codifica ricercaClassificatoreGerarchicoByCodiceAndTipoAndAnno(Codifica classificatore, TipologiaClassificatore tipoClassificatore, Integer uidEnte, Integer anno) {
		return ricercaClassificatoreByCodiceAndTipoAndAnno(classificatore, tipoClassificatore, uidEnte, anno, BilMapId.SiacTClass_ClassificatoreGerarchico);
	}
	
	public Codifica ricercaClassificatoreGenericoByCodiceAndTipoAndAnno(Codifica classificatore, TipologiaClassificatore tipoClassificatore, Integer uidEnte, Integer anno) {
		return ricercaClassificatoreByCodiceAndTipoAndAnno(classificatore, tipoClassificatore, uidEnte, anno, BilMapId.SiacTClass_ClassificatoreGenerico);
	}
	
	private Codifica ricercaClassificatoreByCodiceAndTipoAndAnno(Codifica classificatore, TipologiaClassificatore tipoClassificatore, Integer uidEnte, Integer anno, MapId mapId) {
		SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byTipologiaClassificatore(tipoClassificatore);
		SiacTClass siacTClass = siacTClassDao.findClassifByCodiceAndEnteAndTipoCode(classificatore.getCodice(), uidEnte, anno, siacDClassTipoEnum.getCodice());
		
		return toClassificatore(siacTClass);
	}
	
	public ElementoPianoDeiConti ricercaElementoPianoDeiContiByCodiceAndAnno(Codifica classificatore, Integer uidEnte, Integer anno) {
		Set<SiacDClassTipoEnum> siacDClassTipoEnums = SiacDClassTipoEnum.byTipologiaClassificatore(Arrays.asList(TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II,
				TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V));
		
		Set<String> classifTipoCodes = new HashSet<String>();
		for(SiacDClassTipoEnum sdcte : siacDClassTipoEnums) {
			classifTipoCodes.add(sdcte.getCodice());
		}
		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByCodiceAndEnteAndTipoCodeLike(classificatore.getCodice(), uidEnte, anno, classifTipoCodes);
		
		if(siacTClasses.isEmpty()) {
			throw new IllegalStateException("Non esistono elementi del piano dei conti corrispondenti al codice " + classificatore.getCodice() + " per l'anno " + anno);
		}
		if(siacTClasses.size() > 1) {
			throw new IllegalStateException("Esistono piu' elementi del piano dei conti corrispondenti al codice " + classificatore.getCodice() + " per l'anno " + anno);
		}
		
		return toClassificatore(siacTClasses.get(0));
	}


	/**
	 *  Cerca il V livello del Piano Dei Conti collegato ad un impegno
	 * 
	 * @param movgestId uid dell'impegno
	 * @return il Piano dei Conti trovato
	 */
	public ElementoPianoDeiConti findVLivelloPdcAssociatoAImpegno(Integer movgestId) {
		SiacTClass siacTClass = siacTClassRepository.findVLivelloPdcByAMovgest(movgestId);
		return mapNotNull(siacTClass, ElementoPianoDeiConti.class, BilMapId.SiacTClass_ElementoPianoDeiConti);
	}
	
	/**
	 * Ottiene la categoria capitolo di tipo <code>STANDARD</code>.
	 * 
	 * @param uidEnte l'uid dell'ente
	 */
	public CategoriaCapitolo findCategoriaCapitoloStandard(Integer uidEnte) {
		return findCategoriaCapitolo( uidEnte, Constants.CATEGORIA_CAPITOLO_STANDARD.getValue()) ;
		//SiacDBilElemCategoria siacDBilElemCategoria = siacDBilElemCategoriaRepository.findCategoriaCapitoloByCodice("STD", uidEnte);
		//return mapNotNull(siacDBilElemCategoria, CategoriaCapitolo.class, BilMapId.SiacDBilElemCategoria_CategoriaCapitolo);
	}

	/**
	 * Ottiene la categoria capitolo del tipo passato.
	 * 
	 * @param uidEnte l'uid dell'ente
	 */
	public CategoriaCapitolo findCategoriaCapitolo(Integer uidEnte,String categoria) {
		SiacDBilElemCategoria siacDBilElemCategoria = siacDBilElemCategoriaRepository.findCategoriaCapitoloByCodice(categoria, uidEnte);
		return mapNotNull(siacDBilElemCategoria, CategoriaCapitolo.class, BilMapId.SiacDBilElemCategoria_CategoriaCapitolo);
	}

	
	public TipologiaClassificatore ricercaTipologiaClassificatoreByUidClassificatore(Codifica codifica) {
		SiacTClass siacTClass = siacTClassRepository.findOne(codifica.getUid());
		SiacDClassTipo siacDClassTipo = siacTClass.getSiacDClassTipo();
		
		SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byCodice(siacDClassTipo.getClassifTipoCode());
		return siacDClassTipoEnum.getTipologiaClassificatore();
	}

	/**
	 * Ricerca in maniera sintetica il classificatore sulla base dei parametri di input.
	 * 
	 * @param ente l'ente per la ricerca
	 * @param tipologiaClassificatore la tipologia del classificatore da cercare
	 * @param anno l'anno del classificatore
	 * @param codice il codice per la ricerca puntuale
	 * @param descrizione la descrizione per la ricerca
	 * @param parametriPaginazione i parametri di paginazione
	 * @return la lista paginata dei classificatori corrispondenti al filtro
	 */
	public ListaPaginata<Codifica> ricercaSinteticaClassificatore(Ente ente, TipologiaClassificatore tipologiaClassificatore, Integer anno, String codice, String descrizione, ParametriPaginazione parametriPaginazione) {
		SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore);
		
		Page<SiacTClass> siacTClasses = siacTClassDao.ricercaSinteticaClassificatore(ente.getUid(),
				siacDClassTipoEnum,
				anno,
				codice,
				descrizione,
				toPageable(parametriPaginazione));
		Codifica c = siacDClassTipoEnum.getCodificaInstance();
		return (ListaPaginata<Codifica>) toListaPaginata(siacTClasses, c, siacDClassTipoEnum.getMapId());
	}
	
	/**
	 * Conta il numero di classificatori corrispondenti all'ente, alla tipologia e all'anno
	 * 
	 * @param ente l'ente per la ricerca
	 * @param tipologiaClassificatore la tipologia da cercare
	 * @param anno l'anno dei classificatori
	 * @return in numero di classificatori
	 */
	public Long contaByTipoClassificatoreAndAnno(Ente ente, TipologiaClassificatore tipologiaClassificatore, Integer anno) {
		Collection<String> classifTipoCodes = new HashSet<String>();
		classifTipoCodes.add(SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore).getCodice());
		
		return siacTClassDao.countByEnteProprietarioIdAndClassifTipoCodesAndAnno(ente.getUid(), classifTipoCodes, anno);
	}
	
	/**
	 * Conta il numero di classificatori corrispondenti all'ente, alla tipologia e all'anno
	 * 
	 * @param ente l'ente per la ricerca
	 * @param tipologiaClassificatore la tipologia da cercare
	 * @param anno l'anno dei classificatori
	 * @return in numero di classificatori
	 */
	public Codifica findSingleByTipoClassificatoreAndAnno(Ente ente, TipologiaClassificatore tipologiaClassificatore, Integer anno) {
		Collection<String> classifTipoCodes = new HashSet<String>();
		final SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore);
		classifTipoCodes.add(siacDClassTipoEnum.getCodice());
		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByEnteAndClassifTipoCodes(ente.getUid(), anno, classifTipoCodes);
		// So di avere un unico elemento. Se cosi' non e', lancio un'eccezione
		if(siacTClasses == null || siacTClasses.isEmpty() || siacTClasses.size() > 1) {
			throw new IllegalStateException("Stato su base dati incompatibile tra le queries");
		}
		Codifica c = siacDClassTipoEnum.getCodificaInstance();
		map(siacTClasses.get(0), c, siacDClassTipoEnum.getMapId());
		return c;
	}
	
	/**
	 * Conta il numero di classificatori corrispondenti all'ente, alla tipologia e all'anno
	 * 
	 * @param ente l'ente per la ricerca
	 * @param tipologiaClassificatore la tipologia da cercare
	 * @param anno l'anno dei classificatori
	 * @return in numero di classificatori
	 */
	public Codifica findByEnteCodiceTipoClassificatoreAndAnno(Ente ente, String codice, TipologiaClassificatore tipologiaClassificatore, Integer anno) {
		Collection<String> classifTipoCodes = new HashSet<String>();
		final SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore);
		classifTipoCodes.add(siacDClassTipoEnum.getCodice());
		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByEnteAndClassifCodeAndClassifTipoCodes(ente.getUid(), codice, anno, classifTipoCodes);
		// So di avere un unico elemento. Se cosi' non e', lancio un'eccezione
		if(siacTClasses == null || siacTClasses.isEmpty()){
			return null;
		}
		if(siacTClasses.size() > 1) {
			throw new IllegalStateException("Stato su base dati incompatibile tra le queries: piu' di un classificatore trovato per codice \"" + codice
					+ "\", tipologia \"" + tipologiaClassificatore.toString() + " ed anno " + anno);
		}
		Codifica c = siacDClassTipoEnum.getCodificaInstance();
		map(siacTClasses.get(0), c, siacDClassTipoEnum.getMapId());
		return c;
	}
	
	
	/*public List<Integer> findFigliClassificatoreIds(String codiceClassificatore, String tipoClassificatore, String famigliaClass){
	
	return siacTClassDao.findFigliClassificatoreIds(codiceClassificatore, 
													SiacDClassTipoEnum.byCodice(tipoClassificatore), 
													SiacDClassFamEnum.byCodice(famigliaClass));
	
}*/

//	/**
//	 * Ricerca un classificatore e ne restituisce la sua istanza in base al tipo passato ed al mappingId
//	 * @param classifId
//	 * @param tipo
//	 * @param mappingId BilMapId.SiacTClass_ClassificatoreGenerico oppure BilMapId.SiacTClass_ClassificatoreGerarchico
//	 * @return
//	 */
//	private <T extends Codifica> T ricercaClassificatoreById(Integer classifId, SiacDClassTipoEnum tipo, BilMapId mappingId) {
//		if(classifId==null){
//			return null;
//		}
//
//		SiacTClass classif = siacTClassRepository.findOne(classifId);
//		
//		T classificatore = tipo.getCodificaInstance();
//		map(classif,classificatore,mappingId);
//		
//		TipoClassificatore tc = new TipoClassificatore();
//		tc.setCodice(tipo.getCodice());		
//		setTipoClassificatore(classificatore, tc);
//		
//		return classificatore;
//	}
	
	/**
	 * Trova i classificatori corrispondenti all'ente, alla tipologia e all'anno
	 * 
	 * @param ente l'ente per la ricerca
	 * @param tipologiaClassificatore la tipologia da cercare
	 * @param anno l'anno dei classificatori
	 * @return la lista di classificatori
	 */
	public List<Codifica> findListByEnteTipoClassificatoreAndAnno(Ente ente, List<TipologiaClassificatore> tipologiaClassificatore, Integer anno) {
		Collection<String> classifTipoCodes = new HashSet<String>();
		List<Codifica> result = new ArrayList<Codifica>();

		for (TipologiaClassificatore tc : tipologiaClassificatore) {
			classifTipoCodes.add(SiacDClassTipoEnum.byTipologiaClassificatore(tc).getCodice());
		}
		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByEnteAndClassifTipoCodes(ente.getUid(), anno, classifTipoCodes);
		
		if(siacTClasses == null || siacTClasses.isEmpty()){
			return result;
		}
		
		for (SiacTClass siacTClass : siacTClasses) {

			SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byCodiceEvenNull( siacTClass.getSiacDClassTipo().getClassifTipoCode());
			Codifica c = siacDClassTipoEnum.getCodificaInstance();
			map(siacTClass, c, siacDClassTipoEnum.getMapId());
			result.add(c);
		}
		
		
		return result;
	}
	
	/**
	 * Ottiene le tipologie classificatore
	 * @param ente l'ente per cui filtrare
	 * @param tipologiaClassificatore le tipologie di classificatore
	 * @param anno l'anno
	 * @param soloConFigli se ricercare la tipologia solo se ha almeno un figlio
	 * @return i tipi di classificatore
	 */
	public List<TipoClassificatore> findByTipologiaClassificatore(Ente ente, List<TipologiaClassificatore> tipologiaClassificatore, Integer anno) {
		
		Set<SiacDClassTipoEnum> siacDClassTipoEnums = SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore);
		Set<String> classifTipoCodes = new HashSet<String>();
		for(SiacDClassTipoEnum sdcte : siacDClassTipoEnums) {
			classifTipoCodes.add(sdcte.getCodice());
		}
		List<SiacDClassTipo> siacDClassTipos = siacTClassRepository.findSiacDClassTipoByEnteProprietarioIdAndClassifTipoCodesAndAnno(ente.getUid(), classifTipoCodes, anno);
		return convertiLista(siacDClassTipos, TipoClassificatore.class, BilMapId.SiacDClassTipo_TipoClassificatore);
	}
	
	/**
	 * Ottiene le tipologie classificatore
	 * @param ente l'ente per cui filtrare
	 * @param tipologiaClassificatore le tipologie di classificatore
	 * @param anno l'anno
	 * @param soloConFigli se ricercare la tipologia solo se ha almeno un figlio
	 * @return i tipi di classificatore
	 */
	public List<TipoClassificatore> findByTipologiaClassificatoreAndTipoElementoBilancio(Ente ente, List<TipologiaClassificatore> tipologiaClassificatore, Integer anno, String tipoElementoBilancio) {
		
		Set<SiacDClassTipoEnum> siacDClassTipoEnums = SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore);
		Set<String> classifTipoCodes = new HashSet<String>();
		for(SiacDClassTipoEnum sdcte : siacDClassTipoEnums) {
			classifTipoCodes.add(sdcte.getCodice());
		}
		List<SiacDClassTipo> siacDClassTipos = siacTClassRepository.findSiacDClassTipoByEnteProprietarioIdAndClassifTipoCodesAndAnnoAndElemTipoCodeAndExistsSiacTClass(
				ente.getUid(), classifTipoCodes, anno, tipoElementoBilancio);
		return convertiLista(siacDClassTipos, TipoClassificatore.class, BilMapId.SiacDClassTipo_TipoClassificatore);
	}
}
