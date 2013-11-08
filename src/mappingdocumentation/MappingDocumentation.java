
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mappingdocumentation;

//~--- JDK imports ------------------------------------------------------------
/**
 * Импорт библиотек для работы с файлами
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author dmitryrassakhatsky
 */
public class MappingDocumentation {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String fileCatalog, fileResultCatalog;
        MappingDocumentation mappingDocumentation;

        fileCatalog = "/Users/rassakhatsky/Desktop/Data.xml";
        fileResultCatalog = "/Users/rassakhatsky/Desktop/out.csv";
        mappingDocumentation = new MappingDocumentation();
        mappingDocumentation.execute(fileCatalog, fileResultCatalog);
    }

    /**
     * Процесс обработки файла
     *
     * @param fileCatalog - полный путь к файлу, из которого нужно считать
     * данные данные
     * @param fileResultCatalog - полный путь к файлу, в который необходимо
     * записать данные
     *
     * @throws IOException
     */
    public void execute(String fileCatalog, String fileResultCatalog) throws IOException {
        FileInputStream fis;
        StringBuilder preSource;
        Table table;
        int index;    // Технический элемент, использующий для преобразования байтового массива в String

        /**
         * Определяем входные параметры:
         * 1) Каталог
         * 2) Входящий файл
         * 3) Исходящий файл
         */
        fis = new FileInputStream(fileCatalog);
        preSource = new StringBuilder("");

        /**
         * Передача байтового массива в String
         */
        while ((index = fis.read()) != -1) {
            preSource.append((char) index);
        }
        fis.close();
        
        table = new Table();
        table.createTable(preSource.toString(), fileResultCatalog);
    }
}
