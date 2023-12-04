/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping;

import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.siacbilser.integration.entity.SiacDFilePagopaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;
import it.csi.siac.siaccommonser.util.dozer.MapId;

/**
 * Id dei mapping di coversione.
 * 
 * Naming convention: * 
 * Il nome dell'Entity ed il nome del Model concatenati da "_" 
 * 
 * @author Domenico
 *
 */
public enum CorMapId implements MapId {

	//SIAC-8264
	SiacTOperazioneAsincrona_OperazioneAsincrona_Bil
	;

}
