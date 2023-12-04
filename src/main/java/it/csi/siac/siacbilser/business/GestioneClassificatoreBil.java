/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siaccorser.business.BaseGestione;

// TODO: Auto-generated Javadoc
/**
 * Servizio per la gestione dei classificatori del modulo BIL.
 *
 * @author rmontuori
 * @version $Id: $
 */
public interface GestioneClassificatoreBil extends BaseGestione {

	/**
	 * Ricerca i classificatori che hanno un livello di dipendenza con 
	 * quello passato in input = idPadreClassificatore 
	 *   
	 * LeggiClassificatoriByIdPadre (request):.
	 *
	 * @param params the params
	 * @return the leggi classificatori bil by id padre response
	 */
	public LeggiClassificatoriBilByIdPadreResponse findClassificatoriByIdPadre(
			LeggiClassificatoriBilByIdPadre params);

	/**
	 * Ricerca i classificatori generici per tipo elemento di bilancio  
	 *   
	 * LeggiClassificatoriByIdPadre (request):.
	 *
	 * @param req the req
	 * @return the leggi classificatori generici by tipo elemento bil response
	 */
	public LeggiClassificatoriGenericiByTipoElementoBilResponse findClassificatoriGenericiByTipoElementoBil(
			LeggiClassificatoriGenericiByTipoElementoBil req);

	
	/**
	 * Ricerca i classificatori gerarchici, per tipo elemento di bilancio, di primo livello  
	 *   
	 * LeggiClassificatoriByIdPadre (request):.
	 *
	 * @param req the req
	 * @return the leggi classificatori by tipo elemento bil response
	 */
	public LeggiClassificatoriByTipoElementoBilResponse findClassificatoriConLivelloByTipoElementoBil(
			LeggiClassificatoriByTipoElementoBil req);

	
	/**
	 * Ricerca il piano dei conti per idFamigliaTree e idCodificaPadre (es. Macroaggregato)
	 *   
	 * LeggiClassificatoriByIdPadre (request):
	 *
	 * @param req the req
	 * @return the leggi tree piano dei conti response
	 */
	public LeggiTreePianoDeiContiResponse findTreePianoDeiConti(
			LeggiTreePianoDeiConti req);

}
