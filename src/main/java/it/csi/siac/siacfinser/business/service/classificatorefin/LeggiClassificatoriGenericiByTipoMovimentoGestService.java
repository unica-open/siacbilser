/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.classificatorefin;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGestResponse;
import it.csi.siac.siacfinser.integration.dad.ClassificatoreFinDad;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriGenericiByTipoMovimentoGestService extends 
					AbstractBaseService<LeggiClassificatoriGenericiByTipoMovimentoGest,LeggiClassificatoriGenericiByTipoMovimentoGestResponse>{
	
	@Autowired
	private ClassificatoreFinDad classificatoreFinDad;
	
	@Autowired
	protected Mapper dozerBeanMapper;
		
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"),false);
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente Proprietario"),false);
	}
	
	@Override
	protected void init() {
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public LeggiClassificatoriGenericiByTipoMovimentoGestResponse executeService(LeggiClassificatoriGenericiByTipoMovimentoGest serviceRequest) {
//		return super.executeService(serviceRequest);
//	}

	@Override
	public void execute() {
		String methodName = "LeggiClassificatoriGenericiByTipoMovimentoGestService - execute()";
			
		/*
		 *  lista dei classificatori generici utilizzati per 
		 *  Impegno e Accertamento
		 * 
		 */
		List<ClassificatoreGenerico> listaClassificatoriGenerici = classificatoreFinDad.findClassificatoriGenericiByTipoMovimentoGestione(req.getAnno(),req.getIdEnteProprietario(),req.getCodiceTipoMovimentoGestione());   

		if (listaClassificatoriGenerici == null || listaClassificatoriGenerici.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori", "anno: "+req.getAnno()+" idPadre: "+req.getCodiceTipoMovimentoGestione() ));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		for (ClassificatoreGenerico classificatoreGenerico : listaClassificatoriGenerici) {
			TipologiaClassificatore tipo = TipologiaClassificatore.fromCodice(classificatoreGenerico.getTipoClassificatore().getCodice());
			
			if(tipo==null){
				continue;
			}

			switch (tipo) {
				case CLASSIFICATORE_11:
					res.getClassificatoriGenerici11().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_12:
					res.getClassificatoriGenerici12().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_13:
					res.getClassificatoriGenerici13().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_14:
					res.getClassificatoriGenerici14().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_15:
					res.getClassificatoriGenerici15().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_16:
					res.getClassificatoriGenerici16().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_17:
					res.getClassificatoriGenerici17().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_18:
					res.getClassificatoriGenerici18().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_19:
					res.getClassificatoriGenerici19().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_20:
					res.getClassificatoriGenerici20().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;	
				default:
					break;
			}
		}
		
	}
}
