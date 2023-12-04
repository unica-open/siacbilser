/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.common;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ListeService extends AbstractBaseService<Liste, ListeResponse> {

	@Autowired
	CommonDad commonDad;
	
	BeanInfo info;
	PropertyDescriptor[] propDescs;
	
	@Override
	protected void init() {
		super.init();
			try {
				info = Introspector.getBeanInfo(ListeResponse.class);
			} catch (IntrospectionException e) {
				throw new RuntimeException(e);
			}
			propDescs = info.getPropertyDescriptors();
	}
	
	
//	@Override
//	@Transactional(readOnly=true)
//	public ListeResponse executeService(Liste serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	@Transactional
	public void execute() {
		String methodName = "ListeService - execute()";
		
		
		/*
		 *  metodo unificato per il caricamento di oggetti di tipo CodificaFin
		 *  al fine del riempimento delle combo box
		 *  
		 *  Funziona con un identificativo unico in TipiLista con lo speculare 
		 *  per il mapping uno-a-uno nella response ListeResponse
		 *  
		 */
		
		Bilancio bilancio = req.getBilancio();
		
		if (req.getTipi().length > 0) {
			Ente ente = req.getRichiedente().getAccount().getEnte();
			for (TipiLista tipo : req.getTipi()) {
				if (tipo != null) {
					switch (tipo) {
					case NAZIONI:
						setLista(TipiLista.NAZIONI, commonDad.findNazioni(ente));
						break;
					case CLASSE_SOGGETTO:
						setLista(TipiLista.CLASSE_SOGGETTO, commonDad.findSoggettiClasse(ente));
						break;
					case GIURIDICA_SOGGETTO:
						setLista(TipiLista.GIURIDICA_SOGGETTO, commonDad.findListaNaturaGiuridicaSoggetto(ente));
						break;
					case NATURA_GIURIDICA:
						setLista(TipiLista.NATURA_GIURIDICA, commonDad.findListaNaturaGiuridica(ente));
						break;
					case TIPO_INDIRIZZO_SEDE:
						setLista(TipiLista.TIPO_INDIRIZZO_SEDE, commonDad.findTipiIndirizziSede(ente));
						break;
					case STATO_OPERATIVO_SOGGETTO:
						setLista(TipiLista.STATO_OPERATIVO_SOGGETTO, commonDad.findStatiOperativi(ente));
						break;
					case FORMA_GIURIDICA_TIPO:
						setLista(TipiLista.FORMA_GIURIDICA_TIPO, commonDad.findFormaGiuridicaTipo(ente));
						break;
					case RECAPITO_MODO:
						setLista(TipiLista.RECAPITO_MODO, commonDad.findRecapitoModo(ente));
						break;		
					case TIPO_ONERE:
						setLista(TipiLista.TIPO_ONERE, commonDad.findTipoOneri(ente));
						break;	
					case TIPO_ACCREDITO:
						setLista(TipiLista.TIPO_ACCREDITO, commonDad.findTipoAccredito(ente));
						break;
					case TIPO_LEGAME:
						setLista(TipiLista.TIPO_LEGAME, commonDad.findTipoLegame(ente));
						break;
					case STATO_MOVGEST:
						setLista(TipiLista.STATO_MOVGEST, commonDad.findStatoOperativoMovgest(ente));
						break;
					case TIPO_IMPEGNO:
						setLista(TipiLista.TIPO_IMPEGNO, commonDad.findTipoImpegno(ente));
						break;
					case TIPO_MOTIVO:
						setLista(TipiLista.TIPO_MOTIVO, commonDad.findTipoMotivoByCodiceApplicazione(ente, "GEN"));
						break;
					case TIPO_MOTIVO_ROR:
						setLista(TipiLista.TIPO_MOTIVO_ROR, commonDad.findTipoMotivoByCodiceApplicazione(ente, "ROR"));
						break;
					case TIPO_MOTIVO_AGG:
						setLista(TipiLista.TIPO_MOTIVO_AGG, commonDad.findTipoMotivoByCodiceApplicazione(ente, "AGG-RID"));
						break;
					case DISTINTA:
						setLista(TipiLista.DISTINTA,  commonDad.findDistintaTipoS(ente));
						break;
					case CONTO_TESORERIA:
						res.setContoTesoreria(commonDad.findContoTesoreria(ente)); 					
						res.addTipiLista(tipo);
						break;
					case CODICE_BOLLO:
						setLista(TipiLista.CODICE_BOLLO,  commonDad.findCodiceBollo(ente));
						break;
					case COMMISSIONI:
						setLista(TipiLista.COMMISSIONI,  commonDad.findCommissioniOrdinativo(ente));
						break;	
					case AVVISO:
						setLista(TipiLista.AVVISO,  commonDad.findTipoAvviso(ente));
						break;
					case NOTE_TESORIERE:
						setLista(TipiLista.NOTE_TESORIERE,  commonDad.findNoteTesoriere(ente));
						break;
					case STATO_ORDINATIVO:
						setLista(TipiLista.STATO_ORDINATIVO, commonDad.findStatoOperativoOrdinativo(ente));
						break;
					case DISTINTA_ENTRATA:
						setLista(TipiLista.DISTINTA_ENTRATA,  commonDad.findDistintaTipoE(ente));
						break;	
					case STATO_OP_CARTA_CONTABILE:	
						setLista(TipiLista.STATO_OP_CARTA_CONTABILE,  commonDad.findStatiOpCartaCont(ente));
						break;	
					case VALUTA:
						setLista(TipiLista.VALUTA, commonDad.findValuta(ente));
						break;
					case COMMISSIONI_ESTERO:
						setLista(TipiLista.COMMISSIONI_ESTERO, commonDad.findCommissioniEstero(ente));
						break;
					case TIPO_DOCUMENTO_SPESA:
						setLista(TipiLista.TIPO_DOCUMENTO_SPESA, commonDad.findTipoDocumentoSpesa(ente));
						break;
					case TIPO_LEGAME_SOGGETTO:
						setLista(TipiLista.TIPO_LEGAME_SOGGETTO, commonDad.findTipoLegameSoggetto(ente));
						break;
						
					case TIPO_SIOPE_SPESA_I:
						setLista(TipiLista.TIPO_SIOPE_SPESA_I, commonDad.findTipoSiopeSpesaI(ente,bilancio));
						break;
						
					case TIPO_SIOPE_ENTRATA_I:
						setLista(TipiLista.TIPO_SIOPE_ENTRATA_I, commonDad.findTipoSiopeEntrataI(ente,bilancio));
						break;
						
					case MOTIVAZIONE_ASSENZA_CIG:
						setLista(TipiLista.MOTIVAZIONE_ASSENZA_CIG, commonDad.findMotivazioniAssenzaCig(ente,bilancio));
						break;
						
					default:
						log.debug(null, "Tipo non gestito");
						break;
					}
				}
			}
		}
		
	}
	
	protected void setLista(TipiLista tipo, ArrayList<? extends CodificaFin> lista) {
		try {
			for (PropertyDescriptor pd : propDescs) {
				if (tipo.getAttr().equals(pd.getName())) {
					pd.getWriteMethod().invoke(res, lista);
					res.addTipiLista(tipo);
					break;
				}
			}
		} catch (Exception exc) {
			log.error("setLista", exc);
		}
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="CHECKPARAM";
		log.debug(methodName, "INIZIO ESECUZIONE");
		
		checkCondition(req.getTipi() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipi lista"));
	}
}
