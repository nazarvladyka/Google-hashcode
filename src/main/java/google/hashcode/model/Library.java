package google.hashcode.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Library {
  private int id;
  private int signUpTime;
  private int scanPerDay;
  private List<Integer> books;
}
