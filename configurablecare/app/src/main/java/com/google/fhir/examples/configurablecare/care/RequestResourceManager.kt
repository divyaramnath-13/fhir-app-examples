/*
 * Copyright 2022-2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.fhir.examples.configurablecare.care

import org.hl7.fhir.r4.model.CarePlan

interface RequestResourceManager<T> {

  suspend fun updateRequestResource(resource: T, requestResourceConfig: RequestResourceConfig): T

  suspend fun updateRequestResourceStatus(resource: T, status: String)

  fun mapRequestResourceStatusToCarePlanStatus(resource: T): CarePlan.CarePlanActivityStatus

  suspend fun linkCarePlanToRequestResource(resource: T, carePlan: CarePlan)

  suspend fun assignOwner(resource: T, ownerId: String)
}
