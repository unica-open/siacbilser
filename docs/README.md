# Configurations
All configuration MUST be set in the `buildfiles/<env>.properties` file used for compilation
- nome.ambiente = the name of the environment
- flag-debug = the Java compiler flag to activate debug symbols (on/off)
- flag-optimize = the Java compiler flag to activate compile-time optimizations (on/off)
- flag-compress = the Java packager flag to activate packaging compressions for
    WAR/EAR/JAR files (on/off)
- current.env = the currently executing environment
- datasource.jndi-url = no more used. May be left to blank or to a default value
- messageSources.cacheSeconds = no more used. May be left to -1
- endpoint.url.service.core = Endpoint for the COR backend service
- endpoint.url.service.fin = Endpoint for the FIN backend service
- endpoint.url.service.rep = Endpoint for the REP backend service
- persistence.unit.showSql = Specifies whether the JPA-generated SQL should be logged
- persistence.unit.formatSql = Specifies whether the JPA-generated SQL should be formatted
- persistence.unit.use_get_generated_keys = Tells the JPA provider to retrieve the
    generated keys
- persistence.unit.use_jdbc_metadata_defaults = Tells the JPA provider not to connect to
    the database to retrieve metadata informations
- CONFIGURATION FOR INTEGRATION TO CSI AttiLiq
  - attiliqStartServlet.host = Hostname of the service
  - attiliqStartServlet.port = Port of the service
  - attiliqStartServlet.serviceUri = URI of the service (to be added to the host)
  - attiliqStartServlet.tokenRenewalUri = URI for the Token Renewal API
  - attiliqStartServlet.consumerKey = Key for the Token Renewal API
  - attiliqStartServlet.consumerSecret = Secret for the Token Renewal API
- CONFIGURATION FOT INTEGRATION TO CSI Marc
  - MARC.endpointAddress = Endpoint to the service
  - MARC.wsdlDocumentUrl = Service WSDL location
- CONFIGURATION FOR INTEGRATION TO CSI AppJ
  - APPJ.endpointAddress = Endpoint to the service
  - APPJ.wsdlDocumentUrl = Service WSDL location
  - APPJ.tokenRenewalUrl = URL for the Token Renewal API
  - APPJ.consumerKey = Key for the Token Renewal API
  - APPJ.consumerSecret = Secret for the Token Renewal API
- CONFIGURATION FOR INTEGRATION TO CSI HR
  - HR.endpointAddress = Endpoint to the service
  - HR.wsdlDocumentUrl = Service WSDL location
  - HR.tokenRenewalUrl = URL for the Token Renewal API
  - HR.consumerKey = Key for the Token Renewal API
  - HR.consumerSecret = Secret for the Token Renewal API
- CONFIGURATION FOR INTEGRATION TO CSI FEL
  - FEL.endpointAddress = Endpoint to the service
  - FEL.wsdlDocumentUrl = Service WSDL location
  - FEL.tokenRenewalUrl = URL for the Token Renewal API
  - FEL.consumerKey = Key for the Token Renewal API
  - FEL.consumerSecret = Secret for the Token Renewal API
  - FEL.appCode = caller application code
- CONFIGURATION FOR INTEGRATION TO CSI CPASS
  - CPASS.endpointAddress = Endpoint to the service
  - CPASS.wsdlDocumentUrl = Service WSDL location
  - CPASS.tokenRenewalUrl = URL for the Token Renewal API
  - CPASS.consumerKey = Key for the Token Renewal API
  - CPASS.consumerSecret = Secret for the Token Renewal API
- longExecutionTimeExecutor.poolSize = Pool size for the executor handling the
    (expected) long-running asynchronous elaborations
- asyncExecutor.poolSize = Pool size for the executor handling the asynchronous
    elaborations
