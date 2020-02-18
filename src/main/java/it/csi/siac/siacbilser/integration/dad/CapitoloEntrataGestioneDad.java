/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * The Class CapitoloEntrataGestioneDad.
 *
 * @author 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CapitoloEntrataGestioneDad extends CapitoloDad {	

	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;	
	
	/** The siac t bil elem repository. */
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	

	/**
	 * Creates the.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @return the capitolo entrata gestione
	 */
	public CapitoloEntrataGestione create(CapitoloEntrataGestione capitoloEntrataGestione/*, List<ImportiCapitoloEG> importiCapitoloUP,
			TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti,
			List<ClassificatoreGenerico> listaClassificatoriGenerici,
			StrutturaAmministrativoContabile struttAmmContabile, CategoriaTipologiaTitolo categoriaTipologiaTitolo*/) {
		
		final String methodName = "create";

		SiacTBilElem bilElem = buidSiacTBilElem(capitoloEntrataGestione/*, importiCapitoloUP, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti, listaClassificatoriGenerici, struttAmmContabile,categoriaTipologiaTitolo*/);
		
		capitoloDao.create(bilElem);		
		log.info(methodName, "created.");	
		
		capitoloEntrataGestione.setUid(bilElem.getUid());

		return capitoloEntrataGestione;

	}
	

	/**
	 * Update.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @return the capitolo entrata gestione
	 */
	public CapitoloEntrataGestione update(CapitoloEntrataGestione capitoloEntrataGestione/*, List<ImportiCapitoloEG> importiCapitolo, TipoFondo tipoFondo, 
			TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile,
			CategoriaTipologiaTitolo categoriaTipologiaTitolo*/) {

		final String methodName = "update";
		
		SiacTBilElem bilElem = buidSiacTBilElem(capitoloEntrataGestione/*, importiCapitolo, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti,listaClassificatoriGenerici, struttAmmContabile,  categoriaTipologiaTitolo*/);
		
		setDataCancellazioneAPartireDaDataFineValidita(bilElem);
		
		bilElem = capitoloDao.update(bilElem);
		log.info(methodName, "updated.");	
		
		return map(bilElem, CapitoloEntrataGestione.class);
	}

	
	/**
	 * Elimina.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	public void elimina(CapitoloEntrataGestione capitoloUscitaPrev) {
		SiacTBilElem bilElem = capitoloDao.findById(capitoloUscitaPrev.getUid());
		capitoloDao.deleteLogical(bilElem);
		
	}

	/**
	 * Annulla.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	@Transactional
	public void annulla(CapitoloEntrataGestione capitoloUscitaPrev) {
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
	private SiacTBilElem buidSiacTBilElem(CapitoloEntrataGestione cap/*,
			List<ImportiCapitoloEG> importiCapitolo, TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento,
			ElementoPianoDeiConti elementoPianoDeiConti,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile,
			 CategoriaTipologiaTitolo categoriaTipologiaTitolo*/) {

		
		SiacTBilElem bilElem = map(bilancio, SiacTBilElem.class, BilMapId.SiacTBilElem_Bilancio);	
		map(ente, bilElem, BilMapId.SiacTBilElem_Ente);		
		cap.setLoginOperazione(loginOperazione);
		bilElem.setLoginOperazione(loginOperazione);
		bilElem.setLivello(1);
		bilElem.setSiacDBilElemTipo(eef.getEntity(SiacDBilElemTipoEnum.CapitoloEntrataGestione, ente.getUid(), SiacDBilElemTipo.class));//SiacDBilElemTipoEnum.CapitoloEntrataGestione.getEntity());
		
		
		map(cap, bilElem, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
		
		
		
		map(cap.getListaImportiCapitoloEG(), bilElem, BilMapId.SiacTBilElem_CapitoloEntrataGestione_ImportiCapitolo);
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
	 * Ricerca puntuale capitolo entrata gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @return the capitolo entrata gestione
	 */
	public CapitoloEntrataGestione ricercaPuntualeCapitoloEntrataGestione(RicercaPuntualeCapitoloEGest criteriRicerca) {
		final String methodName = "ricercaPuntualeCapitoloEntrataGestione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice():null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(), 
				mapToString(criteriRicerca.getAnnoCapitolo(),null), //mapToString(criteriRicerca.getAnnoEsercizio(),null),
				SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice(),				
				mapToString(criteriRicerca.getNumeroCapitolo(),null),				
				mapToString(criteriRicerca.getNumeroArticolo(),null),
				mapToString(criteriRicerca.getNumeroUEB(),null),
				codiceStato,new PageRequest(0, 1));
		
		SiacTBilElem bilElem; 
		
		try {			
			bilElem =  result.getContent().get(0);			
		} catch (RuntimeException re){
			log.warn(methodName, "", re);
			bilElem = null;
		}
		
		
		log.debug(methodName, "result: "+bilElem);
		return mapNotNull(bilElem, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
	}
	
	/**
	 * Ricerca puntuale capitolo entrata gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @return the capitolo entrata gestione
	 */
	public CapitoloEntrataGestione ricercaPuntualeModulareCapitoloEntrataGestione(RicercaPuntualeCapitoloEGest criteriRicerca, CapitoloEntrataGestioneModelDetail... modelDetails) {
		final String methodName = "ricercaPuntualeCapitoloEntrataGestione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio() != null ? SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice() : null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(),
				mapToString(criteriRicerca.getAnnoCapitolo(), null),
				SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice(),
				mapToString(criteriRicerca.getNumeroCapitolo(), null),
				mapToString(criteriRicerca.getNumeroArticolo(), null),
				mapToString(criteriRicerca.getNumeroUEB(), null),
				codiceStato,new PageRequest(0, 1));
		
		SiacTBilElem bilElem;
		
		try {
			bilElem =  result.getContent().get(0);
		} catch (RuntimeException re){
			log.warn(methodName, "", re);
			bilElem = null;
		}
		
		log.debug(methodName, "result: " + bilElem);
		return mapNotNull(bilElem, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	
	
	
	/**
	 * Ricerca sintetica capitolo entrata gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<CapitoloEntrataGestione> ricercaSinteticaCapitoloEntrataGestione(RicercaSinteticaCapitoloEGest criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Page<SiacTBilElem> listaCapitoloEntrataGestione = capitoloDao.ricercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloEntrataGestione,
				criteriRicerca.getCategoriaCapitolo()!=null && criteriRicerca.getCategoriaCapitolo().getUid()!=0?criteriRicerca.getCategoriaCapitolo().getUid():null,
				criteriRicerca.getCategoriaCapitolo()!=null?criteriRicerca.getCategoriaCapitolo().getCodice():null,
				//SiacDBilElemCategoria.byCategoriaCapitoloEvenNull(criteriRicerca.getCategoriaCapitolo()),
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
				null,//criteriRicerca.getFlagPerMemoria(),
				criteriRicerca.getFlagRilevanteIva(),
				null, //criteriRicerca.getFlagTrasferimentoOrganiComunitari(),	
				criteriRicerca.getFlagEntrateRicorrenti(),
				null, //criteriRicerca.getFlagFondoPluriennaleVinc(),
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),	
				criteriRicerca.getCodiceRicorrenteEntrata(), null, //criteriRicerca.getCodiceRicorrenteSpesa(),
				criteriRicerca.getCodicePerimetroSanitarioEntrata(), null, //criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(), null, //criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa(),
				null,//criteriRicerca.getCodicePoliticheRegionaliUnitarie(), 
				criteriRicerca.getCodiceClassificatoreGenerico1(),
				criteriRicerca.getCodiceClassificatoreGenerico2(),
				criteriRicerca.getCodiceClassificatoreGenerico3(),
				criteriRicerca.getCodiceClassificatoreGenerico4(),
				criteriRicerca.getCodiceClassificatoreGenerico5(),
				criteriRicerca.getCodiceClassificatoreGenerico6(),
				criteriRicerca.getCodiceClassificatoreGenerico7(),
				criteriRicerca.getCodiceClassificatoreGenerico8(),
				criteriRicerca.getCodiceClassificatoreGenerico9(),
				criteriRicerca.getCodiceClassificatoreGenerico10(),
				
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
				null,
				null,
				null,
				null,
				
				//classificatori gerarchici
				criteriRicerca.getCodicePianoDeiConti(),
				null,null,//criteriRicerca.getCodiceCofog(),criteriRicerca.getCodiceTipoCofog(),
				criteriRicerca.getCodiceStrutturaAmmCont(), criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				
				criteriRicerca.getCodiceSiopeEntrata(), criteriRicerca.getCodiceTipoSiopeEntrata(),
				null,null,//criteriRicerca.getCodiceSiopeSpesa(), criteriRicerca.getCodiceTipoSiopeSpesa(),
				
				null,null,//criteriRicerca.getCodiceMissione(), criteriRicerca.getCodiceProgramma(),
				null,null,//criteriRicerca.getCodiceTitoloUscita(), criteriRicerca.getCodiceMacroaggregato(),
				criteriRicerca.getCodiceTitoloEntrata(), criteriRicerca.getCodiceTipologia(), criteriRicerca.getCodiceCategoria(),
				
				criteriRicerca.getNumeroAttoDilegge(),
				mapToString(criteriRicerca.getAnnoAttoDilegge()),
				criteriRicerca.getArticoloAttoDilegge(),
				criteriRicerca.getCommaAttoDilegge(),
				criteriRicerca.getPuntoAttoDilegge(),
				criteriRicerca.getTipoAttoDilegge(),
				null,
				
				toPageable(parametriPaginazione));
		
				
		return toListaPaginata(listaCapitoloEntrataGestione,CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
	}
	
	public ImportiCapitoloEG importiRicercaSintetica(RicercaSinteticaCapitoloEGest criteriRicerca) {
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		List<Object[]> objs = capitoloDao.importiRicercaSinteticaCapitolo(enteDto,
				mapToString(criteriRicerca.getAnnoEsercizio(), null),
				SiacDBilElemTipoEnum.CapitoloEntrataGestione,
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
				// criteriRicerca.getFlagPerMemoria()
				null,
				criteriRicerca.getFlagRilevanteIva(),
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
				// criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				null,
				criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(),
				// criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa()
				null,
				//criteriRicerca.getCodicePoliticheRegionaliUnitarie()
				null,
				criteriRicerca.getCodiceClassificatoreGenerico1(),
				criteriRicerca.getCodiceClassificatoreGenerico2(),
				criteriRicerca.getCodiceClassificatoreGenerico3(),
				criteriRicerca.getCodiceClassificatoreGenerico4(),
				criteriRicerca.getCodiceClassificatoreGenerico5(),
				criteriRicerca.getCodiceClassificatoreGenerico6(),
				criteriRicerca.getCodiceClassificatoreGenerico7(),
				criteriRicerca.getCodiceClassificatoreGenerico8(),
				criteriRicerca.getCodiceClassificatoreGenerico9(),
				criteriRicerca.getCodiceClassificatoreGenerico10(),
				
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
				null,
				null,
				null,
				null,
				
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
				// criteriRicerca.getCodiceProgramma(),
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
				null);
		if(objs == null) {
			return null;
		}
		
		// FIXME: riscrivere in maniera migliore
		ImportiCapitoloEG iceg = new ImportiCapitoloEG();
		popolaImporti(iceg, objs);
		
		return iceg;
	}
	
	/**
	 * Count ricerca sintetica capitolo entrata gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the long
	 */
	public Long countRicercaSinteticaCapitoloEntrataGestione(RicercaSinteticaCapitoloEGest criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Long result =  capitoloDao.countRicercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloEntrataGestione,
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
				null,//criteriRicerca.getFlagPerMemoria(),
				criteriRicerca.getFlagRilevanteIva(),
				null, //criteriRicerca.getFlagTrasferimentoOrganiComunitari(),	
				criteriRicerca.getFlagEntrateRicorrenti(),
				null, //criteriRicerca.getFlagFondoPluriennaleVinc(),
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),
				
				criteriRicerca.getCodiceRicorrenteEntrata(), null, //criteriRicerca.getCodiceRicorrenteSpesa(),
				criteriRicerca.getCodicePerimetroSanitarioEntrata(), null, //criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(), null, //criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa(),
				null,//criteriRicerca.getCodicePoliticheRegionaliUnitarie(), 
				
				criteriRicerca.getCodiceClassificatoreGenerico1(),
				criteriRicerca.getCodiceClassificatoreGenerico2(),
				criteriRicerca.getCodiceClassificatoreGenerico3(),
				criteriRicerca.getCodiceClassificatoreGenerico4(),
				criteriRicerca.getCodiceClassificatoreGenerico5(),
				criteriRicerca.getCodiceClassificatoreGenerico6(),
				criteriRicerca.getCodiceClassificatoreGenerico7(),
				criteriRicerca.getCodiceClassificatoreGenerico8(),
				criteriRicerca.getCodiceClassificatoreGenerico9(),
				criteriRicerca.getCodiceClassificatoreGenerico10(),
				
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
				null,
				null,
				null,
				null,
				
				//classificatori gerarchici
				criteriRicerca.getCodicePianoDeiConti(),
				null,null,//criteriRicerca.getCodiceCofog(),criteriRicerca.getCodiceTipoCofog(),
				criteriRicerca.getCodiceStrutturaAmmCont(), criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				
				criteriRicerca.getCodiceSiopeEntrata(), criteriRicerca.getCodiceTipoSiopeEntrata(),
				null,null,//criteriRicerca.getCodiceSiopeSpesa(), criteriRicerca.getCodiceTipoSiopeSpesa(),
				
				null,null,//criteriRicerca.getCodiceMissione(), criteriRicerca.getCodiceProgramma(),
				null,null,//criteriRicerca.getCodiceTitoloUscita(), criteriRicerca.getCodiceMacroaggregato(),
				criteriRicerca.getCodiceTitoloEntrata(), criteriRicerca.getCodiceTipologia(), criteriRicerca.getCodiceCategoria(),
				
				criteriRicerca.getNumeroAttoDilegge(),
				mapToString(criteriRicerca.getAnnoAttoDilegge()),
				criteriRicerca.getArticoloAttoDilegge(),
				criteriRicerca.getCommaAttoDilegge(),
				criteriRicerca.getPuntoAttoDilegge(),
				criteriRicerca.getTipoAttoDilegge(),
				null
				);
		
				
		return result;
	}

	

	

	/**
	 * Ricerca dettaglio capitolo entrata gestione.
	 *
	 * @param ricercaDettaglioCapitoloUPrev the ricerca dettaglio capitolo u prev
	 * @return the capitolo entrata gestione
	 */
	public CapitoloEntrataGestione ricercaDettaglioCapitoloEntrataGestione(RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloUPrev) {		
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(ricercaDettaglioCapitoloUPrev.getChiaveCapitolo());
		return mapNotNull(bilElem, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
	}
	

	public CapitoloEntrataGestione ricercaDettaglioModulareCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione, CapitoloEntrataGestioneModelDetail... modelDetails) {
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(capitoloEntrataGestione.getUid());
		if(siacTBilElem == null) {
			return null;
		}
		CapitoloEntrataGestione cep = new CapitoloEntrataGestione();
		if(modelDetails != null && modelDetails.length > 0){
			return map(siacTBilElem, cep, BilMapId.SiacTBilElem_Capitolo_Minimal, Converters.byModelDetails(modelDetails));
		}
		map(siacTBilElem, cep, BilMapId.SiacTBilElem_Capitolo_Minimal);
		return cep;
	} 
	
	
	/**
	 * Ricerca classificatore elemento piano dei conti capitolo.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @return the elemento piano dei conti
	 */
	public ElementoPianoDeiConti ricercaClassificatoreElementoPianoDeiContiCapitolo(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoreByClassFamAndUid(capitoloEntrataGestione.getUid(), SiacDClassFamEnum.PianoDeiConti);		
	}
	
	/**
	 * Ricerca classificatore struttura amministrativa contabile capitolo.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @return the struttura amministrativo contabile
	 */
	public StrutturaAmministrativoContabile ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(CapitoloEntrataGestione capitoloEntrataGestione) {
		StrutturaAmministrativoContabile sac =  ricercaClassificatoreByClassFamAndUid(capitoloEntrataGestione.getUid(), SiacDClassFamEnum.StrutturaAmministrativaContabile);	
		if(sac!=null){
			String assessorato = ricercaAttributoTestoClassificatore(sac.getUid(), "assessorato");
			sac.setAssessorato(assessorato);
		}
		return sac;
		
	}	
	
	/**
	 * Ricerca classificatore categoria tipologia titolo.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the categoria tipologia titolo
	 */
	public CategoriaTipologiaTitolo ricercaClassificatoreCategoriaTipologiaTitolo(CapitoloEntrataGestione capitoloUscitaGestione) {
		return ricercaClassificatoreByClassFamAndUid(capitoloUscitaGestione.getUid(), SiacDClassFamEnum.EntrataTitolitipologiecategorie);		
	}
	
	/**
	 * Ricerca classificatore tipologia titolo.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @param categoriaTipologiaTitolo the categoria tipologia titolo
	 * @return the tipologia titolo
	 */
	public TipologiaTitolo ricercaClassificatoreTipologiaTitolo(CapitoloEntrataGestione capitoloEntrataGestione,
			CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		
		if(categoriaTipologiaTitolo!=null){
			return classificatoriDad.ricercaPadreCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.Tipologia);
	}
	
	/**
	 * Ricerca classificatore titolo entrata.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @param tipologiaTitolo the tipologia titolo
	 * @return the titolo entrata
	 */
	public TitoloEntrata ricercaClassificatoreTitoloEntrata(CapitoloEntrataGestione capitoloEntrataGestione, TipologiaTitolo tipologiaTitolo) {
		
		if(tipologiaTitolo!=null){
			return classificatoriDad.ricercaPadreTipologiaTitolo(tipologiaTitolo);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.TitoloEntrata);
	}
	
	
/*
	public Missione ricercaClassificatoreMissione(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.Missione);		
	}

	public Programma ricercaClassificatoreProgramma(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.Programma);		
	}
	
	public TitoloSpesa ricercaClassificatoreTitoloSpesa(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.TitoloSpesa);		
	}
	
	public Macroaggregato ricercaClassificatoreMacroaggregato(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.Macroaggregato);		
	}
*/
	/**
 * Ricerca classificatore tipo finanziamento.
 *
 * @param capitoloEntrataGestione the capitolo entrata gestione
 * @return the tipo finanziamento
 */
public TipoFinanziamento ricercaClassificatoreTipoFinanziamento(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.TipoFinanziamento);		
	}
	
	/**
	 * Ricerca classificatore tipo fondo.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @return the tipo fondo
	 */
	public TipoFondo ricercaClassificatoreTipoFondo(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(capitoloEntrataGestione.getUid(), SiacDClassTipoEnum.TipoFondo);		
	}
	
	
	/**
	 * Ricerca classificatori generici.
	 *
	 * @param capitoloEntrataGestione the capitolo entrata gestione
	 * @return the list
	 */
	public List<ClassificatoreGenerico> ricercaClassificatoriGenerici(CapitoloEntrataGestione capitoloEntrataGestione) {
		return ricercaClassificatoriGenerici(capitoloEntrataGestione.getUid());
	}
	
	
	
	/**
	 * Populate flags.
	 *
	 * @param capitolo the capitolo
	 */
	public void populateFlags(CapitoloEntrataGestione capitolo) {
		super.populateAttrs(capitolo);
		
	}
	
	
	/**
	 * Gets the ex capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the ex capitolo
	 */
	public CapitoloEntrataGestione getExCapitolo(CapitoloEntrataGestione capitolo) {
		final String methodName = "getExCapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		
		if(bilElems.isEmpty()) { // SE non è definito l'ex capitolo su SiacRBilElemRelTempo allora l'ex capitolo è l'equivalente
			bilElems = siacTBilElemRepository.findCapitoloExByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		}	
		
		if(bilElems.isEmpty()){
			return null;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un ex capitolo per il capitolo con uid: "+ capitolo.getUid());
		}
		
		return mapNotNull(bilElems.get(0), CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
	}
//
//	/**
//	 * Gets the equivalente.
//	 *
//	 * @param capitolo the capitolo
//	 * @return the equivalente
//	 */
//	public CapitoloEntrataGestione getEquivalente(CapitoloEntrataGestione capitolo) {
//		final String methodName = "getEquivalente";
//		
//		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
//		
//		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
//		
//		if(bilElems.isEmpty()){
//			return null;
//		}
//		
//		if(bilElems.size()>1){
//			log.warn(methodName, "trovato piu' di un equivalete capitolo per il capitolo con uid: "+ capitolo.getUid());
//		}
//		
//				
//		return mapNotNull(bilElems.get(0), CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
//	}



//	public CapitoloEntrataGestione getExCapitoloEntrataGestione(CapitoloEntrataGestione capitolo) {
//		final String methodName = "getEquivalenteGestione";
//		
//		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
//		
//		SiacTBilElem bilElemOld = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());		
//		
//		return mapNotNull(bilElemOld, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
//	}
//	
//
//	public CapitoloEntrataGestione getEquivalente(CapitoloEntrataGestione capitolo) {
//		final String methodName = "getEquivalente";
//		
//		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
//		
//		SiacTBilElem bilElemOld = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());		
//		
//		return mapNotNull(bilElemOld, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);
//	}


	/**
	 * aggiunta Ahmad per ricercare il capitolo nell'entita codici stipendi
	 * @param capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione findCapitoloEntrataGestioneFromCodiciStipendi(CapitoloEntrataGestione capitoloEntrataGestione,String stipendioCode){
		final String methodName = "findCapitoloEntrataGestioneFromCodiciStipendi";
		//SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice()
		// ottengo una lista dei capitoli sia entrata che gestione 
		log.debug(methodName, "ente uid :" +capitoloEntrataGestione.getEnte().getUid() +" NumeroArticolo : " +capitoloEntrataGestione.getNumeroArticolo()
				+" numeroCapitolo : "+capitoloEntrataGestione.getNumeroCapitolo() +" anno di bilancio "+capitoloEntrataGestione.getBilancio().getAnno()
				+" TIPO CAPITOLO " +SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice() + " codiceStipendio: " + stipendioCode);
		List<SiacTBilElem> bilElems = siacTBilElemRepository.ricercaCapitoliFromStipendio(capitoloEntrataGestione.getEnte().getUid(),String.valueOf(capitoloEntrataGestione.getBilancio().getAnno()),
				String.valueOf(capitoloEntrataGestione.getNumeroCapitolo()), String.valueOf(capitoloEntrataGestione.getNumeroArticolo()), stipendioCode, SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		if(bilElems == null || bilElems.isEmpty()){
			return null;
		}
		//prendo il primo
		SiacTBilElem siacTBilElem = bilElems.get(0);
		
		return mapNotNull(siacTBilElem, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione);

	}

	
	public Long ricercaAccertamentiNonAnnullatiCapitoloEntrataGestione(CapitoloEntrataGestione ceg){
		final String methodName = "ricercaAccertamentiCapitoloEntrataGestione";		
		log.debug(methodName, "uid capitolo entrata gestione: "+ ceg.getUid());
		Long count = siacTBilElemRepository.countMovgestNonAnnullatiByBilElemId(ceg.getUid());
		return count;		
	}


	

	

	
	
}
