package com.google.looker.beam.io;

import com.google.looker.ApiClient;
import com.google.looker.ApiException;
import com.google.lookerx.ApiClientConfigReader;
import com.google.lookerx.ApiClientFactory;
import com.google.lookerx.secrets.googlecloud.SecretManager;
import java.io.IOException;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;

public abstract class LookerIO<InputT extends PInput, OutputT extends POutput> extends PTransform<InputT, OutputT> {

  private String googleSecretName;
  private String googleProjectId;

  public LookerIO(String project, String secretName) {
    this.googleSecretName = secretName;
    this.googleProjectId = project;
  }

  public String getGoogleSecretName() {
    return googleSecretName;
  }

  public void setGoogleSecretName(String googleSecretName) {
    this.googleSecretName = googleSecretName;
  }

  public String getGoogleProjectId() {
    return googleProjectId;
  }

  public void setGoogleProjectId(String googleProjectId) {
    this.googleProjectId = googleProjectId;
  }

  public ApiClient getApiClient() throws IOException, ApiException {
    SecretManager.Reader secretReader = SecretManager.reader(getGoogleProjectId(),getGoogleSecretName());
    ApiClientConfigReader apiClientConfigReader = ApiClientConfigReader.from(secretReader);
    ApiClient apiClient = ApiClientFactory.from(apiClientConfigReader).create();

    return apiClient;
  }

}
