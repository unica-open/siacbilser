/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.frontend.webservice.msg;

import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siacintegser.business.service.limiteimpegnabile.model.CapitoloLimiteImpegnabile;

public class AggiornaCapitoloLimiteImpegnabile extends ServiceRequest
{
	private Integer idEnte;
	private CapitoloLimiteImpegnabile capitoloLimiteImpegnabile;

	public Integer getIdEnte()
	{
		return idEnte;
	}

	public void setIdEnte(Integer idEnte)
	{
		this.idEnte = idEnte;
	}

	public CapitoloLimiteImpegnabile getCapitoloLimiteImpegnabile()
	{
		return capitoloLimiteImpegnabile;
	}

	public void setCapitoloLimiteImpegnabile(CapitoloLimiteImpegnabile capitoloLimiteImpegnabile)
	{
		this.capitoloLimiteImpegnabile = capitoloLimiteImpegnabile;
	}

}
