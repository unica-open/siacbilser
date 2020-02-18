/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.classificatorefin;

import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.BaseDao;
import it.csi.siac.siacfinser.integration.entity.SiacCodifica;

public interface SiacTClassFinDao extends BaseDao{
	
	
	/*******************************************
	 * 
	 * 
	 * 				RAFFA
	 * 
	 *******************************************/
	
	/**
	 * Restituisce i classificatori generici (es: classif 1..2..2) ricercati per :
	 * 
	 * @param anno di esercizio
	 * @param idEnteProprietario 
	 * @param tipoMovimentoGestione es: impegno
	 * @return
	 */
	public List<SiacCodifica> findClassificatoriGenericiByTipoMovimentoGestione(int anno,int idEnteProprietario,String codiceTipoMovimentoGestione);
	
	/**
	 * Restituisce i classificatori generici (es: classif 21..22..26..27) ricercati per :
	 * 
	 * @param anno di esercizio
	 * @param idEnteProprietario 
	 * @param tipoOrdinativoGestione es: ordinativo_incasso / ordinativo_pagamento
	 * @return
	 */
	public List<SiacCodifica> findClassificatoriGenericiByTipoOrdinativoGestione(int anno, int idEnteProprietario, String codiceTipoOrdinativoGestione);

	/**
	 * Restituisce i classificatori gerarchici di primo livello (es: missione) ricercati per :
	 *  
	 * @param anno di esercizio
	 * @param idEnteProprietario 
	 * @param tipoMovimentoGestione es: impegno
	 * @return
	 */
	public List<SiacCodifica> findClassificatoriGerarchiciILivelloByTipoMovimentoGestione(int anno, int idEnteProprietario, String codiceTipoMovimentoGestione);

	/**
	 * Restituisce i classificatori gerarchici di secondo livello (es: programma, cofog) 
	 * ricercati per :
	 *  
	 * @param anno di esercizio
	 * @param idEnteProprietario 
	 * @param idPadre es: impegno
	 * @return
	 */
	public List<SiacCodifica> findClassificatoriGerarchiciByIdPadre(int anno, int idEnteProprietario, int idPadre);

	/**
	 * Restituisce il tree piano del conto economico
	 * 
	 * @param anno di esercizio
	 * @param idEnteProprietario 
	 * @param idFamigliaTree
	 * @param idCodificaPadre
	 * 
	 * @return
	 */
	public List<SiacCodifica> findTreeClassificatoriGerarchiciByFamigliaId(Integer anno, Integer enteProprietarioId, String codiceFamigliaTree, Integer idCodificaPadre);
}