package google.hashcode.service;

import java.util.List;
import java.util.Set;

public interface FileService {
  List<String> readFile(String filePath);

  void writeFile(String filePath, List<String> lines);
}
