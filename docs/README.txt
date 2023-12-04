------------------------
SIAC Bil Server Impl
------------------------


Ambiente di Dev
-----------------
- Database
USER_CONN:    jdbc:postgresql://tst-domdb46.csi.it:5432/PGDEV02
USER_AUTH:    siac/xefai2xa 

- App server
Il dominio deve essere avviato seguendo i passi indicati nella home utente
Collegarsi via ssh ai server: dev-spjb601-mst01.self.csi.it (master) -- dev-spjb601-sl01.self.csi.it (slave1)
con i seguenti parametri per la gestione:
Utenza Unix Jboss:               dev-jboss601-005
Password Unix Jboss:             ait5iegh
Nome Dominio:                    dom005
Dopo aver deployato un applicativo la visualizzazione sara' possibile tramite i seguenti url:
Url istanza1 ( default ):    http://dev-spjb601-sl01.self.csi.it:12010/<applicativo>

LOAD Dependencies
-------------------
Dopo aver scaricato i sorgenti dal repository di subversion:
   - eseguire il target load-local-dependencies di build.xml e fare il refresh del progetto
   - in Project Properties -> Java Build Path -> Libraries aggiungere 
     tutti i jar delle sottocartelle di target/lib alle librerie del progetto
   - in Project Properties -> Java Build Path -> Source, impostare come sorgenti le seguenti cartelle:
       * src/main/java
       * src/main/resources
       * src/test/java
       * src/test/resources
