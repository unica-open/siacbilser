/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.RisorsaAccantonata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipoVincolo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccommonser.util.dozer.MapId;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.model.TipoAvviso;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfinser.model.ElementoContoEconomico;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.CodiceBilancio;


/**
 * @author Domenico
 *
 */
@EnumEntity(entityName="SiacDClassTipo", idPropertyName="classifTipoId", codePropertyName="classifTipoCode")
public enum SiacDClassTipoEnum {
	
	//SpesaMissioniprogrammi			-> Missione, Programma
	Missione("MISSIONE", TipologiaClassificatore.MISSIONE,  Missione.class, SiacDClassFamEnum.SpesaMissioniprogrammi, null),
	
	//figlio di Missione
	Programma("PROGRAMMA", TipologiaClassificatore.PROGRAMMA, Programma.class, SiacDClassFamEnum.SpesaMissioniprogrammi, SiacDClassTipoEnum.Missione),
	
	//Cofog
	//figlio di Programma
    CofogDivisione("DIVISIONE_COFOG", TipologiaClassificatore.DIVISIONE_COFOG, ClassificazioneCofogProgramma.class, SiacDClassFamEnum.Cofog, null), //FIXME Aggiunto RTI
    CofogGruppo("GRUPPO_COFOG", TipologiaClassificatore.GRUPPO_COFOG, ClassificazioneCofogProgramma.class, SiacDClassFamEnum.Cofog, SiacDClassTipoEnum.CofogDivisione),
	CofogClasse("CLASSE_COFOG", TipologiaClassificatore.CLASSE_COFOG, ClassificazioneCofogProgramma.class, SiacDClassFamEnum.Cofog, SiacDClassTipoEnum.CofogGruppo),
	
	//SpesaTitolimacroaggregati			-> TitoloSpesa, Macroaggregato		  (solo Uscita)
	TitoloSpesa("TITOLO_SPESA", TipologiaClassificatore.TITOLO_SPESA, TitoloSpesa.class, SiacDClassFamEnum.SpesaTitolimacroaggregati, null),
	Macroaggregato("MACROAGGREGATO", TipologiaClassificatore.MACROAGGREGATO, Macroaggregato.class, SiacDClassFamEnum.SpesaTitolimacroaggregati, SiacDClassTipoEnum.TitoloSpesa),
	
	//EntrataTitolitipologiecategorie	-> TitoloEntrata, Tipologia, Categoria (solo Entrata)
	TitoloEntrata("TITOLO_ENTRATA", TipologiaClassificatore.TITOLO_ENTRATA, TitoloEntrata.class, SiacDClassFamEnum.EntrataTitolitipologiecategorie, null),		
	
	//figlio di TitoloEntrata
	Tipologia("TIPOLOGIA", TipologiaClassificatore.TIPOLOGIA, TipologiaTitolo.class, SiacDClassFamEnum.EntrataTitolitipologiecategorie, SiacDClassTipoEnum.TitoloEntrata), 
	//figlio di Tipologia
    Categoria("CATEGORIA", TipologiaClassificatore.CATEGORIA, CategoriaTipologiaTitolo.class, SiacDClassFamEnum.EntrataTitolitipologiecategorie, SiacDClassTipoEnum.Tipologia), 
	
	
	//StrutturaAmministrativaContabile 	-> Centro di Responsabilità, CDC (Settore)
	CentroDiResponsabilita("CDR", TipologiaClassificatore.CDR, StrutturaAmministrativoContabile.class,
			SiacDClassFamEnum.StrutturaAmministrativaContabile, null), // TOLTO DB MA SERVE!!!
    //figlio di CentroDiResponsabilità
	Cdc("CDC", TipologiaClassificatore.CDC, StrutturaAmministrativoContabile.class,
			SiacDClassFamEnum.StrutturaAmministrativaContabile, SiacDClassTipoEnum.CentroDiResponsabilita),

	//Assessorato(9, "ASS",  StrutturaAmministrativoContabile.class, SiacDClassFamEnum.StrutturaAmministrativaContabile),
	
