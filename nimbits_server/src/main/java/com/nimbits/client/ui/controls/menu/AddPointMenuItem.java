/*
 * Copyright 2016 Benjamin Sautner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.nimbits.client.ui.controls.menu;

import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.nimbits.client.ui.icons.Icons;


public class AddPointMenuItem extends MenuItem {


    private static final String DATA_POINT = "New Data Point";

    public AddPointMenuItem() {
        super(DATA_POINT);
        super.setToolTip(DATA_POINT);
        super.setIcon((Icons.INSTANCE.addNew()));

    }


}