/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.elementobilancio;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.utility.CompareOperator;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


// TODO: Auto-generated Javadoc
/**
 * The Interface CapitoloDao.
 */
public interface CapitoloDao extends Dao<SiacTBilElem, Integer>  {
	
	/**
	 * Creates the.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the siac t bil elem
	 */
	SiacTBilElem create(SiacTBilElem capitoloUscitaPrevisione);

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.Dao#findById(java.lang.Object)
	 */
	SiacTBilElem findById(Integer id);

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.Dao#update(java.lang.Object)
	 */
	SiacTBilElem update(SiacTBilElem capitoloUscitaPrevisione);
	
	/**
	 * Delete logical.
	 *
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the siac t bil elem
	 */
	SiacTBilElem deleteLogical(SiacTBilElem capitoloUscitaPrevisione);
	
	
	/**
	 * Ricerca sintetica capitolo.
	 *
	 * @param enteDto the ente dto
	 * @param annoEsercizio the anno esercizio
	 * @param tipoCapitolo the tipo capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param stato the stato
	 * @param exAnnoCapitolo the ex anno capitolo
	 * @param exNumeroCapitolo the ex numero capitolo
	 * @param exNumeroArticolo the ex numero articolo
	 * @param exNumeroUEB the ex numero ueb
	 * @param faseBilancio the fase bilancio
	 * @param descrizioneCapitolo the descrizione capitolo
	 * @param descrizioneArticolo the descrizione articolo
	 * @param flagAssegnabile the flag assegnabile
	 * @param flagFondoSvalutazioneCrediti the flag fondo svalutazione crediti
	 * @param flagFunzioniDelegate the flag funzioni delegate
	 * @param flagPerMemoria the flag per memoria
	 * @param flagRilevanteIva the flag rilevante iva
	 * @param flagTrasferimentoOrganiComunitari the flag trasferimento organi comunitari
	 * @param flagEntrateRicorrenti the flag entrate ricorrenti
	 * @param flagFondoPluriennaleVinc the flag fondo pluriennale vinc
	 * @param codiceTipoFinanziamento the codice tipo finanziamento
	 * @param codiceTipoFondo the codice tipo fondo
	 * @param codiceTipoVincolo the codice tipo vincolo
	 * @param codiceRicorrenteEntrata the codice ricorrente entrata
	 * @param codiceRicorrenteSpesa the codice ricorrente spesa
	 * @param codicePerimetroSanitarioEntrata the codice perimetro sanitario entrata
	 * @param codicePerimetroSanitarioSpesa the codice perimetro sanitario spesa
	 * @param codiceTransazioneUnioneEuropeaEntrata the codice transazione unione europea entrata
	 * @param codiceTransazioneUnioneEuropeaSpesa the codice transazione unione europea spesa
	 * @param codicePoliticheRegionaliUnitarie the codice politiche regionali unitarie
	 * @param codiceClassificatoreGenerico1 the codice classificatore generico1
	 * @param codiceClassificatoreGenerico2 the codice classificatore generico2
	 * @param codiceClassificatoreGenerico3 the codice classificatore generico3
	 * @param codiceClassificatoreGenerico4 the codice classificatore generico4
	 * @param codiceClassificatoreGenerico5 the codice classificatore generico5
	 * @param codiceClassificatoreGenerico6 the codice classificatore generico6
	 * @param codiceClassificatoreGenerico7 the codice classificatore generico7
	 * @param codiceClassificatoreGenerico8 the codice classificatore generico8
	 * @param codiceClassificatoreGenerico9 the codice classificatore generico9
	 * @param codiceClassificatoreGenerico10 the codice classificatore generico10
	 * @param codicePianoDeiConti the codice piano dei conti
	 * @param codiceCofog the codice cofog
	 * @param codiceTipoCofog the codice tipo cofog
	 * @param codiceStruttAmmCont the codice strutt amm cont
	 * @param codiceTipoStruttAmmCont the codice tipo strutt amm cont
	 * @param codiceSiopeEntrata the codice siope entrata
	 * @param codiceTipoSiopeEntrata the codice tipo siope entrata
	 * @param codiceSiopeSpesa the codice siope spesa
	 * @param codiceTipoSiopeSpesa the codice tipo siope spesa
	 * @param codiceMissione the codice missione
	 * @param codiceProgrmma the codice progrmma
	 * @param codiceTitoloSpesa the codice titolo spesa
	 * @param codiceMacroaggregato the codice macroaggregato
	 * @param codiceTitoloEntrata the codice titolo entrata
	 * @param codiceTipologia the codice tipologia
	 * @param codiceCategoria the codice categoria
	 * @param attoleggeNumero the attolegge numero
	 * @param attoleggeAnno the attolegge anno
	 * @param attoleggeArticolo the attolegge articolo
	 * @param attoleggeComma the attolegge comma
	 * @param attoleggePunto the attolegge punto
	 * @param attoleggeTipoCode the attolegge tipo code
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTBilElem> ricercaSinteticaCapitolo(SiacTEnteProprietario enteDto, String annoEsercizio,SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo, String annoCapitolo,
			String numeroCapitolo, String numeroArticolo, String numeroUEB,String stato, 
			
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			
			String faseBilancio, 
			String descrizioneCapitolo, 
			String descrizioneArticolo,
			
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,	
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			
			//classificatori generici
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie,
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			
			//classificatori gerarchici
			String codicePianoDeiConti,
			String codiceCofog, String codiceTipoCofog,
			String codiceStruttAmmCont, String codiceTipoStruttAmmCont,
			String codiceSiopeEntrata, String codiceTipoSiopeEntrata,
			String codiceSiopeSpesa, String codiceTipoSiopeSpesa,
			String codiceMissione, String codiceProgrmma,
			String codiceTitoloSpesa, String codiceMacroaggregato,
			String codiceTitoloEntrata, String codiceTipologia, String codiceCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			
			// SIAC-4088
			Boolean collegatoFondiDubbiaEsigibilita,
			
			Pageable pageable);

	/**
	 * Ricerca sintetica capitolo.
	 *
	 * @param enteDto the ente dto
	 * @param annoEsercizio the anno esercizio
	 * @param tipoCapitolo the tipo capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param stato the stato
	 * @param exAnnoCapitolo the ex anno capitolo
	 * @param exNumeroCapitolo the ex numero capitolo
	 * @param exNumeroArticolo the ex numero articolo
	 * @param exNumeroUEB the ex numero ueb
	 * @param faseBilancio the fase bilancio
	 * @param descrizioneCapitolo the descrizione capitolo
	 * @param descrizioneArticolo the descrizione articolo
	 * @param flagAssegnabile the flag assegnabile
	 * @param flagFondoSvalutazioneCrediti the flag fondo svalutazione crediti
	 * @param flagFunzioniDelegate the flag funzioni delegate
	 * @param flagPerMemoria the flag per memoria
	 * @param flagRilevanteIva the flag rilevante iva
	 * @param flagTrasferimentoOrganiComunitari the flag trasferimento organi comunitari
	 * @param flagEntrateRicorrenti the flag entrate ricorrenti
	 * @param flagFondoPluriennaleVinc the flag fondo pluriennale vinc
	 * @param codiceTipoFinanziamento the codice tipo finanziamento
	 * @param codiceTipoFondo the codice tipo fondo
	 * @param codiceTipoVincolo the codice tipo vincolo
	 * @param codiceRicorrenteEntrata the codice ricorrente entrata
	 * @param codiceRicorrenteSpesa the codice ricorrente spesa
	 * @param codicePerimetroSanitarioEntrata the codice perimetro sanitario entrata
	 * @param codicePerimetroSanitarioSpesa the codice perimetro sanitario spesa
	 * @param codiceTransazioneUnioneEuropeaEntrata the codice transazione unione europea entrata
	 * @param codiceTransazioneUnioneEuropeaSpesa the codice transazione unione europea spesa
	 * @param codicePoliticheRegionaliUnitarie the codice politiche regionali unitarie
	 * @param codiceClassificatoreGenerico1 the codice classificatore generico1
	 * @param codiceClassificatoreGenerico2 the codice classificatore generico2
	 * @param codiceClassificatoreGenerico3 the codice classificatore generico3
	 * @param codiceClassificatoreGenerico4 the codice classificatore generico4
	 * @param codiceClassificatoreGenerico5 the codice classificatore generico5
	 * @param codiceClassificatoreGenerico6 the codice classificatore generico6
	 * @param codiceClassificatoreGenerico7 the codice classificatore generico7
	 * @param codiceClassificatoreGenerico8 the codice classificatore generico8
	 * @param codiceClassificatoreGenerico9 the codice classificatore generico9
	 * @param codiceClassificatoreGenerico10 the codice classificatore generico10
	 * @param classifIdPianoDeiConti the classif id piano dei conti
	 * @param classifIdCofog the classif id cofog
	 * @param classifIdStruttAmmCont the classif id strutt amm cont
	 * @param classifIdSiopeEntrata the classif id siope entrata
	 * @param classifIdSiopeSpesa the classif id siope spesa
	 * @param classifIdMissioneProgramma the classif id missione programma
	 * @param classifIdTitoloUscitaMacroaggregato the classif id titolo uscita macroaggregato
	 * @param classifIdTitoloEntrataTipologiaCategoria the classif id titolo entrata tipologia categoria
	 * @param attoleggeNumero the attolegge numero
	 * @param attoleggeAnno the attolegge anno
	 * @param attoleggeArticolo the attolegge articolo
	 * @param attoleggeComma the attolegge comma
	 * @param attoleggePunto the attolegge punto
	 * @param attoleggeTipoCode the attolegge tipo code
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTBilElem> ricercaSinteticaCapitolo( SiacTEnteProprietario enteDto, String annoEsercizio,SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo,
			String annoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato, 
			
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			
			String faseBilancio,
			String descrizioneCapitolo,
			String descrizioneArticolo,
			
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			
			//classificatori generici
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,

			//classificatori gerarchici
			List<Integer> classifIdPianoDeiConti, 
			List<Integer> classifIdCofog,
			List<Integer> classifIdStruttAmmCont,  
			List<Integer> classifIdSiopeEntrata,  
			List<Integer> classifIdSiopeSpesa,  
			List<Integer> classifIdMissioneProgramma, 
			List<Integer> classifIdTitoloUscitaMacroaggregato,
			List<Integer> classifIdTitoloEntrataTipologiaCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			
			// SIAC-4088
			Boolean collegatoFondiDubbiaEsigibilita,
			
			Pageable pageable);
	
	
	
	/**
	 * Count ricerca sintetica capitolo.
	 *
	 * @param enteDto the ente dto
	 * @param annoEsercizio the anno esercizio
	 * @param tipoCapitolo the tipo capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param stato the stato
	 * @param exAnnoCapitolo the ex anno capitolo
	 * @param exNumeroCapitolo the ex numero capitolo
	 * @param exNumeroArticolo the ex numero articolo
	 * @param exNumeroUEB the ex numero ueb
	 * @param faseBilancio the fase bilancio
	 * @param descrizioneCapitolo the descrizione capitolo
	 * @param descrizioneArticolo the descrizione articolo
	 * @param flagAssegnabile the flag assegnabile
	 * @param flagFondoSvalutazioneCrediti the flag fondo svalutazione crediti
	 * @param flagFunzioniDelegate the flag funzioni delegate
	 * @param flagPerMemoria the flag per memoria
	 * @param flagRilevanteIva the flag rilevante iva
	 * @param flagTrasferimentoOrganiComunitari the flag trasferimento organi comunitari
	 * @param flagEntrateRicorrenti the flag entrate ricorrenti
	 * @param flagFondoPluriennaleVinc the flag fondo pluriennale vinc
	 * @param codiceTipoFinanziamento the codice tipo finanziamento
	 * @param codiceTipoFondo the codice tipo fondo
	 * @param codiceTipoVincolo the codice tipo vincolo
	 * @param codiceRicorrenteEntrata the codice ricorrente entrata
	 * @param codiceRicorrenteSpesa the codice ricorrente spesa
	 * @param codicePerimetroSanitarioEntrata the codice perimetro sanitario entrata
	 * @param codicePerimetroSanitarioSpesa the codice perimetro sanitario spesa
	 * @param codiceTransazioneUnioneEuropeaEntrata the codice transazione unione europea entrata
	 * @param codiceTransazioneUnioneEuropeaSpesa the codice transazione unione europea spesa
	 * @param codicePoliticheRegionaliUnitarie the codice politiche regionali unitarie
	 * @param codiceClassificatoreGenerico1 the codice classificatore generico1
	 * @param codiceClassificatoreGenerico2 the codice classificatore generico2
	 * @param codiceClassificatoreGenerico3 the codice classificatore generico3
	 * @param codiceClassificatoreGenerico4 the codice classificatore generico4
	 * @param codiceClassificatoreGenerico5 the codice classificatore generico5
	 * @param codiceClassificatoreGenerico6 the codice classificatore generico6
	 * @param codiceClassificatoreGenerico7 the codice classificatore generico7
	 * @param codiceClassificatoreGenerico8 the codice classificatore generico8
	 * @param codiceClassificatoreGenerico9 the codice classificatore generico9
	 * @param codiceClassificatoreGenerico10 the codice classificatore generico10
	 * @param codicePianoDeiConti the codice piano dei conti
	 * @param codiceCofog the codice cofog
	 * @param codiceTipoCofog the codice tipo cofog
	 * @param codiceStruttAmmCont the codice strutt amm cont
	 * @param codiceTipoStruttAmmCont the codice tipo strutt amm cont
	 * @param codiceSiopeEntrata the codice siope entrata
	 * @param codiceTipoSiopeEntrata the codice tipo siope entrata
	 * @param codiceSiopeSpesa the codice siope spesa
	 * @param codiceTipoSiopeSpesa the codice tipo siope spesa
	 * @param codiceMissione the codice missione
	 * @param codiceProgrmma the codice progrmma
	 * @param codiceTitoloSpesa the codice titolo spesa
	 * @param codiceMacroaggregato the codice macroaggregato
	 * @param codiceTitoloEntrata the codice titolo entrata
	 * @param codiceTipologia the codice tipologia
	 * @param codiceCategoria the codice categoria
	 * @param attoleggeNumero the attolegge numero
	 * @param attoleggeAnno the attolegge anno
	 * @param attoleggeArticolo the attolegge articolo
	 * @param attoleggeComma the attolegge comma
	 * @param attoleggePunto the attolegge punto
	 * @param attoleggeTipoCode the attolegge tipo code
	 * @return the long
	 */
	Long countRicercaSinteticaCapitolo(SiacTEnteProprietario enteDto, String annoEsercizio,SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo, String annoCapitolo,
			String numeroCapitolo, String numeroArticolo, String numeroUEB,String stato, 
			
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			
			String faseBilancio, 
			String descrizioneCapitolo, 
			String descrizioneArticolo,
			
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,	
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			
			//classificatori generici
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			
			//classificatori gerarchici
			String codicePianoDeiConti,
			String codiceCofog, String codiceTipoCofog,
			String codiceStruttAmmCont, String codiceTipoStruttAmmCont,
			String codiceSiopeEntrata, String codiceTipoSiopeEntrata,
			String codiceSiopeSpesa, String codiceTipoSiopeSpesa,
			String codiceMissione, String codiceProgrmma,
			String codiceTitoloSpesa, String codiceMacroaggregato,
			String codiceTitoloEntrata, String codiceTipologia, String codiceCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			
			// SIAC-4088
			Boolean collegatoFondiDubbiaEsigibilita
			);

