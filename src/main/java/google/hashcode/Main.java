package google.hashcode;

import google.hashcode.model.enums.DataSet;
import google.hashcode.service.LibraryService;
import google.hashcode.service.impl.LibraryServiceImpl;

public class Main {
  public static void main(String[] args) {
    LibraryService libraryService = new LibraryServiceImpl();

//    libraryService.manageLibrariesToScan(DataSet.A);
//    libraryService.manageLibrariesToScan(DataSet.B);
//    libraryService.manageLibrariesToScan(DataSet.C);
    libraryService.manageLibrariesToScan(DataSet.D);
//    libraryService.manageLibrariesToScan(DataSet.E);
//    libraryService.manageLibrariesToScan(DataSet.F);
  }
}
