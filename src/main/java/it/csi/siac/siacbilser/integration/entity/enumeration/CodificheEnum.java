/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.entity.SiacDAccreditoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleSospensione;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoriaCalcoloTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacDEventoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDPccCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDPccDebitoStato;
import it.csi.siac.siacbilser.integration.entity.SiacDPccOperazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDPdceContoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDPdceFam;
import it.csi.siac.siacbilser.integration.entity.SiacDPdceRelTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaRelTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaAffidamento;
import it.csi.siac.siacbilser.integration.entity.SiacDRichiestaEconTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeDocumentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeDocumentoTipoAnalogico;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeScadenzaMotivo;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeTipoDebito;
import it.csi.siac.siacbilser.integration.entity.SiacDSommaNonSoggettaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDTrasportoMezzo;
import it.csi.siac.siacbilser.integration.entity.SiacDValuta;
import it.csi.siac.siacbilser.integration.entity.SiacDVincoloGenere;
import it.csi.siac.siacbilser.integration.entity.SiacDVincoloRisorseVincolate;
import it.csi.siac.siacbilser.integration.entity.SiacRAccreditoTipoCassaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.model.GenereVincolo;
import it.csi.siac.siacbilser.model.RisorsaVincolata;
import it.csi.siac.siacbilser.model.ModalitaAffidamentoProgetto;
import it.csi.siac.siaccecser.model.MezziDiTrasporto;
import it.csi.siac.siaccecser.model.ModalitaAccreditoCassaEconomale;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccespser.model.CategoriaCalcoloTipoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccommonser.util.dozer.MapId;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.CausaleSospensione;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.siopeplus.SiopeScadenzaMotivo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoConto;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.TipoLegame;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * Descrive il mapping dell codifiche.
 * 
 * @author Domenico Lisi
 */
public enum CodificheEnum {
	
	TipoDocumento(TipoDocumento.class ,SiacDDocTipo.class, "docTipoCode" , "docTipoDesc"),
	AttoAmministrativoTipo(TipoAtto.class,SiacDAttoAmmTipo.class, "attoammTipoCode", "attoammTipoDesc"),
	ModalitaAccreditoSoggetto(ModalitaAccreditoSoggetto.class, SiacDAccreditoTipo.class, "accreditoTipoCode", "accreditoTipoDesc"),
	Valuta(Valuta.class, SiacDValuta.class, "valutaCode", BilMapId.SiacDValuta_Valuta),
	
	//Cassa Economale
	MezziDiTrasporto(MezziDiTrasporto.class, SiacDTrasportoMezzo.class, "mtraCode", /*"mtraDesc",*/ CecMapId.SiacDTrasportoMezzo_MezziDiTrasporto),
	TipoGiustificativo(TipoGiustificativo.class, SiacDGiustificativoTipo.class, "giustTipoCode", "giustTipoDesc"),
	TipoRichiestaEconomale(TipoRichiestaEconomale.class, SiacDRichiestaEconTipo.class, "riceconTipoCode" ,CecMapId.SiacDRichiestaEconTipo_TipoRichiestaEconomale),
	ModalitaPagamentoDipendente(ModalitaPagamentoDipendente.class, SiacRAccreditoTipoCassaEcon.class, CecMapId.SiacRAccreditoTipoCassaEcon_ModalitaPagamentoDipendente, ""),
	ModalitaAccreditoCassaEconomale(ModalitaAccreditoCassaEconomale.class, SiacDAccreditoTipo.class, "accreditoTipoCode", "accreditoTipoDesc"),
	
	//Contabilita Generale
	TipoConto(TipoConto.class, SiacDPdceContoTipo.class, GenMapId.SiacDPdceContoTipo_TipoConto),
	
	CategoriaCespiti(CategoriaCespiti.class, SiacDCespitiCategoria.class, CespMapId.SiacDCespitiCategoria_CategoriaCespiti_Minimal, 
			" AND siacDAmbito.ambitoCode = '"+SiacDAmbitoEnum.AmbitoFin.getCodice()+"' ", " ORDER BY cescatCode" ),
	
