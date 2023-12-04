/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface VariazioneImportiCapitoloDao.
 */
public interface VariazioneImportiCapitoloDao extends Dao<SiacTVariazione, Integer> {
	
	
	/**
	 * Ricerca una variazione di bilancio per id
	 * 
	 * @param id l'uid da cercare
	 * 
	 * @return la SiacTVariazione trovata
	 * 
	 */
	SiacTVariazione findById(Integer id);

	/**
	 * Crea una SiacTVariazione.
	 *
	 * @param var la SiacTVariazione da inserire
	 * 
	 * @return la SiacTVariazione inserita
	 */
	SiacTVariazione create(SiacTVariazione var);
	
	/**
	 * Aggiorna una SiacTVariazione.
	 *
	 * @param var la SiacTVariazione da aggiornare
	 * 
	 * @return la SiacTVariazione aggiornata
	 */
	SiacTVariazione update(SiacTVariazione var);

	/**
	 * Ricerca sintetica di una variazione di bilancio.
	 *
	 * @param variazioneTipo the variazione tipo
	 * @param enteProprietarioId the ente proprietario id
	 * @param annoBilancio the anno bilancio
	 * @param variazioneNum the variazione num
	 * @param variazioneDesc the variazione desc
	 * @param variazioneStato the variazione stato
	 * @param attoAmmId the atto amm id
	 * @param capitoloId the capitolo id
	 * @param capitoloCodificaId the capitolo codifica id
	 * @param capitoloSorgenteId the capitolo sorgente id
	 * @param capitoloDestinazioneId the capitolo destinazione id
	 * @param bilElemsTipo the bil elems tipo
	 * @param bilElemsCodificaTipo the bil elems codifica tipo
	 * @param operatoreImporti the operatore importi
	 * @param limitaRisultatiDefinitiveODecentrate 
	 * @param pageable the pageable
	 * 
	 * @return la lista paginata delle variazioni trovate
	 */
	Page<SiacTVariazione> ricercaSinteticaVariazioneDiBilancio(Collection<SiacDVariazioneTipoEnum> variazioneTipo, Integer enteProprietarioId, 
			String annoBilancio, 
			Integer variazioneNum, 
			String variazioneDesc,
			Date dataAperturaProposta,
			Date dataChiusuraProposta,
			Integer direzioneProponenteId,
			SiacDVariazioneStatoEnum variazioneStato,			
			Integer attoAmmId,
			Integer capitoloId, //capitolo legato alla variazione di bilancio (importo)
			Integer capitoloCodificaId, //capitolo legato alla variazine di codifica
			Integer capitoloSorgenteId,  //capitolo sorgente storno
			Integer capitoloDestinazioneId, //capitolo destinazione storno
			List<SiacDBilElemTipoEnum> bilElemsTipo, //tipi di capitolo sul quale opera la variazione di bilancio (importo)
			List<SiacDBilElemTipoEnum> bilElemsCodificaTipo, //tipi di capitolo sul quale opera la variazione di codifica
			String operatoreImporti, // L'operatore da utilizzare per gli importi: <, <=, =, >=, > di zero
			Integer attoAmmVarBilId,
			Integer attoAmmIdMultiple, // Il provvedimento sia PEG che variazione
			boolean limitaRisultatiDefinitiveODecentrate, Pageable pageable);

	
	
	/** CONTABILIA-285
	 * Ricerca sintetica di una variazione di bilancio.
	 *
	 * @param variazioneTipo the variazione tipo
	 * @param enteProprietarioId the ente proprietario id
	 * @param annoBilancio the anno bilancio
	 * @param variazioneNum the variazione num
	 * @param variazioneDesc the variazione desc
	 * @param variazioneStato the variazione stato
	 * @param attoAmmId the atto amm id
	 * @param capitoloId the capitolo id
	 * @param capitoloCodificaId the capitolo codifica id
	 * @param capitoloSorgenteId the capitolo sorgente id
	 * @param capitoloDestinazioneId the capitolo destinazione id
	 * @param bilElemsTipo the bil elems tipo
	 * @param bilElemsCodificaTipo the bil elems codifica tipo
	 * @param operatoreImporti the operatore importi
	 * @param pageable the pageable
	 * 
	 * @return la lista paginata delle variazioni trovate
	 */
	Page<SiacTVariazione> ricercaSinteticaVariazioneNeutreDiBilancio(Collection<SiacDVariazioneTipoEnum> variazioneTipo, Integer enteProprietarioId, 
			String annoBilancio, 
			Integer variazioneNum, 
			String variazioneDesc,
			Date dataAperturaProposta,
			Date dataChiusuraProposta,
			Integer direzioneProponenteId,
			SiacDVariazioneStatoEnum variazioneStato,			
			Integer attoAmmId,
			Integer capitoloId, //capitolo legato alla variazione di bilancio (importo)
			Integer capitoloCodificaId, //capitolo legato alla variazine di codifica
			Integer capitoloSorgenteId,  //capitolo sorgente storno
			Integer capitoloDestinazioneId, //capitolo destinazione storno
			List<SiacDBilElemTipoEnum> bilElemsTipo, //tipi di capitolo sul quale opera la variazione di bilancio (importo)
			List<SiacDBilElemTipoEnum> bilElemsCodificaTipo, //tipi di capitolo sul quale opera la variazione di codifica
			String operatoreImporti, // L'operatore da utilizzare per gli importi: <, <=, =, >=, > di zero
			Integer attoAmmVarBilId,
			Integer attoAmmIdMultiple, // Il provvedimento sia PEG che variazione
			Pageable pageable);
	
	
	
	
	/**
	 * Aggiorna solo l'anagrafica di una SiacTVariazione.
	 *
	 * @param var la SiacTVariazione da aggiornare
	 * 
	 * @return la SiacTVariazione aggiornata
	 */
	SiacTVariazione updateAnagrafica(SiacTVariazione var);
	
	/**
	 * Ottiene una lista paginata di SiacTBilElem a partire dagli uid
	 */
	Page<SiacTBilElem> findCapitoliNellaVariazioneByUid(
			Integer variazioneId,
			Pageable pageable);

	Long countDistinctClassifPadre(Integer variazioneId, String classifTipoCodePadre, String classifTipoCodeFiglio);
	Long countDistinctClassifNonno(Integer variazioneId, String classifTipoCodeNonno, String classifTipoCodePadre, String classifTipoCodeFiglio);

	//SIAC-5016
	List<Object[]> findAllDettagliVariazioneImportoCapitoloByUidVariazione(int uidVariazione);
	
	
	//SIAC-6884 find primo capitolo inserito nella variazione


	Page<SiacTBilElem> findPrimoCapitoloNellaVariazioneByUid(int uidVariazione, Pageable pageable, Integer capitoloId);

	Integer getFirstCapitoloIdByUidVariazioneTipoCap(int uidVariazione, String tipoCapitolo, String annoBilancio);

	//Page<SiacTBilElem> findPrimoCapitoloNellaVariazioneByUid(int uidVariazione, Pageable pageable, String tipoCapitolo);

	//Page<SiacTBilElem> findPrimoCapitoloNellaVariazioneByUid(int uidVariazione, String tipoCapitolo, Pageable pageable);
	
    Long countCapitoliComuni(Integer uidVariazione, List<Integer> uidVariaziones);

	List<Object[]> findStanziamentoCapitoloInVariazioneInDiminuzione(Integer idVariazione, Collection<Integer> idCapitoliDaEscludere);

	List<Object[]> findStanziamentoComponenteCapitoloInVariazioneInDiminuzione(Integer idVariazione, List<Integer> idCapitolispesa);
	
}