	//PianoDeiConti			 			-> PrimoLivelloPDC,SecondoLivelloPDC,TerzoLivelloPDC...QuintoLivelloPDC
    @Deprecated	PianoDeiConti("PDC", TipologiaClassificatore.PDC,ElementoPianoDeiConti.class, SiacDClassFamEnum.PianoDeiConti, null),	//FIXME credo non sia utilizzato! presente solo perchè c'è anche su db
	PrimoLivelloPdc("PDC_I", TipologiaClassificatore.PDC_I,ElementoPianoDeiConti.class, SiacDClassFamEnum.PianoDeiConti, SiacDClassTipoEnum.PianoDeiConti),
	SecondoLivelloPdc("PDC_II", TipologiaClassificatore.PDC_II,ElementoPianoDeiConti.class, SiacDClassFamEnum.PianoDeiConti, SiacDClassTipoEnum.PrimoLivelloPdc),
	TerzoLivelloPdc("PDC_III", TipologiaClassificatore.PDC_III,ElementoPianoDeiConti.class, SiacDClassFamEnum.PianoDeiConti, SiacDClassTipoEnum.SecondoLivelloPdc),
	QuartoLivelloPdc("PDC_IV", TipologiaClassificatore.PDC_IV,ElementoPianoDeiConti.class, SiacDClassFamEnum.PianoDeiConti, SiacDClassTipoEnum.TerzoLivelloPdc),
	QuintoLivelloPdc("PDC_V", TipologiaClassificatore.PDC_V,ElementoPianoDeiConti.class, SiacDClassFamEnum.PianoDeiConti, SiacDClassTipoEnum.QuartoLivelloPdc),
	
	SiopeEntrata("SIOPE_ENTRATA", TipologiaClassificatore.SIOPE_ENTRATA, SiopeEntrata.class, SiacDClassFamEnum.SiopeEntrata, null),
	PrimoLivelloSiopeEntrata("SIOPE_ENTRATA_I", TipologiaClassificatore.SIOPE_ENTRATA_I, SiopeEntrata.class, SiacDClassFamEnum.SiopeEntrata, SiacDClassTipoEnum.SiopeEntrata),
	SecondoLivelloSiopeEntrata("SIOPE_ENTRATA_II", TipologiaClassificatore.SIOPE_ENTRATA_II, SiopeEntrata.class, SiacDClassFamEnum.SiopeEntrata, SiacDClassTipoEnum.PrimoLivelloSiopeEntrata),
	TerzoLivelloSiopeEntrata("SIOPE_ENTRATA_III", TipologiaClassificatore.SIOPE_ENTRATA_III, SiopeEntrata.class, SiacDClassFamEnum.SiopeEntrata, SiacDClassTipoEnum.SecondoLivelloSiopeEntrata),
	SiopeSpesa("SIOPE_SPESA", TipologiaClassificatore.SIOPE_SPESA, SiopeSpesa.class, SiacDClassFamEnum.SiopeSpesa, null),
	PrimoLivelloSiopeSpesa("SIOPE_SPESA_I", TipologiaClassificatore.SIOPE_SPESA_I, SiopeSpesa.class, SiacDClassFamEnum.SiopeSpesa, SiacDClassTipoEnum.SiopeSpesa),
	SecondoLivelloSiopeSpesa("SIOPE_SPESA_II", TipologiaClassificatore.SIOPE_SPESA_II, SiopeSpesa.class, SiacDClassFamEnum.SiopeSpesa, SiacDClassTipoEnum.PrimoLivelloSiopeSpesa),
	TerzoLivelloSiopeSpesa("SIOPE_SPESA_III", TipologiaClassificatore.SIOPE_SPESA_III, SiopeSpesa.class, SiacDClassFamEnum.SiopeSpesa, SiacDClassTipoEnum.SecondoLivelloSiopeSpesa),
	
	//Generici
	TipoFondo("TIPO_FONDO", TipologiaClassificatore.TIPO_FONDO, TipoFondo.class,null,null),
	TipoFinanziamento(TipologiaClassificatore.TIPO_FINANZIAMENTO.name(), TipologiaClassificatore.TIPO_FINANZIAMENTO, TipoFinanziamento.class,null,null),
	TipoVincolo("TPV", TipologiaClassificatore.TPV, TipoVincolo.class,null,null),
	RicorrenteSpesa("RICORRENTE_SPESA", TipologiaClassificatore.RICORRENTE_SPESA, RicorrenteSpesa.class,null,null),
	RicorrenteEntrata("RICORRENTE_ENTRATA", TipologiaClassificatore.RICORRENTE_ENTRATA, RicorrenteEntrata.class,null,null),
	PerimetroSanitarioSpesa("PERIMETRO_SANITARIO_SPESA", TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA, PerimetroSanitarioSpesa.class,null,null),
	PerimetroSanitarioEntrata("PERIMETRO_SANITARIO_ENTRATA", TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA, PerimetroSanitarioEntrata.class,null,null),
	TransazioneUnioneEuropeaEntrata("TRANSAZIONE_UE_ENTRATA", TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA, TransazioneUnioneEuropeaEntrata.class,null,null),
	TransazioneUnioneEuropeaSpesa("TRANSAZIONE_UE_SPESA", TipologiaClassificatore.TRANSAZIONE_UE_SPESA, TransazioneUnioneEuropeaSpesa.class,null,null),
	PoliticheRegionaliUnitarie("POLITICHE_REGIONALI_UNITARIE", TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE, PoliticheRegionaliUnitarie.class,null,null),
	TipoImpresa("TIPO_IMPRESA", TipologiaClassificatore.TIPO_IMPRESA, TipoImpresa.class,null,null),
	TipoAvviso("TIPO_AVVISO", TipologiaClassificatore.TIPO_AVVISO, TipoAvviso.class,null,null),
	