	TipoLegame(TipoLegame.class, SiacDPdceRelTipo.class, "pdcerelCode", "pdcerelDesc"),
	
	//NOTA: L'ordine è importante!! Va messa per prima quella che rappresenta il default per la ricerca byModelClass
	ClassePiano(ClassePiano.class, SiacDPdceFam.class,GenMapId.SiacDPdceFam_ClassePiano), 
	ClassePiano_GSA(ClassePiano.class, SiacDPdceFam.class,GenMapId.SiacDPdceFam_ClassePiano, "AND siacDAmbito.ambitoCode = '"+SiacDAmbitoEnum.AmbitoGsa.getCodice()+"'", "ORDER BY pdceFamCode"),
	ClassePiano_FIN(ClassePiano.class, SiacDPdceFam.class,GenMapId.SiacDPdceFam_ClassePiano, "AND siacDAmbito.ambitoCode = '"+SiacDAmbitoEnum.AmbitoFin.getCodice()+"'", "ORDER BY pdceFamCode"),
	ClassePiano_INV(ClassePiano.class, SiacDPdceFam.class,GenMapId.SiacDPdceFam_ClassePiano, "AND siacDAmbito.ambitoCode = '"+SiacDAmbitoEnum.AmbitoInv.getCodice()+"'", "ORDER BY pdceFamCode"),

	ClassePiano_CEC(ClassePiano.class, SiacDPdceFam.class,GenMapId.SiacDPdceFam_ClassePiano, "AND siacDAmbito.ambitoCode = '"+SiacDAmbitoEnum.AmbitoCec.getCodice()+"'", "ORDER BY pdceFamCode"), //non previsto per ora
	
	TipoEvento(TipoEvento.class, SiacDEventoTipo.class, "eventoTipoCode", "eventoTipoDesc", GenMapId.SiacDEventoTipo_TipoEvento, null, null),
	Evento(Evento.class, SiacDEvento.class, "eventoCode", "eventoDesc", GenMapId.SiacDEvento_Evento, null, "ORDER BY eventoCode"),
	TipoRelazionePrimaNota(TipoRelazionePrimaNota.class, SiacDPrimaNotaRelTipo.class, GenMapId.SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota),
	
	//PCC
	TipoOperazionePCC(TipoOperazionePCC.class, SiacDPccOperazioneTipo.class, BilMapId.SiacDPccOperazioneTipo_TipoOperazionePCC, "ORDER BY pccopTipoCode"),
	StatoDebito(StatoDebito.class, SiacDPccDebitoStato.class, BilMapId.SiacDPccDebitoStato_StatoDebito, "ORDER BY pccdebStatoCode"),
	CausalePCC(CausalePCC.class, SiacDPccCausale.class, BilMapId.SiacDPccCausale_CausalePCC, "ORDER BY pcccauCode"),
	
	CodiceSommaNonSoggetta(CodiceSommaNonSoggetta.class, SiacDSommaNonSoggettaTipo.class, BilMapId.SiacDSommaNonSoggettaTipo_CodiceSommaNonSoggetta),
	// SIAC-4679
	CausaleSospensione(CausaleSospensione.class, SiacDCausaleSospensione.class, "causSospCode", "causSospDesc"),
	// SIAC-5076
	GenereVincolo(GenereVincolo.class, SiacDVincoloGenere.class, "vincoloGenCode", "vincoloGenDesc"),
	
	// SIAC-7129
	RisorsaVincolata(RisorsaVincolata.class, SiacDVincoloRisorseVincolate.class, "vincoloRisorseVincolateCode", "vincoloRisorseVincolateDesc"),
	
