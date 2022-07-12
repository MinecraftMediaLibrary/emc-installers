package io.github.pulsebeat02.emcinstallers.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.pulsebeat02.emcinstallers.OS;
import io.github.pulsebeat02.emcinstallers.json.GsonProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class ResourceUtils {

  private ResourceUtils() {}

  public static InputStreamReader getResourceAsStream(final String resource) {
    return new InputStreamReader(requireNonNull(ResourceUtils.class.getResourceAsStream(resource)));
  }

  public static Table<OS, Boolean, String> parseTable(final String resource) {
    final Gson gson = GsonProvider.getGson();
    try (final Reader reader = getResourceAsStream(resource)) {
      final TypeToken<Map<OS, Map<Boolean, String>>> token = new TypeToken<>() {};
      final Map<OS, Map<Boolean, String>> map = gson.fromJson(reader, token.getType());
      return getHashTable(map);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Table<OS, Boolean, String> getHashTable(final Map<OS, Map<Boolean, String>> map) {
    final Table<OS, Boolean, String> table = HashBasedTable.create();
    map.forEach((os, map1) -> map1.forEach((bits, path) -> table.put(os, bits, path)));
    return table;
  }
}
