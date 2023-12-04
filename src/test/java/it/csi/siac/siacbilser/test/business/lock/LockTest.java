/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.lock;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.ServiceBaseJunit4TestCase;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.UploadFile;
import it.csi.siac.siacfin2ser.frontend.webservice.TestService;

public class LockTest extends ServiceBaseJunit4TestCase {
	
	
//	@Autowired
//	ApplicationContext appCtx;
	
//	@Autowired
//	private Test1Service test1Service;
//	
//	@Autowired
//	private Test2Service test2Service;
	
	@Autowired
	private TestService testService;
	
	
	@Test
	public void test(){
		final UploadFile req = new UploadFile();
		req.setRichiedente(getRichiedenteTest());
		
		Thread t1 = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					//test1Service.executeService(req);
					testService.test1(req);
				} catch (Exception e) {
					System.out.println("Fallito Service 1");
					e.printStackTrace();
				}

			}
		},"Service1");
		
		Thread t2 =  new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					//test2Service.executeService(req);
					testService.test2(req);
				} catch (Exception e) {
					System.out.println("Fallito Service 2");
					e.printStackTrace();
				}
			}
		},"Service2");
		
	
		t1.start();
		t2.start();
		
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("DONE!!");
		
	}
	
	
	
	
	
}
