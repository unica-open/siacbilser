/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.errore.ErroreCruscotto;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

/**
 * The Class ErroriMessaggiTest. Classe che logga i codici di errore (anche qelli duplicati).
 * Creata in seguito a richiesta ENG di sapere quali codici
 * @author elisa
 * @version 1.0.0 - 08-08-2019
 */
public class ErroriMessaggiTest extends BaseJunit4TestCase {
	
	
	public static void main(String[] args) {
		analizzaErrori();
		analizzaMessaggi();
	}


	/**
	 * Analizza messaggi contenuti in MessaggioBil.java, MessaggioCore.java, MessaggioInteg.java
	 * per ognuno, si prende il codice, si segna quali siano i codici suplicati e include 
	 * queste informazioni nella classe di wrapper  WrapperCodiciErrori.
	 * logga inoltre tutti i codici, il codice massimo diviso per tipologia (ERR, INFO ecc)
	 */
	private static void analizzaMessaggi() {
		MessaggioBil[] messaggioBils = MessaggioBil.values();
		MessaggioCore[] messaggioCores = MessaggioCore.values();
		MessaggioInteg[] messaggioIntegs = MessaggioInteg.values();
		
		ErroriMessaggiTest.WrapperCodiciErrori wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		
		
		for (MessaggioBil err : messaggioBils) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		for (MessaggioCore err : messaggioCores) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		for (MessaggioInteg err : messaggioIntegs) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
	}


	/**
	 * Analizza messaggi contenuti in MessaggioBil.java, MessaggioCore.java, MessaggioInteg.java
	 * per ognuno, si prende il codice, si segna quali siano i codici suplicati e include 
	 * queste informazioni nella classe di wrapper  WrapperCodiciErrori.
	 * logga inoltre tutti i codici, il codice massimo diviso per tipologia (ERR, INFO ecc)
	 */
	private static void analizzaErrori() {
		ErroreAtt[] erroreAtts = ErroreAtt.values();
		ErroreBil[] erroreBils = ErroreBil.values();
		ErroreCEC[] erroreCECs = ErroreCEC.values();
		it.csi.siac.siacfinser.model.errore.ErroreFin[] erroreFins = it.csi.siac.siacfinser.model.errore.ErroreFin.values();
		it.csi.siac.siacfin2ser.model.errore.ErroreFin[] erroreFins2 = it.csi.siac.siacfin2ser.model.errore.ErroreFin.values(); 
		ErroreGEN[] erroreGENs = ErroreGEN.values();
		ErroreCore[] erroreCores = ErroreCore.values();
		ErroreCruscotto[] erroreCruscottos = ErroreCruscotto.values();
		ErroriMessaggiTest.WrapperCodiciErrori wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		
				
		for (ErroreAtt err : erroreAtts) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
				
		for (ErroreBil err : erroreBils) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		for (ErroreCEC err : erroreCECs) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		
		
		for (ErroreFin err : erroreFins) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		
		
		for (it.csi.siac.siacfin2ser.model.errore.ErroreFin err : erroreFins2) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		
		
		for (ErroreGEN err : erroreGENs) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		
		
		for (ErroreCore err : erroreCores) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
		
		
		
		for (ErroreCruscotto err : erroreCruscottos) {
			wrapper.addCodice(err.getCodice());
		}
		
		wrapper.loggaAllCodici();
		wrapper.loggaMassimo();
		wrapper = new ErroriMessaggiTest().new WrapperCodiciErrori();
		System.out.println(" ------------------------------------------------------------ \n\n\n");
	}
	
	public class WrapperCodiciErrori {
		private List<String> codiceErrore = new ArrayList<String>();
		private List<String> codiciErroreDuplicati = new ArrayList<String>();
		
		public WrapperCodiciErrori() {
			this.codiceErrore = new ArrayList<String>();
			this.codiciErroreDuplicati = new ArrayList<String>();
		}
		
		
		public void addCodice(String cd) {
			if(codiceErrore.contains(cd)) {
				codiciErroreDuplicati.add(cd);
				return;
			}
			
			codiceErrore.add(cd);
		}
		
		public void loggaAllCodici() {
			loggaCodiciDuplicati();
//			loggaCodici();
		}
		
