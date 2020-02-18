/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.limiteimpegnabile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacboser.business.service.limiteimpegnabile.AggiornaCapitoloLimiteImpegnabileService;
import it.csi.siac.siacboser.frontend.webservice.LimiteImpegnabileService;
import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabile;
import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabileResponse;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.business.service.limiteimpegnabile.model.CapitoloLimiteImpegnabile;
import it.csi.siac.siacintegser.business.service.limiteimpegnabile.util.FileLimiteImpegnabileHandler;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class ElaboraFileLimiteImpegnabileService extends ElaboraFileBaseService
{
	@Autowired
	private LimiteImpegnabileService limiteImpegnabileService;

	@Autowired
	private FileLimiteImpegnabileHandler fileLimiteImpegnabileHandler;

	private List<CapitoloLimiteImpegnabile> elencoCapitoliLimiteImpegnabile;

	@Override
	public void checkServiceParam() throws ServiceParamError
	{
		super.checkServiceParam();

	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public ElaboraFileResponse executeServiceTxRequiresNew(ElaboraFile serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}

	@Override
	protected void init()
	{
		super.init();
	}

	@Override
	protected void initFileData()
	{
		final String methodName = "initFileData";

		log.debug(methodName, "lunghezza del file: " + fileBytes.length);

		try
		{
			elencoCapitoliLimiteImpegnabile = fileLimiteImpegnabileHandler
					.readElencoCapitoliLimiteImpegnabile(fileBytes);
		}
		catch (Exception e)
		{
			log.error(methodName, e.getMessage(), e);

			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	protected void elaborateData()
	{
		String methodName = "elaborateData";

		for (final CapitoloLimiteImpegnabile capitoloLimiteImpegnabile : elencoCapitoliLimiteImpegnabile)
		{
			AggiornaCapitoloLimiteImpegnabile reqACLI = new AggiornaCapitoloLimiteImpegnabile();
			reqACLI.setCapitoloLimiteImpegnabile(capitoloLimiteImpegnabile);
			reqACLI.setIdEnte(req.getEnte().getUid());
			reqACLI.setRichiedente(req.getRichiedente());

			serviceExecutor.executeServiceTxRequiresNew(AggiornaCapitoloLimiteImpegnabileService.class, reqACLI,
					new ResponseHandler<AggiornaCapitoloLimiteImpegnabileResponse>()
					{
						@Override
						protected void handleResponse(AggiornaCapitoloLimiteImpegnabileResponse resACLI)
						{
							res.addMessaggi(resACLI.getMessaggi());
							res.addErrori(resACLI.getErrori());

							if (resACLI.isFallimento())
							{
								res.addErrore(ErroreCore.OPERAZIONE_ABBANDONATA
										.getErrore(String.format("aggiornamento capitolo limite impegnabile %s",
												capitoloLimiteImpegnabile.getCodiceCompleto())));
							}
						}
					});
		}
	}

}
