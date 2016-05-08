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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.slg.homelinux.core.domain.TempData;

public class Util {

    private static Util instance = new Util();
    private int cnt = 0;

    private Util() {
    }

    public static synchronized Util getInstance() {
        return instance;
    }

    public List<TempData> reduceData(List<TempData> data, int reduceFaktor) {

        data.sort(new Comparator<TempData>() {
            @Override
            public int compare(TempData o1, TempData o2) {
                return o1.getDatum().compareTo(o2.getDatum());
            }
        });

        cnt = 0;

        List<TempData> newList = new ArrayList<TempData>(10);

        data.forEach(d -> {
            if (cnt < reduceFaktor) {

            } else {
                newList.add(d);
                cnt = 0;
            }
            cnt++;
        });

        return newList;
    }

}
