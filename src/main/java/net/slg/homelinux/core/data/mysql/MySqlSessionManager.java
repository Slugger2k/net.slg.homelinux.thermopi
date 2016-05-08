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
package net.slg.homelinux.core.data.mysql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlSessionManager {

    private static final String PROPERTIES = "thermopi.properties";
    private static MySqlSessionManager msm = new MySqlSessionManager();
    private Connection connect;

    private MySqlSessionManager() {
        // Not usable
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Singelton
    public static synchronized MySqlSessionManager getInstance() {
        return msm;
    }

    public Connection createSession() throws Exception {
        try {
            DriverManager.setLoginTimeout(5);
            Properties p = new Properties();
            try {
                p.load(new FileReader(new File(PROPERTIES)));
            } catch (FileNotFoundException e) {
                try {
                    InputStream stream = MySqlSessionManager.class.getResourceAsStream(PROPERTIES);
                    p.load(new InputStreamReader(stream));
                } catch (Exception ee) {
                    ee.printStackTrace();
                    throw e;
                }
            }

            connect = DriverManager.getConnection(p.getProperty("MySqlJdbc"), p.getProperty("MySqlUser"), p.getProperty("MySqlPass"));
            return connect;
        } catch (Exception e) {
            throw e;
        }
    }

    public void closeSession() throws Exception {
        try {
            if (getConnect().isValid(5)) {
                getConnect().close();
            }
        } catch (Exception e) {
            try {
                getConnect().close();
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw e1;
            }
            throw e;
        }
    }

    public Connection getConnect() throws Exception {
        if (connect == null) {
            return createSession();
        }
        try {
            if (connect.isValid(5)) {
                return connect;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return createSession();
        }
    }

}
