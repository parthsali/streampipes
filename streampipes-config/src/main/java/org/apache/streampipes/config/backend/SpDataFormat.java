/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.streampipes.config.backend;


import org.apache.streampipes.vocabulary.MessageFormat;

public enum SpDataFormat {

  CBOR("Cbor", MessageFormat.CBOR),
  JSON("JSON", MessageFormat.JSON),
  FST("Fast-Serializer", MessageFormat.FST),
  SMILE("Smile", MessageFormat.SMILE);

  private String name;
  private String messageFormat;

  SpDataFormat(String name, String messageFormat) {
    this.name = name;
    this.messageFormat = messageFormat;
  }

  public String getName() {
    return name;
  }

  public String getMessageFormat() {
    return messageFormat;
  }
}
