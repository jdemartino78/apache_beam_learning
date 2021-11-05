package com.google.looker.beam.transforms;

import com.google.auto.value.AutoValue;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Log is a utility class for logging
 */
public class Log implements Serializable {
  public enum Severity {
    INFO,
    ERROR,
  }
  public static <T> Writer<T> info(String name) {
    return new AutoValue_Log_Writer.Builder<T>()
        .setName(name)
        .setSeverity(Severity.INFO)
        .build();
  }
  public static <T> Writer<T> error(String name) {
    return new AutoValue_Log_Writer.Builder<T>()
        .setName(name)
        .setSeverity(Severity.ERROR)
        .build();
  }
  @AutoValue
  public static abstract class Writer<T> extends PTransform<PCollection<T>, PCollection<Void>> {
    public abstract Severity getSeverity();
    public abstract String getName();

    @Override
    public PCollection<Void> expand(PCollection<T> input) {
      return input.apply(ParDo.of(new DoFn<T, Void>() {
        private Logger logger;
        @Setup
        public void setup() {
          logger = LoggerFactory.getLogger(getName());
        }

        @ProcessElement
        public void process(ProcessContext context) {
          Severity severity = getSeverity();
          if (severity.equals(Severity.ERROR)) {
            logger.error("{}: {}", getName(), context.element());
            return;
          }
          logger.info("{}: {}", getName(), context.element());
        }
      }));
    }

    @AutoValue.Builder
    public static abstract class Builder<T> {
      public abstract Builder<T> setName(String name);
      public abstract Builder<T> setSeverity(Severity severity);
      public abstract Writer<T> build();
    }
  }
}