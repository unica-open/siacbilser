/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.frontend.webservice;

import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabile;
import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabileResponse;

public interface LimiteImpegnabileService
{
	public AggiornaCapitoloLimiteImpegnabileResponse aggiornaCapitoloLimiteImpegnabile( 
			AggiornaCapitoloLimiteImpegnabile req);
}