		public void loggaCodiciDuplicati() {
			Collections.sort(this.codiciErroreDuplicati);
			
			if(this.codiciErroreDuplicati.size() != 0) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
				System.out.println("Attenzione, i seguenti codici sono duplicati: ");
			}
			
			for (String codeDuplicati : this.codiciErroreDuplicati) {
				System.out.println(codeDuplicati);
			}
			
			System.out.println("-------------------------------");
		}
		
		private void loggaCodici() {
			Collections.sort(this.codiceErrore);
			
			for (String code : this.codiceErrore) {
				System.out.println(code);
			}
			System.out.println("-------------------------------n");
		}
		
		public void loggaMassimo() {
			Map<String, Map<String, String>> mappaErroreMassimo = new HashMap<String, Map<String, String>>();
			for (String code : this.codiceErrore) {
				String[] splitted = code.split("_|-");
				if(splitted == null || splitted.length <3) {
					System.out.println("non posso definire il massimo per il codice: " + code);
					continue;
				}
				String famigliaErrore = splitted[0];
				if(mappaErroreMassimo.get(famigliaErrore) == null) {
					mappaErroreMassimo.put(famigliaErrore, new HashMap<String, String>());
				}
				Map<String, String> mappaMassimo = mappaErroreMassimo.get(famigliaErrore);
				String tipoErrore = splitted[1];
				String massimoOld = mappaMassimo.get(tipoErrore);
				String massimoNew = splitted[2];
				mappaMassimo.put(tipoErrore, ottieniValoreMassimo(massimoOld, massimoNew));
				
			}
			
			StringBuilder sb = new StringBuilder();
			for (String famigliaErrore : mappaErroreMassimo.keySet()) {
				sb.append(" In una stessa classe di errore, per la famiglia ")
				.append(famigliaErrore)
				.append(" ho trovato i seguenti massimi:\n ");
				Map<String, String> mappaMassimo = mappaErroreMassimo.get(famigliaErrore);
				for (String key : mappaMassimo.keySet() ) {
					sb.append(famigliaErrore).append("_").append(key).append("_").append( mappaMassimo.get(key)).append("\n ");
				}
			}
			
			System.out.println(sb.toString());
					
		}

		
		public void loggaMassimoOld() {
			String famigliaErrore = null;
			Map<String, String> mappaMassimo = new HashMap<String, String>();
			for (String code : this.codiceErrore) {
				String[] splitted = code.split("_|-");
				if(splitted == null || splitted.length <3) {
					System.out.println("non posso definire il massimo per il codice: " + code);
					continue;
				}
				if(StringUtils.isBlank(famigliaErrore)) {
					famigliaErrore = splitted[0];
				}
				String tipoErrore = splitted[1];
				String massimoOld = mappaMassimo.get(tipoErrore);
				String massimoNew = splitted[2];
				mappaMassimo.put(tipoErrore, ottieniValoreMassimo(massimoOld, massimoNew));
				
			}
			StringBuilder sb = new StringBuilder()
					.append("Per la famiglia ")
					.append(famigliaErrore)
					.append(" ho trovato i seguenti massimi: ");
			for (String key : mappaMassimo.keySet()) {
				sb.append(key).append(" : ").append( mappaMassimo.get(key)).append(" , ");
			}
			
			System.out.println(sb.toString());
					
		}


		private String ottieniValoreMassimo(String massimoOld, String massimoNew) {
			
			if(StringUtils.isBlank(massimoOld) || getIntegerValue(massimoOld) == null) {
				return massimoNew;
			}
			if(StringUtils.isBlank(massimoNew)) {
				return massimoOld;
			}
			
			
			Integer massimoNewInteger =getIntegerValue(massimoNew);
			
			return massimoNewInteger != null && massimoNewInteger.compareTo(Integer.valueOf(massimoOld)) >0? massimoNew : massimoOld ;
		}
		
		public Integer getIntegerValue(String massimo) {
			Integer massimoInteger = null;
			try {
				massimoInteger = Integer.valueOf(massimo);
			} catch (NumberFormatException e) {
				System.out.println("Non posso ordinare il seguente valore: " + massimo);
			}
			return massimoInteger;
		}

	}
}