	Classificatore1("CLASSIFICATORE_1", TipologiaClassificatore.CLASSIFICATORE_1, ClassificatoreGenerico.class,null,null),
	Classificatore2("CLASSIFICATORE_2", TipologiaClassificatore.CLASSIFICATORE_2, ClassificatoreGenerico.class,null,null),
	Classificatore3("CLASSIFICATORE_3", TipologiaClassificatore.CLASSIFICATORE_3, ClassificatoreGenerico.class,null,null),
	Classificatore4("CLASSIFICATORE_4", TipologiaClassificatore.CLASSIFICATORE_4, ClassificatoreGenerico.class,null,null),
	Classificatore5("CLASSIFICATORE_5", TipologiaClassificatore.CLASSIFICATORE_5, ClassificatoreGenerico.class,null,null),
	Classificatore6("CLASSIFICATORE_6", TipologiaClassificatore.CLASSIFICATORE_6, ClassificatoreGenerico.class,null,null),
	Classificatore7("CLASSIFICATORE_7", TipologiaClassificatore.CLASSIFICATORE_7, ClassificatoreGenerico.class,null,null),
	Classificatore8("CLASSIFICATORE_8", TipologiaClassificatore.CLASSIFICATORE_8, ClassificatoreGenerico.class,null,null),
	Classificatore9("CLASSIFICATORE_9", TipologiaClassificatore.CLASSIFICATORE_9, ClassificatoreGenerico.class,null,null),
	Classificatore10("CLASSIFICATORE_10", TipologiaClassificatore.CLASSIFICATORE_10, ClassificatoreGenerico.class,null,null),
	
