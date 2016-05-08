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
package net.slg.homelinux.core.data.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import net.slg.homelinux.core.data.DataAccessObject;
import net.slg.homelinux.core.domain.TempData;

public class RestDao implements DataAccessObject {
    
    @Override
    public List<TempData> getData(String url) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        TempData[] responseEntity = restTemplate.getForObject(url, TempData[].class);
        
        List<TempData> list = Arrays.asList(responseEntity);
        
        return list;
    }

    @Override
    public int getLastId() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

}
