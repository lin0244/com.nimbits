/*
 * Copyright (c) 2013 Nimbits Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.client.model.sync;

import com.nimbits.client.enums.EntityType;
import com.nimbits.client.enums.ProtectionLevel;
import com.nimbits.client.model.calculation.Calculation;
import com.nimbits.client.model.calculation.CalculationModel;
import com.nimbits.client.model.common.CommonIdentifier;
import com.nimbits.client.model.common.impl.CommonFactory;
import com.nimbits.client.model.entity.Entity;
import com.nimbits.client.model.entity.EntityName;
import com.nimbits.client.model.trigger.Trigger;
import com.nimbits.client.model.trigger.TriggerEntity;
import com.nimbits.client.model.trigger.TriggerModel;


public class SyncModel extends TriggerModel implements Sync {

    private String targetInstance;
    private String targetPoint;
    private String accessKey;

    public SyncModel(String key, CommonIdentifier name, String description, EntityType entityType, ProtectionLevel protectionLevel, String parent, String owner, String uuid, String target, String trigger, boolean enabled, String targetInstance, String targetPoint, String accessKey) {
        super(key, name, description, entityType, protectionLevel, parent, owner, uuid, target, trigger, enabled);
        this.targetInstance = targetInstance;
        this.targetPoint = targetPoint;
        this.accessKey = accessKey;
    }

    @Override
    public String getTargetInstance() {
        return targetInstance;
    }

    @Override
    public String getAccessKey() {
        return accessKey;
    }

    @Override
    public void setTargetInstance(String targetInstance) {
        this.targetInstance = targetInstance;
    }

    @Override
    public String getTargetPoint() {
        return targetPoint;
    }

    @Override
    public void setTargetPoint(String targetPoint) {
        this.targetPoint = targetPoint;
    }




    @SuppressWarnings("unused")
    public SyncModel() {
    }


    public static class Builder extends TriggerBuilder  {

        private String targetInstance;
        private String targetPoint;
        private String accessKey;




        public Builder targetInstance(String targetInstance) {
            this.targetInstance = targetInstance;
            return this;
        }

        public Builder setTargetPoint(String targetPoint) {
            this.targetPoint = targetPoint;
            return this;
        }

        public Builder accessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        @Override
        public Builder target(String v) {
            this.target = v;
            return this;
        }

        @Override
        public Builder trigger(String v) {
            this.trigger = v;
            return this;
        }

        @Override
        public Builder enabled(boolean v) {
            this.enabled = v;
            return this;
        }

        public Builder name(String name) {
            this.name = CommonFactory.createName(name, EntityType.sync);
            return this;
        }

        public Sync create() {
            if (protectionLevel == null) {
                protectionLevel = ProtectionLevel.everyone;
            }


            return new SyncModel(key, name, description, EntityType.sync, protectionLevel, parent, owner, uuid,target,
                    trigger, enabled, targetInstance, targetPoint, accessKey);
        }

        @Override
        public Builder parent(String parent) {

            this.parent = parent;
            return this;
        }


        @Override
        public Builder entityType(EntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        private void initEntity(Trigger anEntity) {
            this.trigger = anEntity.getTrigger();
            this.target = anEntity.getTarget();
            this.enabled = anEntity.isEnabled();

            this.key = anEntity.getKey();
            this.id = anEntity.getKey();
            this.name = anEntity.getName();
            this.description = anEntity.getDescription();
            this.entityType = anEntity.getEntityType();
            this.parent = anEntity.getParent();
            this.owner = anEntity.getOwner();
            this.protectionLevel = anEntity.getProtectionLevel();
            this.alertType = anEntity.getAlertType().getCode();
            this.uuid = anEntity.getUUID();

        }

        public Builder init(Sync c) {
            initEntity(c);
            targetInstance = c.getTargetInstance();
            targetPoint = c.getTargetPoint();
            accessKey = c.getAccessKey();
            return this;
        }

        @Override
        public Builder name(EntityName name) {
            this.name = name;
            return this;
        }
        @Override
        public Builder key(String key) {
            this.key = key;
            return this;
        }
        @Override
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        @Override
        public Builder protectionLevel(ProtectionLevel protectionLevel) {
            this.protectionLevel = protectionLevel;
            return this;
        }
        @Override
        public Builder alertType(int alertType) {
            this.alertType = alertType;
            return this;
        }
        @Override
        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }
        @Override
        public Builder readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }
        @Override
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        @Override
        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        @Override
        public Builder action(String action) {
            this.action = action;
            return this;
        }
    }


}
