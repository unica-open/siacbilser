/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiBeneTipoDao.
 *
 * @author Antonino
 */
public interface TipoBeneCespiteDao extends Dao<SiacDCespitiBeneTipo, Integer> {

	SiacDCespitiBeneTipo create(SiacDCespitiBeneTipo r);


	SiacDCespitiBeneTipo update(SiacDCespitiBeneTipo r);

	
	SiacDCespitiBeneTipo delete(int uidTipoBeneCespiti, String loginOperazione);

	
	Page<SiacDCespitiBeneTipo> ricercaSinteticaTipoBeneCespite(		
			int enteProprietarioId, 
			String cespitiBeneTipoCodice,					
			String cespitiBeneTipoDescrizione,					
			Integer categoriaCespitiUid,
			String	categoriaCespitiCodice,
			Integer contoPatrimonialeUid,
			String contoPatrimonialeCodice,
			Date dataInizioValiditaFiltro,
			Pageable pageable);



}
