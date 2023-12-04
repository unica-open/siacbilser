/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
//import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemCategoriaEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * The Class CapitoloEntrataPrevisioneDad.
 *
 * @author 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CapitoloEntrataPrevisioneDad extends CapitoloDad {
	
	/**
	 * Creates the.
	 *
	 * @param capitoEntrataPrevisione the capitolo uscita gestione
	 * @return the capitolo entrata previsione
	 */
	public CapitoloEntrataPrevisione create(CapitoloEntrataPrevisione capitoEntrataPrevisione/*, List<ImportiCapitoloEP> importiCapitoloUP,
			TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti,
			List<ClassificatoreGenerico> listaClassificatoriGenerici,
			StrutturaAmministrativoContabile struttAmmContabile, CategoriaTipologiaTitolo categoriaTipologiaTitolo*/) {
		
		final String methodName = "create";

		SiacTBilElem bilElem = buidSiacTBilElem(capitoEntrataPrevisione /*, importiCapitoloUP, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti, listaClassificatoriGenerici, struttAmmContabile, categoriaTipologiaTitolo*/);
		
		capitoloDao.create(bilElem);		
		log.info(methodName, "created.");	
		
		capitoEntrataPrevisione.setUid(bilElem.getUid());

		return capitoEntrataPrevisione;

	}
	

	/**
	 * Update.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the capitolo entrata previsione
	 */
	public CapitoloEntrataPrevisione update(CapitoloEntrataPrevisione capitoloUscitaGestione/*, List<ImportiCapitoloEP> importiCapitoloUP, TipoFondo tipoFondo, 
			TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile, 
			 CategoriaTipologiaTitolo categoriaTipologiaTitolo*/) {

		final String methodName = "update";
		
		SiacTBilElem bilElem = buidSiacTBilElem(capitoloUscitaGestione/*, importiCapitoloUP, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti, listaClassificatoriGenerici, struttAmmContabile, categoriaTipologiaTitolo*/);
		
		setDataCancellazioneAPartireDaDataFineValidita(bilElem);
		
		bilElem = capitoloDao.update(bilElem);
		log.info(methodName, "updated.");	
		
		return map(bilElem, CapitoloEntrataPrevisione.class);
	}

	
	/**
	 * Elimina.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	public void elimina(CapitoloEntrataPrevisione capitoloUscitaPrev) {
		SiacTBilElem bilElem = capitoloDao.findById(capitoloUscitaPrev.getUid());
		capitoloDao.deleteLogical(bilElem);
		
	}
	
	/**
	 * Annulla.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	@Transactional
	public void annulla(CapitoloEntrataPrevisione capitoloUscitaPrev) {
		SiacTBilElem bilElem = capitoloDao.findById(capitoloUscitaPrev.getUid());
		
		for(SiacRBilElemStato stato : bilElem.getSiacRBilElemStatos()){ //NOTA: dovrebbe esserci un solo stato!!! 
			//stato.setSiacDBilElemStato(SiacDBilElemStatoEnum.Annullato.getEntity());
			SiacDBilElemStato siacDBilElemStato = eef.getEntity(SiacDBilElemStatoEnum.Annullato, ente.getUid(), SiacDBilElemStato.class);
			stato.setSiacDBilElemStato(siacDBilElemStato);
		}
		
		//capitoloDao.update(bilElem);
		
	}
	
	
	
	/**
	 * Buid siac t bil elem.
	 *
	 * @param cap the cap
	 * @return the siac t bil elem
	 */
	private SiacTBilElem buidSiacTBilElem(CapitoloEntrataPrevisione cap/*,
			List<ImportiCapitoloEP> importiCapitoloUP, TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento,
			ElementoPianoDeiConti elementoPianoDeiConti,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile,
			CategoriaTipologiaTitolo categoriaTipologiaTitolo*/) {

		
		SiacTBilElem bilElem = map(bilancio, SiacTBilElem.class, BilMapId.SiacTBilElem_Bilancio);	
		map(ente, bilElem, BilMapId.SiacTBilElem_Ente);
		cap.setLoginOperazione(loginOperazione);
		bilElem.setLoginOperazione(loginOperazione);
		bilElem.setLivello(1);
		//bilElem.setSiacDBilElemTipo(SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getEntity());
		bilElem.setSiacDBilElemTipo(eef.getEntity(SiacDBilElemTipoEnum.CapitoloEntrataPrevisione, ente.getUid(), SiacDBilElemTipo.class));
		
		
		map(cap, bilElem, BilMapId.SiacTBilElem_CapitoloEntrataPrevisione);
						
		map(cap.getListaImportiCapitoloEP(), bilElem, BilMapId.SiacTBilElem_CapitoloEntrataPrevisione_ImportiCapitolo);
		mapNotNull(cap.getTipoFondo(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);		
		mapNotNull(cap.getTipoFinanziamento(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		mapNotNull(cap.getElementoPianoDeiConti(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
//		mapNotNull(classCofogProgramma, bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getStrutturaAmministrativoContabile(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
//		mapNotNull(macroaggregato, bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
//		mapNotNull(programma, bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getCategoriaTipologiaTitolo(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		
		mapNotNull(cap.getRicorrenteEntrata(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		mapNotNull(cap.getPerimetroSanitarioEntrata(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		mapNotNull(cap.getSiopeEntrata(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getTransazioneUnioneEuropeaEntrata(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		
		mapNotNull(cap.getClassificatoriGenerici(), bilElem, BilMapId.SiacTBilElem_ClassificatoriGenerici);		
		
		mapAttrs(cap, bilElem);
		return bilElem;
	}

	
	



	/**
	 * Ricerca puntuale capitolo entrata previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @return the capitolo entrata previsione
	 */
	public CapitoloEntrataPrevisione ricercaPuntualeCapitoloEntrataPrevisione(RicercaPuntualeCapitoloEPrev criteriRicerca) {
		final String methodName = "ricercaPuntualeCapitoloEntrataPrevisione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice():null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(), 
				mapToString(criteriRicerca.getAnnoCapitolo(),null),//mapToString(criteriRicerca.getAnnoEsercizio(),null),
				SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice(),				
				mapToString(criteriRicerca.getNumeroCapitolo(),null),				
				mapToString(criteriRicerca.getNumeroArticolo(),null),
				mapToString(criteriRicerca.getNumeroUEB(),null),
				codiceStato,new PageRequest(0, 1));
		
		SiacTBilElem bilElem; 
		
		try {			
			bilElem =  result.getContent().get(0);			
		} catch (RuntimeException re){
			log.warn(methodName, "bilElem non trovato. Returning null. ", re);
			bilElem = null;
		}
		
		
		log.debug(methodName, "result: "+bilElem);
		return mapNotNull(bilElem, CapitoloEntrataPrevisione.class, BilMapId.SiacTBilElem_CapitoloEntrataPrevisione);
	}
	
	/**
	 * Ricerca puntuale capitolo entrata previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param modelDetails the model details
	 * @return the capitolo entrata previsione
	 */
	public CapitoloEntrataPrevisione ricercaPuntualeModulareCapitoloEntrataPrevisione(RicercaPuntualeCapitoloEPrev criteriRicerca, CapitoloEntrataPrevisioneModelDetail... modelDetails) {
		final String methodName = "ricercaPuntualeCapitoloEntrataPrevisione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio() != null ? SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice() : null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(),
				mapToString(criteriRicerca.getAnnoCapitolo(), null),
				SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice(),
				mapToString(criteriRicerca.getNumeroCapitolo(),null),
				mapToString(criteriRicerca.getNumeroArticolo(),null),
				mapToString(criteriRicerca.getNumeroUEB(),null),
				codiceStato,new PageRequest(0, 1));
		
		SiacTBilElem bilElem;
		
		try {
			bilElem =  result.getContent().get(0);
		} catch (RuntimeException re){
			log.warn(methodName, "bilElem non trovato. Returning null. ", re);
			bilElem = null;
		}
		
		log.debug(methodName, "result: "+bilElem);
		return mapNotNull(bilElem, CapitoloEntrataPrevisione.class, BilMapId.SiacTBilElem_CapitoloEntrataPrevisione_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	
	
	
	/**
	 * Ricerca sintetica capitolo entrata previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<CapitoloEntrataPrevisione> ricercaSinteticaCapitoloEntrataPrevisione(RicercaSinteticaCapitoloEPrev criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Page<SiacTBilElem> listaCapitoloEntrataPrevisione = capitoloDao.ricercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloEntrataPrevisione,
				criteriRicerca.getCategoriaCapitolo()!=null && criteriRicerca.getCategoriaCapitolo().getUid()!=0?criteriRicerca.getCategoriaCapitolo().getUid():null,
				criteriRicerca.getCategoriaCapitolo()!=null?criteriRicerca.getCategoriaCapitolo().getCodice():null,
						
			//	SiacDBilElemCategoriaEnum.byCategoriaCapitoloEvenNull(criteriRicerca.getCategoriaCapitolo()),
				mapToString(criteriRicerca.getAnnoCapitolo(),null), 
				mapToString(criteriRicerca.getNumeroCapitolo(),null),  
				mapToString(criteriRicerca.getNumeroArticolo(),null), 
				mapToString(criteriRicerca.getNumeroUEB(),null),				
				codiceStato,	
				
				mapToString(criteriRicerca.getExAnnoCapitolo(),null), 
				mapToString(criteriRicerca.getExNumeroCapitolo(),null), 
				mapToString(criteriRicerca.getExNumeroArticolo(),null), 
				mapToString(criteriRicerca.getExNumeroUEB(),null), 
				
				criteriRicerca.getFaseBilancio(), 
				criteriRicerca.getDescrizioneCapitolo(),
				criteriRicerca.getDescrizioneArticolo(),
				
				null,//criteriRicerca.getFlagAssegnabile(),
				null,//criteriRicerca.getFlagFondoSvalutazioneCrediti(),
				null,//criteriRicerca.getFlagFunzioniDelegate(),
				criteriRicerca.getFlagPerMemoria(),
				//SIAC-8547 si ripristina il flag rilevante iva
				criteriRicerca.getFlagRilevanteIva(),
				null,//criteriRicerca.getFlagTrasferimentoOrganiComunitari(),	
				criteriRicerca.getFlagEntrateRicorrenti(),
				null,//criteriRicerca.getFlagFondoPluriennaleVinc(),
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),			
				
				criteriRicerca.getCodiceRicorrenteEntrata(), null, //criteriRicerca.getCodiceRicorrenteSpesa(),
				criteriRicerca.getCodicePerimetroSanitarioEntrata(), null, //criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(), null, //criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa(),
				null,//criteriRicerca.getCodicePoliticheRegionaliUnitarie(), 
				
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,	
				
				null,
				null,
				null,
				null,
				null,
				criteriRicerca.getCodiceClassificatoreGenerico36(),
				criteriRicerca.getCodiceClassificatoreGenerico37(),
				criteriRicerca.getCodiceClassificatoreGenerico38(),
				criteriRicerca.getCodiceClassificatoreGenerico39(),
				criteriRicerca.getCodiceClassificatoreGenerico40(),
				criteriRicerca.getCodiceClassificatoreGenerico41(),
				criteriRicerca.getCodiceClassificatoreGenerico42(),
				criteriRicerca.getCodiceClassificatoreGenerico43(),
				criteriRicerca.getCodiceClassificatoreGenerico44(),
				criteriRicerca.getCodiceClassificatoreGenerico45(),
				criteriRicerca.getCodiceClassificatoreGenerico46(),
				criteriRicerca.getCodiceClassificatoreGenerico47(),
				criteriRicerca.getCodiceClassificatoreGenerico48(),
				criteriRicerca.getCodiceClassificatoreGenerico49(),
				criteriRicerca.getCodiceClassificatoreGenerico50(),
				
				//classificatori gerarchici
				criteriRicerca.getCodicePianoDeiConti(),
				null,null,//criteriRicerca.getCodiceCofog(),criteriRicerca.getCodiceTipoCofog(),
				criteriRicerca.getCodiceStrutturaAmmCont(), criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				//task-90
				mapToString(criteriRicerca.getIdStrutturaAmmCont(),null),
				criteriRicerca.getCodiceSiopeEntrata(), criteriRicerca.getCodiceTipoSiopeEntrata(),
				null,null,//criteriRicerca.getCodiceSiopeSpesa(), criteriRicerca.getCodiceTipoSiopeSpesa(),
				
				null, null, //criteriRicerca.getCodiceMissione(), criteriRicerca.getCodiceProgramma(),
				null, null, //criteriRicerca.getCodiceTitoloUscita(), criteriRicerca.getCodiceMacroaggregato(),
				criteriRicerca.getCodiceTitoloEntrata(), criteriRicerca.getCodiceTipologia(), criteriRicerca.getCodiceCategoria(),
				
				criteriRicerca.getNumeroAttoDilegge(),
				mapToString(criteriRicerca.getAnnoAttoDilegge()),
				criteriRicerca.getArticoloAttoDilegge(),
				criteriRicerca.getCommaAttoDilegge(),
				criteriRicerca.getPuntoAttoDilegge(),
				criteriRicerca.getTipoAttoDilegge(),
				
				criteriRicerca.getRichiediAccantonamentoFondiDubbiaEsigibilita(),
				null,
				criteriRicerca.getFlagEntrataDubbiaEsigFCDE(),
				criteriRicerca.getVersioneAccFcde(),
				criteriRicerca.getTipoAccFcde(),
				
				null,
				toPageable(parametriPaginazione));
		
				
		return toListaPaginata(listaCapitoloEntrataPrevisione,CapitoloEntrataPrevisione.class, BilMapId.SiacTBilElem_CapitoloEntrataPrevisione);
	}
	
	public ImportiCapitoloEP importiRicercaSintetica(RicercaSinteticaCapitoloEPrev criteriRicerca) {
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		List<Object[]> objs = capitoloDao.importiRicercaSinteticaCapitolo(enteDto,
				mapToString(criteriRicerca.getAnnoEsercizio(),null),
				SiacDBilElemTipoEnum.CapitoloEntrataPrevisione,
				criteriRicerca.getCategoriaCapitolo() != null && criteriRicerca.getCategoriaCapitolo().getUid() != 0 ? criteriRicerca.getCategoriaCapitolo().getUid() : null,
				criteriRicerca.getCategoriaCapitolo() != null ? criteriRicerca.getCategoriaCapitolo().getCodice() : null,
				mapToString(criteriRicerca.getAnnoCapitolo(),null),
				mapToString(criteriRicerca.getNumeroCapitolo(),null),
				mapToString(criteriRicerca.getNumeroArticolo(),null),
				mapToString(criteriRicerca.getNumeroUEB(),null),
				codiceStato,
				
				mapToString(criteriRicerca.getExAnnoCapitolo(),null),
				mapToString(criteriRicerca.getExNumeroCapitolo(),null),
				mapToString(criteriRicerca.getExNumeroArticolo(),null),
				mapToString(criteriRicerca.getExNumeroUEB(),null),
				
				criteriRicerca.getFaseBilancio(),
				criteriRicerca.getDescrizioneCapitolo(),
				criteriRicerca.getDescrizioneArticolo(),
				
				// criteriRicerca.getFlagAssegnabile()
				null,
				// criteriRicerca.getFlagFondoSvalutazioneCrediti()
				null,
				// criteriRicerca.getFlagFunzioniDelegate()
				null,
				criteriRicerca.getFlagPerMemoria(),
				// criteriRicerca.getFlagRilevanteIva()
				null,
				// criteriRicerca.getFlagTrasferimentoOrganiComunitari()
				null,
				criteriRicerca.getFlagEntrateRicorrenti(),
				// criteriRicerca.getFlagFondoPluriennaleVinc()
				null,
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),
				
				criteriRicerca.getCodiceRicorrenteEntrata(),
				// criteriRicerca.getCodiceRicorrenteSpesa()
				null,
				criteriRicerca.getCodicePerimetroSanitarioEntrata(),
				// criteriRicerca.getCodicePerimetroSanitarioSpesa()
				null,
				criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(),
				// criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa()
				null,
				// criteriRicerca.getCodicePoliticheRegionaliUnitarie()
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,				
				null,
				null,
				null,
				null,
				null,
				criteriRicerca.getCodiceClassificatoreGenerico36(),
				criteriRicerca.getCodiceClassificatoreGenerico37(),
				criteriRicerca.getCodiceClassificatoreGenerico38(),
				criteriRicerca.getCodiceClassificatoreGenerico39(),
				criteriRicerca.getCodiceClassificatoreGenerico40(),
				criteriRicerca.getCodiceClassificatoreGenerico41(),
				criteriRicerca.getCodiceClassificatoreGenerico42(),
				criteriRicerca.getCodiceClassificatoreGenerico43(),
				criteriRicerca.getCodiceClassificatoreGenerico44(),
				criteriRicerca.getCodiceClassificatoreGenerico45(),
				criteriRicerca.getCodiceClassificatoreGenerico46(),
				criteriRicerca.getCodiceClassificatoreGenerico47(),
				criteriRicerca.getCodiceClassificatoreGenerico48(),
				criteriRicerca.getCodiceClassificatoreGenerico49(),
				criteriRicerca.getCodiceClassificatoreGenerico50(),
				
				//classificatori gerarchici
				criteriRicerca.getCodicePianoDeiConti(),
				// criteriRicerca.getCodiceCofog()
				null,
				// criteriRicerca.getCodiceTipoCofog()
				null,
				criteriRicerca.getCodiceStrutturaAmmCont(),
				criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				
				criteriRicerca.getCodiceSiopeEntrata(),
				criteriRicerca.getCodiceTipoSiopeEntrata(),
				// criteriRicerca.getCodiceSiopeSpesa()
				null,
				// criteriRicerca.getCodiceTipoSiopeSpesa()
				null,
				
				// criteriRicerca.getCodiceMissione()
				null,
				// criteriRicerca.getCodiceProgramma()
				null,
				// criteriRicerca.getCodiceTitoloUscita()
				null,
				// criteriRicerca.getCodiceMacroaggregato()
				null,
				criteriRicerca.getCodiceTitoloEntrata(),
				criteriRicerca.getCodiceTipologia(),
				criteriRicerca.getCodiceCategoria(),
				
				criteriRicerca.getNumeroAttoDilegge(),
				mapToString(criteriRicerca.getAnnoAttoDilegge()),
				criteriRicerca.getArticoloAttoDilegge(),
				criteriRicerca.getCommaAttoDilegge(),
				criteriRicerca.getPuntoAttoDilegge(),
				criteriRicerca.getTipoAttoDilegge(),
				criteriRicerca.getRichiediAccantonamentoFondiDubbiaEsigibilita(),
				null,
				criteriRicerca.getFlagEntrataDubbiaEsigFCDE()
				);
		if(objs == null) {
			return null;
		}
		
		// FIXME: riscrivere in maniera migliore
		ImportiCapitoloEP icep = new ImportiCapitoloEP();
		popolaImporti(icep, objs);
		
		return icep;
	}
	
	/**
	 * Count ricerca sintetica capitolo entrata previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the long
	 */
	public Long countRicercaSinteticaCapitoloEntrataPrevisione(RicercaSinteticaCapitoloEPrev criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Long result = capitoloDao.countRicercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloEntrataPrevisione,
				//SiacDBilElemCategoriaEnum.byCategoriaCapitoloEvenNull(criteriRicerca.getCategoriaCapitolo()),
				criteriRicerca.getCategoriaCapitolo()!=null && criteriRicerca.getCategoriaCapitolo().getUid()!=0?criteriRicerca.getCategoriaCapitolo().getUid():null,
				criteriRicerca.getCategoriaCapitolo()!=null?criteriRicerca.getCategoriaCapitolo().getCodice():null,
				mapToString(criteriRicerca.getAnnoCapitolo(),null), 
				mapToString(criteriRicerca.getNumeroCapitolo(),null),  
				mapToString(criteriRicerca.getNumeroArticolo(),null), 
				mapToString(criteriRicerca.getNumeroUEB(),null),				
				codiceStato,	
				
				mapToString(criteriRicerca.getExAnnoCapitolo(),null), 
				mapToString(criteriRicerca.getExNumeroCapitolo(),null), 
				mapToString(criteriRicerca.getExNumeroArticolo(),null), 
				mapToString(criteriRicerca.getExNumeroUEB(),null), 
				
				criteriRicerca.getFaseBilancio(), 
				criteriRicerca.getDescrizioneCapitolo(),
				criteriRicerca.getDescrizioneArticolo(),
				
				null,//criteriRicerca.getFlagAssegnabile(),
				null,//criteriRicerca.getFlagFondoSvalutazioneCrediti(),
				null,//criteriRicerca.getFlagFunzioniDelegate(),
				criteriRicerca.getFlagPerMemoria(),
				null,//criteriRicerca.getFlagRilevanteIva(),
				null,//criteriRicerca.getFlagTrasferimentoOrganiComunitari(),	
				criteriRicerca.getFlagEntrateRicorrenti(),
				null,//criteriRicerca.getFlagFondoPluriennaleVinc(),
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),		
				
				criteriRicerca.getCodiceRicorrenteEntrata(), null, //criteriRicerca.getCodiceRicorrenteSpesa(),
				criteriRicerca.getCodicePerimetroSanitarioEntrata(), null, //criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(), null, //criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa(),
				null,//criteriRicerca.getCodicePoliticheRegionaliUnitarie(), 
				
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				
				null,
				null,
				null,
				null,
				null,
				criteriRicerca.getCodiceClassificatoreGenerico36(),
				criteriRicerca.getCodiceClassificatoreGenerico37(),
				criteriRicerca.getCodiceClassificatoreGenerico38(),
				criteriRicerca.getCodiceClassificatoreGenerico39(),
				criteriRicerca.getCodiceClassificatoreGenerico40(),
				criteriRicerca.getCodiceClassificatoreGenerico41(),
				criteriRicerca.getCodiceClassificatoreGenerico42(),
				criteriRicerca.getCodiceClassificatoreGenerico43(),
				criteriRicerca.getCodiceClassificatoreGenerico44(),
				criteriRicerca.getCodiceClassificatoreGenerico45(),
				criteriRicerca.getCodiceClassificatoreGenerico46(),
				criteriRicerca.getCodiceClassificatoreGenerico47(),
				criteriRicerca.getCodiceClassificatoreGenerico48(),
				criteriRicerca.getCodiceClassificatoreGenerico49(),
				criteriRicerca.getCodiceClassificatoreGenerico50(),
				
				//classificatori gerarchici
				criteriRicerca.getCodicePianoDeiConti(),
				null,null,//criteriRicerca.getCodiceCofog(),criteriRicerca.getCodiceTipoCofog(),
				criteriRicerca.getCodiceStrutturaAmmCont(), criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				
				criteriRicerca.getCodiceSiopeEntrata(), criteriRicerca.getCodiceTipoSiopeEntrata(),
				null,null,//criteriRicerca.getCodiceSiopeSpesa(), criteriRicerca.getCodiceTipoSiopeSpesa(),
				
				null, null, //criteriRicerca.getCodiceMissione(), criteriRicerca.getCodiceProgramma(),
				null, null, //criteriRicerca.getCodiceTitoloUscita(), criteriRicerca.getCodiceMacroaggregato(),
				criteriRicerca.getCodiceTitoloEntrata(), criteriRicerca.getCodiceTipologia(), criteriRicerca.getCodiceCategoria(),
				
				criteriRicerca.getNumeroAttoDilegge(),
				mapToString(criteriRicerca.getAnnoAttoDilegge()),
				criteriRicerca.getArticoloAttoDilegge(),
				criteriRicerca.getCommaAttoDilegge(),
				criteriRicerca.getPuntoAttoDilegge(),
				criteriRicerca.getTipoAttoDilegge(),
				
				criteriRicerca.getRichiediAccantonamentoFondiDubbiaEsigibilita(),
				criteriRicerca.getFlagEntrataDubbiaEsigFCDE()
				);
		
				
		return result;
	}
	
	
	
	

	/**
	 * Ricerca dettaglio capitolo entrata previsione.
	 *
	 * @param ricercaDettaglioCapitoloUPrev the ricerca dettaglio capitolo u prev
	 * @return the capitolo entrata previsione
	 */
	public CapitoloEntrataPrevisione ricercaDettaglioCapitoloEntrataPrevisione(RicercaDettaglioCapitoloEPrev ricercaDettaglioCapitoloUPrev) {		
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(ricercaDettaglioCapitoloUPrev.getChiaveCapitolo());
		return mapNotNull(bilElem, CapitoloEntrataPrevisione.class, BilMapId.SiacTBilElem_CapitoloEntrataPrevisione);
	}
	
	public CapitoloEntrataPrevisione ricercaDettaglioModulareCapitoloEntrataPrevisione(CapitoloEntrataPrevisione capitoloEntrataPrevisione,ModelDetailEnum[] modelDetails){
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(capitoloEntrataPrevisione.getUid());
		if(siacTBilElem == null) {
			return null;
		}
		CapitoloEntrataPrevisione cep = new CapitoloEntrataPrevisione();
		if(modelDetails != null && modelDetails.length > 0){
			return map(siacTBilElem, cep, BilMapId.SiacTBilElem_Capitolo_Minimal, Converters.byModelDetails(modelDetails));
		}
		map(siacTBilElem, cep, BilMapId.SiacTBilElem_Capitolo_Minimal);
		return cep;
	}

	/**
	 * Ricerca classificatore elemento piano dei conti capitolo.
	 *
	 * @param capitoloEntrataPrevisione the capitolo entrata previsione
	 * @return the elemento piano dei conti
	 */
	public ElementoPianoDeiConti ricercaClassificatoreElementoPianoDeiContiCapitolo(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		return ricercaClassificatoreByClassFamAndUid(capitoloEntrataPrevisione.getUid(), SiacDClassFamEnum.PianoDeiConti);		
	}
	
	/**
	 * Ricerca classificatore struttura amministrativa contabile capitolo.
	 *
	 * @param capitoloEntrataPrevisione the capitolo entrata previsione
	 * @return the struttura amministrativo contabile
	 */
	public StrutturaAmministrativoContabile ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		StrutturaAmministrativoContabile sac = ricercaClassificatoreByClassFamAndUid(capitoloEntrataPrevisione.getUid(), SiacDClassFamEnum.StrutturaAmministrativaContabile);		
		if(sac!=null){
			String assessorato = ricercaAttributoTestoClassificatore(sac.getUid(), "assessorato");
			sac.setAssessorato(assessorato);
		}
		return sac;
	}
	
	/**
	 * Ricerca classificatore categoria tipologia titolo.
	 *
	 * @param capitoloEntrataPrevisione the capitolo entrata previsione
	 * @return the categoria tipologia titolo
	 */
	public CategoriaTipologiaTitolo ricercaClassificatoreCategoriaTipologiaTitolo(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		return ricercaClassificatoreByClassFamAndUid(capitoloEntrataPrevisione.getUid(), SiacDClassFamEnum.EntrataTitolitipologiecategorie);		
	}
	
	/**
	 * Ricerca classificatore categoria tipologia titolo.
	 *
	 * @param uidCapitolo the uid capitolo
	 * @return the categoria tipologia titolo
	 */
	public CategoriaTipologiaTitolo ricercaClassificatoreCategoriaTipologiaTitolo(Integer uidCapitolo) {
		return ricercaClassificatoreByClassFamAndUid(uidCapitolo, SiacDClassFamEnum.EntrataTitolitipologiecategorie);		
	}
	
	/**
	 * Ricerca classificatore tipologia titolo.
	 *
	 * @param capitoloEntrataPrevisione the capitolo entrata previsione
	 * @param categoriaTipologiaTitolo the categoria tipologia titolo
	 * @return the tipologia titolo
	 */
	public TipologiaTitolo ricercaClassificatoreTipologiaTitolo(CapitoloEntrataPrevisione capitoloEntrataPrevisione, CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		
		if(categoriaTipologiaTitolo!=null){
			return classificatoriDad.ricercaPadreCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataPrevisione.getUid(), SiacDClassTipoEnum.Tipologia);
	}
	
	/**
	 * Ricerca classificatore tipologia titolo.
	 *
	 * @param capitoloUid the capitolo uid
	 * @param categoriaTipologiaTitolo the categoria tipologia titolo
	 * @return the tipologia titolo
	 */
	public TipologiaTitolo ricercaClassificatoreTipologiaTitolo(Integer capitoloUid, CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		
		if(categoriaTipologiaTitolo!=null){
			return classificatoriDad.ricercaPadreCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUid, SiacDClassTipoEnum.Tipologia);
	}
	
	/**
	 * Ricerca classificatore titolo entrata.
	 *
	 * @param capitoloEntrataPrevisione the capitolo entrata previsione
	 * @param tipologiaTitolo the tipologia titolo
	 * @return the titolo entrata
	 */
	public TitoloEntrata ricercaClassificatoreTitoloEntrata(CapitoloEntrataPrevisione capitoloEntrataPrevisione, TipologiaTitolo tipologiaTitolo) {
		
		if(tipologiaTitolo!=null){
			return classificatoriDad.ricercaPadreTipologiaTitolo(tipologiaTitolo);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataPrevisione.getUid(), SiacDClassTipoEnum.TitoloEntrata);
	}
	
	/**
	 * Ricerca classificatore titolo entrata.
	 *
	 * @param capitoloEntrataPrevisione the capitolo entrata previsione
	 * @param tipologiaTitolo the tipologia titolo
	 * @return the titolo entrata
	 */
	public TitoloEntrata ricercaClassificatoreTitoloEntrata(Integer capitoloUid, TipologiaTitolo tipologiaTitolo) {
		
		if(tipologiaTitolo!=null){
			return classificatoriDad.ricercaPadreTipologiaTitolo(tipologiaTitolo);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUid, SiacDClassTipoEnum.TitoloEntrata);
	}

	/*
	public Missione ricercaClassificatoreMissione(CapitoloEntrataPrevisione capitoloUscitaGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.Missione);		
	}

	public Programma ricercaClassificatoreProgramma(CapitoloEntrataPrevisione capitoloUscitaGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.Programma);		
	}
	
	public TitoloSpesa ricercaClassificatoreTitoloSpesa(CapitoloEntrataPrevisione capitoloUscitaGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.TitoloSpesa);		
	}
	
	public Macroaggregato ricercaClassificatoreMacroaggregato(CapitoloEntrataPrevisione capitoloUscitaGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.Macroaggregato);		
	}*/
	
	/**
	 * Ricerca classificatore tipo finanziamento.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the tipo finanziamento
	 */
	public TipoFinanziamento ricercaClassificatoreTipoFinanziamento(CapitoloEntrataPrevisione capitoloUscitaGestione) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.TipoFinanziamento);		
	}
	
	/**
	 * Ricerca classificatore tipo fondo.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the tipo fondo
	 */
	public TipoFondo ricercaClassificatoreTipoFondo(CapitoloEntrataPrevisione capitoloUscitaGestione) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.TipoFondo);		
	}
	
	
	/**
	 * Ricerca classificatori generici.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the list
	 */
	public List<ClassificatoreGenerico> ricercaClassificatoriGenerici(CapitoloEntrataPrevisione capitoloUscitaGestione) {
		return ricercaClassificatoriGenerici(capitoloUscitaGestione.getUid());
	}
	
	
	
	
	/**
	 * Populate flags.
	 *
	 * @param capitolo the capitolo
	 */
	public void populateFlags(CapitoloEntrataPrevisione capitolo) {
		super.populateAttrs(capitolo);
		
	}
	
	
	
	/**
	 * Gets the ex capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the ex capitolo
	 */
	public CapitoloEntrataGestione getExCapitolo(CapitoloEntrataPrevisione capitolo) {
		final String methodName = "getExCapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());	
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(), SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		
		if(bilElems.isEmpty()) { // SE non è definito l'ex capitolo su SiacRBilElemRelTempo allora l'ex capitolo è l'equivalente
			bilElems = siacTBilElemRepository.findCapitoloExByIdAndTipoCode(capitolo.getUid(), SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		}		
		
		if(bilElems.isEmpty()){
			return null;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un ex capitolo per il capitolo con uid: "+ capitolo.getUid());
		}
		
		
		return mapNotNull(bilElems.get(0), CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
	}
	
	public int getExCapitoloUid(CapitoloEntrataPrevisione capitolo) {
		final String methodName = "getExCapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(), SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		
		if(bilElems.isEmpty()) { // SE non è definito l'ex capitolo su SiacRBilElemRelTempo allora l'ex capitolo è l'equivalente
			bilElems = siacTBilElemRepository.findCapitoloExByIdAndTipoCode(capitolo.getUid(), SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		}		
		
		if(bilElems.isEmpty()){
			return 0;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un ex capitolo per il capitolo con uid: "+ capitolo.getUid());
		}		
		
		return bilElems.get(0).getUid();
	}
	

	/**
	 * Gets the equivalente.
	 *
	 * @param capitolo the capitolo
	 * @return the equivalente
	 */
	public CapitoloEntrataGestione getEquivalente(CapitoloEntrataPrevisione capitolo) {
		final String methodName = "getEquivalente";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		
		if(bilElems.isEmpty()){
			return null;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un equivalente per il capitolo con uid: "+ capitolo.getUid());
		}
		
				
		return mapNotNull(bilElems.get(0), CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
	}
	
	public int getEquivalenteUid(CapitoloEntrataPrevisione capitolo) {
		final String methodName = "getEquivalente";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		
		if(bilElems.isEmpty()){
			return 0;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un equivalente per il capitolo con uid: "+ capitolo.getUid());
		}
				
		return bilElems.get(0).getUid();
	}

	// SIAC-7858
	public Map<Integer, BigDecimal> getImportoAccertatoPerAnno(int uidCapitolo, int annoMin, int annoMax) {
		List<Object[]> importiPerAnno = siacTBilElemRepository.findImportoAccertatoPerAnno(uidCapitolo, annoMin, annoMax);
		return toMapAnnoImporto(importiPerAnno);
	}
	public Map<Integer, BigDecimal> getImportoIncassatoPerAnno(int uidCapitolo, int annoMin, int annoMax) {
		List<Object[]> importiPerAnno = siacTBilElemRepository.findImportoIncassatoPerAnno(uidCapitolo, annoMin, annoMax);
		return toMapAnnoImporto(importiPerAnno);
	}
	
}

