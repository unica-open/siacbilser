/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommon.util.CoreUtils;
import it.csi.siac.siaccorser.business.BaseGestioneImpl;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Implementazione del servizio di gestione dei Classificatori.
 *
 * @author rmontuori
 * @version $Id: $
 */
@Component
@Transactional
public class GestioneClassificatoreBilImpl extends BaseGestioneImpl implements
		GestioneClassificatoreBil {

	/** The codifica bil dao. */
	@Autowired
	private CodificaBilDao codificaBilDao;

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.GestioneClassificatoreBil#findClassificatoriByIdPadre(it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre)
	 */
	@Override
	public LeggiClassificatoriBilByIdPadreResponse findClassificatoriByIdPadre(LeggiClassificatoriBilByIdPadre params) {

		LeggiClassificatoriBilByIdPadreResponse res = new LeggiClassificatoriBilByIdPadreResponse();

		try {

			if (params == null) {
				res.addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
			} else {

				if (params.getAnno() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("anno"));
				}

				if (params.getIdEnteProprietario() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("enteProprietarioId"));
					}

				if (params.getIdPadre() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("IdPadre"));
				}
					
			}

			if (!res.hasErrori()) {

				List<SiacTClass> siacTClasses = codificaBilDao.findCodificheByIdPadre(params.getAnno(), params.getIdEnteProprietario(),
						params.getIdPadre());

				if (siacTClasses != null && !siacTClasses.isEmpty()) {

					for (SiacTClass c : siacTClasses) {
						
						TipologiaClassificatore tipo = TipologiaClassificatore.fromCodice(c
								.getSiacDClassTipo().getClassifTipoCode());
						
						if(tipo==null){
							continue;
						}

						switch (tipo) {
						case PROGRAMMA:
							res.getClassificatoriProgramma().add(
									getDozerBeanMapper().map(c, Programma.class));
							break;
						case MACROAGGREGATO:
							res.getClassificatoriMacroaggregato().add(
									getDozerBeanMapper().map(c, Macroaggregato.class));
							break;
						case CLASSIFICAZIONE_COFOG:
							res.getClassificatoriClassificazioneCofog().add(
									getDozerBeanMapper().map(c, ClassificazioneCofog.class));
							break;
						case TIPOLOGIA:
							res.getClassificatoriTipologiaTitolo().add(
									getDozerBeanMapper().map(c, TipologiaTitolo.class));
							break;
						case CATEGORIA:
							res.getClassificatoriCategoriaTipologiaTitolo().add(
									getDozerBeanMapper().map(c, CategoriaTipologiaTitolo.class));
							break;

						default:
							break;
						}
					}

				} else {
					res.addErrore(ErroreCore.ENTITA_NON_TROVATA
							.getErrore("classificatori"));
					res.setEsito(Esito.FALLIMENTO);
				}

			} else{
				res.setEsito(Esito.FALLIMENTO);
			}
				

		} catch (Exception t) {
			//t.printStackTrace();
			log.error("findClassificatoriGenericiByTipoElementoBil errore generico.", t);
			res.addErroreDiSistema(t);
			res.setEsito(Esito.FALLIMENTO);
		}
		return res;

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.GestioneClassificatoreBil#findClassificatoriGenericiByTipoElementoBil(it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil)
	 */
	@Override
	public LeggiClassificatoriGenericiByTipoElementoBilResponse findClassificatoriGenericiByTipoElementoBil(
			LeggiClassificatoriGenericiByTipoElementoBil req) {

		LeggiClassificatoriGenericiByTipoElementoBilResponse res = new LeggiClassificatoriGenericiByTipoElementoBilResponse();
		try {

			if (req == null) {
				res.addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
			} else {

				if (req.getAnno() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("anno"));
				}
					
				if (req.getIdEnteProprietario() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("enteProprietarioId"));
				}
					
			}

			if (!res.hasErrori()) {

				List<SiacTClass> siacTClasses = codificaBilDao
						.findCodificheByTipoElemBilancio(req.getAnno(),
								req.getIdEnteProprietario(),
								req.getTipoElementoBilancio());

				if (siacTClasses != null && !siacTClasses.isEmpty()) {

					for (SiacTClass c : siacTClasses) {
						
						log.debug("findClassificatoriGenericiByTipoElementoBil. Codice: "+c.getSiacDClassTipo().getClassifTipoCode());

						TipologiaClassificatore tipo = TipologiaClassificatore
								.fromCodice(c.getSiacDClassTipo().getClassifTipoCode());
						
						if(tipo==null){
							continue;
						}

						// TODO: da migliorare...
						switch (tipo) {
						case TIPO_FONDO:
							res.getClassificatoriTipoFondo().add(
									getDozerBeanMapper().map(c, TipoFondo.class));
							break;
						case TIPO_FINANZIAMENTO:
							res.getClassificatoriTipoFinanziamento().add(
									getDozerBeanMapper().map(c, TipoFinanziamento.class));
							break;
						case CLASSIFICATORE_1:
							res.getClassificatoriGenerici1().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_2:
							res.getClassificatoriGenerici2().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_3:
							res.getClassificatoriGenerici3().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_4:
							res.getClassificatoriGenerici4().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_5:
							res.getClassificatoriGenerici5().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_6:
							res.getClassificatoriGenerici6().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_7:
							res.getClassificatoriGenerici7().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_8:
							res.getClassificatoriGenerici8().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_9:
							res.getClassificatoriGenerici9().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						case CLASSIFICATORE_10:
							res.getClassificatoriGenerici10().add(
									getDozerBeanMapper().map(c, ClassificatoreGenerico.class));
							break;
						default:
							break;
						}
					}

				} else {
					res.addErrore(ErroreCore.ENTITA_NON_TROVATA
							.getErrore("classificatori"));
					res.setEsito(Esito.FALLIMENTO);
				}

			} else{
				res.setEsito(Esito.FALLIMENTO);
			}

		} catch (Exception t) {
			//t.printStackTrace();
			log.error("findClassificatoriGenericiByTipoElementoBil errore generico.", t);
			res.addErroreDiSistema(t);
			res.setEsito(Esito.FALLIMENTO);
		}
		return res;

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.GestioneClassificatoreBil#findClassificatoriConLivelloByTipoElementoBil(it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil)
	 */
	@Override
	public LeggiClassificatoriByTipoElementoBilResponse findClassificatoriConLivelloByTipoElementoBil(
			LeggiClassificatoriByTipoElementoBil req) {

		LeggiClassificatoriByTipoElementoBilResponse res = new LeggiClassificatoriByTipoElementoBilResponse();
		try {

			if (req == null) {
				res.addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
			} else {

				if (req.getAnno() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("anno"));
				}
					
				if (req.getIdEnteProprietario() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("enteProprietarioId"));
				}
					
			}

			if (!res.hasErrori()) {

				List<SiacTClass> siacTClasses = codificaBilDao
						.findCodificheConLivelloByTipoElemBilancio(
								req.getAnno(), req.getIdEnteProprietario(),
								req.getTipoElementoBilancio());

				log.debug("Dto trovati: " + (siacTClasses!=null?siacTClasses.size():"null"));
				
				if (siacTClasses != null && !siacTClasses.isEmpty()) {				

					for (SiacTClass c : siacTClasses) {						
						log.debug("CodificaDto: "+ c);
						if(c!=null && c.getSiacDClassTipo()!=null){
							log.debug("CodificaDto codice: "+ c.getSiacDClassTipo().getClassifTipoCode());
							
							TipologiaClassificatore codice = TipologiaClassificatore.fromCodice(c
									.getSiacDClassTipo().getClassifTipoCode());
							
							if(codice==null){
								continue;
							}				
							
							switch (codice) {
							case MISSIONE:
								res.getClassificatoriMissione().add(
										getDozerBeanMapper().map(c, Missione.class));
								break;
							case TITOLO_SPESA:
								res.getClassificatoriTitoloSpesa().add(
										getDozerBeanMapper().map(c, TitoloSpesa.class));
								break;
							case TITOLO_ENTRATA:
								res.getClassificatoriTitoloEntrata().add(
										getDozerBeanMapper().map(c, TitoloEntrata.class));
								break;
								
							default:
								break;
							}
						}
					}

				} else {
					res.addErrore(ErroreCore.ENTITA_NON_TROVATA
							.getErrore("classificatori"));
					res.setEsito(Esito.FALLIMENTO);
				}

			} else{
				res.setEsito(Esito.FALLIMENTO);
			}
				
		} catch (Exception t) {
			//t.printStackTrace();
			log.error("findClassificatoriGenericiByTipoElementoBil errore generico.", t);
			res.addErroreDiSistema(t);
			res.setEsito(Esito.FALLIMENTO);
		}
		CoreUtils.logXmlTypeObject(res, "Service Response param");
		return res;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.GestioneClassificatoreBil#findTreePianoDeiConti(it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti)
	 */
	@Override
	public LeggiTreePianoDeiContiResponse findTreePianoDeiConti(
			LeggiTreePianoDeiConti req) {

		LeggiTreePianoDeiContiResponse res = new LeggiTreePianoDeiContiResponse();
		List<ElementoPianoDeiConti> result = new ArrayList<ElementoPianoDeiConti>();
		
		try {

			if (req == null) {
				res.addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
			} else {

				if (req.getAnno() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("anno"));
				}

				if (req.getIdEnteProprietario() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("IdEnteProprietario"));
				}
					
//				if (req.getFamigliaTreeCodice() == null)
//					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
//							.getErrore("IdFamigliaTree"));

				if (req.getIdCodificaPadre() == 0){
					res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore("IdCodificaPadre"));
				}
					
			}
			res.setRichiedente(req.getRichiedente());	
			if (!res.hasErrori()) {

				List<SiacTClass> dtos = codificaBilDao
						.findTreeByCodiceFamiglia(req.getAnno(),
								req.getIdEnteProprietario(),
								SiacDClassFamEnum.PianoDeiConti.getCodice(),
								req.getIdCodificaPadre(), false);

				if (dtos != null && !dtos.isEmpty()) {
					
					result = convertTreeDto(dtos, result,
							new ElementoPianoDeiConti());

				} else {
					res.addErrore(ErroreCore.ENTITA_NON_TROVATA
							.getErrore("elemento piano dei conti"));
					res.setEsito(Esito.FALLIMENTO);
				}

			} else{
				res.setEsito(Esito.FALLIMENTO);
			}
				
		} catch (Exception t) {
			//t.printStackTrace();
			log.error("findClassificatoriGenericiByTipoElementoBil errore generico.", t);
			res.addErroreDiSistema(t);
			res.setEsito(Esito.FALLIMENTO);
		}
		
		res.setCardinalitaComplessiva(result.size());
		res.setTreeElementoPianoDeiConti(result);
		return res;
	}

	/**
	 * Converte un tree di CodificaDto con famiglia
	 * StrutturaAmministrativoContabile in un tree di
	 * StrutturaAmministrativoContabile.
	 *
	 * @param dtos the dtos
	 * @param list the list
	 * @param padre the padre
	 * @return the list
	 */
	private List<ElementoPianoDeiConti> convertTreeDto(List<SiacTClass> dtos,
			List<ElementoPianoDeiConti> list, ElementoPianoDeiConti padre) {

		for (SiacTClass dto : dtos) {

			ElementoPianoDeiConti obj = new ElementoPianoDeiConti();

			obj.setUid(dto.getUid());
			obj.setCodice(dto.getClassifCode());
			obj.setDescrizione(dto.getClassifDesc());

			if (!dto.getSiacRClassFamTreesPadre().isEmpty()) {
				padre = obj;

				List<SiacRClassFamTree> siacRClassFamTreesPadre = new ArrayList<SiacRClassFamTree>(dto.getSiacRClassFamTreesPadre());
				List<SiacTClass> figli = new ArrayList<SiacTClass>();
				for(SiacRClassFamTree srcft : siacRClassFamTreesPadre) {
					if(srcft.getSiacTClassFiglio() != null) {
						figli.add(srcft.getSiacTClassFiglio());
					}
				}
				List<ElementoPianoDeiConti> elemPdc = new ArrayList<ElementoPianoDeiConti>();
				//elemPdc.addAll(convertTreeDto(figli, elemPdc, padre));
				elemPdc = convertTreeDto(figli, elemPdc, padre);
				padre.setElemPdc(elemPdc);
				list.add(padre);

			} else {
				list.add(obj);
			}

		}
		return list;
	}

	/**
	 * Gets the codifica bil dao.
	 *
	 * @return the codifica bil dao
	 */
	public CodificaBilDao getCodificaBilDao() {
		return codificaBilDao;
	}

	/**
	 * Sets the codifica bil dao.
	 *
	 * @param codificaBilDao the new codifica bil dao
	 */
	public void setCodificaBilDao(CodificaBilDao codificaBilDao) {
		this.codificaBilDao = codificaBilDao;
	}

}
