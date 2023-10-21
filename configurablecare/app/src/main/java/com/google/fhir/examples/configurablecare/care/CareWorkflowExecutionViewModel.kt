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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.fhir.examples.configurablecare.FhirApplication
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.ResourceType
import org.hl7.fhir.r4.model.Task

/** TODO(mjajoo@): Explore if applying PlanDefinition could be done in background. */
class CareWorkflowExecutionViewModel(application: Application) : AndroidViewModel(application) {
  private val fhirEngine =
    FhirApplication.fhirEngine(getApplication<Application>().applicationContext)
  private val carePlanManager =
    FhirApplication.carePlanManager(getApplication<Application>().applicationContext)
  private lateinit var activeRequestResourceConfiguration: List<RequestResourceConfig>
  lateinit var currentPlanDefinitionId: String

  /**
   * Shared flow of [CareWorkflowExecutionRequest]. For each collected patient the execution shall
   * run in blocking mode as asynchronous execution is resource exhaustive. extraBufferCapacity > 0
   * so that pending executions are collected properly.
   */
  // Having a map of patients is better ? Check TVPF.updateWorkflowExecutionBar
  // replay to 5 is not good since for multiple patients will be in queue. map makes lot more sense
  val patientFlowForCareWorkflowExecution =
    MutableSharedFlow<CareWorkflowExecutionRequest>(replay = 5)
  private var totalPlanDefinitionsToApply = AtomicInteger(0)
  private var totalPlanDefinitionsApplied = AtomicInteger(0)

  init {
    /**
     * [patientFlowForCareWorkflowExecution] collects each patient in a coroutine and executes
     * workflow blocking. This can be invoked when there is an operation on a Patient or some Task
     * is updated.
     */
    viewModelScope.launch(Dispatchers.IO) {
      patientFlowForCareWorkflowExecution.collect { careWorkflowExecutionRequest ->
        if (careWorkflowExecutionRequest.careWorkflowExecutionStatus
            is CareWorkflowExecutionStatus.Finished
        )
          return@collect
        /**
         * runBlocking because we want to run care workflows sequentially to avoid resource
         * exhaustion.
         */
        runBlocking {
          carePlanManager.applyPlanDefinitionOnPatient(
            currentPlanDefinitionId,
            careWorkflowExecutionRequest.patient,
            getActiveRequestResourceConfiguration()
          )
        }
        patientFlowForCareWorkflowExecution.emit(
          CareWorkflowExecutionRequest(
            careWorkflowExecutionRequest.patient,
            CareWorkflowExecutionStatus.Finished(
              totalPlanDefinitionsApplied.incrementAndGet(),
              totalPlanDefinitionsToApply.get()
            )
          )
        )
      }
    }
  }
  fun executeCareWorkflowForPatient(patient: Patient) {
    viewModelScope.launch {
      patientFlowForCareWorkflowExecution.emit(
        CareWorkflowExecutionRequest(
          patient,
          CareWorkflowExecutionStatus.Started(totalPlanDefinitionsToApply.incrementAndGet())
        )
      )
    }
  }
  /**
   * Updating task statuses should be done in scope of [CareWorkflowExecutionViewModel] under
   * activity context. Also re-triggering of [PlanDefinition].apply is done here by fetching the
   * [Patient] from FhirEngine. Update: Updating tasks could also happen in background!
   */
  fun updateTaskAndCarePlanStatus(
    taskLogicalId: String,
    taskStatus: Task.TaskStatus,
    encounterReferences: List<Reference>,
    updateCarePlan: Boolean,
  ) {
    viewModelScope.launch {
      val task = fhirEngine.get(ResourceType.Task, taskLogicalId) as Task
      val taskPatient =
        fhirEngine.get(ResourceType.Patient, task.`for`.reference.substring("Patient/".length))
          as Patient
      carePlanManager.updateCarePlanActivity(
        task,
        taskStatus.toString(),
        encounterReferences,
        updateCarePlan
      )
      executeCareWorkflowForPatient(taskPatient)
    }
  }

  fun setActiveRequestResourceConfiguration(planDefinitionUrl: String) {
    activeRequestResourceConfiguration =
      ConfigurationManager.careConfiguration
        ?.supportedImplementationGuides
        ?.firstOrNull { it.implementationGuideConfig.entryPoint == planDefinitionUrl }
        ?.implementationGuideConfig
        ?.requestResourceConfigurations!!
  }

  fun getActiveRequestResourceConfiguration(): List<RequestResourceConfig> {
    return activeRequestResourceConfiguration
  }
}
