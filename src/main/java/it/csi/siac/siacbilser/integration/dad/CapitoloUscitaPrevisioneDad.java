/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;


import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
//import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemCategoriaEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaPrevisioneModelDetail;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloUscitaPrevisioneDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CapitoloUscitaPrevisioneDad extends CapitoloDad {
	
	/** The siac t bil elem repository. */
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	

	/**
	 * Creates the.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the capitolo uscita previsione
	 */
	public CapitoloUscitaPrevisione create(CapitoloUscitaPrevisione capitoloUscitaPrevisione /*, List<ImportiCapitoloUP> importiCapitoloUP,
			TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti,
			ClassificazioneCofogProgramma classCofogProgramma, List<ClassificatoreGenerico> listaClassificatoriGenerici,
			StrutturaAmministrativoContabile struttAmmContabile, Macroaggregato macroaggregato, Programma programma*/) {

		final String methodName = "create";
		SiacTBilElem cup = buidSiacTBilElem(capitoloUscitaPrevisione /*, importiCapitoloUP, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti, classCofogProgramma, listaClassificatoriGenerici, struttAmmContabile, macroaggregato, programma*/);

		if (log.isDebugEnabled()) {
			log.debug(methodName, "delego al dao!");
			log.debug(methodName, "Inserimento di: " + ToStringBuilder.reflectionToString(cup, ToStringStyle.MULTI_LINE_STYLE));
			log.debug(methodName, "Inserimento di: " + ToStringBuilder.reflectionToString(cup.getSiacTBil(), ToStringStyle.MULTI_LINE_STYLE));
			log.debug(methodName, "Inserimento di: " + ToStringBuilder.reflectionToString(cup.getSiacTBil().getSiacRBilStatoOps(), ToStringStyle.MULTI_LINE_STYLE));
			log.debug(methodName, "Inserimento di dettagli: ");
			for (SiacTBilElemDet elemDet : cup.getSiacTBilElemDets()) {
				log.debug(methodName, ToStringBuilder.reflectionToString(elemDet, ToStringStyle.MULTI_LINE_STYLE));
			}
		}
		
		//capitoloUscitaPrevisioneRepository.saveAndFlush(cup);
		capitoloDao.create(cup);
		log.debug(methodName, "Il dao ha fatto persist!");
		
		capitoloUscitaPrevisione.setUid(cup.getUid());

		return capitoloUscitaPrevisione;

	}
	
	
	
	/**
	 * Buid siac t bil elem.
	 *
	 * @param cap the cap
	 * @return the siac t bil elem
	 */
	private SiacTBilElem buidSiacTBilElem(CapitoloUscitaPrevisione cap /*,
			List<ImportiCapitoloUP> importiCapitoloUP, TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento,
			ElementoPianoDeiConti elementoPianoDeiConti, ClassificazioneCofogProgramma classCofogProgramma,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile,
			Macroaggregato macroaggregato, Programma programma*/) {

		
		SiacTBilElem bilElem = map(bilancio, SiacTBilElem.class, BilMapId.SiacTBilElem_Bilancio);	
		map(ente, bilElem, BilMapId.SiacTBilElem_Ente);		
		cap.setLoginOperazione(loginOperazione);
		bilElem.setLoginOperazione(loginOperazione);
		bilElem.setLivello(1);
		//bilElem.setSiacDBilElemTipo(SiacDBilElemTipoEnum.CapitoloUscitaPrevisione.getEntity());	
		bilElem.setSiacDBilElemTipo(eef.getEntity(SiacDBilElemTipoEnum.CapitoloUscitaPrevisione, ente.getUid(), SiacDBilElemTipo.class));
		
		map(cap, bilElem, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione);
		//map(capitoloUscitaPrevisione, bilElem, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione_StatoOperativoElementoDiBilancio); //NB: va eseguita per forza dopo il mapping dell'ente e del login operazione
		
			
		
		map(cap.getListaImportiCapitoloUP(), bilElem, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione_ImportiCapitolo);
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

	
	
	
	
	/* Versione con i DTO! non cancellare!
	private CapitoloUscitaPrevisioneDto buidCapitoloUscitaPrevisioneDto(CapitoloUscitaPrevisione capitoloUscitaPrevisione,
			List<ImportiCapitoloUP> importiCapitoloUP, TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento,
			ElementoPianoDeiConti elementoPianoDeiConti, ClassificazioneCofogProgramma classCofogProgramma,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile,
			Macroaggregato macroaggregato, Programma programma) {

		
		CapitoloUscitaPrevisioneDto capitoloUscitaPrevisioneDto = map(capitoloUscitaPrevisione, CapitoloUscitaPrevisioneDto.class, BilMapId.CapitoloUscitaPrevisione);		
		capitoloUscitaPrevisioneDto.setLoginOperazione(loginOperazione);//map(loginOperazione, capitoloUscitaPrevisioneDto, DmB.CapitoloUscitaPrevisioneDto_loginOperazione);		
		capitoloUscitaPrevisioneDto.setLivello(1);
		
		
		map(bilancio, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_Bilancio);		
		map(ente, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_Ente);		
		//capitoloUscitaPrevisioneDto.setPeriodo(periodoDao.findPeriodoValido(capitoloUscitaPrevisioneDto.getEnteProprietario()));
		
		map(importiCapitoloUP, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ImportiCapitolo);
		mapNotNull(tipoFondo, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoreGenerico);		
		mapNotNull(tipoFinanziamento, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoreGenerico);
		mapNotNull(elementoPianoDeiConti, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoreGerarchico);
		mapNotNull(classCofogProgramma, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoreGerarchico);
		mapNotNull(struttAmmContabile, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoreGerarchico);
		mapNotNull(listaClassificatoriGenerici, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoriGenerici);		
		mapNotNull(macroaggregato, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoreGerarchico);
		mapNotNull(programma, capitoloUscitaPrevisioneDto, BilMapId.CapitoloUscitaPrevisioneDto_ClassificatoreGerarchico);
		
		return capitoloUscitaPrevisioneDto;
	}*/
	
	
	/*private CapitoloUscitaPrevisioneDto buidCapitoloUscitaPrevisioneDto___TO_REPLACE___OLD____(CapitoloUscitaPrevisione capitoloUscitaPrevisione,
			List<ImportiCapitoloUP> importiCapitoloUP, TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento,
			ElementoPianoDeiConti elementoPianoDeiConti, ClassificazioneCofogProgramma classCofogProgramma,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile,
			Macroaggregato macroaggregato, Programma programma) {

		CapitoloUscitaPrevisioneDto capitoloUscitaPrevisioneDto = buildCapitoloUscitaPrevisione(capitoloUscitaPrevisione, bilancio, ente);


		capitoloUscitaPrevisioneDto.setLoginOperazione(loginOperazione);

		setImporti(capitoloUscitaPrevisioneDto, importiCapitoloUP, loginOperazione);

		addCodifiche(capitoloUscitaPrevisioneDto, tipoFondo, tipoFinanziamento, listaClassificatoriGenerici, elementoPianoDeiConti,
				struttAmmContabile, classCofogProgramma, macroaggregato, programma, loginOperazione);
		return capitoloUscitaPrevisioneDto;
	}

	

	public CapitoloUscitaPrevisioneDto buildCapitoloUscitaPrevisione(CapitoloUscitaPrevisione capitoloUscitaPrevisione, Bilancio bilancio, Ente ente) {
		// Mapper dozerBeanMapper = businessHandler.getDozerBeanMapper();

		EnteDto enteDto = map(ente, EnteDto.class);

		CapitoloUscitaPrevisioneDto capitoloUscitaPrevisioneDto = map(capitoloUscitaPrevisione, CapitoloUscitaPrevisioneDto.class);

		capitoloUscitaPrevisioneDto.setOrdine(String.format("%05d", capitoloUscitaPrevisione.getNumeroCapitolo()));
		capitoloUscitaPrevisioneDto.setLivello(1);

		capitoloUscitaPrevisioneDto.setBilancio(map(bilancio, BilancioDto.class));

		capitoloUscitaPrevisioneDto.setEnteProprietario(enteDto);

		capitoloUscitaPrevisioneDto.setPeriodo(periodoDao.findPeriodoValido(enteDto));

		return capitoloUscitaPrevisioneDto;
	}

	private static Map<Integer, String> TIPO_ELEM_BIL_TO_IMPORTO_MAP = new HashMap<Integer, String>();
	static {
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(1, "stanziamento");
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(2, "stanziamentoCassa");
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(3, "stanziamentoResiduo");
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(4, "fondoPluriennaleVinc");
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(5, "stanziamentoCassaIniziale");
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(6, "stanziamentoIniziale");
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(7, "stanziamentoProposto");
		TIPO_ELEM_BIL_TO_IMPORTO_MAP.put(8, "stanziamentoResiduoIniziale");
	}
	
	public void setImporti(CapitoloUscitaPrevisioneDto capitoloUscitaPrevisioneDto, List<ImportiCapitoloUP> listaImportiCapitoloUP,
			String loginOperazione) {
		EnteDto enteDto = capitoloUscitaPrevisioneDto.getEnteProprietario();

		Set<DettaglioElementoBilancioDto> dettagliElementoBilancio = new HashSet<DettaglioElementoBilancioDto>();

		for (ImportiCapitoloUP importiCapitolo : listaImportiCapitoloUP) {
			PeriodoDto periodo = periodoDao.findByAnno(importiCapitolo.getAnnoCompetenza(), enteDto);

			BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(importiCapitolo);

			for (Map.Entry<Integer, String> e : TIPO_ELEM_BIL_TO_IMPORTO_MAP.entrySet()) {
				DettaglioElementoBilancioDto deb = new DettaglioElementoBilancioDto();

				deb.setElementoBilancio(capitoloUscitaPrevisioneDto);
				deb.setEnteProprietario(enteDto);
				deb.setLoginOperazione(loginOperazione);

				deb.setImporto((BigDecimal) bwic.getPropertyValue(e.getValue()));
				deb.setTipo(e.getKey());

				deb.setPeriodo(periodo);

				dettagliElementoBilancio.add(deb);
			}
		}

		capitoloUscitaPrevisioneDto.setDettagliElementoBilancio(dettagliElementoBilancio);
	}

	public void addCodifiche(CapitoloUscitaPrevisioneDto capitoloUscitaPrevisioneDto, TipoFondo tipoFondo, TipoFinanziamento tipoFinanziamento,
			List<ClassificatoreGenerico> listaClassificatoriGenerici, ElementoPianoDeiConti elementoPianoDeiConti,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile, ClassificazioneCofogProgramma classificazioneCofogProgramma,
			Macroaggregato macroaggregato, Programma programma, String loginOperazione) {

		EnteDto enteDto = capitoloUscitaPrevisioneDto.getEnteProprietario();
		

		if (tipoFondo != null)
			capitoloUscitaPrevisioneDto.addCodifica(map(tipoFondo, CodificaDto.class), enteDto, loginOperazione);

		if (tipoFinanziamento != null)
			capitoloUscitaPrevisioneDto.addCodifica(map(tipoFinanziamento, CodificaDto.class), enteDto, loginOperazione);

		if (elementoPianoDeiConti != null)
			capitoloUscitaPrevisioneDto.addCodifica(map(elementoPianoDeiConti, CodificaDto.class), enteDto, loginOperazione);

		if (strutturaAmministrativoContabile != null)
			capitoloUscitaPrevisioneDto.addCodifica(map(strutturaAmministrativoContabile, CodificaDto.class), enteDto,
					loginOperazione);

		if (classificazioneCofogProgramma != null)
			capitoloUscitaPrevisioneDto.addCodifica(map(classificazioneCofogProgramma, CodificaDto.class), enteDto, loginOperazione);

		if (macroaggregato != null)
			capitoloUscitaPrevisioneDto.addCodifica(map(macroaggregato, CodificaDto.class), enteDto, loginOperazione);

		if (programma != null)
			capitoloUscitaPrevisioneDto.addCodifica(map(programma, CodificaDto.class), enteDto, loginOperazione);

		if (listaClassificatoriGenerici != null)
			for (ClassificatoreGenerico cg : listaClassificatoriGenerici)
				if (cg != null)
					capitoloUscitaPrevisioneDto.addCodifica(map(cg, CodificaDto.class), enteDto, loginOperazione);

	}*/

	/**
	 * Update.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the capitolo uscita previsione
	 */
	public CapitoloUscitaPrevisione update(CapitoloUscitaPrevisione capitoloUscitaPrevisione /*, List<ImportiCapitoloUP> importiCapitoloUP, TipoFondo tipoFondo, 
			TipoFinanziamento tipoFinanziamento, ElementoPianoDeiConti elementoPianoDeiConti, ClassificazioneCofogProgramma classCofogProgramma, 
			List<ClassificatoreGenerico> listaClassificatoriGenerici, StrutturaAmministrativoContabile struttAmmContabile, Macroaggregato macroaggregato, Programma programma*/) {
		
		final String methodName = "update";
		
		SiacTBilElem bilElem = buidSiacTBilElem(capitoloUscitaPrevisione /*, importiCapitoloUP, tipoFondo, tipoFinanziamento,
				elementoPianoDeiConti, classCofogProgramma, listaClassificatoriGenerici, struttAmmContabile, macroaggregato, programma*/);
				
		setDataCancellazioneAPartireDaDataFineValidita(bilElem);
		
		bilElem = capitoloDao.update(bilElem);
		log.info(methodName, "updated.");
		
		return map(bilElem, CapitoloUscitaPrevisione.class);
	}

	

	



	/**
	 * Ricerca puntuale capitolo uscita previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @return the capitolo uscita previsione
	 */
	public CapitoloUscitaPrevisione ricercaPuntualeCapitoloUscitaPrevisione(RicercaPuntualeCapitoloUPrev criteriRicerca) {
		final String methodName = "ricercaPuntualeCapitoloUscitaPrevisione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice():null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(), 
				mapToString(criteriRicerca.getAnnoCapitolo(),null),	//mapToString(criteriRicerca.getAnnoEsercizio(),null),
				SiacDBilElemTipoEnum.CapitoloUscitaPrevisione.getCodice(),
				mapToString(criteriRicerca.getNumeroCapitolo(),null),				
				mapToString(criteriRicerca.getNumeroArticolo(),null),
				mapToString(criteriRicerca.getNumeroUEB(),null),
				codiceStato,new PageRequest(0, 1));
		
		SiacTBilElem cup; 
		
		try {
			//if(result.hasContent() && result.getContent()!=null && result.getContent().size()>0){
			cup =  result.getContent().get(0);
			//}
		} catch (RuntimeException re){
			log.warn(methodName, "cup non trovato. Returning null", re);
			cup = null;
		}
		
		
		//SiacTBilElem cup = capitoloUscitaPrevisioneDao.ricercaPuntualeCapitoloUscitaPrevisione(null, enteDto, criteriRicerca);
		log.debug(methodName, "result: "+cup);
		return mapNotNull(cup, CapitoloUscitaPrevisione.class, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione);
	}
	
	/**
	 * Ricerca puntuale capitolo uscita previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param modelDetails the model details
	 * @return the capitolo uscita previsione
	 */
	public CapitoloUscitaPrevisione ricercaPuntualeModulareCapitoloUscitaPrevisione(RicercaPuntualeCapitoloUPrev criteriRicerca, CapitoloUscitaPrevisioneModelDetail... modelDetails) {
		final String methodName = "ricercaPuntualeCapitoloUscitaPrevisione";
		
		String codiceStato = criteriRicerca.getStatoOperativoElementoDiBilancio() != null ? SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativoElementoDiBilancio()).getCodice() : null;		
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(),
				mapToString(criteriRicerca.getAnnoCapitolo(),null),
				SiacDBilElemTipoEnum.CapitoloUscitaPrevisione.getCodice(),
				mapToString(criteriRicerca.getNumeroCapitolo(),null),
				mapToString(criteriRicerca.getNumeroArticolo(),null),
				mapToString(criteriRicerca.getNumeroUEB(),null),
				codiceStato,new PageRequest(0, 1));
		
		SiacTBilElem cup;
		
		try {
			cup =  result.getContent().get(0);
		} catch (RuntimeException re){
			log.warn(methodName, "cup non trovato. Returning null", re);
			cup = null;
		}
		
		log.debug(methodName, "result: " + cup);
		return mapNotNull(cup, CapitoloUscitaPrevisione.class, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Ricerca sintetica capitolo uscita previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<CapitoloUscitaPrevisione> ricercaSinteticaCapitoloUscitaPrevisione(RicercaSinteticaCapitoloUPrev criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Page<SiacTBilElem> listaCapitoloUscitaPrevisione = capitoloDao.ricercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloUscitaPrevisione,
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
				null,
				
								
				toPageable(parametriPaginazione));
		
				
		return toListaPaginata(listaCapitoloUscitaPrevisione,CapitoloUscitaPrevisione.class, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione);
	}
	
	public ImportiCapitoloUP importiRicercaSintetica(RicercaSinteticaCapitoloUPrev criteriRicerca) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		List<Object[]> objs = capitoloDao.importiRicercaSinteticaCapitolo(enteDto,
				mapToString(criteriRicerca.getAnnoEsercizio(), null),
				SiacDBilElemTipoEnum.CapitoloUscitaPrevisione,
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
				//criteriRicerca.getFlagEntrateRicorrenti(),
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
				criteriRicerca.getCodiceSiopeSpesa(),
				criteriRicerca.getCodiceTipoSiopeSpesa(),
				
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
		ImportiCapitoloUP icup = new ImportiCapitoloUP();
		popolaImporti(icup, objs);
		
		return icup;
	}
	
	
	/**
	 * Count ricerca sintetica capitolo uscita previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the long
	 */
	public Long countRicercaSinteticaCapitoloUscitaPrevisione(RicercaSinteticaCapitoloUPrev criteriRicerca, ParametriPaginazione parametriPaginazione) {
		
		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		String codiceStato = criteriRicerca.getStatoOperativo()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStatoOperativo()).getCodice():null;
		
		
		Long result  = capitoloDao.countRicercaSinteticaCapitolo(enteDto, 
				mapToString(criteriRicerca.getAnnoEsercizio(),null), 
				SiacDBilElemTipoEnum.CapitoloUscitaPrevisione,
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
	 * Ricerca dettaglio capitolo uscita previsione.
	 *
	 * @param ricercaDettaglioCapitoloUPrev the ricerca dettaglio capitolo u prev
	 * @return the capitolo uscita previsione
	 */
	public CapitoloUscitaPrevisione ricercaDettaglioCapitoloUscitaPrevisione(RicercaDettaglioCapitoloUPrev ricercaDettaglioCapitoloUPrev) {
		final String methodName = "ricercaDettaglioCapitoloUscitaPrevisione";
		//SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		
		SiacTBilElem cup = siacTBilElemRepository.findOne(ricercaDettaglioCapitoloUPrev.getChiaveCapitolo());
		
		//SiacTBilElem cup = ((CapitoloUscitaPrevisioneDaoImpl) capitoloUscitaPrevisioneDao).ricercaDettaglioCapitoloUscitaPrevisione(null, enteDto, ricercaDettaglioCapitoloUPrev);
		log.debug(methodName, "dao result: "+cup);
		return mapNotNull(cup, CapitoloUscitaPrevisione.class, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione);
	}
	
	public CapitoloUscitaPrevisione ricercaDettaglioModulareCapitoloUscitaPrevisione(CapitoloUscitaPrevisione capitoloUscitaPrevisione,CapitoloUscitaPrevisioneModelDetail[] modelDetails){
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(capitoloUscitaPrevisione.getUid());
		if(siacTBilElem == null) {
			return null;
		}
		CapitoloUscitaPrevisione cep = new CapitoloUscitaPrevisione();
		if(modelDetails != null && modelDetails.length > 0){
			return map(siacTBilElem, cep, BilMapId.SiacTBilElem_Capitolo_Minimal, Converters.byModelDetails(modelDetails));
		}
		map(siacTBilElem, cep, BilMapId.SiacTBilElem_Capitolo_Minimal);
		return cep;
	}
	
	

//	public ListaPaginata<CapitoloUscitaPrevisione> ricercaSinteticaCapitoloUscitaPrevisione(RicercaSinteticaCapitoloUPrev criteriRicerca, ParametriPaginazione parametriPaginazione) {
//		
//		SiacTEnteProprietario enteDto = map(ente,SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
//		
//		//PianoDeiConti			 			-> PrimoLivelloPDC,SecondoLivelloPDC,TerzoLivelloPDC...QuintoLivelloPDC
//		//StrutturaAmministrativaContabile 	-> Centro di Responsabilità, CDC (Settore)
//		//SpesaMissioniprogrammi			-> Missione, Programma
//		//SpesaTitolimacroaggregati			-> TitoloSpesa, Macroaggregato		  (solo Uscita)
//		//EntrataTitolitipologiecategorie	-> TitoloEntrata, Tipologia, Categoria (solo Entrata)
//		
//		String codicePianoDeiConti = criteriRicerca.getCodicePianoDeiConti();//"E.9.02.00.00.000";//troverà "E.9.02.03.04.000";
//		List<Integer> classifIdPianoDeiConti = siacTClassDao.findFigliClassificatoreIds(codicePianoDeiConti, SiacDClassTipoEnum.PianoDeiConti , SiacDClassFamEnum.PianoDeiConti);
//		
//		String codiceStruttAmmCont = criteriRicerca.getCodiceStrutturaAmmCont(); //005 CDR troverà //004 CDC
//		String codiceTipoStruttAmmCont = criteriRicerca.getCodiceTipoStrutturaAmmCont();
//		List<Integer> classifIdStruttAmmCont = siacTClassDao.findFigliClassificatoreIds(codiceStruttAmmCont, SiacDClassTipoEnum.byCodice(codiceTipoStruttAmmCont) , SiacDClassFamEnum.StrutturaAmministrativaContabile);
//		
//		String codiceMissioneProgramma = criteriRicerca.getCodiceProgramma();
//		SiacDClassTipoEnum tipoMissioneProgramma = SiacDClassTipoEnum.Programma;
//		if(codiceMissioneProgramma==null){
//			codiceMissioneProgramma = criteriRicerca.getCodiceMissione();
//			tipoMissioneProgramma = SiacDClassTipoEnum.Missione;
//		}	
//		List<Integer> classifIdMissioneProgramma = siacTClassDao.findFigliClassificatoreIds(codiceMissioneProgramma, tipoMissioneProgramma, SiacDClassFamEnum.SpesaMissioniprogrammi);
//		
//		
//		String codiceTitoloSpesaMacroaggregato = criteriRicerca.getCodiceMacroaggregato();
//		SiacDClassTipoEnum tipoTitoloSpesaMacroaggregato = SiacDClassTipoEnum.Macroaggregato;
//		if(codiceTitoloSpesaMacroaggregato==null){
//			codiceTitoloSpesaMacroaggregato = criteriRicerca.getCodiceTitoloUscita();
//			tipoTitoloSpesaMacroaggregato = SiacDClassTipoEnum.TitoloSpesa;
//		}	
//		List<Integer> classifIdTitoloUscitaMacroaggregato = siacTClassDao.findFigliClassificatoreIds(codiceTitoloSpesaMacroaggregato, tipoTitoloSpesaMacroaggregato, SiacDClassFamEnum.SpesaTitolimacroaggregati);
//	
//				
//		String codiceStato = criteriRicerca.getStato()!=null?SiacDBilElemStatoEnum.byStatoOperativoElementoDiBilancio(criteriRicerca.getStato()).getCodice():null;
//		
//		
//		Page<SiacTBilElem> listaCapitoloUscitaPrevisione = capitoloUscitaPrevisioneDao.ricercaSinteticaCapitoloUscitaPrevisione(enteDto, 
//				toString(criteriRicerca.getAnnoEsercizio(),null), 
//				toString(criteriRicerca.getAnnoCapitolo(),null), 
//				toString(criteriRicerca.getNumeroCapitolo(),null),  
//				toString(criteriRicerca.getNumeroArticolo(),null), 
//				toString(criteriRicerca.getNumeroUEB(),null), 
//				criteriRicerca.getFaseBilancio(), criteriRicerca.getDescrizioneCapitolo(), codiceStato, 
//				
////				codiceTitoloSpesaMacroaggregato, 
//				classifIdTitoloUscitaMacroaggregato,
//				
////				codiceMissioneProgramma, 
//				classifIdMissioneProgramma,
//				
////				codicePianoDeiConti, 
//				classifIdPianoDeiConti, 
//				
////				codiceStruttAmmCont, 
//				classifIdStruttAmmCont, 
//				
//				toPageable(parametriPaginazione));
//				
//		/*
//		Page<SiacTBilElem> listaCapitoloUscitaPrevisione = siacTBilElemCapitoloUPRepository.ricercaSinteticaCapitoloUscitaPrevisione(enteDto, 
//				toString(criteriRicerca.getAnnoEsercizio(),null), 
//				toString(criteriRicerca.getAnnoCapitolo(),null), 
//				toString(criteriRicerca.getNumeroCapitolo(),null),  
//				toString(criteriRicerca.getNumeroArticolo(),null), 
//				toString(criteriRicerca.getNumeroUEB(),null), 
//				criteriRicerca.getFaseBilancio(), criteriRicerca.getDescrizioneCapitolo(), codiceStato, 
//				
//				codiceTitoloSpesaMacroaggregato, 
//				classifIdTitoloUscitaMacroaggregato,
//				
//				codiceMissioneProgramma, 
//				classifIdMissioneProgramma,
//				
//				codicePianoDeiConti, 
//				classifIdPianoDeiConti, 
//				
//				codiceStruttAmmCont, 
//				classifIdStruttAmmCont, 
//				
//				toPageable(parametriPaginazione));
//		*/
//
//		//Page<SiacTBilElem> listaCapitoloUscitaPrevisione = capitoloUscitaPrevisioneDao.ricercaSinteticaCapitoloUscitaPrevisione(enteDto, criteriRicerca, toPageable(parametriPaginazione));
//		
//		//ListaPaginata<SiacTBilElem> listaPaginataCapitoloUscitaPrevisione = toListaPaginata(listaCapitoloUscitaPrevisione);
//		
//		/*
//		ListaPaginata<CapitoloUscitaPrevisione> list = new ListaPaginataImpl<CapitoloUscitaPrevisione>();
//		
//		map(listaPaginataCapitoloUscitaPrevisione, list, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione);
//		
//		return list;*/
//		
//		return toListaPaginata(listaCapitoloUscitaPrevisione,CapitoloUscitaPrevisione.class, BilMapId.SiacTBilElem_CapitoloUscitaPrevisione);
//	}

	
	//------------Classificatori-------------

	
	/**
 * Ricerca classificatore elemento piano dei conti capitolo.
 *
 * @param capitoloUscitaPrevisione the capitolo uscita previsione
 * @return the elemento piano dei conti
 */
public ElementoPianoDeiConti ricercaClassificatoreElementoPianoDeiContiCapitolo(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		//capitoloUscitaPrevisione.setUid(40908250); //TODO for test purpose only! delete this line!
		return ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloUscitaPrevisione.getUid());		
	}	
	
	/**
	 * Ricerca classificatore cofog capitolo.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the classificazione cofog
	 */
	public ClassificazioneCofog ricercaClassificatoreCofogCapitolo(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		return ricercaClassificatoreCofogCapitolo(capitoloUscitaPrevisione.getUid());	
	}
	
	/**
	 * Ricerca classificatore struttura amministrativa contabile capitolo.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the struttura amministrativo contabile
	 */
	public StrutturaAmministrativoContabile ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		//capitoloUscitaPrevisione.setUid(3509);//TODO for test purpose only! delete this line!
		return ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloUscitaPrevisione.getUid());
	}	
	
	/**
	 * Ricerca classificatore missione.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @param programma the programma
	 * @return the missione
	 */
	public Missione ricercaClassificatoreMissione(CapitoloUscitaPrevisione capitoloUscitaPrevisione, Programma programma) {
		return ricercaClassificatoreMissione(capitoloUscitaPrevisione.getUid(),programma);
	}	
	
	/**
	 * Ricerca classificatore programma.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the programma
	 */
	public Programma ricercaClassificatoreProgramma(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		return ricercaClassificatoreProgramma(capitoloUscitaPrevisione.getUid());		
	}
	
	/**
	 * Ricerca classificatore titolo spesa.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @param macroaggregato the macroaggregato
	 * @return the titolo spesa
	 */
	public TitoloSpesa ricercaClassificatoreTitoloSpesa(CapitoloUscitaPrevisione capitoloUscitaPrevisione, Macroaggregato macroaggregato) {		
		return ricercaClassificatoreTitoloSpesa(capitoloUscitaPrevisione.getUid(),macroaggregato);			
	}	
	
	/**
	 * Ricerca classificatore macroaggregato.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the macroaggregato
	 */
	public Macroaggregato ricercaClassificatoreMacroaggregato(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		return ricercaClassificatoreMacroaggregato(capitoloUscitaPrevisione.getUid());		
	}
	
	/**
	 * Ricerca classificatore tipo finanziamento.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the tipo finanziamento
	 */
	public TipoFinanziamento ricercaClassificatoreTipoFinanziamento(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		return ricercaClassificatoreTipoFinanziamento(capitoloUscitaPrevisione.getUid());		
	}
	
	/**
	 * Ricerca classificatore tipo fondo.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the tipo fondo
	 */
	public TipoFondo ricercaClassificatoreTipoFondo(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		return ricercaClassificatoreTipoFondo(capitoloUscitaPrevisione.getUid());		
	}
		
