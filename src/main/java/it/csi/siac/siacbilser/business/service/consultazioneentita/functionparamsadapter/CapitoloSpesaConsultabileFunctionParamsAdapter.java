/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter;

import java.sql.Types;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.utility.function.jdbc.SQLParam;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaCapitoloSpesaConsultabile;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * {@link FunctionParamsAdapter} per la ricerca dei capitoli di spesa.
 * 
 * @author Domenico
 *
 */
public class CapitoloSpesaConsultabileFunctionParamsAdapter extends BaseImplFunctionParamsAdapter<ParametriRicercaCapitoloSpesaConsultabile>  {


	@Override
	public Object[] toFunctionParamsArray(ParametriRicercaCapitoloSpesaConsultabile pr, Ente ente) {
		
		return toArray(
				integer(ente), 
				varchar(pr.getAnnoCapitolo()), 
				varchar(pr.getNumeroCapitolo()), 
				varchar(pr.getNumeroArticolo()),
				varchar(pr.getNumeroUEB()),
				integer(pr.getTitoloSpesa()),
				new SQLParam(null, Types.INTEGER), //Tipologia
				new SQLParam(null, Types.INTEGER), //Categoria
				integer(pr.getMacroaggregato()),
				integer(pr.getStrutturaAmministrativoContabile()),
				varchar(FaseBilancio.GESTIONE.equals(pr.getFaseBilancio()) ? 
						SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice()
						: SiacDBilElemTipoEnum.CapitoloUscitaPrevisione.getCodice())
				);
		

	}

}
