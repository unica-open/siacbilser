/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamenti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class CespiteDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnteprimaAmmortamentoAnnuoCespiteDaoImpl extends JpaDao<SiacTCespitiElabAmmortamenti, Integer> implements AnteprimaAmmortamentoAnnuoCespiteDao {
	
	private static final String FUNZIONE_INSERIMENTO_ANTEPRIMA = "fnc_siac_cespiti_elab_ammortamenti";
	
	public SiacTCespitiElabAmmortamenti create(SiacTCespitiElabAmmortamenti e){
		Date now = new Date();
		e.setDataModificaInserimento(now);		
		e.setUid(null);		
		super.save(e);
		return e;
	}

	public SiacTCespitiElabAmmortamenti update(SiacTCespitiElabAmmortamenti e){		
		SiacTCespitiElabAmmortamenti eAttuale = this.findById(e.getUid());		
		Date now = new Date();
		e.setDataInizioValidita(eAttuale.getDataInizioValidita());
		e.setDataModifica(now);
		entityManager.flush();		
		super.update(e);
		return e;
	}
	
	/**
	 * Inserisci anteprima ammortamento annuo cespite.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param anno the anno
	 * @param loginOperazione the login operazione
	 * @return the list
	 */
	@Override
	public List<Object[]> inserisciAnteprimaAmmortamentoAnnuoCespite(Integer enteProprietarioId, Integer anno, String loginOperazione) {
		final String methodName = "inserisciAnteprimaAmmortamentoAnnuoCespite";
		
		if(enteProprietarioId == null || anno == null || loginOperazione == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile invocare la function " + FUNZIONE_INSERIMENTO_ANTEPRIMA + " : parametri in input non inizializzati"));
		}
		
		
		log.debug(methodName, "Calling functionName: " + FUNZIONE_INSERIMENTO_ANTEPRIMA + " for anno:  "+ anno);
		String sql = "SELECT * FROM "+ FUNZIONE_INSERIMENTO_ANTEPRIMA + "(:enteProprietarioId, :loginOperazione, :anno)";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.setParameter("enteProprietarioId", enteProprietarioId);
		query.setParameter("loginOperazione", loginOperazione);
		query.setParameter("anno", anno);
		
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.getResultList();
		return result;
	}

	@Override
	public Page<SiacTCespitiElabAmmortamentiDett> ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(int enteProprietarioId, Integer annoAnteprima, Pageable pageable) {
		final String methodName = "ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryDettaglioAnteprimaAmmortamentoAnnuoCespite(jpql, param, enteProprietarioId, annoAnteprima);
		
		jpql.append(" ORDER BY tdet.elabDettId, tdet.elabDetSegno, tdet.numeroCespiti ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	/**
	 * Componi query dettaglio anteprima ammortamento annuo cespite.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param enteProprietarioId the ente proprietario id
	 * @param annoAnteprima the anno anteprima
	 */
	private void componiQueryDettaglioAnteprimaAmmortamentoAnnuoCespite(StringBuilder jpql, Map<String, Object> param, int enteProprietarioId, Integer annoAnteprima) {
		jpql.append(" FROM SiacTCespitiElabAmmortamentiDett tdet ");
		jpql.append(" WHERE tdet.dataCancellazione IS NULL ");
		jpql.append(" AND tdet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", Integer.valueOf(enteProprietarioId));
		if(annoAnteprima != null) {
			jpql.append(" AND tdet.siacTCespitiElabAmmortamenti.anno = :anno ");
			param.put("anno", annoAnteprima);
		}
		
	}
	
}
