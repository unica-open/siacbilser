/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CategoriaCespitiDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/*
 CREATE TABLE siac.siac_d_cespiti_categoria (
  cescat_id SERIAL,
  cescat_code VARCHAR(200) NOT NULL,
  cescat_desc VARCHAR(500) NOT NULL,


  
  
  
  
  ambito_id INTEGER NOT NULL,
  aliquota_annua NUMERIC,
  cescat_calcolo_tipo_id INTEGER,
  CONSTRAINT pk_siac_d_cespiti_categoria PRIMARY KEY(cescat_id),
  CONSTRAINT siac_d_cespiti_categoria_calcolo_tipo_siac_d_cespiti_categoria FOREIGN KEY (cescat_calcolo_tipo_id)
    REFERENCES siac.siac_d_cespiti_categoria_calcolo_tipo(cescat_calcolo_tipo_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE,
  CONSTRAINT siac_t_ente_proprietario_siac_d_cespiti_categoria FOREIGN KEY (ente_proprietario_id)
    REFERENCES siac.siac_t_ente_proprietario(ente_proprietario_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE
) 

 */
/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciCategoriaCespitiService extends CheckedAccountBaseService<InserisciCategoriaCespiti, InserisciCategoriaCespitiResponse> {

	//DAD
	@Autowired
	private CategoriaCespitiDad categoriaCespitiDad;

	private CategoriaCespiti categoriaCespiti;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		categoriaCespiti = req.getCategoriaCespiti();
		checkNotNull(req.getCategoriaCespiti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("categoria cespiti"));		
		checkNotBlank(categoriaCespiti.getCodice(), "codice categoria cespiti",false);
		checkNotBlank(categoriaCespiti.getDescrizione(), "descrizione categoria cespiti",false);		
		checkNotNull(categoriaCespiti.getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito"));
		checkNotNull(categoriaCespiti.getAliquotaAnnua(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("aliquota annua"));
		checkEntita(categoriaCespiti.getCategoriaCalcoloTipoCespite(), "tipo calcolo");
	}
	
	@Override
	protected void init() {
		super.init();
		categoriaCespitiDad.setEnte(ente);
		categoriaCespitiDad.setLoginOperazione(loginOperazione);	

	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisciCategoriaCespitiResponse executeService(InserisciCategoriaCespiti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		checkCodiceGiaEsistente();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		categoriaCespiti.setDataInizioValidita(inizioAnno);
		
		CategoriaCespiti catCesp = categoriaCespitiDad.inserisciCategoriaCespiti(categoriaCespiti);				
		res.setCategoriaCespiti(catCesp);
	}

	/**
	 * Check codice gia esistente.
	 */
	private void checkCodiceGiaEsistente() {
		if(categoriaCespitiDad.findCategoriaCespitiByCodice(categoriaCespiti.getCodice()) != null){
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Categoria cespite",categoriaCespiti.getCodice()));
		}
	}
	

}