	Classificatore31("CLASSIFICATORE_31", TipologiaClassificatore.CLASSIFICATORE_31, ClassificatoreGenerico.class,null,null),
	Classificatore32("CLASSIFICATORE_32", TipologiaClassificatore.CLASSIFICATORE_32, ClassificatoreGenerico.class,null,null),
	Classificatore33("CLASSIFICATORE_33", TipologiaClassificatore.CLASSIFICATORE_33, ClassificatoreGenerico.class,null,null),
	Classificatore34("CLASSIFICATORE_34", TipologiaClassificatore.CLASSIFICATORE_34, ClassificatoreGenerico.class,null,null),
	Classificatore35("CLASSIFICATORE_35", TipologiaClassificatore.CLASSIFICATORE_35, ClassificatoreGenerico.class,null,null),
	Classificatore36("CLASSIFICATORE_36", TipologiaClassificatore.CLASSIFICATORE_36, ClassificatoreGenerico.class,null,null),
	Classificatore37("CLASSIFICATORE_37", TipologiaClassificatore.CLASSIFICATORE_37, ClassificatoreGenerico.class,null,null),
	Classificatore38("CLASSIFICATORE_38", TipologiaClassificatore.CLASSIFICATORE_38, ClassificatoreGenerico.class,null,null),
	Classificatore39("CLASSIFICATORE_39", TipologiaClassificatore.CLASSIFICATORE_39, ClassificatoreGenerico.class,null,null),
	Classificatore40("CLASSIFICATORE_40", TipologiaClassificatore.CLASSIFICATORE_40, ClassificatoreGenerico.class,null,null),
	Classificatore41("CLASSIFICATORE_41", TipologiaClassificatore.CLASSIFICATORE_41, ClassificatoreGenerico.class,null,null),
	Classificatore42("CLASSIFICATORE_42", TipologiaClassificatore.CLASSIFICATORE_42, ClassificatoreGenerico.class,null,null),
	Classificatore43("CLASSIFICATORE_43", TipologiaClassificatore.CLASSIFICATORE_43, ClassificatoreGenerico.class,null,null),
	Classificatore44("CLASSIFICATORE_44", TipologiaClassificatore.CLASSIFICATORE_44, ClassificatoreGenerico.class,null,null),
	Classificatore45("CLASSIFICATORE_45", TipologiaClassificatore.CLASSIFICATORE_45, ClassificatoreGenerico.class,null,null),
	Classificatore46("CLASSIFICATORE_46", TipologiaClassificatore.CLASSIFICATORE_46, ClassificatoreGenerico.class,null,null),
	Classificatore47("CLASSIFICATORE_47", TipologiaClassificatore.CLASSIFICATORE_47, ClassificatoreGenerico.class,null,null),
	Classificatore48("CLASSIFICATORE_48", TipologiaClassificatore.CLASSIFICATORE_48, ClassificatoreGenerico.class,null,null),
	Classificatore49("CLASSIFICATORE_49", TipologiaClassificatore.CLASSIFICATORE_49, ClassificatoreGenerico.class,null,null),
	Classificatore50("CLASSIFICATORE_50", TipologiaClassificatore.CLASSIFICATORE_50, ClassificatoreGenerico.class,null,null),
	Classificatore51("CLASSIFICATORE_51", TipologiaClassificatore.CLASSIFICATORE_51, ClassificatoreGenerico.class,null,null),
	Classificatore52("CLASSIFICATORE_52", TipologiaClassificatore.CLASSIFICATORE_52, ClassificatoreGenerico.class,null,null),
	Classificatore53("CLASSIFICATORE_53", TipologiaClassificatore.CLASSIFICATORE_53, ClassificatoreGenerico.class,null,null),
	//SIAC-7192
	RisorsaAccantonata("RISACC", TipologiaClassificatore.RISORSA_ACCANTONATA, RisorsaAccantonata.class,null,null),
	
	
	TipoAmbito("TIPO_AMBITO", TipologiaClassificatore.TIPO_AMBITO, TipoAmbito.class, null, null), 
	
	
	
	CodiceBilancioContoEconomico("CE_CODBIL", null/*TipologiaClassificatore.CODICE_BILANCIO_I*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioContoEconomico, null),
	CodiceBilancioStatoPatrimonialeAttivo("SPA_CODBIL", null/*TipologiaClassificatore.CODICE_BILANCIO_II*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioStatoPatrimonialeAttivo, null),  
	CodiceBilancioStatoPatrimonialePassivo("SPP_CODBIL", null/*TipologiaClassificatore.CODICE_BILANCIO_III*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioStatoPatrimonialePassivo, null),  
	CodiceBilancioContiDOrdine("CO_CODBIL", null/*TipologiaClassificatore.CODICE_BILANCIO_IV*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioContiDOrdine, null),  
	
	CodiceBilancioContoEconomicoGsa("CE_CODBIL_GSA", null/*TipologiaClassificatore.CODICE_BILANCIO_I*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioContoEconomicoGsa, null),
	CodiceBilancioStatoPatrimonialeAttivoGsa("SPA_CODBIL_GSA", null/*TipologiaClassificatore.CODICE_BILANCIO_II*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioStatoPatrimonialeAttivoGsa, null),  
	CodiceBilancioStatoPatrimonialePassivoGsa("SPP_CODBIL_GSA", null/*TipologiaClassificatore.CODICE_BILANCIO_III*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioStatoPatrimonialePassivoGsa, null),  
	CodiceBilancioContiDOrdineGsa("CO_CODBIL_GSA", null/*TipologiaClassificatore.CODICE_BILANCIO_IV*/, CodiceBilancio.class, SiacDClassFamEnum.CodiceBilancioContiDOrdine, null),  
	
	//PianoDeiContiFinanziario("PianoDeiContiFinanziario", null /*TipologiaClassificatore.TIPO_AMBITO*/, PianoDeiContiFinanziario.class, null, null), In realta' e' il PianoDeiConti
	
	ValoreBene("VALORE_BENE", TipologiaClassificatore.VALORE_BENE, ClassificatoreEP.class, null, null),
	ModalitaAcquisizioneBene("MODALITA_AQUISIZIONE_BENE", TipologiaClassificatore.MODALITA_AQUISIZIONE_BENE, ClassificatoreEP.class, null, null),
	TipoDocumentoCollegato("TIPO_DOCUMENTO_COLLEGATO", TipologiaClassificatore.TIPO_DOCUMENTO_COLLEGATO, ClassificatoreEP.class, null, null),
	//TipoOnereFiscale("TIPO_ONERE_FISCALE", TipologiaClassificatore.TIPO_ONERE_FISCALE, ClassificatoreEP.class, null, null),
	RilevanteIva("RILEVANTE_IVA", TipologiaClassificatore.RILEVANTE_IVA, ClassificatoreEP.class, null, null),
	
