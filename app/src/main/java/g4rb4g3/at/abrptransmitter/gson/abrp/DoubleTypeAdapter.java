package g4rb4g3.at.abrptransmitter.gson.abrp;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * ABRP Api seems to return null sometimes for double values, so we need to overwrite original double handling so it does not throw an error
 */
public class DoubleTypeAdapter extends TypeAdapter<Double> {
  @Override
  public void write(JsonWriter out, Double value) throws IOException {
    out.value(value);
  }

  @Override
  public Double read(JsonReader in) throws IOException {
    JsonToken jsonToken = in.peek();
    switch (jsonToken) {
      case NUMBER:
      case STRING:
        String s = in.nextString();
        try {
          return Double.parseDouble(s);
        } catch (NumberFormatException ignored) {
        }
        return null;
      case NULL:
        in.nextNull();
        return null;
      default:
        throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
    }
  }
}