	/**
	 * Count ricerca sintetica capitolo.
	 *
	 * @param enteDto the ente dto
	 * @param annoEsercizio the anno esercizio
	 * @param tipoCapitolo the tipo capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param stato the stato
	 * @param exAnnoCapitolo the ex anno capitolo
	 * @param exNumeroCapitolo the ex numero capitolo
	 * @param exNumeroArticolo the ex numero articolo
	 * @param exNumeroUEB the ex numero ueb
	 * @param faseBilancio the fase bilancio
	 * @param descrizioneCapitolo the descrizione capitolo
	 * @param descrizioneArticolo the descrizione articolo
	 * @param flagAssegnabile the flag assegnabile
	 * @param flagFondoSvalutazioneCrediti the flag fondo svalutazione crediti
	 * @param flagFunzioniDelegate the flag funzioni delegate
	 * @param flagPerMemoria the flag per memoria
	 * @param flagRilevanteIva the flag rilevante iva
	 * @param flagTrasferimentoOrganiComunitari the flag trasferimento organi comunitari
	 * @param flagEntrateRicorrenti the flag entrate ricorrenti
	 * @param flagFondoPluriennaleVinc the flag fondo pluriennale vinc
	 * @param codiceTipoFinanziamento the codice tipo finanziamento
	 * @param codiceTipoFondo the codice tipo fondo
	 * @param codiceTipoVincolo the codice tipo vincolo
	 * @param codiceRicorrenteEntrata the codice ricorrente entrata
	 * @param codiceRicorrenteSpesa the codice ricorrente spesa
	 * @param codicePerimetroSanitarioEntrata the codice perimetro sanitario entrata
	 * @param codicePerimetroSanitarioSpesa the codice perimetro sanitario spesa
	 * @param codiceTransazioneUnioneEuropeaEntrata the codice transazione unione europea entrata
	 * @param codiceTransazioneUnioneEuropeaSpesa the codice transazione unione europea spesa
	 * @param codicePoliticheRegionaliUnitarie the codice politiche regionali unitarie
	 * @param codiceClassificatoreGenerico1 the codice classificatore generico1
	 * @param codiceClassificatoreGenerico2 the codice classificatore generico2
	 * @param codiceClassificatoreGenerico3 the codice classificatore generico3
	 * @param codiceClassificatoreGenerico4 the codice classificatore generico4
	 * @param codiceClassificatoreGenerico5 the codice classificatore generico5
	 * @param codiceClassificatoreGenerico6 the codice classificatore generico6
	 * @param codiceClassificatoreGenerico7 the codice classificatore generico7
	 * @param codiceClassificatoreGenerico8 the codice classificatore generico8
	 * @param codiceClassificatoreGenerico9 the codice classificatore generico9
	 * @param codiceClassificatoreGenerico10 the codice classificatore generico10
	 * @param classifIdPianoDeiConti the classif id piano dei conti
	 * @param classifIdCofog the classif id cofog
	 * @param classifIdStruttAmmCont the classif id strutt amm cont
	 * @param classifIdSiopeEntrata the classif id siope entrata
	 * @param classifIdSiopeSpesa the classif id siope spesa
	 * @param classifIdMissioneProgramma the classif id missione programma
	 * @param classifIdTitoloUscitaMacroaggregato the classif id titolo uscita macroaggregato
	 * @param classifIdTitoloEntrataTipologiaCategoria the classif id titolo entrata tipologia categoria
	 * @param attoleggeNumero the attolegge numero
	 * @param attoleggeAnno the attolegge anno
	 * @param attoleggeArticolo the attolegge articolo
	 * @param attoleggeComma the attolegge comma
	 * @param attoleggePunto the attolegge punto
	 * @param attoleggeTipoCode the attolegge tipo code
	 * @return the long
	 */
	Long countRicercaSinteticaCapitolo( SiacTEnteProprietario enteDto, String annoEsercizio,SiacDBilElemTipoEnum tipoCapitolo, Integer uidCategoriaCapitolo, String codiceCategoriaCapitolo,
			String annoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, String stato, 
			
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			
			String faseBilancio,
			String descrizioneCapitolo,
			String descrizioneArticolo,
			
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			
			//classificatori generici
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,			
			String codiceRicorrenteEntrata, String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata, String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata, String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie, 
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,

			//classificatori gerarchici
			List<Integer> classifIdPianoDeiConti, 
			List<Integer> classifIdCofog,
			List<Integer> classifIdStruttAmmCont,  
			List<Integer> classifIdSiopeEntrata,  
			List<Integer> classifIdSiopeSpesa,  
			List<Integer> classifIdMissioneProgramma, 
			List<Integer> classifIdTitoloUscitaMacroaggregato,
			List<Integer> classifIdTitoloEntrataTipologiaCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			// SIAC-4088
			Boolean collegatoFondiDubbiaEsigibilita
			);


	