	//ContoEconomico (verra' sostituito da siac_t_pdce_conto "Lotto i")
	@Deprecated PrimoLivelloContoEconomico("PCE_I", TipologiaClassificatore.PCE_I, ElementoContoEconomico.class, SiacDClassFamEnum.ContoEconomico, null),
	@Deprecated SecondoLivelloContoEconomico("PCE_II", TipologiaClassificatore.PCE_II, ElementoContoEconomico.class, SiacDClassFamEnum.ContoEconomico, SiacDClassTipoEnum.PrimoLivelloContoEconomico),
	@Deprecated TerzoLivelloContoEconomico("PCE_III", TipologiaClassificatore.PCE_III, ElementoContoEconomico.class, SiacDClassFamEnum.ContoEconomico, SiacDClassTipoEnum.SecondoLivelloContoEconomico),
	@Deprecated QuartoLivelloContoEconomico("PCE_IV", TipologiaClassificatore.PCE_IV, ElementoContoEconomico.class, SiacDClassFamEnum.ContoEconomico, SiacDClassTipoEnum.TerzoLivelloContoEconomico),
	@Deprecated QuintoLivelloContoEconomico("PCE_V", TipologiaClassificatore.PCE_V, ElementoContoEconomico.class, SiacDClassFamEnum.ContoEconomico, SiacDClassTipoEnum.QuartoLivelloContoEconomico),

	ContoCorrentePredoc("CBPI", TipologiaClassificatore.CONTO_CORRENTE_PREDISPOSIZIONE_INCASSO, ContoCorrentePredocumentoEntrata.class, null, null),
	ClassificatoreStipendi("CLASSIFICATORE_STIPENDI", TipologiaClassificatore.CLASSIFICATORE_STIPENDI, ClassificatoreStipendi.class, null, null),
	
	;

	/** The codice. */
	private final String codice;
	
	/** The codifica class. */
	private final Class<? extends Codifica> codificaClass;
	
	/** The famiglia. */
	private final SiacDClassFamEnum famiglia;
	
	/** The tipologia classificatore. */
	private final TipologiaClassificatore tipologiaClassificatore;
	
	/** The class tipo padre. */
	private final SiacDClassTipoEnum classTipoPadre;
		

	/**
	 * Instantiates a new siac d class tipo enum.
	 *
	 * @param codice the codice
	 * @param tipologiaClassificatore the tipologia classificatore
	 * @param codificaClass the codifica class
	 * @param famiglia the famiglia
	 * @param classTipoPadre the class tipo padre
	 */
	SiacDClassTipoEnum(String codice, TipologiaClassificatore tipologiaClassificatore,  
			Class<? extends Codifica> codificaClass, SiacDClassFamEnum famiglia, SiacDClassTipoEnum classTipoPadre){
		//this.id = id;
		this.codice = codice;
		this.tipologiaClassificatore = tipologiaClassificatore;
		this.codificaClass = codificaClass;
		this.famiglia = famiglia;
		this.classTipoPadre = classTipoPadre;
	}


	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
		
	/**
	 * Gets the famiglia.
	 *
	 * @return the famiglia
	 */
	public SiacDClassFamEnum getFamiglia() {
		return famiglia;
	}
	
