/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.fcde;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDaoImpl extends JpaDao<SiacTAccFondiDubbiaEsigBil, Integer> implements AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao {
	
	@Override
	public SiacTAccFondiDubbiaEsigBil create(SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil){
		Date now = new Date();
		siacTAccFondiDubbiaEsigBil.setDataModificaInserimento(now);
		siacTAccFondiDubbiaEsigBil.setUid(null);
		
		super.save(siacTAccFondiDubbiaEsigBil);
		return siacTAccFondiDubbiaEsigBil;
	}

	@Override
	public SiacTAccFondiDubbiaEsigBil update(SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil){
		Date now = new Date();
		siacTAccFondiDubbiaEsigBil.setDataModificaAggiornamento(now);
		return super.update(siacTAccFondiDubbiaEsigBil);
	}

	@Override
	public SiacTAccFondiDubbiaEsigBil logicalDelete(SiacTAccFondiDubbiaEsigBil siacTAccFondiDubbiaEsigBil) {
		Date now = new Date();
		siacTAccFondiDubbiaEsigBil.setDataCancellazioneIfNotSet(now);
		return siacTAccFondiDubbiaEsigBil;
	}

	@Override
	public Page<SiacTAccFondiDubbiaEsigBil> ricerca(Integer bilId, String afdeTipoCode, String afdeStatoCode, Integer afdeBilVersione, Integer afdeBilVersioneNotEqual, Pageable pageable) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacTAccFondiDubbiaEsigBil tafdeb ");
		jpql.append(" WHERE tafdeb.dataCancellazione IS NULL ");
		Utility.jpqlAddIfNotNull(jpql, params, " AND tafdeb.siacTBil.bilId = :bilId ", "bilId", bilId);
		Utility.jpqlAddIfNotNull(jpql, params, " AND tafdeb.siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode ", "afdeTipoCode", afdeTipoCode);
		Utility.jpqlAddIfNotNull(jpql, params, " AND tafdeb.siacDAccFondiDubbiaEsigStato.afdeStatoCode = :afdeStatoCode ", "afdeStatoCode", afdeStatoCode);
		Utility.jpqlAddIfNotNull(jpql, params, " AND tafdeb.afdeBilVersione = :afdeBilVersione ", "afdeBilVersione", afdeBilVersione);
		Utility.jpqlAddIfNotNull(jpql, params, " AND tafdeb.afdeBilVersione <> :afdeBilVersioneNotEqual ", "afdeBilVersioneNotEqual", afdeBilVersioneNotEqual);
		
		Utility.jpqlInjectSortingString(jpql, pageable.getSort());
		
		return getPagedList(jpql.toString(), params, pageable);
	}
	
}
