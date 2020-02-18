/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.classificatorefin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGestResponse;
import it.csi.siac.siacfinser.integration.dad.ClassificatoreFinDad;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriGenericiByTipoOrdinativoGestService extends AbstractBaseService<LeggiClassificatoriGenericiByTipoOrdinativoGest,LeggiClassificatoriGenericiByTipoOrdinativoGestResponse>{
	
	@Autowired
	private ClassificatoreFinDad classificatoreFinDad;
	
	@Autowired
	protected Mapper dozerBeanMapper;
	
	private static final Map<String, String> MAPPA_CLASS_STIPENDI;
	
	static {
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put(Constanti.D_ORDINATIVO_TIPO_INCASSO,Constanti.CLASS_RITENUTE_STIPENDI);
		tmp.put(Constanti.D_ORDINATIVO_TIPO_PAGAMENTO,Constanti.CLASS_STIPENDI);
		
		MAPPA_CLASS_STIPENDI = Collections.unmodifiableMap(tmp);
	}
		
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"), false);
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente Proprietario"), false);

		if(StringUtils.isEmpty(req.getCodiceTipoOrdinativoGestione())){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice Tipo Ordinativo"), false);
		}else{
			if(!req.getCodiceTipoOrdinativoGestione().equalsIgnoreCase(Constanti.D_ORDINATIVO_TIPO_INCASSO) &&
			   !req.getCodiceTipoOrdinativoGestione().equalsIgnoreCase(Constanti.D_ORDINATIVO_TIPO_PAGAMENTO)){
				String tipiOrdinativoAmmessi = Constanti.D_ORDINATIVO_TIPO_INCASSO + " o " + Constanti.D_ORDINATIVO_TIPO_PAGAMENTO;
				checkCondition(false, ErroreCore.PARAMETRO_ERRATO.getErrore("Codice Tipo Ordinativo", req.getCodiceTipoOrdinativoGestione(), tipiOrdinativoAmmessi), false);
			}	
		}		
	}
	
	@Override
	protected void init() {
	}
	
	
//	@Override
//	@Transactional(readOnly=true)
//	public LeggiClassificatoriGenericiByTipoOrdinativoGestResponse executeService(LeggiClassificatoriGenericiByTipoOrdinativoGest serviceRequest) {
//		return super.executeService(serviceRequest);
//	}

	@Override
	public void execute() {
		String methodName = "LeggiClassificatoriGenericiByTipoOrdinativoGestService - execute()";
			
		/*
		 *  lista dei classificatori generici utilizzati per 
		 *  Ordinativo di Pagamento e di Incasso
		 * 
		 */
		List<ClassificatoreGenerico> listaClassificatoriGenerici = classificatoreFinDad.findClassificatoriGenericiByTipoOrdinativoGestione(req.getAnno(),
				                                                                                                                           req.getIdEnteProprietario(),
				                                                                                                                           req.getCodiceTipoOrdinativoGestione());   

		if (listaClassificatoriGenerici == null || listaClassificatoriGenerici.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori", "anno : " + req.getAnno() + " tipoOrdinativo : " + req.getCodiceTipoOrdinativoGestione()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		for (ClassificatoreGenerico classificatoreGenerico : listaClassificatoriGenerici) {

			TipologiaClassificatore tipo = TipologiaClassificatore.fromCodice(classificatoreGenerico.getTipoClassificatore().getCodice());
			
			if(tipo==null){
				continue;
			}

			switch (tipo) {
				/**
				 * Classificatori generici dell'ORDINATIVO DI PAGAMENTO
				 * 
				 * IN CORSO DI SVILUPPO AGGIUNGERE QUELLI CHE MANCANO
				 */
				case CLASSIFICATORE_21:
					res.getClassificatoriGenerici21().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_22:
					res.getClassificatoriGenerici22().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_23:
					res.getClassificatoriGenerici23().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_24:
					res.getClassificatoriGenerici24().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_25:
					res.getClassificatoriGenerici25().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;		

				/**
				 * Classificatori generici dell'ORDINATIVO DI INCASSO
				 * 
				 * IN CORSO DI SVILUPPO AGGIUNGERE QUELLI CHE MANCANO
				 */
				case CLASSIFICATORE_26:
					res.getClassificatoriGenerici26().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_27:
					res.getClassificatoriGenerici27().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_28:
					res.getClassificatoriGenerici28().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_29:
					res.getClassificatoriGenerici29().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;
				case CLASSIFICATORE_30:
					res.getClassificatoriGenerici30().add(dozerBeanMapper.map(classificatoreGenerico,ClassificatoreGenerico.class));
					break;	
				case CLASSIFICATORE_STIPENDI:
					//QUESTA COSA ANDREBBE GESTITA DIVERSAMENTE, con i classificatori generici. Dal momento pero' che ogni ente ha ormai un classsificatore generico con un numero diverso
					//e che non e' sicuro prendere un classificatore pre-esistente, si opta per quetsa cosa brutta. Chiedo venia. ricordarsi che una eventuale modifica deve essere gestita con un calssificatore.
					if(MAPPA_CLASS_STIPENDI.get(req.getCodiceTipoOrdinativoGestione()).equals(classificatoreGenerico.getCodice())) {
						res.getClassificatoriStipendi().add((ClassificatoreStipendi) dozerBeanMapper.map(classificatoreGenerico,ClassificatoreStipendi.class));
					}
					break;
				default:
					break;
			}
		}
	}
}