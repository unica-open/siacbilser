/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDTipoDocumentoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per il Tipo Documento.
 * 
 * @author Andrea Fontana
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TipoDocumentoDad extends BaseDadImpl {
	
	/** The ente. */
	protected Ente ente;
	
	/** The siac d tipo documento repository. */
	@Autowired
	private SiacDTipoDocumentoRepository siacDTipoDocumentoRepository;

	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param tipoFamigliaDocumento the tipo famiglia documento
	 * @param flagSubordinato the flag subordinato
	 * @param flagRegolarizzazione the flag regolarizzazione
	 * @return the list
	 */
	public List<TipoDocumento> ricercaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento, Boolean flagSubordinato, Boolean flagRegolarizzazione) {
		
		String flagRegolarizzazioneDB = null;
		String flagSubordinatoDB = null;
		
		if (flagRegolarizzazione != null && flagRegolarizzazione){
			flagRegolarizzazioneDB = "flagRegolarizzazione";//FIXME: questa stringa non mi piace
		}
		
		if (flagSubordinato != null && flagSubordinato){
			flagSubordinatoDB = "flagSubordinato";//FIXME: questa stringa non mi piace
		}
			
		List<SiacDDocTipo> elencoTipiDocDB = siacDTipoDocumentoRepository.findTipoDocByEnteFamigliaFlag(ente.getUid(), 
				tipoFamigliaDocumento!=null?tipoFamigliaDocumento.getCodice():null, 
				flagSubordinatoDB, 
				flagRegolarizzazioneDB);
		
		if(elencoTipiDocDB == null) {
			return new ArrayList<TipoDocumento>();
		}
		
		List<TipoDocumento> elencoTipoDocReturn = new ArrayList<TipoDocumento>(elencoTipiDocDB.size());
		
		for (SiacDDocTipo tipoDocDB : elencoTipiDocDB) {			
			TipoDocumento tipoDocToAdd = map(tipoDocDB, TipoDocumento.class,BilMapId.SiacDDocTipo_TipoDocumento); //mapTipoDoc(tipoDocDB);
			elencoTipoDocReturn.add(tipoDocToAdd);
		}
		return elencoTipoDocReturn;
	}
	
	
	/**
	 * Sets the ente.
	 *
	 * @param ente the new ente
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;			
	}
}
