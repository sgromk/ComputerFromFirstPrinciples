package n2t;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the standard VM mapping for the Hack computer.
 */
public class Mapping {
  Mapping() {}

  private static final Map<String, Integer> vmMap = new HashMap<String, Integer>() {
    {
      put("SP", 0);
      put("LCL", 1);
      put("ARG", 2);
      put("THIS", 3);
      put("THAT", 4);
    }
  };
}
