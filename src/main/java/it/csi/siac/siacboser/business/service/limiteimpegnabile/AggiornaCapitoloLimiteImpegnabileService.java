/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.business.service.limiteimpegnabile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabile;
import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabileResponse;
import it.csi.siac.siacboser.integration.dad.LimiteImpegnabileDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacintegser.business.service.limiteimpegnabile.model.CapitoloLimiteImpegnabile;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloLimiteImpegnabileService
		extends CheckedAccountBaseService<AggiornaCapitoloLimiteImpegnabile, AggiornaCapitoloLimiteImpegnabileResponse>
{
	@Autowired
	private LimiteImpegnabileDad limiteImpegnabileDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{
		checkNotNull(req.getIdEnte(), "idEnte");
	}

	@Override
	protected void execute()
	{
		CapitoloLimiteImpegnabile capitoloLimiteImpegnabile = req.getCapitoloLimiteImpegnabile();
		
		try
		{
			List<Messaggio> messaggi = limiteImpegnabileDad.aggiornaCapitoloLimiteImpegnabile(capitoloLimiteImpegnabile, req.getIdEnte(),
					req.getRichiedente().getOperatore().getCodiceFiscale());
			
			res.setMessaggi(messaggi);
		}
		catch (Exception e)
		{
			throw new BusinessException(String.format("capitolo %s, %s: %s",
					capitoloLimiteImpegnabile.getCodiceCompleto(), e.getClass().getCanonicalName(), e.getMessage()));
		}

		res.setEsito(Esito.SUCCESSO);
	}

	@Transactional
	@Override
	public AggiornaCapitoloLimiteImpegnabileResponse executeService(AggiornaCapitoloLimiteImpegnabile serviceRequest)
	{
		return super.executeService(serviceRequest);
	}
}
