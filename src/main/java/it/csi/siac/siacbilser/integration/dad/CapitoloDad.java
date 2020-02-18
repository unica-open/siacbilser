/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRBilElemTipoAttrIdElemCodeRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRBilElemTipoClassTipElemCodeRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.utility.CompareOperator;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.model.CapitoloModelDetail;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;

/*
			 _____             _ _        _      ______          _ 
			/  __ \           (_) |      | |     |  _  \        | |
			| /  \/ __ _ _ __  _| |_ ___ | | ___ | | | |__ _  __| |
welcome to	| |    / _` | '_ \| | __/ _ \| |/ _ \| | | / _` |/ _` |
			| \__/\ (_| | |_) | | || (_) | | (_) | |/ / (_| | (_| |
			 \____/\__,_| .__/|_|\__\___/|_|\___/|___/ \__,_|\__,_|
			            | |                                        
			            |_|                                        
 */

/**
 * The Class CapitoloDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
@Primary
public class CapitoloDad extends ExtendedBaseDadImpl {
	
	/** The bilancio. */
	protected Bilancio bilancio;
	
	/** The capitolo dao. */
	@Autowired
	protected CapitoloDao capitoloDao;
	
	/** The siac t bil elem repository. */
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	/** The siac r bil elem tipo class tip elem code repository. */
	@Autowired
	private SiacRBilElemTipoClassTipElemCodeRepository siacRBilElemTipoClassTipElemCodeRepository;
	
	/** The siac r bil elem tipo attr id elem code repository. */
	@Autowired
	private SiacRBilElemTipoAttrIdElemCodeRepository siacRBilElemTipoAttrIdElemCodeRepository;
	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	/** The siac t class dao. */
	@Autowired
	private SiacTClassDao siacTClassDao;
		
	/** The eef. */
	@Autowired
	protected EnumEntityFactory eef;
	
	
	/**
	 * Ottiene le informazioni di base di un capitolo a partire dal suo Uid:
	 * Anno,numero,articolo,ueb, tipoCapitolo, .
	 *
	 * @param uid the uid
	 * @return the capitolo
	 */
	public Capitolo<?, ?> findOne(Integer uid){
		final String methodName = "findOne";		
		log.debug(methodName, "uid: "+ uid);
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(uid);
		
		if(bilElem == null){
			throw new IllegalArgumentException("Capitolo con uid: "+ uid +" non trovato.");
		}
		
		Capitolo<?, ?> c = SiacDBilElemTipoEnum.byCodice(bilElem.getSiacDBilElemTipo().getElemTipoCode()).getCapitoloInstance();
		map(bilElem, c, BilMapId.SiacTBilElem_Capitolo_Base);
		
		return c;
	}
	
	/**
	 * Ottiene le informazioni minime di un capitolo a partire dal suo Uid
	 *
	 * @param uid the uid
	 * @return the capitolo
	 */
	public Capitolo<?, ?> findOneWithMinimalData(Integer uid, CapitoloModelDetail... modelDetails) {
		final String methodName = "findOneWithMinimalData";
		log.debug(methodName, "uid: "+ uid);
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(uid);
		
		if(bilElem == null){
			throw new IllegalArgumentException("Capitolo con uid: "+ uid +" non trovato.");
		}
		Capitolo<?, ?> c = SiacDBilElemTipoEnum.byCodice(bilElem.getSiacDBilElemTipo().getElemTipoCode()).getCapitoloInstance();
		map(bilElem, c, BilMapId.SiacTBilElem_Capitolo_Minimal, Converters.byModelDetails(modelDetails));
		
		return c;
	}
	
	/**
	 * Gets the bilancio associato a capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the bilancio associato a capitolo
	 */
	@Transactional
	public Bilancio getBilancioAssociatoACapitolo(Capitolo<?, ?> capitolo) {
		final String methodName = "getBilancioAssociatoACapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(capitolo.getUid());
		
		SiacTBil siacTBil = bilElem.getSiacTBil();
		
		return map(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);
		
	}
	
	
	/**
	 * Ricerca classificatore generico by class tipo and uid.
	 *
	 * @param <T> the generic type
	 * @param uid the uid
	 * @param tipoClassificatore the tipo classificatore
	 * @return the t
	 */
	protected <T extends ClassificatoreGenerico> T ricercaClassificatoreGenericoByClassTipoAndUid(Integer uid, SiacDClassTipoEnum tipoClassificatore) {		
		T classif = ricercaClassificatoreByClassTipoAndUid(uid,tipoClassificatore);
		return classif;
	}
	
	/**
	 * Ricerca classificatore gerarchico by class tipo and uid.
	 *
	 * @param <T> the generic type
	 * @param uid the uid
	 * @param tipoClassificatore the tipo classificatore
	 * @return the t
	 */
	protected <T extends ClassificatoreGerarchico> T ricercaClassificatoreGerarchicoByClassTipoAndUid(Integer uid, SiacDClassTipoEnum tipoClassificatore) {		
		T classif = ricercaClassificatoreByClassTipoAndUid(uid,tipoClassificatore);
		return classif;
	}
	
	/**
	 * Ricerca classificatore by class tipo and uid.
	 *
	 * @param <T> the generic type
	 * @param uid - id elemento di bilanico (capitolo UEB) a cui è associato il classificatore
	 * @param tipoClassificatore the tipo classificatore
	 * @return the t
	 */
	protected <T extends Codifica> T ricercaClassificatoreByClassTipoAndUid(Integer uid, SiacDClassTipoEnum tipoClassificatore) {
		final String methodName = "ricercaClassificatoreByTipoAndUid";		
		
		List<SiacTClass> classes = siacTBilElemRepository.ricercaClassificatoriByClassTipo(uid,tipoClassificatore.getCodice());		
		
		if(log.isDebugEnabled()){
			for (SiacTClass siacTClass : classes) {			
				log.debug(methodName, tipoClassificatore+" id: "+siacTClass.getClassifId()+" code: "+siacTClass.getClassifCode() + " desc: "+siacTClass.getClassifDesc() +
				" [DClassTipo: "+siacTClass.getSiacDClassTipo().getClassifTipoId() +"|"+siacTClass.getSiacDClassTipo().getClassifTipoCode() + "|" +siacTClass.getSiacDClassTipo().getClassifTipoDesc()+"]");
				
			}
		}
		
		for (SiacTClass siacTClass : classes) {			
			log.debug(methodName, "Returning "+tipoClassificatore+" id: "+siacTClass.getClassifId()+" code: "+siacTClass.getClassifCode() + " desc: "+siacTClass.getClassifDesc() +
			" [DClassTipo: "+siacTClass.getSiacDClassTipo().getClassifTipoId() +"|"+siacTClass.getSiacDClassTipo().getClassifTipoCode() + "|" +siacTClass.getSiacDClassTipo().getClassifTipoDesc()+"]");
			
			
			
			T classificatore = tipoClassificatore.getCodificaInstance(); //Come se facesse ad esempio: ElementoPianoDeiConti classificatore = new ElementoPianoDeiConti();
			if(classificatore instanceof ClassificatoreGerarchico){
				map(siacTClass,classificatore,BilMapId.SiacTClass_ClassificatoreGerarchico);
			} else {
				map(siacTClass,classificatore,BilMapId.SiacTClass_ClassificatoreGenerico);
			}
					
			
			return classificatore; //FIXME Occhio sto restituendo il primo che trovo!! In teoria sul db dovrebbe essercene uno solo (per quel tipo di classificatore) associato ad un elemento di bilancio? 
									//In teoria sì uno solo per tipo!
		}
		
		
		return null;
	}
	
	
	
	/**
	 * Ricerca classificatore by class fam and uid.
	 *
	 * @param <T> the generic type
	 * @param uid - id elemento di bilancio a cui è associato il classificatore
	 * @param tipoClassificatore the tipo classificatore
	 * @return the t
	 */
	protected <T extends Codifica> T ricercaClassificatoreByClassFamAndUid(Integer uid, SiacDClassFamEnum tipoClassificatore) {
		final String methodName = "ricercaClassificatoreByTipoAndUid";		
		
		List<SiacTClass> classes = siacTBilElemRepository.ricercaClassificatoriByClassFam(uid,tipoClassificatore.getCodice());		
		
		if(log.isDebugEnabled()){
			for (SiacTClass siacTClass : classes) {			
				log.debug(methodName, tipoClassificatore+" id: "+siacTClass.getClassifId()+" code: "+siacTClass.getClassifCode() + " desc: "+siacTClass.getClassifDesc() +
				" [DClassTipo: "+siacTClass.getSiacDClassTipo().getClassifTipoId() +"|"+siacTClass.getSiacDClassTipo().getClassifTipoCode() + "|" +siacTClass.getSiacDClassTipo().getClassifTipoDesc()+"]");
				
			}
		}
		
		for (SiacTClass siacTClass : classes) {			
			log.debug(methodName, "Returning "+tipoClassificatore+" id: "+siacTClass.getClassifId()+" code: "+siacTClass.getClassifCode() + " desc: "+siacTClass.getClassifDesc() +
			" [DClassTipo: "+siacTClass.getSiacDClassTipo().getClassifTipoId() +"|"+siacTClass.getSiacDClassTipo().getClassifTipoCode() + "|" +siacTClass.getSiacDClassTipo().getClassifTipoDesc()+"]");
			
			//ElementoPianoDeiConti classificatore = new ElementoPianoDeiConti();
			
			T classificatore = tipoClassificatore.getCodificaInstance();
			map(siacTClass,classificatore,BilMapId.SiacTClass_ClassificatoreGerarchico);
			return classificatore; //FIXME Occhio sto restituendo il primo che trovo!! In teoria sul db dovrebbe essercene solo uno associato ad un elemento di bilancio?
		}
		
		
		return null;
	}
	
	
	/**
	 * Ricerca classificatore elemento piano dei conti capitolo.
	 *
	 * @param uidCap the uid cap
	 * @return the elemento piano dei conti
	 */
	protected ElementoPianoDeiConti ricercaClassificatoreElementoPianoDeiContiCapitolo(Integer uidCap) {
		return ricercaClassificatoreByClassFamAndUid(uidCap, SiacDClassFamEnum.PianoDeiConti);		
	}
	
	/**
	 * Ricerca classificatore cofog capitolo.
	 *
	 * @param uidCap the uid cap
	 * @return the classificazione cofog
	 */
	protected ClassificazioneCofog ricercaClassificatoreCofogCapitolo(Integer uidCap) {
		return ricercaClassificatoreByClassFamAndUid(uidCap, SiacDClassFamEnum.Cofog);	
	}
	
	/**
	 * Ricerca classificatore struttura amministrativa contabile capitolo.
	 *
	 * @param uidCap the uid cap
	 * @return the struttura amministrativo contabile
	 */
	protected StrutturaAmministrativoContabile ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(Integer uidCap) {
		StrutturaAmministrativoContabile sac =  ricercaClassificatoreByClassFamAndUid(uidCap, SiacDClassFamEnum.StrutturaAmministrativaContabile);
		if(sac!=null){
			String assessorato = ricercaAttributoTestoClassificatore(sac.getUid(), "assessorato");
			sac.setAssessorato(assessorato);
		}
		return sac;
	}
	
	/**
	 * Ricerca classificatore siope spesa.
	 *
	 * @param uidCap the uid cap
	 * @return the siope spesa
	 */
	public SiopeSpesa ricercaClassificatoreSiopeSpesa(Integer uidCap) {
		return ricercaClassificatoreByClassFamAndUid(uidCap, SiacDClassFamEnum.SiopeSpesa);
	}
	
	/**
	 * Ricerca classificatore siope entrata.
	 *
	 * @param uidCap the uid cap
	 * @return the siope entrata
	 */
	public SiopeEntrata ricercaClassificatoreSiopeEntrata(Integer uidCap) {
		return ricercaClassificatoreByClassFamAndUid(uidCap, SiacDClassFamEnum.SiopeEntrata);
	}
	
	/**
	 * Ricerca classificatore missione.
	 *
	 * @param uidCap the uid cap
	 * @param programma the programma
	 * @return the missione
	 */
	public Missione ricercaClassificatoreMissione(Integer uidCap, Programma programma) {
		
		if(programma!=null){
			return classificatoriDad.ricercaPadreProgramma(programma);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.Missione);			
	}
	
	/**
	 * Ricerca classificatore programma.
	 *
	 * @param uidCap the uid cap
	 * @return the programma
	 */
	public Programma ricercaClassificatoreProgramma(Integer uidCap) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.Programma);		
	}
	
	/**
	 * Ricerca classificatore titolo spesa.
	 *
	 * @param uidCap the uid cap
	 * @param macroaggregato the macroaggregato
	 * @return the titolo spesa
	 */
	public TitoloSpesa ricercaClassificatoreTitoloSpesa(Integer uidCap, Macroaggregato macroaggregato) {
		
		if(macroaggregato!=null){
			return classificatoriDad.ricercaPadreMacroaggregato(macroaggregato);
		} 
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.TitoloSpesa);			
	}
	
	/**
	 * Ricerca classificatore macroaggregato.
	 *
	 * @param uidCap the uid cap
	 * @return the macroaggregato
	 */
	public Macroaggregato ricercaClassificatoreMacroaggregato(Integer uidCap) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.Macroaggregato);		
	}
	
	/**
	 * Ricerca classificatore tipo finanziamento.
	 *
	 * @param uidCap the uid cap
	 * @return the tipo finanziamento
	 */
	protected TipoFinanziamento ricercaClassificatoreTipoFinanziamento(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.TipoFinanziamento);		
	}
	
	/**
	 * Ricerca classificatore tipo fondo.
	 *
	 * @param uidCap the uid cap
	 * @return the tipo fondo
	 */
	protected TipoFondo ricercaClassificatoreTipoFondo(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.TipoFondo);		
	}
	
	/**
	 * Ricerca classificatore ricorrente spesa.
	 *
	 * @param uidCap the uid cap
	 * @return the ricorrente spesa
	 */
	public RicorrenteSpesa ricercaClassificatoreRicorrenteSpesa(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.RicorrenteSpesa);		
	}
	
	/**
	 * Ricerca classificatore ricorrente entrata.
	 *
	 * @param uidCap the uid cap
	 * @return the ricorrente entrata
	 */
	public RicorrenteEntrata ricercaClassificatoreRicorrenteEntrata(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.RicorrenteEntrata);		
	}
	
	/**
	 * Ricerca classificatore perimetro sanitario spesa.
	 *
	 * @param uidCap the uid cap
	 * @return the perimetro sanitario spesa
	 */
	public PerimetroSanitarioSpesa ricercaClassificatorePerimetroSanitarioSpesa(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.PerimetroSanitarioSpesa);		
	}
	
	/**
	 * Ricerca classificatore perimetro sanitario entrata.
	 *
	 * @param uidCap the uid cap
	 * @return the perimetro sanitario entrata
	 */
	public PerimetroSanitarioEntrata ricercaClassificatorePerimetroSanitarioEntrata(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.PerimetroSanitarioEntrata);		
	}
	
	/**
	 * Ricerca classificatore transazione unione europea spesa.
	 *
	 * @param uidCap the uid cap
	 * @return the transazione unione europea spesa
	 */
	public TransazioneUnioneEuropeaSpesa ricercaClassificatoreTransazioneUnioneEuropeaSpesa(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.TransazioneUnioneEuropeaSpesa);		
	}
	
	/**
	 * Ricerca classificatore transazione unione europea entrata.
	 *
	 * @param uidCap the uid cap
	 * @return the transazione unione europea entrata
	 */
	public TransazioneUnioneEuropeaEntrata ricercaClassificatoreTransazioneUnioneEuropeaEntrata(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.TransazioneUnioneEuropeaEntrata);		
	}
		
	/**
	 * Ricerca classificatore politiche regionali unitarie.
	 *
	 * @param uidCap the uid cap
	 * @return the politiche regionali unitarie
	 */
	public PoliticheRegionaliUnitarie ricercaClassificatorePoliticheRegionaliUnitarie(Integer uidCap) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, SiacDClassTipoEnum.PoliticheRegionaliUnitarie);		
	}
	
	/**
	 * Ricerca un classificatore specificandone il tipo
	 *
	 * @param uidCap the uid cap
	 * @param tipologiaClassificatore the tipologia classificatore
	 * @return the politiche regionali unitarie
	 */
	public <T extends ClassificatoreGenerico> T ricercaClassificatoreGenerico(Integer uidCap, TipologiaClassificatore tipologiaClassificatore) {
		return ricercaClassificatoreGenerico(uidCap, tipologiaClassificatore, null);
	}
			
	/**
	 * Ricerca un classificatore specificandone il tipo
	 * Se però il tipo non è compreso nella lista tipologieClassificatoriRichiesti allora restituisce null.
	 *
	 * @param uidCap the uid cap
	 * @param tipologiaClassificatore the tipologia classificatore
	 * @return the politiche regionali unitarie
	 */
	public <T extends ClassificatoreGenerico> T ricercaClassificatoreGenerico(Integer uidCap, TipologiaClassificatore tipologiaClassificatore, Set<TipologiaClassificatore> tipologieClassificatoriRichiesti) {
		if(tipologieClassificatoriRichiesti!= null && !tipologieClassificatoriRichiesti.contains(tipologiaClassificatore)) {
			return null;
		}
		
		SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore);
		return ricercaClassificatoreGenericoByClassTipoAndUid(uidCap, siacDClassTipoEnum);		
	}
		
	
	/**
	 * Ricerca classificatore gerarchico.
	 *
	 * @param uidCap the uid cap
	 * @param tipologiaClassificatore the tipologia classificatore
	 * @return the classificatore gerarchico
	 */
	public <T extends ClassificatoreGerarchico> T ricercaClassificatoreGerarchico(Integer uidCap, TipologiaClassificatore tipologiaClassificatore, Set<TipologiaClassificatore> tipologieClassificatoriRichiesti){
		return ricercaClassificatoreGerarchico(uidCap, tipologiaClassificatore, null, tipologieClassificatoriRichiesti);
	}
	
	
	/**
	 * Ricerca classificatore gerarchico.
	 * 
	 * Restituisce null se il classificatore non è nell'elenco di quelli richiesti.
	 *
	 * @param uidCap the uid cap
	 * @param tipologiaClassificatore the tipologia classificatore
	 * @param figlio the figlio
	 * @return the classificatore gerarchico
	 */
	public <T extends ClassificatoreGerarchico> T ricercaClassificatoreGerarchico(Integer uidCap, TipologiaClassificatore tipologiaClassificatore, ClassificatoreGerarchico figlio, Set<TipologiaClassificatore> tipologieClassificatoriRichiesti) {
		if(tipologieClassificatoriRichiesti!= null && !tipologieClassificatoriRichiesti.contains(tipologiaClassificatore)) {
			return null;
		}
		
		if(figlio!=null){
			return classificatoriDad.ricercaPadreClassificatore(figlio);
		}
		
		SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byTipologiaClassificatore(tipologiaClassificatore);
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(uidCap, siacDClassTipoEnum);			
	}
	
	
	
	/**
	 * Ricerca classificatori generici.
	 *
	 * @param uid the uid
	 * @return the list
	 */
	public List<ClassificatoreGenerico> ricercaClassificatoriGenerici(Integer uid) {
		return ricercaClassificatoriGenerici(uid, null);
	}
	
	/**
	 * Ricerca classificatori generici.
	 *
	 * @param uid the uid
	 * @return the list
	 */
	public List<ClassificatoreGenerico> ricercaClassificatoriGenerici(Integer uid, Set<TipologiaClassificatore> tipologieClassificatoriRichiesti) {
		List<ClassificatoreGenerico> result = new ArrayList<ClassificatoreGenerico>();
		
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore1, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore2, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore3, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore4, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore5, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore6, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore7, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore8, tipologieClassificatoriRichiesti);	
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore9, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore10, tipologieClassificatoriRichiesti);
		
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore31, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore32, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore33, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore34, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore35, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore36, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore37, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore38, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore39, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore40, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore41, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore42, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore43, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore44, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore45, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore46, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore47, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore48, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore49, tipologieClassificatoriRichiesti);
		addClassificatoreGenerico(result, uid, SiacDClassTipoEnum.Classificatore50, tipologieClassificatoriRichiesti);
		
		
		
		
		return result;
	}
	
	protected void addClassificatoreGenerico(List<ClassificatoreGenerico> result, Integer uid,  SiacDClassTipoEnum tipo) {
		addClassificatoreGenerico(result, uid, tipo, null);
	}
	
	/**
	 * Adds the classificatore generico.
	 *
	 * @param result the result
	 * @param uid the uid
	 * @param tipo the tipo
	 */
	protected void addClassificatoreGenerico(List<ClassificatoreGenerico> result, Integer uid,  SiacDClassTipoEnum tipo, Set<TipologiaClassificatore> tipologieClassificatoriRichiesti) {
		
		if(tipologieClassificatoriRichiesti != null && !tipologieClassificatoriRichiesti.contains(tipo.getTipologiaClassificatore())){
			return;
		}
		
		ClassificatoreGenerico cg = ricercaClassificatoreGenericoByClassTipoAndUid(uid, tipo);
		if(cg!=null){
			result.add(cg);
		}
	}
	
	/**
	 *  
	 * Popola i flag di un Capitolo.
	 * Gli attributi di tipo boolean sono legati ad un elemento di bilancio dalla siac_r_bil_elem_attr
	 * 
	 * 
	 * Da SiacTBilElem  a Capitolo
	 *
	 * @param <T> the generic type
	 * @param capitolo the capitolo
	 */
	protected <T extends Capitolo<?, ?>> void populateAttrs(T capitolo) {
		final String methodName = "populateAttrs";
		
		//TODO valutare di riscriverlo utilizzando prima ricercaAttributiAll
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(capitolo.getUid());
		
		BeanWrapper bCup = PropertyAccessorFactory.forBeanPropertyAccess(capitolo);
		
		log.debug(methodName, "numero attributi: " +bilElem.getSiacRBilElemAttrs().size());
		
		for (SiacRBilElemAttr rBilElemAttr : bilElem.getSiacRBilElemAttrs()) {
			String attrCode = rBilElemAttr.getSiacTAttr().getAttrCode();
			String fieldName;
			
			try {
				fieldName = SiacTAttrEnum.byCodice(attrCode).getModelFieldName();
			} catch (IllegalArgumentException e){
				log.debug(methodName, "Saltato attrCode: "+attrCode + " [" + e.getMessage()+"]");
				continue;
			} catch (NullPointerException npe){
				log.debug(methodName, "Saltato attrCode: "+attrCode + " [Non di tipo flag.]");
				continue;
			}
			
			Object fieldValue = getFieldAttrValue(rBilElemAttr);
			
			bCup.setPropertyValue(fieldName, fieldValue);
			
			log.info(methodName, "Set "+fieldName + " = "+fieldValue);
			
		}
		
		log.debug(methodName, "fine");
	}


	/**
	 * Gets the field attr value.
	 *
	 * @param rBilElemAttr the r bil elem attr
	 * @return the field attr value
	 */
	private Object getFieldAttrValue(SiacRBilElemAttr rBilElemAttr) {
		if(rBilElemAttr.getBoolean_()!=null){ //Tipo Boolean
			return "S".equalsIgnoreCase(rBilElemAttr.getBoolean_());
		} else if(rBilElemAttr.getTesto()!=null){ //Tipo Testo
			return rBilElemAttr.getTesto();
		} //TODO aggiungere qui gestione altri Tipi di attributo
		
		return null;
	}
	
	/**
	 * Da Capitolo a SiacTBilElem.
	 *
	 * @param <C> the generic type
	 * @param capitolo the capitolo
	 * @param bilElem the bil elem
	 */
	protected <C extends Capitolo<?, ?>> void mapAttrs(C capitolo, SiacTBilElem bilElem) {
		final String methodName = "mapAttrs";
		
		//Map<String, Boolean> attrs = Utility.getFieldNameValueMapByType(capitolo, Boolean.class);
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(capitolo);
		
		List<SiacRBilElemAttr> siacRBilElemAttrs = new ArrayList<SiacRBilElemAttr>();
		
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			
			SiacTAttrEnum tipoAttrEnum = SiacTAttrEnum.byCapitoloFieldName(fieldName);
			SiacTAttr tipoAttr;
			try {
				tipoAttr = eef.getEntity(tipoAttrEnum, ente.getUid(), SiacTAttr.class);	
			} catch (IllegalArgumentException iae) {				
				log.debug(methodName, "saltato fieldName: "+fieldName + " " + iae.getMessage());
				continue;
			}	
			
			
			SiacRBilElemAttr attr = new SiacRBilElemAttr();
			attr.setSiacTAttr(tipoAttr);
			setFieldAttrValue(fieldName, fieldValue,/*, tipoAttr*/ attr, tipoAttrEnum);		
			
			attr.setSiacTBilElem(bilElem);
			attr.setSiacTEnteProprietario(siacTEnteProprietario);
			attr.setLoginOperazione(loginOperazione);
			siacRBilElemAttrs.add(attr);
			
		}
		
		
		bilElem.setSiacRBilElemAttrs(siacRBilElemAttrs);
	}


	/**
	 * Sets the field attr value.
	 *
	 * @param fieldName the field name
	 * @param fieldValue the field value
	 * @param attr the attr
	 * @param tipoAttrEnum the tipo attr enum
	 */
	private void setFieldAttrValue( String fieldName, Object fieldValue, SiacRBilElemAttr attr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "setAttrFiledValue";
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			attr.setBoolean_(Boolean.TRUE.equals(fieldValue)?"S":"N");
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value: "+attr.getBoolean_());
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			attr.setTesto((String) fieldValue);
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value: "+attr.getTesto());
		} //TODO aggiungere qui la gestione per gli altri tipoi di attributo
	}
	

	/**
	 * Imposta la data di cancellazione uguale alla data di fine validità se presente.
	 *
	 * @param bilElem the new data cancellazione a partire da data fine validita
	 */
	protected void setDataCancellazioneAPartireDaDataFineValidita(SiacTBilElem bilElem) {		
		
		bilElem.setDataCancellazione(bilElem.getDataFineValidita());
		
		if(bilElem.getSiacRBilElemStatos()!=null){
			for(SiacRBilElemStato stato : bilElem.getSiacRBilElemStatos()){
				stato.setDataCancellazione(stato.getDataFineValidita());
				//Non sarà mai settata la data fine validità di un stato poichè assente sul modello di dominio
			}
		}
		
		if(bilElem.getSiacTBilElemDets()!=null) { 
			for(SiacTBilElemDet deb  :bilElem.getSiacTBilElemDets()){
				deb.setDataCancellazione(deb.getDataFineValidita()); 
				//Non sarà mai settata la data fine validità di un importo singolo poichè assente sul modello di dominio
			}
		}
		
		if(bilElem.getSiacRBilElemClasses()!=null) {
			for(SiacRBilElemClass rclassNew : bilElem.getSiacRBilElemClasses()){
				//rclassNew.setDataCancellazione(rclassNew.getDataFineValidita()); //da qui data fine validità sarà sempre null
				rclassNew.setDataCancellazione(rclassNew.getSiacTClass().getDataFineValidita());
				//devo prenderla dal classificatore perchè il puntamento alla relazione classificatore-capitolo non c'è sul modello di dominio
			}
		}
		
	}
	
	/**
	 * Ricerca un attributo di tipo testo di un classificatore.
	 *
	 * @param uid the uid
	 * @param codiceAttributo - codice attributo sulla tabella siac_t_attr
	 * @return the string
	 */
	protected String ricercaAttributoTestoClassificatore(int uid, String codiceAttributo) {
		return siacTClassRepository.findAttrTestoByCodeAndClassifId(uid,codiceAttributo);		
	}
	
	/**
	 * Restituisce il tipo del capitolo a partire da suo uid.
	 *
	 * @param elemId the elem id
	 * @return the tipo capitolo
	 */
	public TipoCapitolo findTipoCapitolo(int elemId){
		String elemTipoCode = siacTBilElemRepository.findElemTipoCodeByElemId(elemId);
		if(elemTipoCode == null){
			throw new IllegalArgumentException("Impossibile determinare il tipo per il capitolo con uid: "+ elemId + ". Verificare che esista. ");
		}
		return SiacDBilElemTipoEnum.byCodice(elemTipoCode).getTipoCapitolo();
	}
	
	
	/**
	 * Checks if is tipo entrata gestione.
	 *
	 * @param elemId the elem id
	 * @return the boolean
	 */
	public Boolean isTipoEntrataGestione(int elemId){
		String elemTipoCode = siacTBilElemRepository.findElemTipoCodeByElemId(elemId);
		return SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice().equals(elemTipoCode);
	}
	
	/**
	 * Checks if is tipo entrata previsione.
	 *
	 * @param elemId the elem id
	 * @return the boolean
	 */
	public Boolean isTipoEntrataPrevisione(int elemId){
		String elemTipoCode = siacTBilElemRepository.findElemTipoCodeByElemId(elemId);
		return SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice().equals(elemTipoCode);
	}
	
	/**
	 * Checks if is tipo uscita gestione.
	 *
	 * @param elemId the elem id
	 * @return the boolean
	 */
	public Boolean isTipoUscitaGestione(int elemId){
		String elemTipoCode = siacTBilElemRepository.findElemTipoCodeByElemId(elemId);
		return SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice().equals(elemTipoCode);
	}
	
	/**
	 * Checks if is tipo uscita previsione.
	 *
	 * @param elemId the elem id
	 * @return the boolean
	 */
	public Boolean isTipoUscitaPrevisione(int elemId){
		String elemTipoCode = siacTBilElemRepository.findElemTipoCodeByElemId(elemId);
		return SiacDBilElemTipoEnum.CapitoloUscitaPrevisione.getCodice().equals(elemTipoCode);
	}
	
	/**
	 * Count capitoli.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @return the long
	 */
	public Long countCapitoli(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB){
		
		String codiceTipoCapitolo = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice();
		return siacTBilElemRepository.countCapitoli(ente.getUid(),""+bilancio.getAnno(),codiceTipoCapitolo, mapToString(numeroCapitolo), mapToString(numeroArticolo), mapToString(numeroUEB));
	}
	
	/**
	 * Ricerca l'elenco degli id di SiacTBilElem per l'Ente e il Bilancio impostato che riscontrano i parametri di ricerca indicati come parametro. 
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param stato - facoltativo
	 * @return the list
	 */
	public List<Integer> findCapitoliElemIds(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB, StatoOperativoElementoDiBilancio stato){
		
		String elemTipoCode = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice();
		SiacDBilElemStatoEnum bilElemStato = SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancioEvenNull(stato);
		String elemStatoCode = bilElemStato!=null?bilElemStato.getCodice():null;
		
		List<Integer> result =  siacTBilElemRepository.findCapitoliElemIds(ente.getUid(),""+bilancio.getAnno(),elemTipoCode, mapToString(numeroCapitolo), mapToString(numeroArticolo), mapToString(numeroUEB), elemStatoCode);
		log.debug("findCapitoliElemIds", "trovati capitoli con uid: "+ result);
		return result;
		
	}
	
	
	/**
	 * Find classificatori legati a capitolo.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @return the map
	 */
	public Map<TipologiaClassificatore, Integer> findClassificatoriLegatiACapitolo(TipoCapitolo tipoCapitolo, Integer numeroCapitolo){
		return findClassificatoriLegati(tipoCapitolo, Arrays.asList(1), numeroCapitolo, null);
	}
	
	/**
	 * Find classificatori legati a capitolo articolo.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @return the map
	 */
	public Map<TipologiaClassificatore, Integer> findClassificatoriLegatiACapitoloArticolo(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo){
		return findClassificatoriLegati(tipoCapitolo, Arrays.asList(1,2), numeroCapitolo, numeroArticolo);
	}
	
	/**
	 * Find classificatori legati a capitolo articolo ueb.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @return the map
	 */
	public Map<TipologiaClassificatore, Integer> findClassificatoriLegatiACapitoloArticoloUEB(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo){
		return findClassificatoriLegati(tipoCapitolo, Arrays.asList(1,2,3), numeroCapitolo, numeroArticolo);
	}
	



	/**
	 * Find classificatori legati.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param elemCodes the elem codes
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @return the map
	 */
	private Map<TipologiaClassificatore, Integer> findClassificatoriLegati(TipoCapitolo tipoCapitolo, List<Integer> elemCodes, Integer numeroCapitolo, Integer numeroArticolo) {
		final String methodName = "findClassificatoriLegati";
		
		List<SiacDClassTipo> classTipoLegati = siacRBilElemTipoClassTipElemCodeRepository.findClassTipoLegati(SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice(),  elemCodes /*Arrays.asList("1","2","3")*/, ente.getUid());
		log.debug(methodName, "classTipoLegati: "+classTipoLegati + " [" + tipoCapitolo + "/" + numeroCapitolo + "/" + numeroArticolo + "/"+ elemCodes + "]");
			
		
		Map<TipologiaClassificatore, Integer> result = new HashMap<TipologiaClassificatore, Integer>();
		List<Integer> elemIds = findCapitoliElemIds(tipoCapitolo, numeroCapitolo, numeroArticolo, null, StatoOperativoElementoDiBilancio.VALIDO);
		if (elemIds == null || elemIds.isEmpty()) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning empty map.");
			return result;
		}
		
		Integer bilElemId = elemIds.get(0); //La prima occorrenza in stato valido!
		log.debug(methodName, "bilElemId: "+bilElemId);
		
		
		Map<String, SiacTClass> classAssociati = ricercaClassificatoriAll(bilElemId);
				
		
		for (SiacDClassTipo classTipoLegato : classTipoLegati) {
				
			
			SiacDClassTipoEnum tipoEnum = SiacDClassTipoEnum.byCodice(classTipoLegato.getClassifTipoCode());
			
			SiacTClass classAssociato = classAssociati.get(classTipoLegato.getClassifTipoCode());			

			Integer idClassificatore = classAssociato!=null?classAssociato.getClassifId():null;			
			
			result.put(tipoEnum.getTipologiaClassificatore(), idClassificatore);

			
		}
		
		return result;
	}
	
	public Map<TipologiaClassificatore, Integer> findClassificatoriLegati(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB, TipologiaClassificatore... tipiClassif) {
		return findClassificatoriLegati(tipoCapitolo, numeroCapitolo, numeroArticolo, numeroUEB, Arrays.asList(tipiClassif));
		
	}
	public Map<TipologiaClassificatore, Integer> findClassificatoriLegati(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB, List<TipologiaClassificatore> tipiClassif) {
		final String methodName = "findClassificatoriLegati";
		
		Map<TipologiaClassificatore, Integer> result = new HashMap<TipologiaClassificatore, Integer>();
		List<Integer> elemIds = findCapitoliElemIds(tipoCapitolo, numeroCapitolo, numeroArticolo, numeroUEB, StatoOperativoElementoDiBilancio.VALIDO);
		if (elemIds == null || elemIds.isEmpty()) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning empty map.");
			return result;
		}
		
		Integer bilElemId = elemIds.get(0); //La prima occorrenza in stato valido!
		log.debug(methodName, "bilElemId: "+bilElemId);
		
		
		Set<SiacDClassTipoEnum> classTipi = SiacDClassTipoEnum.byTipologiaClassificatore(tipiClassif);
		Set<SiacDClassFamEnum> classFams  = SiacDClassFamEnum.byTipologiaClassificatore(tipiClassif);
		
		Map<String, SiacTClass> classAssociati = ricercaClassificatori(bilElemId, classFams, classTipi);
		
		
		for (SiacDClassTipoEnum classTipoLegato : classTipi) {
				
			SiacTClass classAssociato = classAssociati.get(classTipoLegato.getCodice());			

			Integer idClassificatore = classAssociato!=null?classAssociato.getClassifId():null;			
			
			result.put(classTipoLegato.getTipologiaClassificatore(), idClassificatore);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Find attributi legati a capitolo.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @return the map
	 */
	@Transactional
	public Map<TipologiaAttributo, Object> findAttributiLegatiACapitolo(TipoCapitolo tipoCapitolo, Integer numeroCapitolo){
		return findAttributiLegati(tipoCapitolo, Arrays.asList(1), numeroCapitolo, null);
	}
	
	/**
	 * Find attributi legati a capitolo articolo.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @return the map
	 */
	@Transactional
	public Map<TipologiaAttributo, Object> findAttributiLegatiACapitoloArticolo(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo){
		return findAttributiLegati(tipoCapitolo, Arrays.asList(1,2), numeroCapitolo, numeroArticolo);
	}
	
	/**
	 * Find attributi legati a capitolo articolo ueb.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @return the map
	 */
	@Transactional
	public Map<TipologiaAttributo, Object> findAttributiLegatiACapitoloArticoloUEB(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo){
		return findAttributiLegati(tipoCapitolo, Arrays.asList(1,2,3), numeroCapitolo, numeroArticolo);
	}
	
	/**
	 * Find attributi legati.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param elemCodes the elem codes
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @return the map
	 */
	private Map<TipologiaAttributo, Object> findAttributiLegati(TipoCapitolo tipoCapitolo, List<Integer> elemCodes, Integer numeroCapitolo, Integer numeroArticolo) {
		final String methodName = "findAttributiLegati";
		
		List<SiacTAttr> attrIdLegati = siacRBilElemTipoAttrIdElemCodeRepository.findAttrIdLegati(SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice(),  elemCodes /*Arrays.asList("1","2","3")*/, ente.getUid());
		log.debug(methodName, "classTipoLegati: "+attrIdLegati + " [" + tipoCapitolo + "/" + numeroCapitolo + "/" + numeroArticolo + "/"+ elemCodes + "]");
			
		
		Map<TipologiaAttributo, Object> result = new HashMap<TipologiaAttributo, Object>();
		List<Integer> elemIds = findCapitoliElemIds(tipoCapitolo, numeroCapitolo, numeroArticolo, null, StatoOperativoElementoDiBilancio.VALIDO);
		if (elemIds == null || elemIds.isEmpty()) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning empty map.");
			return result;
		}
		
		Integer bilElemId = elemIds.get(0); //La prima occorrenza in stato valido!
		log.debug(methodName, "bilElemId: "+bilElemId);
		
		
		Map<String, Object> attrAssociati = ricercaAttributiAll(bilElemId);
				
		
		for (SiacTAttr attrLegato : attrIdLegati) {				
			
			SiacTAttrEnum attrEnum = SiacTAttrEnum.byCodice(attrLegato.getAttrCode());
			
			Object valoreAttributo = attrAssociati.get(attrEnum.getCodice());			
			result.put(attrEnum.getTipologiaAttributo(), valoreAttributo);
			
		}
		
		return result;
	}
		
	/**
	 * Restituisce tutti gli attributi associati ad un Capitolo con id passato.
	 *
	 * @param bilElemId the bil elem id
	 * @return mappa che ha per chiave il codice attributo e valore il valore dell'attributo.
	 */
	protected Map<String, Object> ricercaAttributiAll(Integer bilElemId) {
		final String methodName = "ricercaAttributiAll";
		
		Map<String, Object> result = new HashMap<String, Object>(); 
				
		
		log.debug(methodName, "bilElemId: "+ bilElemId);
		
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(bilElemId);		
		
		log.debug(methodName, "numero attributi: " +bilElem.getSiacRBilElemAttrs().size());
		
		for (SiacRBilElemAttr rBilElemAttr : bilElem.getSiacRBilElemAttrs()) {
			String attrCode = rBilElemAttr.getSiacTAttr().getAttrCode();			
			Object fieldValue = getFieldAttrValue(rBilElemAttr);			
			
			result.put(attrCode, fieldValue);
			
		}
		
		log.debug(methodName, "returnin map: "+result);	
		
		return result;
	}

	/**
	 * Ricerca un classificatore gerarchico associato ad un elemento di bilancio partendo dalla sua famiglia.
	 * Restituisce una mappa con tutti i padri associatia quel classificatore.
	 * La mappa contiene come chiave il Codice Tipo classificatore e come valore il classificatore. 
	 *
	 * @param bilElemId the bil elem id
	 * @param classFam the class fam
	 * @return the map
	 */
	protected Map<String, SiacTClass> ricercaClassificatoriMapByClassFam(Integer bilElemId, SiacDClassFamEnum classFam) {
		Map<String, SiacTClass> result = new HashMap<String, SiacTClass>();
		
		List<SiacTClass> listClass = siacTBilElemRepository.ricercaClassificatoriByClassFam(bilElemId,classFam.getCodice());
		if(!listClass.isEmpty()) {
			SiacTClass classif = listClass.get(0);		
			siacTClassDao.ricercaClassifRicorsivaByClassifId(result,classif);		
		}
		return result;
	}


	/**
	 * Restituisce una mappa di tutti i classificatori associati al capitolo con id passato come parametro.
	 * 
	 * La mappa contiene come chiave il Codice Tipo classificatore e come valore il classificatore. 
	 *
	 * @param bilElemId the bil elem id
	 * @return the map
	 */
	protected Map<String, SiacTClass> ricercaClassificatoriAll(Integer bilElemId) {
		
		Map<String, SiacTClass> result = new HashMap<String, SiacTClass>();
				
		//Classificatori gerarchici
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.SpesaMissioniprogrammi));
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.SpesaTitolimacroaggregati ));
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.EntrataTitolitipologiecategorie));
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.StrutturaAmministrativaContabile));
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.PianoDeiConti));
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.Cofog));
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.SiopeSpesa));
		result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, SiacDClassFamEnum.SiopeEntrata));
		
		//Classificatori generici
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.TipoFinanziamento));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.TipoFondo));		
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.RicorrenteSpesa));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.RicorrenteEntrata));		
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.PerimetroSanitarioSpesa));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.PerimetroSanitarioEntrata));		
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.TransazioneUnioneEuropeaSpesa));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.TransazioneUnioneEuropeaEntrata));		
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.PoliticheRegionaliUnitarie));		
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore1));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore2));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore3));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore4));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore5));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore6));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore7));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore8));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore9));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore10));
		
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore31));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore32));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore33));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore34));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore35));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore36));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore37));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore38));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore39));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore40));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore41));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore42));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore43));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore44));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore45));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore46));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore47));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore48));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore49));
		result.putAll(ricercaClassificatoreMapByTipo(bilElemId, SiacDClassTipoEnum.Classificatore50));
		
		return result;
	}

	
	protected Map<String, SiacTClass> ricercaClassificatori(Integer bilElemId, Set<SiacDClassFamEnum> famiglie, Set<SiacDClassTipoEnum> tipi) {
		Map<String, SiacTClass> result = new HashMap<String, SiacTClass>();
		
		if(famiglie!=null) {
			for(SiacDClassFamEnum famiglia: famiglie){
				result.putAll(ricercaClassificatoriMapByClassFam(bilElemId, famiglia));
			}
		}
		
		if(tipi!=null) {
			for(SiacDClassTipoEnum tipo: tipi){
				result.putAll(ricercaClassificatoreMapByTipo(bilElemId, tipo));
			}
		}
		
		return result;
	}

	/**
	 * Ricerca un classificatore generico per tipo.
	 *
	 * @param bilElemId the bil elem id
	 * @param tipoClassif the tipo classif
	 * @return the map
	 */
	protected Map<String, SiacTClass> ricercaClassificatoreMapByTipo(Integer bilElemId, SiacDClassTipoEnum tipoClassif) {
		Map<String, SiacTClass> result = new HashMap<String, SiacTClass>();
		List<SiacTClass> tfins = siacTBilElemRepository.ricercaClassificatoriByClassTipo(bilElemId, tipoClassif.getCodice());	
		if(!tfins.isEmpty()){
			result.put(tipoClassif.getCodice(), tfins.get(0));
		}
		return result;
	}

	
	/**
	 * Imposta lo stato di un capitolo.
	 *
	 * @param uidCapitolo the uid capitolo
	 * @param soeb the soeb
	 */
	@Transactional
	public void aggiornaStato(Integer uidCapitolo, StatoOperativoElementoDiBilancio soeb) {
		final String methodName = "aggiornaStato";
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(uidCapitolo);
		log.info(methodName, "aggiorna stato capitolo con uid: "+ uidCapitolo +" a: "+soeb);
		for(SiacRBilElemStato stato : bilElem.getSiacRBilElemStatos()){ //Per come è ora ce ne è uno solo
			SiacDBilElemStato siacDBilElemStato = eef.getEntity(SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(soeb), ente.getUid(), SiacDBilElemStato.class);
			stato.setSiacDBilElemStato(siacDBilElemStato);
		}
		
		
	}
	
	/**
	 * Aggiorna i classificatori di un capitolo; aggiunge quelli nuovi e sostituisce quelli vecchi lasciando invariato tutto il resto.
	 *
	 * @param capitolo the capitolo
	 * @param classifGenerici the classif generici
	 * @param classifGerarchici the classif gerarchici
	 */
	public void aggiornaClassificatoriCapitolo(Capitolo<?, ?> capitolo, List<ClassificatoreGenerico> classifGenerici,	List<ClassificatoreGerarchico> classifGerarchici) {
		
		SiacTBilElem bilElem = new SiacTBilElem(); //siacTBilElemRepository.findOne(capitolo.getUid());
		bilElem.setLoginOperazione(loginOperazione);
		map(ente, bilElem, BilMapId.SiacTBilElem_Ente);	
		
		bilElem.setUid(capitolo.getUid());
		
		for (ClassificatoreGenerico classificatoreGenerico : classifGenerici) {
			mapNotNull(classificatoreGenerico, bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		}
		
		for (ClassificatoreGerarchico classificatoreGerarchico : classifGerarchici) {
			mapNotNull(classificatoreGerarchico, bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		}	
		
		setDataCancellazioneAPartireDaDataFineValidita(bilElem);
		
		//capitoloDao.update(bilElem);
		capitoloDao.aggiornaClassificatori(bilElem);
		
		
	}
	
	/**
	 * Aggiorna i dati di base del capitolo per le Variazioni.
	 * Per ora l'implementazione attuale aggiorna solo le descrizioni capitolo ed articolo.
	 * 
	 * @param capitolo
	 */
	public void aggiornaDatiDiBaseCapitolo(Capitolo<?, ?> capitolo) {
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(capitolo.getUid());
		siacTBilElem.setElemDesc(capitolo.getDescrizione());
		siacTBilElem.setElemDesc2(capitolo.getDescrizioneArticolo());
	}
	
	/**
	 * Sets the bilancio.
	 *
	 * @param bilancio the new bilancio
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	public ElementoPianoDeiConti findPianoDeiContiCapitolo(int uidCapitolo) {
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(uidCapitolo);
		ElementoPianoDeiConti epdc = new ElementoPianoDeiConti();
		
		// Elemento del piano dei conti
		SiacRBilElemClass siacRBilElemClass = siacTBilElemRepository.findBilElemClassByTipoClassCodes(siacTBilElem.getUid(),
				Arrays.asList(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice(), SiacDClassTipoEnum.QuintoLivelloPdc.getCodice()));
		
		if(siacRBilElemClass != null) {
			SiacTClass siacTClass = siacRBilElemClass.getSiacTClass();
			epdc.setUid(siacTClass.getUid());
			epdc.setCodice(siacTClass.getClassifCode());
			epdc.setDescrizione(siacTClass.getClassifDesc());
			TipoClassificatore tipoClassificatore = new TipoClassificatore();
			tipoClassificatore.setCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
			tipoClassificatore.setUid(siacTClass.getSiacDClassTipo().getUid());
			epdc.setTipoClassificatore(tipoClassificatore);
		}
		return epdc;
	}

	/**
	 * Restituisce gli uid dei capitoli corrispondenti ala chiave passata come parametro
	 * @param tipoCapitolo
	 * @param annoCapitolo
	 * @param numeroCapitolo
	 * @param numeroArticolo
	 * @param numeroUEB
	 * @return
	 */
	public List<Integer> ricercaIdCapitoliPerChiave(TipoCapitolo tipoCapitolo, Integer annoCapitolo, Integer numeroCapitolo,
			Integer numeroArticolo, Integer numeroUEB) {
		
		SiacDBilElemTipoEnum siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo);
		return capitoloDao.ricercaIdCapitoli(ente.getUid(), mapToString(annoCapitolo), siacDBilElemTipoEnum.getCodice(), mapToString(numeroCapitolo), mapToString(numeroArticolo), mapToString(numeroUEB));
		
	}

	public Capitolo<?, ?> findCapitoloByMovimentoGestionre(Impegno movimentoGestione) {
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findBilElemByMovGest(movimentoGestione.getUid());
		return mapNotNull(siacTBilElem, Capitolo.class, BilMapId.SiacTBilElem_Capitolo_Base);
	}

	/**
	 * Popola gli importi del capitolo a partire dalla lista codice-valore.
	 * 
	 * @param ic gli importi
	 * @param objs la lista da cui ottenere i dati
	 */
	protected void popolaImporti(ImportiCapitolo ic, List<Object[]> objs) {
		BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(ic);
		for(Object[] obj : objs) {
			popolaImporto(bwic, obj);
		}
	}

	private void popolaImporto(BeanWrapper bwic, Object[] obj) {
		for(SiacDBilElemDetTipoEnum sdbedte : SiacDBilElemDetTipoEnum.values()) {
			if(sdbedte.getCodice().equals(obj[0])) {
				bwic.setPropertyValue(sdbedte.getImportiCapitoloFieldName(), obj[1]);
				return;
			}
		}
	}
	
	
	public Long countMovimentiNonAnnullatiCapitolo(int uidCapitolo){
		final String methodName = "countMovimentiNonAnnullatiCapitolo";		
		log.debug(methodName, "uid capitolo: "+ uidCapitolo);
		
		
		
		Long count = siacTBilElemRepository.countMovgestNonAnnullatiByBilElemId(uidCapitolo);
		return count;		
	}

	public Long countMovimentiNonAnnullatiCapitolo(TipoCapitolo tipoCapitolo, Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB) {
		List<Integer> elemIds = findCapitoliElemIds(tipoCapitolo, numeroCapitolo, numeroArticolo, numeroUEB, StatoOperativoElementoDiBilancio.VALIDO);
		return countMovimentiNonAnnullatiCapitolo(elemIds);
	} 
	
	public Long countMovimentiNonAnnullatiCapitolo(List<Integer> elemIds) {
		String methodName = "countMovimentiNonAnnullatiCapitolo";
		Long result = Long.valueOf(0);
		
		if (elemIds == null || elemIds.isEmpty()) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = siacTBilElemRepository.countMovgestNonAnnullatiByBilElemIds(elemIds);
		log.info(methodName, "result: " + result);
		return result;	
	}

	public Long countVincoliCapitolo(Capitolo<?, ?> cap, Collection<StatoOperativo> statiVincolo){
		Collection<String> codes = new ArrayList<String>();
		for(StatoOperativo sv : statiVincolo) {
			codes.add(SiacDVincoloStatoEnum.byStatoOperativo(sv).getCodice());
		}
		return siacTBilElemRepository.countSiacTVincoloNonAnnullatiByBilElemId(Arrays.asList(cap.getUid()), codes);
	}
	
	public Long countVariazioniImportiCapitolo(Capitolo<?, ?> cap, Collection<StatoOperativoVariazioneDiBilancio> statiVariazione){
		Collection<String> codes = new ArrayList<String>();
		for(StatoOperativoVariazioneDiBilancio sovdb : statiVariazione) {
			codes.add(SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(sovdb).getCodice());
		}
		
		return siacTBilElemRepository.countSiacTVariazioniImportiByBilElemIdAndStatoCodes(Arrays.asList(cap.getUid()), codes);
	}
	
	public Long countVariazioniCodificheCapitolo(Capitolo<?, ?> cap, Collection<StatoOperativoVariazioneDiBilancio> statiVariazione){
		Collection<String> codes = new ArrayList<String>();
		for(StatoOperativoVariazioneDiBilancio sovdb : statiVariazione) {
			codes.add(SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(sovdb).getCodice());
		}
		return siacTBilElemRepository.countSiacTVariazioniCodificheByBilElemIdAndStatoCodes(Arrays.asList(cap.getUid()), codes);
	}
	
	public Long countMovimentoGestioneCapitolo(Capitolo<?, ?> cap, Collection<String> stati){
		return siacTBilElemRepository.countMovgestByBilElemIdsAndStatoCodes(Arrays.asList(cap.getUid()), stati);
	}
	
	/**
	 * Ricerca puntuale capitolo uscita previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @return the capitolo uscita previsione
	 */
	public Integer ricercaIdByDatiRicercaPuntualeCapitolo(TipoCapitolo tipoCapitolo, Integer annoCapitolo, Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB,
			StatoOperativoElementoDiBilancio getStatoOperativoElementoDiBilancio) {
		final String methodName = "ricercaIdByDatiRicercaPuntualeCapitolo";
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(),
				mapToString(annoCapitolo, null),
				SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo).getCodice(),
				mapToString(numeroCapitolo, null),
				mapToString(numeroArticolo, null),
				mapToString(numeroUEB, null),
				SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(getStatoOperativoElementoDiBilancio).getCodice(),
				new PageRequest(0, 1));
		if(result.getNumberOfElements() == 0) {
			log.warn(methodName, "Nessun elemento corrispondente ai criteri di ricerca");
			return null;
		}
		
		return result.getContent().get(0).getUid();
	}

	public StatoOperativoElementoDiBilancio findStatoOperativoCapitolo(Integer idCapitolo) {
		List<SiacDBilElemStato> siacDBilElemStatos = siacTBilElemRepository.findSiacDBilElemStatosByElemId(idCapitolo);
		for(SiacDBilElemStato sdbes : siacDBilElemStatos) {
			return SiacDBilElemStatoEnum.byCodice(sdbes.getElemStatoCode()).getStatoOperativoElementoDiBilancio();
		}
		return null;
	}
	
	public CategoriaCapitolo findCategoriaCapitolo(Integer uidCapitolo) {
		List<SiacDBilElemCategoria> siacDBilElemCategorias = siacTBilElemRepository.findSiacDBilElemCategoriaByElemId(uidCapitolo);
		for(SiacDBilElemCategoria sdbec : siacDBilElemCategorias) {
			if(sdbec != null) {
				// Evito di usare dozer per tre soli campi...
				CategoriaCapitolo cc = new CategoriaCapitolo();
				cc.setUid(sdbec.getUid());
				cc.setCodice(sdbec.getElemCatCode());
				cc.setDescrizione(sdbec.getElemCatDesc());
				return cc;
			}
		}
		return null;
	}
	
	public Long countMovimentiNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "countMovimentiNonAnnullatiCapitoloByAnno";
		Long result = Long.valueOf(0);
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.countMovgestNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public Long countLiquidazioniNonAnnullateCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "countLiquidazioniNonAnnullateCapitoloByAnno";
		Long result = Long.valueOf(0);
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.countLiquidazioniNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public Long countOrdinativiIncassoNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "countOrdinativiIncassoNonAnnullatiCapitoloByAnno";
		Long result = Long.valueOf(0);
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.countOrdinativiIncassoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public Long countOrdinativiPagamentoNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "countOrdinativiPagamentoNonAnnullatiCapitoloByAnno";
		Long result = Long.valueOf(0);
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.countOrdinativiPagamentoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public BigDecimal computeTotaleImportiMovimentiNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "computeTotaleImportiMovimentiNonAnnullatiCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumMovgestImportoNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public BigDecimal computeTotaleImportiDaRiaccMovimentiNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "computeTotaleImportiDaRiaccMovimentiNonAnnullatiCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumMovgestImportoDaRiaccNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public BigDecimal computeTotaleImportiDaEserciziPrecMovimentiNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator,Integer annoEsercizio) {
		String methodName = "computeTotaleImportiDaEserciziPrecMovimentiNonAnnullatiCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumMovgestImportoDaEserciziPrecNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(elemIds, anno, compareOperator,annoEsercizio.toString());
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	
	// SIAC-6899 
	public BigDecimal computeTotaleImportidaAvanzodaFPVNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator,List<String>  avavincoloTipoCode) {
		String methodName = "computeTotaleImportiFinanziatodaAvanzoNonAnnullatiCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumMovgestImportoFinanziatodaAvanzodaFPVNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(elemIds, anno, compareOperator,avavincoloTipoCode);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	 
	
	public BigDecimal computeTotaleImportiDaPrenotazioneMovimentiNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "computeTotaleImportiDaPrenotazioneMovimentiNonAnnullatiCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumMovgestImportoDaPrenotazioneNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public BigDecimal computeTotaleImportiLiquidazioniNonAnnullateCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "computeTotaleImportiLiquidazioniNonAnnullateCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumLiquidazioniImportoNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public BigDecimal computeTotaleImportiDaPrenotazioneLiquidazioniNonAnnullateCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "computeTotaleImportiDaPrenotazioneLiquidazioniNonAnnullateCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumLiquidazioniImportoDaPrenotazioneNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}	
	
	public BigDecimal computeTotaleImportiOrdinativoIncassoNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "computeTotaleImportiOrdinativoIncassoNonAnnullatiCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumOrdinativoIncassoImportoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	
	public BigDecimal computeTotaleImportiOrdinativoPagamentoNonAnnullatiCapitoloByAnno(List<Integer> elemIds, Integer anno, CompareOperator compareOperator) {
		String methodName = "computeTotaleImportiOrdinativoPagamentoNonAnnullatiCapitoloByAnno";
		BigDecimal result = BigDecimal.ZERO;
		
		if (elemIds == null || elemIds.isEmpty() || anno == null) {
			log.debug(methodName, "Nessun capitolo trovato in stato valido. Returning 0.");
			return result;
		}
		
		log.debug(methodName, "elemIds: " + elemIds);
		result = capitoloDao.sumOrdinativoPagamentoImportoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(elemIds, anno, compareOperator);
		log.info(methodName, "result: " + result);
		return result;	
	}
	/**
	 * @param subdocIds
	 * @return mappa la mappa capitolo subdoc
	 */
	public Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> findCapitoliBySubdoc(List<Integer> subdocIds){
		
		List<Object[]> datiCapitoliSubdoc = capitoloDao.findCapitoliBySubdocIds(subdocIds);
		if(datiCapitoliSubdoc == null || datiCapitoliSubdoc.isEmpty()) {
			//non ho trovato capitoli corrispondenti agli id cercati
			return null;	
		}
		
		Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> mappa = popolaMappaCapitoloSubdocs(datiCapitoliSubdoc);
		
		return mappa;
		
	}
	
	/**
	 * @param subdocIds
	 * @return mappa la mappa capitolo subdoc
	 */
	public Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> findCapitoliByElenco(List<Integer> subdocIds){
		
		List<Object[]> datiCapitoliSubdoc = capitoloDao.findCapitoliByElenco(subdocIds);
		if(datiCapitoliSubdoc == null || datiCapitoliSubdoc.isEmpty()) {
			//non ho trovato capitoli corrispondenti agli id cercati
			return null;	
		}
		
		Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> mappa = popolaMappaCapitoloSubdocs(datiCapitoliSubdoc);
		
		return mappa;
		
	}

	/**
	 * @param datiCapitoliSubdoc
	 * @return
	 */
	private Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> popolaMappaCapitoloSubdocs(List<Object[]> datiCapitoliSubdoc) {
		Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> mappa = 
				new TreeMap<CapitoloUscitaGestione, List<SubdocumentoSpesa>>(new Comparator<CapitoloUscitaGestione>() {

					@Override
					public int compare(CapitoloUscitaGestione o1, CapitoloUscitaGestione o2) {
						if (o1 == o2)
							return 0;

						if (o1 == null)
							return -1;

						if (o2 == null)
							return 1;

						return Integer.valueOf(o1.getUid()).compareTo(o2.getUid());
					}
				});
		
		for (Object[] o : datiCapitoliSubdoc) {
			CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
			cug.setUid((Integer)o[0]);
			cug.setAnnoCapitolo((Integer)o[1]);
			cug.setNumeroCapitolo((Integer)o[2]);
			cug.setNumeroArticolo((Integer)o[3]);
			cug.setNumeroUEB((Integer)o[4]);
			if(mappa.get(cug) == null) {
				mappa.put(cug, new ArrayList<SubdocumentoSpesa>());
			}
			SubdocumentoSpesa ss = new SubdocumentoSpesa();
			ss.setUid((Integer)o[5]);
			DocumentoSpesa ds = new DocumentoSpesa();
			ds.setAnno((Integer)o[6]);
			ds.setNumero((String)o[7]);
			ss.setDocumento(ds);
			ss.setNumero((Integer)o[8]);
			ss.setImporto((BigDecimal)o[9]);
			ss.setImportoDaDedurre((BigDecimal)o[10]);
			mappa.get(cug).add(ss);
			
		}
		return mappa;
	}

	
	/**
	 * Gets the tipo capitolo ex capitolo.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @return the tipo capitolo ex capitolo
	 */
	public TipoCapitolo getTipoCapitoloExCapitolo(TipoCapitolo tipoCapitolo) {
		SiacDBilElemTipoEnum sde = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo);
		return sde != null? sde.getTipoCapitoloEx() : null;
	}
	

	/**
	 * Checks if is capitolo fondino.
	 *
	 * @param uidCapitolo the uid capitolo
	 * @return true, if is capitolo fondino
	 */
	public boolean isCapitoloFondino(int uidCapitolo) {
		SiacTBilElem tbe = siacTBilElemRepository.findCapitoloByCategoriaEClassificatoreGenerico(uidCapitolo, "STD", "CLASSIFICATORE_3", "01"); 
		return tbe != null;
	}
}
