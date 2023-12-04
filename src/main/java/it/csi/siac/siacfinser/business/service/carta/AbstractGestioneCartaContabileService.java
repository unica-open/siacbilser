/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;


import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;

public abstract class AbstractGestioneCartaContabileService <REQ extends ServiceRequest, RES extends ServiceResponse> 
			extends AbstractBaseService<REQ, RES> {
	
	protected boolean isRigaDaMovimenti(PreDocumentoCarta preDocumentoCarta){
		boolean daMovimenti = true;
		if(!isEmpty(preDocumentoCarta.getListaSubDocumentiSpesaCollegati())){
			daMovimenti = false;
		}
		return daMovimenti;
	}
	
}