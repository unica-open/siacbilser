/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.StatoOperativoTipoGiustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaTipoGiustificativoService extends CheckedAccountBaseService<AggiornaTipoGiustificativo, AggiornaTipoGiustificativoResponse> {
		
	
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	private TipoGiustificativo tipoGiustificativo;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getTipoGiustificativo(), "tipo giustificativo");
		tipoGiustificativo = req.getTipoGiustificativo();
		
		checkEntita(tipoGiustificativo.getCassaEconomale(), "cassa economale tipo giustificativo", false);
		checkNotNull(tipoGiustificativo.getTipologiaGiustificativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipologia giustificativo"), false);
		checkCondition(StringUtils.isNotBlank(tipoGiustificativo.getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo giustificativo"), false);
		checkCondition(StringUtils.isNotBlank(tipoGiustificativo.getDescrizione()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo giustificativo"), false);
		
		checkCondition(tipoGiustificativo.getPercentualeAnticipoMissione() == null || 
				(tipoGiustificativo.getPercentualeAnticipoMissione().signum()>=0 && tipoGiustificativo.getPercentualeAnticipoMissione().subtract(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).signum()<=0), 
		ErroreCore.FORMATO_NON_VALIDO.getErrore("percentuale anticipo missione ", ": deve essere compreso tra zero e cento."), false);

		checkCondition(tipoGiustificativo.getPercentualeAnticipoTrasferta() == null || 
				(tipoGiustificativo.getPercentualeAnticipoTrasferta().signum()>=0 && tipoGiustificativo.getPercentualeAnticipoTrasferta().subtract(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).signum()<=0), 
		ErroreCore.FORMATO_NON_VALIDO.getErrore("percentuale anticipo trasferta ", ": deve essere compreso tra zero e cento."), false);
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		tipoGiustificativoDad.setEnte(ente);
		tipoGiustificativoDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaTipoGiustificativoResponse executeService(AggiornaTipoGiustificativo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkStatoValido();
		tipoGiustificativoDad.aggiornaTipoGiustificativo(tipoGiustificativo);
		res.setTipoGiustificativo(tipoGiustificativo);
		
	}

	private void checkStatoValido() {
		TipoGiustificativo tipo = tipoGiustificativoDad.ricercaDettaglioTipoGiustificativo(tipoGiustificativo.getUid());
		if(StatoOperativoTipoGiustificativo.ANNULLATO.equals(tipo.getStatoOperativoTipoGiustificativo())){
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("tipo giustificativo",tipo.getStatoOperativoTipoGiustificativo().getDescrizione()));
		}
	}

	
	
}
