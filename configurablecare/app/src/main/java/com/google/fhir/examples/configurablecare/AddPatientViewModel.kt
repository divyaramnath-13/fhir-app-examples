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
package com.google.fhir.examples.configurablecare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.datacapture.mapping.ResourceMapper
import com.google.android.fhir.datacapture.mapping.StructureMapExtractionContext
import com.google.android.fhir.datacapture.validation.Invalid
import com.google.android.fhir.datacapture.validation.QuestionnaireResponseValidator
import com.google.android.fhir.get
import com.google.android.fhir.testing.jsonParser
import java.util.UUID
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.ResourceType
import org.hl7.fhir.r4.model.StructureMap
import org.hl7.fhir.r4.utils.StructureMapUtilities

/** ViewModel for patient registration screen {@link AddPatientFragment}. */
class AddPatientViewModel(application: Application, private val state: SavedStateHandle) :
  AndroidViewModel(application) {

  val questionnaire: String
    get() = getQuestionnaireJson()
  val savedPatient = MutableLiveData<Patient?>()
  val context = application.applicationContext

  private val questionnaireResource: Questionnaire
    get() =
      FhirContext.forCached(FhirVersionEnum.R4).newJsonParser().parseResource(questionnaire)
        as Questionnaire
  private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)
  private var questionnaireJson: String? = null

  /**
   * Saves patient registration questionnaire response into the application database.
   *
   * @param questionnaireResponse patient registration questionnaire response
   */
  fun savePatient(questionnaireResponse: QuestionnaireResponse) {
    viewModelScope.launch {
      if (QuestionnaireResponseValidator.validateQuestionnaireResponse(
            questionnaireResource,
            questionnaireResponse,
            getApplication()
          )
          .values
          .flatten()
          .any { it is Invalid }
      ) {
        savedPatient.value = null
        return@launch
      }
      // val mappingResource = fhirEngine.get<StructureMap>("IMMZCQRToPatient")
      // mappingResource.
      // val mapping = jsonParser.encodeResourceToString(mappingResource)

      // val mapping = """
      //   map "http://fhir.org/guides/who/smart-immunization/StructureMap/IMMZCQRToPatient" = "IMMZCQRToPatient"
      //
      //   uses "http://hl7.org/fhir/StructureDefinition/QuestionnaireResponse" alias QResp as source
      //   uses "http://fhir.org/guides/who/smart-immunization/StructureDefinition/IMMZCRegisterClient" alias IMMZC as source
      //   uses "http://hl7.org/fhir/StructureDefinition/Patient" alias Patient as target
      //
      //   imports "http://fhir.org/guides/who/smart-immunization/StructureMap/IMMZCQRToLM"
      //   imports "http://fhir.org/guides/who/smart-immunization/StructureMap/IMMZCLMToPatient"
      //
      //   group QRestToIMMZC(source qr : QResp, target patient : Patient) {
      //     qr -> create('http://fhir.org/guides/who/smart-immunization/StructureDefinition/IMMZCRegisterClient') as model then {
      //       qr -> model then QRespToIMMZC(qr, model) "QRtoLM";
      //       qr -> patient then IMMZCToPatient(model, patient) "LMtoPatient";
      //     } "QRtoPatient";
      //   }
      //
      // """.trimIndent()
      // val bundle = ResourceMapper.extract(questionnaireResource,
      //                                     questionnaireResponse,
      //                                     StructureMapExtractionContext(context = context)
      //                                     { _, worker ->
      //                                       StructureMapUtilities(worker).parse(mapping, "")
      // })
      val bundle = ResourceMapper.extract(questionnaireResource, questionnaireResponse)
      var flag = false
      var patient: Patient
      for (entry in bundle.entry) {
        if (entry.resource is Patient) {
          patient = entry.resource as Patient
          patient.id = generateUuid()
          fhirEngine.create(patient)
          savedPatient.value = patient

          // create Immunization Review Task


          flag = true
        }
      }
      if (!flag)
        return@launch

      // patient = entry.resource as Patient
      // patient.id = generateUuid()
      // fhirEngine.create(patient)
      // savedPatient.value = patient
    }
  }

  private fun getQuestionnaireJson(): String {
    questionnaireJson?.let {
      return it
    }
    questionnaireJson = readFileFromAssets(state[AddPatientFragment.QUESTIONNAIRE_FILE_PATH_KEY]!!)
    return questionnaireJson!!
  }

  private fun readFileFromAssets(filename: String): String {
    return getApplication<Application>().assets.open(filename).bufferedReader().use {
      it.readText()
    }
  }

  private fun generateUuid(): String {
    return UUID.randomUUID().toString()
  }
}
