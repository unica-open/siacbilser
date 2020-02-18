/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping;

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
public enum AttMapId implements MapId {
	
	
	SiacTAttoLegge, 
	SiacTAttoAmm,
	SiacRBilElemAttoLegge_AttoDiLeggeCapitolo, 
	SiacTBilElem_Capitolo;
	
	
}
