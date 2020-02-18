/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;

// TODO: Auto-generated Javadoc
/**
 * Dao dei classificatori.
 *
 * @author Domenico
 */
public interface SiacTClassDao {
	

	/**
	 * Ricerca per id.
	 *
	 * @param id the id
	 * @return the siac t class
	 */
	SiacTClass findById(Integer id);
	
	
	/**
	 * Restituisce gli id dei figli di un classificatore.
	 *
	 * @param tipoClassificatore the tipo classificatore
	 * @param siacTEnteProprietarioId the siac t ente proprietario id
	 * @param annoEsercizio the anno esercizio
	 * @param classifCodes the classif codes
	 * @return the list
	 * @see findFigliClassificatore(SiacDClassTipoEnum tipoClassificatore, String... classifCodes)
	 */
	List<Integer> findFigliClassificatoreIds(String annoEsercizio, SiacDClassTipoEnum tipoClassificatore, Integer siacTEnteProprietarioId, String... classifCodes);
	

	
	/**
	 * Restituisce i figli di un classificatore.
	 * Ad esempio per piano dei conti con classifCode = "E.9.02.00.00.000" viene restituito tra i figli anche il classificatore "E.9.02.03.00.000"
	 * 
	 * (Nota per piano dei conti con classifCode = "E.9.02.03.03.000"  -> i padri sono: "+ "E.9.02.03.00.000" +"E.9.02.00.00.000" +"E.9.00.00.00.000")	
	 *  
	 *
	 * @param tipoClassificatore the tipo classificatore
	 * @param siacTEnteProprietarioId the siac t ente proprietario id
	 * @param annoEsercizio the anno esercizio
	 * @param classifCodes the classif codes
	 * @return the list
	 */
	 List<SiacTClass> findFigliClassificatore(String annoEsercizio, SiacDClassTipoEnum tipoClassificatore, Integer siacTEnteProprietarioId, String... classifCodes);


	/**
	 * Dato un clasificatore gerarchico ricerca ricorsivamente tutti i suoi figli.
	 *
	 * @param tclass the tclass
	 * @return the list
	 */
	List<SiacTClass> recursiveFindFigliClassificatore(SiacTClass tclass);
	
	
	
	/**
	 * Ricerca un classificatore a partire dall'elenco della gerarchia dei suoi codici.
	 *
	 * @param tipoClassificatore the tipo classificatore
	 * @param siacTEnteProprietarioId l'id dell'ente proprietario
	 * @param annoEsercizio the anno esercizio
	 * @param classifCodes the classif codes
	 * @return the siac t class
	 */
	SiacTClass findClassificatore(String annoEsercizio, SiacDClassTipoEnum tipoClassificatore, Integer siacTEnteProprietarioId, String... classifCodes);
	
	
	/**
	 * Ricerca un classificatore gerarchico restituendo tutti i suoi padri in una mappa.
	 *
	 * @param classifId the classif id
	 * @return Mappa con chiave il codice tipo classificatore e valore l'id del classificatore
	 */	
	Map<String, SiacTClass> ricercaClassificatoriMapByClassId(Integer classifId);

	/**
	 * Ricerca un classificatore gerarchico restituendo tutti i suoi padri in una mappa.
	 *
	 * @param result the result
	 * @param classif the classif
	 * @return Mappa con chiave il codice tipo classificatore e valore l'id del classificatore
	 */
	void ricercaClassifRicorsivaByClassifId(Map<String, SiacTClass> result, SiacTClass classif);
	
	
	
//	/**
//	 * 
//	 * @param codiceClassificatore
//	 * @param tipoClassificatore
//	 * @param famigliaClass
//	 * @deprecated use findFigliClassificatoreIds(SiacDClassTipoEnum tipoClassificatore, String... classifCodes) instead
//	 * @return
//	 */
//	@Deprecated
//	List<Integer> findFigliClassificatoreIds(String codiceClassificatore, SiacDClassTipoEnum tipoClassificatore, SiacDClassFamEnum famigliaClass);
	
	
//	/**
//	 * 
//	 * Restituisce i figli di un classificatore.
//	 * Ad esempio per piano dei conti con classifCode = "E.9.02.00.00.000" viene restituito tra i figli anche il classificatore "E.9.02.03.00.000"
//	 * 
//	 * (Nota per piano dei conti con classifCode = "E.9.02.03.03.000"  -> i padri sono: "+ "E.9.02.03.00.000" +"E.9.02.00.00.000" +"E.9.00.00.00.000")		
//	 * 
//	 * @param classifCode esempio: "E.9.02.00.00.000"
//	 * @param tipoClassificatore esempio: SiacDClassTipoEnum.PDC
//	 * @param famigliaClass esempio: SiacDClassFamEnum.PianoDeiConti
//	 * @deprecated use findFigliClassificatore(SiacDClassTipoEnum tipoClassificatore, String... classifCodes) instead
//	 * @return
//	 */
//	List<SiacTClass> findFigliClassificatore(String classifCode, SiacDClassTipoEnum tipoClassificatore, SiacDClassFamEnum famigliaClass);
	
	
//	@Deprecated
//	SiacTClass findPadreClassificatore(String codice, SiacDClassTipoEnum programma, SiacDClassFamEnum spesamissioniprogrammi);

	/**
	 * Ricerca sintetica del classificatore.
	 * 
	 * @param enteProprietarioId l'id dell'ente
	 * @param siacDClassTipoEnum il tipo ci classificatore
	 * @param anno l'anno
	 * @param classifCode il codice del classificatore
	 * @param classifDesc la descrizione del classificatore
	 * @param pageable la paginazione
	 * @return la pagina corrispondente alla ricerca
	 */
	Page<SiacTClass> ricercaSinteticaClassificatore(Integer enteProprietarioId, SiacDClassTipoEnum siacDClassTipoEnum, Integer anno, String classifCode, String classifDesc, Pageable pageable);

	List<SiacTClass> findClassifByEnteAndClassifTipoCodes(Integer enteProprietarioId, Integer anno,
			Collection<String> classifTipoCodes);

	List<SiacDClassTipo> findClassTipoByEnteAndAnnoAndTipoCode(Integer enteProprietarioId, Integer anno,
			Collection<String> classifTipoCodes);

	Long countByEnteProprietarioIdAndClassifTipoCodesAndAnno(Integer enteProprietarioId,
			Collection<String> classifTipoCodes, Integer anno);

	List<SiacTClass> findClassifByEnteAndClassifCodeAndClassifTipoCodes(Integer enteProprietarioId,
			String classifCode, Integer anno, Collection<String> classifTipoCodes);

	List<SiacTClass> findByClassTipoAndByAnnoAndByEnteProprietarioId(String classifTipoCodice, Integer anno,
			Integer enteProprietarioIid);

	SiacTClass findClassifByCodiceAndEnteAndTipoCode(String classifCode, Integer enteProprietarioId,
			Integer anno, String classifTipoCode);

	List<SiacTClass> findClassifByEnteAndTipoCode(Integer enteProprietarioId, Integer anno,
			String classifTipoCode);

	List<SiacTClass> findClassifByCodiceAndEnteAndTipoCodeLike(String classifCode, Integer enteProprietarioId,
			Integer anno, Collection<String> classifTipoCodes);

	List<SiacTClass> findClassificatoriByClassifIdPadre(Integer classifIdPadre, Integer enteProprietarioId,
			String anno);
	
	List<SiacTClass> findByClassifCodeAndClasifTipoCodeAndFamCode(String classifCode, String classifTipoCode, String famCode, Integer enteProprietarioId, String anno);
	
	
}