	/**
	 * By codice.
	 *
	 * @param codices the codices
	 * @return the siac d class tipo enums
	 */
	public static Set<SiacDClassTipoEnum> byCodice(Collection<String> codices){
		Set<SiacDClassTipoEnum> result = EnumSet.noneOf(SiacDClassTipoEnum.class);
		
		for(String codice : codices){
			result.add(byCodice(codice));
		}
		return result;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d class tipo enum
	 */
	public static SiacDClassTipoEnum byCodice(String codice){
		for(SiacDClassTipoEnum e : SiacDClassTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDClassTipoEnum");
	}
	
	/**
	 * By codice even null.
	 *
	 * @param codice the codice
	 * @return the siac d class tipo enum
	 */
	public static SiacDClassTipoEnum byCodiceEvenNull(String codice){		
		if(codice==null){
			return null;
		}
		
		return byCodice(codice);
	}
	
	/**
	 * By tipologia classificatore.
	 *
	 * @param tipologiaClassificatores the tipologia classificatores
	 * @return the siac d class tipo enums
	 */
	public static Set<SiacDClassTipoEnum> byTipologiaClassificatore(Collection<TipologiaClassificatore> tipologiaClassificatores){
		Set<SiacDClassTipoEnum> result = EnumSet.noneOf(SiacDClassTipoEnum.class);
		
		for(TipologiaClassificatore tipologiaClassificatore : tipologiaClassificatores){
			result.add(byTipologiaClassificatore(tipologiaClassificatore));
		}
		return result;
	}
	
	/**
	 * By tipologia classificatore.
	 *
	 * @param tipologiaClassificatore the tipologia classificatore
	 * @return the siac d class tipo enum
	 */
	public static SiacDClassTipoEnum byTipologiaClassificatore(TipologiaClassificatore tipologiaClassificatore){
		for(SiacDClassTipoEnum e : SiacDClassTipoEnum.values()){
			if(tipologiaClassificatore.equals(e.getTipologiaClassificatore())){
				return e;
			}
		}
		throw new IllegalArgumentException("La tipologia "+ tipologiaClassificatore + " non ha un mapping corrispondente in SiacDClassTipoEnum");
	}
	
	/**
	 * By tipologia classificatore even null.
	 *
	 * @param tipologiaClassificatore the tipologia classificatore
	 * @return the siac d class tipo enum
	 */
	public static SiacDClassTipoEnum byTipologiaClassificatoreEvenNull(TipologiaClassificatore tipologiaClassificatore){
		if(tipologiaClassificatore==null){
			return null;
		}
		
		return byTipologiaClassificatore(tipologiaClassificatore);
	}
	
	

	/**
	 * Gets the codifica class.
	 *
	 * @return the codifica class
	 */
	public Class<? extends Codifica> getCodificaClass() {
		return codificaClass;
	}
	
	/**
	 * Gets the codifica instance.
	 *
	 * @param <T> the generic type
	 * @return the codifica instance
	 */
	@SuppressWarnings("unchecked")
	public <T extends Codifica> T getCodificaInstance() {
		try {
			return (T) codificaClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Eccezione istanziamento SiacDClassTipo."+name()+"->"+codificaClass + " ["+codice+"]",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Impossibile accedere al costruttore vuoto di  "+codificaClass + " ["+codice+"]",e);
		}
	}
	
	/**
	 * determina se il classificatore è di tipoi gerarchico
	 * 
	 * @return true se il classificatore è di tipo gerarchico
	 */
	public boolean isGerarchico(){
		return ClassificatoreGerarchico.class.isAssignableFrom(codificaClass);
	}
	
	/**
	 * determina se il classificatore è di tipoi generico
	 * 
	 * @return true se il classificatore è di tipo generico
	 */
	public boolean isGenerico(){
		return !isGerarchico();
	}
	

	/**
	 * Gets the tipologia classificatore.
	 *
	 * @return the tipologiaClassificatore
	 */
	public TipologiaClassificatore getTipologiaClassificatore() {
		return tipologiaClassificatore;
	}

	/**
	 * Gets the class tipo padre.
	 *
	 * @return the classTipoPadre
	 */
	public SiacDClassTipoEnum getClassTipoPadre() {
		return classTipoPadre;
	}

	/**
	 * Gets the class tipo figlio.
	 *
	 * @return the class tipo figlio
	 */
	public SiacDClassTipoEnum getClassTipoFiglio() {
		
		for(SiacDClassTipoEnum tipo : SiacDClassTipoEnum.values()){
			if(this.equals(tipo.getClassTipoPadre())){
				return tipo;
			}
		}
		
		return null;
	}

	

	/**
	 * Ottiene il MapId in base al tipo di classificatore.
	 * 
	 * @return BilMapId.SiacTClass_ClassificatoreGenerico oppure BilMapId.SiacTClass_ClassificatoreGerarchico
	 */
	public MapId getMapId(){
		Codifica ci = getCodificaInstance();		
		if(ci instanceof ClassificatoreGenerico){
			return BilMapId.SiacTClass_ClassificatoreGenerico;
		} else if(ci instanceof ClassificatoreGerarchico){
			return BilMapId.SiacTClass_ClassificatoreGerarchico;
		} else {
			throw new IllegalArgumentException("Il tipo di classificatore "+this.name()+" non ha un mapping id noto.");
		}
	}
	
	
	
	
	

}
