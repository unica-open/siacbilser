/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.test;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleTipo;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.UploadFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.UploadFileResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Test1Service extends ExtendedBaseService<UploadFile, UploadFileResponse> {

//	@Autowired
//	private SiacDCausaleTipoRepository siacDCausaleTipoRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	@Transactional//(isolation=Isolation.SERIALIZABLE)
	public UploadFileResponse executeService(UploadFile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";
		
		Date now = new Date();
		sleep(1000);
		
		TransactionStatus currentTransactionStatus = TransactionAspectSupport.currentTransactionStatus();
		log.info(methodName, "isNewTransaction: " + currentTransactionStatus.isNewTransaction());
		
		//SiacDCausaleTipo c = siacDCausaleTipoRepository.findOne(1);
		SiacDCausaleTipo c = entityManager.find(SiacDCausaleTipo.class, 1);
		
		
		log.info(methodName, "Accesso in lettura: uid: "+ c.getUid()+ " code:" + c.getCausTipoCode() + " desc: "+c.getCausTipoDesc());
		String descNew = c.getCausTipoDesc() + " ["+  getServiceName() +"]";
		sleep(1001);
		
		c.setCausTipoDesc(descNew);
		c.setDataModifica(now);
		log.info(methodName, "Modificata causale desc: uid: "+ c.getUid()+ " code:" + c.getCausTipoCode() + " desc: "+c.getCausTipoDesc() + " now: "+now);
		//siacDCausaleTipoRepository.save(c);
		//entityManager.persist(c);
		
		
		sleep(1002);
		
		
		
		
		log.info(methodName, "Save: uid: "+ c.getUid()+ " code:" + c.getCausTipoCode() + " desc: "+c.getCausTipoDesc() + " now: "+now);
		log.info(methodName, "END!");
	}
	
	
	

	private void sleep(long millis) {
		String methodName = "sleep";
		try {
			log.info(methodName, "seleeping for " + millis + " millis");
			Thread.sleep(millis);
			log.info(methodName, "seleeping done!");
		} catch (InterruptedException e) {
			log.error(methodName, "Interrupted!", e);
		}
	}

}
