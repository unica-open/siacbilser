/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Versione customizzata di DocumentoSpesaDad da usare solo per metodi ottimizzati per richiami critici come tempi di esecuzione
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DocumentoSpesaFinDad extends AbstractFinDad {
	
	public  DocumentoSpesa findDocumentoSpesaById(SiacTDoc siacTDoc) {
		DocumentoSpesa documentoMappato = null;
		if(siacTDoc!=null && siacTDoc.getDocId()!=null){
			documentoMappato =  mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa);
		}
		return documentoMappato;
	}

	
}
