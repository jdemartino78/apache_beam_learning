package com.google.looker.beam.io;

import com.google.looker.ApiClient;
import com.google.looker.ApiException;
import com.google.looker.api.FolderApi;
import com.google.looker.model.Folder;
import java.io.IOException;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Impulse;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;

public class Folders extends LookerIO<PBegin, PCollection<Folder>>{

  public Folders(String project, String secretName) {
    super(project, secretName);
  }

  public static Folders read(String project, String secretName) {
    return new Folders(project, secretName);
  }
  @Override
  public PCollection<Folder> expand(PBegin input) {
    return input.apply(Impulse.create()).apply(ParDo.of(new DoFn<byte[], Folder>() {
      @ProcessElement
      public void process(ProcessContext ctx) throws IOException, ApiException {
        ApiClient apiClient = getApiClient();
        FolderApi api = new FolderApi(apiClient);
        for(Folder f: api.allFolders("")) {
          ctx.output(f);
        }
      }
    }));
  }
}
