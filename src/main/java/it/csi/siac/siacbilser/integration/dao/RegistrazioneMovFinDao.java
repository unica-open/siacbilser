/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRegMovFinStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;


/**
 * The Interface RegistrazioneMovFinDao.
 *
 * @author Valentina
 * @author Domenico
 */
public interface RegistrazioneMovFinDao extends Dao<SiacTRegMovfin, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param siacTRegMovfin the siac t reg movfin
	 * @return the siac t reg movfin
	 */
	SiacTRegMovfin create(SiacTRegMovfin siacTRegMovfin);
	

	@Override
	SiacTRegMovfin update(SiacTRegMovfin entity);
	

	/**
	 * Ricerca sintetica registrazione mov fin.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param bilId the bil id
	 * @param eventoId the evento id
	 * @param docId the doc id
	 * @param campoPkId the campo pk id
	 * @param dataRegistrazioneDa the data registrazione da
	 * @param dataRegistrazioneA the data registrazione a
	 * @param siacDRegMovFinStatoEnum the siac d reg mov fin stato enum
	 * @param siacDRegMovFinStatosEnumDaEscludere the siac d reg mov fin statos enum da escludere
	 * @param classifId the classif id
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTRegMovfin> ricercaSinteticaRegistrazioneMovFin(Integer enteProprietarioId, 
																Integer bilId,
																SiacDAmbitoEnum siacDAmbitoEnum,
																Integer eventoId,
																List<String> eventoCodes,
																SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum,
																Integer docId,
																List<Integer> campoPkId, 
																Date dataRegistrazioneDa,
																Date dataRegistrazioneA,
																SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnum, 
																Set<SiacDRegMovFinStatoEnum> siacDRegMovFinStatosEnumDaEscludere,
																Integer classifId,
																Pageable pageable);
	
	/**
	 * Ricerca sintetica registrazione mov fin.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param bilId the bil id
	 * @param siacDAmbitoEnum the siac D ambito enum
	 * @param eventoId the evento id
	 * @param eventoCodes the evento codes
	 * @param pkIdsByEventoCodes the pk ids by evento codes
	 * @param siacDCollegamentoTipoEnum the siac D collegamento tipo enum
	 * @param docId the doc id
	 * @param dataRegistrazioneDa the data registrazione da
	 * @param dataRegistrazioneA the data registrazione a
	 * @param siacDRegMovFinStatoEnum the siac d reg mov fin stato enum
	 * @param siacDRegMovFinStatosEnumDaEscludere the siac d reg mov fin statos enum da escludere
	 * @param classifId the classif id
	 * @param bilElemId the bil elem id
	 * @param soggettoId the soggetto id
	 * @param movgestId the movgest id
	 * @param movgestTsId the movgest ts id
	 * @param siacDCollegamentoTipoEnumlist the siac D collegamento tipo enumlist
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTRegMovfin> ricercaSinteticaRegistrazioneMovFin(
		Integer enteProprietarioId,
		Integer bilId,
		SiacDAmbitoEnum siacDAmbitoEnum,
		Integer eventoId,
		Collection<String> eventoCodes,
		Map<Collection<String>, Collection<Integer>> pkIdsByEventoCodes,
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum,
		Integer docId,
		Date dataRegistrazioneDa,
		Date dataRegistrazioneA,
		SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnum,
		Set<SiacDRegMovFinStatoEnum> siacDRegMovFinStatosEnumDaEscludere,
		Integer classifId,
		Integer bilElemId,
		Integer soggettoId,
		Integer movgestId,
		Integer movgestTsId,
		//SIAC-5799		
		Integer attoAmmId,
		//SIAC-5944
		Integer idSac,
		Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnumList,
		Pageable pageable);
	
	/**
	 * Ricerca movimento by id.
	 *
	 * @param entity the entity
	 * @param column the column
	 * @param campoPkId the campo pk id
	 * @return the siac t base
	 */
	SiacTBase ricercaMovimentoById(String entity, String column, Integer campoPkId);


	/**
	 * Find id movimento.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param eventoTipoEnum the evento tipo enum
	 * @param annoMovimento the anno movimento
	 * @param numeroMovimento the numero movimento
	 * @param numeroSubdocumento the numero subdocumento
	 * @return the integer
	 */
	Integer findIdMovimento(Integer enteProprietarioId, 
									SiacDCollegamentoTipoEnum eventoTipoEnum,
									Integer annoMovimento, 
									String numeroMovimento,
									Integer numeroSubdocumento);
	/**
	 * Find lista id movimenti.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param eventoTipoEnum the evento tipo enum
	 * @param annoMovimento the anno movimento
	 * @param numeroMovimento the numero movimento
	 * @param numeroSubmovimento the numero submovimento
	 * @return the integers
	 */
	List<Integer> findListaIdMovimento(Integer enteProprietarioId, 
			SiacDCollegamentoTipoEnum eventoTipoEnum,
			Integer annoMovimento, 
			String numeroMovimento,
			Integer numeroSubmovimento);
	
	List<SiacTRegMovfin> ricercaRegistrazionByPrimaNota(Integer uidPrimaNota, Set<SiacDRegMovFinStatoEnum> siacDRegMovFinStatosEnumDaEscludere);

}
