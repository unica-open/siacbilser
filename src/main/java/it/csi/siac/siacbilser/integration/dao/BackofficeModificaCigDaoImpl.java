/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

@Component
@Transactional
public class BackofficeModificaCigDaoImpl extends JpaDao<SiacTMovgest, Integer> implements BackofficeModificaCigDao {
	
	
	
	@Override
	public Integer backofficeModificaCigMovgest(int uid, Integer uidTipoDebito, String cig,
			Integer uidMotivazioneAssenzaCig, String numeroRemedy) {
		
		final String methodName = "modificaCigSMovgest";
		
		Query query = entityManager.createNativeQuery("SELECT fnc_siac_bko_impegno_cig_su_movgest"
				+ "(:uid, :uidTipoDebito, :cig, :uidMotivazioneAssenzaCig, :numeroRemedy)");
		query.setParameter("uid", uid);
		query.setParameter("uidTipoDebito", uidTipoDebito);
		query.setParameter("cig", StringUtils.isNotBlank(cig) ? cig : "");
		query.setParameter("uidMotivazioneAssenzaCig", uidMotivazioneAssenzaCig != null ? uidMotivazioneAssenzaCig : Integer.valueOf(0));
		// Evolutive BackofficeModificaCigRemedy
		query.setParameter("numeroRemedy", StringUtils.isNotBlank(numeroRemedy) ? numeroRemedy : "");
		
		Integer result = (Integer) query.getSingleResult();
		
		log.debug(methodName, "Returning result: "+ result + " for functionName: fnc_siac_bko_impegno_cig_su_movgest");
		
		return 0;
	}

	@Override
	public Integer backofficeModificaCigCollegati(int uid, Integer uidTipoDebito, String cig,
			Integer uidMotivazioneAssenzaCig, String numeroRemedy) {

		final String methodName = "modificaCigCorrelati";

		Query query = entityManager.createNativeQuery("SELECT fnc_siac_bko_impegno_cig_su_collegati"
				+ "(:uid, :uidTipoDebito, :cig, :uidMotivazioneAssenzaCig, :numeroRemedy)");
		query.setParameter("uid", uid);
		query.setParameter("uidTipoDebito", uidTipoDebito);
		query.setParameter("cig", StringUtils.isNotBlank(cig) ? cig : "");
		query.setParameter("uidMotivazioneAssenzaCig", uidMotivazioneAssenzaCig != null ? uidMotivazioneAssenzaCig : Integer.valueOf(-1));
		// Evolutive BackofficeModificaCigRemedy
		query.setParameter("numeroRemedy", StringUtils.isNotBlank(numeroRemedy) ? numeroRemedy : "");
		
		Integer result = (Integer) query.getSingleResult();

		log.debug(methodName, "Returning result: "+ result + " for functionName: fnc_siac_bko_impegno_cig_su_collegati");
		
		return 0;
	}

}
