package google.hashcode.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class InputInfo {
  private Map<Integer, Integer> booksMap;
  private List<Library> libraries;
  private int totalNumberOfDays;
}
