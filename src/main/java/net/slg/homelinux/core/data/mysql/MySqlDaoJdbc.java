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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.slg.homelinux.core.data.DataAccessObject;
import net.slg.homelinux.core.domain.TempData;

public class MySqlDaoJdbc implements DataAccessObject {

    private MySqlSessionManager instance = MySqlSessionManager.getInstance();
    private Connection connect;

    public MySqlDaoJdbc() throws Exception {
        instance.createSession();
        connect = instance.getConnect();
    }

    public List<TempData> getData(String sql) throws Exception {
        List<TempData> list = new ArrayList<TempData>();

        try {
            if (connect.isValid(3)) {
                Statement statement = connect.createStatement();
                ResultSet executeQuery = statement.executeQuery(sql);

                while (executeQuery.next()) {
                    TempData data = new TempData();

                    int id = executeQuery.getInt("id");
                    Timestamp ts = executeQuery.getTimestamp("date");
                    double temp1 = executeQuery.getDouble("temp1");
                    double temp2 = executeQuery.getDouble("temp2");
                    double temp3 = executeQuery.getDouble("temp3");
                    double hum1 = executeQuery.getDouble("hum1");

                    data.setId(id);
                    data.setTs(ts);
                    data.setDatum(new java.sql.Date(ts.getTime()));
                    data.setTemp1(temp1);
                    data.setTemp2(temp2);
                    data.setTemp3(temp3);
                    data.setHuminity(hum1);
                    list.add(data);
                }
            } else {
                instance.createSession();
            }
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public int getLastId() throws Exception {
        String sql = "select count(*) as cnt from temperatur";

        try {
            if (connect.isValid(3)) {
                Statement statement = connect.createStatement();
                ResultSet executeQuery = statement.executeQuery(sql);

                executeQuery.next();
                int anz = executeQuery.getInt("cnt");

                return anz;
            }
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }

}