//	public RicorrenteSpesa ricercaClassificatoreRicorrenteSpesa(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
//		return ricercaClassificatoreRicorrenteSpesa(capitoloUscitaPrevisione.getUid());		
//	}
//	
//	public RicorrenteEntrata ricercaClassificatoreRicorrenteEntrata(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
//		return ricercaClassificatoreRicorrenteEntrata(capitoloUscitaPrevisione.getUid());		
//	}
		
	
	/**
 * Ricerca classificatori generici.
 *
 * @param capitoloUscitaPrevisione the capitolo uscita previsione
 * @return the list
 */
public List<ClassificatoreGenerico> ricercaClassificatoriGenerici(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		return ricercaClassificatoriGenerici(capitoloUscitaPrevisione.getUid());
	}
	
	//-------------------------
	
	
	
	
	
	
	
	
	




	

	/**
	 * Elimina.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	public void elimina(CapitoloUscitaPrevisione capitoloUscitaPrev) {
		SiacTBilElem bilElem = capitoloDao.findById(capitoloUscitaPrev.getUid());
		capitoloDao.deleteLogical(bilElem);
		
	}

	/**
	 * Annulla.
	 *
	 * @param capitoloUscitaPrev the capitolo uscita prev
	 */
	@Transactional
	public void annulla(CapitoloUscitaPrevisione capitoloUscitaPrev) {
		SiacTBilElem bilElem = capitoloDao.findById(capitoloUscitaPrev.getUid());
		
		for(SiacRBilElemStato stato : bilElem.getSiacRBilElemStatos()){ //NOTA: dovrebbe esserci un solo stato!!! 
			//String statoCode = stato.getSiacDBilElemStato().getElemStatoCode();
			//SiacDBilElemStatoEnum.byCodice(statoCode);
			//stato.setSiacDBilElemStato(SiacDBilElemStatoEnum.Annullato.getEntity());
			SiacDBilElemStato siacDBilElemStato = eef.getEntity(SiacDBilElemStatoEnum.Annullato, ente.getUid(), SiacDBilElemStato.class);
			stato.setSiacDBilElemStato(siacDBilElemStato);
		}
		
		//capitoloDao.update(bilElem);
		
	}
	
	

	/**
	 *  
	 * Popola i flag di un CapitoloUscitaPrevisione.
	 * Gli attributi di tipo boolean sono legati ad un elemento di bilancio dalla siac_r_bil_elem_attr
	 *
	 * @param capitolo the capitolo
	 */
	
	public void populateFlags(CapitoloUscitaPrevisione capitolo) {
		super.populateAttrs(capitolo);		
	}


	/**
	 * Gets the ex capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the ex capitolo
	 */
	public CapitoloUscitaGestione getExCapitolo(CapitoloUscitaPrevisione capitolo) {
		final String methodName = "getExCapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		
		if(bilElems.isEmpty()) { // SE non è definito l'ex capitolo su SiacRBilElemRelTempo allora l'ex capitolo è l'equivalente
			bilElems = siacTBilElemRepository.findCapitoloExByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		}
		
		if(bilElems.isEmpty()) {
			return null;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un ex capitolo per il capitolo con uid: "+ capitolo.getUid());
		}
		
		return mapNotNull(bilElems.get(0), CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
	}
	
	public int getExCapitoloUid(CapitoloUscitaPrevisione capitolo) {
		final String methodName = "getExCapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findRelTempoByElemTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		
		if(bilElems.isEmpty()) { // SE non è definito l'ex capitolo su SiacRBilElemRelTempo allora l'ex capitolo è l'equivalente
			bilElems = siacTBilElemRepository.findCapitoloExByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		}	
		
		if(bilElems.isEmpty()) {
			return 0;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un ex capitolo per il capitolo con uid: "+ capitolo.getUid());
		}
		
		return bilElems.get(0).getUid();
	}
	
	

	/**
	 * Ottiene il capitolo equivalente.
	 *
	 * @param capitolo the capitolo
	 * @return the equivalente 
	 */
	public CapitoloUscitaGestione getEquivalente(CapitoloUscitaPrevisione capitolo) {
		final String methodName = "getEquivalente";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		
		if(bilElems.isEmpty()){
			return null;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un equivalente capitolo per il capitolo con uid: "+ capitolo.getUid());
		}
				
		return mapNotNull(bilElems.get(0), CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione);
	}
	
	public int getEquivalenteUid(CapitoloUscitaPrevisione capitolo) {
		final String methodName = "getEquivalente";
		
		log.debug(methodName, "capitolo.uid: "+ capitolo.getUid());
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(capitolo.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		
		if(bilElems.isEmpty()){
			return 0;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un equivalente capitolo per il capitolo con uid: "+ capitolo.getUid());
		}
				
		return bilElems.get(0).getUid();
	}

}