	/**
	 * Aggiorna classificatori.
	 *
	 * @param bilElem the bil elem
	 */
	void aggiornaClassificatori(SiacTBilElem bilElem);

	
	/**
	 * Invoca la Function indicata dal parametro functionName su database per calcolare un importo derivato del capitolo.
	 *
	 * @param bilElemId the bil elem id
	 * @param functionName the function name
	 * @return the big decimal
	 */
	BigDecimal findImportoDerivato(Integer bilElemId, String functionName);
	
	/**
	 * Invoca la Function indicata dal parametro functionName su database per calcolare un importo derivato del capitolo.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @param functionName the function name
	 * @return the big decimal
	 */
	BigDecimal findImportoDerivato(SiacTBilElem siacTBilElem, String functionName);

	List<Object[]> importiRicercaSinteticaCapitolo(
			SiacTEnteProprietario enteDto,
			String annoEsercizio,
			SiacDBilElemTipoEnum tipoCapitolo,
			Integer uidCategoriaCapitolo,
			String codiceCategoriaCapitolo,
			String annoCapitolo,
			String numeroCapitolo,
			String numeroArticolo,
			String numeroUEB,
			String stato,
			
			String exAnnoCapitolo,
			String exNumeroCapitolo,
			String exNumeroArticolo,
			String exNumeroUEB,
			
			String faseBilancio,
			String descrizioneCapitolo,
			String descrizioneArticolo,
			
			String flagAssegnabile,
			String flagFondoSvalutazioneCrediti,
			String flagFunzioniDelegate,
			String flagPerMemoria,
			String flagRilevanteIva,
			String flagTrasferimentoOrganiComunitari,	
			String flagEntrateRicorrenti,
			String flagFondoPluriennaleVinc,
			
			//classificatori generici
			String codiceTipoFinanziamento,
			String codiceTipoFondo,
			String codiceTipoVincolo,
			String codiceRicorrenteEntrata,
			String codiceRicorrenteSpesa,
			String codicePerimetroSanitarioEntrata,
			String codicePerimetroSanitarioSpesa,
			String codiceTransazioneUnioneEuropeaEntrata,
			String codiceTransazioneUnioneEuropeaSpesa,
			String codicePoliticheRegionaliUnitarie,
			String codiceClassificatoreGenerico1,
			String codiceClassificatoreGenerico2,
			String codiceClassificatoreGenerico3,
			String codiceClassificatoreGenerico4,
			String codiceClassificatoreGenerico5,
			String codiceClassificatoreGenerico6,
			String codiceClassificatoreGenerico7,
			String codiceClassificatoreGenerico8,
			String codiceClassificatoreGenerico9,
			String codiceClassificatoreGenerico10,
			
			String codiceClassificatoreGenerico31,
			String codiceClassificatoreGenerico32,
			String codiceClassificatoreGenerico33,
			String codiceClassificatoreGenerico34,
			String codiceClassificatoreGenerico35,
			String codiceClassificatoreGenerico36,
			String codiceClassificatoreGenerico37,
			String codiceClassificatoreGenerico38,
			String codiceClassificatoreGenerico39,
			String codiceClassificatoreGenerico40,
			String codiceClassificatoreGenerico41,
			String codiceClassificatoreGenerico42,
			String codiceClassificatoreGenerico43,
			String codiceClassificatoreGenerico44,
			String codiceClassificatoreGenerico45,
			String codiceClassificatoreGenerico46,
			String codiceClassificatoreGenerico47,
			String codiceClassificatoreGenerico48,
			String codiceClassificatoreGenerico49,
			String codiceClassificatoreGenerico50,
			
			//classificatori gerarchici
			String codicePianoDeiConti,
			String codiceCofog,
			String codiceTipoCofog,
			String codiceStruttAmmCont,
			String codiceTipoStruttAmmCont,
			String codiceSiopeEntrata,
			String codiceTipoSiopeEntrata,
			String codiceSiopeSpesa,
			String codiceTipoSiopeSpesa,
			String codiceMissione,
			String codiceProgrmma,
			String codiceTitoloSpesa,
			String codiceMacroaggregato,
			String codiceTitoloEntrata,
			String codiceTipologia,
			String codiceCategoria,
			
			Integer attoleggeNumero,
			String attoleggeAnno,
			String attoleggeArticolo,
			String attoleggeComma,
			String attoleggePunto,
			String attoleggeTipoCode,
			Boolean collegatoFondiDubbiaEsigibilita);
	
