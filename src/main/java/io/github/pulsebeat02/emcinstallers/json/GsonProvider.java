package io.github.pulsebeat02.emcinstallers.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonProvider {

  private static final Gson GSON;

  static {
    GSON = getBuilder().create();
  }

  private static GsonBuilder getBuilder() {
    return new GsonBuilder();
  }

  public static Gson getGson() {
    return GSON;
  }
}