	// SIAC-5311 SIOPE+
	SiopeDocumentoTipo(SiopeDocumentoTipo.class, SiacDSiopeDocumentoTipo.class, "siopeDocumentoTipoCode", "siopeDocumentoTipoDesc"),
	SiopeDocumentoTipoAnalogico(SiopeDocumentoTipoAnalogico.class, SiacDSiopeDocumentoTipoAnalogico.class, "siopeDocumentoTipoAnalogicoCode", "siopeDocumentoTipoAnalogicoDesc"),
	SiopeAssenzaMotivazione(SiopeAssenzaMotivazione.class, SiacDSiopeAssenzaMotivazione.class, "siopeAssenzaMotivazioneCode", "siopeAssenzaMotivazioneDesc"),
	SiopeScadenzaMotivo(SiopeScadenzaMotivo.class, SiacDSiopeScadenzaMotivo.class, "siopeScadenzaMotivoCode", "siopeScadenzaMotivoDesc"),
	SiopeTipoDebito(SiopeTipoDebito.class, SiacDSiopeTipoDebito.class, "siopeTipoDebitoCode", "siopeTipoDebitoDesc"),
	
	//CESPITI
	CategoriaCalcoloTipoCespite(CategoriaCalcoloTipoCespite.class, SiacDCespitiCategoriaCalcoloTipo.class, "cescatCalcoloTipoCode","cescatCalcoloTipoDesc"),
	//SIAC-6255
	ModalitaAffidamentoProgetto(ModalitaAffidamentoProgetto.class, SiacDProgrammaAffidamento.class, "programmaAffidamentoCode", "programmaAffidamentoDesc")
	;
	
	
	private final Class<? extends Codifica> modelClass;
	private final Class<? extends SiacTBase> entityClass;
	private final String codeColumnName;
	private final String descColumnName;
	private final String idColumnName;
	private final MapId mapId;
	private final String jpqlConditions;
	private final String jpqlOrderBy;
	
	/**
	 * 
	 * @param modelClass Classe di Model
	 * @param entityClass Classe di Entitty (es: SiacD*.class)
	 * @param codeColumnName nome jpql della colonna contenente il codice
	 * @param descColumnName nome jpql della colonna contenente la descrizione
	 * @param mapId mapId per il mapping di Dozer (facoltativo da utilizzare per casi in cui serva avere altri dati oltre codice e descrizione)
	 * @param jpqlConditions condizioni jpql aggiuntive (facoltativo) (es: "AND nomeColonna = 'A' ")
	 * @param jpqlOrderBy condizione jpql di order by (facoltativo, di default viene ordinato per codeColumnName) (es: "ORDER BY nomeColonnaA,nomeColonnaB)
	 */
	private CodificheEnum(Class<? extends Codifica> modelClass, Class<? extends SiacTBase> entityClass, String codeColumnName, String descColumnName,
			MapId mapId, String jpqlConditions, String jpqlOrderBy) {
		this.modelClass = modelClass;
		this.entityClass = entityClass;
		this.codeColumnName = codeColumnName;
		this.descColumnName = descColumnName;
		this.mapId = mapId;
		this.jpqlConditions = jpqlConditions != null ? jpqlConditions : "";
		this.jpqlOrderBy = StringUtils.isBlank(jpqlOrderBy) && StringUtils.isNotBlank(codeColumnName) ? "ORDER BY "+ codeColumnName : jpqlOrderBy;
		this.idColumnName = initIdColumnName();
	}

	private CodificheEnum(Class<? extends Codifica> modelClass, Class<? extends SiacTBase> entityClass, String codeColumnName, String descColumnName){
		this(modelClass,entityClass,codeColumnName,descColumnName,null,null,null);
	}
	
	private CodificheEnum(Class<? extends Codifica> modelClass, Class<? extends SiacTBase> entityClass, String codeColumnName, MapId mapId){
		this(modelClass,entityClass,codeColumnName,null,mapId,null,null);
	}
	
	private CodificheEnum(Class<? extends Codifica> modelClass, Class<? extends SiacTBase> entityClass, MapId mapId){
		this(modelClass,entityClass,null,null,mapId,null,"");
	}
	
