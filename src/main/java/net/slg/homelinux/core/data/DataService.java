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
package net.slg.homelinux.core.data;

import java.util.List;

import net.slg.homelinux.core.data.mysql.MySqlDaoJdbc;
import net.slg.homelinux.core.data.rest.RestDao;
import net.slg.homelinux.core.domain.TempData;

public class DataService {
    

    public DataService() {
    }
    
    public List<TempData> getLastData(boolean db, int lastCnt) throws Exception {
        DataAccessObject dao;
        List<TempData> data;
        
        if (db) {
            dao = new MySqlDaoJdbc();
            int anzRows = dao.getLastId();
            data = dao.getData("select * from temperatur order by id asc limit " + (anzRows - lastCnt) + ", " + anzRows);
        } else {
            dao = new RestDao();
            data = dao.getData("http://slg.homelinux.net/last?cnt=" + lastCnt);
        }
        return data;
    }
    

}
