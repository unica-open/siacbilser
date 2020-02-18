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
//import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemCategoriaEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;

/**
 * The Class CapitoloUscitaGestioneDad.
 *
 * @author 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CapitoloUscitaGestioneDad extends CapitoloDad {
	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;	
	
	/** The siac t bil elem repository. */
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;	
	

	/**
	 * Creates the.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the capitolo uscita gestione
	 */
	public CapitoloUscitaGestione create(CapitoloUscitaGestione capitoloUscitaGestione/*, List<ImportiCapitoloUG> importiCapitolo,
			TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti,
			ClassificazioneCofogProgramma classCofogProgramma, List<ClassificatoreGenerico> listaClassificatoriGenerici,
			StrutturaAmministrativoContabile struttAmmContabile, Macroaggregato macroaggregato, Programma programma*/) {
		
		final String methodName = "create";

		SiacTBilElem bilElem = buidSiacTBilElem(capitoloUscitaGestione/*, importiCapitolo, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti, classCofogProgramma, listaClassificatoriGenerici, struttAmmContabile, macroaggregato, programma*/);
		
		capitoloDao.create(bilElem);		
		log.info(methodName, "created.");		
		
		capitoloUscitaGestione.setUid(bilElem.getUid());

		return capitoloUscitaGestione;

	}
	

	/**
	 * Update.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the capitolo uscita gestione
	 */
	public CapitoloUscitaGestione update(CapitoloUscitaGestione capitoloUscitaGestione/*, List<ImportiCapitoloUG> importiCapitolo, TipoFondo tipoFondo, 
			TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti, ClassificazioneCofogProgramma classCofogProgramma, 
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile, Macroaggregato macroaggregato, Programma programma*/) {

		final String methodName = "update";
		
		SiacTBilElem bilElem = buidSiacTBilElem(capitoloUscitaGestione/*, importiCapitolo, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti, classCofogProgramma, listaClassificatoriGenerici, struttAmmContabile, macroaggregato, programma*/);
		
		setDataCancellazioneAPartireDaDataFineValidita(bilElem);
		
		bilElem = capitoloDao.update(bilElem);
		log.info(methodName, "updated.");	
		
		return map(bilElem, CapitoloUscitaGestione.class);
	}

	
	/**
	 * Elimina.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	public void elimina(CapitoloUscitaGestione capitoloUscitaPrev) {
		SiacTBilElem bilElem = capitoloDao.findById(capitoloUscitaPrev.getUid());
		capitoloDao.deleteLogical(bilElem);
		
	}

	/**
	 * Annulla.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	@Transactional
	public void annulla(CapitoloUscitaGestione capitoloUscitaPrev) {
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
	private SiacTBilElem buidSiacTBilElem(CapitoloUscitaGestione cap/*,
			List<ImportiCapitoloUG> importiCapitolo, TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento,
			ElementoPianoDeiConti elementoPianoDeiConti, ClassificazioneCofogProgramma classCofogProgramma,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile,
			Macroaggregato macroaggregato, Programma programma*/) {

		
		SiacTBilElem bilElem = map(bilancio, SiacTBilElem.class, BilMapId.SiacTBilElem_Bilancio);	
		map(ente, bilElem, BilMapId.SiacTBilElem_Ente);
		cap.setLoginOperazione(loginOperazione);
		bilElem.setLoginOperazione(loginOperazione);
		bilElem.setLivello(1);
		//bilElem.setSiacDBilElemTipo(SiacDBilElemTipoEnum.CapitoloUscitaGestione.getEntity());
		bilElem.setSiacDBilElemTipo(eef.getEntity(SiacDBilElemTipoEnum.CapitoloUscitaGestione, ente.getUid(), SiacDBilElemTipo.class));
		
		
		map(cap, bilElem, BilMapId.SiacTBilElem_CapitoloUscitaGestione);


		
		map(cap.getListaImportiCapitoloUG(), bilElem, BilMapId.SiacTBilElem_CapitoloUscitaGestione_ImportiCapitolo);
		mapNotNull(cap.getTipoFondo(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);		
		mapNotNull(cap.getTipoFinanziamento(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		mapNotNull(cap.getElementoPianoDeiConti(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getClassificazioneCofogProgramma(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getStrutturaAmministrativoContabile(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getMacroaggregato(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getProgramma(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		
		mapNotNull(cap.getRicorrenteSpesa(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		mapNotNull(cap.getPerimetroSanitarioSpesa(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		mapNotNull(cap.getSiopeSpesa(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGerarchico);
		mapNotNull(cap.getTransazioneUnioneEuropeaSpesa(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		mapNotNull(cap.getPoliticheRegionaliUnitarie(), bilElem, BilMapId.SiacTBilElem_ClassificatoreGenerico);
		
		mapNotNull(cap.getClassificatoriGenerici(), bilElem, BilMapId.SiacTBilElem_ClassificatoriGenerici);		
		
		mapAttrs(cap, bilElem);
		
		return bilElem;
	}

	
	



	/**
	 * Ricerca puntuale capitolo uscita gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @return the capitolo uscita gestione
	 */
	public CapitoloUscitaGestione ricercaPuntualeCapitoloUscitaGestione(RicercaPuntualeCapitoloUGest criteriRicerca) {
		final String methodName = "ricercaPuntualeCapitoloUscitaGestione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice():null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(), 
				mapToString(criteriRicerca.getAnnoCapitolo(),null),//mapToString(criteriRicerca.getAnnoEsercizio(),null),
				SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice(),				
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
		return mapNotNull(bilElem, CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
	}
	
	/**
	 * Ricerca puntuale capitolo uscita gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param modelDetails the model details
	 * @return the capitolo uscita gestione
	 */
	public CapitoloUscitaGestione ricercaPuntualeModulareCapitoloUscitaGestione(RicercaPuntualeCapitoloUGest criteriRicerca, CapitoloUscitaGestioneModelDetail... modelDetails) {
		final String methodName = "ricercaPuntualeCapitoloUscitaGestione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio() != null ? SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice() : null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(),
				mapToString(criteriRicerca.getAnnoCapitolo(), null),
				SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice(),
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
		return mapNotNull(bilElem, CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	
	
	/**
	 * Ricerca sintetica capitolo uscita gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<CapitoloUscitaGestione> ricercaSinteticaCapitoloUscitaGestione(RicercaSinteticaCapitoloUGest criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Page<SiacTBilElem> listaCapitoloUscitaGestione = capitoloDao.ricercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloUscitaGestione,
			//	SiacDBilElemCategoriaEnum.byCategoriaCapitoloEvenNull(criteriRicerca.getCategoriaCapitolo()),
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
				
				criteriRicerca.getFlagAssegnabile(),
				criteriRicerca.getFlagFondoSvalutazioneCrediti(),
				criteriRicerca.getFlagFunzioniDelegate(),
				criteriRicerca.getFlagPerMemoria(),
				criteriRicerca.getFlagRilevanteIva(),
				criteriRicerca.getFlagTrasferimentoOrganiComunitari(),
				null,//criteriRicerca.getFlagEntrateRicorrenti(),
				criteriRicerca.getFlagFondoPluriennaleVincolato(),
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),	
				
				null,/*criteriRicerca.getCodiceRicorrenteEntrata(),*/ criteriRicerca.getCodiceRicorrenteSpesa(),
				null,/*criteriRicerca.getCodicePerimetroSanitarioEntrata(),*/ criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				null,/*criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(),*/ criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa(),
				criteriRicerca.getCodicePoliticheRegionaliUnitarie(), 
				
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
				criteriRicerca.getCodiceCofog(),criteriRicerca.getCodiceTipoCofog(),
				criteriRicerca.getCodiceStrutturaAmmCont(), criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				
				null,null,//criteriRicerca.getCodiceSiopeEntrata(), criteriRicerca.getCodiceTipoSiopeEntrata(),
				criteriRicerca.getCodiceSiopeSpesa(), criteriRicerca.getCodiceTipoSiopeSpesa(),
				
				criteriRicerca.getCodiceMissione(), criteriRicerca.getCodiceProgramma(),
				criteriRicerca.getCodiceTitoloUscita(), criteriRicerca.getCodiceMacroaggregato(),
				null,null,null,//criteriRicerca.getCodiceTitoloEntrata(), criteriRicerca.getCodiceTipologia(), criteriRicerca.getCodiceCategoria(),
				
				criteriRicerca.getNumeroAttoDilegge(),
				mapToString(criteriRicerca.getAnnoAttoDilegge()),
				criteriRicerca.getArticoloAttoDilegge(),
				criteriRicerca.getCommaAttoDilegge(),
				criteriRicerca.getPuntoAttoDilegge(),
				criteriRicerca.getTipoAttoDilegge(),
				null,
				
				toPageable(parametriPaginazione));
		
				
		return toListaPaginata(listaCapitoloUscitaGestione,CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
	}
	
	public ImportiCapitoloUG importiRicercaSintetica(RicercaSinteticaCapitoloUGest criteriRicerca) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		List<Object[]> objs = capitoloDao.importiRicercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloUscitaGestione,
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
				
				criteriRicerca.getFlagAssegnabile(),
				criteriRicerca.getFlagFondoSvalutazioneCrediti(),
				criteriRicerca.getFlagFunzioniDelegate(),
				criteriRicerca.getFlagPerMemoria(),
				criteriRicerca.getFlagRilevanteIva(),
				criteriRicerca.getFlagTrasferimentoOrganiComunitari(),
				// criteriRicerca.getFlagEntrateRicorrenti(),
				null,
				criteriRicerca.getFlagFondoPluriennaleVincolato(),
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),
				
				// criteriRicerca.getCodiceRicorrenteEntrata()
				null,
				criteriRicerca.getCodiceRicorrenteSpesa(),
				// criteriRicerca.getCodicePerimetroSanitarioEntrata()
				null,
				criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				// criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata() 
				null,
				criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa(),
				criteriRicerca.getCodicePoliticheRegionaliUnitarie(), 
				
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
				criteriRicerca.getCodiceCofog(),criteriRicerca.getCodiceTipoCofog(),
				criteriRicerca.getCodiceStrutturaAmmCont(), criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				
				// criteriRicerca.getCodiceSiopeEntrata()
				null,
				// criteriRicerca.getCodiceTipoSiopeEntrata()
				null,
				criteriRicerca.getCodiceSiopeSpesa(), criteriRicerca.getCodiceTipoSiopeSpesa(),
				
				criteriRicerca.getCodiceMissione(),
				criteriRicerca.getCodiceProgramma(),
				criteriRicerca.getCodiceTitoloUscita(),
				criteriRicerca.getCodiceMacroaggregato(),
				// criteriRicerca.getCodiceTitoloEntrata()
				null,
				// criteriRicerca.getCodiceTipologia()
				null,
				// criteriRicerca.getCodiceCategoria()
				null,
				
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
		ImportiCapitoloUG icug = new ImportiCapitoloUG();
		popolaImporti(icug, objs);
		
		return icug;
	}
	
	
	/**
	 * Count ricerca sintetica capitolo uscita gestione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the long
	 */
	public Long countRicercaSinteticaCapitoloUscitaGestione(RicercaSinteticaCapitoloUGest criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Long result = capitoloDao.countRicercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloUscitaGestione,
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
				
				criteriRicerca.getFlagAssegnabile(),
				criteriRicerca.getFlagFondoSvalutazioneCrediti(),
				criteriRicerca.getFlagFunzioniDelegate(),
				criteriRicerca.getFlagPerMemoria(),
				criteriRicerca.getFlagRilevanteIva(),
				criteriRicerca.getFlagTrasferimentoOrganiComunitari(),
				null,//criteriRicerca.getFlagEntrateRicorrenti(),
				criteriRicerca.getFlagFondoPluriennaleVincolato(),
				
				//classificatori generici
				criteriRicerca.getCodiceTipoFinanziamento(),
				criteriRicerca.getCodiceTipoFondo(),
				criteriRicerca.getCodiceTipoVincolo(),	
				
				null,/*criteriRicerca.getCodiceRicorrenteEntrata(),*/ criteriRicerca.getCodiceRicorrenteSpesa(),
				null,/*criteriRicerca.getCodicePerimetroSanitarioEntrata(),*/ criteriRicerca.getCodicePerimetroSanitarioSpesa(),
				null,/*criteriRicerca.getCodiceTransazioneUnioneEuropeaEntrata(),*/ criteriRicerca.getCodiceTransazioneUnioneEuropeaSpesa(),
				criteriRicerca.getCodicePoliticheRegionaliUnitarie(), 
				
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
				criteriRicerca.getCodiceCofog(),criteriRicerca.getCodiceTipoCofog(),
				criteriRicerca.getCodiceStrutturaAmmCont(), criteriRicerca.getCodiceTipoStrutturaAmmCont(),
				
				null,null,//criteriRicerca.getCodiceSiopeEntrata(), criteriRicerca.getCodiceTipoSiopeEntrata(),
				criteriRicerca.getCodiceSiopeSpesa(), criteriRicerca.getCodiceTipoSiopeSpesa(),
				
				
				criteriRicerca.getCodiceMissione(), criteriRicerca.getCodiceProgramma(),
				criteriRicerca.getCodiceTitoloUscita(), criteriRicerca.getCodiceMacroaggregato(),
				null,null,null,//criteriRicerca.getCodiceTitoloEntrata(), criteriRicerca.getCodiceTipologia(), criteriRicerca.getCodiceCategoria(),
				
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
	 * Ricerca dettaglio capitolo uscita gestione.
	 *
	 * @param ricercaDettaglioCapitoloUGest the ricerca dettaglio capitolo u gest
	 * @return the capitolo uscita gestione
	 */
	public CapitoloUscitaGestione ricercaDettaglioCapitoloUscitaGestione(RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest) {		
		SiacTBilElem bilElem = siacTBilElemRepository.findOne(ricercaDettaglioCapitoloUGest.getChiaveCapitolo());
		return mapNotNull(bilElem, CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
	}
	
	/**
	 * Ricerca dettaglio capitolo uscita gestione mediante il model detail (se fornito).
	 *
	 * @param capitoloUscitaGestione il capitolo di cui si fa la ricerca
	 * @param modelDetails i model details da utilizzare (se non forniti, viene utilizzato il mapping base)
	 * 
	 * @return the capitolo uscita gestione popolato
	 */
	public CapitoloUscitaGestione ricercaDettaglioModulareCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione, CapitoloUscitaGestioneModelDetail... modelDetails) {
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(capitoloUscitaGestione.getUid());
		if(siacTBilElem == null) {
			return null;
		}
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		if(modelDetails != null && modelDetails.length > 0){
			return map(siacTBilElem, cug, BilMapId.SiacTBilElem_Capitolo_Minimal, Converters.byModelDetails(modelDetails));
		}
		map(siacTBilElem, cug, BilMapId.SiacTBilElem_Capitolo_Minimal);
		return cug;
	}
	
	
	/**
	 * Ricerca classificatore elemento piano dei conti capitolo.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the elemento piano dei conti
	 */
	public ElementoPianoDeiConti ricercaClassificatoreElementoPianoDeiContiCapitolo(CapitoloUscitaGestione capitoloUscitaGestione) {
		return ricercaClassificatoreByClassFamAndUid(capitoloUscitaGestione.getUid(), SiacDClassFamEnum.PianoDeiConti);		
	}
	
	/**
	 * Ricerca classificatore cofog capitolo.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the classificazione cofog
	 */
	public ClassificazioneCofog ricercaClassificatoreCofogCapitolo(CapitoloUscitaGestione capitoloUscitaGestione) {
		return ricercaClassificatoreByClassFamAndUid(capitoloUscitaGestione.getUid(), SiacDClassFamEnum.Cofog);	
	}
	
	/**
	 * Ricerca classificatore struttura amministrativa contabile capitolo.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the struttura amministrativo contabile
	 */
	public StrutturaAmministrativoContabile ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(CapitoloUscitaGestione capitoloUscitaGestione) {
		StrutturaAmministrativoContabile sac =  ricercaClassificatoreByClassFamAndUid(capitoloUscitaGestione.getUid(), SiacDClassFamEnum.StrutturaAmministrativaContabile);
		if(sac!=null){
			String assessorato = ricercaAttributoTestoClassificatore(sac.getUid(), "assessorato");
			sac.setAssessorato(assessorato);
		}
		return sac;
	}

	/**
	 * Ricerca classificatore missione.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @param programma the programma
	 * @return the missione
	 */
	public Missione ricercaClassificatoreMissione(CapitoloUscitaGestione capitoloUscitaGestione, Programma programma) {
		if(programma!=null){
			return classificatoriDad.ricercaPadreProgramma(programma);
		}
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.Missione);		
	}

	/**
	 * Ricerca classificatore programma.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the programma
	 */
	public Programma ricercaClassificatoreProgramma(CapitoloUscitaGestione capitoloUscitaGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.Programma);		
	}
	
	/**
	 * Ricerca classificatore titolo spesa.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @param macroaggregato the macroaggregato
	 * @return the titolo spesa
	 */
	public TitoloSpesa ricercaClassificatoreTitoloSpesa(CapitoloUscitaGestione capitoloUscitaGestione, Macroaggregato macroaggregato) {
		if(macroaggregato!=null){
			return classificatoriDad.ricercaPadreMacroaggregato(macroaggregato);
		} 
		
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.TitoloSpesa);		
	}
	
	/**
	 * Ricerca classificatore macroaggregato.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the macroaggregato
	 */
	public Macroaggregato ricercaClassificatoreMacroaggregato(CapitoloUscitaGestione capitoloUscitaGestione) {
		return ricercaClassificatoreGerarchicoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.Macroaggregato);		
	}
	
	/**
	 * Ricerca classificatore tipo finanziamento.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the tipo finanziamento
	 */
	public TipoFinanziamento ricercaClassificatoreTipoFinanziamento(CapitoloUscitaGestione capitoloUscitaGestione) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.TipoFinanziamento);		
	}
	
	/**
	 * Ricerca classificatore tipo fondo.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the tipo fondo
	 */
	public TipoFondo ricercaClassificatoreTipoFondo(CapitoloUscitaGestione capitoloUscitaGestione) {
		return ricercaClassificatoreGenericoByClassTipoAndUid(capitoloUscitaGestione.getUid(), SiacDClassTipoEnum.TipoFondo);		
	}
	
	
	/**
	 * Ricerca classificatori generici.
	 *
	 * @param capitoloUscitaGestione the capitolo uscita gestione
	 * @return the list
	 */
	public List<ClassificatoreGenerico> ricercaClassificatoriGenerici(CapitoloUscitaGestione capitoloUscitaGestione) {
		return ricercaClassificatoriGenerici(capitoloUscitaGestione.getUid());
	}
	
	
	/**
	 * Populate flags.
	 *
	 * @param capitolo the capitolo
	 */
	public void populateFlags(CapitoloUscitaGestione capitolo) {
		super.populateAttrs(capitolo);
		
	}
	
	
	/**
	 * Gets the ex capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the ex capitolo
	 */
	public CapitoloUscitaGestione getExCapitolo(CapitoloUscitaGestione capitolo) {
		final String methodName = "getExCapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		
		if(bilElems.isEmpty()) { // SE non è definito l'ex capitolo su SiacRBilElemRelTempo allora l'ex capitolo è l'equivalente
			bilElems = siacTBilElemRepository.findCapitoloExByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		}
		
		if(bilElems.isEmpty()){
			return null;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un ex per il capitolo con uid: "+ capitolo.getUid());
		}
		
		return mapNotNull(bilElems.get(0), CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
	}

//	/**
//	 * Gets the equivalente.
//	 *
//	 * @param capitolo the capitolo
//	 * @return the equivalente
//	 */
//	public CapitoloUscitaGestione getEquivalente(CapitoloUscitaGestione capitolo) {
//		final String methodName = "getEquivalente";
//		
//		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
//		
//		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
//		
//		if(bilElems.isEmpty()){
//			return null;
//		}
//		
//		if(bilElems.size()>1){
//			log.warn(methodName, "trovato piu' di un equivalente per il capitolo con uid: "+ capitolo.getUid());
//		}
//				
//		return mapNotNull(bilElems.get(0), CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
//	}



//	public CapitoloUscitaGestione getExCapitoloUscitaGestione(CapitoloUscitaGestione capitolo) {
//		final String methodName = "getExCapitoloUscitaGestione";
//		
//		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
//		
//		SiacTBilElem bilElemOld = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());		
//		
//		return mapNotNull(bilElemOld, CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
//	}
//	
//
//	public CapitoloUscitaGestione getEquivalente(CapitoloUscitaGestione capitolo) {
//		final String methodName = "getEquivalente";
//		
//		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
//		
//		SiacTBilElem bilElemOld = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());		
//		
//		return mapNotNull(bilElemOld, CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
//	}

	/**
	 * aggiunta Ahmad per ricercare il capitolo nell'entita codici stipendi
	 * @param capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione findCapitoloUscitaGestioneFromCodiciStipendi(CapitoloUscitaGestione capitoloUscitaGestione , String stipendioCode){
		final String methodName = "findCapitoloUscitaGestioneFromCodiciStipendi";
		// SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice()
		// ottengo una lista dei capitoli sia entrata che gestione 
		log.debug(methodName, "Ente : "+capitoloUscitaGestione.getEnte().getUid() +"anno bilancio :"+capitoloUscitaGestione.getBilancio().getAnno() );
		List<SiacTBilElem> bilElems = siacTBilElemRepository.ricercaCapitoliFromStipendio(capitoloUscitaGestione.getEnte().getUid(),String.valueOf(capitoloUscitaGestione.getBilancio().getAnno()),
				String.valueOf(capitoloUscitaGestione.getNumeroCapitolo()), String.valueOf(capitoloUscitaGestione.getNumeroArticolo()), stipendioCode, SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		if(bilElems == null || bilElems.isEmpty()){
			return null;
		}
		//prendo il primo
		SiacTBilElem siacTBilElem = bilElems.get(0);
		
		return mapNotNull(siacTBilElem, CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
	}
	
	public Long ricercaImpegniNonAnnullatiCapitoloUscitaGestione(CapitoloUscitaGestione cug){
		Long count = siacTBilElemRepository.countMovgestNonAnnullatiByBilElemId(cug.getUid());
		return count;		
	}



	
}