	Long countMovgestNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	Long countLiquidazioniNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	
	Long countOrdinativiIncassoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	Long countOrdinativiPagamentoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	
	BigDecimal sumMovgestImportoNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	BigDecimal sumMovgestImportoDaRiaccNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	BigDecimal sumMovgestImportoDaEserciziPrecNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator, String annoEsercizio);
	

	
	// SIAC-6899 
	BigDecimal sumMovgestImportoFinanziatodaAvanzodaFPVNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator,List<String>  avavincoloTipoCode);
	

	
	BigDecimal sumMovgestImportoDaPrenotazioneNonAnnullatiByBilElemIdsAndMovgestAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	
	BigDecimal sumLiquidazioniImportoNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	BigDecimal sumLiquidazioniImportoDaPrenotazioneNonAnnullateByBilElemIdsAndLiqAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	
	BigDecimal sumOrdinativoIncassoImportoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	BigDecimal sumOrdinativoPagamentoImportoNonAnnullatiByBilElemIdsAndOrdAnnoAndOperator(List<Integer> elemIds, Integer movgestAnno, CompareOperator jpqlCompareOperator);
	List<Object[]> findCapitoliBySubdocIds(List<Integer> subdocIds);
	List<Object[]> findCapitoliByElenco(List<Integer> elencoIds);
	
	List<Integer> ricercaIdCapitoli(Integer enteProprietarioId, String annoBilancio, String elemTipoCode, String numeroCapitolo, String numeroArticolo, String numeroUEB);
}