	private CodificheEnum(Class<? extends Codifica> modelClass, Class<? extends SiacTBase> entityClass, MapId mapId, String jpqlOrderBy){
		this(modelClass,entityClass,null,null,mapId,null,jpqlOrderBy);
	}
	
	private CodificheEnum(Class<? extends Codifica> modelClass, Class<? extends SiacTBase> entityClass, MapId mapId, String jpqlConditions, String jpqlOrderBy){
		this(modelClass,entityClass,null,null,mapId,jpqlConditions,jpqlOrderBy);
	}
	
	
	
	/**
	 * By model class.
	 *
	 * @param modelClass the model class
	 * @return the codifiche enum
	 */
	public static CodificheEnum byModelClass(Class<? extends Codifica> modelClass){
		for(CodificheEnum ce : CodificheEnum.values()){
			if(ce.getModelClass().equals(modelClass)){
				return ce;
			}
		}
		throw new IllegalArgumentException("Codifica non supportata per la classe: "+ modelClass + ". Mapping non presente in CodificheEnum.");
	}
	
	/**
	 * By codifica name.
	 *
	 * @param codificaName the codifica name
	 * @return the codifiche enum
	 */
	public static CodificheEnum byCodificaName(String codificaName) {
		try {
			return CodificheEnum.valueOf(codificaName);
		} catch (IllegalArgumentException iae){
			throw new IllegalArgumentException("Codifica "+codificaName+" non supportata. Mapping non presente in CodificheEnum.");
		} catch (NullPointerException npe){
			throw new IllegalArgumentException("Codifica "+codificaName+" non supportata. Mapping non presente in CodificheEnum.");
		}
	}
	
	/**
	 * By model class and codifica name.
	 *
	 * @param modelClass the model class
	 * @param codificaName the codifica name
	 * @return the codifiche enum
	 */
	public static CodificheEnum byModelClassAndCodificaName(Class<? extends Codifica> modelClass, String codificaName) {
		
		//se NON ho codificaName uso la ricerca per modelClass
		if(StringUtils.isBlank(codificaName)){
			return byModelClass(modelClass);
		}
		
		//Altrimenti uso la ricerca per codificaName..	
		CodificheEnum ce = byCodificaName(codificaName);
		
		//..e verifico che se valorizzata la classe questa sia congruente per la codificaName specificata.
		if(modelClass!=null && !ce.getModelClass().isAssignableFrom(modelClass)) { //valutare se essere più restittivi usando equals!
			throw new IllegalArgumentException("La codifica "+ce.name()+" non e' congruente con la classe : "+ modelClass);
		}
		return ce;
		
	}
	
	@SuppressWarnings("unchecked")
	public <C extends Codifica> C newModelInstance() {
		try {
			return (C) modelClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Eccezione istanziamento Codifica "+name()+" -> "+modelClass + " ",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Impossibile accedere al costruttore vuoto di "+modelClass + " ["+name()+"] ",e);
		}
	}


	/**
	 * @return the modelClass
	 */
	public Class<? extends Codifica> getModelClass() {
		return modelClass;
	}

	/**
	 * @return the entityClass
	 */
	public Class<? extends SiacTBase> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return the codeColumnName
	 */
	public String getCodeColumnName() {
		return codeColumnName;
	}

	/**
	 * @return the descColumnName
	 */
	public String getDescColumnName() {
		return descColumnName;
	}
	
	/**
	 * @return the mapId
	 */
	public MapId getMapId() {
		return mapId;
	}
	
	public String getJpqlConditions() {
		return jpqlConditions;
	}


	public String getJpqlOrderBy() {
		return jpqlOrderBy;
	}
	
	/**
	 * @return the idColumnName
	 */
	public String getIdColumnName() {
		return idColumnName;
	}

	private String initIdColumnName() {
		Field[] fields = this.entityClass.getDeclaredFields();
		for(Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if(id != null) {
				return field.getName();
			}
		}
		return null;
	}

}
