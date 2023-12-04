/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.ImportiCapitoloDao;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ImportiCapitoloUPConverter;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siacbilser.model.utils.DettaglioImportoCapitolo;
import it.csi.siac.siacbilser.model.utils.TipoImportoCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponente;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteAnniSuccNoStanz;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteTriennioNoStanz;

/**
 * The Class ImportiCapitoloDad.
 */
@Component
@Transactional
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImportiCapitoloDad extends ExtendedBaseDadImpl {
	
	private static final Long ZERO = Long.valueOf(0);

	/** The siac t periodo repository. */
	@Autowired private SiacTPeriodoRepository siacTPeriodoRepository;
	/** The importi capitolo repository. */
	@Autowired private SiacTBilElemDetRepository siacTBilElemDetRepository;
	/** The importi capitolo converter. */
	@Autowired private ImportiCapitoloUPConverter importiCapitoloConverter;
	/** The capitolo dao */
	@Autowired private CapitoloDao capitoloDao;
	/** The importi capitolo dao */
	@Autowired private ImportiCapitoloDao importiCapitoloDao;


	
	/**
	 * Totale stanziamento capitoli entrata previsione.
	 *
	 * @param annoEsercizio the anno esercizio
	 * @return the big decimal
	 */
	public BigDecimal totaleStanziamentoCapitoliEntrataPrevisione(Integer bilId, int annoEsercizio){
		return totaleByTipoImporto(bilId, annoEsercizio, SiacDBilElemTipoEnum.CapitoloEntrataPrevisione, SiacDBilElemDetTipoEnum.Stanziamento);
	}
	
	/**
	 * Totale stanziamento capitoli uscita previsione.
	 *
	 * @param annoEsercizio the anno esercizio
	 * @return the big decimal
	 */
	public BigDecimal totaleStanziamentoCapitoliUscitaPrevisione(Integer bilId, int annoEsercizio){
		return totaleByTipoImporto(bilId, annoEsercizio, SiacDBilElemTipoEnum.CapitoloUscitaPrevisione, SiacDBilElemDetTipoEnum.Stanziamento);
	}
	
	/**
	 * Totale stanziamento residuo capitoli entrata previsione.
	 *
	 * @param annoEsercizio the anno esercizio
	 * @return the big decimal
	 */
	public BigDecimal totaleStanziamentoResiduoCapitoliEntrataPrevisione(Integer bilId, int annoEsercizio){
		return totaleByTipoImporto(bilId, annoEsercizio, SiacDBilElemTipoEnum.CapitoloEntrataPrevisione, SiacDBilElemDetTipoEnum.StanziamentoResiduo);
	}
		
	/**
	 * Totale stanziamento residuo capitoli uscita previsione.
	 *
	 * @param annoEsercizio the anno esercizio
	 * @return the big decimal
	 */
	public BigDecimal totaleStanziamentoResiduoCapitoliUscitaPrevisione(Integer bilId, int annoEsercizio){
		return totaleByTipoImporto(bilId, annoEsercizio, SiacDBilElemTipoEnum.CapitoloUscitaPrevisione, SiacDBilElemDetTipoEnum.StanziamentoResiduo);
	}
	
	/**
	 * Totale stanziamento cassa capitoli entrata previsione.
	 *
	 * @param annoEsercizio the anno esercizio
	 * @return the big decimal
	 */
	public BigDecimal totaleStanziamentoCassaCapitoliEntrataPrevisione(Integer bilId, int annoEsercizio){
		return totaleByTipoImporto(bilId, annoEsercizio, SiacDBilElemTipoEnum.CapitoloEntrataPrevisione, SiacDBilElemDetTipoEnum.StanziamentoCassa);
	}
		
	/**
	 * Totale stanziamento cassa capitoli uscita previsione.
	 *
	 * @param annoEsercizio the anno esercizio
	 * @return the big decimal
	 */
	public BigDecimal totaleStanziamentoCassaCapitoliUscitaPrevisione(Integer bilId, int annoEsercizio){
		return totaleByTipoImporto(bilId, annoEsercizio, SiacDBilElemTipoEnum.CapitoloUscitaPrevisione, SiacDBilElemDetTipoEnum.StanziamentoCassa);
	}
	
	/**
	 * Totale by tipo importo.
	 *
	 * @param annoEsercizio the anno esercizio
	 * @param capitolo the capitolo
	 * @param stanziamento the stanziamento
	 * @return the big decimal
	 */
	private BigDecimal totaleByTipoImporto(Integer bilId, int annoEsercizio, SiacDBilElemTipoEnum capitolo, SiacDBilElemDetTipoEnum stanziamento) {
		SiacTPeriodo periodoEntity = siacTPeriodoRepository.findByAnnoAndEnteProprietario(Integer.toString(annoEsercizio), siacTEnteProprietario);
		BigDecimal importo = siacTBilElemDetRepository.totaleByElemTipoAndElemDetTipoAndPeriodo(bilId, periodoEntity, siacTEnteProprietario, capitolo.getCodice(), stanziamento.getCodice());
		return importo == null ? BigDecimal.ZERO : importo;
	}
	
	/**
	 * Aggiorna importi capitolo.
	 *
	 * @param <T> the generic type
	 * @param capitolo the capitolo
	 * @param importiCapitolo the importi capitolo
	 * @param annoEsercizio the anno esercizio
	 */
	public <T extends ImportiCapitolo> void aggiornaImportiCapitolo(Capitolo<?, ?> capitolo, T importiCapitolo, int annoEsercizio) {
		final String methodName = "aggiornaImportiCapitolo";
		log.debug(methodName,"chiamo importiCapitoloRepository");
		
		log.debug(methodName, "uid: "+capitolo.getUid() + " anno: "+annoEsercizio + " ente: "+siacTEnteProprietario.getUid());
		
		List<SiacTBilElemDet> bilElemDets = siacTBilElemDetRepository.findBilElemDetsByBilElemIdAndAnno(capitolo.getUid(), ""+annoEsercizio);
		
		BeanWrapper bwImporti = PropertyAccessorFactory.forBeanPropertyAccess(importiCapitolo);
		
		for (SiacTBilElemDet siacTBilElemDet : bilElemDets) {
			String elemDetTipoCode = siacTBilElemDet.getSiacDBilElemDetTipo().getElemDetTipoCode();
			String importoFieldName = SiacDBilElemDetTipoEnum.byCodice(elemDetTipoCode).getImportiCapitoloFieldName();
			
			BigDecimal importoDaAggiornare; 
			try{
				importoDaAggiornare = (BigDecimal) bwImporti.getPropertyValue(importoFieldName);
				log.info(methodName, "Importo da aggiornare da "+siacTBilElemDet.getElemDetImporto()+" a : "+importoDaAggiornare);			
				
				
			} catch (BeansException be){
				if(ImportiCapitolo.class.equals(importiCapitolo.getClass())){
					//Nel caso utilizzo ImportiCapitolo come classe vuol dire che non mi interessa tirare su TUTTI gli importi ma
					//mi bastano quelli della superclasse ImportiCapitolo.class quindi ignoro eventuali errori nel settaggio di tali importi.
					log.warn(methodName, "Cannot set importo:"+importoFieldName + " tipo:"+elemDetTipoCode +". "+be.getMessage());
					continue;
				} else {
					throw be;
				}				
			}
			
			siacTBilElemDet.setElemDetImporto(importoDaAggiornare);			
			siacTBilElemDet.setDataModifica(new Date());
			
			
		}
		
		
	}
	
	
	/**
	 * Ottiene gli importi di un capitolo a partire dal suo uid e dall'anno di competenza degli importi.
	 *
	 * @param <T> the generic type
	 * @param capitolo the capitolo
	 * @param annoCompetenza the anno competenza
	 * @param importiCapitoloClass the importi capitolo class
	 * @return the t
	 */
	public <T extends ImportiCapitolo> T findImportiCapitolo(Capitolo<?, ?> capitolo, int annoCompetenza, Class<T> importiCapitoloClass, Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		
		return findImportiCapitolo(capitolo.getUid(), annoCompetenza, importiCapitoloClass, importiDerivatiRichiesti, false);
		
	}
	
	
	/**
	 * Ottiene gli importi di un capitolo a partire dal suo uid e dall'anno di competenza degli importi.
	 *
	 * @param <T> the generic type
	 * @param capitolo the capitolo
	 * @param annoCompetenza the anno competenza
	 * @param importiCapitoloClass the importi capitolo class
	 * @return the t
	 */
	public <T extends ImportiCapitolo> T findImportiCapitolo(Capitolo<?, ?> capitolo, int annoCompetenza, Class<T> importiCapitoloClass, Set<ImportiCapitoloEnum> importiDerivatiRichiesti, boolean forzaPopolamentoImportaDerivati) {
		
		return findImportiCapitolo(capitolo.getUid(), annoCompetenza, importiCapitoloClass, importiDerivatiRichiesti, forzaPopolamentoImportaDerivati);
		
	}

	/**
	 * Find importi capitolo.
	 *
	 * @param <T> the generic type
	 * @param uidCapitolo the uid capitolo
	 * @param annoCompetenza the anno competenza
	 * @param importiCapitoloClass the importi capitolo class
	 * @return the t
	 */
	public <T extends ImportiCapitolo> T findImportiCapitolo(int uidCapitolo, int annoCompetenza, Class<T> importiCapitoloClass, Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		return findImportiCapitolo(uidCapitolo, annoCompetenza, importiCapitoloClass, importiDerivatiRichiesti, false);
	}
	
	/**
	 * Find importi capitolo.
	 *
	 * @param <T> the generic type
	 * @param uidCapitolo the uid capitolo
	 * @param annoCompetenza the anno competenza
	 * @param importiCapitoloClass the importi capitolo class
	 * @return the t
	 */
	public <T extends ImportiCapitolo> T findImportiCapitolo(int uidCapitolo, int annoCompetenza, Class<T> importiCapitoloClass, Set<ImportiCapitoloEnum> importiDerivatiRichiesti, boolean forzaPopolamentoImportaDerivati) {
		final String methodName = "findImportiCapitolo";
		
		T ic = importiCapitoloConverter.toImportiCapitolo(uidCapitolo , annoCompetenza, importiDerivatiRichiesti, forzaPopolamentoImportaDerivati);
		
		if(importiCapitoloClass!=null && !importiCapitoloClass.isInstance(ic)) {
			log.warn(methodName, "Attenzione! Gli importi appartengono ad un tipo di capitolo diverso da "+importiCapitoloClass.getSimpleName());
		}
		
		if(log.isTraceEnabled()){
			log.trace(methodName, ToStringBuilder.reflectionToString(ic, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.debug(methodName, "returning " + (ic != null ? "ImportiCapitolo of type: " + ic.getClass().getSimpleName() : "null!"));
		return ic;
	}
	
	/**
	 * Controlla se esistano degli importi per l'anno selezionato.
	 * 
	 * @param uidCapitolo the uid capitolo
	 * @param annoCompetenza the anno competenza
	 * @return the present
	 */
	public Boolean isPresent(Integer uidCapitolo, Integer annoCompetenza) {
		Long count = siacTBilElemDetRepository.countBilElemDetsByBilElemIdAndAnno(uidCapitolo, annoCompetenza.toString());
		return count != null && ZERO.compareTo(count) < 0;
	}

	public BigDecimal findImportoDerivato(Integer uid, ImportoDerivatoFunctionEnum importoDerivatoFunction) {
		return capitoloDao.findImportoDerivato(uid, importoDerivatoFunction.getFunctionName());
	}
	
	//SIAC-7349  - START - SR90 - 02/04/2020 Calcolo della disponibilita impegnare per capitolo UG
	public BigDecimal findDisponibilitaImpegnareComponente(Integer uid, Integer uidComp,  String importoDispCompFunction) {
		return capitoloDao.findDisponibilitaImpegnareComponente(uid, uidComp, importoDispCompFunction);
	}
	//SIAC-7349 - FINE
	
	//SIAC-7349  - START - SR90 - 07/05/2020 Calcolo impegnato anni succ no stanziamento per capitolo UP
	public List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> findImpegnatoAnniSuccNoStanz(Integer uid, List<Integer> uidComp,  String fncImportoImpAnniSuccNoStanz) {
		return capitoloDao.findImpegnatoAnniSuccNoStanz(uid, uidComp, fncImportoImpAnniSuccNoStanz);
	}
	//SIAC-7349 - FINE

	// SIAC-7349 GS 17/07/2020
	public List<ImportiImpegnatoPerComponenteTriennioNoStanz> findImpegnatoTriennioNoStanz(Integer uid, List<Integer> uidComp,  String fncImportoImpTriennioNoStanz) {
		return capitoloDao.findImpegnatoTriennioNoStanz(uid, uidComp, fncImportoImpTriennioNoStanz);
	}

	
	//SIAC-7349  - START - SR210 - 16/04/2020 Calcolo della disponibilita impegnare per capitolo UG
	public BigDecimal findDisponibilitaVariareComponente(Integer uid, Integer uidComp,  String importoDispVarCompFunction) {
		return capitoloDao.findDisponibilitaVariareComponente(uid, uidComp, importoDispVarCompFunction);
	}
	//SIAC-7349 - FINE
	//SIAC-7349  - START - SR200 - 09/04/2020 Calcolo dell impegnato per capitolo UP
	public List<ImportiImpegnatoPerComponente> findImpegnatoComponente(Integer uid, Integer uidComp,  String importoImpegnatoFunction) {
		return capitoloDao.findImpegnatoComponente(uid, uidComp, importoImpegnatoFunction);
	}
	//SIAC-7349 - FINE
	
	// SIAC-6881: dettagli importo
	public DettaglioImportoCapitolo findDettaglioImportoCapitoloById(Integer uid) {
		SiacTBilElemDet siacTBilElemDet = siacTBilElemDetRepository.findOne(uid);
		return mapNotNull(siacTBilElemDet, DettaglioImportoCapitolo.class, BilMapId.SiacTBilElemDet_DettaglioImportoCapitolo);
	}
	
	public DettaglioImportoCapitolo findDettaglioImportoCapitoloByCapitoloTipoAnno(Integer uidCapitolo, String tipo, Integer anno) {
		List<SiacTBilElemDet> siacTBilElemDet = siacTBilElemDetRepository.findBilElemDetsByBilElemIdAndAnnoAndTipo(uidCapitolo, anno.toString(), tipo);
		return mapNotNull(siacTBilElemDet != null && !siacTBilElemDet.isEmpty() ? siacTBilElemDet.get(0) : null, DettaglioImportoCapitolo.class, BilMapId.SiacTBilElemDet_DettaglioImportoCapitolo);
	}
	
	public DettaglioImportoCapitolo aggiornaDettaglioImportoCapitolo(DettaglioImportoCapitolo dettaglioImportoCapitolo) {
		SiacTBilElemDet siacTBilElemDet = buildSiacTBilElemDet(dettaglioImportoCapitolo);
		importiCapitoloDao.update(siacTBilElemDet);
		return dettaglioImportoCapitolo;
	}
	public DettaglioImportoCapitolo inserisceDettaglioImportoCapitolo(DettaglioImportoCapitolo dettaglioImportoCapitolo) {
		SiacTBilElemDet siacTBilElemDet = buildSiacTBilElemDet(dettaglioImportoCapitolo);
		importiCapitoloDao.create(siacTBilElemDet);
		dettaglioImportoCapitolo.setUid(siacTBilElemDet.getElemDetId());
		return dettaglioImportoCapitolo;
	}
	
	/**
	 * Builds the SiacTBilElemDet.
	 *
	 * @param dettaglioImportoCapitolo the dettaglio importo capitolo
	 * @return the siac t bil elem det
	 */
	private SiacTBilElemDet buildSiacTBilElemDet(DettaglioImportoCapitolo dettaglioImportoCapitolo) {
		SiacTBilElemDet siacTBilElemDet = new SiacTBilElemDet();
		
		siacTBilElemDet.setLoginOperazione(loginOperazione);
		siacTBilElemDet.setSiacTEnteProprietario(siacTEnteProprietario);
		dettaglioImportoCapitolo.setLoginOperazione(loginOperazione);
		
		map(dettaglioImportoCapitolo, siacTBilElemDet, BilMapId.SiacTBilElemDet_DettaglioImportoCapitolo);
		return siacTBilElemDet;
	}
	
	public BigDecimal caricaSingoloImportoCapitolo(Integer uidCapitolo, Integer annoImporto, String elemDetTipoCode){
		return siacTBilElemDetRepository.findElemDetImportoByBilElemIdAndAnno(uidCapitolo, ""+annoImporto, elemDetTipoCode);
	}
	
	public BigDecimal caricaSingoloImportoCapitolo(Integer uidCapitolo, Integer annoImporto, SiacDBilElemDetTipoEnum enumTipo){
		if(enumTipo == null) {
			return null;
		}
		return caricaSingoloImportoCapitolo(uidCapitolo, annoImporto, enumTipo.getCodice());
	}
	
	
	
	
	
}
