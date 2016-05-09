#ThermoPi JavaFX ChartClient

Example JavaFX ChartClient.
JavaFX Client for showing temperature values in a chart.  
For DataSource MySQL Database or REST-WebService can be used.


##Build with maven:
```
mvn clean package
```

##Properties File: (thermopi.properties)

```
##############################################
##          ThermoPi Config File     ##
##############################################

## MySQL config
MySqlJdbc=jdbc:mysql://127.0.0.1/thermopi
MySqlUser=thermopi
MySqlPass=password
```


##Usage:
```
java -jar ThermoPi-0.1.5.jar ScreenSize 
java -jar ThermoPi-0.1.5.jar 800x600
```


##License:
```
Copyright 2016 Christian Mueller  (christian.muell3r@gmail.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
