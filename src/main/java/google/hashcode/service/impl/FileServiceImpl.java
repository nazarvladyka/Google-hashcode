package google.hashcode.service.impl;

import google.hashcode.service.FileService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileServiceImpl implements FileService {
  @Override
  public List<String> readFile(String filePath) {
    List<String> lines = new ArrayList<>();
    String line;

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return lines;
  }

  @Override
  public void writeFile(String filePath, List<String> lines) {
    try (BufferedWriter writer = new BufferedWriter((new FileWriter(filePath)))) {
      for (String line : lines) {
        writer.write(line + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
