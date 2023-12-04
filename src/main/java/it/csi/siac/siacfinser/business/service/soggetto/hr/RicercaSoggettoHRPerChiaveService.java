/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto.hr;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneDataType;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSoggettoHRPerChiaveService extends
		BaseRicercaSoggettiHRService<RicercaSoggettoPerChiave, RicercaSoggettoPerChiaveResponse>
{
	@Override
	protected void execute()
	{
		ParametroRicercaSoggettoK parametroSoggettoK = req.getParametroSoggettoK();

		if (parametroSoggettoK.getMatricola() != null)
		{
			List<MissioneDataType> listMissioneDataType;

			listMissioneDataType = hrServiceDelegate.viDipendentiMatr(StringUtils.upperCase(parametroSoggettoK.getMatricola()));

			if (listMissioneDataType != null && !listMissioneDataType.isEmpty())
				res.setSoggetto(toSoggetto(listMissioneDataType.get(0)));
		}
	}
}
