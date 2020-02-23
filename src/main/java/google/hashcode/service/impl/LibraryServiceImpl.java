package google.hashcode.service.impl;

import google.hashcode.constant.FilePath;
import google.hashcode.model.InputInfo;
import google.hashcode.model.Library;
import google.hashcode.model.enums.DataSet;
import google.hashcode.service.FileService;
import google.hashcode.service.LibraryService;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class LibraryServiceImpl implements LibraryService {
  private static String INPUT_FILE_PATH;
  private static String OUTPUT_FILE_PATH;

  private InputInfo inputInfo = new InputInfo();
  private Integer totalNumberOfDays;
  private Map<Library, Double> libsEfficiencyMap = new HashMap<>();
  private List<Library> usedLibs = new ArrayList<>();
  private List<Library> remainingLibs = new ArrayList<>();
  private Set<Integer> usedBooks = new HashSet<>();
  private double totalUsedTime = 0;

  private FileService fileService = new FileServiceImpl();

  private Map<Integer, Integer> booksMap;

  @Override
  public void manageLibrariesToScan(DataSet dataSet) {
    setInputOutputPath(dataSet);
    List<String> inputLines = fileService.readFile(INPUT_FILE_PATH);

    initData(inputLines);
    sortBooksInLibs(remainingLibs);

    while (totalUsedTime < totalNumberOfDays && !remainingLibs.isEmpty()) {
      calculateEfficiency(remainingLibs);
      Library theBestLib = remainingLibs.get(0);
      usedLibs.add(theBestLib);
      remainingLibs.remove(theBestLib);
      usedBooks.addAll(theBestLib.getBooks());
      totalUsedTime = totalUsedTime + theBestLib.getSignUpTime();
      deleteAllUsedBooks();
    }

    List<String> outputLines = new ArrayList<>();
    outputLines.add(String.valueOf(usedLibs.size()));

    for (Library lib : usedLibs) {
      outputLines.add(lib.getId() + " " + lib.getBooks().size());
      outputLines.add(
          lib.getBooks().stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

    fileService.writeFile(OUTPUT_FILE_PATH, outputLines);
  }

  private void deleteAllUsedBooks() {
    for (Library lib : remainingLibs) {
      lib.getBooks().removeAll(usedBooks);
    }
  }

  private void sortBooksInLibs(List<Library> libs) {
    for (Library lib : libs) {
      lib.getBooks().sort((a, b) -> Integer.compare(booksMap.get(b), booksMap.get(a)));
    }
  }

  private void calculateEfficiency(List<Library> libs) {
    double unusedDays = totalNumberOfDays - totalUsedTime;
    for (Library lib : libs) {
      int scoreSum = 0;

      double workingDays = Math.ceil((double) lib.getBooks().size() / lib.getScanPerDay());
      double scanAndSignUpTime = workingDays + lib.getSignUpTime();
      double canWorkDays;

      if ((unusedDays - scanAndSignUpTime) > 0) {
        canWorkDays = workingDays;
      } else {
        canWorkDays = scanAndSignUpTime + (unusedDays - scanAndSignUpTime);
      }

      double booksCanTake = canWorkDays * lib.getScanPerDay();

      double booksTaken = 0;
      for (Integer bookId : lib.getBooks()) {
        if (booksTaken < booksCanTake) {
          scoreSum += inputInfo.getBooksMap().get(bookId);
          booksTaken++;
        }
      }

      double efficiency = (double) scoreSum / lib.getSignUpTime();
      libsEfficiencyMap.put(lib, efficiency);
    }

    libs.sort((a, b) -> Double.compare(libsEfficiencyMap.get(b), libsEfficiencyMap.get(a)));
  }

  private void initData(List<String> lines) {
    String[] firsRow = lines.get(0).split(" ");
    String[] secondRow = lines.get(1).split(" ");

    int totalNumberOfBooksInAllLibs = parseInt(firsRow[0]);
    int totalNumberOfLibs = parseInt(firsRow[1]);
    int totalNumberOfDays = parseInt(firsRow[2]);

    inputInfo.setBooksMap(new HashMap<>(totalNumberOfBooksInAllLibs));
    inputInfo.setLibraries(new ArrayList<>(totalNumberOfLibs));
    inputInfo.setTotalNumberOfDays(totalNumberOfDays);

    booksMap = inputInfo.getBooksMap();
    this.totalNumberOfDays = inputInfo.getTotalNumberOfDays();

    for (int i = 0; i < secondRow.length; i++) {
      int score = parseInt(secondRow[i]);
      booksMap.put(i, score);
    }

    for (int i = 2; i < lines.size(); i = i + 2) {
      String[] libDescription = lines.get(i).split(" ");
      String[] libBooksId = lines.get(i + 1).split(" ");
      List<Integer> booksIdInt =
          Arrays.stream(libBooksId).map(Integer::valueOf).collect(Collectors.toList());

      Library lib =
          Library.builder()
              .id((i - 2) / 2)
              .signUpTime(parseInt(libDescription[1]))
              .scanPerDay(parseInt(libDescription[2]))
              .books(booksIdInt)
              .build();
      remainingLibs.add(lib);
    }
  }

  private void setInputOutputPath(DataSet dataSet) {
    switch (dataSet) {
      case A:
        INPUT_FILE_PATH = FilePath.A_INPUT;
        OUTPUT_FILE_PATH = FilePath.A_OUTPUT;
        break;
      case B:
        INPUT_FILE_PATH = FilePath.B_INPUT;
        OUTPUT_FILE_PATH = FilePath.B_OUTPUT;
        break;
      case C:
        INPUT_FILE_PATH = FilePath.C_INPUT;
        OUTPUT_FILE_PATH = FilePath.C_OUTPUT;
        break;
      case D:
        INPUT_FILE_PATH = FilePath.D_INPUT;
        OUTPUT_FILE_PATH = FilePath.D_OUTPUT;
        break;
      case E:
        INPUT_FILE_PATH = FilePath.E_INPUT;
        OUTPUT_FILE_PATH = FilePath.E_OUTPUT;
        break;
      case F:
        INPUT_FILE_PATH = FilePath.F_INPUT;
        OUTPUT_FILE_PATH = FilePath.F_OUTPUT;
        break;
    }
  }
}
