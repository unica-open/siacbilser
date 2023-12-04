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

import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciByIdPadre;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciByIdPadreResponse;
import it.csi.siac.siacfinser.integration.dad.ClassificatoreFinDad;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriGerarchiciByIdPadreService 
	extends AbstractBaseService<LeggiClassificatoriGerarchiciByIdPadre, LeggiClassificatoriGerarchiciByIdPadreResponse> {

	@Autowired
	private ClassificatoreFinDad classificatoreFinDad;
	
	@Autowired
	protected Mapper dozerBeanMapper;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"), false);
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente proprietario"), false);
		checkCondition(req.getIdPadre() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id. padre"), false);
	}
	
	@Override
	protected void init() {
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public LeggiClassificatoriGerarchiciByIdPadreResponse executeService(LeggiClassificatoriGerarchiciByIdPadre serviceRequest) {
//		return super.executeService(serviceRequest);
//	}

	@Override
    public void execute() {
		String methodName = "LeggiClassificatoriGerarchiciByIdPadreService - execute()";
		List<ClassificatoreGerarchico> listaClassificatoriGerarchici = classificatoreFinDad
				.findClassificatoriGerarchiciByIdPadre(
						req.getAnno(), req.getIdEnteProprietario(), req.getIdPadre());
	
		if (listaClassificatoriGerarchici == null || listaClassificatoriGerarchici.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori", "anno: "+req.getAnno()+" idPadre: "+req.getIdPadre()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		for (ClassificatoreGerarchico c : listaClassificatoriGerarchici) {
			TipologiaClassificatore tipo = TipologiaClassificatore.fromCodice(c.getTipoClassificatore().getCodice());
			
			if(tipo==null){
				continue;
			}

			switch (tipo) {
			case PROGRAMMA:
				res.getClassificatoriProgramma().add(dozerBeanMapper.map(c, Programma.class));
				break;
			case DIVISIONE_COFOG:
			case GRUPPO_COFOG:
			case CLASSE_COFOG:	
				res.getClassificatoriCofog().add(dozerBeanMapper.map(c,ClassificazioneCofog.class));
				break;
			
			default:
				break;
			}
		}
	
	}

}
