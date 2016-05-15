/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package com.raxware.linkster;

import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BAbstractService;
import javax.baja.sys.BIcon;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

/**
 * A service that goes in the Services container and provides a means of automating
 * a many-to-many link creation.
 *
 * @author Will Chapman
 */

@NiagaraType
public class BLinksterService extends BAbstractService {

    public Type[] getServiceTypes() {
        return new Type[]{getType()};
    }

    public BIcon getIcon() {
        return BIcon.std("link.png");
    }


    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $com.raxware.linkster.BLinksterService(1239832666)1.0$ @*/
    /* Generated Sun Jul 27 20:12:50 EDT 2008 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */
////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
    public Type getType() {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BLinksterService.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
}
