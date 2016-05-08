/*
 * Copyright 2016 Christian Mueller (christian.muell3r@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.slg.homelinux.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtil {

    
    private final static LoggerUtil instance = new LoggerUtil();

    private LoggerUtil() {
    }

    public static synchronized LoggerUtil getInstance() {
        return instance;
    }

    public String log(String message) {
        String log = getNow() + " : " + message;
        return log;
    }

    private String getNow() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(d);
    }

}
