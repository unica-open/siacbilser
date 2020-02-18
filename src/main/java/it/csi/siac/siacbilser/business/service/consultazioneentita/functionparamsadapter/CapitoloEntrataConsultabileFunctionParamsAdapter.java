/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter;


import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaCapitoloEntrataConsultabile;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;

/**
 * {@link FunctionParamsAdapter} per la ricerca dei capitoli di spesa.
 * 
 * @author Domenico
 *
 */
public class CapitoloEntrataConsultabileFunctionParamsAdapter extends BaseImplFunctionParamsAdapter<ParametriRicercaCapitoloEntrataConsultabile>  {


	@Override
	public Object[] toFunctionParamsArray(ParametriRicercaCapitoloEntrataConsultabile pr, Ente ente) {
		
		return toArray(
				integer(ente), 
				varchar(pr.getAnnoCapitolo()), 
				varchar(pr.getNumeroCapitolo()), 
				varchar(pr.getNumeroArticolo()),
				varchar(pr.getNumeroUEB()),
				integer(pr.getTitoloEntrata()),
				integer(pr.getTipologiaTitolo()),
				integer(pr.getCategoriaTipologiaTitolo()),
				integer(pr.getStrutturaAmministrativoContabile()),
				varchar(FaseBilancio.GESTIONE.equals(pr.getFaseBilancio()) ? 
						SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice()
						: SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice())
				);
		

	}

}
